package com.rabobank.statement.parser.impls;

import java.io.File;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.springframework.stereotype.Component;

import com.rabobank.statement.parser.StatementParser;
import com.rabobank.statement.parser.exception.StatementParserException;
import com.rabobank.statement.parser.objects.Statement;
import com.rabobank.statement.parser.objects.Statements;
import com.rabobank.statement.parser.objects.XMLStatements;

/**
 * 
 * @author vijai
 *
 */
@Component(value="xmlStatementParser")
public class XMLStatementParser implements StatementParser {

	@Override
	public List<Statement> parse(File file) throws StatementParserException {
		Statements statements;
		JAXBContext jaxbContext;
		try {
			jaxbContext = JAXBContext.newInstance(XMLStatements.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			statements = (XMLStatements) unmarshaller.unmarshal(file);
		} catch (Exception exception) {
			throw new StatementParserException(exception);
		}
		return statements.getStatements();
	}

}
