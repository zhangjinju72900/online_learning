package com.tedu.plugin.product;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.tedu.base.common.error.ErrorCode;
import com.tedu.base.common.error.ValidException;
import com.tedu.base.common.utils.LogUtil;
import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.task.SpringUtils;
/**
 * 
 * @author zhangruiting
 *
 */
@Service("insertProductInformation")
public class InsertProductInformation implements ILogicPlugin {
	FormMapper formMapper = SpringUtils.getBean("simpleDao");
	public final Logger log = Logger.getLogger(this.getClass());
	
	@Override
	public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
		/*log.info(formModel.getData().toString());
		Map<String,Object> map=(Map<String, Object>) formModel.getData();
		List uploadPicture=(List) map.get("uploadPicture");
		log.info(uploadPicture);
		List list = new ArrayList();
		//int beforeSize=0;
		for(int i=0;i<uploadPicture.size();i++){
			Map<String,Object> pic = (Map<String, Object>) uploadPicture.get(i);
			Object cil = pic.get("name");
			list.add(cil);
			//beforeSize++;
		}
		log.info(list);
		for(int i=uploadPicture.size()-1;i<list.size();i++){
			if(!((list.get(i)+"").contains("jpg")) && !((list.get(i)+"").contains("png")) && !((list.get(i)+"").contains("jpeg"))){
				throw new ValidException(ErrorCode.INVALIDATE_FORM_DATA," ",list.get(i)+"不符合格式要求");
			}
		}*/
		//log.info(formModel.getData().get("explain").toString());
		
		/*积分与元设置为负数不能保存*/
		Map<String,Object> map=(Map<String, Object>) formModel.getData();
		List meetFile=(List) map.get("meetFile");
		log.info(meetFile);
		List integralList = new ArrayList();
		List amountList=new ArrayList(); 
		/*for(int i=0;i<meetFile.size();i++){
			Map<String,Object> pic = (Map<String, Object>) meetFile.get(i);
			Object cil = pic.get("integral");
			integralList.add(cil);
			Object amount = pic.get("amount");
			amountList.add(amount);
			int Evintegral=Integer.parseInt(integralList.get(i)+"");
			int Evamount=Integer.parseInt(amountList.get(i)+"");
			if(Evamount<0&&Evintegral<0){
				throw new ValidException(ErrorCode.INVALIDATE_FORM_DATA," ","积分与单价不能为负数");
			}
			if(Evintegral<0){
				throw new ValidException(ErrorCode.INVALIDATE_FORM_DATA," ","积分不能为负数");
			}
			if(Evamount<0){
				throw new ValidException(ErrorCode.INVALIDATE_FORM_DATA," ","单价不能为负数");
			}
		}*/
		
		return null;
	}
	
	@Override
	public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult, FormEngineResponse responseObj) {
//		  log.info(formModel.getData());
//		  Map<String,Object> map=(Map<String, Object>) formModel.getData();
//		  //获取主表的id
//		  String goodId=formModel.getPrimaryFieldValue().toString();
//		  //获取roleid并解析
//		  String ides = map.get("goodId")==null?"":map.get("goodId").toString();
//		  String[] ids=ides.startsWith("[")?ides.substring(1, ides.length()-1).split(","):ides.split(",");
//		  
//		  String mode = map.get("Mode")==null?"":map.get("Mode").toString();
//		  //为编辑模式时先将原来的主表id对应的记录然后重新插入
//		  if("Edit".equals(mode)){
//			  String updateSql = "deleteProductPicture";
//			  CustomFormModel cModel = new CustomFormModel();
//			  cModel.setSqlId(updateSql);
//			  cModel.setData(formModel.getData());		
//			  Map<String, Object> logData = formModel.getData();
//			  logData.put("id", goodId);
//			  LogUtil.info(cModel.getData().toString());
//			  formMapper.saveCustom(cModel);
//		  }
//		  
//		//新增产品把数据插入到t_goods_pic表和t_goods_pay_detail表中
//		CustomFormModel cModel = new CustomFormModel();
//		cModel.setSqlId("InsertProductPicture");
//		cModel.setData(formModel.getData());
//		Map<String, Object> logData = formModel.getData();
//					logData.put("curriculum_id", goodId);
		//当未选取roleId时，不执行插入
//			for (String courseId : ids) {
//				if(!(courseId.length()<=0)){
//					logData.put("course_id", courseId);
//					LogUtil.info(cModel.getData().toString());
//					formMapper.saveCustom(cModel);
//				}
//				
//			}
		

	}

}
