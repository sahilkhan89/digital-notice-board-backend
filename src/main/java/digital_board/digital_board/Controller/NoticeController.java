package digital_board.digital_board.Controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import digital_board.digital_board.Dto.CategoryNoticeDto;
import digital_board.digital_board.Dto.NoticeDto;
import digital_board.digital_board.Entity.ExceptionResponse;
import digital_board.digital_board.Entity.Notice;
import digital_board.digital_board.Repository.NoticeRepository;
import digital_board.digital_board.ServiceImpl.NoticeServiceImpl;
import digital_board.digital_board.constants.ResponseMessagesConstants;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/notice")
public class NoticeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(NoticeController.class);

    @Autowired
    private NoticeServiceImpl noticeServiceImpl;
    @Autowired
    private NoticeRepository noticeRepository;

    // schedule by end date
    @GetMapping("/path")
    public List<Notice> getMethodName() {
        return noticeServiceImpl.getAllNoticeByScheduling();
    }

    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> createNoticeByUser(@RequestBody Notice notice) {
        LOGGER.info("Start Notic Controller : createNoticeByUser method");
        Map<String, Object> response = new HashMap<>();
        try {
            Notice savedNotice = this.noticeServiceImpl.createNoticeByUser(notice);
            MDC.put("useremail", notice.getCreatedBy());
            MDC.put("path", "notice/add");
            LOGGER.info("createNoticeByUser method : notice created");
            MDC.clear();
            String successMessage = ResponseMessagesConstants.messagelist.stream()
                    .filter(exceptionResponse -> "NOTICE_CREATE_SUCCESS".equals(exceptionResponse.getExceptonName()))
                    .map(ExceptionResponse::getMassage)
                    .findFirst()
                    .orElse("Default success message if not found");

            response.put("message", successMessage);
            response.put("data", savedNotice);

            LOGGER.info("End Notic Controller : createNoticeByUser method");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            String failureMessage = ResponseMessagesConstants.messagelist.stream()
                    .filter(exceptionResponse -> "NOTICE_CREATE_FAILURE".equals(exceptionResponse.getExceptonName()))
                    .map(ExceptionResponse::getMassage)
                    .findFirst()
                    .orElse("Default message if not found");

            response.put("message", failureMessage);
            LOGGER.info("End Notic Controller : createNoticeByUser method");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Map<String, Object>> updateNoticeByNoticeId(@RequestBody Notice notice) {
        Map<String, Object> response = new HashMap<>();
        try {
            Notice updatedNotice = this.noticeServiceImpl.updateNotice(notice);
            if (updatedNotice.getStatus().startsWith("disable")) {
                String successMessage = ResponseMessagesConstants.messagelist.stream()
                        .filter(exceptionResponse -> "NOTICE_DELETE_SUCCESS"
                                .equals(exceptionResponse.getExceptonName()))
                        .map(ExceptionResponse::getMassage)
                        .findFirst()
                        .orElse("Default success message if not found");

                response.put("message", successMessage);
                MDC.put("useremail", updatedNotice.getCreatedBy());
                MDC.put("path", "notice/update/delete");
                LOGGER.info("updateNoticeByNoticeId method : notice deleted");
                MDC.clear();

            } else {

                String successMessage = ResponseMessagesConstants.messagelist.stream()
                        .filter(exceptionResponse -> "NOTICE_UPDATED_SUCCESS"
                                .equals(exceptionResponse.getExceptonName()))
                        .map(ExceptionResponse::getMassage)
                        .findFirst()
                        .orElse("Default success message if not found");

                response.put("message", successMessage);

                response.put("message", successMessage);
                response.put("data", updatedNotice);

                MDC.put("useremail", notice.getCreatedBy());
                MDC.put("path", "notice/update/");
                LOGGER.info("updateNoticeByNoticeId method : notice update");
                MDC.clear();
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            String failureMessage = ResponseMessagesConstants.messagelist.stream()
                    .filter(exceptionResponse -> "NOTICE_UPDATE_FAILURE".equals(exceptionResponse.getExceptonName()))
                    .map(ExceptionResponse::getMassage)
                    .findFirst()
                    .orElse("Default failure message if not found");

            response.put("message", failureMessage);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/get/byNoticeId/{noticeId}")
    public ResponseEntity<Notice> getNoticeByNoticeId(@PathVariable String noticeId) {
        LOGGER.info("Start NoticeController: getNoticeByNoticeId method");
        Notice notice = noticeServiceImpl.getNoticeByNoticeId(noticeId);
        LOGGER.info("Start NoticeController: getNoticeByNoticeId method");
        return ResponseEntity.ok(notice);
    }

    @GetMapping("/getAll/byAdminEmail/{adminEmail}")
    public ResponseEntity<Map<String, Object>> getNoticeByUserEmail(@PathVariable String adminEmail,

            @RequestParam(required = false, defaultValue = "noticeCreatedDate,desc") String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        LOGGER.info("Start NoticeController: getNoticeByUserEmail method");
        Map<String, Object> response = new HashMap<>();
        Pageable pageable = PageRequest.of(page, size, parseSortString(sort));
        Page<Notice> notice = noticeServiceImpl.getNoticeByUserEmail(adminEmail, pageable);
        response.put("count", notice.getTotalElements());
        response.put("data", notice.getContent());

        if (notice.isEmpty()) {
            String emptyMessage = ResponseMessagesConstants.messagelist.stream()
                    .filter(exceptionResponse -> "LIST_IS_EMPTY".equals(exceptionResponse.getExceptonName()))
                    .map(ExceptionResponse::getMassage)
                    .findFirst()
                    .orElse("Default failure message if not found");

            response.put("message", emptyMessage);
            LOGGER.info("End NoticeController: getNoticeByUserEmail method");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        LOGGER.info("End NoticeController: getNoticeByUserEmail method");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/byCategory/{category}")
    public ResponseEntity<Map<String, Object>> getNoticesByCategory(@PathVariable List<String> category,
            @RequestParam(required = false) List<String> department,
            @RequestParam(required = false, defaultValue = "noticeCreatedDate,desc") String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        LOGGER.info("Start NoticeController: getNoticesByCategory method");
        Map<String, Object> response = new HashMap<>();
        Pageable pageable = PageRequest.of(page, size, parseSortString(sort));
        Page<Notice> notice = noticeServiceImpl.getNoticesByCategory(category, department, pageable);
        response.put("count", notice.getTotalElements());
        response.put("data", notice.getContent());

        if (notice.isEmpty()) {
            // Return a JSON response with a message for data not found
            String emptyMessage = ResponseMessagesConstants.messagelist.stream()
                    .filter(exceptionResponse -> "LIST_IS_EMPTY".equals(exceptionResponse.getExceptonName()))
                    .map(ExceptionResponse::getMassage)
                    .findFirst()
                    .orElse("Default failure message if not found");

            response.put("message", emptyMessage);
            LOGGER.info("End NoticeController: getNoticesByCategory method");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        // Return the list of notices if data is found
        LOGGER.info("End NoticeController: getNoticesByCategory method");
        return ResponseEntity.ok(response);
    }

    // http://localhost:8080/notices/byDepartment/iteg?sort=asc
    @GetMapping("/byDepartment/{departmentName}")
    public ResponseEntity<Map<String, Object>> getNoticesByDepartment(@PathVariable List<String> departmentName,
            @RequestParam(required = false) List<String> categories,
            @RequestParam(required = false, defaultValue = "noticeCreatedDate,desc") String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        LOGGER.info("Start NoticeController: getNoticesByDepartment method");
        Map<String, Object> response = new HashMap<>();
        Pageable pageable = PageRequest.of(page, size, parseSortString(sort));
        Page<Notice> notice = noticeServiceImpl.getNoticesByDepartment(departmentName, categories, pageable);
        response.put("count", notice.getTotalElements());
        response.put("data", notice.getContent());

        if (notice.isEmpty()) {
            // Return a JSON response with a message for data not found
            String emptyMessage = ResponseMessagesConstants.messagelist.stream()
                    .filter(exceptionResponse -> "LIST_IS_EMPTY".equals(exceptionResponse.getExceptonName()))
                    .map(ExceptionResponse::getMassage)
                    .findFirst()
                    .orElse("Default failure message if not found");

            response.put("message", emptyMessage);
            LOGGER.info("End NoticeController: getNoticesByDepartment method");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        // Return the list of notices if data is found
        LOGGER.info("End NoticeController: getNoticesByDepartment method");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getAll")
    public ResponseEntity<Map<String, Object>> getAllNotice(
            @RequestParam(required = false, defaultValue = "noticeCreatedDate,desc") String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        LOGGER.info("Start NoticeController: getAllNotice method");
        Map<String, Object> response = new HashMap<>();

        Pageable pageable = PageRequest.of(page, size, parseSortString(sort));
        Page<Notice> notice = noticeServiceImpl.getAllNoticesSorted(pageable);
        System.out.println("notice =>" + notice);
        response.put("count", notice.getTotalElements());
        response.put("data", notice.getContent());

        if (notice.isEmpty()) {
            // Return a JSON response with a message for data not found
            String emptyMessage = ResponseMessagesConstants.messagelist.stream()
                    .filter(exceptionResponse -> "LIST_IS_EMPTY".equals(exceptionResponse.getExceptonName()))
                    .map(ExceptionResponse::getMassage)
                    .findFirst()
                    .orElse("Default failure message if not found");

            response.put("message", emptyMessage);
            LOGGER.info("End NoticeController: getAllNotice method");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        // Return the list of notices if data is found
        LOGGER.info("End NoticeController: getAllNotice method");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countNoticesByCriteria(@RequestParam(required = false) String category,
            @RequestParam(required = false) String departmentName) {
        if (category != null) {
            // Count notices by category
            return ResponseEntity.ok(noticeServiceImpl.countByCategory(category));
        } else if (departmentName != null) {
            // Count notices by departmentName
            return ResponseEntity.ok(noticeServiceImpl.countByDepartmentName(departmentName));
        } else {
            // No criteria provided, return total count
            return ResponseEntity.ok(noticeServiceImpl.getTotalNoticeCount());
        }
    }

    private Sort parseSortString(String sort) {
        String[] sortParams = sort.split(",");
        if (sortParams.length == 2) {
            Sort.Direction direction = sortParams[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC
                    : Sort.Direction.ASC;
            return Sort.by(new Sort.Order(direction, sortParams[0]));
        } else {
            return Sort.by(Sort.Order.asc("noticeCreatedDate")); // Default sorting by noticeCreatedDate in ascending
                                                                 // order
        }
    }

    @GetMapping("/search/{query}")
    public ResponseEntity<Map<String, Object>> searchNotices(@PathVariable String query,
            @RequestParam(required = false, defaultValue = "noticeCreatedDate,desc") String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        LOGGER.info("Start NoticeController: searchNotices method");
        Map<String, Object> response = new HashMap<>();
        Pageable pageable = PageRequest.of(page, size, parseSortString(sort));
        Page<Notice> notice = noticeServiceImpl.searchNotices(query, pageable);
        response.put("count", notice.getTotalElements());
        response.put("data", notice.getContent());

        if (notice.isEmpty()) {
            String emptyMessage = ResponseMessagesConstants.messagelist.stream()
                    .filter(exceptionResponse -> "LIST_IS_EMPTY".equals(exceptionResponse.getExceptonName()))
                    .map(ExceptionResponse::getMassage)
                    .findFirst()
                    .orElse("Default failure message if not found");
            response.put("message", emptyMessage);
            LOGGER.info("Start NoticeController: searchNotices method");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        LOGGER.info("Start NoticeController: searchNotices method");
        return ResponseEntity.ok(response);

    }

    // get important notice by limit
    @GetMapping("/important")
    ResponseEntity<Map<String, Object>> getAllImportantNoticeByLimit(

            @RequestParam(required = false, defaultValue = "desc") String order,
            @RequestParam(required = false, defaultValue = "3") int limit) {

        Sort.Direction direction = Sort.Direction.DESC; // Default sorting order
        LOGGER.info("Start NoticeController: getAllImportantNoticeByLimit method");
        if ("asc".equalsIgnoreCase(order)) {
            direction = Sort.Direction.ASC;
        }

        Sort sort = Sort.by(direction, "noticeCreatedDate");

        Map<String, Object> response = new HashMap<>();
        List<Notice> resultofnotice = noticeServiceImpl.noticefindByStatusImportant(sort, limit);
        response.put("data", noticeServiceImpl.noticefindByStatusImportant(sort, limit));
        if (resultofnotice.isEmpty()) {
            response.put("message", ResponseMessagesConstants.messagelist.stream()
                    .filter(exceptionResponse -> "LIST_IS_EMPTY".equals(exceptionResponse.getExceptonName()))
                    .map(ExceptionResponse::getMassage)
                    .findFirst()
                    .orElse("Default failure message if not found"));
            LOGGER.info("Start NoticeController: getAllImportantNoticeByLimit method");
            return ResponseEntity.ok(response);
        }
        LOGGER.info("Start NoticeController: getAllImportantNoticeByLimit method");
        return ResponseEntity.ok(response);

    }

    @GetMapping("/getAll/byfilter")
    public ResponseEntity<Map<String, Object>> getAllNoticesByfilter(
            @RequestParam(required = false) List<String> department,
            @RequestParam(required = false) List<String> categories,
            @RequestParam(required = false) List<String> admins,
            @RequestParam(required = false) String status,
            @RequestParam(required = false, defaultValue = "noticeCreatedDate,desc") String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        LOGGER.info("Start NoticeController: getAllNoticesByfilter method");
        Map<String, Object> response = new HashMap<>();
        Pageable pageable = PageRequest.of(page, size, parseSortString(sort));
        Page<Notice> notice = noticeServiceImpl.getAllNoticesByfilter(categories, department, admins, status, pageable);
        response.put("count", notice.getTotalElements());
        response.put("data", notice.getContent());

        if (notice.isEmpty()) {
            // Return a JSON response with a message for data not found
            String emptyMessage = ResponseMessagesConstants.messagelist.stream()
                    .filter(exceptionResponse -> "LIST_IS_EMPTY".equals(exceptionResponse.getExceptonName()))
                    .map(ExceptionResponse::getMassage)
                    .findFirst()
                    .orElse("Default failure message if not found");

            response.put("message", emptyMessage);
            LOGGER.info("End NoticeController: getAllNoticesByfilter method");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        // Return the list of notices if data is found
        LOGGER.info("End NoticeController: getAllNoticesByfilter method");
        return ResponseEntity.ok(response);
    }

    // today created notice count

    @GetMapping("/today/created/notice/count")
    public ResponseEntity<Map<String, Object>> todayCreatedNotice() {
        Map<String, Object> response = new HashMap<>();
        List<Notice> todayCreatedNoticeCount = noticeServiceImpl.todayCreatedNoticeCount();
        System.out.println(todayCreatedNoticeCount);
        if (todayCreatedNoticeCount.isEmpty()) {
            response.put("message", ResponseMessagesConstants.messagelist.stream()
                    .filter(exceptionResponse -> "TODAY_NOTICE_NOT_FOUND".equals(exceptionResponse.getExceptonName()))
                    .map(ExceptionResponse::getMassage)
                    .findFirst()
                    .orElse("Default message if not found"));
        } else {
            response.put("message", ResponseMessagesConstants.messagelist.stream()
                    .filter(exceptionResponse -> "TODAY_NOTICE".equals(exceptionResponse.getExceptonName()))
                    .map(ExceptionResponse::getMassage)
                    .findFirst()
                    .orElse("Default message if not found"));
            response.put("count", todayCreatedNoticeCount.size());

        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/activeNoticeDepartmentCount")
    public ResponseEntity<Map<String, Object>> countAllEnableNotices() {
        LOGGER.info("Start NoticeController: countAllEnableNotices method");
        Map<String, Object> response = new HashMap<>();

        List<NoticeDto> noticeDto = noticeServiceImpl.getCountAllEnableDepartmentNotices();
        response.put("data", noticeDto);
        if (noticeDto.isEmpty()) {
            // Return a JSON response with a message for data not found
            response.put("count", noticeDto.size());
            LOGGER.info("End NoticeController: countAllEnableNotices method");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        // Return the list of notices if data is found
        LOGGER.info("End NoticeController: countAllEnableNotices method");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/activeNoticeCategoryCount")
    public ResponseEntity<Map<String, Object>> countAllCategoryNotices() {
        LOGGER.info("Start NoticeController: countAllCategoryNotices method");
        Map<String, Object> response = new HashMap<>();

        List<CategoryNoticeDto> categoryNoticeDtos = noticeServiceImpl.getCountAllEnableCategoryNotices();
        response.put("data", categoryNoticeDtos);
        if (categoryNoticeDtos.isEmpty()) {
            // Return a JSON response with a message for data not found
            response.put("count", categoryNoticeDtos.size());
            LOGGER.info("End NoticeController: countAllCategoryNotices method");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        // Return the list of notices if data is found
        LOGGER.info("End NoticeController: countAllCategoryNotices method");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/last7Days")
    public List<Map<String, Object>> getLast7Days() {
        LOGGER.info("Start NoticeController: getLast7Days method");
        LOGGER.info("End NoticeController: getLast7Days method");
        return noticeServiceImpl.getLast7DaysCount();
    }

    @GetMapping("/categories/count")
    public List<Map<String, Object>> getNoticebyCategory() {
        return noticeServiceImpl.getnoticesByCategory();
    }

    @GetMapping("/NoticeCategoryCountBySuperAdmin")
    public ResponseEntity<Map<String, Object>> getFindNoticeCountsByDepartmentForSuperAdmin() {
        LOGGER.info("Start NoticeController: getFindNoticeCountsByDepartmentForSuperAdmin method");
        Map<String, Object> response = new HashMap<>();

        List<NoticeDto> noticeDtos = noticeServiceImpl.getFindNoticeCountsByDepartmentForSuperAdmin();
        response.put("data", noticeDtos);
        if (noticeDtos.isEmpty()) {
            // Return a JSON response with a message for data not found
            response.put("count", noticeDtos.size());
            LOGGER.info("End NoticeController: getFindNoticeCountsByDepartmentForSuperAdmin method");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        // Return the list of notices if data is found
        LOGGER.info("End NoticeController: getFindNoticeCountsByDepartmentForSuperAdmin method");
        return ResponseEntity.ok(response);
    }

    // scheduling
    @Scheduled(fixedRate = 6000)
    public String noticeDoCompleted() {
        System.out.println("run notice completed");
        noticeRepository.updateStatusToCompleted();
        noticeRepository.updateStatusForActiveNotices();
        return "notice status completed successfully";
    }

    // get all notice draft
    @GetMapping("/getall/draft")
    public List<Notice> getAllNoticeDraft() {
        return noticeRepository.getAllNoticeDraft();
    }

    @GetMapping("/getAll/draft/byAdminEmail/{adminEmail}")
    public ResponseEntity<Map<String, Object>> getAllNoticeStatusDraftByUserEmail(@PathVariable String adminEmail,

            @RequestParam(required = false, defaultValue = "noticeCreatedDate,desc") String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        LOGGER.info("Start NoticeController: getAllNoticeStatusDraftByUserEmail method");
        Map<String, Object> response = new HashMap<>();
        Pageable pageable = PageRequest.of(page, size, parseSortString(sort));
        Page<Notice> notice = noticeServiceImpl.getAllNoticeStatusDraftByUserEmail(adminEmail, pageable);
        response.put("count", notice.getTotalElements());
        response.put("data", notice.getContent());

        if (notice.isEmpty()) {
            String emptyMessage = ResponseMessagesConstants.messagelist.stream()
                    .filter(exceptionResponse -> "LIST_IS_EMPTY".equals(exceptionResponse.getExceptonName()))
                    .map(ExceptionResponse::getMassage)
                    .findFirst()
                    .orElse("Default failure message if not found");

            response.put("message", emptyMessage);
            LOGGER.info("End NoticeController: getAllNoticeStatusDraftByUserEmail method");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        LOGGER.info("End NoticeController: getAllNoticeStatusDraftByUserEmail method");
        return ResponseEntity.ok(response);
    }

}
