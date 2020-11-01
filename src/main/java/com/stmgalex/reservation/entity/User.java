package com.stmgalex.reservation.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@Entity
@Table(name = "[User]")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String nationalId;

    @Column(nullable = false)
    private String mobileNumber;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OrderBy("id desc")
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reservation> reservations = new ArrayList<>();


    @Transient
    public Mass getLastActiveMass() {
        Reservation reservation = getLastActiveReservation();
        if (Objects.nonNull(reservation))
            return reservation.getMass();
        return null;
    }

    @Transient
    public Reservation getLastActiveReservation() {
        return reservations.stream()
                .filter(Reservation::isActive)
                .findFirst()
                .orElse(null);
    }

    @Transient
    public Reservation getLastReservation() {
        return reservations.stream()
                .filter(r -> r.isActive() && r.getMass().getDate().isAfter(LocalDate.now()))
                .findFirst()
                .orElse(null);
    }

}


