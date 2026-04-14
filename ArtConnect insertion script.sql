-- insertion script of the ArtConnect database.

USE ArtConnect;

-- Prompts used for the LLM(ChatGPT) to insert sample data.
/*
Create a sample data insertion script (artists, works, events, members…) to 
demonstrate interesting cases (multiple artists, multiple events, cross
participations, etc.).


CREATE DATABASE ArtConnect;
USE ArtConnect;

CREATE TABLE Artists(
   artist_id INT,
   name VARCHAR(50) NOT NULL,
   bio TEXT,
   birth_year INT,
   contact_email VARCHAR(50),
   phone VARCHAR(50),
   city VARCHAR(50),
   website VARCHAR(100),
   is_active BOOLEAN,
   PRIMARY KEY(artist_id)
);

CREATE TABLE Social_medias(
	artist_id INT,
    platform VARCHAR(50),
    accountHandle VARCHAR(50),
    PRIMARY KEY(artist_id, platform),
    FOREIGN KEY(artist_id) REFERENCES Artists(artist_id)
);

CREATE TABLE Disciplines(
   disciplines_id INT,
   name VARCHAR(50) NOT NULL,
   PRIMARY KEY(disciplines_id)
);

CREATE TABLE Artworks(
   artwork_id INT,
   title VARCHAR(100),
   creation_year INT,
   type VARCHAR(25),
   medium VARCHAR(25),
   description TEXT,
   price DECIMAL(15,2),
   status VARCHAR(25) NOT NULL,
   artist_id INT NOT NULL,
   PRIMARY KEY(artwork_id),
   FOREIGN KEY(artist_id) REFERENCES Artists(artist_id)
);

CREATE TABLE Dimensions(
	artwork_id INT,
    length DECIMAL(15,2),
    width DECIMAL(15,2),
    depth DECIMAL(15,2),
    PRIMARY KEY(artwork_id),
    FOREIGN KEY(artwork_id) REFERENCES Artworks(artwork_id)
);

CREATE TABLE Artwork_tags(
   artwork_tag_id INT,
   name VARCHAR(100) NOT NULL,
   PRIMARY KEY(artwork_tag_id)
);

CREATE TABLE Workshops(
   workshop_id INT,
   title VARCHAR(100) NOT NULL,
   workshop_date DATE,
   duration_minutes INT,
   max_participant INT,
   price DECIMAL(15,2),
   location VARCHAR(100),
   description TEXT,
   level VARCHAR(25),
   instructor INT NOT NULL,
   PRIMARY KEY(workshop_id),
   FOREIGN KEY(instructor) REFERENCES Artists(artist_id)
);

CREATE TABLE Community_members(
   community_member_id INT,
   name VARCHAR(50) NOT NULL,
   email VARCHAR(50),
   birth_year INT,
   phone VARCHAR(50),
   city VARCHAR(50),
   membership_type VARCHAR(25) NOT NULL,
   PRIMARY KEY(community_member_id)
);

CREATE TABLE Galleries(
   gallery_id INT,
   name VARCHAR(100) NOT NULL,
   address VARCHAR(100),
   owner_name VARCHAR(50),
   contact_phone VARCHAR(50),
   rating DECIMAL(5,2),
   website VARCHAR(100),
   PRIMARY KEY(gallery_id)
);

CREATE TABLE Exhibitions(
   exhibition_id INT,
   title VARCHAR(100),
   start_date DATE,
   end_date DATE,
   description TEXT,
   curator_name VARCHAR(50),
   theme VARCHAR(100),
   gallery_id INT NOT NULL,
   PRIMARY KEY(exhibition_id),
   FOREIGN KEY(gallery_id) REFERENCES Galleries(gallery_id)
);

CREATE TABLE Practices(  -- ArtistDisciplines
   artist_id INT,
   disciplines_id INT,
   PRIMARY KEY(artist_id, disciplines_id),
   FOREIGN KEY(artist_id) REFERENCES Artists(artist_id),
   FOREIGN KEY(disciplines_id) REFERENCES Disciplines(disciplines_id)
);

CREATE TABLE Tagged(
   artwork_id INT,
   artwork_tag_id INT,
   PRIMARY KEY(artwork_id, artwork_tag_id),
   FOREIGN KEY(artwork_id) REFERENCES Artworks(artwork_id),
   FOREIGN KEY(artwork_tag_id) REFERENCES Artwork_tags(artwork_tag_id)
);

CREATE TABLE Bookings(
   workshop_id INT,
   community_member_id INT,
   booking_date DATE NOT NULL,
   payement_status VARCHAR(25) NOT NULL,
   PRIMARY KEY(workshop_id, community_member_id),
   FOREIGN KEY(workshop_id) REFERENCES Workshops(workshop_id),
   FOREIGN KEY(community_member_id) REFERENCES Community_members(community_member_id)
);

CREATE TABLE Favorite_disciplines(
   disciplines_id INT,
   community_member_id INT,
   PRIMARY KEY(disciplines_id, community_member_id),
   FOREIGN KEY(disciplines_id) REFERENCES Disciplines(disciplines_id),
   FOREIGN KEY(community_member_id) REFERENCES Community_members(community_member_id)
);

CREATE TABLE Reviews(
   artwork_id INT,
   community_member_id INT,
   rating DECIMAL(5,2) NOT NULL,
   comment TEXT,
   review_date DATE NOT NULL,
   PRIMARY KEY(artwork_id, community_member_id),
   FOREIGN KEY(artwork_id) REFERENCES Artworks(artwork_id),
   FOREIGN KEY(community_member_id) REFERENCES Community_members(community_member_id)
);

CREATE TABLE Exhibited(
   artwork_id INT,
   exhibition_id INT,
   PRIMARY KEY(artwork_id, exhibition_id),
   FOREIGN KEY(artwork_id) REFERENCES Artworks(artwork_id),
   FOREIGN KEY(exhibition_id) REFERENCES Exhibitions(exhibition_id)
);

CREATE TABLE Opening_hours (
	exhibition_id INT,  -- we linked it to exhibitions instead of galleries
    day VARCHAR(50),
    opening_time TIME,
    closing_time TIME,
	PRIMARY KEY(exhibition_id, day),
    FOREIGN KEY(exhibition_id) REFERENCES Exhibitions(exhibition_id)
);
*/
/*
add as the case of an inactive artist (tell me where to add new data)
*/
/*
create a new artwork not exhibited
*/
/*
generate another description for this artwork (real description of the artwork)
*/


