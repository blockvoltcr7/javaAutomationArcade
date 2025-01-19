package Algorithms;

import java.util.*;

/**
 * Implementation of Dijkstra's algorithm for finding shortest paths.
 * 
 * <p>Real-world use cases in test automation:
 * - Optimizing test execution paths
 * - Analyzing network topology for performance testing
 * - Route optimization in location-based services testing
 * 
 * <p>Time Complexity: O(V^2) where V is number of vertices
 * Space Complexity: O(V) for storing distances and visited nodes
 */
public class DijkstraShortestPath {

    private static final int NO_PARENT = -1;

    /**
     * Finds shortest paths from source to all other vertices
     * @param adjacencyMatrix Graph representation
     * @param startVertex Starting vertex
     */
    public static void dijkstra(int[][] adjacencyMatrix, int startVertex) {
        int nVertices = adjacencyMatrix[0].length;

        // shortestDistances[i] will hold the shortest distance from src to i
        int[] shortestDistances = new int[nVertices];

        // added[i] will true if vertex i is included in shortest path tree
        boolean[] added = new boolean[nVertices];

        // Initialize all distances as INFINITE and added[] as false
        Arrays.fill(shortestDistances, Integer.MAX_VALUE);
        Arrays.fill(added, false);

        // Distance of source vertex from itself is always 0
        shortestDistances[startVertex] = 0;

        // Parent array to store shortest path tree
        int[] parents = new int[nVertices];
        parents[startVertex] = NO_PARENT;

        // Find shortest path for all vertices
        for (int i = 1; i < nVertices; i++) {
            // Pick the minimum distance vertex from the set of vertices not yet processed
            int nearestVertex = -1;
            int shortestDistance = Integer.MAX_VALUE;
            for (int vertexIndex = 0; vertexIndex < nVertices; vertexIndex++) {
                if (!added[vertexIndex] && shortestDistances[vertexIndex] < shortestDistance) {
                    nearestVertex = vertexIndex;
                    shortestDistance = shortestDistances[vertexIndex];
                }
            }

            // Mark the picked vertex as processed
            added[nearestVertex] = true;

            // Update distance value of the adjacent vertices
            for (int vertexIndex = 0; vertexIndex < nVertices; vertexIndex++) {
                int edgeDistance = adjacencyMatrix[nearestVertex][vertexIndex];
                
                if (edgeDistance > 0 && ((shortestDistance + edgeDistance) < shortestDistances[vertexIndex])) {
                    parents[vertexIndex] = nearestVertex;
                    shortestDistances[vertexIndex] = shortestDistance + edgeDistance;
                }
            }
        }

        printSolution(startVertex, shortestDistances, parents);
    }

    private static void printSolution(int startVertex, int[] distances, int[] parents) {
        System.out.println("\nVertex\t Distance\tPath");
        
        for (int vertexIndex = 0; vertexIndex < distances.length; vertexIndex++) {
            if (vertexIndex != startVertex) {
                System.out.print("\n" + startVertex + " -> ");
                System.out.print(vertexIndex + " \t\t ");
                System.out.print(distances[vertexIndex] + "\t\t");
                printPath(vertexIndex, parents);
            }
        }
    }

    private static void printPath(int currentVertex, int[] parents) {
        if (currentVertex == NO_PARENT) {
            return;
        }
        printPath(parents[currentVertex], parents);
        System.out.print(currentVertex + " ");
    }

    public static void main(String[] args) {
        // Example graph representing test execution paths
        int[][] graph = new int[][] {
            {0, 4, 0, 0, 0, 0, 0, 8, 0},
            {4, 0, 8, 0, 0, 0, 0, 11, 0},
            {0, 8, 0, 7, 0, 4, 0, 0, 2},
            {0, 0, 7, 0, 9, 14, 0, 0, 0},
            {0, 0, 0, 9, 0, 10, 0, 0, 0},
            {0, 0, 4, 14, 10, 0, 2, 0, 0},
            {0, 0, 0, 0, 0, 2, 0, 1, 6},
            {8, 11, 0, 0, 0, 0, 1, 0, 7},
            {0, 0, 2, 0, 0, 0, 6, 7, 0}
        };

        System.out.println("Optimizing test execution paths using Dijkstra's algorithm:");
        dijkstra(graph, 0);
    }
}
