import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class Scraper {

	private final String USER_AGENT = "Mozilla/5.0";

	public static void main(String[] args) throws Exception {
		Scraper http = new Scraper();
		Scanner keyboard = new Scanner(System.in);
		System.out.println("Testing 1 - Send Http GET request");

		System.out.print("Enter your wikipedia article title: ");
		String article = keyboard.nextLine();
		if (article.contains(" "))
			article = article.replace(" ", "_");
		http.sendGet(article);
		keyboard.close();
	}

	// HTTP GET request
	private String sendGet(String articleTitle) throws Exception {
		String notUnderScore = articleTitle.replace("_", " ");

		String[] token = notUnderScore.split(" ");
		for (int i = 0; i < token.length; i++) {
			char[] digit = token[i].toCharArray();
			digit[0] = Character.toUpperCase(digit[0]);
			token[i] = new String(digit);
		}

		String firstWord = "";
		for (int i = 0; i < token.length; ++i) {
			if (i == token.length - 1)
				firstWord = firstWord + token[i];
			else {
				firstWord = firstWord + token[i] + " ";
			}
		}

		String url = "http://en.wikipedia.org/wiki/Special:Export/"
				+ firstWord.replace(" ", "_");
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("GET");
		con.setRequestProperty("User-Agent", USER_AGENT);

		BufferedReader in = new BufferedReader(new InputStreamReader(
				con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		Writer writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(firstWord.replace(" ", "_") + ".txt"),
				"utf-8"));

		boolean start = false;
		while ((inputLine = in.readLine()) != null) {
			if (inputLine.startsWith("'''" + firstWord + "'''"))
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