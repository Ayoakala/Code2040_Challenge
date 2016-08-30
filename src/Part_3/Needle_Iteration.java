package Part_3;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Needle_Iteration {

	public static void main(String[] args) throws JSONException {
		final String url = "http://challenge.code2040.org/api/haystack";
		final String token = "\"b47c051cdd77da27b1d91635927f82f8\"";
		final String git = "\"https://github.com/Ayoakala/Code2040_Challenge\"";
		final String validate = "http://challenge.code2040.org/api/haystack/validate";
		String payload_1 = "{\"token\":" + token + ",\"github\":" + git + "}";

		JSONObject response = new JSONObject(sendPostRequest(url, payload_1));
		int index = -1;
		String indexToString = "\"";

		String needle = response.getString("needle");
		ArrayList<String> haystack = getHaystack(response);

		// simple for loop that searches for where the needle is in the array
		// and get the index
		for (int i = 0; i < haystack.size(); i++) {
			if (needle.equals(haystack.get(i))) {
				index = i;
				break;
			}
		}
		indexToString += index + "\"";
		String payload_2 = "{\"token\":" + token + ",\"needle\":" + indexToString + "}";
		System.out.println(sendPostRequest(validate, payload_2));
	}

	/*
	 * I tried something a little different here. I realized that I get a JSON
	 * Object back by sending a payload to the endpoint and I thought that there
	 * should be a better way to parse the information since JSON is pretty
	 * universal. I used:
	 * https://processing.org/reference/JSONObject_getJSONArray_.html This
	 * parses out the variable given by the JSON response and since I get a JSON
	 * array back there is also a call for this in java
	 */
	public static ArrayList<String> getHaystack(JSONObject response) throws JSONException {
		ArrayList<String> haystack = new ArrayList<String>();
		JSONArray respArray = response.getJSONArray("haystack");

		for (int i = 0; i < respArray.length(); i++) {
			haystack.add(respArray.getString(i));
		}
		return haystack;
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
