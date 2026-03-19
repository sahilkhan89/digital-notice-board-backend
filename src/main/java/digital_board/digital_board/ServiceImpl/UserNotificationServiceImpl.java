package digital_board.digital_board.ServiceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import digital_board.digital_board.Entity.ExceptionResponse;
import digital_board.digital_board.Entity.UserNotification;
import digital_board.digital_board.Exception.ResourceNotFoundException;
import digital_board.digital_board.Repository.UserNotificationRepository;
import digital_board.digital_board.Servies.UserNotificationService;
import digital_board.digital_board.constants.ResponseMessagesConstants;

@Service
public class UserNotificationServiceImpl implements UserNotificationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserNotificationServiceImpl.class);
    @Autowired
    UserNotificationRepository userNotificationRepository;

    @Override
    public Map<String, Object> createNotificationByUser(UserNotification userNotification) {
        LOGGER.info("Start UserNotificationServiceImpl: createNotificationByUser method");
        UserNotification user = userNotificationRepository.getbyemail(userNotification.getUserEmail());
        Map<String, Object> response = new HashMap<>();
        try {

            if (user == null) {
                UserNotification save = this.userNotificationRepository.save(userNotification);
                String succesmessage = ResponseMessagesConstants.messagelist.stream()
                        .filter(exceptionResponse -> "NOTIFICATION_CREATED_SUCCESSFULLY".equals(exceptionResponse.getExceptonName()))
                        .map(ExceptionResponse::getMassage)
                        .findFirst()
                        .orElse("Default failure message if not found");

                response.put("message", succesmessage);
                response.put("data", save);
                return response;

            }
            String conflictMessage = ResponseMessagesConstants.messagelist.stream()
                    .filter(exceptionResponse -> "EMAIL_ALREADY_EXISTS".equals(exceptionResponse.getExceptonName()))
                    .map(ExceptionResponse::getMassage)
                    .findFirst()
                    .orElse("Default failure message if not found");
            response.put("message", conflictMessage);
            return response;

      } catch (Exception e) {
    throw new ResourceNotFoundException(ResponseMessagesConstants.messagelist.stream()
            .filter(exceptionResponse -> "FAILED_TO_CREATE_NOTIFICATION".equals(exceptionResponse.getExceptonName()))
            .map(ExceptionResponse::getMassage)
            .findFirst()
            .orElse("Default failure message if not found")); 
}
        //  return response;
    }

    @Override
    public List<UserNotification> getAllUserNotification() {
        LOGGER.info("Start UserNotificationServiceImpl: getAllUserNotification method");
        List<UserNotification> userNotification = this.userNotificationRepository.findAll();
        LOGGER.info("End UserNotificationServiceImpl: getAllUserNotification method");
        return userNotification;

    }

    @Override
    public List<UserNotification> getUserNotificationByDepartment() {
        LOGGER.info("Start UserNotificationServiceImpl: getUserNotificationByDepartment method");
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getUserNotificationByDepartment'");
    }

    @Override
    public List<UserNotification> getUserByFilter(String userName) {
        
        return userNotificationRepository.findByUserNameOrEmail(userName);
    }

   

}
