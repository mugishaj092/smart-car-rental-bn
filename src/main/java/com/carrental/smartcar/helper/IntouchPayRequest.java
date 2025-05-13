package com.carrental.smartcar.helper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class IntouchPayRequest {

    public static Map<String, Object> requestPayment(String username, String password, String phoneNumber, double amount, String callbackUrl) throws IOException, IOException, InterruptedException {
        String requestTransactionId = UUID.randomUUID().toString().replace("-", "").substring(0, 16);
        Map<String, Object> payload = new HashMap<>();
        payload.put("username", username);
        payload.put("password", password);
        payload.put("timestamp", "20161231115242");
        payload.put("mobilephone", phoneNumber);
        payload.put("amount", amount);
        payload.put("requesttransactionid", requestTransactionId);
        payload.put("callbackurl", callbackUrl);

        ObjectMapper mapper = new ObjectMapper();
        String jsonPayload = mapper.writeValueAsString(payload);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://www.intouchpay.co.rw/api/requestpayment/"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return mapper.readValue(response.body(), new TypeReference<>() {});
    }
}
