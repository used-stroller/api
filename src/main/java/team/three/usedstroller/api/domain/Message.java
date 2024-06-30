package team.three.usedstroller.api.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column
  @Enumerated(EnumType.STRING)
  private MessageType messageType;

  @Column
  private String room;

  @Column
  private String username;

  @Column
  private String message;

  @Column
  private LocalDateTime createdAt;

  public enum MessageType {
    SERVER, CLIENT
  }
}
