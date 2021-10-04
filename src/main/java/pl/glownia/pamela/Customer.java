package pl.glownia.pamela;

class Customer {
    private final int id;
    private final String name;
    private final int carId;

    Customer(int id, String name, int carId) {
        this.name = name;
        this.id = id;
        this.carId = carId;
    }

    @Override
    public String toString() {
        return id + ". " + name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
