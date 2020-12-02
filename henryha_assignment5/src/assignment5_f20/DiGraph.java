package assignment5_f20;

import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentHashMap;

public class DiGraph implements DiGraphInterface {
	Map<Long, Node> myNode = new ConcurrentHashMap<>();
	Map<Long, Edge> myEdge = new ConcurrentHashMap<>();
	PriorityQueue<Node> pqueue = new PriorityQueue<>();

	public DiGraph() { // default constructor
	}

	/*
	 * return: boolean returns false if node number is less than 0 returns false if
	 * label is not unique (or is null) returns true if node is successfully added
	 */
	public boolean addNode(long idNum, String label) {
		if (idNum < 0 || label == null) {
			return false;
		}
		for (Node temp : myNode.values()) {
			if (temp.idNum == idNum) {
				return false;
			}
		}
		if (myNode.containsKey(idNum)) {
			return false;
		}
		for (Node temp1 : myNode.values()) {
			if (temp1.label.equals(label)) {
				return false;
			}
		}
		Node tester = new Node(label);

		if (myNode.containsValue(tester)) {
			return false;
		}

		Node temp3 = new Node(idNum, label);
		myNode.put(idNum, temp3);
		return true;
	}

	/*
	 * return: boolean returns false if edge number is less than 0 returns false if
	 * source node is not in graph returns false if destination node is not in graph
	 * returns false is there already is an edge between these 2 nodes returns true
	 * if edge is successfully added
	 */
	public boolean addEdge(long idNum, String sLabel, String dLabel, long weight, String eLabel) {
		boolean fNode = false;
		for (Node tempNode : myNode.values()) {
			if (tempNode.label.equals(sLabel)) {
				fNode = true;
				break;
			}
		}
		if (idNum < 0 || sLabel == null || dLabel == null) {
			return false;
		}
		for (Edge tempEdge : myEdge.values()) {
			if (tempEdge.idNum == idNum) {
				return false;
			}
		}
		if (!fNode) {
			return false;
		}
		boolean targetNode = false;
		for (Node tempNode2 : myNode.values()) {
			if (tempNode2.label.equals(dLabel)) {
				targetNode = true;
				break;
			}
		}
		if (!targetNode) {
			return false;
		}
		for (Edge tempEdge1 : myEdge.values()) {
			if (tempEdge1.sLabel.equals(sLabel) && tempEdge1.dLabel.equals(dLabel)) {
				return false;
			}
		}
		Edge tempEdge2 = new Edge(idNum, sLabel, dLabel, weight, eLabel);
		myEdge.put(idNum, tempEdge2);
		for (Node tempNode3 : myNode.values()) {
			if (tempNode3.label.equals(sLabel)) {
				tempNode3.degreeOut++;
			}
			if (tempNode3.label.equals(dLabel)) {
				tempNode3.degreeIn++;
			}
		}
		return true;
	}

	/*
	 * out: boolean return false if the node does not exist return true if the node
	 * is found and successfully removed
	 */
	@Override
	public boolean delNode(String label) {
		// if the node doesn't exist, nothing to delete
		boolean fBool = false;
		Node tempNode = null;
		for (Node tempNode1 : myNode.values()) {
			if (tempNode1.label.equals(label)) {
				fBool = true;
				tempNode = tempNode1;
			}
		}
		if (!fBool) {
			return false;
		} else {
			if (tempNode.degreeIn > 0) {
				for (Edge tempEdge : myEdge.values()) {
					if (tempEdge.dLabel.equals(tempNode.label)) {
						myEdge.remove(tempEdge.idNum);
					}
					tempNode.degreeIn--;
				}
			} else if (tempNode.degreeOut > 0) {
				for (Edge tempEdge1 : myEdge.values()) {
					if (tempEdge1.sLabel.equals(tempNode.label)) {
						myEdge.remove(tempEdge1.idNum);
					}
					tempNode.degreeOut--;
				}
			}
			if (tempNode.degreeIn == 0 && tempNode.degreeOut == 0) {
				myNode.remove(tempNode.idNum);
			}
		}
		return true;
	}

	/*
	 * out: boolean return false if the edge does not exist return true if the edge
	 * is found and successfully removed
	 */
	public boolean delEdge(String sLabel, String dLabel) {
		boolean fBool = false;
		Edge tempEdge = null;
		for (Edge tempEdge1 : myEdge.values()) {
			if (tempEdge1.sLabel.equals(sLabel) && tempEdge1.dLabel.equals(dLabel)) {
				fBool = true;
				tempEdge = tempEdge1;
			}
		}
		if (!fBool) {
			return false;
		} else {
			myEdge.remove(tempEdge.idNum);
		}
		return true;
	}

	/*
	 * return: integer 0 or greater reports how many nodes are in the graph
	 */
	public long numNodes() {
		long counter = 0;
		for (Node tempNode : myNode.values()) {
			counter++;
		}
		return counter;
	}

	public long numEdges() {
		long counter = 0;
		for (Edge tempEdge : myEdge.values()) {
			counter++;
		}
		return counter;
	}

	/*
	 * return: integer 0 or greater reports how many edges are in the graph
	 */
	@Override
	public ShortestPathInfo[] shortestPath(String label) {
		Node permNode = null;
		boolean fBool = false;
		for (Node tempNode : myNode.values()) {
			if (tempNode.label.equals(label)) {
				permNode = tempNode;
				fBool = true;
			}
		}
		if (!fBool) {
			return null;
		} else {
			for (Node tempNode2 : myNode.values()) {
				if (tempNode2.label.contentEquals(permNode.label)) {
					tempNode2.weight = 0;
					pqueue.add(tempNode2);
				} else {
					tempNode2.weight = Integer.MAX_VALUE;
					pqueue.add(tempNode2);
				}
			}
		}

		while (!pqueue.isEmpty()) {
			Node leastNode = pqueue.remove();
			leastNode.touched = true;
			for (Edge tempEdge : myEdge.values()) {
				if (tempEdge.sLabel.equals(leastNode.label)) {
					Node close = null;

					for (Node tempNode3 : myNode.values()) {
						if (tempNode3.label.equals(tempEdge.dLabel)) {
							close = tempNode3;
						}
					}
					long updatedWeight = leastNode.weight + tempEdge.weight;
					if (updatedWeight < close.weight) {
						close.weight = updatedWeight;
					}
				}
			}
		}
		for (Node tmpNode : myNode.values()) {
			if (tmpNode.weight == Integer.MAX_VALUE) {
				tmpNode.weight = -1;
			}
		}

		ShortestPathInfo[] tempList = new ShortestPathInfo[(int) (numNodes())];
		int counter = 0;
		for (Node tempNode0 : myNode.values()) {
			tempList[counter] = new ShortestPathInfo(tempNode0.label, tempNode0.weight);
			counter++;
		}
		return tempList;
	}
}
