package digital_board.digital_board.Servies;


import java.util.List;
import java.util.Map;

import digital_board.digital_board.Entity.UserNotification;

public interface UserNotificationService {

    public Map<String,Object> createNotificationByUser(UserNotification userNotification);

    public  List<UserNotification> getAllUserNotification();

    public  List<UserNotification> getUserNotificationByDepartment();

    // get users by filter
    public List<UserNotification> getUserByFilter(String userName);
}
