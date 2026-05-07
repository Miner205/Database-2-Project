package com.project.artconnect.dao;

import com.project.artconnect.model.Gallery;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public interface GalleryDao {
    Optional<Gallery> findById(Connection connection, int id);

    List<Gallery> findAll(Connection connection);
}
