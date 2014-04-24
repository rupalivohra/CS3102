import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
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
		ArrayList<Node> clo = new ArrayList<Node>();
		for (Node n : storage.values()) {
			if (!n.getWord().equals(""))
				clo.add(n);
		}

		Collections.sort(clo);

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
						.referrer("http://www.google.com").timeout(0).get();
				div = doc.select("div.synonyms span.text");// .select("span");
			} catch (IOException e1) {
				System.out.println("Could not access url for synonyms");
				e1.printStackTrace();
			}
			if (div != null) {
				for (Element e : div) {
					String thiselement = e.text();
					// if both the synonym and the original word are in the
					// article
					if (storage.containsKey(thiselement)) {
						storage.get(key).connectNode(storage.get(thiselement));
					}
				}
			}
			System.out.println();
			System.out
					.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n\n");
		}

		for (Entry<String, Node> entry : storage.entrySet()) {
			if (entry.getValue().getConnected().size() > 0) {
				System.out.print("Connected: " + entry.getValue() + "to: ");
				System.out.println(entry.getValue().getConnected());
			}
		}
	}

	public static List<String> getWords(String raw) {
		Scanner scan = new Scanner(raw);
		scan.useDelimiter("[\\* {\\[\\|,\"]+");

		ArrayList<String> lst = new ArrayList<String>();
		while (scan.hasNext()) {
			String list = scan.next();
			if (!list.equals("")) {
				while (list.startsWith("'")) {
					list = list.substring(1);
				}
				while (list.endsWith("'")) {
					list = list.substring(0, list.length() - 2);
				}
				if (list.endsWith("'s")) {
					list = list.replace("'s", "");
				}
				lst.add(list);
			}
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
		Scanner s;
		ArrayList<String> commonWords = new ArrayList<String>();
		try {
			s = new Scanner(new File("commonWords.txt"));
			while (s.hasNext()) {
				commonWords.add(s.next());
			}
			s.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		for (int i = 0; i < commonWords.size(); ++i) {
			if (lst.contains(commonWords.get(i))) {
				lst.remove(commonWords.get(i));
				i--;
			}
		}

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
		try {
			return "http://en.wikipedia.org/wiki/Special:Export/"
					+ URLEncoder
							.encode(articletitle.replace(" ", "_"), "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String findCorrectURL(String articletitle) {
		return followRedirects(firstGuessURL(articletitle));
	}
}