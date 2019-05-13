package pl.krzysztof.drzazga.service;

import pl.krzysztof.drzazga.model.Vertex;

import java.util.Comparator;

public class VertexComparator implements Comparator<Vertex> {
    @Override
    public int compare(Vertex o1, Vertex o2) {
        int count1 = o1==null?0:o1.getCount();
        int count2 = o2==null?0:o2.getCount();
        return Double.compare(count1, count2);
    }

    @Override
    public boolean equals(Object obj) {
        return false;
    }
}
