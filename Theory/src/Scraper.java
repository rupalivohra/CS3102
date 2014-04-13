import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.TreeMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Scraper {

	private final String USER_AGENT = "Mozilla/5.0";
	private static String textfile;

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
		
		//Rupali's merged ness:
		//TreeMap<String, Word> storage = new TreeMap<String,Word>();
		TreeMap<String, Node> storage = new TreeMap<String, Node>();
		File file = new File(textfile);
		try {
			//1. Read in a text file; store each unique word in a map
			Scanner scanner = new Scanner(file);
			scanner.useDelimiter(",");
			while (scanner.hasNext()) {
				String word = scanner.next();
				Node n = new Node(word);
				if (!storage.containsKey(word)) {
					storage.put(word, n);
				} else {
					//2. If a word repeats, update the frequency field in the word
					storage.get(word).incFreq();
				}
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("Error trying to open the file with the scanner.");
		}
		//Map should be populated with each word as a key
		System.out.println(storage.keySet());
		//3. Add related words based on thesaurus
		String url = "http://www.thesaurus.com/browse/";
		for (Entry<String, Node> entry: storage.entrySet()) {
			String key = entry.getKey();
			Node k = new Node(key);
//			System.out.println(key);
			
			String url2 = url+key;
			//Some assistance from Greg Colella here
			Document doc = Jsoup.connect(url2)
					 .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
				      .referrer("http://www.google.com")
				      .get();
			Elements div = doc.select("div.synonyms span.text");//.select("span");
			for(Element e : div){
				String thiselement = e.text();
				Node t = new Node(thiselement);
				if(storage.containsKey(thiselement)){
					k.connectNode(t);
					//storage.get(thiselement);
				}
			}
		}
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n\n");
		for (Entry<String,Node> entry: storage.entrySet()) {
			System.out.println("node: " + entry.getKey() + ";degree: " + entry.getValue().getDegree() + ";frequency: " + entry.getValue().getFreq());
		}
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

		textfile = firstWord.replace(" ", "_") + ".txt";
		Writer writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(textfile),
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