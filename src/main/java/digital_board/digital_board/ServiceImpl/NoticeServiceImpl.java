package digital_board.digital_board.ServiceImpl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import digital_board.digital_board.Dto.CategoryNoticeDto;
import digital_board.digital_board.Dto.NoticeDto;
import digital_board.digital_board.Entity.ExceptionResponse;
import digital_board.digital_board.Entity.Notice;
import digital_board.digital_board.Entity.UserNotification;
import digital_board.digital_board.Exception.ResourceNotFoundException;
import digital_board.digital_board.Repository.NoticeRepository;
import digital_board.digital_board.Servies.NoticeService;
import digital_board.digital_board.constants.ResponseMessagesConstants;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.concurrent.CompletableFuture;

@Service
public class NoticeServiceImpl implements NoticeService {

  private static final Logger LOGGER = LoggerFactory.getLogger(
      NoticeServiceImpl.class);

  @Autowired
  NoticeRepository noticeRepository;

  @Autowired
  private EmailServiceImpl emailServices;

  @Autowired
  UserNotificationServiceImpl notificationServiceImpl;

  @Autowired
  private Cloudinary cloudinary;

  // @Override
  // public Notice createNoticeByUser(Notice notice) {
  // LOGGER.info("Start NoticeServiceImpl: createNoticeByUser method");
  // List<String> base64ImageStrings = notice.getImages_url();
  // LOGGER.info("Start NoticeServiceImpl: createNoticeByUser method ");
  // List<String> listofdata = new ArrayList<>();
  // if (base64ImageStrings != null) {
  // try {
  // for (String base64Image : base64ImageStrings) {
  // Map uploadResult =
  // this.cloudinary.uploader()
  // .upload(base64Image, ObjectUtils.emptyMap());
  // String imageUrl = (String) uploadResult.get("url");
  // listofdata.add(imageUrl);
  // }
  // } catch (IOException e) {
  // e.printStackTrace();
  // }
  // }
  // notice.setImages_url(listofdata);
  // Notice saveNotice = this.noticeRepository.save(notice);
  // try {
  // List<UserNotification> userNotification =
  // this.notificationServiceImpl.getAllUserNotification();

  // for (UserNotification user : userNotification) {
  // emailServices.sendSimpleMessage(
  // user.getUserEmail(),
  // "New Notice",
  // user.getUserName()
  // );
  // }
  // } catch (Exception e) {
  // LOGGER.info(
  // "End NoticeServiceImpl: createNoticeByUser method ! mail sending error"
  // );
  // }

  // LOGGER.info("End NoticeServiceImpl: createNoticeByUser method");
  // return saveNotice;
  // }

  @Async
  public void sendEmailNotifications(List<UserNotification> userNotifications) {
    for (UserNotification user : userNotifications) {
      emailServices.sendSimpleMessage(
          user.getUserEmail(),
          "New Notice",
          user.getUserName());
    }
  }

