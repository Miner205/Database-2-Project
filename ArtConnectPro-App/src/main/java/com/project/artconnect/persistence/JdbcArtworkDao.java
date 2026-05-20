package com.project.artconnect.persistence;

import com.project.artconnect.dao.impl.ArtworkDao;
import com.project.artconnect.model.Artwork;
import com.project.artconnect.model.Dimension;
import com.project.artconnect.model.Artist;
import com.project.artconnect.model.ArtworkTag;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * JDBC implementation for ArtworkDao.
 */
public class JdbcArtworkDao implements ArtworkDao {
    @Override
    public Optional<Artwork> findById(Connection connection, int id) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Artworks WHERE artwork_id = ?");
            statement.setInt(1, id);
            ResultSet artworkData = statement.executeQuery();
            if (artworkData.next()) {
                String title = artworkData.getString("title");
                int creationYear = artworkData.getInt("creation_year");
                String type = artworkData.getString("type");
                String medium = artworkData.getString("medium");
                Dimension dimension = findDimension(connection, id).orElse(null);
                String description = artworkData.getString("description");
                double price = artworkData.getDouble("price");
                String statusStr = artworkData.getString("status");
                Artist artist = new JdbcArtistDao().findById(connection, artworkData.getInt("artist_id")).orElse(null);
                List<ArtworkTag> artworkTags = findArtworkTags(connection, id);

                Artwork newArtwork = new Artwork(title, creationYear, type, price, artist);
                newArtwork.setArtworkId(id);
                newArtwork.setMedium(medium);
                newArtwork.setDimensions(dimension);
                newArtwork.setDescription(description);
                Artwork.Status status = switch (statusStr) {
                    case "FOR_SALE" -> Artwork.Status.FOR_SALE;
                    case "SOLD" -> Artwork.Status.SOLD;
                    case "EXHIBITED" -> Artwork.Status.EXHIBITED;
                    default -> null;
                };
                newArtwork.setStatus(status);
                newArtwork.setTags(artworkTags);
                return Optional.of(newArtwork);
            }
            return Optional.empty();
        } catch (SQLException sqlException) {
            return Optional.empty();
        }
    }

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
                String medium = resultData.getString("medium");
                Dimension dimension = findDimension(connection, artworkId).orElse(null);
                String description = resultData.getString("description");
                double price = resultData.getDouble("price");
                String statusStr = resultData.getString("status");
                Artist artist = new JdbcArtistDao().findById(connection, resultData.getInt("artist_id")).orElse(null);
                List<ArtworkTag> artworkTags = findArtworkTags(connection, artworkId);

                Artwork newArtwork = new Artwork(title, creationYear, type, price, artist);
                newArtwork.setArtworkId(artworkId);
                newArtwork.setMedium(medium);
                newArtwork.setDimensions(dimension);
                newArtwork.setDescription(description);
                Artwork.Status status = switch (statusStr) {
                    case "FOR_SALE" -> Artwork.Status.FOR_SALE;
                    case "SOLD" -> Artwork.Status.SOLD;
                    case "EXHIBITED" -> Artwork.Status.EXHIBITED;
                    default -> null;
                };
                newArtwork.setStatus(status);
                newArtwork.setTags(artworkTags);
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
                    "INSERT INTO Artworks (artwork_id, title, creation_year, type, medium, description, price, status, artist_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"
            );

            statement.setInt(1, artwork.getArtworkId());
            statement.setString(2, artwork.getTitle());
            statement.setInt(3, artwork.getCreationYear());
            statement.setString(4, artwork.getType());
            statement.setString(5, artwork.getMedium());
            statement.setString(6, artwork.getDescription());
            statement.setDouble(7, artwork.getPrice());
            Artwork.Status status = artwork.getStatus();
            String statusStr = switch (status) {
                case FOR_SALE -> statusStr = "FOR_SALE";
                case SOLD -> statusStr = "SOLD";
                case EXHIBITED -> statusStr = "EXHIBITED";
            };
            statement.setString(8, statusStr);
            statement.setInt(9, artwork.getArtist().getId());

            statement.executeUpdate();

            replaceDimension(connection, artwork);
            replaceTagged(connection, artwork);
        } catch (SQLException sqlException) {
            System.out.println("Failed to insert artwork : " + sqlException.getMessage());
        }
    }

    @Override
    public void update(Connection connection, Artwork artwork) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE Artworks SET title=?, creation_year=?, type=?, medium=?, description=?, price=?, status=?, artist_id=? WHERE artwork_id=?"
            );

            statement.setString(1, artwork.getTitle());
            statement.setInt(2, artwork.getCreationYear());
            statement.setString(3, artwork.getType());
            statement.setString(4, artwork.getMedium());
            statement.setString(5, artwork.getDescription());
            statement.setDouble(6, artwork.getPrice());
            Artwork.Status status = artwork.getStatus();
            String statusStr = switch (status) {
                case FOR_SALE -> statusStr = "FOR_SALE";
                case SOLD -> statusStr = "SOLD";
                case EXHIBITED -> statusStr = "EXHIBITED";
            };
            statement.setString(7, statusStr);
            statement.setInt(8, artwork.getArtist().getId());
            statement.setInt(9, artwork.getArtworkId());

            statement.executeUpdate();

            deleteDimension(connection, artwork.getArtworkId());
            replaceDimension(connection, artwork);
            deleteTagged(connection, artwork.getArtworkId());
            replaceTagged(connection, artwork);
        } catch (SQLException sqlException) {
            System.out.println("Failed to update artwork : " + sqlException.getMessage());
        }
    }

    @Override
    public void delete(Connection connection, String title) {
        try {
            PreparedStatement idStatement = connection.prepareStatement("SELECT artwork_id FROM Artworks WHERE title = ?");
            idStatement.setString(1, title);
            ResultSet artworkData = idStatement.executeQuery();
            if (artworkData.next()) {
                int artworkId = artworkData.getInt("artwork_id");

                deleteDimension(connection, artworkId);
                deleteTagged(connection, artworkId);
                deleteExhibited(connection, artworkId);
                deleteReviews(connection, artworkId);
    
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

        } catch (SQLException sqlException) {
            System.out.println("Failed to find artwork : " + sqlException.getMessage());
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
                String medium = resultData.getString("medium");
                Dimension dimension = findDimension(connection, artworkId).orElse(null);
                String description = resultData.getString("description");
                double price = resultData.getDouble("price");
                String statusStr = resultData.getString("status");
                Artist artist = new JdbcArtistDao().findById(connection, resultData.getInt("artist_id")).orElse(null);
                List<ArtworkTag> artworkTags = findArtworkTags(connection, artworkId);

                Artwork newArtwork = new Artwork(title, creationYear, type, price, artist);
                newArtwork.setArtworkId(artworkId);
                newArtwork.setMedium(medium);
                newArtwork.setDimensions(dimension);
                newArtwork.setDescription(description);
                Artwork.Status status = switch (statusStr) {
                    case "FOR_SALE" -> Artwork.Status.FOR_SALE;
                    case "SOLD" -> Artwork.Status.SOLD;
                    case "EXHIBITED" -> Artwork.Status.EXHIBITED;
                    default -> null;
                };
                newArtwork.setStatus(status);
                newArtwork.setTags(artworkTags);
                results.add(newArtwork);
            }
            return results;
        } catch (SQLException ex) {
            Logger.getLogger(JdbcArtworkDao.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public Optional<Dimension> findDimension(Connection connection, int artworkId) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Dimensions d JOIN Artworks art ON d.artwork_id = art.artwork_id WHERE art.artwork_id = ?");
            statement.setInt(1, artworkId);
            ResultSet dimensionData = statement.executeQuery();
            if (dimensionData.next()) {
                double length = dimensionData.getDouble("length");
                double width = dimensionData.getDouble("width");
                double depth = dimensionData.getDouble("depth");

                Dimension newDimension = new Dimension(length, width, depth);
                return Optional.of(newDimension);
            }
            return Optional.empty();
        } catch (SQLException sqlException) {
            return Optional.empty();
        }
    }

    public void replaceDimension(Connection connection, Artwork artwork) {
        try {
            Dimension dimension = artwork.getDimensions();
            PreparedStatement statement = connection.prepareStatement("REPLACE INTO Dimensions (artwork_id, length, width, depth) VALUES (?, ?, ?, ?)");

            statement.setInt(1, artwork.getArtworkId());
            statement.setDouble(2, dimension.getLength());
            statement.setDouble(3, dimension.getWidth());
            statement.setDouble(4, dimension.getDepth());

            statement.executeUpdate();
        } catch (SQLException sqlException) {
            System.out.println("Failed to insert dimensions : " + sqlException.getMessage());
        }
    }

    public void deleteDimension(Connection connection, int artworkId) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM Dimensions WHERE artwork_id = ?");

            statement.setInt(1, artworkId);

            statement.executeUpdate();
        } catch (SQLException sqlException) {
            System.out.println("Failed to delete dimensions : " + sqlException.getMessage());
        }
    }

    public List<ArtworkTag> findArtworkTags(Connection connection, int artworkId) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Artwork_tags artt JOIN Tagged t ON artt.artwork_tag_id = t.artwork_tag_id JOIN Artworks art ON t.artwork_id = art.artwork_id WHERE art.artwork_id = ?");
            statement.setInt(1, artworkId);
            ResultSet artworkTagData = statement.executeQuery();
            List<ArtworkTag> artworkTags = new ArrayList<>();
            while (artworkTagData.next()) {
                int artworkTagId = artworkTagData.getInt("artwork_tag_id");
                String name = artworkTagData.getString("name");

                ArtworkTag newArtworkTag = new ArtworkTag(name);
                newArtworkTag.setArtworkTagId(artworkTagId);
                artworkTags.add(newArtworkTag);
            }
            return artworkTags;
        } catch (SQLException sqlException) {
            return new ArrayList<>();
        }
    }

    public void replaceTagged(Connection connection, Artwork artwork) {
        try {
            for (ArtworkTag artworkTag : artwork.getTags()) {
                PreparedStatement statement = connection.prepareStatement("REPLACE INTO Tagged (artwork_id, artwork_tag_id) VALUES (?, ?)");

                statement.setInt(1, artwork.getArtworkId());
                statement.setInt(2, artworkTag.getArtworkTagId());

                statement.executeUpdate();
            }
        } catch (SQLException sqlException) {
            System.out.println("Failed to replace tagged : " + sqlException.getMessage());
        }
    }

    public void deleteTagged(Connection connection, int artworkId) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM Tagged WHERE artwork_id = ?");

            statement.setInt(1, artworkId);

            statement.executeUpdate();
        } catch (SQLException sqlException) {
            System.out.println("Failed to delete tagged : " + sqlException.getMessage());
        }
    }

    public void deleteExhibited(Connection connection, int artworkId) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM Exhibited WHERE artwork_id = ?");

            statement.setInt(1, artworkId);

            statement.executeUpdate();
        } catch (SQLException sqlException) {
            System.out.println("Failed to delete exhibited : " + sqlException.getMessage());
        }
    }

    public void deleteReviews(Connection connection, int artworkId) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM Reviews WHERE artwork_id = ?");

            statement.setInt(1, artworkId);

            statement.executeUpdate();
        } catch (SQLException sqlException) {
            System.out.println("Failed to delete review : " + sqlException.getMessage());
        }
    }

}
