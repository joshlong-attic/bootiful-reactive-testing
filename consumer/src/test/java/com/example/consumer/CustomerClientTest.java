package com.example.consumer;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

/**
	* @author <a href="mailto:josh@joshlong.com">Josh Long</a>
	*/
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RunWith(SpringRunner.class)
@Import(ConsumerApplication.class)
//@org.springframework.cloud.contract.wiremock.AutoConfigureWireMock(port = 8080)
@AutoConfigureStubRunner(ids = "com.example:producer:+:8080",
	stubsMode = StubRunnerProperties.StubsMode.LOCAL)
public class CustomerClientTest {

		@Autowired
		private CustomerClient client;

		//		@Before
		public void setupWireMock() throws Exception {

				WireMock.stubFor(
					WireMock
						.get("/customers")
						.willReturn(
							WireMock
								.aResponse()
								.withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE)
								.withBody("[{ \"id\":\"1\", \"name\":\"Jane\"},{ \"id\":\"2\", \"name\":\"John\"}]")
						)
				);

		}

		@Test
		public void getAllCustomers() {
				Flux<Customer> customers = this.client.getAllCustomers();
				StepVerifier
					.create(customers)
					.expectNext(new Customer("1", "Jane"))
					.expectNext(new Customer("2", "John"))
					.verifyComplete();
		}

}