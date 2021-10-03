package pl.glownia.pamela;

class Customer {
    private final int id;
    private final String name;

    Customer(int id, String name) {
        this.name = name;
        this.id = id;
    }

    @Override
    public String toString() {
        return id + ". " + name;
    }
}
