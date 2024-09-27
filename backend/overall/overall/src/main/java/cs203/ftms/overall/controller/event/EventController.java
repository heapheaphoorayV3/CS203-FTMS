package cs203.ftms.overall.controller.event;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cs203.ftms.overall.dto.CreateEventDTO;
import cs203.ftms.overall.dto.clean.CleanEventDTO;
import cs203.ftms.overall.exception.FencerProfileIncompleteException;
import cs203.ftms.overall.model.tournamentrelated.Event;
import cs203.ftms.overall.model.userrelated.Fencer;
import cs203.ftms.overall.model.userrelated.Organiser;
import cs203.ftms.overall.model.userrelated.User;
import cs203.ftms.overall.service.event.EventService;
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
    public ResponseEntity<String> createEvent(@PathVariable int tid, @RequestBody @Valid List<CreateEventDTO> e) throws MethodArgumentNotValidException {
        System.out.println("pls");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        System.out.println("1");
        boolean newE = eventService.createEvent(tid, (Organiser) user, e);
        System.out.println("10");
        if (newE) {
            return new ResponseEntity<>("event creation successful", HttpStatus.CREATED);
        }
        return new ResponseEntity<>("event creation unsuccessful", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/register/{eid}")
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

    
    @GetMapping("/event-details/{eid}")
    public ResponseEntity<CleanEventDTO> getEvent(@PathVariable int eid) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Event e = eventService.getEvent(eid);
        CleanEventDTO ce = eventService.getCleanEventDTO(e);
        if (ce == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(ce, HttpStatus.OK);
    }

    
}
