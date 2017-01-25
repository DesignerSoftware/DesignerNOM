/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Instituciones;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarInstitucionesInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import ControlNavegacion.ControlListaNavegacion;
import java.util.Map;
import java.util.LinkedHashMap;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
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
@Named(value = "controlInstituciones")
@SessionScoped
public class ControlInstituciones implements Serializable {

   @EJB
   AdministrarRastrosInterface administrarRastros;
   @EJB
   AdministrarInstitucionesInterface administrarInstituciones;

   private List<Instituciones> listaInstituciones;
   private List<Instituciones> filtradoListaInstituciones;
   private List<Instituciones> listaInstitucionesCrear;
   private List<Instituciones> listaInstitucionesBorrar;
   private List<Instituciones> listaInstitucionesModificar;
   private Instituciones institucionSeleccionada;
   private Instituciones nuevoInstitucion;
   private Instituciones duplicarInstitucion;
   private Instituciones editarInstitucion;
   private BigInteger l;
   private int k, bandera, tipoLista, cualCelda;
   private Column codigo, descripcion, contacto, telefono;
   private boolean aceptar, permitirIndex, guardado, activarLov;
   private String altoTabla, inforegistro, mensajeValidacion;
   private DataTable tablaC;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

   public ControlInstituciones() {
      listaInstitucionesCrear = new ArrayList<Instituciones>();
      listaInstitucionesBorrar = new ArrayList<Instituciones>();
      listaInstitucionesModificar = new ArrayList<Instituciones>();
      permitirIndex = true;
      aceptar = true;
      tipoLista = 0;
      institucionSeleccionada = null;
      editarInstitucion = new Instituciones();
      nuevoInstitucion = new Instituciones();
      duplicarInstitucion = new Instituciones();
      cualCelda = -1;
      altoTabla = "270";
      guardado = true;
      activarLov = true;
      paginaAnterior = " ";
      mapParametros.put("paginaAnterior", paginaAnterior);
   }

   public void recibirPaginaEntrante(String pagina) {
      paginaAnterior = pagina;
      listaInstituciones = null;
      getListaInstituciones();
      if (listaInstituciones != null) {
         institucionSeleccionada = listaInstituciones.get(0);
      }
   }

   public void recibirParametros(Map<String, Object> map) {
      mapParametros = map;
      paginaAnterior = (String) mapParametros.get("paginaAnterior");
      listaInstituciones = null;
      getListaInstituciones();
      if (listaInstituciones != null) {
         institucionSeleccionada = listaInstituciones.get(0);
      }
   }

   //Reemplazar la funcion volverAtras, retornarPagina, Redirigir.....Atras.etc
   public void navegar(String pag) {
      FacesContext fc = FacesContext.getCurrentInstance();
      ControlListaNavegacion controlListaNavegacion = (ControlListaNavegacion) fc.getApplication().evaluateExpressionGet(fc, "#{controlListaNavegacion}", ControlListaNavegacion.class);
      if (pag.equals("atras")) {
         pag = paginaAnterior;
         paginaAnterior = "nominaf";
         controlListaNavegacion.quitarPagina();
      } else {
         String pagActual = "instituciones";
         //Map<String, Object> mapParaEnviar = new LinkedHashMap<String, Object>();
         //mapParametros.put("paginaAnterior", pagActual);
         //mas Parametros
//         if (pag.equals("rastrotabla")) {
//           ControlRastro controlRastro = (ControlRastro) fc.getApplication().evaluateExpressionGet(fc, "#{controlRastro}", ControlRastro.class);
         //           controlRastro.recibirDatosTabla(conceptoSeleccionado.getSecuencia(), "Conceptos", pagActual);
         //      } else if (pag.equals("rastrotablaH")) {
         //       ControlRastro controlRastro = (ControlRastro) fc.getApplication().evaluateExpressionGet(fc, "#{controlRastro}", ControlRastro.class);
         //     controlRastro.historicosTabla("Conceptos", pagActual);
         //   pag = "rastrotabla";
         //}
         controlListaNavegacion.adicionarPagina(pagActual);
      }
      fc.getApplication().getNavigationHandler().handleNavigation(fc, null, pag);
   }

   @PostConstruct
   public void inicializarAdmnistrador() {
      try {
         FacesContext x = FacesContext.getCurrentInstance();
         HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
         administrarRastros.obtenerConexion(ses.getId());
         administrarInstituciones.obtenerConexion(ses.getId());
      } catch (Exception e) {
         System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
         System.out.println("Causa: " + e.getCause());
      }
   }

