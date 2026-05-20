package com.project.artconnect.ui;

import com.project.artconnect.model.Artist;
import com.project.artconnect.model.Discipline;
import com.project.artconnect.model.SocialMedia;
import com.project.artconnect.service.ArtistService;
import com.project.artconnect.util.ServiceProvider;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;
import java.util.Objects;

public class ArtistController {
    @FXML
    private TextField searchField;
    @FXML
    private ComboBox<Discipline> disciplineFilter;
    @FXML
    private TableView<Artist> artistTable;
    @FXML
    private TableColumn<Artist, String> nameColumn;
    @FXML
    private TableColumn<Artist, String> cityColumn;
    @FXML
    private TableColumn<Artist, String> emailColumn;
    @FXML
    private TableColumn<Artist, String> websiteColumn;
    @FXML
    private TableColumn<Artist, Integer> yearColumn;
    @FXML
    private TableColumn<Artist, String> phoneColumn;
    @FXML
    private TableColumn<Artist, Boolean> activeColumn;
    @FXML
    private TableColumn<Artist, String> disciplinesColumn;
    @FXML
    private TableColumn<Artist, String> mediasColumn;
    @FXML
    private TableColumn<Artist, Void> deleteColumn;

    private final ArtistService artistService = ServiceProvider.getArtistService();

    @FXML
    public void initialize() {
        addDeleteButtonToTable();

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        cityColumn.setCellValueFactory(new PropertyValueFactory<>("city"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("contactEmail"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("birthYear"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        activeColumn.setCellValueFactory(new PropertyValueFactory<>("isActive"));

        websiteColumn.setCellValueFactory(cellData -> {
            String website = cellData.getValue().getWebsite();
            return new SimpleStringProperty(Objects.requireNonNullElse(website, "no website"));
        });

        disciplinesColumn.setCellValueFactory(cellData -> {
            List<Discipline> disciplines = cellData.getValue().getDisciplines();
            if (disciplines.isEmpty()) {
                return new SimpleStringProperty("no disciplines");
            } else {
                StringBuilder sb = new StringBuilder();
                for (Discipline discipline: disciplines) {
                    sb.append(discipline.toString());
                    sb.append(", ");
                }
                String s = sb.substring(0, sb.length() - 2);
                return new SimpleStringProperty(s);
            }
        });

        mediasColumn.setCellValueFactory(cellData -> {
            List<SocialMedia> medias = cellData.getValue().getSocialMedias();
            if (medias.isEmpty()) {
                return new SimpleStringProperty("no social medias");
            } else {
                StringBuilder sb = new StringBuilder();
                for (SocialMedia media: medias) {
                    sb.append(media.toString());
                    sb.append(", ");
                }
                String s = sb.substring(0, sb.length() - 2);
                return new SimpleStringProperty(s);
            }
        });

        disciplineFilter.setItems(FXCollections.observableArrayList(artistService.getAllDisciplines()));
        refreshTable();
    }

    @FXML
    private void handleSearch() {
        String query = searchField.getText();
        Discipline d = disciplineFilter.getValue();
        String dName = (d != null) ? d.getName() : null;
        artistTable.setItems(FXCollections.observableArrayList(artistService.searchArtists(query, dName, null)));
    }

    @FXML
    private void handleReset() {
        searchField.clear();
        disciplineFilter.setValue(null);
        refreshTable();
    }

    private void refreshTable() {
        artistTable.setItems(FXCollections.observableArrayList(artistService.getAllArtists()));
    }

    private void addDeleteButtonToTable() {
        deleteColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");
            {
                deleteButton.setOnAction(event -> {
                    Artist artist = getTableView().getItems().get(getIndex());
                    artistService.deleteArtist(artist.getName());
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
