package pl.glownia.pamela;

enum CarMenuOption {

    CAR_LIST(1, "Car list"),
    CAR_CREATOR(2, "Create a car"),
    CAR_DELETION(3, "Delete a car"),
    BACK(0, "Back");

    private final int number;
    private final String text;

    CarMenuOption(int number, String text) {
        this.number = number;
        this.text = text;
    }

    private int getNumber() {
        return number;
    }

    private String getText() {
        return text;
    }

    static void printCarMenu() {
        for (CarMenuOption option : CarMenuOption.values()) {
            System.out.println(option.getNumber() + ". " + option.getText());
        }
    }

    static int checkUserDecision(int userDecision) {
        int decision = 0;
        for (CarMenuOption option : CarMenuOption.values()) {
            if (option.getNumber() == userDecision) {
                decision = option.getNumber();
            }
        }
        return decision;
    }
}