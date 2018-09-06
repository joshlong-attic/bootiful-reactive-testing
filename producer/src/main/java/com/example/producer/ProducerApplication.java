package com.example.producer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;

import java.util.UUID;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@SpringBootApplication
public class ProducerApplication {

		public static void main(String[] args) {
				SpringApplication.run(ProducerApplication.class, args);
		}
}

@Configuration
class CustomerWebConfiguration {

		@Bean
		RouterFunction<ServerResponse> routes(CustomerRepository cr) {
				return route(GET("/customers"), r -> ServerResponse.ok().body(cr.findAll(), Customer.class));
		}

//		@Bean
		ApplicationRunner runner(CustomerRepository cr) {
				return args -> cr
					.saveAll(Flux.just(new Customer(UUID.randomUUID().toString(), "Jane"), new Customer(UUID.randomUUID().toString(), "John")))
					.subscribe(d -> System.out.println("saved " + d.toString()));
		}
}

interface CustomerRepository extends ReactiveMongoRepository<Customer, String> {

		Flux<Customer> findByName(String name);
}

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
class Customer {

		@Id
		private String id;
		private String name;
}