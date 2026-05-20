package com.project.artconnect.dao.impl;

import com.project.artconnect.model.Gallery;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public interface GalleryDao {
    Optional<Gallery> findById(Connection connection, int id);

    List<Gallery> findAll(Connection connection);

    void save(Connection connection, Gallery gallery);

    void update(Connection connection, Gallery gallery);

    void delete(Connection connection, String name);

}
