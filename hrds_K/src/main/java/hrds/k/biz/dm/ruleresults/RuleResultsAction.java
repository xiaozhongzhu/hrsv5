package hrds.k.biz.dm.ruleresults;

import fd.ng.core.annotation.DocClass;
import fd.ng.core.annotation.Method;
import fd.ng.core.annotation.Param;
import fd.ng.core.annotation.Return;
import fd.ng.core.utils.StringUtil;
import fd.ng.core.utils.Validator;
import fd.ng.db.jdbc.DefaultPageImpl;
import fd.ng.db.jdbc.Page;
import fd.ng.db.jdbc.SqlOperator;
import fd.ng.web.conf.WebinfoConf;
import fd.ng.web.util.Dbo;
import hrds.commons.base.BaseAction;
import hrds.commons.codes.DataBaseCode;
import hrds.commons.collection.ProcessingData;
import hrds.commons.entity.Dq_definition;
import hrds.commons.entity.Dq_index3record;
import hrds.commons.entity.Dq_result;
import hrds.commons.exception.BusinessException;
import hrds.commons.utils.FileDownloadUtil;
import hrds.k.biz.dm.ruleresults.bean.RuleResultSearchBean;
import hrds.k.biz.utils.CheckBeanUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.supercsv.io.CsvListWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@DocClass(desc = "数据管控-规则结果", author = "BY-HLL", createdate = "2020/4/11 0013 上午 09:39")
public class RuleResultsAction extends BaseAction {

    private static final Logger logger = LogManager.getLogger();

    @Method(desc = "获取规则检查结果信息", logicStep = "获取规则检查结果信息")
    @Return(desc = "规则检查结果信息", range = "规则检查结果信息")
    public Map<String, Object> getRuleResultInfos() {
        //初始化执行sql
        SqlOperator.Assembler asmSql = SqlOperator.Assembler.newInstance();
        asmSql.clean();
        asmSql.addSql("SELECT t1.verify_date, t1.start_date, t1.start_time, t1.verify_result,t1.exec_mode,t1.dl_stat," +
                " t1.task_id, t2.target_tab, t2.reg_name,t2.reg_num, t2.flags, t2.rule_src, t2.rule_tag FROM " +
                Dq_result.TableName + " t1 JOIN " + Dq_definition.TableName + " t2 ON t1.reg_num = t2.reg_num " +
                " where user_id=? ORDER BY t1.verify_date DESC").addParam(getUserId());
        List<Map<String, Object>> rule_result_s = Dbo.queryList(asmSql.sql(), asmSql.params());
        Map<String, Object> ruleResultMap = new HashMap<>();
        ruleResultMap.put("rule_result_s", rule_result_s);
        ruleResultMap.put("totalSize", rule_result_s.size());
        return ruleResultMap;
    }

