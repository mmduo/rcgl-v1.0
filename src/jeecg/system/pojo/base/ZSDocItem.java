package jeecg.system.pojo.base;
// default package

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.jeecgframework.core.common.entity.IdEntity;

/**
 * 文档附表
 */
@Entity
@Table(name = "z_s_docitem")
public class ZSDocItem extends IdEntity implements java.io.Serializable {
	
	private String docTitle;//文档标题
	private ZSDoc zsDoc;//父表
	
	private String docIndex;//条目编号
	private String docTxt;//条目内容
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "docid")
	public ZSDoc getZsDoc() {
		return zsDoc;
	}
	public void setZsDoc(ZSDoc zsDoc) {
		this.zsDoc = zsDoc;
	}
	
	@Column(name = "doctitle")
	public String getDocTitle() {
		return docTitle;
	}
	
	public void setDocTitle(String docTitle) {
		this.docTitle = docTitle;
	}

	@Column(name = "docindex")
	public String getDocIndex() {
		return docIndex;
	}
	public void setDocIndex(String docIndex) {
		this.docIndex = docIndex;
	}
	
	@Column(name = "doctxt")
	public String getDocTxt() {
		return docTxt;
	}
	public void setDocTxt(String docTxt) {
		this.docTxt = docTxt;
	}
}