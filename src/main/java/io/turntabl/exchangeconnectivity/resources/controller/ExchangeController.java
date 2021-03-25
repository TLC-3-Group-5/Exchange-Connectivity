package io.turntabl.exchangeconnectivity.resources.controller;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.gson.JsonObject;
import io.turntabl.exchangeconnectivity.resources.model.Trade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${app.exchange_api_key}")
    private String exchangeApiKey;

    @PostMapping(path = "create-order")
    public String createOrders(@RequestBody Trade trade) throws JsonProcessingException {
        HttpEntity<Trade> trades = new HttpEntity<>(trade);
        String exchangeUrl = "https://" + trade.getExchange() + ".matraining.com/" + exchangeApiKey + "/order";
        // String exchangeUrl = "https://"+ "exchange2" + ".matraining.com/" +
        // exchangeApiKey + "/order";
        ResponseEntity<String> orderResponse = this.restTemplate.postForEntity(exchangeUrl, trades, String.class);
        JsonNode root = objectMapper.readTree(orderResponse.getBody());
        return root.toString();
    }

    @GetMapping(path = "get-order-status/{exchangeId}/{exchange}")
    public String getOrderStatus(@PathVariable("exchangeId") String exchangeId,
            @PathVariable("exchange") String exchange) throws JsonProcessingException {
        String exchangeUrl = "https://" + exchange + ".matraining.com/" + exchangeApiKey + "/order/" + exchangeId;
        String result = restTemplate.getForObject(exchangeUrl, String.class);
        
        JsonNode root = objectMapper.readTree(result);

        return root.toString();
    }

    @DeleteMapping(path = "cancel-order/{exchangeId}/{exchange}")
    public void cancelOrder(@PathVariable String exchangeId, @PathVariable String exchange) {
        String exchangeUrl = "https://" + exchange + ".matraining.com/" + exchangeApiKey + "/order/" + exchangeId;
        restTemplate.delete(exchangeUrl);
    }
}
