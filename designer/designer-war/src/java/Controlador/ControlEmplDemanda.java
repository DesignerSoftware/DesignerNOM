/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Demandas;
import Entidades.Empleados;
import Entidades.MotivosDemandas;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarEmplDemandaInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import ControlNavegacion.ControlListaNavegacion;
import java.util.Map;
import java.util.LinkedHashMap;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.primefaces.component.column.Column;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.export.Exporter;
import org.primefaces.context.RequestContext;

/**
 *
 * @author user
 */
@ManagedBean
@SessionScoped
public class ControlEmplDemanda implements Serializable {

   @EJB
   AdministrarEmplDemandaInterface administrarEmplDemanda;
   @EJB
   AdministrarRastrosInterface administrarRastros;
   private List<Demandas> listDemandasEmpleado;
   private List<Demandas> filtrarListDemandasEmpleado;
   private List<Demandas> listDemandaEmpleadoBorrar;
   private List<Demandas> listDemandaEmpleadoModificar;
   private List<Demandas> listDemandaEmpleadoCrear;
   private Demandas demandaTablaSeleccionada;
   public Demandas nuevaDemandaEmpleado;
   private Demandas editarDemandaEmpleado;
   private Demandas duplicarDemandaEmpleado;
   //LOV MOTIVOS DEMANDAS
   private List<MotivosDemandas> lovMotivosDemandas;
   private MotivosDemandas motivoDemandaSeleccionado;
   private List<MotivosDemandas> filtrarListMotivosDemandas;
   private int tipoActualizacion;
   private int banderaD;
   private Column dMotivo, dSeguimiento, dFecha;
   private boolean aceptar;
   private boolean guardado;
   private BigInteger l;
   private int k;
   private boolean permitirIndexD;
   private String motivo;
   private int cualCelda, tipoLista;
   private Date fechaInic;
   private Empleados empleado;
   private Date fechaParametro;
   //
   private String infoRegistro;
   private String infoRegistroMotivo;
   private String altoTabla;
   private DataTable tablaC;
   private boolean activarLov;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

   public ControlEmplDemanda() {
      altoTabla = "280";
      empleado = new Empleados();
      tipoLista = 0;
      //Otros
      aceptar = true;
      listDemandaEmpleadoBorrar = new ArrayList<Demandas>();
      k = 0;
      listDemandaEmpleadoModificar = new ArrayList<Demandas>();
      editarDemandaEmpleado = new Demandas();
      tipoLista = 0;
      guardado = true;

      banderaD = 0;
      permitirIndexD = true;
      demandaTablaSeleccionada = null;
      cualCelda = -1;
      lovMotivosDemandas = null;
      nuevaDemandaEmpleado = new Demandas();
      nuevaDemandaEmpleado.setMotivo(new MotivosDemandas());
      nuevaDemandaEmpleado.setFecha(new Date());
      listDemandaEmpleadoCrear = new ArrayList<Demandas>();
      activarLov = true;
      mapParametros.put("paginaAnterior", paginaAnterior);
   }

   public void recibirPaginaEntrante(String pagina) {
      paginaAnterior = pagina;
      //inicializarCosas(); Inicializar cosas de ser necesario
   }

   public void recibirParametros(Map<String, Object> map) {
      mapParametros = map;
      paginaAnterior = (String) mapParametros.get("paginaAnterior");
      //inicializarCosas(); Inicializar cosas de ser necesario
   }

   //Reemplazar la funcion volverAtras, retornarPagina, Redirigir.....Atras.etc
   public void navegar(String pag) {
      FacesContext fc = FacesContext.getCurrentInstance();
      ControlListaNavegacion controlListaNavegacion = (ControlListaNavegacion) fc.getApplication().evaluateExpressionGet(fc, "#{controlListaNavegacion}", ControlListaNavegacion.class);
      String pagActual = "empldemanda";
      if (pag.equals("atras")) {
         pag = paginaAnterior;
         paginaAnterior = "nominaf";
         controlListaNavegacion.quitarPagina(pagActual);
      } else {
         controlListaNavegacion.guardarNavegacion(pagActual, pag);
         fc.getApplication().getNavigationHandler().handleNavigation(fc, null, pag);
//Map<String, Object> mapParaEnviar = new LinkedHashMap<String, Object>();
         //mapParaEnviar.put("paginaAnterior", pagActual);
         //mas Parametros
//         if (pag.equals("rastrotabla")) {
//           ControlRastro controlRastro = (ControlRastro) fc.getApplication().evaluateExpressionGet(fc, "#{controlRastro}", ControlRastro.class);
         //           controlRastro.recibirDatosTabla(conceptoSeleccionado.getSecuencia(), "Conceptos", pagActual);
         //      } else if (pag.equals("rastrotablaH")) {
         //       ControlRastro controlRastro = (ControlRastro) fc.getApplication().evaluateExpressionGet(fc, "#{controlRastro}", ControlRastro.class);
         //     controlRastro.historicosTabla("Conceptos", pagActual);
         //   pag = "rastrotabla";
         //}
      }
      limpiarListasValor();
   }

