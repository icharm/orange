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

    private String openid;
    /**
     * wechat openid
     */
    private String phone;
    /**
     * phone number
     */
    private String email;
    /**
     * email address
     */
    private String nickName;
    /**
     * nick name
     */
    private String avatar;
    /**
     * avatar url
     */
    private String sex;
    /**
     * sex
     */
    private String location;
    /**
     * user location
     */
    private String password;
    private String salt;
    /**
     * random str to encode password
     */
    private String role;
    /**
     * user role (Json Format)
     */
    private Integer status = UserStatusEnum.NORMAL.code;
    /**
     * user status
     */

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
        List<String> rolelist = JSON.parseArray(role, String.class);
        List<SimpleGrantedAuthority> authList = new ArrayList<>();
        for (String role : rolelist) {
            authList.add(new SimpleGrantedAuthority(role));
        }
        return authList;
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

    public void setRole(String role) {
        List<String> roleList = new ArrayList<>();
        if (StringUtils.isBlank(this.role)) {
            roleList.add(role);
            this.role = JSON.toJSONString(roleList);
        } else {
            roleList.addAll(JSON.parseArray(this.role, String.class));
            roleList.add(role);
            this.role = JSON.toJSONString(roleList);
        }
    }

    public List<String> getRole(){
        List<String> roleList = new ArrayList<>();
        if (StringUtils.isBlank(this.role)) {
            return roleList;
        }
        roleList = JSON.parseArray(this.role, String.class);
        return roleList;
    }
}
