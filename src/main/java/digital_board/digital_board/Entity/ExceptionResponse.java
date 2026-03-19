package digital_board.digital_board.Entity;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionResponse {
    @Id
    private int exceptionId;
    private String ExceptonName;
    private String massage;
}
