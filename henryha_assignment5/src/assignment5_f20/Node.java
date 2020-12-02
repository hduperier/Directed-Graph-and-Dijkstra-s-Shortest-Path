package assignment5_f20;

public class Node implements Comparable<Node>{
	int degreeOut = 0;
	int degreeIn = 0;
	String label;
	Long idNum;
	long weight;
	boolean touched;
	
	public Node(Long idNum, String label) {
		this.idNum = idNum;
		this.label = label;
		touched = false;
	}

	public Node(String label2) { // Do I need this?
		this.label = label2;
	}

	@Override
	public int compareTo(Node other) {
		if (this.weight > other.weight) {
			return 1;
		} else if (this.weight < other.weight) {
			return -1;
		} else {
			return 0;
		}
	}
}
