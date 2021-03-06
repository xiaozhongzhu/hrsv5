package hrds.commons.entity;
/**Auto Created by VBScript Do not modify!*/
import hrds.commons.entity.fdentity.ProjectTableEntity;
import fd.ng.db.entity.anno.Table;
import fd.ng.core.annotation.DocBean;
import java.math.BigDecimal;
import java.util.Set;
import java.util.HashSet;
import java.util.Collections;

/**
 * 仪表板组件关联信息表
 */
@Table(tableName = "auto_asso_info")
public class Auto_asso_info extends ProjectTableEntity
{
	private static final long serialVersionUID = 321566870187324L;
	private transient static final Set<String> __PrimaryKeys;
	public static final String TableName = "auto_asso_info";
	/**
	* 检查给定的名字，是否为主键中的字段
	* @param name String 检验是否为主键的名字
	* @return
	*/
	public static boolean isPrimaryKey(String name) { return __PrimaryKeys.contains(name); } 
	public static Set<String> getPrimaryKeyNames() { return __PrimaryKeys; } 
	/** 仪表板组件关联信息表 */
	static {
		Set<String> __tmpPKS = new HashSet<>();
		__tmpPKS.add("asso_info_id");
		__PrimaryKeys = Collections.unmodifiableSet(__tmpPKS);
	}
	@DocBean(name ="asso_info_id",value="关联信息表id:",dataType = Long.class,required = true)
	private Long asso_info_id;
	@DocBean(name ="serial_number",value="序号:",dataType = Integer.class,required = true)
	private Integer serial_number;
	@DocBean(name ="x_axis_coord",value="X轴坐标:",dataType = Integer.class,required = true)
	private Integer x_axis_coord;
	@DocBean(name ="y_axis_coord",value="Y轴坐标:",dataType = Integer.class,required = true)
	private Integer y_axis_coord;
	@DocBean(name ="length",value="长度:",dataType = Integer.class,required = true)
	private Integer length;
	@DocBean(name ="width",value="宽度:",dataType = Integer.class,required = true)
	private Integer width;
	@DocBean(name ="dashboard_id",value="仪表板id:",dataType = Long.class,required = false)
	private Long dashboard_id;
	@DocBean(name ="component_id",value="组件ID:",dataType = Long.class,required = false)
	private Long component_id;

	/** 取得：关联信息表id */
	public Long getAsso_info_id(){
		return asso_info_id;
	}
	/** 设置：关联信息表id */
	public void setAsso_info_id(Long asso_info_id){
		this.asso_info_id=asso_info_id;
	}
	/** 设置：关联信息表id */
	public void setAsso_info_id(String asso_info_id){
		if(!fd.ng.core.utils.StringUtil.isEmpty(asso_info_id)){
			this.asso_info_id=new Long(asso_info_id);
		}
	}
	/** 取得：序号 */
	public Integer getSerial_number(){
		return serial_number;
	}
	/** 设置：序号 */
	public void setSerial_number(Integer serial_number){
		this.serial_number=serial_number;
	}
	/** 设置：序号 */
	public void setSerial_number(String serial_number){
		if(!fd.ng.core.utils.StringUtil.isEmpty(serial_number)){
			this.serial_number=new Integer(serial_number);
		}
	}
	/** 取得：X轴坐标 */
	public Integer getX_axis_coord(){
		return x_axis_coord;
	}
	/** 设置：X轴坐标 */
	public void setX_axis_coord(Integer x_axis_coord){
		this.x_axis_coord=x_axis_coord;
	}
	/** 设置：X轴坐标 */
	public void setX_axis_coord(String x_axis_coord){
		if(!fd.ng.core.utils.StringUtil.isEmpty(x_axis_coord)){
			this.x_axis_coord=new Integer(x_axis_coord);
		}
	}
	/** 取得：Y轴坐标 */
	public Integer getY_axis_coord(){
		return y_axis_coord;
	}
	/** 设置：Y轴坐标 */
	public void setY_axis_coord(Integer y_axis_coord){
		this.y_axis_coord=y_axis_coord;
	}
	/** 设置：Y轴坐标 */
	public void setY_axis_coord(String y_axis_coord){
		if(!fd.ng.core.utils.StringUtil.isEmpty(y_axis_coord)){
			this.y_axis_coord=new Integer(y_axis_coord);
		}
	}
	/** 取得：长度 */
	public Integer getLength(){
		return length;
	}
	/** 设置：长度 */
	public void setLength(Integer length){
		this.length=length;
	}
	/** 设置：长度 */
	public void setLength(String length){
		if(!fd.ng.core.utils.StringUtil.isEmpty(length)){
			this.length=new Integer(length);
		}
	}
	/** 取得：宽度 */
	public Integer getWidth(){
		return width;
	}
	/** 设置：宽度 */
	public void setWidth(Integer width){
		this.width=width;
	}
	/** 设置：宽度 */
	public void setWidth(String width){
		if(!fd.ng.core.utils.StringUtil.isEmpty(width)){
			this.width=new Integer(width);
		}
	}
	/** 取得：仪表板id */
	public Long getDashboard_id(){
		return dashboard_id;
	}
	/** 设置：仪表板id */
	public void setDashboard_id(Long dashboard_id){
		this.dashboard_id=dashboard_id;
	}
	/** 设置：仪表板id */
	public void setDashboard_id(String dashboard_id){
		if(!fd.ng.core.utils.StringUtil.isEmpty(dashboard_id)){
			this.dashboard_id=new Long(dashboard_id);
		}
	}
	/** 取得：组件ID */
	public Long getComponent_id(){
		return component_id;
	}
	/** 设置：组件ID */
	public void setComponent_id(Long component_id){
		this.component_id=component_id;
	}
	/** 设置：组件ID */
	public void setComponent_id(String component_id){
		if(!fd.ng.core.utils.StringUtil.isEmpty(component_id)){
			this.component_id=new Long(component_id);
		}
	}
}
