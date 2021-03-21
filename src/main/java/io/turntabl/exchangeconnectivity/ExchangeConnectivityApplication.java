package io.turntabl.exchangeconnectivity;

import io.turntabl.exchangeconnectivity.resources.controller.ReceiveOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class ExchangeConnectivityApplication {

	@Autowired
	private Environment env;

	@Bean
	private static RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	public static void main(String[] args) {
		// Get application context
		ConfigurableApplicationContext ctx = SpringApplication.run(ExchangeConnectivityApplication.class, args);
		// create instance of ReceiveOrder with context
		ReceiveOrder receiveOrder = ctx.getBean(ReceiveOrder.class);
		// Call receiveOrder() method to keep checking queue
		receiveOrder.receiveOrder();

	}

}
