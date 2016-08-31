package Part_3;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Needle {

	public static void main(String[] args) {
		final String url = "http://challenge.code2040.org/api/haystack";
		final String token = "\"b47c051cdd77da27b1d91635927f82f8\"";
		final String git = "\"https://github.com/Ayoakala/Code2040_Challenge\"";
		final String validate = "http://challenge.code2040.org/api/haystack/validate";

		String payload_1 = "{\"token\":" + token + ",\"github\":" + git + "}";

		String JsonArray = sendPostRequest(url, payload_1);
		System.out.println(JsonArray);

		// for my first approach to the challenge i decided to remove all the
		// JSON syntax from the response so that i can load all the different
		// indexes into an array although another person may not immediately
		// understand what i was doing
		String[] toArray = JsonArray.split(":|\\,+|\\{|\\}+|\\[|\\]");

		// I also had to create another array but when i did that really wanted
		// to iterate on this because i was essentially making use of more
		// memory than needed and also its probably less readable because others
		// may not understand why i was setting up my array like this
		String findThis = toArray[2];
		int index = -1;
		String indexToString = "\"";
		for (int x = 5; x < toArray.length; x++) {
			if (toArray[x].equals(findThis)) {
				index = x - 5;
			}
		}
		indexToString += index + "\"";
		System.out.println(indexToString);
		String payload_2 = "{\"token\":" + token + ",\"needle\":" + indexToString + "}";

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