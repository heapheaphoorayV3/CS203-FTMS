package cs203.ftms.overall.service.event;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import cs203.ftms.overall.comparator.TournamentFencerComparator;
import cs203.ftms.overall.comparator.TournamentFencerPouleComparator;
import cs203.ftms.overall.datastructure.CustomMatchHeap;
import cs203.ftms.overall.dto.CreateEventDTO;
import cs203.ftms.overall.dto.CreatePoulesDTO;
import cs203.ftms.overall.dto.DirectEliminationBracketDTO;
import cs203.ftms.overall.dto.DirectEliminationBracketFencerDTO;
import cs203.ftms.overall.dto.PouleResultsDTO;
import cs203.ftms.overall.dto.PouleTableDTO;
import cs203.ftms.overall.dto.SinglePouleTableDTO;
import cs203.ftms.overall.dto.UpdateDirectEliminationMatchDTO;
import cs203.ftms.overall.dto.clean.CleanEventDTO;
import cs203.ftms.overall.dto.clean.CleanFencerDTO;
import cs203.ftms.overall.dto.clean.CleanMatchDTO;
import cs203.ftms.overall.dto.clean.CleanPouleDTO;
import cs203.ftms.overall.dto.clean.CleanTournamentFencerDTO;
import cs203.ftms.overall.exception.EntityDoesNotExistException;
import cs203.ftms.overall.exception.EventAlreadyExistsException;
import cs203.ftms.overall.exception.EventCannotEndException;
import cs203.ftms.overall.exception.SignUpDateOverExcpetion;
import cs203.ftms.overall.model.tournamentrelated.DirectEliminationMatch;
import cs203.ftms.overall.model.tournamentrelated.Event;
import cs203.ftms.overall.model.tournamentrelated.Match;
import cs203.ftms.overall.model.tournamentrelated.Poule;
import cs203.ftms.overall.model.tournamentrelated.PouleMatch;
import cs203.ftms.overall.model.tournamentrelated.Tournament;
import cs203.ftms.overall.model.tournamentrelated.TournamentFencer;
import cs203.ftms.overall.model.userrelated.Fencer;
import cs203.ftms.overall.model.userrelated.Organiser;
import cs203.ftms.overall.repository.tournamentrelated.DirectEliminationMatchRepository;
import cs203.ftms.overall.repository.tournamentrelated.EventRepository;
import cs203.ftms.overall.repository.tournamentrelated.MatchRepository;
import cs203.ftms.overall.repository.tournamentrelated.PouleRepository;
import cs203.ftms.overall.repository.tournamentrelated.TournamentFencerRepository;
import cs203.ftms.overall.repository.tournamentrelated.TournamentRepository;
import cs203.ftms.overall.repository.userrelated.UserRepository;
import cs203.ftms.overall.service.fencer.FencerService;
import cs203.ftms.overall.validation.OtherValidations;
import jakarta.transaction.Transactional;


@Service
public class EventService {
    private final TournamentRepository tournamentRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final FencerService fencerService;
    private final MatchRepository matchRepository;
    private final TournamentFencerRepository tournamentFencerRepository;
    private final PouleRepository pouleRepository;
    private final DirectEliminationMatchRepository directEliminationMatchRepository; 

    @Autowired
    public EventService(TournamentRepository tournamentRepository, EventRepository eventRepository, UserRepository userRepository, 
    FencerService fencerService, MatchRepository matchRepository, TournamentFencerRepository tournamentFencerRepository, 
    PouleRepository pouleRepository, DirectEliminationMatchRepository directEliminationMatchRepository) {
        this.tournamentRepository = tournamentRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.fencerService = fencerService; 
        this.matchRepository = matchRepository;
        this.tournamentFencerRepository = tournamentFencerRepository;
        this.pouleRepository = pouleRepository;
        this.directEliminationMatchRepository = directEliminationMatchRepository; 
    }

    public CleanEventDTO getCleanEventDTO(Event e) {
        if (e == null) return null;

        List<CleanFencerDTO> cleanFencers = new ArrayList<>(); 
        for (TournamentFencer f : e.getFencers()) {
            cleanFencers.add(fencerService.getCleanFencerDTO(f.getFencer()));
        }

        return new CleanEventDTO(e.getId(), e.getGender(), e.getWeapon(), e.getTournament().getName(), cleanFencers, e.getMinParticipants(), e.getParticipantCount(), e.getDate(), e.getStartTime(), e.getEndTime());
    } 

