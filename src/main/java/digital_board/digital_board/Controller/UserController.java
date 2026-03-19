package digital_board.digital_board.Controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import digital_board.digital_board.Dto.AuthResponse;
import digital_board.digital_board.Dto.SignupRequestDto;
import digital_board.digital_board.Dto.SignupResponseDto;
import digital_board.digital_board.Dto.UserDTO;
import digital_board.digital_board.Entity.ExceptionResponse;
import digital_board.digital_board.Entity.User;
import digital_board.digital_board.ServiceImpl.EmailServiceImpl;
import digital_board.digital_board.ServiceImpl.UserServiceImpl;
import digital_board.digital_board.Servies.Auth0Service;
import digital_board.digital_board.constants.ResponseMessagesConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/user")
public class UserController {
  private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

  @Autowired
  private UserServiceImpl userServiceImpl;

  @Autowired
  private Auth0Service auth0Service;

  @Autowired
  EmailServiceImpl emailServices;

  // @GetMapping("/test")
  // public ResponseEntity<AuthResponse> home(@AuthenticationPrincipal OidcUser
  // principal) {
  // AuthResponse authResponse = new AuthResponse();
  // authResponse.setName(principal.getEmail());
  // authResponse.setToken(principal.getIdToken().getTokenValue());
  // return ResponseEntity.ok(authResponse);
  // }

  @GetMapping("/public")
  public String publicTest() {
    LOGGER.info("Start User Controller : public method");

    MDC.put("useremail", "mashid@gmail.com");
    MDC.put("path", "/public");
    LOGGER.info("Welcome action called..");
    MDC.clear();
    LOGGER.info("End User Controller : public method");

    return "working";
  }

  @GetMapping("/public/mailtest")
  public String publicMailTest() {
    LOGGER.info("Start User Controller : public mail test method");

    emailServices.sendSimpleMessageForPassword("sahilkhanskkhan4@gmail.com", "test", "password");

    LOGGER.info("End User Controller : public mail test method");

    return "working";
  }

