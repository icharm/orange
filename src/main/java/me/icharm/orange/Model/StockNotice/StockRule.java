package me.icharm.orange.Model.StockNotice;

import lombok.Data;
import me.icharm.orange.Constant.StockNotice.StockRuleStatusEnum;
import me.icharm.orange.Model.Common.User;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Stock notice rule
 *
 * @author mylicharm
 * @email icharm.me@outlook.com
 * @date 2018/8/15 11:14
 */
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "sn_stock_rules")
public class StockRule {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * user table 外键
     */
    @ManyToOne(targetEntity = User.class)
    private Long user;

    private String openid;              /** wechat openid */
    private String code;                /** stock code */
    private String name;                /** stock name */
    private String noticeName;          /** stock notice rule name */
    private Boolean riseCheck;          /** is enable rise notice */
    private Boolean dropCheck;          /** is enable drop notice */
    private Integer riseNoticeCount = 0;/** rise notice times */
    private Integer dropNoticeCount = 0;/** drop notice times */
    private Integer noticeCount = 0;    /** all notice times */
    private Double basePrice;           /** base stock price */
    private Double risePrice;           /** rise notice price */
    private Double risePercent;         /** rise notice percent */
    private Double dropPrice;           /** drop notice price */
    private Double dropPercent;         /** drop notice percent */
    private Integer status = StockRuleStatusEnum.NORMAL.code; /** rule status */

    @CreatedDate
    private Timestamp createdAt;

    @LastModifiedDate
    private Timestamp updatedAt;

}