    public CleanTournamentFencerDTO getCleanTournamentFencerDTO(TournamentFencer tf) {
        return new CleanTournamentFencerDTO(tf.getId(), tf.getFencer().getId(), tf.getFencer().getName(), tf.getFencer().getClub(), tf.getFencer().getCountry(), tf.getFencer().getDominantArm(), tf.getTournamentRank(), tf.getEvent().getId(), tf.getPouleWins(), tf.getPoulePoints());
    }

    public CleanMatchDTO getCleanMatchDTO(Match m, char matchType) {
        List<TournamentFencer> fencerList = getFencersInMatch(m);
        if (fencerList.size() == 0) {
            return new CleanMatchDTO(m.getId(), null, m.getScore1(), null, m.getScore2(), m.getWinner(), matchType);
        }
        CleanTournamentFencerDTO ctf1 = getCleanTournamentFencerDTO(fencerList.get(0));
        if (fencerList.size() == 1) {
            return new CleanMatchDTO(m.getId(), ctf1, m.getScore1(), null, m.getScore2(), m.getWinner(), matchType);
        }
        CleanTournamentFencerDTO ctf2 = getCleanTournamentFencerDTO(fencerList.get(1));
        return new CleanMatchDTO(m.getId(), ctf1, m.getScore1(), ctf2, m.getScore2(), m.getWinner(), matchType);
    }

    public CleanPouleDTO getCleanPouleDTO(Poule p) {
        List<CleanMatchDTO> cleanMatches = new ArrayList<>(); 
        for (PouleMatch pm : p.getPouleMatches()) {
            cleanMatches.add(getCleanMatchDTO(pm, 'P'));
        }

        List<CleanTournamentFencerDTO> cleanFencers = new ArrayList<>(); 
        for (TournamentFencer tf : p.getFencers()) {
            cleanFencers.add(getCleanTournamentFencerDTO(tf));
        }
        StringBuilder eventName = new StringBuilder();
        switch(p.getEvent().getGender()) {
            case 'W' -> eventName.append("Women - ");
            case 'M' -> eventName.append("Men - ");
            default -> eventName.append("Invalid - ");
        }

        switch (p.getEvent().getWeapon()){
            case 'E' -> eventName.append("Epee");
            case 'F' -> eventName.append("Foil");
            case 'S' -> eventName.append("Sabre");
            default -> eventName.append("Invalid");
        };
        return new CleanPouleDTO(p.getId(), p.getPouleNumber(), eventName.toString(), p.getEvent().getId(), cleanMatches, cleanFencers);
    }

    public List<TournamentFencer> getFencersInMatch(Match m) {
        List<TournamentFencer> fencerList = new ArrayList<>(2); 
        if (m.getFencer1() != -1) {
            TournamentFencer tf1 = tournamentFencerRepository.findById(m.getFencer1()).orElseThrow(() -> new EntityDoesNotExistException("Tournament Fencer does not exist!"));
            fencerList.add(tf1);
        } 
        if (m.getFencer2() != -1) {
            TournamentFencer tf2 = tournamentFencerRepository.findById(m.getFencer2()).orElseThrow(() -> new EntityDoesNotExistException("Tournament Fencer does not exist!"));
            fencerList.add(tf2);
        }
        return fencerList;
    }

    public Event getEvent(int id) {
        return eventRepository.findById(id).orElse(null);
    }

    @Transactional
    public List<Event> createEvent(int tid, Organiser o, List<CreateEventDTO> dto) throws MethodArgumentNotValidException, EventAlreadyExistsException {

        Tournament tournament = tournamentRepository.findById(tid).orElse(null);
        if (tournament == null) {
            return null;
        }    
        
        if (!tournament.getOrganiser().equals(o)) {
            return null;
        }

        List<Event> events = new ArrayList<>();
        for (CreateEventDTO e : dto) {
            if (eventRepository.findByTournamentAndGenderAndWeapon(tournament, e.getGender(), e.getWeapon()).orElse(null) != null) {
                throw new EventAlreadyExistsException(); 
            } 
            Event event = new Event(tournament, e.getGender(), e.getWeapon(), e.getMinParticipants(), e.getDate(), e.getStartTime(), e.getEndTime());
            OtherValidations.validEventDate(event, tournament);
            eventRepository.save(event);
            events.add(event);
        }
        return events;
    }

