package me.icharm.orange.Repository.StockNotice;

import me.icharm.orange.Model.StockNotice.NoticeRecord;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author mylicharm
 * @email icharm.me@outlook.com
 * @date 2018/8/16 14:37
 */
public interface NoticeRecordRepository extends CrudRepository<NoticeRecord, Long> {

    List<NoticeRecord> findByRule(Long id);

    List<NoticeRecord> findByUser(Long id);
}
