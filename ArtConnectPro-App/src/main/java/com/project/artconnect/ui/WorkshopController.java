package com.project.artconnect.ui;

import com.project.artconnect.model.Workshop;
import com.project.artconnect.service.WorkshopService;
import com.project.artconnect.util.ServiceProvider;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.time.LocalDateTime;

public class WorkshopController {
    @FXML
    private TableView<Workshop> workshopTable;
    @FXML
    private TableColumn<Workshop, String> titleColumn;
    @FXML
    private TableColumn<Workshop, String> dateColumn;
    @FXML
    private TableColumn<Workshop, String> instructorColumn;
    @FXML
    private TableColumn<Workshop, String> durationColumn;
    @FXML
    private TableColumn<Workshop, String> locationColumn;
    @FXML
    private TableColumn<Workshop, Double> priceColumn;
    @FXML
    private TableColumn<Workshop, String> levelColumn;
    @FXML
    private TableColumn<Workshop, String> nbParticipantsColumn;

    private final WorkshopService workshopService = ServiceProvider.getWorkshopService();

    @FXML
    public void initialize() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        levelColumn.setCellValueFactory(new PropertyValueFactory<>("level"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));

        instructorColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getInstructor() != null ? cellData.getValue().getInstructor().getName()
                        : "Unknown"));

        dateColumn.setCellValueFactory(cellData -> {
            LocalDateTime d = cellData.getValue().getDate();
            if (d == null) {
                return new SimpleStringProperty("no data");
            } else {
                return new SimpleStringProperty(d.toLocalDate().toString() + " at " + d.toLocalTime().toString());
            }
        });

        durationColumn.setCellValueFactory(cellData -> {
            int m = cellData.getValue().getDurationMinutes();
            if (m == 0) {
                return new SimpleStringProperty("no data");
            } else {
                return new SimpleStringProperty(m + " minutes");
            }
        });

        nbParticipantsColumn.setCellValueFactory(cellData -> {
            int nb = workshopService.nbMembersInWorkshop(cellData.getValue().getWorkshopId());
            if (nb == -1) {
                return new SimpleStringProperty("failed to get data");
            } else {
                return new SimpleStringProperty(nb + " / " + (cellData.getValue().getMaxParticipants() != 0 ? cellData.getValue().getMaxParticipants() : "no data"));
            }
        });

        workshopTable.setItems(FXCollections.observableArrayList(workshopService.getAllWorkshops()));
    }
}
