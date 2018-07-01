package com.rabobank.statement.parser.impls;

import java.io.File;
import java.util.List;

import org.springframework.stereotype.Component;

import com.rabobank.statement.parser.StatementParser;
import com.rabobank.statement.parser.exception.StatementParserException;
import com.rabobank.statement.parser.objects.Statement;

@Component
public class ParseContext {

	private StatementParser parser;

	public void setParser(StatementParser parser) {
		this.parser = parser;
	}

	public List<Statement> paresFile(File file) throws StatementParserException {
		return parser.parse(file);
	}

}