package pl.krzysztof.drzazga.model;

import java.awt.*;
import java.io.Serializable;

public class Vertex implements Comparable<Vertex>, Serializable {
    private Vertex leftElement;
    private Vertex rightElement;
    protected Point point;
    protected int count;
    protected Vertex upperElement;
    protected double probability;
    protected String code;
    protected int level;

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
        //setCurrentCount();
    }

    public Vertex getRightElement() {
        return rightElement;
    }

    public void setRightElement(Vertex rightElement) {
        this.rightElement = rightElement;
        //setCurrentCount();
    }
    /*public void setCurrentCount() {
        int count1 = this.getRightElement() == null ? 0 : this.getRightElement().getCount();
        int count2 = this.getLeftElement() == null ? 0 : this.getLeftElement().getCount();
        int sum = count1 + count2;
        if (sum != count)
            this.count = sum;
        else return;
        if (upperElement != null) {
            upperElement.setCurrentCount();
        }
    }*/

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

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Vertex() {
        this.code = "";
        this.count = 1;
    }

    @Override
    public String toString() {
        return this.code + ":" + this.getCount();
    }
}
