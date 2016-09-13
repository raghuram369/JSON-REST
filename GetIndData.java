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

public class GetIndData  {
        public static void get_ind_data(Long devid,Long objid,Long indid)
        { 
               try
               {
                ParseData token = new ParseData();
                Long focus;
                String inputLine;
                URL url      = new URL("http://19.19.19.111:80/api/v1/devices/"+devid+"/objects/"+objid+"/indicator/"+indid+"/data?startTime=1473307200000000000&endTime=1473657015262188398");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                //System.out.println("http://19.19.19.111:80/api/v1/devices/"+devid+"/objects/"+objid+"/indicator/"+indid+"/data?startTime=1473307200000000000&endTime=1473657015262188398");
                conn.setRequestProperty("X-Auth-Token",token.auth_token);
                conn.setDoInput(true);
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-Type","application/json");
                conn.setRequestProperty("Accept","application/json");

                JSONParser parser = new JSONParser();
                //GetToken token    = new GetToken();
                         if (conn.getResponseCode() != 200) {
                                throw new RuntimeException("Failed : HTTP error code : "+ conn.getResponseCode());
                        }
                        BufferedReader in  = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        while ((inputLine = in.readLine()) != null) {
                                JSONObject content_obj    =   (JSONObject) parser.parse(inputLine);
                                //JSONArray  content_arr  =   (JSONArray) content_obj.get("content");
                                 focus                    =   (Long) content_obj.get("focus");
                                 System.out.println("Focus:"+focus);
                                        

                                

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

}

