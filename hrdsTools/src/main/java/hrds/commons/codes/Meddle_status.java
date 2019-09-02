package hrds.commons.codes;

import hrds.commons.exception.AppSystemException;
/**Created by automatic  */
/**代码类型名：ETL干预状态  */
public enum Meddle_status {
	/**完成<DONE>  */
	DONE("D","完成","115","ETL干预状态"),
	/**异常<ERROR>  */
	ERROR("E","异常","115","ETL干预状态"),
	/**失效<FALSE>  */
	FALSE("F","失效","115","ETL干预状态"),
	/**有效<TRUE>  */
	TRUE("T","有效","115","ETL干预状态"),
	/**干预中<RUNNING>  */
	RUNNING("R","干预中","115","ETL干预状态");

	private final String code;
	private final String value;
	private final String catCode;
	private final String catValue;

	Meddle_status(String code,String value,String catCode,String catValue){
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
		for (Meddle_status typeCode : Meddle_status.values()) {
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
	public static Meddle_status getCodeObj(String code) {
		for (Meddle_status typeCode : Meddle_status.values()) {
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
		return Meddle_status.values()[0].getCatValue();
	}

	/**
	* 获取代码项的分类代码
	* @return
	*/
	public static String getObjCatCode(){
		return Meddle_status.values()[0].getCatCode();
	}
}
