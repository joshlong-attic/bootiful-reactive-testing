package com.example.producer;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

/**
	* @author <a href="mailto:josh@joshlong.com">Josh Long</a>
	*/
@DataMongoTest
@RunWith(SpringRunner.class)
public class CustomerRepositoryTest {

		@Autowired
		private CustomerRepository customerRepository;

		private final Customer one = new Customer("1", "Jane");
		private final Customer two = new Customer("2", "John");

		@Test
		public void query() throws Exception {

				Publisher<Customer> setup = this.customerRepository
					.deleteAll()
					.thenMany(this.customerRepository.saveAll(Flux.just(this.one, this.two)));

				Publisher<Customer> all = this.customerRepository.findAll();

				Publisher<Customer> composite = Flux
					.from(setup)
					.thenMany(all);

				StepVerifier
					.create(composite)
					.expectNext(this.one, this.two)
					.verifyComplete();

		}
}

