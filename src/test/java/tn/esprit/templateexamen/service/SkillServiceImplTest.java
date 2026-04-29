package tn.esprit.templateexamen.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.templateexamen.entite.NiveauSkill;
import tn.esprit.templateexamen.entite.Skill;
import tn.esprit.templateexamen.repository.SkillRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SkillServiceImplTest {

    @Mock
    private SkillRepository skillRepository;

    @InjectMocks
    private skillserviceImpl skillService;

    private Skill skill1;
    private Skill skill2;

    @BeforeEach
    void setUp() {
        skill1 = Skill.builder()
                .idSkill(1L)
                .nomSkill("Java")
                .description("Langage de programmation")
                .niveau(NiveauSkill.EXPERT)
                .build();

        skill2 = Skill.builder()
                .idSkill(2L)
                .nomSkill("Python")
                .description("Langage de script")
                .niveau(NiveauSkill.INTERMEDIAIRE)
                .build();
    }

    // ✅ Test retrieveAllcompetence
    @Test
    void testRetrieveAllcompetence() {
        when(skillRepository.findAll()).thenReturn(Arrays.asList(skill1, skill2));

        List<Skill> result = skillService.retrieveAllcompetence();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(skillRepository, times(1)).findAll();
    }

    // ✅ Test retrieveSkill - trouvé
    @Test
    void testRetrieveSkill_Found() {
        when(skillRepository.findById(1L)).thenReturn(Optional.of(skill1));

        Skill result = skillService.retrieveSkill(1L);

        assertNotNull(result);
        assertEquals("Java", result.getNomSkill());
        assertEquals(NiveauSkill.EXPERT, result.getNiveau());
        verify(skillRepository, times(1)).findById(1L);
    }

    // ✅ Test retrieveSkill - non trouvé
    @Test
    void testRetrieveSkill_NotFound() {
        when(skillRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> skillService.retrieveSkill(99L));

        assertTrue(exception.getMessage().contains("99"));
        verify(skillRepository, times(1)).findById(99L);
    }

    // ✅ Test addSkill - succès
    @Test
    void testAddSkill_Success() {
        when(skillRepository.existsByNomSkill("Java")).thenReturn(false);
        when(skillRepository.save(skill1)).thenReturn(skill1);

        Skill result = skillService.addSkill(skill1);

        assertNotNull(result);
        assertEquals("Java", result.getNomSkill());
        verify(skillRepository, times(1)).save(skill1);
    }

    // ✅ Test addSkill - nom existe déjà
    @Test
    void testAddSkill_AlreadyExists() {
        when(skillRepository.existsByNomSkill("Java")).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> skillService.addSkill(skill1));

        assertTrue(exception.getMessage().contains("existe déjà"));
        verify(skillRepository, never()).save(any());
    }

    // ✅ Test removeSkill
    @Test
    void testRemoveSkill() {
        when(skillRepository.findById(1L)).thenReturn(Optional.of(skill1));
        doNothing().when(skillRepository).delete(skill1);

        skillService.removeSkill(1L);

        verify(skillRepository, times(1)).delete(skill1);
    }

    // ✅ Test modifySkill
    @Test
    void testModifySkill() {
        skill1.setDescription("Updated description");
        when(skillRepository.findById(1L)).thenReturn(Optional.of(skill1));
        when(skillRepository.save(skill1)).thenReturn(skill1);

        Skill result = skillService.modifySkill(skill1);

        assertNotNull(result);
        assertEquals("Updated description", result.getDescription());
        verify(skillRepository, times(1)).save(skill1);
    }

    // ✅ Test retrievecompetenceByNiveau
    @Test
    void testRetrievecompetenceByNiveau() {
        when(skillRepository.findByNiveau("EXPERT")).thenReturn(Arrays.asList(skill1));

        List<Skill> result = skillService.retrievecompetenceByNiveau("EXPERT");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Java", result.get(0).getNomSkill());
        verify(skillRepository, times(1)).findByNiveau("EXPERT");
    }

    // ✅ Test searchcompetenceByName
    @Test
    void testSearchcompetenceByName() {
        when(skillRepository.findByNomSkillContainingIgnoreCase("java"))
                .thenReturn(Arrays.asList(skill1));

        List<Skill> result = skillService.searchcompetenceByName("java");

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(skillRepository, times(1))
                .findByNomSkillContainingIgnoreCase("java");
    }
}