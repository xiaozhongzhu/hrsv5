package hrds.agent.job.biz.utils;

import fd.ng.core.annotation.DocClass;
import fd.ng.core.conf.ConfFileLoader;
import fd.ng.core.utils.StringUtil;
import fd.ng.core.yaml.YamlArray;
import fd.ng.core.yaml.YamlFactory;
import fd.ng.core.yaml.YamlMap;
import hrds.commons.exception.AppSystemException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@DocClass(desc = "获取每个类型的长度", createdate = "2020/1/8 15:27", author = "zxz")
public class TypeTransLength {
	private static final Map<String, YamlMap> map = new HashMap<>();
	private static final String LKH = "(";
	private static final String RKH = ")";
	private static final String COMMA = ",";
	//TODO 默认的类型长度，基于卸数时不知道最终目的地的原因
	private static final String DEFAULT = "DEFAULT";

	static {
		YamlMap rootConfig = YamlFactory.load(ConfFileLoader.getConfFile("contrast")).asMap();
		YamlArray arrays = rootConfig.getArray("lengthcontrast");
		for (int i = 0; i < arrays.size(); i++) {
			YamlMap trans = arrays.getMap(i);
			map.put(trans.getString("NAME"), trans);
		}
	}

	/**
	 * 获取每个类型的长度
	 */
	public static int getLength(String column_type) {
		column_type = column_type.toUpperCase().trim();
		if (column_type.contains(LKH) && column_type.contains(RKH)) {
			int start = column_type.indexOf(LKH);
			int end = column_type.indexOf(RKH);
			String substring = column_type.substring(start + 1, end);
			if (substring.contains(COMMA)) {
				List<String> split = StringUtil.split(substring, COMMA);
				//对于字段类型为number(22,2)、number(38,12)的字段，在数据库中的长度为22、38，抽取出来的定长会多一个正负号和一个点，
				// 因此统一处理为22+2 和 38+2
				return Integer.parseInt(split.get(0)) + 2;
			}
			return Integer.parseInt(substring);
		} else {
			YamlMap yamlMap = map.get(DEFAULT);
			if (yamlMap == null) {
				throw new AppSystemException("管理员用户没有配置名称为DEFAULT的数据长度对比，请登录管理员用户配置名称为DEFAULT" +
						"的数据长度对比,配置完成后请重新部署agent或者手动更新Agent配置文件contrast.conf再重启Agent");
			}
			return yamlMap.getInt(column_type);
		}
	}

	/**
	 * 获取每个类型的长度
	 */
	public static int getLength(String column_type, String dsl_name) {
		column_type = column_type.toUpperCase().trim();
		if (column_type.contains(LKH) && column_type.contains(RKH)) {
			int start = column_type.indexOf(LKH);
			int end = column_type.indexOf(RKH);
			String substring = column_type.substring(start + 1, end);
			if (substring.contains(COMMA)) {
				List<String> split = StringUtil.split(substring, COMMA);
				//对于字段类型为number(22,2)、number(38,12)的字段，在数据库中的长度为22、38，抽取出来的定长会多一个正负号和一个点，
				// 因此统一处理为22+2 和 38+2
				return Integer.parseInt(split.get(0)) + 2;
//				return Integer.parseInt(split.get(0)) + Integer.parseInt(split.get(1));
			}
			return Integer.parseInt(substring);
		} else {
			YamlMap yamlMap = map.get(dsl_name);
			if (yamlMap == null) {
				throw new AppSystemException("存储层" + dsl_name + "的配置信息在Agent的contrast.conf文件中没有，" +
						"请重新部署agent或者手动更新配置文件再重启Agent");
			}
			return yamlMap.getInt(column_type);
		}
	}
}
