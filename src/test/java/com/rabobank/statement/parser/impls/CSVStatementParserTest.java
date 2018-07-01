package com.rabobank.statement.parser.impls;


import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.io.ClassPathResource;

import com.rabobank.statement.parser.exception.StatementParserException;
import com.rabobank.statement.parser.objects.Statement;

@RunWith(MockitoJUnitRunner.class)
public class CSVStatementParserTest {

	@InjectMocks
	private CSVStatementParser classUnderTest;
	
	private List<Statement> expected =new ArrayList<>();
	
	@Before
	public void setUp() throws Exception{
		Statement statement=new Statement();
		statement.setReference(Long.valueOf("194261"));
		statement.setAccountNumber("NL91RABO0315273637");
		statement.setDescription("Clothes from Jan Bakker");
		statement.setEndBalance(21.6);
		statement.setStartBalance(-20.23);
		statement.setMutation(-41.83);
		Statement statement2=new Statement();
		statement2.setReference(Long.valueOf("112806"));
		statement2.setAccountNumber("NL27SNSB0917829871");
		statement2.setDescription("Clothes for Willem Dekker");
		statement2.setEndBalance(106.8);
		statement2.setStartBalance(91.23);
		statement2.setMutation(+15.57);

		
		expected.add(statement);
		expected.add(statement2);
	}
	@Test
	public void testParseWithReocrds() throws Exception {
		List<Statement> actual=classUnderTest.parse(new ClassPathResource("records.csv").getFile());
		assertEquals(expected,actual);
	
	}

	@Test(expected=StatementParserException.class)
	public void testParseForException() throws Exception {
		classUnderTest.parse(new ClassPathResource("invaildRecords.csv").getFile());
	
	}
	@Test(expected=Exception.class)
	public void testParseForIOException() throws Exception {
		classUnderTest.parse(new ClassPathResource(null).getFile());
	
	}


}
