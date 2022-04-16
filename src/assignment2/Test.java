package assignment2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Test {

	public static void main(String[] args) {
		Graph graph = new Graph(14849); //number of vertices + 1
		String source;
		String destination;
		
		System.out.println("The graph is being read...");
		try {
			File file = new File("graph.txt");
			Scanner scn = new Scanner(file); 
			while (scn.hasNextLine()) {
				String[] currentLine = scn.nextLine().split("\t");
				graph.addEdge(currentLine[0], currentLine[1], Integer.parseInt(currentLine[2]));
			} scn.close();					
        } catch (FileNotFoundException e) {
        	System.out.println("File not found! " + e.getMessage());
        } 
		
		Scanner scn2 = new Scanner(System.in);
		while (true) {
			System.out.print("Please enter the source vertex: ");
			try {
				source = scn2.next().toUpperCase();
				if (!graph.getVertices().contains(source)) continue;
				else break;	
			} catch (Exception e) {  // if an exception appears prints message below
				System.out.println("\nPlease enter the source vertex in the graph list! " + e.getMessage() + "-> ");
				continue;    // continues to loop if exception is found
			}
		}
		while (true) {
			System.out.print("Please enter the destination vertex: ");
			try {
				destination = scn2.next().toUpperCase();
				if (!graph.getVertices().contains(destination)) continue;
				else break;	
			} catch (Exception e) {  // if an exception appears prints message below
				System.out.println("\nPlease enter the destination vertex in the graph list! " + e.getMessage() + "-> ");
				continue;    // continues to loop if exception is found
			}
		}
        fold_fulkerson fordFulkerson = new fold_fulkerson(14848);
        System.out.println("\n-----TASK1-----");
        int maxFlow = fordFulkerson.fordFulkerson(graph.getAdjacency(), graph.getVertices().indexOf(source) + 1, graph.getVertices().indexOf(destination) + 1);
        System.out.println("The Max Flow from " + source + " to " + destination + " is: " + maxFlow);
        
        System.out.println("\n-----TASK2-----");
        System.out.println("The bottleneck edges are: ");
        fordFulkerson.minCut(graph.getAdjacency(), graph.getVertices(), graph.getVertices().indexOf(source) + 1, graph.getVertices().indexOf(destination) + 1);
        
        System.out.println("\n-----TASK3-----");
        fordFulkerson.findIncreaseAmount(graph.getVertices());
	}	
}