    // fencer register for event
    @Transactional
    public boolean registerEvent(int eid, Fencer f) {
        Event event = eventRepository.findById(eid).orElseThrow(() -> new EntityDoesNotExistException("Event does not exist!"));

        if (event.getTournament().getSignupEndDate().isBefore(LocalDate.now())) {
            throw new SignUpDateOverExcpetion("Sign up date is over!");
        }

        // handle relevant relationships
        TournamentFencer tf = new TournamentFencer(f, event);
        
        Set<TournamentFencer> fencers = event.getFencers(); 
        fencers.add(tf);
        event.setFencers(fencers);
        event.setParticipantCount(event.getParticipantCount()+1);

        Set<TournamentFencer> fencerTFs = f.getTournamentFencerProfiles();
        fencerTFs.add(tf);
        f.setTournamentFencerProfiles(fencerTFs);
        
        Fencer nf = userRepository.save(f);

        if (nf != null) {
            Event tc = eventRepository.save(event);
            if (tc != null) {
                return true;
            }
        }
        return false; 
    }

    public Set<String> recommendPoules(int eid) {
        Event e = eventRepository.findById(eid).orElseThrow(() -> new EntityDoesNotExistException("Event does not exist!"));

        int fencerCount = e.getParticipantCount();
        // min fencerCount in 1 poule = 5 
        // max fencerCount in 1 poule = 15 
        int maxPoules = fencerCount / 5; 
        int minPoules = (fencerCount % 15 == 0) ? (fencerCount / 15) : (fencerCount/15 + 1);
        Set<String> poules = new LinkedHashSet<>();
        for (int i = minPoules; i <= maxPoules; i++) {
            int base = fencerCount / i; 
            int remainder = fencerCount % i; 
            if (remainder == 0) {
                poules.add(String.format("%d Poules: %d poules of %d fencers", i, i, base));
            }  else {
                poules.add(String.format("%d Poules: %d pouples of %d fencers and %d poule of %d fencers", i, i - remainder, base, remainder, base + 1));
            }
        }
        return poules;
    }

    public Set<CleanPouleDTO> createPoules(int eid, CreatePoulesDTO dto) {
        Event e = eventRepository.findById(eid).orElseThrow(() -> new EntityDoesNotExistException("Event does not exist!"));

        // if poules already exist, return the existing poules
        if (e.getPoules().size() != 0) {
            Set<CleanPouleDTO> cleanPoules = new LinkedHashSet<>();
            for (Poule p : e.getPoules()) {
                cleanPoules.add(getCleanPouleDTO(p));
            }
            return cleanPoules;
        }

        int poulesCount = dto.getPouleCount();
        Poule[] poules = new Poule[poulesCount];
        for (int i = 0; i < poulesCount; i++) {
            Poule p = new Poule(i + 1, e);
            poules[i] = p;
            pouleRepository.save(p);
        }

        Set<TournamentFencer> t = e.getFencers();
        List<TournamentFencer> fencers = new ArrayList<>(t);
        Collections.sort(fencers, new TournamentFencerComparator());
        

        int i = 0;
        for (TournamentFencer tf : fencers) {
            Set<TournamentFencer> tfs = poules[i % poulesCount].getFencers();
            tfs.add(tf);
            poules[i % poulesCount].setFencers(tfs);
            pouleRepository.save(poules[i % poulesCount]);
            tf.setPoule(poules[i % poulesCount]);
            tournamentFencerRepository.save(tf);
            i++;
        }
        
        
        Set<Poule> poulesSet = new TreeSet<>();
        Collections.addAll(poulesSet, poules);
        e.setPoules(poulesSet);
        eventRepository.save(e); 
        Set<CleanPouleDTO> cleanPoules = new LinkedHashSet<>();
        for (Poule p : poules) {
            cleanPoules.add(getCleanPouleDTO(p));
        }
        return cleanPoules;
    }

    public Set<CleanPouleDTO> getPoulesOfEvent(int eid) {
        Event e = eventRepository.findById(eid).orElseThrow(() -> new EntityDoesNotExistException("Event does not exist!"));
        Set<Poule> poules = e.getPoules(); 
        Set<CleanPouleDTO> cleanPoules = new HashSet<>();
        for (Poule p : poules) {
            cleanPoules.add(getCleanPouleDTO(p));
        }
        return cleanPoules;
    }

