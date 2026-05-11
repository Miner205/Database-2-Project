package com.project.artconnect.service.impl;

import com.project.artconnect.util.ConnectionManager;
import com.project.artconnect.dao.impl.ArtistDao;
import com.project.artconnect.model.Artist;
import com.project.artconnect.model.Discipline;
import com.project.artconnect.persistence.JdbcArtistDao;
import com.project.artconnect.service.ArtistService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ArtistServiceImpl implements ArtistService {
    private final ArtistDao artistDao;

    public ArtistServiceImpl() {
        this.artistDao = new JdbcArtistDao();
    }

    @Override
    public List<Artist> getAllArtists() {
        try (Connection connection = ConnectionManager.getConnection()) {
            return artistDao.findAll(connection);
        } catch (SQLException sqlException) {
            System.out.println("Failed to get artists : " + sqlException.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public Optional<Artist> getArtistByName(String name) {
        return getAllArtists().stream()
                .filter(a -> a.getName().equalsIgnoreCase(name))
                .findFirst();
    }

    @Override
    public void createArtist(Artist artist) {
        try (Connection connection = ConnectionManager.getConnection()) {
            artistDao.save(artist, connection);
        } catch (SQLException sqlException) {
            System.out.println("Failed to create artist : " + sqlException.getMessage());
        }
    }

    @Override
    public void updateArtist(Artist artist) {
        try (Connection connection = ConnectionManager.getConnection()) {
            artistDao.update(artist, connection);
        } catch (SQLException sqlException) {
            System.out.println("Failed to update artist : " + sqlException.getMessage());
        }
    }

    @Override
    public void deleteArtist(String name) {
        try (Connection connection = ConnectionManager.getConnection()) {
            artistDao.delete(name, connection);
        } catch (SQLException sqlException) {
            System.out.println("Failed to delete artist : " + sqlException.getMessage());
        }
    }

    @Override
    public List<Discipline> getAllDisciplines() {

        return getAllArtists().stream()
                .flatMap(a -> a.getDisciplines().stream())
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public List<Artist> searchArtists(String query, String disciplineName, String city) {
        List<Artist> artists = getAllArtists();
        return artists.stream()
                .filter(a -> query == null || a.getName().toLowerCase().contains(query.toLowerCase()))
                .filter(a -> city == null || city.isEmpty() || a.getCity().equalsIgnoreCase(city))
                .filter(a -> disciplineName == null
                        || a.getDisciplines().stream().anyMatch(d -> d.getName().equals(disciplineName)))
                .collect(Collectors.toList());
    }
}