package com.tedu.plugin.teacher.classes.manage.notice;

import com.tedu.base.common.page.QueryPage;
import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.task.SpringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("insertNoticeFilePlugin")
public class insertNoticeFilePlugin implements ILogicPlugin {
    FormMapper formMapper = SpringUtils.getBean("simpleDao");
    public final Logger log = Logger.getLogger(this.getClass());


    @Override
    public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
        log.info(formModel.getData().toString());

        return null;
    }

    @Override
    public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult,
                        FormEngineResponse responseObj) {
        log.info(formModel.getData());
        Map<String, Object> map = (Map<String, Object>) formModel.getData();

        String fileId = map.get("fileId") == null ? "" : map.get("fileId").toString();
        String classId = map.get("classId") == null ? "" : map.get("classId").toString();
        String[] fileIds = fileId.split(",");
        String[] classIds = classId.split(",");
        try {
            CustomFormModel cModel = new CustomFormModel();
            QueryPage sqlQuery = new QueryPage();
            sqlQuery.setQueryParam("khTeacher/classManage/notice/QryLastNotice");
            List<Map<String, Object>> tables = formMapper.query(sqlQuery);
            Map<String, Object> row = new HashMap<>(tables.get(0));
            for (String id : fileIds) {
                row.put("fileId", id);
                sqlQuery.setQueryParam("khTeacher/classManage/notice/QryFileInfo");
                sqlQuery.setDataByMap(row);
                tables = formMapper.query(sqlQuery);
                if (tables.size() > 0) {
                    row.putAll(tables.get(0));
                    cModel.setSqlId("khTeacher/classManage/notice/InsertNoticeFile");
                    cModel.setData(row);
                    formMapper.saveCustom(cModel);
                }
            }
            for (String id: classIds){
                row.put("classId",id);
                row.put("schoolId","");
                sqlQuery.setQueryParam("khTeacher/classManage/notice/QrySchoolInfo");
                sqlQuery.setDataByMap(row);
                tables = formMapper.query(sqlQuery);
                if (tables.size() > 0) {
                    row.putAll(tables.get(0));
                }
                cModel.setSqlId("khTeacher/classManage/notice/InsertNoticeSchool");
                cModel.setData(row);
                formMapper.saveCustom(cModel);
            }



        } catch (Exception e) {
            log.error("增加通知文件失败，请查看日志", e);
            responseObj.setMsg("增加通知文件失败，请查看日志");
            e.printStackTrace();
        }
    }
}
