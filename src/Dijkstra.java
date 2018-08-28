//Bijay Ranabhat and McKenzie Allaben
//DePauw University
//FALL 2017
//MATH 323----> Algorithmic Graph Theory
//Implementation of Dijkstra's Algorithm


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
//import java.util.Timer; 
/*
//The network needs to be in a text file
//Data in the format:   v1 a         //  VertexNumber VertexName //assigning names to each vertex
						v2 b
						v3 c 
						v4 d
						v5 e 
						v6 f
						# 			//  Indicator to change storage from Vertex Name to Edge length in the HashMap
						v1 v2 2.0 	//	Distance between Vertex 1 and Vertex 2 is 2.0
						v1 v3 8.0 	//  Distance between Vertex 1 and Vertex 3 is 8.0
						v2 v3 6.0 	//	Distance between Vertex 2 and Vertex 3 is 6.0
						v2 v4 3.0 	//  Distance between Vertex 2 and Vertex 4 is 3.0
						v3 v2 5.0 	//  Distance between Vertex 3 and Vertex 2 is 5.0
						v3 v5 0.0   // Continue the same format for the rest of the edges
						v4 v3 1.0   
						v4 v5 7.0   
						v4 v6 6.0 
						v5 v4 4.0 
						v5 v6 2.0
*/


class Vertex implements Comparable<Vertex> {
    public final String name;
    
    public List<Edge> adjacencies;
    
    public double minDistance = Double.POSITIVE_INFINITY;
    
    public Vertex previous;

    public Vertex(String argName) {
        name = argName;
        adjacencies = new ArrayList<Edge>();
    }

    public void addEdge(Edge e) {
        adjacencies.add(e);
    }

    public String toString() {
        return name;
    }

    public int compareTo(Vertex other) {
        return Double.compare(minDistance, other.minDistance);
    }

}

class Edge {
    public final Vertex target;
    
    public final double weight;

    public Edge(Vertex argTarget, double argWeight) {
        target = argTarget;
        weight = argWeight;
    }
}


public class Dijkstra {

    public static void computePaths(Vertex source) {
        source.minDistance = 0.;
        
        PriorityQueue<Vertex> vertexQueue = new PriorityQueue<Vertex>();
        
        vertexQueue.add(source);
        
        int itr = 1;
        
        
        while (!vertexQueue.isEmpty()) 
        {
            Vertex u = vertexQueue.poll();
            
            //Vertex rootNode = u;
            Vertex temp = vertexQueue.poll();
            
            
            
            // Visit each edge exiting u
            //for(int i=0; i<vertexMap.size(); i++)
            for (Edge e : u.adjacencies) {
                Vertex v = e.target;
                
                double weight = e.weight;
                double distanceThroughU = u.minDistance + weight;
          
                if (distanceThroughU < v.minDistance) {
                    vertexQueue.remove(v);
                    v.minDistance = distanceThroughU;
                    v.previous = u;
                    
                    if(temp !=u){
                    	temp=u;
                    	System.out.println("Itr: "+itr);
            
                    	System.out.println();
                    	System.out.println("Any vertex in the graph, that is not listed as a target in this iteration can be assumed to have the distance and predcessor values previously assigned in the last iteration. ");
                    	System.out.println();
                    	itr++;
                    }
                    
                    System.out.println("#Root Node: " + u);
                    System.out.println("Target:" + v);
                    System.out.println("Distance to " + v + ": " + v.minDistance);
                    vertexQueue.add(v);
                    List<Vertex> path = getShortestPathTo(v);
                    System.out.println("Pred: " + path.get(path.size() - 2));
                    System.out.println("Path: " + path);
                    System.out.println("In Queue: " + vertexQueue);
                    
                    System.out.println("\n");

                }

            }
        }
    }

    public static List<Vertex> getShortestPathTo(Vertex target) {
        List<Vertex> path = new ArrayList<Vertex>();
        for (Vertex vertex = target; vertex != null; vertex = vertex.previous)
            path.add(vertex);

        Collections.reverse(path);
        return path;
    }

    public static void main(String args[]) throws FileNotFoundException {
    	
    	long startTimer = System.currentTimeMillis();
    	
    	PrintStream o = new PrintStream("Outputfile.txt"); //Output file
        //PrintStream console = System.out;					//If we want to print to console
    	
    	
        Map<String, Vertex> vertexMap = new HashMap<String, Vertex>();
        BufferedReader in = null;
        try {
            //in = new BufferedReader(new FileReader("graph1.txt"));
            in = new BufferedReader(new FileReader("graph2.txt"));
            String line;
            boolean inVertex = true;

            while ((line = in.readLine()) != null) {
                if (line.charAt(0) == '#') {
                    inVertex = false;
                    continue;
                }
                if (inVertex) {
                    //store the vertices
                    int indexOfSpace = line.indexOf(' ');
                    String vertexId = line.substring(0, indexOfSpace);
                    String vertexName = line.substring(indexOfSpace + 1);
                    Vertex v = new Vertex(vertexName);
                    vertexMap.put(vertexId, v);
                } else {
                    //store the edges
                    String[] parts = line.split(" ");
                    String vFrom = parts[0];
                    String vTo = parts[1];
                    double weight = Double.parseDouble(parts[2]);
                    Vertex v = vertexMap.get(vFrom);
                    if (v != null) {
                        v.addEdge(new Edge(vertexMap.get(vTo), weight));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        finally
        {
            if(in!= null)
                try {
                    in.close();
                } catch (IOException ignore) {
                }
        }

        //get a list of all the vertices
        Collection<Vertex> vertices = vertexMap.values();
        Vertex source = vertices.iterator().next();
        System.setOut(o);			//Prints to a txt file
        //System.setOut(console);		//prints to console
        System.out.println("Computing paths from " + source);
        System.out.println("\n");
        System.out.println("Itr: "+ 0);
        System.out.println("Distance to " + "a:" + "0.0");
        System.out.println("Pred: a");
        System.out.println("All vertices except for vertex 'a' have a distance of INFINITY and the predecessor as vertex 'a'."); 
        System.out.println();
        computePaths(source);
        System.out.println("SHORTEST PATH DETERMINED (LAST ITERATION):");
        for (Vertex v : vertices) {
            System.out.println("Distance to " + v + ": " + v.minDistance);
            List<Vertex> path = getShortestPathTo(v);
            System.out.println("Path: " + path);
            System.out.println();
        }
        long endTimer = System.currentTimeMillis(); 
        System.out.println("It took " + ((endTimer-startTimer)/1000.0) + " seconds to solve the problem.");
        
        
    }
}