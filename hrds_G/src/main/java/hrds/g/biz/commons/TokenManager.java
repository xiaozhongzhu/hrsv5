package hrds.g.biz.commons;

import fd.ng.core.annotation.DocClass;
import fd.ng.core.annotation.Method;
import fd.ng.core.annotation.Param;
import fd.ng.core.annotation.Return;
import hrds.g.biz.bean.TokenModel;

@DocClass(desc = "token管理接口", author = "dhw", createdate = "2020/3/30 16:47")
public interface TokenManager {

	@Method(desc = "建一个token关联上指定用户", logicStep = "")
	@Param(name = "user_id", desc = "指定用户的id", range = "新增用户时生成")
	@Return(desc = "生成的token", range = "无限制")
	TokenModel createToken(String user_id, String pwd);

	@Method(desc = "检查token是否有效", logicStep = "")
	@Param(name = "token", desc = "token", range = "使用生成token接口生成")
	@Return(desc = "生成的token", range = "无限制")
	boolean checkToken(String token);

	@Method(desc = "清除token", logicStep = "")
	@Param(name = "token", desc = "使用生成token接口生成", range = "使用生成token接口生成")
	void deleteToken(String token);

}
