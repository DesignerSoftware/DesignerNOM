/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Ciudades;
import Entidades.Departamentos;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarCiudadesInterface;
import InterfaceAdministrar.AdministrarDepartamentosInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
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
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.primefaces.component.column.Column;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.export.Exporter;
import org.primefaces.context.RequestContext;

@ManagedBean
@SessionScoped
public class ControlCiudades implements Serializable {

   private static Logger log = Logger.getLogger(ControlCiudades.class);

   @EJB
   AdministrarCiudadesInterface administrarCiudades;
   @EJB
   AdministrarDepartamentosInterface administrarDepartamentos;
   @EJB
   AdministrarRastrosInterface administrarRastros;
   //
   private List<Ciudades> listaCiudades;
   private List<Ciudades> filtradoListaCiudades;
   private Ciudades ciudadSeleccionada;
   //Listas
   private List<Departamentos> lovDepartamentos;
   private List<Departamentos> filtradoListaDepatartamentos;
   private Departamentos seleccionDepartamento;
   //Otros
   private boolean aceptar;
   private int tipoActualizacion;
   private boolean permitirIndex;
   private int tipoLista;
   private int cualCelda;
   //Activo/Desactivo Crtl + F11
   private int bandera;
   //Columnas Tabla Ciudades
   private Column ciudadesCodigos, ciudadesNombres, nombresDepartamentos, ciudadesCodigosAlternativos;
   //Modificar Ciudades
   private List<Ciudades> listaCiudadesModificar;
   private boolean guardado;
   //Crear Ciudades
   public Ciudades nuevaCiudad;
   private List<Ciudades> listaCiudadesCrear;
   private BigInteger l;
   private int k;
   private String mensajeValidacion;
   //Borrar Ciudad
   private List<Ciudades> listaCiudadesBorrar;
   //AUTOCOMPLETAR
   private String Departamento;
   //editar celda
   private Ciudades editarCiudad;
   private boolean aceptarEditar;
   //DUPLICAR
   private Ciudades duplicarCiudad;
   //VALIDAR SI EL QUE SE VA A BORRAR ESTÁ EN SOLUCIONES FORMULAS
   private int resultado;
   public String altoTabla;
   public String nombreCiudad;
   public String infoRegistroDep;
   public String infoRegistroCiudad;
   //
   private DataTable tablaC;
   public boolean activarLOV;

   private String paginaAnterior;
   private Map<String, Object> mapParametros;

   public ControlCiudades() {
      permitirIndex = true;
      aceptar = true;
      tipoLista = 0;
      listaCiudadesBorrar = new ArrayList<Ciudades>();
      listaCiudadesCrear = new ArrayList<Ciudades>();
      listaCiudadesModificar = new ArrayList<Ciudades>();
      //INICIALIZAR LOVS
      lovDepartamentos = null;
      //editar
      editarCiudad = new Ciudades();
      aceptarEditar = true;
      cualCelda = -1;
      tipoLista = 0;
      nuevaCiudad = new Ciudades();
      nuevaCiudad.setDepartamento(new Departamentos());
      nuevaCiudad.getDepartamento().setNombre(" ");
      k = 0;
      altoTabla = "330";
      guardado = true;
      ciudadSeleccionada = null;
      activarLOV = true;

      paginaAnterior = "nominaf";
      mapParametros = new LinkedHashMap<String, Object>();
      mapParametros.put("paginaAnterior", paginaAnterior);
   }

   public void limpiarListasValor() {
      lovDepartamentos = null;
   }

