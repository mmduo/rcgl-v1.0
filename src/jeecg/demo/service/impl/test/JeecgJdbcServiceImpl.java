package jeecg.demo.service.impl.test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import jeecg.demo.entity.test.JeecgJdbcEntity;
import jeecg.demo.service.test.JeecgJdbcServiceI;
import jeecg.system.pojo.base.ZSZzc;
import net.sf.json.JSONObject;

import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.core.util.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sun.star.reflection.InvocationTargetException;

@Service("jeecgJdbcService")
@Transactional
public class JeecgJdbcServiceImpl extends CommonServiceImpl implements JeecgJdbcServiceI {

	// wait to do: 参数(JeecgJdbcEntity pageObj, DataGrid
	// dataGrid)中用到的数据不多，可以考虑变一下。。。
	// 实际上只有query的条件，以及dataGrid里的分页的数据

	// 方式1, 用底层自带的方式往对象中设值 -------------------
	@Override
	public void getDatagrid1(JeecgJdbcEntity pageObj, DataGrid dataGrid) {
		// *
		String sqlWhere = getSqlWhere(pageObj);

		// 取出总数据条数（为了分页处理, 如果不用分页，取iCount值的这个处理可以不要）
		String sqlCnt = "select count(*) from jeecg_demo t";
		if (!sqlWhere.isEmpty()) {
			sqlCnt += " where" + sqlWhere;
		}
		Long iCount = getCountForJdbcParam(sqlCnt, null);

		String sql = "select t.id,t.user_name as userName,d.departname as depId,t.sex,t.age,t.birthday,t.email,t.mobile_phone as mobilePhone from jeecg_demo t left join t_s_depart d on d.id=t.dep_id";
		if (!sqlWhere.isEmpty()) {
			sql += " where" + sqlWhere;
		}
		// 结果往JeecgDemo这个类的各属性中设值（属性名如果和数据库中不一致，需要在SQL文中命别名，如userName）
		List list = findObjForJdbc(sql, dataGrid.getPage(), dataGrid.getRows(), JeecgJdbcEntity.class);
		// sex(性别的处理见方式2,可类似处理，重新循环一遍)

		// 返回Grid数据
		dataGrid.setReaults(list);
		dataGrid.setTotal(iCount.intValue());
	}
	// end of 方式1 ========================================= */

