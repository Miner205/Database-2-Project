-- ArtConnect Database sql

CREATE DATABASE ArtConnect;
USE ArtConnect;

CREATE TABLE Artists(
   artist_id INT,
   name VARCHAR(50) NOT NULL,
   bio VARCHAR(250),
   birth_year INT,
   contact_email VARCHAR(50),
   phone VARCHAR(50),
   city VARCHAR(50),
   website VARCHAR(50),
   is_active BOOLEAN,
   PRIMARY KEY(artist_id)
);

CREATE TABLE Social_media(
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
   description VARCHAR(250),
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
   description VARCHAR(250),
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
   description VARCHAR(250),
   curator_name VARCHAR(50),
   theme VARCHAR(50),
   gallery_id INT NOT NULL,
   PRIMARY KEY(exhibition_id),
   FOREIGN KEY(gallery_id) REFERENCES Galleries(gallery_id)
);

CREATE TABLE Practice(  -- ArtistDisciplines
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

CREATE TABLE Booking(
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
   comment VARCHAR(250),
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
