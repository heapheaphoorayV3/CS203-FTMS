package cs203.ftms.overall.controller.event;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cs203.ftms.overall.dto.CreateEventDTO;
import cs203.ftms.overall.dto.UpdateEventDTO;
import cs203.ftms.overall.dto.clean.CleanEventDTO;
import cs203.ftms.overall.dto.clean.CleanTournamentFencerDTO;
import cs203.ftms.overall.exception.EventAlreadyExistsException;
import cs203.ftms.overall.exception.FencerProfileIncompleteException;
import cs203.ftms.overall.model.tournamentrelated.Event;
import cs203.ftms.overall.model.tournamentrelated.TournamentFencer;
import cs203.ftms.overall.model.userrelated.Fencer;
import cs203.ftms.overall.model.userrelated.Organiser;
import cs203.ftms.overall.model.userrelated.User;
import cs203.ftms.overall.service.event.EventService;
import jakarta.validation.Valid;

/**
 * Controller class responsible for handling HTTP requests related to event management.
 * Provides endpoints for creating, updating, registering, unregistering, and managing tournament events.
 */
@RestController
@CrossOrigin
@Validated
@RequestMapping("/api/v1/event")
public class EventController {
    
    private final EventService eventService; 

    /**
     * Constructor for EventController.
     * 
     * @param eventService The service layer component for handling event-related operations,
     *                     automatically injected by Spring's dependency injection mechanism.
     */
    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    /**
     * Creates multiple events for a specified tournament.
     *
     * @param tid The ID of the tournament.
     * @param eventList List of events to create, validated as CreateEventDTO.
     * @return ResponseEntity with a list of CleanEventDTOs and HttpStatus.CREATED on success,
     *         or HttpStatus.BAD_REQUEST if creation fails.
     * @throws MethodArgumentNotValidException if the input is not valid.
     * @throws EventAlreadyExistsException if the event already exists.
     */
    @PostMapping("/create-event/{tid}")
    @PreAuthorize("hasRole('ORGANISER')")
    public ResponseEntity<List<CleanEventDTO>> createEvent(@PathVariable int tid, @RequestBody @Valid List<CreateEventDTO> eventList)
            throws MethodArgumentNotValidException, EventAlreadyExistsException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        List<Event> newE = eventService.createEvent(tid, (Organiser) user, eventList);
        if (newE != null) {
            List<CleanEventDTO> res = new ArrayList<>();
            for (Event event : newE) {
                res.add(eventService.getCleanEventDTO(event));
            }
            return new ResponseEntity<>(res, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Updates an existing event.
     *
     * @param eid The ID of the event to update.
     * @param updateEventDTO Event data for update, validated as UpdateEventDTO.
     * @return ResponseEntity with updated CleanEventDTO and HttpStatus.OK on success,
     *         or HttpStatus.BAD_REQUEST if update fails.
     * @throws MethodArgumentNotValidException if the input is not valid.
     */
    @PutMapping("/update-event/{eid}")
    @PreAuthorize("hasRole('ORGANISER')")
    public ResponseEntity<CleanEventDTO> updateEvent(@PathVariable int eid, @RequestBody @Valid UpdateEventDTO updateEventDTO)
            throws MethodArgumentNotValidException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Event event = eventService.updateEvent(eid, (Organiser) user, updateEventDTO);
        return new ResponseEntity<>(eventService.getCleanEventDTO(event), HttpStatus.OK);
    }

    /**
     * Registers a fencer for an event, ensuring the fencer's profile is complete.
     *
     * @param eid The ID of the event to register for.
     * @return ResponseEntity with a success message and HttpStatus.OK if registration succeeds,
     *         or HttpStatus.BAD_REQUEST if registration fails or profile is incomplete.
     */
    @PutMapping("/register/{eid}")
    @PreAuthorize("hasRole('FENCER')")
    public ResponseEntity<String> registerEvent(@PathVariable int eid) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        if (((Fencer) user).getDebutYear() == 0) {
            throw new FencerProfileIncompleteException("Fencer profile incomplete, unable to perform operation!");
        }
        boolean register = eventService.registerEvent(eid, (Fencer) user);
        if (register) {
            return new ResponseEntity<>("event registration successful", HttpStatus.OK);
        }
        return new ResponseEntity<>("event registration unsuccessful", HttpStatus.BAD_REQUEST);
    }

