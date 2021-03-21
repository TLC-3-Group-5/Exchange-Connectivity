package io.turntabl.exchangeconnectivity.resources.controller;

import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

// Class to receive order
@Component
public class ReceiveOrder {

    public ReceiveOrder(){

    }

    // Method to keep checking queue
    public void receiveOrder(){
        Jedis jedis = new Jedis("localhost", 6379);
        while(true)
        {
            System.out.println(jedis.blpop(0, "order"));

        }
    }
}
