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
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class PreferenceControllerIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @DynamicPropertySource
    static void jwtSecret(DynamicPropertyRegistry reg) {
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        String b64 = Encoders.BASE64.encode(key.getEncoded());
        reg.add("app.jwtSecret", () -> b64);
    }

    private String token() throws Exception {
        String creds = objectMapper.writeValueAsString(
            Map.of("username","admin","password","admin123")
        );
        String resp = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(creds))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();
        return objectMapper.readValue(resp, new TypeReference<Map<String,String>>(){}).get("token");
    }

    private Long createCustomer(String jwt) throws Exception {
        String cust = objectMapper.writeValueAsString(
            Map.of("firstName","Bob","lastName","Lee","email","bob@example.com")
        );
        String resp = mockMvc.perform(post("/api/customers")
                .header("Authorization","Bearer "+jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(cust))
            .andExpect(status().isCreated())
            .andReturn().getResponse().getContentAsString();
        return ((Number)objectMapper.readValue(resp, new TypeReference<Map<String,Object>>(){}).get("id")).longValue();
    }

    @Test
    void fullPreferenceCrud_withAuth() throws Exception {
        String jwt = token();
        Long cid = createCustomer(jwt);

        Map<String,Object> req = Map.of("channel","EMAIL","optedIn",true);
        String json = objectMapper.writeValueAsString(req);
        String create = mockMvc.perform(post("/api/customers/{cid}/preferences",cid)
                .header("Authorization","Bearer "+jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.channel").value("EMAIL"))
            .andReturn().getResponse().getContentAsString();

        Long pid = ((Number)objectMapper.readValue(create, new TypeReference<Map<String,Object>>(){}).get("id")).longValue();

        mockMvc.perform(get("/api/customers/{cid}/preferences",cid)
                .header("Authorization","Bearer "+jwt))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].optedIn").value(true));

        String upd = objectMapper.writeValueAsString(Map.of("channel","EMAIL","optedIn",false));
        mockMvc.perform(put("/api/customers/{cid}/preferences/{pid}",cid,pid)
                .header("Authorization","Bearer "+jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(upd))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.optedIn").value(false));

        mockMvc.perform(delete("/api/customers/{cid}/preferences/{pid}",cid,pid)
                .header("Authorization","Bearer "+jwt))
            .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/customers/{cid}/preferences",cid)
                .header("Authorization","Bearer "+jwt))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isEmpty());
    }
}
