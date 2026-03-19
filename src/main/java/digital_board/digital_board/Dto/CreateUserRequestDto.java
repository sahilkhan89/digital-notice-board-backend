package digital_board.digital_board.Dto;



import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserRequestDto {
  
        private String clientId;
        private String email;
        private String password;
        private String connection;
    
        public CreateUserRequestDto(String clientId, String email, String password, String connection) {
            this.clientId = clientId;
            this.email = email;
            this.password = password;
            this.connection = connection;
        }
    
      
}
