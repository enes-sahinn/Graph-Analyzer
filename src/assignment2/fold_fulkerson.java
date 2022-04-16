package assignment2;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class fold_fulkerson {	
	
	private int[] parent;
    private Queue<Integer> queue;
    private int numberOfVertices;
    private boolean[] visited;
    public int[][] residualGraph;
    private ArrayList<String> paths; // paths that from source to destination
    private ArrayList<String> bottleneckEdges;   
    String path = "";
    int maxFlow;
    
    public fold_fulkerson(int numberOfVertices)    {
        this.numberOfVertices = numberOfVertices;
        this.queue = new LinkedList<Integer>();
        parent = new int[numberOfVertices + 1];
        visited = new boolean[numberOfVertices + 1];
        paths = new ArrayList<String>();
        bottleneckEdges = new ArrayList<String>();
    }
 
	public boolean bfs(int source, int goal, int graph[][]) {
		//https://www.sanfoundry.com/java-program-implement-ford-fulkerson-algorithm/ 
		boolean pathFound = false;
		int destination, element;
		
		for (int vertex = 1; vertex <= numberOfVertices; vertex++) {
			parent[vertex] = -1;
			visited[vertex] = false;
		}
		queue.add(source);
		parent[source] = -1;
		visited[source] = true;

		while (!queue.isEmpty()) {
			element = queue.remove();
			destination = 1;

			while (destination <= numberOfVertices) {
				if (graph[element][destination] > 0 && !visited[destination]) {
					parent[destination] = element;
					queue.add(destination);
					visited[destination] = true;
				}
				destination++;
			}
		}
		if (visited[goal]) {
			pathFound = true;
		}
		return pathFound;
	}
 
	public int fordFulkerson(int graph[][], int source, int destination) {
		//https://www.sanfoundry.com/java-program-implement-ford-fulkerson-algorithm/
		int u, v;
		maxFlow = 0;
		int pathFlow;
		residualGraph = new int[numberOfVertices + 1][numberOfVertices + 1];
		
		for (int sourceVertex = 1; sourceVertex <= numberOfVertices; sourceVertex++) {
			for (int destinationVertex = 1; destinationVertex <= numberOfVertices; destinationVertex++) {
				residualGraph[sourceVertex][destinationVertex] = graph[sourceVertex][destinationVertex];
			}
		}
		while (bfs(source, destination, residualGraph)) {
			pathFlow = Integer.MAX_VALUE;
			for (v = destination; v != source; v = parent[v]) {
				u = parent[v];
				pathFlow = Math.min(pathFlow, residualGraph[u][v]);
			}
			for (v = destination; v != source; v = parent[v]) {
				u = parent[v];
				residualGraph[u][v] -= pathFlow;
				residualGraph[v][u] += pathFlow;
				path += Integer.toString(v) + ","; // to store the path
			}
			maxFlow += pathFlow;
			path += Integer.toString(v); 
			String str[] = path.split(",");  // to reverse path to source-destination form
			String revPath = "";             // I used paths arraylist in task3 
			for (int i = str.length - 1; i >= 0; i--) {
				revPath += str[i] + ",";
			}
			paths.add(revPath);
			path = "";
		}
		return maxFlow;
	}

	public void dfs(int[][] rGraph, int s, boolean[] visited) {
		//https://www.geeksforgeeks.org/minimum-cut-in-a-directed-graph/
		visited[s] = true;
		for (int i = 1; i < rGraph.length; i++) {
			if (rGraph[s][i] > 0 && !visited[i]) {
				dfs(rGraph, i, visited);
			}
		}
	}
	
	public void minCut(int[][] orginalGraph, ArrayList<String> vertices, int s, int d) {
		//https://www.geeksforgeeks.org/minimum-cut-in-a-directed-graph/
		boolean[] isVisited = new boolean[orginalGraph.length];
		int outcomes = 0;
		
		for (int i = 0; i < orginalGraph.length; i++) {
			outcomes += orginalGraph[s][i];
		}
		if (maxFlow < outcomes) { // means there is bottleneck edge
			dfs(residualGraph, s, isVisited);		
		
			for (int i = 0; i < orginalGraph.length; i++) {
				for (int j = 0; j < orginalGraph.length; j++) {
					if (orginalGraph[i][j] > 0 && isVisited[i] && !isVisited[j] /*&& (i!=s || j!=d)*/) {
						bottleneckEdges.add(vertices.get(i - 1) + " - " + vertices.get(j - 1));
					}
				}
			}// for remove unnecessary bottleneck edges(that in any path that contains zero edge that not a bottleneck edge)
			/*for (int i = 0; i < paths.size(); i++) {  
				String path = paths.get(i);
				String[] arrOfStr = path.split(",", 0);
				for (int j = 0; j < arrOfStr.length - 1; j++) {
					String edge = vertices.get(Integer.parseInt(arrOfStr[j]) - 1) + " - " + vertices.get(Integer.parseInt(arrOfStr[j + 1]) - 1);
					if (!bottleneckEdges.contains(edge) && residualGraph[Integer.parseInt(arrOfStr[j])][Integer.parseInt(arrOfStr[j + 1])] == 0) {
						for (int k = 0; k < arrOfStr.length - 1; k++) {
							String edge2 = vertices.get(Integer.parseInt(arrOfStr[k]) - 1) + " - " + vertices.get(Integer.parseInt(arrOfStr[k + 1]) - 1);
							if (bottleneckEdges.contains(edge2)) {
								bottleneckEdges.remove(bottleneckEdges.indexOf(edge2));
							}
						}
					}
				}
			}*/
			for (String str : bottleneckEdges) {
				System.out.println(str);
			}	
		} else {
			System.out.println("No bottleneck edge.");
		}	
	}
	// I wrote this function myself
	public void findIncreaseAmount(ArrayList<String> vertices) {
		int increaseAmount = 0;
		for (int i = 0; i < paths.size(); i++) {
			String path = paths.get(i); // any path that from source to dest
	        String[] arrOfStr = path.split(",", 0); 	        
	        increaseAmount = Integer.MAX_VALUE;
	        // Compare edges that not bottleneck in the path to find min edge to define increase amount. 
	        for (int j = 0; j < arrOfStr.length - 1; j++) { 
				String edge = vertices.get(Integer.parseInt(arrOfStr[j]) - 1) + " - " +  vertices.get(Integer.parseInt(arrOfStr[j + 1]) - 1);
				if (!bottleneckEdges.contains(edge)) {
					increaseAmount = Math.min(residualGraph[Integer.parseInt(arrOfStr[j])][Integer.parseInt(arrOfStr[j + 1])], increaseAmount);
				}
			}
	        // Subtract increse amount from edges in the path also worked on only residual graph.
			for (int j = 0; j < arrOfStr.length - 1; j++) {
				residualGraph[Integer.parseInt(arrOfStr[j])][Integer.parseInt(arrOfStr[j + 1])] -= increaseAmount;
			}
			// Print all bottleneck edges wtih their increase amount
			for (int j = 0; j < arrOfStr.length - 1; j++) {
				String edge = vertices.get(Integer.parseInt(arrOfStr[j]) - 1) + " - " +  vertices.get(Integer.parseInt(arrOfStr[j + 1]) - 1);
				if (bottleneckEdges.contains(edge)) {
					if (increaseAmount == Integer.MAX_VALUE) {
						System.out.println(edge + " should be increased by " + increaseAmount + " (mean infinite)");
					} else {
						System.out.println(edge + " should be increased by " + increaseAmount);
					}
					bottleneckEdges.remove(bottleneckEdges.indexOf(edge));
				}
			}
		}	
	}
}
