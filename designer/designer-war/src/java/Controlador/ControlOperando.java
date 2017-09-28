/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Operandos;
import Entidades.Modulos;
import Exportar.ExportarPDFTablasAnchas;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarOperandosInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
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
import javax.faces.bean.SessionScoped;
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
@SessionScoped
public class ControlOperando implements Serializable {

   private static Logger log = Logger.getLogger(ControlOperando.class);

   @EJB
   AdministrarOperandosInterface administrarOperandos;
   @EJB
   AdministrarRastrosInterface administrarRastros;

   private Operandos operandoBaseClon;
   private Operandos operandoSeleccionado;
   private List<Operandos> listaOperandos;
   private List<Operandos> filtradoOperandos;
   //LOV
   private List<Operandos> lovOperandos;
   private List<Operandos> filtradoLovOperandos;
   private Operandos operandoLovSeleccionado;
   //editar celda
   private Operandos editarOperandos;
   private int cualCelda, tipoLista;
   //OTROS
   private boolean aceptar;
   private int tipoActualizacion; //Activo/Desactivo Crtl + F11
   private int bandera, tipoLLamado;
   //RASTROS
   private boolean guardado;
   //Crear Novedades
   private List<Operandos> listaOperandosCrear;
   public Operandos nuevoOperando;
   public Operandos duplicarOperando;
   private int k;
   private BigInteger l;
   private String mensajeValidacion;
   //Modificar Novedades
   private List<Operandos> listaOperandosModificar;
   //Borrar Novedades
   private List<Operandos> listaOperandosBorrar;
   //L.O.V MODULOS
   private List<Modulos> lovListaModulos;
   private List<Modulos> lovFiltradoslistaModulos;
   private Modulos moduloSeleccionado;
   //AUTOCOMPLETAR
   private String Modulo;
   //Columnas Tabla Ciudades
   private Column operandosNombres, operandosTipos, operandosValores, operandosDescripciones, operandosCodigos;
   //ALTO SCROLL TABLA
   private String altoTabla, altoTablaReg;
   //Cambios de PAgina
   public String action;
   public String tipoOperando;
   public String infoRegistro, infoRegistroLovOp;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();
   private String nombreNuevoClon, descripcionNuevoClon;
   String errorClonado;

