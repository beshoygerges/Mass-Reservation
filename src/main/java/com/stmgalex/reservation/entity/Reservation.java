package com.stmgalex.reservation.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;


@NoArgsConstructor
@Data
@Entity
@Table(name = "[Reservation]")
public class Reservation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "massId")
    private Mass mass;

    @Column(nullable = false, columnDefinition = "boolean default true")
    private boolean active = true;

    public Reservation(User user, Mass mass) {
        this.user = user;
        this.mass = mass;
    }
}
