
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
public class test {
	public static void main(String args[])
	{
		JSONParser parser = new JSONParser();
		
		ArrayList<tuplestring> array = new ArrayList<tuplestring>();
		try
		{
        JSONObject obj = (JSONObject) parser.parse(new FileReader("C:/Users/pullavi/workspace/SevOne/src/devices.json"));
        JSONArray arr  = (JSONArray)obj.get("content"); 
        for(Object o : arr)
        {
        tuplestring tuple = new  tuplestring();
        JSONObject jsonObject =  (JSONObject) o;

        String name = (String) jsonObject.get("name");
        tuple.setName(name);
        //System.out.println(name);

        String ipAddress = (String) jsonObject.get("ipAddress");
        tuple.setIpaddr(ipAddress);
        //System.out.println(ipAddress);
        array.add(tuple);
        }
        Iterator<tuplestring> itr = array.iterator();
        while(itr.hasNext())
        {
        	tuplestring element = (tuplestring) itr.next();       	
        	System.out.println(element.getName());
        	System.out.println(element.getIpaddr());
        }
		} catch (FileNotFoundException e) {
			e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	} catch (ParseException e) {
		e.printStackTrace();
	}
	}
	
}
                 	
	