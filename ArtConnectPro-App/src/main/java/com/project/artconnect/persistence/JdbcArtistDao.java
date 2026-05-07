package com.project.artconnect.persistence;

import com.project.artconnect.dao.ArtistDao;
import com.project.artconnect.model.Artist;

import java.sql.SQLException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * JDBC implementation for ArtistDao.
 * TODO: Students must implement this using JDBC and SQL.
 */
public class JdbcArtistDao implements ArtistDao {

    @Override
    public List<Artist> findAll(Connection connection) {
        // SELECT * FROM artist
        try {
            ResultSet resultData = connection.prepareStatement("SELECT * FROM Artists;").executeQuery();
            List<Artist> results = new ArrayList<>();
            while (resultData.next()) {
                int id = resultData.getInt("id");
                String name = resultData.getString("NAME");
                String bio = resultData.getString("BIO");
                int birthYear = resultData.getInt("BIRTH_YEAR");
                String contactEmail = resultData.getString("CONTACT_EMAIL");
                String contactPhone = resultData.getString("CONTACT_PHONE");
                String city = resultData.getString("CITY");
                boolean isActive  = resultData.getBoolean("IS_ACTIVE");
                Artist new_record = new Artist(name, bio, birthYear, contactEmail, city);
                new_record.setPhone(contactPhone);
                new_record.setActive(isActive);
                new_record.setId(id);
                results.add(new_record);
            }
            return results;
        } catch (SQLException ex) {
            Logger.getLogger(JdbcArtistDao.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public void save(Artist artist, Connection connection) {
        // TODO: Implement INSERT INTO artist(...) VALUES(...)
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO RESULTS VALUES (" + artist.getId() + ", " + artist.getName() + ", " + artist.getBio() + ", " + artist.getBirthYear() + "," + artist.getContactEmail() + "," + artist.getPhone()  + "," + artist.getCity() + ", " + artist.getWebsite() + ", " + artist.isActive() + ");");
            int resultData = statement.executeUpdate();
        } catch (SQLException sqlException) {
            System.out.println("Failed to insert artist : " +  sqlException.getMessage());
        }
    }

    @Override
    public void update(Artist artist, Connection connection) {
        // TODO: Implement UPDATE artist SET ... WHERE name = ?
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE Artists SET name =" + artist.getName() + ", bio =" + artist.getBio() + ", birth_year =" + artist.getBirthYear() + ", contact_email =" + artist.getContactEmail() + ", phone =" + artist.getPhone() + ", city =" + artist.getCity() + ", website =" + artist.getWebsite() + " WHERE id =" + artist.getId() + ";");
            int resultData = statement.executeUpdate();
        } catch (SQLException sqlException) {
            System.out.println("Failed to update artist : " +  sqlException.getMessage());
        }
    }

    @Override
    public void delete(String artistName, Connection connection) {
        // TODO: Implement DELETE FROM artist WHERE name = ?
        try {
            int resultData = connection.prepareStatement("DELETE FROM Artists WHERE NAME =" + artistName + ";").executeUpdate();
        } catch (SQLException sqlException) {
            System.out.println("Failed to delete artist : " +  sqlException.getMessage());
        }
        throw new UnsupportedOperationException("JDBC Implementation not yet provided.");
    }

    @Override
    public List<Artist> findByCity(String city, Connection connection) {
        // TODO: Implement SELECT * FROM artist WHERE city = ?
        try {
            ResultSet resultData = connection.prepareStatement("SELECT * FROM Artists WHERE city = " + city + ";").executeQuery();
            List<Artist> results = new ArrayList<>();
            while (resultData.next()) {
                int id = resultData.getInt("id");
                String name = resultData.getString("NAME");
                String bio = resultData.getString("BIO");
                int birthYear = resultData.getInt("BIRTH_YEAR");
                String contactEmail = resultData.getString("CONTACT_EMAIL");
                String contactPhone = resultData.getString("CONTACT_PHONE");
                city = resultData.getString("CITY");
                boolean isActive  = resultData.getBoolean("IS_ACTIVE");
                Artist new_record = new Artist(name, bio, birthYear, contactEmail, city);
                new_record.setPhone(contactPhone);
                new_record.setActive(isActive);
                new_record.setId(id);
                results.add(new_record);
            }
            return results;
        } catch (SQLException ex) {
            Logger.getLogger(JdbcArtistDao.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
