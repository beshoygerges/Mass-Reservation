package com.stmgalex.reservation.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


@NoArgsConstructor
@Data
@Entity
@Table(name = "[Reservation]")
public class MassReservation implements Serializable {

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

  public MassReservation(User user, Mass mass) {
    this.user = user;
    this.mass = mass;
  }
}