    public PouleTableDTO getPouleTable(int eid, boolean createPM) {
        Event e = eventRepository.findById(eid).orElseThrow(() -> new EntityDoesNotExistException("Event does not exist!"));
        PouleTableDTO dto = new PouleTableDTO();
        List<Poule> poules = pouleRepository.findByEvent(e);
        poules.sort(Comparator.comparing(Poule::getPouleNumber));
        for (Poule poule : poules) {
            List<TournamentFencer> fencers = new ArrayList<>(poule.getFencers());
            fencers.sort(Comparator.comparing(TournamentFencer::getId));

            Map<String,String> pouleMap = new LinkedHashMap<>(); 
            for (int i = 0; i < fencers.size(); i++) {
                TournamentFencer tf1 = fencers.get(i);
                String key = String.format("%s (%s) -- %d", tf1.getFencer().getName(), tf1.getFencer().getCountry(), tf1.getId());
                StringBuilder value = new StringBuilder();
                for (int j = 0; j < fencers.size(); j++) {
                    if (i==j) {
                        value.append("-1,");
                    } else {
                        TournamentFencer tf2 = fencers.get(j);
                        if (createPM) {
                            if (i < j) {
                                createPouleMatch(tf1.getId(), tf2.getId(), poule.getId());
                            }
                            value.append("0,");
                        } else {
                            Set<PouleMatch> pMatchs = poule.getPouleMatches();
                            for (PouleMatch pMatch : pMatchs) {
                                Iterator<TournamentFencer> tfIter = getFencersInMatch(pMatch).iterator();
                                TournamentFencer ptf1 = tfIter.next();
                                TournamentFencer ptf2 = tfIter.next();
                                if (i<j) {
                                    if (ptf1.getId() == tf1.getId() && ptf2.getId() == tf2.getId()) {
                                        value.append(String.format("%d,", pMatch.getScore1()));
                                        break;
                                    } else if (ptf1.getId() == tf2.getId() && ptf2.getId() == tf1.getId()) {
                                        value.append(String.format("%d,", pMatch.getScore2()));
                                        break;
                                    }
                                } else {
                                    if (ptf1.getId() == tf1.getId() && ptf2.getId() == tf2.getId()) {
                                        value.append(String.format("%d,", pMatch.getScore1()));
                                        break;
                                    } else if (ptf1.getId() == tf2.getId() && ptf2.getId() == tf1.getId()) {
                                        value.append(String.format("%d,", pMatch.getScore2()));
                                        break;
                                    }
                                }
                            }
                        }   
                        
                    }
                    
                }
                if (value.length() > 0) {
                    value.setLength(value.length() - 1);
                }
                
                pouleMap.put(key, value.toString());
            }
            dto.addPouleTable(pouleMap);
        }
        
        return dto;
    }

    @Transactional
    public PouleMatch createPouleMatch(int fid1, int fid2, int pid){
        Poule poule = pouleRepository.findById(pid).orElseThrow(() -> new EntityDoesNotExistException("Poule does not exist!"));
        TournamentFencer fencer1 = tournamentFencerRepository.findById(fid1).orElseThrow(() -> new EntityDoesNotExistException("Fencer does not exist!"));
        TournamentFencer fencer2 = tournamentFencerRepository.findById(fid2).orElseThrow(() -> new EntityDoesNotExistException("Fencer does not exist!"));

        PouleMatch pouleMatch = new PouleMatch(poule);
        pouleMatch.setFencer1(fid1);
        pouleMatch.setFencer2(fid2);
        matchRepository.save(pouleMatch);

        Set<PouleMatch> pms = poule.getPouleMatches();
        pms.add(pouleMatch);
        poule.setPouleMatches(pms);
        pouleRepository.save(poule);

        fencer1.addMatch(pouleMatch);
        fencer2.addMatch(pouleMatch);
        tournamentFencerRepository.save(fencer1);
        tournamentFencerRepository.save(fencer2);
        return pouleMatch;
    }

