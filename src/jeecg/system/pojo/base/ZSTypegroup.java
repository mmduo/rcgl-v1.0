package jeecg.system.pojo.base;
// default package

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.jeecgframework.core.common.entity.IdEntity;

/**
 * TTypegroup entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "z_s_typegroup")
public class ZSTypegroup extends IdEntity implements java.io.Serializable {
	
	public static Map<String, ZSTypegroup> allTypeGroups = new HashMap<String,ZSTypegroup>();
	public static Map<String, List<ZSType>> allTypes = new HashMap<String,List<ZSType>>();
	
	

	private String typegroupname;
	private String typegroupcode;
	private List<ZSType> ZSTypes = new ArrayList<ZSType>();
	@Column(name = "typegroupname", length = 50)
	public String getTypegroupname() {
		return this.typegroupname;
	}

	public void setTypegroupname(String typegroupname) {
		this.typegroupname = typegroupname;
	}

	@Column(name = "typegroupcode", length = 50)
	public String getTypegroupcode() {
		return this.typegroupcode;
	}

	public void setTypegroupcode(String typegroupcode) {
		this.typegroupcode = typegroupcode;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "ZSTypegroup")
	public List<ZSType> getZSTypes() {
		return this.ZSTypes;
	}

	public void setZSTypes(List<ZSType> ZSTypes) {
		this.ZSTypes = ZSTypes;
	}

}