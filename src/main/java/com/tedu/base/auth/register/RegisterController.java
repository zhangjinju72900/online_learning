package com.tedu.base.auth.register;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tedu.base.common.page.QueryPage;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.service.FormService;

import net.sf.json.JSONObject;

@Controller
public class RegisterController {
	@Value("${base.website}")
	private String baseUrl;
	@Resource
	FormService formService;

	@RequestMapping(value = "/register")
	public String register(Model model, HttpServletRequest request, HttpServletResponse response) {
		model.addAttribute("ctx", baseUrl);
		return "h5/register";
	}

	@RequestMapping(value = "/checkCardNum")
	public void checkCardNum(@RequestBody FormEngineRequest requestObj, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			PrintWriter pw = response.getWriter();
			Map<String, Object> map = requestObj.getData();
			QueryPage queryPage1 = new QueryPage();
			queryPage1.setQueryParam("QryCustomerByCardNum");
			queryPage1.getData().put("cardNum", map.get("cardNum"));
			List<Map<String, Object>> list1 = formService.queryBySqlId(queryPage1);
			if (list1.size() > 0) {
				map.put("data", false);
				map.put("code", "1");
				
				/*formEngineResponse.setData(map);
				formEngineResponse.setCode("0");*/
				JSONObject jsonObject = JSONObject.fromObject(map);
				pw.write(jsonObject.toString());
			}else{
				map.put("data", true);
				map.put("code", "0");
				/*formEngineResponse.setData(map);
				formEngineResponse.setCode("0");*/
				JSONObject jsonObject = JSONObject.fromObject(map);
				pw.write(jsonObject.toString());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
