package digital_board.digital_board.constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import digital_board.digital_board.Entity.ExceptionResponse;
import digital_board.digital_board.Repository.ExceptionResponseRepository;

@Component

public class ResponseMessagesBackup {

    @Autowired
    private ExceptionResponseRepository exceptionResponseRepository;

    public void smsResponseService() {

        List<ExceptionResponse> messagelist = new ArrayList<>();

        messagelist.addAll(Arrays.asList(
            
                new ExceptionResponse(1, "USER_CREATE_SUCCESS", "User Added Successfully"),
                new ExceptionResponse(2, "USER_DELETE_SUCCESS", "User Deleted Sucessfully"),
                new ExceptionResponse(3, "USER_NOT_FOUND", "User Not Found !"),
                new ExceptionResponse(4, "MESSAGE_REGISTER_ERRROR", "User Aready Exists!"),
                new ExceptionResponse(5, "USER_UPDATED_SUCCESS", "User Updated Successfully"),
                new ExceptionResponse(6, "LIST_IS_EMPTY", "Data Not Found !"),
                new ExceptionResponse(7, "NOTICE_CREATE_SUCCESS", "Notice Added Successfully"),
                new ExceptionResponse(8, "NOTICE_DELETE_SUCCESS", "Notice Deleted Sucessfully"),
                new ExceptionResponse(9, "NOTICE_NOT_FOUND", "Notice Not Found !"),
                new ExceptionResponse(10, "NOTICE_UPDATED_SUCCESS", "Notice Updated Successfully"),
                new ExceptionResponse(11, "SPORT_CREATE_SUCCESS", "Sport Added Successfully"),
                new ExceptionResponse(12, "SPORT_DELETE_SUCCESS", "Sport Deleted Sucessfully"),
                new ExceptionResponse(13, "SPORT_NOT_FOUND", "Sport Not Found !"),
                new ExceptionResponse(14, "SPORT_UPDATED_SUCCESS", "Sport Updated Successfully"),
                new ExceptionResponse(15, "EMAIL_SUCCESS", "Email Send Suceessfully"),
                new ExceptionResponse(16, "EMAIL_ERROR", "Something Went Wrong!"),
                new ExceptionResponse(17, "USER_EMAIL_SUCCESS", "Your Email Id Added Successfully"),
                 new ExceptionResponse(18, "EVENT_CREATE_SUCCESS", "Event Added Successfully"),
                new ExceptionResponse(19, "EVENT_DELETE_SUCCESS", "Event Deleted Sucessfully"),
                new ExceptionResponse(20, "EVENT_NOT_FOUND", "Event Not Found !"),
                new ExceptionResponse(21, "EVENT_UPDATED_SUCCESS", "Event Updated Successfully"),
                 new ExceptionResponse(22, "NOTICE_CREATE_FAILURE", "Failed to create notice. Please try again later.!"),
                 new ExceptionResponse(23, "NOTICE_UPDATE_FAILURE", "Notice update Failure!"),
                  new ExceptionResponse(24, "NOT_SUPERADMIN", "User Must be Super Admin!"),
                  new ExceptionResponse(25, "NOTIFICATION_CREATED_SUCCESSFULLY", "Email Notification Added successfully"),
                  new ExceptionResponse(26, "FAILED_TO_CREATE_NOTIFICATION", "Failed to Add Email Notification!"),
                 new ExceptionResponse(27, "EMAIL_ALREADY_EXISTS", "User with this email already exists"),
                 new ExceptionResponse(28, "UNAUTHORIZED_ACCESS", "To access this API, please log in with valid credentials."),
                 new ExceptionResponse(29, "TODAY_NOTICE", "Notices created today.."),
                 new ExceptionResponse(30, "TODAY_NOTICE_NOT_FOUND", "No notices were created today..")

        ));//NOTICE_CREATE_FAILURE

        exceptionResponseRepository.saveAll(messagelist);
    }

}