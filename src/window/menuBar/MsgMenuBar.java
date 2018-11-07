package window.menuBar;

import window.controls.menuBar.MenuBarControl;

public class MsgMenuBar extends MenuBarControl {


    @Override
    public void buildMenu() {
        createMenu("Opcje");
        addItemToMenu("Opcje", "Dodaj");
        addItemToMenu("Opcje", "Ustawienia");
    }
}
