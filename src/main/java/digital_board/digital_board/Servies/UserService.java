package digital_board.digital_board.Servies;


import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import digital_board.digital_board.Dto.UserDTO;
import digital_board.digital_board.Entity.User;
import digital_board.digital_board.Entity.UserNotification;

public interface UserService {
    
    // create user
    User CreateUser(User user);

    // Update User
    User UpdateUser(User user) throws IOException;
    
    // Find All User
    Page<User> FindAllUser(Pageable pageable);

    User getUserByEmail(String email);

   List<UserDTO> getInfoOfAdmins();
  
//    get admin by searching
   public List<User> getAdminBySearching(String userName);
}
