package pl.krzysztof.drzazga.model;

import java.util.Objects;

public class Leaf extends Vertex{
    private String sign;



    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public Leaf(String sign) {
        super();
        this.sign = sign;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Leaf that = (Leaf) o;
        return sign == that.sign;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sign);
    }

    @Override
    public String toString(){
        return (" ".equals(sign)?"space":sign)+":"+this.code;
    }
}
