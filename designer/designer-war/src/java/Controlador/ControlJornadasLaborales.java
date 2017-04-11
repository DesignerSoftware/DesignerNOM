/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Jornadas;
import Entidades.JornadasLaborales;
import Entidades.JornadasSemanales;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarJornadasLaboralesInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
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
import org.primefaces.component.column.Column;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.export.Exporter;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Administrador
 */
@ManagedBean
@SessionScoped
public class ControlJornadasLaborales implements Serializable {

   @EJB
   AdministrarJornadasLaboralesInterface administrarJornadasLaborales;
   @EJB
   AdministrarRastrosInterface administrarRastros;

   //SECUENCIA DE LA JORNADA
   private BigInteger secuenciaJornada;
   //LISTA DE JORNADA LABORAL
   private List<JornadasLaborales> listaJornadasLaborales;
   private List<JornadasLaborales> filtradoListaJornadasLaborales;
   private JornadasLaborales jornadaLaboralSeleccionada;

   //LISTA DE JORNADA SEMANAL
   private List<JornadasSemanales> listaJornadasSemanales;
   private List<JornadasSemanales> filtradoListaJornadasSemanales;
   private JornadasSemanales jornadaSemanalSeleccionada;

   //LISTA DE VALORES DE JORNADAS FALTA!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
   private List<Jornadas> lovJornadas;
   private List<Jornadas> lovFiltradoJornadas;
   private Jornadas jornadaSeleccionada;
   //Otros
   private boolean aceptar;
   private int tipoActualizacion;
   private boolean permitirIndex;
   private int tipoLista;
   private int cualCelda;
   private int tipoListaJS;
   private int CualTabla;

   //Tabla a Imprimir
   private String tablaImprimir, nombreArchivo;
   //Activo/Desactivo Crtl + F11
   private int bandera;
   private int banderaJS;
   //Modificar Jornadas Laborales
   private List<JornadasLaborales> listaJornadasLaboralesModificar;
   private boolean guardado;
   //Crear Jornadas Laborales
   public JornadasLaborales nuevaJornadaLaboral;
   private List<JornadasLaborales> listaJornadasLaboralesCrear;
   private BigInteger l;
   private int k;
   private String mensajeValidacion;
   private String mensajeValidacionhoras;
   private String mensajeValidacionhora;
   private String mensajeValidacionminuto;

   //Borrar Jornadas Laborales
   private List<JornadasLaborales> listaJornadasLaboralesBorrar;
   //Modificar Semana Laboral
   private List<JornadasSemanales> listaJornadasSemanalesModificar;
   //Crear Semana Laboral
   private JornadasSemanales nuevaSemanaLaboral;
   private List<JornadasSemanales> listaJornadasSemanalesCrear;
   private int m;
   private BigInteger n;
//    private String mensajeValidacionJS;
   //Borrar Semana Laboral
   private List<JornadasSemanales> listaJornadasSemanalesBorrar;
   //AUTOCOMPLETAR
   private String jornada;
   //editar celda
   private JornadasLaborales editarJornadaLaboral;
   private JornadasSemanales editarJornadaSemanal;
   private boolean cambioEditor, aceptarEditar;
   //DUPLICAR
   private JornadasLaborales duplicarJornadaLaboral;
   private JornadasSemanales duplicarSemanaLaboral;
   //RASTRO
   //Cual Insertar
   private String cualInsertar;
   //Cual Nuevo Update
   private String cualNuevo;
   public String altoTabla;
   public String altoTabla2;
   public String infoRegistroJornadas;
   public String infoRegistroJL;
   public String infoRegistroSL;
   //Columnas Tabla Jornadas Laborales
   private Column jornadasLaboralesCodigos, jornadasLaboralesDescripcion, jornadasLaboralesHorasDiarias, jornadasLaboralesHorasMensuales, jornadasLaboralesRotativo, jornadasLaboralesTurnoRelativo, jornadasLaboralesCuadrillaHorasDiarias, jornadasLaboralesJornadas;
   //Columnas Tabla Semana Laboral
   private Column SemanaLaboralDia, SemanaLaboralHI, SemanaLaboralMI, SemanaLaboralHIA, SemanaLaboralMIA, SemanaLaboralHFA, SemanaLaboralMFA, SemanaLaboralHoraFinal, SemanaLaboralMinutoFinal;
   ///////////////////////////////////////////////
   ///////////PRUEBAS UNITARIAS COMPONENTES///////
   ///////////////////////////////////////////////
   public String buscarNombre;
   public String diarecuperado, diita, descrecuperado;
   public Short codiguin, hirecuperado, mirecuperado, hiarecuperado, miarecuperado,
           hfarecuperado, mfarecuperado, hfrecuperado, mfrecuperado;
   public boolean buscador;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

   public ControlJornadasLaborales() {
      permitirIndex = true;
      aceptar = true;
      tipoLista = 0;
      duplicarSemanaLaboral = new JornadasSemanales();
      listaJornadasLaboralesBorrar = new ArrayList<JornadasLaborales>();
      listaJornadasLaboralesCrear = new ArrayList<JornadasLaborales>();
      listaJornadasLaboralesModificar = new ArrayList<JornadasLaborales>();
      listaJornadasSemanalesBorrar = new ArrayList<JornadasSemanales>();
      listaJornadasSemanalesCrear = new ArrayList<JornadasSemanales>();
      listaJornadasSemanalesModificar = new ArrayList<JornadasSemanales>();
      lovJornadas = null;
      //editar
      editarJornadaLaboral = new JornadasLaborales();
      editarJornadaSemanal = new JornadasSemanales();
      cambioEditor = false;
      aceptarEditar = true;
      cualCelda = -1;
      tipoLista = 0;
      tipoListaJS = 0;
      nuevaJornadaLaboral = new JornadasLaborales();
      nuevaJornadaLaboral.setJornada(new Jornadas());
      nuevaSemanaLaboral = new JornadasSemanales();
      //duplicar
      duplicarJornadaLaboral = new JornadasLaborales();
      duplicarJornadaLaboral.setJornada(new Jornadas());
      duplicarSemanaLaboral = new JornadasSemanales();

      jornadaLaboralSeleccionada = null;
      k = 0;
      altoTabla = "115";
      altoTabla2 = "115";
      guardado = true;
      tablaImprimir = ":formExportar:datosJornadasLaboralesExportar";
      nombreArchivo = "JornadasLaboralesXML";
      buscador = false;
      mapParametros.put("paginaAnterior", paginaAnterior);
   }

   public void recibirPaginaEntrante(String pagina) {
      paginaAnterior = pagina;
      if (!listaJornadasLaborales.isEmpty()) {
         jornadaLaboralSeleccionada = listaJornadasLaborales.get(0);
         getListaJornadasSemanales();
      }
   }

   public void recibirParametros(Map<String, Object> map) {
      mapParametros = map;
      paginaAnterior = (String) mapParametros.get("paginaAnterior");
      if (!listaJornadasLaborales.isEmpty()) {
         jornadaLaboralSeleccionada = listaJornadasLaborales.get(0);
         getListaJornadasSemanales();
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
         System.out.println("navegar('Atras') : " + pag);
      } else {
         String pagActual = "jornadaslaborales";
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
         controlListaNavegacion.guardarNavegacion(pagActual, pag);
      }
      limpiarListasValor();fc.getApplication().getNavigationHandler().handleNavigation(fc, null, pag);
   }

  public void limpiarListasValor() {

   }

   @PostConstruct
   public void inicializarAdministrador() {
      try {
         FacesContext x = FacesContext.getCurrentInstance();
         HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
         administrarJornadasLaborales.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
         getListaJornadasLaborales();
      } catch (Exception e) {
         System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
         System.out.println("Causa: " + e.getCause());
      }
   }

   public String redirigir() {
      return paginaAnterior;
   }

   //Ubicacion Celda. Tabla 1
   public void cambiarIndice(JornadasLaborales jornadaL, int celda) {
      jornadaLaboralSeleccionada = jornadaL;
      if (permitirIndex == true) {
         cualCelda = celda;
         CualTabla = 0;

         tablaImprimir = ":formExportar:datosJornadasLaboralesExportar";
         nombreArchivo = "JornadasLaboralesXML";
         RequestContext.getCurrentInstance().update("form:exportarXML");
         descrecuperado = jornadaLaboralSeleccionada.getDescripcion();
         codiguin = jornadaLaboralSeleccionada.getCodigo();
         if (cualCelda == 7) {
            jornada = jornadaLaboralSeleccionada.getJornada().getDescripcion();
         }
      }
   }

   //GUARDAR
   public void guardarTodo() {

      System.out.println("Guardado: " + guardado);
      System.out.println("Guardar en Primera Tabla: ");
      if (guardado == false) {
         System.out.println("Realizando Operaciones JornadasLaborales");
         if (!listaJornadasLaboralesBorrar.isEmpty()) {
            for (int i = 0; i < listaJornadasLaboralesBorrar.size(); i++) {
               if (listaJornadasLaboralesBorrar.get(i).getJornada() != null) {
                  if (listaJornadasLaboralesBorrar.get(i).getJornada().getCodigo() == null) {
                     listaJornadasLaboralesBorrar.get(i).setJornada(null);
                  }
               }
            }
            administrarJornadasLaborales.borrarJornadasLaborales(listaJornadasLaboralesBorrar);
            listaJornadasLaboralesBorrar.clear();
         }
         if (!listaJornadasSemanalesBorrar.isEmpty()) {
            administrarJornadasLaborales.borrarJornadasSemanales(listaJornadasSemanalesBorrar);
            System.out.println("Entra semana laboral");
            listaJornadasSemanalesBorrar.clear();
         }
         if (!listaJornadasLaboralesCrear.isEmpty()) {
            for (int i = 0; i < listaJornadasLaboralesCrear.size(); i++) {
               if (listaJornadasLaboralesCrear.get(i).getJornada() != null) {
                  if (listaJornadasLaboralesCrear.get(i).getJornada().getCodigo() == null) {
                     listaJornadasLaboralesCrear.get(i).setJornada(null);
                  }
               }
            }
            administrarJornadasLaborales.crearJornadasLaborales(listaJornadasLaboralesCrear);
            System.out.println("LimpiaLista jornada laboral");
            listaJornadasLaboralesCrear.clear();
         }
         if (!listaJornadasSemanalesCrear.isEmpty()) {
            System.out.println("listaJornadasSemanalesCrear : " + listaJornadasSemanalesCrear);
            administrarJornadasLaborales.crearJornadasSemanales(listaJornadasSemanalesCrear);
            System.out.println("LimpiaLista semana laboral");
            listaJornadasSemanalesCrear.clear();
         }
         if (!listaJornadasLaboralesModificar.isEmpty()) {
            for (int i = 0; i < listaJornadasLaboralesModificar.size(); i++) {
               if (listaJornadasLaboralesModificar.get(i).getJornada() != null) {
                  if (listaJornadasLaboralesModificar.get(i).getJornada().getCodigo() == null) {
                     listaJornadasLaboralesModificar.get(i).setJornada(null);
                  }
               }
            }
            administrarJornadasLaborales.modificarJornadasLaborales(listaJornadasLaboralesModificar);
            listaJornadasLaboralesModificar.clear();
         }
         if (!listaJornadasSemanalesModificar.isEmpty()) {
            administrarJornadasLaborales.modificarJornadasSemanales(listaJornadasSemanalesModificar);
            listaJornadasSemanalesModificar.clear();
         }
         System.out.println("Se guardaron los datos con exito");
         getListaJornadasLaborales();
         contarRegistrosJL();
         RequestContext.getCurrentInstance().update("form:datosJornadasLaborales");
         getListaJornadasSemanales();
         contarRegistrosSL();
         RequestContext.getCurrentInstance().update("form:datosSemanasLaborales");
         FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
         guardado = true;
         permitirIndex = true;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }
      jornadaSemanalSeleccionada = null;
      jornadaLaboralSeleccionada = null;
   }

//Ubicacion Celda Arriba 
   public void cambiarJornadaLaboral() {
      //Si ninguna de las 3 listas (crear,modificar,borrar) tiene algo, hace esto
      //{
      System.out.println("secuencia jornada seleccionada: " + jornadaLaboralSeleccionada.getSecuencia());
      if (listaJornadasSemanalesCrear.isEmpty() && listaJornadasSemanalesBorrar.isEmpty() && listaJornadasSemanalesModificar.isEmpty()) {
         secuenciaJornada = jornadaLaboralSeleccionada.getSecuencia();
         listaJornadasSemanales = null;
         getListaJornadasSemanales();
         if (listaJornadasSemanales != null) {
            if (!listaJornadasSemanales.isEmpty()) {
               jornadaSemanalSeleccionada = listaJornadasSemanales.get(0);
            }
         }
         contarRegistrosSL();
         RequestContext.getCurrentInstance().update("form:datosSemanasLaborales");
      } else {
         RequestContext.getCurrentInstance().update("formularioDialogos:confirmarGuardarSinSalida");
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardarSinSalida').show()");
      }
   }

   public void limpiarListas() {
      listaJornadasSemanalesCrear.clear();
      listaJornadasSemanalesBorrar.clear();
      listaJornadasSemanalesModificar.clear();
      secuenciaJornada = jornadaLaboralSeleccionada.getSecuencia();
      listaJornadasSemanales = null;
      RequestContext.getCurrentInstance().update("form:datosSemanasLaborales");
   }

