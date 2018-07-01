package com.rabobank.statement.api.controllers.v1;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabobank.statement.api.controllers.v1.objects.StatementResponse;
import com.rabobank.statement.constants.ResponseCodeDescription;
import com.rabobank.statement.parser.objects.Statement;
import com.rabobank.statement.service.StatementService;
import com.rabobank.statement.service.impls.FileStorageService;
import com.rabobank.statement.service.objects.StatementServiceResponse;

@RunWith(SpringRunner.class)
@WebMvcTest(value = StatementController.class, secure = false)
public class StatementControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	StatementService statementService;
	
	@MockBean
	FileStorageService fileStorageService;

	List<Statement> statements = new ArrayList<>();

	
	@Before
	public void setUp() throws Exception {
		Statement statement = new Statement();
		statement.setReference(Long.valueOf("165102"));
		statement.setAccountNumber("NL91RABO0315273637");
		statement.setDescription("Tickets from Erik de Vries");
		statement.setEndBalance(-20.23);
		statement.setStartBalance(21.6);
		statement.setMutation(-41.83);
		Statement statement2 = new Statement();
		statement2.setReference(Long.valueOf("165102"));
		statement2.setAccountNumber("NL91RABO0315273637");
		statement2.setDescription("Tickets for Rik Theu");
		statement2.setEndBalance(-20.23);
		statement2.setStartBalance(21.6);
		statement2.setMutation(-41.83);
		statements.add(statement);
		statements.add(statement2);
	}
	
	@Test
	public void testProcessForCSVFileSuccess() throws Exception {

		StatementServiceResponse response = new StatementServiceResponse();
		response.setStatements(statements);
		response.setServiceResponse(ResponseCodeDescription.SUCCESS);
		MockMultipartFile multipartFile  = new MockMultipartFile("file", "records.csv",
                "multipart/form-data", "Spring Framework".getBytes());
		Resource resource= new ClassPathResource("records.csv");
		when(this.fileStorageService.storeFile(any())).thenReturn("records.csv");
		
		when(this.fileStorageService.loadFileAsResource(any())).thenReturn(resource);
		
		when(this.statementService.processCSVStatement(any())).thenReturn(response);

		ObjectMapper mapper = new ObjectMapper();

		StatementResponse expected = mapper.readValue(
				new ClassPathResource("com/rabobank/statement/api/controllers/v1/SuccessResponse.json").getFile(),
				StatementResponse.class);
		MvcResult result = this.mockMvc.perform(multipart("/api/rest/v1/statement/process").file(multipartFile)).andExpect(status().isOk())
				.andReturn();

		assertEquals(mapper.writeValueAsString(expected), result.getResponse().getContentAsString());
	}
	
	@Test
	public void testProcessForXMLFileSuccess() throws Exception {

		StatementServiceResponse response = new StatementServiceResponse();
		response.setStatements(statements);
		response.setServiceResponse(ResponseCodeDescription.SUCCESS);
		MockMultipartFile multipartFile  = new MockMultipartFile("file", "records.xml",
                "multipart/form-data", "Statements process".getBytes());
		Resource resource= new ClassPathResource("records.csv");
		when(this.fileStorageService.storeFile(any())).thenReturn("records.xml");
		
		when(this.fileStorageService.loadFileAsResource(any())).thenReturn(resource);
		
		when(this.statementService.processXMLStatement(any())).thenReturn(response);

		ObjectMapper mapper = new ObjectMapper();

		StatementResponse expected = mapper.readValue(
				new ClassPathResource("com/rabobank/statement/api/controllers/v1/SuccessResponse.json").getFile(),
				StatementResponse.class);
		MvcResult result = this.mockMvc.perform(multipart("/api/rest/v1/statement/process").file(multipartFile)).andExpect(status().isOk())
				.andReturn();

		assertEquals(mapper.writeValueAsString(expected), result.getResponse().getContentAsString());
	}
	@Test
	public void testProcessForInValidFile() throws Exception {

		MockMultipartFile multipartFile  = new MockMultipartFile("file", "records.test",
                "multipart/form-data", "Spring Framework".getBytes());
		Resource resource= new ClassPathResource("records.csv");
		when(this.fileStorageService.storeFile(any())).thenReturn("records.test");
		
		when(this.fileStorageService.loadFileAsResource(any())).thenReturn(resource);
		ObjectMapper mapper = new ObjectMapper();

		StatementResponse expected = mapper.readValue(
				new ClassPathResource("com/rabobank/statement/api/controllers/v1/InvalidFileResponse.json").getFile(),
				StatementResponse.class);
		MvcResult result = this.mockMvc.perform(multipart("/api/rest/v1/statement/process").file(multipartFile)).andExpect(status().is4xxClientError())
				.andReturn();

		assertEquals(mapper.writeValueAsString(expected), result.getResponse().getContentAsString());
	}

	@Test
	public void testProcessForServerError() throws Exception {
		
		MockMultipartFile multipartFile  = new MockMultipartFile("file", "records.test",
                "multipart/form-data", "Spring Framework".getBytes());
		
		when(this.fileStorageService.storeFile(any())).thenThrow(RuntimeException.class);

		ObjectMapper mapper = new ObjectMapper();

		StatementResponse expected = mapper.readValue(
				new ClassPathResource("com/rabobank/statement/api/controllers/v1/ServerErrorResponse.json").getFile(),
				StatementResponse.class);
	
		MvcResult result = this.mockMvc.perform(multipart("/api/rest/v1/statement/process").file(multipartFile)).andExpect(status().is5xxServerError())
				.andReturn();

		assertEquals(mapper.writeValueAsString(expected), result.getResponse().getContentAsString());
	}
	@Test
	public void testProcessCSVForSuccess() throws Exception {

		StatementServiceResponse response = new StatementServiceResponse();
		response.setStatements(statements);
		response.setServiceResponse(ResponseCodeDescription.SUCCESS);

		when(this.statementService.processCSVStatement(any())).thenReturn(response);

		ObjectMapper mapper = new ObjectMapper();

		StatementResponse expected = mapper.readValue(
				new ClassPathResource("com/rabobank/statement/api/controllers/v1/SuccessResponse.json").getFile(),
				StatementResponse.class);
		MvcResult result = this.mockMvc.perform(get("/api/rest/v1/statement/process/csv")).andExpect(status().isOk())
				.andReturn();

		assertEquals(mapper.writeValueAsString(expected), result.getResponse().getContentAsString());
	}

	@Test
	public void testProcessCSVForError() throws Exception {

		StatementServiceResponse response = new StatementServiceResponse();
		response.setServiceResponse(ResponseCodeDescription.ERROR);

		when(this.statementService.processCSVStatement(any())).thenReturn(response);

		ObjectMapper mapper = new ObjectMapper();

		StatementResponse expected = mapper.readValue(
				new ClassPathResource("com/rabobank/statement/api/controllers/v1/ErrorResponse.json").getFile(),
				StatementResponse.class);
		MvcResult result = this.mockMvc.perform(get("/api/rest/v1/statement/process/csv")).andExpect(status().isOk())
				.andReturn();

		assertEquals(mapper.writeValueAsString(expected), result.getResponse().getContentAsString());
	}

	@Test
	public void testProcessCSVForNoDataFound() throws Exception {

		StatementServiceResponse response = new StatementServiceResponse();
		response.setServiceResponse(ResponseCodeDescription.NO_DATE_FOUND);
		response.setStatements(new ArrayList<>());
		when(this.statementService.processCSVStatement(any())).thenReturn(response);

		ObjectMapper mapper = new ObjectMapper();

		StatementResponse expected = mapper.readValue(
				new ClassPathResource("com/rabobank/statement/api/controllers/v1/NoDataFoundResponse.json").getFile(),
				StatementResponse.class);
		MvcResult result = this.mockMvc.perform(get("/api/rest/v1/statement/process/csv")).andExpect(status().isOk())
				.andReturn();

		assertEquals(mapper.writeValueAsString(expected), result.getResponse().getContentAsString());
	}

	@Test
	public void testProcessCSVForServerError() throws Exception {

		when(this.statementService.processCSVStatement(any())).thenThrow(RuntimeException.class);

		ObjectMapper mapper = new ObjectMapper();

		StatementResponse expected = mapper.readValue(
				new ClassPathResource("com/rabobank/statement/api/controllers/v1/ServerErrorResponse.json").getFile(),
				StatementResponse.class);
		MvcResult result = this.mockMvc.perform(get("/api/rest/v1/statement/process/csv"))
				.andExpect(status().is5xxServerError()).andReturn();

		assertEquals(mapper.writeValueAsString(expected), result.getResponse().getContentAsString());
	}

	

	@Test
	public void testProcessXMLForSuccess() throws Exception {

		StatementServiceResponse response = new StatementServiceResponse();
		response.setStatements(statements);
		response.setServiceResponse(ResponseCodeDescription.SUCCESS);

		when(this.statementService.processXMLStatement(any())).thenReturn(response);

		ObjectMapper mapper = new ObjectMapper();

		StatementResponse expected = mapper.readValue(
				new ClassPathResource("com/rabobank/statement/api/controllers/v1/SuccessResponse.json").getFile(),
				StatementResponse.class);
		MvcResult result = this.mockMvc.perform(get("/api/rest/v1/statement/process/xml")).andExpect(status().isOk())
				.andReturn();

		assertEquals(mapper.writeValueAsString(expected), result.getResponse().getContentAsString());
	}

	@Test
	public void testProcessXMLError() throws Exception {

		StatementServiceResponse response = new StatementServiceResponse();
		response.setServiceResponse(ResponseCodeDescription.ERROR);

		when(this.statementService.processXMLStatement(any())).thenReturn(response);

		ObjectMapper mapper = new ObjectMapper();

		StatementResponse expected = mapper.readValue(
				new ClassPathResource("com/rabobank/statement/api/controllers/v1/ErrorResponse.json").getFile(),
				StatementResponse.class);
		MvcResult result = this.mockMvc.perform(get("/api/rest/v1/statement/process/xml")).andExpect(status().isOk())
				.andReturn();

		assertEquals(mapper.writeValueAsString(expected), result.getResponse().getContentAsString());
	}

	@Test
	public void testProcessXMLForNoDataFound() throws Exception {

		StatementServiceResponse response = new StatementServiceResponse();
		response.setServiceResponse(ResponseCodeDescription.NO_DATE_FOUND);
		response.setStatements(new ArrayList<>());
		when(this.statementService.processXMLStatement(any())).thenReturn(response);

		ObjectMapper mapper = new ObjectMapper();

		StatementResponse expected = mapper.readValue(
				new ClassPathResource("com/rabobank/statement/api/controllers/v1/NoDataFoundResponse.json").getFile(),
				StatementResponse.class);
		MvcResult result = this.mockMvc.perform(get("/api/rest/v1/statement/process/xml")).andExpect(status().isOk())
				.andReturn();

		assertEquals(mapper.writeValueAsString(expected), result.getResponse().getContentAsString());
	}

	@Test
	public void testProcessXMLForServerError() throws Exception {

		when(this.statementService.processXMLStatement(any())).thenThrow(RuntimeException.class);

		ObjectMapper mapper = new ObjectMapper();

		StatementResponse expected = mapper.readValue(
				new ClassPathResource("com/rabobank/statement/api/controllers/v1/ServerErrorResponse.json").getFile(),
				StatementResponse.class);
		MvcResult result = this.mockMvc.perform(get("/api/rest/v1/statement/process/xml"))
				.andExpect(status().is5xxServerError()).andReturn();

		assertEquals(mapper.writeValueAsString(expected), result.getResponse().getContentAsString());
	}
}