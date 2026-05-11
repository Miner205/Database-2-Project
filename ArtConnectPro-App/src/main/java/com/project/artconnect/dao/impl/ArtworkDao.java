package com.project.artconnect.dao.impl;

import com.project.artconnect.model.Artwork;

import java.sql.Connection;
import java.util.List;

public interface ArtworkDao {
    List<Artwork> findAll(Connection connection);

    void save(Connection connection, Artwork artwork);

    void update(Connection connection, Artwork artwork);

    void delete(Connection connection, String title);

    List<Artwork> findByArtistName(Connection connection, String artistName);
}
