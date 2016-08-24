package Convertidores;

import Entidades.MotivosCambiosCargos;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

/**
 *
 * @author Huguinho
 */

public class MotivosCambiosCargosConverter implements Converter, Serializable {

    private List<MotivosCambiosCargos> mo;

    public MotivosCambiosCargosConverter() {
        mo = new ArrayList<MotivosCambiosCargos>();
    }

    public List<MotivosCambiosCargos> getMo() {
        return mo;
    }

    public void setMo(List<MotivosCambiosCargos> mo) {
        this.mo = mo;
    }

    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        getMo();
        if (value.trim().equals("")) {
            return null;
        } else {
            try {

                for (MotivosCambiosCargos m : mo) {
                    if (m.getNombre().equalsIgnoreCase(value)) {
                        return m;
                    }
                }

            } catch (NumberFormatException exception) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "no es un motivo permitido"));
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null || value.equals("")) {
            return "";
        } else {
            return String.valueOf(((MotivosCambiosCargos) value).getNombre());
        }
    }
}
