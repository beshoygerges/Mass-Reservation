package com.stmgalex.reservation.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "[Evening]")
public class Evening implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false, columnDefinition = "int default 100")
    private int totalSeats = 100;

    @Column(nullable = false, columnDefinition = "int default 0")
    private int reservedSeats = 0;

    @Column(nullable = false, columnDefinition = "boolean default true")
    private boolean enabled = true;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(mappedBy = "evening")
    private List<EveningReservation> eveningReservations = new ArrayList<>();

    public boolean haveSeats() {
        return totalSeats - reservedSeats > 0;
    }

    public void reserveSeat() {
        reservedSeats++;
    }

    public void releaseSeat() {
        reservedSeats--;
    }

    @Transient
    public String getStatus() {
        return enabled ? "نشط" : "غير نشط";
    }

    @Transient
    public double getAttendancePercentage() {
        return reservedSeats * 1.0 / totalSeats * 100d;
    }

    @Transient
    public int getAvailableSeats() {
        return totalSeats - reservedSeats;
    }

    @Transient
    public boolean isExpired() {
        return date.isBefore(LocalDate.now());
    }

    @Transient
    public String getDay() {
        if (date.getDayOfWeek().equals(DayOfWeek.FRIDAY))
            return "الجمعة";
        if (date.getDayOfWeek().equals(DayOfWeek.SATURDAY))
            return "السبت";
        if (date.getDayOfWeek().equals(DayOfWeek.SUNDAY))
            return "الاحد";
        if (date.getDayOfWeek().equals(DayOfWeek.MONDAY))
            return "الاثنين";
        if (date.getDayOfWeek().equals(DayOfWeek.TUESDAY))
            return "الثلاثاء";
        if (date.getDayOfWeek().equals(DayOfWeek.WEDNESDAY))
            return "الاربعاء";
        if (date.getDayOfWeek().equals(DayOfWeek.THURSDAY))
            return "الخميس";
        return null;

    }
}
