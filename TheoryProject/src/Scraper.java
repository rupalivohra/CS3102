import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
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
		// System.out.println(getWords(content));
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
		ArrayList<Node> cloMore = new ArrayList<Node>();
		for (Node n : storage.values()) {
			if (!n.getWord().equals("") && n.getWord().length() > 1)
				cloMore.add(n);
		}

		Collections.sort(cloMore);

		ArrayList<Node> mid;
		System.out.println("size before cut: " + cloMore.size());

		if (cloMore.size() > 100) {
			mid = new ArrayList<Node>(cloMore.subList(0, 100));
		} else {
			mid = cloMore;
		}
		System.out.println("size after cut: " + mid.size());

		// Map should be populated with each word as a key
		// System.out.println(storage.keySet());
		// 3. Add related words based on thesaurus
		ArrayList<Node> relevant = processRelevancy(mid, storage);
		for (int i = 0; i < relevant.size(); ++i) {
			if (!mid.contains(relevant.get(i)))
				mid.add(relevant.get(i));
		}
		System.out.println("size after joining relevant: " + mid.size());
		Collections.sort(mid);
		// processRelevancy(clo, storage);
		ArrayList<Node> clo;
		if (mid.size() > 200) {
			clo = new ArrayList<Node>(mid.subList(0, 200));
		} else {
			clo = mid;
		}

		for (int i = 0; i < clo.size(); ++i) {
			int number = clo.size() / 10;
			if (i < number) {
				clo.get(i).setFontSize(57);
			} else if (i < 2 * number) {
				clo.get(i).setFontSize(52);
			} else if (i < 3 * number) {
				clo.get(i).setFontSize(47);
			} else if (i < 4 * number) {
				clo.get(i).setFontSize(42);
			} else if (i < 5 * number) {
				clo.get(i).setFontSize(37);
			} else if (i < 6 * number) {
				clo.get(i).setFontSize(32);
			} else if (i < 7 * number) {
				clo.get(i).setFontSize(27);
			} else if (i < 8 * number) {
				clo.get(i).setFontSize(22);
			} else if (i < 9 * number) {
				clo.get(i).setFontSize(17);
			} else if (i < 10 * number) {
				clo.get(i).setFontSize(12);
			}
			clo.get(0).setFontSize(65);
		}

		// System.out
		// .println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n\n");

		Random rand = new Random();
		int r = 0, g = 0, b = 0;
		for (int i = 0; i < clo.size(); i++) {
			if (clo.get(i).getFontColor() == null) {
				int rRand = rand.nextInt((41) + 10);
				int gRand = rand.nextInt((41) + 10);
				int bRand = rand.nextInt((41) + 10);
				r = (r + rRand) % 255;
				g = (g + gRand) % 255;
				b = (b + bRand) % 255;
				Color col = new Color(r, g, b);
				clo.get(i).setFontColor(col);
				for (int j = 0; j < clo.get(i).getConnected().size(); j++) {
					clo.get(i).getConnected().get(j).setFontColor(col);
				}
			}
		}
		Cloud2D c = new Cloud2D();
		c.generate(clo, line);
	}

	@SuppressWarnings("resource")
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
		//removing common suffixes and keeping the meat of the word
		Scanner sufScan;
		ArrayList<String> suffixes = new ArrayList<String>();
		try {
			sufScan = new Scanner(new File("commonSuffixes.txt"));
			while (sufScan.hasNext()) {
				suffixes.add(sufScan.next());
			}
			sufScan.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		for (int i = 0; i < suffixes.size(); i++) {
			for (int j = 0; j < lst.size(); j++) {
				if (lst.get(j).endsWith(suffixes.get(i))) {
					String tempCheck = lst.get(j).substring(0,lst.get(j).length() - suffixes.get(i).length());
//					System.out.print("Word before: " + lst.get(j) + "; word after: " + tempCheck);
					if (lst.contains(tempCheck)) {
						lst.set(j, tempCheck);
//						System.out.println("; the new word: " + lst.get(j));
					}
				}
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

	public static ArrayList<Node> processRelevancy(ArrayList<Node> clo,
			TreeMap<String, Node> storage) {
		ArrayList<Node> relevant = new ArrayList<Node>();
		String url = "http://www.thesaurus.com/browse/";
		int counter = 0;
		for (Node entry : clo) {
			String key = entry.getWord();
			// Node k = new Node(key);
			// System.out.println(key);
			String url2 = url + key;
			// assistance from Greg Colella for the try/catch block 
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
						storage.get(thiselement).connectNode(
								storage.get(thiselement));
						if (!relevant.contains(thiselement)) {
							relevant.add(storage.get(thiselement));
						}
					}
				}
			}
			++counter;
			System.out.println(counter + ": " + entry.getWord());
		}
		return relevant;
	}
}