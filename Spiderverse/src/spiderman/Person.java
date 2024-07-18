package spiderman;

public class Person {
    
    private int dimension_number;
    private String name;
    private int signature;

    public Person(int dim, String name, int sign) {
        this.dimension_number = dim;
        this.name = name;
        this.signature = sign;
    }


    public int getDimension() {
        return dimension_number;
    }

    public void setDimension(int d) {
        dimension_number = d;
    }
    
    public String getName(){
        return name;
    }

    public int getSignature() {
        return signature;
    }
}
