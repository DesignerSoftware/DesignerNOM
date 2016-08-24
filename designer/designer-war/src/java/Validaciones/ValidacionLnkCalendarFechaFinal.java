package Validaciones;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@ManagedBean
@FacesValidator(value = "validacionLnkCalendarFechaFinal")
public class ValidacionLnkCalendarFechaFinal implements Validator {

    public ValidacionLnkCalendarFechaFinal() {
    }

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        Date emputado = (Date) value;
        if (validarFecha(emputado) == false) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Nnnn", "mnnnnn");
            throw new ValidatorException(message);
        }
    }

    public boolean validarInfoFecha(Date fecha) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy"); //año-mes-dia  
        String value = "";
        value = dateFormat.format(fecha);
        String dias = "";
        String mes = "";
        String anio = "";
        dias = value.charAt(0) + "" + value.charAt(1);
        mes = value.charAt(3) + "" + value.charAt(4);
        anio = value.charAt(6) + "" + value.charAt(7) + value.charAt(8) + "" + value.charAt(9);
        int d,m,a;
        d=Integer.parseInt(dias);
        m=Integer.parseInt(mes);
        a=Integer.parseInt(anio);
        return false;
    }

    public boolean validarFecha(Date fecha) {
        if (fecha == null) {
            return false;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy"); //año-mes-dia  
        String value = "";
        value = dateFormat.format(fecha);
        int tamOne = value.trim().length();
        int tamTwo = dateFormat.toPattern().length();
        if (tamOne != tamTwo) {
            return false;
        }
        dateFormat.setLenient(false);
        try {
            System.out.println("dateFormat.parse(value.trim()) : "+dateFormat.parse(value.trim()));
            dateFormat.parse(value.trim());
        } catch (ParseException pe) {
            return false;
        }
        return true;
    }
}