  @Override
  public Notice createNoticeByUser(Notice notice) {
    LOGGER.info("Start NoticeServiceImpl: createNoticeByUser method");
    List<String> base64ImageStrings = notice.getImages_url();
    LOGGER.info("Start NoticeServiceImpl: createNoticeByUser method ");
    List<String> listofdata = new ArrayList<>();
    if (base64ImageStrings != null) {
      try {
        for (String base64Image : base64ImageStrings) {
          Map uploadResult = this.cloudinary.uploader()
              .upload(base64Image, ObjectUtils.emptyMap());
          String imageUrl = (String) uploadResult.get("url");
          listofdata.add(imageUrl);
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    notice.setImages_url(listofdata);
    Notice saveNotice = this.noticeRepository.save(notice);
    CompletableFuture.runAsync(() -> {
      try {
        List<UserNotification> userNotification = this.notificationServiceImpl.getAllUserNotification();

        // Send email notifications asynchronously
        sendEmailNotifications(userNotification);
      } catch (Exception e) {
        LOGGER.info("Mail sending error", e);
      }
    });
    LOGGER.info("End NoticeServiceImpl: createNoticeByUser method");
    return saveNotice;
  }

  @Override
  public Notice getNoticeByNoticeId(String noticeId) {
    LOGGER.info("Start NoticeServiceImpl: getNoticeByNoticeId method");
    LOGGER.info("End NoticeServiceImpl: getNoticeByNoticeId method");
    return this.noticeRepository.findById(noticeId)
        .orElseThrow(() -> new ResourceNotFoundException(
            ResponseMessagesConstants.messagelist
                .stream()
                .filter(exceptionResponse -> "NOTICE_NOT_FOUND".equals(exceptionResponse.getExceptonName()))
                .map(ExceptionResponse::getMassage)
                .findFirst()
                .orElse("Default message if not found")));
  }

  @Override
  public Page<Notice> getNoticeByUserEmail(String email, Pageable pageable) {
    LOGGER.info("Start NoticeServiceImpl: getNoticeByUserEmail method");
    LOGGER.info("End NoticeServiceImpl: getNoticeByUserEmail method");
    return this.noticeRepository.getAllNoticeByUserId(email, pageable);
  }

  @Override
  public List<Notice> getAllNotice() {
    LOGGER.info("Start NoticeServiceImpl: getAllNotice method");
    List<Notice> notice = this.noticeRepository.findAll();
    LOGGER.info("End NoticeServiceImpl: getAllNotice method");
    return notice;
  }

  @Override
  public Page<Notice> getAllNoticesSorted(Pageable pageable) {
    LOGGER.info("Start NoticeServiceImpl: getAllNoticesSorted method");
    LOGGER.info("End NoticeServiceImpl: getAllNoticesSorted method");
    return noticeRepository.findAll(pageable);
  }

  @Override
  public Page<Notice> getNoticesByCategory(
      List<String> category,
      List<String> department,
      Pageable pageable) {
    LOGGER.info("Start NoticeServiceImpl: getNoticesByCategory method");
    LOGGER.info("End NoticeServiceImpl: getNoticesByCategory method");
    return noticeRepository.findByCategoryInDepartmentNameInAndStatusNotDisable(
        category,
        department,
        pageable);
  }

  @Override
  public Page<Notice> getNoticesByDepartment(
      List<String> departmentName,
      List<String> categories,
      Pageable pageable) {
    LOGGER.info("Start NoticeServiceImpl: getNoticesByCategory method");
    if (departmentName != null && departmentName.contains("All")) {
      LOGGER.info("End NoticeServiceImpl: getNoticesByCategory method");
      return null;
    } else {
      LOGGER.info("End NoticeServiceImpl: getNoticesByCategory method");
      if (departmentName.contains("Iteg") ||
          departmentName.contains("Meg") ||
          departmentName.contains("Beg")) {
        departmentName.addAll(Arrays.asList("All"));
      }
      return noticeRepository.findByDepartmentNameInANDcategoriesInAndStatusNotDisable(
          departmentName,
          categories,
          pageable);
    }
  }

  @Override
  public Long getTotalNoticeCount() {
    LOGGER.info("Start NoticeServiceImpl: getTotalNoticeCount method");
    LOGGER.info("End NoticeServiceImpl: getTotalNoticeCount method");
    return noticeRepository.count();
  }

  @Override
  public Page<Notice> searchNotices(String query, Pageable pageable) {
    List<String> status = new ArrayList<>();
    status.add("enable");
    status.add("important");
    return noticeRepository.findByNoticeTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
        query,
        query,
        pageable);
  }

  @Override
  public List<Notice> getAllImportantNotice(int limit) {
    LOGGER.info("Start NoticeServiceImpl: getAllImportantNotice method");
    List<Notice> findNoticesWithLimit = noticeRepository.findNoticesWithLimit(
        limit,
        "important");

    if (findNoticesWithLimit.isEmpty()) {
      throw new ResourceNotFoundException(
          ResponseMessagesConstants.messagelist
              .stream()
              .filter(exceptionResponse -> "LIST_IS_EMPTY".equals(exceptionResponse.getExceptonName()))
              .map(ExceptionResponse::getMassage)
              .findFirst()
              .orElse("Default message if not found"));
    } else {
      LOGGER.info("End NoticeServiceImpl: getAllImportantNotice method");
      return findNoticesWithLimit;
    }
  }

  @Override
  public Notice updateNotice(Notice notice) {
    LOGGER.info("Start NoticeServiceImpl: updateNotice method");
    this.noticeRepository.findById(notice.getNoticeId())
        .orElseThrow(() -> new ResourceNotFoundException(
            ResponseMessagesConstants.messagelist
                .stream()
                .filter(exceptionResponse -> "NOTICE_NOT_FOUND".equals(exceptionResponse.getExceptonName()))
                .map(ExceptionResponse::getMassage)
                .findFirst()
                .orElse("Default message if not found")));
    List<String> listofdata = new ArrayList<>();
    List<String> base64ImageStrings = notice.getImages_url();
    if (base64ImageStrings != null) {
      try {
        for (String base64Image : base64ImageStrings) {

          if (!base64Image.startsWith("https://res.cloudinary.com")) {
            Map uploadResult = this.cloudinary.uploader().upload(base64Image,
                ObjectUtils.emptyMap());
            String imageUrl = (String) uploadResult.get("url");
            listofdata.add(imageUrl);
          }
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    notice.setImages_url(listofdata);
    Notice saveNotice = this.noticeRepository.save(notice);
    LOGGER.info("End NoticeServiceImpl: updateNotice method");
    return saveNotice;

  }

  // searching filter

  public Map<String, Object> filterNotices(
      List<String> department,
      List<String> categories,
      List<String> admins,
      String status,
      int page,
      int size) {
    LOGGER.info("Start NoticeServiceImpl: filterNotices method");

    if (department == null && categories == null) {
      List<Notice> findAllNotDisabled = noticeRepository.findAllNotDisabled();

      if (status == null && admins == null) {
        int startIndex = page * size;
        int endIndex = Math.min(startIndex + size, findAllNotDisabled.size());
        if (startIndex > endIndex) {
          Map<String, Object> response = new HashMap<>();
          response.put("data", Collections.emptyList());
          response.put("count", findAllNotDisabled.size());
          LOGGER.info("End NoticeServiceImpl: filterNotices method");
          return response;
        }
        Map<String, Object> response = new HashMap<>();
        response.put("data", findAllNotDisabled.subList(startIndex, endIndex));
        response.put("count", findAllNotDisabled.size());
        LOGGER.info("End NoticeServiceImpl: filterNotices method");
        return response;
      } else {
        if (status != null && admins != null) {
          List<Notice> findAllNotDisabled2 = findAllNotDisabled
              .stream()
              .filter(notice -> (status != null && status.equals(notice.getStatus())) &&
                  (admins != null && admins.contains(notice.getCreatedBy())))
              .collect(Collectors.toList());
          int startIndex = page * size;
          int endIndex = Math.min(
              startIndex + size,
              findAllNotDisabled2.size());
          if (startIndex > endIndex) {
            Map<String, Object> response = new HashMap<>();
            response.put("data", Collections.emptyList());

            response.put("count", findAllNotDisabled2.size());
            LOGGER.info("End NoticeServiceImpl: filterNotices method");
            return response;
          }
          Map<String, Object> response = new HashMap<>();
          response.put(
              "data",
              findAllNotDisabled2.subList(startIndex, endIndex));
          response.put("count", findAllNotDisabled2.size());
          LOGGER.info("End NoticeServiceImpl: filterNotices method");
          return response;
        } else {
          List<Notice> findAllNotDisabled3 = findAllNotDisabled
              .stream()
              .filter(notice -> (status != null && status.equals(notice.getStatus())) ||
                  (admins != null && admins.contains(notice.getCreatedBy())))
              .collect(Collectors.toList());

          int startIndex = page * size;
          int endIndex = Math.min(
              startIndex + size,
              findAllNotDisabled3.size());
          if (startIndex > endIndex) {
            Map<String, Object> response = new HashMap<>();
            response.put("data", Collections.emptyList());

            response.put("count", findAllNotDisabled3.size());
            LOGGER.info("End NoticeServiceImpl: filterNotices method");
            return response;
          }
          Map<String, Object> response = new HashMap<>();
          response.put(
              "data",
              findAllNotDisabled3.subList(startIndex, endIndex));
          response.put("count", findAllNotDisabled3.size());
          LOGGER.info("End NoticeServiceImpl: filterNotices method");
          return response;
        }
      }
    } else {
      List<Notice> findByCreatedByInAndStatusNotDisable = noticeRepository.findBycategoriesInAndStatusNotDisable(
          categories);

      List<Notice> findByDepartmentAndStatusNotDisabled = noticeRepository.findByDepartmentAndStatusNotDisabled(
          department);
      List<Notice> finalListofData = new ArrayList<>();
      finalListofData.addAll(findByCreatedByInAndStatusNotDisable);
      finalListofData.addAll(findByDepartmentAndStatusNotDisabled);
      if (status == null && admins == null) {
        int startIndex = page * size;
        int endIndex = Math.min(startIndex + size, finalListofData.size());
        if (startIndex > endIndex) {
          Map<String, Object> response = new HashMap<>();
          response.put("data", Collections.emptyList());
          response.put("count", finalListofData.size());
          LOGGER.info("End NoticeServiceImpl: filterNotices method");
          return response;
        }
        Map<String, Object> response = new HashMap<>();
        response.put("data", finalListofData.subList(startIndex, endIndex));
        response.put("count", finalListofData.size());
        LOGGER.info("End NoticeServiceImpl: filterNotices method");
        return response;
      } else {
        if (status != null && admins != null) {
          List<Notice> findAllNotDisabled2 = finalListofData
              .stream()
              .filter(notice -> (status != null && status.equals(notice.getStatus())) &&
                  (admins != null && admins.contains(notice.getCreatedBy())))
              .collect(Collectors.toList());
          int startIndex = page * size;
          int endIndex = Math.min(
              startIndex + size,
              findAllNotDisabled2.size());
          if (startIndex > endIndex) {
            Map<String, Object> response = new HashMap<>();
            response.put("data", Collections.emptyList());
            response.put("count", findAllNotDisabled2.size());
            LOGGER.info("End NoticeServiceImpl: filterNotices method");
            return response;
          }
          Map<String, Object> response = new HashMap<>();
          response.put(
              "data",
              findAllNotDisabled2.subList(startIndex, endIndex));
          response.put("count", findAllNotDisabled2.size());
          LOGGER.info("End NoticeServiceImpl: filterNotices method");
          return response;
        } else {
          List<Notice> findAllNotDisabled3 = finalListofData
              .stream()
              .filter(notice -> (status != null && status.equals(notice.getStatus())) ||
                  (admins != null && admins.contains(notice.getCreatedBy())))
              .collect(Collectors.toList());
          int startIndex = page * size;
          int endIndex = Math.min(
              startIndex + size,
              findAllNotDisabled3.size());
          if (startIndex > endIndex) {
            Map<String, Object> response = new HashMap<>();
            response.put("data", Collections.emptyList());
            response.put("count", findAllNotDisabled3.size());
            LOGGER.info("End NoticeServiceImpl: filterNotices method");
            return response;
          }
          Map<String, Object> response = new HashMap<>();
          response.put(
              "data",
              findAllNotDisabled3.subList(startIndex, endIndex));
          response.put("count", findAllNotDisabled3.size());
          LOGGER.info("End NoticeServiceImpl: filterNotices method");
          return response;
        }
      }
    }
  }

  public Long countByCategory(String category) {
    LOGGER.info("Start NoticeServiceImpl: countByCategory method");
    LOGGER.info("End NoticeServiceImpl: countByCategory method");
    return noticeRepository.countByCategory(category);
  }

  public Long countByDepartmentName(String departmentName) {
    LOGGER.info("Start NoticeServiceImpl: countByDepartmentName method");
    LOGGER.info("End NoticeServiceImpl: countByDepartmentName method");
    return noticeRepository.countByDepartmentName(departmentName);
  }

  // get important notice by limit
  @Override
  public List<Notice> noticefindByStatusImportant(Sort sort, int limit) {
    return noticeRepository.findByImportantTrueAndStatusIs(
        "enable",
        sort,
        PageRequest.of(0, limit));
  }

  @Override
  public Page<Notice> getAllNoticesByfilter(
      List<String> categories,
      List<String> departmentNames,
      List<String> createdBy,
      String status,
      Pageable pageable) {
    LOGGER.info("Start NoticeServiceImpl: getAllNoticesByfilter method");
    // Handle the scenario where "DepartmentName = All"
    if (departmentNames != null) {
      if (departmentNames.contains("Iteg") ||
          departmentNames.contains("Meg") ||
          departmentNames.contains("Beg")) {
        departmentNames.addAll(Arrays.asList("All"));
      }
    }
    if ("false".equals(status) || status == null) {
      LOGGER.info("End NoticeServiceImpl: getAllNoticesByfilter method");
      return noticeRepository.findByCategoryInAndDepartmentNameInAndAndCreatedByIn(
          categories,
          departmentNames,
          createdBy,
          pageable);
    } else {
      LOGGER.info("End NoticeServiceImpl: getAllNoticesByfilter method");
      return noticeRepository.findByCategoryInAndDepartmentNameInAndStatusInAndCreatedByInAndImportant(
          categories,
          departmentNames,
          createdBy,
          pageable);
    }
  }

  // today created notice count
  @Override
  public List<Notice> todayCreatedNoticeCount() {
    return null;

    // return noticeRepository.findByNoticeCreatedDateIsCurrentDate();
  }

  // {http://res.cloudinary.com/dkbdo9top/image/upload/v1702543503/etfonrrzdhazanffusj6.png,http://res.cloudinary.com/dkbdo9top/image/upload/v1702543505/yxnlttu9ejjcs5g6igvv.png}
  @Override
  public List<NoticeDto> getCountAllEnableDepartmentNotices() {
    return noticeRepository.countAllEnableDepartmentNotices();
  }

  @Override
  public List<CategoryNoticeDto> getCountAllEnableCategoryNotices() {
    return noticeRepository.countAllEnableCategoryNotices();
  }

  // schedule by end date
  public List<Notice> getAllNoticeByScheduling() {
    return null;
    // return noticeRepository.findByNoticeEndDateAfterOrEqual();
  }

  @Override
  public List<Map<String, Object>> getLast7DaysCount() {
    LOGGER.info("Start NoticeServiceImpl: getLast7DaysCount method");
    List<Map<String, Object>> last7DaysDataList = new ArrayList<>();
    LocalDate currentDate = LocalDate.now();

    for (int i = 6; i >= 0; i--) {
      LocalDate date = currentDate.minusDays(i);
      String dayOfWeek = date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault());

      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
      String formattedDate = date.format(formatter);

      Map<String, Object> dayData = new HashMap<>();
      dayData.put("DAY", dayOfWeek);
      dayData.put("DATE", formattedDate);
      dayData.put("MEG", noticeRepository.findByDepartmentNameCustomQuery(date, "Meg").size()
          + noticeRepository.findByDepartmentNameCustomQuery(date, "All").size());
      dayData.put("BEG", noticeRepository.findByDepartmentNameCustomQuery(date, "Beg").size()
          + noticeRepository.findByDepartmentNameCustomQuery(date, "All").size());
      dayData.put("ITEG", noticeRepository.findByDepartmentNameCustomQuery(date, "Iteg").size()
          + noticeRepository.findByDepartmentNameCustomQuery(date, "All").size());
      dayData.put("Account", noticeRepository.findByDepartmentNameCustomQuery(date, "Accounts").size());
      last7DaysDataList.add(dayData);
    }
    LOGGER.info("End NoticeServiceImpl: getLast7DaysCount method");
    return last7DaysDataList;
  }

  @Override
  public List<Map<String, Object>> getnoticesByCategory() {
    List<Map<String, Object>> response = new ArrayList<>();
    Map<String, Object> noticeData = new HashMap<>();

    noticeData.put("Department", "Excellence Group");
    noticeData.put("Event", noticeRepository.findByCategoryName("Event").size());
    noticeData.put("Sport", noticeRepository.findByCategoryName("Sport").size());
    noticeData.put("Interview", noticeRepository.findByCategoryName("Interview").size());
    noticeData.put("Exam", noticeRepository.findByCategoryName("Exam").size());
    noticeData.put("Placement", noticeRepository.findByCategoryName("Placement").size());
    noticeData.put("Guest", noticeRepository.findByCategoryName("Guest").size());
    noticeData.put("Result", noticeRepository.findByCategoryName("Result").size());

    response.add(noticeData);

    Map<String, Object> noticeAccount = new HashMap<>();
    noticeAccount.put("Department", "Account");
    noticeAccount.put("NOC", noticeRepository.findByCategoryName("Result").size());
    noticeAccount.put("Enrollment_Form", noticeRepository.findByCategoryName("Result").size());
    noticeAccount.put("Document", noticeRepository.findByCategoryName("Result").size());
    noticeAccount.put("Scholership", noticeRepository.findByCategoryName("Result").size());

    response.add(noticeAccount);
    return response;
  }

  @Override
  public List<NoticeDto> getFindNoticeCountsByDepartmentForSuperAdmin() {
    return noticeRepository.findNoticeCountsByDepartmentForSuperAdmin();
  }

  @Override
  public Page<Notice> getAllNoticeStatusDraftByUserEmail(String email, Pageable pageable) {
    // TODO Auto-generated method stub
    // throw new UnsupportedOperationException("Unimplemented method 'getAllNoticeStatusDraftByUserEmail'");
    LOGGER.info("Start NoticeServiceImpl: getAllNoticeStatusDraftByUserEmail method");
    LOGGER.info("End NoticeServiceImpl: getAllNoticeStatusDraftByUserEmail method");
    return this.noticeRepository.getAllDraftNoticeByUserId(email, pageable);
    

  }

}
