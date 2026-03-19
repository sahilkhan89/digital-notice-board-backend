package digital_board.digital_board.Entity;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notice {

    @Id
    private String noticeId = UUID.randomUUID().toString();
    private String noticeTitle;
    private String description;
    private String category;
    private String departmentName;

    // @Transient
    // private String test;
    
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date noticeStartDate;

    
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date noticeEndDate;

    @ElementCollection
    @CollectionTable(name = "notice_images", joinColumns = @JoinColumn(name = "notice_id"))
    @Column(name = "image_url")
    private List<String> Images_url;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date noticeCreatedDate;

    private String createdBy;
    private boolean important;
    private String noticeBackground;
    private Integer timeDuration;
    private String status;

    @PrePersist
    protected void onCreate() {
        this.noticeCreatedDate = new Date();
    }

}
