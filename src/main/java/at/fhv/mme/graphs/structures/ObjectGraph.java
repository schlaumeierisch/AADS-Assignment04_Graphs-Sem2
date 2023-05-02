package at.fhv.mme.graphs.structures;

import at.fhv.mme.graphs.elements.Edge;
import at.fhv.mme.graphs.elements.Node;
import at.fhv.mme.graphs.exceptions.NodeAlreadyExistsException;
import at.fhv.mme.graphs.exceptions.NodeNotFoundException;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Objects;

public class ObjectGraph implements AdjacencyStructure {
    private final HashMap<String, Node> nodes;

    public ObjectGraph() {
        this.nodes = new HashMap<>();
    }

    @Override
    public void addNode(String nodeName) throws NodeAlreadyExistsException {
        if (this.nodes.containsKey(nodeName)) {
            throw new NodeAlreadyExistsException("Node with name '" + nodeName + "' already exists.");
        }

        Node node = new Node(nodeName);
        this.nodes.put(nodeName, node);
    }

    @Override
    public void addEdge(String firstNodeName, String secondNodeName, int weight) throws NodeNotFoundException {
        Node firstNode = getNode(firstNodeName);
        Node secondNode = getNode(secondNodeName);

        Edge edge = new Edge(firstNode, secondNode, weight);
        firstNode.addEdge(edge);
        secondNode.addEdge(edge);
    }

    @Override
    public LinkedList<Node> getNeighbours(String nodeName) throws NodeNotFoundException {
        Node node = getNode(nodeName);

        LinkedList<Node> neighbours = new LinkedList<>();
        for (Edge edge : node.getEdges()) {
            if (Objects.equals(edge.getFirstNode().getName(), nodeName)) {
                neighbours.add(edge.getSecondNode());
            } else {
                neighbours.add(edge.getFirstNode());
            }
        }

        return neighbours;
    }

    private Node getNode(String nodeName) throws NodeNotFoundException {
        Node node = this.nodes.get(nodeName);

        if (node == null) {
            throw new NodeNotFoundException("Node with name '" + nodeName + "' not found.");
        }

        return node;
    }
}