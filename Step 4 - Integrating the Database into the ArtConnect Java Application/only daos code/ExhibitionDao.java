package com.project.artconnect.dao.impl;

import com.project.artconnect.model.Exhibition;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface ExhibitionDao {
    List<Exhibition> findAll(Connection connection) throws SQLException;

    void save(Connection connection, Exhibition exhibition) throws SQLException ;

    void update(Connection connection, Exhibition exhibition) throws SQLException ;

    void delete(Connection connection, String title) throws SQLException ;
}
