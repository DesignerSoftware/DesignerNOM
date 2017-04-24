package Controlador;

import ClasesAyuda.ErroresNovedades;
import ClasesAyuda.ResultadoBorrarTodoNovedades;
import ControlNavegacion.ControlListaNavegacion;
import Entidades.ActualUsuario;
import Entidades.Conceptos;
import Entidades.Empleados;
import Entidades.Formulas;
import Entidades.TempSoAusentismos;
import Entidades.VWActualesReformasLaborales;
import Entidades.VWActualesTiposContratos;
import Entidades.VWActualesTiposTrabajadores;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarCargueArchivosInterface;
import InterfaceAdministrar.AdministrarTempSoAusentismosInterface;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.UnknownHostException;
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
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.primefaces.component.column.Column;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.export.Exporter;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DualListModel;
import org.primefaces.model.UploadedFile;

@Named(value = "controlTempSoAusentismos")
@SessionScoped
public class ControlTempSoAusentismos implements Serializable {

    @EJB
    AdministrarTempSoAusentismosInterface administrarCargueArchivos;
    private List<TempSoAusentismos> listTempSoAusentismos;
    private List<TempSoAusentismos> filtrarListTempSoAusentismos;
    private List<TempSoAusentismos> modificarTempSoAusentismos;
    private List<TempSoAusentismos> borrarTempSoAusentismos;
    private List<TempSoAusentismos> crearTempSoAusentismos;
    private TempSoAusentismos tempSoAusentismoSeleccionada;
    private TempSoAusentismos duplicarTempSoAusentismos, nuevaTempSoAusentismos;
    private TempSoAusentismos editarNovedad;
    private HashSet hs;
    private TempSoAusentismos tNovedades;
    private ActualUsuario UsuarioBD;
    private List<ErroresNovedades> listErrores;
    private ErroresNovedades erroresNovedad;
    //Otros
    private boolean aceptar;
    private UploadedFile file;
    private String nombreArchivoPlano;
    //SUBTOTAL CONCEPTOS MANUALES
    private BigDecimal subTotal;
    //ACTIVAR - DESACTIVAR BOTONES
    private boolean botones;
    private boolean cargue;
    //REVERSAR 
    private String documentoSoporteReversar;
    private List<String> documentosSoporteCargados;
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
    private Column columnaEmpleado, columnaTipoAusentismo, columnaClaseAusentismo, columnaCausaAusentismo, columnaDias, columnaFechaIni,
            columnaDocumentoSoporte, columnaFechaFin, columnaFechaIniPago, columnaFechaFinPago, columnaFechaExp, columnaPorcentaje, columnaBaseL, columnaFormaL;
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

    public ControlTempSoAusentismos() {
        tempSoAusentismoSeleccionada = null;
        cualCelda = -1;
        editarNovedad = new TempSoAusentismos();
        bandera = 0;
        tipoLista = 0;
        altoTabla = "130";
        tNovedades = new TempSoAusentismos();
        hs = new HashSet();
        listErrores = new ArrayList<ErroresNovedades>();
        erroresNovedad = new ErroresNovedades();
        aceptar = true;
        subTotal = new BigDecimal(0);
        botones = false;
        cargue = true;
        guardado = true;
        documentosSoporteCargados = null;
        documentosSoportes = null;
        documentosEscogidos = new ArrayList<String>();
        resultadoProceso = new ResultadoBorrarTodoNovedades();
        elementosActualizar = new ArrayList<String>();
        listTempSoAusentismos = null;
        modificarTempSoAusentismos = new ArrayList<TempSoAusentismos>();
        borrarTempSoAusentismos = new ArrayList<TempSoAusentismos>();
        crearTempSoAusentismos = new ArrayList<TempSoAusentismos>();
        k = 0;
        nuevaTempSoAusentismos = new TempSoAusentismos();
        duplicarTempSoAusentismos = new TempSoAusentismos();
        mapParametros.put("paginaAnterior", paginaAnterior);
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarCargueArchivos.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct CargarArchivoPlano: " + e);
            System.out.println("Causa: " + e.getCause());
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
        /*if (pag.equals("atras")) {
            pag = paginaAnterior;
            paginaAnterior = "nominaf";
            controlListaNavegacion.quitarPagina(pagActual);
         
        } else {
            */
String pagActual = "tempsoausentismos";
            
            
            


            
            
            
            
            
            
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
        
    }

    public String retornarPagina() {
        return paginaAnterior;
    }

    public void cancelarModificaciones() {
        if (bandera == 1) {
            restaurarTabla();
        }
        nombreArchivoPlano = null;
        listTempSoAusentismos = null;
        contarRegistros();
        tempSoAusentismoSeleccionada = null;
        cualCelda = -1;
        subTotal = new BigDecimal("0");
        guardado = true;
        //
        modificarTempSoAusentismos = new ArrayList<TempSoAusentismos>();
        borrarTempSoAusentismos = new ArrayList<TempSoAusentismos>();
        crearTempSoAusentismos = new ArrayList<TempSoAusentismos>();
        //
        nuevaTempSoAusentismos = new TempSoAusentismos();
        duplicarTempSoAusentismos = new TempSoAusentismos();
        botones = false;
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form:FileUp");
        context.update("form:nombreArchivo");
        context.update("form:subtotal");
        context.update("form:ACEPTAR");
        context.update("form:tempNovedades");
    }

