package com.levanz.customer.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
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

import static org.hamcrest.Matchers.contains;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CustomerBatchUpdateIntegrationTest {

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

    private Long createCustomer(String first, String last, String email) throws Exception {
        String body = objectMapper.writeValueAsString(
            Map.of("firstName", first, "lastName", last, "email", email)
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

    @Test
    void batchUpdateTwoCustomers() throws Exception {
        authenticate();
        Long c1 = createCustomer("Tom","A","tom@a.com");
        Long c2 = createCustomer("Jerry","B","jerry@b.com");

        List<Map<String,Object>> updates = List.of(
          Map.of("id", c1, "firstName","X","lastName","Y","email","x@y.com"),
          Map.of("id", c2, "firstName","A","lastName","B","email","a@b.com")
        );

        mvc.perform(put("/api/customers/batch")
                .header("Authorization","Bearer "+jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updates)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[?(@.id == " + c1 + ")].firstName", contains("X")))
            .andExpect(jsonPath("$[?(@.id == " + c2 + ")].email", contains("a@b.com")));
    }
}
