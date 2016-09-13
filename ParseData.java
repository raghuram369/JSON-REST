
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

public class ParseData  {
        public static String auth_token;
        public static void get_device_obj_list(String ipaddr)
        {
                GetIndData ind_data = new GetIndData(); 
                Long deviceId,objectId,indId;
                String inputLine;
                JSONParser parser = new JSONParser();
                GetToken token    = new GetToken();
                try {
                        URL url                = new URL("http://19.19.19.111:80/api/v1/devices/filter?size=20&includeObjects=true&includeIndicators=true&localOnly=true"); // URL to Parse
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setDoOutput(true);
                        auth_token = token.get_token(); 
                        conn.setRequestProperty("X-Auth-Token",auth_token);
                        conn.setDoInput(true);
                        conn.setRequestMethod("POST");
                        conn.setRequestProperty("Content-Type","application/json");
                        conn.setRequestProperty("Accept","application/json");
                        String input =

                                "{\n"+
                                  "   \"name\":" ;
                        input = input.concat("\"");
                        input = input.concat(ipaddr);
                        input = input.concat("\"");
                        input = input.concat("}\"");
                        //input = input.concat("\"");
                        System.out.println(input);
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
                               
                                     for(Object element : content_arr){
                                                JSONObject device_obj    =   (JSONObject) element;
                                                JSONArray  device_arr    =   (JSONArray)  device_obj.get("objects");
                                                deviceId                 =   (Long) device_obj.get("id");
                                                System.out.println("device id is : " + deviceId);
                                                for(Object element1 : device_arr){
                                                	JSONObject object_obj =    (JSONObject) element1;
                                                	JSONArray  ind_arr    =    (JSONArray)  object_obj.get("indicators");
                                                	objectId              =    (Long) object_obj.get("id");
                                                	System.out.println("object id is : " + objectId);
                                                	for(Object element2 : ind_arr){
                                                		JSONObject ind_obj        =    (JSONObject) element2;
                                                    	        indId                     =    (Long) ind_obj.get("id");
                                                    	System.out.println("Indicator id is : " + indId);
                                                        ind_data.get_ind_data(deviceId,objectId,indId);
                                                	}
                                                	
                                                }
                                                	
                                        }
                       
                        }
                        in.close();
                        conn.disconnect();
                } catch (FileNotFoundException e) {
                        e.printStackTrace();
                } catch (IOException e) {
                        e.printStackTrace();
                }
                catch (ParseException e) {
                        e.printStackTrace();
                }
        }

        public static void main(String[] args)
        {
                 get_device_obj_list("11_MCBD");
                 // get_device_obj_list("DHUBMC");
                 //get_device_obj_list("TRHUBMC");
                 //get_device_obj_list("BRHUB1");
                 //get_device_obj_list("BRHUB2");
                 //get_device_obj_list("BRTRHUB1");
                 //get_device_obj_list("BRTRHUB2");
                 //get_device_obj_list("SITEMCBR");
        }
}


