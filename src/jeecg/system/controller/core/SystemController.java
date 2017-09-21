package jeecg.system.controller.core;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jeecg.system.pojo.base.TSAttachment;
//import jeecg.demo.entity.test.TSDocument1;
import jeecg.system.pojo.base.TSDepart;
import jeecg.system.pojo.base.TSDocument;
import jeecg.system.pojo.base.TSDocument1;
import jeecg.system.pojo.base.TSFunction;
import jeecg.system.pojo.base.TSRole;
import jeecg.system.pojo.base.TSRoleFunction;
import jeecg.system.pojo.base.TSType;
import jeecg.system.pojo.base.TSTypegroup;
import jeecg.system.pojo.base.TSVersion;
import jeecg.system.pojo.base.ZSDoc;
import jeecg.system.pojo.base.ZSDocItem;
import jeecg.system.pojo.base.ZSHt;
import jeecg.system.pojo.base.ZSHtgroup;
import jeecg.system.pojo.base.ZSType;
import jeecg.system.pojo.base.ZSTypegroup;
import jeecg.system.service.SystemService;
import jeecg.system.service.UserService;

import org.apache.log4j.Logger;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.model.FieldsDocumentPart;
import org.apache.poi.hwpf.usermodel.Field;
import org.apache.poi.hwpf.usermodel.Fields;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.hwpf.usermodel.Table;
import org.apache.poi.hwpf.usermodel.TableCell;
import org.apache.poi.hwpf.usermodel.TableIterator;
import org.apache.poi.hwpf.usermodel.TableRow;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.common.UploadFile;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.ComboTree;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.common.model.json.TreeGrid;
import org.jeecgframework.core.common.model.json.ValidForm;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.DataUtils;
import org.jeecgframework.core.util.FileUtils;
import org.jeecgframework.core.util.MyClassLoader;
import org.jeecgframework.core.util.SetListSort;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.core.util.oConvertUtils;
import org.jeecgframework.tag.core.easyui.TagUtil;
import org.jeecgframework.tag.vo.easyui.ComboTreeModel;
import org.jeecgframework.tag.vo.easyui.TreeGridModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.sun.star.script.provider.XScriptContext;
import com.sun.star.text.XText;
import com.sun.star.text.XTextDocument;
import com.sun.star.text.XTextRange;
import com.sun.star.uno.UnoRuntime;

/**
 * 类型字段处理类
 * 
 * @author Administrator
 * 
 */
@Controller
@RequestMapping("/systemController")
public class SystemController extends BaseController {
	private static final Logger logger = Logger.getLogger(SystemController.class);
	private UserService userService;
	private SystemService systemService;
	private DocumentHandler documentHandler;
	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	@Autowired
	public void setDocumentHandler(DocumentHandler documentHandler) {
		this.documentHandler = documentHandler;
	}

	@Autowired
	public void setSystemService(SystemService systemService) {
		this.systemService = systemService;
	}

	public UserService getUserService() {
		return userService;
	}

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	@RequestMapping(params = "druid")
	public ModelAndView druid() {
		return new ModelAndView(new RedirectView("druid/index.html"));
	}
	/**
	 * 类型字典列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "typeGroupTabs")
	public ModelAndView typeGroupTabs(HttpServletRequest request) {
		List<TSTypegroup> typegroupList = systemService.loadAll(TSTypegroup.class);
		request.setAttribute("typegroupList", typegroupList);
		return new ModelAndView("system/type/typeGroupTabs");
	}

	/**
	 * 类型分组列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "typeGroupList")
	public ModelAndView typeGroupList(HttpServletRequest request) {
		return new ModelAndView("system/type/typeGroupList");
	}

	/**
	 * 类型列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "typeList")
	public ModelAndView typeList(HttpServletRequest request) {
		String typegroupid = request.getParameter("typegroupid");
		TSTypegroup typegroup = systemService.getEntity(TSTypegroup.class, typegroupid);
		request.setAttribute("typegroup", typegroup);
		return new ModelAndView("system/type/typeList");
	}

	/**
	 * easyuiAJAX请求数据
	 */

