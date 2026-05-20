package com.project.artconnect.ui;

import com.project.artconnect.model.CommunityMember;
import com.project.artconnect.service.CommunityService;
import com.project.artconnect.util.ServiceProvider;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class CommunityController {
    @FXML
    private TableView<CommunityMember> memberTable;
    @FXML
    private TableColumn<CommunityMember, String> nameColumn;
    @FXML
    private TableColumn<CommunityMember, String> emailColumn;
    @FXML
    private TableColumn<CommunityMember, String> cityColumn;
    @FXML
    private TableColumn<CommunityMember, Integer> yearColumn;
    @FXML
    private TableColumn<CommunityMember, String> phoneColumn;
    @FXML
    private TableColumn<CommunityMember, String> membershipTypeColumn;
    @FXML
    private TableColumn<CommunityMember, Void> deleteColumn;

    private final CommunityService communityService = ServiceProvider.getCommunityService();

    @FXML
    public void initialize() {
        addDeleteButtonToTable();

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        cityColumn.setCellValueFactory(new PropertyValueFactory<>("city"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("birthYear"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        membershipTypeColumn.setCellValueFactory(new PropertyValueFactory<>("membershipType"));

        refreshTable();
    }

    private void refreshTable() {
        memberTable.setItems(FXCollections.observableArrayList(communityService.getAllMembers()));
    }

    private void addDeleteButtonToTable() {
        deleteColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");
            {
                deleteButton.setOnAction(event -> {
                    CommunityMember member = getTableView().getItems().get(getIndex());
                    communityService.deleteCommunityMember(member.getName());
                    refreshTable();
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        });
    }
}
