package me.icharm.orange.Repository.Common;

import me.icharm.orange.Model.Common.SystemParameter;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

/**
 * It's not recommended to use this interface directly.
 * Please use SystemparameterService instead.
 *
 * @author mylicharm
 * @email icharm.me@outlook.com
 * @date 2018/8/15 19:06
 */
public interface SystemParameterRepository extends CrudRepository<SystemParameter, Long> {

    SystemParameter findSystemParameterByKeyword(String key);

    List<SystemParameter> findSystemParametersByKeyword(String key);
}
