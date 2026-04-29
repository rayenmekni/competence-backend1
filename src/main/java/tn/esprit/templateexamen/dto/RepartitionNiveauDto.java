package tn.esprit.templateexamen.dto;

import tn.esprit.templateexamen.entite.NiveauSkill;

/**
 * DTO pour représenter le nombre de compétences par niveau.
 */
public class RepartitionNiveauDto {

    private NiveauSkill niveau;
    private long total;

    public RepartitionNiveauDto() {
    }

    public RepartitionNiveauDto(NiveauSkill niveau, long total) {
        this.niveau = niveau;
        this.total = total;
    }

    public NiveauSkill getNiveau() {
        return niveau;
    }

    public void setNiveau(NiveauSkill niveau) {
        this.niveau = niveau;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}

