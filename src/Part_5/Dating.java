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

	/*
	 * This Part took me the most time! I had to look through what an ISO 8601
	 * (https://en.wikipedia.org/wiki/ISO_8601) which is pretty widely known.
	 * Although i couldn't make add the seconds with my own knowledge i did make
	 * use of Another API. Its called Joda-Time (http://www.joda.org/joda-time/)
	 * and its pretty powerful one too it can basically convert any commonly
	 * used date syntax, it can also add/subtract intervals of time to a time
	 * (the method I used)
	 */
	public static void main(String[] args) throws JSONException {
		final String url = "http://challenge.code2040.org/api/dating";
		final String token = "\"b47c051cdd77da27b1d91635927f82f8\"";
		final String git = "\"https://github.com/Ayoakala/Code2040_Challenge\"";
		final String validate = "http://challenge.code2040.org/api/dating/validate";

		String payload_1 = "{\"token\":" + token + ",\"github\":" + git + "}";

		JSONObject response = new JSONObject(sendPostRequest(url, payload_1));
		// get the JSON variable with the name "datestamp"
		String isoTime = response.getString("datestamp");

		// System.out.println(isoTime);

		int interval = Integer.parseInt(response.getString("interval"));

		String convTime = DateTime.parse(isoTime).plusSeconds(interval).toString();
		// there are many variations to ISO 8601 which adds more indexes to the
		// date but we where required to send the response back in a particular
		// format so i substringened all the extra contents out and added things
		// for it to be valid
		convTime = "\"" + convTime.substring(0, 19) + "Z\"";
		String payload_2 = "{\"token\":" + token + ",\"datestamp\":" + convTime + "}";

		System.out.println(sendPostRequest(validate, payload_2));

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
