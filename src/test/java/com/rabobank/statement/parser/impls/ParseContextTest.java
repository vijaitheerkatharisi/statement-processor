package com.rabobank.statement.parser.impls;

import static org.junit.Assert.assertEquals;
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

import com.rabobank.statement.parser.StatementParser;
import com.rabobank.statement.parser.exception.StatementParserException;
import com.rabobank.statement.parser.objects.Transaction;

@RunWith(MockitoJUnitRunner.class)
public class ParseContextTest {

	@InjectMocks
	private ParseContext classUnderTest;
	
	@Mock
	private StatementParser parser;

	List<Transaction> transactions =new ArrayList<>();
	@Before
	public void setUp() throws Exception{
		Transaction transaction=new Transaction();
		transaction.setReference(Long.valueOf("194261"));
		transaction.setAccountNumber("NL91RABO0315273637");
		transaction.setDescription("description");
		transaction.setEndBalance(-20.23);
		transaction.setStartBalance(21.6);
		transaction.setMutation(-41.83);
		Transaction statement2=new Transaction();
		statement2.setReference(Long.valueOf("194261"));
		statement2.setAccountNumber("NL91RABO0315273637");
		statement2.setDescription("description");
		statement2.setEndBalance(-20.23);
		statement2.setStartBalance(21.6);
		statement2.setMutation(-41.83);

		
		transactions.add(transaction);
		transactions.add(statement2);
	}
	@Test
	public void testForSuccess() throws Exception {
		
		when(this.parser.parse(any())).thenReturn(transactions);
		List<Transaction> actual=classUnderTest.paresFile(new ClassPathResource("records.csv").getFile());
		assertEquals(transactions,actual);
	}
	@Test(expected=StatementParserException.class)
	public void testForException() throws Exception {
		
		when(this.parser.parse(any())).thenThrow(StatementParserException.class);
		
		List<Transaction> actual=classUnderTest.paresFile(new ClassPathResource("records.csv").getFile());
		assertEquals(transactions,actual);
	}

}
