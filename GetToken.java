import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;

public class GetToken {
	     public static String token_resp;
	 	 public String get_token() {

		  try {

			URL url = new URL("http://sevdemo.sevone.com/api/v1/authentication/signin");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");			
			String input = 
	"{\n"+ 	
	"           \"name\": \"admin\",\n" +
	"           \"password\": \"SevOne\"\n" +
	"}";
	        System.out.println(input);
			OutputStream os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();
			String inputLine;
			JSONParser parser    = new JSONParser();
	        BufferedReader in    = new BufferedReader(new InputStreamReader(conn.getInputStream()));            
			while ((inputLine = in.readLine()) != null) {
				
				JSONObject token_obj = (JSONObject) parser.parse(inputLine);
				token_resp = (String) token_obj.get("token");
				}
		  	} catch (MalformedURLException e) {
		  			e.printStackTrace();
		  } catch (IOException e) {
                  e.printStackTrace();
		 } catch (ParseException e) {
		         e.printStackTrace();
		}
	        
		 return token_resp; 
	 }
	 	
}
