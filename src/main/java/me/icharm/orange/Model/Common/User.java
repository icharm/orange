package me.icharm.orange.Model.Common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import me.icharm.orange.Constant.Common.UserStatusEnum;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.alibaba.fastjson.JSON;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author mylicharm
 * @email icharm.me@outlook.com
 * @date 2018/8/15 17:47
 */
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "com_user")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * wechat openid
     */
    private String openid;

    /**
     * phone number
     */
    private String phone;

    /**
     * email address
     */
    private String email;

    /**
     * nick name
     */
    private String nickName;

    /**
     * avatar url
     */
    private String avatar;

    /**
     * sex
     */
    private String sex;

    /**
     * User location
     */
    private String location;

    private String password;
    /**
     * random str to encode password
     */
    private String salt;

    /**
     * User role (Json Format)
     */
    private String authorities;

    /**
     * User status
     */
    private Integer status = UserStatusEnum.UNABLE.code;


    @CreatedDate
    private Timestamp createdAt;

    @LastModifiedDate
    private Timestamp updatedAt;


    /**
     * to implement security UserDetails interface. Other method generated by lombok
     */

    @JsonIgnore
    @Override
    public String getUsername() {
        return this.openid;
    }

    @JsonIgnore
    @Override
    public Collection<SimpleGrantedAuthority> getAuthorities() {
        List<String> rolelist = JSON.parseArray(authorities, String.class);
        List<SimpleGrantedAuthority> authList = new ArrayList<>();
        for (String role : rolelist) {
            authList.add(new SimpleGrantedAuthority(role));
        }
        return authList;
    }

    public void setAuthorities(String role) {
        List<String> roleList = new ArrayList<>();
        if (StringUtils.isBlank(this.authorities)) {
            roleList.add(role);
            this.authorities = JSON.toJSONString(roleList);
        } else {
            roleList = JSON.parseArray(this.authorities, String.class);
            // If this role existed.
            if(!roleList.contains(role)) {
                // don't exist
                roleList.add(role);
            }
            this.authorities = JSON.toJSONString(roleList);
        }
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        if (this.status <= UserStatusEnum.NORMAL.code) {
            return true;
        } else {
            return false;
        }
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        if (this.status <= UserStatusEnum.NORMAL.code) {
            return true;
        } else {
            return false;
        }
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        if (this.status <= UserStatusEnum.NORMAL.code) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isEnabled() {
        if (this.status <= UserStatusEnum.NORMAL.code) {
            return true;
        } else {
            return false;
        }
    }
}
