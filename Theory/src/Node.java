import java.util.ArrayList;

//Nodes = words; edges go between related words (accessible from array list).
public class Node {
	private int degree;
	private String word;
	private ArrayList<Node> connected;
	private int frequency;
	
	public Node() {
		this.frequency = 1;
		this.degree = 0;
		connected = new ArrayList<Node>();
	}
	
	public Node(String w) {
		this.word = w;
		this.degree = 0;
		this.frequency = 1;
		connected = new ArrayList<Node>();
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
	
	public ArrayList<Node> getConnected() {
		return this.connected;
	}
	
	public void connectNode(Node n) { //avoid the infinite loop!!
		connected.add(n);
		this.incDegree();
		n.getConnected().add(this);
		n.incDegree();
	}
}