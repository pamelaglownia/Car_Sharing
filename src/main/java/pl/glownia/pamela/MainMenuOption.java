package pl.glownia.pamela;

enum MainMenuOption {
    MANAGER_LOGIN(1, "Log in as a manager"),
    CUSTOMER_LOGIN(2, "Log in as a customer"),
    EXIT(0, "Exit");

    private final int number;
    private final String text;

    MainMenuOption(int number, String text) {
        this.number = number;
        this.text = text;
    }

    private int getNumber() {
        return number;
    }

    private String getText() {
        return text;
    }

    static void printMainMenu() {
        for (MainMenuOption option : MainMenuOption.values()) {
            System.out.println(option.getNumber() + ". " + option.getText());
        }
    }

    static int checkUserDecision(int userDecision) {
        int decision = 0;
        for (MainMenuOption option : MainMenuOption.values()) {
            if (option.getNumber() == userDecision) {
                decision = option.getNumber();
            }
        }
        return decision;
    }
}