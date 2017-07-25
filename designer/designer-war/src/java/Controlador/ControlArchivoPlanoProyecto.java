/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import ClasesAyuda.ErroresNovedades;
import ClasesAyuda.ResultadoBorrarTodoNovedades;
import ControlNavegacion.ControlListaNavegacion;
import Entidades.ActualUsuario;
import Entidades.TempProrrateosProy;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarArchivoPlanoProyectoInterface;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.primefaces.component.column.Column;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.export.Exporter;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DualListModel;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author user
 */
@Named(value = "controlArchivoPlanoProyecto")
@SessionScoped
public class ControlArchivoPlanoProyecto implements Serializable {

   private static Logger log = Logger.getLogger(ControlArchivoPlanoProyecto.class);

   @EJB
   AdministrarArchivoPlanoProyectoInterface administrarArchivoPlanoProyecto;
   private List<TempProrrateosProy> listTempProrrateosProy;
   private List<TempProrrateosProy> filtrarListTempProrrateosProy;
   private List<TempProrrateosProy> modificarTempProrrateosProy;
   private List<TempProrrateosProy> borrarTempProrrateosProy;
   private List<TempProrrateosProy> crearTempProrrateosProy;
   private TempProrrateosProy tempProrrateoSeleccionado;
   private TempProrrateosProy duplicarTempProrrateosProy, nuevaTempProrrateosProy;
   private TempProrrateosProy editarNovedad;
   private HashSet hs;
   private TempProrrateosProy tNovedades;
   private ActualUsuario UsuarioBD;
   private List<ErroresNovedades> listErrores;
   private ErroresNovedades erroresNovedad;
   //Otros
   private boolean aceptar;
   private UploadedFile file;
   private String nombreArchivoPlano;
   //ACTIVAR - DESACTIVAR BOTONES
   private boolean botones;
   private boolean cargue;
   //REVERSAR 
   private String documentoSoporteReversar;
   private List<String> lovdocumentosSoporteCargados;
   private List<String> filtradoDocumentosSoporteCargados;
   private String seleccionDocumentosSoporteCargado;
   private int resultado;
   //CARGAR
   private int diferenciaRegistrosN;
   //BORRAR TODO PICKLIST
   private DualListModel<String> documentosSoportes;
   private List<String> documentosEscogidos;
   private ResultadoBorrarTodoNovedades resultadoProceso;
   //ACTUALIZAR COLLECTION
   private Collection<String> elementosActualizar;
   //
   private String infoRegistroFormula, infoRegistroDocumento, infoRegistro;
   //
   private String altoTabla;
   //
   private int bandera, tipoLista;
   //
   private Column columnaCodigoEmpl, columnaEmpleado, columnaCodigoProy, columnaProyecto, columnaFechaIni, columnaFechaFin, columnaPorcentaje;
   //
   private int cualCelda;
   //
   private boolean guardado;
   //
   private DataTable tabla;
   private int k;
   private BigInteger l;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

   /**
    * Creates a new instance of ControlArchivoPlanoProyecto
    */
   public ControlArchivoPlanoProyecto() {
      tempProrrateoSeleccionado = null;
      cualCelda = -1;
      editarNovedad = new TempProrrateosProy();
      bandera = 0;
      tipoLista = 0;
      altoTabla = "140";
      tNovedades = new TempProrrateosProy();
      hs = new HashSet();
      listErrores = new ArrayList<ErroresNovedades>();
      erroresNovedad = new ErroresNovedades();
      aceptar = true;
      botones = false;
      cargue = true;
      guardado = true;
      lovdocumentosSoporteCargados = null;
      documentosSoportes = null;
      documentosEscogidos = new ArrayList<String>();
      resultadoProceso = new ResultadoBorrarTodoNovedades();
      elementosActualizar = new ArrayList<String>();
      listTempProrrateosProy = null;
      modificarTempProrrateosProy = new ArrayList<TempProrrateosProy>();
      borrarTempProrrateosProy = new ArrayList<TempProrrateosProy>();
      crearTempProrrateosProy = new ArrayList<TempProrrateosProy>();
      k = 0;
      nuevaTempProrrateosProy = new TempProrrateosProy();
      duplicarTempProrrateosProy = new TempProrrateosProy();
      mapParametros.put("paginaAnterior", paginaAnterior);
   }

   @PostConstruct
   public void inicializarAdministrador() {
      try {
         FacesContext x = FacesContext.getCurrentInstance();
         HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
         administrarArchivoPlanoProyecto.obtenerConexion(ses.getId());
      } catch (Exception e) {
         log.error("Error postconstruct CargarArchivoPlano: " + e);
         log.error("Causa: " + e.getCause());
      }
   }

   public void recibirPaginaEntrante(String pagina) {
      paginaAnterior = pagina;
   }

   public void recibirParametros(Map<String, Object> map) {
      mapParametros = map;
      paginaAnterior = (String) mapParametros.get("paginaAnterior");
   }