    @Method(desc = "检索规则结果信息", logicStep = "检索规则结果信息")
    @Param(name = "ruleResultSearchBean", desc = "自定义Bean", range = "ruleResultSearchBean")
    @Return(desc = "检索规则结果信息", range = "检索规则结果信息")
    public Map<String, Object> searchRuleResultInfos(RuleResultSearchBean ruleResultSearchBean) {
        //数据校验
        if (CheckBeanUtil.checkFullNull(ruleResultSearchBean)) {
            throw new BusinessException("检索条件全为空!");
        }
        //初始化执行sql
        SqlOperator.Assembler asmSql = SqlOperator.Assembler.newInstance();
        asmSql.clean();
        asmSql.addSql("SELECT t1.verify_date, t1.start_date, t1.start_time, t1.verify_result,t1.exec_mode, t1.dl_stat," +
                " t1.task_id, t2.target_tab, t2.reg_name,t2.reg_num, t2.flags, t2.rule_src, t2.rule_tag FROM " +
                Dq_result.TableName + " t1 JOIN " + Dq_definition.TableName + " t2 ON t1.reg_num = t2.reg_num" +
                " where t2.user_id=?").addParam(getUserId());
        if (StringUtil.isNotBlank(ruleResultSearchBean.getVerify_date())) {
            asmSql.addSql(" and t1.verify_date = ?").addParam(ruleResultSearchBean.getVerify_date());
        }
        if (StringUtil.isNotBlank(ruleResultSearchBean.getStart_date())) {
            asmSql.addSql(" and t1.start_date = ?").addParam(ruleResultSearchBean.getStart_date());
        }
        if (StringUtil.isNotBlank(ruleResultSearchBean.getRule_src())) {
            asmSql.addLikeParam(" t2.rule_src", '%' + ruleResultSearchBean.getRule_src() + '%');
        }
        if (StringUtil.isNotBlank(ruleResultSearchBean.getRule_tag())) {
            asmSql.addLikeParam(" t2.rule_tag", '%' + ruleResultSearchBean.getRule_tag() + '%');
        }
        if (StringUtil.isNotBlank(ruleResultSearchBean.getReg_name())) {
            asmSql.addLikeParam(" t2.reg_name", '%' + ruleResultSearchBean.getReg_name() + '%');
        }
        if (StringUtil.isNotBlank(ruleResultSearchBean.getReg_num())) {
            asmSql.addLikeParam(" cast(t1.reg_num as varchar(100))",
                    '%' + ruleResultSearchBean.getReg_num() + '%');
        }
        if (null != ruleResultSearchBean.getCase_type() && ruleResultSearchBean.getCase_type().length > 0) {
            asmSql.addORParam("t1.case_type", ruleResultSearchBean.getCase_type());
        }
        if (null != ruleResultSearchBean.getExec_mode() && ruleResultSearchBean.getExec_mode().length > 0) {
            asmSql.addORParam("t1.exec_mode", ruleResultSearchBean.getExec_mode());
        }
        if (null != ruleResultSearchBean.getVerify_result() && ruleResultSearchBean.getVerify_result().length > 0) {
            asmSql.addORParam("t1.verify_result", ruleResultSearchBean.getVerify_result());
        }
        List<Map<String, Object>> rule_result_s = Dbo.queryList(asmSql.sql(), asmSql.params());
        Map<String, Object> search_result_map = new HashMap<>();
        search_result_map.put("rule_result_s", rule_result_s);
        search_result_map.put("totalSize", rule_result_s.size());
        return search_result_map;
    }

    @Method(desc = "规则执行详细信息", logicStep = "规则执行详细信息")
    @Param(name = "task_id", desc = "任务编号", range = "String类型")
    @Return(desc = "规则执行详细信息", range = "规则执行详细信息")
    public Dq_result getRuleDetectDetail(String task_id) {
        //数据校验
        Validator.notBlank(task_id, "查看的任务编号为空!");
        //设置查询对象
        Dq_result dq_result = new Dq_result();
        dq_result.setTask_id(task_id);
        //获取本任务的详细信息
        return Dbo.queryOneObject(Dq_result.class, "SELECT * FROM " + Dq_result.TableName + " WHERE task_id = ?",
                dq_result.getTask_id()).orElseThrow(() -> (new BusinessException("任务执行详细信息的SQL失败!")));
    }

    @Method(desc = "规则执行历史信息",
            logicStep = "规则执行历史信息")
    @Param(name = "reg_num", desc = "规则编号", range = "long类型")
    @Param(name = "currPage", desc = "分页当前页", range = "大于0的正整数", valueIfNull = "1")
    @Param(name = "pageSize", desc = "分页查询每页显示条数", range = "大于0的正整数", valueIfNull = "10")
    @Return(desc = "规则执行历史信息", range = "规则执行历史信息")
    public Map<String, Object> getRuleExecuteHistoryInfo(long reg_num, int currPage, int pageSize) {
        //数据校验
        if (StringUtil.isBlank(String.valueOf(reg_num))) {
            throw new BusinessException("规则编号为空!");
        }
        //设置分页信息
        Page page = new DefaultPageImpl(currPage, pageSize);
        //获取该规则相关执行的历史信息
        List<Dq_result> dq_result_s = Dbo.queryPagedList(Dq_result.class, page, "SELECT * FROM dq_result WHERE" +
                " reg_num = ? ORDER BY verify_date DESC", reg_num);
        //初始化返会结果Map
        Map<String, Object> dq_result_map = new HashMap<>();
        dq_result_map.put("dq_result_s", dq_result_s);
        dq_result_map.put("totalSize", page.getTotalSize());
        return dq_result_map;
    }

