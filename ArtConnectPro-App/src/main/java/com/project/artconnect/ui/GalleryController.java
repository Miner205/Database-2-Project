package com.project.artconnect.ui;

import com.project.artconnect.model.Gallery;
import com.project.artconnect.service.GalleryService;
import com.project.artconnect.util.ServiceProvider;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

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

    private final GalleryService galleryService = ServiceProvider.getGalleryService();

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        ratingColumn.setCellValueFactory(cellData -> {
            Double rating = cellData.getValue().getRating();
            if (rating == null) {
                return new SimpleStringProperty("Not rated");
            } else {
                return new SimpleStringProperty(rating + "/5.0");
            }
        });
        ownerColumn.setCellValueFactory(new PropertyValueFactory<>("ownerName"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("contactPhone"));

        galleryTable.setItems(FXCollections.observableArrayList(galleryService.getAllGalleries()));
    }
}