	// 方式2, 取值自己处理(代码量多一些，但执行效率应该会稍高一些) -------------------------------
	@Override
	public void getDatagrid2(JeecgJdbcEntity pageObj, DataGrid dataGrid) {
		String sqlWhere = getSqlWhere(pageObj);

		// 取出总数据条数（为了分页处理, 如果不用分页，取iCount值的这个处理可以不要）
		String sqlCnt = "select count(*) from jeecg_demo t";
		if (!sqlWhere.isEmpty()) {
			sqlCnt += " where" + sqlWhere;
		}
		Long iCount = getCountForJdbcParam(sqlCnt, null);

		String sql = "select t.*,d.departname from jeecg_demo t left join t_s_depart d on d.id=t.dep_id";
		if (!sqlWhere.isEmpty()) {
			sql += " where" + sqlWhere;
		}
		List<Map<String, Object>> mapList = findForJdbc(sql, dataGrid.getPage(), dataGrid.getRows());

		List list = new ArrayList();
		JeecgJdbcEntity obj = null;
		for (Map<String, Object> m : mapList) {
			try {
				obj = new JeecgJdbcEntity();
				obj.setId((String) m.get("id"));
				obj.setUserName((String) m.get("user_name"));
				obj.setDepId((String) m.get("departname"));
				String sex = (String) m.get("sex");
				if (sex == null) {
					obj.setSex("");
				} else if (sex.equals("0")) {
					obj.setSex("男");
				} else {
					obj.setSex("女");
				}
				obj.setAge((Integer) m.get("age"));
				Date birthday = (Date) m.get("birthday");
				if (birthday != null) {
					obj.setBirthday(birthday);
				}
				obj.setEmail((String) m.get("email"));
				obj.setMobilePhone((String) m.get("mobile_phone"));
				list.add(obj);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// 返回Grid数据
		dataGrid.setReaults(list);
		dataGrid.setTotal(iCount.intValue());
	}
	// end of 方式2 ========================================= */

	// 推荐方法
	// 方式3, 取值进一步自己处理(直接转换成easyUI的datagrid需要的东西，执行效率最高，最自由)
	// -------------------------------
	@Override
	public JSONObject getDatagrid3(JeecgJdbcEntity pageObj, DataGrid dataGrid) {
		String sqlWhere = getSqlWhere(pageObj);

		// 取出总数据条数（为了分页处理, 如果不用分页，取iCount值的这个处理可以不要）
		String sqlCnt = "select count(*) from jeecg_demo t";
		if (!sqlWhere.isEmpty()) {
			sqlCnt += " where" + sqlWhere;
		}
		Long iCount = getCountForJdbcParam(sqlCnt, null);

		// 取出当前页的数据
		String sql = "select t.*,d.departname from jeecg_demo t left join t_s_depart d on d.id=t.dep_id";
		if (!sqlWhere.isEmpty()) {
			sql += " where" + sqlWhere;
		}
		List<Map<String, Object>> mapList = findForJdbc(sql, dataGrid.getPage(), dataGrid.getRows());

		// 将结果集转换成页面上对应的数据集
		Db2Page[] db2Pages = { new Db2Page("id"), new Db2Page("userName", "user_name", null),
				new Db2Page("depId", "departName", null), new Db2Page("sex", null, new MyDataExchangerSex()),
				new Db2Page("age"), new Db2Page("birthday"), new Db2Page("email"),
				new Db2Page("mobilePhone", "mobile_phone", null) };
		JSONObject jObject = getJsonDatagridEasyUI(mapList, iCount.intValue(), db2Pages);
		return jObject;
		// end of 方式3 ========================================= */
	}

	// 拼查询条件（where语句）
	String getSqlWhere(JeecgJdbcEntity pageObj) {
		// 拼出条件语句
		String sqlWhere = "";
		if (StringUtil.isNotEmpty(pageObj.getUserName())) {
			if (!sqlWhere.isEmpty()) {
				sqlWhere += " and";
			}
			sqlWhere += " t.user_name like '%" + pageObj.getUserName() + "%'";
		}
		if (StringUtil.isNotEmpty(pageObj.getMobilePhone())) {
			if (!sqlWhere.isEmpty()) {
				sqlWhere += " and";
			}
			sqlWhere += " t.mobile_phone like '%" + pageObj.getMobilePhone() + "%'";
		}
		return sqlWhere;
	}

	// -----------------------------------------------------------------------------------
	// 以下各函数可以提成共用部件 (Add by Quainty)
	// -----------------------------------------------------------------------------------
	/**
	 * 返回easyUI的DataGrid数据格式的JSONObject对象
	 * 
	 * @param mapList
	 *            : 从数据库直接取得的结果集列表
	 * @param iTotalCnt
	 *            : 从数据库直接取得的结果集总数据条数
	 * @param dataExchanger
	 *            : 页面表示数据与数据库字段的对应关系列表
	 * @return JSONObject
	 */
	public JSONObject getJsonDatagridEasyUI(List<Map<String, Object>> mapList, int iTotalCnt, Db2Page[] dataExchanger) {
		// easyUI的dataGrid方式 －－－－这部分可以提取成统一处理
		String jsonTemp = "{\'total\':" + iTotalCnt + ",\'rows\':[";
		for (int j = 0; j < mapList.size(); j++) {
			Map<String, Object> m = mapList.get(j);
			if (j > 0) {
				jsonTemp += ",";
			}
			jsonTemp += "{";
			for (int i = 0; i < dataExchanger.length; i++) {
				if (i > 0) {
					jsonTemp += ",";
				}
				jsonTemp += "'" + dataExchanger[i].getKey() + "'" + ":";
				Object objValue = dataExchanger[i].getData(m);
				if (objValue == null) {
					jsonTemp += "null";
				} else {
					jsonTemp += "'" + objValue + "'";
				}
			}
			jsonTemp += "}";
		}
		jsonTemp += "]}";
		JSONObject jObject = JSONObject.fromObject(jsonTemp);
		return jObject;
	}

	// 数据变换的统一接口
	interface IMyDataExchanger {
		public Object exchange(Object value);
	}

	// 页面表示数据与数据库字段的对应关系
	class Db2Page {
		String fieldPage; // 页面的fieldID
		String columnDB; // 数据库的字段名
		IMyDataExchanger dataExchanger; // 数据变换

		// 构造函数1：当页面的fieldID与数据库字段一致时（数据也不用变换）
		public Db2Page(String fieldPage) {
			this.fieldPage = fieldPage;
			this.columnDB = fieldPage;
			this.dataExchanger = null;
		}

		// 构造函数2：当页面的fieldID与数据库字段不一致时（数据不用变换）
		public Db2Page(String fieldPage, String columnDB) {
			this.fieldPage = fieldPage;
			if (columnDB == null) {// 与fieldPage相同
				this.columnDB = fieldPage;
			} else {
				this.columnDB = columnDB;
			}
			this.dataExchanger = null;
		}

		// 构造函数3：当页面的fieldID与数据库字段不一致，且数据要进行变换（当然都用这个构造函数也行）
		public Db2Page(String fieldPage, String columnDB, IMyDataExchanger dataExchanger) {
			this.fieldPage = fieldPage;
			if (columnDB == null) {// 与fieldPage相同
				this.columnDB = fieldPage;
			} else {
				this.columnDB = columnDB;
			}
			this.dataExchanger = dataExchanger;
		}

		/**
		 * 取页面表示绑定的fieldID
		 */
		public String getKey() {
			return fieldPage;
		}

		/**
		 * 取页面表示对应的值
		 * 
		 * @param mapDB
		 *            : 从数据库直接取得的结果集(一条数据的MAP)
		 * @return Object : 页面表示对应的值
		 */
		public Object getData(Map mapDB) {
			Object objValue = mapDB.get(columnDB);
			if (objValue == null) {
				return null;
			} else {
				if (dataExchanger != null) {
					return dataExchanger.exchange(objValue);
				} else {
					return objValue;
				}
			}
		}
	}

	// 性别的数据变换实体
	class MyDataExchangerSex implements IMyDataExchanger {
		public Object exchange(Object value) {
			if (value == null) {
				return "";
			} else if (value.equals("0")) {
				return "男";
			} else {
				return "女";
			}
		}
	}

	// 推荐方法
	// 方式3, 取值进一步自己处理(直接转换成easyUI的datagrid需要的东西，执行效率最高，最自由)
	// -------------------------------
	@Override
	public JSONObject getZzcDatagrid(ZSZzc zsZzc, DataGrid dataGrid) {
		String sqlWhere = getZzcSqlWhere(zsZzc);

		// 取出总数据条数（为了分页处理, 如果不用分页，取iCount值的这个处理可以不要）
		String sqlCnt = "select count(*) from z_s_zzc t";
		if (!sqlWhere.isEmpty()) {
			sqlCnt += " where" + sqlWhere;
		}
		Long iCount = getCountForJdbcParam(sqlCnt, null);

		// 取出当前页的数据
		String sql = "select t.* from z_s_zzc t,z_s_type z ";
		if (!sqlWhere.isEmpty()) {
			sql += " where z.typename = t.depart and z.typegroupid='1' and" + sqlWhere;
			sql += " order by z.typecode";
		}
		List<Map<String, Object>> mapList = findForJdbc(sql, dataGrid.getPage(), dataGrid.getRows());

		// 将结果集转换成页面上对应的数据集
		Db2Page[] db2Pages = { new Db2Page("id"), new Db2Page("zzcdepart", "depart", null),
				new Db2Page("name", "name", null), new Db2Page("zw", "zw", null), new Db2Page("bzgzl"),
				new Db2Page("spld"), new Db2Page("spdate"), new Db2Page("ksdate"),
				new Db2Page("ljdate", "ljdate", null), new Db2Page("fjdate", "fjdate", null), new Db2Page("jsdate"),
				new Db2Page("cxtype"), new Db2Page("qwaddress"), new Db2Page("note", "note", null) };
		JSONObject jObject = getJsonDatagridEasyUI(mapList, iCount.intValue(), db2Pages);
		return jObject;
		// end of 方式3 ========================================= */
	}

	// 拼查询条件（where语句）
	String getZzcSqlWhere(ZSZzc zsZzc) {
		// 拼出条件语句
		String sqlWhere = "";
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");// 可以方便地修改日期格式
		String hehe = dateFormat.format(now);
		if (!sqlWhere.isEmpty()) {
			sqlWhere += " and";
		}
		sqlWhere += " t.jsdate >= '" + hehe + "'";
		/*
		 * if (StringUtil.isNotEmpty(zsZzc.getJsdate())) { if
		 * (!sqlWhere.isEmpty()) { sqlWhere += " and"; } sqlWhere +=
		 * " t.jsdate > '" + zsZzc.getJsdate() + "'"; }
		 */
		/*
		 * if (StringUtil.isNotEmpty(pageObj.getMobilePhone())) { if
		 * (!sqlWhere.isEmpty()) { sqlWhere += " and"; } sqlWhere +=
		 * " t.mobile_phone like '%" + pageObj.getMobilePhone() + "%'"; }
		 */
		return sqlWhere;
	}

	// 推荐方法
	// 方式3, 取值进一步自己处理(直接转换成easyUI的datagrid需要的东西，执行效率最高，最自由)
	// -------------------------------
	@Override
	public JSONObject getZzcDatagridylj(ZSZzc zsZzc, DataGrid dataGrid, String qdate) {
		String sqlWhere = getZzcSqlWhereylj(zsZzc, qdate);

		// 取出总数据条数（为了分页处理, 如果不用分页，取iCount值的这个处理可以不要）
		String sqlCnt = "select count(*) from z_s_zzc t";
		if (!sqlWhere.isEmpty()) {
			sqlCnt += " where" + sqlWhere;
		}
		Long iCount = getCountForJdbcParam(sqlCnt, null);

		// 取出当前页的数据
		String sql = "select DISTINCT CONCAT_WS('--',t.ksdate,t.jsdate) as qzdate,t.* from z_s_zzc t,z_s_type z ";
		if (!sqlWhere.isEmpty()) {
			sql += " where z.typename = t.depart and z.typegroupid='1' and" + sqlWhere;
			sql += " order by z.typecode";
		}
		List<Map<String, Object>> mapList = findForJdbc(sql, dataGrid.getPage(), 100);

		// 将结果集转换成页面上对应的数据集
		Db2Page[] db2Pages = { new Db2Page("id"), new Db2Page("rownum", "rownum", null),
				new Db2Page("zzcdepart", "depart", null), new Db2Page("name", "name", null),
				new Db2Page("zw", "zw", null), new Db2Page("bzgzl"), new Db2Page("spld"), new Db2Page("spdate"),
				new Db2Page("ksdate"), new Db2Page("qzdate"), new Db2Page("ljdate", "ljdate", null),
				new Db2Page("fjdate", "fjdate", null), new Db2Page("jsdate"), new Db2Page("cxtype"),
				new Db2Page("qwaddress"), new Db2Page("note", "note", null) };
		JSONObject jObject = getJsonYljWlj(mapList, qdate, db2Pages);
		return jObject;
		// end of 方式3 ========================================= */
	}

	// 推荐方法
	// 方式3, 取值进一步自己处理(直接转换成easyUI的datagrid需要的东西，执行效率最高，最自由)
	// -------------------------------
	@Override
	public List<Map<String, Object>> getZzcDataylj(ZSZzc zsZzc, String qdate) {
		String sqlWhere = getZzcSqlWhereylj(zsZzc, qdate);

		// 取出总数据条数（为了分页处理, 如果不用分页，取iCount值的这个处理可以不要）
		String sqlCnt = "select count(*) from z_s_zzc t";
		if (!sqlWhere.isEmpty()) {
			sqlCnt += " where" + sqlWhere;
		}
		Long iCount = getCountForJdbcParam(sqlCnt, null);

		// 取出当前页的数据
		String sql = "select DISTINCT CONCAT_WS('--',t.ksdate,t.jsdate) as qzdate,t.* from z_s_zzc t,z_s_type z ";
		if (!sqlWhere.isEmpty()) {
			sql += " where z.typename = t.depart and z.typegroupid='1' and" + sqlWhere;
			sql += " order by z.typecode";
		}
		List<Map<String, Object>> mapList = findForJdbc(sql);

		return mapList;
		// end of 方式3 ========================================= */
	}

	// 拼查询条件（where语句）
	String getZzcSqlWhereylj(ZSZzc zsZzc, String qdate) {
		// 拼出条件语句
		String sqlWhere = "";
		if (!sqlWhere.isEmpty()) {
			sqlWhere += " and";
		}
		sqlWhere += " t.fjdate >= '" + qdate + "'";
		sqlWhere += " and";
		sqlWhere += " t.ljdate <= '" + qdate + "'";
		/*
		 * if (StringUtil.isNotEmpty(zsZzc.getJsdate())) { if
		 * (!sqlWhere.isEmpty()) { sqlWhere += " and"; } sqlWhere +=
		 * " t.jsdate > '" + zsZzc.getJsdate() + "'"; }
		 */
		/*
		 * if (StringUtil.isNotEmpty(pageObj.getMobilePhone())) { if
		 * (!sqlWhere.isEmpty()) { sqlWhere += " and"; } sqlWhere +=
		 * " t.mobile_phone like '%" + pageObj.getMobilePhone() + "%'"; }
		 */
		return sqlWhere;
	}

	// 推荐方法
	// 方式3, 取值进一步自己处理(直接转换成easyUI的datagrid需要的东西，执行效率最高，最自由)
	// -------------------------------
	@Override
	public JSONObject getZzcDatagridwlj(ZSZzc zsZzc, DataGrid dataGrid, String qdate) {
		String sqlWhere = getZzcSqlWherewlj(zsZzc, qdate);

		// 取出总数据条数（为了分页处理, 如果不用分页，取iCount值的这个处理可以不要）
		String sqlCnt = "select count(*) from z_s_zzc t";
		if (!sqlWhere.isEmpty()) {
			sqlCnt += " where" + sqlWhere;
		}
		Long iCount = getCountForJdbcParam(sqlCnt, null);

		// 取出当前页的数据
		String sql = "select DISTINCT CONCAT_WS('--',t.ksdate,t.jsdate) as qzdate,t.* from z_s_zzc t "
				+ "inner join z_s_type z on z.typename = t.depart and z.typegroupid='1' ";
		if (!sqlWhere.isEmpty()) {
			sql += " where " + sqlWhere;
			sql += " order by z.typecode";
		}
		List<Map<String, Object>> mapList = findForJdbc(sql, dataGrid.getPage(), 100);

		// 将结果集转换成页面上对应的数据集
		Db2Page[] db2Pages = { new Db2Page("id"), new Db2Page("rownum", "rownum", null),
				new Db2Page("zzcdepart", "depart", null), new Db2Page("name", "name", null),
				new Db2Page("zw", "zw", null), new Db2Page("bzgzl"), new Db2Page("spld"), new Db2Page("spdate"),
				new Db2Page("ksdate"), new Db2Page("qzdate"), new Db2Page("ljdate", "ljdate", null),
				new Db2Page("fjdate", "fjdate", null), new Db2Page("jsdate"), new Db2Page("cxtype"),
				new Db2Page("qwaddress"), new Db2Page("note", "note", null) };
		JSONObject jObject = getJsonYljWlj(mapList, qdate, db2Pages);
		return jObject;
		// end of 方式3 ========================================= */
	}

	// 推荐方法
	// 方式3, 取值进一步自己处理(直接转换成easyUI的datagrid需要的东西，执行效率最高，最自由)
	// -------------------------------
	@Override
	public List<Map<String, Object>> getZzcDatawlj(ZSZzc zsZzc, String qdate) {
		String sqlWhere = getZzcSqlWherewlj(zsZzc, qdate);

		// 取出总数据条数（为了分页处理, 如果不用分页，取iCount值的这个处理可以不要）
		String sqlCnt = "select count(*) from z_s_zzc t";
		if (!sqlWhere.isEmpty()) {
			sqlCnt += " where" + sqlWhere;
		}
		Long iCount = getCountForJdbcParam(sqlCnt, null);

		// 取出当前页的数据
		String sql = "select DISTINCT CONCAT_WS('--',t.ksdate,t.jsdate) as qzdate,t.* from z_s_zzc t "
				+ "inner join z_s_type z on z.typename = t.depart and z.typegroupid='1' ";
		if (!sqlWhere.isEmpty()) {
			sql += " where " + sqlWhere;
			sql += " order by z.typecode";
		}
		List<Map<String, Object>> mapList = findForJdbc(sql);

		return mapList;
		// end of 方式3 ========================================= */
	}

	// 拼查询条件（where语句）
	String getZzcSqlWherewlj(ZSZzc zsZzc, String qdate) {
		// 拼出条件语句
		String sqlWhere = "";
		if (!sqlWhere.isEmpty()) {
			sqlWhere += " and";
		}
		sqlWhere += "(t.ljdate>'0' and t.ksdate <='" + qdate + "' and t.ljdate >'" + qdate + "') or"
				+ " (t.fjdate>'0' and t.fjdate <'" + qdate + "' and t.jsdate>='" + qdate + "') or"
				+ " (t.fjdate='0' and t.ljdate='0' and t.ksdate <='" + qdate + "' and t.jsdate>='" + qdate + "')";
		/*
		 * sqlWhere += " t.ksdate <= '" + qdate + "'"; sqlWhere += " and";
		 * sqlWhere += " t.ljdate > '" + qdate + "'";
		 */
		/*
		 * if (StringUtil.isNotEmpty(zsZzc.getJsdate())) { if
		 * (!sqlWhere.isEmpty()) { sqlWhere += " and"; } sqlWhere +=
		 * " t.jsdate > '" + zsZzc.getJsdate() + "'"; }
		 */
		/*
		 * if (StringUtil.isNotEmpty(pageObj.getMobilePhone())) { if
		 * (!sqlWhere.isEmpty()) { sqlWhere += " and"; } sqlWhere +=
		 * " t.mobile_phone like '%" + pageObj.getMobilePhone() + "%'"; }
		 */
		return sqlWhere;
	}

	// 推荐方法
	// 方式3, 取值进一步自己处理(直接转换成easyUI的datagrid需要的东西，执行效率最高，最自由)
	// -------------------------------
	@Override
	public JSONObject getZzcDatagridhz(ZSZzc zsZzc, DataGrid dataGrid, String qdate) {
		String sqlWhere = getZzcSqlWherehz(zsZzc, qdate);

		// 取出总数据条数（为了分页处理, 如果不用分页，取iCount值的这个处理可以不要）
		String sqlCnt = "select count(*) from z_s_type t";
		if (!sqlWhere.isEmpty()) {
			sqlCnt += " where" + sqlWhere;
		}
		Long iCount = getCountForJdbcParam(sqlCnt, null);

		// 取出当前页的数据
		String sql = "SELECT t.typename,s.jl,(case when w.wlj is null then 0 else w.wlj END) as wlj,(case when y.ylj is null then 0 else y.ylj END) as ylj,"
				+ "(case when n.nlj is null then 0 else n.nlj END) as nlj,(case when y.ylj is null then w.wlj when w.wlj is null then y.ylj else w.wlj+y.ylj END) as bzghj from "
				+ "(select * from z_s_type where typegroupid=1) t LEFT JOIN "
				+ "(select (CASE WHEN z.sy is NULL then t1.`value` else cast(t1.`value`-sy as char) END) as jl,t1.typename from z_s_type t1 "
				+ "LEFT JOIN (select count(z.id) as sy,z.depart from z_s_zzc z where z.ksdate<='" + qdate
				+ "' and z.jsdate>='" + qdate + "' group by z.depart) z on t1.typename=z.depart) s "
				+ "on t.typename=s.typename LEFT JOIN "
				+ "(select count(*) as wlj,z1.depart as depart1 from z_s_zzc z1 "
				+ "where (z1.ljdate>'0' and z1.ksdate<='" + qdate + "' and z1.ljdate>'" + qdate
				+ "') OR (z1.fjdate>'0' and z1.fjdate<'" + qdate + "' and z1.jsdate>='" + qdate
				+ "') or (z1.fjdate='0' and z1.ljdate='0' and z1.ksdate <='" + qdate + "' and z1.jsdate>='" + qdate
				+ "') group by z1.depart) w " + "on t.typename=w.depart1 LEFT JOIN "
				+ "(select count(*) as ylj,z2.depart as depart2 from z_s_zzc z2 where z2.ljdate<='" + qdate
				+ "' and z2.fjdate>='" + qdate + "' group by z2.depart) y " + "on t.typename=y.depart2 LEFT JOIN "
				+ "(select count(*) as nlj,z3.depart as depart3 from z_s_zzc z3 where z3.spdate<='" + qdate
				+ "' and z3.ljdate>'" + qdate + "' group by z3.depart) n "
				+ "on t.typename=n.depart3 group by t.typename,t.`value` order by t.typecode ";
		if (!sqlWhere.isEmpty()) {
			sql += " where" + sqlWhere;
		}
		// 取出当前页的数据
		String sql2 = "select count(*) as wlj,'0' as ylj,'0' as nlj from z_s_zzc z1 where (z1.ljdate>'0' and z1.ksdate<='"
				+ qdate + "' and z1.ljdate>'" + qdate + "') OR (z1.fjdate>'0' and z1.fjdate<'" + qdate
				+ "' and z1.jsdate>='" + qdate + "') or (z1.fjdate='0' and z1.ljdate='0' and z1.ksdate <='" + qdate
				+ "' and z1.jsdate>='" + qdate + "')  "
				+ "UNION select '0',count(*) as ylj,'0' from z_s_zzc z2 where z2.ljdate<='" + qdate
				+ "' and z2.fjdate>='" + qdate + "' "
				+ "UNION select '0','0',count(*) as nlj from z_s_zzc z3 where z3.spdate<='" + qdate
				+ "' and z3.ljdate>'" + qdate + "' ";

		List<Map<String, Object>> mapList1 = findForJdbc(sql, dataGrid.getPage(), 50);
		// List<Map<String, Object>> mapList2 = findForJdbc(sql2, 1, 50);

		// 将结果集转换成页面上对应的数据集
		Db2Page[] db2Pages = { new Db2Page("typename"), new Db2Page("jl"), new Db2Page("wlj", "wlj", null),
				new Db2Page("ylj", "ylj", null), new Db2Page("nlj", "nlj", null), new Db2Page("bzghj", "bzghj", null) };
		JSONObject jObject = getJsonDatagridEasyUI4(mapList1, qdate, db2Pages);
		return jObject;
		// end of 方式3 ========================================= */
	}

	// 推荐方法
	// 方式3, 取值进一步自己处理(直接转换成easyUI的datagrid需要的东西，执行效率最高，最自由)
	// -------------------------------
	@Override
	public List<Map<String, Object>> getZzcDatahz(ZSZzc zsZzc, String qdate) {
		String sqlWhere = getZzcSqlWherehz(zsZzc, qdate);

		// 取出总数据条数（为了分页处理, 如果不用分页，取iCount值的这个处理可以不要）
		String sqlCnt = "select count(*) from z_s_type t";
		if (!sqlWhere.isEmpty()) {
			sqlCnt += " where" + sqlWhere;
		}
		Long iCount = getCountForJdbcParam(sqlCnt, null);

		// 取出当前页的数据
		String sql = "SELECT CONCAT(t.typename,'') as typename,CONCAT(s.jl,'') as jl,CONCAT((case when w.wlj is null then 0 else w.wlj END),'') as wlj,CONCAT((case when y.ylj is null then 0 else y.ylj END),'') as ylj,CONCAT((case when n.nlj is null then 0 else n.nlj END),'') as nlj,CONCAT((case when y.ylj is null then w.wlj when w.wlj is null then y.ylj else w.wlj+y.ylj END),'') as bzghj from "
				+ "(select * from z_s_type where typegroupid=1) t LEFT JOIN "
				+ "(select (CASE WHEN z.sy is NULL then t1.`value` else cast(t1.`value`-sy as char) END) as jl,t1.typename from z_s_type t1 "
				+ "LEFT JOIN (select count(z.id) as sy,z.depart from z_s_zzc z where z.ksdate<='" + qdate
				+ "' and z.jsdate>='" + qdate + "' group by z.depart) z on t1.typename=z.depart) s "
				+ "on t.typename=s.typename LEFT JOIN "
				+ "(select count(*) as wlj,z1.depart as depart1 from z_s_zzc z1 "
				+ "where (z1.ljdate>'0' and z1.ksdate<='" + qdate + "' and z1.ljdate>'" + qdate
				+ "') OR (z1.fjdate>'0' and z1.fjdate<'" + qdate + "' and z1.jsdate>='" + qdate
				+ "') or (z1.fjdate='0' and z1.ljdate='0' and z1.ksdate <='" + qdate + "' and z1.jsdate>='" + qdate
				+ "') group by z1.depart) w " + "on t.typename=w.depart1 LEFT JOIN "
				+ "(select count(*) as ylj,z2.depart as depart2 from z_s_zzc z2 where z2.ljdate<='" + qdate
				+ "' and z2.fjdate>='" + qdate + "' group by z2.depart) y " + "on t.typename=y.depart2 LEFT JOIN "
				+ "(select count(*) as nlj,z3.depart as depart3 from z_s_zzc z3 where z3.spdate<='" + qdate
				+ "' and z3.ljdate>'" + qdate + "' group by z3.depart) n "
				+ "on t.typename=n.depart3 group by t.typename,t.`value` order by t.typecode ";
		if (!sqlWhere.isEmpty()) {
			sql += " where" + sqlWhere;
		}
		List<Map<String, Object>> mapList1 = findForJdbc(sql);

		return mapList1;
		// end of 方式3 ========================================= */
	}

	// 拼查询条件（where语句）
	String getZzcSqlWherehz(ZSZzc zsZzc, String qdate) {
		// 拼出条件语句
		String sqlWhere = "";
		/*
		 * Date now = new Date(); SimpleDateFormat dateFormat = new
		 * SimpleDateFormat("yyyyMMdd");// 可以方便地修改日期格式 String hehe =
		 * dateFormat.format(now); if (!sqlWhere.isEmpty()) { sqlWhere += " and"
		 * ; } sqlWhere += " t.ksdate <= '" + hehe + "'"; sqlWhere += " and";
		 * sqlWhere += " t.ljdate >= '" + hehe + "'";
		 */
		/*
		 * if (StringUtil.isNotEmpty(zsZzc.getJsdate())) { if
		 * (!sqlWhere.isEmpty()) { sqlWhere += " and"; } sqlWhere +=
		 * " t.jsdate > '" + zsZzc.getJsdate() + "'"; }
		 */
		/*
		 * if (StringUtil.isNotEmpty(pageObj.getMobilePhone())) { if
		 * (!sqlWhere.isEmpty()) { sqlWhere += " and"; } sqlWhere +=
		 * " t.mobile_phone like '%" + pageObj.getMobilePhone() + "%'"; }
		 */
		return sqlWhere;
	}

	/**
	 * 返回汇总表JSONObject对象
	 * 
	 * @param mapList
	 *            : 从数据库直接取得的结果集列表
	 * @param iTotalCnt
	 *            : 从数据库直接取得的结果集总数据条数
	 * @param dataExchanger
	 *            : 页面表示数据与数据库字段的对应关系列表
	 * @return JSONObject
	 */
	public JSONObject getJsonDatagridEasyUI2(List<Map<String, Object>> mapList, String qdate, Db2Page[] dataExchanger) {
		// easyUI的dataGrid方式 －－－－这部分可以提取成统一处理
		String jsonTemp = "{\'qdate\':'" + qdate + "',\'rows\':[";
		for (int j = 0; j < mapList.size(); j++) {
			Map<String, Object> m = mapList.get(j);
			if (j > 0) {
				jsonTemp += ",";
			}
			jsonTemp += "{";
			for (int i = 0; i < dataExchanger.length; i++) {
				if (i > 0) {
					jsonTemp += ",";
				}
				jsonTemp += "'" + dataExchanger[i].getKey() + "'" + ":";
				Object objValue = dataExchanger[i].getData(m);
				if (objValue == null) {
					jsonTemp += "0";
				} else {
					jsonTemp += "'" + objValue + "'";
				}
			}
			jsonTemp += "}";
		}
		jsonTemp += "]}";
		JSONObject jObject = JSONObject.fromObject(jsonTemp);
		return jObject;
	}

	/**
	 * 返回汇总表JSONObject对象
	 * 
	 * @param mapList
	 *            : 从数据库直接取得的结果集列表
	 * @param iTotalCnt
	 *            : 从数据库直接取得的结果集总数据条数
	 * @param dataExchanger
	 *            : 页面表示数据与数据库字段的对应关系列表
	 * @return JSONObject
	 */
	public JSONObject getJsonDatagridEasyUI4(List<Map<String, Object>> mapList1, String qdate,
			Db2Page[] dataExchanger) {
		// easyUI的dataGrid方式 －－－－这部分可以提取成统一处理
		String jsonTemp = "{\'qdate\':'" + qdate + "',\'rows\':[";
		for (int j = 0; j < mapList1.size(); j++) {
			Map<String, Object> m = mapList1.get(j);
			if (j > 0) {
				jsonTemp += ",";
			}
			jsonTemp += "{";
			for (int i = 0; i < dataExchanger.length; i++) {
				if (i > 0) {
					jsonTemp += ",";
				}
				jsonTemp += "'" + dataExchanger[i].getKey() + "'" + ":";
				Object objValue = dataExchanger[i].getData(m);
				if (objValue == null) {
					jsonTemp += "0";
				} else {
					jsonTemp += "'" + objValue + "'";
				}
			}
			jsonTemp += "}";
		}
		/*
		 * jsonTemp += "],\'hj\':[}"; for (int j = 0; j < mapList2.size(); j++)
		 * { Map<String, Object> m = mapList2.get(j); if (j > 0) { jsonTemp +=
		 * ","; } jsonTemp += "{"; for (int i = 0; i < dataExchanger.length;
		 * i++) { if (i > 0) { jsonTemp += ","; } jsonTemp += "'" +
		 * dataExchanger[i].getKey() + "'" + ":"; Object objValue =
		 * dataExchanger[i].getData(m); if (objValue == null) { jsonTemp += "0";
		 * } else { jsonTemp += "'" + objValue + "'"; } } jsonTemp += "}"; }
		 */
		jsonTemp += "]}";
		JSONObject jObject = JSONObject.fromObject(jsonTemp);
		return jObject;
	}

	/**
	 * 返回已离京未离京JSONObject对象
	 * 
	 * @param mapList
	 *            : 从数据库直接取得的结果集列表
	 * @param iTotalCnt
	 *            : 从数据库直接取得的结果集总数据条数
	 * @param dataExchanger
	 *            : 页面表示数据与数据库字段的对应关系列表
	 * @return JSONObject
	 */
	public JSONObject getJsonYljWlj(List<Map<String, Object>> mapList, String qdate, Db2Page[] dataExchanger) {
		// easyUI的dataGrid方式 －－－－这部分可以提取成统一处理
		String jsonTemp = "{\'qdate\':'" + qdate + "',\'rows\':[";
		for (int j = 0; j < mapList.size(); j++) {
			Map<String, Object> m = mapList.get(j);
			if (j > 0) {
				jsonTemp += ",";
			}
			jsonTemp += "{";
			for (int i = 0; i < dataExchanger.length; i++) {
				if (i > 0) {
					jsonTemp += ",";
				}
				jsonTemp += "'" + dataExchanger[i].getKey() + "'" + ":";
				Object objValue = dataExchanger[i].getData(m);
				if (objValue == null) {
					jsonTemp += "0";
				} else {
					jsonTemp += "'" + objValue + "'";
				}
			}
			jsonTemp += "}";
		}
		jsonTemp += "]}";
		JSONObject jObject = JSONObject.fromObject(jsonTemp);
		return jObject;
	}

	// 推荐方法
	// 方式3, 取值进一步自己处理(直接转换成easyUI的datagrid需要的东西，执行效率最高，最自由)
	// -------------------------------
	@Override
	public JSONObject getZzcDatagridylj1(ZSZzc zsZzc, DataGrid dataGrid, String qdate) {
		String sqlWhere = "";
		if (!"".equals(qdate)) {
			sqlWhere = getZzcSqlWhereylj(zsZzc, qdate);
		}

		// 取出总数据条数（为了分页处理, 如果不用分页，取iCount值的这个处理可以不要）
		String sqlCnt = "select count(*) from z_s_zzc t";
		if (!sqlWhere.isEmpty()) {
			sqlCnt += " where" + sqlWhere;
		}
		Long iCount = getCountForJdbcParam(sqlCnt, null);

		// 取出当前页的数据
		String sql = "select DISTINCT CONCAT_WS('--',t.ksdate,t.jsdate) as qzdate,t.* from z_s_zzc t,z_s_type z ";
		if (!sqlWhere.isEmpty()) {
			sql += " where z.typename = t.depart and z.typegroupid='1' and" + sqlWhere;
			sql += " order by z.typecode";
		}
		List<Map<String, Object>> mapList = findForJdbc(sql, dataGrid.getPage(), dataGrid.getRows());

		// 将结果集转换成页面上对应的数据集
		Db2Page[] db2Pages = { new Db2Page("id"), new Db2Page("rownum", "rownum", null),
				new Db2Page("zzcdepart", "depart", null), new Db2Page("name", "name", null),
				new Db2Page("zw", "zw", null), new Db2Page("bzgzl"), new Db2Page("spld"), new Db2Page("spdate"),
				new Db2Page("ksdate"), new Db2Page("qzdate"), new Db2Page("ljdate", "ljdate", null),
				new Db2Page("fjdate", "fjdate", null), new Db2Page("jsdate"), new Db2Page("cxtype"),
				new Db2Page("qwaddress"), new Db2Page("note", "note", null) };
		JSONObject jObject = getJsonDatagridEasyUI(mapList, iCount.intValue(), db2Pages);
		return jObject;
		// end of 方式3 ========================================= */
	}

	// 推荐方法
	// 方式3, 取值进一步自己处理(直接转换成easyUI的datagrid需要的东西，执行效率最高，最自由)
	// -------------------------------
	@Override
	public JSONObject getZzcDatagridwlj1(ZSZzc zsZzc, DataGrid dataGrid, String qdate) {
		String sqlWhere = "";
		if (!"".equals(qdate)) {
			sqlWhere = getZzcSqlWherewlj(zsZzc, qdate);
		}

		// 取出总数据条数（为了分页处理, 如果不用分页，取iCount值的这个处理可以不要）
		String sqlCnt = "select count(*) from z_s_zzc t";
		if (!sqlWhere.isEmpty()) {
			sqlCnt += " where" + sqlWhere;
		}
		Long iCount = getCountForJdbcParam(sqlCnt, null);

		// 取出当前页的数据
		String sql = "select DISTINCT CONCAT_WS('--',t.ksdate,t.jsdate) as qzdate,t.* from z_s_zzc t "
				+ "inner join z_s_type z on z.typename = t.depart and z.typegroupid='1' ";
		if (!sqlWhere.isEmpty()) {
			sql += " where " + sqlWhere;
			sql += " order by z.typecode";
		}
		List<Map<String, Object>> mapList = findForJdbc(sql, dataGrid.getPage(), dataGrid.getRows());

		// 将结果集转换成页面上对应的数据集
		Db2Page[] db2Pages = { new Db2Page("id"), new Db2Page("rownum", "rownum", null),
				new Db2Page("zzcdepart", "depart", null), new Db2Page("name", "name", null),
				new Db2Page("zw", "zw", null), new Db2Page("bzgzl"), new Db2Page("spld"), new Db2Page("spdate"),
				new Db2Page("ksdate"), new Db2Page("qzdate"), new Db2Page("ljdate", "ljdate", null),
				new Db2Page("fjdate", "fjdate", null), new Db2Page("jsdate"), new Db2Page("cxtype"),
				new Db2Page("qwaddress"), new Db2Page("note", "note", null) };
		JSONObject jObject = getJsonDatagridEasyUI(mapList, iCount.intValue(), db2Pages);
		return jObject;
		// end of 方式3 ========================================= */
	}

	// 推荐方法
	// 方式3, 取值进一步自己处理(直接转换成easyUI的datagrid需要的东西，执行效率最高，最自由)
	// -------------------------------
	@Override
	public JSONObject getZzcDatagridhz1(ZSZzc zsZzc, DataGrid dataGrid, String qdate) {
		String sqlWhere = "";
		if (!"".equals(qdate)) {
			sqlWhere = getZzcSqlWherehz(zsZzc, qdate);
		}

		// 取出总数据条数（为了分页处理, 如果不用分页，取iCount值的这个处理可以不要）
		String sqlCnt = "select count(*) from z_s_type t";
		if (!sqlWhere.isEmpty()) {
			sqlCnt += " where" + sqlWhere;
		}
		Long iCount = getCountForJdbcParam(sqlCnt, null);

		// 取出当前页的数据
		String sql = "SELECT t.typename,s.jl,(case when w.wlj is null then 0 else w.wlj END) as wlj,(case when y.ylj is null then 0 else y.ylj END) as ylj,"
				+ "(case when n.nlj is null then 0 else n.nlj END) as nlj,(case when y.ylj is null then w.wlj when w.wlj is null then y.ylj else w.wlj+y.ylj END) as bzghj from "
				+ "(select * from z_s_type where typegroupid=1) t LEFT JOIN "
				+ "(select (CASE WHEN z.sy is NULL then t1.`value` else cast(t1.`value`-sy as char) END) as jl,t1.typename from z_s_type t1 "
				+ "LEFT JOIN (select count(z.id) as sy,z.depart from z_s_zzc z where z.ksdate<='" + qdate
				+ "' and z.jsdate>='" + qdate + "' group by z.depart) z on t1.typename=z.depart) s "
				+ "on t.typename=s.typename LEFT JOIN "
				+ "(select count(*) as wlj,z1.depart as depart1 from z_s_zzc z1 "
				+ "where (z1.ljdate>'0' and z1.ksdate<='" + qdate + "' and z1.ljdate>'" + qdate
				+ "') OR (z1.fjdate>'0' and z1.fjdate<'" + qdate + "' and z1.jsdate>='" + qdate
				+ "') or (z1.fjdate='0' and z1.ljdate='0' and z1.ksdate <='" + qdate + "' and z1.jsdate>='" + qdate
				+ "') group by z1.depart) w " + "on t.typename=w.depart1 LEFT JOIN "
				+ "(select count(*) as ylj,z2.depart as depart2 from z_s_zzc z2 where z2.ljdate<='" + qdate
				+ "' and z2.fjdate>='" + qdate + "' group by z2.depart) y " + "on t.typename=y.depart2 LEFT JOIN "
				+ "(select count(*) as nlj,z3.depart as depart3 from z_s_zzc z3 where z3.spdate<='" + qdate
				+ "' and z3.ljdate>'" + qdate + "' group by z3.depart) n "
				+ "on t.typename=n.depart3 group by t.typename,t.`value` order by t.typecode ";
		if (!sqlWhere.isEmpty()) {
			sql += " where" + sqlWhere;
		}
		// 取出当前页的数据
		/*
		 * String sql2 =
		 * "select count(*) as wlj,'0' as ylj,'0' as nlj from z_s_zzc z1 where (z1.ljdate>'0' and z1.ksdate<='"
		 * + qdate + "' and z1.ljdate>'" + qdate +
		 * "') OR (z1.fjdate>'0' and z1.fjdate<'" + qdate + "' and z1.jsdate>='"
		 * + qdate + "') or (z1.fjdate='0' and z1.ljdate='0' and z1.ksdate <='"
		 * + qdate + "' and z1.jsdate>='" + qdate + "')  " +
		 * "UNION select '0',count(*) as ylj,'0' from z_s_zzc z2 where z2.ljdate<='"
		 * + qdate + "' and z2.fjdate>='" + qdate + "' " +
		 * "UNION select '0','0',count(*) as nlj from z_s_zzc z3 where z3.spdate<='"
		 * + qdate + "' and z3.ljdate>'" + qdate + "' ";
		 */

		List<Map<String, Object>> mapList = findForJdbc(sql, dataGrid.getPage(), dataGrid.getRows());
		// List<Map<String, Object>> mapList2 = findForJdbc(sql2,
		// dataGrid.getPage(), dataGrid.getRows());

		// 将结果集转换成页面上对应的数据集
		Db2Page[] db2Pages = { new Db2Page("typename"), new Db2Page("jl"), new Db2Page("wlj", "wlj", null),
				new Db2Page("ylj", "ylj", null), new Db2Page("nlj", "nlj", null), new Db2Page("bzghj", "bzghj", null) };
		JSONObject jObject = getJsonDatagridEasyUI3(mapList, iCount.intValue(), db2Pages);
		return jObject;
		// end of 方式3 ========================================= */
	}

	/**
	 * 返回easyUI的DataGrid数据格式的JSONObject对象
	 * 
	 * @param mapList
	 *            : 从数据库直接取得的结果集列表
	 * @param iTotalCnt
	 *            : 从数据库直接取得的结果集总数据条数
	 * @param dataExchanger
	 *            : 页面表示数据与数据库字段的对应关系列表
	 * @return JSONObject
	 */
	public JSONObject getJsonDatagridEasyUI3(List<Map<String, Object>> mapList, int iTotalCnt,
			Db2Page[] dataExchanger) {
		// easyUI的dataGrid方式 －－－－这部分可以提取成统一处理
		String jsonTemp = "{\'total\':" + iTotalCnt + ",\'rows\':[";
		for (int j = 0; j < mapList.size(); j++) {
			Map<String, Object> m = mapList.get(j);
			if (j > 0) {
				jsonTemp += ",";
			}
			jsonTemp += "{";
			for (int i = 0; i < dataExchanger.length; i++) {
				if (i > 0) {
					jsonTemp += ",";
				}
				jsonTemp += "'" + dataExchanger[i].getKey() + "'" + ":";
				Object objValue = dataExchanger[i].getData(m);
				if (objValue == null) {
					jsonTemp += "0";
				} else {
					jsonTemp += "'" + objValue + "'";
				}
			}
			jsonTemp += "}";
		}
		jsonTemp += "]}";
		JSONObject jObject = JSONObject.fromObject(jsonTemp);
		return jObject;
	}

	// 推荐方法
	// 方式3, 取值进一步自己处理(直接转换成easyUI的datagrid需要的东西，执行效率最高，最自由)
	// -------------------------------
	@Override
	public List<Map<String, Object>> getZzcDatanlj(ZSZzc zsZzc, String qdate) {
		String sqlWhere = getZzcSqlWherenlj(zsZzc, qdate);

		// 取出总数据条数（为了分页处理, 如果不用分页，取iCount值的这个处理可以不要）
		String sqlCnt = "select count(*) from z_s_zzc t";
		if (!sqlWhere.isEmpty()) {
			sqlCnt += " where" + sqlWhere;
		}
		Long iCount = getCountForJdbcParam(sqlCnt, null);

		// 取出当前页的数据
		String sql = "select DISTINCT CONCAT_WS('--',t.ksdate,t.jsdate) as qzdate,t.* from z_s_zzc t "
				+ "inner join z_s_type z on z.typename = t.depart and z.typegroupid='1' ";
		if (!sqlWhere.isEmpty()) {
			sql += " where " + sqlWhere;
			sql += " order by z.typecode";
		}
		List<Map<String, Object>> mapList = findForJdbc(sql);

		return mapList;
		// end of 方式3 ========================================= */
	}

	// 推荐方法
	// 方式3, 取值进一步自己处理(直接转换成easyUI的datagrid需要的东西，执行效率最高，最自由)
	// -------------------------------
	@Override
	public JSONObject getZzcDatagridnlj(ZSZzc zsZzc, DataGrid dataGrid, String qdate) {
		String sqlWhere = getZzcSqlWherenlj(zsZzc, qdate);

		// 取出总数据条数（为了分页处理, 如果不用分页，取iCount值的这个处理可以不要）
		String sqlCnt = "select count(*) from z_s_zzc t";
		if (!sqlWhere.isEmpty()) {
			sqlCnt += " where" + sqlWhere;
		}
		Long iCount = getCountForJdbcParam(sqlCnt, null);

		// 取出当前页的数据
		String sql = "select DISTINCT CONCAT_WS('--',t.ksdate,t.jsdate) as qzdate,t.* from z_s_zzc t "
				+ "inner join z_s_type z on z.typename = t.depart and z.typegroupid='1' ";
		if (!sqlWhere.isEmpty()) {
			sql += " where " + sqlWhere;
			sql += " order by z.typecode";
		}
		List<Map<String, Object>> mapList = findForJdbc(sql, dataGrid.getPage(), 100);

		// 将结果集转换成页面上对应的数据集
		Db2Page[] db2Pages = { new Db2Page("id"), new Db2Page("rownum", "rownum", null),
				new Db2Page("zzcdepart", "depart", null), new Db2Page("name", "name", null),
				new Db2Page("zw", "zw", null), new Db2Page("bzgzl"), new Db2Page("spld"), new Db2Page("spdate"),
				new Db2Page("ksdate"), new Db2Page("qzdate"), new Db2Page("ljdate", "ljdate", null),
				new Db2Page("fjdate", "fjdate", null), new Db2Page("jsdate"), new Db2Page("cxtype"),
				new Db2Page("qwaddress"), new Db2Page("note", "note", null) };
		JSONObject jObject = getJsonYljWlj(mapList, qdate, db2Pages);
		return jObject;
		// end of 方式3 ========================================= */
	}

	// 推荐方法
	// 方式3, 取值进一步自己处理(直接转换成easyUI的datagrid需要的东西，执行效率最高，最自由)
	// -------------------------------
	@Override
	public JSONObject getZzcDatagridnlj1(ZSZzc zsZzc, DataGrid dataGrid, String qdate) {
		String sqlWhere = "";
		if (!"".equals(qdate)) {
			sqlWhere = getZzcSqlWherenlj(zsZzc, qdate);
		}

		// 取出总数据条数（为了分页处理, 如果不用分页，取iCount值的这个处理可以不要）
		String sqlCnt = "select count(*) from z_s_zzc t";
		if (!sqlWhere.isEmpty()) {
			sqlCnt += " where" + sqlWhere;
		}
		Long iCount = getCountForJdbcParam(sqlCnt, null);

		// 取出当前页的数据
		String sql = "select DISTINCT CONCAT_WS('--',t.ksdate,t.jsdate) as qzdate,t.* from z_s_zzc t "
				+ "inner join z_s_type z on z.typename = t.depart and z.typegroupid='1' ";
		if (!sqlWhere.isEmpty()) {
			sql += " where " + sqlWhere;
			sql += " order by z.typecode";
		}
		List<Map<String, Object>> mapList = findForJdbc(sql, dataGrid.getPage(), dataGrid.getRows());

		// 将结果集转换成页面上对应的数据集
		Db2Page[] db2Pages = { new Db2Page("id"), new Db2Page("rownum", "rownum", null),
				new Db2Page("zzcdepart", "depart", null), new Db2Page("name", "name", null),
				new Db2Page("zw", "zw", null), new Db2Page("bzgzl"), new Db2Page("spld"), new Db2Page("spdate"),
				new Db2Page("ksdate"), new Db2Page("qzdate"), new Db2Page("ljdate", "ljdate", null),
				new Db2Page("fjdate", "fjdate", null), new Db2Page("jsdate"), new Db2Page("cxtype"),
				new Db2Page("qwaddress"), new Db2Page("note", "note", null) };
		JSONObject jObject = getJsonDatagridEasyUI(mapList, iCount.intValue(), db2Pages);
		return jObject;
		// end of 方式3 ========================================= */
	}

	// 拼查询条件（where语句）
	String getZzcSqlWherenlj(ZSZzc zsZzc, String qdate) {
		// 拼出条件语句
		String sqlWhere = "";
		if (!sqlWhere.isEmpty()) {
			sqlWhere += " and";
		}
		sqlWhere += " t.ljdate>'" + qdate + "' ";

		return sqlWhere;
	}

	// 推荐方法
	// 方式3, 取值进一步自己处理(直接转换成easyUI的datagrid需要的东西，执行效率最高，最自由)
	// -------------------------------
	@Override
	public JSONObject getZzcDatagridbzg(ZSZzc zsZzc, DataGrid dataGrid, String qdate) {
		String sqlWhere = getZzcSqlWherebzg(zsZzc, qdate);

		// 取出总数据条数（为了分页处理, 如果不用分页，取iCount值的这个处理可以不要）
		String sqlCnt = "select count(*) from z_s_zzc t";
		if (!sqlWhere.isEmpty()) {
			sqlCnt += " where" + sqlWhere;
		}
		Long iCount = getCountForJdbcParam(sqlCnt, null);

		// 取出当前页的数据
		String sql = "select DISTINCT CONCAT_WS('--',t.ksdate,t.jsdate) as qzdate,t.* from z_s_zzc t "
				+ "inner join z_s_type z on z.typename = t.depart and z.typegroupid='1' ";
		if (!sqlWhere.isEmpty()) {
			sql += " where " + sqlWhere;
			sql += " order by z.typecode";
		}
		List<Map<String, Object>> mapList = findForJdbc(sql, dataGrid.getPage(), 100);

		// 将结果集转换成页面上对应的数据集
		Db2Page[] db2Pages = { new Db2Page("id"), new Db2Page("rownum", "rownum", null),
				new Db2Page("zzcdepart", "depart", null), new Db2Page("name", "name", null),
				new Db2Page("zw", "zw", null), new Db2Page("bzgzl"), new Db2Page("spld"), new Db2Page("spdate"),
				new Db2Page("ksdate"), new Db2Page("qzdate"), new Db2Page("ljdate", "ljdate", null),
				new Db2Page("fjdate", "fjdate", null), new Db2Page("jsdate"), new Db2Page("cxtype"),
				new Db2Page("qwaddress"), new Db2Page("note", "note", null) };
		JSONObject jObject = getJsonYljWlj(mapList, qdate, db2Pages);
		return jObject;
		// end of 方式3 ========================================= */
	}

	// 推荐方法
	// 方式3, 取值进一步自己处理(直接转换成easyUI的datagrid需要的东西，执行效率最高，最自由)
	// -------------------------------
	@Override
	public List<Map<String, Object>> getZzcDatabzg(ZSZzc zsZzc, String qdate) {
		String sqlWhere = getZzcSqlWherebzg(zsZzc, qdate);

		// 取出总数据条数（为了分页处理, 如果不用分页，取iCount值的这个处理可以不要）
		String sqlCnt = "select count(*) from z_s_zzc t";
		if (!sqlWhere.isEmpty()) {
			sqlCnt += " where" + sqlWhere;
		}
		Long iCount = getCountForJdbcParam(sqlCnt, null);

		// 取出当前页的数据
		String sql = "select DISTINCT CONCAT_WS('--',t.ksdate,t.jsdate) as qzdate,t.* from z_s_zzc t "
				+ "inner join z_s_type z on z.typename = t.depart and z.typegroupid='1' ";
		if (!sqlWhere.isEmpty()) {
			sql += " where " + sqlWhere;
			sql += " order by z.typecode";
		}
		List<Map<String, Object>> mapList = findForJdbc(sql);
		return mapList;
		// end of 方式3 ========================================= */
	}

	// 推荐方法
	// 方式3, 取值进一步自己处理(直接转换成easyUI的datagrid需要的东西，执行效率最高，最自由)
	// -------------------------------
	@Override
	public JSONObject getZzcDatagridbzg1(ZSZzc zsZzc, DataGrid dataGrid, String qdate) {
		String sqlWhere = "";
		if (!"".equals(qdate)) {
			sqlWhere = getZzcSqlWherebzg(zsZzc, qdate);
		}

		// 取出总数据条数（为了分页处理, 如果不用分页，取iCount值的这个处理可以不要）
		String sqlCnt = "select count(*) from z_s_zzc t";
		if (!sqlWhere.isEmpty()) {
			sqlCnt += " where" + sqlWhere;
		}
		Long iCount = getCountForJdbcParam(sqlCnt, null);

		// 取出当前页的数据
		String sql = "select DISTINCT CONCAT_WS('--',t.ksdate,t.jsdate) as qzdate,t.* from z_s_zzc t "
				+ "inner join z_s_type z on z.typename = t.depart and z.typegroupid='1' ";
		if (!sqlWhere.isEmpty()) {
			sql += " where " + sqlWhere;
			sql += " order by z.typecode";
		}
		List<Map<String, Object>> mapList = findForJdbc(sql, dataGrid.getPage(), dataGrid.getRows());

		// 将结果集转换成页面上对应的数据集
		Db2Page[] db2Pages = { new Db2Page("id"), new Db2Page("rownum", "rownum", null),
				new Db2Page("zzcdepart", "depart", null), new Db2Page("name", "name", null),
				new Db2Page("zw", "zw", null), new Db2Page("bzgzl"), new Db2Page("spld"), new Db2Page("spdate"),
				new Db2Page("ksdate"), new Db2Page("qzdate"), new Db2Page("ljdate", "ljdate", null),
				new Db2Page("fjdate", "fjdate", null), new Db2Page("jsdate"), new Db2Page("cxtype"),
				new Db2Page("qwaddress"), new Db2Page("note", "note", null) };
		JSONObject jObject = getJsonDatagridEasyUI(mapList, iCount.intValue(), db2Pages);
		return jObject;
		// end of 方式3 ========================================= */
	}

	// 拼查询条件（where语句）
	String getZzcSqlWherebzg(ZSZzc zsZzc, String qdate) {
		// 拼出条件语句
		String sqlWhere = "";
		if (!sqlWhere.isEmpty()) {
			sqlWhere += " and";
		}
		sqlWhere += " t.ksdate<='" + qdate + "' ";
		sqlWhere += " and";
		sqlWhere += " t.jsdate>='" + qdate + "' ";

		return sqlWhere;
	}

	/**
	 * 根据List<Map<String, Object>>数据转换为JavaBean数据
	 * 
	 * @param <T>
	 * 
	 * @param datas
	 * @param beanClass
	 * @return
	 * @throws CommonException
	 * @throws java.lang.reflect.InvocationTargetException
	 */
	public <T> List<T> ListMap2JavaBean(List<Map<String, Object>> datas, Class<T> beanClass)
			throws CommonException, java.lang.reflect.InvocationTargetException {
		// 返回数据集合
		List<T> list = null;
		// 对象字段名称
		String fieldname = "";
		// 对象方法名称
		String methodname = "";
		// 对象方法需要赋的值
		Object methodsetvalue = "";
		try {
			list = new ArrayList<T>();
			// 得到对象所有字段
			Field fields[] = beanClass.getDeclaredFields();
			// 遍历数据
			for (Map<String, Object> mapdata : datas) {
				// 创建一个泛型类型实例
				T t = beanClass.newInstance();
				// 遍历所有字段，对应配置好的字段并赋值
				for (Field field : fields) {
					if (null != field) { // 有注解配置，下一步操作
						// 全部转化为大写
						String dbfieldname = field.getName().toUpperCase();
						// 获取字段名称
						fieldname = field.getName();
						// 拼接set方法
						methodname = "set" + capitalize(fieldname);
						// 获取data里的对应值
						methodsetvalue = mapdata.get(dbfieldname);
						// 赋值给字段
						Method m = beanClass.getDeclaredMethod(methodname, field.getType());
						m.invoke(t, methodsetvalue);
					}
				}
				// 存入返回列表
				list.add(t);
			}
		} catch (InstantiationException e) {
			throw new CommonException(e, "创建beanClass实例异常");
		} catch (IllegalAccessException e) {
			throw new CommonException(e, "创建beanClass实例异常");
		} catch (SecurityException e) {
			throw new CommonException(e, "获取[" + fieldname + "] getter setter 方法异常");
		} catch (NoSuchMethodException e) {
			throw new CommonException(e, "获取[" + fieldname + "] getter setter 方法异常");
		} catch (IllegalArgumentException e) {
			throw new CommonException(e, "[" + methodname + "] 方法赋值异常");
		}
		// 返回
		return list;
	}

	/**
	 * 公共异常类 备注：与原异常没有区别，只是多了一个errormsg字段，保存开发人员提供的异常提示信息
	 * 
	 * @author suny
	 * @date 2017-7-4
	 * 
	 *       <pre>
	 *  desc:
	 *       </pre>
	 */
	public class CommonException extends Exception {
		// 原始异常
		private Throwable target;
		// 开发提供异常提示内容
		private String errormsg = "";

		public Throwable getTargetException() {
			return target;
		}

		public Throwable getCause() {
			return target;
		}

		protected CommonException() {
			super((Throwable) null);
		}

		public CommonException(Throwable target, String s) {
			super(s, null);
			this.target = target;
			this.errormsg = s;
		}

		public CommonException(Throwable target) {
			super((Throwable) null);
			this.target = target;
		}

		public String getErrormsg() {
			return errormsg;
		}
	}

	public static String capitalize(String str) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0) {
			return str;
		}
		return new StringBuilder(strLen).append(Character.toTitleCase(str.charAt(0))).append(str.substring(1))
				.toString();
	}
	

