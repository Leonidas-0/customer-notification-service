package com.levanz.customer;

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
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.security.Key;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CustomerNotificationApplicationTests {

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

        // specify generic type explicitly to avoid unchecked conversion
        Map<String,String> map = objectMapper.readValue(
            resp,
            new TypeReference<Map<String,String>>() {}
        );
        return map.get("token");
    }

    @Test
    void fullCustomerCrudLifecycle_withAuth() throws Exception {
        String jwt = obtainJwt();

        // create
        Map<String,Object> createReq = Map.of(
          "firstName", "John",
          "lastName",  "Doe",
          "email", "john.doe@example.com"
        );
        String createJson = objectMapper.writeValueAsString(createReq);

        String createResp = mockMvc.perform(post("/api/customers")
                .header("Authorization", "Bearer " + jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(createJson))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").isNumber())
            .andExpect(jsonPath("$.firstName").value("John"))
            .andReturn()
            .getResponse()
            .getContentAsString();

        // specify generic type explicitly to avoid unchecked conversion
        Map<String,Object> created = objectMapper.readValue(
            createResp,
            new TypeReference<Map<String,Object>>() {}
        );
        Integer id = (Integer) created.get("id");

        // read one
        mockMvc.perform(get("/api/customers/{id}", id)
                .header("Authorization", "Bearer " + jwt))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.email").value("john.doe@example.com"));

        // list
        mockMvc.perform(get("/api/customers")
                .header("Authorization", "Bearer " + jwt))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content").isArray());

        // update
        Map<String,Object> updateReq = Map.of(
          "firstName", "Jane",
          "lastName",  "Doe",
          "email", "jane.doe@example.com"
        );
        String updateJson = objectMapper.writeValueAsString(updateReq);

        mockMvc.perform(put("/api/customers/{id}", id)
                .header("Authorization", "Bearer " + jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJson))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.firstName").value("Jane"))
            .andExpect(jsonPath("$.email").value("jane.doe@example.com"));

        // delete
        mockMvc.perform(delete("/api/customers/{id}", id)
                .header("Authorization", "Bearer " + jwt))
            .andExpect(status().isNoContent());

        // confirm gone
        mockMvc.perform(get("/api/customers/{id}", id)
                .header("Authorization", "Bearer " + jwt))
            .andExpect(status().isNotFound());
    }
}
