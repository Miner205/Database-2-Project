package com.project.artconnect.ui;

import com.project.artconnect.model.Artwork;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ExhibitionController {
    @FXML
    private TableView<Exhibition> exhibitionTable;
    @FXML
    private TableColumn<Exhibition, String> titleColumn;
    @FXML
    private TableColumn<Exhibition, LocalDate> sdateColumn;
    @FXML
    private TableColumn<Exhibition, String> edateColumn;
    @FXML
    private TableColumn<Exhibition, String> themeColumn;
    @FXML
    private TableColumn<Exhibition, String> galleryColumn;
    @FXML
    private TableColumn<Exhibition, String> curatorColumn;
    @FXML
    private TableColumn<Exhibition, String> artworksColumn;
    @FXML
    private TableColumn<Exhibition, Void> deleteColumn;

    private final GalleryService galleryService = ServiceProvider.getGalleryService();

    @FXML
    public void initialize() {
        addDeleteButtonToTable();

        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        sdateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        edateColumn.setCellValueFactory(cellData -> {
            LocalDate endDate = cellData.getValue().getEndDate();
            if (endDate == null) {
                return new SimpleStringProperty("Permanent exhibition");
            } else {
                return new SimpleStringProperty(endDate.toString());
            }
        });
        themeColumn.setCellValueFactory(new PropertyValueFactory<>("theme"));
        galleryColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getGallery() != null ? cellData.getValue().getGallery().getName() : "Unknown"));
        curatorColumn.setCellValueFactory(new PropertyValueFactory<>("curatorName"));

        artworksColumn.setCellValueFactory(cellData -> {
            List<Artwork> artws = cellData.getValue().getArtworks();
            if (artws.isEmpty()) {
                return new SimpleStringProperty("no artworks");
            } else {
                StringBuilder sb = new StringBuilder();
                for (Artwork artw: artws) {
                    sb.append(artw.toString());
                    sb.append(", ");
                }
                String s = sb.substring(0, sb.length() - 2);
                return new SimpleStringProperty(s);
            }
        });

        refreshData();
    }

    private void refreshData() {
        List<Exhibition> all = new ArrayList<>();
        for (Gallery g : galleryService.getAllGalleries()) {
            all.addAll(g.getExhibitions());
        }
        exhibitionTable.setItems(FXCollections.observableArrayList(all));
    }

    private void addDeleteButtonToTable() {
        deleteColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");
            {
                deleteButton.setOnAction(event -> {
                    Exhibition exhibition = getTableView().getItems().get(getIndex());
                    galleryService.deleteExhibition(exhibition.getTitle());
                    refreshData();

                    //how to refresh onglet gallery ?

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
