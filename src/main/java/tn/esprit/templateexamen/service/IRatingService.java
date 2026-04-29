package tn.esprit.templateexamen.service;

import tn.esprit.templateexamen.entite.Rating;
import java.util.List;

public interface IRatingService {
    // CRUD de base
    List<Rating> retrieveAllRatings();
    Rating retrieveRating(Long ratingId);
    Rating addRating(Rating rating, Long skillId); // Avec association au skill
    void removeRating(Long ratingId);
    Rating modifyRating(Rating rating);

    // Méthodes spécifiques
    List<Rating> retrieveRatingsBySkill(Long skillId);
    Double getAverageRatingForSkill(Long skillId);
    Long getRatingsCountForSkill(Long skillId);
}