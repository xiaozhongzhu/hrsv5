package hrds.agent.trans.biz.agentserver;

import fd.ng.core.annotation.DocClass;
import fd.ng.core.annotation.Method;
import fd.ng.core.annotation.Param;
import fd.ng.core.annotation.Return;
import fd.ng.core.utils.DateUtil;
import fd.ng.core.utils.StringUtil;
import fd.ng.core.utils.SystemUtil;
import hrds.commons.base.AgentBaseAction;
import hrds.commons.exception.AppSystemException;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//FIXME @徐超
// Agent等程序，也要有自己的 BaseAction。因为也需要做权限控制，比如每次交互都有传递一个固定的令牌做验证
@DocClass(desc = "获取当前程序所在的服务器信息的接口类", author = "zxz", createdate = "2019/9/11 17:38")
public class AgentServerInfo extends AgentBaseAction {
	//系统目录的集合
	private static final ArrayList<String> windows_nolist;
	//linux可查看的目录的集合
	private static final ArrayList<String> linux_nolist;

	//需要过滤的系统目录
	static {
		windows_nolist = new ArrayList<>();
		windows_nolist.add("C:\\");
		linux_nolist = new ArrayList<>();
		linux_nolist.add("/bin");
		linux_nolist.add("/boot");
		linux_nolist.add("/lib");
		linux_nolist.add("/proc");
		linux_nolist.add("/sbin");
		linux_nolist.add("/srv");
		linux_nolist.add("/sys");
		linux_nolist.add("/dev");
		linux_nolist.add("/etc");
		linux_nolist.add("/lib64");
		linux_nolist.add("/media");
		linux_nolist.add("/run");
		linux_nolist.add("/lost+found");
		linux_nolist.add("/usr");
		linux_nolist.add("/var");
		linux_nolist.add("/opt");
		linux_nolist.add("/mnt");
		linux_nolist.add("/root");
		linux_nolist.add("/cdrom");
		linux_nolist.add("/snap");
	}

	@Method(desc = "获取当前服务器的时间、日期、操作系统名称、操作系统用户名"
			, logicStep = "1.获取当前日期、时间、系统名称、用户名称放到map")
	@Return(desc = "包含Agent日期、时间、系统名称、用户名称的map", range = "不会为空")
	public Map<String, Object> getServerInfo() {
		//1.获取当前日期、时间、系统名称、用户名称放map
		Map<String, Object> map = new HashMap<>();
		map.put("agentdate", DateUtil.getSysDate());
		map.put("agenttime", DateUtil.getSysTime());
		map.put("osName", SystemUtil.OS_NAME);
		map.put("userName", SystemUtil.USER_NAME);
		return map;
	}

	@Method(desc = "获取服务器指定文件夹下的目录及文件"
			, logicStep = "1.如果需要显示文件夹的路径为空，则默认取根目录下的文件和文件夹" +
			"2.取到文件和文件夹则进行遍历" +
			"3.根据操作系统的类型取消系统的一些目录" +
			"4.需要显示文件且是文件则放到list")
	@Param(name = "pathVal", desc = "页面选择的文件夹路径，为空则表示根目录", nullable = true, range = "可为空")
	@Param(name = "isFile", desc = "是否显示当前目录下的文件，默认false", valueIfNull = "false", range = "可为空")
	@Return(desc = "当前文件夹下所有的目录(当isFile为true时返回当前文件夹下所有的目录和文件)", range = "可能为空")
	public List<Map<String, String>> getSystemFileInfo(String pathVal, String isFile) {
		File[] file_array;
		//1.如果需要显示文件夹的路径为空，则默认取根目录下的文件和文件夹
		if (StringUtil.isBlank(pathVal)) {
			file_array = File.listRoots();
		} else {
			file_array = new File(pathVal).listFiles();
		}
		List<Map<String, String>> list = new ArrayList<>();
		//获取操作系统的名称
		String osName = SystemUtil.OS_NAME;
//		log.info("获取到了操作系统的名称============" + osName);
		//2.取到文件和文件夹则进行遍历
		if (file_array != null && file_array.length > 0) {
			for (File file : file_array) {
				//是文件夹直接放到list
				if (file.isDirectory()) {
					String name = file.getName();
					String path_hy = file.getPath();
					//3.根据操作系统的类型取消系统的一些目录
					if (osName.toLowerCase().contains("windows")) {
						if (!windows_nolist.contains(path_hy)
								&& (!name.startsWith(".") || ".bin".equalsIgnoreCase(name))) {
							Map<String, String> map = new HashMap<>();
							map.put("isFolder", "true");
							map.put("name", name);
							map.put("path", path_hy);
							map.put("osName", osName);
							map.put("canExecute", String.valueOf(file.canExecute()));
							map.put("canWrite", String.valueOf(file.canWrite()));
							map.put("canRead", String.valueOf(file.canRead()));
							list.add(map);
						}
					} else if (osName.toLowerCase().contains("linux")) {
						if (!linux_nolist.contains(path_hy)
								&& (!name.startsWith(".") || ".bin".equalsIgnoreCase(name))) {
							Map<String, String> map = new HashMap<>();
							map.put("name", name);
							map.put("path", path_hy);
							map.put("isFolder", "true");
							map.put("osName", osName);
							map.put("canExecute", String.valueOf(file.canExecute()));
							map.put("canWrite", String.valueOf(file.canWrite()));
							map.put("canRead", String.valueOf(file.canRead()));
							list.add(map);
						}
					} else {
						throw new AppSystemException("不支持的操作系统类型");
					}
				}
			}
			//4.需要显示文件且是文件则放到list
			if ("true".equals(isFile)) {
				for (File file : file_array) {
					String name = file.getName();
					String path = file.getPath();
					if (!file.isDirectory() && !name.startsWith(".") && file.canRead()) {
						Map<String, String> map = new HashMap<>();
						map.put("name", name);
						map.put("path", path);
						map.put("isFolder", "false");
						map.put("osName", osName);
						list.add(map);
					}
				}
			}
		}
		return list;
	}

	public static void main(String[] args) {
		List<Map<String, String>> aFalse = new AgentServerInfo().getSystemFileInfo("C://", "true");
		aFalse.forEach(map -> {
			map.forEach((k, v) ->
					System.out.println(k + "===========" + v)
			);
			System.out.println("========================================================");
		});
	}
}
