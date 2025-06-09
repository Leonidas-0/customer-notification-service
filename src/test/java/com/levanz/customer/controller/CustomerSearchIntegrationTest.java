package com.levanz.customer.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.levanz.customer.dto.CustomerSearchCriteriaDto;
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

import static org.hamcrest.Matchers.is;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
public class CustomerSearchIntegrationTest {

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
            Map.of("username", "admin", "password", "admin123")
        );
        String resp = mvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(creds))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        Map<String, String> m = objectMapper.readValue(
            resp, new TypeReference<>() {});
        this.jwt = m.get("token");
    }

    private Long createCustomer(String firstName, String lastName, String email) throws Exception {
        String body = objectMapper.writeValueAsString(
            Map.of("firstName", firstName,
                   "lastName", lastName,
                   "email", email)
        );
        String resp = mvc.perform(post("/api/customers")
                .header("Authorization", "Bearer " + jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

        Map<String, Object> m = objectMapper.readValue(
            resp, new TypeReference<>() {});
        return ((Number) m.get("id")).longValue();
    }

    private void createPreference(Long customerId, boolean optedIn) throws Exception {
        String body = objectMapper.writeValueAsString(
            Map.of("channel", "EMAIL", "optedIn", optedIn)
        );
        mvc.perform(post("/api/customers/{cid}/preferences", customerId)
                .header("Authorization", "Bearer " + jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isCreated());
    }

    @Test
    void searchByNameAndOptIn() throws Exception {
        // 1) login
        authenticate();

        // 2) create two customers
        Long aliceId = createCustomer("Alice", "Smith", "alice@example.com");
        createPreference(aliceId, true);

        Long bobId = createCustomer("Bob", "Jones", "bob@example.com");
        createPreference(bobId, false);

        CustomerSearchCriteriaDto crit = new CustomerSearchCriteriaDto();
        crit.setFirstName("Ali");
        crit.setOptedIn(true);

        mvc.perform(post("/api/customers/search")
                .header("Authorization", "Bearer " + jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(crit)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content.length()", is(1)))
            .andExpect(jsonPath("$.content[0].firstName", is("Alice")));
    }
}
