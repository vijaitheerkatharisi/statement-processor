package com.rabobank.statement.parser;

import java.io.File;
import java.util.List;

import com.rabobank.statement.parser.exception.StatementParserException;
import com.rabobank.statement.parser.objects.Statement;

/**
 * Statement parser interface which need to be implemented by file parser
 * 
 * @author vijai
 *
 */
public interface StatementParser {

	public List<Statement> parse(File file) throws StatementParserException;

}
	