package tn.esprit.templateexamen.repository;

import tn.esprit.templateexamen.entite.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    // Trouver tous les ratings d'un skill spécifique
    List<Rating> findBySkillIdSkill(Long skillId);

    // Trouver les ratings par note minimum
    List<Rating> findByNoteGreaterThanEqual(int note);

    // Calculer la moyenne des notes pour un skill
    @Query("SELECT AVG(r.note) FROM Rating r WHERE r.skill.idSkill = :skillId")
    Double findAverageNoteBySkillId(@Param("skillId") Long skillId);

    // Compter le nombre de ratings pour un skill
    @Query("SELECT COUNT(r) FROM Rating r WHERE r.skill.idSkill = :skillId")
    Long countRatingsBySkillId(@Param("skillId") Long skillId);

    // Nombre total de notes (toutes compétences confondues)
    @Query("SELECT COUNT(r) FROM Rating r")
    Long countAllRatings();

    // Moyenne globale de toutes les notes
    @Query("SELECT AVG(r.note) FROM Rating r")
    Double findGlobalAverageNote();
}