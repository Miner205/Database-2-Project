package com.project.artconnect.dao.impl;

import com.project.artconnect.model.CommunityMember;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public interface CommunityMemberDao {
    Optional<CommunityMember> findById(Connection connection, int id);

    List<CommunityMember> findAll(Connection connection);

    void save(Connection connection, CommunityMember communityMember);

    void update(Connection connection, CommunityMember communityMember);

    void delete(Connection connection, String name);

}
