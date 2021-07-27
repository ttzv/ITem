package com.ttzv.item.ui.controller;

import com.ttzv.item.entity.CommandBox;
import com.ttzv.item.service.CommandBoxService;
import com.ttzv.item.service.CommandBoxServiceImpl;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import ttzv.uiUtils.CommandNode;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class CommandBoxController extends AnchorPane {

    private CommandBoxService commandBoxService;

    @FXML
    private TextField searchField;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private VBox commandList;

    private ObservableList<CommandNode> commandNodelist;


    @FXML
    public void initialize(){
        commandNodelist = FXCollections.observableArrayList();
        commandBoxService = new CommandBoxServiceImpl();
        List<CommandBox> commandBoxList = commandBoxService.getAllCommands();
        for (CommandBox c : commandBoxList) {
            addCommandNode(c);
        }
        refreshList();
        addSearchFieldListener();
    }

    @FXML
    void addAction(ActionEvent event) {
        boolean lastAddedNodeTitleIsEmpty = commandNodelist.size() != 0 && commandNodelist.get(commandNodelist.size() - 1).getTitle().isEmpty();
        if (!lastAddedNodeTitleIsEmpty) {
            CommandNode commandNode = new CommandNode();
            String nextUid = String.valueOf(commandBoxService.getNextUid());
            commandNode.setUid(nextUid);
            CommandBox commandBox = new CommandBox();
            commandBox.setUid(nextUid);
            commandBoxService.saveCommand(commandBox);
            commandNodelist.add(commandNode);
            addDeleteBtnAction(commandNode, commandBox);
            addUpdateBtnAction(commandNode, commandBox);
            refreshList();
            scrollToBottom();
        } else {
            scrollToBottom();
        }
    }

    @FXML
    void refresh(ActionEvent event) {
        commandNodelist.clear();
        for (CommandBox c : commandBoxService.getAllCommands()) {
            addCommandNode(c);
        }
        refreshList();
    }

    private void refreshList(){
        commandList.getChildren().setAll(commandNodelist);
    }

    private void deleteItem(String uid) throws IOException, SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm delete");
        alert.setHeaderText("Do you wish to delete this element?");
        alert.setContentText("This element will be permanently deleted");
        Optional<ButtonType> result = alert.showAndWait();
        result.ifPresent(buttonType -> {
            if(result.get() == ButtonType.OK) {
                commandBoxService.deleteCommand(uid);
                commandNodelist.removeIf(p -> p.getUid().equals(uid));
                refreshList();
            }
        });

    }

    private void addSearchFieldListener(){
        searchField.textProperty().addListener((observableValue, s, t1) -> {
            commandList.getChildren().setAll(commandNodelist.filtered(c -> {
                String title = c.getTitle();
                String tags = c.getTagsFieldText();
                boolean titleContains = title.contains(t1.toLowerCase().trim());
                if(tags != null) return titleContains || tags.contains(t1.toLowerCase().trim());
                return titleContains;
            }));
        });
    }

    private void addDeleteBtnAction(CommandNode commandNode, CommandBox commandBox){
        commandNode.getBtnDelete().setOnAction(e -> {
            String uid = commandNode.getUid();
            try {
                deleteItem(uid);
            } catch (IOException | SQLException ex) {
                ex.printStackTrace();
            }
        });
    }

    private void addUpdateBtnAction(CommandNode commandNode, CommandBox commandBox){
        commandNode.getBtnUpdate().setOnAction(e -> {
            commandBox.setUid(commandNode.getUid());
            commandBox.setTitle(commandNode.getTitle().trim());
            commandBox.setContent(commandNode.getContent().trim());
            commandBox.setTags(commandNode.getTagsFieldText().trim());
            commandBoxService.updateCommand(commandBox);
        });
    }

    private void addCommandNode(CommandBox commandBox){
        CommandNode commandNode = new CommandNode();
        commandNode.setTitleField(commandBox.getTitle());
        commandNode.setContent(commandBox.getContent());
        commandNode.setTagsField(commandBox.getTags());
        commandNode.setUid(commandBox.getUid());
        addDeleteBtnAction(commandNode, commandBox);
        addUpdateBtnAction(commandNode, commandBox);
        this.commandNodelist.add(commandNode);
    }

    private void scrollToBottom(){
        Platform.runLater(() -> this.scrollPane.setVvalue(1.0));
    }

}