   public void navegar(String pag) {
      FacesContext fc = FacesContext.getCurrentInstance();
      ControlListaNavegacion controlListaNavegacion = (ControlListaNavegacion) fc.getApplication().evaluateExpressionGet(fc, "#{controlListaNavegacion}", ControlListaNavegacion.class);
      String pagActual = "archivoplanoproyecto";
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
         //           controlRastro.recibirDatosTabla(conceptoSeleccionado.getSecuencia(), "Proyectos", pagActual);
         //      } else if (pag.equals("rastrotablaH")) {
         //       ControlRastro controlRastro = (ControlRastro) fc.getApplication().evaluateExpressionGet(fc, "#{controlRastro}", ControlRastro.class);
         //     controlRastro.historicosTabla("Proyectos", pagActual);
         //   pag = "rastrotabla";
         //}
      }
      limpiarListasValor();
   }

   public void limpiarListasValor() {
      lovdocumentosSoporteCargados = null;
   }

   public void salir() {
      cancelarModificaciones();
      navegar("atras");
   }

   public void cancelarModificaciones() {
      if (bandera == 1) {
         restaurarTabla();
      }
      nombreArchivoPlano = null;
      listTempProrrateosProy = null;
      contarRegistros();
      tempProrrateoSeleccionado = null;
      cualCelda = -1;
      guardado = true;
      modificarTempProrrateosProy = new ArrayList<TempProrrateosProy>();
      borrarTempProrrateosProy = new ArrayList<TempProrrateosProy>();
      crearTempProrrateosProy = new ArrayList<TempProrrateosProy>();
      nuevaTempProrrateosProy = new TempProrrateosProy();
      duplicarTempProrrateosProy = new TempProrrateosProy();
      botones = false;
      RequestContext context = RequestContext.getCurrentInstance();
      context.update("form:FileUp");
      context.update("form:nombreArchivo");
      context.update("form:ACEPTAR");
      context.update("form:tempNovedades");
   }

   public void editarCelda() {
      if (tempProrrateoSeleccionado != null) {
         RequestContext context = RequestContext.getCurrentInstance();
         editarNovedad = tempProrrateoSeleccionado;
         if (cualCelda == 0) {//Proyecto
            context.update("formDialogos:editarProyecto");
            context.execute("PF('editarProyecto').show()");
         } else if (cualCelda == 1) {//Empleado
            context.update("formDialogos:editarEmpleado");
            context.execute("PF('editarEmpleado').show()");
         } else if (cualCelda == 2) {//Fecha inicial
            context.update("formDialogos:editarFechainicial");
            context.execute("PF('editarFechainicial').show()");
         } else if (cualCelda == 3) {//Fecha final
            context.update("formDialogos:editarFechaFinal");
            context.execute("PF('editarFechaFinal').show()");
         }
      }
   }

   public void posicionTablaNovedad() {
      int index;
      FacesContext context = FacesContext.getCurrentInstance();
      Map<String, String> map = context.getExternalContext().getRequestParameterMap();
      String name = map.get("n"); // name attribute of node
      String type = map.get("t"); // type attribute of node
      index = Integer.parseInt(type);
      cualCelda = Integer.parseInt(name);
      tempProrrateoSeleccionado = listTempProrrateosProy.get(index);
      cambiarIndice(tempProrrateoSeleccionado, cualCelda);
   }

   public void cambiarIndice(TempProrrateosProy tempausen, int celda) {
      tempProrrateoSeleccionado = tempausen;
      cualCelda = celda;
      tempProrrateoSeleccionado.getSecuencia();
   }

   public void modificarTempNovedad(TempProrrateosProy tempNovedad) {
      tempProrrateoSeleccionado = tempNovedad;
      if (!crearTempProrrateosProy.contains(tempProrrateoSeleccionado)) {
         if (modificarTempProrrateosProy.isEmpty()) {
            modificarTempProrrateosProy.add(tempProrrateoSeleccionado);
         } else if (!modificarTempProrrateosProy.contains(tempProrrateoSeleccionado)) {
            modificarTempProrrateosProy.add(tempProrrateoSeleccionado);
         }
         if (guardado) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      }
   }

   public void recordarSeleccion() {
      if (tempProrrateoSeleccionado != null) {
         FacesContext c = FacesContext.getCurrentInstance();
         tabla = (DataTable) c.getViewRoot().findComponent("form:tempNovedades");
         tabla.setSelection(tempProrrateoSeleccionado);
      }
   }

   public void modificarTempNovedad(TempProrrateosProy tempNovedad, int celda, String valor) {
      tempProrrateoSeleccionado = tempNovedad;
      cualCelda = celda;
      if (cualCelda == 0) {
         tempProrrateoSeleccionado.setCodigoEmpleado(new BigInteger(valor));
      }
      modificarTempNovedad(tempProrrateoSeleccionado);
   }

   public void cargarArchivo(FileUploadEvent event) throws IOException {
      if (event.getFile().getFileName().substring(event.getFile().getFileName().lastIndexOf(".") + 1).equalsIgnoreCase("prn")) {
         nombreArchivoPlano = event.getFile().getFileName();
         log.info("CargarArchivoPlano.cargarArchivo()");
         log.info("event.getFile().getSize() : " + event.getFile().getSize());
         log.info("event.getFile().getContentType() : " + event.getFile().getContentType());
         log.info("Arrays.toString(event.getFile().getContents()) : " + Arrays.toString(event.getFile().getContents()));
         log.info("event.getFile().getFileName() : " + event.getFile().getFileName());
         log.info("event.getFile().getInputstream() : " + event.getFile().getInputstream());

         transformarArchivo(event.getFile().getSize(), event.getFile().getInputstream(), event.getFile().getFileName());
         contarRegistros();
      } else {
         RequestContext context = RequestContext.getCurrentInstance();
         context.update("form:errorExtensionArchivo");
         context.execute("PF('errorExtensionArchivo').show()");
      }
   }

   public void transformarArchivo(long size, InputStream in, String nombreArchivo) {
      try {
         if (nombreArchivo.length() <= 30) {
//            String destino = "C:\\Prueba\\Archivos_Planos_Cargados\\" + nombreArchivo;
            String destino = administrarArchivoPlanoProyecto.consultarRuta() + nombreArchivo;
            log.info("transformarArchivo() destino : _" + destino + "_");
            OutputStream out = new FileOutputStream(new File(destino));
            int reader = 0;
            byte[] bytes = new byte[(int) size];
            while ((reader = in.read(bytes)) != -1) {
               out.write(bytes, 0, reader);
            }
            in.close();
            out.flush();
            out.close();
            leerTxt(destino, nombreArchivo);
         } else {
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("form:errorNombreArchivo");
            context.execute("PF('errorNombreArchivo').show()");
         }
      } catch (Exception e) {
         log.warn("Error transformarArchivo Controlador : " + e.toString());
      }
   }

   ///PROYECTO
