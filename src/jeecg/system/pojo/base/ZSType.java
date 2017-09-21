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
 * 通用类型字典表
 */
@Entity
@Table(name = "z_s_type")
public class ZSType extends IdEntity implements java.io.Serializable,Comparable<ZSType> {

	private ZSTypegroup ZSTypegroup;//类型分组
	private ZSType ZSType;//父类型
	private String typename;//类型名称
	private String typecode;//类型编码
	private String value;//类型编码
//	private List<TPProcess> TSProcesses = new ArrayList();
	private List<ZSType> ZSTypes =new ArrayList();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "typegroupid")
	public ZSTypegroup getZSTypegroup() {
		return this.ZSTypegroup;
	}

	public void setZSTypegroup(ZSTypegroup ZSTypegroup) {
		this.ZSTypegroup = ZSTypegroup;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "typepid")
	public ZSType getZSType() {
		return this.ZSType;
	}

	public void setZSType(ZSType ZSType) {
		this.ZSType = ZSType;
	}

	@Column(name = "typename", length = 50)
	public String getTypename() {
		return this.typename;
	}

	public void setTypename(String typename) {
		this.typename = typename;
	}

	@Column(name = "typecode", length = 50)
	public String getTypecode() {
		return this.typecode;
	}

	public void setTypecode(String typecode) {
		this.typecode = typecode;
	}
	@Column(name = "value")
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "ZSType")
	public List<ZSType> getZSTypes() {
		return this.ZSTypes;
	}

	public void setZSTypes(List<ZSType> ZSTypes) {
		this.ZSTypes = ZSTypes;
	}

	@Override
	public int compareTo(ZSType t) {
		// TODO Auto-generated method stub
		if (t == null)
            return 1;
        int value = Integer.parseInt(this.typecode) - Integer.parseInt(t.typecode);
        return value;
	}
}