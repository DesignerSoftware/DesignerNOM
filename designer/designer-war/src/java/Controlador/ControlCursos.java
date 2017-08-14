/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Cursos;
import Entidades.TiposCursos;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarCursosInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import InterfaceAdministrar.AdministrarTiposCursosInterface;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import ControlNavegacion.ControlListaNavegacion;
import java.util.Map;
import java.util.LinkedHashMap;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.primefaces.component.column.Column;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.export.Exporter;
import org.primefaces.context.RequestContext;

/**
 *
 * @author user
 */
@ManagedBean
@Named(value = "controlCursos")
@SessionScoped
public class ControlCursos implements Serializable {

   private static Logger log = Logger.getLogger(ControlCursos.class);

   @EJB
   AdministrarRastrosInterface administrarRastros;
   @EJB
   AdministrarCursosInterface administrarCursos;
   @EJB
   AdministrarTiposCursosInterface administrarTiposCursos;

   private List<Cursos> listaCursos;
   private List<Cursos> filtradoListaCursos;
   private List<Cursos> listaCursosCrear;
   private List<Cursos> listaCursosBorrar;
   private List<Cursos> listaCursosModificar;
   private Cursos cursoSeleccionado;
   private Cursos nuevoCurso;
   private Cursos duplicarCurso;
   private Cursos editarCurso;

   private List<TiposCursos> lovTiposCursos;
   private List<TiposCursos> lovFiltrarTiposCursos;
   private TiposCursos tipocursoSeleccionado;
   private BigInteger l;
   private int k, bandera, tipoLista, cualCelda, tipoActualizacion;
   private Column codigo, descripcion, tipocurso, objetivo;
   private boolean aceptar, permitirIndex, guardado, activarLov;
   private String altoTabla, inforegistro, mensajeValidacion, infoRegistroLov;
   private DataTable tablaC;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

   public ControlCursos() {
      listaCursosCrear = new ArrayList<Cursos>();
      listaCursosBorrar = new ArrayList<Cursos>();
      listaCursosModificar = new ArrayList<Cursos>();
      permitirIndex = true;
      aceptar = true;
      tipoLista = 0;
      cursoSeleccionado = null;
      editarCurso = new Cursos();
      nuevoCurso = new Cursos();
      duplicarCurso = new Cursos();
      cualCelda = -1;
      altoTabla = "270";
      guardado = true;
      activarLov = true;
      listaCursos = null;
      mapParametros.put("paginaAnterior", paginaAnterior);
   }

   public void recibirPaginaEntrante(String pagina) {
      paginaAnterior = pagina;
      listaCursos = null;
      getListaCursos();
      lovTiposCursos = null;
      deshabilitarBotonLov();
      if (listaCursos != null) {
         cursoSeleccionado = listaCursos.get(0);
      }
   }

   public void recibirParametros(Map<String, Object> map) {
      mapParametros = map;
      paginaAnterior = (String) mapParametros.get("paginaAnterior");
      listaCursos = null;
      getListaCursos();
      lovTiposCursos = null;
      deshabilitarBotonLov();
      if (listaCursos != null) {
         cursoSeleccionado = listaCursos.get(0);
      }
   }

