package tn.esprit.templateexamen.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.templateexamen.dto.RepartitionNiveauDto;
import tn.esprit.templateexamen.dto.StatistiqueCompetenceDto;
import tn.esprit.templateexamen.dto.StatistiquesGlobalesDto;
import tn.esprit.templateexamen.entite.NiveauSkill;
import tn.esprit.templateexamen.entite.Skill;
import tn.esprit.templateexamen.repository.RatingRepository;
import tn.esprit.templateexamen.repository.SkillRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * Implémentation du service de statistiques.
 */
@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class StatistiqueServiceImpl implements StatistiqueService {

    private final SkillRepository skillRepository;
    private final RatingRepository ratingRepository;

    @Override
    public StatistiquesGlobalesDto getStatistiquesGlobales() {
        long totalCompetences = skillRepository.count();

        Long totalNotesObj = ratingRepository.countAllRatings();
        long totalNotes = (totalNotesObj != null) ? totalNotesObj : 0L;

        Double moyenneBrute = ratingRepository.findGlobalAverageNote();
        double noteMoyenneGlobale = 0.0;
        if (moyenneBrute != null) {
            noteMoyenneGlobale = BigDecimal
                    .valueOf(moyenneBrute)
                    .setScale(2, RoundingMode.HALF_UP)
                    .doubleValue();
        }

        List<RepartitionNiveauDto> repartition = new ArrayList<>();
        List<Object[]> lignes = skillRepository.countcompetenceByNiveau();
        for (Object[] ligne : lignes) {
            NiveauSkill niveau = (NiveauSkill) ligne[0];
            Long count = (Long) ligne[1];
            repartition.add(new RepartitionNiveauDto(niveau, count != null ? count : 0L));
        }

        return new StatistiquesGlobalesDto(
                totalCompetences,
                totalNotes,
                noteMoyenneGlobale,
                repartition
        );
    }

    @Override
    public StatistiqueCompetenceDto getStatistiquesPourCompetence(Long skillId) {
        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new IllegalArgumentException("Compétence introuvable avec id=" + skillId));

        Long nombreNotesObj = ratingRepository.countRatingsBySkillId(skillId);
        long nombreNotes = (nombreNotesObj != null) ? nombreNotesObj : 0L;

        Double moyenneBrute = ratingRepository.findAverageNoteBySkillId(skillId);
        double noteMoyenne = 0.0;
        if (moyenneBrute != null) {
            noteMoyenne = BigDecimal
                    .valueOf(moyenneBrute)
                    .setScale(2, RoundingMode.HALF_UP)
                    .doubleValue();
        }

        return new StatistiqueCompetenceDto(
                skill.getIdSkill(),
                skill.getNomSkill(),
                nombreNotes,
                noteMoyenne
        );
    }
}

