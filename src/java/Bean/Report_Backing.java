/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Bean;

import Entity.Documents;
import JPA.UsersJpaController;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author GIGABYTE
 */
@ManagedBean
@ViewScoped
public class Report_Backing {

    /**
     * Creates a new instance of Report_Backing
     */
    private final URL resource;
    private String pathName = "";
    UsersJpaController JPAUsers = new UsersJpaController();

    public Report_Backing() {
        resource = getClass().getResource("/");

        pathName = resource.getPath();
        pathName = pathName.replaceAll("%20", " ");

    }

    public void groups_name() throws JRException, IOException {
        String RNaaaaaame = "تقرير اسماء المجموعات";
        pathName += "Report/mine/Groupsname.jasper";
        bbb(RNaaaaaame, pathName);
    }

    public void groupsUser() throws JRException, IOException {
        String RNaaaaaame = "تقرير بيانات المستخدمين بالمجموعات";
        pathName += "Report/mine/GroupsUser.jasper";
        bbb(RNaaaaaame, pathName);
    }

    public void report3() throws JRException, IOException {
        String RNaaaaaame = "تفاصيل المجموعات";
        pathName += "Report/mine/report3.jasper";
        bbb(RNaaaaaame, pathName);
    }

    public void report4() throws JRException, IOException {
        String RNaaaaaame = "تقرير اسماء المستخدمين";
        pathName += "Report/mine/report4.jasper";
        bbb(RNaaaaaame, pathName);
    }

    public void report5() throws JRException, IOException {
        String RNaaaaaame = "تفاصيل المستخدمين";
        pathName += "Report/mine/report5.jasper";
        bbb(RNaaaaaame, pathName);
    }

    public void report7() throws JRException, IOException {
        String RNaaaaaame = "نماذج الرسائل";
        pathName += "Report/mine/report7.jasper";
        bbb(RNaaaaaame, pathName);
    }

    public void report8() throws JRException, IOException {
        String RNaaaaaame = "تصنيف المستندات";
        pathName += "Report/mine/report8.jasper";
        bbb(RNaaaaaame, pathName);
    }

    public void report14() throws JRException, IOException {
        String RNaaaaaame = "تقرير طلبات الاطلاع";
        pathName += "Report/mine/report14.jasper";
        bbb(RNaaaaaame, pathName);
    }

    public void report15() throws JRException, IOException {
        String RNaaaaaame = "تقرير طلبات الاطلاع المقبولة";
        pathName += "Report/mine/report15.jasper";
        bbb(RNaaaaaame, pathName);
    }

    public void report16() throws JRException, IOException {
        String RNaaaaaame = "تقرير طلبات الاطلاع المرفوضة";
        pathName += "Report/mine/report16.jasper";
        bbb(RNaaaaaame, pathName);
    }

    public void report61() throws JRException, IOException {
        String RNaaaaaame = "تقرير  تفاصيل صفحات الويب";
        pathName += "Report/mine/report61.jasper";
        bbb(RNaaaaaame, pathName);
    }

    public String logIn(Documents dc) throws IOException, JRException {
        try {
            String Path = "";
            dc.getHomeId().getHomePath();
            if (dc.getHomeId().getHomePath().endsWith("/") || dc.getHomeId().getHomePath().endsWith("\\")) {
                Path = "\"" + dc.getHomeId().getHomePath() + dc.getFileName() + "\"";
            } else {
                Path = "\"" + dc.getHomeId().getHomePath() + "\\" + dc.getFileName() + "\"";
            }
            System.out.println("Path " + Path);
            pathName += "Report/mine/reportPrint.jrxml";
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(pathName);
            Node employee = document.getElementsByTagName("image").item(1);
            NodeList nodes = employee.getChildNodes();
            for (int i = 0; i < nodes.getLength(); i++) {
                Node element = nodes.item(i);
                if ("imageExpression".equals(element.getNodeName())) {
                    element.setTextContent("<![CDATA[" + Path + "]]>");
                    System.out.println("Element " + element.getNodeName() + " Value " + element.getTextContent());
                }
                System.out.println("Element " + element.getNodeName() + " Value " + element.getTextContent());
            }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File(pathName));
            transformer.transform(domSource, streamResult);
            System.out.println("The XML File was ");
        } catch (ParserConfigurationException | TransformerException | IOException | SAXException pce) {
            pce.getCause();
        }
        reportPrint();
        return "";
    }

    public void reportPrint() throws JRException, IOException {
        String RNaaaaaame = "نسخة طبق الأصل";
        bbb(RNaaaaaame, JasperCompileManager.compileReportToFile(pathName));
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("تم", "انشاء التقرير فيC://Reports//  "));
    }

    public void bbb(String RNaaaaaame, String selectedReport) throws JRException, IOException {
        Connection conn;
        File file;
        int userID = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userId").toString());

        boolean s = (file = new File("C://Reports//" + JPAUsers.findUsers(userID).getUserName() + "//")).mkdirs();
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/arch-1?useUnicode=yes&characterEncoding=UTF-8", "root", "root");
            JasperPrint jasperPrint
                    = JasperFillManager.fillReport(selectedReport, new HashMap(), conn);
            JasperExportManager.exportReportToHtmlFile(jasperPrint, file + "//" + RNaaaaaame + ".html");

        } catch (SQLException ex) {
            Logger.getLogger(Report_Backing.class.getName()).log(Level.SEVERE, null, ex);
        }
        pathName = resource.getPath();
        pathName = pathName.replaceAll("%20", " ");
    }
}