	/***** Upd By ZM 20170922 增加重复记录check start******/		
	/****  查询数据库是否存在重复时间记录，若存在则返回true，否则返回false**/
	@Override
	public Boolean checkDuplicate(ZSZzc zsZzc) {
		Boolean boolRtn = false;
		String sqlWhere = " z.id != '" + zsZzc.getId() + "' and ";
		sqlWhere += " z.depart = '" + zsZzc.getZzcdepart() + "' and ";
		sqlWhere += " z.name = '" + zsZzc.getName() + "' and ";
		sqlWhere += " ((z.ksdate <= '" + zsZzc.getKsdate() + "'";
		sqlWhere += " and z.jsdate >= '" + zsZzc.getKsdate() + "') or";
		sqlWhere += " (z.ksdate <= '" + zsZzc.getJsdate() + "'";
		sqlWhere += " and z.jsdate >= '" + zsZzc.getJsdate() + "') or";
		sqlWhere += " (z.ksdate >= '" + zsZzc.getKsdate() + "'";
		sqlWhere += " and z.jsdate <= '" + zsZzc.getJsdate() + "'))";

		// 取出总数据条数（为了分页处理, 如果不用分页，取iCount值的这个处理可以不要）
		String sqlCnt = "select count(*) from z_s_zzc z ";
			   sqlCnt += " where " + sqlWhere;
		Long iCount = getCountForJdbc(sqlCnt);
		if (iCount == 0) {boolRtn = true;}		
		return boolRtn;
	}
	/***** Upd By ZM 20170922 增加重复记录check end******/	
}