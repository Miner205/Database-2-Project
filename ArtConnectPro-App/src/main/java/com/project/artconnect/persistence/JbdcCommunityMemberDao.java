package com.project.artconnect.persistence;

import com.project.artconnect.dao.impl.CommunityMemberDao;
import com.project.artconnect.model.CommunityMember;
import com.project.artconnect.model.Discipline;

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
                List<Discipline> disciplines = findDisciplines(connection, id);
                String membershipType = communityMemberData.getString("membership_type");

                CommunityMember newCommunityMember = new CommunityMember(name, email);
                newCommunityMember.setBirthYear(birthYear);
                newCommunityMember.setPhone(phone);
                newCommunityMember.setCity(city);
                newCommunityMember.setFavoriteDisciplines(disciplines);
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
                int communityMemberId = communityMemberData.getInt("community_member_id");
                String name = communityMemberData.getString("name");
                String email = communityMemberData.getString("email");
                int birthYear = communityMemberData.getInt("birth_year");
                String phone = communityMemberData.getString("phone");
                String city = communityMemberData.getString("city");
                List<Discipline> disciplines = findDisciplines(connection, communityMemberId);
                String membershipType = communityMemberData.getString("membership_type");

                CommunityMember newCommunityMember = new CommunityMember(name, email);
                newCommunityMember.setCommunityMemberId(communityMemberId);
                newCommunityMember.setBirthYear(birthYear);
                newCommunityMember.setPhone(phone);
                newCommunityMember.setCity(city);
                newCommunityMember.setFavoriteDisciplines(disciplines);
                newCommunityMember.setMembershipType(membershipType);
                communityMembers.add(newCommunityMember);
            }
            return communityMembers;
        } catch (SQLException sqlException) {
            return new ArrayList<>();
        }
    }

    public List<Discipline> findDisciplines(Connection connection, int communityMemberId) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Disciplines d JOIN Favorite_disciplines fd ON d.discipline_id = fd.discipline_id JOIN Community_members cm ON fd.community_member_id = cm.community_member_id WHERE cm.community_member_id = ?");
            statement.setInt(1, communityMemberId);
            ResultSet disciplineData = statement.executeQuery();
            List<Discipline> disciplines = new ArrayList<>();
            while (disciplineData.next()) {
                int disciplineId = disciplineData.getInt("discipline_id");
                String disciplineName = disciplineData.getString("name");

                Discipline newDiscipline = new Discipline(disciplineName);
                newDiscipline.setDisciplineId(disciplineId);
                disciplines.add(newDiscipline);
            }
            return disciplines;
        } catch (SQLException sqlException) {
            return new ArrayList<>();
        }
    }

}
