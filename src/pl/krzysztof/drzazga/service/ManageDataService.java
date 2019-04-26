package pl.krzysztof.drzazga.service;

import pl.krzysztof.drzazga.model.Leaf;
import pl.krzysztof.drzazga.model.Vertex;
import pl.krzysztof.drzazga.view.TreePanel;

import java.awt.*;
import java.util.ArrayList;

public class ManageDataService {
    private ArrayList<Leaf> elements;
    private double entropy;
    private double average;

    public TreePanel getPanel() {
        return panel;
    }

    public void setPanel(TreePanel panel) {
        this.panel = panel;
    }

    private TreePanel panel;
    public ManageDataService() {
        this.elements = new ArrayList<>();
        this.entropy = 0.0;
        this.average = 0.0;
        prepareLeafsList();
        this.panel = new TreePanel();
    }

    public ArrayList<Leaf> getElements() {
        return elements;
    }

    public void setElements(ArrayList<Leaf> elements) {
        this.elements = elements;
    }

    public double getEntropy() {
        return entropy;
    }

    public void setEntropy(double entropy) {
        this.entropy = entropy;
    }

    public double getAverage() {
        return average;
    }

    public void setAverage(double average) {
        this.average = average;
    }

    public void prepareLeafsList(){
        this.elements = new ArrayList<>();
        Leaf nyt = new Leaf(null);
        nyt.setCount(0);
        this.elements.add(nyt);
        this.panel = new TreePanel();
    }


    public void addLetter(char key){
        Leaf leaf = elements.stream().filter((current) ->
                Character.toString(key).equals(current.getSign())
        ).findFirst().orElse(null);
        if (leaf == null) {
            leaf = new Leaf(Character.toString(key));
            leaf.setCount(1);
            elements.add(leaf);
            addNewNode(leaf);
        } else {
            leaf.setCount(leaf.getCount() + 1);
            updateTree(leaf.getUpperElement());
        }
    }

    public void addNewNode(Leaf leaf) {
        Vertex min = elements.stream().filter(current -> current.getCount() == 0).findFirst().get();
        Vertex vertex = new Vertex();
        Vertex upper = min.getUpperElement();
        if(upper!= null){
            upper.setLeftElement(vertex);
        }
        vertex.setUpperElement(upper);
        vertex.setLeftElement(min);
        vertex.setRightElement(leaf);
        vertex.setCount(1);
        leaf.setUpperElement(vertex);
        min.setUpperElement(vertex);
        updateTree(vertex.getUpperElement());
    }

    public void updateTree(Vertex vertex) {
        if (vertex == null)
            return;
        vertex.setCount(vertex.getCount() + 1);
        if (vertex.getRightElement().getCount() < vertex.getLeftElement().getCount()) {
            Vertex helper = vertex.getLeftElement();
            vertex.setLeftElement(vertex.getRightElement());
            vertex.setRightElement(helper);
        }
        updateTree(vertex.getUpperElement());
    }

    public Vertex getRoot() {
        Vertex vertex = elements.stream().filter(current -> current.getCount() == 0).findFirst().get();
        while (vertex.getUpperElement() != null) {
            vertex = vertex.getUpperElement();
        }
        return vertex;
    }

    public void calculateCodes(Vertex vertex, String code, int count, int min, int max, Object parentVertex){
        if(vertex == null)
            return;
        int y = (vertex.getUpperElement()!=null)?vertex.getUpperElement().getPoint().y:0;
        vertex.setPoint(new Point((min+max/2),y+100));
        vertex.setCode(code);
        Object parent = this.panel.addVertex(vertex, parentVertex);
        if (vertex instanceof Leaf && vertex.getCount()>0) {
            Leaf leaf = (Leaf) vertex;
            double probability = (leaf.getCount()*1.0)/(count);
            leaf.setProbability(probability);
            this.setEntropy(this.getEntropy()+probability * Math.log(1/probability)/Math.log(2));
            this.setAverage(this.getAverage()+probability* leaf.getCode().length());
        } else {
            calculateCodes(vertex.getRightElement(), vertex.getCode()+"1", count, (max+min)/2, max, parent);
            calculateCodes(vertex.getLeftElement(), vertex.getCode()+"0", count, 0, (max+min)/2, parent);
        }
    }
}
