package at.fhv.mme.graphs.structures;

import at.fhv.mme.graphs.elements.Edge;
import at.fhv.mme.graphs.elements.Node;
import at.fhv.mme.graphs.exceptions.NodeAlreadyExistsException;
import at.fhv.mme.graphs.exceptions.NodeNotFoundException;

import java.util.HashMap;
import java.util.LinkedList;

public class AdjacencyList implements AdjacencyStructure {
    private final HashMap<String, Node> nodes;
    private final HashMap<String, LinkedList<Node>> adjList;

    public AdjacencyList() {
        this.nodes = new HashMap<>();
        this.adjList = new HashMap<>();
    }

    @Override
    public void addNode(String nodeName) throws NodeAlreadyExistsException {
        // check if node exists
        if (this.nodes.containsKey(nodeName)) {
            throw new NodeAlreadyExistsException("Node with name '" + nodeName + "' already exists.");
        }

        Node node = new Node(nodeName);
        this.nodes.put(nodeName, node);
        adjList.put(nodeName, new LinkedList<>());
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
        Node firstNode = this.nodes.get(firstNodeName);
        Node secondNode = this.nodes.get(secondNodeName);

        // check if nodes exist
        if (firstNode == null) {
            throw new NodeNotFoundException("Node with name '" + firstNodeName + "' not found.");
        }

        if (secondNode == null) {
            throw new NodeNotFoundException("Node with name '" + secondNodeName + "' not found.");
        }

        // add edges to first and second node
        Edge edge = new Edge(firstNode, secondNode, weight);
        firstNode.addEdge(edge);
        secondNode.addEdge(edge);

        // update adjList
        adjList.get(firstNodeName).add(secondNode);
        adjList.get(secondNodeName).add(firstNode);
    }

    @Override
    public LinkedList<Node> getNeighbours(String nodeName) throws NodeNotFoundException {
        // check if node exists
        if (this.nodes.get(nodeName) == null) {
            throw new NodeNotFoundException("Node with name '" + nodeName + "' not found.");
        }

        return adjList.get(nodeName);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (HashMap.Entry<String, LinkedList<Node>> entry : adjList.entrySet()) {
            String nodeName = entry.getKey();
            LinkedList<Node> neighbours = entry.getValue();

            sb.append(nodeName).append(" -> ");

            for (Node neighbour : neighbours) {
                sb.append(neighbour.getName()).append(", ");
            }

            // remove the last comma and space, then append a new line
            if (!neighbours.isEmpty()) {
                sb.setLength(sb.length() - 2);
            }
            sb.append("\n");
        }

        return sb.toString();
    }
}