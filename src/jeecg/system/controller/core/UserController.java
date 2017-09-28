package jeecg.system.controller.core;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jeecg.demo.entity.test.JpPersonEntity;
import jeecg.demo.service.test.JeecgJdbcServiceI;
import jeecg.system.pojo.base.TSDepart;
import jeecg.system.pojo.base.TSFunction;
import jeecg.system.pojo.base.TSRole;
import jeecg.system.pojo.base.TSRoleFunction;
import jeecg.system.pojo.base.TSRoleUser;
import jeecg.system.pojo.base.TSUser;
import jeecg.system.pojo.base.ZSDoc;
import jeecg.system.pojo.base.ZSType;
import jeecg.system.pojo.base.ZSZzc;
import jeecg.system.pojo.base.ZSZzcDel;
import jeecg.system.service.SystemService;
import jeecg.system.service.UserService;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.common.UploadFile;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.ComboBox;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.common.model.json.ValidForm;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.ExceptionUtil;
import org.jeecgframework.core.util.ListtoMenu;
import org.jeecgframework.core.util.PasswordUtil;
import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.core.util.SetListSort;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.core.util.oConvertUtils;
import org.jeecgframework.core.util.excel.ExcelUtil;
import org.jeecgframework.tag.core.easyui.TagUtil;
import org.jeecgframework.tag.vo.datatable.DataTableReturn;
import org.jeecgframework.tag.vo.datatable.DataTables;
import org.jeecgframework.tag.vo.datatable.SortDirection;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

/**
 * @ClassName: UserController
 * @Description: TODO(用户管理处理类)
 * @author jeecg
 */
@Controller
@RequestMapping("/userController")
public class UserController {
	/**
	 * Logger for this class
	 */
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(UserController.class);

	private UserService userService;
	private SystemService systemService;
	private DocumentHandler documentHandler;
	private JeecgJdbcServiceI jeecgJdbcService;
	private String message = null;

	@Autowired
	public void setJeecgJdbcService(JeecgJdbcServiceI jeecgJdbcService) {
		this.jeecgJdbcService = jeecgJdbcService;
	}

	@Autowired
	public void setDocumentHandler(DocumentHandler documentHandler) {
		this.documentHandler = documentHandler;
	}

	@Autowired
	public void setSystemService(SystemService systemService) {
		this.systemService = systemService;
	}

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	/**
	 * 菜单列表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "menu")
	public void menu(HttpServletRequest request, HttpServletResponse response) {
		SetListSort sort = new SetListSort();
		TSUser u = ResourceUtil.getSessionUserName();
		// 登陆者的权限
		Set<TSFunction> loginActionlist = new HashSet();// 已有权限菜单
		List<TSRoleUser> rUsers = systemService.findByProperty(TSRoleUser.class, "TSUser.id", u.getId());
		for (TSRoleUser ru : rUsers) {
			TSRole role = ru.getTSRole();
			List<TSRoleFunction> roleFunctionList = systemService.findByProperty(TSRoleFunction.class, "TSRole.id",
					role.getId());
			if (roleFunctionList.size() > 0) {
				for (TSRoleFunction roleFunction : roleFunctionList) {
					TSFunction function = (TSFunction) roleFunction.getTSFunction();
					loginActionlist.add(function);
				}
			}
		}
		List<TSFunction> bigActionlist = new ArrayList();// 一级权限菜单
		List<TSFunction> smailActionlist = new ArrayList();// 二级权限菜单
		if (loginActionlist.size() > 0) {
			for (TSFunction function : loginActionlist) {
				if (function.getFunctionLevel() == 0) {
					bigActionlist.add(function);
				} else if (function.getFunctionLevel() == 1) {
					smailActionlist.add(function);
				}
			}
		}
		// 菜单栏排序
		Collections.sort(bigActionlist, sort);
		Collections.sort(smailActionlist, sort);
		String logString = ListtoMenu.getMenu(bigActionlist, smailActionlist);
		// request.setAttribute("loginMenu",logString);
		try {
			response.getWriter().write(logString);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 用户列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "user1")
	public String user1(HttpServletRequest request) {
		// ----------------------------------------------------------------
		// update-begin--Author:Quainty Date:20130529 for：用户管理，添加按照部门过滤的功能
		// 给部门查询条件中的下拉框准备数据
		String departsReplace = "";
		List<TSDepart> departList = systemService.getList(TSDepart.class);
		for (TSDepart depart : departList) {
			if (departsReplace.length() > 0) {
				departsReplace += ",";
			}
			departsReplace += depart.getDepartname() + "_" + depart.getId();
		}
		request.setAttribute("departsReplace", departsReplace);
		// update-end--Author:Quainty Date:20130529 for：用户管理，添加按照部门过滤的功能
		// ----------------------------------------------------------------
		return "system/user/userList";
		// return "system/user/userList-search-demo";
	}

	/**
	 * 用户信息
	 * 
	 * @return
	 */
	@RequestMapping(params = "userinfo")
	public String userinfo(HttpServletRequest request) {
		TSUser user = ResourceUtil.getSessionUserName();
		request.setAttribute("user", user);
		return "system/user/userinfo";
	}

	/**
	 * 修改密码
	 * 
	 * @return
	 */
	@RequestMapping(params = "changepassword")
	public String changepassword(HttpServletRequest request) {
		TSUser user = ResourceUtil.getSessionUserName();
		request.setAttribute("user", user);
		return "system/user/changepassword";
	}

