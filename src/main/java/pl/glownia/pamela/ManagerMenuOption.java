package pl.glownia.pamela;

enum ManagerMenuOption {
    COMPANY_LIST(1, "Company list"),
    COMPANY_CREATOR(2, "Create a company"),
    COMPANY_DELETION(3, "Delete a company"),
    CUSTOMER_CREATOR(4, "Create a customer"),
    CUSTOMER_DELETION(5, "Delete a customer"),
    BACK(0,"Back");

    private final int number;
    private final String text;

    ManagerMenuOption(int number, String text) {
        this.number = number;
        this.text = text;
    }

    private int getNumber() {
        return number;
    }

    private String getText() {
        return text;
    }

    static void printManagerMenu() {
        for (ManagerMenuOption option : ManagerMenuOption.values()) {
            System.out.println(option.getNumber() + ". " + option.getText());
        }
    }

    static int checkUserDecision(int userDecision) {
        int decision = 0;
        for (ManagerMenuOption option : ManagerMenuOption.values()) {
            if (option.getNumber() == userDecision) {
                decision = option.getNumber();
            }
        }
        return decision;
    }
}