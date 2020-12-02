package assignment5_f20;

public class Edge {
	
	long idNum;
	long weight;
	String sLabel;
	String dLabel;
	String eLabel;
	int degrees = 0;
	
	public Edge(long idNum, String labelS, String labelD, long weight, String labelE) {
		this.idNum = idNum;
		this.weight = weight;
		this.sLabel = labelS;
		this.dLabel = labelD;
		this.eLabel = labelE;
	}

}