   //Ubicacion Celda. Tabla 2
   public void cambiarIndiceJS(JornadasSemanales jornadaS, int celdaJS) {
      jornadaSemanalSeleccionada = jornadaS;
      if (permitirIndex == true) {
         cualCelda = celdaJS;
         CualTabla = 1;
         tablaImprimir = ":formExportar:datosJornadasSemanalesExportar";
         cualNuevo = ":formularioDialogos:nuevaSemanaLaboral";
         cualInsertar = "formularioDialogos:NuevoRegistroJornadaSemanal";
         nombreArchivo = "JornadasSemanalesXML";
         RequestContext.getCurrentInstance().update("form:exportarXML");
         System.out.println("entre cambiar indice");
         diarecuperado = jornadaSemanalSeleccionada.getDia();
         diita = jornadaSemanalSeleccionada.getEstadoDia();
         hirecuperado = jornadaSemanalSeleccionada.getHorainicial();
         mirecuperado = jornadaSemanalSeleccionada.getMinutoinicial();
         hiarecuperado = jornadaSemanalSeleccionada.getHorainicialalimentacion();
         miarecuperado = jornadaSemanalSeleccionada.getMinutoinicialalimentacion();
         hfarecuperado = jornadaSemanalSeleccionada.getHorafinalalimentacion();
         mfarecuperado = jornadaSemanalSeleccionada.getMinutofinalalimentacion();
         hfrecuperado = jornadaSemanalSeleccionada.getHorafinal();
         mfrecuperado = jornadaSemanalSeleccionada.getMinutofinal();
      }
   }

   //AUTOCOMPLETAR
   public void modificarJornadasLaborales(JornadasLaborales jornadaL, String confirmarCambio, String valorConfirmar) {
      jornadaLaboralSeleccionada = jornadaL;
      int pasa = 0;
      int pasas = 0;
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      if (confirmarCambio.equalsIgnoreCase("N")) {
         if (!listaJornadasLaboralesCrear.contains(jornadaLaboralSeleccionada)) {

            if (listaJornadasLaboralesModificar.isEmpty()) {
               listaJornadasLaboralesModificar.add(jornadaLaboralSeleccionada);
            } else if (!listaJornadasLaboralesModificar.contains(jornadaLaboralSeleccionada)) {
               listaJornadasLaboralesModificar.add(jornadaLaboralSeleccionada);
            }
            if (guardado == true) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");

            }
         }
         RequestContext.getCurrentInstance().update("form:datosJornadasLaborales");
      } else if (confirmarCambio.equalsIgnoreCase("D")) {
         if (!listaJornadasLaboralesCrear.contains(jornadaLaboralSeleccionada)) {

            if (jornadaLaboralSeleccionada.getDescripcion() == null || jornadaLaboralSeleccionada.getDescripcion().equals("")) {
               pasa++;
            }

            if (pasa != 0) {
               jornadaLaboralSeleccionada.setDescripcion(descrecuperado);
               RequestContext.getCurrentInstance().update("formularioDialogos:validacionDescripcion");
               RequestContext.getCurrentInstance().execute("PF('validacionDescripcion').show()");
            }

            if (pasa == 0) {

               if (listaJornadasLaboralesModificar.isEmpty()) {
                  listaJornadasLaboralesModificar.add(jornadaLaboralSeleccionada);
               } else if (!listaJornadasLaboralesModificar.contains(jornadaLaboralSeleccionada)) {
                  listaJornadasLaboralesModificar.add(jornadaLaboralSeleccionada);
               }
               if (guardado == true) {
                  guardado = false;
                  RequestContext.getCurrentInstance().update("form:ACEPTAR");
               }
            }
         }

         RequestContext.getCurrentInstance().update("form:datosJornadasLaborales");
      } else if (confirmarCambio.equalsIgnoreCase("C")) {
         if (!listaJornadasLaboralesCrear.contains(jornadaLaboralSeleccionada)) {

            if (jornadaLaboralSeleccionada.getCodigo() != null) {
               System.out.println("codiguin 1 " + codiguin);
               System.out.println("codiguin 1.2 " + jornadaLaboralSeleccionada.getCodigo());

               for (int i = 0; i < listaJornadasLaborales.size(); i++) {
                  if (jornadaLaboralSeleccionada.getCodigo().compareTo(listaJornadasLaborales.get(i).getCodigo()) == 0) {
                     pasas++;
                     System.out.println("codiguin 2" + codiguin);
                  }
               }
            }
            if (pasas == 1) {

               if (listaJornadasLaboralesModificar.isEmpty()) {
                  listaJornadasLaboralesModificar.add(jornadaLaboralSeleccionada);
               } else if (!listaJornadasLaboralesModificar.contains(jornadaLaboralSeleccionada)) {
                  listaJornadasLaboralesModificar.add(jornadaLaboralSeleccionada);
               }
               if (guardado == true) {
                  guardado = false;
                  RequestContext.getCurrentInstance().update("form:ACEPTAR");

               }
            } else {
               jornadaLaboralSeleccionada.setCodigo(codiguin);
               RequestContext.getCurrentInstance().update("formularioDialogos:validacionCodigo");
               RequestContext.getCurrentInstance().execute("PF('validacionCodigo').show()");
            }
         }
         RequestContext.getCurrentInstance().update("form:datosJornadasLaborales");
      } else if (confirmarCambio.equalsIgnoreCase("JORNADAS")) {
         jornadaLaboralSeleccionada.getJornada().setDescripcion(jornada);
         for (int i = 0; i < lovJornadas.size(); i++) {
            if (lovJornadas.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            jornadaLaboralSeleccionada.setJornada(lovJornadas.get(indiceUnicoElemento));
            lovJornadas.clear();
            getLovJornadas();
         } else {
            permitirIndex = false;
            RequestContext.getCurrentInstance().update("formularioDialogos:jornadasDialogo");
            RequestContext.getCurrentInstance().execute("PF('jornadasDialogo').show()");
            tipoActualizacion = 0;
         }
      }
      if (coincidencias == 1) {
         if (!listaJornadasLaboralesCrear.contains(jornadaLaboralSeleccionada)) {
            if (listaJornadasLaboralesModificar.isEmpty()) {
               listaJornadasLaboralesModificar.add(jornadaLaboralSeleccionada);
            } else if (!listaJornadasLaboralesModificar.contains(jornadaLaboralSeleccionada)) {
               listaJornadasLaboralesModificar.add(jornadaLaboralSeleccionada);
            }
            if (guardado == true) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
         }
      }
      RequestContext.getCurrentInstance().update("form:datosJornadasLaborales");
   }

   //AUTOCOMPLETAR SEGUNDA TABLA
   public void modificarJornadasSemanales(JornadasSemanales jornadaS, String confirmarCambio, String valorConfirmar) {
      jornadaSemanalSeleccionada = jornadaS;
      int coincidencias = 0;
      if (confirmarCambio.equalsIgnoreCase("N")) {

         int pasa = 0;
         int pasas = 0;
         mensajeValidacion = " ";
         mensajeValidacionhora = " ";
         mensajeValidacionminuto = " ";
         JornadasSemanales aux = null;
         JornadasSemanales aux2 = null;
         aux = jornadaSemanalSeleccionada;
         if (!listaJornadasSemanalesCrear.contains(jornadaSemanalSeleccionada)) {

            if (aux.getHorainicial() == null) {
               System.out.println("Entro a Hora Inicial");
               mensajeValidacion = mensajeValidacion + " * Hora Inicial\n ";
               pasa++;
            }
            if (aux.getHorainicial() != null && (aux.getHorainicial() > 23 || aux.getHorainicial() < 0)) {
               aux.setHorainicial(hirecuperado);
               pasas++;
               RequestContext.getCurrentInstance().update("formularioDialogos:validacionHorasSL");
               RequestContext.getCurrentInstance().execute("PF('validacionHorasSL').show()");
            }
            if (aux.getMinutoinicial() == null) {
               System.out.println("Entro a Minuto Inicial");
               pasa++;
            }
            if (aux.getMinutoinicial() != null && (aux.getMinutoinicial() > 59 || aux.getMinutoinicial() < 0)) {
               aux.setMinutoinicial(mirecuperado);
               pasas++;
               RequestContext.getCurrentInstance().update("formularioDialogos:validacionMinutosSL");
               RequestContext.getCurrentInstance().execute("PF('validacionMinutosSL').show()");
            }
            if (aux.getHorainicialalimentacion() == null) {
               System.out.println("Entro a Hora Inicial Alimentación");
               mensajeValidacion = mensajeValidacion + " * Hora Inicial Alimentacion\n ";
               pasa++;
            }
            if (aux.getHorainicialalimentacion() != null && (aux.getHorainicialalimentacion() > 23 || aux.getHorainicialalimentacion() < 0)) {
               aux.setHorainicialalimentacion(hiarecuperado);
               pasas++;
               RequestContext.getCurrentInstance().update("formularioDialogos:validacionHorasSL");
               RequestContext.getCurrentInstance().execute("PF('validacionHorasSL').show()");
            }
            if (aux.getMinutoinicialalimentacion() == null) {
               System.out.println("Entro a Minuto Inicial Alimentación");
               mensajeValidacion = mensajeValidacion + " * Minuto Inicial Alimentacion\n ";
               pasa++;
            }
            if (aux.getMinutoinicialalimentacion() != null && (aux.getMinutoinicialalimentacion() > 59 || aux.getMinutoinicialalimentacion() < 0)) {
               aux.setMinutoinicialalimentacion(miarecuperado);
               pasas++;
               RequestContext.getCurrentInstance().update("formularioDialogos:validacionMinutosSL");
               RequestContext.getCurrentInstance().execute("PF('validacionMinutosSL').show()");
            }
            if (aux.getHorafinalalimentacion() == null) {
               System.out.println("Entro a Hora Final Alimentación");
               mensajeValidacion = mensajeValidacion + " * Hora Final Alimentacion\n ";
               pasa++;
            }
            if (aux.getHorafinalalimentacion() != null && (aux.getHorafinalalimentacion() > 23 || aux.getHorafinalalimentacion() < 0)) {
               aux.setHorafinalalimentacion(hfarecuperado);
               pasas++;
               RequestContext.getCurrentInstance().update("formularioDialogos:validacionHorasSL");
               RequestContext.getCurrentInstance().execute("PF('validacionHorasSL').show()");
            }
            if (aux.getMinutofinalalimentacion() == null) {
               System.out.println("Entro a Minuto Final Alimentación");
               mensajeValidacion = mensajeValidacion + " * Minuto Final Alimentacion\n ";
               pasa++;
            }
            if (aux.getMinutofinalalimentacion() != null && (aux.getMinutofinalalimentacion() > 59 || aux.getMinutofinalalimentacion() < 0)) {
               aux.setMinutofinalalimentacion(mfarecuperado);
               pasas++;
               RequestContext.getCurrentInstance().update("formularioDialogos:validacionMinutosSL");
               RequestContext.getCurrentInstance().execute("PF('validacionMinutosSL').show()");
            }
            if (aux.getHorafinal() == null) {
               System.out.println("Entro a Hora Final");
               mensajeValidacion = mensajeValidacion + " * Hora Final\n ";
               pasa++;
            }
            if (aux.getHorafinal() != null && (aux.getHorafinal() > 23 || aux.getHorafinal() < 0)) {
               aux.setHorafinal(hfrecuperado);
               pasas++;
               RequestContext.getCurrentInstance().update("formularioDialogos:validacionHorasSL");
               RequestContext.getCurrentInstance().execute("PF('validacionHorasSL').show()");
            }
            if (aux.getMinutofinal() == null) {
               System.out.println("Entro a Minuto Final");
               mensajeValidacion = mensajeValidacion + " * Minuto Final\n ";
               pasa++;
            }
            if (aux.getMinutofinal() != null && (aux.getMinutofinal() > 59 || aux.getMinutofinal() < 0)) {
               aux.setMinutofinal(mfrecuperado);
               pasas++;
               RequestContext.getCurrentInstance().update("formularioDialogos:validacionMinutosSL");
               RequestContext.getCurrentInstance().execute("PF('validacionMinutosSL').show()");
            }

            if (pasa != 0) {
               aux.setHorainicial(hirecuperado);
               aux.setMinutoinicial(mirecuperado);
               aux.setHorainicialalimentacion(hiarecuperado);
               aux.setMinutoinicialalimentacion(miarecuperado);
               aux.setHorafinalalimentacion(hfarecuperado);
               aux.setMinutofinalalimentacion(mfarecuperado);
               aux.setHorafinal(hfrecuperado);
               aux.setMinutofinal(mfrecuperado);
               RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevaJornadaSemanal");
               RequestContext.getCurrentInstance().execute("PF('validacionNuevaJornadaSemanal').show()");
            }
            if (pasa == 0 && pasas == 0) {
               if (listaJornadasSemanalesModificar.isEmpty()) {
                  listaJornadasSemanalesModificar.add(jornadaSemanalSeleccionada);
               } else if (!listaJornadasSemanalesModificar.contains(jornadaSemanalSeleccionada)) {
                  listaJornadasSemanalesModificar.add(jornadaSemanalSeleccionada);
               }
               if (guardado == true) {
                  guardado = false;
                  RequestContext.getCurrentInstance().update("form:ACEPTAR");
               }
            }
         }
         RequestContext.getCurrentInstance().update("form:datosSemanasLaborales");
      }

   }

   //ASIGNAR INDEX PARA DIALOGOS COMUNES (LND = LISTA - NUEVO - DUPLICADO)
   public void asignarIndex(JornadasLaborales jornadaL, int dlg, int LND) {
      jornadaLaboralSeleccionada = jornadaL;
      tipoActualizacion = LND;
      if (dlg == 0) {
         RequestContext.getCurrentInstance().update("formularioDialogos:jornadasDialogo");
         RequestContext.getCurrentInstance().execute("PF('jornadasDialogo').show()");
      }
   }

