package com.project.artconnect.persistence;

import com.project.artconnect.dao.impl.CommunityMemberDao;
import com.project.artconnect.model.CommunityMember;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JbdcCommunityMemberDao implements CommunityMemberDao {
    @Override
    public Optional<CommunityMember> findById(Connection connection, int id) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Community_members WHERE community_member_id = ?");
            statement.setInt(1, id);
            ResultSet communityMemberData = statement.executeQuery();
            if (communityMemberData.next()) {
                String name = communityMemberData.getString("name");
                String email = communityMemberData.getString("email");
                int birthYear = communityMemberData.getInt("birth_year");
                String phone = communityMemberData.getString("phone");
                String city = communityMemberData.getString("city");
                String membershipType = communityMemberData.getString("membership_type");
                CommunityMember newCommunityMember = new CommunityMember(name, email);
                newCommunityMember.setBirthYear(birthYear);
                newCommunityMember.setPhone(phone);
                newCommunityMember.setCity(city);
                newCommunityMember.setMembershipType(membershipType);
                return Optional.of(newCommunityMember);
            }
            return Optional.empty();
        } catch (SQLException sqlException) {
            return Optional.empty();
        }
    }

    @Override
    public List<CommunityMember> findAll(Connection connection) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Community_members");
            ResultSet communityMemberData = statement.executeQuery();
            List<CommunityMember> communityMembers = new ArrayList<>();
            while (communityMemberData.next()) {
                String name = communityMemberData.getString("name");
                String email = communityMemberData.getString("email");
                int birthYear = communityMemberData.getInt("birth_year");
                String phone = communityMemberData.getString("phone");
                String city = communityMemberData.getString("city");
                String membershipType = communityMemberData.getString("membership_type");
                CommunityMember newCommunityMember = new CommunityMember(name, email);
                newCommunityMember.setBirthYear(birthYear);
                newCommunityMember.setPhone(phone);
                newCommunityMember.setCity(city);
                newCommunityMember.setMembershipType(membershipType);
                communityMembers.add(newCommunityMember);
            }
            return communityMembers;
        } catch (SQLException sqlException) {
            return new ArrayList<>();
        }
    }

}
