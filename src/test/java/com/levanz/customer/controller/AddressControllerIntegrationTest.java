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
class AddressControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @DynamicPropertySource
    static void registerJwtSecret(DynamicPropertyRegistry registry) {
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        String base64Key = Encoders.BASE64.encode(key.getEncoded());
        registry.add("app.jwtSecret", () -> base64Key);
    }

    private String obtainJwt() throws Exception {
        Map<String,String> creds = Map.of(
            "username", "admin",
            "password", "admin123"
        );
        String body = objectMapper.writeValueAsString(creds);

        String resp = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").isString())
            .andReturn()
            .getResponse()
            .getContentAsString();

        Map<String,String> map = objectMapper.readValue(
            resp,
            new TypeReference<Map<String,String>>() {}
        );
        return map.get("token");
    }

    private Long createCustomer(String jwt) throws Exception {
        Map<String,String> cust = Map.of(
            "firstName","Alice","lastName","Smith","email","alice@example.com"
        );
        String json = objectMapper.writeValueAsString(cust);

        String resp = mockMvc.perform(post("/api/customers")
                .header("Authorization","Bearer "+jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

        Map<String,Object> created = objectMapper.readValue(
            resp,
            new TypeReference<Map<String,Object>>() {}
        );
        return ((Number)created.get("id")).longValue();
    }

    @Test
    void fullAddressCrudLifecycle_withAuth() throws Exception {
        String jwt = obtainJwt();
        Long customerId = createCustomer(jwt);

        Map<String,Object> req = Map.of(
          "type","POSTAL",
          "country","Georgia",
          "city","Tbilisi",
          "street","Rustaveli Ave",
          "zip","0101"
        );
        String reqJson = objectMapper.writeValueAsString(req);

        String createResp = mockMvc.perform(post("/api/customers/{cust}/addresses", customerId)
                .header("Authorization","Bearer "+jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(reqJson))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").isNumber())
            .andExpect(jsonPath("$.city").value("Tbilisi"))
            .andReturn()
            .getResponse()
            .getContentAsString();

        Map<String,Object> addr = objectMapper.readValue(
            createResp,
            new TypeReference<Map<String,Object>>() {}
        );
        Long addressId = ((Number)addr.get("id")).longValue();

        Map<String,Object> upd = Map.of(
          "type","POSTAL",
          "country","Georgia",
          "city","Batumi",
          "street","Chavchavadze St",
          "zip","6000"
        );
        String updJson = objectMapper.writeValueAsString(upd);

        mockMvc.perform(put("/api/customers/{cust}/addresses/{addr}", customerId, addressId)
                .header("Authorization","Bearer "+jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updJson))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.city").value("Batumi"));

        mockMvc.perform(delete("/api/customers/{cust}/addresses/{addr}", customerId, addressId)
                .header("Authorization","Bearer "+jwt))
            .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/customers/{cust}/addresses", customerId)
                .header("Authorization","Bearer "+jwt))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isEmpty());
    }
}
