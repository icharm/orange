package me.icharm.orange.Model.MsgForward;

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
 * @date 2018/11/16 17:26
 */
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "mf_record")
public class Record {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Mfuser mfuser;

    private String content;
    private String hashCode;
    private String status;

    @CreatedDate
    private Timestamp createdAt;

    @LastModifiedDate
    private Timestamp updatedAt;
}
