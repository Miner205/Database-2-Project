package com.project.artconnect.persistence;

import com.project.artconnect.dao.impl.WorkshopDao;
import com.project.artconnect.model.Artist;
import com.project.artconnect.model.Workshop;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

}