  @PostMapping("/signup")
  public ResponseEntity<Map<String, Object>> signUp(@RequestBody SignupRequestDto signupRequestDto) {
    LOGGER.info("Start User Controller : signUp method");
    Map<String, Object> response = new HashMap<>();
    try {
      SignupResponseDto signupResponseDto = auth0Service.signUp(signupRequestDto);

      String successMessage = ResponseMessagesConstants.messagelist.stream()
          .filter(exceptionResponse -> "USER_CREATE_SUCCESS".equals(exceptionResponse.getExceptonName()))
          .map(ExceptionResponse::getMassage)
          .findFirst()
          .orElse("Default success message if not found");

      response.put("message", successMessage);
      response.put("data", signupResponseDto);
      MDC.put("useremail", signupRequestDto.getCreatedBy());
      MDC.put("path", "/user/signup");
      LOGGER.info("User Controller : signUp method");
      MDC.clear();
      LOGGER.info("End User Controller : signUp method");
      return ResponseEntity.ok(response);
    } catch (Exception e) {
      String failureMessage = ResponseMessagesConstants.messagelist.stream()
          .filter(exceptionResponse -> "USER_CREATE_FAILURE".equals(exceptionResponse.getExceptonName()))
          .map(ExceptionResponse::getMassage)
          .findFirst()
          .orElse("Default failure message if not found");

      response.put("message", failureMessage);
      LOGGER.info("End User Controller : signUp method");
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

  }

  // UpdateUser
  @PutMapping("/update")
  public ResponseEntity<Map<String, Object>> updateUser(@RequestBody User user)
      throws IOException {
    LOGGER.info("Start User Controller : updateUser method");
    if (user.getStatus().startsWith("disable")) {

      Map<String, Object> response = new HashMap<>();
      String successMessage = ResponseMessagesConstants.messagelist.stream()
          .filter(exceptionResponse -> "USER_DELETE_SUCCESS".equals(exceptionResponse.getExceptonName()))
          .map(ExceptionResponse::getMassage)
          .findFirst()
          .orElse("Default success message if not found");

      response.put("message", successMessage);
      response.put("user", userServiceImpl.UpdateUser(user));
      MDC.put("useremail", user.getEmail());
      MDC.put("path", "/user/update/delete");
      LOGGER.info("User Controller : delete method");
      MDC.clear();
      LOGGER.info("End User Controller : updateUser method");
      return ResponseEntity.ok(response);
    } else {

      Map<String, Object> response = new HashMap<>();
      response.put("message", ResponseMessagesConstants.messagelist.stream()
          .filter(exceptionResponse -> "USER_UPDATED_SUCCESS".equals(exceptionResponse.getExceptonName()))
          .map(ExceptionResponse::getMassage)
          .findFirst()
          .orElse("Default message if not found"));
      response.put("user", userServiceImpl.UpdateUser(user));
      MDC.put("useremail", user.getEmail());
      MDC.put("path", "/user/update");
      LOGGER.info("User Controller : update method");
      MDC.clear();
      LOGGER.info("End User Controller : updateUser method");
      return ResponseEntity.ok(response);
    }

  }

  // Find All User
  @GetMapping("/FindAllUser")

  public ResponseEntity<Map<String, Object>> findAllUser(
      @RequestParam(required = false, defaultValue = "userName,asc") String sort,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {
    LOGGER.info("Start User Controller : findAllUser method");
    Map<String, Object> response = new HashMap<>();
    Pageable pageable = PageRequest.of(page, size, parseSortString(sort));

    Page<User> userDetails = userServiceImpl.FindAllUser(pageable);

    response.put("count", userDetails.getTotalElements());
    response.put("data", userDetails.getContent());
    if (userDetails.isEmpty()) {
      // Return a JSON response with a message for data not found
      String emptyMessage = ResponseMessagesConstants.messagelist.stream()
          .filter(exceptionResponse -> "LIST_IS_EMPTY".equals(exceptionResponse.getExceptonName()))
          .map(ExceptionResponse::getMassage)
          .findFirst()
          .orElse("Default failure message if not found");

      response.put("message", emptyMessage);
      LOGGER.info("End User Controller : findAllUser method");
      return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    // Return the list of notices if data is found
    LOGGER.info("End User Controller : findAllUser method");
    return ResponseEntity.ok(response);
  }

  @GetMapping("/getByEmail/{email}")
  public ResponseEntity<?> getUserByEmail(@PathVariable String email) {
    LOGGER.info("Start UserController: getUserByEmail method");
    User user = userServiceImpl.getUserByEmail(email);

    if (user == null) {
      return new ResponseEntity<>(ResponseMessagesConstants.messagelist.stream()
          .filter(exceptionResponse -> "LIST_IS_EMPTY".equals(exceptionResponse.getExceptonName()))
          .map(ExceptionResponse::getMassage)
          .findFirst()
          .orElse("Default message if not found"), HttpStatus.OK);
    }
    LOGGER.info("End UserController: getUserByEmail method");
    return ResponseEntity.ok(user);
  }

  @GetMapping("/admin-list")
  public ResponseEntity<Map<String, Object>> getInfoOfAdmins() {
    LOGGER.info("Start UserController: getInfoOfAdmins method");
    List<UserDTO> activeAdminList = userServiceImpl.getInfoOfAdmins();

    Map<String, Object> response = new HashMap<>();
    response.put("count", activeAdminList.size());
    response.put("data", activeAdminList);

    if (activeAdminList.isEmpty()) {
      // Return a JSON response with a message for data not found
      String emptyMessage = ResponseMessagesConstants.messagelist.stream()
          .filter(exceptionResponse -> "LIST_IS_EMPTY".equals(exceptionResponse.getExceptonName()))
          .map(ExceptionResponse::getMassage)
          .findFirst()
          .orElse("Default failure message if not found");

      response.put("message", emptyMessage);
      LOGGER.info("End User Controller : getInfoOfAdmins method");
      return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    LOGGER.info("End User Controller : getInfoOfAdmins method");
    return ResponseEntity.ok(response);

  }

  private Sort parseSortString(String sort) {
    String[] sortParams = sort.split(",");
    if (sortParams.length == 2) {
      Sort.Direction direction = sortParams[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC
          : Sort.Direction.ASC;
      return Sort.by(new Sort.Order(direction, sortParams[0]));
    } else {
      return Sort.by(Sort.Order.asc("userName")); // Default sorting by noticeCreatedDate in ascending
                                                  // order
    }
  }

  // get admin by searching
  @GetMapping("searching/admin/{name}")
  public ResponseEntity<Map<String, Object>> getAdminBySearching(@PathVariable String name) {
    LOGGER.info("Start UserController: getAdminBySearching method");
    List<User> listOfAdmin = userServiceImpl.getAdminBySearching(name);
    Map<String, Object> response = new HashMap<>();
    if (listOfAdmin.isEmpty()) {
      response.put("message", ResponseMessagesConstants.messagelist.stream()
          .filter(exceptionResponse -> "LIST_IS_EMPTY".equals(exceptionResponse.getExceptonName()))
          .map(ExceptionResponse::getMassage)
          .findFirst()
          .orElse("Default failure message if not found"));
      response.put("data", listOfAdmin);
      LOGGER.info("End UserController: getAdminBySearching method");
      return ResponseEntity.ok(response);
    }
    response.put("data", listOfAdmin);
    LOGGER.info("End UserController: getAdminBySearching method");
    return ResponseEntity.ok(response);
  }

}
