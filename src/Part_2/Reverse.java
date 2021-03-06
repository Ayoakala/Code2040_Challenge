package Part_2;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Reverse {

	public static void main(String[] args) {

		// This builds the request with the payload to send to the web endpoint
		final String reverse = "http://challenge.code2040.org/api/reverse";
		final String token = "\"b47c051cdd77da27b1d91635927f82f8\"";
		final String git = "\"https://github.com/Ayoakala/Code2040_Challenge\"";
		final String validate = "http://challenge.code2040.org/api/reverse/validate";

		String payload_1 = "{\"token\":" + token + ",\"github\":" + git + "}";

		String reverseThis = sendPostRequest(reverse, payload_1);

		System.out.println(reverseThis);
		// this puts all the characters into an array and allows me to perform
		// some manipulation
		String[] toArray = reverseThis.split("");
		String converted = "\"";
		for (int x = toArray.length - 1; x >= 0; x--) {
			converted += toArray[x];
		}

		// you need to include the quotations in order for the payload to be
		// to send to the web endpoint
		converted += "\"";
		System.out.println(converted);

		String payload_2 = "{\"token\":" + token + ",\"string\":" + converted + "}";

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
