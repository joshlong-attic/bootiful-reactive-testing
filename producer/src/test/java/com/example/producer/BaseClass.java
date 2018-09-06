package com.example.producer;

import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;

/**
	* @author <a href="mailto:josh@joshlong.com">Josh Long</a>
	*/

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "server.port=0")
public class BaseClass {

		@LocalServerPort
		private int port;

		@Configuration
		@Import(ProducerApplication.class)
		public static class TestConfiguration {
		}

		@MockBean
		private CustomerRepository customerRepository;

		@Before
		public void before() throws Exception {

				RestAssured.baseURI = "http://localhost:" + this.port;

				Mockito
					.when(this.customerRepository.findAll())
					.thenReturn(Flux.just(new Customer("1", "Jane"), new Customer("2", "John")));

		}
}
