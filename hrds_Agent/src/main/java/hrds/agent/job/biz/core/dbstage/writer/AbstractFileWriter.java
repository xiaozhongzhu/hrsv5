package hrds.agent.job.biz.core.dbstage.writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * ClassName: AbstractFileWriter <br/>
 * Function: 数据库直连采集以指定的格式将数据卸到指定的数据文件. <br/>
 * Reason: 接口适配器，抽象类
 * Date: 2019/8/1 15:24 <br/>
 * <p>
 * Author WangZhengcheng
 * Version 1.0
 * Since JDK 1.8
 **/
public class AbstractFileWriter implements FileWriterInterface {
	private final static Logger LOGGER = LoggerFactory.getLogger(AbstractFileWriter.class);

	/**
	 * 根据数据元信息和ResultSet，写指定格式的数据文件,这是一个空实现，留给每个具体的实现类去实现
	 *
	 * @Param: metaDataMap Map<String, Object>
	 *         含义：包含有列元信息，清洗规则的map
	 *         取值范围：不为空，共有7对Entry，key分别为
	 *                      columnsTypeAndPreci：表示列数据类型(长度/精度)
	 *                      columnsLength : 列长度，在生成信号文件的时候需要使用
	 *                      columns : 列名
	 *                      colTypeArr : 列数据类型(java.sql.Types),用于判断，对不同数据类型做不同处理
	 *                      columnCount ：该表的列的数目
	 *                      columnCleanRule ：该表每列的清洗规则
	 *                      tableCleanRule ：整表清洗规则
	 * @Param: rs ResultSet
	 *         含义：当前线程执行分页SQL得到的结果集
	 *         取值范围：不为空
	 * @Param: tableName String
	 *         含义：表名, 用于大字段数据写avro
	 *         取值范围：不为空
	 *
	 * @return: String
	 *          含义：生成的数据文件的路径
	 *          取值范围：不会为null
	 *
	 * */
	@Override
	public String writeDataAsSpecifieFormat(Map<String, Object> metaDataMap, ResultSet rs, String tableName)
			throws IOException, SQLException {
		throw new IllegalStateException("这是一个空实现");
	}

	/**
	 * 将LONGVARCHAR和CLOB类型转换为字节数组，用于写Avro,在抽象类中实现，请子类不要覆盖这个方法
	 *
	 * 1、将characterStream用BufferedReader进行读取
	 * 2、读取的结果存到ByteArrayOutputStream内置的字节数组中
	 * 3、获得字节数组并返回
	 *
	 * @Param: characterStream Reader
	 *         含义：java.io.Reader形式得到此ResultSet结果集中当前行中指定列的值
	 *         取值范围：不为空
	 *
	 * @return: byte[]
	 *          含义：此ResultSet结果集中当前行中指定列的值转换得到的字节数组
	 *          取值范围：不会为null
	 *
	 * */
	@Override
	public byte[] longvarcharToByte(Reader characterStream) {
		ByteArrayOutputStream bytestream = null;
		BufferedReader in = null;
		byte imgdata[] = null;
		try {
			bytestream = new ByteArrayOutputStream();
			//1、将characterStream用BufferedReader进行读取
			in = new BufferedReader(characterStream);
			int ch;
			//2、读取的结果存到ByteArrayOutputStream内置的字节数组中
			while ((ch = in.read()) != -1) {
				bytestream.write(ch);
			}
			//3、获得字节数组并返回
			imgdata = bytestream.toByteArray();
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		} finally {
			try {
				if(bytestream != null){
					bytestream.close();
				}
				if(in != null){
					in.close();
				}
			} catch (Exception ex) {
				LOGGER.error(ex.getMessage());
			}
		}
		return imgdata;
	}

	/**
	 * 把Blob类型转换为byte字节数组, 用于写Avro，在抽象类中实现，请子类不要覆盖这个方法
	 *
	 * 1、以流的形式获取此Blob实例指定的BLOB值,并获取BufferedInputStream实例
	 * 2、构建用于保存结果的字节数组
	 * 3、从流中读数据并保存到字节数组中
	 *
	 * @Param: blob Blob
	 *         含义：采集得到的Blob类型的列的值
	 *         取值范围：不为空
	 *
	 * @return: byte[]
	 *          含义：采集得到的Blob类型的列的值转换得到的字节数组
	 *          取值范围：不会为null
	 *
	 * */
	@Override
	public byte[] blobToBytes(Blob blob) {
		BufferedInputStream is = null;
		try {
			//1、以流的形式获取此Blob实例指定的BLOB值,并获取BufferedInputStream实例
			is = new BufferedInputStream(blob.getBinaryStream());
			//2、构建用于保存结果的字节数组
			byte[] bytes = new byte[(int) blob.length()];
			int len = bytes.length;
			int offset = 0;
			int read;
			//3、从流中读数据并保存到字节数组中
			while (offset < len && (read = is.read(bytes, offset, len - offset)) >= 0) {
				offset += read;
			}
			return bytes;
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		} finally {
			try {
				if(is != null){
					is.close();
				}
			} catch (IOException e) {
				LOGGER.error(e.getMessage());
			}
		}
		return null;
	}
}