	@RequestMapping(params = "typeGroupGrid")
	public void typeGroupGrid(HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(TSTypegroup.class, dataGrid);
		this.systemService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * easyuiAJAX请求数据
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 * @param user
	 */

	@RequestMapping(params = "typeGrid")
	public void typeGrid(HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		String typegroupid = request.getParameter("typegroupid");
		//update-begin--Author:zhaojunfu  Date:20130528 for：131 追加查询条件
		String typename = request.getParameter("typename");
		CriteriaQuery cq = new CriteriaQuery(TSType.class, dataGrid);
		cq.eq("TSTypegroup.id", typegroupid);
		cq.like("typename", typename);
		//update-end--Author:zhaojunfu  Date:20130528 for：131 追加查询条件
		cq.add();
		this.systemService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除类型分组
	 * 
	 * @return
	 */
	@RequestMapping(params = "delTypeGroup")
	@ResponseBody
	public AjaxJson delTypeGroup(TSTypegroup typegroup, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		typegroup = systemService.getEntity(TSTypegroup.class, typegroup.getId());
		message = "类型分组: " + typegroup.getTypegroupname() + "被删除 成功";
		systemService.delete(typegroup);
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		//刷新缓存
		systemService.refleshTypeGroupCach();
		j.setMsg(message);
		return j;
	}

	/**
	 * 删除类型
	 * 
	 * @return
	 */
	@RequestMapping(params = "delType")
	@ResponseBody
	public AjaxJson delType(TSType type, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		type = systemService.getEntity(TSType.class, type.getId());
		message = "类型: " + type.getTypename() + "被删除 成功";
		systemService.delete(type);
		//刷新缓存
		systemService.refleshTypesCach(type);
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		j.setMsg(message);
		return j;
	}

	/**
	 * 检查分组代码
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "checkTypeGroup")
	@ResponseBody
	public ValidForm checkTypeGroup(HttpServletRequest request) {
		ValidForm v = new ValidForm();
		String typegroupcode=oConvertUtils.getString(request.getParameter("param"));
		String code=oConvertUtils.getString(request.getParameter("code"));
		List<TSTypegroup> typegroups=systemService.findByProperty(TSTypegroup.class,"typegroupcode",typegroupcode);
		if(typegroups.size()>0&&!code.equals(typegroupcode))
		{
			v.setInfo("分组已存在");
			v.setStatus("n");
		}
		return v;
	}
	/**
	 * 添加类型分组
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "saveTypeGroup")
	@ResponseBody
	public AjaxJson saveTypeGroup(TSTypegroup typegroup, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		if (StringUtil.isNotEmpty(typegroup.getId())) {
			message = "类型分组: " + typegroup.getTypegroupname() + "被更新成功";
			userService.saveOrUpdate(typegroup);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} else {
			message = "类型分组: " + typegroup.getTypegroupname() + "被添加成功";
			userService.save(typegroup);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}
		//刷新缓存
		systemService.refleshTypeGroupCach();
		j.setMsg(message);
		return j;
	}
	/**
	 * 检查类型代码
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "checkType")
	@ResponseBody
	public ValidForm checkType(HttpServletRequest request) {
		ValidForm v = new ValidForm();
		String typecode=oConvertUtils.getString(request.getParameter("param"));
		String code=oConvertUtils.getString(request.getParameter("code"));
		String typeGroupCode=oConvertUtils.getString(request.getParameter("typeGroupCode"));
		StringBuilder hql = new StringBuilder("FROM ").append(TSType.class.getName()).append(" AS entity WHERE 1=1 ");
		hql.append(" AND entity.TSTypegroup.typegroupcode =  '").append(typeGroupCode).append("'");
		hql.append(" AND entity.typecode =  '").append(typecode).append("'");
		List<Object> types = this.systemService.findByQueryString(hql.toString());
		if(types.size()>0&&!code.equals(typecode))
		{
			v.setInfo("类型已存在");
			v.setStatus("n");
		}
		return v;
	}
	/**
	 * 添加类型
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "saveType")
	@ResponseBody
	public AjaxJson saveType(TSType type, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		if (StringUtil.isNotEmpty(type.getId())) {
			message = "类型: " + type.getTypename() + "被更新成功";
			userService.saveOrUpdate(type);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} else {
			message = "类型: " + type.getTypename() + "被添加成功";
			userService.save(type);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}
		//刷新缓存
		systemService.refleshTypesCach(type);
		j.setMsg(message);
		return j;
	}

	

	/**
	 * 类型分组列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "aouTypeGroup")
	public ModelAndView aouTypeGroup(TSTypegroup typegroup, HttpServletRequest req) {
		if (typegroup.getId() != null) {
			typegroup = systemService.getEntity(TSTypegroup.class, typegroup.getId());
			req.setAttribute("typegroup", typegroup);
		}
		return new ModelAndView("system/type/typegroup");
	}

	/**
	 * 类型列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "addorupdateType")
	public ModelAndView addorupdateType(TSType type, HttpServletRequest req) {
		String typegroupid = req.getParameter("typegroupid");
		req.setAttribute("typegroupid", typegroupid);
		if (StringUtil.isNotEmpty(type.getId())) {
			type = systemService.getEntity(TSType.class, type.getId());
			req.setAttribute("type", type);
		}
		return new ModelAndView("system/type/type");
	}

	/*
	 * *****************部门管理操作****************************
	 */

	/**
	 * 部门列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "depart")
	public ModelAndView depart() {
		return new ModelAndView("system/depart/departList");
	}

	/**
	 * easyuiAJAX请求数据
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 * @param user
	 */

	@RequestMapping(params = "datagridDepart")
	public void datagridDepart(HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(TSDepart.class, dataGrid);
		this.systemService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
		;
	}

	/**
	 * 删除部门
	 * 
	 * @return
	 */
	@RequestMapping(params = "delDepart")
	@ResponseBody
	public AjaxJson delDepart(TSDepart depart, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		depart = systemService.getEntity(TSDepart.class, depart.getId());
		message = "部门: " + depart.getDepartname() + "被删除 成功";
		systemService.delete(depart);
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);

		return j;
	}

	/**
	 * 添加部门
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "saveDepart")
	@ResponseBody
	public AjaxJson saveDepart(TSDepart depart, HttpServletRequest request) {
		// 设置上级部门
		String pid = request.getParameter("TSPDepart.id");
		if (pid.equals("")) {
			depart.setTSPDepart(null);
		}
		AjaxJson j = new AjaxJson();
		if (StringUtil.isNotEmpty(depart.getId())) {
			message = "部门: " + depart.getDepartname() + "被更新成功";
			userService.saveOrUpdate(depart);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} else {
			message = "部门: " + depart.getDepartname() + "被添加成功";
			userService.save(depart);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 部门列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "addorupdateDepart")
	public ModelAndView addorupdateDepart(TSDepart depart, HttpServletRequest req) {
		List<TSDepart> departList = systemService.getList(TSDepart.class);
		req.setAttribute("departList", departList);
		if (depart.getId() != null) {
			depart = systemService.getEntity(TSDepart.class, depart.getId());
			req.setAttribute("depart", depart);
		}
		return new ModelAndView("system/depart/depart");
	}

	/**
	 * 父级权限列表
	 * 
	 * @param role
	 * @param request
	 * @param response
	 * @param dataGrid
	 * @param user
	 * @return
	 */
	@RequestMapping(params = "setPFunction")
	@ResponseBody
	public List<ComboTree> setPFunction(HttpServletRequest request, ComboTree comboTree) {
		CriteriaQuery cq = new CriteriaQuery(TSDepart.class);
		if (StringUtil.isNotEmpty(comboTree.getId())) {
			cq.eq("TSPDepart.id", comboTree.getId());
		}
		// ----------------------------------------------------------------
		// update-begin--Author:liutao Date:20130205 for：将isNotEmpty方法改为isEmpty
		// ----------------------------------------------------------------
		if (StringUtil.isEmpty(comboTree.getId())) {
			cq.isNull("TSPDepart.id");
		}
		// ----------------------------------------------------------------
		// update-begin--Author:liutao Date:20130205 for：将isNotEmpty方法改为isEmpty
		// ----------------------------------------------------------------
		cq.add();
		List<TSDepart> departsList = systemService.getListByCriteriaQuery(cq, false);
		List<ComboTree> comboTrees = new ArrayList<ComboTree>();
		comboTrees = systemService.comTree(departsList, comboTree);
		return comboTrees;

	}

	/*
	 * *****************角色管理操作****************************
	 */
	/**
	 * 角色列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "role")
	public ModelAndView role() {
		return new ModelAndView("system/role/roleList");
	}

	/**
	 * easyuiAJAX请求数据
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 * @param user
	 */

	@RequestMapping(params = "datagridRole")
	public void datagridRole(HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(TSRole.class, dataGrid);
		this.systemService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除角色
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "delRole")
	@ResponseBody
	public AjaxJson delRole(TSRole role, String ids, HttpServletRequest request) {
		message = "角色: " + role.getRoleName() + "被删除成功";
		AjaxJson j = new AjaxJson();
		role = systemService.getEntity(TSRole.class, role.getId());
		userService.delete(role);
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		j.setMsg(message);
		return j;
	}

	/**
	 * 角色录入
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "saveRole")
	@ResponseBody
	public AjaxJson saveRole(TSRole role, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		if (role.getId() != null) {
			message = "角色: " + role.getRoleName() + "被更新成功";
			userService.saveOrUpdate(role);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} else {
			message = "角色: " + role.getRoleName() + "被添加成功";
			userService.saveOrUpdate(role);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 角色列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "fun")
	public ModelAndView fun(HttpServletRequest request) {
		Integer roleid = oConvertUtils.getInt(request.getParameter("roleid"), 0);
		request.setAttribute("roleid", roleid);
		return new ModelAndView("system/role/roleList");
	}

	/**
	 * 设置权限
	 * 
	 * @param role
	 * @param request
	 * @param response
	 * @param dataGrid
	 * @param user
	 * @return
	 */
	@RequestMapping(params = "setAuthority")
	@ResponseBody
	public List<ComboTree> setAuthority(TSRole role, HttpServletRequest request, ComboTree comboTree) {
		CriteriaQuery cq = new CriteriaQuery(TSFunction.class);
		if (comboTree.getId() != null) {
			cq.eq("TFunction.functionid", oConvertUtils.getInt(comboTree.getId(), 0));
		}
		if (comboTree.getId() == null) {
			cq.isNull("TFunction");
		}
		cq.add();
		List<TSFunction> functionList = systemService.getListByCriteriaQuery(cq, false);
		List<ComboTree> comboTrees = new ArrayList<ComboTree>();
		Integer roleid = oConvertUtils.getInt(request.getParameter("roleid"), 0);
		List<TSFunction> loginActionlist = new ArrayList<TSFunction>();// 已有权限菜单
		role = this.systemService.get(TSRole.class, roleid);
		if (role != null) {
			List<TSRoleFunction> roleFunctionList = systemService.findByProperty(TSRoleFunction.class, "TSRole.id", role.getId());
			if (roleFunctionList.size() > 0) {
				for (TSRoleFunction roleFunction : roleFunctionList) {
					TSFunction function = (TSFunction) roleFunction.getTSFunction();
					loginActionlist.add(function);
				}
			}
		}
		ComboTreeModel comboTreeModel = new ComboTreeModel("id", "functionName", "TSFunctions");
		comboTrees = systemService.ComboTree(functionList, comboTreeModel, loginActionlist);
		return comboTrees;
	}

	/**
	 * 更新权限
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "updateAuthority")
	public String updateAuthority(HttpServletRequest request) {
		Integer roleid = oConvertUtils.getInt(request.getParameter("roleid"), 0);
		String rolefunction = request.getParameter("rolefunctions");
		TSRole role = this.systemService.get(TSRole.class, roleid);
		List<TSRoleFunction> roleFunctionList = systemService.findByProperty(TSRoleFunction.class, "TSRole.id", role.getId());
		systemService.deleteAllEntitie(roleFunctionList);
		String[] roleFunctions = null;
		if (rolefunction != "") {
			roleFunctions = rolefunction.split(",");
			for (String s : roleFunctions) {
				TSRoleFunction rf = new TSRoleFunction();
				TSFunction f = this.systemService.get(TSFunction.class, Integer.valueOf(s));
				rf.setTSFunction(f);
				rf.setTSRole(role);
				this.systemService.save(rf);
			}
		}
		return "system/role/roleList";
	}

	/**
	 * 角色页面跳转
	 * 
	 * @param role
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "addorupdateRole")
	public ModelAndView addorupdateRole(TSRole role, HttpServletRequest req) {
		if (role.getId() != null) {
			role = systemService.getEntity(TSRole.class, role.getId());
			req.setAttribute("role", role);
		}
		return new ModelAndView("system/role/role");
	}

	/**
	 * 操作列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "operate")
	public ModelAndView operate(HttpServletRequest request) {
		String roleid = request.getParameter("roleid");
		request.setAttribute("roleid", roleid);
		return new ModelAndView("system/role/functionList");
	}

	/**
	 * 权限操作列表
	 * 
	 * @param role
	 * @param request
	 * @param response
	 * @param dataGrid
	 * @param user
	 * @return
	 */
	@RequestMapping(params = "setOperate")
	@ResponseBody
	public List<TreeGrid> setOperate(HttpServletRequest request, TreeGrid treegrid) {
		String roleid = request.getParameter("roleid");
		CriteriaQuery cq = new CriteriaQuery(TSFunction.class);
		if (treegrid.getId() != null) {
			cq.eq("TFunction.functionid", oConvertUtils.getInt(treegrid.getId(), 0));
		}
		if (treegrid.getId() == null) {
			cq.isNull("TFunction");
		}
		cq.add();
		List<TSFunction> functionList = systemService.getListByCriteriaQuery(cq, false);
		List<TreeGrid> treeGrids = new ArrayList<TreeGrid>();
		Collections.sort(functionList, new SetListSort());
		TreeGridModel treeGridModel = new TreeGridModel();
		treeGridModel.setRoleid(roleid);
		treeGrids = systemService.treegrid(functionList, treeGridModel);
		return treeGrids;

	}

	/**
	 * 操作录入
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "saveOperate")
	@ResponseBody
	public AjaxJson saveOperate(HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		String fop = request.getParameter("fp");
		String roleid = request.getParameter("roleid");
		// 录入操作前清空上一次的操作数据
		clearp(roleid);
		String[] fun_op = fop.split(",");
		String aa = "";
		String bb = "";
		// 只有一个被选中
		if (fun_op.length == 1) {
			bb = fun_op[0].split("_")[1];
			aa = fun_op[0].split("_")[0];
			savep(roleid, bb, aa);
		} else {
			// 至少2个被选中
			for (int i = 0; i < fun_op.length; i++) {
				String cc = fun_op[i].split("_")[0]; // 操作id
				if (i > 0 && bb.equals(fun_op[i].split("_")[1])) {
					aa += "," + cc;
					if (i == (fun_op.length - 1)) {
						savep(roleid, bb, aa);
					}
				} else if (i > 0) {
					savep(roleid, bb, aa);
					aa = fun_op[i].split("_")[0]; // 操作ID
					if (i == (fun_op.length - 1)) {
						bb = fun_op[i].split("_")[1]; // 权限id
						savep(roleid, bb, aa);
					}

				} else {
					aa = fun_op[i].split("_")[0]; // 操作ID
				}
				bb = fun_op[i].split("_")[1]; // 权限id

			}
		}

		return j;
	}

	/**
	 * 更新操作
	 * 
	 * @param roleid
	 * @param functionid
	 * @param ids
	 */
	public void savep(String roleid, String functionid, String ids) {
		String hql = "from TRoleFunction t where" + " t.TSRole.id=" + roleid + " " + "and t.TFunction.functionid=" + functionid;
		TSRoleFunction rFunction = systemService.singleResult(hql);
		if (rFunction != null) {
			rFunction.setOperation(ids);
			systemService.saveOrUpdate(rFunction);
		}
	}

	/**
	 * 清空操作
	 * 
	 * @param roleid
	 */
	public void clearp(String roleid) {
		String hql = "from TRoleFunction t where" + " t.TSRole.id=" + roleid;
		List<TSRoleFunction> rFunctions = systemService.findByQueryString(hql);
		if (rFunctions.size() > 0) {
			for (TSRoleFunction tRoleFunction : rFunctions) {
				tRoleFunction.setOperation(null);
				systemService.saveOrUpdate(tRoleFunction);
			}
		}
	}

	/************************************** 版本维护 ************************************/

	/**
	 * 版本维护列表
	 */
	@RequestMapping(params = "versionList")
	public void versionList(HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(TSVersion.class, dataGrid);
		this.systemService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
		;
	}

	/**
	 * 删除版本
	 */

	@RequestMapping(params = "delVersion")
	@ResponseBody
	public AjaxJson delVersion(TSVersion version, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		version = systemService.getEntity(TSVersion.class, version.getId());
		message = "版本：" + version.getVersionName() + "被删除 成功";
		systemService.delete(version);
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);

		return j;
	}

