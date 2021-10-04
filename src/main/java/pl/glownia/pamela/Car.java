package pl.glownia.pamela;

class Car {
    private final int id;
    private final String name;
    private final int companyId;

    Car(int id, String name, int companyId) {
        this.name = name;
        this.id = id;
        this.companyId = companyId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return id + ". " + name;
    }

}
