package Part_4;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Prefix {

	
	public static void main(String[] args) throws JSONException {
		final String token = "\"b47c051cdd77da27b1d91635927f82f8\"";
		final String url = "http://challenge.code2040.org/api/prefix";
		final String git = "\"https://github.com/Ayoakala/Code2040_Challenge\"";
		final String validate = "http://challenge.code2040.org/api/prefix/validate";

		String payload_1 = "{\"token\":" + token + ",\"github\":" + git + "}";

		JSONObject response = new JSONObject(sendPostRequest(url, payload_1));
		
		// gets the JSON variable with the name "prefix"
		String prefix = response.getString("prefix");
		// System.out.println(prefix);
		ArrayList<String> prefixArray = getArray(response);

		// System.out.println();
		for (int i = 0; i < prefixArray.size(); i++) {
			if (prefixArray.get(i).contains(prefix)) {
				prefixArray.remove(i);
				i = i - 1;
			}
		}

		JSONArray reply = new JSONArray(prefixArray);
		String payload_2 = "{\"token\":" + token + ",\"array\":" + reply + "}";

		System.out.println(sendPostRequest(validate, payload_2));

	}

	/*
	 * I used the same logic in my Part 3 (Needle_Iteration) to make use of the
	 * JSON object properties
	 */
	public static ArrayList<String> getArray(JSONObject response) throws JSONException {
		ArrayList<String> array = new ArrayList<String>();
		JSONArray respArray = response.getJSONArray("array");

		for (int i = 0; i < respArray.length(); i++) {
			array.add(respArray.getString(i));
		}
		return array;
	}

	/*
	 * This method opens up a network to connect with the web end point for the
	 * API so that we can send request and receive JSON objects back syntax
	 * gotten from:
	 * http://stackoverflow.com/questions/15570656/how-to-send-request-payload-
	 * to-rest-api-in-java and:
	 * http://programmers.stackexchange.com/questions/158603/what-does-the-term-
	 * payload-mean-in-programming
	 */
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
