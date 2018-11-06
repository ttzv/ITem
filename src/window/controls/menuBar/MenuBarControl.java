package window.controls.menuBar;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

import java.util.HashMap;

public class MenuBarControl {

    private final MenuBar menuBar;
    private HashMap<String, Menu> menusMap;
    private HashMap<String, HashMap<String, MenuItem>> menuItemsMap;

    public MenuBarControl() {
        this.menuBar = new MenuBar();
        this.menuItemsMap = new HashMap<>();
        this.menusMap = new HashMap<>();
    }

    /**
     * Use this method to gain access to created MenuBar (e.g. for use in UI)
     * @return New customized MenuBar
     */
    public MenuBar getMenuBar(){
        buildMenuBar();
        return menuBar;
    }

    private void createMenu(String name){
        Menu menu = new Menu(name);
        menusMap.put(name, menu);
    }

    /**
     * Creates new MenuItem
     * @param nameItem name (identifier) of MenuItem
     * @return new MenuItem
     */
    private MenuItem createMenuItem(String nameItem){
        return new MenuItem(nameItem);
    }

    private void buildMenuBar(){
        buildMenu();
        this.menuBar.getMenus().addAll(menusMap.values());
    }

    /**
     * Main method for building menuBar, creation of components happens here
     */
    private void buildMenu(){
        createMenu("Options");
        addItemToMenu("Options","Settings");
    }

    /**
     * Add new item to existing menu
     * @param menu
     * @param item
     */
    private void addItemToMenu(String menu, String item){
        if(menusMap.keySet().contains(menu)){
            if(menuItemsMap.keySet().contains(menu)) {
                menuItemsMap.get(menu).put(item, createMenuItem(item));
            }else{
                HashMap<String, MenuItem> itemsMap = new HashMap<>();
                itemsMap.put(item, createMenuItem(item));
                menuItemsMap.put(menu, itemsMap);
            }
            menusMap.get(menu).getItems().add(menuItemsMap.get(menu).get(item));
        }else{
            System.out.println("Menu" + menu + "doesn't exist, please create it first");
        }
    }

    /**
     * Method used for getting specific MenuItem, used mainly for adding handlers.
     * @param item name of item
     * @param menu name of menu
     * @return MenuItem form specific menu
     */
    public MenuItem getItemFromMenu(String item, String menu){
        if(menuItemsMap.containsKey(menu)){
            if(menuItemsMap.get(menu).containsKey(item)){
                return menuItemsMap.get(menu).get(item);
            }else
                System.out.println("no such item exists");
            return null;
        }else{
            System.out.println("no such menu exists");
            return null;
        }
    }


}
