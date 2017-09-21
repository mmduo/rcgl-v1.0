package jeecg.system.pojo.base;
// default package

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.jeecgframework.core.common.entity.IdEntity;

/**
 * 文档下载,新闻,法规表
 */
@Entity
@Table(name = "t_s_document1")
public class TSDocument1 extends IdEntity implements java.io.Serializable {
	
	private String documentTitle;//文档标题
	private TSAttachment tsAttachment;//父表
	
	private String docTitle1;//内容名称
	private String docBigleader;//总队领导
	private String docClassleader;//代班领导
	private String docClassnum;//值班警力
	
	private String docXtwh;//系统维护
	private String docXtbz;//信通报障
	private String docGzcl;//故障处理
	private String docZbqk;//其他值班情况
	private String docNote;//备注
	private String docTime;//时间
	
	private String docIndex;//条目编号
	private String docTxt;//条目内容
	//private Short showHome;//是否首页显示
	//private TSType TSType;//文档分类
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "flid")
	public TSAttachment getTsAttachment() {
		return tsAttachment;
	}
	public void setTsAttachment(TSAttachment tsAttachment) {
		this.tsAttachment = tsAttachment;
	}
	
	@Column(name = "documenttitle", length = 100)
	public String getDocumentTitle() {
		return documentTitle;
	}
	public void setDocumentTitle(String documentTitle) {
		this.documentTitle = documentTitle;
	}
	@Column(name = "docindex")
	public String getDocIndex() {
		return docIndex;
	}
	public void setDocIndex(String docIndex) {
		this.docIndex = docIndex;
	}
	@Column(name = "docnote")
	public String getDocNote() {
		return docNote;
	}
	public void setDocNote(String docNote) {
		this.docNote = docNote;
	}
	@Column(name = "doctitle1")
	public String getDocTitle1() {
		return docTitle1;
	}
	public void setDocTitle1(String docTitle1) {
		this.docTitle1 = docTitle1;
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
	@Column(name = "doctime")
	public String getDocTime() {
		return docTime;
	}
	public void setDocTime(String docTime) {
		this.docTime = docTime;
	}
	@Column(name = "doctxt")
	public String getDocTxt() {
		return docTxt;
	}
	public void setDocTxt(String docTxt) {
		this.docTxt = docTxt;
	}
}