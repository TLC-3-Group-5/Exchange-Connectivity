package io.turntabl.exchangeconnectivity.resources.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.turntabl.exchangeconnectivity.resources.model.Response;
import io.turntabl.exchangeconnectivity.resources.model.Trade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Class to receive order
@Component
public class ReceiveOrder {

    @Autowired
    RestTemplate restTemplate;

    public ReceiveOrder(){

    }
    ObjectMapper objectMapper = new ObjectMapper();
    // Method to keep checking queue
    public void receiveOrder() throws JsonProcessingException {
        Jedis jedis = new Jedis("localhost", 6379);
        while(true)
        {
            List<String> output = jedis.blpop(0, "exchange1");
            Trade request = objectMapper.readValue(output.get(1), Trade.class);
            System.out.println(output.get(1));
            System.out.println(request);
            ResponseEntity<String> exchangeId =
                    restTemplate.postForEntity("http://localhost:8084/create-order", request, String.class);
            System.out.println(exchangeId.getBody());
            Map<String, String> variables = new HashMap<>();
            variables.put("tradeId", String.valueOf(request.getId()));
            variables.put("exchangeId",exchangeId.getBody().replaceAll("\"", ""));
            restTemplate.put("http://localhost:8083/trade/{tradeId}/{exchangeId}", Trade.class , variables);
        }
    }
}
