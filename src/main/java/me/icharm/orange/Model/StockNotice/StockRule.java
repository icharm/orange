package me.icharm.orange.Model.StockNotice;

import lombok.Data;
import me.icharm.orange.Model.Common.User;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * 预警规则
 * @author mylicharm
 * @email icharm.me@outlook.com
 * @date 2018/8/15 11:14
 */
@Data
@Entity
@Table(name = "sn_stock_rules")
public class StockRule {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * user table 外键
     */
    @ManyToOne(targetEntity = User.class)
    private Long userId;

    private String openid;          /** wechat openid */
    private String code;            /** stock code */
    private String name;            /** stock name */
    private String noticeName;      /** stock notice rule name */
    private Boolean riseCheck;      /** is enable rise notice */
    private Boolean dropCheck;      /** is enable drop notice */
    private Integer riseNoticeCount;/** rise notice times */
    private Integer dropNoticeCount;/** drop notice times */
    private Integer noticeCount;    /** all notice times */
    private Double basePrice;       /** base stock price */
    private Double risePrice;       /** rise notice price */
    private Double risePercent;     /** rise notice percent */
    private Double dropPrice;       /** drop notice price */
    private Double dropPercent;     /** drop notice percent */
    private Integer status;         /** rule status */
    @Column(insertable = true)
    private Timestamp createdAt;
    @Column(insertable = true, updatable = true)
    private Timestamp updatedAt;

}
