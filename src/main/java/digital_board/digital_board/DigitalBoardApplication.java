package digital_board.digital_board;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.google.protobuf.Timestamp;

import org.springframework.boot.CommandLineRunner;
import digital_board.digital_board.Entity.ExceptionResponse;
import digital_board.digital_board.Repository.NoticeRepository;
import digital_board.digital_board.ServiceImpl.ExceptionResponseServiceImpl;
import digital_board.digital_board.constants.ResponseMessagesBackup;
import digital_board.digital_board.constants.ResponseMessagesConstants;


@SpringBootApplication
@EnableScheduling
public class DigitalBoardApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(DigitalBoardApplication.class, args);
        System.out.println("ok");
    }

    @Autowired
    ExceptionResponseServiceImpl exceptionResponseServiceImpl;
    @Autowired
    ResponseMessagesBackup responseMessagesBackup;

    @Override
    public void run(String... args) throws Exception {
        ResponseMessagesConstants.messagelist = this.exceptionResponseServiceImpl.GetAllMassage();
        String specificMessage = ResponseMessagesConstants.messagelist.stream()
                .filter(exceptionResponse -> "NOTICE_CREATE_FAILURE".equals(exceptionResponse.getExceptonName()))
                .map(ExceptionResponse::getMassage)
                .findFirst()
                .orElse("Default message if not found");
        System.out.println("Specific Message: " + specificMessage);
        if (ResponseMessagesConstants.messagelist.isEmpty()) {
            responseMessagesBackup.smsResponseService();
        }


    }

}