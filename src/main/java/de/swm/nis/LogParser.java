package de.swm.nis;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;



public class LogParser {

	public Row parseLogLine(String message) {

		Row row = null;
		// First, determine if we have a begin, commit or table modification message (summing up Insert, update,
		// delete):
		String startToken = message.substring(0, 6);
		switch (startToken) {
			case "BEGIN ": {
				log("Transaction start found, ignoring");
				break;
			}
			case "COMMIT": {
				log("Transaction end found, ignoring");
				break;
			}
			case "table ": {
				row = new Row();
				List<String> tokens = splitKeyValuePairs(message);
				row.setTableName(tokens.get(1).substring(0, tokens.get(1).length() - 1));
				switch (tokens.get(2)) {
					case "INSERT:": {
						// leave "old values" empty
						Cell cell = null;
						for (int i = 3; i < tokens.size(); i++) {
							// odd tokens contain name and type
							List<String> nameTypeValue = Lists.newArrayList(Splitter.on(':').trimResults()
									.omitEmptyStrings().split(tokens.get(i)));
							String nameAndType = nameTypeValue.get(0);
							String value = nameTypeValue.get(1);
							List<String> nameAndTypeSplitted = Lists.newArrayList(Splitter.on('[').split(nameAndType));
							String name = nameAndTypeSplitted.get(0);
							String type = nameAndTypeSplitted.get(1).substring(0,
									nameAndTypeSplitted.get(1).length() - 1);
							cell = new Cell(type, name, value);

							row.getNewValues().add(cell);
						}

						break;
					}
					case "UPDATE:": {

						break;
					}
					case "DELETE:": {
						// leave "new values" empty
						break;
					}
				}
				break;
			}
		}
		return row;
	}



	public List<String> splitKeyValuePairs(String message) {
		//within '' escaped Strings blanks are not consiedered as delimiters
		List<String> tokens = Lists.newArrayList(Splitter.on(' ').trimResults().omitEmptyStrings().split(message));

		List<String> returnvalues = new ArrayList<String>(tokens.size());

		boolean quoteOn = false;
		StringBuffer collector= new StringBuffer();
		for (String token : tokens) {
			int singleQuoteCount = CharMatcher.is('\'').countIn(token);
			if (singleQuoteCount == 2) {
				// Begin of Quoting, collect all incoming tokens into a string
				if (!quoteOn) {
					quoteOn = true;
					collector = new StringBuffer(token);
				}
				else {
					// End of Quoting
					quoteOn = false;
					collector.append(" " + token);
					returnvalues.add(collector.toString());
				}
			}
			else if (quoteOn) {
				// Middle of quoting
				collector.append(" " + token);
			} else {
				returnvalues.add(token);
			}
		}
		return returnvalues;
	}



	// TODO setup logging
	private void log(String message) {
		System.out.println(message);
	}

}
