package com.rabobank.statement.parser.impls;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.rabobank.statement.parser.StatementParser;
import com.rabobank.statement.parser.exception.StatementParserException;
import com.rabobank.statement.parser.objects.Statement;

/**
 * 
 * @author vijai
 *
 */
@Component(value = "csvStatementParser")
public class CSVStatementParser implements StatementParser {

	private static final String REFERAANCE_NO = "Reference";
	private static final String ACCOUNT_NUMBER = "AccountNumber";
	private static final String DESCR = "Description";
	private static final String START_BALANCE = "Start Balance";
	private static final String MUTATION = "Mutation";
	private static final String END_BALANCE = "End Balance";

	private static final String[] FILE_HEADER_MAPPING = { REFERAANCE_NO, ACCOUNT_NUMBER, DESCR, START_BALANCE, MUTATION,
			END_BALANCE };

	private static final Logger LOG = LoggerFactory.getLogger(CSVStatementParser.class);

	/**
	 * @param fileName
	 */
	@SuppressWarnings("resource")
	@Override
	public List<Statement> parse(File file) throws StatementParserException {

		CSVFormat csvFileFormat = CSVFormat.DEFAULT.withHeader(FILE_HEADER_MAPPING);
		List<Statement> statements;

		FileReader fileReader = null;

		try {
			fileReader = new FileReader(file);
			LOG.info("created csv parser");

			CSVParser csvParser = new CSVParser(fileReader, csvFileFormat);
			List<CSVRecord> csvRecords = csvParser.getRecords();

			LOG.info("collecting statement records from csv file");

			statements = csvRecords.stream().skip(1)
					.map(record -> new Statement(Long.parseLong(record.get(REFERAANCE_NO)), record.get(ACCOUNT_NUMBER),
							record.get(DESCR), Double.parseDouble(record.get(START_BALANCE)),
							Double.parseDouble(record.get(MUTATION)), Double.parseDouble(record.get(END_BALANCE))))
					.collect(Collectors.toList());

			LOG.info("collected statement records from csv file");

		} catch (Exception exception) {

			LOG.error("csv file procession error", exception);
			throw new StatementParserException(exception);
		} finally {
			try {
				if (fileReader != null)
					fileReader.close();

			} catch (IOException exception) {
				LOG.error("csv file procession error", exception);
			}
		}

		return statements;
	}

}
