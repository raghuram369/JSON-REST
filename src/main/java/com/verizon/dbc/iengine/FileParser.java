package com.verizon.dbc.iengine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.verizon.dbc.iengine.config.CommonProperties;
import com.verizon.dbc.iengine.config.ConnectionProperties;
import com.verizon.dbc.iengine.webservice.ParseData;

@Component
public class FileParser {

	// private final static Pattern LINE_PATTERN =
	// Pattern.compile("([a-zA-Z]{3}\\s+\\d?\\d\\s+\\d\\d:\\d\\d:\\d\\d)\\s+([^\\.]+?)\\..+?([a-zA-Z]{3}\\s+\\d?\\d\\s+\\d\\d:\\d\\d:\\d\\d):\\s+%(.+?):(\\s+.+?:\\s+)");
	private final static Pattern LINE_PATTERN = Pattern
			.compile("([a-zA-Z]{3}\\s+\\d?\\d\\s+\\d\\d:\\d\\d:\\d\\d)\\s+.+?\\s+(\\*[a-zA-Z]{3}\\s+\\d?\\d\\s+\\d\\d\\d\\d\\s+\\d\\d:\\d\\d:\\d\\d)\\s+.+?%(.+?):(\\s+.+?:\\s+)");
	private final static Pattern KEY_PATTERN = Pattern
			.compile("((.+?)=(.+?):\\s+)|((.+?)=(.+?))$");

	@Autowired
	private CommonProperties commonProperty;

	@Autowired
	private ConnectionProperties connProperty;

	public void parseFile(File file) throws SQLException {
		Scanner input = null;
		String inputLine = null;
		Matcher lineMatcher = null, keyMatcher = null;
		int end = 0;
		Map<String, String> keyValue = null;
		// EventDao eventDao = new EventDao();
		// Connection conn =
		// ConnectionFactory.getInstance(connProperty).getConnection(connProperty);
		String eventType = commonProperty.getEventType();
		List<String> eventTypeList = null;
		ParseData parseData = new ParseData(commonProperty);
		// int eventID = 0, fileID = 0;

		// int batchSize = 20, count = 0;
		/*
		 * try{ batchSize =
		 * Integer.parseInt(commonProperty.getCommitBatchSize());
		 * }catch(NumberFormatException e){
		 * System.out.println("Unable to parse commit batch size"); }
		 */

		if (!eventType.equalsIgnoreCase("all")) {
			eventTypeList = Arrays.asList(eventType.split(","));
		}

		System.out.println("Started Parsing:" + new Date());
		// System.out.println("Batch Size:"+ batchSize);
		try {

			// fileID = eventDao.insertFileDetail(conn, file.getName());
			input = new Scanner(file);
			// conn.setAutoCommit(false);
			while (input.hasNextLine()) {
				inputLine = input.nextLine();

				lineMatcher = LINE_PATTERN.matcher(inputLine);

				if (lineMatcher.lookingAt()) {
					// lineMatcher.group(4);
					// lineMatcher.group(2);

					// if(!eventType.equalsIgnoreCase("all") &&
					// !eventTypeList.contains(lineMatcher.group(4))){
					if (!eventType.equalsIgnoreCase("all")
							&& !eventTypeList.contains(lineMatcher.group(3))) {
						continue;
					}

					// eventID = eventDao.insertEvent(conn, fileID,
					// lineMatcher.group(4), lineMatcher.group(2));

					keyValue = new HashMap<String, String>();
					end = lineMatcher.end();

					keyMatcher = KEY_PATTERN.matcher(inputLine);
					while (keyMatcher.find(end)) {
						System.out.println("match found****");
						if (keyMatcher.group(2) != null
								&& keyMatcher.group(3) != null) {
							keyValue.put(keyMatcher.group(2),
									keyMatcher.group(3));
						} else {
							keyValue.put(keyMatcher.group(5),
									keyMatcher.group(6));
						}

						end = keyMatcher.end();
					}
					// ++count;
					// eventDao.insertEventData(conn, eventID, keyValue);
					// if(count % batchSize == 0){
					// System.out.println("Commit : " + count);
					// conn.commit();
					// }
					// System.out.println(keyValue);

					String exit = keyValue.get("New Exit");

					if (exit == null)
						exit = keyValue.get("Exit");

					for (String newExit : exit.split(",")) {
						if (newExit.contains("BR-IP")) {
							// System.out.println("newExit:" + newExit);
							String ipArray[] = newExit.split("=");
							if (ipArray.length > 1) {
								String ipAddress = ipArray[1].trim();
								System.out.println("Event/Alert Type:"
										+ lineMatcher.group(3));
								System.out.println("IP Address:" + ipAddress);
								parseData.get_device_obj_list(ipAddress);
								System.out.println("*********");
							}

						}
					}

				}
			}
			// conn.commit();
			// conn.setAutoCommit(true);

			// eventDao.updateFileStatus(conn, "PROCESSED", null, fileID);
			// System.out.println("Commit end: " + count);
			System.out.println("Parsing Completed:" + new Date());

		} catch (Exception e) {
			System.out.println("Error in parsing CLI File:" + e);
			e.printStackTrace();
			// eventDao.updateFileStatus(conn, "FAILED", e.getMessage(),
			// fileID);
		} finally {
			if (input != null)
				input.close();
			/*
			 * if(conn != null) try { conn.close(); } catch (SQLException e) {
			 * // TODO Auto-generated catch block e.printStackTrace(); }
			 */

		}
	}

