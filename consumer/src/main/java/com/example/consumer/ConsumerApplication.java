package com.example.consumer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.reactivestreams.Publisher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@SpringBootApplication
public class ConsumerApplication {

		@Bean
		WebClient client() {
				return WebClient.builder().build();
		}

		public static void main(String[] args) {
				SpringApplication.run(ConsumerApplication.class, args);
		}
}

@Component
class CustomerClient {

		private final WebClient webClient;

		CustomerClient(WebClient webClient) {
				this.webClient = webClient;
		}

		Flux<Customer> getAllCustomers() {
				return this.webClient
					.get()
					.uri("http://localhost:8080/customers")
					.retrieve()
					.bodyToFlux(Customer.class);
		}
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class Customer {
		private String id;
		private String name;
}