package hrds.b.biz.fulltextsearch;

import fd.ng.core.annotation.Method;
import fd.ng.core.utils.DateUtil;
import fd.ng.core.utils.JsonUtil;
import fd.ng.db.jdbc.DatabaseWrapper;
import fd.ng.db.jdbc.SqlOperator;
import fd.ng.netclient.http.HttpClient;
import fd.ng.web.action.ActionResult;
import hrds.commons.entity.Department_info;
import hrds.commons.entity.Sys_user;
import hrds.commons.entity.User_fav;
import hrds.commons.exception.BusinessException;
import hrds.testbase.WebBaseTestCase;
import org.junit.*;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * <p>类名: FullTextSearchActionTest</p>
 * <p>类说明: 全文检索数据查询测试类</p>
 *
 * @author BY-HLL
 * @date 2019/10/8 0008 下午 03:09
 * @since OpenJDK 1.8
 */
public class FullTextSearchActionTest extends WebBaseTestCase {
	//测试数据的用户ID
	private final long USER_ID = 5000L;
	//测试数据 user_fav 的ID
	private final long FAV_ID = 5000000000L;
	//测试数据的部门ID
	private final long DEP_ID = 5000000000L;
	//当前线程的id
	private final long THREAD_ID = Thread.currentThread().getId();

	@Method(desc = "初始化测试用例依赖表数据",
			logicStep = "1.初始化数据" +
					"1-1.初始化 Sys_user 数据" +
					"1-2.初始化 User_fav 数据" +
					"1-3.初始化 Department_info 数据" +
					"2.提交所有数据库执行操作" +
					"3.根据初始化的 Sys_user 用户模拟登陆")
	@Before
	public void before() {
		try (DatabaseWrapper db = new DatabaseWrapper()) {
			//1-1.初始化 Sys_user 数据
			Sys_user sysUser = new Sys_user();
			sysUser.setUser_id(USER_ID);
			sysUser.setCreate_id(1000L);
			sysUser.setDep_id(DEP_ID);
			sysUser.setRole_id(1001L);
			sysUser.setUser_name("init-hll");
			sysUser.setUser_password("111111");
			sysUser.setUseris_admin("0");
			sysUser.setUser_state("0");
			sysUser.setCreate_date(DateUtil.getSysDate());
			sysUser.setToken("0");
			sysUser.setValid_time(DateUtil.getSysTime());
			sysUser.add(db);
			//1-2.初始化 User_fav 数据
			User_fav userFav = new User_fav();
			userFav.setFav_id(FAV_ID + THREAD_ID);
			userFav.setOriginal_name("init-hll");
			//测试数据的文件id
			String FILE_ID = "999999999999";
			userFav.setFile_id(FILE_ID + THREAD_ID);
			userFav.setUser_id(USER_ID);
			userFav.setFav_flag("1");
			userFav.add(db);
			//1-3.初始化 Department_info 数据
			Department_info departmentInfo = new Department_info();
			departmentInfo.setDep_id(DEP_ID);
			departmentInfo.setDep_name("init-hll");
			departmentInfo.setCreate_date(DateUtil.getSysDate());
			departmentInfo.setCreate_time(DateUtil.getSysTime());
			departmentInfo.add(db);
			//2.提交所有数据库执行操作
			SqlOperator.commitTransaction(db);
			//3.用户模拟登陆
			String bodyString = new HttpClient()
					.addData("user_id", USER_ID)
					.addData("password", "111111")
					.post("http://127.0.0.1:8888/A/action/hrds/a/biz/login/login").getBodyString();
			ActionResult ar = JsonUtil.toObjectSafety(bodyString, ActionResult.class)
					.orElseThrow(() -> new BusinessException("模拟登陆失败!"));
			assertThat(ar.isSuccess(), is(true));
		}
	}

