package pl.glownia.pamela;

class Customer {
    private final int id;
    private final String name;
    private final int carId;
    private final int companyId;

    Customer(int id, String name, int carId, int companyId) {
        this.name = name;
        this.id = id;
        this.carId = carId;
        this.companyId = companyId;
    }

    @Override
    public String toString() {
        return id + ". " + name;
    }

    int getId() {
        return id;
    }

    int getCarId() {
        return carId;
    }

}
