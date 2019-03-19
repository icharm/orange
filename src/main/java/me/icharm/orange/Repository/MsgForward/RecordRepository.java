package me.icharm.orange.Repository.MsgForward;

import me.icharm.orange.Model.MsgForward.Mfuser;
import me.icharm.orange.Model.MsgForward.Record;
import org.springframework.data.repository.CrudRepository;

/**
 * @author mylicharm
 * @email icharm.me@outlook.com
 * @date 2018/12/8 15:27
 */
public interface RecordRepository extends CrudRepository<Record, Long> {

    Record findRecordByHashCodeAndMfuserAndStatus(String hashCode, Mfuser mfuser, Integer status);

}
