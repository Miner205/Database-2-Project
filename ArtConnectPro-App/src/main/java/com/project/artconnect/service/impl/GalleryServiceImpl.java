package com.project.artconnect.service.impl;

import com.project.artconnect.config.DatabaseConfig;
import com.project.artconnect.dao.impl.ExhibitionDao;
import com.project.artconnect.dao.impl.GalleryDao;
import com.project.artconnect.model.Exhibition;
import com.project.artconnect.model.Gallery;
import com.project.artconnect.persistence.JdbcExhibitionDao;
import com.project.artconnect.persistence.JdbcGalleryDao;
import com.project.artconnect.service.GalleryService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class GalleryServiceImpl implements GalleryService {
    private final GalleryDao galleryDao;
    private final ExhibitionDao exhibitionDao;

    public GalleryServiceImpl() {
        this.galleryDao = new JdbcGalleryDao();
        this.exhibitionDao = new JdbcExhibitionDao() {
        };
    }

    @Override
    public List<Gallery> getAllGalleries() {
        try (Connection connection = DatabaseConfig.getConnection()) {
            List<Gallery> galleries = galleryDao.findAll(connection);
            List<Exhibition> exhibitions = exhibitionDao.findAll(connection);
            for (Gallery gallery : galleries) {
                List<Exhibition> galleryExhibitions = exhibitions.stream()
                        .filter(e ->
                                e.getGallery() != null &&
                                        e.getGallery().getGalleryId() == gallery.getGalleryId()
                        )
                        .collect(Collectors.toList());
                gallery.setExhibitions(galleryExhibitions);
            }
            return galleries;
        } catch (SQLException sqlException) {
            System.out.println("Failed to get galleries : " + sqlException.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public Optional<Gallery> getGalleryByName(String name) {
        return getAllGalleries().stream()
                .filter(g -> g.getName().equalsIgnoreCase(name))
                .findFirst();
    }

    @Override
    public List<Exhibition> getExhibitionsByGallery(Gallery gallery) {
        if (gallery == null) {
            return new ArrayList<>();
        }
        try (Connection connection = DatabaseConfig.getConnection()) {
            return exhibitionDao.findAll(connection).stream()
                    .filter(e ->
                            e.getGallery() != null &&
                                    e.getGallery().getGalleryId() == gallery.getGalleryId()
                    )
                    .collect(Collectors.toList());
        } catch (SQLException sqlException) {
            System.out.println("Failed to get exhibitions : " + sqlException.getMessage());
            return new ArrayList<>();
        }
    }
}