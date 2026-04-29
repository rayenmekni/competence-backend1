package tn.esprit.templateexamen.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.templateexamen.entite.Rating;
import tn.esprit.templateexamen.service.IRatingService;

import java.util.List;

@RestController
@RequestMapping("/api/ratings")
@AllArgsConstructor
public class RatingController {

    private final IRatingService ratingService;

    // http://localhost:8081/api/ratings/retrieve-all-ratings
    @GetMapping("/retrieve-all-ratings")
    public List<Rating> retrieveAllRatings() {
        return ratingService.retrieveAllRatings();
    }

    // http://localhost:8081/api/ratings/retrieve-rating/{rating-id}
    @GetMapping("/retrieve-rating/{rating-id}")
    public Rating retrieveRating(@PathVariable("rating-id") Long ratingId) {
        return ratingService.retrieveRating(ratingId);
    }

    // http://localhost:8081/api/ratings/add-rating/{skill-id}
    @PostMapping("/add-rating/{skill-id}")
    public Rating addRating(@RequestBody Rating rating, @PathVariable("skill-id") Long skillId) {
        return ratingService.addRating(rating, skillId);
    }

    // http://localhost:8081/api/ratings/remove-rating/{rating-id}
    @DeleteMapping("/remove-rating/{rating-id}")
    public void removeRating(@PathVariable("rating-id") Long ratingId) {
        ratingService.removeRating(ratingId);
    }

    // http://localhost:8081/api/ratings/modify-rating
    @PutMapping("/modify-rating")
    public Rating modifyRating(@RequestBody Rating rating) {
        return ratingService.modifyRating(rating);
    }

    // http://localhost:8081/api/ratings/retrieve-ratings-by-skill/{skill-id}
    @GetMapping("/retrieve-ratings-by-skill/{skill-id}")
    public List<Rating> retrieveRatingsBySkill(@PathVariable("skill-id") Long skillId) {
        return ratingService.retrieveRatingsBySkill(skillId);
    }

    // http://localhost:8081/api/ratings/average-rating/{skill-id}
    @GetMapping("/average-rating/{skill-id}")
    public Double getAverageRatingForSkill(@PathVariable("skill-id") Long skillId) {
        return ratingService.getAverageRatingForSkill(skillId);
    }

    // http://localhost:8081/api/ratings/count-ratings/{skill-id}
    @GetMapping("/count-ratings/{skill-id}")
    public Long getRatingsCountForSkill(@PathVariable("skill-id") Long skillId) {
        return ratingService.getRatingsCountForSkill(skillId);
    }
}