package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/url")
public class UrlController {

    private HashMap<String, String> map = new HashMap<>();

    @PostMapping("/shorten")
    public ResponseEntity<String> shortenUrl(@RequestBody UrlRequest originalUrl) {
        // Generate a unique short URL (you can use libraries like Apache Commons Codec for this)
        String shortUrl = generateShortUrl();

        // Save the URL mapping in the database

        map.put(shortUrl, originalUrl.getOriginalURL());
        // Return the shortened URL
        return ResponseEntity.ok(shortUrl);
    }

    @GetMapping("/{shortUrl}")
    public ResponseEntity<Object> redirectToOriginalUrl(@PathVariable String shortUrl) {
        String url = map.get(shortUrl);

        if (url != null) {
            return ResponseEntity.status(HttpStatus.FOUND)
                    .header("Location", url)
                    .build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private String generateShortUrl() {
        // Implement your logic to generate a unique short URL
        // This can involve encoding the ID or generating a random string
        // Return a unique short URL
        UUID uuid = UUID.randomUUID();

        // Convert the UUID to a string
        String uuidString = uuid.toString();
        return uuidString;
    }
}