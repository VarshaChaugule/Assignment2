package com.example.SpringBootAssignment.schedule;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.SpringBootAssignment.model.IncidentResult;
import com.example.SpringBootAssignment.service.SpringService;

@Component
public class JobRunSchedule {
	private static final Logger LOGGER = LoggerFactory.getLogger(JobRunSchedule.class);
	private SpringService service;
	private List<String[]> dataLines = new ArrayList<>();

	@Autowired
	public JobRunSchedule(SpringService service) {
		this.service = service;
	}

	@Scheduled(cron = "0 0 4 1/1 * ? ")
	public void runSchedule() throws IOException, ParseException {
		LOGGER.info("!!!  Cron Job Started   !!!");
		dataLines.clear();
		LocalDate today = LocalDate.now();
		Map<String, IncidentResult> incidentMapByDate = service.getIncidentMapByDate(today);
		Collection<IncidentResult> values = incidentMapByDate.values();
		dataLines.add(new String[] { "Asset Name", "Total Incident", "Total Uptime", "Rating" });
		for (IncidentResult name : values) {
			dataLines.add(new String[] { name.getAssetName(), String.valueOf(name.getTotalIncident()),
					String.valueOf(name.getTotalUptime()), String.valueOf(name.getRating()) });
		}

		createOutputCsv();

	}

	public void createOutputCsv() throws IOException {
		LOGGER.info("!!   Creating CSV file  !!");
		File csvOutputFile = new File("output.csv");
		try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
			dataLines.stream().map(this::convertToCSV).forEach(pw::println);
		}
		assertTrue(csvOutputFile.exists());
	}

	public String escapeSpecialCharacters(String data) {
		String escapedData = data.replaceAll("\\R", " ");
		if (data.contains(",") || data.contains("\"") || data.contains("'")) {
			data = data.replace("\"", "\"\"");
			escapedData = "\"" + data + "\"";
		}
		return escapedData;
	}

	public String convertToCSV(String[] data) {
		LOGGER.info("!!  Converting Data to CSV file  !!");
		return Stream.of(data).map(this::escapeSpecialCharacters).collect(Collectors.joining(","));
	}

}
