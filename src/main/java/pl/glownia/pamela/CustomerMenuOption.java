package pl.glownia.pamela;

enum CustomerMenuOption {
    CAR_RENTING(1, "Rent a car"),

    CAR_RETURNING(2, "Return a rented car"),

    RENTED_CAR_INFO(3, "My rented car"),

    BACK(0, "Back");

    private final int number;
    private final String text;

    CustomerMenuOption(int number, String text) {
        this.number = number;
        this.text = text;
    }

    private int getNumber() {
        return number;
    }

    private String getText() {
        return text;
    }

    static void printCustomerMenu() {
        for (CustomerMenuOption option : CustomerMenuOption.values()) {
            System.out.println(option.getNumber() + ". " + option.getText());
        }
    }

    static int checkUserDecision(int userDecision) {
        int decision = 0;
        for (CustomerMenuOption option : CustomerMenuOption.values()) {
            if (option.getNumber() == userDecision) {
                decision = option.getNumber();
            }
        }
        return decision;
    }
}