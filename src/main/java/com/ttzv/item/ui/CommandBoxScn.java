package com.ttzv.item.ui;

import com.ttzv.item.entity.CommandItem;
import com.ttzv.item.entity.EntityDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableListBase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import ttzv.uiUtils.CommandNode;

import javax.naming.NamingException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CommandBoxScn extends AnchorPane {

    @FXML
    private TextField searchField;

    @FXML
    private VBox commandList;


    @FXML
    void addAction(ActionEvent event) {
            CommandNode commandNode = new CommandNode();
            commandNodelist.add(commandNode);
            addDeleteBtnAction(commandNode);
            addUpdateBtnAction(commandNode);
        refreshList();
    }


    @FXML
    public void initialize(){
        for (CommandItem c : commandItemList) {
            addCommandNode(c);
        }
        refreshList();
        addSearchFieldListener();
    }


    private EntityDAO<CommandItem> entityDAOcmddb;
    private List<CommandItem> commandItemList;
    private ObservableList<CommandNode> commandNodelist;


    public CommandBoxScn(EntityDAO<CommandItem> entityDAOcmddb) throws SQLException, NamingException, GeneralSecurityException, IOException {
        this.entityDAOcmddb = entityDAOcmddb;
        commandItemList = entityDAOcmddb.getAllEntities();
        commandNodelist = FXCollections.observableList(new ArrayList<>());

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/commandboxscn.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void refreshList(){
        commandList.getChildren().setAll(commandNodelist);
    }

    private void deleteitem(String title){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Potwierdzenie usunięcia");
        alert.setHeaderText("Czy na pewno chcesz usunąć ten element?");
        alert.setContentText("Element zostanie nieodwracalnie usunięty.");
        Optional<ButtonType> result = alert.showAndWait();

        if(result.get() == ButtonType.OK) {
            CommandItem commandItem = new CommandItem(title);
            try {
                entityDAOcmddb.deleteEntity(commandItem);
            } catch (SQLException | IOException ex) {
                ex.printStackTrace();
            }
            commandNodelist.removeIf(p -> p.getTitle().equals(title));
            refreshList();
        }
    }

    private void addSearchFieldListener(){
        searchField.textProperty().addListener((observableValue, s, t1) -> {
            commandList.getChildren().setAll(commandNodelist.filtered(c -> c.getTitle().contains(t1)));
        });
    }

    private void addDeleteBtnAction(CommandNode commandNode){
        commandNode.getBtnDelete().setOnAction(e -> {
            String title = commandNode.getTitle();
            if(!title.isEmpty()) {
                deleteitem(title);
            }
        });
    }

    private void addUpdateBtnAction(CommandNode commandNode){
        commandNode.getBtnUpdate().setOnAction(e -> {
            CommandItem commandItem = new CommandItem(commandNode.getTitle(), commandNode.getContent(), commandNode.getTagsFieldText());
            try {
                this.entityDAOcmddb.updateEntity(commandItem);
            } catch (SQLException | IOException ex) {
                ex.printStackTrace();
            }
        });
    }

    private void addCommandNode(CommandItem commandItem){
        CommandNode commandNode = new CommandNode();
        commandNode.setTitleField(commandItem.getCommandTitle());
        commandNode.setContent(commandItem.getCommandContents());
        commandNode.setTagsField(commandItem.getTags());
        addDeleteBtnAction(commandNode);
        addUpdateBtnAction(commandNode);
        this.commandNodelist.add(commandNode);
    }

    /*
    private CommandItem getCommandItem(String title){

    }
    */



}
