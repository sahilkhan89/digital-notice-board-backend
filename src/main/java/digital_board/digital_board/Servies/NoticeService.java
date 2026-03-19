package digital_board.digital_board.Servies;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import digital_board.digital_board.Dto.CategoryNoticeDto;
import digital_board.digital_board.Dto.NoticeDto;
import digital_board.digital_board.Dto.NoticeFilterDto;
import digital_board.digital_board.Entity.Notice;

public interface NoticeService {

        public Notice createNoticeByUser(Notice notice);

        public Notice getNoticeByNoticeId(String noticeId);

        public Page<Notice> getNoticeByUserEmail(String email, Pageable pageable);

        public List<Notice> getAllNotice();

        public Page<Notice> getNoticesByCategory(List<String> category, List<String> department, Pageable pageable);

        public Page<Notice> getNoticesByDepartment(List<String> departmentName, List<String> categories,
                        Pageable pageable);

        public Page<Notice> getAllNoticesSorted(Pageable pageable);

        // Get ALl important notice
        public List<Notice> getAllImportantNotice(int limit);

        public Long getTotalNoticeCount();

        public Page<Notice> searchNotices(String query, Pageable pageable);

        // update notice
        public Notice updateNotice(Notice notice);

        // public List<Notice> filterNotices(List<String> department, List<String>
        // categories, List<String> admins,
        // String status, int page, int size);

        public Long countByCategory(String category);

        public Long countByDepartmentName(String departmentName);

        // get important notice by limit
        public List<Notice> noticefindByStatusImportant(Sort sort, int limit);

        public Page<Notice> getAllNoticesByfilter(List<String> categories, List<String> departmentNames,
                        List<String> createdBy,
                        String status, Pageable pageable);

        // today created notice count
        public List<Notice> todayCreatedNoticeCount();

        List<NoticeDto> getCountAllEnableDepartmentNotices();

        List<CategoryNoticeDto> getCountAllEnableCategoryNotices();

        List<Map<String, Object>> getLast7DaysCount();

        List<Map<String, Object>> getnoticesByCategory();

        public List<NoticeDto> getFindNoticeCountsByDepartmentForSuperAdmin();

        public Page<Notice> getAllNoticeStatusDraftByUserEmail(String email, Pageable pageable);



}
