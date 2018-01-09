/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Converter;

import Entity.Users;
import JPA.UsersJpaController;
import java.util.Map;
import java.util.WeakHashMap;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.primefaces.component.picklist.PickList;
import org.primefaces.model.DualListModel;

/**
 *
 * @author norsin
 */
@ViewScoped
@FacesConverter(value = "UserConverter")
public class UserConverter implements Converter {

    private static Map<Object, String> entities = new WeakHashMap<Object, String>();
    UsersJpaController JPA = new UsersJpaController();

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String submittedValue) {
        for (Map.Entry<Object, String> entry : entities.entrySet()) {
            if (entry.getValue().equals(submittedValue)) {
                Users u = JPA.findUsers(Integer.parseInt(submittedValue));
                return u;
            }
        }
        return null;

    }

    @Override
    public String getAsString(FacesContext arg0, UIComponent component, Object value) {
        synchronized (entities) {
            if (!entities.containsKey(value)) {
                String uuid = value.toString();
                entities.put(value, uuid);
                return uuid;
            } else {
                return entities.get(value);
            }
        }
    }
}
