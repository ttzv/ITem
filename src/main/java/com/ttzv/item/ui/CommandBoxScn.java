package com.ttzv.item.ui;

import com.ttzv.item.entity.CommandItem;
import com.ttzv.item.entity.EntityDAO;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableListBase;
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
import java.util.*;
import java.util.stream.Collectors;

public class CommandBoxScn extends AnchorPane {

    @FXML
    private TextField searchField;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private VBox commandList;

    @FXML
    void addAction(ActionEvent event) {
        boolean lastAddedNodeTitleIsEmpty = commandNodelist.get(commandNodelist.size() - 1).getTitle().isEmpty();
        if (!lastAddedNodeTitleIsEmpty) {
            CommandNode commandNode = new CommandNode();
            commandNode.setUid(getNextUid());
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
        for (CommandItem c : commandItemList) {
            addCommandNode(c);
        }
        refreshList();
        addSearchFieldListener();
    }

    @FXML
    void refresh(ActionEvent event) {
        try {
            commandItemList = entityDAOcmddb.getAllEntities();
        } catch (SQLException | IOException | NamingException | GeneralSecurityException e) {
            e.printStackTrace();
        }
        commandNodelist.clear();
        for (CommandItem c : commandItemList) {
            addCommandNode(c);
        }
        refreshList();
    }

    private EntityDAO<CommandItem> entityDAOcmddb;
    private List<CommandItem> commandItemList;
    private List<Integer> commandItemUidList;
    private ObservableList<CommandNode> commandNodelist;



    public CommandBoxScn(EntityDAO<CommandItem> entityDAOcmddb) throws SQLException, NamingException, GeneralSecurityException, IOException {
        this.entityDAOcmddb = entityDAOcmddb;
        commandItemList = entityDAOcmddb.getAllEntities();
        commandItemUidList = commandItemList.stream().map(c -> Integer.parseInt(c.getUid())).collect(Collectors.toList());
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

    private void deleteitem(String uid){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Potwierdzenie usunięcia");
        alert.setHeaderText("Czy na pewno chcesz usunąć ten element?");
        alert.setContentText("Element zostanie nieodwracalnie usunięty.");
        Optional<ButtonType> result = alert.showAndWait();

        if(result.get() == ButtonType.OK) {
            CommandItem commandItem = new CommandItem(uid);
            if(commandItemUidList.contains(Integer.parseInt(commandItem.getUid()))) {
                try {
                    entityDAOcmddb.deleteEntity(commandItem);
                } catch (SQLException | IOException ex) {
                    ex.printStackTrace();
                }
            }
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
            deleteitem(uid);
        });
    }

    private void addUpdateBtnAction(CommandNode commandNode){
        commandNode.getBtnUpdate().setOnAction(e -> {
            CommandItem commandItem = new CommandItem(commandNode.getUid(), commandNode.getTitle().trim(), commandNode.getContent().trim(), commandNode.getTagsFieldText().trim());
                try {
                    this.entityDAOcmddb.updateEntity(commandItem);
                } catch (SQLException | IOException ex) {
                    ex.printStackTrace();
                }
                commandItemList.add(commandItem);
                commandItemUidList.add(Integer.parseInt(commandItem.getUid()));
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
        this.commandItemUidList.add(Integer.parseInt(commandItem.getUid()));
    }

    private void scrollToBottom(){
        Platform.runLater(() -> this.scrollPane.setVvalue(1.0));

    }

    private String getNextUid(){
        Collections.sort(commandItemUidList);
        return String.valueOf(commandItemUidList.get(commandItemUidList.size()-1) + 1);
    }

}