-- Generated (and verified/tweaked) insertion script:

-- =========================
-- ARTISTS
-- =========================
INSERT INTO Artists (artist_id, name, bio, birth_year, contact_email, phone, city, website, is_active) VALUES
(1, 'Alice Moreau', 'Contemporary painter exploring identity.', 1985, 'alice@mail.com', '123456789', 'Paris', 'aliceart.com', TRUE),
(2, 'Lucas Bernard', 'Digital artist and sculptor.', 1990, 'lucas@mail.com', '987654321', 'Lyon', 'lucasdesign.com', TRUE),
(3, 'Sofia Rossi', 'Photographer focused on urban life.', 1988, 'sofia@mail.com', '456123789', 'Marseille', 'sofiaphoto.com', TRUE),
(4, 'Marc Dubois', 'Former sculptor, now retired.', 1970, 'marc@mail.com', '000111222', 'Bordeaux', 'marcdubois.com', FALSE);

-- =========================
-- SOCIAL MEDIA
-- =========================
INSERT INTO Social_medias (artist_id, platform, account_handle) VALUES
(1, 'Instagram', '@aliceart'),
(1, 'Twitter', '@alice_paints'),
(2, 'Instagram', '@lucas3d'),
(3, 'Instagram', '@sofiashots'),
(3, 'LinkedIn', 'sofia-rossi-photo');

-- =========================
-- DISCIPLINES
-- =========================
INSERT INTO Disciplines (disciplines_id, name) VALUES
(1, 'Painting'),
(2, 'Sculpture'),
(3, 'Photography'),
(4, 'Digital Art');

-- =========================
-- PRACTICES (many-to-many)
-- =========================
INSERT INTO Practices (artist_id, disciplines_id) VALUES
(1,1),
(2,2),
(2,4), -- Lucas does multiple disciplines
(3,3),
(4,2);

-- =========================
-- ARTWORKS
-- =========================
INSERT INTO Artworks (artwork_id, title, creation_year, type, medium, description, price, status, artist_id) VALUES
(1, 'Fragments of Self', 2022, 'Painting', 'Oil on canvas', 'Explores fragmented identity.', 1200.00, 'EXHIBITED', 1),
(2, 'Digital Dreams', 2023, 'Digital', '3D Render', 'Futuristic digital landscape.', 800.00, 'EXHIBITED', 2),
(3, 'Urban Silence', 2021, 'Photography', 'Black & White Photo', 'City life in silence.', 500.00, 'EXHIBITED', 3),
(4, 'Hybrid Form', 2023, 'Sculpture', 'Mixed Media', 'Fusion of physical and digital.', 1500.00, 'EXHIBITED', 2),
(5, 'Silent Stone', 2015, 'Sculpture', 'Marble', 'Minimalist marble sculpture.', 2000.00, 'SOLD', 4),
(6, 'Hidden Horizons', 2024, 'Painting', 'Acrylic on canvas', 'A contemplative landscape where layered acrylic textures evoke distant horizons fading into light. Subtle gradients of blue and ochre suggest the meeting of sky and earth, while soft brushwork creates a sense of depth and stillness. The piece invites the viewer to reflect on distance, memory, and the quiet beauty of unseen places.', 900.00, 'FOR_SALE', 1);

