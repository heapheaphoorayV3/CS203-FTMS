package cs203.ftms.overall.service.event;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import cs203.ftms.overall.dto.CreateEventDTO;
import cs203.ftms.overall.dto.CreatePoulesDTO;
import cs203.ftms.overall.dto.PouleResultsDTO;
import cs203.ftms.overall.dto.PouleTableDTO;
import cs203.ftms.overall.dto.SinglePouleTableDTO;
import cs203.ftms.overall.dto.UpdatePouleMatchScoreDTO;
import cs203.ftms.overall.dto.clean.CleanEventDTO;
import cs203.ftms.overall.dto.clean.CleanFencerDTO;
import cs203.ftms.overall.dto.clean.CleanMatchDTO;
import cs203.ftms.overall.dto.clean.CleanPouleDTO;
import cs203.ftms.overall.dto.clean.CleanTournamentFencerDTO;
import cs203.ftms.overall.exception.EntityDoesNotExistException;
import cs203.ftms.overall.exception.EventAlreadyExistsException;
import cs203.ftms.overall.exception.SignUpDateOverExcpetion;
import cs203.ftms.overall.model.tournamentrelated.Event;
import cs203.ftms.overall.model.tournamentrelated.Match;
import cs203.ftms.overall.model.tournamentrelated.Poule;
import cs203.ftms.overall.model.tournamentrelated.PouleMatch;
import cs203.ftms.overall.model.tournamentrelated.Tournament;
import cs203.ftms.overall.model.tournamentrelated.TournamentFencer;
import cs203.ftms.overall.model.userrelated.Fencer;
import cs203.ftms.overall.model.userrelated.Organiser;
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

    @Autowired
    public EventService(TournamentRepository tournamentRepository, EventRepository eventRepository, 
    UserRepository userRepository, FencerService fencerService, MatchRepository matchRepository, TournamentFencerRepository tournamentFencerRepository, PouleRepository pouleRepository) {
        this.tournamentRepository = tournamentRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.fencerService = fencerService; 
        this.matchRepository = matchRepository;
        this.tournamentFencerRepository = tournamentFencerRepository;
        this.pouleRepository = pouleRepository;
    }

    public CleanEventDTO getCleanEventDTO(Event e) {
        if (e == null) return null;

        Set<CleanFencerDTO> cleanFencers = new HashSet<>(); 
        for (TournamentFencer f : e.getFencers()) {
            cleanFencers.add(fencerService.getCleanFencerDTO(f.getFencer()));
        }

        return new CleanEventDTO(e.getId(), e.getGender(), e.getWeapon(), e.getTournament().getName(), cleanFencers, e.getMinParticipants(), e.getParticipantCount(), e.getDate(), e.getStartTime(), e.getEndTime());
    } 

    public CleanTournamentFencerDTO getCleanTournamentFencerDTO(TournamentFencer tf) {
        return new CleanTournamentFencerDTO(tf.getId(), tf.getFencer().getId(), tf.getFencer().getName(), tf.getFencer().getClub(), tf.getFencer().getCountry(), tf.getFencer().getDominantArm(), tf.getTournamentRank(), tf.getEvent().getId());
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
        Set<CleanMatchDTO> cleanMatches = new LinkedHashSet<>(); 
        for (PouleMatch pm : p.getPouleMatches()) {
            cleanMatches.add(getCleanMatchDTO(pm, 'P'));
        }

        Set<CleanTournamentFencerDTO> cleanFencers = new LinkedHashSet<>(); 
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
        CleanPouleDTO dto = new CleanPouleDTO(p.getId(), p.getPouleNumber(), eventName.toString(), p.getEvent().getId(), cleanMatches, cleanFencers);
        return dto;
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

    @Transactional
    public boolean registerEvent(int eid, Fencer f) {
        Event event = eventRepository.findById(eid).orElseThrow(() -> new EntityDoesNotExistException("Event does not exist!"));

        List<TournamentFencer> tfs = tournamentFencerRepository.findAll(); 
        for (TournamentFencer tf : tfs) {
            System.out.println(tf);
        }
        if (event.getTournament().getSignupEndDate().isBefore(LocalDate.now())) {
            throw new SignUpDateOverExcpetion("Sign up date is over!");
        }
        // System.out.println("fencer = "+ f);
        TournamentFencer tf = new TournamentFencer(f, event);
        
        Set<TournamentFencer> fencers = event.getFencers(); 
        fencers.add(tf);
        event.setFencers(fencers);
        event.setParticipantCount(event.getParticipantCount()+1);

        //??? 
        // Set<Event> ftc = f.getEventsPart(); 
        // ftc.add(event);
        // f.setEventsPart(ftc);

        Set<TournamentFencer> fencerTFs = f.getTournamentFencerProfiles();
        fencerTFs.add(tf);
        f.setTournamentFencerProfiles(fencerTFs);
        
        Fencer nf = userRepository.save(f);

        if (nf != null) {
            Event tc = eventRepository.save(event);
            // tournamentFencerRepository.save(tf);
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
        // List<Fencer> fencers = new ArrayList<>(e.getFencers());
        // int fencerCount = fencers.size();
        Event e = eventRepository.findById(eid).orElseThrow(() -> new EntityDoesNotExistException("Event does not exist!"));
        // if (e.getTournament().getSignupEndDate().isAfter(LocalDate.now())) throw new SignUpDateNotOverException();

        if (e.getPoules().size() != 0) {
            Set<CleanPouleDTO> cleanPoules = new LinkedHashSet<>();
            for (Poule p : e.getPoules()) {
                cleanPoules.add(getCleanPouleDTO(p));
            }
            return cleanPoules;
        }

        // for (Poule p : e.getPoules()) {
        //     for (PouleMatch pm : p.getPouleMatches()) {
        //         Iterator<TournamentFencer> fencers = pm.getFencers().iterator(); 
        //         while (fencers.hasNext()) {
        //             TournamentFencer tf = fencers.next();
        //             tf.removeMatch(pm);
        //             System.out.println("fencer pm delete");
        //             tournamentFencerRepository.save(tf);
        //         }
        //         System.out.println("poule match delete");
        //         matchRepository.delete(pm);
        //     }
        //     System.out.println("poule delete");
        //     pouleRepository.delete(p);
        // }

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
            System.out.println("fencer " + tf.getFencer().getName() + " points = " + tf.getFencer().getPoints());
            Set<TournamentFencer> tfs = poules[i % poulesCount].getFencers();
            // System.out.println(tfs.size());
            tfs.add(tf);
            poules[i % poulesCount].setFencers(tfs);
            pouleRepository.save(poules[i % poulesCount]);
            tf.setPoule(poules[i % poulesCount]);
            tournamentFencerRepository.save(tf);
            i++;
        }

        // for (int k = 0; k < poulesCount; k++) {
        //     pouleRepository.save(poules[k]);
        // }
        

        Set<Poule> poulesSet = new TreeSet<>();
        Collections.addAll(poulesSet, poules);
        e.setPoules(poulesSet);
        // for (int i = 0; i < fencers.size(); i++) {
        //     int p = i % poulesCount;
        //     Set<Fencer> f = poules[p].getFencers();
        //     f.add(fencers.get(i));
        //     // poules[p].set;
        // }
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
                                        value.append(String.format("%d,", pMatch.getScore1()));
                                        break;
                                    }
                                } else {
                                    if (ptf1.getId() == tf1.getId() && ptf2.getId() == tf2.getId()) {
                                        value.append(String.format("%d,", pMatch.getScore2()));
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
    public boolean updatePouleTable(int eid, SinglePouleTableDTO dto) {
        Event event = eventRepository.findById(eid).orElseThrow(() -> new EntityDoesNotExistException("Event does not exist!"));
        Poule poule = pouleRepository.findByEventAndPouleNumber(event, dto.getPouleNumber()).get(0); 
        Map<String, String> newPouleTable = dto.getSingleTable(); 
        List<TournamentFencer> fencers = new ArrayList<>(poule.getFencers());
        fencers.sort(Comparator.comparing(TournamentFencer::getId));
        for (int i = 0; i < newPouleTable.size(); i++) {
            TournamentFencer tf1 = fencers.get(i);
            String key = String.format("%s (%s) -- %d", tf1.getFencer().getName(), tf1.getFencer().getCountry(), tf1.getId());
            System.out.println(key);
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
                                // value.append(String.format("%d,", pMatch.getScore1()));
                                pMatch.setScore1(Integer.parseInt(values[j]));
                                break;
                            } else if (ptf1.getId() == tf2.getId() && ptf2.getId() == tf1.getId()) {
                                // value.append(String.format("%d,", pMatch.getScore1()));
                                pMatch.setScore1(Integer.parseInt(values[j]));
                                break;
                            }
                        } else {
                            if (ptf1.getId() == tf1.getId() && ptf2.getId() == tf2.getId()) {
                                // value.append(String.format("%d,", pMatch.getScore2()));
                                pMatch.setScore2(Integer.parseInt(values[j]));
                                break;
                            } else if (ptf1.getId() == tf2.getId() && ptf2.getId() == tf1.getId()) {
                                // value.append(String.format("%d,", pMatch.getScore2()));
                                pMatch.setScore2(Integer.parseInt(values[j]));
                                break;
                            }
                        }
                        matchRepository.save(pMatch);
                    }
                }
            }
        
        }
        return true; 

    }

    @Transactional
    public PouleMatch createPouleMatch(int fid1, int fid2, int pid){
        Poule poule = pouleRepository.findById(pid).orElseThrow(() -> new EntityDoesNotExistException("Poule does not exist!"));
        TournamentFencer fencer1 = tournamentFencerRepository.findById(fid1).orElseThrow(() -> new EntityDoesNotExistException("Fencer does not exist!"));
        TournamentFencer fencer2 = tournamentFencerRepository.findById(fid2).orElseThrow(() -> new EntityDoesNotExistException("Fencer does not exist!"));

        Set<TournamentFencer> matchFencers = new LinkedHashSet<>();
        matchFencers.add(fencer1);
        matchFencers.add(fencer2);
        PouleMatch pouleMatch = new PouleMatch(poule);
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

    public boolean updatePouleScore(Set<UpdatePouleMatchScoreDTO> dto) {
        for (UpdatePouleMatchScoreDTO d : dto) {
            Match m = matchRepository.findById(d.getMatchId()).orElse(null);
            if (m == null) return false;
            m.setScore1(d.getFencer1Score());
            m.setScore2(d.getFencer2Score());
            matchRepository.save(m);
        }
        return true;
    }
    
    public Set<TournamentFencer> updateTournamentFencerPoints(int eid, int pid) {
        Event event = eventRepository.findById(eid).orElseThrow(() -> new EntityDoesNotExistException("Event does not exist!"));
        List<Poule> poules = pouleRepository.findByEventAndPouleNumber(event, pid);
        if (poules.size() == 0 || poules.size() > 1) return null; 
        Set<TournamentFencer> tfs = new LinkedHashSet<>(); 
        for (PouleMatch pm : poules.get(0).getPouleMatches()) {
            updateTournamentFencerPoulePoints(pm);
            Iterator<TournamentFencer> iter = getFencersInMatch(pm).iterator();
            tfs.add(iter.next());
            tfs.add(iter.next());
        }
        return tfs;
        
    }

    public void updateTournamentFencerPoulePoints(PouleMatch pm){
        Iterator<TournamentFencer> iter = getFencersInMatch(pm).iterator();
        TournamentFencer fencer1 = iter.next();
        TournamentFencer fencer2 = iter.next();

        fencer1.setPoulePoints(fencer1.getPoulePoints() + pm.getScore1());
        fencer2.setPoulePoints(fencer2.getPoulePoints() + pm.getScore2());

        if (pm.getScore1() > pm.getScore2()) {
            fencer1.setPouleWins(fencer1.getPouleWins() + 1);
            pm.setWinner(fencer1.getId()); 
        } else{
            fencer2.setPouleWins(fencer2.getPouleWins() + 1);
            pm.setWinner(fencer2.getId()); 
        }
    }

    public PouleResultsDTO poulesResult(int eid) {
        Event event = eventRepository.findById(eid).orElseThrow(() -> new EntityDoesNotExistException("Event does not exist!"));
        PouleResultsDTO dto = new PouleResultsDTO(); 
        int advancementRate = event.getTournament().getAdvancementRate(); 
        int fencersAdvanced = event.getParticipantCount() * (advancementRate/100); 
        int nearestPO2 = nextPowerOf2(fencersAdvanced); 
        int bypass = fencersAdvanced;
        List<TournamentFencer> tfs = tournamentFencerRepository.findByEvent(event);
        List<CleanTournamentFencerDTO> ctfs = new ArrayList<>(); 
        for (TournamentFencer tf : tfs) {
            ctfs.add(getCleanTournamentFencerDTO(tf));
        }
        if (fencersAdvanced != nearestPO2) {
            if (nearestPO2 > event.getParticipantCount()) {
                fencersAdvanced = event.getParticipantCount();
                bypass = nearestPO2/2;
            } else {
                fencersAdvanced += (nearestPO2 - fencersAdvanced); 
                bypass = nearestPO2/2;
            }
        }
        dto.setBypassFencers(ctfs.subList(0, bypass));
        dto.setFenceOffFencers(ctfs.subList(bypass, fencersAdvanced));
        dto.setEliminatedFencers(ctfs.subList(fencersAdvanced, ctfs.size()));
        return dto;
    }

    private static int nextPowerOf2(int a) {
        int b = 1;
        while (b < a) {
            b = b << 1;
        }
        return b;
    }

    // public List<DirectEliminationMatch> createDirectEliminationMatches(List<TournamentFencer> fencers) {
    //     List<DirectEliminationMatch> directEliminationMatches = new ArrayList<>(); 

    // }
}
