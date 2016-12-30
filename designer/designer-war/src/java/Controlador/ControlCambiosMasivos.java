/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.CambiosMasivos;
import Entidades.Causasausentismos;
import Entidades.Clasesausentismos;
import Entidades.Conceptos;
import Entidades.Empleados;
import Entidades.Estructuras;
import Entidades.Formulas;
import Entidades.MotivosCambiosSueldos;
import Entidades.MotivosDefinitivas;
import Entidades.MotivosRetiros;
import Entidades.Papeles;
import Entidades.Parametros;
import Entidades.ParametrosCambiosMasivos;
import Entidades.Periodicidades;
import Entidades.Terceros;
import Entidades.TercerosSucursales;
import Entidades.TiposEntidades;
import Entidades.TiposSueldos;
import Entidades.Tiposausentismos;
import Entidades.Unidades;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarCambiosMasivosInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.export.Exporter;
import org.primefaces.context.RequestContext;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.event.TabCloseEvent;

/**
 *
 * @author user
 */
@ManagedBean
@Named(value = "controlCambiosMasivos")
@SessionScoped
public class ControlCambiosMasivos {

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
   //LOVS
   private List<Estructuras> lovEstructuras;
   private List<Estructuras> filtroLovEstructuras;
   private String infoRegistroEstructuras;
   private Estructuras estructuraSeleccionada;

   private List<MotivosDefinitivas> lovMotivosDefinitivas;
   private List<MotivosDefinitivas> filtroLovMotivosDefinitivas;
   private String infoRegistroMotivosDefinitivas;
   private MotivosDefinitivas motivoDefinitivaSeleccionada;

   private List<MotivosRetiros> lovMotivosRetiros;
   private List<MotivosRetiros> filtroLovMotivosRetiros;
   private String infoRegistroMotivosRetiros;
   private MotivosRetiros motivoRetiroSeleccionado;

   private List<TiposEntidades> lovTiposEntidades;
   private List<TiposEntidades> filtroLovTiposEntidades;
   private String infoRegistroTiposEntidades;
   private TiposEntidades tipoEntidadSeleccionada;

   private List<TercerosSucursales> lovTercerosSucursales;
   private List<TercerosSucursales> filtroLovTercerosSucursales;
   private String infoRegistroTercerosSucursales;
   private TercerosSucursales tercerosSucursalSeleccionada;

   private List<Periodicidades> lovPeriodicidades;
   private List<Periodicidades> filtroLovPeriodicidades;
   private String infoRegistroPeriodicidades;
   private Periodicidades periodicidadSeleccionada;

   private List<Conceptos> lovConceptos;
   private List<Conceptos> filtroLovConceptos;
   private String infoRegistroConceptos;
   private Conceptos conceptoSeleccionado;

   private List<Formulas> lovFormulas;
   private List<Formulas> filtroLovFormulas;
   private String infoRegistroFormulas;
   private Formulas formulaSeleccionada;

   private List<Terceros> lovTerceros;
   private List<Terceros> filtroLovTerceros;
   private String infoRegistroTerceros;
   private Terceros terceroSeleccionado;

   private List<MotivosCambiosSueldos> lovMotivosSueldos;
   private List<MotivosCambiosSueldos> filtroLovMotivosSueldos;
   private String infoRegistroMotivosSueldos;
   private MotivosCambiosSueldos motivoSueldoSeleccionado;

   private List<TiposSueldos> lovTiposSueldos;
   private List<TiposSueldos> filtroLovTiposSueldos;
   private String infoRegistroTiposSueldos;
   private TiposSueldos tipoSueldoSeleccionado;

   private List<Tiposausentismos> lovTiposausentismos;
   private List<Tiposausentismos> filtroLovTiposausentismos;
   private String infoRegistroTiposausentismos;
   private Tiposausentismos tipoausentismoSeleccionado;

   private List<Unidades> lovUnidades;
   private List<Unidades> filtroLovUnidades;
   private String infoRegistroUnidades;
   private Unidades unidadSeleccionada;

   private List<Empleados> lovEmpleados;
   private List<Empleados> filtroLovEmpleados;
   private String infoRegistroEmpleados;
   private Empleados empleadoSeleccionado;

   private List<String> lovIbcs;

   private List<Papeles> lovPapeles;
   private List<Papeles> filtroLovPapeles;
   private String infoRegistroPapeles;
   private Papeles papelSeleccionado;

   private List<Causasausentismos> lovCausasausentismos;
   private List<Causasausentismos> filtroLovCausasausentismos;
   private String infoRegistroCausasausentismos;
   private Causasausentismos causaausentismoSeleccionado;

   private List<Clasesausentismos> lovClasesausentismos;
   private List<Clasesausentismos> filtroLovClasesausentismos;
   private String infoRegistroClasesausentismos;
   private Clasesausentismos clasesausentismosSeleccionado;

   private String paginaAnterior;
   private boolean guardado, aceptar;
   private String infoRegistroParametros, infoRegistroCambiosMasivos;
   private int panelActivo, campo;

