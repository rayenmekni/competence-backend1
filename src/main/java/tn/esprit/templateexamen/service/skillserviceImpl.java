package tn.esprit.templateexamen.service;

import lombok.AllArgsConstructor;
import tn.esprit.templateexamen.entite.Skill;
import tn.esprit.templateexamen.repository.SkillRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@AllArgsConstructor
public class skillserviceImpl implements Iskillservice {

    private final SkillRepository skillRepository;

    @Override
    public List<Skill> retrieveAllcompetence() {
        return skillRepository.findAll();
    }

    @Override
    public Skill retrieveSkill(Long skillId) {
        return skillRepository.findById(skillId)
                .orElseThrow(() -> new RuntimeException("Skill non trouvé avec l'id: " + skillId));
    }

    @Override
    public Skill addSkill(Skill skill) {
        // Vérifier si le nom existe déjà
        if (skillRepository.existsByNomSkill(skill.getNomSkill())) {
            throw new RuntimeException("Un skill avec ce nom existe déjà!");
        }
        return skillRepository.save(skill);
    }

    @Override
    public void removeSkill(Long skillId) {
        Skill skill = retrieveSkill(skillId);
        skillRepository.delete(skill);
    }

    @Override
    public Skill modifySkill(Skill skill) {
        // Vérifier que le skill existe
        retrieveSkill(skill.getIdSkill());
        return skillRepository.save(skill);
    }

    @Override
    public List<Skill> retrievecompetenceByNiveau(String niveau) {
        return skillRepository.findByNiveau(niveau);
    }

    @Override
    public List<Skill> searchcompetenceByName(String nom) {
        return skillRepository.findByNomSkillContainingIgnoreCase(nom);
    }
}