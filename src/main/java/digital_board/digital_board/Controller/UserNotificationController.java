package digital_board.digital_board.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import digital_board.digital_board.Entity.UserNotification;
import digital_board.digital_board.ServiceImpl.UserNotificationServiceImpl;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/notification")
public class UserNotificationController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserNotificationController.class);

    @Autowired
    UserNotificationServiceImpl notificationServiceImpl;

    @PostMapping("/create")
    public ResponseEntity<?> createNotificationByUser(@RequestBody UserNotification userNotification) {
        LOGGER.info("Start UserNotificationController Controller : createNotificationByUser method");
        return ResponseEntity.ok(this.notificationServiceImpl.createNotificationByUser(userNotification));
    }

    @GetMapping("/getAll")
    public List<UserNotification> getAllUserNotification() {
        LOGGER.info("Start UserNotificationController Controller : getAllUserNotification method");
        List<UserNotification> userNotification = this.notificationServiceImpl.getAllUserNotification();
        LOGGER.info("Start UserNotificationController Controller : getAllUserNotification method");
        return userNotification;

    }
    @GetMapping("/search/user/{userName}")
    public ResponseEntity<List<UserNotification>> getMethodName(@RequestParam String userName) {
        return ResponseEntity.ok(notificationServiceImpl.getUserByFilter(userName));
    }
    

}
