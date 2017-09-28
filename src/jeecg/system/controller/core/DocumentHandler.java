package jeecg.system.controller.core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeecgframework.core.common.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@Controller
@RequestMapping("/documentHandler")
public class DocumentHandler extends BaseController {
	private Configuration configuration = null;

	public DocumentHandler() {
		configuration = new Configuration();
		configuration.setDefaultEncoding("utf-8");
	}

	public void createDoc(Object object) throws IOException {
		// 建立数据模型（Map）
		Map<String, String> root = new HashMap<String, String>();
		root.put("attachmenttitle", "测试");

		getData(root);
		// 设置模本装置方法和路径,FreeMarker支持多种模板装载方法。可以重servlet，classpath，数据库装载，
		// 这里我们的模板是放在resources.jeecg.template包下面
		configuration.setDirectoryForTemplateLoading(new File("D:/"));
		Template t = null;
		try {
			// test.ftl为要装载的模板
			t = configuration.getTemplate("sddzlhjl_zs.ftl");
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 输出文档路径及名称
		File outFile = new File("D:/outFile.doc");
		Writer out = null;
		try {
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), "UTF-8"));

		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		try {
			t.process(root, out);
			out.flush();
			out.close();
		} catch (TemplateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 注意dataMap里存放的数据Key值要与模板中的参数相对应
	 * 
	 * @param dataMap
	 */
	private void getData(Map dataMap) {

	}

	public void createDoc1(Object object, String qdate) throws IOException {
		/*
		 * //建立数据模型（Map） Map<String, String> root = new HashMap<String,
		 * String>(); root.put("attachmenttitle", "测试");
		 * 
		 * getData(root);
		 */
		// 设置模本装置方法和路径,FreeMarker支持多种模板装载方法。可以重servlet，classpath，数据库装载，
		// 这里我们的模板是放在resources.jeecg.template包下面
		configuration.setDirectoryForTemplateLoading(new File("D:/"));
		Template t = null;
		try {
			// test.ftl为要装载的模板
			t = configuration.getTemplate("bzgylj.ftl");
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 输出文档路径及名称
		qdate = qdate + "十二总队民警不在岗已离京情况一览表.doc";
		File outFile = new File("D:/导出文件/" + qdate);
		Writer out = null;
		try {
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), "UTF-8"));

		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		try {
			t.process(object, out);
			out.flush();
			out.close();
		} catch (TemplateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void createDoc2(Object object, String qdate) throws IOException {
		/*
		 * //建立数据模型（Map） Map<String, String> root = new HashMap<String,
		 * String>(); root.put("attachmenttitle", "测试");
		 * 
		 * getData(root);
		 */
		// 设置模本装置方法和路径,FreeMarker支持多种模板装载方法。可以重servlet，classpath，数据库装载，
		// 这里我们的模板是放在resources.jeecg.template包下面
		configuration.setDirectoryForTemplateLoading(new File("D:/"));
		Template t = null;
		try {
			// test.ftl为要装载的模板
			t = configuration.getTemplate("bzgwlj.ftl");
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 输出文档路径及名称
		qdate = qdate + "十二总队民警不在岗未离京情况一览表.doc";
		File outFile = new File("D:/导出文件/" + qdate);
		Writer out = null;
		try {
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), "UTF-8"));

		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		try {
			t.process(object, out);
			out.flush();
			out.close();
		} catch (TemplateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void createDoc3(Object object, String qdate) throws IOException {
		/*
		 * //建立数据模型（Map） Map<String, String> root = new HashMap<String,
		 * String>(); root.put("attachmenttitle", "测试");
		 * 
		 * getData(root);
		 */
		// 设置模本装置方法和路径,FreeMarker支持多种模板装载方法。可以重servlet，classpath，数据库装载，
		// 这里我们的模板是放在resources.jeecg.template包下面
		configuration.setDirectoryForTemplateLoading(new File("D:/"));
		Template t = null;
		try {
			// test.ftl为要装载的模板
			t = configuration.getTemplate("bzgqkhzb.ftl");
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 输出文档路径及名称
		qdate = qdate + "十二总队民警不在岗情况汇总表.doc";
		File outFile = new File("D:/导出文件/" + qdate);
		Writer out = null;
		try {
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), "UTF-8"));

		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		try {
			t.process(object, out);
			out.flush();
			out.close();
		} catch (TemplateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void createDoc4(Object object, String qdate) throws IOException {
		/*
		 * //建立数据模型（Map） Map<String, String> root = new HashMap<String,
		 * String>(); root.put("attachmenttitle", "测试");
		 * 
		 * getData(root);
		 */
		// 设置模本装置方法和路径,FreeMarker支持多种模板装载方法。可以重servlet，classpath，数据库装载，
		// 这里我们的模板是放在resources.jeecg.template包下面
		configuration.setDirectoryForTemplateLoading(new File("D:/"));
		Template t = null;
		try {
			// test.ftl为要装载的模板
			t = configuration.getTemplate("bzgnlj.ftl");
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 输出文档路径及名称
		qdate = qdate + "十二总队民警拟离京情况一览表.doc";
		File outFile = new File("D:/导出文件/" + qdate);
		Writer out = null;
		try {
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), "UTF-8"));

		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		try {
			t.process(object, out);
			out.flush();
			out.close();
		} catch (TemplateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void createDoc5(Object object, String qdate) throws IOException {
		/*
		 * //建立数据模型（Map） Map<String, String> root = new HashMap<String,
		 * String>(); root.put("attachmenttitle", "测试");
		 * 
		 * getData(root);
		 */
		// 设置模本装置方法和路径,FreeMarker支持多种模板装载方法。可以重servlet，classpath，数据库装载，
		// 这里我们的模板是放在resources.jeecg.template包下面
		configuration.setDirectoryForTemplateLoading(new File("D:/"));
		Template t = null;
		try {
			// test.ftl为要装载的模板
			t = configuration.getTemplate("bzg.ftl");
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 输出文档路径及名称
		qdate = qdate + "十二总队民警不在岗情况一览表.doc";
		File outFile = new File("D:/导出文件/" + qdate);
		Writer out = null;
		try {
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), "UTF-8"));

		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		try {
			t.process(object, out);
			out.flush();
			out.close();
		} catch (TemplateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}