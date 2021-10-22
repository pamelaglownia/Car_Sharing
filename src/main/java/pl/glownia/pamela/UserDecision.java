package pl.glownia.pamela;

class UserDecision {

    private final Input input = new Input();

    String getDataBaseName() {
        return input.getDataBaseFileName();
    }

    int chooseOptionFromInitialMenu() {
        MainMenuOption.printMainMenu();
        return MainMenuOption.checkUserDecision(input.takeUserDecision(0, 2));
    }

    int chooseOptionFromManagerMenu() {
        ManagerMenuOption.printManagerMenu();
        return ManagerMenuOption.checkUserDecision(input.takeUserDecision(0, 5));
    }

    int chooseOptionFromCarMenu() {
        CarMenuOption.printCarMenu();
        return CarMenuOption.checkUserDecision(input.takeUserDecision(0, 3));
    }

    int chooseOptionFromCustomerMenu() {
        CustomerMenuOption.printCustomerMenu();
        return CustomerMenuOption.checkUserDecision(input.takeUserDecision(0, 3));
    }
}