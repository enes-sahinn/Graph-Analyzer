package assignment2;

import java.util.ArrayList;

public class Graph {	
	private ArrayList<String> vertices; // to keep vertex names
	private int[][] adjacency;  // to keep edges
	
	public Graph(int size) {
		vertices = new ArrayList<String>();
		adjacency = new int[size][size];
	}
	
	public void addEdge(String source, String destination, int weight) {
		
		if(!vertices.contains(source)) {
			vertices.add(source);
		}
		if(!vertices.contains(destination)) {
			vertices.add(destination);
		}	
		int source_index = vertices.indexOf(source);
		int destination_index = vertices.indexOf(destination);
		adjacency[source_index + 1][destination_index + 1] += weight; // += for adding of same edges
	}
		
	public int[][] getAdjacency() {
		return adjacency;
	}

	public ArrayList<String> getVertices() {
		return vertices;
	}	
}