   public void limpiarListasValor() {
      lovMotivosDemandas = null;
   }

   @PostConstruct
   public void inicializarAdministrador() {
      try {
         FacesContext x = FacesContext.getCurrentInstance();
         HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
         administrarEmplDemanda.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
      } catch (Exception e) {
         System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
         System.out.println("Causa: " + e.getCause());
      }
   }

   public void recibirEmpleado(BigInteger secuencia) {
      empleado = new Empleados();
      empleado = administrarEmplDemanda.actualEmpleado(secuencia);
      listDemandasEmpleado = null;
      getListDemandasEmpleado();
   }

   public boolean validarFechasRegistro(int i) {
      boolean retorno = true;
      fechaParametro = new Date();
      fechaParametro.setYear(0);
      fechaParametro.setMonth(1);
      fechaParametro.setDate(1);
      if (i == 0) {
         Demandas auxiliar = new Demandas();
         if (tipoLista == 0) {
            auxiliar = demandaTablaSeleccionada;
         }
         if (tipoLista == 1) {
            auxiliar = demandaTablaSeleccionada;
         }
         if (auxiliar.getFecha() != null) {
            if (auxiliar.getFecha().after(fechaParametro)) {
               retorno = true;
            } else {
               retorno = false;
            }
         } else {
            retorno = false;
         }

      }
      if (i == 1) {
         if (nuevaDemandaEmpleado.getFecha() != null) {
            if (nuevaDemandaEmpleado.getFecha().after(fechaParametro)) {
               retorno = true;
            } else {
               retorno = false;
            }
         } else {
            retorno = false;
         }
      }
      if (i == 2) {
         if (duplicarDemandaEmpleado.getFecha() != null) {
            if (duplicarDemandaEmpleado.getFecha().after(fechaParametro)) {
               retorno = true;
            } else {
               retorno = false;
            }
         } else {
            retorno = false;
         }
      }

      return retorno;
   }

   public void modificacionesFechas(Demandas demanda, int c) {
      demandaTablaSeleccionada = demanda;
      if (validarDatosNull(0) == true) {
         if (validarFechasRegistro(0) == true) {
            cambiarIndiceD(demandaTablaSeleccionada, c);
            modificarDemanda(demandaTablaSeleccionada);

         } else {
            if (tipoLista == 0) {
               demandaTablaSeleccionada.setFecha(fechaInic);
            }
            if (tipoLista == 1) {
               demandaTablaSeleccionada.setFecha(fechaInic);
            }
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosDemanda");
            RequestContext.getCurrentInstance().execute("PF('errorFechas').show()");
         }
      } else {
         if (tipoLista == 0) {
            demandaTablaSeleccionada.setFecha(fechaInic);
         }
         if (tipoLista == 1) {
            demandaTablaSeleccionada.setFecha(fechaInic);
         }
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:datosDemanda");
         RequestContext.getCurrentInstance().execute("PF('errorRegNull').show()");
      }
   }

   public void modificarDemanda(Demandas demanda) {
      demandaTablaSeleccionada = demanda;
      if (!listDemandaEmpleadoCrear.contains(demandaTablaSeleccionada)) {
         if (listDemandaEmpleadoModificar.isEmpty()) {
            listDemandaEmpleadoModificar.add(demandaTablaSeleccionada);
         } else if (!listDemandaEmpleadoModificar.contains(demandaTablaSeleccionada)) {
            listDemandaEmpleadoModificar.add(demandaTablaSeleccionada);
         }
         guardado = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         RequestContext.getCurrentInstance().update("form:datosDemanda");
      }
   }

   public void valoresBackupAutocompletarDemanda(int tipoNuevo, String Campo) {

      if (Campo.equals("MOTIVOS")) {
         if (tipoNuevo == 1) {
            motivo = nuevaDemandaEmpleado.getMotivo().getDescripcion();
         } else if (tipoNuevo == 2) {
            motivo = duplicarDemandaEmpleado.getMotivo().getDescripcion();
         }
      }
   }

