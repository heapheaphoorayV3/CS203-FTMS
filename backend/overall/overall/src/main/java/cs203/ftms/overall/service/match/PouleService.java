package cs203.ftms.overall.service.match;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import cs203.ftms.overall.comparator.TournamentFencerComparator;
import cs203.ftms.overall.comparator.TournamentFencerPouleComparator;
import cs203.ftms.overall.dto.CreatePoulesDTO;
import cs203.ftms.overall.dto.PouleResultsDTO;
import cs203.ftms.overall.dto.PouleTableDTO;
import cs203.ftms.overall.dto.SinglePouleTableDTO;
import cs203.ftms.overall.dto.clean.CleanMatchDTO;
import cs203.ftms.overall.dto.clean.CleanPouleDTO;
import cs203.ftms.overall.dto.clean.CleanTournamentFencerDTO;
import cs203.ftms.overall.exception.EntityDoesNotExistException;
import cs203.ftms.overall.exception.SignUpDateNotOverException;
import cs203.ftms.overall.model.tournamentrelated.Event;
import cs203.ftms.overall.model.tournamentrelated.Poule;
import cs203.ftms.overall.model.tournamentrelated.PouleMatch;
import cs203.ftms.overall.model.tournamentrelated.TournamentFencer;
import cs203.ftms.overall.model.userrelated.Organiser;
import cs203.ftms.overall.repository.tournamentrelated.EventRepository;
import cs203.ftms.overall.repository.tournamentrelated.MatchRepository;
import cs203.ftms.overall.repository.tournamentrelated.PouleRepository;
import cs203.ftms.overall.repository.tournamentrelated.TournamentFencerRepository;
import cs203.ftms.overall.service.event.EventService;
import cs203.ftms.overall.validation.OtherValidations;
import jakarta.transaction.Transactional;

/**
 * Service class for managing poule-related operations.
 * Provides methods for creating, updating, and retrieving poules.
 */
@Service
public class PouleService {
    private final MatchService matchService;
    private final EventService eventService;
    private final EventRepository eventRepository;
    private final PouleRepository pouleRepository;
    private final TournamentFencerRepository tournamentFencerRepository;
    private final MatchRepository matchRepository;

    // constants for recommendPoules
    private static final int MIN_FENCERS_PER_POULE = 5;
    private static final int MAX_FENCERS_PER_POULE = 15;

    @Autowired
    public PouleService(MatchService matchService, EventService eventService, EventRepository eventRepository,
            PouleRepository pouleRepository, TournamentFencerRepository tournamentFencerRepository,
            MatchRepository matchRepository) {
        this.matchService = matchService;
        this.eventService = eventService;
        this.eventRepository = eventRepository;
        this.pouleRepository = pouleRepository;
        this.tournamentFencerRepository = tournamentFencerRepository;
        this.matchRepository = matchRepository;
    }
    
    /**
     * Converts a Poule object into a CleanPouleDTO containing relevant data.
     *
     * @param p the Poule to convert
     * @return a CleanPouleDTO object with the necessary details
     */
    public CleanPouleDTO getCleanPouleDTO(Poule p) {
        List<CleanMatchDTO> cleanMatches = convertPouleMatches(p);
        List<CleanTournamentFencerDTO> cleanFencers = convertTournamentFencers(p);
        String eventName = buildEventName(p);

        return new CleanPouleDTO(
                p.getId(),
                p.getPouleNumber(),
                eventName,
                p.getEvent().getId(),
                cleanMatches,
                cleanFencers);
    }

    // Helper method to convert a poule into a list of matches
    private List<CleanMatchDTO> convertPouleMatches(Poule p) {
        return p.getPouleMatches().stream()
                .map(pm -> matchService.getCleanMatchDTO(pm, 'P'))
                .collect(Collectors.toList());
    }

    // Helper method to convert a poule into a list of tournament fencers
    private List<CleanTournamentFencerDTO> convertTournamentFencers(Poule p) {
        return p.getFencers().stream()
                .map(eventService::getCleanTournamentFencerDTO)
                .collect(Collectors.toList());
    }

    // Helper method to build the event name for a poule
    private String buildEventName(Poule p) {
        StringBuilder eventName = new StringBuilder();

        switch (p.getEvent().getGender()) {
            case 'W' -> eventName.append("Women - ");
            case 'M' -> eventName.append("Men - ");
            default -> eventName.append("Invalid - ");
        }

        switch (p.getEvent().getWeapon()) {
            case 'E' -> eventName.append("Epee");
            case 'F' -> eventName.append("Foil");
            case 'S' -> eventName.append("Sabre");
            default -> eventName.append("Invalid");
        }

        return eventName.toString();
    }

