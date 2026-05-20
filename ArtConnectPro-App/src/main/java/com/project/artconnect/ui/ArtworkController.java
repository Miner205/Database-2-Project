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

import java.util.List;

public class ArtworkController {
    @FXML
    private TableView<Artwork> artworkTable;
    @FXML
    private TableColumn<Artwork, String> titleColumn;
    @FXML
    private TableColumn<Artwork, String> typeColumn;
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

    private final ArtworkService artworkService = ServiceProvider.getArtworkService();

    @FXML
    public void initialize() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
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

        artworkTable.setItems(FXCollections.observableArrayList(artworkService.getAllArtworks()));
    }
}
