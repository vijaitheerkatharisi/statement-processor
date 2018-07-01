package com.rabobank.statement.parser.objects;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * XML Statement object
 * 
 * @author vijai
 *
 */
@XmlRootElement(name = "records")
public class XMLStatements implements Statements {
	
	
	List<Statement> statements;

	@XmlElement(name = "record")
	public List<Statement> getStatements() {
		return statements;
	}

	public void setStatements(List<Statement> statements) {
		this.statements = statements;
	}
	
}
