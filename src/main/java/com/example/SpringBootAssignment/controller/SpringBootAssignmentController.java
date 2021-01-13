package com.example.SpringBootAssignment.controller;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.context.ConfigurationPropertiesAutoConfiguration;
import org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.HttpHandlerAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.ReactiveWebServerFactoryAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.WebFluxAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.error.ErrorWebFluxAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.SpringBootAssignment.SpringBootAssignmentApplication;
import com.example.SpringBootAssignment.model.IncidentResult;
import com.example.SpringBootAssignment.schedule.JobRunSchedule;
import com.example.SpringBootAssignment.service.SpringService;

@SpringBootConfiguration
@Import({ WebFluxAutoConfiguration.class, ReactiveWebServerFactoryAutoConfiguration.class,
		ErrorWebFluxAutoConfiguration.class, HttpHandlerAutoConfiguration.class,
		ConfigurationPropertiesAutoConfiguration.class, PropertyPlaceholderAutoConfiguration.class })


@RestController
public class SpringBootAssignmentController {

	public static Logger logger= LoggerFactory.getLogger(SpringBootAssignmentApplication.class);
	
	@Autowired
	private SpringService springService;

	@Autowired
	private JobRunSchedule job;

	@RequestMapping(path = "feedCustomerData")
	public void setDataInDB() {
		logger.info("In setDataInDB method");
		try {
			
			springService.saveRecord();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
		}
		logger.info("Ends setDataInDB method");
	}

	@RequestMapping(path = "asset")
	public ResponseEntity getAsset() throws ParseException, IOException {
		
		
		logger.info("In getAsset method");

		List<String> assetNames = springService.getAssetNames();
		LocalDate today = LocalDate.now();

		Map<String, IncidentResult> incidentMapByDate = springService.getIncidentMapByDate(today);

		logger.info("Ends getAsset method");

		return ResponseEntity.ok(assetNames);
	}
	
	
	

}
