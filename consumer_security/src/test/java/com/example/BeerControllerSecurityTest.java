package com.example;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerPort;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Marcin Grzejszczak
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
// remove::start[]
// example of usage with fixed port
// @AutoConfigureStubRunner(stubsMode = StubRunnerProperties.StubsMode.LOCAL, ids =
// "com.example:beer-api-producer:+:stubs:8090")
@AutoConfigureStubRunner(stubsMode = StubRunnerProperties.StubsMode.LOCAL, ids = "com.example:beer-api-producer-security")
// remove::end[]
@DirtiesContext
public class BeerControllerSecurityTest extends AbstractTest {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	BeerController beerController;

	// remove::start[]
	@StubRunnerPort("beer-api-producer-security")
	int producerPort;

	@Before
	public void setupPort() {
		this.beerController.port = this.producerPort;
	}

	// remove::end[]
	// tag::tests[]
	@Test
	public void should_give_me_a_beer_when_im_old_enough() throws Exception {
		// remove::start[]
		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/beer")
						.contentType(MediaType.APPLICATION_JSON)
						.content(this.json.write(new Person("Old Enough", 42)).getJson()))
				.andExpect(status().isOk()).andExpect(content().string("THERE YOU GO"));
		// remove::end[]
	}

	@Test
	public void should_reject_a_beer_when_im_too_young() throws Exception {
		// remove::start[]
		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/beer")
						.contentType(MediaType.APPLICATION_JSON)
						.content(this.json.write(new Person("Too Young", 16)).getJson()))
				.andExpect(status().isOk()).andExpect(content().string("GET LOST"));
		// remove::end[]
	}
	// end::tests[]

}