    /**
     * Provides recommendations for the number of poules based on the event's participant count.
     *
     * @param eventId the ID of the event
     * @return a set of recommendations for poules with fencer distribution
     */
    public Set<String> recommendPoules(int eventId) {
        Event event = eventService.getEvent(eventId);

        int fencerCount = event.getParticipantCount();

        if (fencerCount <= 0) {
            return new HashSet<>();
        }

        int maxPoules = fencerCount / MIN_FENCERS_PER_POULE;
        int minPoules = (fencerCount % MAX_FENCERS_PER_POULE == 0) ? fencerCount / MAX_FENCERS_PER_POULE
                : fencerCount / MAX_FENCERS_PER_POULE + 1;

        Set<String> pouleRecommendations = new LinkedHashSet<>();
        for (int numPoules = minPoules; numPoules <= maxPoules; numPoules++) {
            pouleRecommendations.add(formatPouleRecommendation(fencerCount, numPoules));
        }

        return pouleRecommendations;
    }

    // Helper method to format the poule recommendation
    private String formatPouleRecommendation(int fencerCount, int numPoules) {
        int baseFencersPerPoule = fencerCount / numPoules;
        int extraPoules = fencerCount % numPoules;

        if (extraPoules == 0) {
            return String.format("%d Poules: %d poules of %d fencers", numPoules, numPoules, baseFencersPerPoule);
        } else {
            return String.format(
                    "%d Poules: %d poules of %d fencers and %d poule(s) of %d fencers",
                    numPoules,
                    numPoules - extraPoules,
                    baseFencersPerPoule,
                    extraPoules,
                    baseFencersPerPoule + 1);
        }
    }

    /**
     * Creates poules for an event with the specified number of poules in the DTO.
     *
     * @param eid the ID of the event
     * @param dto the DTO containing the number of poules to create
     * @param o   the organiser initiating the request
     * @return a set of CleanPouleDTOs representing the created poules
     */
    @Transactional
    public Set<CleanPouleDTO> createPoules(int eid, CreatePoulesDTO dto, Organiser o) {
        Event event = eventService.getEvent(eid);

        if (event.getTournament().getSignupEndDate().isAfter(LocalDate.now())) {
            throw new SignUpDateNotOverException("Sign up date is not over!");
        }

        eventService.validateOrganiser(event, o);
        if (poulesAlreadyExist(event)) {
            return getExistingPoules(event);
        }

        Poule[] poules = createAndSavePoules(event, dto.getPouleCount());
        List<TournamentFencer> sortedFencers = getSortedFencers(event);

        assignFencersToPoules(poules, sortedFencers);
        savePoulesAndEvent(event, poules);

        return getCleanPoules(poules);
    }

    // Helper method to check if poules already exist for an event
    private boolean poulesAlreadyExist(Event event) {
        return event.getPoules().size() != 0;
    }

    // Helper method to get existing poules for an event
    private Set<CleanPouleDTO> getExistingPoules(Event event) {
        Set<CleanPouleDTO> cleanPoules = new LinkedHashSet<>();
        for (Poule poule : event.getPoules()) {
            cleanPoules.add(getCleanPouleDTO(poule));
        }
        return cleanPoules;
    }

    // Helper method to create and save poules for an event
    private Poule[] createAndSavePoules(Event event, int pouleCount) {
        Poule[] poules = new Poule[pouleCount];
        for (int i = 0; i < pouleCount; i++) {
            Poule poule = new Poule(i + 1, event);
            poules[i] = poule;
            pouleRepository.save(poule);
        }
        return poules;
    }

    // Helper method to get sorted fencers for an event
    private List<TournamentFencer> getSortedFencers(Event event) {
        Set<TournamentFencer> fencers = event.getFencers();
        List<TournamentFencer> sortedFencers = new ArrayList<>(fencers);
        Collections.sort(sortedFencers, new TournamentFencerComparator());
        return sortedFencers;
    }

