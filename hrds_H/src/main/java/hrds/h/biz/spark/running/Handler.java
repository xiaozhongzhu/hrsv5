package hrds.h.biz.spark.running;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.io.Serializable;

/**
 * @author mick
 * @title: Handle
 * @projectName hrsv5
 * @description: TODO
 * @date 20-4-13下午6:00
 */
public abstract class Handler implements Serializable {
    SparkSession spark;
    Dataset<Row> dataset;
    SparkHandleArgument args;
    String tableName;

    Handler(SparkSession spark, Dataset<Row> dataset, SparkHandleArgument args) {
        this.spark = spark;
        this.dataset = dataset;
        this.args = args;
        this.tableName = args.getTableName();
    }

    public abstract void insert() throws Exception;

    public abstract void increment();

    public SparkHandleArgument getArgs() {
        return args;
    }
}
