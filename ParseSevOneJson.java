
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

public class ParseSevOneJson  {
    public static String auth_token;
	public static ArrayList<Long> get_indicator_list(ArrayList<Tuple> tuples)
	{
		JSONParser parser = new JSONParser();
		ArrayList<Long> indicators_list = new ArrayList<Long>();
		Long deviceID,objectID,indicator_id;
		String inputLine;
		
		for(Object obj : tuples)
		{
			Tuple object = (Tuple)obj;
			deviceID     = object.getObjectID();
			objectID     = object.getObjectID();
			try {
					URL url                = new URL("https://sevdemo.sevone.com/api/v1/devices/"+deviceID+"/objects/"+objectID+"/indicators?size=20"); // URL to Parse
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setDoOutput(true);
					conn.setRequestMethod("GET");
					conn.setRequestProperty("Content-Type", "application/json");
					conn.setRequestProperty("X-Auth-Token",auth_token);  //Need to add this line , get token.
					if (conn.getResponseCode() != 200) {
						throw new RuntimeException("Failed : HTTP error code : "+ conn.getResponseCode());
					}
					
					BufferedReader in  = new BufferedReader(new InputStreamReader(conn.getInputStream()));            
						while ((inputLine = in.readLine()) != null) {
							JSONObject content_obj = (JSONObject) parser.parse(inputLine);
							JSONArray content_arr  = (JSONArray) content_obj.get("content");
							for (Object contentObj : content_arr) {
								JSONObject object_arr = (JSONObject) contentObj;
								indicator_id          = (Long)object_arr.get("id");
								indicators_list.add(indicator_id);
							}
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
				return indicators_list;
	}

	public static ArrayList<Tuple> get_device_obj_list(String ipaddr)
	{
		ArrayList<Tuple> tuple_list =  new ArrayList<Tuple>();
		Long deviceId , objectId;
		String inputLine;
		JSONParser parser = new JSONParser();
		GetToken token    = new GetToken();
		try {          
				URL url                = new URL("https://sevdemo.sevone.com/api/v1/devices"); // URL to Parse
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setDoOutput(true);
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Content-Type", "application/json");
				auth_token = token.get_token();
				String input = 

"{\n"+ 	
		"\"ipAddress\":" + ipaddr +
"}";
				conn.setRequestProperty("X-Auth-Token",auth_token); 
				OutputStream os = conn.getOutputStream();
				os.write(input.getBytes());
				os.flush();				
				
				if (conn.getResponseCode() != 200) {
					throw new RuntimeException("Failed : HTTP error code : "+ conn.getResponseCode());
				}
				
				BufferedReader in  = new BufferedReader(new InputStreamReader(conn.getInputStream()));            
					while ((inputLine = in.readLine()) != null) {
						JSONObject content_obj    =   (JSONObject) parser.parse(inputLine);
						JSONArray  content_arr    =   (JSONArray) content_obj.get("content");
							for (Object contentObj : content_arr) {
								JSONArray object_arr = (JSONArray) contentObj;
									for(Object element : object_arr){
										JSONObject object_obj = (JSONObject) element;
										Tuple tuple           =  new Tuple();
										deviceId              = (Long) object_obj.get("deviceID");
										objectId              = (Long) object_obj.get("id");
										tuple.setDeviceID(deviceId);
										tuple.setObjectID(objectId);
										tuple_list.add(tuple);          			
									}
							}
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
			return tuple_list;
		}
	
	public static void main(String[] args)
	{
		//ArrayList<Tuple> dev_obj_id_tuple = get_device_obj_list();
		//ArrayList<Long> indicators_list = get_indicator_list(dev_obj_id_tuple);	
	}
}
