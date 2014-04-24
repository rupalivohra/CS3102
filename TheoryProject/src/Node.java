//import java.util.ArrayList;
import java.util.ArrayList;
import java.util.Comparator;

//Nodes = words; edges go between related words (accessible from array list).
public class Node implements Comparable<Node> {
	private int degree;
	private String word;
	private ArrayList<Node> connected;
	// private ArrayList<Node> connected;
	private int frequency;
	private int fontSize;

	public Node() {
		this.frequency = 1;
		this.degree = 0;
		connected = new ArrayList<Node>();
		this.fontSize = 0;
	}

	public Node(String w) {
		this.word = w;
		this.degree = 0;
		this.frequency = 1;
		connected = new ArrayList<Node>();
		this.fontSize = 0;
	}
	
	public int getFontSize() {
		return this.fontSize;
	}
	
	public void setFontSize(int size) {
		this.fontSize = size;
	}
	
	public int getDegree() {
		return this.degree;
	}

	public void incDegree() {
		++degree;
	}

	public void decDegree() {
		degree--;
	}

	public int getFreq() {
		return this.frequency;
	}

	public void incFreq() {
		++frequency;
	}

	public void decFreq() {
		frequency--;
	}

	public String getWord() {
		return this.word;
	}

	public ArrayList<String> getConnectedString() {
		// TreeSet<String> ret = new TreeSet<String>();
		ArrayList<String> ret = new ArrayList<String>();
		for (Node n : this.connected) {
			ret.add(n.getWord());
		}
		return ret;
	}

	public ArrayList<Node> getConnected() {
		return this.connected;
	}

	public void connectNode(Node n) { // avoid the infinite loop!!
		if (!connected.contains(n)) {
			connected.add(n);
			this.incDegree();
			n.getConnected().add(this);
			n.incDegree();
			// System.out.println("Connected " + this + " to " + n);
		}
	}

	@Override
	public String toString() {
		return "Node [word=" + word + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((word == null) ? 0 : word.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Node other = (Node) obj;
		if (word == null) {
			if (other.word != null)
				return false;
		} else if (!word.equals(other.word))
			return false;
		return true;
	}

	@Override
	public int compareTo(Node no) {
		Integer object1 = this.getFreq();
		Integer object2 = no.getFreq();
		return object1.compareTo(object2);
	}
}