-- =========================
-- DIMENSIONS
-- =========================
INSERT INTO Dimensions (artwork_id, length, width, depth) VALUES
(1, 100, 80, NULL),
(2, NULL, NULL, NULL),
(3, 60, 40, NULL),
(4, 50, 50, 120),
(5, 70, 40, 150),
(6, 80, 60, NULL);

-- =========================
-- TAGS
-- =========================
INSERT INTO Artwork_tags (artwork_tag_id, name) VALUES
(1, 'Abstract'),
(2, 'Urban'),
(3, 'Modern'),
(4, 'Experimental');

-- =========================
-- TAGGED (many-to-many)
-- =========================
INSERT INTO Tagged (artwork_id, artwork_tag_id) VALUES
(1,1),
(1,3),
(2,3),
(2,4),
(3,2),
(4,4),
(6,1),
(6,3);

-- =========================
-- COMMUNITY MEMBERS
-- =========================
INSERT INTO Community_members (community_member_id, name, email, birth_year, phone, city, membership_type) VALUES
(1, 'Emma Dubois', 'emma@mail.com', 1995, '111222333', 'Paris', 'premium'),
(2, 'Noah Martin', 'noah@mail.com', 1992, '444555666', 'Lyon', 'free'),
(3, 'Léa Petit', 'lea@mail.com', 2000, '777888999', 'Nice', 'free');

-- =========================
-- FAVORITE DISCIPLINES
-- =========================
INSERT INTO Favorite_disciplines (disciplines_id, community_member_id) VALUES
(1,1),
(3,1),
(4,2),
(2,3);

-- =========================
-- GALLERIES
-- =========================
INSERT INTO Galleries (gallery_id, name, address, owner_name, contact_phone, rating, website) VALUES
(1, 'Galerie Lumière', 'Paris Center', 'Jean Dupont', '0102030405', 4.5, 'galerielumiere.com'),
(2, 'Modern Art Space', 'Lyon Downtown', 'Claire Martin', '0607080910', 4.7, 'modernartspace.com');

-- =========================
-- EXHIBITIONS
-- =========================
INSERT INTO Exhibitions (exhibition_id, title, start_date, end_date, description, curator_name, theme, gallery_id) VALUES
(1, 'Voices of the City', '2024-06-01', '2024-06-30', 'Urban themed exhibition.', 'Marie Curator', 'Urban Life', 1),
(2, 'Digital Futures', '2023-07-01', NULL, 'Exploring digital art.', 'Paul Curator', 'Technology', 2);

-- =========================
-- EXHIBITED (cross artist participation)
-- =========================
INSERT INTO Exhibited (artwork_id, exhibition_id) VALUES
(1,1),
(3,1), -- multiple artworks(from different artists) in same exhibition
(2,2),
(4,2);

-- =========================
-- OPENING HOURS
-- =========================
INSERT INTO Opening_hours (exhibition_id, day, opening_time, closing_time) VALUES
(1, 'Monday', '10:00:00', '18:00:00'),
(1, 'Tuesday', '10:00:00', '18:00:00'),
(2, 'Wednesday', '11:00:00', '19:00:00');

-- =========================
-- WORKSHOPS
-- =========================
INSERT INTO Workshops (workshop_id, title, workshop_date, duration_minutes, max_participant, price, location, description, level, instructor) VALUES
(1, 'Intro to Painting', '2024-06-10', 120, 10, 50.00, 'Paris Studio', 'Learn basics of painting.', 'beginner', 1),
(2, '3D Art Masterclass', '2024-07-05', 180, 8, 100.00, 'Lyon Lab', 'Advanced digital sculpting.', 'advanced', 2);

-- =========================
-- BOOKINGS (many-to-many)
-- =========================
INSERT INTO Bookings (workshop_id, community_member_id, booking_date, payement_status) VALUES
(1,1,'2024-05-20','PAID'),
(1,2,'2024-05-22','PENDING'),
(2,1,'2024-06-15','PAID'), -- Emma joins multiple workshops
(2,3,'2024-06-16','PAID');

-- =========================
-- REVIEWS (cross interactions)
-- =========================
INSERT INTO Reviews (artwork_id, community_member_id, rating, comment, review_date) VALUES
(1,1,4.5,'Beautiful and emotional.','2024-06-05'),
(2,1,5.0,'Stunning digital work!','2024-07-02'),
(3,2,4.0,'Very powerful photo.','2024-06-10'),
(1,3,3.5,'Interesting concept.','2024-06-12');
