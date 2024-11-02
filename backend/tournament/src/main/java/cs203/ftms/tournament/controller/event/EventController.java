package cs203.ftms.tournament.controller.event;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cs203.ftms.tournament.dto.CreateEventDTO;
import cs203.ftms.tournament.dto.UpdateEventDTO;
import cs203.ftms.tournament.dto.clean.CleanEventDTO;
import cs203.ftms.tournament.dto.clean.CleanTournamentFencerDTO;
import cs203.ftms.tournament.exception.EntityDoesNotExistException;
import cs203.ftms.tournament.exception.EventAlreadyExistsException;
import cs203.ftms.tournament.exception.FencerProfileIncompleteException;
import cs203.ftms.tournament.model.Event;
import cs203.ftms.tournament.model.TournamentFencer;
import cs203.ftms.tournament.model.userrelated.Fencer;
import cs203.ftms.tournament.model.userrelated.Organiser;
import cs203.ftms.tournament.model.userrelated.User;
import cs203.ftms.tournament.service.event.EventService;
import jakarta.validation.Valid;



@RestController
@CrossOrigin
@Validated
@RequestMapping("/api/v1/event")
public class EventController {
    private final EventService eventService; 

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping("/{tid}/create-event")
    @PreAuthorize("hasRole('ORGANISER')")
    public ResponseEntity<List<CleanEventDTO>> createEvent(@PathVariable int tid, @RequestBody @Valid List<CreateEventDTO> e) throws MethodArgumentNotValidException, EventAlreadyExistsException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        List<Event> newE = eventService.createEvent(tid, (Organiser) user, e);
        if (newE != null) {
            List<CleanEventDTO> dto = new ArrayList<>();
            for (Event event : newE) {
                dto.add(eventService.getCleanEventDTO(event));
            }
            return new ResponseEntity<>(dto, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/{eid}/update-event")
    @PreAuthorize("hasRole('ORGANISER')")
    public ResponseEntity<CleanEventDTO> updateEvent(@PathVariable int eid, @RequestBody @Valid UpdateEventDTO e) throws MethodArgumentNotValidException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Event event = eventService.updateEvent(eid, (Organiser) user, e);
        if (event != null) {
            return new ResponseEntity<>(eventService.getCleanEventDTO(event), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/register/{eid}")
    @PreAuthorize("hasRole('FENCER')")
    public ResponseEntity<String> registerEvent(@PathVariable int eid) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        if (((Fencer) user).getDebutYear() == 0) {
            throw new FencerProfileIncompleteException();
        }
        boolean register = eventService.registerEvent(eid, (Fencer) user);
        if (register) {
            return new ResponseEntity<>("event registration successful", HttpStatus.OK);
        }
        return new ResponseEntity<>("event registration unsuccessful", HttpStatus.BAD_REQUEST);
    }

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

    
    @GetMapping("/event-details/{eid}")
    public ResponseEntity<CleanEventDTO> getEvent(@PathVariable int eid) {
        Event event;
        try {
            event = eventService.getEvent(eid);
        } catch (EntityDoesNotExistException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        CleanEventDTO ce = eventService.getCleanEventDTO(event);
        if (ce == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(ce, HttpStatus.OK);
    }

    @GetMapping("/{eid}/get-event-ranking")
    @PreAuthorize("hasAnyRole('FENCER', 'ORGANISER', 'ADMIN')")
    public ResponseEntity<List<CleanTournamentFencerDTO>> getEventRanking(@PathVariable int eid) {
        List<TournamentFencer> rankings = eventService.getTournamentRanks(eid);
        List<CleanTournamentFencerDTO> resultDTO = new ArrayList<>(); 
        for (TournamentFencer tf : rankings) {
            resultDTO.add(eventService.getCleanTournamentFencerDTO(tf));
        }
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @PutMapping("/{eid}/end-event")
    @PreAuthorize("hasRole('ORGANISER')")
    public ResponseEntity<String> endEvent(@PathVariable int eid) {
        eventService.endTournamentEvent(eid);
        return new ResponseEntity<>("event ended", HttpStatus.OK);
    }
}   


