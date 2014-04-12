import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;

public class Scraper {

	private final String USER_AGENT = "Mozilla/5.0";

	public static void main(String[] args) throws Exception {
		Scraper http = new Scraper();
		System.out.println("Testing 1 - Send Http GET request");

		http.sendGet("water");

	}

	// HTTP GET request
	private String sendGet(String articleTitle) throws Exception {
		String url = "http://en.wikipedia.org/wiki/Special:Export/"
				+ articleTitle;
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("GET");
		con.setRequestProperty("User-Agent", USER_AGENT);

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(new InputStreamReader(
				con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		Writer writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(articleTitle + ".txt"), "utf-8"));

		boolean start = false;
		while ((inputLine = in.readLine()) != null) {
			if (inputLine.startsWith("'''"
					+ articleTitle.substring(0, 1).toUpperCase()
					+ articleTitle.substring(1) + "'''"))
				start = true;

			inputLine = inputLine.replaceAll("[^\\w\\s\\_]", "");
			inputLine = inputLine.replaceAll("-", "").trim();

			if (start) {
				if (!inputLine.contains("References")) {
					inputLine = inputLine.replaceAll("\\s", ",");
					writer.write(inputLine);
				} else {
					break;
				}
			}
		}
		in.close();
		writer.close();

		return response.toString();
	}
}