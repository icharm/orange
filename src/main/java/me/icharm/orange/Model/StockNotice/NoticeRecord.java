package me.icharm.orange.Model.StockNotice;

import lombok.Data;
import me.icharm.orange.Model.Common.User;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * @author mylicharm
 * @email icharm.me@outlook.com
 * @date 2018/8/16 14:25
 */
@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
@Table(name = "sn_notice_record")
public class NoticeRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(targetEntity = StockRule.class)
    private Long rule;

    @ManyToOne(targetEntity = User.class)
    private Long user;

    private Integer noticeType;                     /** notice type rise or drop */
    private String content;

    @CreatedDate
    private Timestamp createdAt;

    @LastModifiedDate
    private Timestamp updatedAt;
}
