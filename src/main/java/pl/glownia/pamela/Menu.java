package pl.glownia.pamela;

public class Menu {

    private final Input input = new Input();
    private final Printer printer = new Printer();
//    private final CarSharingJDBC database = new CarSharingJDBC();

    void runMenu() {
//        database.runDataBase();
        makeFirstDecision();
    }

    void makeFirstDecision() {
        printer.printMainMenu();
        int userDecision = input.takeUserDecision(0, 1);
        if (userDecision == 1) {
            makeManagerDecision();
        } else {
            System.exit(0);
        }
    }

    void makeManagerDecision() {
        printer.printManagerMenu();
        int userDecision = input.takeUserDecision(0, 2);
        switch (userDecision) {
            case 1:
                manageCompanyList();
                break;
            case 2:
                System.out.println("Creating company list...");
                break;
            default:
                makeFirstDecision();
        }
    }

    void manageCompanyList() {
        printer.printCompanyList();
        int userDecision = input.takeUserDecision(1, 3);
        switch (userDecision) {
            case 1:
                System.out.println("First...");
                break;
            case 2:
                System.out.println("Second...");
                break;
            case 3:
                System.out.println("Third...");
                break;
        }
    }
}
