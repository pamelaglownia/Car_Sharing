package pl.glownia.pamela;

enum CompanyMenuOption {
    COMPANY_LIST(1, "Company list"),
    COMPANY_CREATOR(2, "Create a company"),
    BACK(0, "Back");

    private final int number;
    private final String text;

    CompanyMenuOption(int number, String text) {
        this.number = number;
        this.text = text;
    }

    private int getNumber() {
        return number;
    }

    private String getText() {
        return text;
    }

    static void printCompanyMenu() {
        for (CompanyMenuOption option : CompanyMenuOption.values()) {
            System.out.println(option.getNumber() + ". " + option.getText());
        }
    }

    static int checkUserDecision(int userDecision) {
        int decision = 0;
        for (CompanyMenuOption option : CompanyMenuOption.values()) {
            if (option.getNumber() == userDecision) {
                decision = option.getNumber();
            }
        }
        return decision;
    }
}