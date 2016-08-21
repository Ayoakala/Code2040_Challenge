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

	public static ArrayList<String> getHaystack(JSONObject response) throws JSONException {
		ArrayList<String> haystack = new ArrayList<String>();
		JSONArray respArray = response.getJSONArray("haystack");

		for (int i = 0; i < respArray.length(); i++) {
			haystack.add(respArray.getString(i));
		}
		return haystack;
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
