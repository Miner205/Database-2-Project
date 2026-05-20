package com.project.artconnect.persistence;

import com.project.artconnect.dao.impl.WorkshopDao;
import com.project.artconnect.model.Artist;
import com.project.artconnect.model.Workshop;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcWorkshopDao implements WorkshopDao {
    @Override
    public Optional<Workshop> findById(Connection connection, int id) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Workshops WHERE workshop_id = ?");
            statement.setInt(1, id);
            ResultSet workshopData = statement.executeQuery();
            if (workshopData.next()) {
                String title = workshopData.getString("title");
                LocalDateTime workshopDate = workshopData.getTimestamp("workshop_date") != null ? workshopData.getTimestamp("workshop_date").toLocalDateTime() : null;
                int durationMinutes = workshopData.getInt("duration_minutes");
                int maxParticipant = workshopData.getInt("max_participant");
                double price = workshopData.getDouble("price");
                String location = workshopData.getString("location");
                String description = workshopData.getString("description");
                String level = workshopData.getString("level");
                Artist instructor = new JdbcArtistDao().findById(connection, workshopData.getInt("instructor")).orElse(null);
                Workshop newWorkshop = new Workshop(title, workshopDate, instructor, price);
                newWorkshop.setWorkshopId(id);
                newWorkshop.setDurationMinutes(durationMinutes);
                newWorkshop.setMaxParticipants(maxParticipant);
                newWorkshop.setLocation(location);
                newWorkshop.setDescription(description);
                newWorkshop.setLevel(level);
                return Optional.of(newWorkshop);
            }
            return Optional.empty();
        } catch (SQLException sqlException) {
            return Optional.empty();
        }
    }

    @Override
    public List<Workshop> findAll(Connection connection) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Workshops");
            ResultSet workshopData = statement.executeQuery();
            List<Workshop> workshops = new ArrayList<>();
            while (workshopData.next()) {
                int workshopId = workshopData.getInt("workshop_id");
                String title = workshopData.getString("title");
                LocalDateTime workshopDate = workshopData.getTimestamp("workshop_date") != null ? workshopData.getTimestamp("workshop_date").toLocalDateTime() : null;
                int durationMinutes = workshopData.getInt("duration_minutes");
                int maxParticipant = workshopData.getInt("max_participant");
                double price = workshopData.getDouble("price");
                String location = workshopData.getString("location");
                String description = workshopData.getString("description");
                String level = workshopData.getString("level");
                Artist instructor = new JdbcArtistDao().findById(connection, workshopData.getInt("instructor")).orElse(null);
                Workshop newWorkshop = new Workshop(title, workshopDate, instructor, price);
                newWorkshop.setWorkshopId(workshopId);
                newWorkshop.setDurationMinutes(durationMinutes);
                newWorkshop.setMaxParticipants(maxParticipant);
                newWorkshop.setLocation(location);
                newWorkshop.setDescription(description);
                newWorkshop.setLevel(level);
                workshops.add(newWorkshop);
            }
            return workshops;
        } catch (SQLException sqlException) {
            return new ArrayList<>();
        }
    }

    @Override
    public int getNbMembersInWorkshop(Connection connection, int workshopId) {
        try {
            PreparedStatement statement = connection.prepareStatement("{CALL get_nb_members_in_workshop(?)}");
            statement.setInt(1, workshopId);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                return result.getInt("nb_members_registered");
            }
            return 0;
        } catch (SQLException sqlException) {
            System.out.println("Failed to get nb of members in workshop : " + sqlException.getMessage());
            return -1;
        }
    }

    @Override
    public void save(Connection connection, Workshop workshop) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO Workshops (workshop_id, title, workshop_date, duration_minutes, max_participant, price, location, description, level, instructor) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

            statement.setInt(1, workshop.getWorkshopId());
            statement.setString(2, workshop.getTitle());
            statement.setTime(3, Time.valueOf(workshop.getDate().toLocalTime()));
            statement.setInt(4, workshop.getDurationMinutes());
            statement.setInt(5, workshop.getMaxParticipants());
            statement.setDouble(6, workshop.getPrice());
            statement.setString(7, workshop.getLocation());
            statement.setString(8, workshop.getDescription());
            statement.setString(9, workshop.getLevel());
            statement.setInt(10, workshop.getInstructor().getId());

            statement.executeUpdate();
        } catch (SQLException sqlException) {
            System.out.println("Failed to insert workshop : " + sqlException.getMessage());
        }
    }

    @Override
    public void update(Connection connection, Workshop workshop) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE Workshops SET title = ?, workshop_date = ?, duration_minutes = ?, max_participant = ?, price = ?, location = ?, description = ?, level = ?, instructor = ? WHERE workshop_id = ?");

            statement.setString(1, workshop.getTitle());
            statement.setTimestamp(2, Timestamp.valueOf(workshop.getDate()));
            statement.setInt(3, workshop.getDurationMinutes());
            statement.setInt(4, workshop.getMaxParticipants());
            statement.setDouble(5, workshop.getPrice());
            statement.setString(6, workshop.getLocation());
            statement.setString(7, workshop.getDescription());
            statement.setString(8, workshop.getLevel());
            statement.setInt(9, workshop.getInstructor().getId());
            statement.setInt(10, workshop.getWorkshopId());

            statement.executeUpdate();
        } catch (SQLException sqlException) {
            System.out.println("Failed to update workshop : " + sqlException.getMessage());
        }
    }

    @Override
    public void delete(Connection connection, String title) {
        try {
            PreparedStatement idStatement = connection.prepareStatement("SELECT workshop_id FROM Workshops WHERE title = ?");
            idStatement.setString(1, title);
            ResultSet workshopData = idStatement.executeQuery();
            if (workshopData.next()) {
                int worshopId = workshopData.getInt("workshop_id");

                deleteBooking(connection, worshopId);

                try {
                    PreparedStatement statement = connection.prepareStatement("DELETE FROM Workshops WHERE title = ?");
                    statement.setString(1, title);

                    statement.executeUpdate();
                } catch (SQLException sqlException) {
                    System.out.println("Failed to delete workshop : " + sqlException.getMessage());
                }
            }

        } catch (SQLException sqlException) {
            System.out.println("Failed to find workshop : " + sqlException.getMessage());
        }
    }

    public void deleteBooking(Connection connection, int workshopId) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM Bookings WHERE workshop_id = ?");

            statement.setInt(1, workshopId);

            statement.executeUpdate();
        } catch (SQLException sqlException) {
            System.out.println("Failed to delete workshopId : " + sqlException.getMessage());
        }
    }

}
