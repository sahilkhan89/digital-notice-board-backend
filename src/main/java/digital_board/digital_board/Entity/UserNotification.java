package digital_board.digital_board.Entity;


import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserNotification {
   @Id
   private String id = UUID.randomUUID().toString();
   private String userName;
   private String userEmail;
   private String departmentName;
   private Boolean status;

}
