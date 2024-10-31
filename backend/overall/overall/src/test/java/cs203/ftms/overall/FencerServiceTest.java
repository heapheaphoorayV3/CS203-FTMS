package cs203.ftms.overall;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.bind.MethodArgumentNotValidException;

import cs203.ftms.overall.dto.CompleteFencerProfileDTO;
import cs203.ftms.overall.dto.clean.CleanFencerDTO;
import cs203.ftms.overall.model.userrelated.Fencer;
import cs203.ftms.overall.repository.userrelated.FencerRepository;
import cs203.ftms.overall.repository.userrelated.UserRepository;
import cs203.ftms.overall.service.fencer.FencerService;

class FencerServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private FencerRepository fencerRepository;

    @InjectMocks
    private FencerService fencerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getCleanFencerDTO_ValidFencer_ReturnCleanFencer() {
        // Given
        Fencer fencer = new Fencer();
        fencer.setId(1);
        fencer.setName("John Doe");
        fencer.setEmail("john@example.com");
        fencer.setContactNo("123456789");
        fencer.setCountry("USA");
        fencer.setDateOfBirth(LocalDate.of(1990, 1, 1));
        fencer.setDominantArm('R');
        fencer.setWeapon('F');
        fencer.setClub("Best Club");
        fencer.setPoints(100);
        fencer.setDebutYear(2008);
        fencer.setGender('M');

        // When
        CleanFencerDTO cleanFencerDTO = fencerService.getCleanFencerDTO(fencer);

        // Then
        assertNotNull(cleanFencerDTO);
        assertEquals(fencer.getId(), cleanFencerDTO.getId());
        assertEquals(fencer.getName(), cleanFencerDTO.getName());
        assertEquals(fencer.getEmail(), cleanFencerDTO.getEmail());
        assertEquals(fencer.getDebutYear(), cleanFencerDTO.getDebutYear());
    }

    @Test
    void getCleanFencerDTO_InvalidFencer_ReturnNull() {
        // When
        CleanFencerDTO cleanFencerDTO = fencerService.getCleanFencerDTO(null);

        // Then
        assertNull(cleanFencerDTO);
    }

    @Test
    void completeProfile_ValidInput_ReturnFencer() throws MethodArgumentNotValidException {
        // Given
        Fencer fencer = new Fencer();
        fencer.setDateOfBirth(LocalDate.of(1990, 1, 1));

        CompleteFencerProfileDTO dto = new CompleteFencerProfileDTO(null, null, null, null, 0);
        dto.setClub("Best Club");
        dto.setDebutYear(2010);
        dto.setDominantArm('R');
        dto.setGender('M');
        dto.setWeapon('F');

        when(userRepository.save(any(Fencer.class))).thenReturn(fencer);

        // When
        Fencer updatedFencer = fencerService.completeProfile(fencer, dto);

        // Then
        assertNotNull(updatedFencer);
        assertEquals("Best Club", updatedFencer.getClub());
        assertEquals(2010, updatedFencer.getDebutYear());
        assertEquals('R', updatedFencer.getDominantArm());
        assertEquals('M', updatedFencer.getGender());
        verify(userRepository, times(1)).save(fencer);
    }

    @Test
    void completeProfile_InvalidDebutYear_ReturnException() {
        // Given
        Fencer fencer = new Fencer();
        fencer.setDateOfBirth(LocalDate.of(2010, 1, 1));

        CompleteFencerProfileDTO dto = new CompleteFencerProfileDTO('L', 'S', 'M', "SMU", 2002);
        dto.setClub("Best Club");
        dto.setDebutYear(2015); // Invalid: less than 8 years after DoB
        dto.setDominantArm('R');
        dto.setGender('M');
        dto.setWeapon('F');

        // Expect exception
        assertThrows(MethodArgumentNotValidException.class, () -> {
            fencerService.completeProfile(fencer, dto);
        });

        verify(userRepository, times(0)).save(any(Fencer.class));
    }

    // Tests for getInternationalRank
    @Test
    void testGetInternationalRank_ReturnsSortedFencers() {
        // Arrange
        Fencer fencer1 = new Fencer();
        fencer1.setId(1);
        fencer1.setPoints(50);
        Fencer fencer2 = new Fencer();
        fencer2.setId(2);
        fencer2.setPoints(100);
        Fencer fencer3 = new Fencer();
        fencer3.setId(3);
        fencer3.setPoints(75);
        
        List<Fencer> fencers = Arrays.asList(fencer1, fencer2, fencer3);
        when(fencerRepository.findAll()).thenReturn(fencers);

        // Act
        List<Fencer> result = fencerService.getInternationalRank();

        // Assert
        assertEquals(3, result.size());
        assertEquals(2, result.get(0).getId());  // Highest points
        assertEquals(3, result.get(1).getId());  // Second highest points
        assertEquals(1, result.get(2).getId());  // Lowest points
        verify(fencerRepository, times(1)).findAll();
    }

    @Test
    void testGetInternationalRank_ReturnsEmptyList_WhenNoFencersExist() {
        // Arrange
        when(fencerRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Fencer> result = fencerService.getInternationalRank();

        // Assert
        assertTrue(result.isEmpty());
        verify(fencerRepository, times(1)).findAll();
    }
}
