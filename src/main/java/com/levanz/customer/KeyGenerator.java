package com.levanz.customer;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.io.Encoders;


public class KeyGenerator {

    public static void main(String[] args) {
        String base64 = Encoders.BASE64.encode(
                Keys.secretKeyFor(SignatureAlgorithm.HS512).getEncoded());

        System.out.println("\nYour new HS512 key (Base-64):\n\n" + base64 + "\n");
    }

    private KeyGenerator() { /* no-instantiation */ }
}
