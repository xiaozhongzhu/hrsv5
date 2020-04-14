package hrds.commons.collection;

import fd.ng.core.annotation.DocClass;
import fd.ng.core.annotation.Method;
import fd.ng.core.annotation.Param;
import fd.ng.core.annotation.Return;
import fd.ng.db.jdbc.DatabaseWrapper;
import fd.ng.db.jdbc.SqlOperator;
import hrds.commons.codes.CollectType;
import hrds.commons.codes.DataSourceType;
import hrds.commons.codes.Store_type;
import hrds.commons.collection.bean.LayerBean;
import hrds.commons.collection.bean.LayerTypeBean;
import hrds.commons.entity.*;
import hrds.commons.exception.AppSystemException;
import hrds.commons.utils.DruidParseQuerySql;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.*;

@DocClass(desc = "数据处理类，获取表的存储等信息", author = "xchao", createdate = "2020年3月31日 16:32:43")
public abstract class ProcessingData {


	@Method(desc = "根据表面获取该表相应的存储信息", logicStep = "1")
	@Param(name = "tableName", desc = "数据表明", range = "只能是集市、采集等表中的tablename字段")
	@Param(name = "db", desc = "数据库db操作对象", range = "不可为空")
	@Return(desc = "查询出来的rs", range = "数据")
	public void getDataLayer(String sql, DatabaseWrapper db) {

		LayerTypeBean ltb = getAllTableIsLayer(sql, db);
		//只有一个存储，且是jdbc的方式
		if (ltb.getConnType() == LayerTypeBean.ConnPyte.oneJdbc) {
			Long dsl_id = ltb.getLayerBean().getDsl_id();
			getResultSet(sql, db, dsl_id);
		}
		//只有一种存储，是什么，可以使用ltb.getLayerBean().getStore_type(),进行判断
		else if (ltb.getConnType() == LayerTypeBean.ConnPyte.oneOther) {
			//ltb.getLayerBean().getStore_type();
			//TODO 数据在一种存介质中，但不是jdbc
		}
		//有多种存储，但都支持JDBC，是否可以使用dblink的方式
		else if (ltb.getConnType() == LayerTypeBean.ConnPyte.moreJdbc) {
			//List<LayerBean> layerBeanList = ltb.getLayerBeanList();
			//TODO 数据都在关系型数据库，也就说都可以使用jdbc的方式的实现方式
		}
		// 其他，包含了不同的存储，如jdbc、hbase、solr等不同给情况
		else if (ltb.getConnType() == LayerTypeBean.ConnPyte.moreOther) {
			//List<LayerBean> layerBeanList = ltb.getLayerBeanList();
			// TODO 混搭模式
		}
	}
	/**
	 * 获取表的存储位置
	 *
	 * @param tableName {@link String} 表名
	 * @param db        {@link DatabaseWrapper} db
	 */
	private static List<LayerBean> getTableLayer(String tableName, DatabaseWrapper db) {
		List<LayerBean> mapTaberLayer = new ArrayList<>();
		/**
		 * 查询贴元表信息，也就是通过数据采集过来的数据表
		 */
		SqlOperator.Assembler asmSql = SqlOperator.Assembler.newInstance()
				.addSql("select * from " + Data_store_reg.TableName + " where collect_type in (?,?) and hyren_name = ?")
				.addParam(CollectType.DBWenJianCaiJi.getCode()).addParam(CollectType.ShuJuKuCaiJi.getCode())
				.addParam(tableName);
		Optional<Data_store_reg> opdsr = SqlOperator.queryOneObject(db, Data_store_reg.class, asmSql.sql(), asmSql.params());
		if (opdsr.isPresent()) {
			Data_store_reg dsr = opdsr.get();
			Long table_id = dsr.getTable_id();
			List<LayerBean> maps = SqlOperator.queryList(db, LayerBean.class,
					"select dsl.dsl_id,dsl.dsl_name,dsl.store_type,'" + DataSourceType.DCL.getCode() + "'  as dst from "
							+ Table_storage_info.TableName + " tsi join " + Data_relation_table.TableName + " drt "
							+ "on tsi.storage_id = drt.storage_id join " + Data_store_layer.TableName + " dsl "
							+ "on drt.dsl_id = dsl.dsl_id where tsi.table_id = ?", table_id);
			//记录数据表在哪个系统存储层
			for (LayerBean map : maps) {
				map.setLayerAttr(getDslidByLayer(map.getDsl_id(), db));
				mapTaberLayer.add(map);
			}
			return mapTaberLayer;
		}
		/**
		 * 查询集市表信息，通过数据集市产生的数表
		 */
		List<LayerBean> dslMap = SqlOperator.queryList(db, LayerBean.class,
				"select dsl.dsl_id,dsl.dsl_name,dsl.store_type ,'" + DataSourceType.DML.getCode() + "' as dst from "
						+ Dm_datatable.TableName + " dd join  " + Dm_relation_datatable.TableName + " drd " +
						"on dd.datatable_id = drd.datatable_id join " + Data_store_layer.TableName + " dsl " +
						"on drd.dsl_id = dsl.dsl_id where datatable_en_name = ?", tableName);
		if (dslMap.size() != 0) {
			for (LayerBean map : dslMap) {
				map.setLayerAttr(getDslidByLayer(map.getDsl_id(), db));
				mapTaberLayer.add(map);
			}
			return mapTaberLayer;
		}
		/**
		 * TODO 这里以后需要添加加工数据、机器学习、流数据、系统管理维护的表、系统管理等
		 */
		return null;
	}

