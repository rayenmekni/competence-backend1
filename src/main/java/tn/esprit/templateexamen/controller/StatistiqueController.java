package tn.esprit.templateexamen.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.templateexamen.dto.StatistiqueCompetenceDto;
import tn.esprit.templateexamen.dto.StatistiquesGlobalesDto;
import tn.esprit.templateexamen.service.StatistiqueService;

/**
 * Contrôleur REST exposant les statistiques.
 *
 * Avec le context-path configuré (ex: /Competence) :
 * - GET /Competence/api/statistiques/global
 * - GET /Competence/api/statistiques/competences/{id}/notes
 *
 * Via la gateway :
 * - GET http://localhost:8080/api/competence/api/statistiques/global
 * - GET http://localhost:8080/api/competence/api/statistiques/competences/{id}/notes
 */
@RestController
@RequestMapping("/api/statistiques")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class StatistiqueController {

    private final StatistiqueService statistiqueService;

    @GetMapping("/global")
    public StatistiquesGlobalesDto getStatistiquesGlobales() {
        return statistiqueService.getStatistiquesGlobales();
    }

    @GetMapping("/competences/{id}/notes")
    public StatistiqueCompetenceDto getStatistiquesPourCompetence(@PathVariable("id") Long skillId) {
        try {
            return statistiqueService.getStatistiquesPourCompetence(skillId);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }
}

