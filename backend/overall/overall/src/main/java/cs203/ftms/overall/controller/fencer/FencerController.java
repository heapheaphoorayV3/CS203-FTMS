package cs203.ftms.overall.controller.fencer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cs203.ftms.overall.dto.CompleteFencerProfileDTO;
import cs203.ftms.overall.dto.clean.CleanEventDTO;
import cs203.ftms.overall.dto.clean.CleanFencerDTO;
import cs203.ftms.overall.dto.clean.CleanTournamentDTO;
import cs203.ftms.overall.model.tournamentrelated.Event;
import cs203.ftms.overall.model.tournamentrelated.Tournament;
import cs203.ftms.overall.model.userrelated.Fencer;
import cs203.ftms.overall.model.userrelated.User;
import cs203.ftms.overall.service.fencer.FencerService;
import cs203.ftms.overall.service.event.EventService;
import jakarta.validation.Valid;



@RestController
@CrossOrigin
@RequestMapping("/api/v1/fencer")
public class FencerController {
    private final FencerService fencerService;
    private final EventService eventService;

    @Autowired
    public FencerController(FencerService fencerService, EventService eventService) {
        this.fencerService = fencerService;
        this.eventService = eventService;
    }

    @GetMapping("/profile")
    @PreAuthorize("hasRole('FENCER')")
    public ResponseEntity<CleanFencerDTO> getProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        CleanFencerDTO cf = fencerService.getCleanFencerDTO((Fencer) user);
        if (cf == null) return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(cf, HttpStatus.OK);
    }
    
    @PutMapping("/complete-profile")
    @PreAuthorize("hasRole('FENCER')")
    public ResponseEntity<String> completeProfile(@Valid @RequestBody CompleteFencerProfileDTO dto) throws MethodArgumentNotValidException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Fencer f = fencerService.completeProfile((Fencer) user, dto);
        if (f != null) {
            return new ResponseEntity<>("fencer profile completed successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("fencer profile completion unsuccessful", HttpStatus.BAD_REQUEST);
    }
    
    @GetMapping("/international-ranking")
    @PreAuthorize("hasAnyRole('FENCER', 'ORGANISER', 'ADMIN')")
    public ResponseEntity<List<CleanFencerDTO>> getInternationalRanking() {
        List<Fencer> fencers = fencerService.getInternationalRank(); 
        List<CleanFencerDTO> res = new ArrayList<>();
        for (Fencer fencer : fencers) {
            res.add(fencerService.getCleanFencerDTO(fencer));
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/events")
    @PreAuthorize("hasRole('FENCER')")
    public ResponseEntity<List<CleanEventDTO>> getAllFencerEvents() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        List<Event> eList = fencerService.getFencerEvents((Fencer) user);
        
        List<CleanEventDTO> ctList = new ArrayList<>();
        for (Event e : eList) {
            ctList.add(eventService.getCleanEventDTO(e));
        }
        return new ResponseEntity<>(ctList, HttpStatus.OK);
    }

    @GetMapping("/upcoming-events")
    @PreAuthorize("hasRole('FENCER')")
    public ResponseEntity<List<CleanEventDTO>> getFencerUpcomingEvents() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        List<Event> eList = fencerService.getFencerEvents((Fencer) user);
        
        List<CleanEventDTO> ctList = new ArrayList<>();
        for (Event e : eList) {
            if(e.getDate().isAfter(LocalDate.now())){
                ctList.add(eventService.getCleanEventDTO(e));
            }
        }
        return new ResponseEntity<>(ctList, HttpStatus.OK);
    }

    @GetMapping("/past-events")
    @PreAuthorize("hasRole('FENCER')")
    public ResponseEntity<List<CleanEventDTO>> getFencerPastEvents() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        List<Event> eList = fencerService.getFencerEvents((Fencer) user);
        
        List<CleanEventDTO> ctList = new ArrayList<>();
        for (Event e : eList) {
            if(e.getDate().isBefore(LocalDate.now())){
                ctList.add(eventService.getCleanEventDTO(e));
            }
        }
        return new ResponseEntity<>(ctList, HttpStatus.OK);
    }

    @GetMapping("/men-sabre-ranking")
    @PreAuthorize("hasAnyRole('FENCER', 'ORGANISER', 'ADMIN')")
    public ResponseEntity<List<CleanFencerDTO>> getMenSabreRanking() {
        List<Fencer> fencers = fencerService.getInternationalRank(); 
        List<CleanFencerDTO> res = new ArrayList<>();
        for (Fencer fencer : fencers) {
            if(fencer.getGender() == 'M' && fencer.getWeapon() == 'S'){
                res.add(fencerService.getCleanFencerDTO(fencer));
            }
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/women-sabre-ranking")
    @PreAuthorize("hasAnyRole('FENCER', 'ORGANISER', 'ADMIN')")
    public ResponseEntity<List<CleanFencerDTO>> getWomenSabreRanking() {
        List<Fencer> fencers = fencerService.getInternationalRank(); 
        List<CleanFencerDTO> res = new ArrayList<>();
        for (Fencer fencer : fencers) {
            if(fencer.getGender() == 'W' && fencer.getWeapon() == 'S'){
                res.add(fencerService.getCleanFencerDTO(fencer));
            }
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/men-epee-ranking")
    @PreAuthorize("hasAnyRole('FENCER', 'ORGANISER', 'ADMIN')")
    public ResponseEntity<List<CleanFencerDTO>> getMenEpeeRanking() {
        List<Fencer> fencers = fencerService.getInternationalRank(); 
        List<CleanFencerDTO> res = new ArrayList<>();
        for (Fencer fencer : fencers) {
            if(fencer.getGender() == 'M' && fencer.getWeapon() == 'E'){
                res.add(fencerService.getCleanFencerDTO(fencer));
            }
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/women-epee-ranking")
    @PreAuthorize("hasAnyRole('FENCER', 'ORGANISER', 'ADMIN')")
    public ResponseEntity<List<CleanFencerDTO>> getWomenEpeeRanking() {
        List<Fencer> fencers = fencerService.getInternationalRank(); 
        List<CleanFencerDTO> res = new ArrayList<>();
        for (Fencer fencer : fencers) {
            if(fencer.getGender() == 'W' && fencer.getWeapon() == 'E'){
                res.add(fencerService.getCleanFencerDTO(fencer));
            }
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/men-foil-ranking")
    @PreAuthorize("hasAnyRole('FENCER', 'ORGANISER', 'ADMIN')")
    public ResponseEntity<List<CleanFencerDTO>> getMenFoilRanking() {
        List<Fencer> fencers = fencerService.getInternationalRank(); 
        List<CleanFencerDTO> res = new ArrayList<>();
        for (Fencer fencer : fencers) {
            if(fencer.getGender() == 'M' && fencer.getWeapon() == 'F'){
                res.add(fencerService.getCleanFencerDTO(fencer));
            }
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/women-foil-ranking")
    @PreAuthorize("hasAnyRole('FENCER', 'ORGANISER', 'ADMIN')")
    public ResponseEntity<List<CleanFencerDTO>> getWomenFoilRanking() {
        List<Fencer> fencers = fencerService.getInternationalRank(); 
        List<CleanFencerDTO> res = new ArrayList<>();
        for (Fencer fencer : fencers) {
            if(fencer.getGender() == 'W' && fencer.getWeapon() == 'F'){
                res.add(fencerService.getCleanFencerDTO(fencer));
            }
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

}