package tn.esprit.templateexamen.dto;

import java.util.List;

/**
 * DTO utilisé pour exposer les statistiques globales
 * (nombre total de compétences, de notes, moyenne globale, répartition par niveau).
 */
public class StatistiquesGlobalesDto {

    private long totalCompetences;
    private long totalNotes;
    private double noteMoyenneGlobale;
    private List<RepartitionNiveauDto> repartitionParNiveau;

    public StatistiquesGlobalesDto() {
    }

    public StatistiquesGlobalesDto(long totalCompetences,
                                   long totalNotes,
                                   double noteMoyenneGlobale,
                                   List<RepartitionNiveauDto> repartitionParNiveau) {
        this.totalCompetences = totalCompetences;
        this.totalNotes = totalNotes;
        this.noteMoyenneGlobale = noteMoyenneGlobale;
        this.repartitionParNiveau = repartitionParNiveau;
    }

    public long getTotalCompetences() {
        return totalCompetences;
    }

    public void setTotalCompetences(long totalCompetences) {
        this.totalCompetences = totalCompetences;
    }

    public long getTotalNotes() {
        return totalNotes;
    }

    public void setTotalNotes(long totalNotes) {
        this.totalNotes = totalNotes;
    }

    public double getNoteMoyenneGlobale() {
        return noteMoyenneGlobale;
    }

    public void setNoteMoyenneGlobale(double noteMoyenneGlobale) {
        this.noteMoyenneGlobale = noteMoyenneGlobale;
    }

    public List<RepartitionNiveauDto> getRepartitionParNiveau() {
        return repartitionParNiveau;
    }

    public void setRepartitionParNiveau(List<RepartitionNiveauDto> repartitionParNiveau) {
        this.repartitionParNiveau = repartitionParNiveau;
    }
}