//
//v_vcCodigoempleado:= substr((LINEBUF),1,15);
//v_vcPROYECTO:= substr((LINEBUF),16,15);
//v_vcfechainicial:= substr((LINEBUF),31,10);
//v_vcfechafinal:= substr((LINEBUF),41,10);
//v_vcPorcentaje:= substr((LINEBUF),51,5);
//
//
//////CEMTRO COSTO
//
//v_vcCodigoempleado:= substr((LINEBUF),1,15);
//v_vcCentrocosto:= substr((LINEBUF),16,15);
//v_vcfechainicial:= substr((LINEBUF),31,10);
//v_vcfechafinal:= substr((LINEBUF),41,10);
//v_vcPorcentaje:= substr((LINEBUF),51,5);
//v_vcPROYECTO:= substr((LINEBUF),56,15);
//v_vcSUBPorcentaje:= substr((LINEBUF),71,5);
   public void leerTxt(String locArchivo, String nombreArchivo) throws FileNotFoundException, IOException {
      log.info("Cargue.CargarArchivoPlano.leerTxt()");
      try {
         File archivo = new File(locArchivo);
         FileReader fr = new FileReader(archivo);
         BufferedReader bf = new BufferedReader(fr);
         SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
         String sCadena;
         RequestContext context = RequestContext.getCurrentInstance();
         listTempProrrateosProy.clear();
         listErrores.clear();
         while ((sCadena = bf.readLine()) != null) {
            tNovedades = new TempProrrateosProy();
            //LEER EMPLEADO
            String sEmpleado = sCadena.substring(1, 15).trim();
            if (!sEmpleado.equals("")) {
               try {
                  BigInteger codEmpleado = new BigInteger(sEmpleado);
                  tNovedades.setCodigoEmpleado(codEmpleado);
               } catch (Exception e) {
                  context.update("form:errorArchivo");
                  context.execute("PF('errorArchivo').show()");
                  break;
               }
            } else {
               tNovedades.setCodigoEmpleado(null);
            }
            //LEER TIPO AUSENTISMO
            String sProyecto = sCadena.substring(16, 30).trim();
            if (!sProyecto.equals("")) {
               try {
                  BigInteger codProyecto = new BigInteger(sProyecto);
                  tNovedades.setCodigoProyecto(codProyecto);
               } catch (Exception e) {
                  context.update("form:errorArchivo");
                  context.execute("PF('errorArchivo').show()");
                  break;
               }
            } else {
               tNovedades.setCodigoProyecto(null);
            }
            //LEER CLASE AUSENTISMO
            String fechaIni = sCadena.substring(31, 40).trim();
            if (!fechaIni.equals("")) {
               if (fechaIni.indexOf("-") > 0) {
                  fechaIni = fechaIni.replaceAll("-", "/");
               }
               try {
                  Date fechaInicial = formato.parse(fechaIni);
                  tNovedades.setFechaInicial(fechaInicial);
               } catch (Exception e) {
                  context.update("form:errorArchivo");
                  context.execute("PF('errorArchivo').show()");
                  break;
               }
            } else {
               tNovedades.setFechaInicial(null);
            }
            //LEER CAUSA AUSENTIMO
            String fechaFin = sCadena.substring(41, 50).trim();
            if (!fechaFin.equals("")) {
               if (fechaFin.indexOf("-") > 0) {
                  fechaFin = fechaFin.replaceAll("-", "/");
               }
               try {
                  Date fechaFinal = formato.parse(fechaFin);
                  tNovedades.setFechaFinal(fechaFinal);
               } catch (Exception e) {
                  context.update("form:errorArchivo");
                  context.execute("PF('errorArchivo').show()");
                  break;
               }
            } else {
               tNovedades.setFechaFinal(null);
            }
            // LEER DIAS AUSENTISMO
            String sPorcentaje = sCadena.substring(51, 55).trim();
            if (!sPorcentaje.equals("")) {
               try {
                  BigDecimal porcentaje = new BigDecimal(sPorcentaje);
                  tNovedades.setPorcentaje(porcentaje);
               } catch (Exception e) {
                  context.update("form:errorArchivo");
                  context.execute("PF('errorArchivo').show()");
                  break;
               }
            } else {
               tNovedades.setPorcentaje(null);
            }

            tNovedades.setUsuarioBD(UsuarioBD.getAlias());
            tNovedades.setEstado("N");
            //NOMBRE ARCHIVO
            tNovedades.setArchivo(nombreArchivo);
            tNovedades.setSecuencia(BigInteger.valueOf(1));
            listTempProrrateosProy.add(tNovedades);
            tNovedades = null;
         }
         administrarArchivoPlanoProyecto.borrarRegistrosTempProrrateosProy(UsuarioBD.getAlias());
         insertarNovedadTempProrrateosProy();
         listTempProrrateosProy = null;
         listTempProrrateosProy = administrarArchivoPlanoProyecto.obtenerTempProrrateosProy(UsuarioBD.getAlias());
         if (listTempProrrateosProy != null) {
            botones = true;
            cargue = false;
            elementosActualizar.add("form:tempNovedades");
            elementosActualizar.add("form:FileUp");
            elementosActualizar.add("form:nombreArchivo");
            elementosActualizar.add("form:cargar");
            context.update(elementosActualizar);
            elementosActualizar.clear();
         }
      } catch (Exception e) {
         log.warn("Excepcion: (leerTxt) " + e);
      }
   }

   public void activarCtrlF11() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 0) {
         columnaCodigoEmpl = (Column) c.getViewRoot().findComponent("form:tempNovedades:columnaCodigoEmpl");
         columnaCodigoEmpl.setFilterStyle("width: 85% !important");
         columnaEmpleado = (Column) c.getViewRoot().findComponent("form:tempNovedades:columnaEmpleado");
         columnaEmpleado.setFilterStyle("width: 85% !important");
         columnaProyecto = (Column) c.getViewRoot().findComponent("form:tempNovedades:columnaProyecto");
         columnaProyecto.setFilterStyle("width: 85% !important");
         columnaCodigoProy = (Column) c.getViewRoot().findComponent("form:tempNovedades:columnaCodigoProy");
         columnaCodigoProy.setFilterStyle("width: 85% !important");
         columnaFechaIni = (Column) c.getViewRoot().findComponent("form:tempNovedades:columnaFechaIni");
         columnaFechaIni.setFilterStyle("width: 85% !important");
         columnaFechaFin = (Column) c.getViewRoot().findComponent("form:tempNovedades:columnaFechaFin");
         columnaFechaFin.setFilterStyle("width: 85% !important");
         columnaPorcentaje = (Column) c.getViewRoot().findComponent("form:tempNovedades:columnaPorcentaje");
         columnaPorcentaje.setFilterStyle("width: 85% !important");
         altoTabla = "120";
         RequestContext.getCurrentInstance().update("form:tempNovedades");
         bandera = 1;
      } else if (bandera == 1) {
         restaurarTabla();
      }
      cualCelda = -1;
   }

   public void revisarNovedad(BigInteger secnovedad) {
      log.info("Cargue.CargarArchivoPlano.revisarNovedad() secnovedad : " + secnovedad);
      erroresNovedad = null;
      for (int i = 0; i < listErrores.size(); i++) {
         BigInteger secuencia = listErrores.get(i).getSecNovedad();
         if (secuencia.compareTo(secnovedad) == 0) {
            erroresNovedad = listErrores.get(i);
            i = listErrores.size();
         }
      }
      if (erroresNovedad != null) {
         if (erroresNovedad.getNumeroErrores() != 0) {
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("form:erroresNovedad");
            context.execute("PF('erroresNovedad').show()");
         }
      }
   }

   public void borrar() {
      if (tempProrrateoSeleccionado != null) {
         if (!modificarTempProrrateosProy.isEmpty() && modificarTempProrrateosProy.contains(tempProrrateoSeleccionado)) {
            modificarTempProrrateosProy.remove(tempProrrateoSeleccionado);
            borrarTempProrrateosProy.add(tempProrrateoSeleccionado);
         } else if (!crearTempProrrateosProy.isEmpty() && crearTempProrrateosProy.contains(tempProrrateoSeleccionado)) {
            crearTempProrrateosProy.remove(tempProrrateoSeleccionado);
         } else {
            borrarTempProrrateosProy.add(tempProrrateoSeleccionado);
         }
         listTempProrrateosProy.remove(tempProrrateoSeleccionado);
         if (tipoLista == 1) {
            filtrarListTempProrrateosProy.remove(tempProrrateoSeleccionado);
         }
         contarRegistros();
         RequestContext.getCurrentInstance().update("form:tempNovedades");
         tempProrrateoSeleccionado = null;
         if (guardado) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void duplicar() {
      if (tempProrrateoSeleccionado == null) {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      } else {
         duplicarTempProrrateosProy = new TempProrrateosProy();
         k++;
         l = BigInteger.valueOf(k);
         duplicarTempProrrateosProy.setSecuencia(l);
         duplicarTempProrrateosProy.setEmpleado(tempProrrateoSeleccionado.getEmpleado());
         duplicarTempProrrateosProy.setArchivo(tempProrrateoSeleccionado.getArchivo());
         duplicarTempProrrateosProy.setCausaRechazo(tempProrrateoSeleccionado.getCausaRechazo());
         duplicarTempProrrateosProy.setCodigoEmpleado(tempProrrateoSeleccionado.getCodigoEmpleado());
         duplicarTempProrrateosProy.setCodigoEmpleadoCargue(tempProrrateoSeleccionado.getCodigoEmpleadoCargue());
         duplicarTempProrrateosProy.setCodigoProyecto(tempProrrateoSeleccionado.getCodigoProyecto());
         duplicarTempProrrateosProy.setEstado(tempProrrateoSeleccionado.getEstado());
         duplicarTempProrrateosProy.setFechaFinal(tempProrrateoSeleccionado.getFechaFinal());
         duplicarTempProrrateosProy.setFechaInicial(tempProrrateoSeleccionado.getFechaInicial());
         duplicarTempProrrateosProy.setFechaSistema(tempProrrateoSeleccionado.getFechaSistema());
         duplicarTempProrrateosProy.setNombreEmpleado(tempProrrateoSeleccionado.getNombreEmpleado());
         duplicarTempProrrateosProy.setNombreProyecto(tempProrrateoSeleccionado.getNombreProyecto());
         duplicarTempProrrateosProy.setPorcentaje(tempProrrateoSeleccionado.getPorcentaje());
         duplicarTempProrrateosProy.setProyecto(tempProrrateoSeleccionado.getProyecto());
         duplicarTempProrrateosProy.setTerminal(tempProrrateoSeleccionado.getTerminal());
         duplicarTempProrrateosProy.setUsuarioBD(tempProrrateoSeleccionado.getUsuarioBD());
         duplicarTempProrrateosProy.setVigLocalizacion(tempProrrateoSeleccionado.getVigLocalizacion());

         RequestContext.getCurrentInstance().update("formDialogos:duplicarTempNDialogo");
         RequestContext.getCurrentInstance().execute("PF('duplicarTempNDialogo').show()");
      }
   }

   public void confirmarDuplicar() {
      listTempProrrateosProy.add(duplicarTempProrrateosProy);
      crearTempProrrateosProy.add(duplicarTempProrrateosProy);
      tempProrrateoSeleccionado = listTempProrrateosProy.get(listTempProrrateosProy.indexOf(duplicarTempProrrateosProy));

      RequestContext.getCurrentInstance().update("form:tempNovedades");
      contarRegistros();
      if (guardado) {
         guardado = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }
      if (bandera == 1) {
         restaurarTabla();
      }
      duplicarTempProrrateosProy = new TempProrrateosProy();

      RequestContext.getCurrentInstance().update("formDialogos:duplicarTempNDialogo");
      RequestContext.getCurrentInstance().execute("PF('duplicarTempNDialogo').hide()");
   }

   public void agregarNuevaTempAusentismo() {
      k++;
      l = BigInteger.valueOf(k);
      nuevaTempProrrateosProy.setSecuencia(l);
      listTempProrrateosProy.add(nuevaTempProrrateosProy);
      crearTempProrrateosProy.add(nuevaTempProrrateosProy);
      tempProrrateoSeleccionado = listTempProrrateosProy.get(listTempProrrateosProy.indexOf(nuevaTempProrrateosProy));

      RequestContext.getCurrentInstance().update("form:tempNovedades");
      contarRegistros();
      if (guardado) {
         guardado = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }
      if (bandera == 1) {
         restaurarTabla();
      }
      nuevaTempProrrateosProy = new TempProrrateosProy();

      RequestContext.getCurrentInstance().update("formDialogos:nuevaTempNDialogo");
      RequestContext.getCurrentInstance().execute("PF('nuevaTempNDialogo').hide()");
   }

   public void limpiarNuevo() {
      nuevaTempProrrateosProy = new TempProrrateosProy();
      duplicarTempProrrateosProy = new TempProrrateosProy();
      RequestContext.getCurrentInstance().update("formDialogos:nuevaTempNDialogo");
      RequestContext.getCurrentInstance().update("formDialogos:duplicarTempNDialogo");
   }

   public void insertarNovedadTempProrrateosProy() {
      if (!listTempProrrateosProy.isEmpty()) {
         administrarArchivoPlanoProyecto.crear(listTempProrrateosProy);
      }
   }

   public void guardar() {
      if (guardado == false) {
         guardado = true;
         if (!modificarTempProrrateosProy.isEmpty()) {
            for (int i = 0; i < modificarTempProrrateosProy.size(); i++) {
               administrarArchivoPlanoProyecto.editar(modificarTempProrrateosProy.get(i));
            }
         }
         if (!borrarTempProrrateosProy.isEmpty()) {
            for (int i = 0; i < borrarTempProrrateosProy.size(); i++) {
               administrarArchivoPlanoProyecto.borrar(borrarTempProrrateosProy.get(i));
            }
         }
         if (!crearTempProrrateosProy.isEmpty()) {
            administrarArchivoPlanoProyecto.crear(crearTempProrrateosProy);
         }
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      } else {
         FacesMessage msg = new FacesMessage("Información", "NO HAY CAMBIOS QUE GUARDAR");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }
   }

   public void botonListaValores() {
      RequestContext context = RequestContext.getCurrentInstance();
      context.update("formDialogos:formulasDialogo");
      context.execute("PF('formulasDialogo').show()");
   }

   public void restaurarTabla() {
      FacesContext c = FacesContext.getCurrentInstance();
      columnaCodigoEmpl = (Column) c.getViewRoot().findComponent("form:tempNovedades:columnaCodigoEmpl");
      columnaCodigoEmpl.setFilterStyle("display: none; visibility: hidden;");
      columnaEmpleado = (Column) c.getViewRoot().findComponent("form:tempNovedades:columnaEmpleado");
      columnaEmpleado.setFilterStyle("display: none; visibility: hidden;");
      columnaCodigoProy = (Column) c.getViewRoot().findComponent("form:tempNovedades:columnaCodigoProy");
      columnaCodigoProy.setFilterStyle("display: none; visibility: hidden;");
      columnaProyecto = (Column) c.getViewRoot().findComponent("form:tempNovedades:columnaProyecto");
      columnaProyecto.setFilterStyle("display: none; visibility: hidden;");
      columnaFechaIni = (Column) c.getViewRoot().findComponent("form:tempNovedades:columnaFechaIni");
      columnaFechaIni.setFilterStyle("display: none; visibility: hidden;");
      columnaFechaFin = (Column) c.getViewRoot().findComponent("form:tempNovedades:columnaFechaFin");
      columnaFechaFin.setFilterStyle("display: none; visibility: hidden;");
      columnaPorcentaje = (Column) c.getViewRoot().findComponent("form:tempNovedades:columnaPorcentaje");
      columnaPorcentaje.setFilterStyle("display: none; visibility: hidden;");
      altoTabla = "140";
      RequestContext.getCurrentInstance().update("form:tempNovedades");
      bandera = 0;
      filtrarListTempProrrateosProy = null;
      tipoLista = 0;
   }

   //AUTOCOMPLETAR DOCUMENTO SOPORTE
   public void autocompletarDocumentoSoporte() {
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      lovdocumentosSoporteCargados = null;
      getLovdocumentosSoporteCargados();
      hs.addAll(lovdocumentosSoporteCargados);
      lovdocumentosSoporteCargados.clear();
      lovdocumentosSoporteCargados.addAll(hs);
      for (int i = 0; i < lovdocumentosSoporteCargados.size(); i++) {
         if (lovdocumentosSoporteCargados.get(i).startsWith(documentoSoporteReversar.toUpperCase())) {
            indiceUnicoElemento = i;
            coincidencias++;
         }
      }

      if (coincidencias == 1) {
         documentoSoporteReversar = lovdocumentosSoporteCargados.get(indiceUnicoElemento);
         lovdocumentosSoporteCargados = null;
         getLovdocumentosSoporteCargados();
      } else {
         documentoSoporteReversar = null;
         context.update("formDialogos:documentoSoporteDialogo");
         context.execute("PF('documentoSoporteDialogo').show()");
      }
      context.update("form:documentoR");
   }

   //LOV DOCUMENTOS SOPORTE
   public void seleccionarDocumentoSoporte() {
      filtradoDocumentosSoporteCargados = null;
      lovdocumentosSoporteCargados = null;
      aceptar = true;
      documentoSoporteReversar = seleccionDocumentosSoporteCargado;
      seleccionDocumentosSoporteCargado = null;
      RequestContext context = RequestContext.getCurrentInstance();
      context.update("form:documentoR");
      context.reset("formDialogos:lovDocumentoSoporte:globalFilter");
      context.execute("PF('lovDocumentoSoporte').clearFilters()");
      context.execute("PF('documentoSoporteDialogo').hide()");
   }

   public void cancelarSeleccionDocumentoSoporte() {
      filtradoDocumentosSoporteCargados = null;
      lovdocumentosSoporteCargados = null;
      seleccionDocumentosSoporteCargado = null;
      aceptar = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formDialogos:lovDocumentoSoporte:globalFilter");
      context.execute("PF('lovDocumentoSoporte').clearFilters()");
      context.execute("PF('documentoSoporteDialogo').hide()");
   }

   public void llamarDialogoDocumentoSoporte() {
      RequestContext context = RequestContext.getCurrentInstance();
      lovdocumentosSoporteCargados = null;
      context.update("formDialogos:lovDocumentoSoporte");
      context.execute("PF('documentoSoporteDialogo').show()");
   }

   //CARGUE NOVEDADES
   public void cargarNovedades() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (!listTempProrrateosProy.isEmpty() || listTempProrrateosProy != null) {
         int pasa = 0;
         for (int i = 0; i < listErrores.size(); i++) {
            if (listErrores.get(i).getNumeroErrores() != 0) {
               pasa++;
            }
         }
         if (pasa == 0) {
            administrarArchivoPlanoProyecto.cargarTempProrrateosProy();
            int registrosNAntes = listTempProrrateosProy.size();
            listTempProrrateosProy = administrarArchivoPlanoProyecto.obtenerTempProrrateosProy(UsuarioBD.getAlias());
            int registrosNDespues = listTempProrrateosProy.size();
            diferenciaRegistrosN = registrosNAntes - registrosNDespues;
            context.update("form:tempNovedades");
            if (diferenciaRegistrosN == registrosNAntes) {
               context.update("form:novedadesCargadas");
               context.execute("PF('novedadesCargadas').show()");
            }
            listErrores.clear();
            erroresNovedad = null;
            cargue = true;
            nombreArchivoPlano = null;
            documentosSoportes = null;
            context.update("form:pickListDocumentosSoporte");
            botones = false;
            context.update("form:FileUp");
            context.update("form:nombreArchivo");
            context.update("form:formula");
            context.update("form:cargar");
         }
      }
   }

   public void borrarRegistrosNoCargados() {
      administrarArchivoPlanoProyecto.borrarRegistrosTempProrrateosProy(UsuarioBD.getAlias());
      listTempProrrateosProy = administrarArchivoPlanoProyecto.obtenerTempProrrateosProy(UsuarioBD.getAlias());
      contarRegistros();
      nombreArchivoPlano = null;
      botones = false;
      cargue = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.update("form:FileUp");
      context.update("form:formula");
      context.update("form:usoFormulaC");
      context.update("form:cargar");
      context.update("form:tempNovedades");
      context.update("form:nombreArchivo");
      listErrores.clear();
      erroresNovedad = null;
   }

   public void reversar() {
      RequestContext context = RequestContext.getCurrentInstance();
      resultado = administrarArchivoPlanoProyecto.reversarTempProrrateosProy(UsuarioBD, documentoSoporteReversar);
      documentoSoporteReversar = null;
      context.update("form:documentoR");
      context.execute("PF('reversarDialogo').hide()");
      context.execute("PF('confirmarReversar').hide()");
      if (resultado > 0) {
         context.update("form:reversarExito");
         context.execute("PF('reversarExito').show()");
      } else {
         context.update("form:reversarFallo");
         context.execute("PF('reversarFallo').show()");
      }
   }

   public void cancelarReversar() {
      RequestContext context = RequestContext.getCurrentInstance();
      documentoSoporteReversar = null;
      context.update("form:documentoR");
      context.execute("PF('reversarDialogo').hide()");
   }

   public void confirmarReversar() {
      RequestContext context = RequestContext.getCurrentInstance();
      lovdocumentosSoporteCargados = administrarArchivoPlanoProyecto.obtenerDocumentosSoporteCargados();
      hs.addAll(lovdocumentosSoporteCargados);
      lovdocumentosSoporteCargados.clear();
      lovdocumentosSoporteCargados.addAll(hs);
      hs.clear();
      int existeDocumento = 0;
      for (int i = 0; i < lovdocumentosSoporteCargados.size(); i++) {
         if (lovdocumentosSoporteCargados.get(i).equals(documentoSoporteReversar)) {
            existeDocumento++;
         }
      }
      if (existeDocumento == 1) {
         context.update("form:confirmarReversar");
         context.execute("PF('confirmarReversar').show()");
      }
   }

   public void confirmarBorrarTodo() {
      if (!documentosSoportes.getTarget().isEmpty()) {
         RequestContext context = RequestContext.getCurrentInstance();
//         resultadoProceso = administrarArchivoPlanoProyecto.BorrarTodo(UsuarioBD, documentosSoportes.getTarget());
         log.info("NO ESTA BORRANDO TODO");
         documentosSoportes = null;
         context.execute("PF('borrarTodoDialogo').hide()");
         context.update("form:pickListDocumentosSoporte");
         if (resultadoProceso.getDocumentosNoBorrados() == null) {
            context.update("form:borrarTodoExito");
            context.execute("PF('borrarTodoExito').show()");
         } else {
            context.update("form:erroresBorrarTodo");
            context.execute("PF('erroresBorrarTodo').show()");
         }
      }
   }

   public void cancelarBorrarTodo() {
      RequestContext context = RequestContext.getCurrentInstance();
      documentosSoportes = null;
      context.execute("PF('borrarTodoDialogo').hide()");
      context.update("form:pickListDocumentosSoporte");
   }

   public void activarAceptar() {
      aceptar = false;
   }

   public void exportPDF() throws IOException {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formExportar:exportarTempProrrateosProy");
      FacesContext context = c;
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "Novedades_PDF", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void exportXLS() throws IOException {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formExportar:exportarTempProrrateosProy");
      FacesContext context = c;
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "Novedades_XLS", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void eventoFiltrar() {
      if (tipoLista == 0) {
         tipoLista = 1;
      }
      contarRegistros();
      tempProrrateoSeleccionado = null;
   }

   public void contarRegistrosFormulas() {
      RequestContext.getCurrentInstance().update("formDialogos:infoRegistroFormula");
   }

   public void contarRegistrosDocumentos() {
      RequestContext.getCurrentInstance().update("formDialogos:infoRegistroDocumento");
   }

   public void contarRegistros() {
      RequestContext.getCurrentInstance().update("form:infoRegistro");
   }

   //GETTER AND SETTER
   public List<TempProrrateosProy> getListTempProrrateosProy() {
      if (UsuarioBD == null) {
         UsuarioBD = administrarArchivoPlanoProyecto.actualUsuario();
      }
      if (UsuarioBD.getAlias() != null && listTempProrrateosProy == null) {
         listTempProrrateosProy = administrarArchivoPlanoProyecto.obtenerTempProrrateosProy(UsuarioBD.getAlias());
      }
      return listTempProrrateosProy;
   }

   public void setListTempProrrateosProy(List<TempProrrateosProy> listTempProrrateosProy) {
      this.listTempProrrateosProy = listTempProrrateosProy;
   }

   public ErroresNovedades getErroresNovedad() {
      return erroresNovedad;
   }

   public boolean isAceptar() {
      return aceptar;
   }

   public UploadedFile getFile() {
      return file;
   }

   public void setFile(UploadedFile file) {
      this.file = file;
   }

   public String getNombreArchivoPlano() {
      return nombreArchivoPlano;
   }

   public void setNombreArchivoPlano(String nombreArchivoPlano) {
      this.nombreArchivoPlano = nombreArchivoPlano;
   }

   public boolean isBotones() {
      return botones;
   }

   public boolean isCargue() {
      return cargue;
   }

   public String getDocumentoSoporteReversar() {
      return documentoSoporteReversar;
   }

   public void setDocumentoSoporteReversar(String documentoSoporteReversar) {
      this.documentoSoporteReversar = documentoSoporteReversar;
   }

   public List<String> getLovdocumentosSoporteCargados() {
      lovdocumentosSoporteCargados = administrarArchivoPlanoProyecto.obtenerDocumentosSoporteCargados();
      if (lovdocumentosSoporteCargados != null) {
         hs.addAll(lovdocumentosSoporteCargados);
         lovdocumentosSoporteCargados.clear();
         lovdocumentosSoporteCargados.addAll(hs);
         hs.clear();
      }
      return lovdocumentosSoporteCargados;
   }

   public void setLovdocumentosSoporteCargados(List<String> lovdocumentosSoporteCargados) {
      this.lovdocumentosSoporteCargados = lovdocumentosSoporteCargados;
   }

   public List<String> getFiltradoDocumentosSoporteCargados() {
      return filtradoDocumentosSoporteCargados;
   }

   public void setFiltradoDocumentosSoporteCargados(List<String> filtradoDocumentosSoporteCargados) {
      this.filtradoDocumentosSoporteCargados = filtradoDocumentosSoporteCargados;
   }

   public String getSeleccionDocumentosSoporteCargado() {
      return seleccionDocumentosSoporteCargado;
   }

   public void setSeleccionDocumentosSoporteCargado(String seleccionDocumentosSoporteCargado) {
      this.seleccionDocumentosSoporteCargado = seleccionDocumentosSoporteCargado;
   }

   public int getDiferenciaRegistrosN() {
      return diferenciaRegistrosN;
   }

   public int getResultado() {
      return resultado;
   }

   public DualListModel<String> getDocumentosSoportes() {
      lovdocumentosSoporteCargados = null;
      getLovdocumentosSoporteCargados();
      documentosSoportes = new DualListModel<String>(lovdocumentosSoporteCargados, documentosEscogidos);
      return documentosSoportes;
   }

   public void setDocumentosSoportes(DualListModel<String> documentosSoportes) {
      this.documentosSoportes = documentosSoportes;
   }

   public ResultadoBorrarTodoNovedades getResultadoProceso() {
      return resultadoProceso;
   }

   public void setResultadoProceso(ResultadoBorrarTodoNovedades resultadoProceso) {
      this.resultadoProceso = resultadoProceso;
   }

   public List<TempProrrateosProy> getFiltrarListTempProrrateosProy() {
      return filtrarListTempProrrateosProy;
   }

   public void setFiltrarListTempProrrateosProy(List<TempProrrateosProy> filtrarListTempProrrateosProy) {
      this.filtrarListTempProrrateosProy = filtrarListTempProrrateosProy;
   }

   public TempProrrateosProy getTempProrrateoSeleccionado() {
      //getListTempProrrateosProy();
      return tempProrrateoSeleccionado;
   }

   public void setTempProrrateoSeleccionado(TempProrrateosProy tempProrrateoSeleccionado) {
      this.tempProrrateoSeleccionado = tempProrrateoSeleccionado;
   }

   public String getInfoRegistroFormula() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formDialogos:lovFormulas");
      infoRegistroFormula = String.valueOf(tabla.getRowCount());
      return infoRegistroFormula;
   }

   public void setInfoRegistroFormula(String infoRegistroFormula) {
      this.infoRegistroFormula = infoRegistroFormula;
   }

   public String getInfoRegistroDocumento() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formDialogos:lovDocumentoSoporte");
      infoRegistroDocumento = String.valueOf(tabla.getRowCount());
      return infoRegistroDocumento;
   }

   public void setInfoRegistroDocumento(String infoRegistroDocumento) {
      this.infoRegistroDocumento = infoRegistroDocumento;
   }

   public String getInfoRegistro() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:tempNovedades");
      infoRegistro = String.valueOf(tabla.getRowCount());
      return infoRegistro;
   }

   public void setInfoRegistro(String infoRegistro) {
      this.infoRegistro = infoRegistro;
   }

   public String getAltoTabla() {
      return altoTabla;
   }

   public void setAltoTabla(String altoTabla) {
      this.altoTabla = altoTabla;
   }

   public TempProrrateosProy getEditarNovedad() {
      return editarNovedad;
   }

   public void setEditarNovedad(TempProrrateosProy editarNovedad) {
      this.editarNovedad = editarNovedad;
   }

   public boolean isGuardado() {
      return guardado;
   }

   public void setGuardado(boolean guardado) {
      this.guardado = guardado;
   }

   public TempProrrateosProy getDuplicarTempProrrateosProy() {
      return duplicarTempProrrateosProy;
   }

   public void setDuplicarTempProrrateosProy(TempProrrateosProy duplicarTempProrrateosProy) {
      this.duplicarTempProrrateosProy = duplicarTempProrrateosProy;
   }

   public TempProrrateosProy getNuevaTempProrrateosProy() {
      return nuevaTempProrrateosProy;
   }

   public void setNuevaTempProrrateosProy(TempProrrateosProy nuevaTempProrrateosProy) {
      this.nuevaTempProrrateosProy = nuevaTempProrrateosProy;
   }
}
