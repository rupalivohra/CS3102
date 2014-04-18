<<<<<<< HEAD
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
=======
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
>>>>>>> 12bc26c5ac338fe7523c563521d6586a9a121305
import java.util.Scanner;
import java.util.TreeMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

<<<<<<< HEAD
public class Scraper {

	private final String USER_AGENT = "Mozilla/5.0";
	private static String textfile;
=======
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
>>>>>>> 12bc26c5ac338fe7523c563521d6586a9a121305

public class Scraper {

<<<<<<< HEAD
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
			Document doc = Jsoup.connect(url2) //something might be causing aproblem with this expression; the error occurred at line 73
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
=======
	public static void main(String[] args) {
		System.out.println("What article would you like? ");
		String line = new Scanner(System.in).nextLine();
		String targurl = findCorrectURL(line.trim());
		System.out.println("going for " + targurl);
		Document dc = getURL(targurl);
		String content = dc.select("text").first().text();
		System.out.println(getWords(content));
		List<String> wordsList = getWords(content);
>>>>>>> 12bc26c5ac338fe7523c563521d6586a9a121305
	}

	public static List<String> getWords(String raw) {
		Scanner scan = new Scanner(raw);
		scan.useDelimiter("[\\* {\\[\\|,\"]+");

		ArrayList<String> lst = new ArrayList<String>();
		while (scan.hasNext()) {
			String list = scan.next();
			if (list.startsWith("'''"))
				lst.add(list.substring(3));
			else if (list.endsWith("'''"))
				lst.add(list.substring(0, list.length() - 4));
			else if (list.startsWith("''"))
				lst.add(list.substring(2));
			else if (list.endsWith("''"))
				lst.add(list.substring(0, list.length() - 3));
			else if (list.startsWith("'"))
				lst.add(list.substring(1));
			else if (list.endsWith("'"))
				lst.add(list.substring(0, list.length() - 2));
			else
				lst.add(list);
		}

		ArrayList<String> bad = new ArrayList<String>();
		bad.add("Link");
		for (String s : lst) {
			for (int i = 0; i < s.length(); i++) {
				if (!"abcdefghijklmnopqrstuvwxyz'\"".contains(s.substring(i,
						i + 1).toLowerCase())) {
					bad.add(s);
				}
			}
		}
		lst.removeAll(bad);

		return lst;
	}

	public static String stripCrap(String raw) {
		raw = raw.replaceAll(" *[ ]", " ");

<<<<<<< HEAD
		textfile = firstWord.replace(" ", "_") + ".txt";
		Writer writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(textfile),
				"utf-8"));
=======
		return raw; // raw.replaceAll("[^\\w\\.\\s\\-\\'']", "").trim();
	}
>>>>>>> 12bc26c5ac338fe7523c563521d6586a9a121305

	public static Document getURL(String url) {
		try {
			return Jsoup.connect(url).userAgent("Mozilla/5.0")
					.referrer("www.google.com").get();
		} catch (IOException e) {

			e.printStackTrace();
			return null;
		}
	}

	public static String followRedirects(String originalURL) {
		Document doc = getURL(originalURL);
		Elements el = doc.select("redirect");
		if (el.size() == 0) {
			return originalURL;
		}
		Element e = el.first();
		String title = e.attr("title");
		System.out.println("Redirected to: " + title);
		return firstGuessURL(title);
	}

	public static String firstGuessURL(String articletitle) {
		return "http://en.wikipedia.org/wiki/Special:Export/"
				+ URLEncoder.encode(articletitle.replace(" ", "_"));
	}

	public static String findCorrectURL(String articletitle) {
		return followRedirects(firstGuessURL(articletitle));
	}
}