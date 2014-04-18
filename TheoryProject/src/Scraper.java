import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.TreeMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Scraper {
	public static void main(String[] args) {
		System.out.println("What article would you like? ");
		String line = new Scanner(System.in).nextLine();
		String targurl = findCorrectURL(line.trim());
		System.out.println("going for " + targurl);
		Document dc = getURL(targurl);
		String content = dc.select("text").first().text();
		System.out.println(getWords(content));
		List<String> wordList = getWords(content);

		// merging Map.java:
		TreeMap<String, Node> storage = new TreeMap<String, Node>();
		for (int i = 0; i < wordList.size(); i++) {
			Node n = new Node(wordList.get(i));
			if (!storage.containsKey(wordList.get(i))) {
				storage.put(wordList.get(i), n);
			} else {
				// 2. If a word repeats, update the frequency field in the word
				storage.get(wordList.get(i)).incFreq();
			}
		}
		// Map should be populated with each word as a key
		System.out.println(storage.keySet());
		// 3. Add related words based on thesaurus
		String url = "http://www.thesaurus.com/browse/";
		for (Entry<String, Node> entry : storage.entrySet()) {
			String key = entry.getKey();
			// Node k = new Node(key);
			// System.out.println(key);
			String url2 = url + key;
			// Some assistance from Greg Colella here
			Document doc;
			Elements div = null;
			try {
				doc = Jsoup
						.connect(url2)
						.userAgent(
								"Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
						.referrer("http://www.google.com").get();
				div = doc.select("div.synonyms span.text");// .select("span");
			} catch (IOException e1) {
				System.out.println("Could not access url for synonyms");
				e1.printStackTrace();
			}
			if (div != null) {
				for (Element e : div) {
					String thiselement = e.text();
					if (storage.containsKey(thiselement)) {
						storage.get(thiselement).connectNode(entry.getValue());
						System.out.println("Connected " + entry.getValue()
								+ " to synonym: " + thiselement);
					}
				}
			}
			System.out.println();
			System.out
					.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n\n");
		}
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
		return raw; // raw.replaceAll("[^\\w\\.\\s\\-\\'']", "").trim();
	}

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