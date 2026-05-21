package com.project.artconnect.persistence;

import com.project.artconnect.dao.impl.CommunityMemberDao;
import com.project.artconnect.model.*;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
                List<Booking> bookings = findBookings(connection, id);
                List<Review> reviews = findReviews(connection, id);

                CommunityMember newCommunityMember = new CommunityMember(name, email);
                newCommunityMember.setBirthYear(birthYear);
                newCommunityMember.setPhone(phone);
                newCommunityMember.setCity(city);
                newCommunityMember.setFavoriteDisciplines(disciplines);
                newCommunityMember.setMembershipType(membershipType);
                for (Booking booking : bookings) {
                    newCommunityMember.addBooking(booking);
                }
                for (Review review : reviews) {
                    newCommunityMember.addReview(review);
                }
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
                List<Booking> bookings = findBookings(connection, communityMemberId);
                List<Review> reviews = findReviews(connection, communityMemberId);

                CommunityMember newCommunityMember = new CommunityMember(name, email);
                newCommunityMember.setCommunityMemberId(communityMemberId);
                newCommunityMember.setBirthYear(birthYear);
                newCommunityMember.setPhone(phone);
                newCommunityMember.setCity(city);
                newCommunityMember.setFavoriteDisciplines(disciplines);
                newCommunityMember.setMembershipType(membershipType);
                for (Booking booking : bookings) {
                    newCommunityMember.addBooking(booking);
                }
                for (Review review : reviews) {
                    newCommunityMember.addReview(review);
                }
                communityMembers.add(newCommunityMember);
            }
            return communityMembers;
        } catch (SQLException sqlException) {
            return new ArrayList<>();
        }
    }

    @Override
    public void save(Connection connection, CommunityMember communityMember) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO Community_members (community_member_id, name, email, birth_year, phone, city, membership_type) VALUES (?, ?, ?, ?, ?, ?, ?)");

            statement.setInt(1, communityMember.getCommunityMemberId());
            statement.setString(2, communityMember.getName());
            statement.setString(3, communityMember.getEmail());
            statement.setInt(4, communityMember.getBirthYear());
            statement.setString(5, communityMember.getPhone());
            statement.setString(6, communityMember.getCity());
            statement.setString(7, communityMember.getMembershipType());

            statement.executeUpdate();

            replaceFavoriteDisciplines(connection, communityMember);
            replaceBookings(connection, communityMember);
            replaceBookings(connection, communityMember);
        } catch (SQLException sqlException) {
            System.out.println("Failed to insert community member : " + sqlException.getMessage());
        }
    }

    @Override
    public void update(Connection connection, CommunityMember communityMember) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE Community_members SET name = ?, email = ?, birth_year = ?, phone = ?, city = ?, membership_type = ? WHERE community_member_id = ?");

            statement.setString(1, communityMember.getName());
            statement.setString(2, communityMember.getEmail());
            statement.setInt(3, communityMember.getBirthYear());
            statement.setString(4, communityMember.getPhone());
            statement.setString(5, communityMember.getCity());
            statement.setString(6, communityMember.getMembershipType());
            statement.setInt(7, communityMember.getCommunityMemberId());

            statement.executeUpdate();

            deleteFavoriteDisciplines(connection, communityMember.getCommunityMemberId());
            replaceFavoriteDisciplines(connection, communityMember);
            deleteBookings(connection, communityMember.getCommunityMemberId());
            replaceBookings(connection, communityMember);
            deleteReviews(connection, communityMember.getCommunityMemberId());
            replaceReviews(connection, communityMember);
        } catch (SQLException sqlException) {
            System.out.println("Failed to update community member : " + sqlException.getMessage());
        }
    }

    @Override
    public void delete(Connection connection, String name) {
        try {
            PreparedStatement idStatement = connection.prepareStatement("SELECT community_member_id FROM Community_members WHERE name = ?");
            idStatement.setString(1, name);
            ResultSet communityMemberData = idStatement.executeQuery();
            if (communityMemberData.next()) {
                int communityMemberId = communityMemberData.getInt("community_member_id");

                deleteFavoriteDisciplines(connection, communityMemberId);
                deleteBookings(connection, communityMemberId);
                deleteReviews(connection, communityMemberId);

                try {
                    PreparedStatement statement = connection.prepareStatement("DELETE FROM Community_members WHERE name = ?");
                    statement.setString(1, name);

                    statement.executeUpdate();
                } catch (SQLException sqlException) {
                    System.out.println("Failed to delete community member : " + sqlException.getMessage());
                }
            }

        } catch (SQLException sqlException) {
            System.out.println("Failed to find community member : " + sqlException.getMessage());
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

    public void replaceFavoriteDisciplines(Connection connection, CommunityMember communityMember) {
        try {
            for (Discipline discipline : communityMember.getFavoriteDisciplines()) {
                PreparedStatement statement = connection.prepareStatement("REPLACE INTO Favorite_disciplines (discipline_id, community_member_id) VALUES (?, ?)");

                statement.setInt(1, discipline.getDisciplineId());
                statement.setInt(2, communityMember.getCommunityMemberId());

                statement.executeUpdate();
            }
        } catch (SQLException sqlException) {
            System.out.println("Failed to replace favorite disciplines : " + sqlException.getMessage());
        }
    }

    public void deleteFavoriteDisciplines(Connection connection, int communityMemberId) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM Favorite_disciplines WHERE community_member_id = ?");

            statement.setInt(1, communityMemberId);

            statement.executeUpdate();
        } catch (SQLException sqlException) {
            System.out.println("Failed to delete favorite disciplines : " + sqlException.getMessage());
        }
    }

    public List<Booking> findBookings(Connection connection, int communityMemberId) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Bookings b JOIN Community_members cm ON b.community_member_id = cm.community_member_id WHERE cm.community_member_id = ?");
            statement.setInt(1, communityMemberId);
            ResultSet bookingData = statement.executeQuery();
            List<Booking> bookings = new ArrayList<>();
            while (bookingData.next()) {
                Workshop workshop = new JdbcWorkshopDao().findById(connection, bookingData.getInt("workshop_id")).orElse(null);
                LocalDateTime bookingDate = bookingData.getTimestamp("booking_date").toLocalDateTime();
                String paymentStatus = bookingData.getString("payement_status");

                Booking newBooking = new Booking(workshop, null);
                newBooking.setBookingDate(bookingDate);
                newBooking.setPaymentStatus(paymentStatus);
                bookings.add(newBooking);
            }
            return bookings;
        } catch (SQLException sqlException) {
            return new ArrayList<>();
        }
    }

    public void replaceBookings(Connection connection, CommunityMember communityMember) {
        try {
            for (Booking booking : communityMember.getBookings()) {
                PreparedStatement statement = connection.prepareStatement("REPLACE INTO Bookings (workshop_id, community_member_id, booking_date, payement_status) VALUES (?, ?, ?, ?)");

                statement.setInt(1, booking.getWorkshop().getWorkshopId());
                statement.setInt(2, communityMember.getCommunityMemberId());
                statement.setDate(3, Date.valueOf(booking.getBookingDate().toLocalDate()));
                statement.setString(4, booking.getPaymentStatus());

                statement.executeUpdate();
            }
        } catch (SQLException sqlException) {
            System.out.println("Failed to replace bookings : " + sqlException.getMessage());
        }
    }

    public void deleteBookings(Connection connection, int communityMemberId) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM Bookings WHERE community_member_id = ?");

            statement.setInt(1, communityMemberId);

            statement.executeUpdate();
        } catch (SQLException sqlException) {
            System.out.println("Failed to delete bookings : " + sqlException.getMessage());
        }
    }

    public List<Review> findReviews(Connection connection, int communityMemberId) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Reviews r JOIN Community_members cm ON r.community_member_id = cm.community_member_id WHERE cm.community_member_id = ?");
            statement.setInt(1, communityMemberId);
            ResultSet reviewData = statement.executeQuery();
            List<Review> reviews = new ArrayList<>();
            while (reviewData.next()) {
                Artwork artwork = new JdbcArtworkDao().findById(connection, reviewData.getInt("artwork_id")).orElse(null);
                int rating = reviewData.getInt("rating");
                String comment = reviewData.getString("comment");
                LocalDate reviewDate = reviewData.getDate("review_date").toLocalDate();

                Review newReview = new Review(artwork, null, rating, comment);
                newReview.setReviewDate(reviewDate);
                reviews.add(newReview);
            }
            return reviews;
        } catch (SQLException sqlException) {
            return new ArrayList<>();
        }
    }

    public void replaceReviews(Connection connection, CommunityMember communityMember) {
        try {
            for (Review review : communityMember.getReviews()) {
                PreparedStatement statement = connection.prepareStatement("REPLACE INTO Reviews (artwork_id, community_member_id, rating, comment, review_date) VALUES (?, ?, ?, ?, ?)");

                statement.setInt(1, review.getArtwork().getArtworkId());
                statement.setInt(2, communityMember.getCommunityMemberId());
                statement.setDouble(3, review.getRating());
                statement.setString(4, review.getComment());
                statement.setDate(5, Date.valueOf(review.getReviewDate()));

                statement.executeUpdate();
            }
        } catch (SQLException sqlException) {
            System.out.println("Failed to replace reviews : " + sqlException.getMessage());
        }
    }

    public void deleteReviews(Connection connection, int communityMemberId) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM Reviews WHERE community_member_id = ?");

            statement.setInt(1, communityMemberId);

            statement.executeUpdate();
        } catch (SQLException sqlException) {
            System.out.println("Failed to delete reviews : " + sqlException.getMessage());
        }
    }

}
