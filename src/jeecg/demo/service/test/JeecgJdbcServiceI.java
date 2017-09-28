package jeecg.demo.service.test;

import jeecg.demo.entity.test.JeecgJdbcEntity;
import jeecg.demo.service.impl.test.JeecgJdbcServiceImpl.CommonException;
import jeecg.system.pojo.base.ZSZzc;
import net.sf.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.common.service.CommonService;

public interface JeecgJdbcServiceI extends CommonService{
	public void getDatagrid1(JeecgJdbcEntity pageObj, DataGrid dataGrid);
	public void getDatagrid2(JeecgJdbcEntity pageObj, DataGrid dataGrid);
	public JSONObject getDatagrid3(JeecgJdbcEntity pageObj, DataGrid dataGrid);
	public JSONObject getZzcDatagrid(ZSZzc zsZzc, DataGrid dataGrid);
	public JSONObject getZzcDatagridylj(ZSZzc zsZzc, DataGrid dataGrid,String qdate);
	public JSONObject getZzcDatagridwlj(ZSZzc zsZzc, DataGrid dataGrid,String qdate);
	public JSONObject getZzcDatagridhz(ZSZzc zsZzc, DataGrid dataGrid,String qdate);
	public JSONObject getZzcDatagridnlj(ZSZzc zsZzc, DataGrid dataGrid,String qdate);
	public JSONObject getZzcDatagridbzg(ZSZzc zsZzc, DataGrid dataGrid,String qdate);
	
	public JSONObject getZzcDatagridylj1(ZSZzc zsZzc, DataGrid dataGrid,String qdate);
	public JSONObject getZzcDatagridwlj1(ZSZzc zsZzc, DataGrid dataGrid,String qdate);
	public JSONObject getZzcDatagridhz1(ZSZzc zsZzc, DataGrid dataGrid,String qdate);
	public JSONObject getZzcDatagridnlj1(ZSZzc zsZzc, DataGrid dataGrid,String qdate);
	public JSONObject getZzcDatagridbzg1(ZSZzc zsZzc, DataGrid dataGrid,String qdate);
	
	public <T> List<T> getZzcDataylj(ZSZzc zsZzc, String qdate);
	public <T> List<T> getZzcDatawlj(ZSZzc zsZzc, String qdate);
	public <T> List<T> getZzcDatahz(ZSZzc zsZzc, String qdate);
	public <T> List<T> getZzcDatanlj(ZSZzc zsZzc, String qdate);
	public <T> List<T> getZzcDatabzg(ZSZzc zsZzc, String qdate);
	
	public <T> List<T> ListMap2JavaBean(List<Map<String, Object>> datas, Class<T> beanClass) throws CommonException, InvocationTargetException;

	/***** Add By ZM 20170924 增加重复记录check ******/
	public Boolean checkDuplicate(ZSZzc zsZzc);
}
