package pl.glownia.pamela;

class Car {
    private final int id;
    private final String name;
    private final int companyId;
    private final boolean isAvailable;

    Car(int id, String name, int companyId, boolean isAvailable) {
        this.name = name;
        this.id = id;
        this.companyId = companyId;
        this.isAvailable = isAvailable;
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

    public boolean isAvailable() {
        return isAvailable;
    }

    public int getCompanyId() {
        return companyId;
    }
}
