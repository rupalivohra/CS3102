import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
		List<String> wordsList = getWords(content);
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