/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Converter;

import Entity.Groups;
import JPA.GroupsJpaController;
import java.util.Map;
import java.util.Map.Entry;
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
@FacesConverter(value = "GroupConverter")
public class GroupConverter implements Converter {

    int c = 0;
    private static Map<Object, String> entities = new WeakHashMap<Object, String>();
    GroupsJpaController JPA = new GroupsJpaController();

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String submittedValue) {
        PickList p = (PickList) component;
        DualListModel dl = (DualListModel) p.getValue();
        for (Entry<Object, String> entry : entities.entrySet()) {
            if (entry.getValue().equals(submittedValue)) {
                Groups g = JPA.findGroups(Integer.parseInt(submittedValue));
                return g;
            }
        }
        return null;

    }

    @Override
    public String getAsString(FacesContext arg0, UIComponent component, Object value) {
        PickList p = (PickList) component;
        DualListModel dl = (DualListModel) p.getValue();
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
