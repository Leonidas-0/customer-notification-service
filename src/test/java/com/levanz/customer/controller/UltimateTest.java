package com.levanz.customer.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.levanz.customer.dto.CustomerSearchCriteriaDto;
import com.levanz.customer.dto.CustomerUpdateDto;
import com.levanz.customer.entity.NotificationStatus;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.security.Key;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_CLASS;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DirtiesContext(classMode = AFTER_CLASS)
public class UltimateTest {

    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper json;

    private String jwt;

    @DynamicPropertySource
    static void jwtSecret(DynamicPropertyRegistry reg) {
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        reg.add("app.jwtSecret", () -> Encoders.BASE64.encode(key.getEncoded()));
    }

    @BeforeEach
    void init() throws Exception {
        this.jwt = obtainJwt();
    }

    @Test
    void fullApplicationFlow() throws Exception {
        Long custId = createCustomer(jwt);
        Long addrId = createAddress(jwt, custId, "Tbilisi", "Rustaveli Ave", "0101");
        Long prefId = createPreference(jwt, custId, "EMAIL", true);
        Long notifId = createNotification(jwt, custId, NotificationStatus.PENDING);

        CustomerSearchCriteriaDto crit = new CustomerSearchCriteriaDto();
        crit.setFirstName("End");
        crit.setOptedIn(true);

        mvc.perform(post("/api/customers/search")
                .header("Authorization", "Bearer " + jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.writeValueAsString(crit)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content.length()").value(1))
            .andExpect(jsonPath("$.content[0].id").value(custId.intValue()));

        CustomerUpdateDto upd = new CustomerUpdateDto();
        upd.setId(custId);
        upd.setFirstName("King");
        upd.setLastName("OfE2E");
        upd.setEmail("e2e@done.com");

        mvc.perform(put("/api/customers/batch")
                .header("Authorization", "Bearer " + jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.writeValueAsString(List.of(upd))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].firstName").value("King"));

        mvc.perform(get("/api/reports/preferences")
                .header("Authorization", "Bearer " + jwt))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].channel").value("EMAIL"));

        mvc.perform(get("/api/reports/notifications")
                .header("Authorization", "Bearer " + jwt))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].status").value("PENDING"));

        String custJson = mvc.perform(get("/api/customers/{id}", custId)
                .header("Authorization", "Bearer " + jwt))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();

        Map<String, Object> custMap = json.readValue(custJson, new TypeReference<>() {});
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> addrs = (List<Map<String, Object>>) custMap.get("addresses");
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> prefs = (List<Map<String, Object>>) custMap.get("preferences");
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> notifs = (List<Map<String, Object>>) custMap.get("notifications");

        assertThat(addrs).hasSize(1).extracting(m -> m.get("id")).contains(addrId.intValue());
        assertThat(prefs).hasSize(1).extracting(m -> m.get("id")).contains(prefId.intValue());
        assertThat(notifs).hasSize(1).extracting(m -> m.get("id")).contains(notifId.intValue());

        mvc.perform(delete("/api/customers/{id}", custId)
                .header("Authorization", "Bearer " + jwt))
            .andExpect(status().isNoContent());

        mvc.perform(get("/api/customers/{id}", custId)
                .header("Authorization", "Bearer " + jwt))
            .andExpect(status().isNotFound());

        mvc.perform(get("/api/customers/{cid}/addresses", custId)
                .header("Authorization", "Bearer " + jwt))
            .andExpect(status().isNotFound());
        mvc.perform(get("/api/customers/{cid}/preferences", custId)
                .header("Authorization", "Bearer " + jwt))
            .andExpect(status().isNotFound());
        mvc.perform(get("/api/customers/{cid}/notifications", custId)
                .header("Authorization", "Bearer " + jwt))
            .andExpect(status().isNotFound());
    }

    private String obtainJwt() throws Exception {
        String creds = json.writeValueAsString(Map.of("username", "admin", "password", "admin123"));
        String resp = mvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(creds))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").isString())
            .andReturn().getResponse().getContentAsString();

        Map<String, String> map = json.readValue(resp, new TypeReference<>() {});
        return map.get("token");
    }

    private Long createCustomer(String jwt) throws Exception {
        String body = json.writeValueAsString(
            Map.of("firstName", "End", "lastName", "ToEnd", "email", "e2e@example.com")
        );
        String c = mvc.perform(post("/api/customers")
                .header("Authorization", "Bearer " + jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isCreated())
            .andReturn().getResponse().getContentAsString();
        return ((Number) json.readValue(c, new TypeReference<Map<String,Object>>(){}).get("id")).longValue();
    }

    private Long createAddress(String jwt, Long custId, String city, String street, String zip) throws Exception {
        String body = json.writeValueAsString(
            Map.of("type","POSTAL","country","Georgia","city",city,"street",street,"zip",zip)
        );
        String r = mvc.perform(post("/api/customers/{cid}/addresses", custId)
                .header("Authorization","Bearer "+jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isCreated())
            .andReturn().getResponse().getContentAsString();
        return ((Number)json.readValue(r,new TypeReference<Map<String,Object>>(){}).get("id")).longValue();
    }

    private Long createPreference(String jwt, Long custId, String channel, boolean optIn) throws Exception {
        String body = json.writeValueAsString(Map.of("channel",channel,"optedIn",optIn));
        String r = mvc.perform(post("/api/customers/{cid}/preferences", custId)
                .header("Authorization","Bearer "+jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isCreated())
            .andReturn().getResponse().getContentAsString();
        return ((Number)json.readValue(r,new TypeReference<Map<String,Object>>(){}).get("id")).longValue();
    }

    private Long createNotification(String jwt, Long custId, NotificationStatus status) throws Exception {
        String body = json.writeValueAsString(Map.of("status", status));
        String r = mvc.perform(post("/api/customers/{cid}/notifications", custId)
                .header("Authorization","Bearer "+jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isCreated())
            .andReturn().getResponse().getContentAsString();
        return ((Number)json.readValue(r,new TypeReference<Map<String,Object>>(){}).get("id")).longValue();
    }
}
