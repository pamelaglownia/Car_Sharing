package pl.glownia.pamela;

class Company {
    private final int id;
    private final String name;

    Company(int id, String name) {
        this.name = name;
        this.id = id;
    }

    @Override
    public String toString() {
        return id + ". " + name;
    }
}
