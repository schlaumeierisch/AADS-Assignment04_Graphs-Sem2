package at.fhv.mme.graphs;

import at.fhv.mme.graphs.elements.Node;
import at.fhv.mme.graphs.exceptions.EmptyFileException;
import at.fhv.mme.graphs.exceptions.InvalidFileFormatException;
import at.fhv.mme.graphs.exceptions.NodeAlreadyExistsException;
import at.fhv.mme.graphs.exceptions.NodeNotFoundException;
import at.fhv.mme.graphs.structures.AdjacencyList;
import at.fhv.mme.graphs.structures.AdjacencyMatrix;
import at.fhv.mme.graphs.structures.AdjacencyStructure;
import at.fhv.mme.graphs.structures.ObjectGraph;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Graph {
    private final AdjacencyStructure adjStructure;

    private Graph(AdjacencyStructure adjStructure) {
        this.adjStructure = adjStructure;
    }

    public static Graph load(String fileName, GraphType graphType) throws IOException, EmptyFileException, InvalidFileFormatException, NodeNotFoundException, NodeAlreadyExistsException {
        AdjacencyStructure adjStructure = switch (graphType) {
            case ADJACENCY_LIST -> new AdjacencyList();
            case ADJACENCY_MATRIX -> new AdjacencyMatrix();
            case OBJECT_GRAPH -> new ObjectGraph();
        };

        Graph graph = new Graph(adjStructure);

        Path filePath = Paths.get(fileName);
        List<String> lines = Files.readAllLines(filePath);

        // check if file is empty
        if (lines.isEmpty()) {
            throw new EmptyFileException("File is empty.");
        }

        // first line: Nodes
        String[] nodeNames = lines.get(0).split(",");
        for (String nodeName : nodeNames) {
            graph.addNode(nodeName.trim());
        }

        // remaining lines: Edges
        for (int i = 1; i < lines.size(); i++) {
            String[] edge = lines.get(i).split(",");

            // check if line has the correct format
            if (edge.length != 3) {
                throw new InvalidFileFormatException("Invalid format in line " + (i + 1) + ".");
            }

            String firstNodeName = edge[0].trim();
            String secondNodeName = edge[1].trim();
            int weight;

            // check if weight has the correct format
            try {
                weight = Integer.parseInt(edge[2].trim());
            } catch (NumberFormatException e) {
                throw new InvalidFileFormatException("Invalid weight in line " + (i + 1) + ".");
            }

            try {
                graph.addEdge(firstNodeName, secondNodeName, weight);
            } catch (NodeNotFoundException e) {
                throw new NodeNotFoundException("Node not found in line " + (i + 1) + ": " + e.getMessage());
            }
        }

        return graph;
    }

    public void addNode(String name) throws NodeAlreadyExistsException {
        this.adjStructure.addNode(name);
    }

    public Node getNode(String nodeName) throws NodeNotFoundException {
        return this.adjStructure.getNode(nodeName);
    }

    public void addEdge(String firstNode, String secondNode, int weight) throws NodeNotFoundException {
        this.adjStructure.addEdge(firstNode, secondNode, weight);
    }

    public LinkedList<Node> getNeighbours(String nodeName) throws NodeNotFoundException {
        return this.adjStructure.getNeighbours(nodeName);
    }

    public void traverse(String startNodeName, TraversalAlgorithm algorithm) throws NodeNotFoundException {
        Node node = getNode(startNodeName);

        switch (algorithm) {
            case DFS_RECURSIVE -> depthFirstSearchRecursive(node, new HashSet<>());
            case DFS_ITERATIVE -> depthFirstSearchIterative(node);
            case BFS_ITERATIVE -> breadthFirstSearchIterative(node);
        }
    }

    private void depthFirstSearchRecursive(Node node, Set<Node> visited) throws NodeNotFoundException {
        if (visited.contains(node)) {
            return;
        }

        System.out.println("Visiting: " + node.getName());
        visited.add(node);

        LinkedList<Node> neighbours = adjStructure.getNeighbours(node.getName());
        for (Node neighbour : neighbours) {
            depthFirstSearchRecursive(neighbour, visited);
        }
    }

    private void depthFirstSearchIterative(Node startNode) {

        // TODO

    }

    private void breadthFirstSearchIterative(Node startNode) {

        // TODO

    }

    public void print() {
        System.out.println(this.adjStructure.toString());
    }
}