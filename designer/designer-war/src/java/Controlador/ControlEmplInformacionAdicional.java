/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Empleados;
import Entidades.GruposInfAdicionales;
import Entidades.InformacionesAdicionales;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarEmplInformacionAdicionalInterface;
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
public class ControlEmplInformacionAdicional implements Serializable {

   @EJB
   AdministrarEmplInformacionAdicionalInterface administrarEmplInformacionAdicional;
   @EJB
   AdministrarRastrosInterface administrarRastros;
   private List<InformacionesAdicionales> listInformacionAdicional;
   private List<InformacionesAdicionales> filtrarListInformacionAdicional;
   private InformacionesAdicionales informacionTablaSeleccionada;
   private List<GruposInfAdicionales> listGruposInfAdicional;
   private GruposInfAdicionales grupoSelecionado;
   private List<GruposInfAdicionales> filtrarListGruposInfAdicional;
   private Empleados empleado;
   private int tipoActualizacion;
   private int bandera;
   private Column infoAdFechaInicial, infoAdFechaFinal, infoAdGrupo, infoAdCaracter, infoAdNumerico, infoAdFecha, infoAdObservacion;
   private boolean aceptar;
   private List<InformacionesAdicionales> listInfoAdicionalModificar;
   private boolean guardado;
   public InformacionesAdicionales nuevaInfoAdicional;
   private List<InformacionesAdicionales> listInfoAdicionalCrear;
   private BigInteger l;
   private int k;
   private List<InformacionesAdicionales> listInfoAdicionalBorrar;
   private InformacionesAdicionales editarInfoAdicional;
   private int cualCelda, tipoLista;
   private InformacionesAdicionales duplicarInfoAdicional;
   private String grupo;
   private boolean permitirIndex;
   private BigInteger backUpSecRegistro;
   private Date fechaParametro;
   private Date fechaIni, fechaFin;
   private String altoTabla;
   private String infoRegistro;
   private String infoRegistroGrupo;
   private DataTable tablaC;
   private boolean activarLov;
   private boolean activarCaracter, activarNumero, activarFecha;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

   public ControlEmplInformacionAdicional() {
      altoTabla = "305";
      informacionTablaSeleccionada = null;
      grupoSelecionado = new GruposInfAdicionales();
      backUpSecRegistro = null;
      listInformacionAdicional = null;
      listGruposInfAdicional = null;
      empleado = new Empleados();
      aceptar = true;
      listInfoAdicionalBorrar = new ArrayList<InformacionesAdicionales>();
      listInfoAdicionalCrear = new ArrayList<InformacionesAdicionales>();
      k = 0;
      listInfoAdicionalModificar = new ArrayList<InformacionesAdicionales>();
      editarInfoAdicional = new InformacionesAdicionales();
      cualCelda = -1;
      tipoLista = 0;
      guardado = true;
      nuevaInfoAdicional = new InformacionesAdicionales();
      nuevaInfoAdicional.setGrupo(new GruposInfAdicionales());
      duplicarInfoAdicional = new InformacionesAdicionales();
      duplicarInfoAdicional.setGrupo(new GruposInfAdicionales());
      permitirIndex = true;
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
      /*if (pag.equals("atras")) {
         pag = paginaAnterior;
         paginaAnterior = "nominaf";
         controlListaNavegacion.quitarPagina(pagActual);

      } else {
         */
String pagActual = "emplinformacionadicional";
         
         
         


         
         
         
         
         
         
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

   }

   @PostConstruct
   public void inicializarAdministrador() {
      try {
         FacesContext x = FacesContext.getCurrentInstance();
         HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
         administrarEmplInformacionAdicional.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
      } catch (Exception e) {
         System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
         System.out.println("Causa: " + e.getCause());
      }
   }

   public void recibirEmpleado(BigInteger empl) {
      listInformacionAdicional = null;
      empleado = administrarEmplInformacionAdicional.empleadoActual(empl);
      getListInformacionAdicional();
      deshabilitarBotonLov();
      if (!listInformacionAdicional.isEmpty()) {
         informacionTablaSeleccionada = listInformacionAdicional.get(0);
      }
   }

   public void modificarInfoAd(InformacionesAdicionales informacionAdicional) {
      informacionTablaSeleccionada = informacionAdicional;
      System.out.println("informacionTablaSeleccionada.getTipodato() : " + informacionTablaSeleccionada.getTipodato());
      if (!listInfoAdicionalCrear.contains(informacionTablaSeleccionada)) {

         if (listInfoAdicionalModificar.isEmpty()) {
            listInfoAdicionalModificar.add(informacionTablaSeleccionada);
         } else if (!listInfoAdicionalModificar.contains(informacionTablaSeleccionada)) {
            listInfoAdicionalModificar.add(informacionTablaSeleccionada);
         }
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      }
   }

   public void modificarTipoDato(InformacionesAdicionales informacionAdicional, String valorConfirmar) {

      informacionTablaSeleccionada = informacionAdicional;
      System.out.println("informacionTablaSeleccionada.getTipodato() : " + informacionTablaSeleccionada.getTipodato());
      if (valorConfirmar.equals("CARACTER")) {
         informacionTablaSeleccionada.setTipodato("C");
         activarCaracter = false;
         activarFecha = true;
         activarNumero = true;
//            RequestContext.getCurrentInstance().update("form:editarCaracter");
//            RequestContext.getCurrentInstance().update("form:editarNumerico");
//            RequestContext.getCurrentInstance().update("form:editFecha");
      } else if (valorConfirmar.equals("FECHA")) {
         informacionTablaSeleccionada.setTipodato("F");
         activarCaracter = true;
         activarFecha = false;
         activarNumero = true;
//            RequestContext.getCurrentInstance().update("form:editarCaracter");
//            RequestContext.getCurrentInstance().update("form:editarNumerico");
//            RequestContext.getCurrentInstance().update("form:editFecha");
      } else if (valorConfirmar.equals("NUMERICO")) {
         informacionTablaSeleccionada.setTipodato("N");
         activarCaracter = true;
         activarFecha = true;
         activarNumero = false;
//            RequestContext.getCurrentInstance().update("form:editarCaracter");
//            RequestContext.getCurrentInstance().update("form:editarNumerico");
//            RequestContext.getCurrentInstance().update("form:editFecha");
      }

      if (!listInfoAdicionalCrear.contains(informacionTablaSeleccionada)) {

         if (listInfoAdicionalModificar.isEmpty()) {
            listInfoAdicionalModificar.add(informacionTablaSeleccionada);
         } else if (!listInfoAdicionalModificar.contains(informacionTablaSeleccionada)) {
            listInfoAdicionalModificar.add(informacionTablaSeleccionada);
         }
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      }
      RequestContext.getCurrentInstance().update("form:datosInfoAdEmpleado");
   }

