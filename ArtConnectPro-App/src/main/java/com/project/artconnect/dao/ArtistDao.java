package com.project.artconnect.dao;

import com.project.artconnect.model.Artist;
import java.util.List;
import java.sql.*;

/**
 * Data Access Object for Artist entity.
 */
public interface ArtistDao {
    List<Artist> findAll(Connection connection);

    void save(Artist artist, Connection connection);

    void update(Artist artist, Connection connection);

    void delete(String artistName, Connection connection);

    List<Artist> findByCity(String city, Connection connection);
}
