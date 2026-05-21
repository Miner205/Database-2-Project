package com.project.artconnect.ui;

import com.project.artconnect.model.Exhibition;
import com.project.artconnect.model.Gallery;
import com.project.artconnect.model.Workshop;
import com.project.artconnect.service.GalleryService;
import com.project.artconnect.service.WorkshopService;
import com.project.artconnect.util.ServiceProvider;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.geometry.Insets;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DiscoverController {
    @FXML
    private FlowPane discoverPane;

    private final GalleryService galleryService = ServiceProvider.getGalleryService();
    private final WorkshopService workshopService = ServiceProvider.getWorkshopService();

    @FXML
    public void initialize() {
        refreshCards();
    }

    public void refreshCards() {
        discoverPane.getChildren().clear();
        // Collect some exhibitions from galleries
        List<Exhibition> featuredExhibitions = new ArrayList<>();
        for (Gallery g : galleryService.getAllGalleries()) {
            featuredExhibitions.addAll(g.getExhibitions());
            /*if (featuredExhibitions.size() >= 3)
                break;*/
        }
        featuredExhibitions.forEach(this::addExhibitionCard); // .stream().limit(3)
        workshopService.getAllWorkshops().forEach(this::addWorkshopCard); // .stream().limit(3)
    }

    private void addExhibitionCard(Exhibition e) {
        VBox card = new VBox(5);
        card.setPadding(new Insets(10));
        card.setStyle(
                "-fx-background-color: #e3f2fd; -fx-border-color: #2196f3; -fx-border-radius: 5; -fx-background-radius: 5;");
        card.setPrefWidth(250);
        card.getChildren().addAll(
                new Label(getExhibitionLabel(e)),
                new Label(e.getTitle()) {
                    {
                        setStyle("-fx-font-weight: bold;");
                    }
                },
                new Label("Theme: " + e.getTheme()),
                new Label("Gallery: " + (e.getGallery() != null ? e.getGallery().getName() : "Unknown")));
        discoverPane.getChildren().add(card);
    }

    private void addWorkshopCard(Workshop w) {
        VBox card = new VBox(5);
        card.setPadding(new Insets(10));
        card.setStyle(
                "-fx-background-color: #f1f8e9; -fx-border-color: #4caf50; -fx-border-radius: 5; -fx-background-radius: 5;");
        card.setPrefWidth(250);
        card.getChildren().addAll(
                new Label(getWorkshopLabel(w)),
                new Label(w.getTitle()) {
                    {
                        setStyle("-fx-font-weight: bold;");
                    }
                },
                new Label("Instructor: " + (w.getInstructor() != null ? w.getInstructor().getName() : "Unknown")),
                new Label("Price: $" + w.getPrice()));
        discoverPane.getChildren().add(card);
    }

    private String getExhibitionLabel(Exhibition e) {
        LocalDate now = LocalDate.now();
        if (e.getStartDate().isAfter(now)) {
            return "UPCOMING EXHIBITION";
        } else if (e.getEndDate() == null || !e.getEndDate().isBefore(now)) {
            return "CURRENT EXHIBITION";
        } else {
            return "PAST EXHIBITION";
        }
    }

    private String getWorkshopLabel(Workshop w) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = w.getDate();
        LocalDateTime end = start.plusMinutes(w.getDurationMinutes());
        if (start.isAfter(now)) {
            return "UPCOMING WORKSHOP";
        } else if (!start.isAfter(now) && end.isAfter(now)) {
            return "CURRENT WORKSHOP";
        } else {
            return "PAST WORKSHOP";
        }
    }
}
