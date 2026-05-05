-- 2 Triggers 2 stored programs 

-- Indexes
CREATE INDEX Slack_Art ON Artworks(title, creation_year); -- To help researching among a massive list of artworks, we need to look at the title and creation year
CREATE INDEX Slack_Community ON Community_Members(name, email, membership_type); -- Same logic among community members, except we look at name, email and membership type
CREATE INDEX Slack_Workshop ON Workshops(title, workshop_date, instructor); -- We look at title, date and instructor of workshop
CREATE INDEX Slack_Artist ON Artists(name, contact_email, is_active); -- We look at name, contactEmail and isActive

-- Stored Programs
DROP FUNCTION IF EXISTS Is_Exhibited;
DROP PROCEDURE IF EXISTS Find_Exhibitions;
DELIMITER //

CREATE FUNCTION Is_Exhibited(artwork VARCHAR(100), chosen_date DATE) -- Function that checks if at a chosen date, an artwork designated by its title is exhibited somewhere
RETURNS BOOLEAN DETERMINISTIC
BEGIN
	SET @id = (SELECT artwork_id FROM Artworks WHERE title = artwork);
	IF (SELECT COUNT(*) FROM Exhibited INNER JOIN Exhibitions ON Exhibited.exhibition_id = Exhibitions.exhibition_id WHERE artwork_id = @id AND chosen_date >= start_date AND chosen_date <= end_date) != 0 THEN
		RETURN 1;
	ELSE
		RETURN 0;
	END IF;
END//

CREATE PROCEDURE Find_Exhibitions(artwork VARCHAR(100), chosen_date DATE) -- Echoing the function above, displays the exhibitions the artwork is in, else null return
BEGIN
	SET @id = (SELECT artwork_id FROM Artworks WHERE title = artwork);
	IF Is_Exhibited(artwork, chosen_date) = 1 THEN
		SELECT Exhibitions.title, Exhibitions.description, Exhibitions.curator_name, Exhibitions.start_date, Exhibitions.end_date, Galleries.name FROM Exhibited INNER JOIN Exhibitions ON Exhibited.exhibition_id = Exhibitions.exhibition_id INNER JOIN Galleries ON Galleries.gallery_id = Exhibitions.gallery_id WHERE artwork_id = @id AND chosen_date >= start_date AND chosen_date <= end_date;
    ELSE
		SELECT Exhibitions.title, Exhibitions.description, Exhibitions.curator_name, Exhibitions.start_date, Exhibitions.end_date, Galleries.name FROM Exhibited INNER JOIN Exhibitions ON Exhibited.exhibition_id = Exhibitions.exhibition_id INNER JOIN Galleries ON Galleries.gallery_id = Exhibitions.gallery_id WHERE artwork_id = @id AND chosen_date >= start_date AND chosen_date <= end_date; -- Will return null anyway, so we leave it here
    END IF;
END//

-- Triggers
DROP TRIGGER IF EXISTS Date_check//
DROP TRIGGER IF EXISTS Artwork_check//
DROP TRIGGER IF EXISTS Fully_Booked//

CREATE TRIGGER Date_check BEFORE INSERT ON Exhibitions -- Trigger that acts as a validation condition, raising an error in case there is an attempted insertion of an exhibition with a starting date after end date
FOR EACH ROW
BEGIN
	IF NEW.start_date > NEW.end_date THEN
		SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = "A Start date shall not be posterior to the end date, please reenter the data.";
    END IF;
END//

CREATE TRIGGER Artwork_check BEFORE INSERT ON Artworks -- Trigger that detects if the inserted artwork record was created before artist birth year 
FOR EACH ROW
BEGIN
	SET @birthy = (SELECT birth_year FROM Artists WHERE artist_id = NEW.artist_id);
	IF NEW.creation_year <= @birthy THEN
		SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = "Creation year before artist's birth.";
	END IF;
END//

CREATE TRIGGER Fully_Booked BEFORE INSERT ON Bookings -- Triggers that sends an error if the number of maximum participants is reached
FOR EACH ROW
BEGIN
	IF (SELECT max_participant FROM Workshops WHERE workshop_id = NEW.workshop_id) <= (SELECT COUNT(*) FROM Bookings WHERE workshop_id = NEW.workshop_id) THEN
		SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = "Workshop is full. Please try booking another workshop.";
    END IF;
END//

DELIMITER ;

SELECT Is_Exhibited("Fragments of Self", "2024-06-29");
SELECT Is_Exhibited("Fragments of Self", "2024-07-01");
SELECT Is_Exhibited("Hidden Horizons", "2024-06-29");

CALL Find_Exhibitions("Fragments of Self", "2024-06-29");
CALL Find_Exhibitions("Fragments of Self", "2024-07-01");

-- Trigger tests

INSERT INTO Exhibitions (exhibition_id, title, start_date, end_date, description, curator_name, theme, gallery_id)
VALUES (3, 'Valid Expo', '2024-08-01', '2024-08-30', 'Test expo', 'Test Curator', 'Test Theme', 1);
INSERT INTO Exhibitions (exhibition_id, title, start_date, end_date, description, curator_name, theme, gallery_id)
VALUES (4, 'Invalid Expo', '2024-09-01', '2024-08-01', 'Should fail', 'Test Curator', 'Test Theme', 1);

INSERT INTO Artworks (artwork_id, title, creation_year, type, medium, description, price, status, artist_id)
VALUES (7, 'Valid Artwork', 2000, 'Painting', 'Oil', 'Valid test', 300, 'FOR_SALE', 1);
INSERT INTO Artworks (artwork_id, title, creation_year, type, medium, description, price, status, artist_id)
VALUES (8, 'Impossible Artwork', 1980, 'Painting', 'Oil', 'Should fail', 300, 'FOR_SALE', 1);

-- Adds 8 participants to reach 10
INSERT INTO Community_members (community_member_id, name, email, birth_year, phone, city, membership_type) VALUES
(4, 'Test User 4', 'u4@mail.com', 1990, '000000004', 'Paris', 'free'),
(5, 'Test User 5', 'u5@mail.com', 1991, '000000005', 'Paris', 'free'),
(6, 'Test User 6', 'u6@mail.com', 1992, '000000006', 'Paris', 'free'),
(7, 'Test User 7', 'u7@mail.com', 1993, '000000007', 'Paris', 'free'),
(8, 'Test User 8', 'u8@mail.com', 1994, '000000008', 'Paris', 'free'),
(9, 'Test User 9', 'u9@mail.com', 1995, '000000009', 'Paris', 'free'),
(10, 'Test User 10', 'u10@mail.com', 1996, '000000010', 'Paris', 'free');

INSERT INTO Bookings (workshop_id, community_member_id, booking_date, payement_status) VALUES
(1,3,'2024-05-23','PAID'),
(1,4,'2024-05-24','PAID'),
(1,5,'2024-05-25','PAID'),
(1,6,'2024-05-26','PAID'),
(1,7,'2024-05-27','PAID'),
(1,8,'2024-05-28','PAID'),
(1,9,'2024-05-29','PAID'),
(1,10,'2024-05-30','PAID');

INSERT INTO Bookings (workshop_id, community_member_id, booking_date, payement_status)
VALUES (1,4,'2024-06-01','PAID');