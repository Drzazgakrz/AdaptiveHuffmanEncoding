package pl.krzysztof.drzazga.model;

import java.awt.*;
import java.io.Serializable;

public class Vertex implements Comparable<Vertex>, Serializable {
    protected Vertex leftElement;
    protected Vertex rightElement;
    protected Point point;
    protected int count;
    protected Vertex upperElement;
    protected double probability;
    protected String code;

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public Vertex getUpperElement() {
        return upperElement;
    }

    public void setUpperElement(Vertex upperElement) {
        this.upperElement = upperElement;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Vertex getLeftElement() {
        return leftElement;
    }

    public void setLeftElement(Vertex leftElement) {
        this.leftElement = leftElement;
    }

    public Vertex getRightElement() {
        return rightElement;
    }

    public void setRightElement(Vertex rightElement) {
        this.rightElement = rightElement;
    }

    public double getProbability() {
        return probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public int compareTo(Vertex o) {
        return Double.compare(this.getCount(), o.getCount());
    }

    public Vertex() {
        this.code="";
        this.count = 1;
    }

    @Override
    public String toString(){
        return this.code;
    }
}
