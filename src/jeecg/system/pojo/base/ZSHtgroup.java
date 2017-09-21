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
 * ZHgroup entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "z_s_htgroup")
public class ZSHtgroup extends IdEntity implements java.io.Serializable {
	//���е������ֵ�
	public static Map<String, ZSHtgroup> allHtGroups = new HashMap<String,ZSHtgroup>();
	public static Map<String, List<ZSHt>> allHts = new HashMap<String,List<ZSHt>>();
	
	

	private String htgroupname;
	private String htgroupcode;
	private List<ZSHt> ZSHts = new ArrayList<ZSHt>();
	@Column(name = "htgroupname")
	public String getHtgroupname() {
		return this.htgroupname;
	}

	public void setHtgroupname(String htgroupname) {
		this.htgroupname = htgroupname;
	}

	@Column(name = "htgroupcode")
	public String getHtgroupcode() {
		return this.htgroupcode;
	}

	public void setHtgroupcode(String htgroupcode) {
		this.htgroupcode = htgroupcode;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "ZSHtgroup")
	public List<ZSHt> getZSHts() {
		return this.ZSHts;
	}

	public void setZSHts(List<ZSHt> ZSHts) {
		this.ZSHts = ZSHts;
	}

}