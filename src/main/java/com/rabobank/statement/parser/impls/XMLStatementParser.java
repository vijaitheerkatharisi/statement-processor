package com.rabobank.statement.parser.impls;

import java.io.File;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.springframework.stereotype.Component;

import com.rabobank.statement.parser.StatementParser;
import com.rabobank.statement.parser.exception.StatementParserException;
import com.rabobank.statement.parser.objects.Transaction;
import com.rabobank.statement.parser.objects.Transactions;
import com.rabobank.statement.parser.objects.XMLTransactions;

/**
 * 
 * @author vijai
 *
 */
@Component(value = "xmlStatementParser")
public class XMLStatementParser implements StatementParser {

	@Override
	public List<Transaction> parse(File file) throws StatementParserException {
		Transactions transactions;
		JAXBContext jaxbContext;
		try {
			jaxbContext = JAXBContext.newInstance(XMLTransactions.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			transactions = (XMLTransactions) unmarshaller.unmarshal(file);
		} catch (Exception exception) {
			throw new StatementParserException(exception);
		}
		return transactions.getStatements();
	}

}
