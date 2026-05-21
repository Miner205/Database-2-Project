package com.project.artconnect.service.impl;

import com.project.artconnect.util.ConnectionManager;
import com.project.artconnect.dao.impl.CommunityMemberDao;
import com.project.artconnect.model.CommunityMember;
import com.project.artconnect.model.Review;
import com.project.artconnect.persistence.JbdcCommunityMemberDao;
import com.project.artconnect.service.CommunityService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CommunityServiceImpl implements CommunityService {
    private final CommunityMemberDao communityMemberDao;

    public CommunityServiceImpl() {
        this.communityMemberDao = new JbdcCommunityMemberDao();
    }

    @Override
    public List<CommunityMember> getAllMembers() {
        try (Connection connection = ConnectionManager.getConnection()) {
            return communityMemberDao.findAll(connection);
        } catch (SQLException sqlException) {
            System.out.println(
                    "Failed to get community members : " + sqlException.getMessage()
            );
            return new ArrayList<>();
        }
    }

    @Override
    public Optional<CommunityMember> getMemberByName(String name) {
        return getAllMembers().stream()
                .filter(m -> m.getName().equalsIgnoreCase(name))
                .findFirst();
    }

    @Override
    public List<Review> getReviewsByMember(CommunityMember member) {
        if (member == null) {
            return new ArrayList<>();
        }
        return member.getReviews();
    }

    @Override
    public void deleteCommunityMember(String name) {
        try (Connection connection = ConnectionManager.getConnection()) {
            communityMemberDao.delete(connection, name);
        } catch (SQLException sqlException) {
            System.out.println("Failed to delete community member : " + sqlException.getMessage());
        }
    }
}