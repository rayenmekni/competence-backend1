package tn.esprit.templateexamen.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.templateexamen.entite.NiveauSkill;
import tn.esprit.templateexamen.entite.Rating;
import tn.esprit.templateexamen.entite.Skill;
import tn.esprit.templateexamen.repository.RatingRepository;
import tn.esprit.templateexamen.repository.SkillRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RatingServiceImplTest {

    @Mock
    private RatingRepository ratingRepository;

    @Mock
    private SkillRepository skillRepository;

    @Mock
    private Iskillservice skillservice;

    @InjectMocks
    private RatingServiceImpl ratingService;

    private Skill skill;
    private Rating rating1;
    private Rating rating2;

    @BeforeEach
    void setUp() {
        skill = Skill.builder()
                .idSkill(1L)
                .nomSkill("Java")
                .description("Langage de programmation")
                .niveau(NiveauSkill.EXPERT)
                .build();

        rating1 = Rating.builder()
                .idRating(1L)
                .note(4)
                .commentaire("Très bien")
                .dateEvaluation(LocalDate.now())
                .skill(skill)
                .build();

        rating2 = Rating.builder()
                .idRating(2L)
                .note(5)
                .commentaire("Excellent")
                .dateEvaluation(LocalDate.now())
                .skill(skill)
                .build();
    }

    // ✅ Test retrieveAllRatings
    @Test
    void testRetrieveAllRatings() {
        when(ratingRepository.findAll()).thenReturn(Arrays.asList(rating1, rating2));

        List<Rating> result = ratingService.retrieveAllRatings();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(ratingRepository, times(1)).findAll();
    }

    // ✅ Test retrieveRating - trouvé
    @Test
    void testRetrieveRating_Found() {
        when(ratingRepository.findById(1L)).thenReturn(Optional.of(rating1));

        Rating result = ratingService.retrieveRating(1L);

        assertNotNull(result);
        assertEquals(4, result.getNote());
        assertEquals("Très bien", result.getCommentaire());
        verify(ratingRepository, times(1)).findById(1L);
    }

    // ✅ Test retrieveRating - non trouvé
    @Test
    void testRetrieveRating_NotFound() {
        when(ratingRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> ratingService.retrieveRating(99L));

        assertTrue(exception.getMessage().contains("99"));
        verify(ratingRepository, times(1)).findById(99L);
    }

    // ✅ Test addRating - succès
    @Test
    void testAddRating_Success() {
        Rating newRating = Rating.builder()
                .note(3)
                .commentaire("Bien")
                .build();

        when(skillservice.retrieveSkill(1L)).thenReturn(skill);
        when(ratingRepository.save(any(Rating.class))).thenReturn(newRating);

        Rating result = ratingService.addRating(newRating, 1L);

        assertNotNull(result);
        assertEquals(3, result.getNote());
        verify(ratingRepository, times(1)).save(any(Rating.class));
    }

    // ✅ Test addRating - note invalide (trop basse)
    @Test
    void testAddRating_NoteTropBasse() {
        Rating invalidRating = Rating.builder()
                .note(0)
                .commentaire("Invalide")
                .build();

        when(skillservice.retrieveSkill(1L)).thenReturn(skill);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> ratingService.addRating(invalidRating, 1L));

        assertTrue(exception.getMessage().contains("entre 1 et 5"));
        verify(ratingRepository, never()).save(any());
    }

    // ✅ Test addRating - note invalide (trop haute)
    @Test
    void testAddRating_NoteTropHaute() {
        Rating invalidRating = Rating.builder()
                .note(6)
                .commentaire("Invalide")
                .build();

        when(skillservice.retrieveSkill(1L)).thenReturn(skill);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> ratingService.addRating(invalidRating, 1L));

        assertTrue(exception.getMessage().contains("entre 1 et 5"));
        verify(ratingRepository, never()).save(any());
    }

    // ✅ Test addRating - date auto si null
    @Test
    void testAddRating_DateAutoSet() {
        Rating newRating = Rating.builder()
                .note(3)
                .commentaire("Bien")
                .dateEvaluation(null)
                .build();

        when(skillservice.retrieveSkill(1L)).thenReturn(skill);
        when(ratingRepository.save(any(Rating.class))).thenAnswer(i -> i.getArgument(0));

        Rating result = ratingService.addRating(newRating, 1L);

        assertNotNull(result.getDateEvaluation());
        assertEquals(LocalDate.now(), result.getDateEvaluation());
    }

    // ✅ Test removeRating
    @Test
    void testRemoveRating() {
        when(ratingRepository.findById(1L)).thenReturn(Optional.of(rating1));
        doNothing().when(ratingRepository).delete(rating1);

        ratingService.removeRating(1L);

        verify(ratingRepository, times(1)).delete(rating1);
    }

    // ✅ Test modifyRating - succès
    @Test
    void testModifyRating_Success() {
        rating1.setNote(5);
        when(ratingRepository.findById(1L)).thenReturn(Optional.of(rating1));
        when(ratingRepository.save(rating1)).thenReturn(rating1);

        Rating result = ratingService.modifyRating(rating1);

        assertNotNull(result);
        assertEquals(5, result.getNote());
        verify(ratingRepository, times(1)).save(rating1);
    }

    // ✅ Test modifyRating - note invalide
    @Test
    void testModifyRating_NoteInvalide() {
        rating1.setNote(0);
        when(ratingRepository.findById(1L)).thenReturn(Optional.of(rating1));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> ratingService.modifyRating(rating1));

        assertTrue(exception.getMessage().contains("entre 1 et 5"));
        verify(ratingRepository, never()).save(any());
    }

    // ✅ Test retrieveRatingsBySkill
    @Test
    void testRetrieveRatingsBySkill() {
        when(skillservice.retrieveSkill(1L)).thenReturn(skill);
        when(ratingRepository.findBySkillIdSkill(1L))
                .thenReturn(Arrays.asList(rating1, rating2));

        List<Rating> result = ratingService.retrieveRatingsBySkill(1L);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(ratingRepository, times(1)).findBySkillIdSkill(1L);
    }

    // ✅ Test getAverageRatingForSkill
    @Test
    void testGetAverageRatingForSkill() {
        when(skillservice.retrieveSkill(1L)).thenReturn(skill);
        when(ratingRepository.findAverageNoteBySkillId(1L)).thenReturn(4.5);

        Double result = ratingService.getAverageRatingForSkill(1L);

        assertNotNull(result);
        assertEquals(4.5, result);
        verify(ratingRepository, times(1)).findAverageNoteBySkillId(1L);
    }

    // ✅ Test getRatingsCountForSkill
    @Test
    void testGetRatingsCountForSkill() {
        when(skillservice.retrieveSkill(1L)).thenReturn(skill);
        when(ratingRepository.countRatingsBySkillId(1L)).thenReturn(5L);

        Long result = ratingService.getRatingsCountForSkill(1L);

        assertNotNull(result);
        assertEquals(5L, result);
        verify(ratingRepository, times(1)).countRatingsBySkillId(1L);
    }
}