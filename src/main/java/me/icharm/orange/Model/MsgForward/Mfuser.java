package me.icharm.orange.Model.MsgForward;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

import me.icharm.orange.Model.Common.User;

import java.sql.Timestamp;
import java.util.Collection;

/**
 * @author mylicharm
 * @email icharm.me@outlook.com
 * @date 2018/11/16 17:25
 */
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "mf_user")
public class Mfuser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * User table 外键
     */
    @OneToOne(mappedBy = "mfuser", fetch = FetchType.LAZY)
    private User user;

    @OneToMany(mappedBy = "mfuser")
    private Collection<Record> records;

    @Column(unique = true)
    private String openid;

    private String secret;
    private int todayCount = 0;
    private Long totalCount = 0L;

    @CreatedDate
    private Timestamp createdAt;

    @LastModifiedDate
    private Timestamp updatedAt;
}