    // Helper method to assign fencers to poules
    private void assignFencersToPoules(Poule[] poules, List<TournamentFencer> fencers) {
        int pouleCount = poules.length;
        int i = 0;
        for (TournamentFencer fencer : fencers) {
            Poule poule = poules[i % pouleCount];
            poule.getFencers().add(fencer);
            pouleRepository.save(poule);
            fencer.setPoule(poule);
            tournamentFencerRepository.save(fencer);
            i++;
        }
    }

    // Helper method to save poules and event
    private void savePoulesAndEvent(Event event, Poule[] poules) {
        Set<Poule> pouleSet = new TreeSet<>(Arrays.asList(poules));
        event.setPoules(pouleSet);
        eventRepository.save(event);
    }

    // Helper method to get clean poules from an array of poules
    private Set<CleanPouleDTO> getCleanPoules(Poule[] poules) {
        Set<CleanPouleDTO> cleanPoules = new LinkedHashSet<>();
        for (Poule poule : poules) {
            cleanPoules.add(getCleanPouleDTO(poule));
        }
        return cleanPoules;
    }

   /**
     * Retrieves all poules for an event and converts them into CleanPouleDTOs.
     *
     * @param eid the ID of the event
     * @return a set of CleanPouleDTOs for the event
     */
    public Set<CleanPouleDTO> getPoulesOfEvent(int eid) {
        Event event = eventService.getEvent(eid);
        Set<Poule> poules = event.getPoules();
        Set<CleanPouleDTO> cleanPoules = new HashSet<>();
        for (Poule poule : poules) {
            cleanPoules.add(getCleanPouleDTO(poule));
        }
        return cleanPoules;
    }

    /**
     * Creates or retrieves a table for poules within an event.
     *
     * @param eid      the ID of the event
     * @param createPM whether to create poule matches during table generation
     * @return a PouleTableDTO representing the table for all poules
     */
    public PouleTableDTO getPouleTable(int eid, boolean createPM) {
        Event event = eventService.getEvent(eid);
        List<Poule> poules = getPoulesByEvent(event);
        PouleTableDTO pouleTableDTO = new PouleTableDTO();

        for (Poule poule : poules) {
            Map<String, String> pouleMap = createPouleMap(poule, createPM);
            pouleTableDTO.addPouleTable(pouleMap);
        }

        return pouleTableDTO;
    }

    // Helper method to get poules by event
    private List<Poule> getPoulesByEvent(Event event) {
        List<Poule> poules = pouleRepository.findByEvent(event);
        poules.sort(Comparator.comparing(Poule::getPouleNumber));
        return poules;
    }

    // Helper method to create a map for a poule
    private Map<String, String> createPouleMap(Poule poule, boolean createPM) {
        List<TournamentFencer> fencers = getSortedFencers(poule);
        Map<String, String> pouleMap = new LinkedHashMap<>();

        for (int i = 0; i < fencers.size(); i++) {
            TournamentFencer tf1 = fencers.get(i);
            String key = createPouleKey(tf1);
            String value = createPouleValue(fencers, tf1, i, createPM, poule);
            pouleMap.put(key, value);
        }

        return pouleMap;
    }

    // Helper method to get sorted fencers for a poule
    private List<TournamentFencer> getSortedFencers(Poule poule) {
        List<TournamentFencer> fencers = new ArrayList<>(poule.getFencers());
        fencers.sort(Comparator.comparing((TournamentFencer tf) -> tf.getFencer().getPoints()).reversed());
        return fencers;
    }

    // Helper method to create a key for the poule map
    private String createPouleKey(TournamentFencer tf1) {
        return String.format("%s (%s) -- %d", tf1.getFencer().getName(), tf1.getFencer().getCountry(), tf1.getId());
    }

    // Helper method to create a value for the poule map
    private String createPouleValue(List<TournamentFencer> fencers, TournamentFencer tf1, int i, boolean createPM,
            Poule poule) {
        StringBuilder value = new StringBuilder();

        for (int j = 0; j < fencers.size(); j++) {
            if (i == j) {
                value.append("-1,");
            } else {
                TournamentFencer tf2 = fencers.get(j);
                if (createPM) {
                    handleCreatePouleMatch(tf1, tf2, poule, i, j, value);
                } else {
                    handleExistingPouleMatch(tf1, tf2, poule, i, j, value);
                }
            }
        }

        if (value.length() > 0) {
            value.setLength(value.length() - 1);
        }

        return value.toString();
    }

