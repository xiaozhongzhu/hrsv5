package hrds.agent.job.biz.core.increasement;

/**
 * Increasement
 * date: 2020/5/22 11:16
 * author: zxz
 */
public interface Increasement {
	/**
	 * 比较出所有的增量数据入增量表
	 */
	void calculateIncrement() throws Exception;

	/**
	 * 根据临时增量表合并出新的增量表，删除以前的增量表
	 */
	void mergeIncrement() throws Exception;

	/**
	 * 追加
	 */
	void append();

	/**
	 * 替换
	 */
	void replace();

	/**
	 * 重跑，或者当次跑批失败恢复数据
	 */
	void restore(String storageType);

	void close();

	/**
	 * 没有数据保留天数的时候，删除当天卸数的数据
	 */
	void dropTodayTable();

	/**
	 * 增量数据算拉链
	 */
	void incrementalDataZipper() throws Exception;
}
