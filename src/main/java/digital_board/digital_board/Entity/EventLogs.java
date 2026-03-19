package digital_board.digital_board.Entity;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

// import javax.persistence.Entity;
// import javax.persistence.Id;

@Entity
@Getter
@Setter
@Table(name = "event_logs")
public class EventLogs {
  @Id
  // private String id;
  private String eventdate;
  private String level;
  private String logger;
  private String useremail;
  private String path;
  private String message;
  private String exception;
}
