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
@Table(name = "z_s_doc")
@Inheritance(strategy = InheritanceType.JOINED)
public  class ZSDoc extends IdEntity implements java.io.Serializable {
	
	private TSUser TSUser;// 创建人
	private String attachmenttitle;// 附件名称
	private String realpath;// 附件物理路径
	private Timestamp createdate;//导入日期
	private String note;//创建日期
	private String extend;// 扩展名
	
	private String docTitle;//内容名称
	private String docBigleader;//总队领导
	private String docClassleader;//代班领导
	private String docClassnum;//值班警力
	
	private String docXtwh;//系统维护
	private String docXtbz;//信通报障
	private String docGzcl;//故障处理
	private String docZbqk;//其他值班情况
	private String docNote;//备注
	
	@Column(name = "extend", length = 32)
	public String getExtend() {
		return extend;
	}

	public void setExtend(String extend) {
		this.extend = extend;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userid")
	public TSUser getTSUser() {
		return this.TSUser;
	}

	public void setTSUser(TSUser TSUser) {
		this.TSUser = TSUser;
	}

	@Column(name = "attachmenttitle", length = 100)
	public String getAttachmenttitle() {
		return this.attachmenttitle;
	}

	public void setAttachmenttitle(String attachmenttitle) {
		this.attachmenttitle = attachmenttitle;
	}

	@Column(name = "realpath", length = 100)
	public String getRealpath() {
		return this.realpath;
	}

	public void setRealpath(String realpath) {
		this.realpath = realpath;
	}


	@Column(name = "createdate", length = 35)
	public Timestamp getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Timestamp createdate) {
		this.createdate = createdate;
	}

	@Column(name = "note", length = 300)
	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	
	@Column(name = "doctitle")
	public String getDocTitle() {
		return docTitle;
	}

	public void setDocTitle(String docTitle) {
		this.docTitle = docTitle;
	}

	@Column(name = "docbigleader")
	public String getDocBigleader() {
		return docBigleader;
	}

	public void setDocBigleader(String docBigleader) {
		this.docBigleader = docBigleader;
	}

	@Column(name = "docclassleader")
	public String getDocClassleader() {
		return docClassleader;
	}

	public void setDocClassleader(String docClassleader) {
		this.docClassleader = docClassleader;
	}

	@Column(name = "docclassnum")
	public String getDocClassnum() {
		return docClassnum;
	}

	public void setDocClassnum(String docClassnum) {
		this.docClassnum = docClassnum;
	}

	@Column(name = "docxtwh")
	public String getDocXtwh() {
		return docXtwh;
	}

	public void setDocXtwh(String docXtwh) {
		this.docXtwh = docXtwh;
	}

	@Column(name = "docxtbz")
	public String getDocXtbz() {
		return docXtbz;
	}

	public void setDocXtbz(String docXtbz) {
		this.docXtbz = docXtbz;
	}

	@Column(name = "docgzcl")
	public String getDocGzcl() {
		return docGzcl;
	}

	public void setDocGzcl(String docGzcl) {
		this.docGzcl = docGzcl;
	}

	@Column(name = "doczbqk")
	public String getDocZbqk() {
		return docZbqk;
	}

	public void setDocZbqk(String docZbqk) {
		this.docZbqk = docZbqk;
	}

	@Column(name = "docnote")
	public String getDocNote() {
		return docNote;
	}

	public void setDocNote(String docNote) {
		this.docNote = docNote;
	}
}