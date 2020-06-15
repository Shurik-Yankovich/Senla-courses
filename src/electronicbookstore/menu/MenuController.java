package electronicbookstore.menu;

import java.util.Scanner;

public class MenuController {

    private Builder builder;
    private Navigator navigator;

    private Scanner scanner;

    public MenuController() {
        builder = new Builder();
        navigator = new Navigator();
        scanner = new Scanner(System.in);
    }

    public void run() {
        int choice;
        Menu currentMenu;

        builder.buildMenu();
        navigator.setCurrentMenu(builder.getRootMenu());
        while (true) {
            navigator.printMenu();
            choice = scanner.nextInt();
            currentMenu = navigator.getCurrentMenu();
            if (choice >= 0 && choice < currentMenu.getMenuItems().length) {
                navigator.navigate(choice);
                if (currentMenu.getName().equals(navigator.getCurrentMenu().getName())) {
                    navigator.setCurrentMenu(builder.getRootMenu());
                }
            } else {
                System.out.println("Неверно выбран пункт меню!\nПожалуйста выбирете нужный пункт меню:\n");
            }
        }
    }
}
