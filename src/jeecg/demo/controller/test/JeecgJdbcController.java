package jeecg.demo.controller.test;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jeecg.demo.entity.test.JeecgDemo;
import jeecg.demo.entity.test.JeecgJdbcEntity;
import jeecg.demo.service.test.JeecgJdbcServiceI;
import jeecg.system.controller.core.DocumentHandler;
import jeecg.system.pojo.base.BzgZzc;
import jeecg.system.pojo.base.HzZzc;
import jeecg.system.pojo.base.NljZzc;
import jeecg.system.pojo.base.WljZzc;
import jeecg.system.pojo.base.YljZzc;
import jeecg.system.pojo.base.ZSZzc;
import jeecg.system.service.SystemService;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.dao.ICommonDao;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.BrowserUtils;
import org.jeecgframework.core.util.MyBeanUtils;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.core.util.oConvertUtils;
import org.jeecgframework.core.util.excel.ExcelExportUtil;
import org.jeecgframework.tag.core.easyui.TagUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Title: Controller
 * @Description: 通过JDBC访问数据库
 * @author Quainty
 * @date 2013-05-20 13:18:38
 * @version V1.0
 *
 */
@Controller
@RequestMapping("/jeecgJdbcController")
public class JeecgJdbcController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(JeecgJdbcController.class);

	@Autowired
	private JeecgJdbcServiceI jeecgJdbcService;
	@Autowired
	private SystemService systemService;
	private String message;
	private DocumentHandler documentHandler;

	@Autowired
	public void setDocumentHandler(DocumentHandler documentHandler) {
		this.documentHandler = documentHandler;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * 通过JDBC访问数据库列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "jeecgJdbc")
	public ModelAndView jeecgJdbc(HttpServletRequest request) {
		return new ModelAndView("jeecg/demo/test/jeecgJdbcList");
	}

	/**
	 * easyui AJAX请求数据
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 * @param user
	 */

	@RequestMapping(params = "datagrid")
	public void datagrid(JeecgJdbcEntity jeecgJdbc, HttpServletRequest request, HttpServletResponse response,
			DataGrid dataGrid) {
		// 方式1, 用底层自带的方式往对象中设值 -------------------
		/*
		 * this.jeecgJdbcService.getDatagrid1(jeecgJdbc, dataGrid);
		 * TagUtil.datagrid(response, dataGrid); // end of 方式1
		 * =========================================
		 */

		// 方式2, 取值自己处理(代码量多一些，但执行效率应该会稍高一些) -------------------------------
		/*
		 * this.jeecgJdbcService.getDatagrid2(jeecgJdbc, dataGrid);
		 * TagUtil.datagrid(response, dataGrid); // end of 方式2
		 * =========================================
		 */

		// 方式3, 取值进一步自己处理(直接转换成easyUI的datagrid需要的东西，执行效率最高，最自由)
		// -------------------------------
		// *
		JSONObject jObject = this.jeecgJdbcService.getDatagrid3(jeecgJdbc, dataGrid);
		responseDatagrid(response, jObject);
		// end of 方式3 ========================================= */
	}

	/**
	 * 删除通过JDBC访问数据库
	 * 
	 * @return
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(JeecgJdbcEntity jeecgJdbc, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();

		String sql = "delete from jeecg_demo where id='" + jeecgJdbc.getId() + "'";
		jeecgJdbcService.executeSql(sql);

		message = "删除成功";
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);

		j.setMsg(message);
		return j;
	}

	/**
	 * 添加通过JDBC访问数据库
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "save")
	@ResponseBody
	public AjaxJson save(JeecgJdbcEntity jeecgJdbc, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		if (StringUtil.isNotEmpty(jeecgJdbc.getId())) {
			message = "更新成功";
			JeecgJdbcEntity t = jeecgJdbcService.get(JeecgJdbcEntity.class, jeecgJdbc.getId());
			try {
				MyBeanUtils.copyBeanNotNull2Bean(jeecgJdbc, t);
				jeecgJdbcService.saveOrUpdate(t);
				systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			message = "添加成功";
			jeecgJdbcService.save(jeecgJdbc);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}

		return j;
	}

	/**
	 * 通过JDBC访问数据库列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "addorupdate")
	public ModelAndView addorupdate(JeecgJdbcEntity jeecgJdbc, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(jeecgJdbc.getId())) {
			jeecgJdbc = jeecgJdbcService.getEntity(JeecgJdbcEntity.class, jeecgJdbc.getId());
			req.setAttribute("jeecgJdbcPage", jeecgJdbc);
		}
		return new ModelAndView("jeecg/demo/test/jeecgJdbc");
	}

	// -----------------------------------------------------------------------------------
	// 以下各函数可以提成共用部件 (Add by Quainty)
	// -----------------------------------------------------------------------------------
	public void responseDatagrid(HttpServletResponse response, JSONObject jObject) {
		response.setContentType("application/json");
		response.setHeader("Cache-Control", "no-store");
		try {
			PrintWriter pw = response.getWriter();
			pw.write(jObject.toString());
			pw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 通过JDBC访问数据库列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "newZzc")
	public ModelAndView newZzc(HttpServletRequest request) {
		return new ModelAndView("system/depart/newZzc");
	}

	/**
	 * easyui AJAX请求数据
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 * @param user
	 */

	@RequestMapping(params = "zzcdatagrid")
	public void zzcdatagrid(ZSZzc zsZzc, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		// 方式1, 用底层自带的方式往对象中设值 -------------------
		/*
		 * this.jeecgJdbcService.getDatagrid1(jeecgJdbc, dataGrid);
		 * TagUtil.datagrid(response, dataGrid); // end of 方式1
		 * =========================================
		 */

		// 方式2, 取值自己处理(代码量多一些，但执行效率应该会稍高一些) -------------------------------
		/*
		 * this.jeecgJdbcService.getDatagrid2(jeecgJdbc, dataGrid);
		 * TagUtil.datagrid(response, dataGrid); // end of 方式2
		 * =========================================
		 */

		// 方式3, 取值进一步自己处理(直接转换成easyUI的datagrid需要的东西，执行效率最高，最自由)
		// -------------------------------
		// *
		JSONObject jObject = this.jeecgJdbcService.getZzcDatagrid(zsZzc, dataGrid);
		responseDatagrid(response, jObject);
		// end of 方式3 ========================================= */
	}

	/**
	 * 导出文件
	 * 
	 * @return
	 */
	@RequestMapping(params = "zzcexpFiles")
	@ResponseBody
	public AjaxJson zzcexpFiles(ZSZzc zsZzc, HttpServletRequest request, DataGrid dataGrid) {
		AjaxJson j = new AjaxJson();
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");// 可以方便地修改日期格式
		String qdate = dateFormat.format(now);
		JSONObject jObject1 = this.jeecgJdbcService.getZzcDatagridylj(zsZzc, dataGrid, qdate);
		JSONObject jObject2 = this.jeecgJdbcService.getZzcDatagridwlj(zsZzc, dataGrid, qdate);
		JSONObject jObject3 = this.jeecgJdbcService.getZzcDatagridhz(zsZzc, dataGrid, qdate);
		JSONObject jObject4 = this.jeecgJdbcService.getZzcDatagridnlj(zsZzc, dataGrid, qdate);
		JSONObject jObject5 = this.jeecgJdbcService.getZzcDatagridbzg(zsZzc, dataGrid, qdate);
		try {
			documentHandler.createDoc1(jObject1, qdate);
			documentHandler.createDoc2(jObject2, qdate);
			documentHandler.createDoc3(jObject3, qdate);
			documentHandler.createDoc4(jObject4, qdate);
			documentHandler.createDoc5(jObject5, qdate);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		message = "文件导出成功";
		j.setMsg(message);
		return j;
	}

	/**
	 * 已离京导出文件
	 * 
	 * @return
	 */
	@RequestMapping(params = "zzcyljexpFiles")
	@ResponseBody
	public AjaxJson zzcyljexpFiles(ZSZzc zsZzc, HttpServletRequest request, DataGrid dataGrid) {
		AjaxJson j = new AjaxJson();
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");// 可以方便地修改日期格式
		String qdate = dateFormat.format(now);
		if (request.getParameter("zzcyljqdate") != null && !"".equals(request.getParameter("zzcyljqdate"))) {
			qdate = oConvertUtils.getString(request.getParameter("zzcyljqdate").replace("-", "").trim());
		}

		JSONObject jObject1 = this.jeecgJdbcService.getZzcDatagridylj(zsZzc, dataGrid, qdate);
		try {
			documentHandler.createDoc1(jObject1, qdate);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		message = "文件导出成功";
		j.setMsg(message);
		return j;
	}

	/**
	 * 已离京导出Excel文件
	 * 
	 * @return
	 */
	@RequestMapping(params = "zzcyljexpExcelFiles")
	@ResponseBody
	public AjaxJson zzcyljexpExcelFiles(ZSZzc zsZzc, HttpServletRequest request, HttpServletResponse response,
			DataGrid dataGrid) {
		AjaxJson j = new AjaxJson();
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");// 可以方便地修改日期格式
		String qdate = dateFormat.format(now);
		if (request.getParameter("zzcyljqdate") != null && !"".equals(request.getParameter("zzcyljqdate"))) {
			qdate = oConvertUtils.getString(request.getParameter("zzcyljqdate").replace("-", "").trim());
		}

		// 生成提示信息，
		response.setContentType("application/vnd.ms-excel");
		String codedFileName = null;
		OutputStream fOut = null;
		try {
			// ---------begin
			// 输出文档路径及名称
			List<Map<String, Object>> mapList = this.jeecgJdbcService.getZzcDataylj(zsZzc, qdate);
			List<YljZzc> yljZzcs = this.jeecgJdbcService.ListMap2JavaBean(mapList, YljZzc.class);

			qdate = qdate + "十二总队民警不在岗已离京情况一览表";
			codedFileName = qdate;
			// 根据浏览器进行转码，使其支持中文文件名
			String browse = BrowserUtils.checkBrowse(request);
			if ("MSIE".equalsIgnoreCase(browse.substring(0, 4))) {
				response.setHeader("content-disposition",
						"attachment;filename=" + java.net.URLEncoder.encode(codedFileName, "UTF-8") + ".xls");
			} else {
				String newtitle = new String(codedFileName.getBytes("UTF-8"), "ISO8859-1");
				response.setHeader("content-disposition", "attachment;filename=" + newtitle + ".xls");
			}
			// 进行转码，使其支持中文文件名
			// ---------end
			// 产生工作簿对象
			HSSFWorkbook workbook = null;
			workbook = ExcelExportUtil.exportExcel3(qdate, YljZzc.class, yljZzcs);
			fOut = response.getOutputStream();
			workbook.write(fOut);
		} catch (UnsupportedEncodingException e1) {

		} catch (Exception e) {

		} finally {
			try {
				fOut.flush();
				fOut.close();
			} catch (IOException e) {

			}
		}

		message = "文件导出成功";
		j.setMsg(message);
		return j;
	}

	/**
	 * 未离京导出文件
	 * 
	 * @return
	 */
	@RequestMapping(params = "zzcwljexpFiles")
	@ResponseBody
	public AjaxJson zzcwljexpFiles(ZSZzc zsZzc, HttpServletRequest request, DataGrid dataGrid) {
		AjaxJson j = new AjaxJson();
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");// 可以方便地修改日期格式
		String qdate = dateFormat.format(now);
		if (request.getParameter("zzcwljqdate") != null && !"".equals(request.getParameter("zzcwljqdate"))) {
			qdate = oConvertUtils.getString(request.getParameter("zzcwljqdate").replace("-", "").trim());
		}

		JSONObject jObject2 = this.jeecgJdbcService.getZzcDatagridwlj(zsZzc, dataGrid, qdate);
		try {
			documentHandler.createDoc2(jObject2, qdate);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		message = "文件导出成功";
		j.setMsg(message);
		return j;
	}

	/**
	 * 未离京导出excel文件
	 * 
	 * @return
	 */
	@RequestMapping(params = "zzcwljexpExcelFiles")
	@ResponseBody
	public AjaxJson zzcwljexpExcelFiles(ZSZzc zsZzc, HttpServletRequest request, HttpServletResponse response,
			DataGrid dataGrid) {
		AjaxJson j = new AjaxJson();
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");// 可以方便地修改日期格式
		String qdate = dateFormat.format(now);
		if (request.getParameter("zzcwljqdate") != null && !"".equals(request.getParameter("zzcwljqdate"))) {
			qdate = oConvertUtils.getString(request.getParameter("zzcwljqdate").replace("-", "").trim());
		}
		// 生成提示信息，
		response.setContentType("application/vnd.ms-excel");
		String codedFileName = null;
		OutputStream fOut = null;
		try {
			// ---------begin
			// 输出文档路径及名称
			List<Map<String, Object>> mapList = this.jeecgJdbcService.getZzcDatawlj(zsZzc, qdate);
			List<WljZzc> wljZzcs = this.jeecgJdbcService.ListMap2JavaBean(mapList, WljZzc.class);

			qdate = qdate + "十二总队民警不在岗未离京情况一览表";
			codedFileName = qdate;
			// 根据浏览器进行转码，使其支持中文文件名
			String browse = BrowserUtils.checkBrowse(request);
			if ("MSIE".equalsIgnoreCase(browse.substring(0, 4))) {
				response.setHeader("content-disposition",
						"attachment;filename=" + java.net.URLEncoder.encode(codedFileName, "UTF-8") + ".xls");
			} else {
				String newtitle = new String(codedFileName.getBytes("UTF-8"), "ISO8859-1");
				response.setHeader("content-disposition", "attachment;filename=" + newtitle + ".xls");
			}
			// 进行转码，使其支持中文文件名
			// ---------end
			// 产生工作簿对象
			HSSFWorkbook workbook = null;
			workbook = ExcelExportUtil.exportExcel3(qdate, WljZzc.class, wljZzcs);
			fOut = response.getOutputStream();
			workbook.write(fOut);
		} catch (UnsupportedEncodingException e1) {

		} catch (Exception e) {

		} finally {
			try {
				fOut.flush();
				fOut.close();
			} catch (IOException e) {

			}
		}

		message = "文件导出成功";
		j.setMsg(message);
		return j;
	}

	/**
	 * 拟离京导出文件
	 * 
	 * @return
	 */
	@RequestMapping(params = "zzcnljexpFiles")
	@ResponseBody
	public AjaxJson zzcnljexpFiles(ZSZzc zsZzc, HttpServletRequest request, DataGrid dataGrid) {
		AjaxJson j = new AjaxJson();
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");// 可以方便地修改日期格式
		String qdate = dateFormat.format(now);
		if (request.getParameter("zzcnljqdate") != null && !"".equals(request.getParameter("zzcnljqdate"))) {
			qdate = oConvertUtils.getString(request.getParameter("zzcnljqdate").replace("-", "").trim());
		}

		JSONObject jObject2 = this.jeecgJdbcService.getZzcDatagridnlj(zsZzc, dataGrid, qdate);
		try {
			documentHandler.createDoc4(jObject2, qdate);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		message = "文件导出成功";
		j.setMsg(message);
		return j;
	}

	/**
	 * 拟离京导出Excel文件
	 * 
	 * @return
	 */
	@RequestMapping(params = "zzcnljexpExcelFiles")
	@ResponseBody
	public AjaxJson zzcnljexpExcelFiles(ZSZzc zsZzc, HttpServletRequest request, HttpServletResponse response,
			DataGrid dataGrid) {
		AjaxJson j = new AjaxJson();
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");// 可以方便地修改日期格式
		String qdate = dateFormat.format(now);
		if (request.getParameter("zzcnljqdate") != null && !"".equals(request.getParameter("zzcnljqdate"))) {
			qdate = oConvertUtils.getString(request.getParameter("zzcnljqdate").replace("-", "").trim());
		}

		// 生成提示信息，
		response.setContentType("application/vnd.ms-excel");
		String codedFileName = null;
		OutputStream fOut = null;
		try {
			// ---------begin
			// 输出文档路径及名称
			List<Map<String, Object>> mapList = this.jeecgJdbcService.getZzcDatanlj(zsZzc, qdate);
			List<NljZzc> nljZzcs = this.jeecgJdbcService.ListMap2JavaBean(mapList, NljZzc.class);

			qdate = qdate + "十二总队民警拟离京情况一览表";
			codedFileName = qdate;
			// 根据浏览器进行转码，使其支持中文文件名
			String browse = BrowserUtils.checkBrowse(request);
			if ("MSIE".equalsIgnoreCase(browse.substring(0, 4))) {
				response.setHeader("content-disposition",
						"attachment;filename=" + java.net.URLEncoder.encode(codedFileName, "UTF-8") + ".xls");
			} else {
				String newtitle = new String(codedFileName.getBytes("UTF-8"), "ISO8859-1");
				response.setHeader("content-disposition", "attachment;filename=" + newtitle + ".xls");
			}
			// 进行转码，使其支持中文文件名
			// ---------end
			// 产生工作簿对象
			HSSFWorkbook workbook = null;
			workbook = ExcelExportUtil.exportExcel3(qdate, NljZzc.class, nljZzcs);
			fOut = response.getOutputStream();
			workbook.write(fOut);
		} catch (UnsupportedEncodingException e1) {

		} catch (Exception e) {

		} finally {
			try {
				fOut.flush();
				fOut.close();
			} catch (IOException e) {

			}
		}

		message = "文件导出成功";
		j.setMsg(message);
		return j;
	}

	/**
	 * 不在岗导出文件
	 * 
	 * @return
	 */
	@RequestMapping(params = "zzcbzgexpFiles")
	@ResponseBody
	public AjaxJson zzcbzgexpFiles(ZSZzc zsZzc, HttpServletRequest request, DataGrid dataGrid) {
		AjaxJson j = new AjaxJson();
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");// 可以方便地修改日期格式
		String qdate = dateFormat.format(now);
		if (request.getParameter("zzcbzgqdate") != null && !"".equals(request.getParameter("zzcbzgqdate"))) {
			qdate = oConvertUtils.getString(request.getParameter("zzcbzgqdate").replace("-", "").trim());
		}

		JSONObject jObject2 = this.jeecgJdbcService.getZzcDatagridbzg(zsZzc, dataGrid, qdate);
		try {
			documentHandler.createDoc5(jObject2, qdate);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		message = "文件导出成功";
		j.setMsg(message);
		return j;
	}

	/**
	 * 不在岗导出Excel文件
	 * 
	 * @return
	 */
	@RequestMapping(params = "zzcbzgexpExcelFiles")
	@ResponseBody
	public AjaxJson zzcbzgexpExcelFiles(ZSZzc zsZzc, HttpServletRequest request, HttpServletResponse response,
			DataGrid dataGrid) {
		AjaxJson j = new AjaxJson();
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");// 可以方便地修改日期格式
		String qdate = dateFormat.format(now);
		if (request.getParameter("zzcbzgqdate") != null && !"".equals(request.getParameter("zzcbzgqdate"))) {
			qdate = oConvertUtils.getString(request.getParameter("zzcbzgqdate").replace("-", "").trim());
		}

		// 生成提示信息，
		response.setContentType("application/vnd.ms-excel");
		String codedFileName = null;
		OutputStream fOut = null;
		try {
			// ---------begin
			// 输出文档路径及名称
			List<Map<String, Object>> mapList = this.jeecgJdbcService.getZzcDatabzg(zsZzc, qdate);
			List<BzgZzc> bzgZzcs = this.jeecgJdbcService.ListMap2JavaBean(mapList, BzgZzc.class);

			qdate = qdate + "十二总队民警不在岗情况一览表";
			codedFileName = qdate;
			// 根据浏览器进行转码，使其支持中文文件名
			String browse = BrowserUtils.checkBrowse(request);
			if ("MSIE".equalsIgnoreCase(browse.substring(0, 4))) {
				response.setHeader("content-disposition",
						"attachment;filename=" + java.net.URLEncoder.encode(codedFileName, "UTF-8") + ".xls");
			} else {
				String newtitle = new String(codedFileName.getBytes("UTF-8"), "ISO8859-1");
				response.setHeader("content-disposition", "attachment;filename=" + newtitle + ".xls");
			}
			// 进行转码，使其支持中文文件名
			// ---------end
			// 产生工作簿对象
			HSSFWorkbook workbook = null;
			workbook = ExcelExportUtil.exportExcel3(qdate, BzgZzc.class, bzgZzcs);
			fOut = response.getOutputStream();
			workbook.write(fOut);
		} catch (UnsupportedEncodingException e1) {

		} catch (Exception e) {

		} finally {
			try {
				fOut.flush();
				fOut.close();
			} catch (IOException e) {

			}
		}

		message = "文件导出成功";
		j.setMsg(message);
		return j;
	}

	/**
	 * 汇总导出文件
	 * 
	 * @return
	 */
	@RequestMapping(params = "zzchzexpFiles")
	@ResponseBody
	public AjaxJson zzchzexpFiles(ZSZzc zsZzc, HttpServletRequest request, DataGrid dataGrid) {
		AjaxJson j = new AjaxJson();
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");// 可以方便地修改日期格式
		String qdate = dateFormat.format(now);
		if (request.getParameter("zzchzqdate") != null && !"".equals(request.getParameter("zzchzqdate"))) {
			qdate = oConvertUtils.getString(request.getParameter("zzchzqdate").replace("-", "").trim());
		}

		JSONObject jObject3 = this.jeecgJdbcService.getZzcDatagridhz(zsZzc, dataGrid, qdate);
		try {
			documentHandler.createDoc3(jObject3, qdate);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		message = "文件导出成功";
		j.setMsg(message);
		return j;
	}

	/**
	 * 汇总导出Excel文件
	 * 
	 * @return
	 */
	@RequestMapping(params = "zzchzexpExcelFiles")
	@ResponseBody
	public AjaxJson zzchzexpExcelFiles(ZSZzc zsZzc, HttpServletRequest request,HttpServletResponse response, DataGrid dataGrid) {
		AjaxJson j = new AjaxJson();
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");// 可以方便地修改日期格式
		String qdate = dateFormat.format(now);
		if (request.getParameter("zzchzqdate") != null && !"".equals(request.getParameter("zzchzqdate"))) {
			qdate = oConvertUtils.getString(request.getParameter("zzchzqdate").replace("-", "").trim());
		}

		// 生成提示信息，
		response.setContentType("application/vnd.ms-excel");
		String codedFileName = null;
		OutputStream fOut = null;
		try {
			// ---------begin
			// 输出文档路径及名称
			List<Map<String, Object>> mapList = this.jeecgJdbcService.getZzcDatahz(zsZzc, qdate);
			List<HzZzc> hzZzcs = this.jeecgJdbcService.ListMap2JavaBean(mapList, HzZzc.class);

			qdate = qdate + "十二总队民警不在岗情况汇总表";
			codedFileName = qdate;
			// 根据浏览器进行转码，使其支持中文文件名
			String browse = BrowserUtils.checkBrowse(request);
			if ("MSIE".equalsIgnoreCase(browse.substring(0, 4))) {
				response.setHeader("content-disposition",
						"attachment;filename=" + java.net.URLEncoder.encode(codedFileName, "UTF-8") + ".xls");
			} else {
				String newtitle = new String(codedFileName.getBytes("UTF-8"), "ISO8859-1");
				response.setHeader("content-disposition", "attachment;filename=" + newtitle + ".xls");
			}
			// 进行转码，使其支持中文文件名
			// ---------end
			// 产生工作簿对象
			HSSFWorkbook workbook = null;
			workbook = ExcelExportUtil.exportExcel3(qdate, HzZzc.class, hzZzcs);
			fOut = response.getOutputStream();
			workbook.write(fOut);
		} catch (UnsupportedEncodingException e1) {

		} catch (Exception e) {

		} finally {
			try {
				fOut.flush();
				fOut.close();
			} catch (IOException e) {

			}
		}

		message = "文件导出成功";
		j.setMsg(message);
		return j;
	}
}
