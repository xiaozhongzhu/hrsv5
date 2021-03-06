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
 * 数据表存储关系表
 */
@Table(tableName = "dtab_relation_store")
public class Dtab_relation_store extends ProjectTableEntity
{
	private static final long serialVersionUID = 321566870187324L;
	private transient static final Set<String> __PrimaryKeys;
	public static final String TableName = "dtab_relation_store";
	/**
	* 检查给定的名字，是否为主键中的字段
	* @param name String 检验是否为主键的名字
	* @return
	*/
	public static boolean isPrimaryKey(String name) { return __PrimaryKeys.contains(name); } 
	public static Set<String> getPrimaryKeyNames() { return __PrimaryKeys; } 
	/** 数据表存储关系表 */
	static {
		Set<String> __tmpPKS = new HashSet<>();
		__tmpPKS.add("dsl_id");
		__tmpPKS.add("tab_id");
		__PrimaryKeys = Collections.unmodifiableSet(__tmpPKS);
	}
	@DocBean(name ="is_successful",value="是否入库成功(JobExecuteState):100-等待<DengDai> 101-运行<YunXing> 102-暂停<ZanTing> 103-中止<ZhongZhi> 104-完成<WanCheng> 105-失败<ShiBai> ",dataType = String.class,required = false)
	private String is_successful;
	@DocBean(name ="dsl_id",value="存储层配置ID:",dataType = Long.class,required = true)
	private Long dsl_id;
	@DocBean(name ="tab_id",value="对象采集任务编号:",dataType = Long.class,required = true)
	private Long tab_id;
	@DocBean(name ="data_source",value="存储层-数据来源(StoreLayerDataSource):1-db采集<DB> 2-数据库采集<DBA> 3-对象采集<OBJ> 4-数据集市<DM> 5-数据管控<DQ> 6-自定义<UD> ",dataType = String.class,required = true)
	private String data_source;

	/** 取得：是否入库成功 */
	public String getIs_successful(){
		return is_successful;
	}
	/** 设置：是否入库成功 */
	public void setIs_successful(String is_successful){
		this.is_successful=is_successful;
	}
	/** 取得：存储层配置ID */
	public Long getDsl_id(){
		return dsl_id;
	}
	/** 设置：存储层配置ID */
	public void setDsl_id(Long dsl_id){
		this.dsl_id=dsl_id;
	}
	/** 设置：存储层配置ID */
	public void setDsl_id(String dsl_id){
		if(!fd.ng.core.utils.StringUtil.isEmpty(dsl_id)){
			this.dsl_id=new Long(dsl_id);
		}
	}
	/** 取得：对象采集任务编号 */
	public Long getTab_id(){
		return tab_id;
	}
	/** 设置：对象采集任务编号 */
	public void setTab_id(Long tab_id){
		this.tab_id=tab_id;
	}
	/** 设置：对象采集任务编号 */
	public void setTab_id(String tab_id){
		if(!fd.ng.core.utils.StringUtil.isEmpty(tab_id)){
			this.tab_id=new Long(tab_id);
		}
	}
	/** 取得：存储层-数据来源 */
	public String getData_source(){
		return data_source;
	}
	/** 设置：存储层-数据来源 */
	public void setData_source(String data_source){
		this.data_source=data_source;
	}
}