    // Helper method to create a poule match
    private void handleCreatePouleMatch(TournamentFencer tf1, TournamentFencer tf2, Poule poule, int i, int j,
            StringBuilder value) {
        if (i < j) {
            createPouleMatch(tf1.getId(), tf2.getId(), poule.getId());
        }
        value.append("0,");
    }

    // Helper method to get existing poule matches
    private void handleExistingPouleMatch(TournamentFencer tf1, TournamentFencer tf2, Poule poule, int i, int j,
            StringBuilder value) {
        Set<PouleMatch> pouleMatches = poule.getPouleMatches();

        for (PouleMatch pouleMatch : pouleMatches) {
            TournamentFencer ptf1 = matchService.getFencer1(pouleMatch);
            TournamentFencer ptf2 = matchService.getFencer2(pouleMatch);

            if (i < j) {
                appendPouleMatchScore(tf1, tf2, value, pouleMatch, ptf1, ptf2);
            } else {
                appendPouleMatchScore(tf2, tf1, value, pouleMatch, ptf2, ptf1);
            }
        }
    }

    // Helper method to append poule match scores
    private void appendPouleMatchScore(TournamentFencer tf1, TournamentFencer tf2, StringBuilder value,
            PouleMatch pouleMatch, TournamentFencer ptf1, TournamentFencer ptf2) {
        if (ptf1.getId() == tf1.getId() && ptf2.getId() == tf2.getId()) {
            value.append(String.format("%d,", pouleMatch.getScore1()));
        } else if (ptf1.getId() == tf2.getId() && ptf2.getId() == tf1.getId()) {
            value.append(String.format("%d,", pouleMatch.getScore2()));
        }
    }

    /**
     * Creates a PouleMatch between two fencers in a specific poule.
     *
     * @param fid1 the ID of the first fencer
     * @param fid2 the ID of the second fencer
     * @param pid  the ID of the poule
     * @return the created PouleMatch object
     */
    @Transactional
    public PouleMatch createPouleMatch(int fid1, int fid2, int pid) {
        Poule poule = getPouleById(pid);
        TournamentFencer fencer1 = getFencerById(fid1);
        TournamentFencer fencer2 = getFencerById(fid2);

        PouleMatch pouleMatch = createAndSavePouleMatch(poule, fid1, fid2);
        updatePouleWithMatch(poule, pouleMatch);
        updateFencersWithMatch(fencer1, fencer2, pouleMatch);

        return pouleMatch;
    }

    // Helper method to get a poule by ID
    private Poule getPouleById(int pid) {
        return pouleRepository.findById(pid)
                .orElseThrow(() -> new EntityDoesNotExistException("Poule does not exist!"));
    }

    // Helper method to get a fencer by ID
    private TournamentFencer getFencerById(int fid) {
        return tournamentFencerRepository.findById(fid)
                .orElseThrow(() -> new EntityDoesNotExistException("Fencer does not exist!"));
    }

    // Helper method to create and save a poule match
    private PouleMatch createAndSavePouleMatch(Poule poule, int fid1, int fid2) {
        PouleMatch pouleMatch = new PouleMatch(poule);
        pouleMatch.setFencer1(fid1);
        pouleMatch.setFencer2(fid2);
        matchRepository.save(pouleMatch);
        return pouleMatch;
    }

    // Helper method to update a poule in a match
    private void updatePouleWithMatch(Poule poule, PouleMatch pouleMatch) {
        Set<PouleMatch> pouleMatches = poule.getPouleMatches();
        pouleMatches.add(pouleMatch);
        poule.setPouleMatches(pouleMatches);
        pouleRepository.save(poule);
    }

    // Helper method to update fencers in a match
    private void updateFencersWithMatch(TournamentFencer fencer1, TournamentFencer fencer2, PouleMatch pouleMatch) {
        fencer1.addMatch(pouleMatch);
        fencer2.addMatch(pouleMatch);
        tournamentFencerRepository.save(fencer1);
        tournamentFencerRepository.save(fencer2);
    }