    /**
     * Unregisters a fencer from an event.
     *
     * @param eid The ID of the event to unregister from.
     * @return ResponseEntity with a success message and HttpStatus.OK if unregistration succeeds,
     *         or HttpStatus.BAD_REQUEST if unregistration fails.
     */
    @PutMapping("/unregister/{eid}")
    @PreAuthorize("hasRole('FENCER')")
    public ResponseEntity<String> unregisterEvent(@PathVariable int eid) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        boolean unregister = eventService.unregisterEvent(eid, (Fencer) user);
        if (unregister) {
            return new ResponseEntity<>("event unregistration successful", HttpStatus.OK);
        }
        return new ResponseEntity<>("event unregistration unsuccessful", HttpStatus.BAD_REQUEST);
    }

    /**
     * Deletes an event.
     *
     * @param eid The ID of the event to delete.
     * @return ResponseEntity with a success message and HttpStatus.OK upon successful deletion.
     */
    @DeleteMapping("/delete-event/{eid}")
    @PreAuthorize("hasRole('ORGANISER')")
    public ResponseEntity<String> deleteEvent(@PathVariable int eid) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        eventService.deleteEvent(eid, (Organiser) user);
        return new ResponseEntity<>("event deleted", HttpStatus.OK);
    }

    /**
     * Retrieves the details of an event.
     *
     * @param eid The ID of the event to retrieve details for.
     * @return ResponseEntity with CleanEventDTO and HttpStatus.OK if retrieval is successful,
     *         or HttpStatus.BAD_REQUEST if the event does not exist.
     */
    @GetMapping("/event-details/{eid}")
    public ResponseEntity<CleanEventDTO> getEvent(@PathVariable int eid) {
        Event event = eventService.getEvent(eid);
        CleanEventDTO res = eventService.getCleanEventDTO(event);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    /**
     * Retrieves the ranking of fencers in an event.
     *
     * @param eid The ID of the event.
     * @return ResponseEntity with a list of CleanTournamentFencerDTO and HttpStatus.OK.
     */
    @GetMapping("/get-event-ranking/{eid}")
    public ResponseEntity<List<CleanTournamentFencerDTO>> getEventRanking(@PathVariable int eid) {
        List<TournamentFencer> rankings = eventService.getTournamentRanks(eid);
        List<CleanTournamentFencerDTO> res = new ArrayList<>(); 
        for (TournamentFencer tf : rankings) {
            res.add(eventService.getCleanTournamentFencerDTO(tf));
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    /**
     * Ends a tournament event.
     *
     * @param eid The ID of the event to end.
     * @return ResponseEntity with a success message and HttpStatus.OK upon successful ending.
     */
    @PutMapping("/end-event/{eid}")
    @PreAuthorize("hasRole('ORGANISER')")
    public ResponseEntity<String> endEvent(@PathVariable int eid) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Organiser organiser = (Organiser) user;
        eventService.endTournamentEvent(eid, organiser);
        return new ResponseEntity<>("event ended", HttpStatus.OK);
    }

    /**
     * Retrieves all events for specific gender and weapon.
     *
     * @return ResponseEntity with a list of CleanEventDTO and HttpStatus.OK.
     */
    @GetMapping("/get-all-events-by-gender-and-weapon")
    @PreAuthorize("hasRole('FENCER')")
    public ResponseEntity<List<CleanEventDTO>> getAllEventsByGenderAndWeapon(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        List<Event> events = eventService.getFutureEventsByGenderAndWeapon(((Fencer) user).getGender(), ((Fencer) user).getWeapon());
        List<CleanEventDTO> res = new ArrayList<>();
        for (Event event : events) {
            res.add(eventService.getCleanEventDTO(event));
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

}   


