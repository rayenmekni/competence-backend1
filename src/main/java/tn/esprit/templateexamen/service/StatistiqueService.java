package tn.esprit.templateexamen.service;

import tn.esprit.templateexamen.dto.StatistiqueCompetenceDto;
import tn.esprit.templateexamen.dto.StatistiquesGlobalesDto;

/**
 * Service métier pour calculer les statistiques à partir des entités existantes.
 */
public interface StatistiqueService {

    /**
     * Statistiques globales sur toutes les compétences et toutes les notes.
     */
    StatistiquesGlobalesDto getStatistiquesGlobales();

    /**
     * Statistiques pour une compétence donnée (nombre de notes, moyenne).
     */
    StatistiqueCompetenceDto getStatistiquesPourCompetence(Long skillId);
}

