package com.ttzv.item.ui;

import com.ttzv.item.entity.CommandBox;
import com.ttzv.item.entity.CommandItem;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import ttzv.uiUtils.CommandNode;

import javax.naming.NamingException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class CommandBoxScn extends AnchorPane {

    @FXML
    private TextField searchField;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private VBox commandList;

    @FXML
    void addAction(ActionEvent event) {
        boolean lastAddedNodeTitleIsEmpty = commandNodelist.size() != 0 && commandNodelist.get(commandNodelist.size() - 1).getTitle().isEmpty();
        if (!lastAddedNodeTitleIsEmpty) {
            CommandNode commandNode = new CommandNode();
            String nextUid = String.valueOf(commandBox.getNextUid());
            if(nextUid == null) nextUid = "-1";
            commandNode.setUid(nextUid);
            commandNodelist.add(commandNode);
            addDeleteBtnAction(commandNode);
            addUpdateBtnAction(commandNode);
            refreshList();
            scrollToBottom();
        } else {
            scrollToBottom();
        }
    }

    @FXML
    public void initialize(){
        for (CommandItem c : commandBox.getAll()) {
            addCommandNode(c);
        }
        refreshList();
        addSearchFieldListener();
    }

    @FXML
    void refresh(ActionEvent event) {
        try {
            commandBox.reload();
        } catch (SQLException | IOException | NamingException | GeneralSecurityException e) {
            e.printStackTrace();
        }
        commandNodelist.clear();
        for (CommandItem c : commandBox.getAll()) {
            addCommandNode(c);
        }
        refreshList();
    }

    private CommandBox commandBox;
    private ObservableList<CommandNode> commandNodelist;

    public CommandBoxScn(CommandBox commandBox){
        this.commandBox = commandBox;
        commandNodelist = FXCollections.observableList(new ArrayList<>());
        ResourceBundle langResourceBundle = ResourceBundle.getBundle("lang");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/commandboxscn.fxml"), langResourceBundle);
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

    private void deleteitem(String uid) throws IOException, SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Potwierdzenie usunięcia");
        alert.setHeaderText("Czy na pewno chcesz usunąć ten element?");
        alert.setContentText("Element zostanie nieodwracalnie usunięty.");
        Optional<ButtonType> result = alert.showAndWait();

        if(result.get() == ButtonType.OK) {
            CommandItem commandItem = new CommandItem(uid);
            commandBox.remove(commandItem);
            commandNodelist.removeIf(p -> p.getUid().equals(uid));
            refreshList();
        }
    }

    private void addSearchFieldListener(){
        searchField.textProperty().addListener((observableValue, s, t1) -> {
            commandList.getChildren().setAll(commandNodelist.filtered(c -> c.getTitle().contains(t1.toLowerCase().trim()) || c.getTagsFieldText().contains(t1.toLowerCase().trim())));
        });
    }

    private void addDeleteBtnAction(CommandNode commandNode){
        commandNode.getBtnDelete().setOnAction(e -> {
            String uid = commandNode.getUid();
            try {
                deleteitem(uid);
            } catch (IOException | SQLException ex) {
                ex.printStackTrace();
            }
        });
    }

    private void addUpdateBtnAction(CommandNode commandNode){
        commandNode.getBtnUpdate().setOnAction(e -> {
            CommandItem commandItem = new CommandItem(commandNode.getUid(), commandNode.getTitle().trim(), commandNode.getContent().trim(), commandNode.getTagsFieldText().trim());
            try {
                commandBox.update(commandItem);
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
        commandNode.setUid(commandItem.getUid());
        addDeleteBtnAction(commandNode);
        addUpdateBtnAction(commandNode);
        this.commandNodelist.add(commandNode);
    }

    private void scrollToBottom(){
        Platform.runLater(() -> this.scrollPane.setVvalue(1.0));

    }

}
