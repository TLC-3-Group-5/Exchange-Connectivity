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

//    @Bean
//    public RestTemplate restTemplate() {
//        return new RestTemplate();
//    }

    @PostMapping(path="create-order")
    public String createOrders(@RequestBody Trade trade) throws JsonProcessingException {
        HttpEntity<Trade> trades = new HttpEntity<>(trade);
         String exchangeUrl = "https://"+ trade.getExchange() + ".matraining.com/" + env.getProperty("api_key") + "/order";
//         String exchangeUrl = "https://"+ "exchange2" + ".matraining.com/" + env.getProperty("api_key") + "/order";
         ResponseEntity<String> orderResponse = this.restTemplate
                    .postForEntity(exchangeUrl,trades, String.class);
         JsonNode root = objectMapper.readTree(orderResponse.getBody());
         return root.toString();
    }

    @GetMapping(path="get-order-status/{{exchangeId}}/{{exchange}}")
    public String getOrderStatus(@PathVariable String exchangeId, @PathVariable String exchange) throws JsonProcessingException {
        String exchangeUrl = "https://" + exchange +".matraining.com/" + env.getProperty("api_key") + "/order/"+exchangeId;
        return restTemplate.getForObject(exchangeUrl, String.class);
    }

    @DeleteMapping(path="cancel-order/{{exchangeId}}/{{exchange}}")
    public void cancelOrder(@PathVariable String exchangeId, @PathVariable String exchange){
        String exchangeUrl = "https://" + exchange + ".matraining.com/" + env.getProperty("api_key") + "/order/"+exchangeId;
        restTemplate.delete(exchangeUrl);
    }
}
