package jeecg.system.pojo.base;

import org.jeecgframework.core.annotation.Excel;
import org.jeecgframework.core.common.entity.IdEntity;
import org.jeecgframework.core.annotation.Excel;


/**
 * 项目文件父表(其他文件表需继承该表)
 */
public  class BzgZzc extends IdEntity implements java.io.Serializable {
	@Excel(exportName="起止日期", exportConvertSign = 0, exportFieldWidth = 0, importConvertSign = 0)
	private String qzdate;// 起止日期
	@Excel(exportName="单位", exportConvertSign = 0, exportFieldWidth = 10, importConvertSign = 0)
	private String depart;// 单位 
	@Excel(exportName="姓名", exportConvertSign = 0, exportFieldWidth = 10, importConvertSign = 0)
	private String name;// 姓名
	@Excel(exportName="职务", exportConvertSign = 0, exportFieldWidth = 10, importConvertSign = 0)
	private String zw;//职务
	@Excel(exportName="不在岗种类", exportConvertSign = 0, exportFieldWidth = 10, importConvertSign = 0)
	private String bzgzl;// 不在岗种类
	@Excel(exportName="审批领导", exportConvertSign = 0, exportFieldWidth = 10, importConvertSign = 0)
	private String spld;//审批领导
	@Excel(exportName="审批日期", exportConvertSign = 0, exportFieldWidth = 10, importConvertSign = 0)
	private String spdate;//审批日期
	@Excel(exportName="开始日期", exportConvertSign = 0, exportFieldWidth = 10, importConvertSign = 0)
	private String ksdate;//开始日期
	@Excel(exportName="离京日期", exportConvertSign = 0, exportFieldWidth = 10, importConvertSign = 0)
	private String ljdate;//离京日期
	@Excel(exportName="返京日期", exportConvertSign = 0, exportFieldWidth = 10, importConvertSign = 0)
	private String fjdate;//返京日期
	@Excel(exportName="结束日期", exportConvertSign = 0, exportFieldWidth = 10, importConvertSign = 0)
	private String jsdate;//结束日期
	@Excel(exportName="出行方式", exportConvertSign = 0, exportFieldWidth = 10, importConvertSign = 0)
	private String cxtype;//出行方式
	@Excel(exportName="前往地点", exportConvertSign = 0, exportFieldWidth = 10, importConvertSign = 0)
	private String qwaddress;//前往地点
	@Excel(exportName="备注", exportConvertSign = 0, exportFieldWidth = 30, importConvertSign = 0)
	private String note;
	
	
	public String getQzdate() {
		return qzdate;
	}
	public void setQzdate(String qzdate) {
		this.qzdate = qzdate;
	}
	public String getDepart() {
		return depart;
	}
	public void setDepart(String depart) {
		this.depart = depart;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getZw() {
		return zw;
	}
	public void setZw(String zw) {
		this.zw = zw;
	}
	
	public String getBzgzl() {
		return bzgzl;
	}
	public void setBzgzl(String bzgzl) {
		this.bzgzl = bzgzl;
	}

	public String getSpld() {
		return spld;
	}
	public void setSpld(String spld) {
		this.spld = spld;
	}

	public String getSpdate() {
		return spdate;
	}
	public void setSpdate(String spdate) {
		this.spdate = spdate;
	}
	public String getKsdate() {
		return ksdate;
	}
	public void setKsdate(String ksdate) {
		this.ksdate = ksdate;
	}
	public String getLjdate() {
		return ljdate;
	}
	public void setLjdate(String ljdate) {
		this.ljdate = ljdate;
	}
	public String getFjdate() {
		return fjdate;
	}
	public void setFjdate(String fjdate) {
		this.fjdate = fjdate;
	}
	public String getJsdate() {
		return jsdate;
	}
	public void setJsdate(String jsdate) {
		this.jsdate = jsdate;
	}
	public String getCxtype() {
		return cxtype;
	}
	public void setCxtype(String cxtype) {
		this.cxtype = cxtype;
	}
	public String getQwaddress() {
		return qwaddress;
	}
	public void setQwaddress(String qwaddress) {
		this.qwaddress = qwaddress;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
}