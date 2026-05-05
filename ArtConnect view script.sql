USE ArtConnect;

-- View to see the artworks
CREATE VIEW artworks_view AS
	SELECT art.artwork_id,
		art.title,
        art.creation_year,
        art.type,
        art.medium,
        dim.length,
        dim.width,
        dim.depth,
        art.description,
        art.price,
        art.status,
        art.artist_id
		FROM Artworks art
        JOIN Dimensions dim ON art.artwork_id = dim.artwork_id
        JOIN Tagged t ON art.artwork_id = t.artwork_id
        JOIN Artwork_tags artt ON t.artwork_tag_id = artt.artwork_tag_id;

-- View to see the artists
CREATE VIEW artists_view AS
	SELECT a.artist_id,
		a.name AS artist_name,
        a.bio,
        a.birth_year,
        d.name AS discipline_name,
        a.contact_email,
        a.phone,
        a.city,
        a.website,
        sm.platform,
        sm.account_handle,
        a.is_active
		FROM Artists a
        JOIN Practices p ON a.artist_id = p.artist_id
        JOIN Disciplines d ON p.discipline_id
        JOIN Social_medias sm ON a.artist_id = sm.artist_id;

-- View to see the exhibitions per day
CREATE VIEW exhibition_per_day_view AS
	SELECT oh.day,
		oh.opening_time,
        oh.closing_time,
		e.title AS exhibition_title,
		e.start_date,
        e.end_date,
        e.description AS exhibition_description,
        e.curator_name,
        e.theme,
		g.name AS gallery_name,
		g.address,
        g.owner_name,
        g.contact_phone,
        g.rating,
        g.website AS gallery_website
        FROM Opening_hours oh
        JOIN Exhibitions e ON oh.exhibition_id = e.exhibition_id
		JOIN Galleries g ON e.gallery_id = g.gallery_id