	/**
	 * 修改密码
	 * 
	 * @return
	 */
	@RequestMapping(params = "savenewpwd")
	@ResponseBody
	public AjaxJson savenewpwd(HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		TSUser user = ResourceUtil.getSessionUserName();
		String password = oConvertUtils.getString(request.getParameter("password"));
		String newpassword = oConvertUtils.getString(request.getParameter("newpassword"));
		String pString = password;
		if (!pString.equals(user.getPassword())) {
			j.setMsg("原密码不正确");
			j.setSuccess(false);
		} else {
			try {
				user.setPassword(newpassword);
			} catch (Exception e) {
				e.printStackTrace();
			}
			systemService.updateEntitie(user);
			j.setMsg("修改成功");

		}
		return j;
	}
	/**
	 * 修改密码
	 * 
	 * @return
	 */
	@RequestMapping(params = "savenewpwd1")
	@ResponseBody
	public AjaxJson savenewpwd1(HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		TSUser user = ResourceUtil.getSessionUserName();
		String password = oConvertUtils.getString(request.getParameter("password"));
		String newpassword = oConvertUtils.getString(request.getParameter("newpassword"));
		String pString = PasswordUtil.encrypt(user.getUserName(), password, PasswordUtil.getStaticSalt());
		if (!pString.equals(user.getPassword())) {
			j.setMsg("原密码不正确");
			j.setSuccess(false);
		} else {
			try {
				user.setPassword(PasswordUtil.encrypt(user.getUserName(), newpassword, PasswordUtil.getStaticSalt()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			systemService.updateEntitie(user);
			j.setMsg("修改成功");

		}
		return j;
	}

	/**
	 * 得到角色列表
	 * 
	 * @return
	 */
	@RequestMapping(params = "role")
	@ResponseBody
	public List<ComboBox> role(HttpServletResponse response, HttpServletRequest request, ComboBox comboBox) {
		String id = request.getParameter("id");
		List<ComboBox> comboBoxs = new ArrayList<ComboBox>();
		List<TSRole> roles = new ArrayList();
		if (StringUtil.isNotEmpty(id)) {
			List<TSRoleUser> roleUser = systemService.findByProperty(TSRoleUser.class, "TSUser.id", id);
			if (roleUser.size() > 0) {
				for (TSRoleUser ru : roleUser) {
					roles.add(ru.getTSRole());
				}
			}
		}
		List<TSRole> roleList = systemService.getList(TSRole.class);
		comboBoxs = TagUtil.getComboBox(roleList, roles, comboBox);
		return comboBoxs;
	}

	/**
	 * 得到部门列表
	 * 
	 * @return
	 */
	@RequestMapping(params = "depart")
	@ResponseBody
	public List<ComboBox> depart(HttpServletResponse response, HttpServletRequest request, ComboBox comboBox) {
		String id = request.getParameter("id");
		List<ComboBox> comboBoxs = new ArrayList<ComboBox>();
		List<TSDepart> departs = new ArrayList();
		if (StringUtil.isNotEmpty(id)) {
			TSUser user = systemService.get(TSUser.class, id);
			if (user.getTSDepart() != null) {
				TSDepart depart = systemService.get(TSDepart.class, user.getTSDepart().getId());
				departs.add(depart);
			}
		}
		List<TSDepart> departList = systemService.getList(TSDepart.class);
		comboBoxs = TagUtil.getComboBox(departList, departs, comboBox);
		return comboBoxs;
	}

	/**
	 * easyuiAJAX用户列表请求数据
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = "datagrid1")
	public void datagrid1(TSUser user, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(TSUser.class, dataGrid);
		// 查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, user);

		// ----------------------------------------------------------------
		// update-begin--Author:Quainty Date:20130522 for：用户管理，添加按照部门过滤的功能
		// 追加部门条件查询 (如果是input型，传的是部门名；如果是combo型，传的是部门ID)
		String departname = oConvertUtils.getString(request.getParameter("TSDepart_departname"));
		if (!StringUtil.isEmpty(departname)) {
			DetachedCriteria dc = cq.getDetachedCriteria();
			DetachedCriteria dcDepart = dc.createCriteria("TSDepart");
			// dcDepart.add(Restrictions.like("departname", "%" + departname +
			// "%"));// 部门名
			dcDepart.add(Restrictions.eq("id", departname));// 部门ID
		}
		// update-end--Author:Quainty Date:20130522 for：用户管理，添加按照部门过滤的功能
		// ----------------------------------------------------------------

		// String searchfield =
		// oConvertUtils.getString(request.getParameter("searchfield"));//传入字段名称
		// String
		// value=oConvertUtils.getString(request.getParameter(searchfield));//传入值
		// Short[] userstate = new Short[] { Globals.User_Normal,
		// Globals.User_ADMIN };
		// cq.in("status", userstate);
		// if (searchfield != null) {
		// cq.like(searchfield, value);//匹配查询
		// }
		// cq.add();

		Short[] userstate = new Short[] { Globals.User_Normal, Globals.User_ADMIN };
		cq.in("status", userstate);
		cq.add();
		this.systemService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 用户信息录入和更新
	 * 
	 * @param user
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(TSUser user, HttpServletRequest req) {
		AjaxJson j = new AjaxJson();

		// ----------------------------------------------------------------
		// update-begin--Author:shiyanping Date:20130319 for：admin账户不能删除
		if ("admin".equals(user.getUserName())) {
			message = "超级管理员[admin]不可删除";
			j.setMsg(message);
			return j;
		}
		// update-end--Author:shiyanping Date:20130319 for：admin账户不能删除
		// ----------------------------------------------------------------
		user = systemService.getEntity(TSUser.class, user.getId());
		List<TSRoleUser> roleUser = systemService.findByProperty(TSRoleUser.class, "TSUser.id", user.getId());
		if (!user.getStatus().equals(Globals.User_ADMIN)) {
			if (roleUser.size() > 0) {
				// 删除用户时先删除用户和角色关系表
				delRoleUser(user);
				userService.delete(user);
				message = "用户：" + user.getUserName() + "删除成功";
				systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
			} else {
				userService.delete(user);
				message = "用户：" + user.getUserName() + "删除成功";
			}
		} else {
			message = "超级管理员不可删除";
		}

		j.setMsg(message);
		return j;
	}

	public void delRoleUser(TSUser user) {
		// 同步删除用户角色关联表
		List<TSRoleUser> roleUserList = systemService.findByProperty(TSRoleUser.class, "TSUser.id", user.getId());
		if (roleUserList.size() >= 1) {
			for (TSRoleUser tRoleUser : roleUserList) {
				systemService.delete(tRoleUser);
			}
		}
	}

	/**
	 * 检查用户名
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "checkUser")
	@ResponseBody
	public ValidForm checkUser(HttpServletRequest request) {
		ValidForm v = new ValidForm();
		String userName = oConvertUtils.getString(request.getParameter("param"));
		String code = oConvertUtils.getString(request.getParameter("code"));
		List<TSUser> roles = systemService.findByProperty(TSUser.class, "userName", userName);
		if (roles.size() > 0 && !code.equals(userName)) {
			v.setInfo("用户名已存在");
			v.setStatus("n");
		}
		return v;
	}

	/**
	 * 用户录入
	 * 
	 * @param user
	 * @param req
	 * @return
	 */

	@RequestMapping(params = "saveUser1")
	@ResponseBody
	public AjaxJson saveUser1(HttpServletRequest req, TSUser user) {
		AjaxJson j = new AjaxJson();
		// 得到用户的角色
		String roleid = oConvertUtils.getString(req.getParameter("roleid"));
		String password = oConvertUtils.getString(req.getParameter("password"));
		if (StringUtil.isNotEmpty(user.getId())) {
			TSUser users = systemService.getEntity(TSUser.class, user.getId());
			users.setEmail(user.getEmail());
			users.setOfficePhone(user.getOfficePhone());
			users.setMobilePhone(user.getMobilePhone());
			users.setTSDepart(user.getTSDepart());
			users.setRealName(user.getRealName());
			users.setStatus(Globals.User_Normal);
			users.setActivitiSync(user.getActivitiSync());
			systemService.updateEntitie(users);
			List<TSRoleUser> ru = systemService.findByProperty(TSRoleUser.class, "TSUser.id", user.getId());
			systemService.deleteAllEntitie(ru);
			message = "用户: " + users.getUserName() + "更新成功";
			if (StringUtil.isNotEmpty(roleid)) {
				saveRoleUser(users, roleid);
			}
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} else {
			TSUser users = systemService.findUniqueByProperty(TSUser.class, "userName", user.getUserName());
			if (users != null) {
				message = "用户: " + users.getUserName() + "已经存在";
			} else {
				user.setPassword(PasswordUtil.encrypt(user.getUserName(), password, PasswordUtil.getStaticSalt()));
				if (user.getTSDepart().equals("")) {
					user.setTSDepart(null);
				}
				user.setStatus(Globals.User_Normal);
				systemService.save(user);
				message = "用户: " + user.getUserName() + "添加成功";
				if (StringUtil.isNotEmpty(roleid)) {
					saveRoleUser(user, roleid);
				}
				systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
			}

		}
		j.setMsg(message);

		return j;
	}

	protected void saveRoleUser(TSUser user, String roleidstr) {
		String[] roleids = roleidstr.split(",");
		for (int i = 0; i < roleids.length; i++) {
			TSRoleUser rUser = new TSRoleUser();
			TSRole role = systemService.getEntity(TSRole.class, roleids[i]);
			rUser.setTSRole(role);
			rUser.setTSUser(user);
			systemService.save(rUser);

		}
	}

	/**
	 * 用户选择角色跳转页面
	 * 
	 * @return
	 */
	@RequestMapping(params = "roles")
	public String roles() {
		return "system/user/users";
	}

	/**
	 * 角色显示列表
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = "datagridRole")
	public void datagridRole(HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(TSRole.class, dataGrid);
		this.systemService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * easyuiAJAX请求数据： 用户选择角色列表
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 * @param user
	 */
	@RequestMapping(params = "addorupdate1")
	public ModelAndView addorupdate1(TSUser user, HttpServletRequest req) {
		List<TSDepart> departList = systemService.getList(TSDepart.class);
		req.setAttribute("departList", departList);
		if (StringUtil.isNotEmpty(user.getId())) {
			user = systemService.getEntity(TSUser.class, user.getId());
			req.setAttribute("user", user);
			idandname(req, user);
		}
		return new ModelAndView("system/user/user");

	}

	public void idandname(HttpServletRequest req, TSUser user) {
		List<TSRoleUser> roleUsers = systemService.findByProperty(TSRoleUser.class, "TSUser.id", user.getId());
		String roleId = "";
		String roleName = "";
		if (roleUsers.size() > 0) {
			for (TSRoleUser tRoleUser : roleUsers) {
				roleId += tRoleUser.getTSRole().getId() + ",";
				roleName += tRoleUser.getTSRole().getRoleName() + ",";
			}
		}
		req.setAttribute("id", roleId);
		req.setAttribute("roleName", roleName);

	}

	/**
	 * 根据部门和角色选择用户跳转页面
	 */
	@RequestMapping(params = "choose")
	public String choose(HttpServletRequest request) {
		List<TSRole> roles = systemService.loadAll(TSRole.class);
		request.setAttribute("roleList", roles);
		return "system/membership/checkuser";
	}

	/**
	 * 部门和角色选择用户的panel跳转页面
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "chooseUser")
	public String chooseUser(HttpServletRequest request) {
		String departid = request.getParameter("departid");
		String roleid = request.getParameter("roleid");
		request.setAttribute("roleid", roleid);
		request.setAttribute("departid", departid);
		return "system/membership/userlist";
	}

	/**
	 * 部门和角色选择用户的用户显示列表
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = "datagridUser")
	public void datagridUser(HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		String departid = request.getParameter("departid");
		String roleid = request.getParameter("roleid");
		CriteriaQuery cq = new CriteriaQuery(TSUser.class, dataGrid);
		if (departid.length() > 0) {
			cq.eq("TDepart.departid", oConvertUtils.getInt(departid, 0));
			cq.add();
		}
		String userid = "";
		if (roleid.length() > 0) {
			List<TSRoleUser> roleUsers = systemService.findByProperty(TSRoleUser.class, "TRole.roleid",
					oConvertUtils.getInt(roleid, 0));
			if (roleUsers.size() > 0) {
				for (TSRoleUser tRoleUser : roleUsers) {
					userid += tRoleUser.getTSUser().getId() + ",";
				}
			}
			cq.in("userid", oConvertUtils.getInts(userid.split(",")));
			cq.add();
		}
		this.systemService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 根据部门和角色选择用户跳转页面
	 */
	@RequestMapping(params = "roleDepart")
	public String roleDepart(HttpServletRequest request) {
		List<TSRole> roles = systemService.loadAll(TSRole.class);
		request.setAttribute("roleList", roles);
		return "system/membership/roledepart";
	}

	/**
	 * 部门和角色选择用户的panel跳转页面
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "chooseDepart")
	public ModelAndView chooseDepart(HttpServletRequest request) {
		String nodeid = request.getParameter("nodeid");
		ModelAndView modelAndView = null;
		if (nodeid.equals("role")) {
			modelAndView = new ModelAndView("system/membership/users");
		} else {
			modelAndView = new ModelAndView("system/membership/departList");
		}
		return modelAndView;
	}

	/**
	 * 部门和角色选择用户的用户显示列表
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = "datagridDepart")
	public void datagridDepart(HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(TSDepart.class, dataGrid);
		systemService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 测试
	 * 
	 * @param user
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "test")
	public void test(HttpServletRequest request, HttpServletResponse response) {
		String jString = request.getParameter("_dt_json");
		DataTables dataTables = new DataTables(request);
		CriteriaQuery cq = new CriteriaQuery(TSUser.class, dataTables);
		String username = request.getParameter("userName");
		if (username != null) {
			cq.like("userName", username);
			cq.add();
		}
		DataTableReturn dataTableReturn = systemService.getDataTableReturn(cq, true);
		TagUtil.datatable(response, dataTableReturn, "id,userName,mobilePhone,TSDepart_departname");
	}

	/**
	 * 用户列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "index")
	public String index() {
		return "bootstrap/main";
	}

	/**
	 * 用户列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "main")
	public String main() {
		return "bootstrap/test";
	}

	/**
	 * 测试
	 * 
	 * @return
	 */
	@RequestMapping(params = "testpage")
	public String testpage(HttpServletRequest request) {
		return "test/test";
	}

	/**
	 * 设置签名跳转页面
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "addsign")
	public ModelAndView addsign(HttpServletRequest request) {
		String id = request.getParameter("id");
		request.setAttribute("id", id);
		return new ModelAndView("system/user/usersign");
	}

	/**
	 * 用户录入
	 * 
	 * @param user
	 * @param req
	 * @return
	 */

	@RequestMapping(params = "savesign", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson savesign(HttpServletRequest req) {
		UploadFile uploadFile = new UploadFile(req);
		String id = uploadFile.get("id");
		TSUser user = systemService.getEntity(TSUser.class, id);
		uploadFile.setRealPath("signatureFile");
		uploadFile.setCusPath("signature");
		uploadFile.setByteField("signature");
		uploadFile.setBasePath("resources");
		uploadFile.setRename(false);
		uploadFile.setObject(user);
		AjaxJson j = new AjaxJson();
		message = user.getUserName() + "设置签名成功";
		systemService.uploadFile(uploadFile);
		systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		j.setMsg(message);

		return j;
	}

	// ----------------------------------------------------------------
	// update-begin--Author:wangyang Date:20130331 for：组合查询测试
	// ----------------------------------------------------------------
	/**
	 * 测试组合查询功能
	 * 
	 * @param user
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = "testSearch")
	public void testSearch(TSUser user, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(TSUser.class, dataGrid);
		if (user.getUserName() != null) {
			cq.like("userName", user.getUserName());
		}
		if (user.getRealName() != null) {
			cq.like("realName", user.getRealName());
		}
		cq.add();
		this.systemService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	// ----------------------------------------------------------------
	// update-end--Author:wangyang Date:20130331 for：组合查询测试
	// ----------------------------------------------------------------
	/**
	 * 不在岗名单列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "allZzc")
	public String allZzc(HttpServletRequest request) {
		List<ZSType> departList = systemService.findByProperty(ZSType.class, "ZSTypegroup.id", "1");
		Collections.sort(departList);
		request.setAttribute("departList" , departList);
		
		List<ZSType> zwList = systemService.findByProperty(ZSType.class, "ZSTypegroup.id", "2");
		Collections.sort(zwList);
		request.setAttribute("zwList" , zwList);
		
		return "system/user/zzcList";
	}
	/**
	 * 民警不在岗管理页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "mjZzc")
	public String mjZzc(HttpServletRequest request) {
		TSUser u = ResourceUtil.getSessionUserName();
		if(u.getBrowser() != null && !"".equals(u.getBrowser())){
			request.setAttribute("departList" , u.getBrowser().trim());
		}
		
		List<ZSType> zwList = systemService.findByProperty(ZSType.class, "ZSTypegroup.id", "2");
		Collections.sort(zwList);
		request.setAttribute("zwList" , zwList);
		
		return "system/user/zzcmjList";
	}

	/**
	 * 已离京列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "yljZzc")
	public String yljZzc(HttpServletRequest request) {
		return "system/user/zzcyljList";
	}

	/**
	 * 未离京列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "wljZzc")
	public String wljZzc(HttpServletRequest request) {
		return "system/user/zzcwljList";
	}
	/**
	 * 拟离京列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "nljZzc")
	public String nljZzc(HttpServletRequest request) {
		return "system/user/zzcnljList";
	}
	/**
	 * 不在岗列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "bzgZzc")
	public String bzgZzc(HttpServletRequest request) {
		return "system/user/zzcbzgList";
	}
	/**
	 * 汇总列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "hzZzc")
	public String hzZzc(HttpServletRequest request) {
		return "system/user/zzchzList";
	}

	/**
	 * easyuiAJAX用户列表请求数据
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = "zzcdatagrid")
	public void zzcdatagrid(ZSZzc zsZzc, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid)
			throws ParseException {
		CriteriaQuery cq = new CriteriaQuery(ZSZzc.class, dataGrid);

		if (zsZzc.getFjdate() != null) {
			String fjdate = oConvertUtils.getString(zsZzc.getFjdate().replace("-", "").trim());
//			cq.eq("fjdate", fjdate);
			zsZzc.setFjdate(fjdate);
		}
		if (zsZzc.getJsdate() != null) {
			String jsdate = oConvertUtils.getString(zsZzc.getJsdate().replace("-", "").trim());
//			cq.eq("jsdate", jsdate);
			zsZzc.setJsdate(jsdate);
		}
		if (zsZzc.getKsdate() != null) {
			String ksdate = oConvertUtils.getString(zsZzc.getKsdate().replace("-", "").trim());
//			cq.eq("ksdate", ksdate);
			zsZzc.setKsdate(ksdate);
		}
		if (zsZzc.getLjdate() != null) {
			String ljdate = oConvertUtils.getString(zsZzc.getLjdate().replace("-", "").trim());
//			cq.eq("ljdate", ljdate);
			zsZzc.setLjdate(ljdate);
		}
		if (zsZzc.getSpdate() != null) {
			String spdate = oConvertUtils.getString(zsZzc.getSpdate().replace("-", "").trim());
//			cq.eq("spdate", spdate);
			zsZzc.setSpdate(spdate);
		}
		cq.addOrder("zzcdepart", SortDirection.asc);
		cq.add();
		// 查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, zsZzc);
		this.systemService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}
	
	/**
	 * easyuiAJAX用户列表请求数据
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = "zzcmjdatagrid")
	public void zzcmjdatagrid(ZSZzc zsZzc, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid)
			throws ParseException {
		TSUser u = ResourceUtil.getSessionUserName();
		CriteriaQuery cq = new CriteriaQuery(ZSZzc.class, dataGrid);

		if(u.getBrowser() != null && !"".equals(u.getBrowser())){
			request.setAttribute("departList" , u.getBrowser().trim());
			cq.eq("zzcdepart", u.getBrowser().trim());
		}
		if (zsZzc.getFjdate() != null) {
			String fjdate = oConvertUtils.getString(zsZzc.getFjdate().replace("-", "").trim());
			cq.eq("fjdate", fjdate);
			zsZzc.setFjdate(fjdate);
		}
		if (zsZzc.getJsdate() != null) {
			String jsdate = oConvertUtils.getString(zsZzc.getJsdate().replace("-", "").trim());
			cq.eq("jsdate", jsdate);
			zsZzc.setJsdate(jsdate);
		}
		if (zsZzc.getKsdate() != null) {
			String ksdate = oConvertUtils.getString(zsZzc.getKsdate().replace("-", "").trim());
			cq.eq("ksdate", ksdate);
			zsZzc.setKsdate(ksdate);
		}
		if (zsZzc.getLjdate() != null) {
			String ljdate = oConvertUtils.getString(zsZzc.getLjdate().replace("-", "").trim());
			cq.eq("ljdate", ljdate);
			zsZzc.setLjdate(ljdate);
		}
		if (zsZzc.getSpdate() != null) {
			String spdate = oConvertUtils.getString(zsZzc.getSpdate().replace("-", "").trim());
			cq.eq("spdate", spdate);
			zsZzc.setSpdate(spdate);
		}
		cq.addOrder("zzcdepart", SortDirection.asc);
		cq.add();
		// 查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, zsZzc);
		this.systemService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 已离京easyuiAJAX用户列表请求数据
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = "zzcyljdatagrid")
	public void zzcyljdatagrid(ZSZzc zsZzc, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid)
			throws ParseException {
		String qdate = "";
		if (request.getParameter("zzcyljqdate") != null && !"".equals(request.getParameter("zzcyljqdate"))) {
			qdate = oConvertUtils.getString(request.getParameter("zzcyljqdate").replace("-", "").trim());
			JSONObject jObject1 = this.jeecgJdbcService.getZzcDatagridylj1(zsZzc, dataGrid, qdate);
			responseDatagrid(response, jObject1);
		}
	}

	/**
	 * 未离京easyuiAJAX用户列表请求数据
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = "zzcwljdatagrid")
	public void zzcwljdatagrid(ZSZzc zsZzc, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid)
			throws ParseException {
		String qdate = "";
		if (request.getParameter("zzcwljqdate") != null && !"".equals(request.getParameter("zzcwljqdate"))) {
			qdate = oConvertUtils.getString(request.getParameter("zzcwljqdate").replace("-", "").trim());
			JSONObject jObject2 = this.jeecgJdbcService.getZzcDatagridwlj1(zsZzc, dataGrid, qdate);
			responseDatagrid(response, jObject2);
		}
	}
	
	/**
	 * 拟离京easyuiAJAX用户列表请求数据
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = "zzcnljdatagrid")
	public void zzcnljdatagrid(ZSZzc zsZzc, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid)
			throws ParseException {
		String qdate = "";
		if (request.getParameter("zzcnljqdate") != null && !"".equals(request.getParameter("zzcnljqdate"))) {
			qdate = oConvertUtils.getString(request.getParameter("zzcnljqdate").replace("-", "").trim());
			JSONObject jObject2 = this.jeecgJdbcService.getZzcDatagridnlj1(zsZzc, dataGrid, qdate);
			responseDatagrid(response, jObject2);
		}
	}
	/**
	 * 不在岗easyuiAJAX用户列表请求数据
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = "zzcbzgdatagrid")
	public void zzcbzgdatagrid(ZSZzc zsZzc, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid)
			throws ParseException {
		String qdate = "";
		if (request.getParameter("zzcbzgqdate") != null && !"".equals(request.getParameter("zzcbzgqdate"))) {
			qdate = oConvertUtils.getString(request.getParameter("zzcbzgqdate").replace("-", "").trim());
			JSONObject jObject2 = this.jeecgJdbcService.getZzcDatagridbzg1(zsZzc, dataGrid, qdate);
			responseDatagrid(response, jObject2);
		}
	}
	
	/**
	 * 汇总easyuiAJAX用户列表请求数据
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = "zzchzdatagrid")
	public void zzchzdatagrid(ZSZzc zsZzc, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid)
			throws ParseException {
		String qdate = "";
		if (request.getParameter("zzchzqdate") != null && !"".equals(request.getParameter("zzchzqdate"))) {
			qdate = oConvertUtils.getString(request.getParameter("zzchzqdate").replace("-", "").trim());
			JSONObject jObject3 = this.jeecgJdbcService.getZzcDatagridhz1(zsZzc, dataGrid, qdate);
			responseDatagrid(response, jObject3);
		}
	}

	/**
	 * easyuiAJAX请求数据： 不在岗民警录入或修改跳转
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 * @param user
	 */
	@RequestMapping(params = "zzcaddorupdate")
	public ModelAndView zzcaddorupdate(ZSZzc zsZzc, HttpServletRequest req) {

		List<ZSType> departList = systemService.findByProperty(ZSType.class, "ZSTypegroup.id", "1");
		Collections.sort(departList);
		req.setAttribute("departList", departList);
		
		List<ZSType> zwList = systemService.findByProperty(ZSType.class, "ZSTypegroup.id", "2");
		Collections.sort(zwList);
		req.setAttribute("zwList", zwList);
		
		/*************** Upd By ZM 20170918 input改为 checkbox Start *************/
		/** 不在岗种类列表  **/
		List<ZSType> bzgzlList = systemService.findByProperty(ZSType.class, "ZSTypegroup.id", "3");
		Collections.sort(bzgzlList);
		req.setAttribute("bzgzlList" , bzgzlList);
		/** 出行方式列表  **/
		List<ZSType> cxtypeList = systemService.findByProperty(ZSType.class, "ZSTypegroup.id", "4");
		Collections.sort(cxtypeList);
		req.setAttribute("cxtypeList" , cxtypeList);
		
		/***************  Upd By ZM 20170918 input改为 checkbox End  *************/

		if (StringUtil.isNotEmpty(zsZzc.getId())) {
			zsZzc = systemService.getEntity(ZSZzc.class, zsZzc.getId());
			req.setAttribute("zsZzc", zsZzc);
		}
		return new ModelAndView("system/user/zsZzc");
	}
	/**
	 * easyuiAJAX请求数据： 民警录入或修改跳转
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 * @param user
	 */
	@RequestMapping(params = "zzcmjaddorupdate")
	public ModelAndView zzcmjaddorupdate(ZSZzc zsZzc, HttpServletRequest req) {
		TSUser u = ResourceUtil.getSessionUserName();

		req.setAttribute("departList", u.getBrowser().trim());
		
		List<ZSType> zwList = systemService.findByProperty(ZSType.class, "ZSTypegroup.id", "2");
		Collections.sort(zwList);
		req.setAttribute("zwList", zwList);
		
		List<ZSType> bzgList = systemService.findByProperty(ZSType.class, "ZSTypegroup.id", "3");
		Collections.sort(bzgList);
		req.setAttribute("bzgList", bzgList);

		if (StringUtil.isNotEmpty(zsZzc.getId())) {
			zsZzc = systemService.getEntity(ZSZzc.class, zsZzc.getId());
			req.setAttribute("zsZzc", zsZzc);
		}
		return new ModelAndView("system/user/zsZzcmj");
	}

	/**
	 * easyuiAJAX请求数据： 实有警力维护
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 * @param user
	 */
	@RequestMapping(params = "zzcsyjlupdate")
	public ModelAndView zzcsyjlupdate(ZSType zsType, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(zsType.getId())) {
			zsType = systemService.getEntity(ZSType.class, zsType.getId());
			req.setAttribute("zsType", zsType);
		}
		return new ModelAndView("system/user/zssyjlZzc");
	}

	/**
	 * 民警录入
	 * 
	 * @param user
	 * @param req
	 * @return
	 */

	@RequestMapping(params = "zzcsaveUser")
	@ResponseBody
	public AjaxJson zzcsaveUser(HttpServletRequest req, ZSZzc zsZzc) {
		AjaxJson j = new AjaxJson();
		// 得到用户的角色
		if (StringUtil.isNotEmpty(zsZzc.getId())) {
			ZSZzc zsZzcs = systemService.getEntity(ZSZzc.class, zsZzc.getId());
			zsZzcs.setZzcdepart(zsZzc.getZzcdepart().trim());
			zsZzcs.setZw(zsZzc.getZw().trim());
			zsZzcs.setBzgzl(zsZzc.getBzgzl().trim());
			zsZzcs.setSpld(zsZzc.getSpld().trim());
			zsZzcs.setSpdate(oConvertUtils.getString(zsZzc.getSpdate().replace("-", "").trim()));
			zsZzcs.setKsdate(oConvertUtils.getString(zsZzc.getKsdate().replace("-", "").trim()));
			if (zsZzc.getLjdate() != null && !"".equals(zsZzc.getLjdate())) {
				zsZzcs.setLjdate(oConvertUtils.getString(zsZzc.getLjdate().replace("-", "").trim()));
			} else {
				zsZzcs.setLjdate("0");
			}
			if (zsZzc.getFjdate() != null && !"".equals(zsZzc.getFjdate())) {
				zsZzcs.setFjdate(oConvertUtils.getString(zsZzc.getFjdate().replace("-", "").trim()));
			} else {
				zsZzcs.setFjdate("0");
			}
			if (zsZzc.getJsdate() != null && !"".equals(zsZzc.getJsdate())) {
				zsZzcs.setJsdate(oConvertUtils.getString(zsZzc.getJsdate().replace("-", "").trim()));
			} else {
				zsZzcs.setJsdate("99999999");
			}

			if (zsZzc.getCxtype() != null && !"".equals(zsZzc.getCxtype())) {
				zsZzcs.setCxtype(zsZzc.getCxtype().trim());
			}
			if (zsZzc.getQwaddress() != null && !"".equals(zsZzc.getQwaddress())) {
				zsZzcs.setQwaddress(zsZzc.getQwaddress().trim());
			}
			if (zsZzc.getNote() != null && !"".equals(zsZzc.getNote())) {
				zsZzcs.setNote(zsZzc.getNote().trim());
			}	
			
			/***** Upd By ZM 20170924增加重复记录check start******/
			Boolean isDuplicate = this.jeecgJdbcService.checkDuplicate(zsZzcs);
			if(!isDuplicate){
				message = "民警: " + zsZzcs.getName() + "休假时间重复，更新失败！！";
				j.setMsg(message);
				return j;
			} else {
				
				systemService.updateEntitie(zsZzcs);
	
				message = "民警: " + zsZzcs.getName() + "不在岗情况更新成功";
				/*
				 * if (StringUtil.isNotEmpty(roleid)) { saveRoleUser(users, roleid);
				 * }
				 */
				systemService.addLog(message, Globals.Log_Type_LOGIN, Globals.Log_Leavel_INFO);
			}
			/***** Upd By ZM 20170924增加重复记录check end******/
		} else {
			zsZzc.setSpdate(oConvertUtils.getString(zsZzc.getSpdate().replace("-", "").trim()));
			zsZzc.setKsdate(oConvertUtils.getString(zsZzc.getKsdate().replace("-", "").trim()));
			if (zsZzc.getLjdate() != null && !"".equals(zsZzc.getLjdate())) {
				zsZzc.setLjdate(oConvertUtils.getString(zsZzc.getLjdate().replace("-", "").trim()));
			} else {
				zsZzc.setLjdate("0");
			}
			if (zsZzc.getFjdate() != null && !"".equals(zsZzc.getFjdate())) {
				zsZzc.setFjdate(oConvertUtils.getString(zsZzc.getFjdate().replace("-", "").trim()));
			} else {
				zsZzc.setFjdate("0");
			}
			if (zsZzc.getJsdate() != null && !"".equals(zsZzc.getJsdate())) {
				zsZzc.setJsdate(oConvertUtils.getString(zsZzc.getJsdate().replace("-", "").trim()));
			} else {
				zsZzc.setJsdate("99999999");
			}

			/***** Upd By ZM 20170924 增加重复记录check start******/
			Boolean isDuplicate = this.jeecgJdbcService.checkDuplicate(zsZzc);
			if(!isDuplicate){
				message = "民警: " + zsZzc.getName() + "休假时间重复，添加失败！！！";
				j.setMsg(message);
				return j;
			} else {
				systemService.save(zsZzc);
				message = "民警: " + zsZzc.getName() + "不在岗情况添加成功";
				systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
			}
			/***** Upd By ZM 20170924 增加重复记录check end******/

		}
		j.setMsg(message);
		return j;
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
	 * 民警录入
	 * 
	 * @param user
	 * @param req
	 * @return
	 */

	@RequestMapping(params = "zzcsyjlsaveUser")
	@ResponseBody
	public AjaxJson zzcsyjlsaveUser(HttpServletRequest req, ZSType zsType) {
		AjaxJson j = new AjaxJson();
		// 得到用户的角色
		/*
		 * String roleid = oConvertUtils.getString(req.getParameter("roleid"));
		 * String password =
		 * oConvertUtils.getString(req.getParameter("password"));
		 */
		if (StringUtil.isNotEmpty(zsType.getId())) {
			ZSType zsTypes = systemService.getEntity(ZSType.class, zsType.getId());
			zsTypes.setValue(zsType.getValue().trim());

			systemService.updateEntitie(zsTypes);

			message = "部门: " + zsTypes.getTypename() + "实有警力更新成功";
			/*
			 * if (StringUtil.isNotEmpty(roleid)) { saveRoleUser(users, roleid);
			 * }
			 */
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} else {
			systemService.save(zsType);
			message = "部门: " + zsType.getTypename() + "实有警力更新成功";
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);

		}
		j.setMsg(message);
		return j;
	}

	// ----------------------------------------------------------------
	// update-begin--Author:shiyanping Date:20130327 for：excel导入
	// update-end--Author:shiyanping Date:20130327 for：excel导入
	// ----------------------------------------------------------------
	@RequestMapping(params = "goImplXls")
	public ModelAndView goImplXls(HttpServletRequest request) {
		return new ModelAndView("jeecg/demo/test/zzcUpload");
	}
	/**
	 * 删除zzc记录
	 * 
	 * @param user
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "delzzc")
	@ResponseBody
	public AjaxJson delzzc(ZSZzc zsZzc, HttpServletRequest req) {
		AjaxJson j = new AjaxJson();

		String zzcid = "";
		if (req.getParameter("zzcid") != null && !"".equals(req.getParameter("zzcid"))) {
			zzcid = oConvertUtils.getString(req.getParameter("zzcid").trim());
			zsZzc = systemService.getEntity(ZSZzc.class, zzcid);
			ZSZzcDel zsZzcDle = new ZSZzcDel();
			BeanUtils.copyProperties(zsZzc,zsZzcDle);
			message = "记录"+zsZzc.getZzcdepart()+""+zsZzc.getName()+""+zsZzc.getBzgzl()+"删除成功";
			systemService.addLog(message, Globals.Log_Type_LOGIN, Globals.Log_Leavel_INFO);
			systemService.save(zsZzcDle);
			systemService.delete(zsZzc);
		}
				
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 用户列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "user")
	public String user(HttpServletRequest request) {
		List<ZSType> departList = systemService.findByProperty(ZSType.class, "ZSTypegroup.id", "1");
		Collections.sort(departList);
		request.setAttribute("departList" , departList);
		
		List<ZSType> zwList = systemService.findByProperty(ZSType.class, "ZSTypegroup.id", "2");
		Collections.sort(zwList);
		request.setAttribute("zwList" , zwList);
		
		return "system/user/userList";
	}
	/**
	 * easyuiAJAX请求数据： 用户选择角色列表
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 * @param user
	 */
	@RequestMapping(params = "addorupdate")
	public ModelAndView addorupdate(TSUser user, HttpServletRequest req) {
		List<ZSType> departList = systemService.findByProperty(ZSType.class, "ZSTypegroup.id", "1");
		Collections.sort(departList);
		req.setAttribute("departList", departList);
		
		List<ZSType> zwList = systemService.findByProperty(ZSType.class, "ZSTypegroup.id", "2");
		Collections.sort(zwList);
		req.setAttribute("zwList", zwList);

		if (StringUtil.isNotEmpty(user.getId())) {
			user = systemService.getEntity(TSUser.class, user.getId());
			req.setAttribute("user", user);
			idandname(req, user);
		}
		return new ModelAndView("system/user/user");
	}
	/**
	 * easyuiAJAX用户列表请求数据
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = "datagrid")
	public void datagrid(TSUser user, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(TSUser.class, dataGrid);
		// 查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, user);
		
		Short[] userstate = new Short[] { Globals.User_Normal, Globals.User_ADMIN };
		cq.in("status", userstate);
		cq.add();
		this.systemService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}
	/**
	 * 用户录入
	 * 
	 * @param user
	 * @param req
	 * @return
	 */

	@RequestMapping(params = "saveUser")
	@ResponseBody
	public AjaxJson saveUser(HttpServletRequest req, TSUser user) {
		AjaxJson j = new AjaxJson();
		// 得到用户的角色
		String roleid = oConvertUtils.getString(req.getParameter("roleid"));
		String password = oConvertUtils.getString(req.getParameter("password"));
		if (StringUtil.isNotEmpty(user.getId())) {
			TSUser users = systemService.getEntity(TSUser.class, user.getId());
			users.setEmail(user.getEmail());
			users.setOfficePhone(user.getOfficePhone());
			users.setMobilePhone(user.getMobilePhone());
			users.setTSDepart(user.getTSDepart());
			users.setRealName(user.getRealName());
			users.setStatus(Globals.User_Normal);
			users.setActivitiSync(user.getActivitiSync());
			systemService.updateEntitie(users);
			List<TSRoleUser> ru = systemService.findByProperty(TSRoleUser.class, "TSUser.id", user.getId());
			systemService.deleteAllEntitie(ru);
			message = "用户: " + users.getUserName() + "更新成功";
			if (StringUtil.isNotEmpty(roleid)) {
				saveRoleUser(users, roleid);
			}
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} else {
			TSUser users = systemService.findUniqueByProperty(TSUser.class, "userName", user.getUserName());
			if (users != null) {
				message = "用户: " + users.getUserName() + "已经存在";
			} else {
				//user.setPassword(PasswordUtil.encrypt(user.getUserName(), password, PasswordUtil.getStaticSalt()));
				user.setPassword(password);
				if (user.getTSDepart().equals("")) {
					user.setTSDepart(null);
				}
				user.setStatus(Globals.User_Normal);
				systemService.save(user);
				message = "用户: " + user.getUserName() + "添加成功";
				if (StringUtil.isNotEmpty(roleid)) {
					saveRoleUser(user, roleid);
				}
				systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
			}

		}
		j.setMsg(message);

		return j;
	}
}