   public void asignarIndex(int dlg, int LND) {
      tipoActualizacion = LND;
      if (dlg == 0) {
         RequestContext.getCurrentInstance().update("formularioDialogos:jornadasDialogo");
         RequestContext.getCurrentInstance().execute("PF('jornadasDialogo').show()");
      }
   }

   //MOSTRAR L.O.V JORNADAS   
   public void actualizarJornadas() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {
         jornadaLaboralSeleccionada.setJornada(jornadaSeleccionada);
         if (!listaJornadasLaboralesCrear.contains(jornadaLaboralSeleccionada)) {
            if (listaJornadasLaboralesModificar.isEmpty()) {
               listaJornadasLaboralesModificar.add(jornadaLaboralSeleccionada);
            } else if (!listaJornadasLaboralesModificar.contains(jornadaLaboralSeleccionada)) {
               listaJornadasLaboralesModificar.add(jornadaLaboralSeleccionada);
            }
         }
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");

         }
         permitirIndex = true;
         RequestContext.getCurrentInstance().update("form:datosJornadasLaborales");
      } else if (tipoActualizacion == 1) {
         nuevaJornadaLaboral.setJornada(jornadaSeleccionada);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaJornadaLaboral");
      } else if (tipoActualizacion == 2) {
         duplicarJornadaLaboral.setJornada(jornadaSeleccionada);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarJornadaLaboral");
      }
      lovFiltradoJornadas = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
      context.reset("formularioDialogos:LOVJornadas:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('LOVJornadas').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('jornadasDialogo').hide()");
      //RequestContext.getCurrentInstance().update("formularioDialogos:LOVJornadas");
   }

   public void cancelarCambioJornadas() {
      lovFiltradoJornadas = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formularioDialogos:LOVJornadas:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('LOVJornadas').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('jornadasDialogo').hide()");
   }

   public void seleccionarDia(String estadoDia, int indiceJS, int celda) {
      System.out.println("entre a modificar");
      int pasas = 0;
      int coincidencia = 0;

      //diarecuperado = jornadaSemanalSeleccionada.getDia();
      diita = jornadaSemanalSeleccionada.getEstadoDia();
      System.out.println("Diita: " + diita);
      //  System.out.println("Diarecuperado: " + diarecuperado);

      for (int i = 0; i < listaJornadasSemanales.size(); i++) {
         System.out.println("listaJornadasSemanales.get(i).getEstadoDia() Posicion : " + i + " " + listaJornadasSemanales.get(i).getEstadoDia());
         if (jornadaSemanalSeleccionada.getEstadoDia().equalsIgnoreCase(listaJornadasSemanales.get(i).getEstadoDia())) {
            coincidencia++;
            //aux.setDia(diarecuperado);                        
         }
         if (coincidencia > 1) {
            listaJornadasSemanales.get(i).setEstadoDia(diita);
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionDia");
            RequestContext.getCurrentInstance().execute("PF('validacionDia').show()");
            System.out.println("I find you 2!");
            System.out.println("dia 2: " + listaJornadasSemanales.get(i).getDia());
            System.out.println("estad dia 2: " + listaJornadasSemanales.get(i).getEstadoDia());
            RequestContext.getCurrentInstance().update("form:datosSemanasLaborales");
         }
      }

      if (jornadaSemanalSeleccionada.getDia() != null) {
         System.out.println("Dia en la Posicion: " + indiceJS + " " + jornadaSemanalSeleccionada.getDia());

      }
      if (pasas == 0) {
         if (estadoDia.equals("LUNES")) {
            jornadaSemanalSeleccionada.setDia("LUN");
         } else if (estadoDia.equals("MARTES")) {
            jornadaSemanalSeleccionada.setDia("MAR");
         } else if (estadoDia.equals("MIERCOLES")) {
            jornadaSemanalSeleccionada.setDia("MIE");
         } else if (estadoDia.equals("JUEVES")) {
            jornadaSemanalSeleccionada.setDia("JUE");
         } else if (estadoDia.equals("VIERNES")) {
            jornadaSemanalSeleccionada.setDia("VIE");
         } else if (estadoDia.equals("SABADO")) {
            jornadaSemanalSeleccionada.setDia("SAB");
         } else if (estadoDia.equals("DOMINGO")) {
            jornadaSemanalSeleccionada.setDia("DOM");
         }

         if (!listaJornadasSemanalesCrear.contains(jornadaSemanalSeleccionada)) {
            if (listaJornadasSemanalesModificar.isEmpty()) {
               listaJornadasSemanalesModificar.add(jornadaSemanalSeleccionada);
            } else if (!listaJornadasSemanalesModificar.contains(jornadaSemanalSeleccionada)) {
               listaJornadasSemanalesModificar.add(jornadaSemanalSeleccionada);
            }
         }
      }
      if (guardado == true) {
         guardado = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }
      RequestContext.getCurrentInstance().update("form:datosSemanasLaborales");

   }

   public void seleccionarDiaNuevaSemana(String estadoDia, int tipoNuevo) {
      if (tipoNuevo == 1) {
         if (estadoDia.equals("LUNES")) {
            nuevaSemanaLaboral.setDia("LUN");
         } else if (estadoDia.equals("MARTES")) {
            nuevaSemanaLaboral.setDia("MAR");
         } else if (estadoDia.equals("MIERCOLES")) {
            nuevaSemanaLaboral.setDia("MIE");
         } else if (estadoDia.equals("JUEVES")) {
            nuevaSemanaLaboral.setDia("JUE");
         } else if (estadoDia.equals("VIERNES")) {
            nuevaSemanaLaboral.setDia("VIE");
         } else if (estadoDia.equals("SABADO")) {
            nuevaSemanaLaboral.setDia("SAB");
         } else if (estadoDia.equals("DOMINGO")) {
            nuevaSemanaLaboral.setDia("DOM");
         } else if (estadoDia.equals("NADA")) {
            nuevaSemanaLaboral.setDia(" ");
         }
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoDiaSemanaLaboral");
      } else {
         if (estadoDia.equals("LUNES")) {
            duplicarSemanaLaboral.setDia("LUN");
         } else if (estadoDia.equals("MARTES")) {
            duplicarSemanaLaboral.setDia("MAR");
         } else if (estadoDia.equals("MIERCOLES")) {
            duplicarSemanaLaboral.setDia("MIE");
         } else if (estadoDia.equals("JUEVES")) {
            duplicarSemanaLaboral.setDia("JUE");
         } else if (estadoDia.equals("VIERNES")) {
            duplicarSemanaLaboral.setDia("VIE");
         } else if (estadoDia.equals("SABADO")) {
            duplicarSemanaLaboral.setDia("SAB");
         } else if (estadoDia.equals("DOMINGO")) {
            duplicarSemanaLaboral.setDia("DOM");
         } else if (estadoDia.equals("NADA")) {
            duplicarSemanaLaboral.setDia(" ");
         }
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarDiaSemanaLaboral");
      }

   }

   //EVENTO FILTRAR
   public void eventoFiltrar() {
      if (tipoLista == 0) {
         tipoLista = 1;
      }
      contarRegistrosJL();
   }

   //EVENTO FILTRARJS
   public void eventoFiltrarJS() {
      if (tipoListaJS == 0) {
         tipoListaJS = 1;
      }
      contarRegistrosSL();
   }

   //MOSTRAR CELDA
   public void editarCelda() {
      if (jornadaLaboralSeleccionada != null && CualTabla == 0) {
         editarJornadaLaboral = jornadaLaboralSeleccionada;

         System.out.println("Entro a editar... valor celda: " + cualCelda);
         if (cualCelda == 0) {
            System.out.println("Codigo: " + editarJornadaLaboral.getCodigo());
            RequestContext.getCurrentInstance().update("formEditar:editarCodigo");
            RequestContext.getCurrentInstance().execute("PF('editarCodigo').show()");
            cualCelda = -1;
         } else if (cualCelda == 1) {
            RequestContext.getCurrentInstance().update("formEditar:editarDescripcion");
            RequestContext.getCurrentInstance().execute("PF('editarDescripcion').show()");
            cualCelda = -1;
         } else if (cualCelda == 2) {
            RequestContext.getCurrentInstance().update("formEditar:editarHorasDiarias");
            RequestContext.getCurrentInstance().execute("PF('editarHorasDiarias').show()");
            cualCelda = -1;
         } else if (cualCelda == 3) {
            RequestContext.getCurrentInstance().update("formEditar:editarHorasMensuales");
            RequestContext.getCurrentInstance().execute("PF('editarHorasMensuales').show()");
            cualCelda = -1;
         } else if (cualCelda == 7) {
            RequestContext.getCurrentInstance().update("formEditar:editarJornada");
            RequestContext.getCurrentInstance().execute("PF('editarJornada').show()");
            cualCelda = -1;
         }
      } else if (jornadaSemanalSeleccionada != null && CualTabla == 1) {
         editarJornadaSemanal = jornadaSemanalSeleccionada;

         RequestContext context = RequestContext.getCurrentInstance();
         System.out.println("Entro a editar... valor celda: " + cualCelda);
         System.out.println("Cual Tabla: " + CualTabla);
         if (cualCelda == 1) {
            RequestContext.getCurrentInstance().update("formEditar:editarHoraInicialJS");
            RequestContext.getCurrentInstance().execute("PF('editarHoraInicial').show()");
            cualCelda = -1;
         } else if (cualCelda == 2) {
            RequestContext.getCurrentInstance().update("formEditar:editarMinutoInicialJS");
            RequestContext.getCurrentInstance().execute("PF('editarMinutoInicialJS').show()");
            cualCelda = -1;
         } else if (cualCelda == 3) {
            RequestContext.getCurrentInstance().update("formEditar:editarHoraInicialAlimentacionJS");
            RequestContext.getCurrentInstance().execute("PF('editarHoraInicialAlimentacionJS').show()");
            cualCelda = -1;
         } else if (cualCelda == 4) {
            RequestContext.getCurrentInstance().update("formEditar:editarMinutoInicialAlimentacionJS");
            RequestContext.getCurrentInstance().execute("PF('editarMinutoInicialAlimentacionJS').show()");
            cualCelda = -1;
         } else if (cualCelda == 5) {
            RequestContext.getCurrentInstance().update("formEditar:editarHoraFinalAlimentacionJS");
            RequestContext.getCurrentInstance().execute("PF('editarHoraFinalAlimentacionJS').show()");
            cualCelda = -1;
         } else if (cualCelda == 6) {
            RequestContext.getCurrentInstance().update("formEditar:editarMinutoFinalAlimentacionJS");
            RequestContext.getCurrentInstance().execute("PF('editarMinutoFinalAlimentacionJS').show()");
            cualCelda = -1;
         } else if (cualCelda == 7) {
            RequestContext.getCurrentInstance().update("formEditar:editarHoraFinalJS");
            RequestContext.getCurrentInstance().execute("PF('editarHoraFinalJS').show()");
            cualCelda = -1;
         } else if (cualCelda == 8) {
            RequestContext.getCurrentInstance().update("formEditar:editarMinutoFinalJS");
            RequestContext.getCurrentInstance().execute("PF('editarMinutoFinalJS').show()");
            cualCelda = -1;
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   //LISTA DE VALORES DINAMICA
   public void listaValoresBoton() {
      if (jornadaLaboralSeleccionada != null) {
         if (cualCelda == 7) {
            RequestContext.getCurrentInstance().update("formularioDialogos:jornadasDialogo");
            RequestContext.getCurrentInstance().execute("PF('jornadasDialogo').show()");
            tipoActualizacion = 0;
         }
      }
   }

   public void validarNuevaSL() {
      if (jornadaLaboralSeleccionada != null) {
         RequestContext.getCurrentInstance().execute("PF('NuevoRegistroJornadaSemanal').show()");
         RequestContext.getCurrentInstance().execute("PF('NuevoRegistroPagina').hide()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistroSel').show()");
      }
   }

   //FILTRADO
   public void activarCtrlF11() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 0) {
         System.out.println("Activar");
         System.out.println("TipoLista= " + tipoLista);
         jornadasLaboralesCodigos = (Column) c.getViewRoot().findComponent("form:datosJornadasLaborales:jornadasLaboralesCodigos");
         jornadasLaboralesCodigos.setFilterStyle("width: 85% !important;");
         jornadasLaboralesDescripcion = (Column) c.getViewRoot().findComponent("form:datosJornadasLaborales:jornadasLaboralesDescripcion");
         jornadasLaboralesDescripcion.setFilterStyle("width: 85% !important;");
         jornadasLaboralesHorasDiarias = (Column) c.getViewRoot().findComponent("form:datosJornadasLaborales:jornadasLaboralesHorasDiarias");
         jornadasLaboralesHorasDiarias.setFilterStyle("width: 85% !important;");
         jornadasLaboralesHorasMensuales = (Column) c.getViewRoot().findComponent("form:datosJornadasLaborales:jornadasLaboralesHorasMensuales");
         jornadasLaboralesHorasMensuales.setFilterStyle("width: 85% !important;");
         jornadasLaboralesRotativo = (Column) c.getViewRoot().findComponent("form:datosJornadasLaborales:jornadasLaboralesRotativo");
         jornadasLaboralesRotativo.setFilterStyle("width: 85% !important;");
         jornadasLaboralesTurnoRelativo = (Column) c.getViewRoot().findComponent("form:datosJornadasLaborales:jornadasLaboralesTurnoRelativo");
         jornadasLaboralesTurnoRelativo.setFilterStyle("width: 85% !important;");
         jornadasLaboralesCuadrillaHorasDiarias = (Column) c.getViewRoot().findComponent("form:datosJornadasLaborales:jornadasLaboralesCuadrillaHorasDiarias");
         jornadasLaboralesCuadrillaHorasDiarias.setFilterStyle("width: 85% !important;");
         jornadasLaboralesJornadas = (Column) c.getViewRoot().findComponent("form:datosJornadasLaborales:jornadasLaboralesJornadas");
         jornadasLaboralesJornadas.setFilterStyle("width: 85% !important;");
         altoTabla = "95";
         RequestContext.getCurrentInstance().update("form:datosJornadasLaborales");
         bandera = 1;

         if (banderaJS == 0) {
            System.out.println("Activar");
            System.out.println("TipoLista= " + tipoLista);
            SemanaLaboralDia = (Column) c.getViewRoot().findComponent("form:datosSemanasLaborales:SemanaLaboralDia");
            SemanaLaboralDia.setFilterStyle("width: 85% !important;");
            SemanaLaboralHI = (Column) c.getViewRoot().findComponent("form:datosSemanasLaborales:SemanaLaboralHI");
            SemanaLaboralHI.setFilterStyle("width: 85% !important;");
            SemanaLaboralMI = (Column) c.getViewRoot().findComponent("form:datosSemanasLaborales:SemanaLaboralMI");
            SemanaLaboralMI.setFilterStyle("width: 85% !important;");
            SemanaLaboralHIA = (Column) c.getViewRoot().findComponent("form:datosSemanasLaborales:SemanaLaboralHIA");
            SemanaLaboralHIA.setFilterStyle("width: 85% !important;");
            SemanaLaboralMIA = (Column) c.getViewRoot().findComponent("form:datosSemanasLaborales:SemanaLaboralMIA");
            SemanaLaboralMIA.setFilterStyle("width: 85% !important;");
            SemanaLaboralHFA = (Column) c.getViewRoot().findComponent("form:datosSemanasLaborales:SemanaLaboralHFA");
            SemanaLaboralHFA.setFilterStyle("width: 85% !important;");
            SemanaLaboralMFA = (Column) c.getViewRoot().findComponent("form:datosSemanasLaborales:SemanaLaboralMFA");
            SemanaLaboralMFA.setFilterStyle("width: 85% !important;");
            SemanaLaboralHoraFinal = (Column) c.getViewRoot().findComponent("form:datosSemanasLaborales:SemanaLaboralHoraFinal");
            SemanaLaboralHoraFinal.setFilterStyle("width: 85% !important;");
            SemanaLaboralMinutoFinal = (Column) c.getViewRoot().findComponent("form:datosSemanasLaborales:SemanaLaboralMinutoFinal");
            SemanaLaboralMinutoFinal.setFilterStyle("width: 85% !important;");
            altoTabla2 = "95";
            RequestContext.getCurrentInstance().update("form:datosSemanasLaborales");
            banderaJS = 1;
         }

      } else if (bandera == 1) {
         System.out.println("Desactivar");
         System.out.println("TipoLista= " + tipoLista);

         jornadasLaboralesCodigos = (Column) c.getViewRoot().findComponent("form:datosJornadasLaborales:jornadasLaboralesCodigos");
         jornadasLaboralesCodigos.setFilterStyle("display: none; visibility: hidden;");
         jornadasLaboralesDescripcion = (Column) c.getViewRoot().findComponent("form:datosJornadasLaborales:jornadasLaboralesDescripcion");
         jornadasLaboralesDescripcion.setFilterStyle("display: none; visibility: hidden;");
         jornadasLaboralesHorasDiarias = (Column) c.getViewRoot().findComponent("form:datosJornadasLaborales:jornadasLaboralesHorasDiarias");
         jornadasLaboralesHorasDiarias.setFilterStyle("display: none; visibility: hidden;");
         jornadasLaboralesHorasMensuales = (Column) c.getViewRoot().findComponent("form:datosJornadasLaborales:jornadasLaboralesHorasMensuales");
         jornadasLaboralesHorasMensuales.setFilterStyle("display: none; visibility: hidden;");
         jornadasLaboralesRotativo = (Column) c.getViewRoot().findComponent("form:datosJornadasLaborales:jornadasLaboralesRotativo");
         jornadasLaboralesRotativo.setFilterStyle("display: none; visibility: hidden;");
         jornadasLaboralesTurnoRelativo = (Column) c.getViewRoot().findComponent("form:datosJornadasLaborales:jornadasLaboralesTurnoRelativo");
         jornadasLaboralesTurnoRelativo.setFilterStyle("display: none; visibility: hidden;");
         jornadasLaboralesCuadrillaHorasDiarias = (Column) c.getViewRoot().findComponent("form:datosJornadasLaborales:jornadasLaboralesCuadrillaHorasDiarias");
         jornadasLaboralesCuadrillaHorasDiarias.setFilterStyle("display: none; visibility: hidden;");
         jornadasLaboralesJornadas = (Column) c.getViewRoot().findComponent("form:datosJornadasLaborales:jornadasLaboralesJornadas");
         jornadasLaboralesJornadas.setFilterStyle("display: none; visibility: hidden;");
         altoTabla = "115";
         RequestContext.getCurrentInstance().update("form:datosJornadasLaborales");
         bandera = 0;
         filtradoListaJornadasLaborales = null;
         tipoLista = 0;
         if (banderaJS == 1) {
            System.out.println("Desactivar");
            System.out.println("TipoLista= " + tipoLista);
            SemanaLaboralDia = (Column) c.getViewRoot().findComponent("form:datosSemanasLaborales:SemanaLaboralDia");
            SemanaLaboralDia.setFilterStyle("display: none; visibility: hidden;");
            SemanaLaboralHI = (Column) c.getViewRoot().findComponent("form:datosSemanasLaborales:SemanaLaboralHI");
            SemanaLaboralHI.setFilterStyle("display: none; visibility: hidden;");
            SemanaLaboralMI = (Column) c.getViewRoot().findComponent("form:datosSemanasLaborales:SemanaLaboralMI");
            SemanaLaboralMI.setFilterStyle("display: none; visibility: hidden;");
            SemanaLaboralHIA = (Column) c.getViewRoot().findComponent("form:datosSemanasLaborales:SemanaLaboralHIA");
            SemanaLaboralHIA.setFilterStyle("display: none; visibility: hidden;");
            SemanaLaboralMIA = (Column) c.getViewRoot().findComponent("form:datosSemanasLaborales:SemanaLaboralMIA");
            SemanaLaboralMIA.setFilterStyle("display: none; visibility: hidden;");
            SemanaLaboralHFA = (Column) c.getViewRoot().findComponent("form:datosSemanasLaborales:SemanaLaboralHFA");
            SemanaLaboralHFA.setFilterStyle("display: none; visibility: hidden;");
            SemanaLaboralMFA = (Column) c.getViewRoot().findComponent("form:datosSemanasLaborales:SemanaLaboralMFA");
            SemanaLaboralMFA.setFilterStyle("display: none; visibility: hidden;");
            SemanaLaboralHoraFinal = (Column) c.getViewRoot().findComponent("form:datosSemanasLaborales:SemanaLaboralHoraFinal");
            SemanaLaboralHoraFinal.setFilterStyle("display: none; visibility: hidden;");
            SemanaLaboralMinutoFinal = (Column) c.getViewRoot().findComponent("form:datosSemanasLaborales:SemanaLaboralMinutoFinal");
            SemanaLaboralMinutoFinal.setFilterStyle("display: none; visibility: hidden;");
            altoTabla2 = "115";
            RequestContext.getCurrentInstance().update("form:datosSemanasLaborales");
            banderaJS = 0;
            filtradoListaJornadasSemanales = null;
            tipoListaJS = 0;
         }
      }
   }

   public void activarAceptar() {
      aceptar = false;
   }

   //EXPORTAR PDF
   public void exportPDF() throws IOException {
      if (CualTabla == 0) {
         DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosJornadasLaboralesExportar");
         FacesContext context = FacesContext.getCurrentInstance();
         Exporter exporter = new ExportarPDF();
         exporter.export(context, tabla, "JornadasLaboralesPDF", false, false, "UTF-8", null, null);
         context.responseComplete();
      } else {
         DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosJornadasSemanalesExportar");
         FacesContext context = FacesContext.getCurrentInstance();
         Exporter exporter = new ExportarPDF();
         exporter.export(context, tabla, "SemanaLaboralPDF", false, false, "UTF-8", null, null);
         context.responseComplete();
      }
   }

   //EXPORTAR XLS
   public void exportXLS() throws IOException {
      if (CualTabla == 0) {
         DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosJornadasLaboralesExportar");
         FacesContext context = FacesContext.getCurrentInstance();
         Exporter exporter = new ExportarXLS();
         exporter.export(context, tabla, "JornadasLaboralesXLS", false, false, "UTF-8", null, null);
         context.responseComplete();
      } else {
         DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosJornadasSemanalesExportar");
         FacesContext context = FacesContext.getCurrentInstance();
         Exporter exporter = new ExportarXLS();
         exporter.export(context, tabla, "SemanaLaboralXLS", false, false, "UTF-8", null, null);
         context.responseComplete();
      }
   }

   // Agregar Nueva Jornada Laboral
   public void agregarNuevaJornadaLaboral() {

      int pasa = 0;
      int pasas = 0;
      mensajeValidacion = " ";
      mensajeValidacionhoras = " ";

      if (nuevaJornadaLaboral.getHorasdiarias() != null && (nuevaJornadaLaboral.getHorasdiarias().compareTo(BigDecimal.ONE) == 0 || nuevaJornadaLaboral.getHorasdiarias().compareTo(BigDecimal.ONE) == -1)) {
         mensajeValidacionhoras = mensajeValidacionhoras + " Horas Diarias. ";
         pasas++;
         RequestContext.getCurrentInstance().update("formularioDialogos:validacionHorasJL");
         RequestContext.getCurrentInstance().execute("PF('validacionHorasJL').show()");
      }
      if (nuevaJornadaLaboral.getHorasmensuales() != null && (nuevaJornadaLaboral.getHorasmensuales().compareTo(Short.valueOf("0")) == 0 || nuevaJornadaLaboral.getHorasmensuales().compareTo(Short.valueOf("0")) < 0)) {
         mensajeValidacionhoras = mensajeValidacionhoras + " Horas Mensuales. ";
         pasas++;
         RequestContext.getCurrentInstance().update("formularioDialogos:validacionHorasJL");
         RequestContext.getCurrentInstance().execute("PF('validacionHorasJL').show()");
      }
      if (nuevaJornadaLaboral.getCodigo() != null) {

         for (int i = 0; i < listaJornadasLaborales.size(); i++) {
            if (nuevaJornadaLaboral.getCodigo() == listaJornadasLaborales.get(i).getCodigo()) {
               pasas++;
               RequestContext.getCurrentInstance().update("formularioDialogos:validacionCodigo");
               RequestContext.getCurrentInstance().execute("PF('validacionCodigo').show()");
            }
         }
      }
      if (nuevaJornadaLaboral.getDescripcion() == null || nuevaJornadaLaboral.getDescripcion().equals("")) {
         System.out.println("Entro a Descripción");
         mensajeValidacion = mensajeValidacion + " * Descripcion\n";
         pasa++;
      }

      if (pasa != 0) {
         RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevaJornadaLaboral");
         RequestContext.getCurrentInstance().execute("PF('validacionNuevaJornadaLaboral').show()");
      }

      if (pasa == 0 && pasas == 0) {
         if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();

            System.out.println("Desactivar");
            System.out.println("TipoLista= " + tipoLista);
            jornadasLaboralesCodigos = (Column) c.getViewRoot().findComponent("form:datosJornadasLaborales:jornadasLaboralesCodigos");
            jornadasLaboralesCodigos.setFilterStyle("width: display: none; visibility: hidden;");
            jornadasLaboralesDescripcion = (Column) c.getViewRoot().findComponent("form:datosJornadasLaborales:jornadasLaboralesDescripcion");
            jornadasLaboralesDescripcion.setFilterStyle("display: none; visibility: hidden;");
            jornadasLaboralesHorasDiarias = (Column) c.getViewRoot().findComponent("form:datosJornadasLaborales:jornadasLaboralesHorasDiarias");
            jornadasLaboralesHorasDiarias.setFilterStyle("display: none; visibility: hidden;");
            jornadasLaboralesHorasMensuales = (Column) c.getViewRoot().findComponent("form:datosJornadasLaborales:jornadasLaboralesHorasMensuales");
            jornadasLaboralesHorasMensuales.setFilterStyle("display: none; visibility: hidden;");
            jornadasLaboralesRotativo = (Column) c.getViewRoot().findComponent("form:datosJornadasLaborales:jornadasLaboralesRotativo");
            jornadasLaboralesRotativo.setFilterStyle("display: none; visibility: hidden;");
            jornadasLaboralesTurnoRelativo = (Column) c.getViewRoot().findComponent("form:datosJornadasLaborales:jornadasLaboralesTurnoRelativo");
            jornadasLaboralesTurnoRelativo.setFilterStyle("display: none; visibility: hidden;");
            jornadasLaboralesCuadrillaHorasDiarias = (Column) c.getViewRoot().findComponent("form:datosJornadasLaborales:jornadasLaboralesCuadrillaHorasDiarias");
            jornadasLaboralesCuadrillaHorasDiarias.setFilterStyle("display: none; visibility: hidden;");
            jornadasLaboralesJornadas = (Column) c.getViewRoot().findComponent("form:datosJornadasLaborales:jornadasLaboralesJornadas");
            jornadasLaboralesJornadas.setFilterStyle("display: none; visibility: hidden;");
            altoTabla = "115";
            RequestContext.getCurrentInstance().update("form:datosJornadasLaborales");
            bandera = 0;
            filtradoListaJornadasLaborales = null;
            tipoLista = 0;

         }
         //AGREGAR REGISTRO A LA LISTA JORNADAS LABORALES.
         k++;
         l = BigInteger.valueOf(k);
         nuevaJornadaLaboral.setSecuencia(l);
         if (nuevaJornadaLaboral.isEstadoRotativo() == true) {
            nuevaJornadaLaboral.setRotativo("S");

         } else if (nuevaJornadaLaboral.isEstadoRotativo() == false) {
            nuevaJornadaLaboral.setRotativo("N");
         }

         if (nuevaJornadaLaboral.isEstadoTurnoRelativo() == true) {
            nuevaJornadaLaboral.setTurnorelativo("S");
         } else if (nuevaJornadaLaboral.isEstadoTurnoRelativo() == false) {
            nuevaJornadaLaboral.setTurnorelativo("N");
         }

         listaJornadasLaboralesCrear.add(nuevaJornadaLaboral);
         listaJornadasLaborales.add(nuevaJornadaLaboral);
         jornadaLaboralSeleccionada = listaJornadasLaborales.get(listaJornadasLaborales.indexOf(nuevaJornadaLaboral));

         contarRegistrosJL();
         nuevaJornadaLaboral = new JornadasLaborales();
         RequestContext.getCurrentInstance().update("form:datosJornadasLaborales");
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroJornadaLaboral");
         RequestContext.getCurrentInstance().execute("PF('NuevoRegistroJornadaLaboral').hide()");
      }
   }

   // Agregar Nueva Semana Laboral
   public void agregarNuevaSemanaLaboral() {

      int pasa = 0;
      int pasas = 0;
      mensajeValidacion = " ";
      mensajeValidacionhora = " ";
      mensajeValidacionminuto = " ";

      if (nuevaSemanaLaboral.getDia() != null) {
         System.out.println("nuevaSemanaLaboral.getDia() : " + nuevaSemanaLaboral.getDia());
         for (int i = 0; i < listaJornadasSemanales.size(); i++) {
            System.out.println("listaJornadasSemanales.get(i).getDia() : " + listaJornadasSemanales.get(i).getDia());
            if (nuevaSemanaLaboral.getDia().equalsIgnoreCase(listaJornadasSemanales.get(i).getDia())) {
               pasas++;
               RequestContext.getCurrentInstance().update("formularioDialogos:validacionDia");
               RequestContext.getCurrentInstance().execute("PF('validacionDia').show()");
               System.out.println("I find you !");
            }
         }
      }
      if (nuevaSemanaLaboral.getHorainicial() == null) {
         System.out.println("Entro a Hora Inicial");
         mensajeValidacion = mensajeValidacion + " * Hora Inicial\n ";
         pasa++;
      }
      if (nuevaSemanaLaboral.getHorainicial() != null && (nuevaSemanaLaboral.getHorainicial() > 23 || nuevaSemanaLaboral.getHorainicial() < 0)) {
         mensajeValidacionhora = mensajeValidacionhora + " Hora Inicial Jornada\n ";
         pasas++;
         RequestContext.getCurrentInstance().update("formularioDialogos:validacionHorasSL");
         RequestContext.getCurrentInstance().execute("PF('validacionHorasSL').show()");
      }
      if (nuevaSemanaLaboral.getMinutoinicial() == null) {
         System.out.println("Entro a Minuto Inicial");
         mensajeValidacion = mensajeValidacion + " * Minuto Inicial\n ";
         pasa++;
      }
      if (nuevaSemanaLaboral.getMinutoinicial() != null && (nuevaSemanaLaboral.getMinutoinicial() > 59 || nuevaSemanaLaboral.getMinutoinicial() < 0)) {
         mensajeValidacionminuto = mensajeValidacionminuto + " Minuto Inicial Jornada\n ";
         pasas++;
         RequestContext.getCurrentInstance().update("formularioDialogos:validacionMinutosSL");
         RequestContext.getCurrentInstance().execute("PF('validacionMinutosSL').show()");
      }
      if (nuevaSemanaLaboral.getHorainicialalimentacion() == null) {
         System.out.println("Entro a Hora Inicial Alimentación");
         mensajeValidacion = mensajeValidacion + " * Hora Inicial Alimentacion\n ";
         pasa++;
      }
      if (nuevaSemanaLaboral.getHorainicialalimentacion() != null && (nuevaSemanaLaboral.getHorainicialalimentacion() > 23 || nuevaSemanaLaboral.getHorainicialalimentacion() < 0)) {
         mensajeValidacionhora = mensajeValidacionhora + " Hora Inicial Alimentacion\n ";
         pasas++;
         RequestContext.getCurrentInstance().update("formularioDialogos:validacionHorasSL");
         RequestContext.getCurrentInstance().execute("PF('validacionHorasSL').show()");
      }
      if (nuevaSemanaLaboral.getMinutoinicialalimentacion() == null) {
         System.out.println("Entro a Minuto Inicial Alimentación");
         mensajeValidacion = mensajeValidacion + " * Minuto Inicial Alimentacion\n ";
         pasa++;
      }
      if (nuevaSemanaLaboral.getMinutoinicialalimentacion() != null && (nuevaSemanaLaboral.getMinutoinicialalimentacion() > 59 || nuevaSemanaLaboral.getMinutoinicialalimentacion() < 0)) {
         mensajeValidacionminuto = mensajeValidacionminuto + " Minuto Inicial Alimentacion\n ";
         pasas++;
         RequestContext.getCurrentInstance().update("formularioDialogos:validacionMinutosSL");
         RequestContext.getCurrentInstance().execute("PF('validacionMinutosSL').show()");
      }
      if (nuevaSemanaLaboral.getHorafinalalimentacion() == null) {
         System.out.println("Entro a Hora Final Alimentación");
         mensajeValidacion = mensajeValidacion + " * Hora Final Alimentacion\n ";
         pasa++;
      }
      if (nuevaSemanaLaboral.getHorafinalalimentacion() != null && (nuevaSemanaLaboral.getHorafinalalimentacion() > 23 || nuevaSemanaLaboral.getHorafinalalimentacion() < 0)) {
         mensajeValidacionhora = mensajeValidacionhora + " Hora Final Alimentacion\n ";
         pasas++;
         RequestContext.getCurrentInstance().update("formularioDialogos:validacionHorasSL");
         RequestContext.getCurrentInstance().execute("PF('validacionHorasSL').show()");
      }
      if (nuevaSemanaLaboral.getMinutofinalalimentacion() == null) {
         System.out.println("Entro a Minuto Final Alimentación");
         mensajeValidacion = mensajeValidacion + " * Minuto Final Alimentacion\n ";
         pasa++;
      }
      if (nuevaSemanaLaboral.getMinutofinalalimentacion() != null && (nuevaSemanaLaboral.getMinutofinalalimentacion() > 59 || nuevaSemanaLaboral.getMinutofinalalimentacion() < 0)) {
         mensajeValidacionminuto = mensajeValidacionminuto + " Minuto Final Alimentacion\n ";
         pasas++;
         RequestContext.getCurrentInstance().update("formularioDialogos:validacionMinutosSL");
         RequestContext.getCurrentInstance().execute("PF('validacionMinutosSL').show()");
      }
      if (nuevaSemanaLaboral.getHorafinal() == null) {
         System.out.println("Entro a Hora Final");
         mensajeValidacion = mensajeValidacion + " * Hora Final\n ";
         pasa++;
      }
      if (nuevaSemanaLaboral.getHorafinal() != null && (nuevaSemanaLaboral.getHorafinal() > 23 || nuevaSemanaLaboral.getHorafinal() < 0)) {
         mensajeValidacionhora = mensajeValidacionhora + " Hora Final Jornada\n ";
         pasas++;
         RequestContext.getCurrentInstance().update("formularioDialogos:validacionHorasSL");
         RequestContext.getCurrentInstance().execute("PF('validacionHorasSL').show()");
      }
      if (nuevaSemanaLaboral.getMinutofinal() == null) {
         System.out.println("Entro a Minuto Final");
         mensajeValidacion = mensajeValidacion + " * Minuto Final\n ";
         pasa++;
      }
      if (nuevaSemanaLaboral.getMinutofinal() != null && (nuevaSemanaLaboral.getMinutofinal() > 59 || nuevaSemanaLaboral.getMinutofinal() < 0)) {
         mensajeValidacionminuto = mensajeValidacionminuto + " Minuto Final Jornada\n ";
         pasas++;
         RequestContext.getCurrentInstance().update("formularioDialogos:validacionMinutosSL");
         RequestContext.getCurrentInstance().execute("PF('validacionMinutosSL').show()");
      }

      if (pasa != 0) {
         RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevaJornadaSemanal");
         RequestContext.getCurrentInstance().execute("PF('validacionNuevaJornadaSemanal').show()");
      }
      if (pasa == 0 && pasas == 0) {
         if (banderaJS == 1) {
            FacesContext c = FacesContext.getCurrentInstance();

            System.out.println("Desactivar");
            System.out.println("TipoLista= " + tipoListaJS);
            SemanaLaboralDia = (Column) c.getViewRoot().findComponent("form:datosSemanasLaborales:SemanaLaboralDia");
            SemanaLaboralDia.setFilterStyle("display: none; visibility: hidden;");
            SemanaLaboralHI = (Column) c.getViewRoot().findComponent("form:datosSemanasLaborales:SemanaLaboralHI");
            SemanaLaboralHI.setFilterStyle("display: none; visibility: hidden;");
            SemanaLaboralMI = (Column) c.getViewRoot().findComponent("form:datosSemanasLaborales:SemanaLaboralMI");
            SemanaLaboralMI.setFilterStyle("display: none; visibility: hidden;");
            SemanaLaboralHIA = (Column) c.getViewRoot().findComponent("form:datosSemanasLaborales:SemanaLaboralHIA");
            SemanaLaboralHIA.setFilterStyle("display: none; visibility: hidden;");
            SemanaLaboralMIA = (Column) c.getViewRoot().findComponent("form:datosSemanasLaborales:SemanaLaboralMIA");
            SemanaLaboralMIA.setFilterStyle("display: none; visibility: hidden;");
            SemanaLaboralHFA = (Column) c.getViewRoot().findComponent("form:datosSemanasLaborales:SemanaLaboralHFA");
            SemanaLaboralHFA.setFilterStyle("display: none; visibility: hidden;");
            SemanaLaboralMFA = (Column) c.getViewRoot().findComponent("form:datosSemanasLaborales:SemanaLaboralMFA");
            SemanaLaboralMFA.setFilterStyle("display: none; visibility: hidden;");
            SemanaLaboralHoraFinal = (Column) c.getViewRoot().findComponent("form:datosSemanasLaborales:SemanaLaboralHoraFinal");
            SemanaLaboralHoraFinal.setFilterStyle("display: none; visibility: hidden;");
            SemanaLaboralMinutoFinal = (Column) c.getViewRoot().findComponent("form:datosSemanasLaborales:SemanaLaboralMinutoFinal");
            SemanaLaboralMinutoFinal.setFilterStyle("display: none; visibility: hidden;");
            altoTabla2 = "115";
            RequestContext.getCurrentInstance().update("form:datosSemanasLaborales");
            banderaJS = 0;
            filtradoListaJornadasSemanales = null;
            tipoListaJS = 0;

         }
         //AGREGAR REGISTRO A LA LISTA JORNADAS LABORALES.
         k++;
         l = BigInteger.valueOf(k);
         nuevaSemanaLaboral.setSecuencia(l);
         nuevaSemanaLaboral.setJornadalaboral(jornadaLaboralSeleccionada);

         listaJornadasSemanalesCrear.add(nuevaSemanaLaboral);
         listaJornadasSemanales.add(nuevaSemanaLaboral);
         jornadaSemanalSeleccionada = listaJornadasSemanales.get(listaJornadasSemanales.indexOf(nuevaSemanaLaboral));

         nuevaSemanaLaboral = new JornadasSemanales();
         contarRegistrosSL();
         RequestContext.getCurrentInstance().update("form:datosSemanasLaborales");
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroJornadaSemanal");
         RequestContext.getCurrentInstance().execute("PF('NuevoRegistroJornadaSemanal').hide()");
      }
   }
