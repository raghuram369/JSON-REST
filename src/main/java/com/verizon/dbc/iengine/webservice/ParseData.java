package com.verizon.dbc.iengine.webservice;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.verizon.dbc.iengine.config.CommonProperties;

public class ParseData {

	private String auth_token;

	private CommonProperties commonProperty;

	public ParseData(CommonProperties commonProperty) {
		this.commonProperty = commonProperty;
	}

	public void get_device_obj_list(String ipaddr) {
		// 
		Long deviceId, objectId, indId;
		String inputLine;
		JSONParser parser = new JSONParser();
		GetToken token = new GetToken(commonProperty);
		GetIndData ind_data = new GetIndData(commonProperty, token.get_token());
		String sevOneURL = commonProperty.getSevOneUrl();
		try {

			URL url = new URL(
					sevOneURL
							+ "/api/v1/devices/filter?size=20&includeObjects=true&includeIndicators=true&localOnly=true"); // URL
																															// to
																															// Parse
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			auth_token = token.get_token();
			conn.setRequestProperty("X-Auth-Token", auth_token);
			conn.setDoInput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/json");
			String input =

			"{\n" + "   \"ipAddress\":";
			input = input.concat("\"");
			input = input.concat(ipaddr);
			input = input.concat("\"");
			input = input.concat("}");
			// input = input.concat("\"");
			//System.out.println(input);
			OutputStream os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();
			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode());
			}
			BufferedReader in = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			while ((inputLine = in.readLine()) != null) {
				JSONObject content_obj = (JSONObject) parser.parse(inputLine);
				JSONArray content_arr = (JSONArray) content_obj.get("content");
				System.out.println(content_obj.toJSONString());
				System.out.println("\n\n");

				/*for (Object element : content_arr) {
					JSONObject device_obj = (JSONObject) element;
					JSONArray device_arr = (JSONArray) device_obj
							.get("objects");
					deviceId = (Long) device_obj.get("id");
					//System.out.println("device id is : " + deviceId);
					for (Object element1 : device_arr) {
						JSONObject object_obj = (JSONObject) element1;
						JSONArray ind_arr = (JSONArray) object_obj
								.get("indicators");
						objectId = (Long) object_obj.get("id");
						//System.out.println("object id is : " + objectId);
						for (Object element2 : ind_arr) {
							JSONObject ind_obj = (JSONObject) element2;
							indId = (Long) ind_obj.get("id");
							System.out.println("Indicator id is : " + indId);
							ind_data.get_ind_data(deviceId,objectId,indId);
							System.out.println("\n\n");
						}

					}

				}*/

			}
			in.close();
			conn.disconnect();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public String getAuth_token() {
		return auth_token;
	}

	public void setAuth_token(String auth_token) {
		this.auth_token = auth_token;
	}

}
