package Part_5;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;

public class Dating {

	
	public static void main(String[] args) throws JSONException{
		final String url = "http://challenge.code2040.org/api/dating";
		final String token = "\"b47c051cdd77da27b1d91635927f82f8\"";
		final String git = "\"https://github.com/Ayoakala/Code2040_Challenge\"";
		final String validate = "http://challenge.code2040.org/api/dating/validate";

		String payload_1="{\"token\":"+token+",\"github\":"+git+"}";	

		JSONObject response = new JSONObject(sendPostRequest(url,  payload_1));
		
		String isoTime = response.getString("datestamp");
		int interval = Integer.parseInt(response.getString("interval"));

		String convTime = DateTime.parse(isoTime).plusSeconds(interval).toString();
		convTime = "\"" + convTime.substring(0, 19) + "Z\"";
		String payload_2="{\"token\":"+token+",\"datestamp\":"+convTime+"}";	

		System.out.println(sendPostRequest(validate,  payload_2));
		
	}
	
	public static String sendPostRequest(String requestUrl, String payload) {
	    try {
	        URL url = new URL(requestUrl);
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

	        connection.setDoInput(true);
	        connection.setDoOutput(true);
	        connection.setRequestMethod("POST");
	        connection.setRequestProperty("Accept", "application/json");
	        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
	        OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
	        writer.write(payload);
	        writer.close();
	        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	        StringBuffer jsonString = new StringBuffer();
	        String line;
	        while ((line = br.readLine()) != null) {
	                jsonString.append(line);
	        }
	        br.close();
	        connection.disconnect();
	        return jsonString.toString();
	    } catch (Exception e) {
	            throw new RuntimeException(e.getMessage());
	    }
	    
	}
}
