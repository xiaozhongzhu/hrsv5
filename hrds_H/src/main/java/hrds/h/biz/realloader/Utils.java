package hrds.h.biz.realloader;

import fd.ng.core.utils.StringUtil;
import fd.ng.db.jdbc.DatabaseWrapper;
import hrds.commons.codes.DatabaseType;
import hrds.commons.collection.ConnectionTool;
import hrds.commons.collection.bean.DbConfBean;
import hrds.commons.entity.Datatable_field_info;
import hrds.h.biz.config.MarketConfUtils;
import hrds.h.biz.spark.running.SparkHandleArgument;

import java.util.List;
import java.util.stream.Collectors;

import static hrds.commons.utils.Constant.*;
import static hrds.commons.utils.Constant.EDATENAME;

/**
 * 一些共用的方法
 *
 * @Author: Mick Yuan
 * @Date:
 * @Since jdk1.8
 */
public class Utils {

    static String buildCreateTableColumnTypes(List<Datatable_field_info> fields, boolean isDatabase) {

        final StringBuilder createTableColumnTypes = new StringBuilder(300);
        fields.forEach(field -> {

            createTableColumnTypes
                    .append(field.getField_en_name())
                    .append(" ").append(field.getField_type());

            String fieldLength = field.getField_length();
            if (StringUtil.isNotBlank(fieldLength)) {
                createTableColumnTypes.append("(")
                        .append(fieldLength).append(")");
            }

            createTableColumnTypes.append(",");

        });
        //把最后一个逗号给删除掉
        createTableColumnTypes.deleteCharAt(createTableColumnTypes.length() - 1);

        //如果是database类型 则类型为定长char类型，否则为string类型（默认）
        if (isDatabase) {
            String str = MarketConfUtils.DEFAULT_STRING_TYPE;
            String s = createTableColumnTypes.toString();
            s = StringUtil.replaceLast(s, str, "char(32)");
            s = StringUtil.replaceLast(s, str, "char(8)");
            s = StringUtil.replaceLast(s, str, "char(8)");
            return s;
        }
        return createTableColumnTypes.toString();


    }

    /**
     * 不带有 hyren 字段的所有字段，以逗号隔开
     *
     * @return
     */
    static String columns(List<Datatable_field_info> fields) {
        //后面的三个hyren字段去掉
        return fields
                .stream()
                .map(Datatable_field_info::getField_en_name)
                .collect(Collectors.joining(","));
    }

    /**
     * 恢复关系型数据库的数据到上次跑批结果
     *
     * @param db
     */
    static void restoreDatabaseData(DatabaseWrapper db, String tableName, String etlDate) {
        if (db.isExistTable(tableName)) {
            db.execute(String.format("DELETE FROM %s WHERE %s = %s",
                    tableName, SDATENAME, etlDate));

            db.execute(String.format("UPDATE %s SET %s = %s WHERE %s = %s",
                    tableName, EDATENAME, MAXDATE, EDATENAME, etlDate));
        }
    }

    /**
     * 注册并返回MD5函数的方法名
     * 如果数据库自带MD5函数，则直接返回数据库自带函数名
     * pgsql自带 md5
     * oracle 写存储过程实现MD5函数，函数名为 HYREN_MD5
     * // TODO 未实现从某个数字开始自增
     *
     * @param db
     * @param type
     * @return
     */
    static String registerMd5Function(DatabaseWrapper db, DatabaseType type) {
        if (DatabaseType.Oracle10g.equals(type)) {
            db.execute("CREATE OR REPLACE FUNCTION HYREN_MD5(passwd IN VARCHAR2) \n" +
                    "RETURN VARCHAR2\n" +
                    "IS\n" +
                    " retval varchar2(32);\n" +
                    "BEGIN\n" +
                    " retval := utl_raw.cast_to_raw(DBMS_OBFUSCATION_TOOLKIT.MD5(INPUT_STRING => passwd));\n" +
                    " RETURN retval;\n" +
                    "END;");
            return "HYREN_MD5";
        }
        return "MD5";
    }

    static String registerAutoIncreasingFunction(DatabaseWrapper db, DatabaseType type) {
        if (DatabaseType.Oracle10g.equals(type)) {
            // TODO 实现自增序列函数并返回函数名
        }
        return "auto_id";
    }

    public static DatabaseWrapper getDb(SparkHandleArgument.DatabaseArgs databaseArgs) {
        //jdbc连接数据库进行更新
        DbConfBean dbConfBean = new DbConfBean();
        dbConfBean.setDatabase_drive(databaseArgs.getDriver());
        dbConfBean.setJdbc_url(databaseArgs.getUrl());
        dbConfBean.setUser_name(databaseArgs.getUser());
        dbConfBean.setDatabase_pad(databaseArgs.getPassword());
        dbConfBean.setDatabase_type(databaseArgs.getDatabaseType());
        return ConnectionTool.getDBWrapper(dbConfBean);
    }

    /**
     * 创建表
     * 如果表存在就删除掉
     */
    static void forceCreateTable(DatabaseWrapper db, String tableName, List<Datatable_field_info> fields) {
        String createTableColumnTypes = buildCreateTableColumnTypes(fields, true);
        if (db.isExistTable(tableName)) {
            db.execute("DROP TABLE " + tableName);
        }
        String createSql = "CREATE TABLE " + tableName + " (" + createTableColumnTypes + ")";
        db.execute(createSql);
    }


}