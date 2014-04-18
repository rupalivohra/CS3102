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
		
		public ArrayList<String> getConnectedString() {
			ArrayList<String> ret = new ArrayList<String>();
			for (int i = 0; i < this.connected.size(); i++) {
				ret.add(this.connected.get(i).getWord());
			}
			return ret;
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
	}
