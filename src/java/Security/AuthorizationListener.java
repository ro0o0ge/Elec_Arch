/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Security;

import Entity.ApplicationPages;
import Entity.Groups;
import Entity.PagePermission;
import JPA.PagePermissionJpaController;
import JPA.UserGroupJpaController;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.faces.application.NavigationHandler;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpSession;

/**
 *
 * @author norsin
 */
@ManagedBean
@ViewScoped
public class AuthorizationListener implements PhaseListener {

    PagePermissionJpaController PPJPA = new PagePermissionJpaController();
    UserGroupJpaController GroupsJPA = new UserGroupJpaController();

    public Set<ApplicationPages> Temp2 = new HashSet();
    boolean done = false;
    boolean UserFlag = false, GroupFlag = false;

    public void afterPhase(PhaseEvent event) {

        Temp2.clear();
        FacesContext facesContext = event.getFacesContext();
        String currentPage = facesContext.getViewRoot().getViewId();

        Object currentUser = null;
        boolean isLoginPage = (currentPage.lastIndexOf("Login.xhtml") > -1);
        boolean isHomePage = (currentPage.lastIndexOf("Home.xhtml") > -1);
        boolean isAboutPage = (currentPage.lastIndexOf("about.xhtml") > -1);
        boolean isContactPage = (currentPage.lastIndexOf("contact.xhtml") > -1);
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);

        if (session == null) {
            NavigationHandler nh = facesContext.getApplication().getNavigationHandler();
            nh.handleNavigation(facesContext, null, "Login.xhtml");
        } else {
            currentUser = session.getAttribute("userName");

            if (currentUser != null) {
                CheckUser(PPJPA.findByUserId(Integer.parseInt(session.getAttribute("userId").toString())), currentPage);
                CheckGroup(GroupsJPA.getGroupsForUser(Integer.parseInt(session.getAttribute("userId").toString())), currentPage);
            }
            if (!isLoginPage && (currentUser == null || currentUser.equals(""))) {
                if (isAboutPage) {
                    NavigationHandler nh = facesContext.getApplication().getNavigationHandler();
                    nh.handleNavigation(facesContext, null, "about.xhtml");
                } else if (isContactPage) {
                    NavigationHandler nh = facesContext.getApplication().getNavigationHandler();
                    nh.handleNavigation(facesContext, null, "contact.xhtml");
                } else if (isHomePage) {
                    NavigationHandler nh = facesContext.getApplication().getNavigationHandler();
                    nh.handleNavigation(facesContext, null, "Home.xhtml");
                } else {
                    NavigationHandler nh = facesContext.getApplication().getNavigationHandler();
                    nh.handleNavigation(facesContext, null, "Login.xhtml");
                }
            } else if (isLoginPage || isHomePage) {
            } else if (isAboutPage || isContactPage) {
            } else {
                if (currentUser != null) {
                    UserFlag = CheckUser(PPJPA.findByUserId(Integer.parseInt(session.getAttribute("userId").toString())), currentPage);
                    GroupFlag = CheckGroup(GroupsJPA.getGroupsForUser(Integer.parseInt(session.getAttribute("userId").toString())), currentPage);
                    if (UserFlag || GroupFlag) {
                    } else {
                        NavigationHandler nh = facesContext.getApplication().getNavigationHandler();
                        nh.handleNavigation(facesContext, null, "Home.xhtml");
                    }
                } else {
                    NavigationHandler nh = facesContext.getApplication().getNavigationHandler();
                    nh.handleNavigation(facesContext, null, "Home.xhtml");
                }
            }
        }
        if (session != null) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("PagesALL");
        }
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("PagesALL", Temp2);

    }

    public boolean CheckGroup(List<Groups> GroupsList, String currentPage) {
        boolean temp = false;
        for (Groups TGroup : GroupsList) {
            List<PagePermission> PageAllowedList2
                    = PPJPA.findByGroupId(TGroup.getGroupId());
            for (PagePermission TPP : PageAllowedList2) {
                Temp2.add(TPP.getPageName());
                boolean IsOKPage = (currentPage.lastIndexOf(TPP.getPageName().getAppPageName()) > -1);
                if (IsOKPage) {
                    temp = true;
                }
            }
        }
        return temp;
    }

    public boolean CheckUser(List<PagePermission> PageAllowedList1, String currentPage) {
        boolean temp = false;
        for (PagePermission TPP : PageAllowedList1) {

            Temp2.add(TPP.getPageName());
            boolean IsOKPage = (currentPage.lastIndexOf(TPP.getPageName().getAppPageName()) != -1);
            if (IsOKPage) {
                temp = true;
            }
        }
        return temp;
    }

    public void beforePhase(PhaseEvent event) {
    }

    public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
    }
}
