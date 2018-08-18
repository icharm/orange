package me.icharm.orange.Model.Common;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * @author mylicharm
 * @email icharm.me@outlook.com
 * @date 2018/8/15 18:52
 */
@Data
@Entity
@Table(name = "com_system_parameter")
public class SystemParameter {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(targetEntity = User.class)
    private Long userId;

    private String keyword;
    private String value;
    private String description;
    @Column(insertable = true)
    private Timestamp createdAt;
    @Column(insertable = true, updatable = true)
    private Timestamp updatedAt;

}
