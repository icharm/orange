package me.icharm.orange.Model.Common;

import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * @author mylicharm
 * @email icharm.me@outlook.com
 * @date 2018/8/15 18:52
 */
@Data
@Entity
@EntityListeners(AuditingEntityListener.class) // to active @CreatedData and @LastModifiedDate Annotation
@Table(name = "com_system_parameter")
public class SystemParameter {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(targetEntity = User.class)
    private Long user;

    private String keyword;
    private String value;
    private String description;

    @CreatedDate
    private Timestamp createdAt;

    @LastModifiedDate
    private Timestamp updatedAt;

}
