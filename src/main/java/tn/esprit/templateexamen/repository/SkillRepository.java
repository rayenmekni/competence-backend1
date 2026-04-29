package tn.esprit.templateexamen.repository;

import tn.esprit.templateexamen.entite.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {
    // Recherche par niveau
    List<Skill> findByNiveau(String niveau);

    // Recherche par nom (ignore case)
    List<Skill> findByNomSkillContainingIgnoreCase(String nom);

    // Requête personnalisée avec JPQL
    @Query("SELECT s FROM Skill s WHERE s.niveau = :niveau ORDER BY s.nomSkill ASC")
    List<Skill> findcompetenceByNiveauOrdered(@Param("niveau") String niveau);

    // Vérifier si un skill existe déjà
    boolean existsByNomSkill(String nomSkill);

    /**
     * Retourne pour chaque niveau le nombre de compétences correspondantes.
     * Résultat : liste de tableaux [NiveauSkill, Long].
     */
    @Query("SELECT s.niveau, COUNT(s) FROM Skill s GROUP BY s.niveau")
    List<Object[]> countcompetenceByNiveau();
}