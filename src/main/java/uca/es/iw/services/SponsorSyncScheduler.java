package uca.es.iw.services;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SponsorSyncScheduler {

    private final UserService userService;

    public SponsorSyncScheduler(UserService sponsorService) {
        this.userService = sponsorService;
    }

    @Scheduled(cron = "0 0 0 * * SUN") // Ejecutar cada lunes a medianoche
    public void scheduleSyncSponsors() {
        System.out.println("Iniciando sincronización de promotores...");
        userService.syncSponsors();
        System.out.println("Sincronización completada.");
    }
}