    @Transactional
    public boolean updatePouleTable(int eid, SinglePouleTableDTO dto) throws MethodArgumentNotValidException {
        Event event = eventRepository.findById(eid).orElseThrow(() -> new EntityDoesNotExistException("Event does not exist!"));
        Poule poule = pouleRepository.findByEventAndPouleNumber(event, dto.getPouleNumber()).get(0); 
        Map<String, String> newPouleTable = dto.getSingleTable(); 
        List<TournamentFencer> fencers = new ArrayList<>(poule.getFencers());
        fencers.sort(Comparator.comparing(TournamentFencer::getId));
        for (int i = 0; i < newPouleTable.size(); i++) {
            TournamentFencer tf1 = fencers.get(i);
            String key = String.format("%s (%s) -- %d", tf1.getFencer().getName(), tf1.getFencer().getCountry(), tf1.getId());
            // System.out.println(key);
            String[] values = newPouleTable.get(key).split(",");
            for (int j = 0; j < newPouleTable.size(); j++) {
                TournamentFencer tf2 = fencers.get(j);
                Set<PouleMatch> pMatchs = poule.getPouleMatches();
                for (PouleMatch pMatch : pMatchs) {
                    Iterator<TournamentFencer> tfIter = getFencersInMatch(pMatch).iterator();
                    TournamentFencer ptf1 = tfIter.next();
                    TournamentFencer ptf2 = tfIter.next();
                    if (i!=j) {
                        if (i<j) {
                            if (ptf1.getId() == tf1.getId() && ptf2.getId() == tf2.getId()) {
                                int score = OtherValidations.validPoulePoint(values[j]);
                                pMatch.setScore1(score);
                                matchRepository.save(pMatch);
                                break;
                            } else if (ptf1.getId() == tf2.getId() && ptf2.getId() == tf1.getId()) {
                                int score = OtherValidations.validPoulePoint(values[j]);
                                pMatch.setScore2(score);
                                matchRepository.save(pMatch);
                                break;
                            }
                        } else {
                            if (ptf1.getId() == tf1.getId() && ptf2.getId() == tf2.getId()) {
                                int score = OtherValidations.validPoulePoint(values[j]);
                                pMatch.setScore1(score);
                                matchRepository.save(pMatch);
                                break;
                            } else if (ptf1.getId() == tf2.getId() && ptf2.getId() == tf1.getId()) {
                                int score = OtherValidations.validPoulePoint(values[j]);
                                pMatch.setScore2(score);
                                matchRepository.save(pMatch);
                                break;
                            }
                        }
                        
                    }
                }
            }
            
            
        }
        for (PouleMatch pMatch : poule.getPouleMatches()) {
            System.out.println("match id = "+pMatch.getId());
            List<TournamentFencer> fs = new ArrayList<>(getFencersInMatch(pMatch));
            System.out.println(fs.get(0).getFencer().getName() + " - " + pMatch.getScore1());
            System.out.println(fs.get(1).getFencer().getName() + " - " + pMatch.getScore2());
            System.out.println();
            updateTournamentFencerPoulePoints(pMatch);
            matchRepository.save(pMatch);
        }
        return true; 
    }

    public PouleMatch updateTournamentFencerPoulePoints(PouleMatch pm){
        Iterator<TournamentFencer> iter = getFencersInMatch(pm).iterator();
        TournamentFencer fencer1 = iter.next();
        TournamentFencer fencer2 = iter.next();


        fencer1.setPoulePoints(fencer1.getPoulePoints() + pm.getScore1());
        fencer2.setPoulePoints(fencer2.getPoulePoints() + pm.getScore2());

        if(pm.getScore1() > pm.getScore2()) {
            fencer1.setPouleWins(fencer1.getPouleWins() + 1);
            pm.setWinner(fencer1.getId()); 
        }else{
            fencer2.setPouleWins(fencer2.getPouleWins() + 1);
            pm.setWinner(fencer2.getId()); 
        }

        tournamentFencerRepository.save(fencer1);
        tournamentFencerRepository.save(fencer2);
        return pm;
        
    }

    public Map<String, List<TournamentFencer>> getFencersAfterPoules(Event event) {
        Map<String, List<TournamentFencer>> mappings = new HashMap<>();
        int advancementRate = event.getTournament().getAdvancementRate(); 
        int fencersAdvanced = (int) (event.getParticipantCount() * (advancementRate/100.0)); 
        int nearestPO2 = nearestLowerPowerOf2(fencersAdvanced); 
        int bypass = fencersAdvanced;
        List<TournamentFencer> tfs = tournamentFencerRepository.findByEvent(event);
        Collections.sort(tfs, new TournamentFencerPouleComparator());
        if (fencersAdvanced != nearestPO2) {
            bypass = fencersAdvanced - 2 * (fencersAdvanced - nearestPO2);
        }

        mappings.put("Bypass", tfs.subList(0, bypass));
        mappings.put("FenceOff", tfs.subList(bypass, fencersAdvanced));
        mappings.put("Eliminated", tfs.subList(fencersAdvanced, tfs.size()));
        return mappings;
    }

