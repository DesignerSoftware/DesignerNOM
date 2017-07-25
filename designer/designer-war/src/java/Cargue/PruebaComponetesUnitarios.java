package Cargue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;

@ManagedBean
@SessionScoped
public class PruebaComponetesUnitarios implements Serializable {

   private static Logger log = Logger.getLogger(PruebaComponetesUnitarios.class);

   private String inputFoco;
   private List<String> listaPrueb;
   private int num;

   public PruebaComponetesUnitarios() {
      listaPrueb = null;
      inputFoco = "form:tablaEjemplo:0:input3";
      num = 0;

   }

   public List<String> getListaPrueb() {
      if (listaPrueb == null) {
         listaPrueb = new ArrayList<String>();
         listaPrueb.add("HOLA1");
         listaPrueb.add("HOLA2");
         listaPrueb.add("HOLA3");
         listaPrueb.add("HOLA4");
      }
      return listaPrueb;
   }

   public void probarFocusDinamico() {
      if (num < 4) {
         num++;
         inputFoco = "form:tablaEjemplo:" + num + ":input3";
         RequestContext context = RequestContext.getCurrentInstance();
         context.update("form:superBoton");
      }
   }

   public void setListaPrueb(List<String> listaPrueb) {
      this.listaPrueb = listaPrueb;
   }

   public String getInputFoco() {
      return inputFoco;
   }

   public void setInputFoco(String inputFoco) {
      this.inputFoco = inputFoco;
   }

   public void cambiarFoco() {
   }
}