   public ControlCambiosMasivos() {
      listaParametros = null;
      listaCambiosMasivos = null;
      cambioMasivoSeleccionado = null;
      parametroCambioMasivoActual = null;
      paginaAnterior = "";
      //LOVS
      estructuraSeleccionada = null;
      motivoDefinitivaSeleccionada = null;
      motivoRetiroSeleccionado = null;
      tipoEntidadSeleccionada = null;
      tercerosSucursalSeleccionada = null;
      periodicidadSeleccionada = null;
      conceptoSeleccionado = null;
      formulaSeleccionada = null;
      terceroSeleccionado = null;
      tipoSueldoSeleccionado = null;
      motivoSueldoSeleccionado = null;
      tipoausentismoSeleccionado = null;
      unidadSeleccionada = null;
      empleadoSeleccionado = null;
      papelSeleccionado = null;
      clasesausentismosSeleccionado = null;
      causaausentismoSeleccionado = null;

      infoRegistroPeriodicidades = "";
      infoRegistroConceptos = "";
      infoRegistroFormulas = "";
      infoRegistroTerceros = "";
      infoRegistroMotivosSueldos = "";
      infoRegistroTiposSueldos = "";
      infoRegistroTiposausentismos = "";
      infoRegistroUnidades = "";
      infoRegistroEmpleados = "";
      infoRegistroPapeles = "";
      infoRegistroEstructuras = "";
      infoRegistroMotivosDefinitivas = "";
      infoRegistroMotivosRetiros = "";
      infoRegistroTiposEntidades = "";
      infoRegistroTercerosSucursales = "";
      infoRegistroCausasausentismos = "";
      infoRegistroClasesausentismos = "";

      lovEstructuras = new ArrayList<Estructuras>();
      lovMotivosDefinitivas = new ArrayList<MotivosDefinitivas>();
      lovMotivosRetiros = new ArrayList<MotivosRetiros>();
      lovTiposEntidades = new ArrayList<TiposEntidades>();
      lovTercerosSucursales = new ArrayList<TercerosSucursales>();
      lovPeriodicidades = new ArrayList<Periodicidades>();
      lovConceptos = new ArrayList<Conceptos>();
      lovFormulas = new ArrayList<Formulas>();
      lovTerceros = new ArrayList<Terceros>();
      lovMotivosSueldos = new ArrayList<MotivosCambiosSueldos>();
      lovTiposSueldos = new ArrayList<TiposSueldos>();
      lovTiposausentismos = new ArrayList<Tiposausentismos>();
      lovUnidades = new ArrayList<Unidades>();
      lovEmpleados = new ArrayList<Empleados>();
      lovIbcs = new ArrayList<String>();
      lovPapeles = new ArrayList<Papeles>();
      lovCausasausentismos = new ArrayList<Causasausentismos>();
      lovClasesausentismos = new ArrayList<Clasesausentismos>();

      campo = 0;
      panelActivo = 1;
      aceptar = true;
      guardado = true;
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

   public void cambiosMasivosHechos() {
      FacesMessage msg = new FacesMessage("Cambios Masivos", "Los Cambios se han realizado Correctamente.");
      FacesContext.getCurrentInstance().addMessage(null, msg);
      RequestContext.getCurrentInstance().update("form:growl");
   }

   public void procesarCargo() {
      if (parametroCambioMasivoActual.getCargoEstructura() != null && parametroCambioMasivoActual.getFechaCargoCambio() != null) {
         System.out.println("ControlCambiosMasivos.procesarCargo() parametroCambioMasivoActual.getCargoEstructura(): " + parametroCambioMasivoActual.getCargoEstructura()
                 + "  y parametroCambioMasivoActual.getFechaCargoCambio() + " + parametroCambioMasivoActual.getFechaCargoCambio());
         administrarCambiosMasivos.adicionaEstructuraCM2(parametroCambioMasivoActual.getCargoEstructura(), parametroCambioMasivoActual.getFechaCargoCambio());
         cambiosMasivosHechos();
      } else {
         RequestContext.getCurrentInstance().execute("PF('validacionCamposVacios').show()");
      }
   }

   public void deshacerCargo() {
      if (parametroCambioMasivoActual.getCargoEstructura() != null && parametroCambioMasivoActual.getFechaCargoCambio() != null) {
         administrarCambiosMasivos.undoAdicionaEstructuraCM2(parametroCambioMasivoActual.getCargoEstructura(), parametroCambioMasivoActual.getFechaCargoCambio());
         cambiosMasivosHechos();
      } else {
         RequestContext.getCurrentInstance().execute("PF('validacionCamposVacios').show()");
      }
   }

   public void procesarVacaciones() {
      if (parametroCambioMasivoActual.getVacaDias() != null && parametroCambioMasivoActual.getFechaVacaCambio() != null && parametroCambioMasivoActual.getFechaVacaPago() != null) {
         administrarCambiosMasivos.adicionaVacacionCM2(parametroCambioMasivoActual.getVacaDias(),
                 parametroCambioMasivoActual.getFechaVacaCambio(), parametroCambioMasivoActual.getFechaVacaPago());
         cambiosMasivosHechos();
      } else {
         RequestContext.getCurrentInstance().execute("PF('validacionCamposVacios').show()");
      }
   }

   public void deshacerVacaciones() {
      if (parametroCambioMasivoActual.getVacaDias() != null && parametroCambioMasivoActual.getFechaVacaCambio() != null && parametroCambioMasivoActual.getFechaVacaPago() != null) {
         System.out.println("ControlCambiosMasivos.DeshacerVacaciones() parametroCambioMasivoActual.getVacaDias(): " + parametroCambioMasivoActual.getVacaDias());
         administrarCambiosMasivos.undoAdicionaVacacionCM2(parametroCambioMasivoActual.getVacaDias(),
                 parametroCambioMasivoActual.getFechaVacaCambio(), parametroCambioMasivoActual.getFechaVacaPago());
         cambiosMasivosHechos();
      } else {
         RequestContext.getCurrentInstance().execute("PF('validacionCamposVacios').show()");
      }
   }

   public void procesarRetiros() {
      if (parametroCambioMasivoActual.getRetiIndemniza() != null && parametroCambioMasivoActual.getRetiMotivoDefinitiva() != null
              && parametroCambioMasivoActual.getRetiMotivoRetiro() != null && parametroCambioMasivoActual.getFechaRetiCambio() != null) {
         administrarCambiosMasivos.adicionaRetiroCM2(parametroCambioMasivoActual.getRetiIndemniza(), parametroCambioMasivoActual.getRetiMotivoDefinitiva(),
                 parametroCambioMasivoActual.getRetiMotivoRetiro(), parametroCambioMasivoActual.getFechaRetiCambio());
         cambiosMasivosHechos();
      } else {
         RequestContext.getCurrentInstance().execute("PF('validacionCamposVacios').show()");
      }
   }

   public void deshacerRetiros() {
      if (parametroCambioMasivoActual.getRetiIndemniza() != null && parametroCambioMasivoActual.getRetiMotivoDefinitiva() != null
              && parametroCambioMasivoActual.getRetiMotivoRetiro() != null && parametroCambioMasivoActual.getFechaRetiCambio() != null) {
         administrarCambiosMasivos.undoAdicionaRetiroCM2(parametroCambioMasivoActual.getRetiIndemniza(), parametroCambioMasivoActual.getRetiMotivoDefinitiva(),
                 parametroCambioMasivoActual.getRetiMotivoRetiro(), parametroCambioMasivoActual.getFechaRetiCambio());
         cambiosMasivosHechos();
      } else {
         RequestContext.getCurrentInstance().execute("PF('validacionCamposVacios').show()");
      }
   }

   public void procesarAfiliaciones() {
      if (parametroCambioMasivoActual.getAfiliaTipoEntidad() != null && parametroCambioMasivoActual.getAfiliaTerceroSucursal() != null
              && parametroCambioMasivoActual.getFechaAfiliaCambio() != null) {
         administrarCambiosMasivos.adicionaAfiliacionCM2(parametroCambioMasivoActual.getAfiliaTipoEntidad(),
                 parametroCambioMasivoActual.getAfiliaTerceroSucursal(), parametroCambioMasivoActual.getFechaAfiliaCambio());
         cambiosMasivosHechos();
      } else {
         RequestContext.getCurrentInstance().execute("PF('validacionCamposVacios').show()");
      }
   }

   public void deshacerAfiliaciones() {
      if (parametroCambioMasivoActual.getAfiliaTipoEntidad() != null && parametroCambioMasivoActual.getAfiliaTerceroSucursal() != null
              && parametroCambioMasivoActual.getFechaAfiliaCambio() != null) {
         administrarCambiosMasivos.undoAdicionaAfiliacionCM2(parametroCambioMasivoActual.getAfiliaTipoEntidad(),
                 parametroCambioMasivoActual.getAfiliaTerceroSucursal(), parametroCambioMasivoActual.getFechaAfiliaCambio());
         cambiosMasivosHechos();
      } else {
         RequestContext.getCurrentInstance().execute("PF('validacionCamposVacios').show()");
      }
   }

   public void procesarCentrosCostos() {
      if (parametroCambioMasivoActual.getLocaliEstructura() != null && parametroCambioMasivoActual.getFechaLocaliCambio() != null) {
         administrarCambiosMasivos.adicionaLocalizacionCM2(parametroCambioMasivoActual.getLocaliEstructura(), parametroCambioMasivoActual.getFechaLocaliCambio());
         cambiosMasivosHechos();
      } else {
         RequestContext.getCurrentInstance().execute("PF('validacionCamposVacios').show()");
      }
   }

   public void deshacerCentrosCostos() {
      if (parametroCambioMasivoActual.getLocaliEstructura() != null && parametroCambioMasivoActual.getFechaLocaliCambio() != null) {
         administrarCambiosMasivos.undoAdicionaLocalizacionCM2(parametroCambioMasivoActual.getLocaliEstructura(), parametroCambioMasivoActual.getFechaLocaliCambio());
         cambiosMasivosHechos();
      } else {
         RequestContext.getCurrentInstance().execute("PF('validacionCamposVacios').show()");
      }
   }

   public void procesarNovedades() {
      if (parametroCambioMasivoActual.getNoveTipo() != null && parametroCambioMasivoActual.getNoveConcepto() != null && parametroCambioMasivoActual.getNovePeriodicidad() != null
              && parametroCambioMasivoActual.getNoveFormula() != null && parametroCambioMasivoActual.getNoveValor() != null && parametroCambioMasivoActual.getFechaNoveCambioInicial() != null) {
         administrarCambiosMasivos.adicionaNovedadCM2(parametroCambioMasivoActual.getNoveTipo(), parametroCambioMasivoActual.getNoveConcepto(),
                 parametroCambioMasivoActual.getNovePeriodicidad(), parametroCambioMasivoActual.getNoveTercero(), parametroCambioMasivoActual.getNoveFormula(),
                 parametroCambioMasivoActual.getNoveValor(), parametroCambioMasivoActual.getNoveSaldo(), parametroCambioMasivoActual.getFechaNoveCambioInicial(),
                 parametroCambioMasivoActual.getFechaNoveCambioFinal(), parametroCambioMasivoActual.getNoveUnidadParteEntera(), parametroCambioMasivoActual.getNoveUnidadParteFraccion());
         cambiosMasivosHechos();
      } else {
         RequestContext.getCurrentInstance().execute("PF('validacionCamposVacios').show()");
      }
   }

   public void deshacerNovedades() {
      if (parametroCambioMasivoActual.getNoveTipo() != null && parametroCambioMasivoActual.getNoveConcepto() != null && parametroCambioMasivoActual.getNovePeriodicidad() != null
              && parametroCambioMasivoActual.getNoveFormula() != null && parametroCambioMasivoActual.getNoveValor() != null && parametroCambioMasivoActual.getFechaNoveCambioInicial() != null) {
         administrarCambiosMasivos.undoAdicionaNovedadCM2(parametroCambioMasivoActual.getNoveTipo(), parametroCambioMasivoActual.getNoveConcepto(),
                 parametroCambioMasivoActual.getNovePeriodicidad(), parametroCambioMasivoActual.getNoveTercero(), parametroCambioMasivoActual.getNoveFormula(),
                 parametroCambioMasivoActual.getNoveValor(), parametroCambioMasivoActual.getNoveSaldo(), parametroCambioMasivoActual.getFechaNoveCambioInicial(),
                 parametroCambioMasivoActual.getFechaNoveCambioFinal());
         cambiosMasivosHechos();
      } else {
         RequestContext.getCurrentInstance().execute("PF('validacionCamposVacios').show()");
      }
   }

   public void procesarSueldos() {
      if (parametroCambioMasivoActual.getSueldoMotivoCambioSueldo() != null && parametroCambioMasivoActual.getSueldoTipoSueldo() != null && parametroCambioMasivoActual.getSueldoUnidadPago() != null
              && parametroCambioMasivoActual.getSueldoValor() != null && parametroCambioMasivoActual.getFechaSueldoCambio() != null) {
         administrarCambiosMasivos.adicionaSueldoCM2(parametroCambioMasivoActual.getSueldoMotivoCambioSueldo(), parametroCambioMasivoActual.getSueldoTipoSueldo(),
                 parametroCambioMasivoActual.getSueldoUnidadPago(), parametroCambioMasivoActual.getSueldoValor(), parametroCambioMasivoActual.getFechaSueldoCambio());
         cambiosMasivosHechos();
      } else {
         RequestContext.getCurrentInstance().execute("PF('validacionCamposVacios').show()");
      }
   }

   public void deshacerSueldos() {
      if (parametroCambioMasivoActual.getSueldoMotivoCambioSueldo() != null && parametroCambioMasivoActual.getSueldoTipoSueldo() != null
              && parametroCambioMasivoActual.getSueldoUnidadPago() != null && parametroCambioMasivoActual.getSueldoValor() != null && parametroCambioMasivoActual.getFechaSueldoCambio() != null) {
         administrarCambiosMasivos.undoAdicionaSueldoCM2(parametroCambioMasivoActual.getSueldoMotivoCambioSueldo(), parametroCambioMasivoActual.getSueldoTipoSueldo(),
                 parametroCambioMasivoActual.getSueldoUnidadPago(), parametroCambioMasivoActual.getSueldoValor(), parametroCambioMasivoActual.getFechaSueldoCambio());
         cambiosMasivosHechos();
      } else {
         RequestContext.getCurrentInstance().execute("PF('validacionCamposVacios').show()");
      }
   }

   public void procesarReingresos() {
      if (parametroCambioMasivoActual.getTrans_fechaReingreso() != null && parametroCambioMasivoActual.getTrans_fechaFinContrato() != null) {
         administrarCambiosMasivos.adicionaReingresoCM2(parametroCambioMasivoActual.getTrans_fechaReingreso(), parametroCambioMasivoActual.getTrans_fechaFinContrato());
         cambiosMasivosHechos();
      } else {
         RequestContext.getCurrentInstance().execute("PF('validacionCamposVacios').show()");
      }
   }

   public void procesarEmplJefe() {
      if (parametroCambioMasivoActual.getTrans_SecEmplJefe() != null && parametroCambioMasivoActual.getTrans_fechaEmplJefe() != null) {
         administrarCambiosMasivos.adicionaEmplJefeCM2(parametroCambioMasivoActual.getTrans_SecEmplJefe(), parametroCambioMasivoActual.getTrans_fechaEmplJefe());
         cambiosMasivosHechos();
      } else {
         RequestContext.getCurrentInstance().execute("PF('validacionCamposVacios').show()");
      }
   }

   public void procesarAusentismos() {
      if (parametroCambioMasivoActual.getTrans_SecTipoAusentismo() != null && parametroCambioMasivoActual.getTrans_SecClase() != null
              && parametroCambioMasivoActual.getTrans_SecCausa() != null && parametroCambioMasivoActual.getTrans_dias() != null && parametroCambioMasivoActual.getTrans_horas() != null
              && parametroCambioMasivoActual.getTrans_fechaInicialAusent() != null && parametroCambioMasivoActual.getTrans_fechaFinalAusent() != null
              && parametroCambioMasivoActual.getTrans_fechaExpedicionAusent() != null && parametroCambioMasivoActual.getTrans_fechaInicialPagoAusent() != null
              && parametroCambioMasivoActual.getTrans_fechaFinalPagoAusent() != null && parametroCambioMasivoActual.getTrans_porcentaje() != null
              && parametroCambioMasivoActual.getTrans_baseLiquidacion() != null && parametroCambioMasivoActual.getTrans_formaLiquidacion() != null) {
         administrarCambiosMasivos.adicionaAusentismoCM2(parametroCambioMasivoActual.getTrans_SecTipoAusentismo(), parametroCambioMasivoActual.getTrans_SecClase(),
                 parametroCambioMasivoActual.getTrans_SecCausa(), parametroCambioMasivoActual.getTrans_dias(), parametroCambioMasivoActual.getTrans_horas(),
                 parametroCambioMasivoActual.getTrans_fechaInicialAusent(), parametroCambioMasivoActual.getTrans_fechaFinalAusent(),
                 parametroCambioMasivoActual.getTrans_fechaExpedicionAusent(), parametroCambioMasivoActual.getTrans_fechaInicialPagoAusent(),
                 parametroCambioMasivoActual.getTrans_fechaFinalPagoAusent(), parametroCambioMasivoActual.getTrans_porcentaje(),
                 parametroCambioMasivoActual.getTrans_baseLiquidacion(), parametroCambioMasivoActual.getTrans_formaLiquidacion());
         cambiosMasivosHechos();
      } else {
         RequestContext.getCurrentInstance().execute("PF('validacionCamposVacios').show()");
      }
   }

   public void deshacerAusentismos() {
      if (parametroCambioMasivoActual.getTrans_SecTipoAusentismo() != null && parametroCambioMasivoActual.getTrans_SecClase() != null
              && parametroCambioMasivoActual.getTrans_SecCausa() != null && parametroCambioMasivoActual.getTrans_dias() != null
              && parametroCambioMasivoActual.getTrans_fechaInicialAusent() != null && parametroCambioMasivoActual.getTrans_fechaFinalAusent() != null) {
         administrarCambiosMasivos.undoAdicionaAusentismoCM2(parametroCambioMasivoActual.getTrans_SecTipoAusentismo(), parametroCambioMasivoActual.getTrans_SecClase(),
                 parametroCambioMasivoActual.getTrans_SecCausa(), parametroCambioMasivoActual.getTrans_dias(),
                 parametroCambioMasivoActual.getTrans_fechaInicialAusent(), parametroCambioMasivoActual.getTrans_fechaFinalAusent());
         cambiosMasivosHechos();
      } else {
         RequestContext.getCurrentInstance().execute("PF('validacionCamposVacios').show()");
      }
   }

   public void procesarPapel() {
      if (parametroCambioMasivoActual.getTrans_secPapel() != null && parametroCambioMasivoActual.getTrans_fechaPapel() != null) {
         administrarCambiosMasivos.adicionaPapelCM2(parametroCambioMasivoActual.getTrans_secPapel(), parametroCambioMasivoActual.getTrans_fechaPapel());
         cambiosMasivosHechos();
      } else {
         RequestContext.getCurrentInstance().execute("PF('validacionCamposVacios').show()");
      }
   }

   public void seleccionarEstructura() {
      aceptar = true;
      if (lovEstructuras != null) {
         if (!lovEstructuras.isEmpty()) {
            if (estructuraSeleccionada != null) {
               if (panelActivo == 1) {
                  parametroCambioMasivoActual.setCargoEstructura(estructuraSeleccionada.getSecuencia());
                  parametroCambioMasivoActual.setStr_cargoEstructura(estructuraSeleccionada.getNombre());
                  RequestContext.getCurrentInstance().update("form:acordionPanel:str_cargoEstructura");
               } else if (panelActivo == 5) {
                  parametroCambioMasivoActual.setLocaliEstructura(estructuraSeleccionada.getSecuencia());
                  parametroCambioMasivoActual.setStr_localiEstructura(estructuraSeleccionada.getNombre());
                  RequestContext.getCurrentInstance().update("form:acordionPanel:str_localiEstructura");
               }
               if (guardado) {
                  guardado = false;
                  RequestContext.getCurrentInstance().update("form:ACEPTAR");
               }
            }
         }
      }
      filtroLovEstructuras = null;
      estructuraSeleccionada = null;
      RequestContext.getCurrentInstance().reset("form:lovEstructuras:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovEstructuras').clearFilters()");
      RequestContext.getCurrentInstance().update("form:aceptarE");
      RequestContext.getCurrentInstance().update("form:lovEstructuras");
      RequestContext.getCurrentInstance().update("form:infoRegistroEstructura");
      RequestContext.getCurrentInstance().update("form:EstructurasDialogo");
   }

   public void cancelarEstructura() {
      if (panelActivo == 1) {
         RequestContext.getCurrentInstance().update("form:acordionPanel:str_cargoEstructura");
      } else if (panelActivo == 5) {
         RequestContext.getCurrentInstance().update("form:acordionPanel:str_localiEstructura");
      }
      filtroLovEstructuras = null;
      estructuraSeleccionada = null;
      RequestContext.getCurrentInstance().reset("form:lovEstructuras:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovEstructuras').clearFilters()");
      RequestContext.getCurrentInstance().update("form:aceptarE");
      RequestContext.getCurrentInstance().update("form:lovEstructuras");
      RequestContext.getCurrentInstance().update("form:infoRegistroEstructura");
      RequestContext.getCurrentInstance().update("form:EstructurasDialogo");
   }

   /**
    *
    * @param t
    */
   public void autocompletarEstructura(String t) {
      aceptar = true;
      System.out.println("ControlCambiosMasivos.autocompletarEstructura() t: _" + t + "_   y panelActivo : " + panelActivo);
      if (panelActivo == 1) {
         if (t == null || "".equals(t) || " ".equals(t) || t.isEmpty()) {
            parametroCambioMasivoActual.setCargoEstructura(null);
            parametroCambioMasivoActual.setStr_cargoEstructura("");
            modificarParametros();
         } else if (lovEstructuras != null) {
            if (!lovEstructuras.isEmpty()) {
               int hayUno = 0;
               int indexObj = 0;
               for (int i = 0; i < lovEstructuras.size(); i++) {
                  if (lovEstructuras.get(i).getNombre().startsWith(t)) {
                     hayUno++;
                     indexObj = i;
                  }
               }
               if (hayUno == 1) {
                  parametroCambioMasivoActual.setCargoEstructura(lovEstructuras.get(indexObj).getSecuencia());
                  parametroCambioMasivoActual.setStr_cargoEstructura(lovEstructuras.get(indexObj).getNombre());
                  modificarParametros();
               } else {
                  hayUno = 0;
                  for (int i = 0; i < lovEstructuras.size(); i++) {
                     if (lovEstructuras.get(i).getSecuencia().equals(parametroCambioMasivoActual.getCargoEstructura())) {
                        parametroCambioMasivoActual.setStr_cargoEstructura(lovEstructuras.get(i).getNombre());
                        hayUno++;
                     }
                  }
                  if (hayUno != 1) {
                     parametroCambioMasivoActual.setCargoEstructura(null);
                     parametroCambioMasivoActual.setStr_cargoEstructura("");
                  }
                  RequestContext.getCurrentInstance().update("form:EstructurasDialogo");
                  RequestContext.getCurrentInstance().execute("PF('EstructurasDialogo').show()");
               }
            }
         }
         RequestContext.getCurrentInstance().update("form:acordionPanel:str_cargoEstructura");
      } else if (panelActivo == 5) {
         if (t == null || "".equals(t) || " ".equals(t) || t.isEmpty()) {
            parametroCambioMasivoActual.setLocaliEstructura(null);
            parametroCambioMasivoActual.setStr_localiEstructura("");
            modificarParametros();
         } else if (lovEstructuras != null) {
            if (!lovEstructuras.isEmpty()) {
               int hayUno = 0;
               int indexObj = 0;
               for (int i = 0; i < lovEstructuras.size(); i++) {
                  if (lovEstructuras.get(i).getNombre().startsWith(t)) {
                     hayUno++;
                     indexObj = i;
                  }
               }
               if (hayUno == 1) {
                  parametroCambioMasivoActual.setLocaliEstructura(lovEstructuras.get(indexObj).getSecuencia());
                  parametroCambioMasivoActual.setStr_localiEstructura(lovEstructuras.get(indexObj).getNombre());
                  modificarParametros();
               } else {
                  hayUno = 0;
                  for (int i = 0; i < lovEstructuras.size(); i++) {
                     if (lovEstructuras.get(i).getSecuencia().equals(parametroCambioMasivoActual.getLocaliEstructura())) {
                        parametroCambioMasivoActual.setStr_localiEstructura(lovEstructuras.get(i).getNombre());
                        hayUno++;
                     }
                  }
                  if (hayUno != 1) {
                     parametroCambioMasivoActual.setLocaliEstructura(null);
                     parametroCambioMasivoActual.setStr_localiEstructura("");
                  }
                  RequestContext.getCurrentInstance().update("form:EstructurasDialogo");
                  RequestContext.getCurrentInstance().execute("PF('EstructurasDialogo').show()");
               }
            }
         }
         RequestContext.getCurrentInstance().update("form:acordionPanel:str_localiEstructura");
      }
   }

   public void seleccionarPeriodicidades() {
      aceptar = true;
      if (lovPeriodicidades != null) {
         if (!lovPeriodicidades.isEmpty()) {
            if (periodicidadSeleccionada != null) {
               parametroCambioMasivoActual.setNovePeriodicidad(periodicidadSeleccionada.getSecuencia());
               parametroCambioMasivoActual.setStr_novePeriodicidad(periodicidadSeleccionada.getNombre());
               if (guardado) {
                  guardado = false;
                  RequestContext.getCurrentInstance().update("form:ACEPTAR");
               }
            }
         }
      }
      RequestContext.getCurrentInstance().update("form:acordionPanel:str_novePeriodicidad(");
      filtroLovPeriodicidades = null;
      periodicidadSeleccionada = null;

      RequestContext.getCurrentInstance().reset("form:lovPeriodicidades:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovPeriodicidades').clearFilters()");
      RequestContext.getCurrentInstance().update("form:aceptarPer");
      RequestContext.getCurrentInstance().update("form:lovPeriodicidades");
      RequestContext.getCurrentInstance().update("form:infoRegistroPeriodicidades");
      RequestContext.getCurrentInstance().update("form:PeriodicidadesDialogo");
   }

   public void cancelarPeriodicidades() {
      RequestContext.getCurrentInstance().update("form:acordionPanel:str_novePeriodicidad");
      filtroLovPeriodicidades = null;
      periodicidadSeleccionada = null;
      RequestContext.getCurrentInstance().reset("form:lovPeriodicidades:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovPeriodicidades').clearFilters()");
      RequestContext.getCurrentInstance().update("form:aceptarPer");
      RequestContext.getCurrentInstance().update("form:lovPeriodicidades");
      RequestContext.getCurrentInstance().update("form:infoRegistroPeriodicidades");
      RequestContext.getCurrentInstance().update("form:PeriodicidadesDialogo");
   }

   /**
    *
    * @param t
    */
   public void autocompletarPeriodicidades(String t) {
      aceptar = true;
      if (t == null || "".equals(t) || " ".equals(t) || t.isEmpty()) {
         parametroCambioMasivoActual.setNovePeriodicidad(null);
         parametroCambioMasivoActual.setStr_novePeriodicidad("");
         modificarParametros();
      } else if (lovPeriodicidades != null) {
         if (!lovPeriodicidades.isEmpty()) {
            int hayUno = 0;
            int indexObj = 0;
            for (int i = 0; i < lovPeriodicidades.size(); i++) {
               if (lovPeriodicidades.get(i).getNombre().startsWith(t)) {
                  hayUno++;
                  indexObj = i;
               }
            }
            if (hayUno == 1) {
               parametroCambioMasivoActual.setNovePeriodicidad(lovPeriodicidades.get(indexObj).getSecuencia());
               parametroCambioMasivoActual.setStr_novePeriodicidad(lovPeriodicidades.get(indexObj).getNombre());
               modificarParametros();
            } else {
               hayUno = 0;
               for (int i = 0; i < lovPeriodicidades.size(); i++) {
                  if (lovPeriodicidades.get(i).getSecuencia().equals(parametroCambioMasivoActual.getNovePeriodicidad())) {
                     parametroCambioMasivoActual.setStr_novePeriodicidad(lovPeriodicidades.get(i).getNombre());
                     hayUno++;
                  }
               }
               if (hayUno != 1) {
                  parametroCambioMasivoActual.setNovePeriodicidad(null);
                  parametroCambioMasivoActual.setStr_novePeriodicidad("");
               }
               RequestContext.getCurrentInstance().update("form:PeriodicidadesDialogo");
               RequestContext.getCurrentInstance().execute("PF('PeriodicidadesDialogo').show()");
            }
         }
      }
      RequestContext.getCurrentInstance().update("form:acordionPanel:str_novePeriodicidad");
   }

   public void seleccionarConceptos() {
      aceptar = true;
      if (lovConceptos != null) {
         if (!lovConceptos.isEmpty()) {
            if (conceptoSeleccionado != null) {
               parametroCambioMasivoActual.setNoveConcepto(conceptoSeleccionado.getSecuencia());
               parametroCambioMasivoActual.setStr_noveConcepto(conceptoSeleccionado.getDescripcion());
               if (guardado) {
                  guardado = false;
                  RequestContext.getCurrentInstance().update("form:ACEPTAR");
               }
            }
         }
      }
      RequestContext.getCurrentInstance().update("form:acordionPanel:str_noveConcepto");
      filtroLovConceptos = null;
      conceptoSeleccionado = null;
      RequestContext.getCurrentInstance().reset("form:lovConceptos:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovConceptos').clearFilters()");
      RequestContext.getCurrentInstance().update("form:aceptarCon");
      RequestContext.getCurrentInstance().update("form:lovConceptos");
      RequestContext.getCurrentInstance().update("form:infoRegistroConceptos");
      RequestContext.getCurrentInstance().update("form:ConceptosDialogo");
   }

   public void cancelarConceptos() {
      RequestContext.getCurrentInstance().update("form:acordionPanel:str_noveConcepto");
      filtroLovConceptos = null;
      conceptoSeleccionado = null;
      RequestContext.getCurrentInstance().reset("form:lovConceptos:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovConceptos').clearFilters()");
      RequestContext.getCurrentInstance().update("form:aceptarCon");
      RequestContext.getCurrentInstance().update("form:lovConceptos");
      RequestContext.getCurrentInstance().update("form:infoRegistroConceptos");
      RequestContext.getCurrentInstance().update("form:ConceptosDialogo");
   }

   /**
    *
    * @param t
    */
   public void autocompletarConceptos(String t) {
      aceptar = true;
      if (t == null || "".equals(t) || " ".equals(t) || t.isEmpty()) {
         parametroCambioMasivoActual.setNoveConcepto(null);
         parametroCambioMasivoActual.setStr_noveConcepto("");
         modificarParametros();
      } else if (lovConceptos != null) {
         if (!lovConceptos.isEmpty()) {
            int hayUno = 0;
            int indexObj = 0;
            for (int i = 0; i < lovConceptos.size(); i++) {
               if (lovConceptos.get(i).getDescripcion().startsWith(t)) {
                  hayUno++;
                  indexObj = i;
               }
            }
            if (hayUno == 1) {
               parametroCambioMasivoActual.setNoveConcepto(lovConceptos.get(indexObj).getSecuencia());
               parametroCambioMasivoActual.setStr_noveConcepto(lovConceptos.get(indexObj).getDescripcion());
               modificarParametros();
            } else {
               hayUno = 0;
               for (int i = 0; i < lovConceptos.size(); i++) {
                  if (lovConceptos.get(i).getSecuencia().equals(parametroCambioMasivoActual.getNoveConcepto())) {
                     parametroCambioMasivoActual.setStr_noveConcepto(lovConceptos.get(i).getDescripcion());
                     hayUno++;
                  }
               }
               if (hayUno != 1) {
                  parametroCambioMasivoActual.setNoveConcepto(null);
                  parametroCambioMasivoActual.setStr_noveConcepto("");
               }
               RequestContext.getCurrentInstance().update("form:ConceptosDialogo");
               RequestContext.getCurrentInstance().execute("PF('ConceptosDialogo').show()");
            }
         }
      }
      RequestContext.getCurrentInstance().update("form:acordionPanel:str_noveConcepto");
   }

   public void seleccionarFormulas() {
      aceptar = true;
      if (lovFormulas != null) {
         if (!lovFormulas.isEmpty()) {
            if (formulaSeleccionada != null) {
               parametroCambioMasivoActual.setNoveFormula(formulaSeleccionada.getSecuencia());
               parametroCambioMasivoActual.setStr_noveFormula(formulaSeleccionada.getNombrelargo());
               if (guardado) {
                  guardado = false;
                  RequestContext.getCurrentInstance().update("form:ACEPTAR");
               }
            }
         }
      }
      RequestContext.getCurrentInstance().update("form:acordionPanel:str_noveFormula");
      filtroLovFormulas = null;
      formulaSeleccionada = null;
      RequestContext.getCurrentInstance().reset("form:lovFormulas:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovFormulas').clearFilters()");
      RequestContext.getCurrentInstance().update("form:aceptarF");
      RequestContext.getCurrentInstance().update("form:lovFormulas");
      RequestContext.getCurrentInstance().update("form:infoRegistroFormulas");
      RequestContext.getCurrentInstance().update("form:FormulasDialogo");
   }

   public void cancelarFormulas() {
      RequestContext.getCurrentInstance().update("form:acordionPanel:str_noveFormula");
      filtroLovFormulas = null;
      formulaSeleccionada = null;
      RequestContext.getCurrentInstance().reset("form:lovFormulas:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovFormulas').clearFilters()");
      RequestContext.getCurrentInstance().update("form:aceptarF");
      RequestContext.getCurrentInstance().update("form:lovFormulas");
      RequestContext.getCurrentInstance().update("form:infoRegistroFormulas");
      RequestContext.getCurrentInstance().update("form:FormulasDialogo");
   }

   /**
    *
    * @param t
    */
   public void autocompletarFormulas(String t) {
      aceptar = true;
      if (t == null || "".equals(t) || " ".equals(t) || t.isEmpty()) {
         parametroCambioMasivoActual.setNoveFormula(null);
         parametroCambioMasivoActual.setStr_noveFormula("");
         modificarParametros();
      } else if (lovFormulas != null) {
         if (!lovFormulas.isEmpty()) {
            int hayUno = 0;
            int indexObj = 0;
            for (int i = 0; i < lovFormulas.size(); i++) {
               if (lovFormulas.get(i).getNombrelargo().startsWith(t)) {
                  hayUno++;
                  indexObj = i;
               }
            }
            if (hayUno == 1) {
               parametroCambioMasivoActual.setNoveFormula(lovFormulas.get(indexObj).getSecuencia());
               parametroCambioMasivoActual.setStr_noveFormula(lovFormulas.get(indexObj).getNombrelargo());
               modificarParametros();
            } else {
               hayUno = 0;
               for (int i = 0; i < lovFormulas.size(); i++) {
                  if (lovFormulas.get(i).getSecuencia().equals(parametroCambioMasivoActual.getNoveFormula())) {
                     parametroCambioMasivoActual.setStr_noveFormula(lovFormulas.get(i).getNombrelargo());
                     hayUno++;
                  }
               }
               if (hayUno != 1) {
                  parametroCambioMasivoActual.setNoveFormula(null);
                  parametroCambioMasivoActual.setStr_noveFormula("");
               }
               RequestContext.getCurrentInstance().update("form:FormulasDialogo");
               RequestContext.getCurrentInstance().execute("PF('FormulasDialogo').show()");
            }
         }
      }
      RequestContext.getCurrentInstance().update("form:acordionPanel:str_noveFormula");
   }

   public void seleccionarTerceros() {
      aceptar = true;
      if (lovTerceros != null) {
         if (!lovTerceros.isEmpty()) {
            if (terceroSeleccionado != null) {
               parametroCambioMasivoActual.setNoveTercero(terceroSeleccionado.getSecuencia());
               parametroCambioMasivoActual.setStr_noveTercero(terceroSeleccionado.getNombre());
               if (guardado) {
                  guardado = false;
                  RequestContext.getCurrentInstance().update("form:ACEPTAR");
               }
            }
         }
      }
      RequestContext.getCurrentInstance().update("form:acordionPanel:str_noveTercero");
      filtroLovTerceros = null;
      terceroSeleccionado = null;
      RequestContext.getCurrentInstance().reset("form:lovTerceros:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovTerceros').clearFilters()");
      RequestContext.getCurrentInstance().update("form:aceptarT");
      RequestContext.getCurrentInstance().update("form:lovTerceros");
      RequestContext.getCurrentInstance().update("form:infoRegistroTerceros");
      RequestContext.getCurrentInstance().update("form:TercerosDialogo");
   }

   public void cancelarTerceros() {
      RequestContext.getCurrentInstance().update("form:acordionPanel:str_noveTercero");
      filtroLovTerceros = null;
      terceroSeleccionado = null;
      RequestContext.getCurrentInstance().reset("form:lovTerceros:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovTerceros').clearFilters()");
      RequestContext.getCurrentInstance().update("form:aceptarT");
      RequestContext.getCurrentInstance().update("form:lovTerceros");
      RequestContext.getCurrentInstance().update("form:infoRegistroTerceros");
      RequestContext.getCurrentInstance().update("form:TercerosDialogo");
   }

   /**
    *
    * @param t
    */
   public void autocompletarTerceros(String t) {
      aceptar = true;
      if (t == null || "".equals(t) || " ".equals(t) || t.isEmpty()) {
         parametroCambioMasivoActual.setNoveTercero(null);
         parametroCambioMasivoActual.setStr_noveTercero("");
         modificarParametros();
      } else if (lovTerceros != null) {
         if (!lovTerceros.isEmpty()) {
            int hayUno = 0;
            int indexObj = 0;
            for (int i = 0; i < lovTerceros.size(); i++) {
               if (lovTerceros.get(i).getNombre().startsWith(t)) {
                  hayUno++;
                  indexObj = i;
               }
            }
            if (hayUno == 1) {
               parametroCambioMasivoActual.setNoveTercero(lovTerceros.get(indexObj).getSecuencia());
               parametroCambioMasivoActual.setStr_noveTercero(lovTerceros.get(indexObj).getNombre());
               modificarParametros();
            } else {
               hayUno = 0;
               for (int i = 0; i < lovTerceros.size(); i++) {
                  if (lovTerceros.get(i).getSecuencia().equals(parametroCambioMasivoActual.getNoveTercero())) {
                     parametroCambioMasivoActual.setStr_noveTercero(lovTerceros.get(i).getNombre());
                     hayUno++;
                  }
               }
               if (hayUno != 1) {
                  parametroCambioMasivoActual.setNoveTercero(null);
                  parametroCambioMasivoActual.setStr_noveTercero("");
               }
               RequestContext.getCurrentInstance().update("form:TercerosDialogo");
               RequestContext.getCurrentInstance().execute("PF('TercerosDialogo').show()");
            }
         }
      }
      RequestContext.getCurrentInstance().update("form:acordionPanel:str_noveTercero");
   }

   public void seleccionarMotivosSueldos() {
      aceptar = true;
      if (lovMotivosSueldos != null) {
         if (!lovMotivosSueldos.isEmpty()) {
            if (motivoSueldoSeleccionado != null) {
               parametroCambioMasivoActual.setSueldoMotivoCambioSueldo(motivoSueldoSeleccionado.getSecuencia());
               parametroCambioMasivoActual.setStr_sueldoMotivoCambioSueldo(motivoSueldoSeleccionado.getNombre());
               if (guardado) {
                  guardado = false;
                  RequestContext.getCurrentInstance().update("form:ACEPTAR");
               }
            }
         }
      }
      RequestContext.getCurrentInstance().update("form:acordionPanel:str_sueldoMotivoCambioSueldo");
      filtroLovMotivosSueldos = null;
      motivoSueldoSeleccionado = null;
      RequestContext.getCurrentInstance().reset("form:lovMotivosSueldos:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovMotivosSueldos').clearFilters()");
      RequestContext.getCurrentInstance().update("form:aceptarMotS");
      RequestContext.getCurrentInstance().update("form:lovMotivosSueldos");
      RequestContext.getCurrentInstance().update("form:infoRegistroMotivosSueldos");
      RequestContext.getCurrentInstance().update("form:MotivosSueldosDialogo");
   }

   public void cancelarMotivosSueldos() {
      RequestContext.getCurrentInstance().update("form:acordionPanel:str_sueldoMotivoCambioSueldo");
      filtroLovMotivosSueldos = null;
      motivoSueldoSeleccionado = null;
      RequestContext.getCurrentInstance().reset("form:lovMotivosSueldos:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovMotivosSueldos').clearFilters()");
      RequestContext.getCurrentInstance().update("form:aceptarMotS");
      RequestContext.getCurrentInstance().update("form:lovMotivosSueldos");
      RequestContext.getCurrentInstance().update("form:infoRegistroMotivosSueldos");
      RequestContext.getCurrentInstance().update("form:MotivosSueldosDialogo");
   }

   /**
    *
    * @param t
    */
   public void autocompletarMotivosSueldos(String t) {
      aceptar = true;
      if (t == null || "".equals(t) || " ".equals(t) || t.isEmpty()) {
         parametroCambioMasivoActual.setSueldoMotivoCambioSueldo(null);
         parametroCambioMasivoActual.setStr_sueldoMotivoCambioSueldo("");
         modificarParametros();
      } else if (lovMotivosSueldos != null) {
         if (!lovMotivosSueldos.isEmpty()) {
            int hayUno = 0;
            int indexObj = 0;
            for (int i = 0; i < lovMotivosSueldos.size(); i++) {
               if (lovMotivosSueldos.get(i).getNombre().startsWith(t)) {
                  hayUno++;
                  indexObj = i;
               }
            }
            if (hayUno == 1) {
               parametroCambioMasivoActual.setSueldoMotivoCambioSueldo(lovMotivosSueldos.get(indexObj).getSecuencia());
               parametroCambioMasivoActual.setStr_sueldoMotivoCambioSueldo(lovMotivosSueldos.get(indexObj).getNombre());
               modificarParametros();
            } else {
               hayUno = 0;
               for (int i = 0; i < lovMotivosSueldos.size(); i++) {
                  if (lovMotivosSueldos.get(i).getSecuencia().equals(parametroCambioMasivoActual.getSueldoMotivoCambioSueldo())) {
                     parametroCambioMasivoActual.setStr_sueldoMotivoCambioSueldo(lovMotivosSueldos.get(i).getNombre());
                     hayUno++;
                  }
               }
               if (hayUno != 1) {
                  parametroCambioMasivoActual.setSueldoMotivoCambioSueldo(null);
                  parametroCambioMasivoActual.setStr_sueldoMotivoCambioSueldo("");
               }
               RequestContext.getCurrentInstance().update("form:MotivosSueldosDialogo");
               RequestContext.getCurrentInstance().execute("PF('MotivosSueldosDialogo').show()");
            }
         }
      }
      RequestContext.getCurrentInstance().update("form:acordionPanel:str_sueldoMotivoCambioSueldo");
   }

   public void seleccionarTiposSueldos() {
      aceptar = true;
      if (lovTiposSueldos != null) {
         if (!lovTiposSueldos.isEmpty()) {
            if (tipoSueldoSeleccionado != null) {
               parametroCambioMasivoActual.setSueldoTipoSueldo(tipoSueldoSeleccionado.getSecuencia());
               parametroCambioMasivoActual.setStr_sueldoTipoSueldo(tipoSueldoSeleccionado.getDescripcion());
               if (guardado) {
                  guardado = false;
                  RequestContext.getCurrentInstance().update("form:ACEPTAR");
               }
            }
         }
      }
      RequestContext.getCurrentInstance().update("form:acordionPanel:str_sueldoTipoSueldo");
      filtroLovTiposSueldos = null;
      tipoSueldoSeleccionado = null;
      RequestContext.getCurrentInstance().reset("form:lovTiposSueldos:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovTiposSueldos').clearFilters()");
      RequestContext.getCurrentInstance().update("form:aceptarTSu");
      RequestContext.getCurrentInstance().update("form:lovTiposSueldos");
      RequestContext.getCurrentInstance().update("form:infoRegistroTiposSueldos");
      RequestContext.getCurrentInstance().update("form:TiposSueldosDialogo");
   }

   public void cancelarTiposSueldos() {
      RequestContext.getCurrentInstance().update("form:acordionPanel:str_sueldoTipoSueldo");
      filtroLovTiposSueldos = null;
      tipoSueldoSeleccionado = null;
      RequestContext.getCurrentInstance().reset("form:lovTiposSueldos:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovTiposSueldos').clearFilters()");
      RequestContext.getCurrentInstance().update("form:aceptarTSu");
      RequestContext.getCurrentInstance().update("form:lovTiposSueldos");
      RequestContext.getCurrentInstance().update("form:infoRegistroTiposSueldos");
      RequestContext.getCurrentInstance().update("form:TiposSueldosDialogo");
   }

   /**
    *
    * @param t
    */
   public void autocompletarTiposSueldos(String t) {
      aceptar = true;
      if (t == null || "".equals(t) || " ".equals(t) || t.isEmpty()) {
         parametroCambioMasivoActual.setSueldoTipoSueldo(null);
         parametroCambioMasivoActual.setStr_sueldoTipoSueldo("");
         modificarParametros();
      } else if (lovTiposSueldos != null) {
         if (!lovTiposSueldos.isEmpty()) {
            int hayUno = 0;
            int indexObj = 0;
            for (int i = 0; i < lovTiposSueldos.size(); i++) {
               if (lovTiposSueldos.get(i).getDescripcion().startsWith(t)) {
                  hayUno++;
                  indexObj = i;
               }
            }
            if (hayUno == 1) {
               parametroCambioMasivoActual.setSueldoTipoSueldo(lovTiposSueldos.get(indexObj).getSecuencia());
               parametroCambioMasivoActual.setStr_sueldoTipoSueldo(lovTiposSueldos.get(indexObj).getDescripcion());
               modificarParametros();
            } else {
               hayUno = 0;
               for (int i = 0; i < lovTiposSueldos.size(); i++) {
                  if (lovTiposSueldos.get(i).getSecuencia().equals(parametroCambioMasivoActual.getSueldoTipoSueldo())) {
                     parametroCambioMasivoActual.setStr_sueldoTipoSueldo(lovTiposSueldos.get(i).getDescripcion());
                     hayUno++;
                  }
               }
               if (hayUno != 1) {
                  parametroCambioMasivoActual.setSueldoTipoSueldo(null);
                  parametroCambioMasivoActual.setStr_sueldoTipoSueldo("");
               }
               RequestContext.getCurrentInstance().update("form:TiposSueldosDialogo");
               RequestContext.getCurrentInstance().execute("PF('TiposSueldosDialogo').show()");
            }
         }
      }
      RequestContext.getCurrentInstance().update("form:acordionPanel:str_sueldoTipoSueldo");
   }

   public void seleccionarTiposausentismos() {
      aceptar = true;
      if (lovTiposausentismos != null) {
         if (!lovTiposausentismos.isEmpty()) {
            if (tipoausentismoSeleccionado != null) {
               parametroCambioMasivoActual.setTrans_SecTipoAusentismo(tipoausentismoSeleccionado.getSecuencia());
               parametroCambioMasivoActual.setTrans_tipoAusentismo(tipoausentismoSeleccionado.getDescripcion());
               if (guardado) {
                  guardado = false;
                  RequestContext.getCurrentInstance().update("form:ACEPTAR");
               }
            }
         }
      }
      RequestContext.getCurrentInstance().update("form:acordionPanel:trans_tipoAusentismo");
      filtroLovTiposausentismos = null;
      tipoausentismoSeleccionado = null;
      RequestContext.getCurrentInstance().reset("form:lovTiposausentismos:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovTiposausentismos').clearFilters()");
      RequestContext.getCurrentInstance().update("form:aceptarTAu");
      RequestContext.getCurrentInstance().update("form:lovTiposausentismos");
      RequestContext.getCurrentInstance().update("form:infoRegistroTiposausentismos");
      RequestContext.getCurrentInstance().update("form:TiposausentismosDialogo");
   }

   public void cancelarTiposausentismos() {
      RequestContext.getCurrentInstance().update("form:acordionPanel:trans_tipoAusentismo");
      filtroLovTiposausentismos = null;
      tipoausentismoSeleccionado = null;
      RequestContext.getCurrentInstance().reset("form:lovTiposausentismos:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovTiposausentismos').clearFilters()");
      RequestContext.getCurrentInstance().update("form:aceptarTAu");
      RequestContext.getCurrentInstance().update("form:lovTiposausentismos");
      RequestContext.getCurrentInstance().update("form:infoRegistroTiposausentismos");
      RequestContext.getCurrentInstance().update("form:TiposausentismosDialogo");
   }

   /**
    *
    * @param t
    */
   public void autocompletarTiposausentismos(String t) {
      aceptar = true;
      if (t == null || "".equals(t) || " ".equals(t) || t.isEmpty()) {
         parametroCambioMasivoActual.setTrans_SecTipoAusentismo(null);
         parametroCambioMasivoActual.setTrans_tipoAusentismo("");
         modificarParametros();
      } else if (lovTiposausentismos != null) {
         if (!lovTiposausentismos.isEmpty()) {
            int hayUno = 0;
            int indexObj = 0;
            for (int i = 0; i < lovTiposausentismos.size(); i++) {
               if (lovTiposausentismos.get(i).getDescripcion().startsWith(t)) {
                  hayUno++;
                  indexObj = i;
               }
            }
            if (hayUno == 1) {
               parametroCambioMasivoActual.setTrans_SecTipoAusentismo(lovTiposausentismos.get(indexObj).getSecuencia());
               parametroCambioMasivoActual.setTrans_tipoAusentismo(lovTiposausentismos.get(indexObj).getDescripcion());
               modificarParametros();
            } else {
               parametroCambioMasivoActual.setTrans_SecTipoAusentismo(null);
               parametroCambioMasivoActual.setTrans_tipoAusentismo("");
            }
            RequestContext.getCurrentInstance().update("form:TiposausentismosDialogo");
            RequestContext.getCurrentInstance().execute("PF('TiposausentismosDialogo').show()");
         }
      }
      RequestContext.getCurrentInstance().update("form:acordionPanel:trans_tipoAusentismo");
   }

   public void seleccionarUnidades() {
      aceptar = true;
      if (lovUnidades != null) {
         if (!lovUnidades.isEmpty()) {
            if (unidadSeleccionada != null) {
               parametroCambioMasivoActual.setSueldoUnidadPago(unidadSeleccionada.getSecuencia());
               parametroCambioMasivoActual.setStr_sueldoUnidadPago(unidadSeleccionada.getNombre());
               if (guardado) {
                  guardado = false;
                  RequestContext.getCurrentInstance().update("form:ACEPTAR");
               }
            }
         }
      }
      RequestContext.getCurrentInstance().update("form:acordionPanel:str_sueldoUnidadPago");
      filtroLovUnidades = null;
      unidadSeleccionada = null;
      RequestContext.getCurrentInstance().reset("form:lovUnidades:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovUnidades').clearFilters()");
      RequestContext.getCurrentInstance().update("form:aceptarU");
      RequestContext.getCurrentInstance().update("form:lovUnidades");
      RequestContext.getCurrentInstance().update("form:infoRegistroUnidades");
      RequestContext.getCurrentInstance().update("form:UnidadesDialogo");
   }

   public void cancelarUnidades() {
      RequestContext.getCurrentInstance().update("form:acordionPanel:str_sueldoUnidadPago");
      filtroLovUnidades = null;
      unidadSeleccionada = null;
      RequestContext.getCurrentInstance().reset("form:lovUnidades:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovUnidades').clearFilters()");
      RequestContext.getCurrentInstance().update("form:aceptarU");
      RequestContext.getCurrentInstance().update("form:lovUnidades");
      RequestContext.getCurrentInstance().update("form:infoRegistroUnidades");
      RequestContext.getCurrentInstance().update("form:UnidadesDialogo");
   }

   /**
    *
    * @param t
    */
   public void autocompletarUnidades(String t) {
      aceptar = true;
      if (t == null || "".equals(t) || " ".equals(t) || t.isEmpty()) {
         parametroCambioMasivoActual.setSueldoUnidadPago(null);
         parametroCambioMasivoActual.setStr_sueldoUnidadPago("");
         modificarParametros();
      } else if (lovUnidades != null) {
         if (!lovUnidades.isEmpty()) {
            int hayUno = 0;
            int indexObj = 0;
            for (int i = 0; i < lovUnidades.size(); i++) {
               if (lovUnidades.get(i).getNombre().startsWith(t)) {
                  hayUno++;
                  indexObj = i;
               }
            }
            if (hayUno == 1) {
               parametroCambioMasivoActual.setSueldoUnidadPago(lovUnidades.get(indexObj).getSecuencia());
               parametroCambioMasivoActual.setStr_sueldoUnidadPago(lovUnidades.get(indexObj).getNombre());
               modificarParametros();
            } else {
               hayUno = 0;
               for (int i = 0; i < lovUnidades.size(); i++) {
                  if (lovUnidades.get(i).getSecuencia().equals(parametroCambioMasivoActual.getSueldoUnidadPago())) {
                     parametroCambioMasivoActual.setStr_sueldoUnidadPago(lovUnidades.get(i).getNombre());
                     hayUno++;
                  }
               }
               if (hayUno != 1) {
                  parametroCambioMasivoActual.setSueldoUnidadPago(null);
                  parametroCambioMasivoActual.setStr_sueldoUnidadPago("");
               }
               RequestContext.getCurrentInstance().update("form:UnidadesDialogo");
               RequestContext.getCurrentInstance().execute("PF('UnidadesDialogo').show()");
            }
         }
      }
      RequestContext.getCurrentInstance().update("form:acordionPanel:str_sueldoUnidadPago");
   }

   public void seleccionarEmpleados() {
      aceptar = true;
      if (lovEmpleados != null) {
         if (!lovEmpleados.isEmpty()) {
            if (empleadoSeleccionado != null) {
               parametroCambioMasivoActual.setTrans_SecEmplJefe(empleadoSeleccionado.getSecuencia());
               parametroCambioMasivoActual.setTrans_nombreJefe(empleadoSeleccionado.getPersona().getNombreCompleto());
               if (guardado) {
                  guardado = false;
                  RequestContext.getCurrentInstance().update("form:ACEPTAR");
               }
            }
         }
      }
      RequestContext.getCurrentInstance().update("form:acordionPanel:trans_nombreJefe");
      filtroLovEmpleados = null;
      empleadoSeleccionado = null;
      RequestContext.getCurrentInstance().reset("form:lovEmpleados:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovEmpleados').clearFilters()");
      RequestContext.getCurrentInstance().update("form:aceptarEmpl");
      RequestContext.getCurrentInstance().update("form:lovEmpleados");
      RequestContext.getCurrentInstance().update("form:infoRegistroEmpleados");
      RequestContext.getCurrentInstance().update("form:EmpleadosDialogo");
   }

   public void cancelarEmpleados() {
      RequestContext.getCurrentInstance().update("form:acordionPanel:trans_nombreJefe");
      filtroLovEmpleados = null;
      empleadoSeleccionado = null;
      RequestContext.getCurrentInstance().reset("form:lovEmpleados:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovEmpleados').clearFilters()");
      RequestContext.getCurrentInstance().update("form:aceptarEmpl");
      RequestContext.getCurrentInstance().update("form:lovEmpleados");
      RequestContext.getCurrentInstance().update("form:infoRegistroEmpleados");
      RequestContext.getCurrentInstance().update("form:EmpleadosDialogo");
   }

   /**
    *
    * @param t
    */
   public void autocompletarEmpleados(String t) {
      aceptar = true;
      if (t == null || "".equals(t) || " ".equals(t) || t.isEmpty()) {
         parametroCambioMasivoActual.setTrans_SecEmplJefe(null);
         parametroCambioMasivoActual.setTrans_nombreJefe("");
         modificarParametros();
      } else if (lovEmpleados != null) {
         if (!lovEmpleados.isEmpty()) {
            int hayUno = 0;
            int indexObj = 0;
            for (int i = 0; i < lovEmpleados.size(); i++) {
               if (lovEmpleados.get(i).getPersona().getNombreCompleto().startsWith(t)) {
                  hayUno++;
                  indexObj = i;
               }
            }
            if (hayUno == 1) {
               parametroCambioMasivoActual.setTrans_SecEmplJefe(lovEmpleados.get(indexObj).getSecuencia());
               parametroCambioMasivoActual.setTrans_nombreJefe(lovEmpleados.get(indexObj).getPersona().getNombreCompleto());
               modificarParametros();
            } else {
               hayUno = 0;
               for (int i = 0; i < lovEmpleados.size(); i++) {
                  if (lovEmpleados.get(i).getSecuencia().equals(parametroCambioMasivoActual.getTrans_nombreJefe())) {
                     parametroCambioMasivoActual.setTrans_nombreJefe(lovEmpleados.get(i).getPersona().getNombreCompleto());
                     hayUno++;
                  }
               }
               if (hayUno != 1) {
                  parametroCambioMasivoActual.setTrans_SecEmplJefe(null);
                  parametroCambioMasivoActual.setTrans_nombreJefe("");
               }
               RequestContext.getCurrentInstance().update("form:EmpleadosDialogo");
               RequestContext.getCurrentInstance().execute("PF('EmpleadosDialogo').show()");
            }
         }
      }
      RequestContext.getCurrentInstance().update("form:acordionPanel:trans_nombreJefe");
   }

   public void seleccionarPapeles() {
      aceptar = true;
      if (lovPapeles != null) {
         if (!lovPapeles.isEmpty()) {
            if (papelSeleccionado != null) {
               parametroCambioMasivoActual.setTrans_secPapel(papelSeleccionado.getSecuencia());
               parametroCambioMasivoActual.setTrans_papel(papelSeleccionado.getDescripcion());
               if (guardado) {
                  guardado = false;
                  RequestContext.getCurrentInstance().update("form:ACEPTAR");
               }
            }
         }
      }
      RequestContext.getCurrentInstance().update("form:acordionPanel:trans_papel");
      filtroLovPapeles = null;
      papelSeleccionado = null;
      RequestContext.getCurrentInstance().reset("form:lovPapeles:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovPapeles').clearFilters()");
      RequestContext.getCurrentInstance().update("form:aceptarPap");
      RequestContext.getCurrentInstance().update("form:lovPapeles");
      RequestContext.getCurrentInstance().update("form:infoRegistroPapeles");
      RequestContext.getCurrentInstance().update("form:PapelesDialogo");
   }

   public void cancelarPapeles() {
      RequestContext.getCurrentInstance().update("form:acordionPanel:trans_papel");
      filtroLovPapeles = null;
      papelSeleccionado = null;
      RequestContext.getCurrentInstance().reset("form:lovPapeles:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovPapeles').clearFilters()");
      RequestContext.getCurrentInstance().update("form:aceptarPap");
      RequestContext.getCurrentInstance().update("form:lovPapeles");
      RequestContext.getCurrentInstance().update("form:infoRegistroPapeles");
      RequestContext.getCurrentInstance().update("form:PapelesDialogo");
   }

   /**
    *
    * @param t
    */
   public void autocompletarPapeles(String t) {
      aceptar = true;
      if (t == null || "".equals(t) || " ".equals(t) || t.isEmpty()) {
         parametroCambioMasivoActual.setTrans_secPapel(null);
         parametroCambioMasivoActual.setTrans_papel("");
         modificarParametros();
      } else if (lovPapeles != null) {
         if (!lovPapeles.isEmpty()) {
            int hayUno = 0;
            int indexObj = 0;
            for (int i = 0; i < lovPapeles.size(); i++) {
               if (lovPapeles.get(i).getDescripcion().startsWith(t)) {
                  hayUno++;
                  indexObj = i;
               }
            }
            if (hayUno == 1) {
               parametroCambioMasivoActual.setTrans_secPapel(lovPapeles.get(indexObj).getSecuencia());
               parametroCambioMasivoActual.setTrans_papel(lovPapeles.get(indexObj).getDescripcion());
               modificarParametros();
            } else {
               hayUno = 0;
               for (int i = 0; i < lovPapeles.size(); i++) {
                  if (lovPapeles.get(i).getSecuencia().equals(parametroCambioMasivoActual.getTrans_papel())) {
                     parametroCambioMasivoActual.setTrans_papel(lovPapeles.get(i).getDescripcion());
                     hayUno++;
                  }
               }
               if (hayUno != 1) {
                  parametroCambioMasivoActual.setTrans_secPapel(null);
                  parametroCambioMasivoActual.setTrans_papel("");
               }
               RequestContext.getCurrentInstance().update("form:PapelesDialogo");
               RequestContext.getCurrentInstance().execute("PF('PapelesDialogo').show()");
            }
         }
      }
      RequestContext.getCurrentInstance().update("form:acordionPanel:trans_papel");
   }

   public void seleccionarMotivosDefinitivas() {
      aceptar = true;
      if (lovMotivosDefinitivas != null) {
         if (!lovMotivosDefinitivas.isEmpty()) {
            if (motivoDefinitivaSeleccionada != null) {
               parametroCambioMasivoActual.setRetiMotivoDefinitiva(motivoDefinitivaSeleccionada.getSecuencia());
               parametroCambioMasivoActual.setStr_retiMotivoDefinitiva(motivoDefinitivaSeleccionada.getNombre());
               if (guardado) {
                  guardado = false;
                  RequestContext.getCurrentInstance().update("form:ACEPTAR");
               }
            }
         }
      }
      RequestContext.getCurrentInstance().update("form:acordionPanel:str_retiMotivoDefinitiva");
      filtroLovMotivosDefinitivas = null;
      motivoDefinitivaSeleccionada = null;
      RequestContext.getCurrentInstance().reset("form:lovMotivosDefinitivas:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovMotivosDefinitivas').clearFilters()");
      RequestContext.getCurrentInstance().update("form:aceptarMotDef");
      RequestContext.getCurrentInstance().update("form:infoRegistroMotivosDefinitivas");
      RequestContext.getCurrentInstance().update("form:lovMotivosDefinitivas");
      RequestContext.getCurrentInstance().update("form:MotivosDefinitivasDialogo");
   }

   public void cancelarMotivosDefinitivas() {
      RequestContext.getCurrentInstance().update("form:acordionPanel:str_retiMotivoDefinitiva");
      filtroLovMotivosDefinitivas = null;
      motivoDefinitivaSeleccionada = null;
      RequestContext.getCurrentInstance().reset("form:lovMotivosDefinitivas:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovMotivosDefinitivas').clearFilters()");
      RequestContext.getCurrentInstance().update("form:aceptarMotDef");
      RequestContext.getCurrentInstance().update("form:infoRegistroMotivosDefinitivas");
      RequestContext.getCurrentInstance().update("form:lovMotivosDefinitivas");
      RequestContext.getCurrentInstance().update("form:MotivosDefinitivasDialogo");
   }

   /**
    *
    * @param t
    */
   public void autocompletarMotivosDefinitivas(String t) {
      aceptar = true;
      if (t == null || "".equals(t) || " ".equals(t) || t.isEmpty()) {
         parametroCambioMasivoActual.setRetiMotivoDefinitiva(null);
         parametroCambioMasivoActual.setStr_retiMotivoDefinitiva("");
         modificarParametros();
      } else if (lovMotivosDefinitivas != null) {
         if (!lovMotivosDefinitivas.isEmpty()) {
            int hayUno = 0;
            int indexObj = 0;
            for (int i = 0; i < lovMotivosDefinitivas.size(); i++) {
               if (lovMotivosDefinitivas.get(i).getNombre().startsWith(t)) {
                  hayUno++;
                  indexObj = i;
               }
            }
            if (hayUno == 1) {
               parametroCambioMasivoActual.setRetiMotivoDefinitiva(lovMotivosDefinitivas.get(indexObj).getSecuencia());
               parametroCambioMasivoActual.setStr_retiMotivoDefinitiva(lovMotivosDefinitivas.get(indexObj).getNombre());
               modificarParametros();
            } else {
               hayUno = 0;
               for (int i = 0; i < lovMotivosDefinitivas.size(); i++) {
                  if (lovMotivosDefinitivas.get(i).getSecuencia().equals(parametroCambioMasivoActual.getRetiMotivoDefinitiva())) {
                     parametroCambioMasivoActual.setStr_retiMotivoDefinitiva(lovMotivosDefinitivas.get(i).getNombre());
                     hayUno++;
                  }
               }
               if (hayUno != 1) {
                  parametroCambioMasivoActual.setRetiMotivoDefinitiva(null);
                  parametroCambioMasivoActual.setStr_retiMotivoDefinitiva("");
               }
               RequestContext.getCurrentInstance().update("form:MotivosDefinitivasDialogo");
               RequestContext.getCurrentInstance().execute("PF('MotivosDefinitivasDialogo').show()");
            }
         }
      }
      RequestContext.getCurrentInstance().update("form:acordionPanel:str_retiMotivoDefinitiva");
   }

   public void seleccionarMotivosRetiros() {
      aceptar = true;
      if (lovMotivosRetiros != null) {
         if (!lovMotivosRetiros.isEmpty()) {
            if (motivoRetiroSeleccionado != null) {
               parametroCambioMasivoActual.setRetiMotivoRetiro(motivoRetiroSeleccionado.getSecuencia());
               parametroCambioMasivoActual.setStr_retiMotivoRetiro(motivoRetiroSeleccionado.getNombre());
               if (guardado) {
                  guardado = false;
                  RequestContext.getCurrentInstance().update("form:ACEPTAR");
               }
            }
         }
      }
      RequestContext.getCurrentInstance().update("form:acordionPanel:str_retiMotivoRetiro");
      filtroLovMotivosRetiros = null;
      motivoRetiroSeleccionado = null;
      RequestContext.getCurrentInstance().reset("form:lovMotivosRetiros:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovMotivosRetiros').clearFilters()");
      RequestContext.getCurrentInstance().update("form:aceptarMotR");
      RequestContext.getCurrentInstance().update("form:infoRegistroMotivosRetiros");
      RequestContext.getCurrentInstance().update("form:lovMotivosRetiros");
      RequestContext.getCurrentInstance().update("form:MotivosRetirosDialogo");
   }

   public void cancelarMotivosRetiros() {
      RequestContext.getCurrentInstance().update("form:acordionPanel:str_retiMotivoRetiro");
      filtroLovMotivosRetiros = null;
      motivoRetiroSeleccionado = null;
      RequestContext.getCurrentInstance().reset("form:lovMotivosRetiros:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovMotivosRetiros').clearFilters()");
      RequestContext.getCurrentInstance().update("form:aceptarMotR");
      RequestContext.getCurrentInstance().update("form:infoRegistroMotivosRetiros");
      RequestContext.getCurrentInstance().update("form:lovMotivosRetiros");
      RequestContext.getCurrentInstance().update("form:MotivosRetirosDialogo");
   }

   /**
    *
    * @param t
    */
   public void autocompletarMotivosRetiros(String t) {
      aceptar = true;
      if (t == null || "".equals(t) || " ".equals(t) || t.isEmpty()) {
         parametroCambioMasivoActual.setRetiMotivoRetiro(null);
         parametroCambioMasivoActual.setStr_retiMotivoRetiro("");
         modificarParametros();
      } else if (lovMotivosRetiros != null) {
         if (!lovMotivosRetiros.isEmpty()) {
            int hayUno = 0;
            int indexObj = 0;
            for (int i = 0; i < lovMotivosRetiros.size(); i++) {
               if (lovMotivosRetiros.get(i).getNombre().startsWith(t)) {
                  hayUno++;
                  indexObj = i;
               }
            }
            if (hayUno == 1) {
               parametroCambioMasivoActual.setRetiMotivoRetiro(lovMotivosRetiros.get(indexObj).getSecuencia());
               parametroCambioMasivoActual.setStr_retiMotivoRetiro(lovMotivosRetiros.get(indexObj).getNombre());
               modificarParametros();
            } else {
               hayUno = 0;
               for (int i = 0; i < lovMotivosRetiros.size(); i++) {
                  if (lovMotivosRetiros.get(i).getSecuencia().equals(parametroCambioMasivoActual.getRetiMotivoRetiro())) {
                     parametroCambioMasivoActual.setStr_retiMotivoRetiro(lovMotivosRetiros.get(i).getNombre());
                     hayUno++;
                  }
               }
               if (hayUno != 1) {
                  parametroCambioMasivoActual.setRetiMotivoRetiro(null);
                  parametroCambioMasivoActual.setStr_retiMotivoRetiro("");
               }
               RequestContext.getCurrentInstance().update("form:MotivosRetirosDialogo");
               RequestContext.getCurrentInstance().execute("PF('MotivosRetirosDialogo').show()");
            }
         }
      }
      RequestContext.getCurrentInstance().update("form:acordionPanel:str_retiMotivoRetiro");
   }

   public void seleccionarTiposEntidades() {
      aceptar = true;
      if (lovTiposEntidades != null) {
         if (!lovTiposEntidades.isEmpty()) {
            if (tipoEntidadSeleccionada != null) {
               parametroCambioMasivoActual.setAfiliaTipoEntidad(tipoEntidadSeleccionada.getSecuencia());
               parametroCambioMasivoActual.setStr_afiliaTipoEntidad(tipoEntidadSeleccionada.getNombre());
               if (guardado) {
                  guardado = false;
                  RequestContext.getCurrentInstance().update("form:ACEPTAR");
               }
            }
         }
      }
      RequestContext.getCurrentInstance().update("form:acordionPanel:str_afiliaTipoEntidad");
      filtroLovTiposEntidades = null;
      tipoEntidadSeleccionada = null;
      RequestContext.getCurrentInstance().reset("form:lovTiposEntidades:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovTiposEntidades').clearFilters()");
      RequestContext.getCurrentInstance().update("form:aceptartipoEnt");
      RequestContext.getCurrentInstance().update("form:lovTiposEntidades");
      RequestContext.getCurrentInstance().update("form:infoRegistroTiposEntidades");
      RequestContext.getCurrentInstance().update("form:TiposEntidadesDialogo");
   }

   public void cancelarTiposEntidades() {
      RequestContext.getCurrentInstance().update("form:acordionPanel:str_afiliaTipoEntidad");
      filtroLovTiposEntidades = null;
      tipoEntidadSeleccionada = null;
      RequestContext.getCurrentInstance().reset("form:lovTiposEntidades:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovTiposEntidades').clearFilters()");
      RequestContext.getCurrentInstance().update("form:aceptartipoEnt");
      RequestContext.getCurrentInstance().update("form:lovTiposEntidades");
      RequestContext.getCurrentInstance().update("form:infoRegistroTiposEntidades");
      RequestContext.getCurrentInstance().update("form:TiposEntidadesDialogo");
   }

   /**
    *
    * @param t
    */
   public void autocompletarTiposEntidades(String t) {
      aceptar = true;
      if (t == null || "".equals(t) || " ".equals(t) || t.isEmpty()) {
         parametroCambioMasivoActual.setAfiliaTipoEntidad(null);
         parametroCambioMasivoActual.setStr_afiliaTipoEntidad("");
         modificarParametros();
      } else if (lovTiposEntidades != null) {
         if (!lovTiposEntidades.isEmpty()) {
            int hayUno = 0;
            int indexObj = 0;
            for (int i = 0; i < lovTiposEntidades.size(); i++) {
               if (lovTiposEntidades.get(i).getNombre().startsWith(t)) {
                  hayUno++;
                  indexObj = i;
               }
            }
            if (hayUno == 1) {
               parametroCambioMasivoActual.setAfiliaTipoEntidad(lovTiposEntidades.get(indexObj).getSecuencia());
               parametroCambioMasivoActual.setStr_afiliaTipoEntidad(lovTiposEntidades.get(indexObj).getNombre());
               modificarParametros();
            } else {
               hayUno = 0;
               for (int i = 0; i < lovTiposEntidades.size(); i++) {
                  if (lovTiposEntidades.get(i).getSecuencia().equals(parametroCambioMasivoActual.getAfiliaTipoEntidad())) {
                     parametroCambioMasivoActual.setStr_afiliaTipoEntidad(lovTiposEntidades.get(i).getNombre());
                     hayUno++;
                  }
               }
               if (hayUno != 1) {
                  parametroCambioMasivoActual.setAfiliaTipoEntidad(null);
                  parametroCambioMasivoActual.setStr_afiliaTipoEntidad("");
               }
               RequestContext.getCurrentInstance().update("form:TiposEntidadesDialogo");
               RequestContext.getCurrentInstance().execute("PF('TiposEntidadesDialogo').show()");
            }
         }
      }
      RequestContext.getCurrentInstance().update("form:acordionPanel:str_afiliaTipoEntidad");
   }

   public void seleccionarTercerosSucursales() {
      aceptar = true;
      if (lovTercerosSucursales != null) {
         if (!lovTercerosSucursales.isEmpty()) {
            if (tercerosSucursalSeleccionada != null) {
               parametroCambioMasivoActual.setAfiliaTerceroSucursal(tercerosSucursalSeleccionada.getSecuencia());
               parametroCambioMasivoActual.setStr_afiliaTerceroSucursal(tercerosSucursalSeleccionada.getDescripcion());
               if (guardado) {
                  guardado = false;
                  RequestContext.getCurrentInstance().update("form:ACEPTAR");
               }
            }
         }
      }
      RequestContext.getCurrentInstance().update("form:acordionPanel:str_afiliaTerceroSucursal");
      filtroLovTercerosSucursales = null;
      tercerosSucursalSeleccionada = null;
      RequestContext.getCurrentInstance().reset("form:lovTercerosSucursales:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovTercerosSucursales').clearFilters()");
      RequestContext.getCurrentInstance().update("form:aceptarTS");
      RequestContext.getCurrentInstance().update("form:lovTercerosSucursales");
      RequestContext.getCurrentInstance().update("form:infoRegistroTercerosSucursales");
      RequestContext.getCurrentInstance().update("form:TercerosSucursalesDialogo");
   }

   public void cancelarTercerosSucursales() {
      RequestContext.getCurrentInstance().update("form:acordionPanel:str_afiliaTerceroSucursal");
      filtroLovTercerosSucursales = null;
      tercerosSucursalSeleccionada = null;
      RequestContext.getCurrentInstance().reset("form:lovTercerosSucursales:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovTercerosSucursales').clearFilters()");
      RequestContext.getCurrentInstance().update("form:aceptarTS");
      RequestContext.getCurrentInstance().update("form:lovTercerosSucursales");
      RequestContext.getCurrentInstance().update("form:infoRegistroTercerosSucursales");
      RequestContext.getCurrentInstance().update("form:TercerosSucursalesDialogo");
   }

   /**
    *
    * @param t
    */
   public void autocompletarTercerosSucursales(String t) {
      aceptar = true;
      if (t == null || "".equals(t) || " ".equals(t) || t.isEmpty()) {
         parametroCambioMasivoActual.setAfiliaTerceroSucursal(null);
         parametroCambioMasivoActual.setStr_afiliaTerceroSucursal("");
         modificarParametros();
      } else if (lovTercerosSucursales != null) {
         if (!lovTercerosSucursales.isEmpty()) {
            int hayUno = 0;
            int indexObj = 0;
            for (int i = 0; i < lovTercerosSucursales.size(); i++) {
               if (lovTercerosSucursales.get(i).getDescripcion().startsWith(t)) {
                  hayUno++;
                  indexObj = i;
               }
            }
            if (hayUno == 1) {
               parametroCambioMasivoActual.setAfiliaTerceroSucursal(lovTercerosSucursales.get(indexObj).getSecuencia());
               parametroCambioMasivoActual.setStr_afiliaTerceroSucursal(lovTercerosSucursales.get(indexObj).getDescripcion());
               modificarParametros();
            } else {
               hayUno = 0;
               for (int i = 0; i < lovTercerosSucursales.size(); i++) {
                  if (lovTercerosSucursales.get(i).getSecuencia().equals(parametroCambioMasivoActual.getAfiliaTerceroSucursal())) {
                     parametroCambioMasivoActual.setStr_afiliaTerceroSucursal(lovTercerosSucursales.get(i).getDescripcion());
                     hayUno++;
                  }
               }
               if (hayUno != 1) {
                  parametroCambioMasivoActual.setAfiliaTerceroSucursal(null);
                  parametroCambioMasivoActual.setStr_afiliaTerceroSucursal("");
               }
               RequestContext.getCurrentInstance().update("form:TercerosSucursalesDialogo");
               RequestContext.getCurrentInstance().execute("PF('TercerosSucursalesDialogo').show()");
            }
         }
      }
      RequestContext.getCurrentInstance().update("form:acordionPanel:str_afiliaTerceroSucursal");
   }

   public void seleccionarCausasausentismos() {
      aceptar = true;
      if (lovCausasausentismos != null) {
         if (!lovCausasausentismos.isEmpty()) {
            if (causaausentismoSeleccionado != null) {
               parametroCambioMasivoActual.setTrans_SecCausa(causaausentismoSeleccionado.getSecuencia());
               parametroCambioMasivoActual.setTrans_causa(causaausentismoSeleccionado.getDescripcion());
               if (guardado) {
                  guardado = false;
                  RequestContext.getCurrentInstance().update("form:ACEPTAR");
               }
            }
         }
      }
      RequestContext.getCurrentInstance().update("form:acordionPanel:trans_causa");
      filtroLovCausasausentismos = null;
      causaausentismoSeleccionado = null;
      RequestContext.getCurrentInstance().reset("form:lovCausasausentismos:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovCausasausentismos').clearFilters()");
      RequestContext.getCurrentInstance().update("form:aceptarCau");
      RequestContext.getCurrentInstance().update("form:lovCausasausentismos");
      RequestContext.getCurrentInstance().update("form:infoRegistroCausasausentismos");
      RequestContext.getCurrentInstance().update("form:CausasausentismosDialogo");
   }

   public void cancelarCausasausentismos() {
      RequestContext.getCurrentInstance().update("form:acordionPanel:trans_causa");
      filtroLovCausasausentismos = null;
      causaausentismoSeleccionado = null;
      RequestContext.getCurrentInstance().reset("form:lovCausasausentismos:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovCausasausentismos').clearFilters()");
      RequestContext.getCurrentInstance().update("form:aceptarCau");
      RequestContext.getCurrentInstance().update("form:lovCausasausentismos");
      RequestContext.getCurrentInstance().update("form:infoRegistroCausasausentismos");
      RequestContext.getCurrentInstance().update("form:CausasausentismosDialogo");
   }

   /**
    *
    * @param t
    */
   public void autocompletarCausasausentismos(String t) {
      aceptar = true;
      if (t == null || "".equals(t) || " ".equals(t) || t.isEmpty()) {
         parametroCambioMasivoActual.setTrans_SecCausa(null);
         parametroCambioMasivoActual.setTrans_causa("");
         modificarParametros();
      } else if (lovCausasausentismos != null) {
         if (!lovCausasausentismos.isEmpty()) {
            int hayUno = 0;
            int indexObj = 0;
            for (int i = 0; i < lovCausasausentismos.size(); i++) {
               if (lovCausasausentismos.get(i).getDescripcion().startsWith(t)) {
                  hayUno++;
                  indexObj = i;
               }
            }
            if (hayUno == 1) {
               parametroCambioMasivoActual.setTrans_SecCausa(lovCausasausentismos.get(indexObj).getSecuencia());
               parametroCambioMasivoActual.setTrans_causa(lovCausasausentismos.get(indexObj).getDescripcion());
               modificarParametros();
            } else {
               hayUno = 0;
               for (int i = 0; i < lovCausasausentismos.size(); i++) {
                  if (lovCausasausentismos.get(i).getSecuencia().equals(parametroCambioMasivoActual.getTrans_causa())) {
                     parametroCambioMasivoActual.setTrans_causa(lovCausasausentismos.get(i).getDescripcion());
                     hayUno++;
                  }
               }
               if (hayUno != 1) {
                  parametroCambioMasivoActual.setTrans_SecCausa(null);
                  parametroCambioMasivoActual.setTrans_causa("");
               }
               RequestContext.getCurrentInstance().update("form:CausasausentismosDialogo");
               RequestContext.getCurrentInstance().execute("PF('CausasausentismosDialogo').show()");
            }
         }
      }
      RequestContext.getCurrentInstance().update("form:acordionPanel:trans_causa");
   }

   public void seleccionarClasesausentismos() {
      aceptar = true;
      if (lovClasesausentismos != null) {
         if (!lovClasesausentismos.isEmpty()) {
            if (clasesausentismosSeleccionado != null) {
               parametroCambioMasivoActual.setTrans_SecClase(clasesausentismosSeleccionado.getSecuencia());
               parametroCambioMasivoActual.setTrans_clase(clasesausentismosSeleccionado.getDescripcion());
               if (guardado) {
                  guardado = false;
                  RequestContext.getCurrentInstance().update("form:ACEPTAR");
               }
            }
         }
      }
      RequestContext.getCurrentInstance().update("form:acordionPanel:trans_clase");
      filtroLovClasesausentismos = null;
      clasesausentismosSeleccionado = null;
      RequestContext.getCurrentInstance().reset("form:lovClasesausentismos:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovClasesausentismos').clearFilters()");
      RequestContext.getCurrentInstance().update("form:aceptarClaseA");
      RequestContext.getCurrentInstance().update("form:lovClasesausentismos");
      RequestContext.getCurrentInstance().update("form:infoRegistroClasesausentismos");
      RequestContext.getCurrentInstance().update("form:ClasesausentismosDialogo");
   }

   public void cancelarClasesausentismos() {
      RequestContext.getCurrentInstance().update("form:acordionPanel:trans_clase");
      filtroLovClasesausentismos = null;
      clasesausentismosSeleccionado = null;
      RequestContext.getCurrentInstance().reset("form:lovClasesausentismos:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovClasesausentismos').clearFilters()");
      RequestContext.getCurrentInstance().update("form:aceptarClaseA");
      RequestContext.getCurrentInstance().update("form:lovClasesausentismos");
      RequestContext.getCurrentInstance().update("form:infoRegistroClasesausentismos");
      RequestContext.getCurrentInstance().update("form:ClasesausentismosDialogo");
   }

   /**
    *
    * @param t
    */
   public void autocompletarClasesausentismos(String t) {
      aceptar = true;
      if (t == null || "".equals(t) || " ".equals(t) || t.isEmpty()) {
         parametroCambioMasivoActual.setTrans_SecClase(null);
         parametroCambioMasivoActual.setTrans_clase("");
         modificarParametros();
      } else if (lovClasesausentismos != null) {
         if (!lovClasesausentismos.isEmpty()) {
            int hayUno = 0;
            int indexObj = 0;
            for (int i = 0; i < lovClasesausentismos.size(); i++) {
               if (lovClasesausentismos.get(i).getDescripcion().startsWith(t)) {
                  hayUno++;
                  indexObj = i;
               }
            }
            if (hayUno == 1) {
               parametroCambioMasivoActual.setTrans_SecClase(lovClasesausentismos.get(indexObj).getSecuencia());
               parametroCambioMasivoActual.setTrans_clase(lovClasesausentismos.get(indexObj).getDescripcion());
               modificarParametros();
            } else {
               hayUno = 0;
               for (int i = 0; i < lovClasesausentismos.size(); i++) {
                  if (lovClasesausentismos.get(i).getSecuencia().equals(parametroCambioMasivoActual.getTrans_clase())) {
                     parametroCambioMasivoActual.setTrans_clase(lovClasesausentismos.get(i).getDescripcion());
                     hayUno++;
                  }
               }
               if (hayUno != 1) {
                  parametroCambioMasivoActual.setTrans_SecClase(null);
                  parametroCambioMasivoActual.setTrans_clase("");
               }
               RequestContext.getCurrentInstance().update("form:ClasesausentismosDialogo");
               RequestContext.getCurrentInstance().execute("PF('ClasesausentismosDialogo').show()");
            }
         }
      }
      RequestContext.getCurrentInstance().update("form:acordionPanel:trans_clase");
   }

   ////////////////////////////////////////////////////////////////////////////////////////////////
   public void llamarLOV(int pan, int camp) {
      aceptar = true;
      if (pan == 1 && camp == 1) {
         cargarLOVEstructurasCargo();
         RequestContext.getCurrentInstance().update("form:EstructurasDialogo");
         RequestContext.getCurrentInstance().execute("PF('EstructurasDialogo').show()");
      } else if (pan == 3) {
         switch (camp) {
            case 1:
               RequestContext.getCurrentInstance().update("form:MotivosDefinitivasDialogo");
               RequestContext.getCurrentInstance().execute("PF('MotivosDefinitivasDialogo').show()");
               break;
            case 2:
               RequestContext.getCurrentInstance().update("form:MotivosRetirosDialogo");
               RequestContext.getCurrentInstance().execute("PF('MotivosRetirosDialogo').show()");
               break;
         }
      } else if (pan == 4) {
         switch (camp) {
            case 1:
               RequestContext.getCurrentInstance().update("form:TiposEntidadesDialogo");
               RequestContext.getCurrentInstance().execute("PF('TiposEntidadesDialogo').show()");
               break;
            case 2:
               RequestContext.getCurrentInstance().update("form:TercerosSucursalesDialogo");
               RequestContext.getCurrentInstance().execute("PF('TercerosSucursalesDialogo').show()");
               break;
         }
      } else if (pan == 5 && camp == 1) {
         RequestContext.getCurrentInstance().update("form:EstructurasDialogo");
         RequestContext.getCurrentInstance().execute("PF('EstructurasDialogo').show()");
      } else if (pan == 6) {
         switch (camp) {
            case 1:
               RequestContext.getCurrentInstance().update("form:ConceptosDialogo");
               RequestContext.getCurrentInstance().execute("PF('ConceptosDialogo').show()");
               break;
            case 2:
               RequestContext.getCurrentInstance().update("form:PeriodicidadesDialogo");
               RequestContext.getCurrentInstance().execute("PF('PeriodicidadesDialogo').show()");
               break;
            case 3:
               RequestContext.getCurrentInstance().update("form:TercerosDialogo");
               RequestContext.getCurrentInstance().execute("PF('TercerosDialogo').show()");
               break;
            case 4:
               RequestContext.getCurrentInstance().update("form:FormulasDialogo");
               RequestContext.getCurrentInstance().execute("PF('FormulasDialogo').show()");
               break;
         }
      } else if (pan == 7) {
         switch (camp) {
            case 1:
               RequestContext.getCurrentInstance().update("form:TiposSueldosDialogo");
               RequestContext.getCurrentInstance().execute("PF('TiposSueldosDialogo').show()");
               break;
            case 2:
               RequestContext.getCurrentInstance().update("form:MotivosSueldosDialogo");
               RequestContext.getCurrentInstance().execute("PF('MotivosSueldosDialogo').show()");
               break;
            case 3:
               RequestContext.getCurrentInstance().update("form:UnidadesDialogo");
               RequestContext.getCurrentInstance().execute("PF('UnidadesDialogo').show()");
               break;
         }
      } else if (pan == 9 && camp == 1) {
         RequestContext.getCurrentInstance().update("form:EmpleadosDialogo");
         RequestContext.getCurrentInstance().execute("PF('EmpleadosDialogo').show()");
      } else if (pan == 10) {
         switch (camp) {
            case 1:
               RequestContext.getCurrentInstance().update("form:TiposausentismosDialogo");
               RequestContext.getCurrentInstance().execute("PF('TiposausentismosDialogo').show()");
               break;
            case 2:
               RequestContext.getCurrentInstance().update("form:ClasesausentismosDialogo");
               RequestContext.getCurrentInstance().execute("PF('ClasesausentismosDialogo').show()");
               break;
            case 3:
               RequestContext.getCurrentInstance().update("form:CausasausentismosDialogo");
               RequestContext.getCurrentInstance().execute("PF('CausasausentismosDialogo').show()");
               break;
         }
      } else if (pan == 11 && camp == 1) {
         RequestContext.getCurrentInstance().update("form:PapelesDialogo");
         RequestContext.getCurrentInstance().execute("PF('PapelesDialogo').show()");
      }
   }

   ////////////////////////////////////////////////////////////////////////////////////////////////
   public void botonLOV() {
      aceptar = true;
      if (panelActivo == 1 && campo == 2) {
         cargarLOVEstructurasCargo();
         RequestContext.getCurrentInstance().update("form:EstructurasDialogo");
         RequestContext.getCurrentInstance().execute("PF('EstructurasDialogo').show()");
      } else if (panelActivo == 3) {
         switch (campo) {
            case 2:
               RequestContext.getCurrentInstance().update("form:MotivosDefinitivasDialogo");
               RequestContext.getCurrentInstance().execute("PF('MotivosDefinitivasDialogo').show()");
               break;
            case 3:
               RequestContext.getCurrentInstance().update("form:MotivosRetirosDialogo");
               RequestContext.getCurrentInstance().execute("PF('MotivosRetirosDialogo').show()");
               break;
         }
      } else if (panelActivo == 4) {
         switch (campo) {
            case 2:
               RequestContext.getCurrentInstance().update("form:TiposEntidadesDialogo");
               RequestContext.getCurrentInstance().execute("PF('TiposEntidadesDialogo').show()");
               break;
            case 3:
               RequestContext.getCurrentInstance().update("form:TercerosSucursalesDialogo");
               RequestContext.getCurrentInstance().execute("PF('TercerosSucursalesDialogo').show()");
               break;
         }
      } else if (panelActivo == 5 && campo == 2) {
         RequestContext.getCurrentInstance().update("form:EstructurasDialogo");
         RequestContext.getCurrentInstance().execute("PF('EstructurasDialogo').show()");
      } else if (panelActivo == 6) {
         switch (campo) {
            case 1:
               RequestContext.getCurrentInstance().update("form:ConceptosDialogo");
               RequestContext.getCurrentInstance().execute("PF('ConceptosDialogo').show()");
               break;
            case 2:
               RequestContext.getCurrentInstance().update("form:PeriodicidadesDialogo");
               RequestContext.getCurrentInstance().execute("PF('PeriodicidadesDialogo').show()");
               break;
            case 3:
               RequestContext.getCurrentInstance().update("form:TercerosDialogo");
               RequestContext.getCurrentInstance().execute("PF('TercerosDialogo').show()");
               break;
            case 4:
               RequestContext.getCurrentInstance().update("form:FormulasDialogo");
               RequestContext.getCurrentInstance().execute("PF('FormulasDialogo').show()");
               break;
         }
      } else if (panelActivo == 7) {
         switch (campo) {
            case 3:
               RequestContext.getCurrentInstance().update("form:TiposSueldosDialogo");
               RequestContext.getCurrentInstance().execute("PF('TiposSueldosDialogo').show()");
               break;
            case 4:
               RequestContext.getCurrentInstance().update("form:MotivosSueldosDialogo");
               RequestContext.getCurrentInstance().execute("PF('MotivosSueldosDialogo').show()");
               break;
            case 5:
               RequestContext.getCurrentInstance().update("form:UnidadesDialogo");
               RequestContext.getCurrentInstance().execute("PF('UnidadesDialogo').show()");
               break;
         }
      } else if (panelActivo == 9 && campo == 2) {
         RequestContext.getCurrentInstance().update("form:EmpleadosDialogo");
         RequestContext.getCurrentInstance().execute("PF('EmpleadosDialogo').show()");
      } else if (panelActivo == 10) {
         switch (campo) {
            case 1:
               RequestContext.getCurrentInstance().update("form:TiposausentismosDialogo");
               RequestContext.getCurrentInstance().execute("PF('TiposausentismosDialogo').show()");
               break;
            case 2:
               RequestContext.getCurrentInstance().update("form:ClasesausentismosDialogo");
               RequestContext.getCurrentInstance().execute("PF('ClasesausentismosDialogo').show()");
               break;
            case 3:
               RequestContext.getCurrentInstance().update("form:CausasausentismosDialogo");
               RequestContext.getCurrentInstance().execute("PF('CausasausentismosDialogo').show()");
               break;
         }
      } else if (panelActivo == 11 && campo == 2) {
         RequestContext.getCurrentInstance().update("form:PapelesDialogo");
         RequestContext.getCurrentInstance().execute("PF('PapelesDialogo').show()");
      }
   }

   public void seleccionarCampo(int camp) {
      System.out.println("ControlCambiosMasivos.seleccionarCampo() camp: " + camp);
      campo = camp;
   }

   public void cancelarModificaciones() {
      listaParametros = null;
      listaCambiosMasivos = null;
      cambioMasivoSeleccionado = null;
      parametroCambioMasivoActual = null;

      estructuraSeleccionada = null;
      motivoDefinitivaSeleccionada = null;
      motivoRetiroSeleccionado = null;
      tipoEntidadSeleccionada = null;
      tercerosSucursalSeleccionada = null;
      periodicidadSeleccionada = null;
      conceptoSeleccionado = null;
      formulaSeleccionada = null;
      terceroSeleccionado = null;
      tipoSueldoSeleccionado = null;
      motivoSueldoSeleccionado = null;
      tipoausentismoSeleccionado = null;
      unidadSeleccionada = null;
      empleadoSeleccionado = null;
      papelSeleccionado = null;
      clasesausentismosSeleccionado = null;
      causaausentismoSeleccionado = null;

      lovEstructuras = new ArrayList<Estructuras>();
      lovMotivosDefinitivas = new ArrayList<MotivosDefinitivas>();
      lovMotivosRetiros = new ArrayList<MotivosRetiros>();
      lovTiposEntidades = new ArrayList<TiposEntidades>();
      lovTercerosSucursales = new ArrayList<TercerosSucursales>();
      lovPeriodicidades = new ArrayList<Periodicidades>();
      lovConceptos = new ArrayList<Conceptos>();
      lovFormulas = new ArrayList<Formulas>();
      lovTerceros = new ArrayList<Terceros>();
      lovMotivosSueldos = new ArrayList<MotivosCambiosSueldos>();
      lovTiposSueldos = new ArrayList<TiposSueldos>();
      lovTiposausentismos = new ArrayList<Tiposausentismos>();
      lovUnidades = new ArrayList<Unidades>();
      lovEmpleados = new ArrayList<Empleados>();
      lovIbcs = new ArrayList<String>();
      lovPapeles = new ArrayList<Papeles>();
      lovCausasausentismos = new ArrayList<Causasausentismos>();
      lovClasesausentismos = new ArrayList<Clasesausentismos>();

      campo = 0;
      panelActivo = 1;
      aceptar = true;
      guardado = true;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext.getCurrentInstance().update("form:scrollPanelPrincipal");
   }

   public void salir() {
      listaParametros = null;
      listaCambiosMasivos = null;
      cambioMasivoSeleccionado = null;
      parametroCambioMasivoActual = null;
      campo = 0;
      panelActivo = 1;
   }

   public void editar() {
      System.out.println("Controlador.ControlCambiosMasivos.editar() campo: " + campo + ",  panelActivo: " + panelActivo);
      if (panelActivo == 1) {
         //Estructura Cargo Desempeñado
         switch (campo) {
            case 1:
               RequestContext.getCurrentInstance().update("formularioDialogos:editCargoFecha");
               RequestContext.getCurrentInstance().execute("PF('editCargoFecha').show()");
               break;
            case 2:
               RequestContext.getCurrentInstance().update("formularioDialogos:editCargoEstructuras");
               RequestContext.getCurrentInstance().execute("PF('editCargoEstructuras').show()");
               break;
         }
      } else if (panelActivo == 2) {
         //Vacaciones
         switch (campo) {
            case 1:
               RequestContext.getCurrentInstance().update("formularioDialogos:editVacaFechaCambio");
               RequestContext.getCurrentInstance().execute("PF('editVacaFechaCambio').show()");
               break;
            case 2:
               RequestContext.getCurrentInstance().update("formularioDialogos:editVacaFechaPago");
               RequestContext.getCurrentInstance().execute("PF('editVacaFechaPago').show()");
               break;
            case 3:
               RequestContext.getCurrentInstance().update("formularioDialogos:editVacaDias");
               RequestContext.getCurrentInstance().execute("PF('editVacaDias').show()");
               break;
         }
      } else if (panelActivo == 3) {
         //Retiros
         switch (campo) {
            case 1:
               RequestContext.getCurrentInstance().update("formularioDialogos:editRetiroFecha");
               RequestContext.getCurrentInstance().execute("PF('editRetiroFecha').show()");
               break;
            case 2:
               RequestContext.getCurrentInstance().update("formularioDialogos:editRetiroMotDef");
               RequestContext.getCurrentInstance().execute("PF('editRetiroMotDef').show()");
               break;
            case 3:
               RequestContext.getCurrentInstance().update("formularioDialogos:editRetiroMotRetiro");
               RequestContext.getCurrentInstance().execute("PF('editRetiroMotRetiro').show()");
               break;
         }
      } else if (panelActivo == 4) {
         //Afiliaciones
         switch (campo) {
            case 1:
               RequestContext.getCurrentInstance().update("formularioDialogos:editAfiliaFecha");
               RequestContext.getCurrentInstance().execute("PF('editAfiliaFecha').show()");
               break;
            case 2:
               RequestContext.getCurrentInstance().update("formularioDialogos:editAfiliaTiposEnt");
               RequestContext.getCurrentInstance().execute("PF('editAfiliaTiposEnt').show()");
               break;
            case 3:
               RequestContext.getCurrentInstance().update("formularioDialogos:editAfiliaTercSuc");
               RequestContext.getCurrentInstance().execute("PF('editAfiliaTercSuc').show()");
               break;
         }
      } else if (panelActivo == 5) {
         //Centro de Costo
         switch (campo) {
            case 1:
               RequestContext.getCurrentInstance().update("formularioDialogos:editCentrosCFecha");
               RequestContext.getCurrentInstance().execute("PF('editCentrosCFecha').show()");
               break;
            case 2:
               RequestContext.getCurrentInstance().update("formularioDialogos:editCentrosCEstr");
               RequestContext.getCurrentInstance().execute("PF('editCentrosCEstr').show()");
               break;
         }
      } else if (panelActivo == 6) {
         //Novedades
         switch (campo) {
            case 1:
               RequestContext.getCurrentInstance().update("formularioDialogos:editNoveConc");
               RequestContext.getCurrentInstance().execute("PF('editNoveConc').show()");
               break;
            case 2:
               RequestContext.getCurrentInstance().update("formularioDialogos:editNovePerio");
               RequestContext.getCurrentInstance().execute("PF('editNovePerio').show()");
               break;
            case 3:
               RequestContext.getCurrentInstance().update("formularioDialogos:editNoveTerce");
               RequestContext.getCurrentInstance().execute("PF('editNoveTerce').show()");
               break;
            case 4:
               RequestContext.getCurrentInstance().update("formularioDialogos:editNoveFormu");
               RequestContext.getCurrentInstance().execute("PF('editNoveFormu').show()");
               break;
            case 5:
               RequestContext.getCurrentInstance().update("formularioDialogos:editNoveFechaIni");
               RequestContext.getCurrentInstance().execute("PF('editNoveFechaIni').show()");
               break;
            case 6:
               RequestContext.getCurrentInstance().update("formularioDialogos:editNoveFechaFin");
               RequestContext.getCurrentInstance().execute("PF('editNoveFechaFin').show()");
               break;
            case 7:
               RequestContext.getCurrentInstance().update("formularioDialogos:editNoveValor");
               RequestContext.getCurrentInstance().execute("PF('editNoveValor').show()");
               break;
            case 8:
               RequestContext.getCurrentInstance().update("formularioDialogos:editNoveSaldo");
               RequestContext.getCurrentInstance().execute("PF('editNoveSaldo').show()");
               break;
            case 9:
               RequestContext.getCurrentInstance().update("formularioDialogos:editNoveHora");
               RequestContext.getCurrentInstance().execute("PF('editNoveHora').show()");
               break;
            case 10:
               RequestContext.getCurrentInstance().update("formularioDialogos:editNoveMin");
               RequestContext.getCurrentInstance().execute("PF('editNoveMin').show()");
               break;
            case 11:
               RequestContext.getCurrentInstance().update("formularioDialogos:editNoveTipo");
               RequestContext.getCurrentInstance().execute("PF('editNoveTipo').show()");
               break;
         }
      } else if (panelActivo == 7) {
         //Sueldos
         switch (campo) {
            case 1:
               RequestContext.getCurrentInstance().update("formularioDialogos:editSuFecha");
               RequestContext.getCurrentInstance().execute("PF('editSuFecha').show()");
               break;
            case 2:
               RequestContext.getCurrentInstance().update("formularioDialogos:editSuValor");
               RequestContext.getCurrentInstance().execute("PF('editSuValor').show()");
               break;
            case 3:
               RequestContext.getCurrentInstance().update("formularioDialogos:editSuTipoSu");
               RequestContext.getCurrentInstance().execute("PF('editSuTipoSu').show()");
               break;
            case 4:
               RequestContext.getCurrentInstance().update("formularioDialogos:editSuMotivoSu");
               RequestContext.getCurrentInstance().execute("PF('editSuMotivoSu').show()");
               break;
            case 5:
               RequestContext.getCurrentInstance().update("formularioDialogos:editSuUnidad");
               RequestContext.getCurrentInstance().execute("PF('editSuUnidad').show()");
               break;
            case 6:
               RequestContext.getCurrentInstance().update("formularioDialogos:editSuFechaRetroac");
               RequestContext.getCurrentInstance().execute("PF('editSuFechaRetroac').show()");
               break;
         }
      } else if (panelActivo == 8) {
         //Reingresos
         switch (campo) {
            case 1:
               RequestContext.getCurrentInstance().update("formularioDialogos:editReinFechaRein");
               RequestContext.getCurrentInstance().execute("PF('editReinFechaRein').show()");
               break;
            case 2:
               RequestContext.getCurrentInstance().update("formularioDialogos:editReinFechaFinC");
               RequestContext.getCurrentInstance().execute("PF('editReinFechaFinC').show()");
               break;
         }
      } else if (panelActivo == 9) {
         //Empleado Jefe
         switch (campo) {
            case 1:
               RequestContext.getCurrentInstance().update("formularioDialogos:editJefeFecha");
               RequestContext.getCurrentInstance().execute("PF('editJefeFecha').show()");
               break;
            case 2:
               RequestContext.getCurrentInstance().update("formularioDialogos:editJefeNombre");
               RequestContext.getCurrentInstance().execute("PF('editJefeNombre').show()");
               break;
         }
      } else if (panelActivo == 10) {
         //Ausentismos
         switch (campo) {
            case 1:
               RequestContext.getCurrentInstance().update("formularioDialogos:editAusenTipo");
               RequestContext.getCurrentInstance().execute("PF('editAusenTipo').show()");
               break;
            case 2:
               RequestContext.getCurrentInstance().update("formularioDialogos:editAusenClase");
               RequestContext.getCurrentInstance().execute("PF('editAusenClase').show()");
               break;
            case 3:
               RequestContext.getCurrentInstance().update("formularioDialogos:editAusenCausa");
               RequestContext.getCurrentInstance().execute("PF('editAusenCausa').show()");
               break;
            case 4:
               RequestContext.getCurrentInstance().update("formularioDialogos:editAusenFormaLiq");
               RequestContext.getCurrentInstance().execute("PF('editAusenFormaLiq').show()");
               break;
            case 5:
               RequestContext.getCurrentInstance().update("formularioDialogos:editAusenBaseLiq");
               RequestContext.getCurrentInstance().execute("PF('editAusenBaseLiq').show()");
               break;
            case 6:
               RequestContext.getCurrentInstance().update("formularioDialogos:editAusenFechaIni");
               RequestContext.getCurrentInstance().execute("PF('editAusenFechaIni').show()");
               break;
            case 7:
               RequestContext.getCurrentInstance().update("formularioDialogos:editAusenFechaFin");
               RequestContext.getCurrentInstance().execute("PF('editAusenFechaFin').show()");
               break;
            case 8:
               RequestContext.getCurrentInstance().update("formularioDialogos:editAusenFechaExpedi");
               RequestContext.getCurrentInstance().execute("PF('editAusenFechaExpedi').show()");
               break;
            case 9:
               RequestContext.getCurrentInstance().update("formularioDialogos:editAusenFechaIniPag");
               RequestContext.getCurrentInstance().execute("PF('editAusenFechaIniPag').show()");
               break;
            case 10:
               RequestContext.getCurrentInstance().update("formularioDialogos:editAusenFechaFinPag");
               RequestContext.getCurrentInstance().execute("PF('editAusenFechaFinPag').show()");
               break;
            case 11:
               RequestContext.getCurrentInstance().update("formularioDialogos:editAusenPorcent");
               RequestContext.getCurrentInstance().execute("PF('editAusenPorcent').show()");
               break;
            case 12:
               RequestContext.getCurrentInstance().update("formularioDialogos:editAusenDias");
               RequestContext.getCurrentInstance().execute("PF('editAusenDias').show()");
               break;
            case 13:
               RequestContext.getCurrentInstance().update("formularioDialogos:editAusenHoras");
               RequestContext.getCurrentInstance().execute("PF('editAusenHoras').show()");
               break;
         }
      } else if (panelActivo == 11) {
         //Papel
         switch (campo) {
            case 1:
               RequestContext.getCurrentInstance().update("formularioDialogos:editPapelFecha");
               RequestContext.getCurrentInstance().execute("PF('editPapelFecha').show()");
               break;
            case 2:
               RequestContext.getCurrentInstance().update("formularioDialogos:editPapelNombre");
               RequestContext.getCurrentInstance().execute("PF('editPapelNombre').show()");
               break;
         }
      }
   }

   public void onTabChange(TabChangeEvent event) {
      campo = 0;
      String panel = event.getTab().getTitle();
      if (panel.equals("Estructura Cargo Desempeñado")) {
         cargarLOVEstructurasCargo();
         panelActivo = 1;
      } else if (panel.equals("Vacaciones")) {
         panelActivo = 2;
      } else if (panel.equals("Retiros")) {
         cargarLOVsRetiro();
         panelActivo = 3;
      } else if (panel.equals("Afiliaciones")) {
         cargarLOVsAfiliaciones();
         panelActivo = 4;
      } else if (panel.equals("Centro de Costo")) {
         cargarLOVEstructurasCargo();
         panelActivo = 5;
      } else if (panel.equals("Novedades")) {
         cargarLOVsNovedad();
         panelActivo = 6;
      } else if (panel.equals("Sueldos")) {
         cargarLOVsSueldo();
         panelActivo = 7;
      } else if (panel.equals("Reingresos")) {
         panelActivo = 8;
      } else if (panel.equals("Empleado Jefe")) {
         cargarLOVEmpleados();
         panelActivo = 9;
      } else if (panel.equals("Ausentismos")) {
         cargarLOVsAusentismo();
         panelActivo = 10;
      } else if (panel.equals("Papel")) {
         cargarLOVPapeles();
         panelActivo = 11;
      }
   }

   public void onTabClose(TabCloseEvent event) {
      panelActivo = 0;
      campo = 0;
   }

   public void recibirPagina(String pagina) {
      paginaAnterior = pagina;
   }

   public String valorPaginaAnterior() {
      return paginaAnterior;
   }

   public void modificarParametros() {
      if (guardado) {
         guardado = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }
   }

   public void guardarCambiosParametros() {
      if (!guardado) {
         administrarCambiosMasivos.actualizarParametroCM(parametroCambioMasivoActual);
      }
      parametroCambioMasivoActual = null;
      administrarCambiosMasivos.consultarEmpleadosParametros();
      campo = 0;
      aceptar = true;
      guardado = true;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext.getCurrentInstance().update("form:scrollPanelPrincipal");
      FacesMessage msg = new FacesMessage("Información", "Se gurdarón los datos con éxito.");
      FacesContext.getCurrentInstance().addMessage(null, msg);
      RequestContext.getCurrentInstance().update("form:growl");
   }

   public void contarRegistrosCM() {
      RequestContext.getCurrentInstance().update("form:informacionRegistroCM");
   }

   public void contarRegistrosP() {
      RequestContext.getCurrentInstance().update("form:informacionRegistroP");
   }

   public void cargarLOVEstructurasCargo() {
      if (lovEstructuras.isEmpty()) {
         lovEstructuras = administrarCambiosMasivos.consultarLovCargos_Estructuras();
      }
   }

   public void cargarLOVsRetiro() {
      if (lovMotivosDefinitivas.isEmpty()) {
         lovMotivosDefinitivas = administrarCambiosMasivos.consultarLovMotivosDefinitivas();
      }
      if (lovMotivosRetiros.isEmpty()) {
         lovMotivosRetiros = administrarCambiosMasivos.consultarLovMotivosRetiros();
      }
   }

   public void cargarLOVsAfiliaciones() {
      if (lovTiposEntidades.isEmpty()) {
         lovTiposEntidades = administrarCambiosMasivos.consultarLovTiposEntidades();
      }
      if (lovTercerosSucursales.isEmpty()) {
         lovTercerosSucursales = administrarCambiosMasivos.consultarLovTercerosSucursales();
      }
   }

   public void cargarLOVsNovedad() {
      if (lovPeriodicidades.isEmpty()) {
         lovPeriodicidades = administrarCambiosMasivos.consultarLovPeriodicidades();
      }
      if (lovConceptos.isEmpty()) {
         lovConceptos = administrarCambiosMasivos.consultarLovConceptos();
      }
      if (lovFormulas.isEmpty()) {
         lovFormulas = administrarCambiosMasivos.consultarLovFormulas();
      }
      if (lovTerceros.isEmpty()) {
         lovTerceros = administrarCambiosMasivos.consultarLovTerceros();
      }
   }

   public void cargarLOVsSueldo() {
      if (lovMotivosSueldos.isEmpty()) {
         lovMotivosSueldos = administrarCambiosMasivos.consultarLovMotivosCambiosSueldos();
      }
      if (lovTiposSueldos.isEmpty()) {
         lovTiposSueldos = administrarCambiosMasivos.consultarLovTiposSueldos();
      }
      if (lovUnidades.isEmpty()) {
         lovUnidades = administrarCambiosMasivos.consultarLovUnidades();
      }
   }

   public void cargarLOVsAusentismo() {
      if (lovTiposausentismos.isEmpty()) {
         lovTiposausentismos = administrarCambiosMasivos.consultarLovTiposausentismos();
      }
      if (lovIbcs.isEmpty()) {
         lovIbcs.add("IBC MES INCAPACIDAD");
         lovIbcs.add("IBC MES ANTERIOR");
         lovIbcs.add("BASICO");
         lovIbcs.add("IBC MES ENERO");
         lovIbcs.add("PROMEDIO IBC 6 MESES");
         lovIbcs.add("PROMEDIO IBC 12 MESES");
         lovIbcs.add("PROMEDIO ACUMULADOS 12 MESES");
      }
      if (lovCausasausentismos.isEmpty()) {
         lovCausasausentismos = administrarCambiosMasivos.consultarLovCausasausentismos();
      }
      if (lovClasesausentismos.isEmpty()) {
         lovClasesausentismos = administrarCambiosMasivos.consultarLovClasesausentismos();
      }
   }

   public void cargarLOVPapeles() {
      if (lovPapeles.isEmpty()) {
         lovPapeles = administrarCambiosMasivos.consultarLovPapeles();
      }
   }

   public void cargarLOVEmpleados() {
      if (lovEmpleados.isEmpty()) {
         lovEmpleados = administrarCambiosMasivos.consultarLovEmpleados();
      }
   }

   public void activarAceptar() {
      aceptar = false;
   }

   public void exportXLS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosCambiosMasivosExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "CambiosMasivos_XLS", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void exportPDF() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosCambiosMasivosExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "CambiosMasivos_PDF", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   //GETS AND SETS
   public List<Parametros> getListaParametros() {
      if (listaParametros == null) {
         listaParametros = administrarCambiosMasivos.consultarEmpleadosParametros();
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
         listaCambiosMasivos = administrarCambiosMasivos.consultarUltimosCambiosMasivos();
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

   public void setInfoRegistroParametros(String infoRegistroParametros) {
      this.infoRegistroParametros = infoRegistroParametros;
   }

   public void setInfoRegistroCambiosMasivos(String infoRegistroCambiosMasivos) {
      this.infoRegistroCambiosMasivos = infoRegistroCambiosMasivos;
   }

   public List<Estructuras> getLovEstructuras() {
//      cargarLOVEstructurasCargo();
      return lovEstructuras;
   }

   public void setLovEstructuras(List<Estructuras> lovEstructuras) {
      this.lovEstructuras = lovEstructuras;
   }

   public List<Estructuras> getFiltroLovEstructuras() {
      return filtroLovEstructuras;
   }

   public void setFiltroLovEstructuras(List<Estructuras> filtroLovEstructuras) {
      this.filtroLovEstructuras = filtroLovEstructuras;
   }

   public void setInfoRegistroEstructuras(String infoRegistroEstructuras) {
      this.infoRegistroEstructuras = infoRegistroEstructuras;
   }

   public Estructuras getEstructuraSeleccionada() {
      return estructuraSeleccionada;
   }

   public void setEstructuraSeleccionada(Estructuras estructuraSeleccionada) {
      this.estructuraSeleccionada = estructuraSeleccionada;
   }

   public List<MotivosDefinitivas> getLovMotivosDefinitivas() {
      return lovMotivosDefinitivas;
   }

   public void setLovMotivosDefinitivas(List<MotivosDefinitivas> lovMotivosDefinitivas) {
      this.lovMotivosDefinitivas = lovMotivosDefinitivas;
   }

   public List<MotivosDefinitivas> getFiltroLovMotivosDefinitivas() {
      return filtroLovMotivosDefinitivas;
   }

   public void setFiltroLovMotivosDefinitivas(List<MotivosDefinitivas> filtroLovMotivosDefinitivas) {
      this.filtroLovMotivosDefinitivas = filtroLovMotivosDefinitivas;
   }

   public void setInfoRegistroMotivosDefinitivas(String infoRegistroMotivosDefinitivas) {
      this.infoRegistroMotivosDefinitivas = infoRegistroMotivosDefinitivas;
   }

   public MotivosDefinitivas getMotivoDefinitivaSeleccionada() {
      return motivoDefinitivaSeleccionada;
   }

   public void setMotivoDefinitivaSeleccionada(MotivosDefinitivas motivoDefinitivaSeleccionada) {
      this.motivoDefinitivaSeleccionada = motivoDefinitivaSeleccionada;
   }

   public List<MotivosRetiros> getLovMotivosRetiros() {
      return lovMotivosRetiros;
   }

   public void setLovMotivosRetiros(List<MotivosRetiros> lovMotivosRetiros) {
      this.lovMotivosRetiros = lovMotivosRetiros;
   }

   public List<MotivosRetiros> getFiltroLovMotivosRetiros() {
      return filtroLovMotivosRetiros;
   }

   public void setFiltroLovMotivosRetiros(List<MotivosRetiros> filtroLovMotivosRetiros) {
      this.filtroLovMotivosRetiros = filtroLovMotivosRetiros;
   }

   public void setInfoRegistroMotivosRetiros(String infoRegistroMotivosRetiros) {
      this.infoRegistroMotivosRetiros = infoRegistroMotivosRetiros;
   }

   public MotivosRetiros getMotivoRetiroSeleccionado() {
      return motivoRetiroSeleccionado;
   }

   public void setMotivoRetiroSeleccionado(MotivosRetiros motivoRetiroSeleccionado) {
      this.motivoRetiroSeleccionado = motivoRetiroSeleccionado;
   }

   public List<TiposEntidades> getLovTiposEntidades() {
      return lovTiposEntidades;
   }

   public void setLovTiposEntidades(List<TiposEntidades> lovTiposEntidades) {
      this.lovTiposEntidades = lovTiposEntidades;
   }

   public List<TiposEntidades> getFiltroLovTiposEntidades() {
      return filtroLovTiposEntidades;
   }

   public void setFiltroLovTiposEntidades(List<TiposEntidades> filtroLovTiposEntidades) {
      this.filtroLovTiposEntidades = filtroLovTiposEntidades;
   }

   public void setInfoRegistroTiposEntidades(String infoRegistroTiposEntidades) {
      this.infoRegistroTiposEntidades = infoRegistroTiposEntidades;
   }

   public TiposEntidades getTipoEntidadSeleccionada() {
      return tipoEntidadSeleccionada;
   }

   public void setTipoEntidadSeleccionada(TiposEntidades tipoEntidadSeleccionada) {
      this.tipoEntidadSeleccionada = tipoEntidadSeleccionada;
   }

   public List<TercerosSucursales> getLovTercerosSucursales() {
      return lovTercerosSucursales;
   }

   public void setLovTercerosSucursales(List<TercerosSucursales> lovTercerosSucursales) {
      this.lovTercerosSucursales = lovTercerosSucursales;
   }

   public List<TercerosSucursales> getFiltroLovTercerosSucursales() {
      return filtroLovTercerosSucursales;
   }

   public void setFiltroLovTercerosSucursales(List<TercerosSucursales> filtroLovTercerosSucursales) {
      this.filtroLovTercerosSucursales = filtroLovTercerosSucursales;
   }

   public void setInfoRegistroTercerosSucursales(String infoRegistroTercerosSucursales) {
      this.infoRegistroTercerosSucursales = infoRegistroTercerosSucursales;
   }

   public TercerosSucursales getTercerosSucursalSeleccionada() {
      return tercerosSucursalSeleccionada;
   }

   public void setTercerosSucursalSeleccionada(TercerosSucursales tercerosSucursalSeleccionada) {
      this.tercerosSucursalSeleccionada = tercerosSucursalSeleccionada;
   }

   public List<Periodicidades> getLovPeriodicidades() {
      return lovPeriodicidades;
   }

   public void setLovPeriodicidades(List<Periodicidades> lovPeriodicidades) {
      this.lovPeriodicidades = lovPeriodicidades;
   }

   public List<Periodicidades> getFiltroLovPeriodicidades() {
      return filtroLovPeriodicidades;
   }

   public void setFiltroLovPeriodicidades(List<Periodicidades> filtroLovPeriodicidades) {
      this.filtroLovPeriodicidades = filtroLovPeriodicidades;
   }

   public void setInfoRegistroPeriodicidades(String infoRegistroPeriodicidades) {
      this.infoRegistroPeriodicidades = infoRegistroPeriodicidades;
   }

   public Periodicidades getPeriodicidadSeleccionada() {
      return periodicidadSeleccionada;
   }

   public void setPeriodicidadSeleccionada(Periodicidades periodicidadSeleccionada) {
      this.periodicidadSeleccionada = periodicidadSeleccionada;
   }

   public List<Conceptos> getLovConceptos() {
      return lovConceptos;
   }

   public void setLovConceptos(List<Conceptos> lovConceptos) {
      this.lovConceptos = lovConceptos;
   }

   public List<Conceptos> getFiltroLovConceptos() {
      return filtroLovConceptos;
   }

   public void setFiltroLovConceptos(List<Conceptos> filtroLovConceptos) {
      this.filtroLovConceptos = filtroLovConceptos;
   }

   public void setInfoRegistroConceptos(String infoRegistroConceptos) {
      this.infoRegistroConceptos = infoRegistroConceptos;
   }

   public Conceptos getConceptoSeleccionado() {
      return conceptoSeleccionado;
   }

   public void setConceptoSeleccionado(Conceptos conceptoSeleccionado) {
      this.conceptoSeleccionado = conceptoSeleccionado;
   }

   public List<Formulas> getLovFormulas() {
      return lovFormulas;
   }

   public void setLovFormulas(List<Formulas> lovFormulas) {
      this.lovFormulas = lovFormulas;
   }

   public List<Formulas> getFiltroLovFormulas() {
      return filtroLovFormulas;
   }

   public void setFiltroLovFormulas(List<Formulas> filtroLovFormulas) {
      this.filtroLovFormulas = filtroLovFormulas;
   }

   public void setInfoRegistroFormulas(String infoRegistroFormulas) {
      this.infoRegistroFormulas = infoRegistroFormulas;
   }

   public Formulas getFormulaSeleccionada() {
      return formulaSeleccionada;
   }

   public void setFormulaSeleccionada(Formulas formulaSeleccionada) {
      this.formulaSeleccionada = formulaSeleccionada;
   }

   public List<Terceros> getLovTerceros() {
      return lovTerceros;
   }

   public void setLovTerceros(List<Terceros> lovTerceros) {
      this.lovTerceros = lovTerceros;
   }

   public List<Terceros> getFiltroLovTerceros() {
      return filtroLovTerceros;
   }

   public void setFiltroLovTerceros(List<Terceros> filtroLovTerceros) {
      this.filtroLovTerceros = filtroLovTerceros;
   }

   public void setInfoRegistroTerceros(String infoRegistroTerceros) {
      this.infoRegistroTerceros = infoRegistroTerceros;
   }

   public Terceros getTerceroSeleccionado() {
      return terceroSeleccionado;
   }

   public void setTerceroSeleccionado(Terceros terceroSeleccionado) {
      this.terceroSeleccionado = terceroSeleccionado;
   }

   public List<MotivosCambiosSueldos> getLovMotivosSueldos() {
      return lovMotivosSueldos;
   }

   public void setLovMotivosSueldos(List<MotivosCambiosSueldos> lovMotivosSueldos) {
      this.lovMotivosSueldos = lovMotivosSueldos;
   }

   public List<MotivosCambiosSueldos> getFiltroLovMotivosSueldos() {
      return filtroLovMotivosSueldos;
   }

   public void setFiltroLovMotivosSueldos(List<MotivosCambiosSueldos> filtroLovMotivosSueldos) {
      this.filtroLovMotivosSueldos = filtroLovMotivosSueldos;
   }

   public void setInfoRegistroMotivosSueldos(String infoRegistroMotivosSueldos) {
      this.infoRegistroMotivosSueldos = infoRegistroMotivosSueldos;
   }

   public MotivosCambiosSueldos getMotivoSueldoSeleccionado() {
      return motivoSueldoSeleccionado;
   }

   public void setMotivoSueldoSeleccionado(MotivosCambiosSueldos motivoSueldoSeleccionado) {
      this.motivoSueldoSeleccionado = motivoSueldoSeleccionado;
   }

   public List<TiposSueldos> getLovTiposSueldos() {
      return lovTiposSueldos;
   }

   public void setLovTiposSueldos(List<TiposSueldos> lovTiposSueldos) {
      this.lovTiposSueldos = lovTiposSueldos;
   }

   public List<TiposSueldos> getFiltroLovTiposSueldos() {
      return filtroLovTiposSueldos;
   }

   public void setFiltroLovTiposSueldos(List<TiposSueldos> filtroLovTiposSueldos) {
      this.filtroLovTiposSueldos = filtroLovTiposSueldos;
   }

   public void setInfoRegistroTiposSueldos(String infoRegistroTiposSueldos) {
      this.infoRegistroTiposSueldos = infoRegistroTiposSueldos;
   }

   public TiposSueldos getTipoSueldoSeleccionado() {
      return tipoSueldoSeleccionado;
   }

   public void setTipoSueldoSeleccionado(TiposSueldos tipoSueldoSeleccionado) {
      this.tipoSueldoSeleccionado = tipoSueldoSeleccionado;
   }

   public List<Tiposausentismos> getLovTiposausentismos() {
      return lovTiposausentismos;
   }

   public void setLovTiposausentismos(List<Tiposausentismos> lovTiposausentismos) {
      this.lovTiposausentismos = lovTiposausentismos;
   }

   public List<Tiposausentismos> getFiltroLovTiposausentismos() {
      return filtroLovTiposausentismos;
   }

   public void setFiltroLovTiposausentismos(List<Tiposausentismos> filtroLovTiposausentismos) {
      this.filtroLovTiposausentismos = filtroLovTiposausentismos;
   }

   public void setInfoRegistroTiposausentismos(String infoRegistroTiposausentismos) {
      this.infoRegistroTiposausentismos = infoRegistroTiposausentismos;
   }

   public Tiposausentismos getTipoausentismoSeleccionado() {
      return tipoausentismoSeleccionado;
   }

   public void setTipoausentismoSeleccionado(Tiposausentismos tipoausentismoSeleccionado) {
      this.tipoausentismoSeleccionado = tipoausentismoSeleccionado;
   }

   public List<Unidades> getLovUnidades() {
      return lovUnidades;
   }

   public void setLovUnidades(List<Unidades> lovUnidades) {
      this.lovUnidades = lovUnidades;
   }

   public List<Unidades> getFiltroLovUnidades() {
      return filtroLovUnidades;
   }

   public void setFiltroLovUnidades(List<Unidades> filtroLovUnidades) {
      this.filtroLovUnidades = filtroLovUnidades;
   }

   public void setInfoRegistroUnidades(String infoRegistroUnidades) {
      this.infoRegistroUnidades = infoRegistroUnidades;
   }

   public Unidades getUnidadSeleccionada() {
      return unidadSeleccionada;
   }

   public void setUnidadSeleccionada(Unidades unidadSeleccionada) {
      this.unidadSeleccionada = unidadSeleccionada;
   }

   public List<Empleados> getLovEmpleados() {
      return lovEmpleados;
   }

   public void setLovEmpleados(List<Empleados> lovEmpleados) {
      this.lovEmpleados = lovEmpleados;
   }

   public List<Empleados> getFiltroLovEmpleados() {
      return filtroLovEmpleados;
   }

   public void setFiltroLovEmpleados(List<Empleados> filtroLovEmpleados) {
      this.filtroLovEmpleados = filtroLovEmpleados;
   }

   public void setInfoRegistroEmpleados(String infoRegistroEmpleados) {
      this.infoRegistroEmpleados = infoRegistroEmpleados;
   }

   public Empleados getEmpleadoSeleccionado() {
      return empleadoSeleccionado;
   }

   public void setEmpleadoSeleccionado(Empleados empleadoSeleccionado) {
      this.empleadoSeleccionado = empleadoSeleccionado;
   }

   public List<String> getLovIbcs() {
      return lovIbcs;
   }

   public void setLovIbcs(List<String> lovIbcs) {
      this.lovIbcs = lovIbcs;
   }

   public List<Papeles> getLovPapeles() {
      return lovPapeles;
   }

   public void setLovPapeles(List<Papeles> lovPapeles) {
      this.lovPapeles = lovPapeles;
   }

   public List<Papeles> getFiltroLovPapeles() {
      return filtroLovPapeles;
   }

   public void setFiltroLovPapeles(List<Papeles> filtroLovPapeles) {
      this.filtroLovPapeles = filtroLovPapeles;
   }

   public void setInfoRegistroPapeles(String infoRegistroPapeles) {
      this.infoRegistroPapeles = infoRegistroPapeles;
   }

   public Papeles getPapelSeleccionado() {
      return papelSeleccionado;
   }

   public void setPapelSeleccionado(Papeles papelSeleccionado) {
      this.papelSeleccionado = papelSeleccionado;
   }

   public List<Causasausentismos> getLovCausasausentismos() {
      return lovCausasausentismos;
   }

   public void setLovCausasausentismos(List<Causasausentismos> lovCausasausentismos) {
      this.lovCausasausentismos = lovCausasausentismos;
   }

   public List<Causasausentismos> getFiltroLovCausasausentismos() {
      return filtroLovCausasausentismos;
   }

   public void setFiltroLovCausasausentismos(List<Causasausentismos> filtroLovCausasausentismos) {
      this.filtroLovCausasausentismos = filtroLovCausasausentismos;
   }

   public void setInfoRegistroCausasausentismos(String infoRegistroCausasausentismos) {
      this.infoRegistroCausasausentismos = infoRegistroCausasausentismos;
   }

   public Causasausentismos getCausaausentismoSeleccionado() {
      return causaausentismoSeleccionado;
   }

   public void setCausaausentismoSeleccionado(Causasausentismos causaausentismoSeleccionado) {
      this.causaausentismoSeleccionado = causaausentismoSeleccionado;
   }

   public List<Clasesausentismos> getLovClasesausentismos() {
      return lovClasesausentismos;
   }

   public void setLovClasesausentismos(List<Clasesausentismos> lovClasesausentismos) {
      this.lovClasesausentismos = lovClasesausentismos;
   }

   public List<Clasesausentismos> getFiltroLovClasesausentismos() {
      return filtroLovClasesausentismos;
   }

   public void setFiltroLovClasesausentismos(List<Clasesausentismos> filtroLovClasesausentismos) {
      this.filtroLovClasesausentismos = filtroLovClasesausentismos;
   }

   public void setInfoRegistroClasesausentismos(String infoRegistroClasesausentismos) {
      this.infoRegistroClasesausentismos = infoRegistroClasesausentismos;
   }

   public Clasesausentismos getClasesausentismosSeleccionado() {
      return clasesausentismosSeleccionado;
   }

   public void setClasesausentismosSeleccionado(Clasesausentismos clasesausentismosSeleccionado) {
      this.clasesausentismosSeleccionado = clasesausentismosSeleccionado;
   }

   public boolean isAceptar() {
      return aceptar;
   }

   public void setAceptar(boolean aceptar) {
      this.aceptar = aceptar;
   }

   public String getInfoRegistroParametros() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosEmpleadosParametros");
      infoRegistroParametros = String.valueOf(tabla.getRowCount());
      return infoRegistroParametros;
   }

   public String getInfoRegistroCambiosMasivos() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosCambiosMasivos");
      infoRegistroCambiosMasivos = String.valueOf(tabla.getRowCount());
      return infoRegistroCambiosMasivos;
   }

   public String getInfoRegistroEstructuras() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovEstructuras");
      infoRegistroEstructuras = String.valueOf(tabla.getRowCount());
      return infoRegistroEstructuras;
   }

