package digital_board.digital_board.Exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResourceNotFoundException extends RuntimeException {
    String message;
    public ResourceNotFoundException(String message) {
        this.message = message;
    }
   

}
