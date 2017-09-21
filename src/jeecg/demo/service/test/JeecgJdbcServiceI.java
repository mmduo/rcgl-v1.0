package jeecg.demo.service.test;

import jeecg.demo.entity.test.JeecgJdbcEntity;
import jeecg.system.pojo.base.ZSZzc;
import net.sf.json.JSONObject;

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
	
	public JSONObject getZzcDatagridylj1(ZSZzc zsZzc, DataGrid dataGrid,String qdate);
	public JSONObject getZzcDatagridwlj1(ZSZzc zsZzc, DataGrid dataGrid,String qdate);
	public JSONObject getZzcDatagridhz1(ZSZzc zsZzc, DataGrid dataGrid,String qdate);
}
