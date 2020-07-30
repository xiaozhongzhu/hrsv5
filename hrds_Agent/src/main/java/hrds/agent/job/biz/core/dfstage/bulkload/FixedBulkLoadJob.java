package hrds.agent.job.biz.core.dfstage.bulkload;

import fd.ng.core.annotation.DocClass;
import fd.ng.core.utils.StringUtil;
import hrds.agent.job.biz.constant.JobConstant;
import hrds.commons.codes.IsFlag;
import hrds.commons.exception.AppSystemException;
import hrds.commons.hadoop.hadoop_helper.HBaseHelper;
import hrds.commons.hadoop.readconfig.ConfigReader;
import hrds.commons.hadoop.utils.BKLoadUtil;
import hrds.commons.utils.Constant;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.HFileOutputFormat2;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.MRJobConfig;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@DocClass(author = "zxz", desc = "定长文件采用bulkLoad方式加载到hbase", createdate = "2020/07/17")
public class FixedBulkLoadJob extends Configured implements Tool {

	private static final Log log = LogFactory.getLog(FixedBulkLoadJob.class);

	public static class BulkLoadMap extends Mapper<LongWritable, Text, ImmutableBytesWritable, Put> {

		private List<byte[]> headByte = null;
		private List<Integer> rowKeyIndex = null;
		private boolean isMd5 = false;
		private List<Integer> everyColLength = null;
		private StringBuilder sb = new StringBuilder();
		private String code;
		private boolean is_header = false;

		@Override
		protected void setup(Context context) {
			Configuration conf = context.getConfiguration();
			List<String> columnList = StringUtil.split(conf.get("columnMetaInfo"), Constant.METAINFOSPLIT);
			headByte = new ArrayList<>(columnList.size());
			columnList.forEach(column -> headByte.add(column.getBytes()));
			isMd5 = conf.get("isMd5").equals(IsFlag.Shi.getCode());
			List<String> rowKeyIndexList = StringUtil.split(conf.get("rowKeyIndex"), Constant.METAINFOSPLIT);
			rowKeyIndex = new ArrayList<>(rowKeyIndexList.size());
			rowKeyIndexList.forEach(index -> rowKeyIndex.add(Integer.valueOf(index)));
			List<String> colLengthInfoList = StringUtil.split(conf.get("colLengthInfo"), Constant.METAINFOSPLIT);
			everyColLength = new ArrayList<>(colLengthInfoList.size());
			rowKeyIndexList.forEach(index -> everyColLength.add(Integer.valueOf(index)));
			code = conf.get("code");
			is_header = IsFlag.Shi.getCode().equals(conf.get("is_header"));
		}

		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			if ("0".equals(key.toString()) && is_header) {
				log.info("包含表头，第一行不处理");
				return;
			}
			String values = value.toString();
			List<String> lineList = getDingChangValueList(values, everyColLength, code);
			String row_key;
			//自己算md5
			if (isMd5) {
				for (int index : rowKeyIndex) {
					sb.append(lineList.get(index));
				}
				row_key = DigestUtils.md5Hex(sb.toString());
				sb.delete(0, sb.length());
			} else {
				for (int index : rowKeyIndex) {
					sb.append(lineList.get(index));
				}
				row_key = sb.toString();
				sb.delete(0, sb.length());
			}
			ImmutableBytesWritable rowkey = new ImmutableBytesWritable(row_key.getBytes());
			Put put = new Put(Bytes.toBytes(row_key));
			for (int i = 0; i < headByte.size(); i++) {
				put.addColumn(Constant.HBASE_COLUMN_FAMILY, headByte.get(i), Bytes.toBytes(lineList.get(i)));
			}
			context.write(rowkey, put);
		}

		private List<String> getDingChangValueList(String line, List<Integer> lengthList, String database_code) {
			try {
				List<String> valueList = new ArrayList<>();
				byte[] bytes = line.getBytes(database_code);
				int begin = 0;
				for (int length : lengthList) {
					byte[] byteTmp = new byte[length];
					System.arraycopy(bytes, begin, byteTmp, 0, length);
					begin += length;
					valueList.add(new String(byteTmp, database_code));
				}
				return valueList;
			} catch (UnsupportedEncodingException e) {
				throw new AppSystemException("bulkload解析定长文件错误", e);
			}
		}
	}

	public int run(String[] args) throws Exception {

		String todayTableName = args[0];
		String hdfsFilePath = args[1];
		String columnMetaInfo = args[2];
		String rowKeyIndex = args[3];
		String configPath = args[4];
		String etlDate = args[5];
		String isMd5 = args[6];
		String hadoop_user_name = args[7];
		String platform = args[8];
		String prncipal_name = args[9];
		String code = args[10];
		String colLengthInfo = args[11];
		String is_header = args[12];
		log.info("Arguments: " + todayTableName + "  " + hdfsFilePath + "  " + columnMetaInfo + "  " + rowKeyIndex
				+ "  " + configPath + "  " + etlDate + "  " + isMd5 + "  " + hadoop_user_name + "  " + platform
				+ "  " + prncipal_name + "  " + code + "  " + colLengthInfo + "  " + is_header);
		Configuration conf = ConfigReader.getConfiguration(configPath, platform, prncipal_name, hadoop_user_name);
		try (HBaseHelper helper = HBaseHelper.getHelper(conf)) {
			conf.set("columnMetaInfo", columnMetaInfo);
			conf.set("colLengthInfo", colLengthInfo);
			conf.set("etlDate", etlDate);
			conf.set("isMd5", isMd5);
			conf.set("rowKeyIndex", rowKeyIndex);
			conf.set("is_header", is_header);
			conf.set(MRJobConfig.QUEUE_NAME, "root." + hadoop_user_name);
			conf.set("code", code);
			Job job = Job.getInstance(conf, "FixedBulkLoadJob_" + todayTableName);
			job.setJarByClass(FixedBulkLoadJob.class);

			Path input = new Path(hdfsFilePath);
			FileInputFormat.addInputPath(job, input);

			String outputPath = JobConstant.TMPDIR + "/bulkload_fixed_length/output" + System.currentTimeMillis();
			Path tmpPath = new Path(outputPath);
			FileOutputFormat.setOutputPath(job, tmpPath);

			job.setInputFormatClass(TextInputFormat.class);
			job.setMapperClass(BulkLoadMap.class);

			job.setMapOutputKeyClass(ImmutableBytesWritable.class);
			job.setMapOutputValueClass(Put.class);
			job.setOutputFormatClass(HFileOutputFormat2.class);

			int resultCode = BKLoadUtil.jobOutLoader(todayTableName, tmpPath, helper, job);
			//delete the hfiles
			try (FileSystem fs = FileSystem.get(conf)) {
				fs.delete(tmpPath, true);
			}

			return resultCode;

		}
	}

	public static void main(String[] args) throws Exception {

		int exitCode = ToolRunner.run(ConfigReader.getConfiguration(), new CsvBulkLoadJob(), args);
		System.exit(exitCode);
	}
}
