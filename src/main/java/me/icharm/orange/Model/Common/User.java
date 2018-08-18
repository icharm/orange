package me.icharm.orange.Model.Common;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * @author mylicharm
 * @email icharm.me@outlook.com
 * @date 2018/8/15 17:47
 */
@Data
@Entity
@Table(name = "com_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String openid;          /** wechat openid */
    private String phone;           /** phone number */
    private String email;           /** email address */
    private String nickName;        /** nick name */
    private String avatar;          /** avatar url */
    private String sex;             /** sex */
    private String location;        /** user location */
    private String password;
    private String salt;            /** random str to encode password */
    private Integer permission;     /** user permission */
    private Integer status;         /** user status */
    @Column(insertable = true)
    private Timestamp createdAt;
    @Column(insertable = true, updatable = true)
    private Timestamp updatedAt;
}
