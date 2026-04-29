package tn.esprit.templateexamen.dto;

/**
 * DTO pour les statistiques d'une compétence donnée :
 * nombre de notes et moyenne des notes.
 */
public class StatistiqueCompetenceDto {

    private Long idSkill;
    private String nomSkill;
    private Long nombreNotes;
    private Double noteMoyenne;

    public StatistiqueCompetenceDto() {
    }

    public StatistiqueCompetenceDto(Long idSkill,
                                    String nomSkill,
                                    Long nombreNotes,
                                    Double noteMoyenne) {
        this.idSkill = idSkill;
        this.nomSkill = nomSkill;
        this.nombreNotes = nombreNotes;
        this.noteMoyenne = noteMoyenne;
    }

    public Long getIdSkill() {
        return idSkill;
    }

    public void setIdSkill(Long idSkill) {
        this.idSkill = idSkill;
    }

    public String getNomSkill() {
        return nomSkill;
    }

    public void setNomSkill(String nomSkill) {
        this.nomSkill = nomSkill;
    }

    public Long getNombreNotes() {
        return nombreNotes;
    }

    public void setNombreNotes(Long nombreNotes) {
        this.nombreNotes = nombreNotes;
    }

    public Double getNoteMoyenne() {
        return noteMoyenne;
    }

    public void setNoteMoyenne(Double noteMoyenne) {
        this.noteMoyenne = noteMoyenne;
    }
}

