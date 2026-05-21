package com.project.artconnect.ui;

import com.project.artconnect.model.Artwork;
import com.project.artconnect.model.ArtworkTag;
import com.project.artconnect.model.Dimension;
import com.project.artconnect.service.ArtworkService;
import com.project.artconnect.util.ServiceProvider;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableCell;
import javafx.scene.control.Button;

import java.util.List;

public class ArtworkController {
    @FXML
    private TableView<Artwork> artworkTable;
    @FXML
    private TableColumn<Artwork, String> titleColumn;
    @FXML
    private TableColumn<Artwork, String> typeColumn;
    @FXML
    private TableColumn<Artwork, String> mediumColumn;
    @FXML
    private TableColumn<Artwork, Double> priceColumn;
    @FXML
    private TableColumn<Artwork, String> statusColumn;
    @FXML
    private TableColumn<Artwork, String> artistColumn;
    @FXML
    private TableColumn<Artwork, Integer> yearColumn;
    @FXML
    private TableColumn<Artwork, String> dimensionsColumn;
    @FXML
    private TableColumn<Artwork, String> tagsColumn;
    @FXML
    private TableColumn<Artwork, Void> deleteColumn;

    private final ArtworkService artworkService = ServiceProvider.getArtworkService();

    private ArtistController artistController;
    private ExhibitionController exhibitionController;

    @FXML
    public void initialize() {
        addDeleteButtonToTable();

        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        mediumColumn.setCellValueFactory(new PropertyValueFactory<>("medium"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        dimensionsColumn.setCellValueFactory(cellData -> {
            Dimension dim = cellData.getValue().getDimensions();
            if (dim == null) {
                return new SimpleStringProperty("no data");
            } else {
                return new SimpleStringProperty(dim.toString());
            }
        });

        tagsColumn.setCellValueFactory(cellData -> {
            List<ArtworkTag> tags = cellData.getValue().getTags();
            if (tags.isEmpty()) {
                return new SimpleStringProperty("no tags");
            } else {
                StringBuilder sb = new StringBuilder();
                for (ArtworkTag tag: tags) {
                    sb.append(tag.toString());
                    sb.append(", ");
                }
                String s = sb.substring(0, sb.length() - 2);
                return new SimpleStringProperty(s);
            }
        });

        artistColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getArtist() != null ? cellData.getValue().getArtist().getName() : "Unknown"));

        yearColumn.setCellValueFactory(new PropertyValueFactory<>("creationYear"));

        refreshTable();
    }

    public void setArtistController(ArtistController artistController) {
        this.artistController = artistController;
    }

    public void setExhibitionController(ExhibitionController exhibitionController) {
        this.exhibitionController = exhibitionController;
    }

    public void refreshTable() {
        artworkTable.setItems(FXCollections.observableArrayList(artworkService.getAllArtworks()));
    }

    private void addDeleteButtonToTable() {
        deleteColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");
            {
                deleteButton.setOnAction(event -> {
                    Artwork artwork = getTableView().getItems().get(getIndex());
                    artworkService.deleteArtwork(artwork.getTitle());
                    refreshTable();
                    artistController.refreshTable();
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
