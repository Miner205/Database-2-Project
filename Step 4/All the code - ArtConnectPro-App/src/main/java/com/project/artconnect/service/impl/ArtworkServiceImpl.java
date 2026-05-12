package com.project.artconnect.service.impl;

import com.project.artconnect.util.ConnectionManager;
import com.project.artconnect.dao.impl.ArtworkDao;
import com.project.artconnect.model.Artist;
import com.project.artconnect.model.Artwork;
import com.project.artconnect.persistence.JdbcArtworkDao;
import com.project.artconnect.service.ArtworkService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ArtworkServiceImpl implements ArtworkService {
    private final ArtworkDao artworkDao;

    public ArtworkServiceImpl() {
        this.artworkDao = new JdbcArtworkDao();
    }

    @Override
    public List<Artwork> getAllArtworks() {
        try (Connection connection = ConnectionManager.getConnection()) {
            return artworkDao.findAll(connection);
        } catch (SQLException sqlException) {
            System.out.println("Failed to get artworks : " + sqlException.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public Optional<Artwork> getArtworkByTitle(String title) {
        if (title == null) return Optional.empty();
        try (Connection connection = ConnectionManager.getConnection()) {
            return artworkDao.findAll(connection)
                    .stream()
                    .filter(a -> title.equalsIgnoreCase(a.getTitle()))
                    .findFirst();
        } catch (SQLException sqlException) {
            System.out.println("Failed to get artwork by title : " + sqlException.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public List<Artwork> getArtworksByArtist(Artist artist) {
        if (artist == null) return new ArrayList<>();
        try (Connection connection = ConnectionManager.getConnection()) {
            return artworkDao.findByArtistName(connection, artist.getName());
        } catch (SQLException sqlException) {
            System.out.println("Failed to get artworks by artist : " + sqlException.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public void createArtwork(Artwork artwork) {
        if (artwork == null) return;
        try (Connection connection = ConnectionManager.getConnection()) {
            artworkDao.save(connection, artwork);
        } catch (SQLException sqlException) {
            System.out.println("Failed to create artwork : " + sqlException.getMessage());
        }
    }

    @Override
    public void updateArtwork(Artwork artwork) {
        if (artwork == null) return;
        try (Connection connection = ConnectionManager.getConnection()) {
            artworkDao.update(connection, artwork);
        } catch (SQLException sqlException) {
            System.out.println("Failed to update artwork : " + sqlException.getMessage());
        }
    }

    @Override
    public void deleteArtwork(String title) {
        if (title == null) return;
        try (Connection connection = ConnectionManager.getConnection()) {
            artworkDao.delete(connection, title);
        } catch (SQLException sqlException) {
            System.out.println("Failed to delete artwork : " + sqlException.getMessage());
        }
    }
}