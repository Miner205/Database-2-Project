package com.project.artconnect.dao.impl;

import com.project.artconnect.model.Artwork;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public interface ArtworkDao {
    Optional<Artwork> findById(Connection connection, int id);

    List<Artwork> findAll(Connection connection);

    void save(Connection connection, Artwork artwork);

    void update(Connection connection, Artwork artwork);

    void delete(Connection connection, String title);

    List<Artwork> findByArtistName(Connection connection, String artistName);

}
