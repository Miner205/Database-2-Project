package com.project.artconnect.dao.impl;

import com.project.artconnect.model.Artist;

import java.util.List;
import java.sql.*;
import java.util.Optional;

/**
 * Data Access Object for Artist entity.
 */
public interface ArtistDao {
    Optional<Artist> findById(Connection connection, int id);

    List<Artist> findAll(Connection connection);

    void save(Artist artist, Connection connection);

    void update(Artist artist, Connection connection);

    void delete(String artistName, Connection connection);

    List<Artist> findByCity(String city, Connection connection);
}