	/**
	 * 版本添加跳转
	 * 
	 * @param icon
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "addversion")
	public ModelAndView addversion(HttpServletRequest req) {
		return new ModelAndView("system/version/version");
	}

	/**
	 * 保存版本
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "saveVersion", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson saveVersion(HttpServletRequest request) throws Exception {
		AjaxJson j = new AjaxJson();
		TSVersion version = new TSVersion();
		String versionName = request.getParameter("versionName");
		String versionCode = request.getParameter("versionCode");
		version.setVersionCode(versionCode);
		version.setVersionName(versionName);
		systemService.save(version);
		j.setMsg("版本保存成功");
		return j;
	}

	//----------------------------------------------------------------------
	//add-begin--Author:lihuan  Date:20130514 for：从V3版本迁移上传下载代码

	/**
	 * 新闻法规文件列表
	 */
	@RequestMapping(params = "documentList")
	public void documentList(HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(TSDocument.class, dataGrid);
		String typecode = oConvertUtils.getString(request.getParameter("typecode"));
		cq.createAlias("TSType", "TSType");
		cq.eq("TSType.typecode", typecode);
		cq.add();
		this.systemService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}
	
	/**
	 * 删除文档
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "delDocument")
	@ResponseBody
	public AjaxJson delDocument(TSDocument document, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		document = systemService.getEntity(TSDocument.class, document.getId());
		message = "" + document.getDocumentTitle() + "被删除成功";
		userService.delete(document);
		systemService.addLog(message, Globals.Log_Type_DEL,
				Globals.Log_Leavel_INFO);
		j.setSuccess(true);
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 文件添加跳转
	 * 
	 * @param icon
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "addFiles")
	public ModelAndView addFiles(HttpServletRequest req) {
		return new ModelAndView("system/document/files");
	}
	
	/**
	 * 保存文件
	 * 
	 * @param ids
	 * @return
	 * @throws IOException 
	 * @throws Exception
	 */
	@RequestMapping(params = "saveFiles", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson saveFiles(HttpServletRequest request, HttpServletResponse response, ZSDocItem zsDocitem) throws IOException{
		AjaxJson j = new AjaxJson();
		Map<String, Object> attributes = new HashMap<String, Object>();
		ArrayList<Object> listd = new ArrayList<Object>();
		
		ZSDoc zsDoc = new ZSDoc();
		UploadFile uploadFile = new UploadFile(request, zsDoc);
		zsDoc = systemService.analysisFile(uploadFile);
		
		String note = oConvertUtils.replaceBlank(zsDoc.getAttachmenttitle().trim());
		String getNote = note.substring(note.indexOf("（") + 1, note.indexOf("）"));
		
		if(checkZsDoc(getNote).equals("0")){
			j.setMsg("请勿重复添加");
			j.setAttributes(attributes);
			return j;
		}		
		String extend = zsDoc.getExtend();
		String realPath = zsDoc.getRealpath();
		if(extend.equals("doc")||extend.equals("DOC")){
			listd = readwriteWord(realPath);
		}else if(extend.equals("docx")||extend.equals("DOCX")){
			try {
				listd = readwriteDocx(realPath);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		ZSDoc t = (ZSDoc)listd.get(0);
		t.setExtend(extend);
		t.setRealpath(realPath);
		t.setAttachmenttitle(zsDoc.getAttachmenttitle());
		t.setCreatedate(DataUtils.gettimestamp());
		t.setNote(getNote);
		systemService.saveOrUpdate((ZSDoc)listd.get(0));
		
		for(int i=1;i<listd.size();i++){
			ZSDocItem ti = (ZSDocItem)listd.get(i);
			ti.setZsDoc(t);
			systemService.saveOrUpdate(ti);
		}
		attributes.put("url",t.getAttachmenttitle());
		attributes.put("fileKey", t.getCreatedate());
		attributes.put("name", t.getAttachmenttitle());
		attributes.put("viewhref", "commonController.do?objfileList&fileKey=" + t.getId());
		attributes.put("delurl", "commonController.do?delObjFile&fileKey=" + t.getId());
		j.setMsg("文件添加成功");
		j.setAttributes(attributes);
		return j;
	}
	
	/**
	 * 导出文件
	 * 
	 * @return
	 */
	@RequestMapping(params = "expFiles")
	@ResponseBody
	public AjaxJson expFiles(ZSDoc zsDoc, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		String docid = oConvertUtils.getString(request.getParameter("docid"));
		zsDoc = systemService.getEntity(ZSDoc.class, docid);
		
		try {
			documentHandler.createDoc(zsDoc);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		message = "文件: " + zsDoc.getAttachmenttitle() + " 导出成功";
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 检查是否存在相同zsDoc
	 * 
	 * @param ids
	 * @return
	 */
	@ResponseBody
	public String checkZsDoc(String getNote){
		String t = "1";
		StringBuilder hql = new StringBuilder("FROM ").append(ZSDoc.class.getName()).append(" AS zsDoc WHERE 1=1 ");
		hql.append(" AND zsDoc.note =  '").append(getNote).append("'");
		List<Object> types = this.systemService.findByQueryString(hql.toString());
		if(types.size()>0){
			t = "0";
		}
		return t;
	}
	
	/**
	 * 保存文件
	 * 
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "saveFiles2", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson saveFiles2(HttpServletRequest request, HttpServletResponse response, TSDocument1 document1, TSDocument document){
		AjaxJson j = new AjaxJson();
		Map<String, Object> attributes = new HashMap<String, Object>();
		List<TSDocument1> listDoc = new ArrayList<TSDocument1>();
		
		//FileInputStream hdt = null;
		
		TSTypegroup tsTypegroup=systemService.getTypeGroup("fieltype","文档分类");
		TSType tsType = systemService.getType("files","附件", tsTypegroup);
		String fileKey = oConvertUtils.getString(request.getParameter("fileKey"));// 文件ID
		String documentTitle = oConvertUtils.getString(request.getParameter("documentTitle"));// 文件标题
		String[] aa = readwriteTxt("D:\\Workspaces\\2017年6月\\十大队早例会记录（20170601）.txt");
		listDoc = readwriteWord1("D:\\Workspaces\\2017年6月\\十大队早例会记录（20170601）.doc");
		try {
			readwriteDocx("D:\\Workspaces\\2017年6月\\十大队早例会记录（20170611）.docx");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		TSAttachment tsAttachment = new TSAttachment();
		tsAttachment.setAttachmenttitle(documentTitle);
		tsAttachment.setCreatedate(DataUtils.gettimestamp());
		tsAttachment.setSubclassname(MyClassLoader.getPackPath(document));
		tsAttachment.setExtend(FileUtils.getExtend("D:\\Workspaces\\2017年6月\\十大队早例会记录（20170601）.doc"));// 获取文件扩展名
		systemService.saveOrUpdate(tsAttachment);
		
		for(TSDocument1 temp: listDoc){
			temp.setTsAttachment(tsAttachment);
			temp.setDocumentTitle(documentTitle);
			systemService.saveOrUpdate(temp);
		}
		
		if(StringUtil.isNotEmpty(fileKey)){
			document1.setId(fileKey);
			document1 = systemService.getEntity(TSDocument1.class, fileKey);
			document1.setDocumentTitle(documentTitle);
		}
		
		
		if (StringUtil.isNotEmpty(fileKey)) {
			document.setId(fileKey);
			document = systemService.getEntity(TSDocument.class, fileKey);
			document.setDocumentTitle(documentTitle);

		}
		document.setSubclassname(MyClassLoader.getPackPath(document));
		document.setCreatedate(DataUtils.gettimestamp());
		document.setTSType(tsType);
		UploadFile uploadFile = new UploadFile(request, document);
		uploadFile.setCusPath("files");
		uploadFile.setSwfpath("swfpath");
		document = systemService.uploadFile(uploadFile);
		attributes.put("url", document.getRealpath());
		attributes.put("fileKey", document.getId());
		attributes.put("name", document.getAttachmenttitle());
		attributes.put("viewhref", "commonController.do?objfileList&fileKey=" + document.getId());
		attributes.put("delurl", "commonController.do?delObjFile&fileKey=" + document.getId());
		j.setMsg("文件添加成功");
		j.setAttributes(attributes);

		return j;
	}

	public static String[] readwriteTxt(String filePath){
		File file = new File(filePath); 
		if(file.exists()){
			System.out.println("此文件存在");
		}else{
			System.out.println("此文件不存在");
		}
		Long filelength = file.length();
		byte[] filecontent = new byte[filelength.intValue()];
		
		BufferedReader br = null;
		String encoding = "GBK";
		try{
			br = new BufferedReader(new InputStreamReader(new FileInputStream(file),encoding));
			String s = null;
			String s2 = null;
			List<String> bList = new ArrayList<String>();
			while((s = br.readLine()) != null){
				bList.add(s);
				s2 += s.split("\r\n");
			}
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			try{
				if(br != null){br.close();}
			}catch(IOException e){
				System.out.println("Error in closing the BufferedReader");
			}
		}
		return null;
	}
	
	/** 
	* 实现对word读取和修改操作 
	*
	* @param filePath 
	*word模板路径和名称 
	* @param map 
	*待填充的数据，从数据库读取 
	*/
	public static ArrayList<Object> readwriteWord(String filePath) {
		// 读取word模板
		// String fileDir = new
		FileInputStream in = null;
		try{
			in = new FileInputStream(new File(filePath));
		}
		catch (FileNotFoundException e1){
			e1.printStackTrace();
		}
		HWPFDocument hdt = null;
		try{
			hdt = new HWPFDocument(in);
		}
		catch (IOException e1){
			e1.printStackTrace();
		}
		Fields fields = hdt.getFields();
		Iterator<Field> it = fields.getFields(FieldsDocumentPart.MAIN)
			.iterator();
		while (it.hasNext()){
			System.out.println(it.next().getType());
		}
		
		ArrayList<Object> listd = new ArrayList<Object>();
		ZSDoc zsDoc = new ZSDoc();
		List<ZSDocItem> listzsDocitem = new ArrayList<ZSDocItem>();
		
		//读取word文本内容
		Range range = hdt.getRange();
		int ri = 0;
		String docTitle = null;
		while(range.getParagraph(ri).text() != null){
			if(range.getParagraph(ri).text().length()<5){ri++;}
			else{
				docTitle = range.getParagraph(ri).text();
				zsDoc.setDocTitle(oConvertUtils.replaceBlank(docTitle.trim()));
				break;
			}
		}
		
		TableIterator tableIt = new TableIterator(range); 
		//迭代文档中的表格
		int ii = 0;
		int zql = 0;
		int docItemNum = 0;
		while (tableIt.hasNext()){
			Table tb = (Table) tableIt.next();
			ii++;
			//迭代行，默认从0开始
			for (int i = 0; i < tb.numRows(); i++) {
				TableRow tr = tb.getRow(i);
				//迭代列，默认从0开始
				for (int j = 0; j < tr.numCells(); j++) {
					TableCell td = tr.getCell(j);//取得单元格
					//取得单元格的内容
					for(int k=0;k<td.numParagraphs();k++){
						Paragraph para =td.getParagraph(k);
						
						String s = para.text().trim();
						if(s.trim().equals("")){
							break;
						}
						switch(docLike.toDoc(s)){
							case 值班总队领导:zql = 1;continue;
							case 带班领导:zql = 2;continue;
							case 值班警力:zql = 3;continue;
							case 系统维护:zql = 4;continue;
							case 信通保障:zql = 5;continue;
							case 故障处理:zql = 6;continue;
							case 其他值班情况:zql = 8;continue;
							default: break;
						}
						
						switch(zql){
							case 1:zql=0;zsDoc.setDocBigleader(s);break;
							case 2:zql=0;zsDoc.setDocClassleader(s);break;
							case 3:zql=0;zsDoc.setDocClassnum(s);break;
							case 4:zql=0;zsDoc.setDocXtwh(s);break;
							case 5:zql=0;zsDoc.setDocXtbz(s);break;
							case 6:zql=7;zsDoc.setDocGzcl(s);break;
							case 8:zql=0;zsDoc.setDocZbqk(s);break;
							default:break;
						}
						
						if(zql == 7 && s.trim().length()>5){
							docItemNum++;
							ZSDocItem zsDocItem = new ZSDocItem();
							zsDocItem.setDocIndex(String.valueOf(docItemNum));
							zsDocItem.setDocTxt(s);
							zsDocItem.setDocTitle(oConvertUtils.replaceBlank(docTitle.trim()));
							listzsDocitem.add(docItemNum-1, zsDocItem);
						}
					}
				}
			}
		}
		listd.add(0,zsDoc);
		for(int i=0;i<listzsDocitem.size();i++){
			listd.add(i+1,listzsDocitem.get(i));
		}
		if(listzsDocitem.isEmpty()){
			ZSDocItem zsDocItem = new ZSDocItem();
			zsDocItem.setDocTitle(docTitle);
			listd.add(1,zsDocItem);
		}
		return listd;
	}
	
	/** 
	* 实现对word读取和修改操作 
	*
	* @param filePath 
	*word模板路径和名称 
	* @param map 
	*待填充的数据，从数据库读取 
	*/
	public static List<TSDocument1> readwriteWord1(String filePath) {
		// 读取word模板
		// String fileDir = new
		FileInputStream in = null;
		try{
			in = new FileInputStream(new File(filePath));
		}
		catch (FileNotFoundException e1){
			e1.printStackTrace();
		}
		HWPFDocument hdt = null;
		try{
			hdt = new HWPFDocument(in);
		}
		catch (IOException e1){
			e1.printStackTrace();
		}
		Fields fields = hdt.getFields();
		Iterator<Field> it = fields.getFields(FieldsDocumentPart.MAIN)
			.iterator();
		while (it.hasNext()){
			System.out.println(it.next().getType());
		}
		
		Map<String, String> doc = new HashMap<String, String>();
		Map<String, String> docItem = new HashMap<String, String>();
		
		//读取word文本内容
		Range range = hdt.getRange();
		String docTitle1 = range.getParagraph(0).text();
		doc.put("docTitle1", docTitle1);
		
		TableIterator tableIt = new TableIterator(range); 
		//迭代文档中的表格
		int ii = 0;
		int zql = 0;
		int docItemNum = 0;
		while (tableIt.hasNext()){
			Table tb = (Table) tableIt.next();
			ii++;
			System.out.println("第"+ii+"个表格数据...................");
			//迭代行，默认从0开始
			outerloop:
			for (int i = 0; i < tb.numRows(); i++) {
				TableRow tr = tb.getRow(i);
				//只读前8行，标题部分
				//if(i >=8) break;
				//迭代列，默认从0开始
				for (int j = 0; j < tr.numCells(); j++) {
					TableCell td = tr.getCell(j);//取得单元格
					//取得单元格的内容
					for(int k=0;k<td.numParagraphs();k++){
						Paragraph para =td.getParagraph(k);
						
						String s = para.text().trim();
						if(s.trim().equals("")){
							break;
						}
						switch(docLike.toDoc(s)){
							case 值班总队领导:zql = 1;continue;
							case 带班领导:zql = 2;continue;
							case 值班警力:zql = 3;continue;
							case 系统维护:zql = 4;continue;
							case 信通保障:zql = 5;continue;
							case 故障处理:zql = 6;continue;
							case 其他值班情况:zql = 8;continue;
							default: System.out.println("default");break;
						}
						
						switch(zql){
							case 1:zql=0;doc.put("docBigleader", s);continue;
							case 2:zql=0;doc.put("docClassleader", s);continue;
							case 3:zql=0;doc.put("docClassnum", s);continue;
							case 4:zql=0;doc.put("docXtwh", s);continue;
							case 5:zql=0;doc.put("docXtbz", s);continue;
							case 6:zql=7;doc.put("docGzcl", s);continue;
							case 8:zql=0;doc.put("docZbqk", s);continue;
							default:System.out.println("default");
						}
						if(zql == 7 && s.trim().length()>5){
							docItemNum++;
							docItem.put(String.valueOf(docItemNum), s);
						}
					} //end for 
				} //end for
			} //end for
		} //end while
		//System.out.println(range.text());
		List<TSDocument1> listDoc = new ArrayList<TSDocument1>();
		for(Map.Entry<String, String> entry : docItem.entrySet()){
			TSDocument1 tsdoctemp = new TSDocument1();
			
			tsdoctemp.setDocTitle1(doc.get("docTitle1"));
			tsdoctemp.setDocBigleader(doc.get("docBigleader"));
			tsdoctemp.setDocClassleader(doc.get("docClassleader"));
			tsdoctemp.setDocClassnum(doc.get("docClassnum"));
			tsdoctemp.setDocXtwh(doc.get("docXtwh"));
			tsdoctemp.setDocXtbz(doc.get("docXtbz"));
			tsdoctemp.setDocGzcl(doc.get("docGzcl"));
			tsdoctemp.setDocZbqk(doc.get("docZbqk"));
			
			tsdoctemp.setDocIndex(entry.getKey());
			tsdoctemp.setDocTxt(entry.getValue());
			
			listDoc.add(tsdoctemp);
		}
		return listDoc;
	}

	/** 
	* 实现对word读取和修改操作 
	*
	* @param filePath 
	*word模板路径和名称 
	* @param map 
	*待填充的数据，从数据库读取 
	 * @throws IOException 
	 * @throws Exception 
	*/
	@SuppressWarnings("resource")
	public static ArrayList<Object> readwriteDocx(String filePath) throws IOException{
		FileInputStream in = null;
		try{
			in = new FileInputStream(new File(filePath));
		}
		catch (FileNotFoundException e1){
			e1.printStackTrace();
		}
		XWPFDocument doc = new XWPFDocument(in);
		List<XWPFParagraph> paras = doc.getParagraphs();
		
		ArrayList<Object> listd = new ArrayList<Object>();
		ZSDoc zsDoc = new ZSDoc();
		ArrayList<ZSDocItem> listzsDocitem = new ArrayList<ZSDocItem>();
		
		String docTitle = null;
		for (XWPFParagraph para : paras){
			if(para.getText().trim().length()>4){
				docTitle = para.getText().trim();
				zsDoc.setDocTitle(oConvertUtils.replaceBlank(docTitle.trim()));
			}
		}
		Iterator<XWPFTable> it = doc.getTablesIterator();//得到word中的表格
		int zql = 0;
		int docItemNum = 0;
		while(it.hasNext()){
			XWPFTable table = it.next();
			List<XWPFTableRow> rows=table.getRows();
			//读取每一行数据
			for (int i = 0; i < rows.size(); i++) {
				XWPFTableRow  row = rows.get(i);
				//读取每一列数据
				List<XWPFTableCell> cells = row.getTableCells();
				for (int j = 0;j < cells.size(); j++) {
					XWPFTableCell cell=cells.get(j);
					//输出当前的单元格的数据
					String s = cell.getText().trim();
					if(s.trim().equals("")){
						break;
					}
					switch(docLike.toDoc(s)){
						case 值班总队领导:zql = 1;continue;
						case 带班领导:zql = 2;continue;
						case 值班警力:zql = 3;continue;
						case 系统维护:zql = 4;continue;
						case 信通保障:zql = 5;continue;
						case 故障处理:zql = 6;continue;
						case 其他值班情况:zql = 8;continue;
						default: break;
					}
					switch(zql){
						case 1:zql=0;zsDoc.setDocBigleader(s);break;
						case 2:zql=0;zsDoc.setDocClassleader(s);break;
						case 3:zql=0;zsDoc.setDocClassnum(s);break;
						case 4:zql=0;zsDoc.setDocXtwh(s);break;
						case 5:zql=0;zsDoc.setDocXtbz(s);break;
						case 6:zql=7;zsDoc.setDocGzcl(s);break;
						case 8:zql=0;zsDoc.setDocZbqk(s);break;
						default:break;
					}
					if(zql == 7 && s.trim().length()>5){
						docItemNum++;
						ZSDocItem zsDocItem = new ZSDocItem();
						zsDocItem.setDocIndex(String.valueOf(docItemNum));
						zsDocItem.setDocTxt(s);
						zsDocItem.setDocTitle(oConvertUtils.replaceBlank(docTitle.trim()));
						listzsDocitem.add(docItemNum-1, zsDocItem);
					}
				}
			}
		}
		listd.add(0,zsDoc);
		for(int i=0;i<listzsDocitem.size();i++){
			listd.add(i+1,listzsDocitem.get(i));
		}
		if(listzsDocitem.isEmpty()){
			ZSDocItem zsDocItem = new ZSDocItem();
			zsDocItem.setDocTitle(docTitle);
			listd.add(1,zsDocItem);
		}
		return listd;
	}
	
	/** 
	* 实现对word读取和修改操作 
	*
	* @param filePath 
	*word模板路径和名称 
	* @param map 
	*待填充的数据，从数据库读取 
	 * @throws IOException 
	 * @throws Exception 
	*/
	@SuppressWarnings("resource")
	public static void readwriteDocx1(String filePath) throws IOException{
		FileInputStream in = null;
		try{
			in = new FileInputStream(new File(filePath));
		}
		catch (FileNotFoundException e1){
			e1.printStackTrace();
		}
		XWPFDocument doc = new XWPFDocument(in);
		/*try {
			doc = new XWPFDocument(in);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
		List<XWPFParagraph> paras = doc.getParagraphs();
		for (XWPFParagraph para : paras){
			System.out.println(para.getText());
			//当前段落的属性  
			// CTPPr pr = para.getCTP().getPPr();
		}  
		//获取文档中所有的表格  
		List<XWPFTable> tables = doc.getTables();
		List<XWPFTableRow> rows;
		List<XWPFTableCell> cells;
		for (XWPFTable table : tables){
			//表格属性
		    // CTTblPr pr = table.getCTTbl().getTblPr();  
			//获取表格对应的行  
			rows = table.getRows();
			for (XWPFTableRow row : rows){
				//获取行对应的单元格
				cells = row.getTableCells();		  
				for (XWPFTableCell cell : cells){
					System.out.println(cell.getText());;  
				}
		   }
		} 
		try {
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public enum docLike{
		值班总队领导,带班领导,值班警力,系统维护,信通保障,故障处理,其他值班情况,晚间巡视情况,空,其他;
		public static docLike toDoc(String str){
			try{
				if(str.trim().equals("")){
					return 空;
				}
				if(str.equals("其他值班情况（队伍建设、系统点名、学习情况等）")){return 其他值班情况;}
				if(str.equals("晚间巡视情况（23时）")){return 晚间巡视情况;}
				return valueOf(str.trim());
			}catch(Exception ex){
				return 其他;
			}
		}
	}
	//---------------------------------------------------------------------
	
	/**
	 * 合同管理页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "htGroupTabs")
	public ModelAndView htGroupTabs(HttpServletRequest request) {
		List<ZSTypegroup> htgroupList = systemService.loadAll(ZSTypegroup.class);
		request.setAttribute("typegroupList", htgroupList);
		return new ModelAndView("system/type/htGroupTabs");
	}
	/**
	 * 类型字典列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "jltypeGroupTabs")
	public ModelAndView jltypeGroupTabs(HttpServletRequest request) {
		List<ZSTypegroup> typegroupList = systemService.findByProperty(ZSTypegroup.class, "id", "1");
		request.setAttribute("typegroupList", typegroupList);
		return new ModelAndView("system/type/jltypeGroupTabs");
	}
	/**
	 * 类型列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "jltypeList")
	public ModelAndView jltypeList(HttpServletRequest request) {
		String typegroupid = request.getParameter("typegroupid");
		ZSTypegroup typegroup = systemService.getEntity(ZSTypegroup.class, typegroupid);
		request.setAttribute("typegroup", typegroup);
		return new ModelAndView("system/type/jltypeList");
	}
	/**
	 * easyuiAJAX请求数据
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 * @param user
	 */

	@RequestMapping(params = "jltypeGrid")
	public void jltypeGrid(HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		String typegroupid = request.getParameter("typegroupid");
		//update-begin--Author:zhaojunfu  Date:20130528 for：131 追加查询条件
		String typename = request.getParameter("typename");
		CriteriaQuery cq = new CriteriaQuery(ZSType.class, dataGrid);
		cq.eq("ZSTypegroup.id", typegroupid);
		cq.like("typename", typename);
		//update-end--Author:zhaojunfu  Date:20130528 for：131 追加查询条件
		cq.add();
		this.systemService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}
}
