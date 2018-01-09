/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Bean;

import Entity.Template;
import JPA.TemplateJpaController;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author GIGABYTE
 */
@ManagedBean
@ViewScoped
public class Template_Backing {

    /**
     * Creates a new instance of Template_Backing
     */
    private String title = "", body = "";

    private Template temp = new Template();
    private TemplateJpaController JPAtemplate = new TemplateJpaController();
    private FacesContext context;

    public Template_Backing() {
    }

    public void resetvalues() {
        title = body = "";

    }

    public void addTemp() {
        context = FacesContext.getCurrentInstance();
        temp = new Template();
        if (!title.equals("") && !body.equals("")) {
            temp.setTTitle(title);
            temp.setTBody(body);

            JPAtemplate.create(temp);
            context.addMessage(null, new FacesMessage("تــم", " إضافة النموذج "));
            title = body = "";
        }else{
            context.addMessage(null, new FacesMessage("تحذير", " برجاء إكمال البيانات "));
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

}
