package me.icharm.orange.Repository.MsgForward;


import me.icharm.orange.Model.MsgForward.Mfuser;
import org.springframework.data.repository.CrudRepository;

/**
 * @author mylicharm
 * @email icharm.me@outlook.com
 * @date 2018/12/6 17:54
 */
public interface MfuserRepository extends CrudRepository<Mfuser, Long> {

    Mfuser findByOpenid(String openid);

    Mfuser findMfuserBySecret(String secret);

}
