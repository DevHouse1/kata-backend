package com.kata.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Command {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Client client;

    @OneToMany(mappedBy = "command", cascade = CascadeType.ALL)
    private List<CommandLines> lignes;

    @Enumerated(EnumType.STRING)
    private Statut statut;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreation;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateValidation;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateAnnulation;

    public enum Statut {
        EN_ATTENTE, VALIDEE, EXPEDIEE, ANNULEE
    }

}



