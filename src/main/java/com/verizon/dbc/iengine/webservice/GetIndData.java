package com.verizon.dbc.iengine.webservice;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.net.HttpURLConnection;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.verizon.dbc.iengine.config.CommonProperties;

public class GetIndData {
	
	private CommonProperties commonProperty;
	
	private String token;

	public GetIndData(CommonProperties commonProperty, String token) {
		this.commonProperty = commonProperty;
		this.token = token;
	}
	
	public void get_ind_data(Long devid, Long objid, Long indid) {
		try {
			// ParseData token = new ParseData();
			Long focus;
			String inputLine;
			URL url = new URL(commonProperty.getSevOneUrl() + "/api/v1/devices/"
							+ devid
							+ "/objects/"
							+ objid
							+ "/indicator/"
							+ indid
							+ "/data?startTime=1473307200000000000&endTime=1473657015262188398");
			
			System.out.println("url:" + url.getHost() + url.getPort());
			
			
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			// System.out.println("http://19.19.19.111:80/api/v1/devices/"+devid+"/objects/"+objid+"/indicator/"+indid+"/data?startTime=1473307200000000000&endTime=1473657015262188398");
			conn.setRequestProperty("X-Auth-Token",token);
			conn.setDoInput(true);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/json");

			JSONParser parser = new JSONParser();
			// GetToken token = new GetToken();
			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode());
			}
			BufferedReader in = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			while ((inputLine = in.readLine()) != null) {
				JSONObject content_obj = (JSONObject) parser.parse(inputLine);
				System.out.println(content_obj.toJSONString());
				// JSONArray content_arr = (JSONArray)
				// content_obj.get("content");
				focus = (Long) content_obj.get("focus");
			}
			in.close();

			conn.disconnect();
		} catch (Exception e) {
			System.out.println("Error in getting performance indicator:" + e);
		}

	}

}