    public void editarCelda() {
        if (tempSoAusentismoSeleccionada != null) {
            RequestContext context = RequestContext.getCurrentInstance();
            editarNovedad = tempSoAusentismoSeleccionada;
            if (cualCelda == 0) {//Concepto
                context.update("formDialogos:editarConcepto");
                context.execute("PF('editarConcepto').show()");
            } else if (cualCelda == 1) {//Empleado
                context.update("formDialogos:editarEmpleado");
                context.execute("PF('editarEmpleado').show()");
            } else if (cualCelda == 2) {//Fecha inicial
                context.update("formDialogos:editarFechainicial");
                context.execute("PF('editarFechainicial').show()");
            } else if (cualCelda == 3) {//Fecha final
                context.update("formDialogos:editarFechaFinal");
                context.execute("PF('editarFechaFinal').show()");
            } else if (cualCelda == 4) {//fecha reporte
                context.update("formDialogos:editarFechaReporte");
                context.execute("PF('editarFechaReporte').show()");
            } else if (cualCelda == 5) {//documento soporte
                context.update("formDialogos:editarDocumentoS");
                context.execute("PF('editarDocumentoS').show()");
            } else if (cualCelda == 6) {//valor
                context.update("formDialogos:editarValor");
                context.execute("PF('editarValor').show()");
            } else if (cualCelda == 7) {//periodicidad
                context.update("formDialogos:editarPeriodicidad");
                context.execute("PF('editarPeriodicidad').show()");
            } else if (cualCelda == 8) {//tercero
                context.update("formDialogos:editarTercero");
                context.execute("PF('editarTercero').show()");
            } else if (cualCelda == 9) {//saldo
                context.update("formDialogos:editarSaldo");
                context.execute("PF('editarSaldo').show()");
            } else if (cualCelda == 10) {//Unidad Fraccion
                context.update("formDialogos:editarUEntera");
                context.execute("PF('editarUEntera').show()");
            } else if (cualCelda == 11) {//Unidad Fraccion
                context.update("formDialogos:editarUFraccion");
                context.execute("PF('editarUFraccion').show()");
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
        tempSoAusentismoSeleccionada = listTempSoAusentismos.get(index);
        cambiarIndice(tempSoAusentismoSeleccionada, cualCelda);
    }

    public void cambiarIndice(TempSoAusentismos tempausen, int celda) {
        tempSoAusentismoSeleccionada = tempausen;
        cualCelda = celda;
        tempSoAusentismoSeleccionada.getSecuencia();
        if (cualCelda == 0) {
            tempSoAusentismoSeleccionada.getEmpleado();
        } else if (cualCelda == 1) {
            tempSoAusentismoSeleccionada.getTipo();
        } else if (cualCelda == 2) {
            tempSoAusentismoSeleccionada.getClase();
        } else if (cualCelda == 3) {
            tempSoAusentismoSeleccionada.getCausa();
        } else if (cualCelda == 4) {
            tempSoAusentismoSeleccionada.getDias();
        } else if (cualCelda == 5) {
            tempSoAusentismoSeleccionada.getFecha();
        } else if (cualCelda == 6) {
            tempSoAusentismoSeleccionada.getFechafinaus();
        } else if (cualCelda == 7) {
            tempSoAusentismoSeleccionada.getFechaexpedicion();
        } else if (cualCelda == 8) {
            tempSoAusentismoSeleccionada.getFechainipago();
        } else if (cualCelda == 9) {
            tempSoAusentismoSeleccionada.getFechafinpago();
        } else if (cualCelda == 10) {
            tempSoAusentismoSeleccionada.getPorcentajeindividual();
        } else if (cualCelda == 11) {
            tempSoAusentismoSeleccionada.getBaseliquidacion();
        } else if (cualCelda == 12) {
            tempSoAusentismoSeleccionada.getFormaliquidacion();
        } else if (cualCelda == 13) {
            tempSoAusentismoSeleccionada.getDocumentosoporte();
        }
    }

    public void modificarTempNovedad(TempSoAusentismos tempNovedad) {
        tempSoAusentismoSeleccionada = tempNovedad;
        if (!crearTempSoAusentismos.contains(tempSoAusentismoSeleccionada)) {
            if (modificarTempSoAusentismos.isEmpty()) {
                modificarTempSoAusentismos.add(tempSoAusentismoSeleccionada);
            } else if (!modificarTempSoAusentismos.contains(tempSoAusentismoSeleccionada)) {
                modificarTempSoAusentismos.add(tempSoAusentismoSeleccionada);
            }
            if (guardado) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        }
    }

    public void recordarSeleccion() {
        if (tempSoAusentismoSeleccionada != null) {
            FacesContext c = FacesContext.getCurrentInstance();
            tabla = (DataTable) c.getViewRoot().findComponent("form:tempNovedades");
            tabla.setSelection(tempSoAusentismoSeleccionada);
        }
    }

    public void modificarTempNovedad(TempSoAusentismos tempNovedad, int celda, String valor) {
        System.out.println("Cargue.CargarArchivoPlano.modificarTempNovedad() tempNovedad : " + tempNovedad + ",  valor : " + valor);
        tempSoAusentismoSeleccionada = tempNovedad;
        cualCelda = celda;
        if (cualCelda == 0) {
            tempSoAusentismoSeleccionada.setEmpleado(new BigInteger(valor));
        }
        modificarTempNovedad(tempSoAusentismoSeleccionada);
    }

    public void cargarArchivo(FileUploadEvent event) throws IOException {
        if (event.getFile().getFileName().substring(event.getFile().getFileName().lastIndexOf(".") + 1).equalsIgnoreCase("prn")) {
            nombreArchivoPlano = event.getFile().getFileName();
            System.out.println("CargarArchivoPlano.cargarArchivo()");
            System.out.println("event.getFile().getSize() : " + event.getFile().getSize());
            System.out.println("event.getFile().getContentType() : " + event.getFile().getContentType());
            System.out.println("Arrays.toString(event.getFile().getContents()) : " + Arrays.toString(event.getFile().getContents()));
            System.out.println("event.getFile().getFileName() : " + event.getFile().getFileName());
            System.out.println("event.getFile().getInputstream() : " + event.getFile().getInputstream());

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
                String destino = administrarCargueArchivos.consultarRuta() + nombreArchivo;
                System.out.println("transformarArchivo() destino : _" + destino + "_");
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
            System.out.println("Error transformarArchivo Controlador : " + e.toString());
        }
    }

    public void leerTxt(String locArchivo, String nombreArchivo) throws FileNotFoundException, IOException {
        System.out.println("Cargue.CargarArchivoPlano.leerTxt()");
        try {
            File archivo = new File(locArchivo);
            FileReader fr = new FileReader(archivo);
            BufferedReader bf = new BufferedReader(fr);
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
            String sCadena;
            RequestContext context = RequestContext.getCurrentInstance();
            listTempSoAusentismos.clear();
            listErrores.clear();
            while ((sCadena = bf.readLine()) != null) {
                tNovedades = new TempSoAusentismos();
                //LEER EMPLEADO
                String sEmpleado = sCadena.substring(1, 16).trim();
                if (!sEmpleado.equals("")) {
                    try {
                        BigInteger empleado = new BigInteger(sEmpleado);
                        tNovedades.setEmpleado(empleado);
                    } catch (Exception e) {
                        context.update("form:errorArchivo");
                        context.execute("PF('errorArchivo').show()");
                        break;
                    }
                } else {
                    tNovedades.setEmpleado(null);
                }
                //LEER TIPO AUSENTISMO
                String sTipo = sCadena.substring(17, 21).trim();
                if (!sTipo.equals("")) {
                    try {
                        BigInteger tipo = new BigInteger(sTipo);
                        tNovedades.setTipo(tipo);
                    } catch (Exception e) {
                        context.update("form:errorArchivo");
                        context.execute("PF('errorArchivo').show()");
                        break;
                    }
                } else {
                    tNovedades.setTipo(null);
                }
                //LEER CLASE AUSENTISMO
                String sClase = sCadena.substring(22, 26).trim();
                if (!sClase.equals("")) {
                    try {
                        BigInteger clase = new BigInteger(sClase);
                        tNovedades.setClase(clase);
                    } catch (Exception e) {
                        context.update("form:errorArchivo");
                        context.execute("PF('errorArchivo').show()");
                        break;
                    }
                } else {
                    tNovedades.setClase(null);
                }
                //LEER CAUSA AUSENTIMO
                String sCausa = sCadena.substring(27, 31).trim();
                if (!sCausa.equals("")) {
                    try {
                        BigInteger causa = new BigInteger(sCausa);
                        tNovedades.setCausa(causa);
                    } catch (Exception e) {
                        context.update("form:errorArchivo");
                        context.execute("PF('errorArchivo').show()");
                        break;
                    }
                } else {
                    tNovedades.setCausa(null);
                }
                // LEER DIAS AUSENTISMO
                String sDias = sCadena.substring(32, 46).trim();
                if (!sDias.equals("")) {
                    try {
                        BigInteger dias = new BigInteger(sDias);
                        tNovedades.setDias(dias);
                    } catch (Exception e) {
                        context.update("form:errorArchivo");
                        context.execute("PF('errorArchivo').show()");
                        break;
                    }
                } else {
                    tNovedades.setDias(null);
                }
                // LEER FECHA INICIAL AUSENTISMO
                String fechaInicial = sCadena.substring(47, 59).trim();
                if (!fechaInicial.equals("")) {
                    if (fechaInicial.indexOf("-") > 0) {
                        fechaInicial = fechaInicial.replaceAll("-", "/");
                    }
                    try {
                        Date FechaInicial = formato.parse(fechaInicial);
                        tNovedades.setFecha(FechaInicial);
                    } catch (Exception e) {
                        context.update("form:errorArchivo");
                        context.execute("PF('errorArchivo').show()");
                        break;
                    }
                } else {
                    tNovedades.setFecha(null);
                }

                //LEER FECHA FINAL AUSENTISMO
                String fechaFinal = sCadena.substring(60, 72).trim();
                if (!fechaFinal.equals("")) {
                    if (fechaFinal.indexOf("-") > 0) {
                        fechaFinal = fechaFinal.replaceAll("-", "/");
                    }
                    try {
                        Date FechaFinal = formato.parse(fechaFinal);
                        tNovedades.setFechafinaus(FechaFinal);
                    } catch (Exception e) {
                        context.update("form:errorArchivo");
                        context.execute("PF('errorArchivo').show()");
                        break;
                    }
                } else {
                    tNovedades.setFechafinaus(null);
                }
                // LEER FECHA EXPEDICION
                String fechaExp = sCadena.substring(73, 86).trim();
                if (!fechaExp.equals("")) {
                    if (fechaExp.indexOf("-") > 0) {
                        fechaExp = fechaExp.replaceAll("-", "/");
                    }
                    try {
                        Date fechaExpe = formato.parse(fechaExp);
                        tNovedades.setFechaexpedicion(fechaExpe);
                    } catch (Exception e) {
                        context.update("form:errorArchivo");
                        context.execute("PF('errorArchivo').show()");
                        break;
                    }
                } else {
                    tNovedades.setFechaexpedicion(null);
                }
                //LEER FECHA INICIO PAGO
                String fechaIniPago = sCadena.substring(87, 101).trim();
                if (!fechaIniPago.equals("")) {
                    if (fechaIniPago.indexOf("-") > 0) {
                        fechaIniPago = fechaIniPago.replaceAll("-", "/");
                    }
                    try {
                        Date fechaIniP = formato.parse(fechaIniPago);
                        tNovedades.setFechainipago(fechaIniP);
                    } catch (Exception e) {
                        context.update("form:errorArchivo");
                        context.execute("PF('errorArchivo').show()");
                        break;
                    }
                } else {
                    tNovedades.setFechainipago(null);
                }
                //LEER FECHA FIN PAGO
                String fechaFinPago = sCadena.substring(102, 115).trim();
                if (!fechaFinPago.equals("")) {
                    if (fechaFinPago.indexOf("-") > 0) {
                        fechaFinPago = fechaFinPago.replaceAll("-", "/");
                    }
                    try {
                        Date fechaFinP = formato.parse(fechaFinPago);
                        tNovedades.setFechafinpago(fechaFinP);
                    } catch (Exception e) {
                        context.update("form:errorArchivo");
                        context.execute("PF('errorArchivo').show()");
                        break;
                    }
                } else {
                    tNovedades.setFechafinpago(null);
                }
                // LEER PORCENTAJE AUSENTISMO
                String sPorcentaje = sCadena.substring(116, 140).trim();
                if (!sPorcentaje.equals("")) {
                    try {
                        BigInteger porcentaje = new BigInteger(sPorcentaje);
                        tNovedades.setPorcentajeindividual(porcentaje);
                    } catch (Exception e) {
                        context.update("form:errorArchivo");
                        context.execute("PF('errorArchivo').show()");
                        break;
                    }
                } else {
                    tNovedades.setPorcentajeindividual(null);
                }
                // LEER BASE LIQUIDACION
                String sBaseL = sCadena.substring(123, 132).trim();
                if (!sBaseL.equals("")) {
                    try {
                        BigInteger baseliq = new BigInteger(sBaseL);
                        tNovedades.setBaseliquidacion(baseliq);
                    } catch (Exception e) {
                        context.update("form:errorArchivo");
                        context.execute("PF('errorArchivo').show()");
                        break;
                    }
                } else {
                    tNovedades.setBaseliquidacion(null);
                }
                // LEER FORMA LIQUIDACION
                String sFormaL = sCadena.substring(134, 183).trim();
                if (!sFormaL.equals("")) {
                    try {
                        tNovedades.setFormaliquidacion(sFormaL);
                    } catch (Exception e) {
                        context.update("form:errorArchivo");
                        context.execute("PF('errorArchivo').show()");
                        break;
                    }
                } else {
                    tNovedades.setFormaliquidacion(null);
                }
                tNovedades.setUsuariobd(UsuarioBD.getAlias());
                //NOMBRE ARCHIVO
                tNovedades.setArchivo(nombreArchivo);
                tNovedades.setSecuencia(BigInteger.valueOf(1));
                listTempSoAusentismos.add(tNovedades);
                tNovedades = null;
            }
            administrarCargueArchivos.borrarRegistrosTempSoAusentismos(UsuarioBD.getAlias());
            insertarNovedadTempSoAusentismos();
            listTempSoAusentismos = null;
            listTempSoAusentismos = administrarCargueArchivos.consultarTempSoAusentismos(UsuarioBD.getAlias());
            subTotal = new BigDecimal(0);
            if (listTempSoAusentismos != null) {
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
            System.out.println("Excepcion: (leerTxt) " + e);
        }
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            columnaEmpleado = (Column) c.getViewRoot().findComponent("form:tempNovedades:columnaEmpleado");
            columnaEmpleado.setFilterStyle("width: 85% !important");
            columnaTipoAusentismo = (Column) c.getViewRoot().findComponent("form:tempNovedades:columnaTipoAusentismo");
            columnaTipoAusentismo.setFilterStyle("width: 85% !important");
            columnaClaseAusentismo = (Column) c.getViewRoot().findComponent("form:tempNovedades:columnaClaseAusentismo");
            columnaClaseAusentismo.setFilterStyle("width: 85% !important");
            columnaCausaAusentismo = (Column) c.getViewRoot().findComponent("form:tempNovedades:columnaCausaAusentismo");
            columnaCausaAusentismo.setFilterStyle("width: 85% !important");
            columnaDias = (Column) c.getViewRoot().findComponent("form:tempNovedades:columnaDias");
            columnaDias.setFilterStyle("width: 85% !important");
            columnaFechaIni = (Column) c.getViewRoot().findComponent("form:tempNovedades:columnaFechaIni");
            columnaFechaIni.setFilterStyle("width: 85% !important");
            columnaFechaFin = (Column) c.getViewRoot().findComponent("form:tempNovedades:columnaFechaFin");
            columnaFechaFin.setFilterStyle("width: 85% !important");
            columnaFechaExp = (Column) c.getViewRoot().findComponent("form:tempNovedades:columnaFechaExp");
            columnaFechaExp.setFilterStyle("width: 85% !important");
            columnaFechaIniPago = (Column) c.getViewRoot().findComponent("form:tempNovedades:columnaFechaIniPago");
            columnaFechaIniPago.setFilterStyle("width: 85% !important");
            columnaFechaFinPago = (Column) c.getViewRoot().findComponent("form:tempNovedades:columnaFechaFinPago");
            columnaFechaFinPago.setFilterStyle("width: 85% !important");
            columnaPorcentaje = (Column) c.getViewRoot().findComponent("form:tempNovedades:columnaPorcentaje");
            columnaPorcentaje.setFilterStyle("width: 85% !important");
            columnaBaseL = (Column) c.getViewRoot().findComponent("form:tempNovedades:columnaBaseL");
            columnaBaseL.setFilterStyle("width: 85% !important");
            columnaFormaL = (Column) c.getViewRoot().findComponent("form:tempNovedades:columnaFormaL");
            columnaFormaL.setFilterStyle("width: 85% !important");
            columnaDocumentoSoporte = (Column) c.getViewRoot().findComponent("form:tempNovedades:columnaDocumentoSoporte");
            columnaDocumentoSoporte.setFilterStyle("width: 85% !important");
            altoTabla = "108";
            RequestContext.getCurrentInstance().update("form:tempNovedades");
            bandera = 1;
        } else if (bandera == 1) {
            restaurarTabla();
        }
        cualCelda = -1;
    }

//   public void validarNovedades() {
//      System.out.println("Cargue.CargarArchivoPlano.validarNovedades()");
//      boolean validacion = false;
//      List<String> erroresN;
//      documentosSoporteCargados = administrarCargueArchivos.consultarDocumentosSoporteCargadosUsuario(UsuarioBD.getAlias());
//      BigInteger secEmpresa = administrarCargueArchivos.consultarParametrosEmpresa(UsuarioBD.getAlias());
//      for (int i = 0; i < listTempSoAusentismos.size(); i++) {
//         ErroresNovedades errorNovedad = new ErroresNovedades();
//         //NUMERO DE ERRORES EN LA FILA
//         int errores = 0;
//         //Validad Codigo Empleado TRUE = "EXITOSO"  - FALSE = "FALLO"
//         erroresN = new ArrayList<String>();
//         errorNovedad.setSecNovedad(listTempSoAusentismos.get(i).getSecuencia());
//         errorNovedad.setMensajeError(erroresN);
//         //PRIMERA ETAPA
//         if (errores == 0) {
//            //PRIMERA FASE (EXISTENCIA)
//            //VALIDACION EMPLEADO
//            if (listTempSoAusentismos.get(i).getEmpleado() != null) {
//               validacion = administrarCargueArchivos.verificarEmpleadoEmpresa(listTempSoAusentismos.get(i).getEmpleado(), secEmpresa);
//               if (validacion == false) {
//                  errores++;
//                  erroresN.add("El código del empleado: " + listTempSoAusentismos.get(i).getEmpleado() + ", no existe.");
//               }
//            } else {
//               errores++;
//               erroresN.add("La Empleado es necesario, campo Vacio.");
//            }
//            //VALIDACION CONCEPTO
//            if (listTempSoAusentismos.get(i).getConcepto() != null) {
//               validacion = administrarCargueArchivos.verificarConcepto(listTempSoAusentismos.get(i).getConcepto());
//               if (validacion == false) {
//                  errores++;
//                  erroresN.add("El concepto: " + listTempSoAusentismos.get(i).getConcepto() + ", no existe.");
//               }
//            } else {
//               errores++;
//               erroresN.add("La Concepto es necesario, campo Vacio.");
//            }
//            //VALIDACION PERIODICIDAD
//            if (listTempSoAusentismos.get(i).getPeriodicidad() != null) {
//               validacion = administrarCargueArchivos.verificarPeriodicidad(listTempSoAusentismos.get(i).getPeriodicidad());
//               if (validacion == false) {
//                  errores++;
//                  erroresN.add("La periodicidad:" + listTempSoAusentismos.get(i).getPeriodicidad() + ", no existe.");
//               }
//            } else {
//               errores++;
//               erroresN.add("La periodicidad es necesaria, campo Vacio.");
//            }
//            //SEGUNDA FASE (CAMPOS NO NULOS)
//            //VALIDAR FECHA INICIAL
//            if (listTempSoAusentismos.get(i).getFechainicial() == null) {
//               errores++;
//               erroresN.add("La fecha inicial es necesaria, campo vacio.");
//            }
//            //VALIDAR FECHA REPORTE
//            if (listTempSoAusentismos.get(i).getFechareporte() == null) {
//               errores++;
//               erroresN.add("La fecha de reporte es necesaria, campo vacio.");
//            }
//            //VALIDAR DOCUMENTO SOPORTE
//            if (listTempSoAusentismos.get(i).getDocumentosoporte() == null) {
//               errores++;
//               erroresN.add("El documento soporte es necesario, campo vacio.");
//            }
//            //VALIDAR UNIDAD PARTE ENTERA
//            if (listTempSoAusentismos.get(i).getUnidadesparteentera() == null) {
//               errores++;
//               erroresN.add("La unidad parte entera es necesaria, campo vacio.");
//            }
//            //VALIDAR UNIDAD PARTE FRACCION
//            if (listTempSoAusentismos.get(i).getUnidadespartefraccion() == null) {
//               errores++;
//               erroresN.add("La unidad parte fracción es necesaria, campo vacio.");
//            }
//            //VALIDAR TIPO
//            if (listTempSoAusentismos.get(i).getTipo() == null) {
//               errores++;
//               erroresN.add("El tipo es necesario, campo vacio.");
//            }
//            //TERCERA FASE (CAMPOS CONDICIONADOS)
//            //VALIDAD FECHA FINAL (NO PUEDE SER MENOR QUE LA INICIAL)
//            if (listTempSoAusentismos.get(i).getFechainicial() != null && listTempSoAusentismos.get(i).getFechafinal() != null && listTempSoAusentismos.get(i).getFechainicial().after(listTempSoAusentismos.get(i).getFechafinal())) {
//               errores++;
//               erroresN.add("La fecha inicial no puede ser mayor que la fecha Final.");
//            }
//            //VALIDAR TERCERO
//            if (listTempSoAusentismos.get(i).getTercero() != null) {
//               validacion = administrarCargueArchivos.verificarTercero(listTempSoAusentismos.get(i).getTercero());
//               if (validacion == false) {
//                  errores++;
//                  erroresN.add("El codigo del tercero:" + listTempSoAusentismos.get(i).getTercero() + ", no existe.");
//               }
//            }
//            //VALIDAR SALDO (DEBE SER MAYOR A CERO)
//            /*if (listTempSoAusentismos.get(i).getSaldo() != null && (listTempSoAusentismos.get(i).getSaldo().compareTo(BigDecimal.valueOf(0)) == -1 || listTempSoAusentismos.get(i).getSaldo().compareTo(BigDecimal.valueOf(0)) == 0)) {
//                 errores++;
//                 erroresN.add("El Saldo deber ser mayor a cero.");
//                 }*/
//         }
//         //SEGUNDA ETAPA
//         if (errores == 0) {
//            validacion = administrarCargueArchivos.verificarTipoEmpleadoActivo(listTempSoAusentismos.get(i).getEmpleado(), secEmpresa);
//            if (validacion == true) {
//               Empleados empleado = administrarCargueArchivos.consultarEmpleadoEmpresa(listTempSoAusentismos.get(i).getEmpleado(), secEmpresa);
//               VWActualesTiposTrabajadores vwActualTipoTrabajador = administrarCargueArchivos.consultarActualTipoTrabajadorEmpleado(empleado.getSecuencia());
//               VWActualesReformasLaborales vwActualReformaLaboral = administrarCargueArchivos.consultarActualReformaLaboralEmpleado(empleado.getSecuencia());
//               VWActualesTiposContratos vwActualTiposContratos = administrarCargueArchivos.consultarActualTipoContratoEmpleado(empleado.getSecuencia());
//
//               Conceptos concepto = administrarCargueArchivos.verificarConceptoEmpresa(listTempSoAusentismos.get(i).getConcepto(), empleado.getEmpresa().getSecuencia());
//               //Conceptos concepto = administrarCargueArchivos.verificarConceptoEmpresa(listTempSoAusentismos.get(i).getConcepto(), empleado.getEmpresa());
//               if (concepto != null) {
//                  if (concepto.getActivo().equalsIgnoreCase("S")) {
//                     String tipoConcepto = administrarCargueArchivos.determinarTipoConcepto(concepto.getSecuencia());
//
//                     if (tipoConcepto.equalsIgnoreCase("MANUAL") || tipoConcepto.equalsIgnoreCase("SEMI-AUTOMATICO")) {
//                        if (listTempSoAusentismos.get(i).getValortotal() != null) {
//                           subTotal = subTotal.add(listTempSoAusentismos.get(i).getValortotal());
//                        }
//                        validacion = administrarCargueArchivos.verificarZonaT(concepto.getSecuencia(), vwActualReformaLaboral.getReformaLaboral().getSecuencia(), vwActualTiposContratos.getTipoContrato().getSecuencia(), vwActualTipoTrabajador.getTipoTrabajador().getSecuencia());
//                        if (validacion == false) {
//                           errores++;
//                           erroresN.add("La Zona T del concepto no coicide con la del empleado (" + empleado.getCodigoempleado() + ").");
//                        }
//                     }
//                     if (tipoConcepto.equalsIgnoreCase("MANUAL")) {
//                        if (listTempSoAusentismos.get(i).getValortotal() == null) {
//                           errores++;
//                           erroresN.add("El concepto es manual, por ende es necesario un valor, campo vacio.");
//                        }
//                        if (listTempSoAusentismos.get(i).getValortotal() != null && listTempSoAusentismos.get(i).getValortotal().compareTo(BigDecimal.valueOf(0)) == 0) {
//                           errores++;
//                           erroresN.add("El concepto es manual, por lo tanto, el valor total debe ser mayor o menor a cero.");
//                        }
//                        if (listTempSoAusentismos.get(i).getUnidadesparteentera() == null || listTempSoAusentismos.get(i).getUnidadesparteentera() != 0) {
//                           errores++;
//                           erroresN.add("El concepto es manual, por lo tanto la unidad parte entera debe ser cero.");
//                        }
//                        if (listTempSoAusentismos.get(i).getUnidadespartefraccion() == null || listTempSoAusentismos.get(i).getUnidadespartefraccion() != 0) {
//                           errores++;
//                           erroresN.add("El concepto es manual, por lo tanto la unidad parte fraccion debe ser cero.");
//                        }
//                        if (listTempSoAusentismos.get(i).getSaldo() != null && (listTempSoAusentismos.get(i).getSaldo().compareTo(BigDecimal.valueOf(0)) == -1 || listTempSoAusentismos.get(i).getSaldo().compareTo(BigDecimal.valueOf(0)) == 0)) {
//                           errores++;
//                           erroresN.add("El Saldo deber ser mayor a cero.");
//                        }
//                     } else if (tipoConcepto.equalsIgnoreCase("SEMI-AUTOMATICO")) {
//
//                        if (listTempSoAusentismos.get(i).getUnidadesparteentera() == 0 && listTempSoAusentismos.get(i).getUnidadespartefraccion() == 0) {
//                           errores++;
//                           erroresN.add("El concepto es semi-automatico, por lo tanto solo una de las 2 unidades (Unidad Fraccion - Unidad Entera), puede ser cero.");
//                        } else {
//                           if (listTempSoAusentismos.get(i).getUnidadesparteentera() < 0) {
//                              errores++;
//                              erroresN.add("El concepto es semi-automatico, por lo tanto la unidad parte entera debe ser un valor positivo.");
//                           }
//                           if (listTempSoAusentismos.get(i).getUnidadespartefraccion() < 0) {
//                              errores++;
//                              erroresN.add("El concepto es semi-automatico, por lo tanto la unidad parte fracción debe ser un valor positivo.");
//                           }
//                        }
//                        if (listTempSoAusentismos.get(i).getSaldo() != null) {
//                           errores++;
//                           erroresN.add("El concepto es semi-automatico, por lo tanto el saldo debe estar vacio.");
//                        }
//                        if (listTempSoAusentismos.get(i).getValortotal() != null) {
//                           if (listTempSoAusentismos.get(i).getValortotal().compareTo(BigDecimal.valueOf(0)) != 0) {
//                              errores++;
//                              erroresN.add("El concepto es semi-automatico, por lo tanto el valor total debe ser cero.");
//                           }
//                        } else {
//                           errores++;
//                           erroresN.add("El concepto es semi-automatico, por lo tanto valor total no puede estar vacio y debe ser cero (0).");
//                        }
//                        if (usarFormulaConcepto.equals("N")) {
//                           validacion = administrarCargueArchivos.verificarFormulaCargueConcepto(concepto.getSecuencia(), formulaUsada.getSecuencia());
//                           if (validacion == false) {
//                              errores++;
//                              erroresN.add("La formula seleccionada para el cargue no coicide con la del concepto.");
//                           }
//                        }
//                        validacion = administrarCargueArchivos.verificarNecesidadTercero(concepto.getSecuencia());
//                        if (validacion == true && listTempSoAusentismos.get(i).getTercero() == null) {
//                           errores++;
//                           erroresN.add("El concepto pertenece al grupo 1, por lo tanto es necesario un tercero.");
//                        }
//                        if (listTempSoAusentismos.get(i).getTercero() != null) {
//                           validacion = administrarCargueArchivos.verificarTerceroEmpresa(listTempSoAusentismos.get(i).getTercero(), empleado.getEmpresa().getSecuencia());
//                           //validacion = administrarCargueArchivos.verificarTerceroEmpresa(listTempSoAusentismos.get(i).getTercero(), empleado.getEmpresa());
//                           if (validacion == false) {
//                              errores++;
//                              erroresN.add("El tercero con nit: " + listTempSoAusentismos.get(i).getTercero() + " no existe para la empresa a la cual esta vinculado el empleado.");
//                           }
//                        }
//                        if (listTempSoAusentismos.get(i).getFechafinal() == null) {
//                           errores++;
//                           erroresN.add("El concepto es semi-automatico, por lo tanto requiere una fecha final.");
//                        }
//                        if (usarFormulaConcepto.equals("N")) {
//                           errores++;
//                           erroresN.add("El concepto es semi-automatico, por lo tanto debe utilizar la formula del concepto.");
//                        }
//                     } else {
//                        errores++;
//                        erroresN.add("No se puede usar el Concepto:" + listTempSoAusentismos.get(i).getConcepto() + ", porque es automatico.");
//                     }
//                  } else {
//                     errores++;
//                     erroresN.add("El Concepto:" + listTempSoAusentismos.get(i).getConcepto() + ", no esta activo.");
//                  }
//               } else {
//                  errores++;
//                  erroresN.add("El codigo del concepto:" + listTempSoAusentismos.get(i).getConcepto() + ", no existe en la empresa a la cual pertenece el empleado.");
//               }
//            } else {
//               errores++;
//               erroresN.add("El Empleado: " + listTempSoAusentismos.get(i).getEmpleado() + ", debe ser Activo.");
//            }
//         } else {
//            //MARCAR EL REGISTRO EN LA BASE DE DATOS PARA SABER QUE TIENE ERRORES
//            errorNovedad.setNumeroErrores(errores);
//            errorNovedad.setMensajeError(erroresN);
//            listTempSoAusentismos.get(i).setEstadovalidacion("I");
//            administrarCargueArchivos.modificarTempSoAusentismos(listTempSoAusentismos.get(i));
//         }
//         //TERCERA ETAPA
//         RequestContext context = RequestContext.getCurrentInstance();
//         if (errores == 0) {
//            if (listTempSoAusentismos.get(i).getDocumentosoporte().length() > 20) {
//               errores++;
//               erroresN.add("El documento soporte debe ser maximo de 20 caracteres.");
//            } else {
//               int duplicado = 0;
//               for (int j = 0; j < documentosSoporteCargados.size(); j++) {
//                  if (listTempSoAusentismos.get(i).getDocumentosoporte().equalsIgnoreCase(documentosSoporteCargados.get(j))) {
//                     duplicado++;
//                  }
//               }
//               if (duplicado > 0) {
//                  errores++;
//                  erroresN.add("El documento soporte (" + listTempSoAusentismos.get(i).getDocumentosoporte() + ") ya existe, cambie el nombre del mismo.");
//
//               }
//            }
//            if (listTempSoAusentismos.get(i).getTipo().equalsIgnoreCase("OCASIONAL") || listTempSoAusentismos.get(i).getTipo().equalsIgnoreCase("FIJA") || listTempSoAusentismos.get(i).getTipo().equalsIgnoreCase("PAGO POR FUERA")) {
//            } else {
//               errores++;
//               erroresN.add("El tipo de novedad debe ser Ocasional, Fija o Pago por Fuera.");
//
//            }
//            context.update("form:subtotal");
//         } else {
//            errorNovedad.setNumeroErrores(errores);
//            errorNovedad.setMensajeError(erroresN);
//            listTempSoAusentismos.get(i).setEstadovalidacion("I");
//            administrarCargueArchivos.modificarTempSoAusentismos(listTempSoAusentismos.get(i));
//            context.update("form:subtotal");
//         }
//         //FINAL
//         if (errores == 0) {
//            errorNovedad.setNumeroErrores(errores);
//            errorNovedad.setMensajeError(erroresN);
//            listTempSoAusentismos.get(i).setEstadovalidacion("C");
//            administrarCargueArchivos.modificarTempSoAusentismos(listTempSoAusentismos.get(i));
//            context.update("form:subtotal");
//         } else {
//            errorNovedad.setNumeroErrores(errores);
//            errorNovedad.setMensajeError(erroresN);
//            listTempSoAusentismos.get(i).setEstadovalidacion("I");
//            administrarCargueArchivos.modificarTempSoAusentismos(listTempSoAusentismos.get(i));
//            context.update("form:subtotal");
//         }
//         listErrores.add(errorNovedad);
//         errorNovedad = null;
//         erroresN = null;
//      }
//      /*for (int i = 0; i < listErrores.size(); i++) {
//         |out.println("\nSecuencia: " + listErrores.get(i).getSecNovedad() + "\nError: " + listErrores.get(i).getMensajeError());
//         }*/
//      //administrarCargueArchivos.revisarConcepto(12);
//   }
    public void revisarNovedad(BigInteger secnovedad) {
        System.out.println("Cargue.CargarArchivoPlano.revisarNovedad() secnovedad : " + secnovedad);
//      validarNovedades();
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
        if (tempSoAusentismoSeleccionada != null) {
            if (!modificarTempSoAusentismos.isEmpty() && modificarTempSoAusentismos.contains(tempSoAusentismoSeleccionada)) {
                modificarTempSoAusentismos.remove(tempSoAusentismoSeleccionada);
                borrarTempSoAusentismos.add(tempSoAusentismoSeleccionada);
            } else if (!crearTempSoAusentismos.isEmpty() && crearTempSoAusentismos.contains(tempSoAusentismoSeleccionada)) {
                crearTempSoAusentismos.remove(tempSoAusentismoSeleccionada);
            } else {
                borrarTempSoAusentismos.add(tempSoAusentismoSeleccionada);
            }
            listTempSoAusentismos.remove(tempSoAusentismoSeleccionada);
            if (tipoLista == 1) {
                filtrarListTempSoAusentismos.remove(tempSoAusentismoSeleccionada);
            }
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:tempNovedades");
            tempSoAusentismoSeleccionada = null;
            if (guardado) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void duplicar() {
        if (tempSoAusentismoSeleccionada == null) {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        } else {
            duplicarTempSoAusentismos = new TempSoAusentismos();
            k++;
            l = BigInteger.valueOf(k);
            duplicarTempSoAusentismos.setSecuencia(l);
            duplicarTempSoAusentismos.setEmpleado(tempSoAusentismoSeleccionada.getEmpleado());
            duplicarTempSoAusentismos.setTipo(tempSoAusentismoSeleccionada.getTipo());
            duplicarTempSoAusentismos.setDocumentosoporte(tempSoAusentismoSeleccionada.getDocumentosoporte());
            duplicarTempSoAusentismos.setClase(tempSoAusentismoSeleccionada.getClase());
            duplicarTempSoAusentismos.setCausa(tempSoAusentismoSeleccionada.getCausa());
            duplicarTempSoAusentismos.setDias(tempSoAusentismoSeleccionada.getDias());
            duplicarTempSoAusentismos.setFecha(tempSoAusentismoSeleccionada.getFecha());
            duplicarTempSoAusentismos.setFechafinaus(tempSoAusentismoSeleccionada.getFechafinaus());
            duplicarTempSoAusentismos.setFechainipago(tempSoAusentismoSeleccionada.getFechainipago());
            duplicarTempSoAusentismos.setFechafinpago(tempSoAusentismoSeleccionada.getFechafinpago());
            duplicarTempSoAusentismos.setFechaexpedicion(tempSoAusentismoSeleccionada.getFechaexpedicion());
            duplicarTempSoAusentismos.setPorcentajeindividual(tempSoAusentismoSeleccionada.getPorcentajeindividual());
            duplicarTempSoAusentismos.setBaseliquidacion(tempSoAusentismoSeleccionada.getBaseliquidacion());
            duplicarTempSoAusentismos.setFormaliquidacion(tempSoAusentismoSeleccionada.getFormaliquidacion());
            duplicarTempSoAusentismos.setDocumentosoporte(tempSoAusentismoSeleccionada.getDocumentosoporte());
            duplicarTempSoAusentismos.setUsuariobd(tempSoAusentismoSeleccionada.getUsuariobd());

            RequestContext.getCurrentInstance().update("formDialogos:duplicarTempNDialogo");
            RequestContext.getCurrentInstance().execute("PF('duplicarTempNDialogo').show()");
        }
    }

    public void confirmarDuplicar() {
        listTempSoAusentismos.add(duplicarTempSoAusentismos);
        crearTempSoAusentismos.add(duplicarTempSoAusentismos);
        tempSoAusentismoSeleccionada = listTempSoAusentismos.get(listTempSoAusentismos.indexOf(duplicarTempSoAusentismos));

        RequestContext.getCurrentInstance().update("form:tempNovedades");
        contarRegistros();
        if (guardado) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
        if (bandera == 1) {
            restaurarTabla();
        }
        duplicarTempSoAusentismos = new TempSoAusentismos();

        RequestContext.getCurrentInstance().update("formDialogos:duplicarTempNDialogo");
        RequestContext.getCurrentInstance().execute("PF('duplicarTempNDialogo').hide()");
    }

    public void agregarNuevaTempAusentismo() {
        k++;
        l = BigInteger.valueOf(k);
        nuevaTempSoAusentismos.setSecuencia(l);
        listTempSoAusentismos.add(nuevaTempSoAusentismos);
        crearTempSoAusentismos.add(nuevaTempSoAusentismos);
        tempSoAusentismoSeleccionada = listTempSoAusentismos.get(listTempSoAusentismos.indexOf(nuevaTempSoAusentismos));

        RequestContext.getCurrentInstance().update("form:tempNovedades");
        contarRegistros();
        if (guardado) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
        if (bandera == 1) {
            restaurarTabla();
        }
        nuevaTempSoAusentismos = new TempSoAusentismos();

        RequestContext.getCurrentInstance().update("formDialogos:nuevaTempNDialogo");
        RequestContext.getCurrentInstance().execute("PF('nuevaTempNDialogo').hide()");
    }

    public void limpiarNuevo() {
        nuevaTempSoAusentismos = new TempSoAusentismos();
        duplicarTempSoAusentismos = new TempSoAusentismos();
        RequestContext.getCurrentInstance().update("formDialogos:nuevaTempNDialogo");
        RequestContext.getCurrentInstance().update("formDialogos:duplicarTempNDialogo");
    }

    public void insertarNovedadTempSoAusentismos() {
        if (!listTempSoAusentismos.isEmpty()) {
            administrarCargueArchivos.crearTempSoAusentismos(listTempSoAusentismos);
        }
    }

    public void guardar() {
        if (guardado == false) {
            guardado = true;
            if (!modificarTempSoAusentismos.isEmpty()) {
                for (int i = 0; i < modificarTempSoAusentismos.size(); i++) {
                    administrarCargueArchivos.modificarTempSoAusentismos(modificarTempSoAusentismos.get(i));
                }
            }
            if (!borrarTempSoAusentismos.isEmpty()) {
                for (int i = 0; i < borrarTempSoAusentismos.size(); i++) {
                    administrarCargueArchivos.borrarTempSoAusentismos(borrarTempSoAusentismos.get(i));
                }
            }
            if (!crearTempSoAusentismos.isEmpty()) {
                administrarCargueArchivos.crearTempSoAusentismos(crearTempSoAusentismos);
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
        columnaEmpleado = (Column) c.getViewRoot().findComponent("form:tempNovedades:columnaEmpleado");
        columnaEmpleado.setFilterStyle("display: none; visibility: hidden;");
        columnaTipoAusentismo = (Column) c.getViewRoot().findComponent("form:tempNovedades:columnaTipoAusentismo");
        columnaTipoAusentismo.setFilterStyle("display: none; visibility: hidden;");
        columnaClaseAusentismo = (Column) c.getViewRoot().findComponent("form:tempNovedades:columnaClaseAusentismo");
        columnaClaseAusentismo.setFilterStyle("display: none; visibility: hidden;");
        columnaCausaAusentismo = (Column) c.getViewRoot().findComponent("form:tempNovedades:columnaCausaAusentismo");
        columnaCausaAusentismo.setFilterStyle("display: none; visibility: hidden;");
        columnaDias = (Column) c.getViewRoot().findComponent("form:tempNovedades:columnaDias");
        columnaDias.setFilterStyle("display: none; visibility: hidden;");
        columnaFechaIni = (Column) c.getViewRoot().findComponent("form:tempNovedades:columnaFechaIni");
        columnaFechaIni.setFilterStyle("display: none; visibility: hidden;");
        columnaFechaFin = (Column) c.getViewRoot().findComponent("form:tempNovedades:columnaFechaFin");
        columnaFechaFin.setFilterStyle("display: none; visibility: hidden;");
        columnaFechaExp = (Column) c.getViewRoot().findComponent("form:tempNovedades:columnaFechaExp");
        columnaFechaExp.setFilterStyle("display: none; visibility: hidden;");
        columnaFechaIniPago = (Column) c.getViewRoot().findComponent("form:tempNovedades:columnaFechaIniPago");
        columnaFechaIniPago.setFilterStyle("display: none; visibility: hidden;");
        columnaFechaFinPago = (Column) c.getViewRoot().findComponent("form:tempNovedades:columnaFechaFinPago");
        columnaFechaFinPago.setFilterStyle("display: none; visibility: hidden;");
        columnaPorcentaje = (Column) c.getViewRoot().findComponent("form:tempNovedades:columnaPorcentaje");
        columnaPorcentaje.setFilterStyle("display: none; visibility: hidden;");
        columnaBaseL = (Column) c.getViewRoot().findComponent("form:tempNovedades:columnaBaseL");
        columnaBaseL.setFilterStyle("display: none; visibility: hidden;");
        columnaFormaL = (Column) c.getViewRoot().findComponent("form:tempNovedades:columnaFormaL");
        columnaFormaL.setFilterStyle("display: none; visibility: hidden;");
        columnaDocumentoSoporte = (Column) c.getViewRoot().findComponent("form:tempNovedades:columnaDocumentoSoporte");
        columnaDocumentoSoporte.setFilterStyle("display: none; visibility: hidden;");
        altoTabla = "130";
        RequestContext.getCurrentInstance().update("form:tempNovedades");
        bandera = 0;
        filtrarListTempSoAusentismos = null;
        tipoLista = 0;
    }

    //AUTOCOMPLETAR DOCUMENTO SOPORTE
    public void autocompletarDocumentoSoporte() {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        documentosSoporteCargados = null;
        getDocumentosSoporteCargados();
        hs.addAll(documentosSoporteCargados);
        documentosSoporteCargados.clear();
        documentosSoporteCargados.addAll(hs);
        for (int i = 0; i < documentosSoporteCargados.size(); i++) {
            if (documentosSoporteCargados.get(i).startsWith(documentoSoporteReversar.toUpperCase())) {
                indiceUnicoElemento = i;
                coincidencias++;
            }
        }

        if (coincidencias == 1) {
            documentoSoporteReversar = documentosSoporteCargados.get(indiceUnicoElemento);
            documentosSoporteCargados = null;
            getDocumentosSoporteCargados();
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
        documentosSoporteCargados = null;
        aceptar = true;
        documentoSoporteReversar = seleccionDocumentosSoporteCargado;
        seleccionDocumentosSoporteCargado = null;
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form:documentoR");
        context.reset("formDialogos:lovDocumentoSoporte:globalFilter");
        context.execute("PF('lovDocumentoSoporte').clearFilters()");
        context.execute("PF('documentoSoporteDialogo').hide()");
        //context.update("formDialogos:lovDocumentoSoporte");
    }

    public void cancelarSeleccionDocumentoSoporte() {
        filtradoDocumentosSoporteCargados = null;
        documentosSoporteCargados = null;
        seleccionDocumentosSoporteCargado = null;
        aceptar = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formDialogos:lovDocumentoSoporte:globalFilter");
        context.execute("PF('lovDocumentoSoporte').clearFilters()");
        context.execute("PF('documentoSoporteDialogo').hide()");
    }

    public void llamarDialogoDocumentoSoporte() {
        RequestContext context = RequestContext.getCurrentInstance();
        documentosSoporteCargados = null;
        context.update("formDialogos:lovDocumentoSoporte");
        context.execute("PF('documentoSoporteDialogo').show()");
    }
    //CARGUE NOVEDADES

//   public void cargarNovedades() {
//      RequestContext context = RequestContext.getCurrentInstance();
//      if (!listTempSoAusentismos.isEmpty() || listTempSoAusentismos != null) {
//         int pasa = 0;
//         for (int i = 0; i < listErrores.size(); i++) {
//            if (listErrores.get(i).getNumeroErrores() != 0) {
//               pasa++;
//            }
//         }
//         if (pasa == 0) {
//            administrarCargueArchivos.cargarTempSoAusentismos(listTempSoAusentismos.get(0).getFechareporte(), formulaUsada.getNombrecorto(), usarFormulaConcepto);
//            int registrosNAntes = listTempSoAusentismos.size();
//            listTempSoAusentismos = administrarCargueArchivos.consultarTempSoAusentismos(UsuarioBD.getAlias());
//            int registrosNDespues = listTempSoAusentismos.size();
//            diferenciaRegistrosN = registrosNAntes - registrosNDespues;
//            context.update("form:tempNovedades");
//            if (diferenciaRegistrosN == registrosNAntes) {
//               context.update("form:novedadesCargadas");
//               context.execute("PF('novedadesCargadas').show()");
//            }
//            subTotal = new BigDecimal(0);
//            context.update("form:subtotal");
//            listErrores.clear();
//            erroresNovedad = null;
//            cargue = true;
//            nombreArchivoPlano = null;
//            documentosSoportes = null;
//            context.update("form:pickListDocumentosSoporte");
//            botones = false;
//            context.update("form:FileUp");
//            context.update("form:nombreArchivo");
//            context.update("form:formula");
//            context.update("form:usoFormulaC");
//            context.update("form:cargar");
//         }
//      }
//   }
    public void borrarRegistrosNoCargados() {
        administrarCargueArchivos.borrarRegistrosTempSoAusentismos(UsuarioBD.getAlias());
        listTempSoAusentismos = administrarCargueArchivos.consultarTempSoAusentismos(UsuarioBD.getAlias());
        contarRegistros();
        nombreArchivoPlano = null;
        subTotal = new BigDecimal(0);
        botones = false;
        cargue = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form:subtotal");
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
        resultado = administrarCargueArchivos.reversarNovedades(UsuarioBD, documentoSoporteReversar);
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
        documentosSoporteCargados = administrarCargueArchivos.consultarDocumentosSoporteCargadosUsuario(UsuarioBD.getAlias());
        hs.addAll(documentosSoporteCargados);
        documentosSoporteCargados.clear();
        documentosSoporteCargados.addAll(hs);
        hs.clear();
        int existeDocumento = 0;
        for (int i = 0; i < documentosSoporteCargados.size(); i++) {
            if (documentosSoporteCargados.get(i).equals(documentoSoporteReversar)) {
                existeDocumento++;
            }
        }
        if (existeDocumento == 1) {
            context.update("form:confirmarReversar");
            context.execute("PF('confirmarReversar').show()");
        }
    }

    //
    public void confirmarBorrarTodo() {
        if (!documentosSoportes.getTarget().isEmpty()) {
            RequestContext context = RequestContext.getCurrentInstance();
            resultadoProceso = administrarCargueArchivos.BorrarTodo(UsuarioBD, documentosSoportes.getTarget());
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

    public void prueba() {
        //administrarCargueArchivos.validarConceptoEmpresa(BigInteger.valueOf(20002), BigInteger.valueOf(19931));
        //administrarCargueArchivos.validarTipoEmpleadoActivo(BigInteger.valueOf(1022337240));
    }

    public void exportPDF() throws IOException {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formExportar:exportarTempSoAusentismos");
        FacesContext context = c;
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "Novedades_PDF", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formExportar:exportarTempSoAusentismos");
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
        tempSoAusentismoSeleccionada = null;
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

//    public void contarSubtotal() {
//        if (listTempSoAusentismos != null) {
//            if (!listTempSoAusentismos.isEmpty()) {
//                subTotal = new BigDecimal(0);
//                for (int i = 0; i < listTempSoAusentismos.size(); i++) {
//                    if (listTempSoAusentismos.get(i).getValortotal() != null) {
//                        subTotal = subTotal.add(listTempSoAusentismos.get(i).getValortotal());
//                    }
//                }
//            }
//        }
//    }
    //GETTER AND SETTER
    public List<TempSoAusentismos> getListTempSoAusentismos() {
        if (UsuarioBD == null) {
            UsuarioBD = administrarCargueArchivos.actualUsuario();
        }
        if (UsuarioBD.getAlias() != null && listTempSoAusentismos == null) {
            listTempSoAusentismos = administrarCargueArchivos.consultarTempSoAusentismos(UsuarioBD.getAlias());
        }
//        contarSubtotal();
        return listTempSoAusentismos;
    }

    public void setListTempSoAusentismos(List<TempSoAusentismos> listTempSoAusentismos) {
        this.listTempSoAusentismos = listTempSoAusentismos;
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

    public BigDecimal getSubTotal() {
        return subTotal;
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

    public List<String> getDocumentosSoporteCargados() {
        documentosSoporteCargados = administrarCargueArchivos.consultarDocumentosSoporteCargadosUsuario(UsuarioBD.getAlias());
        hs.addAll(documentosSoporteCargados);
        documentosSoporteCargados.clear();
        documentosSoporteCargados.addAll(hs);
        hs.clear();
        return documentosSoporteCargados;
    }

    public void setDocumentosSoporteCargados(List<String> documentosSoporteCargados) {
        this.documentosSoporteCargados = documentosSoporteCargados;
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
        documentosSoporteCargados = null;
        getDocumentosSoporteCargados();
        documentosSoportes = new DualListModel<String>(documentosSoporteCargados, documentosEscogidos);
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

    public List<TempSoAusentismos> getFiltrarListTempSoAusentismos() {
        return filtrarListTempSoAusentismos;
    }

    public void setFiltrarListTempSoAusentismos(List<TempSoAusentismos> filtrarListTempSoAusentismos) {
        this.filtrarListTempSoAusentismos = filtrarListTempSoAusentismos;
    }

    public TempSoAusentismos getTempSoAusentismoSeleccionada() {
        //getListTempSoAusentismos();
        return tempSoAusentismoSeleccionada;
    }

    public void setTempSoAusentismoSeleccionada(TempSoAusentismos tempSoAusentismoSeleccionada) {
        this.tempSoAusentismoSeleccionada = tempSoAusentismoSeleccionada;
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

    public TempSoAusentismos getEditarNovedad() {
        return editarNovedad;
    }

    public void setEditarNovedad(TempSoAusentismos editarNovedad) {
        this.editarNovedad = editarNovedad;
    }

    public boolean isGuardado() {
        return guardado;
    }

    public void setGuardado(boolean guardado) {
        this.guardado = guardado;
    }

    public TempSoAusentismos getDuplicarTempSoAusentismos() {
        return duplicarTempSoAusentismos;
    }

    public void setDuplicarTempSoAusentismos(TempSoAusentismos duplicarTempSoAusentismos) {
        this.duplicarTempSoAusentismos = duplicarTempSoAusentismos;
    }

    public TempSoAusentismos getNuevaTempSoAusentismos() {
        return nuevaTempSoAusentismos;
    }

    public void setNuevaTempSoAusentismos(TempSoAusentismos nuevaTempSoAusentismos) {
        this.nuevaTempSoAusentismos = nuevaTempSoAusentismos;
    }

}
