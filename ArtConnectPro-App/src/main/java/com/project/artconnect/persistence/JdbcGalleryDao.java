package com.project.artconnect.persistence;

import com.project.artconnect.dao.impl.GalleryDao;
import com.project.artconnect.model.Artist;
import com.project.artconnect.model.Gallery;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class JdbcGalleryDao implements GalleryDao {
    @Override
    public Optional<Gallery> findById(Connection connection, int id) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Galleries WHERE gallery_id = ?");
            statement.setInt(1, id);
            ResultSet galleryData = statement.executeQuery();
            String name = galleryData.getString("name");
            String address = galleryData.getString("address");
            double rating = galleryData.getDouble("rating");
            String ownerName = galleryData.getString("owner_name");
            String contactPhone = galleryData.getString("contact_phone");
            String website = galleryData.getString("website");
            Gallery new_record = new Gallery(name, address, rating);
            new_record.setGalleryId(id);
            new_record.setOwnerName(ownerName);
            new_record.setContactPhone(contactPhone);
            new_record.setWebsite(website);
            return Optional.of(new_record);
        } catch (SQLException sqlException) {
            return Optional.empty();
        }
    }

    @Override
    public List<Gallery> findAll(Connection connection) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Galleries");
            ResultSet galleryData = statement.executeQuery();
            List<Gallery> galleries = new ArrayList<>();
            while (galleryData.next()) {
                int galleryId = galleryData.getInt("gallery_id");
                String name = galleryData.getString("name");
                String address = galleryData.getString("address");
                String ownerName = galleryData.getString("owner_name");
                String contactPhone = galleryData.getString("contact_phone");
                double rating = galleryData.getDouble("rating");
                String website = galleryData.getString("website");
                Gallery new_record = new Gallery(name, address, rating);
                new_record.setGalleryId(galleryId);
                new_record.setOwnerName(ownerName);
                new_record.setContactPhone(contactPhone);
                new_record.setWebsite(website);
                galleries.add(new_record);
            }
            return galleries;
        } catch (SQLException sqlException) {
            return new ArrayList<>();
        }
    }

}
