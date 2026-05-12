-- 3 roles, users & access rights

-- Create roles

CREATE ROLE 'admin_role';
CREATE ROLE 'artist_role';
CREATE ROLE 'member_role';

-- Grant permissions

-- Admin = full access
GRANT ALL PRIVILEGES ON ArtConnect.* TO 'admin_role';

-- Artist = manage own/artists content (Read everything, Manage artworks & workshops)
GRANT SELECT ON ArtConnect.* TO 'artist_role';
GRANT INSERT, UPDATE, DELETE ON ArtConnect.Artworks TO 'artist_role';
GRANT INSERT, UPDATE, DELETE ON ArtConnect.Dimensions TO 'artist_role';
GRANT INSERT, UPDATE, DELETE ON ArtConnect.Tagged TO 'artist_role';
GRANT INSERT, UPDATE, DELETE ON ArtConnect.Workshops TO 'artist_role';

-- Member = community user (View data, Book workshops, Leave reviews)
GRANT SELECT ON ArtConnect.* TO 'member_role';
GRANT INSERT ON ArtConnect.Bookings TO 'member_role';
GRANT INSERT ON ArtConnect.Reviews TO 'member_role';
GRANT INSERT ON ArtConnect.Favorite_disciplines TO 'member_role';

-- Create users and assign roles

-- Admin
CREATE USER 'admin1'@'localhost' IDENTIFIED BY 'adminpass';
GRANT 'admin_role' TO 'admin1'@'localhost';

-- Artist
CREATE USER 'artist1'@'localhost' IDENTIFIED BY 'artistpass';
GRANT 'artist_role' TO 'artist1'@'localhost';

-- Member
CREATE USER 'member1'@'localhost' IDENTIFIED BY 'memberpass';
GRANT 'member_role' TO 'member1'@'localhost';