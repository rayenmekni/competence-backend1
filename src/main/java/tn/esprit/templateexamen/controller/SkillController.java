package tn.esprit.templateexamen.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.templateexamen.entite.Skill;
import tn.esprit.templateexamen.service.Iskillservice;

import java.util.List;

@RestController
@RequestMapping("/competence")
@AllArgsConstructor
public class SkillController {

    private final Iskillservice skillservice;

    // http://localhost:8081/apicompetence/retrieve-all-competence
    @GetMapping("/retrieve-all-competence")
    public List<Skill> retrieveAllcompetence() {
        return skillservice.retrieveAllcompetence();
    }

    // http://localhost:8081/apicompetence/retrieve-skill/{skill-id}
    @GetMapping("/retrieve-skill/{skill-id}")
    public Skill retrieveSkill(@PathVariable("skill-id") Long skillId) {
        return skillservice.retrieveSkill(skillId);
    }

    // http://localhost:8081/apicompetence/add-skill
    @PostMapping("/add-skill")
    public Skill addSkill(@RequestBody Skill skill) {
        return skillservice.addSkill(skill);
    }

    // http://localhost:8081/apicompetence/remove-skill/{skill-id}
    @DeleteMapping("/remove-skill/{skill-id}")
    public void removeSkill(@PathVariable("skill-id") Long skillId) {
        skillservice.removeSkill(skillId);
    }

    // http://localhost:8081/apicompetence/modify-skill
    @PutMapping("/modify-skill")
    public Skill modifySkill(@RequestBody Skill skill) {
        return skillservice.modifySkill(skill);
    }

    // http://localhost:8081/apicompetence/retrieve-competence-by-niveau/{niveau}
    @GetMapping("/retrieve-competence-by-niveau/{niveau}")
    public List<Skill> retrievecompetenceByNiveau(@PathVariable("niveau") String niveau) {
        return skillservice.retrievecompetenceByNiveau(niveau);
    }

    // http://localhost:8081/apicompetence/search-competence-by-name/{nom}
    @GetMapping("/search-competence-by-name/{nom}")
    public List<Skill> searchcompetenceByName(@PathVariable("nom") String nom) {
        return skillservice.searchcompetenceByName(nom);
    }
}