   public void autocompletarNuevoyDuplicadoDemanda(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      if (confirmarCambio.equalsIgnoreCase("MOTIVOS")) {
         if (tipoNuevo == 1) {
            nuevaDemandaEmpleado.getMotivo().setDescripcion(motivo);
         } else if (tipoNuevo == 2) {
            duplicarDemandaEmpleado.getMotivo().setDescripcion(motivo);
         }
         for (int i = 0; i < lovMotivosDemandas.size(); i++) {
            if (lovMotivosDemandas.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoNuevo == 1) {
               nuevaDemandaEmpleado.setMotivo(lovMotivosDemandas.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaMotivo");
            } else if (tipoNuevo == 2) {
               duplicarDemandaEmpleado.setMotivo(lovMotivosDemandas.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarMotivo");
            }
            lovMotivosDemandas = null;
            getLovMotivosDemandas();
         } else {
            RequestContext.getCurrentInstance().update("form:DemandaDialogo");
            RequestContext.getCurrentInstance().execute("PF('DemandaDialogo').show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaMotivo");
            } else if (tipoNuevo == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarMotivo");
            }
         }
      }
   }

   public void cambiarIndiceD(Demandas demanda, int celda) {
      if (permitirIndexD == true) {
         demandaTablaSeleccionada = demanda;
         cualCelda = celda;
         deshabilitarBotonLov();
         if (cualCelda == 0) {
            deshabilitarBotonLov();
            fechaInic = demandaTablaSeleccionada.getFecha();
         } else if (cualCelda == 1) {
            deshabilitarBotonLov();
            demandaTablaSeleccionada.getSeguimiento();
         } else if (cualCelda == 2) {
            habilitarBotonLov();
            motivo = demandaTablaSeleccionada.getMotivo().getDescripcion();
         }
         demandaTablaSeleccionada.getSecuencia();
      }

   }

   public void guardarSalir() {
      guardadoGeneral();
      salir();
   }

   public void cancelarSalir() {
      guardadoGeneral();
      salir();
   }

   public void guardadoGeneral() {
      guardarCambiosD();
      guardado = true;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void guardarCambiosD() {
      RequestContext context = RequestContext.getCurrentInstance();
      try {
         if (guardado == false) {
            if (!listDemandaEmpleadoBorrar.isEmpty()) {
               administrarEmplDemanda.borrarDemandas(listDemandaEmpleadoBorrar);
               listDemandaEmpleadoBorrar.clear();
            }
            if (!listDemandaEmpleadoCrear.isEmpty()) {
               administrarEmplDemanda.crearDemandas(listDemandaEmpleadoCrear);
               listDemandaEmpleadoCrear.clear();
            }
            if (!listDemandaEmpleadoModificar.isEmpty()) {
               administrarEmplDemanda.editarDemandas(listDemandaEmpleadoModificar);
               listDemandaEmpleadoModificar.clear();
            }
            listDemandasEmpleado = null;
            getListDemandasEmpleado();
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            k = 0;
            FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con Éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            contarRegistros();
            demandaTablaSeleccionada = null;
         }
         RequestContext.getCurrentInstance().update("form:datosDemanda");
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      } catch (Exception e) {
         System.out.println("Error guardarCambios : " + e.toString());
         FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado, intente nuevamente.");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }
   }

   public void cancelarModificacionD() {
      if (banderaD == 1) {
         altoTabla = "280";
         dMotivo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDemanda:dMotivo");
         dMotivo.setFilterStyle("display: none; visibility: hidden;");
         dSeguimiento = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDemanda:dSeguimiento");
         dSeguimiento.setFilterStyle("display: none; visibility: hidden;");
         dFecha = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDemanda:dFecha");
         dFecha.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosDemanda");
         banderaD = 0;
         filtrarListDemandasEmpleado = null;
         tipoLista = 0;
      }
      listDemandaEmpleadoBorrar.clear();
      listDemandaEmpleadoCrear.clear();
      listDemandaEmpleadoModificar.clear();
      k = 0;
      lovMotivosDemandas = null;
      demandaTablaSeleccionada = null;
      listDemandasEmpleado = null;
      guardado = true;
      permitirIndexD = true;
      getListDemandasEmpleado();
      contarRegistros();
      RequestContext.getCurrentInstance().update("form:datosDemanda");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void editarCelda() {
      if (demandaTablaSeleccionada != null) {
         if (tipoLista == 0) {
            editarDemandaEmpleado = demandaTablaSeleccionada;
         }
         if (tipoLista == 1) {
            editarDemandaEmpleado = demandaTablaSeleccionada;
         }
         RequestContext context = RequestContext.getCurrentInstance();
         if (cualCelda == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaDD");
            RequestContext.getCurrentInstance().execute("PF('editarFechaDD').show()");
            cualCelda = -1;
         } else if (cualCelda == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarSeguimientoD");
            RequestContext.getCurrentInstance().execute("PF('editarSeguimientoD').show()");
            cualCelda = -1;
         } else if (cualCelda == 2) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarMotivoD");
            RequestContext.getCurrentInstance().execute("PF('editarMotivoD').show()");
            cualCelda = -1;
         }
      } else {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public boolean validarDatosNull(int i) {
      boolean retorno = true;
      if (i == 0) {
         Demandas demanda = null;
         if (tipoLista == 0) {
            demanda = demandaTablaSeleccionada;
         } else {
            demanda = demandaTablaSeleccionada;
         }
         if (demanda.getFecha() == null || demanda.getMotivo().getSecuencia() == null) {
            retorno = false;
         }
      }
      if (i == 1) {
         if (nuevaDemandaEmpleado.getFecha() == null || nuevaDemandaEmpleado.getMotivo().getSecuencia() == null) {
            retorno = false;
         }
      }
      if (i == 2) {
         if (duplicarDemandaEmpleado.getFecha() == null || duplicarDemandaEmpleado.getMotivo().getSecuencia() == null) {
            retorno = false;
         }
      }
      return retorno;
   }

   public void agregarNuevaD() {
      if (validarDatosNull(1) == true) {
         if (validarFechasRegistro(1) == true) {
            //CERRAR FILTRADO
            if (banderaD == 1) {
               altoTabla = "280";
               dMotivo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDemanda:dMotivo");
               dMotivo.setFilterStyle("display: none; visibility: hidden;");

               dSeguimiento = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDemanda:dSeguimiento");
               dSeguimiento.setFilterStyle("display: none; visibility: hidden;");

               dFecha = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDemanda:dFecha");
               dFecha.setFilterStyle("display: none; visibility: hidden;");

               RequestContext.getCurrentInstance().update("form:datosDemanda");
               banderaD = 0;
               filtrarListDemandasEmpleado = null;
               tipoLista = 0;
            }
            //AGREGAR REGISTRO A LA LISTA VIGENCIAS
            k++;
            l = BigInteger.valueOf(k);
            nuevaDemandaEmpleado.setSecuencia(l);
            nuevaDemandaEmpleado.setEmpleado(empleado);
//                if (nuevaDemandaEmpleado.getSeguimiento() != null) {
//                    String aux = nuevaDemandaEmpleado.getSeguimiento().toUpperCase();
//                    nuevaDemandaEmpleado.setSeguimiento(aux);
//                }
//                if (listDemandasEmpleado == null) {
//                    listDemandasEmpleado = new ArrayList<Demandas>();
//                }
            listDemandaEmpleadoCrear.add(nuevaDemandaEmpleado);
            if (listDemandasEmpleado == null) {
               listDemandasEmpleado = new ArrayList<Demandas>();
            }
            listDemandasEmpleado.add(nuevaDemandaEmpleado);
            demandaTablaSeleccionada = nuevaDemandaEmpleado;
            limpiarNuevaD();
            getListDemandasEmpleado();
            contarRegistros();
            deshabilitarBotonLov();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            RequestContext.getCurrentInstance().update("form:datosDemanda");
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroD').hide()");
            //
            if (guardado == true) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
         } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('errorFechas').show()");
         }
      } else {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().execute("PF('errorRegNull').show()");
      }
   }

   /**
    * Limpia los elementos de una nueva vigencia prorrateo
    */
   public void limpiarNuevaD() {
      nuevaDemandaEmpleado = new Demandas();
      nuevaDemandaEmpleado.setMotivo(new MotivosDemandas());

   }

   public void duplicarDemandaM() {
      if (demandaTablaSeleccionada != null) {
         duplicarDemandaEmpleado = new Demandas();

         if (tipoLista == 0) {
            duplicarDemandaEmpleado.setEmpleado(empleado);
            duplicarDemandaEmpleado.setMotivo(demandaTablaSeleccionada.getMotivo());
            duplicarDemandaEmpleado.setFecha(demandaTablaSeleccionada.getFecha());
            duplicarDemandaEmpleado.setSeguimiento(demandaTablaSeleccionada.getSeguimiento());
         }
         if (tipoLista == 1) {
            duplicarDemandaEmpleado.setEmpleado(empleado);
            duplicarDemandaEmpleado.setMotivo(demandaTablaSeleccionada.getMotivo());
            duplicarDemandaEmpleado.setFecha(demandaTablaSeleccionada.getFecha());
            duplicarDemandaEmpleado.setSeguimiento(demandaTablaSeleccionada.getSeguimiento());
         }
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarD");
         RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroD').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void confirmarDuplicarD() {
      if (validarDatosNull(2) == true) {
         if (validarFechasRegistro(2) == true) {
            if (banderaD == 1) {
               altoTabla = "280";
               //CERRAR FILTRADO
               dMotivo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDemanda:dMotivo");
               dMotivo.setFilterStyle("display: none; visibility: hidden;");
               dSeguimiento = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDemanda:dSeguimiento");
               dSeguimiento.setFilterStyle("display: none; visibility: hidden;");
               dFecha = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDemanda:dFecha");
               dFecha.setFilterStyle("display: none; visibility: hidden;");
               RequestContext.getCurrentInstance().update("form:datosDemanda");
               banderaD = 0;
               filtrarListDemandasEmpleado = null;
               tipoLista = 0;
            }
            k++;
            l = BigInteger.valueOf(k);
            duplicarDemandaEmpleado.setSecuencia(l);
            duplicarDemandaEmpleado.setEmpleado(empleado);
            listDemandasEmpleado.add(duplicarDemandaEmpleado);
            listDemandaEmpleadoCrear.add(duplicarDemandaEmpleado);
            demandaTablaSeleccionada = duplicarDemandaEmpleado;
            getListDemandasEmpleado();
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:datosDemanda");
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroD').hide()");

            if (guardado == true) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            limpiarduplicarD();
         } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('errorFechas').show()");
         }
      } else {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().execute("PF('errorRegNull').show()");
      }
   }

   public void limpiarduplicarD() {
      duplicarDemandaEmpleado = new Demandas();
      duplicarDemandaEmpleado.setMotivo(new MotivosDemandas());
   }

   public void borrarD() {
      if (tipoLista == 0) {
         if (!listDemandaEmpleadoModificar.isEmpty() && listDemandaEmpleadoModificar.contains(demandaTablaSeleccionada)) {
            int modIndex = listDemandaEmpleadoModificar.indexOf(demandaTablaSeleccionada);
            listDemandaEmpleadoModificar.remove(modIndex);
            listDemandaEmpleadoBorrar.add(demandaTablaSeleccionada);
         } else if (!listDemandaEmpleadoCrear.isEmpty() && listDemandaEmpleadoCrear.contains(demandaTablaSeleccionada)) {
            int crearIndex = listDemandaEmpleadoCrear.indexOf(demandaTablaSeleccionada);
            listDemandaEmpleadoCrear.remove(crearIndex);
         } else {
            listDemandaEmpleadoBorrar.add(demandaTablaSeleccionada);
         }
         listDemandasEmpleado.remove(demandaTablaSeleccionada);
      }
      if (tipoLista == 1) {
         filtrarListDemandasEmpleado.remove(demandaTablaSeleccionada);
      }
      contarRegistros();
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
      RequestContext.getCurrentInstance().update("form:datosDemanda");
      demandaTablaSeleccionada = null;
      if (guardado == true) {
         guardado = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }
   }

   public void activarCtrlF11() {

      if (banderaD == 0) {
         altoTabla = "260";
         dMotivo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDemanda:dMotivo");
         dMotivo.setFilterStyle("width: 85% !important");
         dSeguimiento = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDemanda:dSeguimiento");
         dSeguimiento.setFilterStyle("width: 85% !important");
         dFecha = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDemanda:dFecha");
         dFecha.setFilterStyle("width: 85% !important");
         RequestContext.getCurrentInstance().update("form:datosDemanda");
         tipoLista = 1;
         banderaD = 1;
      } else {
         altoTabla = "280";
         dMotivo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDemanda:dMotivo");
         dMotivo.setFilterStyle("display: none; visibility: hidden;");
         dSeguimiento = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDemanda:dSeguimiento");
         dSeguimiento.setFilterStyle("display: none; visibility: hidden;");
         dFecha = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDemanda:dFecha");
         dFecha.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosDemanda");
         banderaD = 0;
         filtrarListDemandasEmpleado = null;
         tipoLista = 0;
      }
   }

   public void salir() {
      limpiarListasValor();
      if (banderaD == 1) {
         altoTabla = "280";
         dMotivo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDemanda:dMotivo");
         dMotivo.setFilterStyle("display: none; visibility: hidden;");
         dSeguimiento = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDemanda:dSeguimiento");
         dSeguimiento.setFilterStyle("display: none; visibility: hidden;");
         dFecha = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDemanda:dFecha");
         dFecha.setFilterStyle("display: none; visibility: hidden;");

         RequestContext.getCurrentInstance().update("form:datosDemanda");
         banderaD = 0;
         filtrarListDemandasEmpleado = null;
         tipoLista = 0;
      }
      listDemandaEmpleadoBorrar.clear();
      listDemandaEmpleadoCrear.clear();
      listDemandaEmpleadoModificar.clear();
      demandaTablaSeleccionada = null;
      k = 0;
      listDemandasEmpleado = null;
      guardado = true;
      tipoActualizacion = -1;
      navegar("atras");
   }
   //ASIGNAR INDEX PARA DIALOGOS COMUNES (LDN = LISTA - NUEVO - DUPLICADO) (list = ESTRUCTURAS - MOTIVOSLOCALIZACIONES - PROYECTOS)

   public void asignarIndex(Demandas demanda, int dlg, int LND) {
      RequestContext context = RequestContext.getCurrentInstance();
      tipoActualizacion = LND;
      if (dlg == 0) {
         contarRegistrosMotivos();
         RequestContext.getCurrentInstance().update("form:DemandaDialogo");
         RequestContext.getCurrentInstance().execute("PF('DemandaDialogo').show()");
      }
   }

   //Motivo Localizacion
   /**
    * Metodo que actualiza el motivo localizacion seleccionado (vigencia
    * localizacion)
    */
   public void actualizarMotivo() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {
         if (tipoLista == 0) {
            demandaTablaSeleccionada.setMotivo(motivoDemandaSeleccionado);
            if (!listDemandaEmpleadoCrear.contains(demandaTablaSeleccionada)) {
               if (listDemandaEmpleadoModificar.isEmpty()) {
                  listDemandaEmpleadoModificar.add(demandaTablaSeleccionada);
               } else if (!listDemandaEmpleadoModificar.contains(demandaTablaSeleccionada)) {
                  listDemandaEmpleadoModificar.add(demandaTablaSeleccionada);
               }
            }
            if (guardado == true) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            permitirIndexD = true;

         } else {
            demandaTablaSeleccionada.setMotivo(motivoDemandaSeleccionado);
            if (!listDemandaEmpleadoCrear.contains(demandaTablaSeleccionada)) {
               if (listDemandaEmpleadoModificar.isEmpty()) {
                  listDemandaEmpleadoModificar.add(demandaTablaSeleccionada);
               } else if (!listDemandaEmpleadoModificar.contains(demandaTablaSeleccionada)) {
                  listDemandaEmpleadoModificar.add(demandaTablaSeleccionada);
               }
            }
            if (guardado == true) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            permitirIndexD = true;

         }
         RequestContext.getCurrentInstance().update("form:datosDemanda");
      } else if (tipoActualizacion == 1) {
         nuevaDemandaEmpleado.setMotivo(motivoDemandaSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaMotivo");
      } else if (tipoActualizacion == 2) {
         duplicarDemandaEmpleado.setMotivo(motivoDemandaSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarMotivo");
      }
      filtrarListMotivosDemandas = null;
      motivoDemandaSeleccionado = null;
      aceptar = true;
      demandaTablaSeleccionada = null;
      tipoActualizacion = -1;

      RequestContext.getCurrentInstance().update("form:DemandaDialogo");
      RequestContext.getCurrentInstance().update("form:lovDemanda");
      RequestContext.getCurrentInstance().update("form:aceptarD");
      context.reset("form:lovDemanda:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovDemanda').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('DemandaDialogo').hide()");
   }

   /**
    * Metodo que cancela la seleccion del motivo localizacion (vigencia
    * localizacion)
    */
   public void cancelarCambioMotivo() {
      filtrarListMotivosDemandas = null;
      motivoDemandaSeleccionado = null;
      aceptar = true;
      demandaTablaSeleccionada = null;
      demandaTablaSeleccionada = null;
      tipoActualizacion = -1;
      permitirIndexD = true;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:DemandaDialogo");
      RequestContext.getCurrentInstance().update("form:lovDemanda");
      RequestContext.getCurrentInstance().update("form:aceptarD");
      context.reset("form:lovDemanda:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovDemanda').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('DemandaDialogo').hide()");
   }

   public void listaValoresBoton() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (demandaTablaSeleccionada != null) {
         if (cualCelda == 2) {
            RequestContext.getCurrentInstance().update("form:DemandaDialogo");
            RequestContext.getCurrentInstance().execute("PF('DemandaDialogo').show()");
            tipoActualizacion = 0;
         }
      }
   }

   public void activarAceptar() {
      aceptar = false;
   }

   public void exportPDF_D() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarD:datosDemandaExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "DemandasPDF", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void exportXLS_D() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarD:datosDemandaExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "DemandasXLS", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void eventoFiltrar() {
      if (tipoLista == 0) {
         tipoLista = 1;
      }
      contarRegistros();
   }

   public void contarRegistros() {
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
   }

   public void contarRegistrosMotivos() {
      RequestContext.getCurrentInstance().update("form:infoRegistroMotivo");
   }

   //RASTRO - COMPROBAR SI LA TABLA TIENE RASTRO ACTIVO
   public void verificarRastro() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (demandaTablaSeleccionada != null) {
         int resultado = administrarRastros.obtenerTabla(demandaTablaSeleccionada.getSecuencia(), "DEMANDAS");
         if (resultado == 1) {
            RequestContext.getCurrentInstance().execute("PF('errorObjetosDB').show()");
         } else if (resultado == 2) {
            RequestContext.getCurrentInstance().execute("PF('confirmarRastro').show()");
         } else if (resultado == 3) {
            RequestContext.getCurrentInstance().execute("PF('errorRegistroRastro').show()");
         } else if (resultado == 4) {
            RequestContext.getCurrentInstance().execute("PF('errorTablaConRastro').show()");
         } else if (resultado == 5) {
            RequestContext.getCurrentInstance().execute("PF('errorTablaSinRastro').show()");
         }
      } else if (administrarRastros.verificarHistoricosTabla("DEMANDAS")) {
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
   }

   public void recordarSeleccionVD() {
      if (demandaTablaSeleccionada != null) {
         FacesContext c = FacesContext.getCurrentInstance();
         tablaC = (DataTable) c.getViewRoot().findComponent("form:datosDemanda");
         tablaC.setSelection(demandaTablaSeleccionada);
      }
   }

   public void habilitarBotonLov() {
      activarLov = false;
      RequestContext.getCurrentInstance().update("form:listaValores");
   }

   public void deshabilitarBotonLov() {
      activarLov = true;
      RequestContext.getCurrentInstance().update("form:listaValores");
   }

   //GET - SET 
   public List<Demandas> getListDemandasEmpleado() {
      try {
         if (listDemandasEmpleado == null) {
            if (empleado.getSecuencia() != null) {
               listDemandasEmpleado = administrarEmplDemanda.listDemandasEmpleadoSecuencia(empleado.getSecuencia());
//                    if (listDemandasEmpleado != null) {
//                        for (int i = 0; i < listDemandasEmpleado.size(); i++) {
//                            if (listDemandasEmpleado.get(i).getMotivo() == null) {
//                                listDemandasEmpleado.get(i).setMotivo(new MotivosDemandas());
//                            }
//                            String aux = listDemandasEmpleado.get(i).getSeguimiento().toUpperCase();
//                            listDemandasEmpleado.get(i).setSeguimiento(aux);
//                        }
//                    }
            }
         }
         return listDemandasEmpleado;
      } catch (Exception e) {
         System.out.println("Error en getListDemandasEmpleado : " + e.toString());
         return null;
      }
   }

   public void setListDemandasEmpleado(List<Demandas> setListDemandasEmpleado) {
      this.listDemandasEmpleado = setListDemandasEmpleado;
   }

   public List<Demandas> getFiltrarListDemandasEmpleado() {
      return filtrarListDemandasEmpleado;
   }

   public void setFiltrarListDemandasEmpleado(List<Demandas> setFiltrarListDemandasEmpleado) {
      this.filtrarListDemandasEmpleado = setFiltrarListDemandasEmpleado;
   }

   public List<MotivosDemandas> getLovMotivosDemandas() {
      try {
         lovMotivosDemandas = administrarEmplDemanda.listMotivosDemandas();
         return lovMotivosDemandas;
      } catch (Exception e) {
         System.out.println("Error getListEmpresas " + e.toString());
         return null;
      }
   }

   public void setLovMotivosDemandas(List<MotivosDemandas> setListMotivosDemandas) {
      this.lovMotivosDemandas = setListMotivosDemandas;
   }

   public MotivosDemandas getMotivoDemandaSeleccionado() {
      return motivoDemandaSeleccionado;
   }

   public void setMotivoDemandaSeleccionado(MotivosDemandas setMotivoDemandaSeleccionado) {
      this.motivoDemandaSeleccionado = setMotivoDemandaSeleccionado;
   }

   public List<MotivosDemandas> getFiltrarListMotivosDemandas() {
      return filtrarListMotivosDemandas;
   }

   public void setFiltrarListMotivosDemandas(List<MotivosDemandas> setFiltrarListMotivosDemandas) {
      this.filtrarListMotivosDemandas = setFiltrarListMotivosDemandas;
   }

   public List<Demandas> getListDemandaEmpleadoModificar() {
      return listDemandaEmpleadoModificar;
   }

   public void setListDemandaEmpleadoModificar(List<Demandas> setListDemandaEmpleadoModificar) {
      this.listDemandaEmpleadoModificar = setListDemandaEmpleadoModificar;
   }

   public Demandas getNuevaDemandaEmpleado() {
      return nuevaDemandaEmpleado;
   }

   public void setNuevaDemandaEmpleado(Demandas setNuevaDemandaEmpleado) {
      this.nuevaDemandaEmpleado = setNuevaDemandaEmpleado;
   }

   public List<Demandas> getListDemandaEmpleadoBorrar() {
      return listDemandaEmpleadoBorrar;
   }

   public void setListDemandaEmpleadoBorrar(List<Demandas> getListDemandaEmpleadoBorrar) {
      this.listDemandaEmpleadoBorrar = getListDemandaEmpleadoBorrar;
   }

   public Demandas getEditarDemandaEmpleado() {
      return editarDemandaEmpleado;
   }

   public void setEditarDemandaEmpleado(Demandas setEditarDemandaEmpleado) {
      this.editarDemandaEmpleado = setEditarDemandaEmpleado;
   }

   public Demandas getDuplicarDemandaEmpleado() {
      return duplicarDemandaEmpleado;
   }

   public void setDuplicarDemandaEmpleado(Demandas setDuplicarDemandaEmpleado) {
      this.duplicarDemandaEmpleado = setDuplicarDemandaEmpleado;
   }

   public List<Demandas> getListDemandaEmpleadoCrear() {
      return listDemandaEmpleadoCrear;
   }

   public void setListDemandaEmpleadoCrear(List<Demandas> setListDemandaEmpleadoCrear) {
      this.listDemandaEmpleadoCrear = setListDemandaEmpleadoCrear;
   }

   public boolean isAceptar() {
      return aceptar;
   }

   public void setAceptar(boolean aceptar) {
      this.aceptar = aceptar;
   }

   public Empleados getEmpleado() {
      return empleado;
   }

   public boolean isGuardado() {
      return guardado;
   }

   public void setGuardado(boolean guardado) {
      this.guardado = guardado;
   }

   public String getInfoRegistro() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosDemanda");
      infoRegistro = String.valueOf(tabla.getRowCount());
      return infoRegistro;
   }

   public void setInfoRegistro(String infoRegistro) {
      this.infoRegistro = infoRegistro;
   }

   public String getInfoRegistroMotivo() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovDemanda");
      infoRegistroMotivo = String.valueOf(tabla.getRowCount());
      return infoRegistroMotivo;
   }

   public void setInfoRegistroMotivo(String infoRegistroMotivo) {
      this.infoRegistroMotivo = infoRegistroMotivo;
   }

   public Demandas getDemandaTablaSeleccionada() {
      getListDemandasEmpleado();
      if (listDemandasEmpleado != null) {
         int tam = listDemandasEmpleado.size();
         if (tam > 0) {
            demandaTablaSeleccionada = listDemandasEmpleado.get(0);
         }
      }
      return demandaTablaSeleccionada;
   }

   public void setDemandaTablaSeleccionada(Demandas demandaTablaSeleccionada) {
      this.demandaTablaSeleccionada = demandaTablaSeleccionada;
   }

   public String getAltoTabla() {
      return altoTabla;
   }

   public void setAltoTabla(String altoTabla) {
      this.altoTabla = altoTabla;
   }

   public boolean isActivarLov() {
      return activarLov;
   }

   public void setActivarLov(boolean activarLov) {
      this.activarLov = activarLov;
   }

}