//
//   public void chiste() {
//      RequestContext context = RequestContext.getCurrentInstance();
//      System.out.println("Cual Tabla= " + CualTabla);
//      System.out.println("listaJornadasLaborales: " + listaJornadasLaborales);
//      System.out.println("listaSemanas: " + listaJornadasSemanales);
//      if ((listaJornadasLaborales.isEmpty() || listaJornadasSemanales.isEmpty())) {
//         RequestContext.getCurrentInstance().update("formularioDialogos:elegirTabla");
//         RequestContext.getCurrentInstance().execute("PF('elegirTabla').show()");
//      } else if (CualTabla == 0) {
//         RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroJornadaLaboral");
//         RequestContext.getCurrentInstance().execute("PF('NuevoRegistroJornadaLaboral').show()");
//      } else if (CualTabla == 1) {
//         RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroJornadaSemanal");
//         RequestContext.getCurrentInstance().execute("PF('NuevoRegistroJornadaSemanal').show()");
//      }
//   }

   //BORRAR JORNADA LABORAL
   public void borrarJornadaLaboral() {
      if (jornadaLaboralSeleccionada != null && CualTabla == 0) {
         if (listaJornadasSemanales == null || listaJornadasSemanales.isEmpty()) {
            if (!listaJornadasLaboralesModificar.isEmpty() && listaJornadasLaboralesModificar.contains(jornadaLaboralSeleccionada)) {
               listaJornadasLaboralesModificar.remove(jornadaLaboralSeleccionada);
               listaJornadasLaboralesBorrar.add(jornadaLaboralSeleccionada);
            } else if (!listaJornadasLaboralesCrear.isEmpty() && listaJornadasLaboralesCrear.contains(jornadaLaboralSeleccionada)) {
               listaJornadasLaboralesCrear.remove(jornadaLaboralSeleccionada);
            } else {
               listaJornadasLaboralesBorrar.add(jornadaLaboralSeleccionada);
            }
            listaJornadasLaborales.remove(jornadaLaboralSeleccionada);
            contarRegistrosJL();
            if (tipoLista == 1) {
               filtradoListaJornadasLaborales.remove(jornadaLaboralSeleccionada);
            }
            RequestContext.getCurrentInstance().update("form:datosJornadasLaborales");

            if (guardado == true) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
         } else {
            FacesMessage msg = new FacesMessage("Advertencia", "No se puede realizar la operación porque tiene un valor asociado");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
         }

      } else if (jornadaSemanalSeleccionada != null && CualTabla == 1) {

         if (!listaJornadasSemanalesModificar.isEmpty() && listaJornadasSemanalesModificar.contains(jornadaSemanalSeleccionada)) {
            listaJornadasSemanalesModificar.remove(jornadaSemanalSeleccionada);
            listaJornadasSemanalesBorrar.add(jornadaSemanalSeleccionada);
         } else if (!listaJornadasSemanalesCrear.isEmpty() && listaJornadasSemanalesCrear.contains(jornadaSemanalSeleccionada)) {
            listaJornadasSemanalesCrear.remove(jornadaSemanalSeleccionada);
         } else {
            listaJornadasSemanalesBorrar.add(jornadaSemanalSeleccionada);
         }
         listaJornadasSemanales.remove(jornadaSemanalSeleccionada);
         contarRegistrosSL();

         if (tipoListaJS == 1) {
            filtradoListaJornadasSemanales.remove(jornadaSemanalSeleccionada);
         }
         RequestContext.getCurrentInstance().update("form:datosSemanasLaborales");

         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   //LIMPIAR NUEVO REGISTRO JORNADA LABORAL
   public void limpiarNuevaJornadaLaboral() {
      nuevaJornadaLaboral = new JornadasLaborales();
   }

   //LIMPIAR NUEVO REGISTRO SEMANA LABORAL
   public void limpiarNuevaSemanaLaboral() {
      nuevaSemanaLaboral = new JornadasSemanales();
   }

   public void valoresBackupAutocompletar(int tipoNuevo, String Campo) {
      if (Campo.equals("JORNADA")) {
         if (tipoNuevo == 1) {
            jornada = nuevaJornadaLaboral.getJornada().getDescripcion();
         } else if (tipoNuevo == 2) {
            jornada = duplicarJornadaLaboral.getJornada().getDescripcion();
         }

      }
   }

   //DUPLICAR JORNADAS LABORALES
   public void duplicarJL() {
      if (jornadaLaboralSeleccionada != null && CualTabla == 0) {
         duplicarJornadaLaboral = new JornadasLaborales();
         duplicarJornadaLaboral.setCodigo(jornadaLaboralSeleccionada.getCodigo());
         duplicarJornadaLaboral.setDescripcion(jornadaLaboralSeleccionada.getDescripcion());
         duplicarJornadaLaboral.setHorasdiarias(jornadaLaboralSeleccionada.getHorasdiarias());
         duplicarJornadaLaboral.setHorasmensuales(jornadaLaboralSeleccionada.getHorasmensuales());
         duplicarJornadaLaboral.setRotativo(jornadaLaboralSeleccionada.getRotativo());
         duplicarJornadaLaboral.setTurnorelativo(jornadaLaboralSeleccionada.getTurnorelativo());
         duplicarJornadaLaboral.setCuadrillahorasdiarias(jornadaLaboralSeleccionada.getCuadrillahorasdiarias());
         duplicarJornadaLaboral.setJornada(jornadaLaboralSeleccionada.getJornada());

         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarJornadaLaboral");
         RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroJornadaLaboral').show()");

      } else if (jornadaSemanalSeleccionada != null && CualTabla == 1) {
         System.out.println("Entra Duplicar JS");

         duplicarSemanaLaboral = new JornadasSemanales();
         m++;
         n = BigInteger.valueOf(m);

         duplicarSemanaLaboral.setJornadalaboral(jornadaLaboralSeleccionada);
         duplicarSemanaLaboral.setDia(jornadaSemanalSeleccionada.getDia());
         duplicarSemanaLaboral.setHorainicial(jornadaSemanalSeleccionada.getHorainicial());
         duplicarSemanaLaboral.setMinutoinicial(jornadaSemanalSeleccionada.getMinutoinicial());
         duplicarSemanaLaboral.setHorainicialalimentacion(jornadaSemanalSeleccionada.getHorainicialalimentacion());
         duplicarSemanaLaboral.setMinutoinicialalimentacion(jornadaSemanalSeleccionada.getMinutoinicialalimentacion());
         duplicarSemanaLaboral.setHorafinalalimentacion(jornadaSemanalSeleccionada.getHorafinalalimentacion());
         duplicarSemanaLaboral.setMinutofinalalimentacion(jornadaSemanalSeleccionada.getMinutofinalalimentacion());
         duplicarSemanaLaboral.setHorafinal(jornadaSemanalSeleccionada.getHorafinal());
         duplicarSemanaLaboral.setMinutofinal(jornadaSemanalSeleccionada.getMinutofinal());

         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarSemanaLaboral");
         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroSemanaLaboral').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void llamarLovJornadas(int tipoJ) {
      tipoActualizacion = tipoJ;
      RequestContext.getCurrentInstance().update("formularioDialogos:jornadasDialogo");
      RequestContext.getCurrentInstance().execute("PF('jornadasDialogo').show()");
   }

   //LIMPIAR DUPLICAR JORNADA LABORAL
   public void limpiarduplicarJornadaLaboral() {
      duplicarJornadaLaboral = new JornadasLaborales();
   }
   //LIMPIAR DUPLICAR SEMANA LABORAL

   public void limpiarduplicarSemanaLaboral() {
      duplicarSemanaLaboral = new JornadasSemanales();
   }

   //CONFIRMAR DUPLICAR JORNADA LABORAL
   public void confirmarDuplicarJL() {
      k++;
      l = BigInteger.valueOf(k);
      duplicarJornadaLaboral.setSecuencia(l);
      int pasa = 0;
      mensajeValidacion = " ";
      mensajeValidacionhoras = " ";

      if (duplicarJornadaLaboral.getHorasdiarias() != null && (duplicarJornadaLaboral.getHorasdiarias().compareTo(BigDecimal.ONE) == 0 || duplicarJornadaLaboral.getHorasdiarias().compareTo(BigDecimal.ONE) == -1)) {
         mensajeValidacionhoras = mensajeValidacionhoras + " Horas Diarias. ";
         pasa++;
         RequestContext.getCurrentInstance().update("formularioDialogos:validacionHorasJL");
         RequestContext.getCurrentInstance().execute("PF('validacionHorasJL').show()");
      }
      if (duplicarJornadaLaboral.getHorasmensuales() != null && (duplicarJornadaLaboral.getHorasmensuales().compareTo(Short.valueOf("0")) == 0 || duplicarJornadaLaboral.getHorasmensuales().compareTo(Short.valueOf("0")) < 0)) {
         mensajeValidacionhoras = mensajeValidacionhoras + " Horas Mensuale. ";
         pasa++;
         RequestContext.getCurrentInstance().update("formularioDialogos:validacionHorasJL");
         RequestContext.getCurrentInstance().execute("PF('validacionHorasJL').show()");
      }
      if (duplicarJornadaLaboral.getCodigo() != null) {
         for (int i = 0; i < listaJornadasLaborales.size(); i++) {
            if (duplicarJornadaLaboral.getCodigo() == listaJornadasLaborales.get(i).getCodigo()) {
               pasa++;
               RequestContext.getCurrentInstance().update("formularioDialogos:validacionCodigo");
               RequestContext.getCurrentInstance().execute("PF('validacionCodigo').show()");
            }
         }
      }
      if (duplicarJornadaLaboral.getDescripcion() == null || duplicarJornadaLaboral.getDescripcion().equals("")) {
         System.out.println("Entro a Descripción");
         mensajeValidacion = mensajeValidacion + " * Descripcion\n";
         pasa++;
      }

      if (pasa == 0) {

         if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();

            System.out.println("Desactivar");
            System.out.println("TipoLista= " + tipoLista);
            jornadasLaboralesCodigos = (Column) c.getViewRoot().findComponent("form:datosJornadasLaborales:jornadasLaboralesCodigos");
            jornadasLaboralesCodigos.setFilterStyle("display: none; visibility: hidden;");
            jornadasLaboralesDescripcion = (Column) c.getViewRoot().findComponent("form:datosJornadasLaborales:jornadasLaboralesDescripcion");
            jornadasLaboralesDescripcion.setFilterStyle("display: none; visibility: hidden;");
            jornadasLaboralesHorasDiarias = (Column) c.getViewRoot().findComponent("form:datosJornadasLaborales:jornadasLaboralesHorasDiarias");
            jornadasLaboralesHorasDiarias.setFilterStyle("display: none; visibility: hidden;");
            jornadasLaboralesHorasMensuales = (Column) c.getViewRoot().findComponent("form:datosJornadasLaborales:jornadasLaboralesHorasMensuales");
            jornadasLaboralesHorasMensuales.setFilterStyle("display: none; visibility: hidden;");
            jornadasLaboralesRotativo = (Column) c.getViewRoot().findComponent("form:datosJornadasLaborales:jornadasLaboralesRotativo");
            jornadasLaboralesRotativo.setFilterStyle("display: none; visibility: hidden;");
            jornadasLaboralesTurnoRelativo = (Column) c.getViewRoot().findComponent("form:datosJornadasLaborales:jornadasLaboralesTurnoRelativo");
            jornadasLaboralesTurnoRelativo.setFilterStyle("display: none; visibility: hidden;");
            jornadasLaboralesCuadrillaHorasDiarias = (Column) c.getViewRoot().findComponent("form:datosJornadasLaborales:jornadasLaboralesCuadrillaHorasDiarias");
            jornadasLaboralesCuadrillaHorasDiarias.setFilterStyle("display: none; visibility: hidden;");
            jornadasLaboralesJornadas = (Column) c.getViewRoot().findComponent("form:datosJornadasLaborales:jornadasLaboralesJornadas");
            jornadasLaboralesJornadas.setFilterStyle("display: none; visibility: hidden;");
            altoTabla = "115";
            RequestContext.getCurrentInstance().update("form:datosJornadasLaborales");
            bandera = 0;
            filtradoListaJornadasLaborales = null;
            tipoLista = 0;

         }
         listaJornadasLaborales.add(duplicarJornadaLaboral);
         listaJornadasLaboralesCrear.add(duplicarJornadaLaboral);
         jornadaLaboralSeleccionada = listaJornadasLaborales.get(listaJornadasLaborales.indexOf(duplicarJornadaLaboral));

         RequestContext.getCurrentInstance().update("form:datosJornadasLaborales");
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }

         duplicarJornadaLaboral = new JornadasLaborales();
         contarRegistrosJL();

         RequestContext.getCurrentInstance().update("formularioDialogos:DuplicarRegistroJornadaLaboral");
         RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroJornadaLaboral').hide()");

      } else if (duplicarJornadaLaboral.getDescripcion() == null || duplicarJornadaLaboral.getDescripcion().equals("")) {
         RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevaJornadaLaboral");
         RequestContext.getCurrentInstance().execute("PF('validacionNuevaJornadaLaboral').show()");
      }
   }

   //CONFIRMAR DUPLICAR JORNADA LABORAL
   public void confirmarDuplicarJS() {

      k++;
      l = BigInteger.valueOf(k);
      duplicarSemanaLaboral.setSecuencia(l);

      int pasa = 0;
      int pasas = 0;
      mensajeValidacion = " ";
      mensajeValidacionhora = " ";
      mensajeValidacionminuto = " ";

      if (duplicarSemanaLaboral.getDia() != null) {
         System.out.println("duplicarSemanaLaboral.getDia() : " + duplicarSemanaLaboral.getDia());
         for (int i = 0; i < listaJornadasSemanales.size(); i++) {
            System.out.println("listaJornadasSemanales.get(i).getDia() : " + listaJornadasSemanales.get(i).getDia());
            if (duplicarSemanaLaboral.getDia().equalsIgnoreCase(listaJornadasSemanales.get(i).getDia())) {
               pasas++;
               RequestContext.getCurrentInstance().update("formularioDialogos:validacionDia");
               RequestContext.getCurrentInstance().execute("PF('validacionDia').show()");
               System.out.println("I find you !");
            }
         }
      }
      if (duplicarSemanaLaboral.getHorainicial() == null) {
         System.out.println("Entro a Hora Inicial");
         mensajeValidacion = mensajeValidacion + " * Hora Inicial\n ";
         pasa++;
      }
      if (duplicarSemanaLaboral.getHorainicial() != null && (duplicarSemanaLaboral.getHorainicial() > 23 || duplicarSemanaLaboral.getHorainicial() < 0)) {
         mensajeValidacionhora = mensajeValidacionhora + " Hora Inicial Jornada\n ";
         pasas++;
         RequestContext.getCurrentInstance().update("formularioDialogos:validacionHorasSL");
         RequestContext.getCurrentInstance().execute("PF('validacionHorasSL').show()");
      }
      if (duplicarSemanaLaboral.getMinutoinicial() == null) {
         System.out.println("Entro a Minuto Inicial");
         mensajeValidacion = mensajeValidacion + " * Minuto Inicial\n ";
         pasa++;
      }
      if (duplicarSemanaLaboral.getMinutoinicial() != null && (duplicarSemanaLaboral.getMinutoinicial() > 59 || duplicarSemanaLaboral.getMinutoinicial() < 0)) {
         mensajeValidacionminuto = mensajeValidacionminuto + " Minuto Inicial Jornada\n ";
         pasas++;
         RequestContext.getCurrentInstance().update("formularioDialogos:validacionMinutosSL");
         RequestContext.getCurrentInstance().execute("PF('validacionMinutosSL').show()");
      }
      if (duplicarSemanaLaboral.getHorainicialalimentacion() == null) {
         System.out.println("Entro a Hora Inicial Alimentación");
         mensajeValidacion = mensajeValidacion + " * Hora Inicial Alimentacion\n ";
         pasa++;
      }
      if (duplicarSemanaLaboral.getHorainicialalimentacion() != null && (duplicarSemanaLaboral.getHorainicialalimentacion() > 23 || duplicarSemanaLaboral.getHorainicialalimentacion() < 0)) {
         mensajeValidacionhora = mensajeValidacionhora + " Hora Inicial Alimentacion\n ";
         pasas++;
         RequestContext.getCurrentInstance().update("formularioDialogos:validacionHorasSL");
         RequestContext.getCurrentInstance().execute("PF('validacionHorasSL').show()");
      }
      if (duplicarSemanaLaboral.getMinutoinicialalimentacion() == null) {
         System.out.println("Entro a Minuto Inicial Alimentación");
         mensajeValidacion = mensajeValidacion + " * Minuto Inicial Alimentacion\n ";
         pasa++;
      }
      if (duplicarSemanaLaboral.getMinutoinicialalimentacion() != null && (duplicarSemanaLaboral.getMinutoinicialalimentacion() > 59 || duplicarSemanaLaboral.getMinutoinicialalimentacion() < 0)) {
         mensajeValidacionminuto = mensajeValidacionminuto + " Minuto Inicial Alimentacion\n ";
         pasas++;
         RequestContext.getCurrentInstance().update("formularioDialogos:validacionMinutosSL");
         RequestContext.getCurrentInstance().execute("PF('validacionMinutosSL').show()");
      }
      if (duplicarSemanaLaboral.getHorafinalalimentacion() == null) {
         System.out.println("Entro a Hora Final Alimentación");
         mensajeValidacion = mensajeValidacion + " * Hora Final Alimentacion\n ";
         pasa++;
      }
      if (duplicarSemanaLaboral.getHorafinalalimentacion() != null && (duplicarSemanaLaboral.getHorafinalalimentacion() > 23 || duplicarSemanaLaboral.getHorafinalalimentacion() < 0)) {
         mensajeValidacionhora = mensajeValidacionhora + " Hora Final Alimentacion\n ";
         pasas++;
         RequestContext.getCurrentInstance().update("formularioDialogos:validacionHorasSL");
         RequestContext.getCurrentInstance().execute("PF('validacionHorasSL').show()");
      }
      if (duplicarSemanaLaboral.getMinutofinalalimentacion() == null) {
         System.out.println("Entro a Minuto Final Alimentación");
         mensajeValidacion = mensajeValidacion + " * Minuto Final Alimentacion\n ";
         pasa++;
      }
      if (duplicarSemanaLaboral.getMinutofinalalimentacion() != null && (duplicarSemanaLaboral.getMinutofinalalimentacion() > 59 || duplicarSemanaLaboral.getMinutofinalalimentacion() < 0)) {
         mensajeValidacionminuto = mensajeValidacionminuto + " Minuto Final Alimentacion\n ";
         pasas++;
         RequestContext.getCurrentInstance().update("formularioDialogos:validacionMinutosSL");
         RequestContext.getCurrentInstance().execute("PF('validacionMinutosSL').show()");
      }
      if (duplicarSemanaLaboral.getHorafinal() == null) {
         System.out.println("Entro a Hora Final");
         mensajeValidacion = mensajeValidacion + " * Hora Final\n ";
         pasa++;
      }
      if (duplicarSemanaLaboral.getHorafinal() != null && (duplicarSemanaLaboral.getHorafinal() > 23 || duplicarSemanaLaboral.getHorafinal() < 0)) {
         mensajeValidacionhora = mensajeValidacionhora + " Hora Final Jornada\n ";
         pasas++;
         RequestContext.getCurrentInstance().update("formularioDialogos:validacionHorasSL");
         RequestContext.getCurrentInstance().execute("PF('validacionHorasSL').show()");
      }
      if (duplicarSemanaLaboral.getMinutofinal() == null) {
         System.out.println("Entro a Minuto Final");
         mensajeValidacion = mensajeValidacion + " * Minuto Final\n ";
         pasa++;
      }
      if (duplicarSemanaLaboral.getMinutofinal() != null && (duplicarSemanaLaboral.getMinutofinal() > 59 || duplicarSemanaLaboral.getMinutofinal() < 0)) {
         mensajeValidacionminuto = mensajeValidacionminuto + " Minuto Final Jornada\n ";
         pasas++;
         RequestContext.getCurrentInstance().update("formularioDialogos:validacionMinutosSL");
         RequestContext.getCurrentInstance().execute("PF('validacionMinutosSL').show()");
      }

      if (pasa != 0) {
         RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevaJornadaSemanal");
         RequestContext.getCurrentInstance().execute("PF('validacionNuevaJornadaSemanal').show()");
      }
      if (pasa == 0 && pasas == 0) {
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         if (banderaJS == 1) {
            FacesContext c = FacesContext.getCurrentInstance();

            System.out.println("Desactivar");
            System.out.println("TipoLista= " + tipoListaJS);
            SemanaLaboralHI = (Column) c.getViewRoot().findComponent("form:datosSemanasLaborales:SemanaLaboralHI");
            SemanaLaboralHI.setFilterStyle("display: none; visibility: hidden;");
            SemanaLaboralMI = (Column) c.getViewRoot().findComponent("form:datosSemanasLaborales:SemanaLaboralMI");
            SemanaLaboralMI.setFilterStyle("display: none; visibility: hidden;");
            SemanaLaboralHIA = (Column) c.getViewRoot().findComponent("form:datosSemanasLaborales:SemanaLaboralHIA");
            SemanaLaboralHIA.setFilterStyle("display: none; visibility: hidden;");
            SemanaLaboralMIA = (Column) c.getViewRoot().findComponent("form:datosSemanasLaborales:SemanaLaboralMIA");
            SemanaLaboralMIA.setFilterStyle("display: none; visibility: hidden;");
            SemanaLaboralHFA = (Column) c.getViewRoot().findComponent("form:datosSemanasLaborales:SemanaLaboralHFA");
            SemanaLaboralHFA.setFilterStyle("display: none; visibility: hidden;");
            SemanaLaboralMFA = (Column) c.getViewRoot().findComponent("form:datosSemanasLaborales:SemanaLaboralMFA");
            SemanaLaboralMFA.setFilterStyle("display: none; visibility: hidden;");
            SemanaLaboralHoraFinal = (Column) c.getViewRoot().findComponent("form:datosSemanasLaborales:SemanaLaboralHoraFinal");
            SemanaLaboralHoraFinal.setFilterStyle("display: none; visibility: hidden;");
            SemanaLaboralMinutoFinal = (Column) c.getViewRoot().findComponent("form:datosSemanasLaborales:SemanaLaboralMinutoFinal");
            SemanaLaboralMinutoFinal.setFilterStyle("display: none; visibility: hidden;");
            altoTabla2 = "115";
            RequestContext.getCurrentInstance().update("form:datosSemanasLaborales");
            banderaJS = 0;
            filtradoListaJornadasSemanales = null;
            tipoListaJS = 0;

         }
         listaJornadasSemanales.add(duplicarSemanaLaboral);
         listaJornadasSemanalesCrear.add(duplicarSemanaLaboral);
         jornadaSemanalSeleccionada = listaJornadasSemanales.get(listaJornadasSemanales.indexOf(duplicarSemanaLaboral));

         RequestContext.getCurrentInstance().update("form:datosSemanasLaborales");
         duplicarSemanaLaboral = new JornadasSemanales();
         contarRegistrosJL();
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarSemanaLaboral");
         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroSemanaLaboral').hide()");
      }
   }

   public void autocompletarNuevoyDuplicado(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      if (confirmarCambio.equalsIgnoreCase("JORNADA")) {
         if (tipoNuevo == 1) {
            nuevaJornadaLaboral.getJornada().setDescripcion(jornada);
         } else if (tipoNuevo == 2) {
            duplicarJornadaLaboral.getJornada().setDescripcion(jornada);
         }
         for (int i = 0; i < lovJornadas.size(); i++) {
            if (lovJornadas.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoNuevo == 1) {
               nuevaJornadaLaboral.setJornada(lovJornadas.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaJornada");
            } else if (tipoNuevo == 2) {
               duplicarJornadaLaboral.setJornada(lovJornadas.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarJornada");
            }
            lovJornadas.clear();
            getLovJornadas();
         } else {
            RequestContext.getCurrentInstance().update("formularioDialogos:jornadasDialogo");
            RequestContext.getCurrentInstance().execute("PF('jornadasDialogo').show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaJornada");
            } else if (tipoNuevo == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarJornada");
            }
         }
      }
   }

   public void dialogoJornadaLaboral() {
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:NuevoRegistroJornadaLaboral");
      RequestContext.getCurrentInstance().execute("PF('NuevoRegistroJornadaLaboral').show()");
   }

   public void dialogoSemanaLaboral() {
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:NuevoRegistroJornadaSemanal");
      RequestContext.getCurrentInstance().execute("PF('NuevoRegistroJornadaSemanal').show()");

   }

   //VERIFICAR RASTRO
   public void verificarRastro() {
      if (CualTabla == 0) {
         System.out.println("lol");
         if (jornadaLaboralSeleccionada != null) {
            System.out.println("lol2");
            int resultado = administrarRastros.obtenerTabla(jornadaLaboralSeleccionada.getSecuencia(), "JORNADASLABORALES");
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
            } else if (resultado == 6) {
               RequestContext.getCurrentInstance().execute("PF('errorTablaSinRastro').show()");
            } else if (resultado == 7) {
               RequestContext.getCurrentInstance().execute("PF('errorTablaSinRastro').show()");
            }
         } else if (administrarRastros.verificarHistoricosTabla("JORNADASLABORALES")) {
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
         } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
         }
      } else if (jornadaSemanalSeleccionada != null) {
         System.out.println("JS2");
         int resultadoJS = administrarRastros.obtenerTabla(jornadaSemanalSeleccionada.getSecuencia(), "JORNADASSEMANALES");
         System.out.println("resultado: " + resultadoJS);
         if (resultadoJS == 1) {
            RequestContext.getCurrentInstance().execute("PF('errorObjetosDBJS').show()");
         } else if (resultadoJS == 2) {
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroJS').show()");
         } else if (resultadoJS == 3) {
            RequestContext.getCurrentInstance().execute("PF('errorRegistroRastroJS').show()");
         } else if (resultadoJS == 4) {
            RequestContext.getCurrentInstance().execute("PF('errorTablaConRastroJS').show()");
         } else if (resultadoJS == 5) {
            RequestContext.getCurrentInstance().execute("PF('errorTablaSinRastroJS').show()");
         } else if (resultadoJS == 6) {
            RequestContext.getCurrentInstance().execute("PF('errorTablaSinRastroJS').show()");
         } else if (resultadoJS == 7) {
            RequestContext.getCurrentInstance().execute("PF('errorTablaSinRastroJS').show()");
         } else if (resultadoJS == 8) {
            RequestContext.getCurrentInstance().execute("PF('errorTablaSinRastroJS').show()");
         }
      } else if (administrarRastros.verificarHistoricosTabla("JORNADASSEMANALES")) {
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistoricoJS').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistoricoJS').show()");
      }
   }

   //CANCELAR MODIFICACIONES
   public void cancelarModificacion() {
      if (bandera == 1) {
         FacesContext c = FacesContext.getCurrentInstance();
         jornadasLaboralesCodigos = (Column) c.getViewRoot().findComponent("form:datosJornadasLaborales:jornadasLaboralesCodigos");
         jornadasLaboralesCodigos.setFilterStyle("display: none; visibility: hidden;");
         jornadasLaboralesDescripcion = (Column) c.getViewRoot().findComponent("form:datosJornadasLaborales:jornadasLaboralesDescripcion");
         jornadasLaboralesDescripcion.setFilterStyle("display: none; visibility: hidden;");
         jornadasLaboralesHorasDiarias = (Column) c.getViewRoot().findComponent("form:datosJornadasLaborales:jornadasLaboralesHorasDiarias");
         jornadasLaboralesHorasDiarias.setFilterStyle("display: none; visibility: hidden;");
         jornadasLaboralesHorasMensuales = (Column) c.getViewRoot().findComponent("form:datosJornadasLaborales:jornadasLaboralesHorasMensuales");
         jornadasLaboralesHorasMensuales.setFilterStyle("display: none; visibility: hidden;");
         jornadasLaboralesRotativo = (Column) c.getViewRoot().findComponent("form:datosJornadasLaborales:jornadasLaboralesRotativo");
         jornadasLaboralesRotativo.setFilterStyle("display: none; visibility: hidden;");
         jornadasLaboralesTurnoRelativo = (Column) c.getViewRoot().findComponent("form:datosJornadasLaborales:jornadasLaboralesTurnoRelativo");
         jornadasLaboralesTurnoRelativo.setFilterStyle("display: none; visibility: hidden;");
         jornadasLaboralesCuadrillaHorasDiarias = (Column) c.getViewRoot().findComponent("form:datosJornadasLaborales:jornadasLaboralesCuadrillaHorasDiarias");
         jornadasLaboralesCuadrillaHorasDiarias.setFilterStyle("display: none; visibility: hidden;");
         jornadasLaboralesJornadas = (Column) c.getViewRoot().findComponent("form:datosJornadasLaborales:pECalificaciones");
         jornadasLaboralesJornadas.setFilterStyle("display: none; visibility: hidden;");
         altoTabla = "115";
         RequestContext.getCurrentInstance().update("form:datosJornadasLaborales");
         bandera = 0;
         filtradoListaJornadasLaborales = null;
         tipoLista = 0;
      }

      if (banderaJS == 1) {
         FacesContext c = FacesContext.getCurrentInstance();
         SemanaLaboralDia = (Column) c.getViewRoot().findComponent("form:datosSemanasLaborales:SemanaLaboralDia");
         SemanaLaboralDia.setFilterStyle("display: none; visibility: hidden;");
         SemanaLaboralHI = (Column) c.getViewRoot().findComponent("form:datosSemanasLaborales:SemanaLaboralHI");
         SemanaLaboralHI.setFilterStyle("display: none; visibility: hidden;");
         SemanaLaboralMI = (Column) c.getViewRoot().findComponent("form:datosSemanasLaborales:SemanaLaboralMI");
         SemanaLaboralMI.setFilterStyle("display: none; visibility: hidden;");
         SemanaLaboralHIA = (Column) c.getViewRoot().findComponent("form:datosSemanasLaborales:SemanaLaboralHIA");
         SemanaLaboralHIA.setFilterStyle("display: none; visibility: hidden;");
         SemanaLaboralMIA = (Column) c.getViewRoot().findComponent("form:datosSemanasLaborales:SemanaLaboralMIA");
         SemanaLaboralMIA.setFilterStyle("display: none; visibility: hidden;");
         SemanaLaboralHFA = (Column) c.getViewRoot().findComponent("form:datosSemanasLaborales:SemanaLaboralHFA");
         SemanaLaboralHFA.setFilterStyle("display: none; visibility: hidden;");
         SemanaLaboralMFA = (Column) c.getViewRoot().findComponent("form:datosSemanasLaborales:SemanaLaboralMFA");
         SemanaLaboralMFA.setFilterStyle("display: none; visibility: hidden;");
         SemanaLaboralHoraFinal = (Column) c.getViewRoot().findComponent("form:datosSemanasLaborales:SemanaLaboralHoraFinal");
         SemanaLaboralHoraFinal.setFilterStyle("display: none; visibility: hidden;");
         SemanaLaboralMinutoFinal = (Column) c.getViewRoot().findComponent("form:datosSemanasLaborales:SemanaLaboralMFA");
         SemanaLaboralMinutoFinal.setFilterStyle("display: none; visibility: hidden;");
         altoTabla2 = "115";
         RequestContext.getCurrentInstance().update("form:datosSemanasLaborales");
         banderaJS = 0;
         filtradoListaJornadasSemanales = null;
         tipoListaJS = 0;
      }
      listaJornadasLaboralesBorrar.clear();
      listaJornadasLaboralesCrear.clear();
      listaJornadasLaboralesModificar.clear();
      jornadaLaboralSeleccionada = null;
      listaJornadasLaborales = null;
      getListaJornadasLaborales();
      if (listaJornadasLaborales != null) {
         if (!listaJornadasLaborales.isEmpty()) {
            jornadaLaboralSeleccionada = listaJornadasLaborales.get(0);
         }
      }
      contarRegistrosJL();
      listaJornadasSemanalesBorrar.clear();
      listaJornadasSemanalesCrear.clear();
      listaJornadasSemanalesModificar.clear();
      jornadaSemanalSeleccionada = null;
      listaJornadasSemanales = null;
      getListaJornadasSemanales();
      if (listaJornadasSemanales != null) {
         if (!listaJornadasSemanales.isEmpty()) {
            jornadaSemanalSeleccionada = listaJornadasSemanales.get(0);
         }
      }
      contarRegistrosSL();
      guardado = true;
      permitirIndex = true;
      RequestContext.getCurrentInstance().update("form:datosJornadasLaborales");
      RequestContext.getCurrentInstance().update("form:datosSemanasLaborales");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void contarRegistrosLovJornadas() {
      RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroJornadas");
   }

   public void contarRegistrosJL() {
      RequestContext.getCurrentInstance().update("form:infoRegistroJL");
   }

   public void contarRegistrosSL() {
      RequestContext.getCurrentInstance().update("form:infoRegistroSL");
   }

   //Getter & Setters
   public BigInteger getSecuenciaJornada() {
      return secuenciaJornada;
   }

   public void setSecuenciaJornada(BigInteger secuenciaJornada) {
      this.secuenciaJornada = secuenciaJornada;
   }

   public List<JornadasLaborales> getListaJornadasLaborales() {
      if (listaJornadasLaborales == null) {
         listaJornadasLaborales = administrarJornadasLaborales.consultarJornadasLaborales();
      }
      return listaJornadasLaborales;
   }

   public void setListaJornadasLaborales(List<JornadasLaborales> listaJornadasLaborales) {
      this.listaJornadasLaborales = listaJornadasLaborales;
   }

   public List<JornadasLaborales> getFiltradoListaJornadasLaborales() {
      return filtradoListaJornadasLaborales;
   }

   public void setFiltradoListaJornadasLaborales(List<JornadasLaborales> filtradoListaJornadasLaborales) {
      this.filtradoListaJornadasLaborales = filtradoListaJornadasLaborales;
   }

   public JornadasLaborales getJornadaLaboralSeleccionada() {
      return jornadaLaboralSeleccionada;
   }

   public void setJornadaLaboralSeleccionada(JornadasLaborales jornadaLaboralSeleccionada) {
      this.jornadaLaboralSeleccionada = jornadaLaboralSeleccionada;
   }

   public List<JornadasSemanales> getListaJornadasSemanales() {
      if (listaJornadasSemanales == null && jornadaLaboralSeleccionada != null) {
         listaJornadasSemanales = administrarJornadasLaborales.consultarJornadasSemanales(jornadaLaboralSeleccionada.getSecuencia());
      }
      return listaJornadasSemanales;
   }

   public void setListaJornadasSemanales(List<JornadasSemanales> listaJornadasSemanales) {
      this.listaJornadasSemanales = listaJornadasSemanales;
   }

   public List<JornadasSemanales> getFiltradoListaJornadasSemanales() {
      return filtradoListaJornadasSemanales;
   }

   public void setFiltradoListaJornadasSemanales(List<JornadasSemanales> filtradoListaJornadasSemanales) {
      this.filtradoListaJornadasSemanales = filtradoListaJornadasSemanales;
   }

   public JornadasSemanales getJornadaSemanalSeleccionada() {
      return jornadaSemanalSeleccionada;
   }

   public void setJornadaSemanalSeleccionada(JornadasSemanales jornadaSemanalSeleccionada) {
      this.jornadaSemanalSeleccionada = jornadaSemanalSeleccionada;
   }

   public String getAltoTabla() {
      return altoTabla;
   }

   public void setAltoTabla(String altoTabla) {
      this.altoTabla = altoTabla;
   }

   public String getAltoTabla2() {
      return altoTabla2;
   }

   public void setAltoTabla2(String altoTabla2) {
      this.altoTabla2 = altoTabla2;
   }

   public List<Jornadas> getLovJornadas() {
      if (lovJornadas == null) {
         lovJornadas = administrarJornadasLaborales.consultarJornadas();
      }
      return lovJornadas;
   }

   public void setLovJornadas(List<Jornadas> lovJornadas) {
      this.lovJornadas = lovJornadas;
   }

   public List<Jornadas> getLovFiltradoJornadas() {
      return lovFiltradoJornadas;
   }

   public void setLovFiltradoJornadas(List<Jornadas> lovFiltradoJornadas) {
      this.lovFiltradoJornadas = lovFiltradoJornadas;
   }

   public Jornadas getJornadaSeleccionada() {
      return jornadaSeleccionada;
   }

   public void setJornadaSeleccionada(Jornadas jornadaSeleccionada) {
      this.jornadaSeleccionada = jornadaSeleccionada;
   }

   public String getInfoRegistroJornadas() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:LOVJornadas");
      infoRegistroJornadas = String.valueOf(tabla.getRowCount());
      return infoRegistroJornadas;
   }

   public void setInfoRegistroJornadas(String infoRegistroJornadas) {
      this.infoRegistroJornadas = infoRegistroJornadas;
   }

   public String getInfoRegistroJL() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosJornadasLaborales");
      infoRegistroJL = String.valueOf(tabla.getRowCount());
      return infoRegistroJL;
   }

   public void setInfoRegistroJL(String infoRegistroJL) {
      this.infoRegistroJL = infoRegistroJL;
   }

   public String getInfoRegistroSL() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosSemanasLaborales");
      infoRegistroSL = String.valueOf(tabla.getRowCount());
      return infoRegistroSL;
   }

   public void setInfoRegistroSL(String infoRegistroSL) {
      this.infoRegistroSL = infoRegistroSL;
   }

   public boolean isAceptar() {
      return aceptar;
   }

   public void setAceptar(boolean aceptar) {
      this.aceptar = aceptar;
   }

   public String getBuscarNombre() {
      return buscarNombre;
   }

   public void setBuscarNombre(String buscarNombre) {
      this.buscarNombre = buscarNombre;
   }

   public boolean isBuscador() {
      return buscador;
   }

   public void setBuscador(boolean buscador) {
      this.buscador = buscador;
   }

   public boolean isGuardado() {
      return guardado;
   }

   public void setGuardado(boolean guardado) {
      this.guardado = guardado;
   }

   public JornadasLaborales getNuevaJornadaLaboral() {
      return nuevaJornadaLaboral;
   }

   public void setNuevaJornadaLaboral(JornadasLaborales nuevaJornadaLaboral) {
      this.nuevaJornadaLaboral = nuevaJornadaLaboral;
   }

   public JornadasSemanales getNuevaJornadaSemanal() {
      return nuevaSemanaLaboral;
   }

   public void setNuevaJornadaSemanal(JornadasSemanales nuevaSemanaLaboral) {
      this.nuevaSemanaLaboral = nuevaSemanaLaboral;
   }

   public String getMensajeValidacion() {
      return mensajeValidacion;
   }

   public void setMensajeValidacion(String mensajeValidacion) {
      this.mensajeValidacion = mensajeValidacion;
   }

   public String getMensajeValidacionhoras() {
      return mensajeValidacionhoras;
   }

   public void setMensajeValidacionhoras(String mensajeValidacionhoras) {
      this.mensajeValidacionhoras = mensajeValidacionhoras;
   }

   public String getMensajeValidacionhora() {
      return mensajeValidacionhora;
   }

   public void setMensajeValidacionhora(String mensajeValidacionhora) {
      this.mensajeValidacionhora = mensajeValidacionhora;
   }

   public String getMensajeValidacionminuto() {
      return mensajeValidacionminuto;
   }

   public void setMensajeValidacionminuto(String mensajeValidacionminuto) {
      this.mensajeValidacionminuto = mensajeValidacionminuto;
   }

   public JornadasLaborales getEditarJornadaLaboral() {
      return editarJornadaLaboral;
   }

   public void setEditarJornadaLaboral(JornadasLaborales editarJornadaLaboral) {
      this.editarJornadaLaboral = editarJornadaLaboral;
   }

   public JornadasSemanales getEditarJornadaSemanal() {
      return editarJornadaSemanal;
   }

   public void setEditarJornadaSemanal(JornadasSemanales editarJornadaSemanal) {
      this.editarJornadaSemanal = editarJornadaSemanal;
   }

   public JornadasLaborales getDuplicarJornadaLaboral() {
      return duplicarJornadaLaboral;
   }

   public void setDuplicarJornadaLaboral(JornadasLaborales duplicarJornadaLaboral) {
      this.duplicarJornadaLaboral = duplicarJornadaLaboral;
   }

   public JornadasSemanales getDuplicarSemanaLaboral() {
      return duplicarSemanaLaboral;
   }

   public void setDuplicarSemanaLaboral(JornadasSemanales duplicarSemanaLaboral) {
      this.duplicarSemanaLaboral = duplicarSemanaLaboral;
   }

   public String getCualInsertar() {
      return cualInsertar;
   }

   public void setCualInsertar(String cualInsertar) {
      this.cualInsertar = cualInsertar;
   }

   public String getCualNuevo() {
      return cualNuevo;
   }

   public void setCualNuevo(String cualNuevo) {
      this.cualNuevo = cualNuevo;
   }

   public String getTablaImprimir() {
      return tablaImprimir;
   }

   public void setTablaImprimir(String tablaImprimir) {
      this.tablaImprimir = tablaImprimir;
   }

   public String getNombreArchivo() {
      return nombreArchivo;
   }

   public void setNombreArchivo(String nombreArchivo) {
      this.nombreArchivo = nombreArchivo;
   }

}
