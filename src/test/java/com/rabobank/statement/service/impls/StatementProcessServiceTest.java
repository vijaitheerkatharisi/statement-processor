package com.rabobank.statement.service.impls;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.io.ClassPathResource;

import com.rabobank.statement.constants.ResponseCodeDescription;
import com.rabobank.statement.parser.exception.StatementParserException;
import com.rabobank.statement.parser.impls.CSVStatementParser;
import com.rabobank.statement.parser.impls.ParseContext;
import com.rabobank.statement.parser.impls.XMLStatementParser;
import com.rabobank.statement.parser.objects.Statement;
import com.rabobank.statement.service.impls.StatementProcessService;
import com.rabobank.statement.service.objects.StatementServiceResponse;
@RunWith(MockitoJUnitRunner.class)
public class StatementProcessServiceTest {

	@InjectMocks
	private StatementProcessService classUnderTest;

	@Mock
	private ParseContext mockParseContext;
	
	@Mock
	CSVStatementParser mockCSVStatementParser;
	
	@Mock
	XMLStatementParser mockXMLStatementParser;
	
	List<Statement> statements =new ArrayList<>();
	List<Statement> uniqueStatements =new ArrayList<>();
	
	@Before
	public void setUp() throws Exception{
		Statement statement=new Statement();
		statement.setReference(Long.valueOf("194261"));
		statement.setAccountNumber("NL91RABO0315273637");
		statement.setDescription("description");
		statement.setEndBalance(-20.23);
		statement.setStartBalance(21.6);
		statement.setMutation(-41.83);
		Statement statement2=new Statement();
		statement2.setReference(Long.valueOf("194261"));
		statement2.setAccountNumber("NL91RABO0315273637");
		statement2.setDescription("description");
		statement2.setEndBalance(-20.23);
		statement2.setStartBalance(21.6);
		statement2.setMutation(-41.83);

		
		statements.add(statement);
		statements.add(statement2);
		
		Statement statement3=new Statement();
		statement3.setReference(Long.valueOf("194261"));
		statement3.setAccountNumber("NL91RABO0315273637");
		statement3.setDescription("description");
		statement3.setEndBalance(-20.23);
		statement3.setStartBalance(21.6);
		statement3.setMutation(-41.83);
		Statement statement4=new Statement();
		statement4.setReference(Long.valueOf("194263"));
		statement4.setAccountNumber("NL91RABO0315273637");
		statement4.setDescription("description");
		statement4.setEndBalance(-20.23);
		statement4.setStartBalance(21.6);
		statement4.setMutation(-41.83);
		uniqueStatements.add(statement3);
		uniqueStatements.add(statement4);
		
	}
	
	@Test
	public void testProcessCSVStatementForSuccess() throws Exception {
		
		StatementServiceResponse expected = new StatementServiceResponse();
		expected.setStatements(statements);
		expected.setServiceResponse(ResponseCodeDescription.SUCCESS);
		
		when(this.mockParseContext.paresFile(any())).thenReturn(statements);
		
		StatementServiceResponse actual = classUnderTest.processCSVStatement(new ClassPathResource("records.csv").getFile());
		assertThat(expected).isEqualToComparingFieldByFieldRecursively(actual);
	}
	@Test
	public void testProcessCSVStatementForNoData() throws Exception {
		
		StatementServiceResponse expected = new StatementServiceResponse();
		expected.setStatements(new ArrayList<Statement>());
		expected.setServiceResponse(ResponseCodeDescription.NO_DATE_FOUND);
		
		when(this.mockParseContext.paresFile(any())).thenReturn(uniqueStatements);
		
		StatementServiceResponse actual = classUnderTest.processCSVStatement(new ClassPathResource("records.csv").getFile());
		assertThat(expected).isEqualToComparingFieldByFieldRecursively(actual);
	}
	
	@Test
	public void testProcessCSVStatementForError() throws Exception {
		
		StatementServiceResponse expected = new StatementServiceResponse();
		expected.setServiceResponse(ResponseCodeDescription.ERROR);
		
		when(this.mockParseContext.paresFile(any())).thenThrow(StatementParserException.class);
		
		StatementServiceResponse actual = classUnderTest.processCSVStatement(new ClassPathResource("records.csv").getFile());
		assertThat(expected).isEqualToComparingFieldByFieldRecursively(actual);
	}
	
	@Test
	public void testProcessXMLStatementForSuccess() throws Exception {
		
		StatementServiceResponse expected = new StatementServiceResponse();
		expected.setStatements(statements);
		expected.setServiceResponse(ResponseCodeDescription.SUCCESS);
		
		when(this.mockParseContext.paresFile(any())).thenReturn(statements);
		
		StatementServiceResponse actual = classUnderTest.processXMLStatement(new ClassPathResource("records.xml").getFile());
		assertThat(expected).isEqualToComparingFieldByFieldRecursively(actual);
	}
	
	@Test
	public void testProcessXMLStatementForSuccessNoDataFound() throws Exception {
		
		StatementServiceResponse expected = new StatementServiceResponse();
		expected.setStatements(new ArrayList<Statement>());
		expected.setServiceResponse(ResponseCodeDescription.NO_DATE_FOUND);
		
		when(this.mockParseContext.paresFile(any())).thenReturn(uniqueStatements);
		
		StatementServiceResponse actual = classUnderTest.processXMLStatement(new ClassPathResource("records.xml").getFile());
		assertThat(expected).isEqualToComparingFieldByFieldRecursively(actual);
	}
	
	@Test
	public void testProcessXMLStatementForError() throws Exception {
		
		StatementServiceResponse expected = new StatementServiceResponse();
		expected.setServiceResponse(ResponseCodeDescription.ERROR);
		
		when(this.mockParseContext.paresFile(any())).thenThrow(StatementParserException.class);
		
		StatementServiceResponse actual = classUnderTest.processXMLStatement(new ClassPathResource("records.xml").getFile());
		assertThat(expected).isEqualToComparingFieldByFieldRecursively(actual);
	}

}