   //Reemplazar la funcion volverAtras, retornarPagina, Redirigir.....Atras.etc
   public void navegar(String pag) {
      FacesContext fc = FacesContext.getCurrentInstance();
      ControlListaNavegacion controlListaNavegacion = (ControlListaNavegacion) fc.getApplication().evaluateExpressionGet(fc, "#{controlListaNavegacion}", ControlListaNavegacion.class);
      String pagActual = "curso";
      if (pag.equals("atras")) {
         pag = paginaAnterior;
         paginaAnterior = "nominaf";
         controlListaNavegacion.quitarPagina(pagActual, this.getClass().getSimpleName());
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
      lovTiposCursos = null;
   }

   @PreDestroy
   public void destruyendoce() {
      log.info(this.getClass().getName() + ".destruyendoce() @Destroy");
   }
   
   @PostConstruct
   public void inicializarAdministrador() {
      log.info(this.getClass().getName() + ".inicializarAdministrador() @PostConstruct");
      try {
         FacesContext x = FacesContext.getCurrentInstance();
         HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
         administrarRastros.obtenerConexion(ses.getId());
         administrarCursos.obtenerConexion(ses.getId());
      } catch (Exception e) {
         log.error("Error postconstruct " + this.getClass().getName() + ": " + e);
         log.error("Causa: " + e.getCause());
      }
   }

   public void editarCelda() {
      if (cursoSeleccionado != null) {
         editarCurso = cursoSeleccionado;
         RequestContext context = RequestContext.getCurrentInstance();
         if (cualCelda == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarCodigosCurso");
            RequestContext.getCurrentInstance().execute("PF('editarCodigosCurso').show()");
            cualCelda = -1;
         } else if (cualCelda == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarDescripcionCurso");
            RequestContext.getCurrentInstance().execute("PF('editarDescripcionCurso').show()");
            cualCelda = -1;
         } else if (cualCelda == 2) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarTipoCurso");
            RequestContext.getCurrentInstance().execute("PF('editarTipoCurso').show()");
            cualCelda = -1;
         } else if (cualCelda == 3) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarObjetivoCurso");
            RequestContext.getCurrentInstance().execute("PF('editarObjetivoCurso').show()");
            cualCelda = -1;
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void asignarIndex(int celda, int LND) {
      tipoActualizacion = LND;
      cualCelda = celda;
      if (cualCelda == 2) {
         getLovTiposCursos();
         contarRegistroLov();
         RequestContext.getCurrentInstance().execute("PF('tipoCursoDialogo').show()");
      }
   }

   public void guardarCambiosCurso() {
      try {
         if (guardado == false) {
            if (!listaCursosBorrar.isEmpty()) {
               administrarCursos.borrar(listaCursosBorrar);
               listaCursosBorrar.clear();
            }
            if (!listaCursosCrear.isEmpty()) {
               administrarCursos.crear(listaCursosCrear);
               listaCursosCrear.clear();
            }
            if (!listaCursosModificar.isEmpty()) {
               administrarCursos.editar(listaCursosModificar);
               listaCursosModificar.clear();
            }

            listaCursos = null;
            getListaCursos();
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            k = 0;
            FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            contarRegistros();
            cursoSeleccionado = null;
         }
         guardado = true;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         RequestContext.getCurrentInstance().update("form:datosCursos");
         deshabilitarBotonLov();
      } catch (Exception e) {
         log.warn("Error guardarCambios : " + e.toString());
         FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado, intente nuevamente.");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }
   }

   public void salir() {
      limpiarListasValor();
      if (bandera == 1) {
         log.info("Desactivar");
         FacesContext c = FacesContext.getCurrentInstance();
         codigo = (Column) c.getViewRoot().findComponent("form:datosCursos:cursoCodigos");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosCursos:cursoDescripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         tipocurso = (Column) c.getViewRoot().findComponent("form:datosCursos:cursoTipo");
         tipocurso.setFilterStyle("display: none; visibility: hidden;");
         objetivo = (Column) c.getViewRoot().findComponent("form:datosCursos:cursoObjetivo");
         objetivo.setFilterStyle("display: none; visibility: hidden;");
         bandera = 0;
         filtradoListaCursos = null;
         tipoLista = 0;
         altoTabla = "270";
         RequestContext.getCurrentInstance().update("form:datosCursos");
      }
      listaCursosBorrar.clear();
      listaCursosCrear.clear();
      listaCursosModificar.clear();
      cursoSeleccionado = null;
      contarRegistros();
      k = 0;
      listaCursos = null;
      guardado = true;
      permitirIndex = true;
      navegar("atras");
   }

   public void agregarNuevoCurso() {
      int pasa = 0;
      int pasaA = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      mensajeValidacion = " ";
      if (nuevoCurso.getNombre().equals(" ") || nuevoCurso.getNombre().equals("")) {
         mensajeValidacion = mensajeValidacion + " * Nombre del Curso\n";
         pasa++;
      }
      if (nuevoCurso.getTipocurso().getDescripcion().equals(" ") || nuevoCurso.getTipocurso().getDescripcion().equals("")) {
         mensajeValidacion = mensajeValidacion + " * Tipo del Curso\n";
         pasa++;
      }
      if (nuevoCurso.getObjetivo().equals(" ") || nuevoCurso.getObjetivo().equals("")) {
         mensajeValidacion = mensajeValidacion + " * Objetivo del Curso\n";
         pasa++;
      }

      for (int i = 0; i < listaCursos.size(); i++) {
         if (nuevoCurso.getCodigo() == listaCursos.get(i).getCodigo()) {
            RequestContext.getCurrentInstance().update("formularioDialogos:existeCodigo");
            RequestContext.getCurrentInstance().execute("PF('existeCodigo').show()");
            pasa++;
         }
         if (pasa != 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevoCurso");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevoCurso').show()");

         }
      }

      if (pasa == 0 && pasaA == 0) {
         if (bandera == 1) {
            //CERRAR FILTRADO
            log.info("Desactivar");
            FacesContext c = FacesContext.getCurrentInstance();
            codigo = (Column) c.getViewRoot().findComponent("form:datosCursos:cursoCodigos");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosCursos:cursoDescripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            tipocurso = (Column) c.getViewRoot().findComponent("form:datosCursos:cursoTipo");
            tipocurso.setFilterStyle("display: none; visibility: hidden;");
            objetivo = (Column) c.getViewRoot().findComponent("form:datosCursos:cursoObjetivo");
            objetivo.setFilterStyle("display: none; visibility: hidden;");
            bandera = 0;
            filtradoListaCursos = null;
            tipoLista = 0;
            altoTabla = "270";
            RequestContext.getCurrentInstance().update("form:datosCursos");
         }
         //AGREGAR REGISTRO A LA LISTA CIUDADES.
         k++;
         l = BigInteger.valueOf(k);
         nuevoCurso.setSecuencia(l);
         listaCursosCrear.add(nuevoCurso);
         listaCursos.add(nuevoCurso);
         contarRegistros();
         cursoSeleccionado = nuevoCurso;
         nuevoCurso = new Cursos();

         RequestContext.getCurrentInstance().update("form:datosCursos");
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         RequestContext.getCurrentInstance().execute("PF('NuevoRegistroCurso').hide()");
      } else {
         RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevoCurso");
         RequestContext.getCurrentInstance().execute("PF('validacionNuevoCurso').show()");
      }
   }

   public void activarCtrlF11() {
      if (bandera == 0) {
         FacesContext c = FacesContext.getCurrentInstance();
         codigo = (Column) c.getViewRoot().findComponent("form:datosCursos:cursoCodigos");
         codigo.setFilterStyle("width: 85% !important");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosCursos:cursoDescripcion");
         descripcion.setFilterStyle("width: 85% !important");
         tipocurso = (Column) c.getViewRoot().findComponent("form:datosCursos:cursoTipo");
         tipocurso.setFilterStyle("width: 85% !important");
         objetivo = (Column) c.getViewRoot().findComponent("form:datosCursos:cursoObjetivo");
         objetivo.setFilterStyle("width: 85% !important");
         altoTabla = "250";
         RequestContext.getCurrentInstance().update("form:datosCursos");
         bandera = 1;
         tipoLista = 1;
      } else if (bandera == 1) {
         FacesContext c = FacesContext.getCurrentInstance();
         codigo = (Column) c.getViewRoot().findComponent("form:datosCursos:cursoCodigos");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosCursos:cursoDescripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         tipocurso = (Column) c.getViewRoot().findComponent("form:datosCursos:cursoTipo");
         tipocurso.setFilterStyle("display: none; visibility: hidden;");
         objetivo = (Column) c.getViewRoot().findComponent("form:datosCursos:cursoObjetivo");
         objetivo.setFilterStyle("display: none; visibility: hidden;");
         altoTabla = "270";
         RequestContext.getCurrentInstance().update("form:datosCursos");
         bandera = 0;
         filtradoListaCursos = null;
         tipoLista = 0;
      }
   }

   public void exportPDF() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosCursosExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "CursosPDF", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void exportXLS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosCursosExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "CursosXLS", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void limpiarNuevoCurso() {
      nuevoCurso = new Cursos();
   }

   public void borrarCursos() {

      if (cursoSeleccionado != null) {

         if (!listaCursosModificar.isEmpty() && listaCursosModificar.contains(cursoSeleccionado)) {
            listaCursosModificar.remove(listaCursosModificar.indexOf(cursoSeleccionado));
            listaCursosBorrar.add(cursoSeleccionado);
         } else if (!listaCursosCrear.isEmpty() && listaCursosCrear.contains(cursoSeleccionado)) {
            listaCursosCrear.remove(listaCursosCrear.indexOf(cursoSeleccionado));
         } else {
            listaCursosBorrar.add(cursoSeleccionado);
         }
         listaCursos.remove(cursoSeleccionado);

         if (tipoLista == 1) {
            filtradoListaCursos.remove(cursoSeleccionado);
         }
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:infoRegistro");
         RequestContext.getCurrentInstance().update("form:datosCursos");
         contarRegistros();
         cursoSeleccionado = null;
         guardado = true;

         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void cambiarIndice(Cursos curso, int celda) {
      if (permitirIndex == true) {
         cursoSeleccionado = curso;
         cualCelda = celda;
         if (cualCelda == 0) {
            cursoSeleccionado.getCodigo();

         } else if (cualCelda == 1) {
            cursoSeleccionado.getNombre();
         } else if (cualCelda == 2) {
            cursoSeleccionado.getTipocurso().getDescripcion();
         } else if (cualCelda == 3) {
            cursoSeleccionado.getObjetivo();
         }
      }
   }

   public void duplicarCursos() {
      if (cursoSeleccionado != null) {
         duplicarCurso = new Cursos();
         k++;
         l = BigInteger.valueOf(k);

         if (tipoLista == 0) {
            duplicarCurso.setSecuencia(l);
            duplicarCurso.setCodigo(cursoSeleccionado.getCodigo());
            duplicarCurso.setNombre(cursoSeleccionado.getNombre());
            duplicarCurso.setTipocurso(cursoSeleccionado.getTipocurso());
            duplicarCurso.setObjetivo(cursoSeleccionado.getObjetivo());
         }
         if (tipoLista == 1) {
            duplicarCurso.setSecuencia(l);
            duplicarCurso.setCodigo(cursoSeleccionado.getCodigo());
            duplicarCurso.setNombre(cursoSeleccionado.getNombre());
            duplicarCurso.setTipocurso(cursoSeleccionado.getTipocurso());
            duplicarCurso.setObjetivo(cursoSeleccionado.getObjetivo());
            altoTabla = "270";
         }

         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCurso");
         RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroCurso').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void limpiarduplicarCurso() {
      duplicarCurso = new Cursos();
   }

   public void confirmarDuplicar() {

      RequestContext context = RequestContext.getCurrentInstance();
      int pasa = 0;

      for (int i = 0; i < listaCursos.size(); i++) {
         if (duplicarCurso.getCodigo() == listaCursos.get(i).getCodigo()) {
            RequestContext.getCurrentInstance().update("formularioDialogos:existeCodigo");
            RequestContext.getCurrentInstance().execute("PF('existeCodigo').show()");
            pasa++;
         }
      }

      if (pasa == 0) {

         listaCursos.add(duplicarCurso);
         listaCursosCrear.add(duplicarCurso);
         cursoSeleccionado = duplicarCurso;
         contarRegistros();
         RequestContext.getCurrentInstance().update("form:datosCursos");
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         if (bandera == 1) {
            log.info("Desactivar");
            FacesContext c = FacesContext.getCurrentInstance();
            codigo = (Column) c.getViewRoot().findComponent("form:datosCursos:cursoCodigos");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosCursos:cursoDescripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            tipocurso = (Column) c.getViewRoot().findComponent("form:datosCursos:cursoTipo");
            tipocurso.setFilterStyle("display: none; visibility: hidden;");
            objetivo = (Column) c.getViewRoot().findComponent("form:datosCursos:cursoObjetivo");
            objetivo.setFilterStyle("display: none; visibility: hidden;");
            bandera = 0;
            filtradoListaCursos = null;
            tipoLista = 0;
            RequestContext.getCurrentInstance().update("form:datosCursos");
            tipoLista = 0;
         }
         duplicarCurso = new Cursos();
      }
      RequestContext.getCurrentInstance().update("formularioDialogos:DuplicarRegistroTipoEducacion");
      RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroTipoEducacion').hide()");
   }

   public void cancelarModificacion() {
      if (bandera == 1) {
         //CERRAR FILTRADO
         FacesContext c = FacesContext.getCurrentInstance();
         codigo = (Column) c.getViewRoot().findComponent("form:datosCursos:cursoCodigos");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosCursos:cursoDescripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         tipocurso = (Column) c.getViewRoot().findComponent("form:datosCursos:cursoTipo");
         tipocurso.setFilterStyle("display: none; visibility: hidden;");
         objetivo = (Column) c.getViewRoot().findComponent("form:datosCursos:cursoObjetivo");
         objetivo.setFilterStyle("display: none; visibility: hidden;");
         bandera = 0;
         filtradoListaCursos = null;
         tipoLista = 0;
         altoTabla = "270";
         RequestContext.getCurrentInstance().update("form:datosCursos");
         tipoLista = 0;
      }
      listaCursosBorrar.clear();
      listaCursosCrear.clear();
      listaCursosModificar.clear();
      contarRegistros();
      cursoSeleccionado = null;
      k = 0;
      listaCursos = null;
      guardado = true;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:datosCursos");
   }

   public void modificarCursos(Cursos curso) {
      cursoSeleccionado = curso;
      if (!listaCursosCrear.contains(cursoSeleccionado)) {
         if (listaCursosModificar.isEmpty()) {
            listaCursosModificar.add(cursoSeleccionado);
         } else if (!listaCursosModificar.contains(cursoSeleccionado)) {
            listaCursosModificar.add(cursoSeleccionado);
         }
         guardado = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }
      RequestContext.getCurrentInstance().update("form:datosCursos");
   }

   public void actualizarTiposCursos() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {
         if (tipoLista == 0) {
            cursoSeleccionado.setTipocurso(tipocursoSeleccionado);
            if (!listaCursosCrear.contains(cursoSeleccionado)) {
               if (listaCursosModificar.isEmpty()) {
                  listaCursosModificar.add(cursoSeleccionado);
               } else if (!listaCursosModificar.contains(cursoSeleccionado)) {
                  listaCursosModificar.add(cursoSeleccionado);
               }
            }
         } else {
            cursoSeleccionado.setTipocurso(tipocursoSeleccionado);
            if (!listaCursosCrear.contains(cursoSeleccionado)) {
               if (listaCursosModificar.isEmpty()) {
                  listaCursosModificar.add(cursoSeleccionado);
               } else if (!listaCursosModificar.contains(cursoSeleccionado)) {
                  listaCursosModificar.add(cursoSeleccionado);
               }
            }
         }
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         permitirIndex = true;
         RequestContext.getCurrentInstance().update("form:datosCursos");
      } else if (tipoActualizacion == 1) {
         nuevoCurso.setTipocurso(tipocursoSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoCurso");
      } else if (tipoActualizacion == 2) {
         duplicarCurso.setTipocurso(tipocursoSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCurso");
      }
      filtradoListaCursos = null;
      tipocursoSeleccionado = null;
      aceptar = true;
      cursoSeleccionado = null;
      tipoActualizacion = -1;
      cualCelda = -1;
      context.reset("formularioDialogos:lovTipoCurso:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovTipoCurso').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('tipoCursoDialogo').hide()");
      RequestContext.getCurrentInstance().update("formularioDialogos:tipoCursoDialogo");
      RequestContext.getCurrentInstance().update("formularioDialogos:lovTipoCurso");
      RequestContext.getCurrentInstance().update("formularioDialogos:aceptart");
   }

   public void cancelarCambioTiposCursos() {
      filtradoListaCursos = null;
      tipocursoSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formularioDialogos:lovTipoCurso:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovTipoCurso').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('tipoCursoDialogo').hide()");
      RequestContext.getCurrentInstance().update("formularioDialogos:tipoCursoDialogo");
      RequestContext.getCurrentInstance().update("formularioDialogos:lovTipoCurso");
      RequestContext.getCurrentInstance().update("formularioDialogos:aceptart");
   }

   public void verificarRastro() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (cursoSeleccionado != null) {
         int resultado = administrarRastros.obtenerTabla(cursoSeleccionado.getSecuencia(), "CURSOS");
         log.info("resultado: " + resultado);
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
      } else if (administrarRastros.verificarHistoricosTabla("CURSOS")) {
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
   }

   public void recordarSeleccionTT() {
      if (cursoSeleccionado != null) {
         FacesContext c = FacesContext.getCurrentInstance();
         tablaC = (DataTable) c.getViewRoot().findComponent("form:datosCursos");
         tablaC.setSelection(cursoSeleccionado);
      }
   }

   public void eventoFiltrar() {
      if (tipoLista == 0) {
         tipoLista = 1;
      }
      deshabilitarBotonLov();
      contarRegistros();
   }

   public void contarRegistroLov() {
      RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistrolov");
   }

   public void contarRegistros() {
      RequestContext.getCurrentInstance().update("form:infoRegistro");
   }

   public void deshabilitarBotonLov() {
      activarLov = true;
   }

   public void activarAceptar() {
      aceptar = false;
   }

   ///////GETS Y SETS/////////
   public List<Cursos> getListaCursos() {
      if (listaCursos == null) {
         listaCursos = administrarCursos.consultarCursos();
      }
      return listaCursos;
   }

   public void setListaCursos(List<Cursos> listaCursos) {
      this.listaCursos = listaCursos;
   }

   public List<Cursos> getFiltradoListaCursos() {
      return filtradoListaCursos;
   }

   public void setFiltradoListaCursos(List<Cursos> filtradoListaCursos) {
      this.filtradoListaCursos = filtradoListaCursos;
   }

   public Cursos getCursoSeleccionado() {
      return cursoSeleccionado;
   }

   public void setCursoSeleccionado(Cursos cursoSeleccionado) {
      this.cursoSeleccionado = cursoSeleccionado;
   }

   public boolean isAceptar() {
      return aceptar;
   }

   public void setAceptar(boolean aceptar) {
      this.aceptar = aceptar;
   }

   public boolean isGuardado() {
      return guardado;
   }

   public void setGuardado(boolean guardado) {
      this.guardado = guardado;
   }

   public boolean isActivarLov() {
      return activarLov;
   }

   public void setActivarLov(boolean activarLov) {
      this.activarLov = activarLov;
   }

   public String getAltoTabla() {
      return altoTabla;
   }

   public void setAltoTabla(String altoTabla) {
      this.altoTabla = altoTabla;
   }

   public String getInforegistro() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosCursos");
      inforegistro = String.valueOf(tabla.getRowCount());
      return inforegistro;
   }

   public void setInforegistro(String inforegistro) {
      this.inforegistro = inforegistro;
   }

   public String getMensajeValidacion() {
      return mensajeValidacion;
   }

   public void setMensajeValidacion(String mensajeValidacion) {
      this.mensajeValidacion = mensajeValidacion;
   }

   public List<TiposCursos> getLovTiposCursos() {
      if (lovTiposCursos == null) {
         lovTiposCursos = administrarTiposCursos.consultarTiposCursos();
      }
      return lovTiposCursos;
   }

   public void setLovTiposCursos(List<TiposCursos> lovTiposCursos) {
      this.lovTiposCursos = lovTiposCursos;
   }

   public List<TiposCursos> getLovFiltrarTiposCursos() {
      return lovFiltrarTiposCursos;
   }

   public void setLovFiltrarTiposCursos(List<TiposCursos> lovFiltrarTiposCursos) {
      this.lovFiltrarTiposCursos = lovFiltrarTiposCursos;
   }

   public TiposCursos getTipocursoSeleccionado() {
      return tipocursoSeleccionado;
   }

   public void setTipocursoSeleccionado(TiposCursos tipocursoSeleccionado) {
      this.tipocursoSeleccionado = tipocursoSeleccionado;
   }

   public String getInfoRegistroLov() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovTipoCurso");
      infoRegistroLov = String.valueOf(tabla.getRowCount());
      return infoRegistroLov;
   }

   public void setInfoRegistroLov(String infoRegistroLov) {
      this.infoRegistroLov = infoRegistroLov;
   }

   public Cursos getNuevoCurso() {
      return nuevoCurso;
   }

   public void setNuevoCurso(Cursos nuevoCurso) {
      this.nuevoCurso = nuevoCurso;
   }

   public Cursos getDuplicarCurso() {
      return duplicarCurso;
   }

   public void setDuplicarCurso(Cursos duplicarCurso) {
      this.duplicarCurso = duplicarCurso;
   }

   public Cursos getEditarCurso() {
      return editarCurso;
   }

   public void setEditarCurso(Cursos editarCurso) {
      this.editarCurso = editarCurso;
   }

}
