
-- tests

USE ArtConnect;

Select * From Artworks a Join Dimensions d on a.artwork_id = d.artwork_id;




-- =========================================================
-- UPDATE REQUESTS (1-2 per main table)
-- =========================================================

-- ARTISTS
UPDATE Artists
SET city = 'Nice'
WHERE artist_id = 1;

UPDATE Artists
SET is_active = FALSE
WHERE artist_id = 2;

-- ARTWORKS
UPDATE Artworks
SET price = 1500.00
WHERE artwork_id = 1;

UPDATE Artworks
SET status = 'Reserved'
WHERE artwork_id = 1;

-- GALLERIES
UPDATE Galleries
SET rating = 4.9
WHERE gallery_id = 1;

-- EXHIBITIONS
UPDATE Exhibitions
SET theme = 'Contemporary Innovation'
WHERE exhibition_id = 1;

-- WORKSHOPS
UPDATE Workshops
SET max_participant = 20
WHERE workshop_id = 1;

UPDATE Workshops
SET price = 110.00
WHERE workshop_id = 1;

-- COMMUNITY MEMBERS
UPDATE Community_members
SET membership_type = 'VIP'
WHERE community_member_id = 2;



-- =========================================================
-- DELETE REQUESTS (careful with FK dependencies)
-- =========================================================

-- 1. Delete review first (dependency)
DELETE FROM Reviews
WHERE artwork_id = 1
AND community_member_id = 1;

-- 2. Delete booking first
DELETE FROM Bookings
WHERE workshop_id = 1
AND community_member_id = 1;

-- 3. Delete tagged relation first
DELETE FROM Tagged
WHERE artwork_id = 1;

-- 4. Delete exhibited relation first
DELETE FROM Exhibited
WHERE artwork_id = 1;

-- 5. Delete dimensions first
DELETE FROM Dimensions
WHERE artwork_id = 1;

