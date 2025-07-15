package team.three.usedstroller.api.rental.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team.three.usedstroller.api.common.domain.BaseTimeEntity;
import team.three.usedstroller.api.product.domain.Model;
import team.three.usedstroller.api.users.entity.Account;

@Getter
@Entity
@Builder
@AllArgsConstructor
@Table(name = "rental")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RentalContractEntity extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "rental_id")
  private RentalEntity rental;

  private String name;
  private String phone;
  private LocalDateTime rentalStart;
  private LocalDateTime rentalEnd;
  private LocalDateTime receiveDate;
  private Long amount;
  private Long period;
  private Long deposit;
  private String status;
  private String memo;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "account_id")
  private Account account;
}
