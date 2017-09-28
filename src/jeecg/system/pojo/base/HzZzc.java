package jeecg.system.pojo.base;

import org.jeecgframework.core.annotation.Excel;
import org.jeecgframework.core.common.entity.IdEntity;

/**
 * 项目文件父表(其他文件表需继承该表)
 */
public class HzZzc extends IdEntity implements java.io.Serializable {
	@Excel(exportName = "单位", exportConvertSign = 0, exportFieldWidth = 20, importConvertSign = 0)
	private String typename;// 单位
	@Excel(exportName = "小计", exportConvertSign = 0, exportFieldWidth = 10, importConvertSign = 0)
	private String bzghj;// 不在岗人数小计
	@Excel(exportName = "不在岗未离京人数", exportConvertSign = 0, exportFieldWidth = 10, importConvertSign = 0)
	private String wlj;// 不在岗未离京人数
	@Excel(exportName = "不在岗已离京人数", exportConvertSign = 0, exportFieldWidth = 10, importConvertSign = 0)
	private String ylj;// 不在岗已离京人数
	@Excel(exportName = "已审批拟离京人数", exportConvertSign = 0, exportFieldWidth = 10, importConvertSign = 0)
	private String nlj;// 已审批拟离京人数
	@Excel(exportName = "实有警力", exportConvertSign = 0, exportFieldWidth = 0, importConvertSign = 0)
	private String jl;// 实有警力
	public String getTypename() {
		return typename;
	}
	public void setTypename(String typename) {
		this.typename = typename;
	}
	public String getBzghj() {
		return bzghj;
	}
	public void setBzghj(String bzghj) {
		this.bzghj = bzghj;
	}
	public String getWlj() {
		return wlj;
	}
	public void setWlj(String wlj) {
		this.wlj = wlj;
	}
	public String getYlj() {
		return ylj;
	}
	public void setYlj(String ylj) {
		this.ylj = ylj;
	}
	public String getNlj() {
		return nlj;
	}
	public void setNlj(String nlj) {
		this.nlj = nlj;
	}
	public String getJl() {
		return jl;
	}
	public void setJl(String jl) {
		this.jl = jl;
	}
}