   public String getInfoRegistroMotivosDefinitivas() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovMotivosDefinitivas");
      infoRegistroMotivosDefinitivas = String.valueOf(tabla.getRowCount());
      return infoRegistroMotivosDefinitivas;
   }

   public String getInfoRegistroMotivosRetiros() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovMotivosRetiros");
      infoRegistroMotivosRetiros = String.valueOf(tabla.getRowCount());
      return infoRegistroMotivosRetiros;
   }

   public String getInfoRegistroTiposEntidades() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovTiposEntidades");
      infoRegistroTiposEntidades = String.valueOf(tabla.getRowCount());
      return infoRegistroTiposEntidades;
   }

   public String getInfoRegistroTercerosSucursales() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovTercerosSucursales");
      infoRegistroTercerosSucursales = String.valueOf(tabla.getRowCount());
      return infoRegistroTercerosSucursales;
   }

   public String getInfoRegistroPeriodicidades() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovPeriodicidades");
      infoRegistroPeriodicidades = String.valueOf(tabla.getRowCount());
      return infoRegistroPeriodicidades;
   }

   public String getInfoRegistroConceptos() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovConceptos");
      infoRegistroConceptos = String.valueOf(tabla.getRowCount());
      return infoRegistroConceptos;
   }

   public String getInfoRegistroFormulas() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovFormulas");
      infoRegistroFormulas = String.valueOf(tabla.getRowCount());
      return infoRegistroFormulas;
   }

   public String getInfoRegistroTerceros() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovTerceros");
      infoRegistroTerceros = String.valueOf(tabla.getRowCount());
      return infoRegistroTerceros;
   }

   public String getInfoRegistroMotivosSueldos() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovMotivosSueldos");
      infoRegistroMotivosSueldos = String.valueOf(tabla.getRowCount());
      return infoRegistroMotivosSueldos;
   }

   public String getInfoRegistroTiposSueldos() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovTiposSueldos");
      infoRegistroTiposSueldos = String.valueOf(tabla.getRowCount());
      return infoRegistroTiposSueldos;
   }

   public String getInfoRegistroTiposausentismos() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovTiposausentismos");
      infoRegistroTiposausentismos = String.valueOf(tabla.getRowCount());
      return infoRegistroTiposausentismos;
   }

   public String getInfoRegistroUnidades() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovUnidades");
      infoRegistroUnidades = String.valueOf(tabla.getRowCount());
      return infoRegistroUnidades;
   }

   public String getInfoRegistroEmpleados() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovEmpleados");
      infoRegistroEmpleados = String.valueOf(tabla.getRowCount());
      return infoRegistroEmpleados;
   }

   public String getInfoRegistroPapeles() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovPapeles");
      infoRegistroPapeles = String.valueOf(tabla.getRowCount());
      return infoRegistroPapeles;
   }

   public String getInfoRegistroCausasausentismos() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovCausasausentismos");
      infoRegistroCausasausentismos = String.valueOf(tabla.getRowCount());
      return infoRegistroCausasausentismos;
   }

   public String getInfoRegistroClasesausentismos() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovClasesausentismos");
      infoRegistroClasesausentismos = String.valueOf(tabla.getRowCount());
      return infoRegistroClasesausentismos;
   }

   public boolean isGuardado() {
      return guardado;
   }

   public void setGuardado(boolean guardado) {
      this.guardado = guardado;
   }

}