    @Transactional
    public void updateTournamentFencerRanks(List<TournamentFencer> tfs) {
        for (int i = 1; i <= tfs.size(); i++) {
            TournamentFencer tf = tfs.get(i-1); 
            tf.setTournamentRank(i);
            tournamentFencerRepository.save(tf);
        }
    }


    public PouleResultsDTO poulesResult(int eid) {
        Event event = eventRepository.findById(eid).orElseThrow(() -> new EntityDoesNotExistException("Event does not exist!"));
        PouleResultsDTO dto = new PouleResultsDTO(); 

        Map<String, List<TournamentFencer>> mappings = getFencersAfterPoules(event);
        
        List<TournamentFencer> tfList = new ArrayList<>();
        tfList.addAll(mappings.get("Bypass"));
        tfList.addAll(mappings.get("FenceOff"));
        tfList.addAll(mappings.get("Eliminated"));
        Collections.sort(tfList, new TournamentFencerPouleComparator());
        updateTournamentFencerRanks(tfList);

        for (String type : mappings.keySet()) {
            List<TournamentFencer> tfs = mappings.get(type);
            List<CleanTournamentFencerDTO> ctfs = new ArrayList<>(); 
            for (TournamentFencer tf : tfs) {
                ctfs.add(getCleanTournamentFencerDTO(tf));
            }
            if (type.equals("Bypass")) {
                dto.setBypassFencers(ctfs);
            } else if (type.equals("FenceOff")) {
                dto.setFenceOffFencers(ctfs);
            } else if (type.equals("Eliminated")) {
                dto.setEliminatedFencers(ctfs);
            }
        }
        return dto;
    }

    private static int nearestLowerPowerOf2(int a) {
        int b = 1;
        while (b < a) {
            b = b << 1;
        }
        return (b > a ? b >> 1 : b);
    }

    public int noOfDEMatches(Event event) {
        int advancementRate = event.getTournament().getAdvancementRate(); 
        int fencersAdvanced = (int) (event.getParticipantCount() * (advancementRate/100.0)); 
        int nearestPO2 = nearestLowerPowerOf2(fencersAdvanced); 

        int totalMatchCount = 0; 
        while (nearestPO2 > 0) {
            totalMatchCount += nearestPO2; 
            nearestPO2 /= 2; 
        }

        return totalMatchCount; 
    }

    @Transactional
    public void createAllDEMatches(int eid){
        Event event = eventRepository.findById(eid).orElseThrow(() -> new EntityDoesNotExistException("Event does not exist!"));
        int matchCount = noOfDEMatches(event);
        CustomMatchHeap heap = new CustomMatchHeap();
        for(int i = 0; i < matchCount; i++){
            Match m = heap.insert(new DirectEliminationMatch(event));
            matchRepository.save(m);
        }

        Map<String, List<TournamentFencer>> mappings = getFencersAfterPoules(event);
        int bypassSize = mappings.get("Bypass").size();

        List<TournamentFencer> fencers = new ArrayList<>();
        fencers.addAll(mappings.get("Bypass"));
        fencers.addAll(mappings.get("FenceOff"));
        Collections.sort(fencers, new TournamentFencerPouleComparator());
        
        populateInitialDEMatches(fencers, heap, bypassSize);
        System.out.println();
        heap.printHeap();
        int lastLevel = (int) (Math.log(heap.size()+1) / Math.log(2)) -1;
        System.out.println(lastLevel);
        List<Match> matches = heap.getLevel(lastLevel);
        System.out.println();
        for (Match m : matches) {
            System.out.println(m.getId());
        }
    }

    public int[] previousRound(int[] currentRound, int totalFencers){
        int size = currentRound.length * 2;
        int[] result = new int[size];
        for(int i = 0; i < size; i+=2){
            result[i] = currentRound[i/2];
            if(size - currentRound[i/2] < totalFencers){
                result[i+1] = size - currentRound[i/2] + 1;
            }else{
                result[i+1] = 0;
            }
        }
        return result;
    }
    
