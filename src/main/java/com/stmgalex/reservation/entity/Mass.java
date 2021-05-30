package com.stmgalex.reservation.entity;

import com.stmgalex.reservation.constants.Gender;
import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Data
@Entity
@Table(name = "[Mass]")
public class Mass implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private LocalTime time;

    @Column(nullable = false, columnDefinition = "int default 100")
    private int totalSeats;

    @Column(nullable = false, columnDefinition = "int default 0")
    private int menSeats;

    @Column(nullable = false, columnDefinition = "int default 0")
    private int womenSeats;

    @Column(nullable = false, columnDefinition = "int default 0")
    private int reservedSeats;

    @Column(nullable = false, columnDefinition = "int default 0")
    private int menReservedSeats;

    @Column(nullable = false, columnDefinition = "int default 0")
    private int womenReservedSeats;

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
    @OneToMany(mappedBy = "mass")
    private List<MassReservation> massReservations = new ArrayList<>();

    public boolean haveSeats(Gender gender) {
        if (gender == Gender.MALE) {
            return menSeats - menReservedSeats > 0;
        }
        return womenSeats - womenReservedSeats > 0;
    }

    public boolean haveSeats() {
        return totalSeats - reservedSeats > 0;
    }

    public void reserveSeat(Gender gender) {
        if (gender == Gender.MALE) {
            menReservedSeats++;
        } else {
            womenReservedSeats++;
        }
        reservedSeats++;
    }

    public void releaseSeat(Gender gender) {
        if (gender == Gender.MALE) {
            menReservedSeats--;
        } else {
            womenReservedSeats--;
        }
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
        if (date.getDayOfWeek().equals(DayOfWeek.FRIDAY)) {
            return "الجمعة";
        }
        if (date.getDayOfWeek().equals(DayOfWeek.SATURDAY)) {
            return "السبت";
        }
        if (date.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
            return "الاحد";
        }
        if (date.getDayOfWeek().equals(DayOfWeek.MONDAY)) {
            return "الاثنين";
        }
        if (date.getDayOfWeek().equals(DayOfWeek.TUESDAY)) {
            return "الثلاثاء";
        }
        if (date.getDayOfWeek().equals(DayOfWeek.WEDNESDAY)) {
            return "الاربعاء";
        }
        if (date.getDayOfWeek().equals(DayOfWeek.THURSDAY)) {
            return "الخميس";
        }
        return null;

    }
}