    /**
     * Updates the results of a poule table based on the provided data.
     *
     * @param eid the ID of the event
     * @param dto the DTO containing updated poule table data
     * @param o   the organiser initiating the request
     * @return true if the update was successful, false otherwise
     * @throws MethodArgumentNotValidException if validation of the provided data fails
     */
    @Transactional
    public boolean updatePouleTable(int eid, SinglePouleTableDTO dto, Organiser o)
            throws MethodArgumentNotValidException {
        Event event = eventService.getEvent(eid);
        eventService.validateOrganiser(event, o);
        Poule poule = getPouleByEventAndNumber(event, dto.getPouleNumber());
        Map<String, String> newPouleTable = dto.getSingleTable();
        List<TournamentFencer> fencers = getSortedFencers(poule);

        for (TournamentFencer tf : poule.getFencers()) {
            tf.setPoulePoints(0);
            tf.setPouleWins(0);
        }

        for (int i = 0; i < newPouleTable.size(); i++) {
            TournamentFencer tf1 = fencers.get(i);
            String key = createPouleKey(tf1);
            String[] values = newPouleTable.get(key).split(",");
            updatePouleMatches(poule, fencers, tf1, values, i);
        }

        updateAllPouleMatches(poule);
        return true;
    }

    // Helper method to get a poule by event and number
    private Poule getPouleByEventAndNumber(Event event, int pouleNumber) {
        return pouleRepository.findByEventAndPouleNumber(event, pouleNumber).get(0);
    }

    // Helper method to update poule matches
    private void updatePouleMatches(Poule poule, List<TournamentFencer> fencers, TournamentFencer tf1, String[] values,
            int i) throws MethodArgumentNotValidException {
        for (int j = 0; j < fencers.size(); j++) {
            TournamentFencer tf2 = fencers.get(j);
            if (i != j) {
                updatePouleMatchScores(poule, tf1, tf2, values[j], i, j);
            }
        }
    }

    // Helper method to update poule match scores
    private void updatePouleMatchScores(Poule poule, TournamentFencer tf1, TournamentFencer tf2, String value, int i,
            int j) throws MethodArgumentNotValidException {
        Set<PouleMatch> pouleMatches = poule.getPouleMatches();

        for (PouleMatch pouleMatch : pouleMatches) {
            TournamentFencer ptf1 = matchService.getFencer1(pouleMatch);
            TournamentFencer ptf2 = matchService.getFencer2(pouleMatch);

            if (i < j) {
                if (ptf1.getId() == tf1.getId() && ptf2.getId() == tf2.getId()) {
                    savePouleMatchScore(value, pouleMatch, 1);
                    break;
                }
            } else {
                if (ptf1.getId() == tf2.getId() && ptf2.getId() == tf1.getId()) {
                    savePouleMatchScore(value, pouleMatch, 2);
                    break;
                }
            }
        }
    }

    // Helper method to update and save poule match score
    private void savePouleMatchScore(String scoreStr, PouleMatch pouleMatch, int index)
            throws MethodArgumentNotValidException {
        int score = OtherValidations.validPoulePoint(scoreStr);
        if (index == 1) {
            pouleMatch.setScore1(score);
        } else if (index == 2) {
            pouleMatch.setScore2(score);
        }
        matchRepository.save(pouleMatch);
    }

    // Helper method to update all poule matches
    private void updateAllPouleMatches(Poule poule) {

        for (PouleMatch pouleMatch : poule.getPouleMatches()) {
            updateTournamentFencerPoulePoints(pouleMatch);
            matchRepository.save(pouleMatch);
        }
    }

    // Helper method to update tournament fencer poule points
    public PouleMatch updateTournamentFencerPoulePoints(PouleMatch pouleMatch) {
        TournamentFencer fencer1 = matchService.getFencer1(pouleMatch);
        TournamentFencer fencer2 = matchService.getFencer2(pouleMatch);

        fencer1.setPoulePoints(fencer1.getPoulePoints() + pouleMatch.getScore1());
        fencer2.setPoulePoints(fencer2.getPoulePoints() + pouleMatch.getScore2());

        if (pouleMatch.getScore1() > pouleMatch.getScore2()) {
            fencer1.setPouleWins(fencer1.getPouleWins() + 1);
            pouleMatch.setWinner(fencer1.getId());
        } else if (pouleMatch.getScore1() < pouleMatch.getScore2()) {
            fencer2.setPouleWins(fencer2.getPouleWins() + 1);
            pouleMatch.setWinner(fencer2.getId());
        } else {
            pouleMatch.setWinner(0);
        }
        tournamentFencerRepository.save(fencer1);
        tournamentFencerRepository.save(fencer2);
        return pouleMatch;
    }

