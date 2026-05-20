package com.project.artconnect.service.impl;

import com.project.artconnect.util.ConnectionManager;
import com.project.artconnect.dao.impl.WorkshopDao;
import com.project.artconnect.model.Booking;
import com.project.artconnect.model.CommunityMember;
import com.project.artconnect.model.Workshop;
import com.project.artconnect.persistence.JdbcWorkshopDao;
import com.project.artconnect.service.WorkshopService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WorkshopServiceImpl implements WorkshopService {
    private final WorkshopDao workshopDao;

    public WorkshopServiceImpl() {
        this.workshopDao = new JdbcWorkshopDao();
    }

    @Override
    public List<Workshop> getAllWorkshops() {
        try (Connection connection = ConnectionManager.getConnection()) {
            return workshopDao.findAll(connection);
        } catch (SQLException sqlException) {
            System.out.println("Failed to get workshops : " + sqlException.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public Optional<Workshop> getWorkshopByTitle(String title) {
        if (title == null) return Optional.empty();
        try (Connection connection = ConnectionManager.getConnection()) {
            return workshopDao.findAll(connection)
                    .stream()
                    .filter(w -> title.equalsIgnoreCase(w.getTitle()))
                    .findFirst();
        } catch (SQLException sqlException) {
            System.out.println("Failed to get workshop by title : " + sqlException.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public int nbMembersInWorkshop(int workshopId) {
        try (Connection connection = ConnectionManager.getConnection()) {
            return workshopDao.getNbMembersInWorkshop(connection, workshopId);
        } catch (SQLException sqlException) {
            System.out.println("Failed to get nb of members in workshop : " + sqlException.getMessage());
            return -1;
        }
    }

    @Override
    public void bookWorkshop(Workshop workshop, CommunityMember member) {
        if (workshop == null || member == null) return;
        Booking booking = new Booking(workshop, member);
        member.addBooking(booking);
    }

    @Override
    public List<Booking> getBookingsByMember(CommunityMember member) {
        if (member == null) return new ArrayList<>();
        return member.getBookings();
    }
}