	public void fileparser() {
		Scanner input = null;
		String inputLine = null;
		Matcher lineMatcher = null, keyMatcher = null;
		int end = 0;
		Map<String, String> keyValue = null;
		// EventDao eventDao = new EventDao();
		// Connection conn =
		// ConnectionFactory.getInstance(connProperty).getConnection(connProperty);
		String eventType = commonProperty.getEventType();
		List<String> eventTypeList = null;
		ParseData parseData = new ParseData(commonProperty);
		// int eventID = 0, fileID = 0;

		// int batchSize = 20, count = 0;
		/*
		 * try{ batchSize =
		 * Integer.parseInt(commonProperty.getCommitBatchSize());
		 * }catch(NumberFormatException e){
		 * System.out.println("Unable to parse commit batch size"); }
		 */

		if (!eventType.equalsIgnoreCase("all")) {
			eventTypeList = Arrays.asList(eventType.split(","));
		}

		System.out.println("Started Parsing:" + new Date());
		// System.out.println("Batch Size:"+ batchSize);
		try {

			// fileID = eventDao.insertFileDetail(conn, file.getName());
			FileReader fr = new FileReader(commonProperty.getMonitorFolder());
		    BufferedReader br = new BufferedReader(fr);

			//input = new Scanner(commonProperty.getMonitorFolder());
			boolean newLine = false;
			// conn.setAutoCommit(false);
			while (true) {
				inputLine = br.readLine();

				if (inputLine == null) {
					newLine = true;
					Thread.sleep(5 * 1000);
				}

				if (newLine && inputLine != null) {

					lineMatcher = LINE_PATTERN.matcher(inputLine);

					if (lineMatcher.lookingAt()) {
						// lineMatcher.group(4);
						// lineMatcher.group(2);

						// if(!eventType.equalsIgnoreCase("all") &&
						// !eventTypeList.contains(lineMatcher.group(4))){
						if (!eventType.equalsIgnoreCase("all")
								&& !eventTypeList
										.contains(lineMatcher.group(3))) {
							continue;
						}

						// eventID = eventDao.insertEvent(conn, fileID,
						// lineMatcher.group(4), lineMatcher.group(2));

						keyValue = new HashMap<String, String>();
						end = lineMatcher.end();

						keyMatcher = KEY_PATTERN.matcher(inputLine);
						while (keyMatcher.find(end)) {
							System.out.println("match found****");
							if (keyMatcher.group(2) != null
									&& keyMatcher.group(3) != null) {
								keyValue.put(keyMatcher.group(2),
										keyMatcher.group(3));
							} else {
								keyValue.put(keyMatcher.group(5),
										keyMatcher.group(6));
							}

							end = keyMatcher.end();
						}
						// ++count;
						// eventDao.insertEventData(conn, eventID, keyValue);
						// if(count % batchSize == 0){
						// System.out.println("Commit : " + count);
						// conn.commit();
						// }
						// System.out.println(keyValue);

						String exit = keyValue.get("New Exit");

						if (exit == null)
							exit = keyValue.get("Exit");

						for (String newExit : exit.split(",")) {
							if (newExit.contains("BR-IP")) {
								// System.out.println("newExit:" + newExit);
								String ipArray[] = newExit.split("=");
								if (ipArray.length > 1) {
									String ipAddress = ipArray[1].trim();
									System.out.println("Event/Alert Type:"
											+ lineMatcher.group(3));
									System.out.println("IP Address:"
											+ ipAddress);
									parseData.get_device_obj_list(ipAddress);
									System.out.println("*********");
								}

							}
						}

					}
				}
			}
			// conn.commit();
			// conn.setAutoCommit(true);

			// eventDao.updateFileStatus(conn, "PROCESSED", null, fileID);
			// System.out.println("Commit end: " + count);
			//System.out.println("Parsing Completed:" + new Date());

		} catch (Exception e) {
			System.out.println("Error in parsing CLI File:" + e);
			e.printStackTrace();
			// eventDao.updateFileStatus(conn, "FAILED", e.getMessage(),
			// fileID);
		} finally {
			if (input != null)
				input.close();
			/*
			 * if(conn != null) try { conn.close(); } catch (SQLException e) {
			 * // TODO Auto-generated catch block e.printStackTrace(); }
			 */

		}
	}

}
