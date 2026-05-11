package com.project.artconnect.util;

import com.project.artconnect.service.*;
import com.project.artconnect.service.impl.InMemory.*;
import com.project.artconnect.service.impl.*;

/**
 * Service Provider to manage singleton instances of services and handle their
 * initialization.
 */
public class ServiceProvider {
    private static final ArtistServiceImpl artistService = new ArtistServiceImpl();
    private static final ArtworkServiceImpl artworkService = new ArtworkServiceImpl();
    private static final GalleryServiceImpl galleryService = new GalleryServiceImpl();
    private static final WorkshopServiceImpl workshopService = new WorkshopServiceImpl();
    private static final CommunityServiceImpl communityService = new CommunityServiceImpl();

    /*static {
        // Initialize services with their dependencies
        artworkService.initData(artistService);
        galleryService.initData(artworkService);
        workshopService.initData(artistService);
        communityService.initData(artworkService);
    }*/

    public static ArtistService getArtistService() {
        return artistService;
    }

    public static ArtworkService getArtworkService() {
        return artworkService;
    }

    public static GalleryService getGalleryService() {
        return galleryService;
    }

    public static WorkshopService getWorkshopService() {
        return workshopService;
    }

    public static CommunityService getCommunityService() {
        return communityService;
    }
}
