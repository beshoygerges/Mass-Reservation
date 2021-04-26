package com.stmgalex.reservation.entity;

import com.stmgalex.reservation.constants.Gender;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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

    @Column
    private LocalDate birthdate;

    @Column(columnDefinition = "int default 0")
    private int age;

    @Enumerated(EnumType.STRING)
    private Gender gender;

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
    private List<MassReservation> massReservations = new ArrayList<>();

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OrderBy("id desc")
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EveningReservation> eveningReservations = new ArrayList<>();


    @Transient
    public MassReservation getLastActiveMassReservation() {
        return massReservations.stream()
            .filter(mr -> mr.isActive() && !mr.getMass().isYonan())
            .findFirst()
            .orElse(null);
    }

    @Transient
    public MassReservation getLastYonanMassReservation() {
        return massReservations.stream()
            .filter(mr -> mr.isActive() && mr.getMass().isYonan())
            .findFirst()
            .orElse(null);
    }

    @Transient
    public MassReservation getMassReservation(LocalDate massDate, LocalTime massTime) {
        return massReservations.stream()
            .filter(r -> r.isActive() &&
                r.getMass().getDate().equals(massDate) &&
                r.getMass().getTime().equals(massTime))
            .findFirst()
            .orElse(null);
    }

}


