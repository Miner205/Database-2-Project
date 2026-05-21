-- one complex transactional scenario

USE ArtConnect;

-- Scenario: Register a Member in multiple Workshops

DROP PROCEDURE IF EXISTS register_member_in_two_workshops;
DELIMITER //
CREATE PROCEDURE register_member_in_two_workshops(
    IN memberId INT,
    IN workshopA INT,
    IN workshopB INT)
BEGIN
	DECLARE already_exists INT DEFAULT 0;
    DECLARE spots INT;

    START TRANSACTION;
    
    -- Check if already registered in either workshop
    SELECT COUNT(*) INTO already_exists
    FROM Bookings
    WHERE community_member_id = memberId
    AND workshop_id IN (workshopA, workshopB);

    -- Check workshop A
    SELECT max_participant - COUNT(*) INTO spots
    FROM Workshops w
    LEFT JOIN Bookings b ON w.workshop_id = b.workshop_id
    WHERE w.workshop_id = workshopA
    GROUP BY w.workshop_id
    FOR UPDATE;

    IF spots <= 0 OR already_exists > 0 THEN
        ROLLBACK;
    ELSE
        INSERT INTO Bookings VALUES (workshopA, memberId, CURDATE(), 'Paid');
    END IF;

    -- Check workshop B
    SELECT max_participant - COUNT(*) INTO spots
    FROM Workshops w
    LEFT JOIN Bookings b ON w.workshop_id = b.workshop_id
    WHERE w.workshop_id = workshopB
    GROUP BY w.workshop_id
    FOR UPDATE;

    IF spots <= 0 OR already_exists > 0 THEN
        ROLLBACK;
    ELSE
        INSERT INTO Bookings VALUES (workshopB, memberId, CURDATE(), 'Paid');
    END IF;

    COMMIT;
END//
DELIMITER ;


DROP VIEW IF EXISTS members_in_workshops;
-- View to see the workshops and the members registered in them
CREATE VIEW members_in_workshops AS
	SELECT w.workshop_id, w.title as workshop_title, w.max_participant,
    m.community_member_id, m.name as member_name,
    b.payement_status
	FROM Workshops w
	JOIN Bookings b ON w.workshop_id = b.workshop_id
	JOIN Community_members m ON m.community_member_id = b.community_member_id;


DROP PROCEDURE IF EXISTS get_nb_members_in_workshop;
-- Procedure to display the nb of members registered in a given workshop
DELIMITER //
CREATE PROCEDURE get_nb_members_in_workshop(IN p_workshop_id INT)
BEGIN
    SELECT workshop_id, COUNT(*) AS nb_members_registered, max_participant
    FROM members_in_workshops
    WHERE workshop_id = p_workshop_id
    GROUP BY workshop_id, max_participant;
END//
DELIMITER ;


SELECT * FROM members_in_workshops;

CALL register_member_in_two_workshops(4, 1, 2);

SELECT * FROM members_in_workshops;
CALL get_nb_members_in_workshop(1);
CALL get_nb_members_in_workshop(2);

INSERT INTO Community_members VALUES
(5, 'Julien Morel', 'julien.morel@mail.com', 1987, '101010101', 'Paris', 'free'),
(6, 'Camille Laurent', 'camille.laurent@mail.com', 1993, '202020202', 'Lyon', 'premium'),
(7, 'Thomas Garcia', 'thomas.garcia@mail.com', 1985, '303030303', 'Marseille', 'free'),
(8, 'Sarah Nguyen', 'sarah.nguyen@mail.com', 1998, '404040404', 'Toulouse', 'free'),
(9, 'Hugo Bernard', 'hugo.bernard@mail.com', 1991, '505050505', 'Nice', 'premium'),
(10, 'Chloé Petit', 'chloe.petit@mail.com', 2001, '606060606', 'Nantes', 'free');

CALL register_member_in_two_workshops(5, 1, 2);
CALL register_member_in_two_workshops(6, 1, 2);
CALL register_member_in_two_workshops(7, 1, 2);
CALL register_member_in_two_workshops(8, 1, 2);
CALL register_member_in_two_workshops(9, 1, 2);

CALL register_member_in_two_workshops(10, 1, 2);
-- > Member 10 is not registered in the workshops since workshops 2 is full.

SELECT * FROM members_in_workshops;
CALL get_nb_members_in_workshop(1);
CALL get_nb_members_in_workshop(2);