package com.project.artconnect.persistence;

import com.project.artconnect.dao.impl.ExhibitionDao;
import com.project.artconnect.model.Exhibition;
import com.project.artconnect.model.Gallery;
import com.project.artconnect.model.OpeningHours;
import com.project.artconnect.model.Artwork;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.sql.Date;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.sql.Time;

public class JdbcExhibitionDao implements ExhibitionDao {

    @Override
    public List<Exhibition> findAll(Connection connection) throws SQLException {
        // DONE: query the Exhibitions table and return all Exhibitions
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Exhibitions");
            ResultSet exhibitionsData = statement.executeQuery();
            List<Exhibition> exhibitions = new ArrayList<>();
            while (exhibitionsData.next()) {
                int exhibitionId = exhibitionsData.getInt("exhibition_id");
                String title = exhibitionsData.getString("title");
                LocalDate startDate = exhibitionsData.getDate("start_date").toLocalDate();
                Date sqlEndDate = exhibitionsData.getDate("end_date");
                LocalDate endDate;
                if (sqlEndDate != null) {
                    endDate = sqlEndDate.toLocalDate();
                } else {
                    endDate = null;
                }
                String description = exhibitionsData.getString("description");
                Gallery gallery = new JdbcGalleryDao().findById(connection, exhibitionsData.getInt("gallery_id")).orElse(null);
                String curatorName = exhibitionsData.getString("curator_name");
                String theme = exhibitionsData.getString("theme");
                List<OpeningHours> openingHours = findOpeningHours(connection, exhibitionId);
                List<Artwork> artworks = findArtworks(connection, exhibitionId);

                Exhibition newExhibition = new Exhibition(title, startDate, endDate, gallery);
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

    @Override
    public void save(Connection connection, Exhibition exhibition) throws SQLException {
        // DONE: INSERT INTO Exhibitions (exhibition_id, title, start_date, ...) VALUES
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO Exhibitions (exhibition_id, title, start_date, end_date, description, gallery_id, curator_name, theme) VALUES (?, ?, ?, ?, ?, ?, ?, ?)"
            );

            statement.setInt(1, exhibition.getExhibitionId());
            statement.setString(2, exhibition.getTitle());
            statement.setDate(3, java.sql.Date.valueOf(exhibition.getStartDate()));
            statement.setDate(4, java.sql.Date.valueOf(exhibition.getEndDate()));
            statement.setString(5, exhibition.getDescription());
            statement.setInt(6, exhibition.getGallery().getGalleryId());
            statement.setString(7, exhibition.getCuratorName());
            statement.setString(8, exhibition.getTheme());

            statement.executeUpdate();

            replaceOpeningHours(connection, exhibition);
            replaceExhibited(connection, exhibition);
        } catch (SQLException sqlException) {
            System.out.println("Failed to insert exhibition : " + sqlException.getMessage());
        }
    }

    @Override
    public void update(Connection connection, Exhibition exhibition) throws SQLException {
        // DONE: UPDATE Exhibitions SET title=?, start_date=?, ... WHERE exhibition_id=?
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE Exhibitions SET title=?, start_date=?, end_date=?, description=?, gallery_id=?, curator_name=?, theme=? WHERE exhibition_id=?"
            );

            statement.setString(1, exhibition.getTitle());
            statement.setDate(2, java.sql.Date.valueOf(exhibition.getStartDate()));
            statement.setDate(3, java.sql.Date.valueOf(exhibition.getEndDate()));
            statement.setString(4, exhibition.getDescription());
            statement.setInt(5, exhibition.getGallery().getGalleryId());
            statement.setString(6, exhibition.getCuratorName());
            statement.setString(7, exhibition.getTheme());
            statement.setInt(8, exhibition.getExhibitionId());

            statement.executeUpdate();

