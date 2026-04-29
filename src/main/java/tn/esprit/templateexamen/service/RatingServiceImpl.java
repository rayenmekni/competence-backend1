package tn.esprit.templateexamen.service;

import lombok.AllArgsConstructor;
import tn.esprit.templateexamen.entite.Rating;
import tn.esprit.templateexamen.entite.Skill;
import tn.esprit.templateexamen.repository.RatingRepository;
import tn.esprit.templateexamen.repository.SkillRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class RatingServiceImpl implements IRatingService {

    private final RatingRepository ratingRepository;
    private final SkillRepository skillRepository;
    private final Iskillservice skillservice;

    @Override
    public List<Rating> retrieveAllRatings() {
        return ratingRepository.findAll();
    }

    @Override
    public Rating retrieveRating(Long ratingId) {
        return ratingRepository.findById(ratingId)
                .orElseThrow(() -> new RuntimeException("Rating non trouvé avec l'id: " + ratingId));
    }

    @Override
    public Rating addRating(Rating rating, Long skillId) {
        // Récupérer le skill associé
        Skill skill = skillservice.retrieveSkill(skillId);

        // Valider la note (entre 1 et 5)
        if (rating.getNote() < 1 || rating.getNote() > 5) {
            throw new RuntimeException("La note doit être entre 1 et 5");
        }

        // Ajouter la date du jour si non fournie
        if (rating.getDateEvaluation() == null) {
            rating.setDateEvaluation(LocalDate.now());
        }

        // Associer le skill
        rating.setSkill(skill);

        return ratingRepository.save(rating);
    }

    @Override
    public void removeRating(Long ratingId) {
        Rating rating = retrieveRating(ratingId);
        ratingRepository.delete(rating);
    }

    @Override
    public Rating modifyRating(Rating rating) {
        // Vérifier que le rating existe
        retrieveRating(rating.getIdRating());

        // Valider la note
        if (rating.getNote() < 1 || rating.getNote() > 5) {
            throw new RuntimeException("La note doit être entre 1 et 5");
        }

        return ratingRepository.save(rating);
    }

    @Override
    public List<Rating> retrieveRatingsBySkill(Long skillId) {
        // Vérifier que le skill existe
        skillservice.retrieveSkill(skillId);
        return ratingRepository.findBySkillIdSkill(skillId);
    }

    @Override
    public Double getAverageRatingForSkill(Long skillId) {
        // Vérifier que le skill existe
        skillservice.retrieveSkill(skillId);
        return ratingRepository.findAverageNoteBySkillId(skillId);
    }

    @Override
    public Long getRatingsCountForSkill(Long skillId) {
        // Vérifier que le skill existe
        skillservice.retrieveSkill(skillId);
        return ratingRepository.countRatingsBySkillId(skillId);
    }
}