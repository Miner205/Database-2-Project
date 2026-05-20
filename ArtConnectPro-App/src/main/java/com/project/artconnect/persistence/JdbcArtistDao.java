package com.project.artconnect.persistence;

import com.project.artconnect.dao.impl.ArtistDao;
import com.project.artconnect.model.Artist;
import com.project.artconnect.model.Discipline;
import com.project.artconnect.model.SocialMedia;

import java.sql.SQLException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * JDBC implementation for ArtistDao.
 * DONE: Students must implement this using JDBC and SQL.
 */
public class JdbcArtistDao implements ArtistDao {

    @Override
    public Optional<Artist> findById(Connection connection, int id) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Artists WHERE artist_id = ?");
            statement.setInt(1, id);
            ResultSet artistData = statement.executeQuery();
            if (artistData.next()) {
                String name = artistData.getString("name");
                String bio = artistData.getString("bio");
                int birthYear = artistData.getInt("birth_year");
                List<Discipline> disciplines = findDisciplines(connection, id);
                String contactEmail = artistData.getString("contact_email");
                String contactPhone = artistData.getString("phone");
                String city = artistData.getString("city");
                String website = artistData.getString("website");
                List<SocialMedia> socialMedias = findSocialMedias(connection, id);
                boolean isActive = artistData.getBoolean("is_active");

                Artist newArtist = new Artist(name, bio, birthYear, contactEmail, city);
                newArtist.setDisciplines(disciplines);
                newArtist.setPhone(contactPhone);
                newArtist.setWebsite(website);
                newArtist.setSocialMedia(socialMedias);
                newArtist.setIsActive(isActive);
                newArtist.setId(id);
                return Optional.of(newArtist);
            }
            return Optional.empty();
        } catch (SQLException sqlException) {
            return Optional.empty();
        }
    }

    @Override
    public List<Artist> findAll(Connection connection) {
        // SELECT * FROM Artists
        try {
            ResultSet artistData = connection.prepareStatement("SELECT * FROM Artists").executeQuery();
            List<Artist> results = new ArrayList<>();
            while (artistData.next()) {
                int id = artistData.getInt("artist_id");
                String name = artistData.getString("name");
                String bio = artistData.getString("bio");
                int birthYear = artistData.getInt("birth_year");
                List<Discipline> disciplines = findDisciplines(connection, id);
                String contactEmail = artistData.getString("contact_email");
                String contactPhone = artistData.getString("phone");
                String city = artistData.getString("city");
                String website = artistData.getString("website");
                List<SocialMedia> socialMedias = findSocialMedias(connection, id);
                boolean isActive = artistData.getBoolean("is_active");

                Artist newArtist = new Artist(name, bio, birthYear, contactEmail, city);
                newArtist.setDisciplines(disciplines);
                newArtist.setPhone(contactPhone);
                newArtist.setWebsite(website);
                newArtist.setSocialMedia(socialMedias);
                newArtist.setIsActive(isActive);
                newArtist.setId(id);
                results.add(newArtist);
            }
            return results;
        } catch (SQLException ex) {
            Logger.getLogger(JdbcArtistDao.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public void save(Artist artist, Connection connection) {
        // DONE: Implement INSERT INTO Artists(...) VALUES(...)
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO Artists (artist_id, name, bio, birth_year, contact_email, phone, city, website, is_active) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"
            );

            statement.setInt(1, artist.getId());
            statement.setString(2, artist.getName());
            statement.setString(3, artist.getBio());
            statement.setInt(4, artist.getBirthYear());
            statement.setString(5, artist.getContactEmail());
            statement.setString(6, artist.getPhone());
            statement.setString(7, artist.getCity());
            statement.setString(8, artist.getWebsite());
            statement.setBoolean(9, artist.getIsActive());

            statement.executeUpdate();

            replacePractices(connection, artist);
            replaceSocialMedias(connection, artist);
        } catch (SQLException sqlException) {
            System.out.println("Failed to insert artist : " + sqlException.getMessage());
        }
    }

    @Override
    public void update(Artist artist, Connection connection) {
        // DONE: Implement UPDATE Artists SET ... WHERE artist_id = ?
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE Artists SET name=?, bio=?, birth_year=?, contact_email=?, phone=?, city=?, website=?, is_active=? WHERE artist_id=?"
            );

            statement.setString(1, artist.getName());
            statement.setString(2, artist.getBio());
            statement.setInt(3, artist.getBirthYear());
            statement.setString(4, artist.getContactEmail());
            statement.setString(5, artist.getPhone());
            statement.setString(6, artist.getCity());
            statement.setString(7, artist.getWebsite());
            statement.setBoolean(8, artist.getIsActive());
            statement.setInt(9, artist.getId());

            statement.executeUpdate();

            deletePractices(connection, artist.getId());
            replacePractices(connection, artist);
            deleteSocialMedias(connection, artist.getId());
            replaceSocialMedias(connection, artist);
        } catch (SQLException sqlException) {
            System.out.println("Failed to update artist : " + sqlException.getMessage());
        }
    }

    @Override
    public void delete(String artistName, Connection connection) {
        // DONE: Implement DELETE FROM Artists WHERE name = ?
        try {
            PreparedStatement idStatement = connection.prepareStatement("SELECT artist_id FROM Artists WHERE name = ?");
            idStatement.setString(1, artistName);
            ResultSet artistData = idStatement.executeQuery();
            int artistId = artistData.getInt("artist_id");

            deletePractices(connection, artistId);
            deleteSocialMedias(connection, artistId);

            try {
                PreparedStatement statement = connection.prepareStatement(
                        "DELETE FROM Artists WHERE name = ?"
                );
                statement.setString(1, artistName);

                statement.executeUpdate();
            } catch (SQLException sqlException) {
                System.out.println("Failed to delete artist : " + sqlException.getMessage());
            }

        } catch (SQLException sqlException) {
            System.out.println("Failed to find artist : " + sqlException.getMessage());
        }
    }

    @Override
    public List<Artist> findByCity(String city, Connection connection) {
        // DONE: Implement SELECT * FROM Artists WHERE city = ?
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Artists WHERE city = ?");
            statement.setString(1, city);
            ResultSet artistData = statement.executeQuery();
            List<Artist> results = new ArrayList<>();
            while (artistData.next()) {
                int id = artistData.getInt("artist_id");
                String name = artistData.getString("name");
                String bio = artistData.getString("bio");
                int birthYear = artistData.getInt("birth_year");
                List<Discipline> disciplines = findDisciplines(connection, id);
                String contactEmail = artistData.getString("contact_email");
                String contactPhone = artistData.getString("phone");
                String website = artistData.getString("website");
                List<SocialMedia> socialMedias = findSocialMedias(connection, id);
                boolean isActive = artistData.getBoolean("is_active");

                Artist newArtist = new Artist(name, bio, birthYear, contactEmail, city);
                newArtist.setDisciplines(disciplines);
                newArtist.setPhone(contactPhone);
                newArtist.setWebsite(website);
                newArtist.setSocialMedia(socialMedias);
                newArtist.setIsActive(isActive);
                newArtist.setId(id);
                results.add(newArtist);
            }
            return results;
        } catch (SQLException ex) {
            Logger.getLogger(JdbcArtistDao.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public List<Discipline> findDisciplines(Connection connection, int artistId) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Disciplines d JOIN Practices p ON d.discipline_id = p.discipline_id JOIN Artists a ON p.artist_id = a.artist_id WHERE a.artist_id = ?");
            statement.setInt(1, artistId);
            ResultSet disciplineData = statement.executeQuery();
            List<Discipline> disciplines = new ArrayList<>();
            while (disciplineData.next()) {
                int disciplineId = disciplineData.getInt("discipline_id");
                String disciplineName = disciplineData.getString("name");

                Discipline newDiscipline = new Discipline(disciplineName);
                newDiscipline.setDisciplineId(disciplineId);
                disciplines.add(newDiscipline);
            }
            return disciplines;
        } catch (SQLException sqlException) {
            return new ArrayList<>();
        }
    }

    public void replacePractices(Connection connection, Artist artist) {
        try {
            for (Discipline discipline : artist.getDisciplines()) {
                PreparedStatement statement = connection.prepareStatement("REPLACE INTO Practices (artist_id, discipline_id) VALUES (?, ?)");

                statement.setInt(1, artist.getId());
                statement.setInt(2, discipline.getDisciplineId());

                statement.executeUpdate();
            }
        } catch (SQLException sqlException) {
            System.out.println("Failed to replace practices : " + sqlException.getMessage());
        }
    }

    public void deletePractices(Connection connection, int artistId) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM Practices WHERE artist_id = ?");

            statement.setInt(1, artistId);

            statement.executeUpdate();
        } catch (SQLException sqlException) {
            System.out.println("Failed to delete practices : " + sqlException.getMessage());
        }
    }

    public List<SocialMedia> findSocialMedias(Connection connection, int artistId) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Social_medias sm JOIN Artists a ON sm.artist_id = a.artist_id WHERE a.artist_id = ?");
            statement.setInt(1, artistId);
            ResultSet socialMediaData = statement.executeQuery();
            List<SocialMedia> socialMedias = new ArrayList<>();
            while (socialMediaData.next()) {
                String platform = socialMediaData.getString("platform");
                String accountHandle = socialMediaData.getString("account_handle");

                SocialMedia newSocialMedia = new SocialMedia(platform, accountHandle);
                socialMedias.add(newSocialMedia);
            }
            return socialMedias;
        } catch (SQLException sqlException) {
            return new ArrayList<>();
        }
    }

    public void replaceSocialMedias(Connection connection, Artist artist) {
        try {
            for (SocialMedia socialMedia : artist.getSocialMedia()) {
                PreparedStatement statement = connection.prepareStatement("REPLACE INTO Social_medias (artist_id, platform, account_handle) VALUES (?, ?, ?)");

                statement.setInt(1, artist.getId());
                statement.setString(2, socialMedia.getPlatform());
                statement.setString(3, socialMedia.getAccountHandle());

                statement.executeUpdate();
            }
        } catch (SQLException sqlException) {
            System.out.println("Failed to replace social medias : " + sqlException.getMessage());
        }
    }

    public void deleteSocialMedias(Connection connection, int artistId) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM Social_medias WHERE artist_id = ?");

            statement.setInt(1, artistId);

            statement.executeUpdate();
        } catch (SQLException sqlException) {
            System.out.println("Failed to delete social medias : " + sqlException.getMessage());
        }
    }



}
