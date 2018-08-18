package me.icharm.orange.Repository.Common;

import me.icharm.orange.Model.Common.User;
import org.springframework.data.repository.CrudRepository;

/**
 * @author mylicharm
 * @email icharm.me@outlook.com
 * @date 2018/8/15 19:09
 */
public interface UserRepository extends CrudRepository<User, Long> {

    User findUserById(Long id);

    User findUserByOpenid(String openid);

}