	/**
	 * 根据表名获取存储层的信息
	 * @param tableName
	 * @param db
	 */
	public static List<LayerBean> getLayerByTable(String tableName, DatabaseWrapper db) {
		return getTableLayer(tableName, db);
	}
	/**
	 * 根据表名获取存储层的信息
	 * @param tableNameList
	 * @param db
	 */
	public static  Map<String, List<LayerBean>> getLayerByTable(List<String> tableNameList, DatabaseWrapper db) {
		Map<String,List<LayerBean>> laytable = new HashMap<>();
		for (String tableName : tableNameList) {
			List<LayerBean> layerByTable = getLayerByTable(tableName, db);
			laytable.put(tableName,layerByTable);
		}
		return laytable;
	}

	/**
	 * 根据存储层ID获取存储层的配置信息
	 *
	 * @param dsl_id {@link Long} 存储层id
	 * @param db     {@link DatabaseWrapper} db
	 * @return
	 */
	private static Map<String, String> getDslidByLayer(Long dsl_id, DatabaseWrapper db) {
		List<Map<String, Object>> dataStoreConfBean =
				SqlOperator.queryList(db, "select * from data_store_layer_attr where dsl_id = ?", dsl_id);
		Map<String, String> layerMap = ConnectionTool.getLayerMap(dataStoreConfBean);
		return layerMap;
	}

