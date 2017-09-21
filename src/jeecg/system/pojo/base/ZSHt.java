package jeecg.system.pojo.base;
// default package

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.jeecgframework.core.common.entity.IdEntity;

/**
 * 通用合同表
 */
@Entity
@Table(name = "z_s_ht")
public class ZSHt extends IdEntity implements java.io.Serializable {

	private ZSHtgroup ZSHtgroup;//类型分组
	private ZSHt ZSHt;//父类型
	
	private String htname;//合同名称
	private String htnote1;//详情
	private String ssxt;//所属系统
	private String yys;//运营商/合作厂商
	private String lxr;//联系人
	private String price;//价格（一般为年费）
	private String expirydate;//有效期
	private String signingdate;//签订日期
	private String ifqs;//是否具有请示
	private String ifsp;//是否具有合同审批单
	private String jbr;//合同经办人
	private String htcl;//原件/复印件
	private String status;//续签/延续执行
	private String htnote2;//备注
	private String cjqk;//承接情况
	private String ifcd;//原件和合同审批单是否交总队档案
	private String ifsave;//是否留原件
	private String wjjbh;//文件夹编号
	private String amount;//数量
	private String zylb;//租用类别
	private String ifxx;//现行/非现行
	private String htnote3;//疑问？
	
	private List<ZSHt> ZSHts =new ArrayList();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "htgroupid")
	public ZSHtgroup getZSHtgroup() {
		return this.ZSHtgroup;
	}

	public void setZSHtgroup(ZSHtgroup ZSHtgroup) {
		this.ZSHtgroup = ZSHtgroup;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "htid")
	public ZSHt getZSHt() {
		return this.ZSHt;
	}

	public void setZSHt(ZSHt ZSHt) {
		this.ZSHt = ZSHt;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "ZSHt")
	public List<ZSHt> getZSHts() {
		return this.ZSHts;
	}

	public void setZSHts(List<ZSHt> ZSHts) {
		this.ZSHts = ZSHts;
	}
	
	@Column(name = "htname")
	public String getHtname() {
		return htname;
	}

	public void setHtname(String htname) {
		this.htname = htname;
	}

	@Column(name = "htnote1")
	public String getHtnote1() {
		return htnote1;
	}

	public void setHtnote1(String htnote1) {
		this.htnote1 = htnote1;
	}
	
	@Column(name = "ssxt")
	public String getSsxt() {
		return ssxt;
	}

	public void setSsxt(String ssxt) {
		this.ssxt = ssxt;
	}

	@Column(name = "yys")
	public String getYys() {
		return yys;
	}

	public void setYys(String yys) {
		this.yys = yys;
	}

	@Column(name = "lxr")
	public String getLxr() {
		return lxr;
	}

	public void setLxr(String lxr) {
		this.lxr = lxr;
	}

	@Column(name = "price")
	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	@Column(name = "expirydate")
	public String getExpirydate() {
		return expirydate;
	}

	public void setExpirydate(String expirydate) {
		this.expirydate = expirydate;
	}

	@Column(name = "signingdate")
	public String getSigningdate() {
		return signingdate;
	}

	public void setSigningdate(String signingdate) {
		this.signingdate = signingdate;
	}

	@Column(name = "ifqs")
	public String getIfqs() {
		return ifqs;
	}

	public void setIfqs(String ifqs) {
		this.ifqs = ifqs;
	}

	@Column(name = "ifsp")
	public String getIfsp() {
		return ifsp;
	}

	public void setIfsp(String ifsp) {
		this.ifsp = ifsp;
	}

	@Column(name = "jbr")
	public String getJbr() {
		return jbr;
	}

	public void setJbr(String jbr) {
		this.jbr = jbr;
	}

	@Column(name = "htcl")
	public String getHtcl() {
		return htcl;
	}

	public void setHtcl(String htcl) {
		this.htcl = htcl;
	}

	@Column(name = "status")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "htnote2")
	public String getHtnote2() {
		return htnote2;
	}

	public void setHtnote2(String htnote2) {
		this.htnote2 = htnote2;
	}

	@Column(name = "cjqk")
	public String getCjqk() {
		return cjqk;
	}

	public void setCjqk(String cjqk) {
		this.cjqk = cjqk;
	}

	@Column(name = "ifcd")
	public String getIfcd() {
		return ifcd;
	}

	public void setIfcd(String ifcd) {
		this.ifcd = ifcd;
	}

	@Column(name = "ifsave")
	public String getIfsave() {
		return ifsave;
	}

	public void setIfsave(String ifsave) {
		this.ifsave = ifsave;
	}

	@Column(name = "wjjbh")
	public String getWjjbh() {
		return wjjbh;
	}

	public void setWjjbh(String wjjbh) {
		this.wjjbh = wjjbh;
	}

	@Column(name = "amount")
	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	@Column(name = "zylb")
	public String getZylb() {
		return zylb;
	}

	public void setZylb(String zylb) {
		this.zylb = zylb;
	}

	@Column(name = "ifxx")
	public String getIfxx() {
		return ifxx;
	}

	public void setIfxx(String ifxx) {
		this.ifxx = ifxx;
	}

	@Column(name = "htnote3")
	public String getHtnote3() {
		return htnote3;
	}

	public void setHtnote3(String htnote3) {
		this.htnote3 = htnote3;
	}
	
}