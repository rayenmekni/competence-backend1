package tn.esprit.templateexamen.entite;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRating;

    private int note; // 1 à 5
    private String commentaire;
    private LocalDate dateEvaluation;

    @ManyToOne
    @JoinColumn(name = "skill_id")
    private Skill skill;
}