package hrds.commons.entity;
/**
 * Auto Created by VBScript Do not modify!
 */

import hrds.commons.entity.fdentity.ProjectTableEntity;

import java.util.HashSet;
import java.util.Set;

/**
 * 数据集市信息表
 */
public class Data_mart_info extends ProjectTableEntity {
	public static final String TableName = "data_mart_info";
	private Long data_mart_id; //数据集市id
	private String mart_name; //数据集市名称
	private String mart_desc; //数据集市描述
	private String mart_storage_path; //数据集市存储路径
	private String remark; //备注
	private Long create_id; //用户ID
	private String create_date; //创建日期
	private String create_time; //创建时间
	private String mart_number; //数据库编号

	/**
	 * 取得：数据集市id
	 */
	public Long getData_mart_id() {
		return data_mart_id;
	}

	/**
	 * 设置：数据集市id
	 */
	public void setData_mart_id(Long data_mart_id) {
		this.data_mart_id = data_mart_id;
	}

	/**
	 * 设置：数据集市id
	 */
	public void setData_mart_id(String data_mart_id) {
		if (!fd.ng.core.utils.StringUtil.isEmpty(data_mart_id))
			this.data_mart_id = new Long(data_mart_id);
	}

	/**
	 * 取得：数据集市名称
	 */
	public String getMart_name() {
		return mart_name;
	}

	/**
	 * 设置：数据集市名称
	 */
	public void setMart_name(String mart_name) {
		this.mart_name = mart_name;
	}

	/**
	 * 取得：数据集市描述
	 */
	public String getMart_desc() {
		return mart_desc;
	}

	/**
	 * 设置：数据集市描述
	 */
	public void setMart_desc(String mart_desc) {
		this.mart_desc = mart_desc;
	}

	/**
	 * 取得：数据集市存储路径
	 */
	public String getMart_storage_path() {
		return mart_storage_path;
	}

	/**
	 * 设置：数据集市存储路径
	 */
	public void setMart_storage_path(String mart_storage_path) {
		this.mart_storage_path = mart_storage_path;
	}

	/**
	 * 取得：备注
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * 设置：备注
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * 取得：用户ID
	 */
	public Long getCreate_id() {
		return create_id;
	}

	/**
	 * 设置：用户ID
	 */
	public void setCreate_id(Long create_id) {
		this.create_id = create_id;
	}

	/**
	 * 设置：用户ID
	 */
	public void setCreate_id(String create_id) {
		if (!fd.ng.core.utils.StringUtil.isEmpty(create_id))
			this.create_id = new Long(create_id);
	}

	/**
	 * 取得：创建日期
	 */
	public String getCreate_date() {
		return create_date;
	}

	/**
	 * 设置：创建日期
	 */
	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}

	/**
	 * 取得：创建时间
	 */
	public String getCreate_time() {
		return create_time;
	}

	/**
	 * 设置：创建时间
	 */
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	/**
	 * 取得：数据库编号
	 */
	public String getMart_number() {
		return mart_number;
	}

	/**
	 * 设置：数据库编号
	 */
	public void setMart_number(String mart_number) {
		this.mart_number = mart_number;
	}

	private Set primaryKeys = new HashSet();

	public boolean isPrimaryKey(String name) {
		return primaryKeys.contains(name);
	}

	public String getPrimaryKey() {
		return primaryKeys.iterator().next().toString();
	}

	/**
	 * 数据集市信息表
	 */
	public Data_mart_info() {
		primaryKeys.add("data_mart_id");
	}
}
