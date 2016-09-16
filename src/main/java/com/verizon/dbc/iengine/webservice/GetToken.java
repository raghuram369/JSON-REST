package com.verizon.dbc.iengine.webservice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.verizon.dbc.iengine.config.CommonProperties;
public class GetToken {
	
	private CommonProperties commonProperty;
	
	public GetToken(CommonProperties commonProperty){
		this.commonProperty = commonProperty;
	}
	
	
	public String get_token() {
		OutputStream os = null;
		BufferedReader in = null;
		String token_resp = null;
		try {
			URL url = new URL(commonProperty.getSevOneUrl()+ "/api/v1/authentication/signin");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			String input = "{\n" + "           \"name\": \"admin\",\n"
					+ "           \"password\": \"SevOne\"\n" + "}";
			// System.out.println(input);
			os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();
			String inputLine;
			in = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			while ((inputLine = in.readLine()) != null) {
				JSONParser parser = new JSONParser();
				JSONObject token_obj = (JSONObject) parser.parse(inputLine);
				token_resp = (String) token_obj.get("token");
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} finally {
			if (os != null)
				try {
					os.close();

					if (in != null)
						in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}

		return token_resp;
	}

}
