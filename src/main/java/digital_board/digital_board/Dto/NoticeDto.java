package digital_board.digital_board.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NoticeDto {
    
    private String departmentName;
    private long notice_count;
}