    @Transactional
    public void populateInitialDEMatches(List<TournamentFencer> tfencers, CustomMatchHeap heap, int bypassSize){
        int lastLevel = (int) (Math.log(heap.size()+1) / Math.log(2)) -1;
        List<Match> matches = heap.getLevel(lastLevel);
        int[] matchArray = new int[]{1, 2};
        for (int i = 0; i < lastLevel; i++) {
            matchArray = previousRound(matchArray, tfencers.size());
        }
        
        int matchCount = 0;
        for (int i = 0; i < matchArray.length; i+=2) {
            DirectEliminationMatch dm = (DirectEliminationMatch) matches.get(matchCount);
            TournamentFencer tf1 = tfencers.get(matchArray[i] - 1);
            dm.setFencer1(tf1.getId());
            tf1.addMatch(dm);
            if (matchArray[i+1] != 0) {
                TournamentFencer tf2 = tfencers.get(matchArray[i+1] - 1);
                dm.setFencer2(tf2.getId());
                tf2.addMatch(dm);
                tournamentFencerRepository.save(tf2);
            } else {
                int nextMatchId = dm.getNextMatchId();
                DirectEliminationMatch nextMatch = (DirectEliminationMatch) matchRepository.findById(nextMatchId).orElseThrow(() -> new EntityDoesNotExistException("Match does not exist!"));
                tf1.addMatch(nextMatch);
                if(nextMatch.getFencer1() == -1){
                    nextMatch.setFencer1(tf1.getId());
                }else{
                    nextMatch.setFencer2(tf1.getId());
                }
                matchRepository.save(nextMatch);
            }
            tournamentFencerRepository.save(tf1);
            matchRepository.save(dm);
            System.out.println("populate fencer size after saving = " + getFencersInMatch(matchRepository.findById(dm.getId()).orElse(null)).size());
            matchCount++;
        }
    }
    @Transactional
    public void updateDEMatch(int eid, UpdateDirectEliminationMatchDTO dto){
        Event event = eventRepository.findById(eid).orElseThrow(() -> new EntityDoesNotExistException("Event does not exist!"));
        Match match = matchRepository.findById(dto.getMatchId()).orElseThrow(() -> new EntityDoesNotExistException("Match does not exist!"));
        if (match.getEvent().getId() != eid) {
            throw new EntityDoesNotExistException("Match does not exist in this event!");
        }

        Set<DirectEliminationMatch> dmsAll = event.getDirectEliminationMatches(); 
        for (DirectEliminationMatch dmm : dmsAll) {
            System.out.println("DM " + dmm.getId() + " fencer size = " + getFencersInMatch(dmm).size());
        }

        DirectEliminationMatch dm = (DirectEliminationMatch) match;
        dm.setScore1(dto.getScore1());
        dm.setScore2(dto.getScore2());

        System.out.println("Match id = " + dm.getId());

        System.out.println("size of fencers = " + getFencersInMatch(dm).size());
        Iterator<TournamentFencer> iter = getFencersInMatch(dm).iterator();
        TournamentFencer fencer1 = iter.next();
        System.out.println("Fencer1 =" + fencer1.getId());

        TournamentFencer fencer2 = iter.next();
        System.out.println("Fencer2 =" + fencer2.getId());

        if (dto.getScore1() > dto.getScore2()) {
            dm.setWinner(fencer1.getId());
            if(fencer1.getTournamentRank() > fencer2.getTournamentRank()){
                int temp = fencer1.getTournamentRank();
                fencer1.setTournamentRank(fencer2.getTournamentRank());
                fencer2.setTournamentRank(temp);
            }
            
        } else {
            dm.setWinner(fencer2.getId());
            if(fencer1.getTournamentRank() < fencer2.getTournamentRank()){
                int temp = fencer1.getTournamentRank();
                fencer1.setTournamentRank(fencer2.getTournamentRank());
                fencer2.setTournamentRank(temp);
            }
        }
        matchRepository.save(dm);   
        
        TournamentFencer winner = tournamentFencerRepository.findById(dm.getWinner()).orElseThrow(() -> new EntityDoesNotExistException("Tournament Fencer does not exist!"));
        if(dm.getRoundOf() != 2){
            DirectEliminationMatch nextMatch = (DirectEliminationMatch) matchRepository.findById(dm.getNextMatchId()).orElseThrow(() -> new EntityDoesNotExistException("Match does not exist!"));
            if(nextMatch.getFencer1() == -1 || nextMatch.getFencer1() == fencer1.getId() || nextMatch.getFencer1() == fencer2.getId()){
                nextMatch.setFencer1(winner.getId());
            }else if(nextMatch.getFencer2() == -1 || nextMatch.getFencer2() == fencer1.getId() || nextMatch.getFencer2() == fencer2.getId()){
                nextMatch.setFencer2(winner.getId());
            }
            matchRepository.save(nextMatch);
            winner.addMatch(nextMatch);
        }

        tournamentFencerRepository.save(winner);
    }
    
