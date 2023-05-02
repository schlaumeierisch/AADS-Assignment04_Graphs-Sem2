package at.fhv.mme.graphs.structures;

import at.fhv.mme.graphs.elements.Edge;
import at.fhv.mme.graphs.elements.Node;
import at.fhv.mme.graphs.exceptions.NodeAlreadyExistsException;
import at.fhv.mme.graphs.exceptions.NodeNotFoundException;

import java.util.HashMap;
import java.util.LinkedList;

public class AdjacencyMatrix implements AdjacencyStructure {
    private final HashMap<String, Node> nodes;
    private final HashMap<String, Integer> nodeIndices;
    private int[][] adjMatrix;

    public AdjacencyMatrix() {
        this.nodes = new HashMap<>();
        this.nodeIndices = new HashMap<>();
        this.adjMatrix = new int[0][0];
    }

    @Override
    public void addNode(String nodeName) throws NodeAlreadyExistsException {
        // check if node exists
        if (this.nodes.containsKey(nodeName)) {
            throw new NodeAlreadyExistsException("Node with name '" + nodeName + "' already exists.");
        }

        Node node = new Node(nodeName);
        this.nodes.put(nodeName, node);
        this.nodeIndices.put(nodeName, this.nodes.size() - 1);

        // increase node count and create new matrix since we added a new node
        int newNodeCount = this.nodes.size();
        int[][] newAdjMatrix = new int[newNodeCount][newNodeCount];

        // copy existing data to new matrix
        for (int i = 0; i < adjMatrix.length; i++) {
            System.arraycopy(adjMatrix[i], 0, newAdjMatrix[i], 0, adjMatrix.length);
        }

        adjMatrix = newAdjMatrix;
    }

    @Override
    public Node getNode(String nodeName) throws NodeNotFoundException {
        Node node = this.nodes.get(nodeName);

        // check if node exists
        if (node == null) {
            throw new NodeNotFoundException("Node with name '" + nodeName + "' not found.");
        }

        return node;
    }

    @Override
    public void addEdge(String firstNodeName, String secondNodeName, int weight) throws NodeNotFoundException {
        Node firstNode = getNode(firstNodeName);
        Node secondNode = getNode(secondNodeName);

        int firstNodeIndex = this.nodeIndices.get(firstNodeName);
        int secondNodeIndex = this.nodeIndices.get(secondNodeName);

        adjMatrix[firstNodeIndex][secondNodeIndex] = weight;
        adjMatrix[secondNodeIndex][firstNodeIndex] = weight;

        // add edges to first and second node
        Edge edge = new Edge(firstNode, secondNode, weight);
        firstNode.addEdge(edge);
        secondNode.addEdge(edge);
    }

    @Override
    public LinkedList<Node> getNeighbours(String nodeName) throws NodeNotFoundException {
        // check if node exists
        if (this.nodes.get(nodeName) == null) {
            throw new NodeNotFoundException("Node with name '" + nodeName + "' not found.");
        }

        LinkedList<Node> neighbours = new LinkedList<>();
        int nodeIndex = nodeIndices.get(nodeName);

        // iterate through the matrix row and find non-zero weights (edges)
        for (int colIndex = 0; colIndex < adjMatrix.length; colIndex++) {
            if (adjMatrix[nodeIndex][colIndex] != 0) {
                // find the corresponding Node object by its index
                Node neighbour = null;
                for (Node node : this.nodes.values()) {
                    if (nodeIndices.get(node.getName()) == colIndex) {
                        neighbour = node;
                        break;
                    }
                }

                if (neighbour != null) {
                    neighbours.add(neighbour);
                }
            }
        }

        return neighbours;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        // calculate the maximum length of node names
        int maxNameLength = this.nodes.values().stream()
                .mapToInt(node -> node.getName().length())
                .max()
                .orElse(4);

        // print header with node names
        sb.append(" ".repeat(maxNameLength + 1));
        for (Node node : this.nodes.values()) {
            sb.append(String.format("%" + (maxNameLength + 1) + "s", node.getName()));
        }
        sb.append("\n");

        // print matrix with row labels (node names)
        for (Node rowNode : this.nodes.values()) {
            sb.append(String.format("%-" + (maxNameLength + 1) + "s", rowNode.getName()));
            int rowNodeIndex = nodeIndices.get(rowNode.getName());

            for (Node colNode : this.nodes.values()) {
                int colNodeIndex = nodeIndices.get(colNode.getName());
                int weight = adjMatrix[rowNodeIndex][colNodeIndex];
                sb.append(String.format("%" + (maxNameLength + 1) + "d", weight));
            }
            sb.append("\n");
        }

        return sb.toString();
    }
}