package cs203.ftms.overall;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.Year;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.bind.MethodArgumentNotValidException;

import cs203.ftms.overall.dto.CompleteFencerProfileDTO;
import cs203.ftms.overall.dto.clean.CleanFencerDTO;
import cs203.ftms.overall.model.userrelated.Fencer;
import cs203.ftms.overall.repository.userrelated.UserRepository;
import cs203.ftms.overall.service.fencer.FencerService;

class FencerServiceTest {

    @Mock
    private UserRepository userRepository;

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
}
