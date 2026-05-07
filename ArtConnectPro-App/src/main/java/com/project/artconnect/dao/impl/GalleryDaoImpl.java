package com.project.artconnect.dao.impl;

import com.project.artconnect.dao.GalleryDao;
import com.project.artconnect.model.Gallery;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class GalleryDaoImpl implements GalleryDao {
    @Override
    public Optional<Gallery> findById(Connection connection, int id) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Galleries WHERE gallery_id = " + id + ";");
            ResultSet galleryData = preparedStatement.executeQuery();
            String name = galleryData.getString("name");
            String address = galleryData.getString("address");
            double rating = galleryData.getDouble("rating");
            return Optional.of(new Gallery(id, name, address, rating));
        } catch (SQLException sqlException) {
            return Optional.empty();
        }
    }

    @Override
    public List<Gallery> findAll(Connection connection) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Galleries;");
            ResultSet galleryData = preparedStatement.executeQuery();
            List<Gallery> galleries = new ArrayList<>();
            while (galleryData.next()) {
                int galleryId = galleryData.getInt("gallery_id");
                String name = galleryData.getString("name");
                String address = galleryData.getString("address");
                String ownerName = galleryData.getString("owner_name");
                String contactPhone = galleryData.getString("contact_phone");
                double rating = galleryData.getDouble("rating");
                String website = galleryData.getString("website");
                galleries.add(new Gallery(galleryId, name, address, ownerName, contactPhone, rating, website));
            }
            return galleries;
        } catch (SQLException sqlException) {
            return new ArrayList<>();
        }
    }

}
