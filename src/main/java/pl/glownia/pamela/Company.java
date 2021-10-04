package pl.glownia.pamela;

class Company {
    private final int id;
    private final String name;

    Company(int id, String name) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return id + ". " + name;
    }

    public int getId() {
        return id;
    }
}