   public void modificarInfoAd(InformacionesAdicionales informacionAdicional, String confirmarCambio, String valorConfirmar) {
      informacionTablaSeleccionada = informacionAdicional;
      int coincidencias = 0;
      // int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      if (confirmarCambio.equalsIgnoreCase("GRUPO")) {
         if (!valorConfirmar.isEmpty()) {
            if (tipoLista == 0) {
               informacionTablaSeleccionada.getGrupo().setDescripcion(grupo);
            } else {
               informacionTablaSeleccionada.getGrupo().setDescripcion(grupo);
            }
            //   for (int i = 0; i < listGruposInfAdicional.size(); i++) {
            if (informacionTablaSeleccionada.getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
               //   indiceUnicoElemento = i;
               coincidencias++;
            }
            
            if (coincidencias == 1) {
               if (tipoLista == 0) {
                  informacionTablaSeleccionada.setGrupo(grupoSelecionado);
               } else {
                  informacionTablaSeleccionada.setGrupo(grupoSelecionado);
               }
               listGruposInfAdicional.clear();
               getListGruposInfAdicional();
            } else {
               permitirIndex = false;
               RequestContext.getCurrentInstance().update("form:GrupoDialogo");
               RequestContext.getCurrentInstance().execute("PF('GrupoDialogo').show()");
               tipoActualizacion = 0;
            }
         } else {
            if (tipoLista == 0) {
               informacionTablaSeleccionada.setGrupo(new GruposInfAdicionales());
            } else {
               informacionTablaSeleccionada.setGrupo(new GruposInfAdicionales());
            }
            coincidencias = 1;
         }
      }
      if (coincidencias == 1) {
         if (tipoLista == 0) {
            if (!listInfoAdicionalCrear.contains(informacionTablaSeleccionada)) {

               if (listInfoAdicionalModificar.isEmpty()) {
                  listInfoAdicionalModificar.add(informacionTablaSeleccionada);
               } else if (!listInfoAdicionalModificar.contains(informacionTablaSeleccionada)) {
                  listInfoAdicionalModificar.add(informacionTablaSeleccionada);
               }
               if (guardado == true) {
                  guardado = false;
                  RequestContext.getCurrentInstance().update("form:ACEPTAR");
               }
            }
         } else if (!listInfoAdicionalCrear.contains(informacionTablaSeleccionada)) {

            if (listInfoAdicionalModificar.isEmpty()) {
               listInfoAdicionalModificar.add(informacionTablaSeleccionada);
            } else if (!listInfoAdicionalModificar.contains(informacionTablaSeleccionada)) {
               listInfoAdicionalModificar.add(informacionTablaSeleccionada);
            }
            if (guardado == true) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
         }
      }
      RequestContext.getCurrentInstance().update("form:datosInfoAdEmpleado");
   }

   public boolean validarFechasRegistro(int i) {
      fechaParametro = new Date();
      fechaParametro.setYear(0);
      fechaParametro.setMonth(1);
      fechaParametro.setDate(1);
      boolean retorno = true;
      if (i == 0) {
         InformacionesAdicionales auxiliar = null;
         if (tipoLista == 0) {
            auxiliar = informacionTablaSeleccionada;
         }
         if (tipoLista == 1) {
            auxiliar = informacionTablaSeleccionada;
         }
         if (auxiliar.getFechainicial().after(fechaParametro) && auxiliar.getFechainicial().before(auxiliar.getFechafinal())) {
            retorno = true;
         } else {
            retorno = false;
         }

      }
      if (i == 1) {
         if (nuevaInfoAdicional.getFechainicial().after(fechaParametro) && nuevaInfoAdicional.getFechainicial().before(nuevaInfoAdicional.getFechafinal())) {
            retorno = true;
         } else {
            retorno = false;
         }

      }
      if (i == 2) {
         if (duplicarInfoAdicional.getFechainicial().after(fechaParametro) && duplicarInfoAdicional.getFechainicial().before(duplicarInfoAdicional.getFechafinal())) {
            retorno = true;
         } else {
            retorno = false;
         }

      }
      return retorno;
   }

   public void modificarFechas(InformacionesAdicionales informacionAdicional, int c) {
      InformacionesAdicionales auxiliar = null;
      if (tipoLista == 0) {
         auxiliar = informacionTablaSeleccionada;
      }
      if (tipoLista == 1) {
         auxiliar = informacionTablaSeleccionada;
      }
      if (auxiliar.getFechainicial() != null && auxiliar.getFechafinal() != null) {
         boolean retorno = false;
         retorno = validarFechasRegistro(0);
         if (retorno == true) {
            cambiarIndice(informacionAdicional, c);
            modificarInfoAd(informacionAdicional);
         } else {
            if (tipoLista == 0) {
               informacionTablaSeleccionada.setFechafinal(fechaFin);
               informacionTablaSeleccionada.setFechainicial(fechaIni);
            }
            if (tipoLista == 1) {
               informacionTablaSeleccionada.setFechafinal(fechaFin);
               informacionTablaSeleccionada.setFechainicial(fechaIni);

            }
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosInfoAdEmpleado");
            RequestContext.getCurrentInstance().execute("PF('form:errorFechas').show()");
         }
      } else {
         if (tipoLista == 0) {
            informacionTablaSeleccionada.setFechafinal(fechaFin);
            informacionTablaSeleccionada.setFechainicial(fechaIni);
         }
         if (tipoLista == 1) {
            informacionTablaSeleccionada.setFechafinal(fechaFin);
            informacionTablaSeleccionada.setFechainicial(fechaIni);

         }
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:datosInfoAdEmpleado");
         RequestContext.getCurrentInstance().execute("PF('errorRegNew').show()");
      }
   }

   public void valoresBackupAutocompletar(int tipoNuevo, String Campo) {
      if (Campo.equals("GRUPO")) {
         if (tipoNuevo == 1) {
            grupo = nuevaInfoAdicional.getGrupo().getDescripcion();
         } else if (tipoNuevo == 2) {
            grupo = duplicarInfoAdicional.getGrupo().getDescripcion();
         }
      }
   }

   public void autocompletarNuevoyDuplicado(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      if (confirmarCambio.equalsIgnoreCase("GRUPO")) {
         if (!valorConfirmar.isEmpty()) {
            if (tipoNuevo == 1) {
               nuevaInfoAdicional.getGrupo().setDescripcion(grupo);
            } else if (tipoNuevo == 2) {
               duplicarInfoAdicional.getGrupo().setDescripcion(grupo);
            }
            for (int i = 0; i < listGruposInfAdicional.size(); i++) {
               if (listGruposInfAdicional.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               if (tipoNuevo == 1) {
                  nuevaInfoAdicional.setGrupo(listGruposInfAdicional.get(indiceUnicoElemento));
                  RequestContext.getCurrentInstance().update("formularioDialogos:nuevaGrupo");
               } else if (tipoNuevo == 2) {
                  duplicarInfoAdicional.setGrupo(listGruposInfAdicional.get(indiceUnicoElemento));
                  RequestContext.getCurrentInstance().update("formularioDialogos:duplicarGrupo");
               }
               listGruposInfAdicional.clear();
               getListGruposInfAdicional();
            } else {
               RequestContext.getCurrentInstance().update("form:GrupoDialogo");
               RequestContext.getCurrentInstance().execute("PF('GrupoDialogo').show()");
               tipoActualizacion = tipoNuevo;
               if (tipoNuevo == 1) {
                  RequestContext.getCurrentInstance().update("formularioDialogos:nuevaGrupo");
               } else if (tipoNuevo == 2) {
                  RequestContext.getCurrentInstance().update("formularioDialogos:duplicarGrupo");
               }
            }
         } else if (tipoNuevo == 1) {
            nuevaInfoAdicional.setGrupo(new GruposInfAdicionales());
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaGrupo");
         } else if (tipoNuevo == 2) {
            duplicarInfoAdicional.setGrupo(new GruposInfAdicionales());
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarGrupo");
         }
      }
   }

   public void cambiarIndice(InformacionesAdicionales informacionAdicional, int celda) {
      if (permitirIndex == true) {
         informacionTablaSeleccionada = informacionAdicional;
         cualCelda = celda;
         if (tipoLista == 0) {
            deshabilitarBotonLov();
            informacionTablaSeleccionada.getSecuencia();
            fechaFin = informacionTablaSeleccionada.getFechafinal();
            fechaIni = informacionTablaSeleccionada.getFechainicial();
            if (cualCelda == 2) {
               habilitarBotonLov();
               contarRegistroGrupo();
               grupo = informacionTablaSeleccionada.getGrupo().getDescripcion();
            }
         }
         if (tipoLista == 1) {
            deshabilitarBotonLov();
            informacionTablaSeleccionada.getSecuencia();
            fechaFin = informacionTablaSeleccionada.getFechafinal();
            fechaIni = informacionTablaSeleccionada.getFechainicial();
            if (cualCelda == 2) {
               habilitarBotonLov();
               contarRegistroGrupo();
               grupo = informacionTablaSeleccionada.getGrupo().getDescripcion();
            }
         }
      }
   }

   public void guardarYSalir() {
      guardarCambiosInfoAd();
      salir();
   }

   public void guardarCambiosInfoAd() {
      RequestContext context = RequestContext.getCurrentInstance();
      try {
         if (guardado == false) {
            if (!listInfoAdicionalBorrar.isEmpty()) {
               administrarEmplInformacionAdicional.borrarInformacionAdicional(listInfoAdicionalBorrar);
               listInfoAdicionalBorrar.clear();
            }
            if (!listInfoAdicionalCrear.isEmpty()) {
               administrarEmplInformacionAdicional.crearInformacionAdicional(listInfoAdicionalCrear);
               listInfoAdicionalCrear.clear();
            }
            if (!listInfoAdicionalModificar.isEmpty()) {
               administrarEmplInformacionAdicional.modificarInformacionAdicional(listInfoAdicionalModificar);
               listInfoAdicionalModificar.clear();
            }
            listInformacionAdicional = null;
            getListInformacionAdicional();
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:infoRegistro");
            RequestContext.getCurrentInstance().update("form:datosInfoAdEmpleado");
            guardado = true;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            k = 0;
            informacionTablaSeleccionada = null;
            RequestContext.getCurrentInstance().update("form:infoRegistro");
            FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con Éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
         }
      } catch (Exception e) {
         System.out.println("Error guardarCambios : " + e.toString());
         FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado, intente nuevamente.");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }
   }
   //CANCELAR MODIFICACIONES

   public void cancelarModificacion() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 1) {
         //CERRAR FILTRADO
         altoTabla = "305";
         infoAdFechaInicial = (Column) c.getViewRoot().findComponent("form:datosInfoAdEmpleado:infoAdFechaInicial");
         infoAdFechaInicial.setFilterStyle("display: none; visibility: hidden;");
         infoAdFechaFinal = (Column) c.getViewRoot().findComponent("form:datosInfoAdEmpleado:infoAdFechaFinal");
         infoAdFechaFinal.setFilterStyle("display: none; visibility: hidden;");
         infoAdGrupo = (Column) c.getViewRoot().findComponent("form:datosInfoAdEmpleado:infoAdGrupo");
         infoAdGrupo.setFilterStyle("display: none; visibility: hidden;");
         infoAdCaracter = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosInfoAdEmpleado:infoAdCaracter");
         infoAdCaracter.setFilterStyle("display: none; visibility: hidden;");
         ////
         infoAdNumerico = (Column) c.getViewRoot().findComponent("form:datosInfoAdEmpleado:infoAdNumerico");
         infoAdNumerico.setFilterStyle("display: none; visibility: hidden;");
         infoAdFecha = (Column) c.getViewRoot().findComponent("form:datosInfoAdEmpleado:infoAdFecha");
         infoAdFecha.setFilterStyle("display: none; visibility: hidden;");
         infoAdObservacion = (Column) c.getViewRoot().findComponent("form:datosInfoAdEmpleado:infoAdObservacion");
         infoAdObservacion.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosInfoAdEmpleado");
         bandera = 0;
         filtrarListInformacionAdicional = null;
         tipoLista = 0;
      }

      listInfoAdicionalBorrar.clear();
      listInfoAdicionalCrear.clear();
      listInfoAdicionalModificar.clear();
      k = 0;
      listInformacionAdicional = null;
      getListInformacionAdicional();
      informacionTablaSeleccionada = null;
      contarRegistros();
      guardado = true;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:infoRegistro");
      RequestContext.getCurrentInstance().update("form:datosInfoAdEmpleado");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void editarCelda() {
      if (informacionTablaSeleccionada != null) {
         if (tipoLista == 0) {
            editarInfoAdicional = informacionTablaSeleccionada;
         }
         if (tipoLista == 1) {
            editarInfoAdicional = informacionTablaSeleccionada;
         }

         RequestContext context = RequestContext.getCurrentInstance();
         if (cualCelda == 0) {
            deshabilitarBotonLov();
            RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaInicialD");
            RequestContext.getCurrentInstance().execute("PF('editarFechaInicialD').show()");
            cualCelda = -1;
         } else if (cualCelda == 1) {
            deshabilitarBotonLov();
            RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaFinalD");
            RequestContext.getCurrentInstance().execute("PF('editarFechaFinalD').show()");
            cualCelda = -1;
         } else if (cualCelda == 2) {
            habilitarBotonLov();
            RequestContext.getCurrentInstance().update("formularioDialogos:editarGrupoD");
            RequestContext.getCurrentInstance().execute("PF('editarGrupoD').show()");
            cualCelda = -1;
         } else if (cualCelda == 3) {
            deshabilitarBotonLov();
            RequestContext.getCurrentInstance().update("formularioDialogos:editarCaracterD");
            RequestContext.getCurrentInstance().execute("PF('editarCaracterD').show()");
            cualCelda = -1;
         } else if (cualCelda == 4) {
            deshabilitarBotonLov();
            RequestContext.getCurrentInstance().update("formularioDialogos:editarNumericoD");
            RequestContext.getCurrentInstance().execute("PF('editarNumericoD').show()");
            cualCelda = -1;
         } else if (cualCelda == 5) {
            deshabilitarBotonLov();
            RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaD");
            RequestContext.getCurrentInstance().execute("PF('editarFechaD').show()");
            cualCelda = -1;
         } else if (cualCelda == 6) {
            deshabilitarBotonLov();
            RequestContext.getCurrentInstance().update("formularioDialogos:editarObservacionD");
            RequestContext.getCurrentInstance().execute("PF('editarObservacionD').show()");
            cualCelda = -1;
         }

      } else {
         RequestContext.getCurrentInstance().execute("PF('formularioDialogos:seleccionarRegistro').show()");
      }
   }

   public void agregarNuevaInfoAd() {
      if (nuevaInfoAdicional.getFechafinal() != null && nuevaInfoAdicional.getFechainicial() != null && nuevaInfoAdicional.getTipodato() != null) {
         if (validarFechasRegistro(1) == true) {
            FacesContext c = FacesContext.getCurrentInstance();
            if (bandera == 1) {
               //CERRAR FILTRADO
               infoAdFechaInicial = (Column) c.getViewRoot().findComponent("form:datosInfoAdEmpleado:infoAdFechaInicial");
               infoAdFechaInicial.setFilterStyle("display: none; visibility: hidden;");
               infoAdFechaFinal = (Column) c.getViewRoot().findComponent("form:datosInfoAdEmpleado:infoAdFechaFinal");
               infoAdFechaFinal.setFilterStyle("display: none; visibility: hidden;");
               infoAdGrupo = (Column) c.getViewRoot().findComponent("form:datosInfoAdEmpleado:infoAdGrupo");
               infoAdGrupo.setFilterStyle("display: none; visibility: hidden;");
               infoAdCaracter = (Column) c.getViewRoot().findComponent("form:datosInfoAdEmpleado:infoAdCaracter");
               infoAdCaracter.setFilterStyle("display: none; visibility: hidden;");
               ////
               infoAdNumerico = (Column) c.getViewRoot().findComponent("form:datosInfoAdEmpleado:infoAdNumerico");
               infoAdNumerico.setFilterStyle("display: none; visibility: hidden;");
               infoAdFecha = (Column) c.getViewRoot().findComponent("form:datosInfoAdEmpleado:infoAdFecha");
               infoAdFecha.setFilterStyle("display: none; visibility: hidden;");
               infoAdObservacion = (Column) c.getViewRoot().findComponent("form:datosInfoAdEmpleado:infoAdObservacion");
               infoAdObservacion.setFilterStyle("display: none; visibility: hidden;");
               RequestContext.getCurrentInstance().update("form:datosInfoAdEmpleado");
               bandera = 0;
               filtrarListInformacionAdicional = null;
               tipoLista = 0;
               altoTabla = "305";
            }
            //AGREGAR REGISTRO A LA LISTA VIGENCIAS CARGOS EMPLEADO.
            k++;
            l = BigInteger.valueOf(k);
            nuevaInfoAdicional.setSecuencia(l);
            nuevaInfoAdicional.setEmpleado(empleado);
            listInfoAdicionalCrear.add(nuevaInfoAdicional);
            listInformacionAdicional.add(nuevaInfoAdicional);
            informacionTablaSeleccionada = nuevaInfoAdicional;
            nuevaInfoAdicional = new InformacionesAdicionales();
            nuevaInfoAdicional.setGrupo(new GruposInfAdicionales());
            RequestContext context = RequestContext.getCurrentInstance();
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:infoRegistro");
            RequestContext.getCurrentInstance().update("form:datosInfoAdEmpleado");
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroInfoAd').hide()");
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
         RequestContext.getCurrentInstance().execute("PF('errorRegNew').show()");
      }
   }

   public void limpiarNuevaInfoAd() {
      nuevaInfoAdicional = new InformacionesAdicionales();
      nuevaInfoAdicional.setGrupo(new GruposInfAdicionales());
   }

   public void duplicarInfoAd() {
      if (informacionTablaSeleccionada != null) {
         duplicarInfoAdicional = new InformacionesAdicionales();

         if (tipoLista == 0) {
            duplicarInfoAdicional.setEmpleado(informacionTablaSeleccionada.getEmpleado());
            duplicarInfoAdicional.setFechainicial(informacionTablaSeleccionada.getFechainicial());
            duplicarInfoAdicional.setFechafinal(informacionTablaSeleccionada.getFechafinal());
            duplicarInfoAdicional.setGrupo(informacionTablaSeleccionada.getGrupo());
            duplicarInfoAdicional.setTipodato(informacionTablaSeleccionada.getTipodato());
            duplicarInfoAdicional.setResultadocaracter(informacionTablaSeleccionada.getResultadocaracter());
            duplicarInfoAdicional.setResultadonumerico(informacionTablaSeleccionada.getResultadonumerico());
            duplicarInfoAdicional.setResultadofecha(informacionTablaSeleccionada.getResultadofecha());
            duplicarInfoAdicional.setDescripcion(informacionTablaSeleccionada.getDescripcion());
            duplicarInfoAdicional.setTipodatoTr(informacionTablaSeleccionada.getTipodatoTr());
         }
         if (tipoLista == 1) {
            duplicarInfoAdicional.setEmpleado(informacionTablaSeleccionada.getEmpleado());
            duplicarInfoAdicional.setFechainicial(informacionTablaSeleccionada.getFechainicial());
            duplicarInfoAdicional.setFechafinal(informacionTablaSeleccionada.getFechafinal());
            duplicarInfoAdicional.setGrupo(informacionTablaSeleccionada.getGrupo());
            duplicarInfoAdicional.setTipodato(informacionTablaSeleccionada.getTipodato());
            duplicarInfoAdicional.setResultadocaracter(informacionTablaSeleccionada.getResultadocaracter());
            duplicarInfoAdicional.setResultadonumerico(informacionTablaSeleccionada.getResultadonumerico());
            duplicarInfoAdicional.setResultadofecha(informacionTablaSeleccionada.getResultadofecha());
            duplicarInfoAdicional.setDescripcion(informacionTablaSeleccionada.getDescripcion());
            duplicarInfoAdicional.setTipodatoTr(informacionTablaSeleccionada.getTipodatoTr());
            altoTabla = "305";
         }

         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarInfoAd");
         RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroInfoAd').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void confirmarDuplicar() {
      if (duplicarInfoAdicional.getFechafinal() != null && duplicarInfoAdicional.getFechainicial() != null && duplicarInfoAdicional.getTipodato() != null) {
         if (validarFechasRegistro(2) == true) {
            k++;
            l = BigInteger.valueOf(k);
            duplicarInfoAdicional.setSecuencia(l);
            listInformacionAdicional.add(duplicarInfoAdicional);
            listInfoAdicionalCrear.add(duplicarInfoAdicional);
            informacionTablaSeleccionada = duplicarInfoAdicional;
            RequestContext context = RequestContext.getCurrentInstance();
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:infoRegistro");
            RequestContext.getCurrentInstance().update("form:datosInfoAdEmpleado");
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroInfoAd').hide()");
            if (guardado == true) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            FacesContext c = FacesContext.getCurrentInstance();
            if (bandera == 1) {
               //CERRAR FILTRADO
               infoAdFechaInicial = (Column) c.getViewRoot().findComponent("form:datosInfoAdEmpleado:infoAdFechaInicial");
               infoAdFechaInicial.setFilterStyle("display: none; visibility: hidden;");
               infoAdFechaFinal = (Column) c.getViewRoot().findComponent("form:datosInfoAdEmpleado:infoAdFechaFinal");
               infoAdFechaFinal.setFilterStyle("display: none; visibility: hidden;");
               infoAdGrupo = (Column) c.getViewRoot().findComponent("form:datosInfoAdEmpleado:infoAdGrupo");
               infoAdGrupo.setFilterStyle("display: none; visibility: hidden;");
               infoAdCaracter = (Column) c.getViewRoot().findComponent("form:datosInfoAdEmpleado:infoAdCaracter");
               infoAdCaracter.setFilterStyle("display: none; visibility: hidden;");
               ////
               infoAdNumerico = (Column) c.getViewRoot().findComponent("form:datosInfoAdEmpleado:infoAdNumerico");
               infoAdNumerico.setFilterStyle("display: none; visibility: hidden;");
               infoAdFecha = (Column) c.getViewRoot().findComponent("form:datosInfoAdEmpleado:infoAdFecha");
               infoAdFecha.setFilterStyle("display: none; visibility: hidden;");
               infoAdObservacion = (Column) c.getViewRoot().findComponent("form:datosInfoAdEmpleado:infoAdObservacion");
               infoAdObservacion.setFilterStyle("display: none; visibility: hidden;");
               RequestContext.getCurrentInstance().update("form:datosInfoAdEmpleado");
               bandera = 0;
               filtrarListInformacionAdicional = null;
               tipoLista = 0;
               altoTabla = "305";
            }
            duplicarInfoAdicional = new InformacionesAdicionales();
         } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('errorFechas').show()");
         }
      } else {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().execute("PF('errorRegNew').show()");
      }
   }

   public void limpiarduplicarInfoAd() {
      duplicarInfoAdicional = new InformacionesAdicionales();
      duplicarInfoAdicional.setGrupo(new GruposInfAdicionales());
   }

   public void borrarInfoAd() {

      if (informacionTablaSeleccionada != null) {
         if (!listInfoAdicionalModificar.isEmpty() && listInfoAdicionalModificar.contains(informacionTablaSeleccionada)) {
            int modIndex = listInfoAdicionalModificar.indexOf(informacionTablaSeleccionada);
            listInfoAdicionalModificar.remove(modIndex);
            listInfoAdicionalBorrar.add(informacionTablaSeleccionada);
         } else if (!listInfoAdicionalCrear.isEmpty() && listInfoAdicionalCrear.contains(informacionTablaSeleccionada)) {
            int crearIndex = listInfoAdicionalCrear.indexOf(informacionTablaSeleccionada);
            listInfoAdicionalCrear.remove(crearIndex);
         } else {
            listInfoAdicionalBorrar.add(informacionTablaSeleccionada);
         }
         listInformacionAdicional.remove(informacionTablaSeleccionada);
         if (tipoLista == 1) {
            filtrarListInformacionAdicional.remove(informacionTablaSeleccionada);
         }

         RequestContext context = RequestContext.getCurrentInstance();
         contarRegistros();
         RequestContext.getCurrentInstance().update("form:infoRegistro");
         RequestContext.getCurrentInstance().update("form:datosInfoAdEmpleado");
         informacionTablaSeleccionada = null;

         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('formularioDialogos:seleccionarRegistro').show()");
      }
   }

   public void activarCtrlF11() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 0) {
         altoTabla = "285";
         infoAdFechaInicial = (Column) c.getViewRoot().findComponent("form:datosInfoAdEmpleado:infoAdFechaInicial");
         infoAdFechaInicial.setFilterStyle("width: 85% !important");
         infoAdFechaFinal = (Column) c.getViewRoot().findComponent("form:datosInfoAdEmpleado:infoAdFechaFinal");
         infoAdFechaFinal.setFilterStyle("width: 85% !important");
         infoAdGrupo = (Column) c.getViewRoot().findComponent("form:datosInfoAdEmpleado:infoAdGrupo");
         infoAdGrupo.setFilterStyle("width: 85% !important;");
         infoAdCaracter = (Column) c.getViewRoot().findComponent("form:datosInfoAdEmpleado:infoAdCaracter");
         infoAdCaracter.setFilterStyle("width: 85% !important;");
         ////
         infoAdNumerico = (Column) c.getViewRoot().findComponent("form:datosInfoAdEmpleado:infoAdNumerico");
         infoAdNumerico.setFilterStyle("width: 85% !important");
         infoAdFecha = (Column) c.getViewRoot().findComponent("form:datosInfoAdEmpleado:infoAdFecha");
         infoAdFecha.setFilterStyle("width: 85% !important");
         infoAdObservacion = (Column) c.getViewRoot().findComponent("form:datosInfoAdEmpleado:infoAdObservacion");
         infoAdObservacion.setFilterStyle("width: 85% !important");
         RequestContext.getCurrentInstance().update("form:datosInfoAdEmpleado");
         bandera = 1;
      } else if (bandera == 1) {
         infoAdFechaInicial = (Column) c.getViewRoot().findComponent("form:datosInfoAdEmpleado:infoAdFechaInicial");
         infoAdFechaInicial.setFilterStyle("display: none; visibility: hidden;");
         infoAdFechaFinal = (Column) c.getViewRoot().findComponent("form:datosInfoAdEmpleado:infoAdFechaFinal");
         infoAdFechaFinal.setFilterStyle("display: none; visibility: hidden;");
         infoAdGrupo = (Column) c.getViewRoot().findComponent("form:datosInfoAdEmpleado:infoAdGrupo");
         infoAdGrupo.setFilterStyle("display: none; visibility: hidden;");
         infoAdCaracter = (Column) c.getViewRoot().findComponent("form:datosInfoAdEmpleado:infoAdCaracter");
         infoAdCaracter.setFilterStyle("display: none; visibility: hidden;");
         ////
         infoAdNumerico = (Column) c.getViewRoot().findComponent("form:datosInfoAdEmpleado:infoAdNumerico");
         infoAdNumerico.setFilterStyle("display: none; visibility: hidden;");
         infoAdFecha = (Column) c.getViewRoot().findComponent("form:datosInfoAdEmpleado:infoAdFecha");
         infoAdFecha.setFilterStyle("display: none; visibility: hidden;");
         infoAdObservacion = (Column) c.getViewRoot().findComponent("form:datosInfoAdEmpleado:infoAdObservacion");
         infoAdObservacion.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosInfoAdEmpleado");
         bandera = 0;
         filtrarListInformacionAdicional = null;
         tipoLista = 0;
         altoTabla = "305";

      }
   }

   public void salir() {
      limpiarListasValor();
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 1) {
         altoTabla = "305";
         infoAdFechaInicial = (Column) c.getViewRoot().findComponent("form:datosInfoAdEmpleado:infoAdFechaInicial");
         infoAdFechaInicial.setFilterStyle("display: none; visibility: hidden;");
         infoAdFechaFinal = (Column) c.getViewRoot().findComponent("form:datosInfoAdEmpleado:infoAdFechaFinal");
         infoAdFechaFinal.setFilterStyle("display: none; visibility: hidden;");
         infoAdGrupo = (Column) c.getViewRoot().findComponent("form:datosInfoAdEmpleado:infoAdGrupo");
         infoAdGrupo.setFilterStyle("display: none; visibility: hidden;");
         infoAdCaracter = (Column) c.getViewRoot().findComponent("form:datosInfoAdEmpleado:infoAdCaracter");
         infoAdCaracter.setFilterStyle("display: none; visibility: hidden;");
         ////
         infoAdNumerico = (Column) c.getViewRoot().findComponent("form:datosInfoAdEmpleado:infoAdNumerico");
         infoAdNumerico.setFilterStyle("display: none; visibility: hidden;");
         infoAdFecha = (Column) c.getViewRoot().findComponent("form:datosInfoAdEmpleado:infoAdFecha");
         infoAdFecha.setFilterStyle("display: none; visibility: hidden;");
         infoAdObservacion = (Column) c.getViewRoot().findComponent("form:datosInfoAdEmpleado:infoAdObservacion");
         infoAdObservacion.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosInfoAdEmpleado");
         bandera = 0;
         filtrarListInformacionAdicional = null;
         tipoLista = 0;
      }
      listInfoAdicionalBorrar.clear();
      listInfoAdicionalCrear.clear();
      listInfoAdicionalModificar.clear();
      informacionTablaSeleccionada = null;
      listInformacionAdicional = null;
      getListInformacionAdicional();
      contarRegistros();
      k = 0;
      guardado = true;
      navegar("atras");
   }

   public void asignarIndex(InformacionesAdicionales informacionAdicional, int list, int LND) {
      informacionTablaSeleccionada = informacionAdicional;
      RequestContext context = RequestContext.getCurrentInstance();
      tipoActualizacion = LND;
      habilitarBotonLov();
      contarRegistroGrupo();
      RequestContext.getCurrentInstance().update("form:GrupoDialogo");
      RequestContext.getCurrentInstance().execute("PF('GrupoDialogo').show()");
   }

   public void actualizarGrupo() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {
         if (tipoLista == 0) {
            informacionTablaSeleccionada.setGrupo(grupoSelecionado);
            if (!listInfoAdicionalCrear.contains(informacionTablaSeleccionada)) {
               if (listInfoAdicionalModificar.isEmpty()) {
                  listInfoAdicionalModificar.add(informacionTablaSeleccionada);
               } else if (!listInfoAdicionalModificar.contains(informacionTablaSeleccionada)) {
                  listInfoAdicionalModificar.add(informacionTablaSeleccionada);
               }
            }
         } else {
            informacionTablaSeleccionada.setGrupo(grupoSelecionado);
            if (!listInfoAdicionalCrear.contains(informacionTablaSeleccionada)) {
               if (listInfoAdicionalModificar.isEmpty()) {
                  listInfoAdicionalModificar.add(informacionTablaSeleccionada);
               } else if (!listInfoAdicionalModificar.contains(informacionTablaSeleccionada)) {
                  listInfoAdicionalModificar.add(informacionTablaSeleccionada);
               }
            }
         }
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         permitirIndex = true;
      } else if (tipoActualizacion == 1) {
         nuevaInfoAdicional.setGrupo(grupoSelecionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaGrupo");
      } else if (tipoActualizacion == 2) {
         duplicarInfoAdicional.setGrupo(grupoSelecionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarGrupo");
      }
      filtrarListGruposInfAdicional = null;
      grupoSelecionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      RequestContext.getCurrentInstance().update("form:GrupoDialogo");
      RequestContext.getCurrentInstance().update("form:lovGrupo");
      RequestContext.getCurrentInstance().update("form:aceptarG");
      context.reset("form:lovGrupo:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('GrupoDialogo').hide()");
   }

   public void cancelarCambioGrupo() {
      filtrarListGruposInfAdicional = null;
      grupoSelecionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      permitirIndex = true;
   }

   public void listaValoresBoton() {
      if (informacionTablaSeleccionada != null) {
         RequestContext context = RequestContext.getCurrentInstance();
         if (cualCelda == 2) {
            habilitarBotonLov();
            contarRegistroGrupo();
            RequestContext.getCurrentInstance().update("form:GrupoDialogo");
            RequestContext.getCurrentInstance().execute("PF('GrupoDialogo').show()");
            tipoActualizacion = 0;
         }
      }
   }

   public void activarAceptar() {
      aceptar = false;
   }
   //EXPORTAR

   /**
    * Metodo que exporta datos a PDF
    *
    * @throws IOException Excepcion de In-Out de datos
    */
   public void exportPDF() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosIAEmpleadoExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "InformacionAdicionalPDF", false, false, "UTF-8", null, null);
      context.responseComplete();
      informacionTablaSeleccionada = null;
   }

   /**
    * Metodo que exporta datos a XLS
    *
    * @throws IOException Excepcion de In-Out de datos
    */
   public void exportXLS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosIAEmpleadoExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "InformacionAdicionalXLS", false, false, "UTF-8", null, null);
      context.responseComplete();
      informacionTablaSeleccionada = null;
   }

   public void verificarRastro() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (listInformacionAdicional != null) {
         if (informacionTablaSeleccionada != null) {
            int resultado = administrarRastros.obtenerTabla(informacionTablaSeleccionada.getSecuencia(), "INFORMACIONESADICIONALES");
            backUpSecRegistro = informacionTablaSeleccionada.getSecuencia();
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
         }
      } else if (administrarRastros.verificarHistoricosTabla("INFORMACIONESADICIONALES")) {
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
      informacionTablaSeleccionada = null;
   }

   public void eventoFiltrar() {
      if (tipoLista == 0) {
         tipoLista = 1;
      }
      RequestContext context = RequestContext.getCurrentInstance();
      contarRegistros();
   }

   public void recordarSeleccion() {
      if (informacionTablaSeleccionada != null) {
         FacesContext c = FacesContext.getCurrentInstance();
         tablaC = (DataTable) c.getViewRoot().findComponent("form:datosInfoAdEmpleado");
         tablaC.setSelection(informacionTablaSeleccionada);
      }
   }

   public void contarRegistroGrupo() {
      RequestContext.getCurrentInstance().update("form:infoRegistroGrupo");
   }

   public void contarRegistros() {
      RequestContext.getCurrentInstance().update("form:infoRegistro");
   }

   public void habilitarBotonLov() {
      activarLov = false;
      RequestContext.getCurrentInstance().update("form:listaValores");
   }

   public void deshabilitarBotonLov() {
      activarLov = true;
      RequestContext.getCurrentInstance().update("form:listaValores");
   }

   ////////////////////////////////GETS Y SETS////////////////////////////////////
   public List<InformacionesAdicionales> getListInformacionAdicional() {
      try {
         if (listInformacionAdicional == null) {
            if (empleado.getSecuencia() != null) {
               listInformacionAdicional = administrarEmplInformacionAdicional.listInformacionesAdicionalesEmpleado(empleado.getSecuencia());
               if (listInformacionAdicional != null) {
                  for (int i = 0; i < listInformacionAdicional.size(); i++) {
                     if (listInformacionAdicional.get(i).getGrupo() == null) {
                        listInformacionAdicional.get(i).setGrupo(new GruposInfAdicionales());
                     }
                  }
               }
            }

         }
         return listInformacionAdicional;

      } catch (Exception e) {
         System.out.println("Error...!! getListInformacionAdicional : " + e.toString());
         return null;
      }
   }

   public void setListInformacionAdicional(List<InformacionesAdicionales> list) {
      this.listInformacionAdicional = list;
   }

   public Empleados getEmpleado() {
      return empleado;
   }

   public void setEmpleado(Empleados empleado) {
      this.empleado = empleado;
   }

   public List<InformacionesAdicionales> getFiltrarListInformacionAdicional() {
      return filtrarListInformacionAdicional;
   }

   public void setFiltrarListInformacionAdicional(List<InformacionesAdicionales> filtrar) {
      this.filtrarListInformacionAdicional = filtrar;
   }

   public InformacionesAdicionales getNuevaInfoAdicional() {
      return nuevaInfoAdicional;
   }

   public void setNuevaInfoAdicional(InformacionesAdicionales nuevaVigencia) {
      this.nuevaInfoAdicional = nuevaVigencia;
   }

   public boolean isAceptar() {
      return aceptar;
   }

   public List<GruposInfAdicionales> getListGruposInfAdicional() {
      listGruposInfAdicional = administrarEmplInformacionAdicional.listGruposInfAdicionales();
      return listGruposInfAdicional;
   }

   public void setListGruposInfAdicional(List<GruposInfAdicionales> listGruposInfAdicional) {
      this.listGruposInfAdicional = listGruposInfAdicional;
   }

   public List<GruposInfAdicionales> getFiltrarListGruposInfAdicional() {
      return filtrarListGruposInfAdicional;
   }

   public void setFiltrarListGruposInfAdicional(List<GruposInfAdicionales> filtrado) {
      this.filtrarListGruposInfAdicional = filtrado;
   }

   public InformacionesAdicionales getEditarInfoAdicional() {
      return editarInfoAdicional;
   }

   public void setEditarInfoAdicional(InformacionesAdicionales editar) {
      this.editarInfoAdicional = editar;
   }

   public InformacionesAdicionales getDuplicarInfoAdicional() {
      return duplicarInfoAdicional;
   }

   public void setDuplicarInfoAdicional(InformacionesAdicionales duplicar) {
      this.duplicarInfoAdicional = duplicar;
   }

   public GruposInfAdicionales getGrupoSelecionado() {
      return grupoSelecionado;
   }

   public void setGrupoSelecionado(GruposInfAdicionales selecionado) {
      this.grupoSelecionado = selecionado;
   }

   public BigInteger getBackUpSecRegistro() {
      return backUpSecRegistro;
   }

   public void setBackUpSecRegistro(BigInteger backUpSecRegistro) {
      this.backUpSecRegistro = backUpSecRegistro;
   }

   public InformacionesAdicionales getInformacionTablaSeleccionada() {
      return informacionTablaSeleccionada;
   }

   public void setInformacionTablaSeleccionada(InformacionesAdicionales informacionTablaSeleccionada) {
      this.informacionTablaSeleccionada = informacionTablaSeleccionada;
   }

   public boolean isGuardado() {
      return guardado;
   }

   public void setGuardado(boolean guardado) {
      this.guardado = guardado;
   }

   public String getAltoTabla() {
      return altoTabla;
   }

   public void setAltoTabla(String altoTabla) {
      this.altoTabla = altoTabla;
   }

   public String getInfoRegistro() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosInfoAdEmpleado");
      infoRegistro = String.valueOf(tabla.getRowCount());
      return infoRegistro;
   }

   public void setInfoRegistro(String infoRegistro) {
      this.infoRegistro = infoRegistro;
   }

   public String getInfoRegistroGrupo() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovGrupo");
      infoRegistroGrupo = String.valueOf(tabla.getRowCount());
      return infoRegistroGrupo;
   }

   public void setInfoRegistroGrupo(String infoRegistroGrupo) {
      this.infoRegistroGrupo = infoRegistroGrupo;
   }

   public boolean isActivarLov() {
      return activarLov;
   }

   public void setActivarLov(boolean activarLov) {
      this.activarLov = activarLov;
   }

   public boolean isActivarCaracter() {
      return activarCaracter;
   }

   public void setActivarCaracter(boolean activarCaracter) {
      this.activarCaracter = activarCaracter;
   }

   public boolean isActivarNumero() {
      return activarNumero;
   }

   public void setActivarNumero(boolean activarNumero) {
      this.activarNumero = activarNumero;
   }

   public boolean isActivarFecha() {
      return activarFecha;
   }

   public void setActivarFecha(boolean activarFecha) {
      this.activarFecha = activarFecha;
   }

}
