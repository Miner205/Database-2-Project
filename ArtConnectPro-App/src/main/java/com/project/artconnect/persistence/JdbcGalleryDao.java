package com.project.artconnect.persistence;

import com.project.artconnect.dao.impl.GalleryDao;
import com.project.artconnect.model.Artwork;
import com.project.artconnect.model.Exhibition;
import com.project.artconnect.model.Gallery;
import com.project.artconnect.model.OpeningHours;

import java.sql.*;
import java.time.LocalDate;
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
            if (galleryData.next()) {
                String name = galleryData.getString("name");
                String address = galleryData.getString("address");
                double rating = galleryData.getDouble("rating");
                String ownerName = galleryData.getString("owner_name");
                String contactPhone = galleryData.getString("contact_phone");
                String website = galleryData.getString("website");
                List<Exhibition> exhibitions = findExhibitions(connection, id);

                Gallery newGallery = new Gallery(name, address, rating);
                newGallery.setGalleryId(id);
                newGallery.setOwnerName(ownerName);
                newGallery.setContactPhone(contactPhone);
                newGallery.setWebsite(website);
                for (Exhibition exhibition : exhibitions) {
                    newGallery.addExhibition(exhibition);
                }
                return Optional.of(newGallery);
            }
            return Optional.empty();
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
                List<Exhibition> exhibitions = findExhibitions(connection, galleryId);

                Gallery newGallery = new Gallery(name, address, rating);
                newGallery.setGalleryId(galleryId);
                newGallery.setOwnerName(ownerName);
                newGallery.setContactPhone(contactPhone);
                newGallery.setWebsite(website);
                for (Exhibition exhibition : exhibitions) {
                    newGallery.addExhibition(exhibition);
                }
                galleries.add(newGallery);
            }
            return galleries;
        } catch (SQLException sqlException) {
            return new ArrayList<>();
        }
    }

    @Override
    public void save(Connection connection, Gallery gallery) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO Galleries (gallery_id, name, address, owner_name, contact_phone, rating, website) VALUES (?, ?, ?, ?, ?, ?, ?)");

            statement.setInt(1, gallery.getGalleryId());
            statement.setString(2, gallery.getName());
            statement.setString(3, gallery.getAddress());
            statement.setString(4, gallery.getOwnerName());
            statement.setString(5, gallery.getContactPhone());
            statement.setDouble(6, gallery.getRating());
            statement.setString(7, gallery.getWebsite());

            statement.executeUpdate();
        } catch (SQLException sqlException) {
            System.out.println("Failed to insert gallery : " + sqlException.getMessage());
        }
    }

    @Override
    public void update(Connection connection, Gallery gallery) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE Galleries SET name = ?, address = ?, owner_name = ?, contact_phone = ?, rating = ?, website = ? WHERE gallery_id = ?");

            statement.setString(1, gallery.getName());
            statement.setString(2, gallery.getAddress());
            statement.setString(3, gallery.getOwnerName());
            statement.setString(4, gallery.getContactPhone());
            statement.setDouble(5, gallery.getRating());
            statement.setString(6, gallery.getWebsite());
            statement.setInt(7, gallery.getGalleryId());

            statement.executeUpdate();
        } catch (SQLException sqlException) {
            System.out.println("Failed to update gallery : " + sqlException.getMessage());
        }
    }

    @Override
    public void delete(Connection connection, String name) {
        try {
            PreparedStatement idStatement = connection.prepareStatement("SELECT gallery_id FROM Galleries WHERE name = ?");
            idStatement.setString(1, name);
            ResultSet galleryData = idStatement.executeQuery();
            if (galleryData.next()) {
                int galleryId = galleryData.getInt("gallery_id");

                deleteExhibitions(connection, galleryId);

                try {
                    PreparedStatement statement = connection.prepareStatement("DELETE FROM Galleries WHERE name = ?");
                    statement.setString(1, name);

                    statement.executeUpdate();
                } catch (SQLException sqlException) {
                    System.out.println("Failed to delete gallery : " + sqlException.getMessage());
                }
            }

        } catch (SQLException sqlException) {
            System.out.println("Failed to find gallery : " + sqlException.getMessage());
        }
    }

    public List<Exhibition> findExhibitions(Connection connection, int galleryId) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Exhibitions e JOIN Gallery g ON e.gallery_id = g.gallery_id WHERE g.gallery_id = ?");
            statement.setInt(1, galleryId);
            ResultSet exhibitionData = statement.executeQuery();
            List<Exhibition> exhibitions = new ArrayList<>();
            JdbcExhibitionDao jdbcExhibitionDao = new JdbcExhibitionDao();
            while (exhibitionData.next()) {
                int exhibitionId = exhibitionData.getInt("exhibition_id");
                String title = exhibitionData.getString("title");
                LocalDate startDate = exhibitionData.getDate("start_date").toLocalDate();
                Date sqlEndDate = exhibitionData.getDate("end_date");
                LocalDate endDate;
                if (sqlEndDate != null) {
                    endDate = sqlEndDate.toLocalDate();
                } else {
                    endDate = null;
                }
                String description = exhibitionData.getString("description");
                String curatorName = exhibitionData.getString("curator_name");
                String theme = exhibitionData.getString("theme");
                List<OpeningHours> openingHours = jdbcExhibitionDao.findOpeningHours(connection, exhibitionId);
                List<Artwork> artworks = jdbcExhibitionDao.findArtworks(connection, exhibitionId);

                Exhibition newExhibition = new Exhibition(title, startDate, endDate, null);
                newExhibition.setExhibitionId(exhibitionId);
                newExhibition.setDescription(description);
                newExhibition.setCuratorName(curatorName);
                newExhibition.setTheme(theme);
                newExhibition.setOpeningHours(openingHours);
                newExhibition.setArtworks(artworks);
                exhibitions.add(newExhibition);
            }
            return exhibitions;
        } catch (SQLException sqlException) {
            return new ArrayList<>();
        }
    }

    public void deleteExhibitions(Connection connection, int galleryId) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT title FROM Exhibitions WHERE gallery_id = ?");
            statement.setInt(1, galleryId);
            ResultSet exhibitionData = statement.executeQuery();
            JdbcExhibitionDao jdbcExhibitionDao = new JdbcExhibitionDao();
            while (exhibitionData.next()) {
                jdbcExhibitionDao.delete(connection, exhibitionData.getString("title"));
            }
        } catch (SQLException sqlException) {
            System.out.println("Failed to find exhibition : " + sqlException.getMessage());
        }
    }

}