   public String redirigir() {
      return paginaAnterior;
   }

   public void editarCelda() {
      if (institucionSeleccionada != null) {
         editarInstitucion = institucionSeleccionada;
         RequestContext context = RequestContext.getCurrentInstance();
         if (cualCelda == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarCodigosInstituciones");
            RequestContext.getCurrentInstance().execute("PF('editarCodigosInstituciones').show()");
            cualCelda = -1;
         } else if (cualCelda == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarDescripcionInstituciones");
            RequestContext.getCurrentInstance().execute("PF('editarDescripcionInstituciones').show()");
            cualCelda = -1;
         } else if (cualCelda == 2) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarContactoInstituciones");
            RequestContext.getCurrentInstance().execute("PF('editarContactoInstituciones').show()");
            cualCelda = -1;
         } else if (cualCelda == 3) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarTelefonoInstitucion");
            RequestContext.getCurrentInstance().execute("PF('editarTelefonoInstitucion').show()");
            cualCelda = -1;
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void guardarCambiosInstituciones() {
      try {
         if (guardado == false) {
            if (!listaInstitucionesBorrar.isEmpty()) {
               administrarInstituciones.borrar(listaInstitucionesBorrar);
               listaInstitucionesBorrar.clear();
            }
            if (!listaInstitucionesCrear.isEmpty()) {
               administrarInstituciones.crear(listaInstitucionesCrear);
               listaInstitucionesCrear.clear();
            }
            if (!listaInstitucionesModificar.isEmpty()) {
               administrarInstituciones.editar(listaInstitucionesModificar);
               listaInstitucionesModificar.clear();
            }

            listaInstituciones = null;
            getListaInstituciones();
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            k = 0;
            FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            contarRegistros();
            institucionSeleccionada = null;
         }
         guardado = true;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         RequestContext.getCurrentInstance().update("form:datosInstituciones");
         deshabilitarBotonLov();
      } catch (Exception e) {
         System.out.println("Error guardarCambios : " + e.toString());
         FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado, intente nuevamente.");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }
   }

   public void salir() {
      if (bandera == 1) {
         System.out.println("desactivar");
         FacesContext c = FacesContext.getCurrentInstance();
         codigo = (Column) c.getViewRoot().findComponent("form:datosInstituciones:institucionCodigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosInstituciones:institucionDescripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         contacto = (Column) c.getViewRoot().findComponent("form:datosInstituciones:institucionContacto");
         contacto.setFilterStyle("display: none; visibility: hidden;");
         telefono = (Column) c.getViewRoot().findComponent("form:datosInstituciones:institucionTelefono");
         telefono.setFilterStyle("display: none; visibility: hidden;");
         bandera = 0;
         filtradoListaInstituciones = null;
         tipoLista = 0;
         altoTabla = "270";
         RequestContext.getCurrentInstance().update("form:datosInstituciones");
      }

      listaInstitucionesBorrar.clear();
      listaInstitucionesCrear.clear();
      listaInstitucionesModificar.clear();
      institucionSeleccionada = null;
      guardado = true;
      permitirIndex = true;
   }

   public void agregarInstitucion() {
      int pasa = 0;
      int pasaA = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      mensajeValidacion = " ";
      if (nuevoInstitucion.getCodigo() == null || nuevoInstitucion.getCodigo().equals(0)) {
         mensajeValidacion = mensajeValidacion + " * Codigo \n";
         pasa++;
      }

      for (int i = 0; i < listaInstituciones.size(); i++) {
         System.out.println("Codigos: " + listaInstituciones.get(i).getCodigo());
         if (listaInstituciones.get(i).getCodigo().compareTo(nuevoInstitucion.getCodigo()) == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:existeCodigo");
            RequestContext.getCurrentInstance().execute("PF('existeCodigo').show()");
            pasaA++;
         }
         if (pasa != 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevaInstitucion");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevaInstitucion').show()");

         }
      }

      if (pasa == 0 && pasaA == 0) {
         if (bandera == 1) {
            //CERRAR FILTRADO
            System.out.println("Desactivar");
            FacesContext c = FacesContext.getCurrentInstance();
            codigo = (Column) c.getViewRoot().findComponent("form:datosInstituciones:institucionCodigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosInstituciones:institucionDescripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            contacto = (Column) c.getViewRoot().findComponent("form:datosInstituciones:institucionContacto");
            contacto.setFilterStyle("display: none; visibility: hidden;");
            telefono = (Column) c.getViewRoot().findComponent("form:datosInstituciones:institucionTelefono");
            telefono.setFilterStyle("display: none; visibility: hidden;");
            bandera = 0;
            filtradoListaInstituciones = null;
            tipoLista = 0;
            altoTabla = "270";
            RequestContext.getCurrentInstance().update("form:datosInstituciones");
         }
         //AGREGAR REGISTRO A LA LISTA CIUDADES.
         k++;
         l = BigInteger.valueOf(k);
         nuevoInstitucion.setSecuencia(l);
         listaInstitucionesCrear.add(nuevoInstitucion);
         listaInstituciones.add(nuevoInstitucion);
         contarRegistros();
         institucionSeleccionada = nuevoInstitucion;
         nuevoInstitucion = new Instituciones();

         RequestContext.getCurrentInstance().update("form:datosInstituciones");
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         RequestContext.getCurrentInstance().execute("PF('NuevoRegistroInstitucion').hide()");
      } else {
         RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevaInstitucion");
         RequestContext.getCurrentInstance().execute("PF('validacionNuevaInstitucion').show()");
      }

   }

   public void activarCtrlF11() {
      if (bandera == 0) {
         System.out.println("Activar");
         System.out.println("TipoLista= " + tipoLista);
         FacesContext c = FacesContext.getCurrentInstance();
         codigo = (Column) c.getViewRoot().findComponent("form:datosInstituciones:institucionCodigo");
         codigo.setFilterStyle("width: 85% !important");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosInstituciones:institucionDescripcion");
         descripcion.setFilterStyle("width: 85% !important");
         contacto = (Column) c.getViewRoot().findComponent("form:datosInstituciones:institucionContacto");
         contacto.setFilterStyle("width: 85% !important");
         telefono = (Column) c.getViewRoot().findComponent("form:datosInstituciones:institucionTelefono");
         telefono.setFilterStyle("width: 85% !important");
         altoTabla = "250";
         RequestContext.getCurrentInstance().update("form:datosInstituciones");
         bandera = 1;
         tipoLista = 1;
      } else if (bandera == 1) {
         System.out.println("Desactivar");
         System.out.println("TipoLista= " + tipoLista);
         FacesContext c = FacesContext.getCurrentInstance();
         codigo = (Column) c.getViewRoot().findComponent("form:datosInstituciones:institucionCodigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosInstituciones:institucionDescripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         contacto = (Column) c.getViewRoot().findComponent("form:datosInstituciones:institucionContacto");
         contacto.setFilterStyle("display: none; visibility: hidden;");
         telefono = (Column) c.getViewRoot().findComponent("form:datosInstituciones:institucionTelefono");
         telefono.setFilterStyle("display: none; visibility: hidden;");
         altoTabla = "270";
         RequestContext.getCurrentInstance().update("form:datosInstituciones");
         bandera = 0;
         filtradoListaInstituciones = null;
         tipoLista = 0;
      }
   }

   public void exportPDF() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosInstitucionesExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "InstitucionesPDF", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void exportXLS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosInstitucionesExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "InstitucionesXLS", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void limpiarNuevaInstitucion() {
      nuevoInstitucion = new Instituciones();
   }

   public void borrarInstituciones() {

      if (institucionSeleccionada != null) {

         if (!listaInstitucionesModificar.isEmpty() && listaInstitucionesModificar.contains(institucionSeleccionada)) {
            listaInstitucionesModificar.remove(listaInstitucionesModificar.indexOf(institucionSeleccionada));
            listaInstitucionesBorrar.add(institucionSeleccionada);
         } else if (!listaInstitucionesCrear.isEmpty() && listaInstitucionesCrear.contains(institucionSeleccionada)) {
            listaInstitucionesCrear.remove(listaInstitucionesCrear.indexOf(institucionSeleccionada));
         } else {
            listaInstitucionesBorrar.add(institucionSeleccionada);
         }
         listaInstituciones.remove(institucionSeleccionada);

         if (tipoLista == 1) {
            filtradoListaInstituciones.remove(institucionSeleccionada);
         }
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:infoRegistro");
         RequestContext.getCurrentInstance().update("form:datosInstituciones");
         contarRegistros();
         institucionSeleccionada = null;
         guardado = true;

         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void cambiarIndice(Instituciones institucion, int celda) {
      if (permitirIndex == true) {
         institucionSeleccionada = institucion;
         cualCelda = celda;
         if (cualCelda == 0) {
            institucionSeleccionada.getCodigo();
         } else if (cualCelda == 1) {
            institucionSeleccionada.getDescripcion();
         } else if (cualCelda == 2) {
            institucionSeleccionada.getContacto();
         } else if (cualCelda == 3) {
            institucionSeleccionada.getTelefono();
         }
      }
   }

   public void duplicarInstituciones() {
      if (institucionSeleccionada != null) {
         duplicarInstitucion = new Instituciones();
         k++;
         l = BigInteger.valueOf(k);

         if (tipoLista == 0) {
            duplicarInstitucion.setSecuencia(l);
            duplicarInstitucion.setCodigo(institucionSeleccionada.getCodigo());
            duplicarInstitucion.setDescripcion(institucionSeleccionada.getDescripcion());
            duplicarInstitucion.setContacto(institucionSeleccionada.getContacto());
            duplicarInstitucion.setTelefono(institucionSeleccionada.getTelefono());
         }
         if (tipoLista == 1) {
            duplicarInstitucion.setSecuencia(l);
            duplicarInstitucion.setCodigo(institucionSeleccionada.getCodigo());
            duplicarInstitucion.setDescripcion(institucionSeleccionada.getDescripcion());
            duplicarInstitucion.setContacto(institucionSeleccionada.getContacto());
            duplicarInstitucion.setTelefono(institucionSeleccionada.getTelefono());
            altoTabla = "270";
         }

         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarInstitucion");
         RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroInstitucion').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void limpiarduplicarInstituciones() {
      duplicarInstitucion = new Instituciones();
   }

   public void confirmarDuplicar() {

      RequestContext context = RequestContext.getCurrentInstance();
      int pasa = 0;

      for (int i = 0; i < listaInstituciones.size(); i++) {
         if (duplicarInstitucion.getCodigo() == listaInstituciones.get(i).getCodigo()) {
            RequestContext.getCurrentInstance().update("formularioDialogos:existeCodigo");
            RequestContext.getCurrentInstance().execute("PF('existeCodigo').show()");
            pasa++;
         }
      }

      if (pasa == 0) {
         listaInstituciones.add(duplicarInstitucion);
         listaInstitucionesCrear.add(duplicarInstitucion);
         institucionSeleccionada = duplicarInstitucion;
         contarRegistros();
         RequestContext.getCurrentInstance().update("form:datosInstituciones");
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         if (bandera == 1) {
            System.out.println("Desactivar");
            FacesContext c = FacesContext.getCurrentInstance();
            codigo = (Column) c.getViewRoot().findComponent("form:datosInstituciones:institucionCodigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosInstituciones:institucionDescripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            contacto = (Column) c.getViewRoot().findComponent("form:datosInstituciones:institucionContacto");
            contacto.setFilterStyle("display: none; visibility: hidden;");
            telefono = (Column) c.getViewRoot().findComponent("form:datosInstituciones:institucionTelefono");
            telefono.setFilterStyle("display: none; visibility: hidden;");
            bandera = 0;
            filtradoListaInstituciones = null;
            RequestContext.getCurrentInstance().update("form:datosInstituciones");
            tipoLista = 0;
         }
         duplicarInstitucion = new Instituciones();
      }
      RequestContext.getCurrentInstance().update("formularioDialogos:DuplicarRegistroInstitucion");
      RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroInstitucion').hide()");
   }

   public void cancelarModificacion() {
      if (bandera == 1) {
         //CERRAR FILTRADO
         FacesContext c = FacesContext.getCurrentInstance();
         codigo = (Column) c.getViewRoot().findComponent("form:datosInstituciones:institucionCodigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosInstituciones:institucionDescripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         contacto = (Column) c.getViewRoot().findComponent("form:datosInstituciones:institucionContacto");
         contacto.setFilterStyle("display: none; visibility: hidden;");
         telefono = (Column) c.getViewRoot().findComponent("form:datosInstituciones:institucionTelefono");
         telefono.setFilterStyle("display: none; visibility: hidden;");
         bandera = 0;
         filtradoListaInstituciones = null;
         tipoLista = 0;
         altoTabla = "270";
         RequestContext.getCurrentInstance().update("form:datosTiposTelefonos");
      }
      listaInstitucionesBorrar.clear();
      listaInstitucionesCrear.clear();
      listaInstitucionesModificar.clear();
      contarRegistros();
      institucionSeleccionada = null;
      k = 0;
      listaInstituciones = null;
      guardado = true;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:datosInstituciones");
   }

   public void modificarInstituciones(Instituciones institucion, String confirmarCambio, String valorConfirmar) {
      institucionSeleccionada = institucion;
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      if (confirmarCambio.equalsIgnoreCase("N")) {
         if (tipoLista == 0) {
            if (!listaInstitucionesCrear.contains(institucionSeleccionada)) {
               if (listaInstitucionesModificar.isEmpty()) {
                  listaInstitucionesModificar.add(institucionSeleccionada);
               } else if (!listaInstitucionesModificar.contains(institucionSeleccionada)) {
                  listaInstitucionesModificar.add(institucionSeleccionada);
               }
               if (guardado == true) {
                  guardado = false;
                  RequestContext.getCurrentInstance().update("form:ACEPTAR");
               }
            }
         } else if (!listaInstitucionesCrear.contains(institucionSeleccionada)) {

            if (listaInstitucionesModificar.isEmpty()) {
               listaInstitucionesModificar.add(institucionSeleccionada);
            } else if (!listaInstitucionesModificar.contains(institucionSeleccionada)) {
               listaInstitucionesModificar.add(institucionSeleccionada);
            }
            if (guardado == true) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
         }
         RequestContext.getCurrentInstance().update("form:datosInstituciones");
      }
   }

   public void verificarRastro() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (institucionSeleccionada != null) {
         int resultado = administrarRastros.obtenerTabla(institucionSeleccionada.getSecuencia(), "INSTITUCIONES");
         System.out.println("resultado: " + resultado);
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
      } else if (administrarRastros.verificarHistoricosTabla("INSTITUCIONES")) {
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
   }

   public void recordarSeleccion() {
      if (institucionSeleccionada != null) {
         FacesContext c = FacesContext.getCurrentInstance();
         tablaC = (DataTable) c.getViewRoot().findComponent("form:datosInstituciones");
         tablaC.setSelection(institucionSeleccionada);
      }
   }

   public void eventoFiltrar() {
      if (tipoLista == 0) {
         tipoLista = 1;
      }
      deshabilitarBotonLov();
      contarRegistros();
   }

   public void contarRegistros() {
      RequestContext.getCurrentInstance().update("form:infoRegistro");
   }

   public void deshabilitarBotonLov() {
      activarLov = true;
   }

   ///////gets y sets////////
   public List<Instituciones> getListaInstituciones() {
      if (listaInstituciones == null) {
         listaInstituciones = administrarInstituciones.Instituciones();
      }
      return listaInstituciones;
   }

   public void setListaInstituciones(List<Instituciones> listaInstituciones) {
      this.listaInstituciones = listaInstituciones;
   }

   public List<Instituciones> getFiltradoListaInstituciones() {
      return filtradoListaInstituciones;
   }

   public void setFiltradoListaInstituciones(List<Instituciones> filtradoListaInstituciones) {
      this.filtradoListaInstituciones = filtradoListaInstituciones;
   }

   public Instituciones getInstitucionSeleccionada() {
      return institucionSeleccionada;
   }

   public void setInstitucionSeleccionada(Instituciones institucionSeleccionada) {
      this.institucionSeleccionada = institucionSeleccionada;
   }

   public Instituciones getNuevoInstitucion() {
      return nuevoInstitucion;
   }

   public void setNuevoInstitucion(Instituciones nuevoInstitucion) {
      this.nuevoInstitucion = nuevoInstitucion;
   }

   public Instituciones getDuplicarInstitucion() {
      return duplicarInstitucion;
   }

   public void setDuplicarInstitucion(Instituciones duplicarInstitucion) {
      this.duplicarInstitucion = duplicarInstitucion;
   }

   public Instituciones getEditarInstitucion() {
      return editarInstitucion;
   }

   public void setEditarInstitucion(Instituciones editarInstitucion) {
      this.editarInstitucion = editarInstitucion;
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
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosInstituciones");
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

}
