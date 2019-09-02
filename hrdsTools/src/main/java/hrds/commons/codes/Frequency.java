package hrds.commons.codes;

import hrds.commons.exception.AppSystemException;
/**Created by automatic  */
/**代码类型名：频率  */
public enum Frequency {
	/**自定义<ZiDingYi>  */
	ZiDingYi("0","自定义","7","频率"),
	/**天<Tian>  */
	Tian("1","天","7","频率"),
	/**周<Zhou>  */
	Zhou("2","周","7","频率"),
	/**月<Yue>  */
	Yue("3","月","7","频率"),
	/**季度<JiDu>  */
	JiDu("4","季度","7","频率");

	private final String code;
	private final String value;
	private final String catCode;
	private final String catValue;

	Frequency(String code,String value,String catCode,String catValue){
		this.code = code;
		this.value = value;
		this.catCode = catCode;
		this.catValue = catValue;
	}
	public String getCode(){return code;}
	public String getValue(){return value;}
	public String getCatCode(){return catCode;}
	public String getCatValue(){return catValue;}

	/**根据指定的代码值转换成中文名字
	* @param code   本代码的代码值
	* @return
	*/
	public static String getValue(String code) {
		for (Frequency typeCode : Frequency.values()) {
			if (typeCode.getCode().equals(code)) {
				return typeCode.value;
			}
		}
		throw new AppSystemException("根据"+code+"没有找到对应的代码项");
	}

	/**根据指定的代码值转换成对象
	* @param code   本代码的代码值
	* @return
	*/
	public static Frequency getCodeObj(String code) {
		for (Frequency typeCode : Frequency.values()) {
			if (typeCode.getCode().equals(code)) {
				return typeCode;
			}
		}
		throw new AppSystemException("根据"+code+"没有找到对应的代码项");
	}

	/**
	* 获取代码项的中文类名名称
	* @return
	*/
	public static String getObjCatValue(){
		return Frequency.values()[0].getCatValue();
	}

	/**
	* 获取代码项的分类代码
	* @return
	*/
	public static String getObjCatCode(){
		return Frequency.values()[0].getCatCode();
	}
}
