package com.project.artconnect.ui;

import com.project.artconnect.model.Exhibition;
import com.project.artconnect.model.Gallery;
import com.project.artconnect.service.GalleryService;
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
import java.util.Objects;

public class GalleryController {
    @FXML
    private TableView<Gallery> galleryTable;
    @FXML
    private TableColumn<Gallery, String> nameColumn;
    @FXML
    private TableColumn<Gallery, String> addressColumn;
    @FXML
    private TableColumn<Gallery, String> ratingColumn;
    @FXML
    private TableColumn<Gallery, String> ownerColumn;
    @FXML
    private TableColumn<Gallery, String> phoneColumn;
    @FXML
    private TableColumn<Gallery, String> websiteColumn;
    @FXML
    private TableColumn<Gallery, String> exhibitionsColumn;
    @FXML
    private TableColumn<Gallery, Void> deleteColumn;

    private final GalleryService galleryService = ServiceProvider.getGalleryService();

    private DiscoverController discoverController;
    private ExhibitionController exhibitionController;

    @FXML
    public void initialize() {
        addDeleteButtonToTable();

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        ratingColumn.setCellValueFactory(cellData -> {
            double rating = cellData.getValue().getRating();
            if (rating == 0) {
                return new SimpleStringProperty("Not rated");
            } else {
                return new SimpleStringProperty(rating + "/5.0");
            }
        });
        ownerColumn.setCellValueFactory(new PropertyValueFactory<>("ownerName"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("contactPhone"));

        websiteColumn.setCellValueFactory(cellData -> {
            String website = cellData.getValue().getWebsite();
            return new SimpleStringProperty(Objects.requireNonNullElse(website, "no website"));
        });

        exhibitionsColumn.setCellValueFactory(cellData -> {
            List<Exhibition> exhibitions = cellData.getValue().getExhibitions();
            if (exhibitions.isEmpty()) {
                return new SimpleStringProperty("no exhibitions");
            } else {
                StringBuilder sb = new StringBuilder();
                for (Exhibition exhibition: exhibitions) {
                    sb.append(exhibition.toString());
                    sb.append(", ");
                }
                String s = sb.substring(0, sb.length() - 2);
                return new SimpleStringProperty(s);
            }
        });

        refreshTable();
    }

    public void setDiscoverController(DiscoverController discoverController) {
        this.discoverController = discoverController;
    }

    public void setExhibitionController(ExhibitionController exhibitionController) {
        this.exhibitionController = exhibitionController;
    }

    public void refreshTable() {
        galleryTable.setItems(FXCollections.observableArrayList(galleryService.getAllGalleries()));
    }

    private void addDeleteButtonToTable() {
        deleteColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");
            {
                deleteButton.setOnAction(event -> {
                    Gallery gallery = getTableView().getItems().get(getIndex());
                    galleryService.deleteGallery(gallery.getName());
                    refreshTable();
                    discoverController.refreshCards();
                    exhibitionController.refreshData();
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
