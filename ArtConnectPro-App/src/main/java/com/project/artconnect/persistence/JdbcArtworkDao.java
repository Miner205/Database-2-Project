
package com.project.artconnect.persistence;

import com.project.artconnect.dao.impl.ArtworkDao;
import com.project.artconnect.model.Artist;
import com.project.artconnect.model.Artwork;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * JDBC implementation for ArtworkDao.
 */
public class JdbcArtworkDao implements ArtworkDao {

    @Override
    public List<Artwork> findAll(Connection connection) {
        try {
            ResultSet resultData = connection.prepareStatement("SELECT * FROM Artworks").executeQuery();
            List<Artwork> results = new ArrayList<>();
            while (resultData.next()) {
                int artworkId = resultData.getInt("artwork_id");
                String title = resultData.getString("title");
                int creationYear = resultData.getInt("creation_year");
                String type = resultData.getString("type");
                String description = resultData.getString("description");
                double price = resultData.getDouble("price");
                String statusStr = resultData.getString("status");
                Artist artist = new JdbcArtistDao().findById(connection, resultData.getInt("artist_id")).orElse(null);
                Artwork newArtwork = new Artwork(title, creationYear, type, price, artist);
                newArtwork.setArtworkId(artworkId);
                newArtwork.setDescription(description);
                Artwork.Status status = null;
                if (statusStr.equals("FOR_SALE")) {
                    status = Artwork.Status.FOR_SALE;
                } else if (statusStr.equals("SOLD")) {
                    status = Artwork.Status.SOLD;
                } else if (statusStr.equals("EXHIBITED")) {
                    status = Artwork.Status.EXHIBITED;
                }
                newArtwork.setStatus(status);
                results.add(newArtwork);
            }
            return results;
        } catch (SQLException ex) {
            Logger.getLogger(JdbcArtworkDao.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public void save(Connection connection, Artwork artwork) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO Artworks (artwork_id, title, creation_year, type, description, price, status, artist_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)"
            );

            statement.setInt(1, artwork.getArtworkId());
            statement.setString(2, artwork.getTitle());
            statement.setInt(3, artwork.getCreationYear());
            statement.setString(4, artwork.getType());
            statement.setString(5, artwork.getDescription());
            statement.setDouble(6, artwork.getPrice());
            Artwork.Status status = artwork.getStatus();
            String statusStr = null;
            switch (status) {
                case FOR_SALE -> statusStr = "FOR_SALE";
                case SOLD -> statusStr = "SOLD";
                case EXHIBITED -> statusStr = "EXHIBITED";
            }
            statement.setString(7, statusStr);
            statement.setInt(8, artwork.getArtist().getId());

            statement.executeUpdate();
        } catch (SQLException sqlException) {
            System.out.println("Failed to insert artwork : " + sqlException.getMessage());
        }
    }

    @Override
    public void update(Connection connection, Artwork artwork) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE Artworks SET title=?, creation_year=?, type=?, description=?, price=?, status=?, artist_id=? WHERE artwork_id=?"
            );

            statement.setString(2, artwork.getTitle());
            statement.setInt(3, artwork.getCreationYear());
            statement.setString(4, artwork.getType());
            statement.setString(5, artwork.getDescription());
            statement.setDouble(6, artwork.getPrice());
            Artwork.Status status = artwork.getStatus();
            String statusStr = null;
            switch (status) {
                case FOR_SALE -> statusStr = "FOR_SALE";
                case SOLD -> statusStr = "SOLD";
                case EXHIBITED -> statusStr = "EXHIBITED";
            }
            statement.setString(7, statusStr);
            statement.setInt(8, artwork.getArtist().getId());
            statement.setInt(1, artwork.getArtworkId());

            statement.executeUpdate();
        } catch (SQLException sqlException) {
            System.out.println("Failed to update artwork : " + sqlException.getMessage());
        }
    }

    @Override
    public void delete(Connection connection, String title) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM Artworks WHERE title = ?"
            );
            statement.setString(1, title);

            statement.executeUpdate();
        } catch (SQLException sqlException) {
            System.out.println("Failed to delete artwork : " + sqlException.getMessage());
        }
    }

    @Override
    public List<Artwork> findByArtistName(Connection connection, String artistName) {
        try {
            ResultSet resultData = connection.prepareStatement("SELECT * FROM Artworks art JOIN Artists a ON art.artist_id = a.artist_id WHERE a.name = ?").executeQuery();
            List<Artwork> results = new ArrayList<>();
            while (resultData.next()) {
                int artworkId = resultData.getInt("artwork_id");
                String title = resultData.getString("title");
                int creationYear = resultData.getInt("creation_year");
                String type = resultData.getString("type");
                String description = resultData.getString("description");
                double price = resultData.getDouble("price");
                String statusStr = resultData.getString("status");
                Artist artist = new JdbcArtistDao().findById(connection, resultData.getInt("artist_id")).orElse(null);
                Artwork newArtwork = new Artwork(title, creationYear, type, price, artist);
                newArtwork.setArtworkId(artworkId);
                newArtwork.setDescription(description);
                Artwork.Status status = null;
                if (statusStr.equals("FOR_SALE")) {
                    status = Artwork.Status.FOR_SALE;
                } else if (statusStr.equals("SOLD")) {
                    status = Artwork.Status.SOLD;
                } else if (statusStr.equals("EXHIBITED")) {
                    status = Artwork.Status.EXHIBITED;
                }
                newArtwork.setStatus(status);
                results.add(newArtwork);
            }
            return results;
        } catch (SQLException ex) {
            Logger.getLogger(JdbcArtworkDao.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

}
