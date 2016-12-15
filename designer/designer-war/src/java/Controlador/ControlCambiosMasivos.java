/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.CambiosMasivos;
import Entidades.Parametros;
import Entidades.ParametrosCambiosMasivos;
import InterfaceAdministrar.AdministrarCambiosMasivosInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

/**
 *
 * @author user
 */
@ManagedBean
@Named(value = "controlCambiosMasivos")
@SessionScoped
public class ControlCambiosMasivos {
//

   @EJB
   AdministrarCambiosMasivosInterface administrarCambiosMasivos;
   @EJB
   AdministrarRastrosInterface administrarRastros;

   private List<Parametros> listaParametros;
   private List<Parametros> filtradoParametros;
   private List<CambiosMasivos> listaCambiosMasivos;
   private CambiosMasivos cambioMasivoSeleccionado;
   private List<CambiosMasivos> filtradoCambiosMasivos;
   private ParametrosCambiosMasivos parametroCambioMasivoActual;

   private String paginaAnterior;

   public ControlCambiosMasivos() {
      listaParametros = null;
      listaCambiosMasivos = null;
      cambioMasivoSeleccionado = null;
      parametroCambioMasivoActual = null;

      paginaAnterior = "";
   }

   @PostConstruct
   public void inicializarAdministrador() {
      System.out.println("ControlCambiosMasivos.inicializarAdministrador()");
      try {
         FacesContext x = FacesContext.getCurrentInstance();
         HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
         administrarCambiosMasivos.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
      } catch (Exception e) {
         System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
         System.out.println("Error: " + e);
      }
   }

   public void recibirPagina(String pagina) {
      paginaAnterior = pagina;
   }

   public String valorPaginaAnterior() {
      return paginaAnterior;
   }

   //GETS AND SETS
   public List<Parametros> getListaParametros() {
      if (listaParametros == null) {
         System.out.println("ControlCambiosMasivos.getListaParametros()");
         listaParametros = administrarCambiosMasivos.consultarEmpleadosParametros();
         System.out.println("Ya consulto listaParametros : " + listaParametros);
      }
      return listaParametros;
   }

   /**
    *
    * @param listaParametros
    */
   public void setListaParametros(List<Parametros> listaParametros) {
      this.listaParametros = listaParametros;
   }

   public List<Parametros> getFiltradoParametros() {
      return filtradoParametros;
   }

   public void setFiltradoParametros(List<Parametros> filtradoParametros) {
      this.filtradoParametros = filtradoParametros;
   }

   public List<CambiosMasivos> getListaCambiosMasivos() {
      if (listaCambiosMasivos == null) {
         System.out.println("ControlCambiosMasivos.getListaCambiosMasivos()");
         listaCambiosMasivos = administrarCambiosMasivos.consultarUltimosCambiosMasivos();
         System.out.println("Ya consulto listaCambiosMasivos : " + listaCambiosMasivos);
      }
      return listaCambiosMasivos;
   }

   public void setListaCambiosMasivos(List<CambiosMasivos> listaCambiosMasivos) {
      this.listaCambiosMasivos = listaCambiosMasivos;
   }

   public CambiosMasivos getCambioMasivoSeleccionado() {
      return cambioMasivoSeleccionado;
   }

   public void setCambioMasivoSeleccionado(CambiosMasivos cambioMasivoSeleccionado) {
      this.cambioMasivoSeleccionado = cambioMasivoSeleccionado;
   }

   public List<CambiosMasivos> getFiltradoCambiosMasivos() {
      return filtradoCambiosMasivos;
   }

   public void setFiltradoCambiosMasivos(List<CambiosMasivos> filtradoCambiosMasivos) {
      this.filtradoCambiosMasivos = filtradoCambiosMasivos;
   }

   public ParametrosCambiosMasivos getParametroCambioMasivoActual() {
      if (parametroCambioMasivoActual == null) {
         System.out.println("ControlCambiosMasivos.getParametroCambioMasivoActual()");
         parametroCambioMasivoActual = administrarCambiosMasivos.consultarParametrosCambiosMasivos();
         System.out.println("Ya consulto parametroCambioMasivoActual : " + parametroCambioMasivoActual);
      }
      return parametroCambioMasivoActual;
   }

   public void setParametroCambioMasivoActual(ParametrosCambiosMasivos parametroCambioMasivoActual) {
      this.parametroCambioMasivoActual = parametroCambioMasivoActual;
   }

}
