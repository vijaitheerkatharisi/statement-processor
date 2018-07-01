package com.rabobank.statement.api.controllers.v1;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.io.Files;
import com.rabobank.statement.api.controllers.v1.objects.Statement;
import com.rabobank.statement.api.controllers.v1.objects.StatementResponse;
import com.rabobank.statement.api.v1.StatementAPI;
import com.rabobank.statement.constants.LogMessages;
import com.rabobank.statement.constants.ResponseCodeDescription;
import com.rabobank.statement.service.StatementService;
import com.rabobank.statement.service.impls.FileStorageService;
import com.rabobank.statement.service.objects.StatementServiceResponse;
import com.rabobank.statement.util.ResponseUtil;

@RestController
public class StatementController implements StatementAPI {

	public static final String FILE = "file";
	public static final Logger LOG = LoggerFactory.getLogger(StatementController.class);

	@Autowired
	StatementService statementService;

	@Autowired
	FileStorageService fileStorageService;
	
	@SuppressWarnings("unchecked")
	@Override
	public ResponseEntity<StatementResponse> process(@RequestParam(FILE) MultipartFile file) {

		StatementResponse response = new StatementResponse();
		Resource resource;
		StatementServiceResponse serviceResponse;
		try {

			String fileName = fileStorageService.storeFile(file);
			resource = fileStorageService.loadFileAsResource(fileName);
			
			String extension = Files.getFileExtension(fileName);
			
			if ("csv".equals(extension)) {
				LOG.info("processing csv file->{}",fileName);
				serviceResponse = statementService.processCSVStatement(resource.getFile());
			} else if ("xml".equals(extension)) {
				LOG.info("processing xml file ->{}",fileName);
				serviceResponse = statementService.processXMLStatement(resource.getFile());
			} else{
				LOG.error("invalid file format");
				return (ResponseEntity<StatementResponse>) ResponseUtil.preperEntityForBadRequest(ResponseCodeDescription.VALIDATION_ERROR, response);
			}
			List<Statement> statements = new ArrayList<>();
			
			if (serviceResponse != null && serviceResponse.getStatements() != null) {
				
				serviceResponse.getStatements().forEach(statement -> {

					Statement responseStatements = new Statement();
					responseStatements.setReference(statement.getReference());
					responseStatements.setDescription(statement.getDescription());
					statements.add(responseStatements);
					LOG.debug(LogMessages.FAILURE_RESPONSE,responseStatements);
				});
			
			}
			LOG.info("set statemts in to response");
			response.setStatement(statements);

		} catch (Exception exception) {
			LOG.error("excepton procession file", exception);
			return (ResponseEntity<StatementResponse>) ResponseUtil.preperEntityForException(ResponseCodeDescription.INTERNAL_SERVER_ERROR, response);
		}
		return (ResponseEntity<StatementResponse>) ResponseUtil.preperEntityForOk(serviceResponse.getServiceResponse(),response);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ResponseEntity<StatementResponse> processXML() {

		StatementResponse response = new StatementResponse();
		StatementServiceResponse serviceResponse;
		try {
			Resource resource = new ClassPathResource("records.xml");
			LOG.info("processing xml file");
			serviceResponse = statementService.processXMLStatement(resource.getFile());
			List<Statement> statements = new ArrayList<>();
			if (serviceResponse.getStatements() != null) {
				serviceResponse.getStatements().forEach(statement -> {

					Statement responseStatements = new Statement();
					responseStatements.setReference(statement.getReference());
					responseStatements.setDescription(statement.getDescription());
					statements.add(responseStatements);
					LOG.debug(LogMessages.FAILURE_RESPONSE,responseStatements);
				});
			}
			LOG.info("set statemts response");
			response.setStatement(statements);

		} catch (Exception exception) {
			LOG.error("exception processin xml file", exception);
			return (ResponseEntity<StatementResponse>) ResponseUtil.preperEntityForException(ResponseCodeDescription.INTERNAL_SERVER_ERROR, response);
		}
		return (ResponseEntity<StatementResponse>) ResponseUtil.preperEntityForOk(serviceResponse.getServiceResponse(),response);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ResponseEntity<StatementResponse> processCSV() {
		StatementResponse response = new StatementResponse();
		StatementServiceResponse serviceResponse;
		try {
			Resource resource = new ClassPathResource("records.csv");
			serviceResponse = statementService.processCSVStatement(resource.getFile());
			List<Statement> statements = new ArrayList<>();
			if (serviceResponse.getStatements() != null) {
				serviceResponse.getStatements().forEach(statement -> {
					LOG.info("processing xml file");
					Statement responseStatements = new Statement();
					responseStatements.setReference(statement.getReference());
					responseStatements.setDescription(statement.getDescription());
					statements.add(responseStatements);
					LOG.debug(LogMessages.FAILURE_RESPONSE,responseStatements);
				});
			}
			response.setStatement(statements);

		} catch (Exception exception) {
			LOG.error("exception processin xml file", exception);
			return (ResponseEntity<StatementResponse>) ResponseUtil.preperEntityForException(ResponseCodeDescription.INTERNAL_SERVER_ERROR, response);
		}
		return (ResponseEntity<StatementResponse>) ResponseUtil.preperEntityForOk(serviceResponse.getServiceResponse(),
				response);
	}

}
