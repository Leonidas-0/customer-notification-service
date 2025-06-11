package com.levanz.customer.ui;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import jakarta.servlet.http.HttpSession;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
public class UiController {

    private final RestTemplate rest;

    @Value("${backend.base-url:http://localhost:8080}")
    private String baseUrl;

    public UiController(RestTemplate rest) {
        this.rest = rest;
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String,String> creds = Map.of("username",username,"password",password);
        HttpEntity<Map<String,String>> req = new HttpEntity<>(creds, headers);
        Map<?,?> resp = rest.postForObject(
            baseUrl + "/api/auth/login",
            req,
            Map.class
        );
        session.setAttribute("jwt", resp.get("token"));
        return "redirect:/customers";
    }

    private HttpHeaders authHeaders(HttpSession session) {
        String token = (String) session.getAttribute("jwt");
        HttpHeaders h = new HttpHeaders();
        h.setBearerAuth(token);
        h.setContentType(MediaType.APPLICATION_JSON);
        return h;
    }

    @GetMapping("/{path:customers|addresses|preferences|notifications}")
    public String listAll(@PathVariable String path,
                          Model model,
                          HttpSession session) {

        ResponseEntity<List<LinkedHashMap<String,Object>>> resp = rest.exchange(
            baseUrl + "/api/" + path,
            HttpMethod.GET,
            new HttpEntity<>(authHeaders(session)),
            new ParameterizedTypeReference<>() {}
        );
        model.addAttribute("path", path);
        model.addAttribute("items", resp.getBody());
        return "list";
    }

    @GetMapping("/{path:customers|addresses|preferences|notifications}/new")
    public String newEntity(@PathVariable String path,
                            Model model) {
        model.addAttribute("path", path);
        model.addAttribute("item", new LinkedHashMap<String,Object>());
        return "form";
    }

    @PostMapping("/{path:customers|addresses|preferences|notifications}/save")
    public String saveEntity(@PathVariable String path,
                             @RequestParam Map<String,String> itemParams,
                             HttpSession session) {
        HttpEntity<Map<String,String>> req = new HttpEntity<>(itemParams, authHeaders(session));
        rest.postForEntity(baseUrl + "/api/" + path, req, Void.class);
        return "redirect:/" + path;
    }

    @GetMapping("/{path:customers|addresses|preferences|notifications}/edit/{id}")
    public String editEntity(@PathVariable String path,
                             @PathVariable String id,
                             Model model,
                             HttpSession session) {
        ResponseEntity<LinkedHashMap<String,Object>> resp = rest.exchange(
            baseUrl + "/api/" + path + "/" + id,
            HttpMethod.GET,
            new HttpEntity<>(authHeaders(session)),
            new ParameterizedTypeReference<>() {}
        );
        model.addAttribute("path", path);
        model.addAttribute("item", resp.getBody());
        return "form";
    }

    @GetMapping("/{path:customers|addresses|preferences|notifications}/delete/{id}")
    public String deleteEntity(@PathVariable String path,
                               @PathVariable String id,
                               HttpSession session) {
        rest.exchange(
            baseUrl + "/api/" + path + "/" + id,
            HttpMethod.DELETE,
            new HttpEntity<>(authHeaders(session)),
            Void.class
        );
        return "redirect:/" + path;
    }

    @GetMapping("/reports")
    public String reports(Model model, HttpSession session) {
        ResponseEntity<List<LinkedHashMap<String,Object>>> prefResp = rest.exchange(
            baseUrl + "/api/reports/preferences",
            HttpMethod.GET,
            new HttpEntity<>(authHeaders(session)),
            new ParameterizedTypeReference<>() {}
        );
        ResponseEntity<List<LinkedHashMap<String,Object>>> notifResp = rest.exchange(
            baseUrl + "/api/reports/notifications",
            HttpMethod.GET,
            new HttpEntity<>(authHeaders(session)),
            new ParameterizedTypeReference<>() {}
        );
        model.addAttribute("prefStats", prefResp.getBody());
        model.addAttribute("notifStats", notifResp.getBody());
        return "reports";
    }
}
