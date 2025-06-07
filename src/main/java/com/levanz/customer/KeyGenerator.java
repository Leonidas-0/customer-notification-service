package com.levanz.customer;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.io.Encoders;

/**
 * Generates a single-use HS512 signing key and prints it to STDOUT.
 *
 * Usage (from project root):
 *   mvn -q exec:java -Dexec.mainClass="com.levanz.customer.util.KeyGenerator"
 *
 * Copy the printed value into application.yml:
 *   security:
 *     jwt-secret: <PASTE_HERE>
 */
public class KeyGenerator {

    public static void main(String[] args) {
        String base64 = Encoders.BASE64.encode(
                Keys.secretKeyFor(SignatureAlgorithm.HS512).getEncoded());

        System.out.println("\nYour new HS512 key (Base-64):\n\n" + base64 + "\n");
    }

    private KeyGenerator() { /* no-instantiation */ }
}
