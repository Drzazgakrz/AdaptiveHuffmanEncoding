package pl.krzysztof.drzazga.service;

import pl.krzysztof.drzazga.model.Leaf;
import pl.krzysztof.drzazga.model.Vertex;
import pl.krzysztof.drzazga.view.TreePanel;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
public class ManageDataService {
    private ArrayList<Vertex> elements;
    private double entropy;
    private double average;
    private Comparator<Vertex> comparator;

    public TreePanel getPanel() {
        return panel;
    }

    private TreePanel panel;

    public ManageDataService() {
        this.elements = new ArrayList<>();
        this.entropy = 0.0;
        this.average = 0.0;
        prepareLeafsList();
        this.panel = new TreePanel();
        this.comparator = new VertexComparator();
    }

    public ArrayList<Vertex> getElements() {
        return elements;
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

    public void prepareLeafsList() {
        this.elements = new ArrayList<>();
        Leaf nyt = new Leaf(null);
        nyt.setCount(0);
        this.elements.add(nyt);
        this.panel = new TreePanel();
    }


    public void addLetter(char key) {
        Leaf leaf = (Leaf) elements.stream().filter((current) ->
                current instanceof Leaf && Character.toString(key).equals(((Leaf) current).getSign())
        ).findFirst().orElse(null);
        if (leaf == null) {
            leaf = new Leaf(Character.toString(key));
            leaf.setCount(1);
            elements.add(leaf);
            addNewNode(leaf);
        } else {
            leaf.setCount(leaf.getCount() + 1);
        }
        updateTree(leaf);
    }

    private void addNewNode(Leaf leaf) {
        Vertex min = elements.stream().filter(current -> current instanceof Leaf && current.getCount()==0).findFirst().get();
        Vertex vertex = new Vertex();
        Vertex upper = min.getUpperElement();
        int level = 0;
        if (upper != null) {
            upper.setLeftElement(vertex);
            level = upper.getLevel() + 1;
        }
        vertex.setUpperElement(upper);
        vertex.setLeftElement(min);
        vertex.setRightElement(leaf);
        vertex.setLevel(level);
        leaf.setUpperElement(vertex);
        leaf.setLevel(level + 1);
        min.setUpperElement(vertex);
        min.setLevel(level + 1);
        elements.add(vertex);
    }

    private void swapInUpperElement(Vertex current, Vertex newUpper, Vertex newElement) {
        if (current.equals(newUpper.getLeftElement())) {
            newUpper.setLeftElement(newElement);
        } else {
            newUpper.setRightElement(newElement);
        }
    }

    private void swap(Vertex current, Vertex newMin) {
        if (current == null || newMin == null)
            return;
        Vertex minUpper = current.getUpperElement();
        Vertex newUpper = newMin.getUpperElement();
        current.setUpperElement(newUpper);
        newMin.setUpperElement(minUpper);

        int levelHelper = newMin.getLevel();
        newMin.setLevel(current.getLevel());
        current.setLevel(levelHelper);

        swapInUpperElement(current, minUpper, newMin);
        swapInUpperElement(newMin, newUpper, current);
    }

    private Vertex getNext(Vertex current) {
        return elements.stream().filter(element -> element.getCount() < current.getCount() && element.getLevel() < current.getLevel())
                .max(comparator).orElse(null);
    }

    private void updateTree(Vertex vertex) {
        Vertex correct = this.getNext(vertex);
        if (correct != null && correct.getLevel()>0) {
            swap(vertex, correct);
        }

        Vertex upper = vertex.getUpperElement();
        if(upper!= null){
            swapVertically(upper);
            upper.setCount(upper.getLeftElement().getCount()+upper.getRightElement().getCount());
            this.updateTree(upper);
        }
    }

    private void swapVertically(Vertex upper){
        if(upper.getRightElement().getCount()<upper.getLeftElement().getCount()){
            swap(upper.getRightElement(), upper.getLeftElement());
        }
    }

    public Vertex getRoot() {
        return elements.stream().filter(element -> !(element instanceof Leaf)).max(comparator).orElse(null);
    }

    public void calculateCodes(Vertex vertex, String code, int count, int min, int max, Object parentVertex) {
        if (vertex == null)
            return;
        int y = (vertex.getUpperElement() != null) ? vertex.getUpperElement().getPoint().y : 0;
        vertex.setPoint(new Point((min + max / 2), y + 100));
        vertex.setCode(code);
        Object parent = this.panel.addVertex(vertex, parentVertex);
        if (vertex instanceof Leaf && vertex.getCount() > 0) {
            Leaf leaf = (Leaf) vertex;
            double probability = (leaf.getCount() * 1.0) / (count);
            leaf.setProbability(probability);
            this.setEntropy(this.getEntropy() + probability * Math.log(1 / probability) / Math.log(2));
            this.setAverage(this.getAverage() + probability * leaf.getCode().length());
        } else {
            calculateCodes(vertex.getRightElement(), vertex.getCode() + "1", count, (max + min) / 2, max, parent);
            calculateCodes(vertex.getLeftElement(), vertex.getCode() + "0", count, 0, (max + min) / 2, parent);
        }
    }
}
