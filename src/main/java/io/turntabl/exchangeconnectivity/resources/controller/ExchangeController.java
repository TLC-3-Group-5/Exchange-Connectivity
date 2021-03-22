package io.turntabl.exchangeconnectivity.resources.controller;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.gson.JsonObject;
import io.turntabl.exchangeconnectivity.resources.model.Trade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
public class ExchangeController {

    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private Environment env;

    private final String EXCHANGE_API_KEY;

    public ExchangeController() {
        EXCHANGE_API_KEY = env.getProperty("app.exchange_api_key");
    }

    @PostMapping(path = "create-order")
    public String createOrders(@RequestBody Trade trade) throws JsonProcessingException {
        HttpEntity<Trade> trades = new HttpEntity<>(trade);

        String exchange1 = "https://exchange.matraining.com/" + EXCHANGE_API_KEY + "/order";
        String exchange2 = "https://exchange2.matraining.com/" + EXCHANGE_API_KEY + "/order";

        ResponseEntity<String> orderResponse = this.restTemplate.postForEntity(exchange1, trades, String.class);

        JsonNode root = objectMapper.readTree(orderResponse.getBody());

        return root.toString();
    }

    @GetMapping(path = "get-order-status")
    public String getOrderStatus() throws JsonProcessingException {
        String orderId = "479db8ee-d5b1-45be-a54e-af9986312a85";
        String exchange1 = "https://exchange.matraining.com/" + EXCHANGE_API_KEY + "/order/" + orderId;
        String exchange2 = "https://exchange2.matraining.com/" + EXCHANGE_API_KEY + "/order";
        return restTemplate.getForObject(exchange1, String.class);
    }

    @DeleteMapping(path = "cancel-order")
    public void cancelOrder() {
        String orderId = "479db8ee-d5b1-45be-a54e-af9986312a85";
        String exchange1 = "https://exchange.matraining.com/" + EXCHANGE_API_KEY + "/order/" + orderId;
        restTemplate.delete(exchange1);
    }
}
