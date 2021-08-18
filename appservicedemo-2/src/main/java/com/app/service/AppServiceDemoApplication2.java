package com.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
@SpringBootApplication
@EnableEurekaClient
@RestController
public class AppServiceDemoApplication2 implements CommandLineRunner {

	@Autowired
	RestTemplateBuilder restTemplateBuilder;
	
	@Autowired
	EurekaClient eurekaClient;
	
	public static void main(String[] args) {
		SpringApplication.run(AppServiceDemoApplication2.class, args);
	}

	private static final String template = "AppService-2 Hello, %s!";
	private final AtomicLong counter = new AtomicLong();

	@GetMapping("/greeting-1")
	public Greeting greeting1(@RequestParam(value = "name", defaultValue = "World") String name) {
		
		return new Greeting(counter.incrementAndGet(), String.format(template, name));
		
	}

	
	@GetMapping("/greeting")
	public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
		RestTemplate restTemplate = restTemplateBuilder.build();
		InstanceInfo instanceInfo = eurekaClient.getNextServerFromEureka("app-service-1", false);
		String baseURL = instanceInfo.getHomePageUrl();
		System.out.println(" baseURL =" + baseURL.toString());
		//call app-service-1 greeting endpoint
		ResponseEntity<Greeting> response= restTemplate.exchange(baseURL +"/greeting", HttpMethod.GET,null,Greeting.class);	
		String responseContent= response.getBody().getContent() + " InstanceId= "+ instanceInfo.getInstanceId() + " GroupName="+instanceInfo.getAppGroupName();
		return new Greeting(counter.incrementAndGet(), String.format(template, responseContent));
	}


	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		
	}

	
}
