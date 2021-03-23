package io.turntabl.exchangeconnectivity.resources.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.turntabl.exchangeconnectivity.resources.model.Response;
import io.turntabl.exchangeconnectivity.resources.model.Trade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

// Class to receive order
@Component
public class ReceiveOrder {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    Environment env;

    public ReceiveOrder() {}

    ObjectMapper objectMapper = new ObjectMapper();
    // Method to keep checking queue
    public void receiveOrder() throws JsonProcessingException {
        Jedis jedis = new Jedis(
            Optional.ofNullable(env.getProperty("app.SPRING_REDIS_URL")).orElse(""),
            Optional.ofNullable(Integer.parseInt(env.getProperty("app.SPRING_REDIS_PORT"))).orElse(0)
        );

        while(true) {
            List<String> output = jedis.blpop(0, "exchange1");
            Trade request = objectMapper.readValue(output.get(1), Trade.class);
            System.out.println(output.get(1));
            System.out.println(request);
            ResponseEntity<String> exchangeId =
                    restTemplate.postForEntity(
                        Optional.ofNullable(env.getProperty("app.host_url")).orElse("")
                            .concat("/create-order"),
                        request, 
                        String.class
                    );
            System.out.println(exchangeId.getBody());
            Map<String, String> variables = new HashMap<>();
            variables.put("tradeId", String.valueOf(request.getId()));
            variables.put("exchangeId",exchangeId.getBody().replaceAll("\"", ""));
            restTemplate.put(
                Optional.ofNullable(env.getProperty("app.trade_engine_url")).orElse("")
                    .concat("/trade/{tradeId}/{exchangeId}"),
                Trade.class,
                variables
            );
        }
    }
}
