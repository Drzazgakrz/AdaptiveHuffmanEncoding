package pl.krzysztof.drzazga.view;

import com.mxgraph.view.mxGraph;
import pl.krzysztof.drzazga.model.Vertex;

import javax.swing.*;


public class TreePanel extends JPanel {
    private mxGraph graph;

    public TreePanel() {
        this.graph = new mxGraph();
    }

    public Object addVertex(Vertex vertex, Object parentVertex){
        String label = vertex.toString();
        Object parent = graph.getDefaultParent();
        Object currentVertex = graph.insertVertex(parent, label, vertex, vertex.getPoint().getX(),
                vertex.getPoint().getY(), label.length()*7,15);
        graph.insertEdge(parent, "", vertex.getCode(),currentVertex, parentVertex);
        return currentVertex;
    }

    public mxGraph getGraph() {
        graph.getModel().endUpdate();
        return graph;
    }

    public void setGraph(mxGraph graph) {
        this.graph = graph;
    }
}
