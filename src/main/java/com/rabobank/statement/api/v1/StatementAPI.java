package com.rabobank.statement.api.v1;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.rabobank.statement.api.controllers.v1.objects.StatementResponse;
import com.rabobank.statement.constants.Documentation;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RequestMapping(value = "/api/rest/v1/statement/")
@Api(tags = { "Statement Service Opearations" })
public interface StatementAPI {

	@PostMapping(value = "process", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(notes=Documentation.STATMENT_PROCESS_CSV_AND_XML_NOTES,consumes = MediaType.APPLICATION_JSON_VALUE, produces = "application/json", value = "Statement File Processor", response = StatementResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Operation Successful "),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found") })
	public ResponseEntity<StatementResponse> process(MultipartFile uploadfile);

	@GetMapping(value = "process/xml", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, value = "XML Statement File Processor", response = StatementResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Operation Successful "),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found") })
	public ResponseEntity<StatementResponse> processXML();

	@GetMapping(value = "process/csv", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, value = "CSV Statement Registration", response = StatementResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Operation Successful "),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found") })
	public ResponseEntity<StatementResponse> processCSV();
}
