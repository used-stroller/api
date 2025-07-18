package team.three.usedstroller.api.rental.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team.three.usedstroller.api.common.domain.BaseTimeEntity;
import team.three.usedstroller.api.users.entity.Account;

@Getter
@Entity
@Builder
@AllArgsConstructor
@Table(name = "rental_contract_info")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RentalContractInfoEntity extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;
  private String phone;
  private LocalDate rentalStart;
  private LocalDate rentalEnd;
  private LocalDate receiveDate;
  private Long amount;
  private Long period;
  private Long deposit;
  private String status;
  private String memo;
  private boolean deleted;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "rental_id")
  private RentalEntity rental;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "account_id")
  private Account account;
}
