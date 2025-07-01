package com.haynesgt.agentic.server;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {

    private static final Logger log = LoggerFactory.getLogger(RootController.class);
    @Value("${telegram.bot.token}")
    String token;

    @Value("${server.port}")
    String server_port;

    @GetMapping("/")
    public String index() {
        log.info("RootController");
        return "Hello World";
    }

    /**
    @PostMapping("/telegram-webhook")
    public String telegramWebhook() {
        log.info("telegram-webhook");
        return "Hello";
    }
    /**/

    @GetMapping("/install")
    public String install() throws IOException {
        String webhookUrl = "https://gh1-30000.haynesgt.com/telegram-webhook";
        URI uri = URI.create("https://api.telegram.org/bot" + token + "/setWebhook");

        HttpURLConnection conn = (HttpURLConnection) uri.toURL().openConnection();

        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        String body = "url=" + webhookUrl;
        try (OutputStream os = conn.getOutputStream()) {
            os.write(body.getBytes(StandardCharsets.UTF_8));
        }

        int responseCode = conn.getResponseCode();
        System.out.println("Response Code: " + responseCode);

        InputStream responseStream = (responseCode >= 200 && responseCode < 300)
                ? conn.getInputStream()
                : conn.getErrorStream();

        try (BufferedReader in = new BufferedReader(new InputStreamReader(responseStream))) {
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            System.out.println("Response Body: " + response);
            return response.toString();
        }
    }
}
