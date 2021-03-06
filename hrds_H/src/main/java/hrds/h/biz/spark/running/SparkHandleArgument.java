package hrds.h.biz.spark.running;

import fd.ng.core.utils.JsonUtil;
import hrds.commons.codes.Store_type;
import hrds.commons.exception.AppSystemException;
import hrds.commons.hadoop.hbaseindexer.bean.HbaseSolrField;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: Mick Yuan
 * @Date:
 * @Since jdk1.8
 */
public class SparkHandleArgument implements Serializable {
    /**
     * 处理类型，如 database,hive,hbase,solr.etc
     */
    private Store_type handleType;
    private boolean overWrite;
    /**
     * spark 任务是否为增量， 默认为 false
     */
    private boolean increment = false;
    private boolean isMultipleInput = false;
    private String tableName;
    private String datatableId;
    private String etlDate;

    public String getDatatableId() {
        return datatableId;
    }

    public void setDatatableId(String datatableId) {
        this.datatableId = datatableId;
    }

    public String getEtlDate() {
        return etlDate;
    }

    public void setEtlDate(String etlDate) {
        this.etlDate = etlDate;
    }

    public void setHandleType(Store_type handleType) {
        this.handleType = handleType;
    }

    public final Store_type getHandleType() {
        return handleType;
    }

    public boolean isOverWrite() {
        return overWrite;
    }

    public void setOverWrite(boolean overWrite) {
        this.overWrite = overWrite;
    }

    public boolean isMultipleInput() {
        return isMultipleInput;
    }

    public void setMultipleInput(boolean multipleInput) {
        isMultipleInput = multipleInput;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public boolean isIncrement() {
        return increment;
    }

    public void setIncrement(boolean increment) {
        this.increment = increment;
    }

    @Override
    public String toString() {
        return JsonUtil.toJson(this);
    }

    public static SparkHandleArgument fromString(String handleArgs, Class<? extends SparkHandleArgument> aClass) {

        return JsonUtil.toObjectSafety(handleArgs, aClass)
                .orElseThrow(() -> new AppSystemException(String.format("handle string转对象失败：[%s] -> [%s].",
                        handleArgs, aClass.getSimpleName())));
    }

    /**
     * 数据库相关参数实体
     */
    public static class DatabaseArgs extends SparkHandleArgument {

        String driver;
        String url;
        String user;
        String password;
        String databaseType;
        String database;

        public String getDriver() {
            return driver;
        }

        public void setDriver(String driver) {
            this.driver = driver;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getDatabaseType() {
            return databaseType;
        }

        public void setDatabaseType(String databaseType) {
            this.databaseType = databaseType;
        }

        public String getDatabase() {
            return database;
        }

        public void setDatabase(String database) {
            this.database = database;
        }
    }

    /**
     * hive 相关参数实体
     */
    public static class HiveArgs extends SparkHandleArgument {

        String columns;
        String database;

        public String getDatabase() {
            return database;
        }

        public void setDatabase(String database) {
            this.database = database;
        }

        public String getColumns() {
            return columns;
        }

        public void setColumns(String columns) {
            this.columns = columns;
        }

    }

    /**
     * hbase solr 相关参数实体
     */
    public static class HbaseSolrArgs extends SparkHandleArgument {

        List<String> columns;
        List<String> rowkeys;
        List<String> solrCols;
        List<HbaseSolrField> hbaseSolrFields;

        public List<String> getColumns() {
            return columns;
        }

        public void setColumns(List<String> columns) {
            this.columns = columns;
        }

        public List<String> getRowkeys() {
            return rowkeys;
        }

        public void setRowkeys(List<String> rowkeys) {
            this.rowkeys = rowkeys;
        }

        public List<String> getSolrCols() {
            return solrCols;
        }

        public void setSolrCols(List<String> solrCols) {
            this.solrCols = solrCols;
        }

        public List<HbaseSolrField> getHbaseSolrFields() {
            return hbaseSolrFields;
        }

        public void setHbaseSolrFields(List<HbaseSolrField> hbaseSolrFields) {
            this.hbaseSolrFields = hbaseSolrFields;
        }
    }

    public static class CarbonArgs extends SparkHandleArgument {
        String columns;
        String database;

        public String getDatabase() {
            return database;
        }

        public void setDatabase(String database) {
            this.database = database;
        }

        public String getColumns() {
            return columns;
        }

        public void setColumns(String columns) {
            this.columns = columns;
        }
    }
}
