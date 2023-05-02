package at.fhv.mme.graphs.elements;

public class Edge {
    private final Node firstNode;
    private final Node secondNode;
    private final int weight;

    public Edge(Node firstNode, Node secondNode, int weight) {
        this.firstNode = firstNode;
        this.secondNode = secondNode;
        this.weight = weight;
    }

    public Node getFirstNode() {
        return firstNode;
    }

    public Node getSecondNode() {
        return secondNode;
    }

    public int getWeight() {
        return weight;
    }
}