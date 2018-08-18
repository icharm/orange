package me.icharm.orange.Model.StockNotice;

import lombok.Data;
import me.icharm.orange.Model.Common.User;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * @author mylicharm
 * @email icharm.me@outlook.com
 * @date 2018/8/16 14:25
 */
@Entity
@Data
@Table(name = "sn_notice_record")
public class NoticeRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(targetEntity = StockRule.class)
    private Long ruleId;

    @ManyToOne(targetEntity = User.class)
    private Long userId;

    private Integer noticeType;                     /** notice type rise or drop */
    private String content;
    @Column(insertable = true)
    private Timestamp createdAt;
    @Column(insertable = true, updatable = true)
    private Timestamp updatedAt;
}