   @PostConstruct
   public void inicializarAdministrador() {
      try {
         FacesContext x = FacesContext.getCurrentInstance();
         HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
         administrarCiudades.obtenerConexion(ses.getId());
         administrarDepartamentos.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());

      } catch (Exception e) {
         log.error("Error postconstruct " + this.getClass().getName() + ": " + e);
         log.error("Causa: " + e.getCause());
      }
   }

   public void recibirPaginaEntrante(String pagina) {
      paginaAnterior = pagina;
      getListaCiudades();
      if (listaCiudades != null) {
         ciudadSeleccionada = listaCiudades.get(0);
      }
      anularBotonLOV();
   }

   public void recibirParametros(Map<String, Object> map) {
      mapParametros = map;
      paginaAnterior = (String) mapParametros.get("paginaAnterior");
      getListaCiudades();
      if (listaCiudades != null) {
         ciudadSeleccionada = listaCiudades.get(0);
      }
      anularBotonLOV();
   }

   public void navegar(String pag) {
      FacesContext fc = FacesContext.getCurrentInstance();
      ControlListaNavegacion controlListaNavegacion = (ControlListaNavegacion) fc.getApplication().evaluateExpressionGet(fc, "#{controlListaNavegacion}", ControlListaNavegacion.class);
      String pagActual = "ciudad";
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

   public void asignarIndex(Ciudades ciudad) {
      ciudadSeleccionada = ciudad;
      tipoActualizacion = 0;
      contarRegistroDep();
      activarBotonLOV();
      RequestContext.getCurrentInstance().update("formularioDialogos:departamentosDialogo");
      RequestContext.getCurrentInstance().execute("PF('departamentosDialogo').show()");
   }

   public void llamarLovDepartamento(int tipoN) {
      if (tipoN == 1) {
         tipoActualizacion = 1;
      } else if (tipoN == 2) {
         tipoActualizacion = 2;
      }
      contarRegistroDep();
      RequestContext.getCurrentInstance().update("formularioDialogos:departamentosDialogo");
      RequestContext.getCurrentInstance().execute("PF('departamentosDialogo').show()");
   }

   //DUPLICAR CIUDAD
   public void duplicarC() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (ciudadSeleccionada != null) {
         duplicarCiudad = new Ciudades();

         duplicarCiudad.setCodigo(ciudadSeleccionada.getCodigo());
         duplicarCiudad.setNombre(ciudadSeleccionada.getNombre());
         duplicarCiudad.setDepartamento(ciudadSeleccionada.getDepartamento());
         duplicarCiudad.setCodigoalternativo(ciudadSeleccionada.getCodigoalternativo());
         anularBotonLOV();
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCiudad");
         RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroCiudad').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void confirmarDuplicar() {
      k++;
      l = BigInteger.valueOf(k);
      duplicarCiudad.setSecuencia(l);
      RequestContext context = RequestContext.getCurrentInstance();
      int pasa = 0;
      anularBotonLOV();
      for (int i = 0; i < listaCiudades.size(); i++) {
         if (duplicarCiudad.getNombre().equals(listaCiudades.get(i).getNombre())) {
            log.info("Entro al IF");
            RequestContext.getCurrentInstance().update("formularioDialogos:existe");
            RequestContext.getCurrentInstance().execute("PF('existe').show()");
            pasa++;
         }
      }

      if (pasa == 0) {
         listaCiudades.add(duplicarCiudad);
         listaCiudadesCrear.add(duplicarCiudad);
         contarRegistrosCiudad();
         ciudadSeleccionada = listaCiudades.get(listaCiudades.indexOf(duplicarCiudad));
         if (tipoLista == 1) {
            altoTabla = "330";
         }
         RequestContext.getCurrentInstance().update("form:datosCiudades");
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         if (bandera == 1) {
            restablecerTabla();
         }
         duplicarCiudad = new Ciudades();

      }
      RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCiudad");
      RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroCiudad').hide()");
   }
   //LIMPIAR DUPLICAR

   public void limpiarduplicarCiudad() {
      duplicarCiudad = new Ciudades();
   }
//UBICACION CELDA

   public void cambiarIndice(Ciudades ciudad, int celda) {
      if (permitirIndex == true) {
         ciudadSeleccionada = ciudad;
         cualCelda = celda;
         nombreCiudad = ciudadSeleccionada.getNombre();
         if (cualCelda == 2) {
            activarBotonLOV();
            Departamento = ciudadSeleccionada.getDepartamento().getNombre();
         } else {
            anularBotonLOV();
         }
      }
   }

   //AUTOCOMPLETAR
   public void modificarCiudades(Ciudades ciudad, String confirmarCambio, String valorConfirmar) {
      ciudadSeleccionada = ciudad;
      RequestContext context = RequestContext.getCurrentInstance();

      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      if (ciudadSeleccionada.getNombre().isEmpty()) {
         RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevaCiudad");
         RequestContext.getCurrentInstance().execute("PF('validacionNuevaCiudad').show()");
         ciudadSeleccionada.setNombre(nombreCiudad);
         RequestContext.getCurrentInstance().update("form:datosCiudades");

      }
      if (confirmarCambio.equalsIgnoreCase("N")) {
         if (!listaCiudadesCrear.contains(ciudadSeleccionada)) {

            if (listaCiudadesModificar.isEmpty()) {
               listaCiudadesModificar.add(ciudadSeleccionada);
            } else if (!listaCiudadesModificar.contains(ciudadSeleccionada)) {
               listaCiudadesModificar.add(ciudadSeleccionada);
            }
            if (guardado == true) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
         }

         RequestContext.getCurrentInstance().update("form:datosCiudades");
      } else if (confirmarCambio.equalsIgnoreCase("DEPARTAMENTOS")) {
         ciudadSeleccionada.getDepartamento().setNombre(Departamento);

         for (int i = 0; i < lovDepartamentos.size(); i++) {
            if (lovDepartamentos.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            ciudadSeleccionada.setDepartamento(lovDepartamentos.get(indiceUnicoElemento));
            activarBotonLOV();
         } else {
            permitirIndex = false;
            RequestContext.getCurrentInstance().update("formularioDialogos:departamentosDialogo");
            RequestContext.getCurrentInstance().execute("PF('departamentosDialogo').show()");
            tipoActualizacion = 0;
         }
      }
      if (coincidencias == 1) {
         if (!listaCiudadesCrear.contains(ciudadSeleccionada)) {
            if (listaCiudadesModificar.isEmpty()) {
               listaCiudadesModificar.add(ciudadSeleccionada);
            } else if (!listaCiudadesModificar.contains(ciudadSeleccionada)) {
               listaCiudadesModificar.add(ciudadSeleccionada);
            }
            if (guardado == true) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
         }
      }
      RequestContext.getCurrentInstance().update("form:datosCiudades");
   }

   //MOSTRAR DATOS CELDA
   public void editarCelda() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (ciudadSeleccionada != null) {
         editarCiudad = ciudadSeleccionada;

         log.info("Entro a editar... valor celda: " + cualCelda);
         if (cualCelda == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarCodigos");
            RequestContext.getCurrentInstance().execute("PF('editarCodigos').show()");
            cualCelda = -1;
         } else if (cualCelda == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarNombresCiudades");
            RequestContext.getCurrentInstance().execute("PF('editarNombresCiudades').show()");
            cualCelda = -1;
         } else if (cualCelda == 2) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarDepartamentos");
            RequestContext.getCurrentInstance().execute("PF('editarDepartamentos').show()");
            cualCelda = -1;
         } else if (cualCelda == 3) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarCodigosAlternativos");
            RequestContext.getCurrentInstance().execute("PF('editarCodigosAlternativos').show()");
            cualCelda = -1;
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }
//LISTA DE VALORES DINAMICA

   public void listaValoresBoton() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (ciudadSeleccionada != null) {
         if (cualCelda == 2) {
            contarRegistroDep();
            RequestContext.getCurrentInstance().update("form:departamentosDialogo");
            RequestContext.getCurrentInstance().execute("PF('departamentosDialogo').show()");
            tipoActualizacion = 0;
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void activarCtrlF11() {
      log.info("TipoLista= " + tipoLista);
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 0) {
         tipoLista = 1;
         log.info("Activar");
         ciudadesCodigos = (Column) c.getViewRoot().findComponent("form:datosCiudades:ciudadesCodigos");
         ciudadesCodigos.setFilterStyle("width: 85% !important");
         ciudadesNombres = (Column) c.getViewRoot().findComponent("form:datosCiudades:ciudadesNombres");
         ciudadesNombres.setFilterStyle("width: 85% !important");
         nombresDepartamentos = (Column) c.getViewRoot().findComponent("form:datosCiudades:nombresDepartamentos");
         nombresDepartamentos.setFilterStyle("width: 85% !important");
         ciudadesCodigosAlternativos = (Column) c.getViewRoot().findComponent("form:datosCiudades:ciudadesCodigosAlternativos");
         ciudadesCodigosAlternativos.setFilterStyle("width: 85% !important");
         altoTabla = "310";
         RequestContext.getCurrentInstance().update("form:datosCiudades");
         bandera = 1;
         anularBotonLOV();
         log.info("TipoLista= " + tipoLista);
      } else if (bandera == 1) {
         restablecerTabla();
      }
   }

   //CREAR CIUDAD
   public void agregarNuevaCiudad() {
      int pasa = 0;
      int pasaA = 0;
      mensajeValidacion = " ";
      RequestContext context = RequestContext.getCurrentInstance();

      if (nuevaCiudad.getNombre() == null && (nuevaCiudad.getNombre().equals(" ")) || (nuevaCiudad.getNombre().equals(""))) {
         log.info("Entra");
         mensajeValidacion = mensajeValidacion + " * Nombre de la Ciudad \n";
         pasa++;
      }
      if (nuevaCiudad.getDepartamento().getSecuencia() == null) {
         log.info("Entra 2");
         mensajeValidacion = mensajeValidacion + "   * Departamento \n";
         pasa++;
      }

      for (int i = 0; i < listaCiudades.size(); i++) {
         if (nuevaCiudad.getNombre().equals(listaCiudades.get(i).getNombre())) {
            log.info("Entro al IF");
            RequestContext.getCurrentInstance().update("formularioDialogos:existe");
            RequestContext.getCurrentInstance().execute("PF('existe').show()");
            pasaA++;

         }
      }

      if (pasa != 0) {
         RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevaCiudad");
         RequestContext.getCurrentInstance().execute("PF('validacionNuevaCiudad').show()");
      }

      if (pasa == 0 && pasaA == 0) {
         if (bandera == 1) {
            restablecerTabla();
         }
         //AGREGAR REGISTRO A LA LISTA CIUDADES.
         k++;
         l = BigInteger.valueOf(k);
         nuevaCiudad.setSecuencia(l);
         listaCiudadesCrear.add(nuevaCiudad);
         listaCiudades.add(nuevaCiudad);
         contarRegistrosCiudad();
         ciudadSeleccionada = listaCiudades.get(listaCiudades.indexOf(nuevaCiudad));
         if (tipoLista == 1) {
            altoTabla = "330";
         }
         nuevaCiudad = new Ciudades();
         //  nuevaCiudad.setNombre(Departamento);
         nuevaCiudad.setDepartamento(new Departamentos());
         nuevaCiudad.getDepartamento().setNombre(" ");
         RequestContext.getCurrentInstance().update("form:datosCiudades");
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         anularBotonLOV();
         RequestContext.getCurrentInstance().execute("PF('NuevoRegistroCiudad').hide()");
      }
   }
   //LIMPIAR NUEVO REGISTRO CIUDAD

   public void limpiarNuevaCiudad() {
      nuevaCiudad = new Ciudades();
      nuevaCiudad.setDepartamento(new Departamentos());
      nuevaCiudad.getDepartamento().setNombre(" ");
      resultado = 0;
   }

   //EXPORTAR
   public void exportPDF() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosCiudadesExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "CiudadesPDF", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void exportXLS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosCiudadesExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "CiudadesXLS", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void valoresBackupAutocompletar(int tipoNuevo) {

      if (tipoNuevo == 1) {
         Departamento = nuevaCiudad.getDepartamento().getNombre();
      } else if (tipoNuevo == 2) {
         Departamento = duplicarCiudad.getDepartamento().getNombre();
      }
   }

   public void autocompletarNuevoyDuplicado(String valorConfirmar, int tipoNuevo) {
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoNuevo == 1) {
         nuevaCiudad.getDepartamento().setNombre(Departamento);
      } else if (tipoNuevo == 2) {
         duplicarCiudad.getDepartamento().setNombre(Departamento);
      }
      for (int i = 0; i < lovDepartamentos.size(); i++) {
         if (lovDepartamentos.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
            indiceUnicoElemento = i;
            coincidencias++;
         }
      }
      if (coincidencias == 1) {
         if (tipoNuevo == 1) {
            nuevaCiudad.setDepartamento(lovDepartamentos.get(indiceUnicoElemento));
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoDepartamento");
         } else if (tipoNuevo == 2) {
            duplicarCiudad.setDepartamento(lovDepartamentos.get(indiceUnicoElemento));
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarDepartamento");
         }
         lovDepartamentos.clear();
         getLovDepartamentos();
      } else {
         RequestContext.getCurrentInstance().update("form:departamentosDialogo");
         RequestContext.getCurrentInstance().execute("PF('departamentosDialogo').show()");
         tipoActualizacion = tipoNuevo;
         if (tipoNuevo == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoDepartamento");
         } else if (tipoNuevo == 2) {
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarDepartamento");
         }
      }
   }

//GUARDAR
   public void guardarCambiosCiudad() {
      boolean borrados = true;
      if (guardado == false) {
         RequestContext context = RequestContext.getCurrentInstance();
         if (!listaCiudadesBorrar.isEmpty()) {
            borrados = administrarCiudades.borrarCiudades(listaCiudadesBorrar);
            listaCiudadesBorrar.clear();
         }
         if (!listaCiudadesCrear.isEmpty()) {
            administrarCiudades.crearCiudades(listaCiudadesCrear);
            listaCiudadesCrear.clear();
         }
         if (!listaCiudadesModificar.isEmpty()) {
            administrarCiudades.modificarCiudades(listaCiudadesModificar);
            listaCiudadesModificar.clear();
         }
         if (!borrados) {
            context.execute("PF('errorBorrando').show()");
         }
         listaCiudades = null;
         getListaCiudades();
         contarRegistrosCiudad();
         context.update("form:informacionRegistro");
         context.update("form:datosCiudades");
         guardado = true;
         permitirIndex = true;
         FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         context.update("form:growl");
         context.update("form:ACEPTAR");
         k = 0;
         anularBotonLOV();
      }
   }

   public void salir() {
      limpiarListasValor();
      if (bandera == 1) {
         restablecerTabla();
      }
      resultado = 0;
      listaCiudadesBorrar.clear();
      listaCiudadesCrear.clear();
      listaCiudadesModificar.clear();
      ciudadSeleccionada = null;
      k = 0;
      listaCiudades = null;
      guardado = true;
      permitirIndex = true;

      RequestContext context = RequestContext.getCurrentInstance();
      context.update("form:ACEPTAR");
      context.update("form:datosCiudades");
      context.update("form:informacionRegistro");
      navegar("atras");
   }

   public void cancelarModificacion() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (bandera == 1) {
         restablecerTabla();
      }
      anularBotonLOV();

      listaCiudadesBorrar.clear();
      listaCiudadesCrear.clear();
      listaCiudadesModificar.clear();
      resultado = 0;
      ciudadSeleccionada = null;
      k = 0;
      listaCiudades = null;
      getListaCiudades();
      contarRegistrosCiudad();
      guardado = true;
      permitirIndex = true;
      context.update("form:ACEPTAR");
      context.update("form:datosCiudades");
      context.update("form:informacionRegistro");
   }

   //METODOS L.O.V CIUDADES
   public void actualizarDepartamentos() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {
         ciudadSeleccionada.setDepartamento(seleccionDepartamento);
         if (!listaCiudadesCrear.contains(ciudadSeleccionada)) {
            if (listaCiudadesModificar.isEmpty()) {
               listaCiudadesModificar.add(ciudadSeleccionada);
            } else if (!listaCiudadesModificar.contains(ciudadSeleccionada)) {
               listaCiudadesModificar.add(ciudadSeleccionada);
            }
         }

         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         permitirIndex = true;
         RequestContext.getCurrentInstance().update("form:datosCiudades");
      } else if (tipoActualizacion == 1) {
         nuevaCiudad.setDepartamento(seleccionDepartamento);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoDepartamento");
      } else if (tipoActualizacion == 2) {
         log.info(seleccionDepartamento.getNombre());
         duplicarCiudad.setDepartamento(seleccionDepartamento);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarDepartamento");
      }
      filtradoListaDepatartamentos = null;
      seleccionDepartamento = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
      context.reset("formularioDialogos:LOVDepartamentos:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('LOVDepartamentos').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('departamentosDialogo').hide()");
      RequestContext.getCurrentInstance().update("formularioDialogos:LOVDepartamentos");
   }

   public void activarAceptar() {
      aceptar = false;
   }

   public void cancelarCambioDepartamentos() {
      RequestContext context = RequestContext.getCurrentInstance();
      filtradoListaDepatartamentos = null;
      seleccionDepartamento = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
      permitirIndex = true;
      context.reset("formularioDialogos:LOVDepartamentos:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('LOVDepartamentos').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('departamentosDialogo').hide()");
      RequestContext.getCurrentInstance().update("formularioDialogos:LOVDepartamentos");
   }

   //BORRAR CIUDADES
   public void borrarCiudades() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (ciudadSeleccionada != null) {
         resultado = administrarCiudades.existeenUbicacionesGeograficas(ciudadSeleccionada.getSecuencia());
         if (resultado == 0) {
            if (!listaCiudadesModificar.isEmpty() && listaCiudadesModificar.contains(ciudadSeleccionada)) {
               listaCiudadesModificar.remove(ciudadSeleccionada);
               listaCiudadesBorrar.add(ciudadSeleccionada);
            } else if (!listaCiudadesCrear.isEmpty() && listaCiudadesCrear.contains(ciudadSeleccionada)) {
               listaCiudadesCrear.remove(ciudadSeleccionada);
            } else {
               listaCiudadesBorrar.add(ciudadSeleccionada);
            }
            listaCiudades.remove(ciudadSeleccionada);
            if (tipoLista == 1) {
               filtradoListaCiudades.remove(ciudadSeleccionada);
            }
            contarRegistrosCiudad();
            RequestContext.getCurrentInstance().update("form:datosCiudades");
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            ciudadSeleccionada = null;
            anularBotonLOV();
            if (guardado == true) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
         } else {
            RequestContext.getCurrentInstance().execute("PF('validacionBorradoCiudad').show()");
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void valoresBackupAutocompletar(int tipoNuevo, String Campo) {
      if (Campo.equals("DEP")) {
         if (tipoNuevo == 1) {
            Departamento = nuevaCiudad.getDepartamento().getNombre();
         } else if (tipoNuevo == 2) {
            Departamento = duplicarCiudad.getDepartamento().getNombre();
         }
      }
   }

   public void verificarRastro() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (ciudadSeleccionada != null) {
         int resultado = administrarRastros.obtenerTabla(ciudadSeleccionada.getSecuencia(), "CIUDADES");
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
      } else if (administrarRastros.verificarHistoricosTabla("CIUDADES")) {
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
      anularBotonLOV();
   }

   public void restablecerTabla() {
      //CERRAR FILTRADO
      FacesContext c = FacesContext.getCurrentInstance();
      altoTabla = "330";
      ciudadesCodigos = (Column) c.getViewRoot().findComponent("form:datosCiudades:ciudadesCodigos");
      ciudadesCodigos.setFilterStyle("display: none; visibility: hidden;");
      ciudadesNombres = (Column) c.getViewRoot().findComponent("form:datosCiudades:ciudadesNombres");
      ciudadesNombres.setFilterStyle("display: none; visibility: hidden;");
      nombresDepartamentos = (Column) c.getViewRoot().findComponent("form:datosCiudades:nombresDepartamentos");
      nombresDepartamentos.setFilterStyle("display: none; visibility: hidden;");
      ciudadesCodigosAlternativos = (Column) c.getViewRoot().findComponent("form:datosCiudades:ciudadesCodigosAlternativos");
      ciudadesCodigosAlternativos.setFilterStyle("display: none; visibility: hidden;");
      bandera = 0;
      filtradoListaCiudades = null;
      tipoLista = 0;
      RequestContext.getCurrentInstance().update("form:datosCiudades");
   }

   public void recordarSeleccion() {
      if (ciudadSeleccionada != null) {
         FacesContext c = FacesContext.getCurrentInstance();
         tablaC = (DataTable) c.getViewRoot().findComponent("form:datosCiudades");
         tablaC.setSelection(ciudadSeleccionada);
      }
   }

   public void anularBotonLOV() {
      activarLOV = true;
      RequestContext.getCurrentInstance().update("form:listaValores");
   }

   public void activarBotonLOV() {
      activarLOV = false;
      RequestContext.getCurrentInstance().update("form:listaValores");
   }

   //EVENTO FILTRAR
   public void eventoFiltrar() {
      if (tipoLista == 0) {
         tipoLista = 1;
      }
      ciudadSeleccionada = null;
      anularBotonLOV();
      contarRegistrosCiudad();
   }

   public void contarRegistrosCiudad() {
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
   }

   public void contarRegistroDep() {
      RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroDepartamentos");
   }

//GETTER AND SETTER
   public List<Ciudades> getListaCiudades() {
      try {
         if (listaCiudades == null) {
            listaCiudades = administrarCiudades.consultarCiudades();
         }
         return listaCiudades;
      } catch (Exception e) {
         return listaCiudades = null;
      }
   }

   public void setListaCiudades(List<Ciudades> listaCiudades) {
      this.listaCiudades = listaCiudades;
   }

   public List<Ciudades> getFiltradoListaCiudades() {
      return filtradoListaCiudades;
   }

   public void setFiltradoListaCiudades(List<Ciudades> filtradoListaCiudades) {
      this.filtradoListaCiudades = filtradoListaCiudades;
   }

   public List<Departamentos> getLovDepartamentos() {
      if (lovDepartamentos == null) {
         lovDepartamentos = administrarDepartamentos.consultarDepartamentos();
      }
      return lovDepartamentos;
   }

   public void setLovDepartamentos(List<Departamentos> lovDepartamentos) {
      this.lovDepartamentos = lovDepartamentos;
   }

   public List<Departamentos> getFiltradoListaDepatartamentos() {
      return filtradoListaDepatartamentos;
   }

   public void setFiltradoListaDepatartamentos(List<Departamentos> filtradoListaDepatartamentos) {
      this.filtradoListaDepatartamentos = filtradoListaDepatartamentos;
   }

   public Departamentos getSeleccionDepartamento() {
      return seleccionDepartamento;
   }

   public void setSeleccionDepartamento(Departamentos seleccionDepartamento) {
      this.seleccionDepartamento = seleccionDepartamento;
   }

   public boolean isAceptar() {
      return aceptar;
   }

   public Ciudades getEditarCiudad() {
      return editarCiudad;
   }

   public void setEditarCiudad(Ciudades editarCiudad) {
      this.editarCiudad = editarCiudad;
   }

   public boolean isAceptarEditar() {
      return aceptarEditar;
   }

   public void setAceptarEditar(boolean aceptarEditar) {
      this.aceptarEditar = aceptarEditar;
   }

   public Ciudades getNuevaCiudad() {
      return nuevaCiudad;
   }

   public Ciudades getDuplicarCiudad() {
      return duplicarCiudad;
   }

   public void setDuplicarCiudad(Ciudades duplicarCiudad) {
      this.duplicarCiudad = duplicarCiudad;
   }

   public void setNuevaCiudad(Ciudades nuevaCiudad) {
      this.nuevaCiudad = nuevaCiudad;
   }

   public String getMensajeValidacion() {
      return mensajeValidacion;
   }

   public Ciudades getCiudadSeleccionada() {
      return ciudadSeleccionada;
   }

   public void setCiudadSeleccionada(Ciudades ciudadSeleccionada) {
      this.ciudadSeleccionada = ciudadSeleccionada;
   }

   public String getAltoTabla() {
      return altoTabla;
   }

   public void setAltoTabla(String altoTabla) {
      this.altoTabla = altoTabla;
   }

   public boolean isGuardado() {
      return guardado;
   }

   public void setGuardado(boolean guardado) {
      this.guardado = guardado;
   }

   public String getInfoRegistroCiudad() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosCiudades");
      infoRegistroCiudad = String.valueOf(tabla.getRowCount());
      return infoRegistroCiudad;
   }

   public String getInfoRegistroDep() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:LOVDepartamentos");
      infoRegistroDep = String.valueOf(tabla.getRowCount());
      return infoRegistroDep;
   }

   public boolean isActivarLOV() {
      return activarLOV;
   }

   public void setActivarLOV(boolean activarLOV) {
      this.activarLOV = activarLOV;
   }
}
