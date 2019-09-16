package hrds.commons.entity;
/**Auto Created by VBScript Do not modify!*/
import fd.ng.db.entity.TableEntity;
import fd.ng.core.utils.StringUtil;
import fd.ng.db.entity.anno.Column;
import fd.ng.db.entity.anno.Table;
import hrds.commons.apiannotation.ApiBean;
import hrds.commons.exception.BusinessException;
import java.math.BigDecimal;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;

/**
 * 工程登记表
 */
@Table(tableName = "etl_sys")
public class Etl_sys extends TableEntity
{
	private static final long serialVersionUID = 321566870187324L;
	private transient static final Set<String> __PrimaryKeys;
	public static final String TableName = "etl_sys";
	/**
	* 检查给定的名字，是否为主键中的字段
	* @param name String 检验是否为主键的名字
	* @return
	*/
	public static boolean isPrimaryKey(String name) { return __PrimaryKeys.contains(name); } 
	public static Set<String> getPrimaryKeyNames() { return __PrimaryKeys; } 
	/** 工程登记表 */
	static {
		Set<String> __tmpPKS = new HashSet<>();
		__tmpPKS.add("etl_sys_cd");
		__PrimaryKeys = Collections.unmodifiableSet(__tmpPKS);
	}
	@ApiBean(name ="etl_sys_cd",value="工程代码",dataType = String.class,required = true)
	private String etl_sys_cd; //工程代码
	@ApiBean(name ="etl_sys_name",value="工程名称",dataType = String.class,required = true)
	private String etl_sys_name; //工程名称
	@ApiBean(name ="etl_serv_ip",value="etl服务器ip",dataType = String.class,required = false)
	private String etl_serv_ip; //etl服务器ip
	@ApiBean(name ="etl_serv_port",value="etl服务器端口",dataType = String.class,required = false)
	private String etl_serv_port; //etl服务器端口
	@ApiBean(name ="contact_person",value="联系人",dataType = String.class,required = false)
	private String contact_person; //联系人
	@ApiBean(name ="contact_phone",value="联系电话",dataType = String.class,required = false)
	private String contact_phone; //联系电话
	@ApiBean(name ="comments",value="备注信息",dataType = String.class,required = false)
	private String comments; //备注信息
	@ApiBean(name ="curr_bath_date",value="当前批量日期",dataType = String.class,required = false)
	private String curr_bath_date; //当前批量日期
	@ApiBean(name ="bath_shift_time",value="系统日切时间",dataType = String.class,required = false)
	private String bath_shift_time; //系统日切时间
	@ApiBean(name ="main_serv_sync",value="主服务器同步标志",dataType = String.class,required = false)
	private String main_serv_sync; //主服务器同步标志
	@ApiBean(name ="sys_run_status",value="系统状态",dataType = String.class,required = false)
	private String sys_run_status; //系统状态
	@ApiBean(name ="serv_file_path",value="部署服务器路径",dataType = String.class,required = false)
	private String serv_file_path; //部署服务器路径
	@ApiBean(name ="user_name",value="主机服务器用户名",dataType = String.class,required = false)
	private String user_name; //主机服务器用户名
	@ApiBean(name ="user_pwd",value="主机用户密码",dataType = String.class,required = false)
	private String user_pwd; //主机用户密码
	@ApiBean(name ="remarks",value="备注",dataType = String.class,required = false)
	private String remarks; //备注
	@ApiBean(name ="user_id",value="用户ID",dataType = Long.class,required = true)
	private Long user_id; //用户ID

	/** 取得：工程代码 */
	public String getEtl_sys_cd(){
		return etl_sys_cd;
	}
	/** 设置：工程代码 */
	public void setEtl_sys_cd(String etl_sys_cd){
		this.etl_sys_cd=etl_sys_cd;
	}
	/** 取得：工程名称 */
	public String getEtl_sys_name(){
		return etl_sys_name;
	}
	/** 设置：工程名称 */
	public void setEtl_sys_name(String etl_sys_name){
		this.etl_sys_name=etl_sys_name;
	}
	/** 取得：etl服务器ip */
	public String getEtl_serv_ip(){
		return etl_serv_ip;
	}
	/** 设置：etl服务器ip */
	public void setEtl_serv_ip(String etl_serv_ip){
		this.etl_serv_ip=etl_serv_ip;
	}
	/** 取得：etl服务器端口 */
	public String getEtl_serv_port(){
		return etl_serv_port;
	}
	/** 设置：etl服务器端口 */
	public void setEtl_serv_port(String etl_serv_port){
		this.etl_serv_port=etl_serv_port;
	}
	/** 取得：联系人 */
	public String getContact_person(){
		return contact_person;
	}
	/** 设置：联系人 */
	public void setContact_person(String contact_person){
		this.contact_person=contact_person;
	}
	/** 取得：联系电话 */
	public String getContact_phone(){
		return contact_phone;
	}
	/** 设置：联系电话 */
	public void setContact_phone(String contact_phone){
		this.contact_phone=contact_phone;
	}
	/** 取得：备注信息 */
	public String getComments(){
		return comments;
	}
	/** 设置：备注信息 */
	public void setComments(String comments){
		this.comments=comments;
	}
	/** 取得：当前批量日期 */
	public String getCurr_bath_date(){
		return curr_bath_date;
	}
	/** 设置：当前批量日期 */
	public void setCurr_bath_date(String curr_bath_date){
		this.curr_bath_date=curr_bath_date;
	}
	/** 取得：系统日切时间 */
	public String getBath_shift_time(){
		return bath_shift_time;
	}
	/** 设置：系统日切时间 */
	public void setBath_shift_time(String bath_shift_time){
		this.bath_shift_time=bath_shift_time;
	}
	/** 取得：主服务器同步标志 */
	public String getMain_serv_sync(){
		return main_serv_sync;
	}
	/** 设置：主服务器同步标志 */
	public void setMain_serv_sync(String main_serv_sync){
		this.main_serv_sync=main_serv_sync;
	}
	/** 取得：系统状态 */
	public String getSys_run_status(){
		return sys_run_status;
	}
	/** 设置：系统状态 */
	public void setSys_run_status(String sys_run_status){
		this.sys_run_status=sys_run_status;
	}
	/** 取得：部署服务器路径 */
	public String getServ_file_path(){
		return serv_file_path;
	}
	/** 设置：部署服务器路径 */
	public void setServ_file_path(String serv_file_path){
		this.serv_file_path=serv_file_path;
	}
	/** 取得：主机服务器用户名 */
	public String getUser_name(){
		return user_name;
	}
	/** 设置：主机服务器用户名 */
	public void setUser_name(String user_name){
		this.user_name=user_name;
	}
	/** 取得：主机用户密码 */
	public String getUser_pwd(){
		return user_pwd;
	}
	/** 设置：主机用户密码 */
	public void setUser_pwd(String user_pwd){
		this.user_pwd=user_pwd;
	}
	/** 取得：备注 */
	public String getRemarks(){
		return remarks;
	}
	/** 设置：备注 */
	public void setRemarks(String remarks){
		this.remarks=remarks;
	}
	/** 取得：用户ID */
	public Long getUser_id(){
		return user_id;
	}
	/** 设置：用户ID */
	public void setUser_id(Long user_id){
		this.user_id=user_id;
	}
	/** 设置：用户ID */
	public void setUser_id(String user_id){
		if(!fd.ng.core.utils.StringUtil.isEmpty(user_id)){
			this.user_id=new Long(user_id);
		}
	}
}
