package tn.esprit.templateexamen.service;

import tn.esprit.templateexamen.entite.Skill;
import java.util.List;

public interface Iskillservice {
    // CRUD de base
    List<Skill> retrieveAllcompetence();
    Skill retrieveSkill(Long skillId);
    Skill addSkill(Skill skill);
    void removeSkill(Long skillId);
    Skill modifySkill(Skill skill);

    // Méthodes supplémentaires
    List<Skill> retrievecompetenceByNiveau(String niveau);
    List<Skill> searchcompetenceByName(String nom);
}