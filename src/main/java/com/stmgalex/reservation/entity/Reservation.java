package com.stmgalex.reservation.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;


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

    @Column(nullable = false, columnDefinition = "int default 0")
    private int seatNumber;

    @Column(nullable = false, columnDefinition = "boolean default true")
    private boolean active = true;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public Reservation(User user, Mass mass) {
        this.user = user;
        this.mass = mass;
    }
}
