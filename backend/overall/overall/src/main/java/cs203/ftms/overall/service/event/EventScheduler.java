package cs203.ftms.overall.service.event;

import java.time.LocalDate;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import cs203.ftms.overall.model.tournamentrelated.Event;
import cs203.ftms.overall.model.tournamentrelated.Tournament;
import cs203.ftms.overall.model.tournamentrelated.TournamentFencer;
import cs203.ftms.overall.model.userrelated.Fencer;
import cs203.ftms.overall.repository.tournamentrelated.EventRepository;
import cs203.ftms.overall.repository.tournamentrelated.TournamentRepository;
import cs203.ftms.overall.service.admin.MailService;

/**
 * Scheduler component for managing event-related tasks.
 * Responsible for checking if events have sufficient participants 
 * and handling event cancellations.
 */
@Component
public class EventScheduler {
    private final TournamentRepository tournamentRepository;
    private final EventRepository eventRepository;
    private final EventService eventService;
    private final MailService mailService;

    public EventScheduler(TournamentRepository tournamentRepository, EventRepository eventRepository, EventService eventService, MailService mailService) {
        this.tournamentRepository = tournamentRepository;
        this.eventRepository = eventRepository;
        this.eventService = eventService;
        this.mailService = mailService;
    }

    /**
     * Scheduled task that runs daily at midnight to check if events
     * have sufficient participants and cancels those that do not.
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void checkEventHasEnoughParticipants() {
        List<Tournament> tournaments = tournamentRepository.findBySignupEndDate(LocalDate.now().minusDays(1));
        for (Tournament tournament : tournaments) {
            for (Event event : tournament.getEvents()) {
                if (event.getParticipantCount() < event.getMinParticipants()) {
                    handleEventCancellation(tournament, event);
                }
            }
        }
    }

    private void handleEventCancellation(Tournament tournament, Event event) {
        notifyFencers(event);
        eventRepository.delete(event);
        notifyOrganiser(tournament, event);
    }

    private void notifyFencers(Event event) {
        for (TournamentFencer tfencer : event.getFencers()) {
            Fencer fencer = tfencer.getFencer();
            eventService.unregisterEvent(event.getId(), fencer);
            String content = String.format(
                "Dear %s,\n\nWe regret to inform you that the event you registered for %s (%s %s) has been cancelled due to insufficient participants. We hope to see you in future events.\n\nBest Regards,\nFTMS",
                fencer.getName(), event.getTournament().getName(), getGenderString(event.getGender()), getWeaponString(event.getWeapon())
            );
            mailService.sendMail(fencer.getEmail(), "Event Cancellation", content);
        }
    }

    private void notifyOrganiser(Tournament tournament, Event event) {
        String content = String.format(
            "Dear %s,\n\nWe regret to inform you that the event %s (%s %s) has been cancelled due to insufficient participants. Sorry for the inconvenience caused.\n\nBest Regards,\nFTMS",
            tournament.getOrganiser().getName(), tournament.getName(), getGenderString(event.getGender()), getWeaponString(event.getWeapon())
        );
        mailService.sendMail(tournament.getOrganiser().getEmail(), "Event Cancellation", content);
    }

    private String getGenderString(char gender) {
        return gender == 'M' ? "Men" : "Women";
    }

    private String getWeaponString(char weapon) {
        switch (weapon) {
            case 'E':
                return "Epee";
            case 'F':
                return "Foil";
            case 'S':
                return "Sabre";
            default:
                return "Unknown";
        }
    }
}
