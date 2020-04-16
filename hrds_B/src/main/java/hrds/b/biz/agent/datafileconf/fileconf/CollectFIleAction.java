package hrds.b.biz.agent.datafileconf.fileconf;

import fd.ng.core.annotation.DocClass;
import fd.ng.core.annotation.Method;
import fd.ng.core.annotation.Param;
import fd.ng.core.annotation.Return;
import fd.ng.web.util.Dbo;
import hrds.b.biz.agent.datafileconf.CheckParam;
import hrds.commons.base.BaseAction;
import hrds.commons.codes.AgentType;
import hrds.commons.codes.IsFlag;
import hrds.commons.entity.Agent_info;
import hrds.commons.entity.Collect_job_classify;
import hrds.commons.entity.Database_set;
import hrds.commons.exception.BusinessException;
import hrds.commons.utils.key.PrimayKeyGener;
import java.util.Map;

@DocClass(desc = "数据文件采集配置管理", author = "Mr.Lee", createdate = "2020-04-10 16:08")
public class CollectFIleAction extends BaseAction {

  @Method(desc = "编辑数据文件采集", logicStep = "1: 检查当前任务是否存在 2: 返回编辑的数据信息")
  @Param(name = "colSetId", desc = "数据采集任务ID(database_id)", range = "不可为空")
  @Return(desc = "返回编辑时需要的数据文件采集信息", range = "不可为空")
  public Map<String, Object> getInitDataFileData(long colSetId) {
    //    1: 检查当前任务是否存在
    long countNum =
        Dbo.queryNumber(
                "SELECT COUNT(1) FROM  " + Database_set.TableName + " WHERE database_id = ?",
                colSetId)
            .orElseThrow(() -> new BusinessException("SQL查询错误"));

    if (countNum == 0) {
      CheckParam.throwErrorMsg("任务( %s )不存在!!!", colSetId);
    }

    //    2: 返回编辑的数据信息
    return Dbo.queryOneObject(
        "SELECT t1.*,t2.classify_num,t2.classify_name FROM "
            + Database_set.TableName
            + " t1 JOIN "
            + Collect_job_classify.TableName
            + " t2 ON t1.classify_id = t2.classify_id WHERE t2.user_id = ? AND t1.is_sendok = ? AND t1.database_id = ?",
        getUserId(),
        IsFlag.Shi.getCode(),
        colSetId);
  }

  @Method(desc = "新增数据文件采集", logicStep = "根据用户信息查询数据文件上次为配置完成的数据信息")
  @Param(name = "source_id", desc = "数据源ID", range = "不可为空")
  @Return(desc = "返回为配置的数据采集信息", range = "可以为空,为空表示不存在上次为配置完成的数据信息")
  public Map<String, Object> addDataFileData(long source_id) {
    return Dbo.queryOneObject(
        "SELECT t1.*,t2.classify_num,t2.classify_name FROM "
            + Database_set.TableName
            + " t1 JOIN "
            + Collect_job_classify.TableName
            + " t2 ON t1.classify_id = t2.classify_id join "
            + Agent_info.TableName
            + " ai on t1.agent_id = ai.agent_id "
            + " where t1.is_sendok = ? AND ai.agent_type = ? AND ai.user_id = ? AND ai.source_id = ?",
        IsFlag.Fou.getCode(),
        AgentType.DBWenJian.getCode(),
        getUserId(),
        source_id);
  }

  @Method(desc = "保存数据文件的配置信息", logicStep = "1: 检查数据编号是否存在" + "2: 设置此次任务的ID" + "3: 返回生成的采集任务ID给前端")
  @Param(name = "database_set", desc = "数据文件的配置实体", range = "不可为空", isBean = true)
  @Return(desc = "返回此次任务的ID", range = "不会为空")
  public String saveDataFile(Database_set database_set) {
    CheckParam.checkData("采集任务编号不能为空", database_set.getDatabase_number());
    CheckParam.checkData("采集数据文件路径不能为空", database_set.getPlane_url());
    CheckParam.checkData("分类编号不能为空", String.valueOf(database_set.getClassify_id()));
    //    1: 检查数据编号是否存在
    long countNum =
        Dbo.queryNumber(
                "SELECT COUNT(1) FROM " + Database_set.TableName + " WHERE database_number = ? ",
                database_set.getDatabase_number())
            .orElseThrow(() -> new BusinessException("SQL查询异常"));
    if (countNum != 0) {
      CheckParam.throwErrorMsg("当前任务编号( %s ),已经存在", database_set.getDatabase_number());
    }
    //    2: 设置此次任务的ID
    String database_id = PrimayKeyGener.getNextId();
    // 设置是否为DB文件为是
    database_set.setDb_agent(IsFlag.Shi.getCode());
    database_set.setDatabase_id(database_id);
    database_set.setIs_sendok(IsFlag.Fou.getCode());
    database_set.add(Dbo.db());
    //    3: 返回生成的采集任务ID给前端
    return database_id;
  }

  @Method(
      desc = "更新数据文件的配置信息",
      logicStep = "1: 检查数据文件采集信息是否存在" + "2: 更新数据文件采集信息" + "3: 返回生成的采集任务ID给前端")
  @Param(name = "database_set", desc = "数据文件的配置实体数据信息", range = "不可为空", isBean = true)
  @Return(desc = "返回此次任务的ID", range = "不会为空")
  public String updateDataFile(Database_set database_set) {
    CheckParam.checkData("任务ID不能为空", database_set.getDatabase_id().toString());
    CheckParam.checkData("任务编号不能为空", database_set.getDatabase_number());
    CheckParam.checkData("采集数据文件路径不能为空", database_set.getPlane_url());
    CheckParam.checkData("分类编号不能为空", String.valueOf(database_set.getClassify_id()));
    //    1: 检查数据文件采集信息是否存在
    long countNum =
        Dbo.queryNumber(
                "SELECT COUNT(1) FROM " + Database_set.TableName + " WHERE  database_id = ?",
                database_set.getDatabase_id())
            .orElseThrow(() -> new BusinessException("SQL查询异常"));
    if (countNum == 0) {
      CheckParam.throwErrorMsg("当前任务编号( %s ),已经不存在", database_set.getDatabase_number());
    }
    //    2: 更新数据文件采集信息
    database_set.setIs_sendok(IsFlag.Fou.getCode());
    database_set.update(Dbo.db());
    //    3: 返回生成的采集任务ID给前端
    return database_set.getDatabase_id().toString();
  }
}