	/**
	 * @param sql
	 * @param db
	 * @return
	 */
	public static LayerTypeBean getAllTableIsLayer(String sql, DatabaseWrapper db) {
		/**
		 * 一、判断所有的表是不是使用了同一个存储层，直接使用sql的方式就可以完成
		 * 二、判断所有的表是不是都使用的jdbc的方式
		 * 三、其他，包含了不同的存储，如jdbc、hbase、solr等不同给情况
		 */
		List<String> listTable = DruidParseQuerySql.parseSqlTableToList(sql);
		/**
		 * 1、使用存储ID为key记录每个存储层下面有多少张表
		 * 获取所有表的存储层，记录所有表的存储层ID
		 */
		Map<String, LayerBean> allTableLayer = new HashMap<>();
		for (String tableName : listTable) {
			List<LayerBean> tableLayer = getTableLayer(tableName, db);
			if(tableLayer == null)
				throw new AppSystemException("根据接卸的表没有找到对应存储层信息，请确认数据是否正确");
			//更加table获取每张表不同的存储信息，有可能一张表存储不同的目的地，所以这里是list
			for (LayerBean objectMap : tableLayer) {
				String layer_id = String.valueOf(objectMap.getDsl_id());
				LayerBean layerBean = allTableLayer.get(layer_id) == null ? objectMap : allTableLayer.get(layer_id);
				/**
				 * 根据id获取map中是否有存储层信息，如果没有，直接添加表的list将存储层信息添加
				 * 如果有，将表表的信息添加的list中就ok
				 */
				Set<String> tableNameList = layerBean.getTableNameList() == null ? new HashSet<>() : layerBean.getTableNameList();
				tableNameList.add(tableName);
				layerBean.setTableNameList(tableNameList);
				allTableLayer.put(layer_id, layerBean);
			}
		}
		/**
		 * 2、计算所有的包的存储目的地，是否是可以通用等方式
		 * 这里开始判断每个表在哪里存储
		 */
		LayerTypeBean layerTypeBean = new LayerTypeBean();
		List<LayerBean> list = new ArrayList<>();
		Iterator<String> iter = allTableLayer.keySet().iterator();
		Set<LayerTypeBean.ConnPyte> setconn = new HashSet<>();
		while (iter.hasNext()) {
			String key = iter.next();
			LayerBean objectMap = allTableLayer.get(key);
			String store_type = objectMap.getStore_type();
			Set<String> tableNameList = objectMap.getTableNameList();
			/**
			 * 1、如果有一个list中的表个数和解析的表个数一样，也就是说所有的表都在一个存储层中存在，所有直接用这个存储层的信息即可
			 * 2、如果不一样，所有的都是jdbc
			 */
			if (tableNameList.size() == listTable.size()) {
				if (Store_type.DATABASE == Store_type.ofEnumByCode(store_type) || Store_type.HIVE == Store_type.ofEnumByCode(store_type))
					layerTypeBean.setConnType(LayerTypeBean.ConnPyte.oneJdbc);
				else
					layerTypeBean.setConnType(LayerTypeBean.ConnPyte.oneOther);
				layerTypeBean.setLayerBean(objectMap);
				return layerTypeBean;
			}
			//2、判断是不是都支持jdbc，是否可以使用dblink的方式进行使用
			if (Store_type.DATABASE == Store_type.ofEnumByCode(store_type) || Store_type.HIVE == Store_type.ofEnumByCode(store_type)) {
				setconn.add(LayerTypeBean.ConnPyte.moreJdbc);
			} else {
				setconn.add(LayerTypeBean.ConnPyte.moreOther);
			}
			list.add(objectMap);
			layerTypeBean.setLayerBeanList(list);
		}
		//set有一个，且是morejdbc，就是有过的且全部是jdbc的方式
		if (setconn.size() == 1 && setconn.contains(LayerTypeBean.ConnPyte.moreJdbc))
			layerTypeBean.setConnType(LayerTypeBean.ConnPyte.moreJdbc);
		else
			layerTypeBean.setConnType(LayerTypeBean.ConnPyte.moreOther);
		return layerTypeBean;
	}


	//************************************************具体的实现********************************************************/

	/**
	 * 实现数据库查询的方式
	 *
	 * @param sql    {@like String} 查询的sql语句
	 * @param db
	 * @param dsl_id
	 */
	private void getResultSet(String sql, DatabaseWrapper db, long dsl_id) {
		List<Map<String, Object>> dataStoreConfBean = SqlOperator.queryList(db,
				"select * from data_store_layer_attr where dsl_id = ?", dsl_id);
		try (DatabaseWrapper dbDataConn = ConnectionTool.getDBWrapper(dataStoreConfBean)) {
			ResultSet rs = dbDataConn.queryGetResultSet(sql);
			ResultSetMetaData meta = rs.getMetaData();
			int cols = meta.getColumnCount();
			while (rs.next()) {
				Map<String, Object> result = new HashMap<String, Object>();
				for (int i = 0; i < cols; i++) {
					String columnName = meta.getColumnName(i + 1).toLowerCase();
					result.put(columnName, rs.getObject(i + 1));
				}
				dealLine(result);
			}
		} catch (Exception e) {
			throw new AppSystemException("系统不支持该数据库类型", e);
		}
	}

	public abstract void dealLine(Map<String, Object> map) throws Exception;
}