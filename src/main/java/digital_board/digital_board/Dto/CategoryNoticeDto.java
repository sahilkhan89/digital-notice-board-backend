package digital_board.digital_board.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategoryNoticeDto {
    
    private String category_name;
    private long notice_count;
}

