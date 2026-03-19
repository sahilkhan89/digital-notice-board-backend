package digital_board.digital_board.Dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupResponseDto {

    private String _id;
    private String email;
    private boolean email_verified;

    // Getters and setters
}