    @Method(desc = "导出指标3结果", logicStep = "导出指标3结果")
    @Param(name = "task_id", desc = "检查结果id", range = "参数例子")
    @Return(desc = "结果说明", range = "结果描述")
    public void exportIndicator3Results(long task_id) {
        //数据校验
        Validator.notBlank(String.valueOf(task_id), "导出指标3结果时,需要的任务id为空!");
        //设置 Dq_index3record
        Dq_index3record di3 = new Dq_index3record();
        di3.setTask_id(task_id);
        //获取指标3存储记录信息
        Dq_result dr = Dbo.queryOneObject(Dq_result.class, "select * from " + Dq_result.TableName +
                " where task_id=?", di3.getTask_id()).orElseThrow(() ->
                (new BusinessException("获取任务指标3存储记录的SQL异常!")));
        di3 = Dbo.queryOneObject(Dq_index3record.class, "select * from " + Dq_index3record.TableName +
                " where task_id=?", di3.getTask_id()).orElseThrow(() ->
                (new BusinessException("获取任务指标3存储记录的SQL异常!")));
        //设置获取数据的sql
        String sql = "select * from " + di3.getTable_name();
        //初始化查询结果集
        List<Map<String, Object>> check_index3_list = new ArrayList<>();
        //根据指标3存储记录信息获取数据
        List<String> cols;
        try {
            cols = new ProcessingData() {
                @Override
                public void dealLine(Map<String, Object> map) {

                    check_index3_list.add(map);
                }
            }.getDataLayer(sql, Dbo.db());
        } catch (Exception e) {
            throw new BusinessException("获取指标3存储记录数据失败!" + e.getMessage());
        }
        //处理数据集
        List<Object[]> data_list = new ArrayList<>();
        check_index3_list.forEach(ci3 -> {
            Object[] o_arr = new Object[ci3.size()];
            for (int i = 0; i < cols.size(); i++) {
                o_arr[i] = ci3.get(cols.get(i));
            }
            data_list.add(o_arr);
        });
        //得到文件的保存目录
        String fileName = dr.getTarget_tab() + "_" + dr.getVerify_date() + ".csv";
        String savePath = WebinfoConf.FileUpload_SavedDirName + File.separator + fileName;
        File file = new File(savePath);
        //判断文件是否存在不存在创建
        if (!file.exists()) {
            try {
                if (!file.createNewFile()) {
                    throw new BusinessException("创建文件失败,请检查是否拥有目录写权限！");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        CsvListWriter writer = null;
        try {
            writer = new CsvListWriter(new OutputStreamWriter(new FileOutputStream(file),
                    DataBaseCode.UTF_8.getValue()), CsvPreference.EXCEL_PREFERENCE);
            //写表头
            writer.write(cols);
            //写表数据
            long counter = 0;
            for (Object[] objects : data_list) {
                //计数器
                counter++;
                writer.write(objects);
                if (counter % 50000 == 0) {
                    logger.info("正在写入文件，已写入" + counter + "行");
                    writer.flush();
                }
            }
            logger.info("文件写入完成，写入" + counter + "行");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException("创建文件流失败!");
        } finally {
            try {
                if (writer != null)
                    writer.close();
            } catch (IOException e) {
                e.printStackTrace();
                logger.error("关闭输出流失败!");
            }
        }
        //下载文件
        FileDownloadUtil.downloadFile(file.getName());
    }
}