	@Method(desc = "测试案例执行完成后清理测试数据",
			logicStep = "1.删除测试用例初始化的数据" +
					"1-1.删除 User_fav 表测试数据" +
					"1-2.删除 Sys_user 表测试数据" +
					"1-3.删除 Department_info 表测试数据")
	@After
	public void after() {
		try (DatabaseWrapper db = new DatabaseWrapper()) {
			//1.删除 User_fav 表测试数据
			SqlOperator.execute(db, "delete from " + User_fav.TableName + " where fav_id=?",
					FAV_ID + THREAD_ID);
			long ufDataNum = SqlOperator.queryNumber(db,
					"select count(1) from " + User_fav.TableName + " where fav_id=?", FAV_ID + THREAD_ID)
					.orElseThrow(() -> new RuntimeException("count fail!"));
			assertThat("User_fav 表此条数据删除后,记录数应该为0", ufDataNum, is(0L));
			//2.删除 Sys_user 表测试数据
			SqlOperator.execute(db,
					"delete from " + Sys_user.TableName + " where user_id=?", USER_ID);
			long suNum = SqlOperator.queryNumber(db,
					"select count(1) from " + Sys_user.TableName + " where user_id =?",
					USER_ID
			).orElseThrow(() -> new RuntimeException("count fail!"));
			assertThat("sys_user 表此条数据删除后,记录数应该为0", suNum, is(0L));
			//3.删除 Department_info 表测试数据
			SqlOperator.execute(db,
					"delete from " + hrds.commons.entity.Department_info.TableName + " where dep_id=?", DEP_ID);
			long depNum = SqlOperator.queryNumber(db,
					"select count(1) from " + hrds.commons.entity.Department_info.TableName + " where dep_id =?",
					DEP_ID
			).orElseThrow(() -> new RuntimeException("count fail!"));
			assertThat("department_info 表此条数据删除后,记录数应该为0", depNum, is(0L));
			SqlOperator.commitTransaction(db);
		}
	}

	@Method(desc = "根据登录用户获取用户收藏的文件列表,返回结果默认显示最近9条收藏的测试方法",
			logicStep = "1.正确数据访问:" +
					"1-1.int类型值的 queryNum 1-99 之间的整数，取输入的整数" +
					"1-2.int类型值的 queryNum 小于1 的整数，取默认的查询条数9" +
					"1-3.int类型值的 queryNum 大于99 的整数，取最大显示条数99" +
					"2.错误数据访问:" +
					"2-1.非int类型值的 queryNum")
	@Test
	public void getCollectFiles() {
		String bodyString;
		ActionResult ar;
		//1-1.int类型值的 queryNum 1-99 之间的整数，取输入的整数
		bodyString = new HttpClient()
				.addData("queryNum", "8")
				.post(getActionUrl("getCollectFiles")).getBodyString();
		ar = JsonUtil.toObjectSafety(bodyString, ActionResult.class).orElseThrow(() -> new BusinessException(
				"获取返回的ActionResult信息失败!"));
		assertThat(ar.isSuccess(), is(true));
		for (int i = 0; i < ar.getDataForResult().getRowCount(); i++) {
			assertThat(ar.getDataForResult().getString(i, "original_name"), is("init-hll"));
			assertThat(ar.getDataForResult().getString(i, "file_id"),
					is("999999999999" + THREAD_ID));
			assertThat(ar.getDataForResult().getString(i, "fav_flag"), is("1"));
		}
		//1-2.int类型值的 queryNum 小于1 的整数，取输入的整数
		bodyString = new HttpClient()
				.addData("queryNum", "-1")
				.post(getActionUrl("getCollectFiles")).getBodyString();
		ar = JsonUtil.toObjectSafety(bodyString, ActionResult.class).orElseThrow(() -> new BusinessException(
				"获取返回的ActionResult信息失败!"));
		assertThat(ar.isSuccess(), is(true));
		for (int i = 0; i < ar.getDataForResult().getRowCount(); i++) {
			assertThat(ar.getDataForResult().getString(i, "original_name"), is("init-hll"));
			assertThat(ar.getDataForResult().getString(i, "file_id"),
					is("999999999999" + THREAD_ID));
			assertThat(ar.getDataForResult().getString(i, "fav_flag"), is("1"));
		}
		//1-3.int类型值的 queryNum 大于99 的整数，取最大显示条数99
		bodyString = new HttpClient()
				.addData("queryNum", "1000")
				.post(getActionUrl("getCollectFiles")).getBodyString();
		ar = JsonUtil.toObjectSafety(bodyString, ActionResult.class).orElseThrow(() -> new BusinessException(
				"获取返回的ActionResult信息失败!"));
		assertThat(ar.isSuccess(), is(true));
		for (int i = 0; i < ar.getDataForResult().getRowCount(); i++) {
			assertThat(ar.getDataForResult().getString(i, "original_name"), is("init-hll"));
			assertThat(ar.getDataForResult().getString(i, "file_id"),
					is("999999999999" + THREAD_ID));
			assertThat(ar.getDataForResult().getString(i, "fav_flag"), is("1"));
		}
		//2-1.非int类型值的 queryNum
		bodyString = new HttpClient()
				.addData("queryNum", "init-hll8")
				.post(getActionUrl("getCollectFiles")).getBodyString();
		ar = JsonUtil.toObjectSafety(bodyString, ActionResult.class).orElseThrow(() -> new BusinessException(
				"获取返回的ActionResult信息失败!"));
		assertThat(ar.isSuccess(), is(false));
	}

	@Method(desc = "全文检索方法测试类", logicStep = "全文检索方法测试类")
	@Test
	public void fullTextSearch() {
		//查询入库数据
	}
}