package jeecg.system.pojo.base;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.jeecgframework.core.common.entity.IdEntity;


/**
 * 项目文件父表(其他文件表需继承该表)
 */
@Entity
@Table(name = "z_s_zzc")
@Inheritance(strategy = InheritanceType.JOINED)
public  class ZSZzc extends IdEntity implements java.io.Serializable {
	
	//private String zzcdepart;// 单位
	private String zzcdepart;// 单位 
	private String name;// 姓名
	private String zw;//职务
	private String bzgzl;// 不在岗种类
	
	private String spld;//审批领导
	private String spdate;//审批日期
	private String ksdate;//开始日期
	private String ljdate;//离京日期
	
	private String fjdate;//返京日期
	private String jsdate;//结束日期
	private String cxtype;//出行方式
	private String qwaddress;//前往地点
	private String note;
	
	@Column(name = "depart")
	public String getZzcdepart() {
		return zzcdepart;
	}
	public void setZzcdepart(String zzcdepart) {
		this.zzcdepart = zzcdepart;
	}
	
	@Column(name = "name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Column(name = "zw")
	public String getZw() {
		return zw;
	}
	public void setZw(String zw) {
		this.zw = zw;
	}
	@Column(name = "bzgzl")
	public String getBzgzl() {
		return bzgzl;
	}
	public void setBzgzl(String bzgzl) {
		this.bzgzl = bzgzl;
	}
	@Column(name = "spld")
	public String getSpld() {
		return spld;
	}
	public void setSpld(String spld) {
		this.spld = spld;
	}
	@Column(name = "spdate")
	public String getSpdate() {
		return spdate;
	}
	public void setSpdate(String spdate) {
		this.spdate = spdate;
	}
	@Column(name = "ksdate")
	public String getKsdate() {
		return ksdate;
	}
	public void setKsdate(String ksdate) {
		this.ksdate = ksdate;
	}
	@Column(name = "ljdate")
	public String getLjdate() {
		return ljdate;
	}
	public void setLjdate(String ljdate) {
		this.ljdate = ljdate;
	}
	@Column(name = "fjdate")
	public String getFjdate() {
		return fjdate;
	}
	public void setFjdate(String fjdate) {
		this.fjdate = fjdate;
	}
	@Column(name = "jsdate")
	public String getJsdate() {
		return jsdate;
	}
	public void setJsdate(String jsdate) {
		this.jsdate = jsdate;
	}
	@Column(name = "cxtype")
	public String getCxtype() {
		return cxtype;
	}
	public void setCxtype(String cxtype) {
		this.cxtype = cxtype;
	}
	@Column(name = "qwaddress")
	public String getQwaddress() {
		return qwaddress;
	}
	public void setQwaddress(String qwaddress) {
		this.qwaddress = qwaddress;
	}
	@Column(name = "note")
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
}