package com.example.SpringBootAssignment.controller;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.SpringBootAssignment.SpringBootAssignmentApplication;
import com.example.SpringBootAssignment.model.IncidentResult;
import com.example.SpringBootAssignment.schedule.JobRunSchedule;
import com.example.SpringBootAssignment.service.SpringService;


@RestController
public class SpringBootAssignmentController {

	public static Logger logger = LoggerFactory.getLogger(SpringBootAssignmentApplication.class);

	@Autowired
	private SpringService springService;

	@Autowired
	private JobRunSchedule job;

	@GetMapping(path = "feedCustomerData")
	public void setDataInDB() throws DataNotSavedInDBException {
		logger.info("In setDataInDB method");
		try {
		springService.saveRecord();}
		catch(Exception e) {
			throw new DataNotSavedInDBException("Data Not Saved");
		}
	}

	@GetMapping(path = "asset")
	public Map<String, IncidentResult> getAsset() throws ParseException, IOException {

		logger.info("In getAsset method");

		@SuppressWarnings("unused")
		List<String> assetNames = springService.getAssetNames();
		LocalDate today = LocalDate.now();

		Map<String, IncidentResult> incidentMapByDate = springService.getIncidentMapByDate(today);

		job.runSchedule();
		logger.info("Ends getAsset method");

		return incidentMapByDate;
	}

}
