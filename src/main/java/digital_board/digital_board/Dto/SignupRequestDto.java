package digital_board.digital_board.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDto {
   
    private String userName;
    private String email;
    private String departmentName;
    private String category;

  
    private String createdBy;
    private String address;
    private String contact;

}