    /**
     * Retrieves fencers classified into groups ("Bypass", "FenceOff", "Eliminated") after the poules.
     *
     * @param event the event to retrieve fencers from
     * @return a mapping of fencer groups based on their classification
     */
    public Map<String, List<TournamentFencer>> getFencersAfterPoules(Event event) {
        Map<String, List<TournamentFencer>> mappings = new HashMap<>();
        int advancementRate = event.getTournament().getAdvancementRate();
        int fencersAdvanced = calculateFencersAdvanced(event, advancementRate);
        int nearestPO2 = nearestLowerPowerOf2(fencersAdvanced);
        int bypass = calculateBypass(fencersAdvanced, nearestPO2);
        if (fencersAdvanced == nearestPO2) {
            bypass = 0;
        }

        List<TournamentFencer> sortedFencers = getSortedFencersByEvent(event);
        updateTournamentFencerRanks(sortedFencers);

        mappings.put("Bypass", sortedFencers.subList(0, bypass));
        mappings.put("FenceOff", sortedFencers.subList(bypass, fencersAdvanced));
        mappings.put("Eliminated", sortedFencers.subList(fencersAdvanced, sortedFencers.size()));

        return mappings;
    }

    // Helper method to calculate number of fencers to advance
    private int calculateFencersAdvanced(Event event, int advancementRate) {
        return (int) (event.getParticipantCount() * (advancementRate / 100.0));
    }

    // Helper method to calculate number of fencers to bypass
    private int calculateBypass(int fencersAdvanced, int nearestPO2) {
        if (fencersAdvanced != nearestPO2) {
            return fencersAdvanced - 2 * (fencersAdvanced - nearestPO2);
        }
        return fencersAdvanced;
    }

    // Helper method to get sorted fencers by event
    private List<TournamentFencer> getSortedFencersByEvent(Event event) {
        List<TournamentFencer> fencers = tournamentFencerRepository.findByEvent(event);
        fencers.sort(new TournamentFencerPouleComparator());
        return fencers;
    }

    // Helper method to get the nearest lower power of 2
    private static int nearestLowerPowerOf2(int a) {
        int b = 1;
        while (b < a) {
            b = b << 1;
        }
        return b > a ? b >> 1 : b;
    }


    @Transactional
    public void updateTournamentFencerRanks(List<TournamentFencer> tfs) {
        for (int i = 1; i <= tfs.size(); i++) {
            TournamentFencer tf = tfs.get(i - 1);
            tf.setTournamentRank(i);
            tournamentFencerRepository.save(tf);
        }
    }

    /**
     * Retrieves the results of poules for a given event.
     *
     * @param eid the ID of the event
     * @return a PouleResultsDTO containing fencer classifications and rankings
     */
    public PouleResultsDTO poulesResult(int eid) {
        Event event = eventService.getEvent(eid);
        PouleResultsDTO dto = new PouleResultsDTO();

        Map<String, List<TournamentFencer>> mappings = getFencersAfterPoules(event);
        List<TournamentFencer> sortedFencers = getSortedFencers(mappings);

        // updateTournamentFencerRanks(sortedFencers);
        populatePouleResultsDTO(dto, mappings);

        return dto;
    }

    // Helper method to get sorted fencers for poulesResult
    private List<TournamentFencer> getSortedFencers(Map<String, List<TournamentFencer>> mappings) {
        List<TournamentFencer> sortedFencers = new ArrayList<>();
        sortedFencers.addAll(mappings.get("Bypass"));
        sortedFencers.addAll(mappings.get("FenceOff"));
        sortedFencers.addAll(mappings.get("Eliminated"));
        Collections.sort(sortedFencers, new TournamentFencerPouleComparator());
        return sortedFencers;
    }

    // Helper method to populate a pouleResultsDTO
    private void populatePouleResultsDTO(PouleResultsDTO dto, Map<String, List<TournamentFencer>> mappings) {
        for (String type : mappings.keySet()) {
            List<TournamentFencer> fencers = mappings.get(type);
            List<CleanTournamentFencerDTO> cleanFencers = fencers.stream()
                    .map(eventService::getCleanTournamentFencerDTO)
                    .collect(Collectors.toList());

            switch (type) {
                case "Bypass":
                    dto.setBypassFencers(cleanFencers);
                    break;
                case "FenceOff":
                    dto.setFenceOffFencers(cleanFencers);
                    break;
                case "Eliminated":
                    dto.setEliminatedFencers(cleanFencers);
                    break;
            }
        }
    }
}
