package cs203.ftms.overall;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import cs203.ftms.overall.dto.VerifyOrgDTO;
import cs203.ftms.overall.dto.clean.CleanAdminDTO;
import cs203.ftms.overall.model.userrelated.Admin;
import cs203.ftms.overall.model.userrelated.Organiser;
import cs203.ftms.overall.repository.userrelated.OrganiserRepository;
import cs203.ftms.overall.service.admin.AdminService;
import cs203.ftms.overall.service.admin.MailService;
import jakarta.persistence.EntityNotFoundException;

public class AdminServiceTest {

    @Mock
    private OrganiserRepository organiserRepository;

    @Mock
    private MailService mailService;

    @InjectMocks
    private AdminService adminService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Tests the getCleanAdmin method with a valid Admin object.
     * Verifies that the admin's details are correctly mapped to the CleanAdminDTO object.
     */
    @Test
    public void testGetCleanAdmin_ValidAdmin() {
        Admin admin = new Admin();
        admin.setId(1);
        admin.setName("Admin");
        admin.setEmail("admin@example.com");
        admin.setContactNo("6591238764");
        admin.setCountry("Singapore");

        CleanAdminDTO cleanAdminDTO = adminService.getCleanAdmin(admin);

        assertNotNull(cleanAdminDTO);
        assertEquals(admin.getId(), cleanAdminDTO.getId());
        assertEquals(admin.getName(), cleanAdminDTO.getName());
        assertEquals(admin.getEmail(), cleanAdminDTO.getEmail());
        assertEquals(admin.getContactNo(), cleanAdminDTO.getContactNo());
        assertEquals(admin.getCountry(), cleanAdminDTO.getCountry());
    }

    /**
     * Tests the getCleanAdmin method with a null Admin object.
     * Verifies that an EntityNotFoundException is thrown when a null Admin is passed.
     */
    @Test
    public void testGetCleanAdmin_NullAdmin() {
        assertThrows(EntityNotFoundException.class, () -> {
            adminService.getCleanAdmin(null);
        });
    }

    /**
     * Tests the getUnverifiedOrgs method when there are unverified organisers in the database.
     * Verifies that the method returns the list of unverified organisers.
     */
    @Test
    public void testGetUnverifiedOrgs_UnverifiedOrgsExist() {
        Organiser organiser1 = new Organiser();
        organiser1.setId(1);
        organiser1.setVerified(false);

        Organiser organiser2 = new Organiser();
        organiser2.setId(2);
        organiser2.setVerified(false);

        when(organiserRepository.findByVerified(false)).thenReturn(Arrays.asList(organiser1, organiser2));

        List<Organiser> unverifiedOrgs = adminService.getUnverifiedOrgs();

        assertNotNull(unverifiedOrgs);
        assertEquals(2, unverifiedOrgs.size());
        assertFalse(unverifiedOrgs.get(0).isVerified());
        assertFalse(unverifiedOrgs.get(1).isVerified());
    }

    /**
     * Tests the getUnverifiedOrgs method when there are no unverified organisers in the database.
     * Verifies that the method returns an empty list when no unverified organisers are found.
     */
    @Test
    public void testGetUnverifiedOrgs_NoUnverifiedOrgs() {
        when(organiserRepository.findByVerified(false)).thenReturn(Arrays.asList());

        List<Organiser> unverifiedOrgs = adminService.getUnverifiedOrgs();

        assertNotNull(unverifiedOrgs);
        assertTrue(unverifiedOrgs.isEmpty());
    }

    /**
     * Tests the verifyOrg method for both approve and deny actions.
     * Verifies that the organiser is approved or denied correctly and the appropriate actions are taken (saving, deleting, sending emails).
     */
    @Test
    public void testVerifyOrg_ApproveAndDeny() {
        Organiser organiser1 = new Organiser();
        organiser1.setId(1);
        organiser1.setVerified(false);

        Organiser organiser2 = new Organiser();
        organiser2.setId(2);
        organiser2.setVerified(false);

        VerifyOrgDTO dto = new VerifyOrgDTO();
        dto.setApprove(Arrays.asList(1));
        dto.setDeny(Arrays.asList(2));

        when(organiserRepository.findById(1)).thenReturn(Optional.of(organiser1));
        when(organiserRepository.findById(2)).thenReturn(Optional.of(organiser2));

        adminService.verifyOrg(dto);

        verify(organiserRepository).save(organiser1);
        assertTrue(organiser1.isVerified());
        verify(mailService).sendMail(eq(organiser1.getEmail()), eq("Account Verified"), anyString());
        verify(organiserRepository).delete(organiser2);
    }

    /**
     * Tests the verifyOrg method when an organiser to approve is not found.
     * Verifies that an EntityNotFoundException is thrown when the organiser does not exist in the repository.
     */
    @Test
    public void testVerifyOrg_ApproveOrganiserNotFound() {
        VerifyOrgDTO dto = new VerifyOrgDTO();
        dto.setApprove(Arrays.asList(1));
        dto.setDeny(Arrays.asList(2));

        when(organiserRepository.findById(1)).thenReturn(Optional.empty());
        when(organiserRepository.findById(2)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            adminService.verifyOrg(dto);
        });
    }

    /**
     * Tests the verifyOrg method when an organiser to deny is not found.
     * Verifies that an EntityNotFoundException is thrown when the organiser does not exist in the repository.
     */
    @Test
    public void testVerifyOrg_DenyOrganiserNotFound() {
        VerifyOrgDTO dto = new VerifyOrgDTO();
        dto.setApprove(Collections.emptyList());
        dto.setDeny(Arrays.asList(2));

        when(organiserRepository.findById(2)).thenReturn(Optional.empty());
        
        assertThrows(EntityNotFoundException.class, () -> {
            adminService.verifyOrg(dto);
        });
    }
}
