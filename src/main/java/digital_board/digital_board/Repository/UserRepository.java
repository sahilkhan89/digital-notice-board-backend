package digital_board.digital_board.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import digital_board.digital_board.Dto.UserDTO;

// import org.springframework.data.jpa.repository.JpaRepository;

import digital_board.digital_board.Entity.User;
import digital_board.digital_board.Entity.UserNotification;

public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email);

     @Query("Select u from User u Where u.email =:email")
    User getbyemail(@Param("email") String email);
    
    List<User> findByStatusIgnoreCase(String status);

    Page<User> findAllByRoleAndStatus(String role, String status,Pageable pageable);

    // @Query("SELECT u.id,u.email,u.userName FROM User u")
    @Query("SELECT new digital_board.digital_board.Dto.UserDTO(u.id, u.email, u.userName) FROM User u where u.status != 'disable'")
    List<UserDTO> findUserNames();

    // get admin by searching
    @Query("SELECT u FROM User u WHERE LOWER(u.userName) LIKE LOWER(CONCAT('%', :username, '%')) OR LOWER(u.email) LIKE LOWER(CONCAT('%', :username, '%'))")
    List<User> findAdminByNameOrEmail(@Param("username") String username);


}
