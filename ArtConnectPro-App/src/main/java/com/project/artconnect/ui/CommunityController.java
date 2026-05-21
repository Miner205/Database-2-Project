package com.project.artconnect.ui;

import com.project.artconnect.model.Booking;
import com.project.artconnect.model.CommunityMember;
import com.project.artconnect.model.Discipline;
import com.project.artconnect.model.Review;
import com.project.artconnect.service.CommunityService;
import com.project.artconnect.util.ServiceProvider;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

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
    private TableColumn<CommunityMember, String> favDisciplinesColumn;
    @FXML
    private TableColumn<CommunityMember, String> bookingsColumn;
    @FXML
    private TableColumn<CommunityMember, String> reviewsColumn;
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

        favDisciplinesColumn.setCellValueFactory(cellData -> {
            List<Discipline> favDisciplines = cellData.getValue().getFavoriteDisciplines();
            if (favDisciplines.isEmpty()) {
                return new SimpleStringProperty("no favorite disciplines");
            } else {
                StringBuilder sb = new StringBuilder();
                for (Discipline favDiscipline: favDisciplines) {
                    sb.append(favDiscipline.toString());
                    sb.append(", ");
                }
                String s = sb.substring(0, sb.length() - 2);
                return new SimpleStringProperty(s);
            }
        });

        bookingsColumn.setCellValueFactory(cellData -> {
            List<Booking> bookings = cellData.getValue().getBookings();
            if (bookings.isEmpty()) {
                return new SimpleStringProperty("no bookings");
            } else {
                StringBuilder sb = new StringBuilder();
                for (Booking booking: bookings) {
                    sb.append(booking.getWorkshop().toString());
                    sb.append(", ");
                }
                String s = sb.substring(0, sb.length() - 2);
                return new SimpleStringProperty(s);
            }
        });

        reviewsColumn.setCellValueFactory(cellData -> {
            List<Review> reviews = cellData.getValue().getReviews();
            if (reviews.isEmpty()) {
                return new SimpleStringProperty("no reviews");
            } else {
                return new SimpleStringProperty(reviews.size() + " review" + (reviews.size() == 1 ? "" : "s"));
            }
        });

        refreshTable();
    }

    public void refreshTable() {
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
