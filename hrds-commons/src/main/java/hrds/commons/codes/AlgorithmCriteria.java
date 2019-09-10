package hrds.commons.codes;

import hrds.commons.exception.AppSystemException;
/**Created by automatic  */
/**代码类型名：算法评判标准  */
public enum AlgorithmCriteria {
	/**准确度<ZhunQueDu>  */
	ZhunQueDu("01","准确度","72","算法评判标准"),
	/**R^2<RDePingFang>  */
	RDePingFang("02","R^2","72","算法评判标准"),
	/**平均根误差<PingJunGenWuCha>  */
	PingJunGenWuCha("03","平均根误差","72","算法评判标准"),
	/**平均绝对误差<PingJunJueDuiWuCha>  */
	PingJunJueDuiWuCha("04","平均绝对误差","72","算法评判标准");

	private final String code;
	private final String value;
	private final String catCode;
	private final String catValue;

	AlgorithmCriteria(String code,String value,String catCode,String catValue){
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
		for (AlgorithmCriteria typeCode : AlgorithmCriteria.values()) {
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
	public static AlgorithmCriteria getCodeObj(String code) {
		for (AlgorithmCriteria typeCode : AlgorithmCriteria.values()) {
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
		return AlgorithmCriteria.values()[0].getCatValue();
	}

	/**
	* 获取代码项的分类代码
	* @return
	*/
	public static String getObjCatCode(){
		return AlgorithmCriteria.values()[0].getCatCode();
	}

	/**
	* 禁止使用类的tostring()方法
	* @return
	*/
	public String toString() {
		throw new AppSystemException("There's no need for you to !");
	}
}