    public DirectEliminationBracketDTO getDirectEliminationBracketDTO(DirectEliminationMatch m) {
        String roundText = null; 
        if (m.getRoundOf() == 2) {
            roundText = "Finals";
        } else if (m.getRoundOf() == 4) {
            roundText = "Semi-Finals";
        } else if (m.getRoundOf() == 8) {
            roundText = "Quarter-Finals";
        } else {
            roundText = String.format("Top %d", m.getRoundOf());
        }
        DirectEliminationBracketFencerDTO[] fencersDTO = new DirectEliminationBracketFencerDTO[getFencersInMatch(m).size()];
        int fencerCount = 0;
        for (TournamentFencer tf : getFencersInMatch(m)) {
            int score = 0;
            if (fencerCount == 0) {
                score = m.getScore1();
            } else {
                score = m.getScore2(); 
            }
            fencersDTO[fencerCount] = getDirectEliminationBracketFencerDTO(tf, m.getWinner(), score);
            fencerCount++;
        }
        return new DirectEliminationBracketDTO(m.getId(), roundText, m.getNextMatchId(), roundText, null, null, fencersDTO);
    }

    public DirectEliminationBracketFencerDTO getDirectEliminationBracketFencerDTO(TournamentFencer tf, int winner, int score) {
        boolean isWinner = false;
        if (winner == tf.getId()) {
            isWinner = true;
        }
        return new DirectEliminationBracketFencerDTO(tf.getId(), String.format("%d", score), isWinner, null, tf.getFencer().getName());
    }

    public List<DirectEliminationBracketDTO> generateDirectEliminationBracketDTOs(int eid) {
        Event event = eventRepository.findById(eid).orElseThrow(() -> new EntityDoesNotExistException("Event does not exist!"));
        List<DirectEliminationMatch> matches = directEliminationMatchRepository.findByEvent(event);
        List<DirectEliminationBracketDTO> bracketDTOs = new ArrayList<>(); 
        for (DirectEliminationMatch m : matches) {
            bracketDTOs.add(getDirectEliminationBracketDTO(m));
        }
        return bracketDTOs; 
    }

    public List<TournamentFencer> getTournamentRanks(int eid) {
        Event event = eventRepository.findById(eid).orElseThrow(() -> new EntityDoesNotExistException("Event does not exist!"));
        List<TournamentFencer> tfs = new ArrayList<>(event.getFencers());
        Collections.sort(tfs, new TournamentFencerComparator());
        return tfs;
    }

    public void endTournamentEvent(int eid) throws EventCannotEndException {
        Event event = eventRepository.findById(eid).orElseThrow(() -> new EntityDoesNotExistException("Event does not exist!"));
        if (event.isOver()) {
            throw new EventCannotEndException("Event has already been ended!");
        }
        List<DirectEliminationMatch> matches = directEliminationMatchRepository.findByEventAndRoundOf(event, 2);
        if (matches.get(0).getWinner() == -1) {
            throw new EventCannotEndException("Final match has not been completed!");
        } 
        updateInternationalRank(event);
        event.setOver(true);
    }

    @Transactional
    public void updateInternationalRank(Event event) {
        List<TournamentFencer> tfs = getTournamentRanks(event.getId());
        double numOfFencersThatGetPoints = tfs.size() * 0.8;
        int totalPoints = getPointsForDistribution(event.getFencers());
        for(int i = 0; i < (int) numOfFencersThatGetPoints; i++){
            int points = calculatePoints(i, totalPoints, (int) numOfFencersThatGetPoints);
            Fencer fencer = tfs.get(i).getFencer();
            fencer.setPoints(points + tfs.get(i).getFencer().getPoints());
            userRepository.save(fencer);
        }
    }

    public int getPointsForDistribution(Set<TournamentFencer> tfencers) {
        int total = 0;
        for(TournamentFencer f :tfencers){
            total += f.getFencer().getPoints();
        }
        return total / 5;
    }

    public int calculatePoints(int rank, int totalPoints, int totalFencers){
        double numerator = totalPoints * Math.pow(totalFencers - rank + 1, 2);
        double denominator = sumOfPowers(totalFencers, 2);
        return (int) (numerator / denominator);
    }

    private static double sumOfPowers(int n, int exponent) {
        double sum = 0;
        for (int i = 1; i <= n; i++) {
            sum += Math.pow(i, exponent);
        }
        return sum;
    }
}
