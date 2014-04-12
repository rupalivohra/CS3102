import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.TreeMap;

public class Map {
	/*
	 * 1) Read in text file; store each unique word in a map
	 * 2) If a word repeats, update the frequency field in the word
	 * 3) Add related words based on the thesaurus
	 */
	public static void main(String[] args) {
		String articleTitle = "test.txt";
		//include the text file name as a command line parameter
		String filename = articleTitle;
		//TreeMap<String, Word> storage = new TreeMap<String,Word>();
		TreeMap<String, Node> storage = new TreeMap<String, Node>();
		File file = new File(filename);
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
		
	}

}
