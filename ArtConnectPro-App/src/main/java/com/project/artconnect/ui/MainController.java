package com.project.artconnect.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.application.Platform;

import java.io.IOException;

public class MainController {
    @FXML
    private TabPane mainTabPane;

    @FXML
    public void initialize() throws IOException {
        // Initialization logic if needed
        FXMLLoader discoverLoader = new FXMLLoader(getClass().getResource("/com/project/artconnect/ui/DiscoverTab.fxml"));
        Parent discoverTabContent = discoverLoader.load();
        DiscoverController discoverController = discoverLoader.getController();

        FXMLLoader artistLoader = new FXMLLoader(getClass().getResource("/com/project/artconnect/ui/ArtistsTab.fxml"));
        Parent artistTabContent = artistLoader.load();
        ArtistController artistController = artistLoader.getController();

        FXMLLoader artworkLoader = new FXMLLoader(getClass().getResource("/com/project/artconnect/ui/ArtworksTab.fxml"));
        Parent artworkTabContent = artworkLoader.load();
        ArtworkController artworkController = artworkLoader.getController();

        FXMLLoader galleryLoader = new FXMLLoader(getClass().getResource("/com/project/artconnect/ui/GalleriesTab.fxml"));
        Parent galleryTabContent = galleryLoader.load();
        GalleryController galleryController = galleryLoader.getController();

        FXMLLoader exhibitionLoader = new FXMLLoader(getClass().getResource("/com/project/artconnect/ui/ExhibitionsTab.fxml"));
        Parent exhibitionTabContent = exhibitionLoader.load();
        ExhibitionController exhibitionController = exhibitionLoader.getController();

        FXMLLoader workshopLoader = new FXMLLoader(getClass().getResource("/com/project/artconnect/ui/WorkshopsTab.fxml"));
        Parent workshopTabContent = workshopLoader.load();
        WorkshopController workshopController = workshopLoader.getController();

        FXMLLoader communityLoader = new FXMLLoader(getClass().getResource("/com/project/artconnect/ui/CommunityTab.fxml"));
        Parent communityTabContent = communityLoader.load();
        CommunityController communityController = communityLoader.getController();

        artistController.setDiscoverController(discoverController);
        artistController.setArtworkController(artworkController);
        artistController.setExhibitionController(exhibitionController);
        artistController.setWorkshopController(workshopController);

        artworkController.setArtistController(artistController);
        artworkController.setExhibitionController(exhibitionController);

        galleryController.setDiscoverController(discoverController);
        galleryController.setExhibitionController(exhibitionController);

        exhibitionController.setDiscoverController(discoverController);
        exhibitionController.setGalleryController(galleryController);

        workshopController.setDiscoverController(discoverController);

        Tab discoverTab = new Tab("Discover", discoverTabContent);
        Tab artistTab = new Tab("Artists", artistTabContent);
        Tab artworkTab = new Tab("Artworks", artworkTabContent);
        Tab galleryTab = new Tab("Galleries", galleryTabContent);
        Tab exhibitionTab = new Tab("Exhibitions", exhibitionTabContent);
        Tab workshopTab = new Tab("Workshops", workshopTabContent);
        Tab communityTab = new Tab("Community", communityTabContent);

        mainTabPane.getTabs().add(discoverTab);
        mainTabPane.getTabs().add(artistTab);
        mainTabPane.getTabs().add(artworkTab);
        mainTabPane.getTabs().add(galleryTab);
        mainTabPane.getTabs().add(exhibitionTab);
        mainTabPane.getTabs().add(workshopTab);
        mainTabPane.getTabs().add(communityTab);

    }

    @FXML
    private void handleExit() {
        Platform.exit();
    }
}