   public ControlOperando() {
      lovOperandos = null;
      operandoBaseClon = new Operandos();
      nuevoOperando = new Operandos();
      nuevoOperando.setCodigo(0);
      nuevoOperando.setDescripcion("");
      nuevoOperando.setNombre("");
      nuevoOperando.setTipo("CONSTANTE");
      lovListaModulos = null;
      listaOperandos = null;
      aceptar = true;
      operandoSeleccionado = null;
      guardado = true;
      tipoLista = 0;
      listaOperandosBorrar = new ArrayList<Operandos>();
      listaOperandosCrear = new ArrayList<Operandos>();
      listaOperandosModificar = new ArrayList<Operandos>();
      altoTabla = "242";
      altoTablaReg = "10";
      duplicarOperando = new Operandos();
      mapParametros.put("paginaAnterior", paginaAnterior);
      tipoLLamado = 0;
      errorClonado = "";
      nombreNuevoClon = "";
      descripcionNuevoClon = "";
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
         administrarOperandos.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
      } catch (Exception e) {
         log.error("Error postconstruct " + this.getClass().getName() + ":  ", e);
         log.error("Causa: " + e.getCause());
      }
   }

   public void limpiarListasValor() {
      lovOperandos = null;
      lovListaModulos = null;
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
      String pagActual = "operando";
      Map<String, Object> mapParaEnviar = new LinkedHashMap<String, Object>();
      mapParaEnviar = new LinkedHashMap<String, Object>();
      mapParaEnviar.put("paginaAnterior", pagActual);
      mapParaEnviar.put("operandoActual", operandoSeleccionado);
//         mas Parametros
      //if (pag.equals("rastrotabla")) {
      //ControlRastro controlRastro = (ControlRastro) fc.getApplication().evaluateExpressionGet(fc, "#{controlRastro}", ControlRastro.class);

      if (pag.equals("FORMULA")) {
         pag = "tipoformula";
         ControlTipoFormula controlTipoFormula = (ControlTipoFormula) fc.getApplication().evaluateExpressionGet(fc, "#{controlTipoFormula}", ControlTipoFormula.class);
         controlTipoFormula.recibirParametros(mapParaEnviar);
      } else if (pag.equals("CONSTANTE")) {
         pag = "tipoconstante";
         ControlTipoConstante controlTipoConstante = (ControlTipoConstante) fc.getApplication().evaluateExpressionGet(fc, "#{controlTipoConstante}", ControlTipoConstante.class);
         controlTipoConstante.recibirParametros(mapParaEnviar);
      } else if (pag.equals("BLOQUE PL/SQL")) {
         pag = "tipobloque";
         ControlTipoBloque controlTipoBloque = (ControlTipoBloque) fc.getApplication().evaluateExpressionGet(fc, "#{controlTipoBloque}", ControlTipoBloque.class);
         controlTipoBloque.recibirParametros(mapParaEnviar);
      } else if (pag.equals("FUNCION")) {
         pag = "tipofuncion";
         ControlTipoFuncion controlTipoFuncion = (ControlTipoFuncion) fc.getApplication().evaluateExpressionGet(fc, "#{controlTipoFuncion}", ControlTipoFuncion.class);
         controlTipoFuncion.recibirParametros(mapParaEnviar);
      } else if (pag.equals("novedadoperando")) {
         pag = "novedadoperando";
         ControlNovedadOperando controlNovedadOperando = (ControlNovedadOperando) fc.getApplication().evaluateExpressionGet(fc, "#{controlNovedadOperando}", ControlNovedadOperando.class);
         controlNovedadOperando.recibirParametros(mapParaEnviar);
      }
      if (pag.equals("atras")) {
         pag = paginaAnterior;
         paginaAnterior = "nominaf";
         controlListaNavegacion.quitarPagina(pagActual, this.getClass().getSimpleName());
      } else {
         RequestContext.getCurrentInstance().execute("PF('datosOperandos').clearFilters()");
         controlListaNavegacion.guardarNavegacion(pagActual, pag);
         fc.getApplication().getNavigationHandler().handleNavigation(fc, null, pag);
      }
      limpiarListasValor();
   }

   public void seleccionarTipo(String estadoTipo, int indice, int celda) {
      if (estadoTipo.equals("FORMULA")) {
         operandoSeleccionado.setTipo("FORMULA");
      } else if (estadoTipo.equals("BLOQUE PL/SQL")) {
         operandoSeleccionado.setTipo("BLOQUE PL/SQL");
      } else if (estadoTipo.equals("CONSTANTE")) {
         operandoSeleccionado.setTipo("CONSTANTE");
      } else if (estadoTipo.equals("FUNCION")) {
         operandoSeleccionado.setTipo("FUNCION");
      } else if (estadoTipo.equals("RELACIONAL")) {
         operandoSeleccionado.setTipo("RELACIONAL");
      }
      if (!listaOperandosCrear.contains(operandoSeleccionado)) {
         if (listaOperandosModificar.isEmpty()) {
            listaOperandosModificar.add(operandoSeleccionado);
         } else if (!listaOperandosModificar.contains(operandoSeleccionado)) {
            listaOperandosModificar.add(operandoSeleccionado);
         }
      }
      if (guardado == true) {
         guardado = false;
      }
      RequestContext.getCurrentInstance().update("form:datosOperandos");
   }

   public void irDetalle(Operandos operando) {
      operandoSeleccionado = operando;
      log.info("ControlOperando.irDetalle() operandoSeleccionado.getTipo() : " + operandoSeleccionado.getTipo());
      if (operandoSeleccionado.getTipo().equals("FORMULA")) {
         navegar("FORMULA");
      } else if (operandoSeleccionado.getTipo().equals("BLOQUE PL/SQL")) {
         navegar("BLOQUE PL/SQL");
      } else if (operandoSeleccionado.getTipo().equals("CONSTANTE")) {
         navegar("CONSTANTE");
      } else if (operandoSeleccionado.getTipo().equals("FUNCION")) {
         navegar("FUNCION");
      } else if (operandoSeleccionado.getTipo().equals("RELACIONAL")) {
//         navegar("RELACIONAL"); No tiene pagina
      }
   }

   //UBICACION CELDA
   public void cambiarIndice(Operandos operando, int celda) {
      operandoSeleccionado = operando;
      cualCelda = celda;
   }

   //AUTOCOMPLETAR
   public void modificarOperandos(Operandos operando, String confirmarCambio, String valorConfirmar) {
      operandoSeleccionado = operando;
      if (confirmarCambio.equalsIgnoreCase("N")) {
         if (!listaOperandosCrear.contains(operandoSeleccionado)) {

            if (listaOperandosModificar.isEmpty()) {
               listaOperandosModificar.add(operandoSeleccionado);
            } else if (!listaOperandosModificar.contains(operandoSeleccionado)) {
               listaOperandosModificar.add(operandoSeleccionado);
            }
            if (guardado == true) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
         }
         RequestContext.getCurrentInstance().update("form:datosOperandos");
      }
   }

   public void eventoFiltrar() {
      if (tipoLista == 0) {
         tipoLista = 1;
      }
      operandoSeleccionado = null;
      contarRegistros();
   }

   public void guardarVariables(Operandos op) {
      operandoSeleccionado = op;
      if (operandoSeleccionado != null) {
         if (listaOperandosCrear.isEmpty() && listaOperandosBorrar.isEmpty() && listaOperandosModificar.isEmpty()) {
            navegar("novedadoperando");
         } else {
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void verificarTipo(BigInteger secuencia) {
      if (listaOperandosCrear.isEmpty() && listaOperandosBorrar.isEmpty() && listaOperandosModificar.isEmpty()) {
         if (operandoSeleccionado != null) {
            if (operandoSeleccionado.getTipo().equals("FUNCION")) {
               action = "funcion";
               tipoOperando = operandoSeleccionado.getTipo();
               RequestContext.getCurrentInstance().execute("PF('dirigirTipoFuncion()");
            }
            if (operandoSeleccionado.getTipo().equals("FORMULA")) {
               action = "formula";
               tipoOperando = operandoSeleccionado.getTipo();
               RequestContext.getCurrentInstance().execute("PF('dirigirTipoFormula()");
            }
            if (operandoSeleccionado.getTipo().equals("CONSTANTE")) {
               action = "constante";
               tipoOperando = operandoSeleccionado.getTipo();
               RequestContext.getCurrentInstance().execute("PF('dirigirTipoConstante()");
            }
            if (operandoSeleccionado.getTipo().equals("BLOQUE PL/SQL")) {
               action = "bloque";
               tipoOperando = operandoSeleccionado.getTipo();
               RequestContext.getCurrentInstance().execute("PF('dirigirTipoBloque()");
            }
         } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
      }
   }

   public void seleccionarTipoNuevoOperando(String estadoTipo, int tipoNuevo) {
      if (tipoNuevo == 1) {
         if (estadoTipo.equals("FORMULA")) {
            nuevoOperando.setTipo("FORMULA");
         } else if (estadoTipo.equals("BLOQUE PL/SQL")) {
            nuevoOperando.setTipo("BLOQUE PL/SQL");
         } else if (estadoTipo.equals("CONSTANTE")) {
            nuevoOperando.setTipo("CONSTANTE");
         } else if (estadoTipo.equals("FUNCION")) {
            nuevoOperando.setTipo("FUNCION");
         } else if (estadoTipo.equals("RELACIONAL")) {
            nuevoOperando.setTipo("RELACIONAL");
         }
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTipo");
      } else {
         if (estadoTipo.equals("FORMULA")) {
            duplicarOperando.setTipo("FORMULA");
         } else if (estadoTipo.equals("BLOQUE PL/SQL")) {
            duplicarOperando.setTipo("BLOQUE PL/SQL");
         } else if (estadoTipo.equals("CONSTANTE")) {
            duplicarOperando.setTipo("CONSTANTE");
         } else if (estadoTipo.equals("FUNCION")) {
            duplicarOperando.setTipo("FUNCION");
         } else if (estadoTipo.equals("RELACIONAL")) {
            duplicarOperando.setTipo("RELACIONAL");
         }
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTipo");
      }
   }

   public void asignarIndex(int tipoLLamado) {
      this.tipoLLamado = tipoLLamado;
      operandoLovSeleccionado = null;
      RequestContext.getCurrentInstance().update("formularioDialogos:operandosDialogo");
      RequestContext.getCurrentInstance().update("formularioDialogos:LOVOperandos");
      RequestContext.getCurrentInstance().execute("PF('operandosDialogo').show()");
   }

   public void autoCompletarClonado(String valor, int tipoAutoComp) {
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      if (tipoAutoComp == 0) {
         short num = Short.parseShort(valor);
         for (int i = 0; i < lovOperandos.size(); i++) {
            if (lovOperandos.get(i).getCodigo() == num) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            operandoBaseClon = lovOperandos.get(indiceUnicoElemento);
         } else {
            operandoBaseClon.setCodigo(new Short("0"));
            operandoBaseClon.setDescripcion("");
            RequestContext.getCurrentInstance().update("form:CodigoBaseClonado");
            RequestContext.getCurrentInstance().update("form:NombreBaseClonado");
            RequestContext.getCurrentInstance().update("formularioDialogos:operandosDialogo");
            RequestContext.getCurrentInstance().update("formularioDialogos:LOVOperandos");
            contarRegistrosLovOp();
            RequestContext.getCurrentInstance().execute("PF('operandosDialogo').show()");
         }
      }
      if (tipoAutoComp == 1) {
         for (int i = 0; i < lovOperandos.size(); i++) {
            if (lovOperandos.get(i).getNombre().startsWith(valor.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            operandoBaseClon = lovOperandos.get(indiceUnicoElemento);
         } else {
            operandoBaseClon.setCodigo(new Short("0"));
            operandoBaseClon.setDescripcion("");
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("form:CodigoBaseClonado");
            context.update("form:NombreBaseClonado");
            context.update("form:NombreNuevoClonado");
            context.update("form:DescripcionNuevoClonado");
            context.update("formularioDialogos:operandosDialogo");
            context.update("formularioDialogos:LOVOperandos");
            contarRegistrosLovOp();
            context.execute("PF('operandosDialogo').show()");
         }
      }
   }

   public void mostrarTodos() {
      listaOperandos = administrarOperandos.buscarOperandos();
      RequestContext.getCurrentInstance().update("form:datosOperandos");
      contarRegistros();
      filtradoOperandos = null;
      aceptar = true;
      operandoSeleccionado = null;
      tipoActualizacion = -1;
      cualCelda = -1;
   }

   public void confirmarDuplicar() {
      int pasa = 0;
      mensajeValidacion = new String();
      if (duplicarOperando.getDescripcion() == null || duplicarOperando.getDescripcion().equals(" ")) {
         mensajeValidacion = mensajeValidacion + " * Descripcion\n";
         pasa++;
      }
      if (duplicarOperando.getCodigo() == 0) {
         mensajeValidacion = mensajeValidacion + " * Codigo\n";
         pasa++;
      }
      if (duplicarOperando.getNombre().equals(" ") || duplicarOperando.getNombre() == null) {
         mensajeValidacion = mensajeValidacion + " * Nombre\n";
         pasa++;
      }
      if (duplicarOperando.getTipo() == null) {
         mensajeValidacion = mensajeValidacion + " * Tipo\n";
         pasa++;
      }
      if (pasa != 0) {
         RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevoOperando");
         RequestContext.getCurrentInstance().execute("PF('validacionNuevoOperando').show()");
      }
      if (pasa == 0) {
         if (bandera == 1) {
            restaurarTabla();
         }
         listaOperandos.add(duplicarOperando);
         listaOperandosCrear.add(duplicarOperando);
         operandoSeleccionado = listaOperandos.get(listaOperandos.indexOf(duplicarOperando));

         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         RequestContext.getCurrentInstance().update("form:datosOperandos");
         contarRegistros();
         duplicarOperando = new Operandos();
         RequestContext.getCurrentInstance().update("formularioDialogos:DuplicarOperando");
         RequestContext.getCurrentInstance().execute("PF('DuplicarOperando').hide()");
      }
   }

   public void activarAceptar() {
      aceptar = false;
   }

   public void actualizarOperandos() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoLLamado == 1) {
         operandoBaseClon = operandoLovSeleccionado;
         context.update("form:CodigoBaseClonado");
         context.update("form:NombreBaseClonado");
      } else {
         if (listaOperandos == null) {
            listaOperandos = new ArrayList<Operandos>();
         }
         if (!listaOperandos.isEmpty()) {
            listaOperandos.clear();
         }
         listaOperandos.add(operandoLovSeleccionado);
         context.reset("formularioDialogos:LOVOperandos:globalFilter");
         context.execute("PF('LOVOperandos').clearFilters()");
         context.update("formularioDialogos:LOVOperandos");
         context.update("formularioDialogos:operandosDialogo");
         context.update("form:datosOperandos");
         context.execute("PF('operandosDialogo').hide()");
         contarRegistros();
         filtradoOperandos = null;
         operandoLovSeleccionado = null;
         aceptar = true;
         tipoActualizacion = -1;
         cualCelda = -1;
      }
   }

   public void activarCtrlF11() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 0) {
         altoTabla = "222";
         altoTablaReg = "9";
         operandosNombres = (Column) c.getViewRoot().findComponent("form:datosOperandos:operandosNombres");
         operandosNombres.setFilterStyle("width: 85% !important;");
         operandosTipos = (Column) c.getViewRoot().findComponent("form:datosOperandos:operandosTipos");
         operandosTipos.setFilterStyle("width: 85% !important;");
         operandosValores = (Column) c.getViewRoot().findComponent("form:datosOperandos:operandosValores");
         operandosValores.setFilterStyle("width: 85% !important;");
         operandosDescripciones = (Column) c.getViewRoot().findComponent("form:datosOperandos:operandosDescripciones");
         operandosDescripciones.setFilterStyle("width: 85% !important;");
         operandosCodigos = (Column) c.getViewRoot().findComponent("form:datosOperandos:operandosCodigos");
         operandosCodigos.setFilterStyle("width: 85% !important;");
         RequestContext.getCurrentInstance().update("form:datosOperandos");
         bandera = 1;
         tipoLista = 1;
      } else if (bandera == 1) {
         restaurarTabla();
      }
      contarRegistros();
   }

   public void restaurarTabla() {
      FacesContext c = FacesContext.getCurrentInstance();
      altoTabla = "242";
      altoTablaReg = "10";
      log.info("Desactivar");
      log.info("TipoLista= " + tipoLista);
      operandosNombres = (Column) c.getViewRoot().findComponent("form:datosOperandos:operandosNombres");
      operandosNombres.setFilterStyle("display: none; visibility: hidden;");
      operandosTipos = (Column) c.getViewRoot().findComponent("form:datosOperandos:operandosTipos");
      operandosTipos.setFilterStyle("display: none; visibility: hidden;");
      operandosValores = (Column) c.getViewRoot().findComponent("form:datosOperandos:operandosValores");
      operandosValores.setFilterStyle("display: none; visibility: hidden;");
      operandosDescripciones = (Column) c.getViewRoot().findComponent("form:datosOperandos:operandosDescripciones");
      operandosDescripciones.setFilterStyle("display: none; visibility: hidden;");
      operandosCodigos = (Column) c.getViewRoot().findComponent("form:datosOperandos:operandosCodigos");
      operandosCodigos.setFilterStyle("display: none; visibility: hidden;");
      RequestContext.getCurrentInstance().execute("PF('datosOperandos').clearFilters()");
      RequestContext.getCurrentInstance().update("form:datosOperandos");
      contarRegistros();
      bandera = 0;
      filtradoOperandos = null;
      tipoLista = 0;
   }

   public void cancelarYSalir() {
//      cancelarModificacion();
      salir();
   }

   public void cancelarModificacion() {
      if (bandera == 1) {
         restaurarTabla();
      }
      listaOperandosBorrar.clear();
      listaOperandosCrear.clear();
      listaOperandosModificar.clear();
      operandoSeleccionado = null;
      k = 0;
      listaOperandos = null;
      guardado = true;
      operandoBaseClon = new Operandos();
      nombreNuevoClon = "";
      descripcionNuevoClon = "";
      RequestContext context = RequestContext.getCurrentInstance();
      context.update("form:CodigoBaseClonado");
      context.update("form:NombreBaseClonado");
      context.update("form:NombreNuevoClonado");
      context.update("form:DescripcionNuevoClonado");
      context.update("form:ACEPTAR");
      context.update("form:datosOperandos");
      contarRegistros();
   }

   public void cancelarCambioOperandos() {
      filtradoLovOperandos = null;
      operandoLovSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formularioDialogos:LOVOperandos:globalFilter");
      context.execute("PF('LOVOperandos').clearFilters()");
      context.execute("PF('operandosDialogo').hide()");
   }

   public void cancelarCambioModulos() {
      lovFiltradoslistaModulos = null;
      moduloSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
   }

   //MOSTRAR DATOS CELDA
   public void editarCelda() {
      if (operandoSeleccionado != null) {
         editarOperandos = operandoSeleccionado;
         if (cualCelda == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarNombres");
            RequestContext.getCurrentInstance().execute("PF('editarNombres').show()");
         } else if (cualCelda == 3) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarValor");
            RequestContext.getCurrentInstance().execute("PF('editarValores').show()");
         } else if (cualCelda == 4) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarDescripciones");
            RequestContext.getCurrentInstance().execute("PF('editarDescripciones').show()");
         } else if (cualCelda == 7) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarCodigos");
            RequestContext.getCurrentInstance().execute("PF('editarCodigos').show()");
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   //EXPORTAR
   public void exportPDF() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosOperandosExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDFTablasAnchas();
      exporter.export(context, tabla, "OperandosPDF", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void exportXLS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosOperandosExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "OperandosXLS", false, false, "UTF-8", null, null);
      context.responseComplete();
   }
   //LIMPIAR NUEVO REGISTRO CIUDAD

   public void limpiarNuevoOperando() {
      nuevoOperando = new Operandos();
      nuevoOperando.setCodigo(0);
      nuevoOperando.setDescripcion(" ");
      nuevoOperando.setNombre(" ");
      nuevoOperando.setTipo("CONSTANTE");
   }

   public void limpiarduplicarOperandos() {
      duplicarOperando = new Operandos();
      duplicarOperando.setCodigo(0);
      duplicarOperando.setDescripcion(" ");
      duplicarOperando.setNombre(" ");
      duplicarOperando.setTipo("CONSTANTE");
   }

   //CREAR Operando
   public void agregarNuevoOperando() {
      int pasa = 0;
      mensajeValidacion = new String();

      RequestContext context = RequestContext.getCurrentInstance();

      if (nuevoOperando.getDescripcion() == null || nuevoOperando.getDescripcion().equals(" ")) {
         mensajeValidacion = mensajeValidacion + " * Descripcion\n";
         pasa++;
      }

      if (nuevoOperando.getNombre().equals(" ") || nuevoOperando.getNombre() == null) {
         mensajeValidacion = mensajeValidacion + " * Nombre\n";
         pasa++;
      }
      if (nuevoOperando.getTipo() == null) {
         mensajeValidacion = mensajeValidacion + " * Tipo\n";
         pasa++;
      }

      if (pasa != 0) {
         RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevoOperando");
         RequestContext.getCurrentInstance().execute("PF('validacionNuevoOperando').show()");
      }
      if (pasa == 0) {
         if (bandera == 1) {
            restaurarTabla();
         }
         //AGREGAR REGISTRO A LA LISTA NOVEDADES .
         k++;
         l = BigInteger.valueOf(k);
         nuevoOperando.setSecuencia(l);

         listaOperandosCrear.add(nuevoOperando);
         listaOperandos.add(nuevoOperando);
         operandoSeleccionado = listaOperandos.get(listaOperandos.indexOf(nuevoOperando));
         nuevoOperando = new Operandos();
         RequestContext.getCurrentInstance().update("form:datosOperandos");
         contarRegistros();
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         RequestContext.getCurrentInstance().execute("PF('NuevoOperando').hide()");
      }
   }

   //BORRAR CIUDADES
   public void borrarOperandos() {
      if (operandoSeleccionado != null) {
         if (!listaOperandosModificar.isEmpty() && listaOperandosModificar.contains(operandoSeleccionado)) {
            listaOperandosModificar.remove(operandoSeleccionado);
            listaOperandosBorrar.add(operandoSeleccionado);
         } else if (!listaOperandosCrear.isEmpty() && listaOperandosCrear.contains(operandoSeleccionado)) {
            listaOperandosCrear.remove(operandoSeleccionado);
         } else {
            listaOperandosBorrar.add(operandoSeleccionado);
         }
         listaOperandos.remove(operandoSeleccionado);
         if (tipoLista == 1) {
            filtradoOperandos.remove(operandoSeleccionado);
         }
         RequestContext.getCurrentInstance().update("form:datosOperandos");
         contarRegistros();
         operandoSeleccionado = null;
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void guardarYSalir() {
      guardarCambiosOperandos();
      salir();
   }

   //GUARDAR
   public void guardarCambiosOperandos() {
      if (guardado == false) {
         if (!listaOperandosBorrar.isEmpty()) {
            for (int i = 0; i < listaOperandosBorrar.size(); i++) {
               log.info("Borrando..." + listaOperandosBorrar.size());

               administrarOperandos.borrarOperando(listaOperandosBorrar.get(i));
            }
            log.info("Entra");
            listaOperandosBorrar.clear();
         }
         if (!listaOperandosCrear.isEmpty()) {
            for (int i = 0; i < listaOperandosCrear.size(); i++) {
               log.info("Creando...");

               administrarOperandos.crearOperando(listaOperandosCrear.get(i));
            }
            log.info("LimpiaLista");
            listaOperandosCrear.clear();
         }
         if (!listaOperandosModificar.isEmpty()) {
            administrarOperandos.modificarOperando(listaOperandosModificar);
            listaOperandosModificar.clear();
         }
         listaOperandos = null;
         RequestContext.getCurrentInstance().update("form:datosOperandos");
         contarRegistros();
         guardado = true;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }
   }

   //DUPLICAR Operando
   public void duplicarO() {
      if (operandoSeleccionado != null) {
         duplicarOperando = new Operandos();
         k++;
         l = BigInteger.valueOf(k);
         duplicarOperando.setSecuencia(l);
         duplicarOperando.setNombre(operandoSeleccionado.getNombre());
         duplicarOperando.setTipo(operandoSeleccionado.getTipo());
         duplicarOperando.setDescripcion(operandoSeleccionado.getDescripcion());
         duplicarOperando.setCambioanual(operandoSeleccionado.getCambioanual());
         duplicarOperando.setActualizable(operandoSeleccionado.getActualizable());
         duplicarOperando.setCodigo(operandoSeleccionado.getCodigo());
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarOperando");
         RequestContext.getCurrentInstance().execute("PF('DuplicarOperando').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   //RASTROS 
   public void verificarRastro() {
      if (operandoSeleccionado != null) {
         int result = administrarRastros.obtenerTabla(operandoSeleccionado.getSecuencia(), "OPERANDOS");
         log.info("resultado: " + result);
         if (result == 1) {
            RequestContext.getCurrentInstance().execute("PF('errorObjetosDB').show()");
         } else if (result == 2) {
            RequestContext.getCurrentInstance().execute("PF('confirmarRastro').show()");
         } else if (result == 3) {
            RequestContext.getCurrentInstance().execute("PF('errorRegistroRastro').show()");
         } else if (result == 4) {
            RequestContext.getCurrentInstance().execute("PF('errorTablaConRastro').show()");
         } else if (result == 5) {
            RequestContext.getCurrentInstance().execute("PF('errorTablaSinRastro').show()");
         }
//         } else {
//            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
//         }
      } else if (administrarRastros.verificarHistoricosTabla("OPERANDOS")) {
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
   }

   public void ubicacionRegistro() {
      FacesContext contexto = FacesContext.getCurrentInstance();
      Map<String, String> map = contexto.getExternalContext().getRequestParameterMap();
      String indice = map.get("INDICE");
      int pos = Integer.parseInt(indice);
      String campo = map.get("CAMPO");
      cualCelda = Integer.parseInt(campo);
      cambiarIndice(listaOperandos.get(pos), cualCelda);
   }

   public void salir() {
      limpiarListasValor();
      if (bandera == 1) {
         restaurarTabla();
      }
      listaOperandosBorrar.clear();
      listaOperandosCrear.clear();
      listaOperandosModificar.clear();
      operandoSeleccionado = null;
      k = 0;
      listaOperandos = null;
      guardado = true;
      navegar("atras");
   }

   public void contarRegistros() {
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
   }

   public void contarRegistrosLovOp() {
      RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroLovOp");
   }

   public boolean validarNuevoOperandoClon() {
      boolean retorno = true;
      int conteo = 0;
      for (int i = 0; i < lovOperandos.size(); i++) {
         if (lovOperandos.get(i).getNombre().equals(nombreNuevoClon)) {
            conteo++;
         }
      }
      if (conteo > 0) {
         retorno = false;
      }
      return retorno;
   }

   public void clonarOperando() {
      if (descripcionNuevoClon != null && nombreNuevoClon != null && operandoBaseClon.getCodigo() >= 1) {
         if (!descripcionNuevoClon.isEmpty() && !nombreNuevoClon.isEmpty()) {
            if (validarNuevoOperandoClon() == true) {
               short newcodigo = (short) operandoBaseClon.getCodigo();
               errorClonado = administrarOperandos.clonarOperando(newcodigo, nombreNuevoClon, descripcionNuevoClon);
               RequestContext context = RequestContext.getCurrentInstance();
               if (errorClonado.equals("BIEN")) {
                  FacesMessage msg = new FacesMessage("Información", "El registro fue clonado con Éxito.");
                  FacesContext.getCurrentInstance().addMessage(null, msg);
                  operandoBaseClon = new Operandos();
                  nombreNuevoClon = "";
                  descripcionNuevoClon = "";
                  cancelarModificacion();
                  context.update("form:CodigoBaseClonado");
                  context.update("form:NombreBaseClonado");
                  context.update("form:NombreNuevoClonado");
                  context.update("form:DescripcionNuevoClonado");
                  context.update("form:growl");
               } else {
                  context.update("form:errorClonado");
                  context.execute("PF('errorClonado').show()");
               }
            } else {
               RequestContext.getCurrentInstance().execute("PF('errorNombreClon').show()");
            }
         } else {
            RequestContext.getCurrentInstance().execute("PF('errorDatosClonado').show()");
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorDatosClonado').show()");
      }
   }

   //Getter & Setter
   public List<Operandos> getListaOperandos() {
      if (listaOperandos == null) {
         listaOperandos = administrarOperandos.buscarOperandos();
         if (listaOperandos != null) {
            if (!listaOperandos.isEmpty()) {
               for (int i = 0; i < listaOperandos.size(); i++) {
                  String valor = administrarOperandos.buscarValores(listaOperandos.get(i).getSecuencia());
                  listaOperandos.get(i).setValor(valor);
               }
            }
         }
      }
      return listaOperandos;
   }

   public void setListaOperandos(List<Operandos> listaOperandos) {
      this.listaOperandos = listaOperandos;
   }

   public List<Operandos> getFiltradoOperandos() {
      return filtradoOperandos;
   }

   public void setFiltradoOperandos(List<Operandos> filtradoOperandos) {
      this.filtradoOperandos = filtradoOperandos;
   }

   public List<Operandos> getLovOperandos() {
      if (lovOperandos == null) {
         lovOperandos = administrarOperandos.buscarOperandos();
      }
      return lovOperandos;
   }

   public void setLovOperandos(List<Operandos> lovOperandos) {
      this.lovOperandos = lovOperandos;
   }

   public List<Operandos> getFiltradoLovOperandos() {
      return filtradoLovOperandos;
   }

   public void setFiltradoLovOperandos(List<Operandos> filtradoLovOperandos) {
      this.filtradoLovOperandos = filtradoLovOperandos;
   }

   public Operandos getOperandoLovSeleccionado() {
      return operandoLovSeleccionado;
   }

   public void setOperandoLovSeleccionado(Operandos operandoLovSeleccionado) {
      this.operandoLovSeleccionado = operandoLovSeleccionado;
   }

   public boolean isAceptar() {
      return aceptar;
   }

   public Operandos getEditarOperandos() {
      return editarOperandos;
   }

   public void setEditarOperandos(Operandos editarOperandos) {
      this.editarOperandos = editarOperandos;
   }

   public String getAltoTabla() {
      return altoTabla;
   }

   public void setAltoTabla(String altoTabla) {
      this.altoTabla = altoTabla;
   }

   public String getAltoTablaReg() {
      return altoTablaReg;
   }

   public void setAltoTablaReg(String altoTablaReg) {
      this.altoTablaReg = altoTablaReg;
   }

   public Operandos getNuevoOperando() {
      return nuevoOperando;
   }

   public void setNuevoOperando(Operandos nuevoOperando) {
      this.nuevoOperando = nuevoOperando;
   }

   public Operandos getDuplicarOperando() {
      return duplicarOperando;
   }

   public void setDuplicarOperando(Operandos duplicarOperando) {
      this.duplicarOperando = duplicarOperando;
   }

   public String getMensajeValidacion() {
      return mensajeValidacion;
   }

   public void setMensajeValidacion(String mensajeValidacion) {
      this.mensajeValidacion = mensajeValidacion;
   }

   public String getAction() {
      return action;
   }

   public void setAction(String action) {
      this.action = action;
   }

   public String getTipoOperando() {
      return tipoOperando;
   }

   public void setTipoOperando(String tipoOperando) {
      this.tipoOperando = tipoOperando;
   }

   public Operandos getOperandoSeleccionado() {
      return operandoSeleccionado;
   }

   public void setOperandoSeleccionado(Operandos operandoRegistro) {
      this.operandoSeleccionado = operandoRegistro;
   }

   public Operandos getOperandoBaseClon() {
      return operandoBaseClon;
   }

   public void setOperandoBaseClon(Operandos operandoBaseClon) {
      this.operandoBaseClon = operandoBaseClon;
   }

   public String getInfoRegistro() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosOperandos");
      infoRegistro = String.valueOf(tabla.getRowCount());
      return infoRegistro;
   }

   public void setInfoRegistro(String infoRegistro) {
      this.infoRegistro = infoRegistro;
   }

   public String getNombreNuevoClon() {
      return nombreNuevoClon;
   }

   public void setNombreNuevoClon(String nombreNuevoClon) {
      this.nombreNuevoClon = nombreNuevoClon;
   }

   public String getDescripcionNuevoClon() {
      return descripcionNuevoClon;
   }

   public void setDescripcionNuevoClon(String descripcionNuevoClon) {
      this.descripcionNuevoClon = descripcionNuevoClon;
   }

   public boolean isGuardado() {
      return guardado;
   }

   public void setGuardado(boolean guardado) {
      this.guardado = guardado;
   }

   public String getInfoRegistroLovOp() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:LOVOperandos");
      infoRegistroLovOp = String.valueOf(tabla.getRowCount());
      return infoRegistroLovOp;
   }

   public void setInfoRegistroLovOp(String infoRegistroLovOp) {
      this.infoRegistroLovOp = infoRegistroLovOp;
   }

   public String getErrorClonado() {
      return errorClonado;
   }

   public void setErrorClonado(String errorClonado) {
      this.errorClonado = errorClonado;
   }

}
