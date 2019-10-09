package hrds.agent.job.biz.dataclean.tableclean;

import fd.ng.core.utils.StringUtil;
import hrds.agent.job.biz.constant.CompleteTypeConstant;
import hrds.agent.job.biz.constant.JobConstant;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * ClassName: TbCompleteImpl <br/>
 * Function: 数据库直连采集表清洗字符补齐实现类 <br/>
 * Reason: 继承AbstractTableClean抽象类，只针对一个字符补齐方法进行实现
 * Date: 2019/8/1 15:24 <br/>
 * <p>
 * Author WangZhengcheng
 * Version 1.0
 * Since JDK 1.8
 **/
public class TbCompleteImpl extends AbstractTableClean {
	/**
	 * 表清洗字符补齐实现
	 *
	 * 1、判断completeSB是否为空，如果不为空，表示要进行字符补齐
	 * 2、获取补齐的长度
	 * 3、获取补齐的类型(前补齐、后补齐)
	 * 4、获取补齐字符
	 * 5、调用方法进行补齐操作
	 *
	 * @Param: completeSB StringBuilder
	 *         含义：用于字符补齐
	 *         取值范围：不为空, 格式为：补齐长度`补齐方式`要补齐的字符串
	 * @Param: columnValue String
	 *         含义：待清洗字段值
	 *         取值范围：不为空
	 *
	 * @return: String
	 *          含义：清洗后的字段值
	 *          取值范围：不会为null
	 *
	 * */
	@Override
	public String complete(StringBuilder completeSB, String columnValue) {
		//1、判断completeSB是否为空，如果不为空，表示要进行字符补齐
		if (completeSB != null) {
			List<String> strings = StringUtil.split(completeSB.toString(),
					JobConstant.CLEAN_SEPARATOR);
			//2、获取补齐的长度
			int completeLength = Integer.parseInt(strings.get(0));
			//3、获取补齐的类型(前补齐、后补齐)
			String completeType = strings.get(1);
			//4、获取补齐字符
			String completeCharacter = strings.get(2);
			//5、调用方法进行补齐操作
			if (CompleteTypeConstant.BEFORE.getCode() == Integer.parseInt(completeType)) {
				// 前补齐
				columnValue = StringUtils.leftPad(columnValue, completeLength, completeCharacter);
			} else if (CompleteTypeConstant.AFTER.getCode() == Integer.parseInt(completeType)) {
				// 后补齐
				columnValue = StringUtils.rightPad(columnValue, completeLength, completeCharacter);
			}
		}
		return columnValue;
	}
}
