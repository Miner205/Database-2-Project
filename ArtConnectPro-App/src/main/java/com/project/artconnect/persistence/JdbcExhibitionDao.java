package com.project.artconnect.persistence;

import com.project.artconnect.dao.ExhibitionDao;
import com.project.artconnect.model.Exhibition;
import com.project.artconnect.model.Gallery;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public abstract class JdbcExhibitionDao implements ExhibitionDao {

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
                LocalDate endDate = exhibitionsData.getDate("end_date").toLocalDate();
                String description = exhibitionsData.getString("description");
                Gallery gallery = .findById(exhibitionsData.getInt("gallery_id"));
                String curatorName = exhibitionsData.getString("curator_name");
                String theme = exhibitionsData.getString("theme");

                Exhibition new_exhibition = new Exhibition(title, startDate, endDate, gallery);
                new_exhibition.setExhibitionId(exhibitionId);
                new_exhibition.setDescription(description);
                new_exhibition.setCuratorName(curatorName);
                new_exhibition.setTheme(theme);
                exhibitions.add(new_exhibition);
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
            PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM Exhibitions WHERE title = ?"
            );
            statement.setString(1, title);

            statement.executeUpdate();
        } catch (SQLException sqlException) {
            System.out.println("Failed to delete exhibition : " + sqlException.getMessage());
        }
    }

}
