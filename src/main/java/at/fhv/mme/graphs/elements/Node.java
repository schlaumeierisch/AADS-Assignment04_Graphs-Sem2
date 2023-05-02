package at.fhv.mme.graphs.elements;

import java.util.LinkedList;

public class Node {
    private final String name;
    private final LinkedList<Edge> edges;

    public Node(String name) {
        this.name = name;
        this.edges = new LinkedList<>();
    }

    public String getName() {
        return name;
    }

    public LinkedList<Edge> getEdges() {
        return edges;
    }

    public void addEdge(Edge edge) {
        edges.add(edge);
    }
}
