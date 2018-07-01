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
public class XMLStatementParserTest {

	@InjectMocks
	private XMLStatementParser classUnderTest;
	
	private List<Statement> expected =new ArrayList<>();
	
	@Before
	public void setUp() throws Exception{
		Statement statement=new Statement();
		statement.setReference(Long.valueOf("130498"));
		statement.setAccountNumber("NL69ABNA0433647324");
		statement.setDescription("Tickets for Peter Theuß");
		statement.setEndBalance(8.12);
		statement.setStartBalance(26.9);
		statement.setMutation(-18.78);
		Statement statement2=new Statement();
		statement2.setReference(Long.valueOf("167875"));
		statement2.setAccountNumber("NL93ABNA0585619023");
		statement2.setDescription("Tickets from Erik de Vries");
		statement2.setEndBalance(6368.00);
		statement2.setStartBalance(5429.00);
		statement2.setMutation(939.00);

		
		expected.add(statement);
		expected.add(statement2);
	}
	@Test
	public void testParseWithReocrds() throws Exception {
		List<Statement> actual=classUnderTest.parse(new ClassPathResource("records.xml").getFile());
		assertEquals(expected,actual);
	
	}

	@Test(expected=StatementParserException.class)
	public void testParseForException() throws Exception {
		classUnderTest.parse(new ClassPathResource("invalidRecords.xml").getFile());
	
	}
	


}
