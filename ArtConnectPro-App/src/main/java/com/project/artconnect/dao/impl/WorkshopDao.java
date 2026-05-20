package com.project.artconnect.dao.impl;

import com.project.artconnect.model.Workshop;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public interface WorkshopDao {
    Optional<Workshop> findById(Connection connection, int id);
    List<Workshop> findAll(Connection connection);
    public int getNbMembersInWorkshop(Connection connection, int workshopId);

}
