package com.rabobank.statement.parser;

import java.io.File;
import java.util.List;

import com.rabobank.statement.parser.exception.StatementParserException;
import com.rabobank.statement.parser.objects.Transaction;

/**
 * Statement parser interface which need to be implemented by file parser
 * 
 * @author vijai
 *
 */
public interface StatementParser {

	public List<Transaction> parse(File file) throws StatementParserException;

}
