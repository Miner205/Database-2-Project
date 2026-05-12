@startuml
title Class Diagram - Dao Classes

interface ArtistDao {
  ~List<Artist> findAll()
  ~void save(artist : Artist)
  ~void update(artist : Artist)
  ~void delete(artistName : String)
  ~List<Artist> findByCity(city : String)
}


interface ArtworkDao {
  ~List<Artwork> findAll()
  ~void save(artwork : Artwork)
  ~void update(artwork : Artwork)
  ~void delete(title : String)
  ~List<Artwork> findByArtistName(artistName : String)
}

interface CommunityMemberDao {
  ~Optional<CommunityMember> findById(id : Long)
  ~List<CommunityMember> findAll()
}

interface ExhibitionDao {
  ~List<Exhibition> findAll()
  ~void save(exhibition : Exhibition)
  ~void update(exhibition : Exhibition)
  ~void delete(title : String)
}

interface GalleryDao {
  ~Optional<Gallery> findById(id : Long)
  ~List<Gallery> findAll()
}

interface WorkshopDao {
  ~Optional<Workshop> findById(id : Long)
  ~List<Workshop> findAll()
}

@enduml
