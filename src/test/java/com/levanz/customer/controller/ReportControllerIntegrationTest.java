package com.levanz.customer.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.levanz.customer.entity.NotificationStatus;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;


import java.security.Key;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ReportControllerIntegrationTest {

    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper objectMapper;
    private String jwt;

    @DynamicPropertySource
    static void registerJwtSecret(DynamicPropertyRegistry reg) {
        Key k = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        reg.add("app.jwtSecret", () -> Encoders.BASE64.encode(k.getEncoded()));
    }

    private void authenticate() throws Exception {
        String creds = objectMapper.writeValueAsString(
            Map.of("username","admin","password","admin123")
        );
        String resp = mvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(creds))
            .andReturn().getResponse().getContentAsString();
        Map<String,String> m = objectMapper.readValue(
            resp, new TypeReference<>() {});
        this.jwt = m.get("token");
    }

    private Long createCustomer() throws Exception {
        String body = objectMapper.writeValueAsString(
            Map.of("firstName","Sam","lastName","Stats","email","sam@stats.com")
        );
        String resp = mvc.perform(post("/api/customers")
                .header("Authorization","Bearer "+jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isCreated())
            .andReturn().getResponse().getContentAsString();
        Map<String,Object> m = objectMapper.readValue(
            resp, new TypeReference<>() {});
        return ((Number)m.get("id")).longValue();
    }

    private void createPreference(Long cid, String channel, boolean optIn) throws Exception {
        String body = objectMapper.writeValueAsString(Map.of("channel",channel,"optedIn",optIn));
        mvc.perform(post("/api/customers/{cid}/preferences", cid)
                .header("Authorization","Bearer "+jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
          .andExpect(status().isCreated());
    }

    private Long createNotification(Long cid, NotificationStatus status) throws Exception {
        String body = objectMapper.writeValueAsString(Map.of("status", status));
        String resp = mvc.perform(post("/api/customers/{cid}/notifications", cid)
                .header("Authorization","Bearer "+jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isCreated())
            .andReturn().getResponse().getContentAsString();
        Map<String,Object> m = objectMapper.readValue(
            resp, new TypeReference<>() {});
        return ((Number)m.get("id")).longValue();
    }

    @Test
    void preferenceAndNotificationStats() throws Exception {
        authenticate();
        Long cid = createCustomer();
        createPreference(cid, "EMAIL", true);
        createPreference(cid, "SMS", false);
        createNotification(cid, NotificationStatus.PENDING);
        createNotification(cid, NotificationStatus.SENT);

        String prefsJson = mvc.perform(get("/api/reports/preferences")
                .header("Authorization","Bearer "+jwt))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();
        List<Map<String,Object>> prefs = objectMapper.readValue(
            prefsJson, new TypeReference<>() {});
        assertThat(prefs).anyMatch(m -> m.get("channel").equals("EMAIL"));

        String notifsJson = mvc.perform(get("/api/reports/notifications")
                .header("Authorization","Bearer "+jwt))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();
        List<Map<String,Object>> notifs = objectMapper.readValue(
            notifsJson, new TypeReference<>() {});
        assertThat(notifs).anyMatch(m -> m.get("status").equals("PENDING"));
    }
}