            deleteOpeningHours(connection, exhibition.getExhibitionId());
            replaceOpeningHours(connection, exhibition);
            deleteExhibited(connection, exhibition.getExhibitionId());
            replaceExhibited(connection, exhibition);
        } catch (SQLException sqlException) {
            System.out.println("Failed to update exhibition : " + sqlException.getMessage());
        }
    }

    @Override
    public void delete(Connection connection, String title) throws SQLException {
        // DONE: DELETE FROM Exhibitions WHERE title=?
        //TODO:
        // Caution: check if there are foreign key constraints
        try {
            PreparedStatement idStatement = connection.prepareStatement("SELECT exhibition_id FROM Exhibitions WHERE title = ?");
            idStatement.setString(1, title);
            ResultSet exhibitionData = idStatement.executeQuery();
            if (exhibitionData.next()) {
                int exhibitionId = exhibitionData.getInt("exhibition_id");

                deleteOpeningHours(connection, exhibitionId);
                deleteExhibited(connection, exhibitionId);

                try {
                    PreparedStatement statement = connection.prepareStatement(
                            "DELETE FROM Exhibitions WHERE title = ?"
                    );
                    statement.setString(1, title);

                    statement.executeUpdate();
                } catch (SQLException sqlException) {
                    System.out.println("Failed to delete exhibition : " + sqlException.getMessage());
                }
            }

        } catch (SQLException sqlException) {
            System.out.println("Failed to find exhibition : " + sqlException.getMessage());
        }
    }

    public List<OpeningHours> findOpeningHours(Connection connection, int exhibitionId) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Opening_hours oh JOIN Exhibitions e ON oh.exhibition_id = e.exhibition_id WHERE e.exhibition_id = ?");
            statement.setInt(1, exhibitionId);
            ResultSet openingHoursData = statement.executeQuery();
            List<OpeningHours> openingHours = new ArrayList<>();
            while (openingHoursData.next()) {
                String day = openingHoursData.getString("day");
                LocalTime openingTime = openingHoursData.getTime("opening_time").toLocalTime();
                LocalTime closingTime = openingHoursData.getTime("closing_time").toLocalTime();

                OpeningHours newOpeningHours = new OpeningHours(day, openingTime, closingTime);
                openingHours.add(newOpeningHours);
            }
            return openingHours;
        } catch (SQLException sqlException) {
            return new ArrayList<>();
        }
    }

    public void replaceOpeningHours(Connection connection, Exhibition exhibition) {
        try {
            for (OpeningHours openingHours : exhibition.getOpeningHours()) {
                PreparedStatement statement = connection.prepareStatement("REPLACE INTO Opening_hours (exhibition_id, day, opening_time, closing_time) VALUES (?, ?, ?, ?)");

                statement.setInt(1, exhibition.getExhibitionId());
                statement.setString(2, openingHours.getDay());
                statement.setTime(3, Time.valueOf(openingHours.getOpeningTime()));
                statement.setTime(4, Time.valueOf(openingHours.getClosingTime()));

                statement.executeUpdate();
            }
        } catch (SQLException sqlException) {
            System.out.println("Failed to replace opening hours : " + sqlException.getMessage());
        }
    }

    public void deleteOpeningHours(Connection connection, int exhibitionId) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM Opening_hours WHERE exhibition_id = ?");

            statement.setInt(1, exhibitionId);

            statement.executeUpdate();
        } catch (SQLException sqlException) {
            System.out.println("Failed to delete opening hours : " + sqlException.getMessage());
        }
    }

    public List<Artwork> findArtworks(Connection connection, int exhibitionId) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT artwork_id FROM Exhibited ed JOIN Exhibitions e ON ed.exhibition_id = e.exhibition_id WHERE e.exhibition_id = ?");
            statement.setInt(1, exhibitionId);
            ResultSet exhibitedData = statement.executeQuery();
            List<Artwork> artworks = new ArrayList<>();
            while (exhibitedData.next()) {
                new JdbcArtworkDao().findById(connection, exhibitedData.getInt("artwork_id")).ifPresent(artworks::add);
            }
            return artworks;
        } catch (SQLException sqlException) {
            return new ArrayList<>();
        }
    }

    public void replaceExhibited(Connection connection, Exhibition exhibition) {
        try {
            for (Artwork artwork : exhibition.getArtworks()) {
                PreparedStatement statement = connection.prepareStatement("REPLACE INTO Exhibited (artwork_id, exhibition_id) VALUES (?, ?)");

                statement.setInt(1, artwork.getArtworkId());
                statement.setInt(2, exhibition.getExhibitionId());

                statement.executeUpdate();
            }
        } catch (SQLException sqlException) {
            System.out.println("Failed to replace exhibited : " + sqlException.getMessage());
        }
    }

    public void deleteExhibited(Connection connection, int exhibitionId) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM Exhibited WHERE exhibition_id = ?");

            statement.setInt(1, exhibitionId);

            statement.executeUpdate();
        } catch (SQLException sqlException) {
            System.out.println("Failed to delete exhibited : " + sqlException.getMessage());
        }
    }

}
