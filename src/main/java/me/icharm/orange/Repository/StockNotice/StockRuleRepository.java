package me.icharm.orange.Repository.StockNotice;

import me.icharm.orange.Model.StockNotice.StockRule;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * JPA会自动实现该接口中所有的方法
 *
 * @author mylicharm
 * @email icharm.me@outlook.com
 * @date 2018/8/15 11:55
 */
public interface StockRuleRepository extends CrudRepository<StockRule, Long> {

    List<StockRule> findStockRulesByStatus(Integer status);

    StockRule findStockRuleById(Long id);
}
