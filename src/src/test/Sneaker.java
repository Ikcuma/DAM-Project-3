package test;

public class Sneaker {
    private String brand;
    private String model;
    private double price;

    public Sneaker(String brand, String model, double price) {
        this.brand = brand;
        this.model = model;
        this.price = price;
    }

    public void isTheSneakerequal(){
        if(this.brand.equals(this.model)){
            System.out.println("Sneaker equal");
        } else {
            System.out.println("Sneaker not equal");
        }
    }

    @Override
    public String toString() {
        return "Sneaker{" +
                "brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", price=" + price +
                '}';
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public double getPrice() {
        return price;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
