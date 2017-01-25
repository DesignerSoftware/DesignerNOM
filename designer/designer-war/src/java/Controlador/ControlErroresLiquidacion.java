/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;


import Entidades.Conceptos;
import Entidades.Empleados;
import Entidades.ErroresLiquidacion;
import Entidades.Formulas;
import Entidades.VigenciasLocalizaciones;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarErroresLiquidacionesInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;import ControlNavegacion.ControlListaNavegacion;
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
public class ControlErroresLiquidacion implements Serializable {

    @EJB
    AdministrarErroresLiquidacionesInterface administrarErroresLiquidacion;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    private List<ErroresLiquidacion> listErroresLiquidacion;
    private List<ErroresLiquidacion> filtrarErroresLiquidacion;
    private List<ErroresLiquidacion> crearErroresLiquidacion;
    private List<ErroresLiquidacion> modificarErroresLiquidacion;
    private List<ErroresLiquidacion> borrarErroresLiquidacion;
    private ErroresLiquidacion nuevoErroresLiquidacion;
    private ErroresLiquidacion editarErroresLiquidacion;
    private ErroresLiquidacion erroresLiquidacionesSeleccionado;
    //otros
    private int cualCelda, tipoLista, index, tipoActualizacion, k, bandera;
    private BigInteger l;
    private boolean aceptar, guardado;
    //AutoCompletar
    private boolean permitirIndex;
    //RASTRO
    private BigInteger secRegistro;
    private Column fechaInicial, fechaFinal, empleado, tipoCentroCosto, concepto, nombreLargo, fecha, error, paquete;
    //borrado
    private int registrosBorrados;
    private String mensajeValidacion;
    //filtrado table
    private int tamano;
    private boolean borrarTodo;

    public ControlErroresLiquidacion() {
        listErroresLiquidacion = null;
        crearErroresLiquidacion = new ArrayList<ErroresLiquidacion>();
        modificarErroresLiquidacion = new ArrayList<ErroresLiquidacion>();
        borrarErroresLiquidacion = new ArrayList<ErroresLiquidacion>();
        permitirIndex = true;
        editarErroresLiquidacion = new ErroresLiquidacion();
        nuevoErroresLiquidacion = new ErroresLiquidacion();
        guardado = true;
        aceptar = true;
        tamano = 270;
        secuenciaEmpleado = null;
        System.out.println("controlErroresLiquidacion Constructor");
    }

       private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>(); mapParametros.put ("paginaAnterior", paginaAnterior);
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
      if (pag.equals("atras")) {
         pag = paginaAnterior;
         paginaAnterior = "nominaf";
         controlListaNavegacion.quitarPagina();
      } else {
         String pagActual = "cargo"XXX;
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
    public void inicializarAdministrador() {
        try {
            System.out.println("ControlErroresLiquidacion PostConstruct ");
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarErroresLiquidacion.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void eventoFiltrar() {
        try {
            System.out.println("\n ENTRE A ControlErroresLiquidacion.eventoFiltrar \n");
            if (tipoLista == 0) {
                tipoLista = 1;
            }
            RequestContext context = RequestContext.getCurrentInstance();
            infoRegistro = "Cantidad de registros: " + filtrarErroresLiquidacion.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
        } catch (Exception e) {
            System.out.println("ERROR ControlErroresLiquidacion eventoFiltrar ERROR===" + e.getMessage());
        }
    }
    private BigInteger secuenciaEmpleado;

    public void recibirEmpleado(BigInteger secEmpleado) {
        if (secEmpleado.equals(new BigInteger("0"))) {
            secuenciaEmpleado = null;
        } else {
            secuenciaEmpleado = secEmpleado;
        }
        getListErroresLiquidacion();
    }

    public void mostrarInfo(int indice, int celda) {

        mensajeValidacion = " ";
        index = indice;
        cualCelda = celda;
        RequestContext context = RequestContext.getCurrentInstance();
        if (permitirIndex == true) {
            if (cualCelda == 0) {
                if (tipoLista == 0) {
                    secRegistro = listErroresLiquidacion.get(index).getSecuencia();

                    if (listErroresLiquidacion.get(indice).getFechadesde() == null) {
                        listErroresLiquidacion.get(indice).setFechadesde(backUpFechaDesde);
                    } else if (!listErroresLiquidacion.get(indice).getFechadesde().equals(backUpFechaDesde) && backUpFechaDesde != null) {
                        listErroresLiquidacion.get(indice).setFechadesde(backUpFechaDesde);
                    }
                    index = -1;
                    secRegistro = null;

                } else {
                    if (filtrarErroresLiquidacion.get(indice).getFechadesde() == null) {
                        filtrarErroresLiquidacion.get(indice).setFechadesde(backUpFechaDesde);
                    } else if (!filtrarErroresLiquidacion.get(indice).getFechadesde().equals(backUpFechaDesde) && backUpFechaDesde != null) {
                        filtrarErroresLiquidacion.get(indice).setFechadesde(backUpFechaDesde);
                    }
                    index = -1;
                    secRegistro = null;
                }
            }
            if (cualCelda == 1) {
                if (tipoLista == 0) {
                    secRegistro = listErroresLiquidacion.get(index).getSecuencia();

                    if (listErroresLiquidacion.get(indice).getFechahasta() == null) {
                        listErroresLiquidacion.get(indice).setFechahasta(backUpFechaHasta);
                    } else if (!listErroresLiquidacion.get(indice).getFechahasta().equals(backUpFechaHasta) && backUpFechaHasta != null) {
                        listErroresLiquidacion.get(indice).setFechahasta(backUpFechaHasta);
                    }
                    index = -1;
                    secRegistro = null;

                } else {
                    if (filtrarErroresLiquidacion.get(indice).getFechahasta() == null) {
                        filtrarErroresLiquidacion.get(indice).setFechahasta(backUpFechaHasta);
                    } else if (!filtrarErroresLiquidacion.get(indice).getFechahasta().equals(backUpFechaHasta) && backUpFechaHasta != null) {
                        filtrarErroresLiquidacion.get(indice).setFechahasta(backUpFechaHasta);
                    }
                    index = -1;
                    secRegistro = null;
                }
            }
            if (cualCelda == 6) {
                if (tipoLista == 0) {
                    secRegistro = listErroresLiquidacion.get(index).getSecuencia();

                    if (listErroresLiquidacion.get(indice).getFecha() == null) {
                        listErroresLiquidacion.get(indice).setFecha(backUpFecha);
                    } else if (!listErroresLiquidacion.get(indice).getFecha().equals(backUpFecha) && backUpFecha != null) {
                        listErroresLiquidacion.get(indice).setFecha(backUpFecha);
                    }
                    index = -1;
                    secRegistro = null;

                } else {
                    if (filtrarErroresLiquidacion.get(indice).getFecha() == null) {
                        filtrarErroresLiquidacion.get(indice).setFecha(backUpFecha);
                    } else if (!filtrarErroresLiquidacion.get(indice).getFecha().equals(backUpFecha) && backUpFecha != null) {
                        filtrarErroresLiquidacion.get(indice).setFecha(backUpFecha);
                    }
                    index = -1;
                    secRegistro = null;
                }
            }

            RequestContext.getCurrentInstance().update("form:datosErroresLiquidacion");

        }

    }
    private Date backUpFechaDesde;
    private Date backUpFechaHasta;
    private Date backUpFecha;
    private String backUpEmpleado;
    private String backUpTipoCentroCosto;
    private String backUpConcepto;
    private String backUpFormula;
    private String backUpError;
    private String backUpPaquete;

    public void cambiarIndice(int indice, int celda) {
        System.err.println("TIPO LISTA = " + tipoLista);

        if (permitirIndex == true) {
            index = indice;
            cualCelda = celda;
            secRegistro = listErroresLiquidacion.get(index).getSecuencia();
            if (cualCelda == 0) {
                if (tipoLista == 0) {
                    backUpFechaDesde = listErroresLiquidacion.get(index).getFechadesde();
                } else {
                    backUpFechaDesde = filtrarErroresLiquidacion.get(index).getFechadesde();
                }
                System.out.println("backUpFechaDesde : " + backUpFechaDesde);
            }
            if (cualCelda == 1) {
                if (tipoLista == 0) {
                    backUpFechaHasta = listErroresLiquidacion.get(index).getFechahasta();
                } else {
                    backUpFechaHasta = filtrarErroresLiquidacion.get(index).getFechahasta();
                }
                System.out.println("backUpFechaHasta : " + backUpFechaHasta);
            }
            if (cualCelda == 2) {
                if (tipoLista == 0) {
                    backUpEmpleado = listErroresLiquidacion.get(index).getEmpleado().getPersona().getNombreCompleto();
                } else {
                    backUpEmpleado = filtrarErroresLiquidacion.get(index).getEmpleado().getPersona().getNombreCompleto();
                }
                System.out.println("backUpEmpleado : " + backUpEmpleado);
            }
            if (cualCelda == 3) {
                if (tipoLista == 0) {
                    backUpTipoCentroCosto = listErroresLiquidacion.get(index).getVigenciaLocalizacion().getLocalizacion().getCentrocosto().getTipocentrocosto().getNombre();
                } else {
                    backUpTipoCentroCosto = filtrarErroresLiquidacion.get(index).getVigenciaLocalizacion().getLocalizacion().getCentrocosto().getTipocentrocosto().getNombre();
                }
                System.out.println("backUpTipoCentroCosto : " + backUpTipoCentroCosto);
            }
            if (cualCelda == 4) {
                if (tipoLista == 0) {
                    backUpConcepto = listErroresLiquidacion.get(index).getConcepto().getDescripcion();
                } else {
                    backUpConcepto = filtrarErroresLiquidacion.get(index).getConcepto().getDescripcion();
                }
                System.out.println("backUpConcepto : " + backUpConcepto);
            }
            if (cualCelda == 5) {
                if (tipoLista == 0) {
                    backUpFormula = listErroresLiquidacion.get(index).getFormula().getNombrelargo();
                } else {
                    backUpFormula = filtrarErroresLiquidacion.get(index).getFormula().getNombrelargo();
                }
                System.out.println("backUpFormula : " + backUpFormula);
            }
            System.out.println("Indice: " + index + " Celda: " + cualCelda);

            if (cualCelda == 6) {
                if (tipoLista == 0) {
                    backUpFecha = listErroresLiquidacion.get(index).getFecha();
                } else {
                    backUpFecha = filtrarErroresLiquidacion.get(index).getFecha();
                }
                System.out.println("backUpFecha : " + backUpFecha);
            }
            if (cualCelda == 7) {
                if (tipoLista == 0) {
                    backUpError = listErroresLiquidacion.get(index).getError();
                } else {
                    backUpError = filtrarErroresLiquidacion.get(index).getError();
                }
                System.out.println("backUpError : " + backUpError);
            }
            if (cualCelda == 8) {
                if (tipoLista == 0) {
                    backUpPaquete = listErroresLiquidacion.get(index).getPaquete();
                } else {
                    backUpPaquete = filtrarErroresLiquidacion.get(index).getPaquete();
                }
                System.out.println("backUpPaquete : " + backUpPaquete);
            }
        }
    }

    public void modificarLiquidacionesLogSinGuardar(int indice, String confirmarCambio, String valorConfirmar) {
        index = indice;
        int coincidencias = 0;
        int indiceUnicoElemento = 0, pass = 0;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("N")) {
            System.err.println("CONTROLERRORESLIQUIDACION modificarLiquidacionesLogSinGuardar");
            if (tipoLista == 0) {
                if (!crearErroresLiquidacion.contains(listErroresLiquidacion.get(indice))) {

                    if (listErroresLiquidacion.get(indice).getEmpleado().getPersona().getNombreCompleto() == null) {
                        System.err.println("CONTROLERRORESLIQUIDACION modificarLiquidacionesLogSinGuardar backUpEmpleado : " + backUpEmpleado);
                        listErroresLiquidacion.get(indice).getEmpleado().getPersona().setNombreCompleto(backUpEmpleado);
                    } else if (!listErroresLiquidacion.get(indice).getEmpleado().getPersona().getNombreCompleto().equals(backUpEmpleado) && backUpEmpleado != null) {
                        listErroresLiquidacion.get(indice).getEmpleado().getPersona().setNombreCompleto(backUpEmpleado);
                        System.err.println("CONTROLERRORESLIQUIDACION modificarLiquidacionesLogSinGuardar backUpEmpleado : " + backUpEmpleado);
                    }
                    if (listErroresLiquidacion.get(indice).getVigenciaLocalizacion().getLocalizacion().getCentrocosto().getTipocentrocosto().getNombre() == null) {
                        listErroresLiquidacion.get(indice).getVigenciaLocalizacion().getLocalizacion().getCentrocosto().getTipocentrocosto().setNombre(backUpTipoCentroCosto);
                        System.err.println("CONTROLERRORESLIQUIDACION modificarLiquidacionesLogSinGuardar backUpTipoCentroCosto : " + backUpTipoCentroCosto);
                    } else if (!listErroresLiquidacion.get(indice).getVigenciaLocalizacion().getLocalizacion().getCentrocosto().getTipocentrocosto().getNombre().equals(backUpTipoCentroCosto) && backUpTipoCentroCosto != null) {
                        listErroresLiquidacion.get(indice).getVigenciaLocalizacion().getLocalizacion().getCentrocosto().getTipocentrocosto().setNombre(backUpTipoCentroCosto);
                        System.err.println("CONTROLERRORESLIQUIDACION modificarLiquidacionesLogSinGuardar backUpTipoCentroCosto : " + backUpTipoCentroCosto);
                    }
                    if (listErroresLiquidacion.get(indice).getConcepto().getDescripcion() == null) {
                        listErroresLiquidacion.get(indice).getConcepto().setDescripcion(backUpConcepto);
                        System.err.println("CONTROLERRORESLIQUIDACION modificarLiquidacionesLogSinGuardar backUpConcepto : " + backUpConcepto);
                    } else if (!listErroresLiquidacion.get(indice).getConcepto().getDescripcion().equals(backUpConcepto) && backUpConcepto != null) {
                        listErroresLiquidacion.get(indice).getConcepto().setDescripcion(backUpConcepto);
                        System.err.println("CONTROLERRORESLIQUIDACION modificarLiquidacionesLogSinGuardar backUpConcepto : " + backUpConcepto);
                    }
                    if (listErroresLiquidacion.get(indice).getFormula().getNombrelargo() == null) {
                        listErroresLiquidacion.get(indice).getFormula().setNombrelargo(backUpFormula);
                        System.err.println("CONTROLERRORESLIQUIDACION modificarLiquidacionesLogSinGuardar backUpValor : " + backUpFormula);
                    } else if (!listErroresLiquidacion.get(indice).getFormula().getNombrelargo().equals(backUpFormula) && backUpFormula != null) {
                        listErroresLiquidacion.get(indice).getFormula().setNombrelargo(backUpFormula);
                        System.err.println("CONTROLERRORESLIQUIDACION modificarLiquidacionesLogSinGuardar backUpFormula : " + backUpFormula);
                    }
                    if (listErroresLiquidacion.get(indice).getError() == null) {
                        listErroresLiquidacion.get(indice).setError(backUpError);
                        System.err.println("CONTROLERRORESLIQUIDACION modificarLiquidacionesLogSinGuardar backUpError : " + backUpError);
                    } else if (!listErroresLiquidacion.get(indice).getError().equals(backUpError) && backUpError != null) {
                        listErroresLiquidacion.get(indice).setError(backUpError);
                        System.err.println("CONTROLERRORESLIQUIDACION modificarLiquidacionesLogSinGuardar backUpError : " + backUpError);
                    }
                    if (listErroresLiquidacion.get(indice).getPaquete() == null) {
                        listErroresLiquidacion.get(indice).setPaquete(backUpPaquete);
                        System.err.println("CONTROLERRORESLIQUIDACION modificarLiquidacionesLogSinGuardar getPaquete : " + backUpPaquete);
                    } else if (!listErroresLiquidacion.get(indice).getPaquete().equals(backUpPaquete) && backUpPaquete != null) {
                        listErroresLiquidacion.get(indice).setPaquete(backUpPaquete);
                        System.err.println("CONTROLERRORESLIQUIDACION modificarLiquidacionesLogSinGuardar getPaquete : " + backUpPaquete);
                    }

                    index = -1;
                    secRegistro = null;
                }
            } else {

                if (!crearErroresLiquidacion.contains(filtrarErroresLiquidacion.get(indice))) {

                    if (filtrarErroresLiquidacion.get(indice).getEmpleado().getPersona().getNombreCompleto() == null) {
                        System.err.println("CONTROLERRORESLIQUIDACION modificarLiquidacionesLogSinGuardar backUpEmpleado : " + backUpEmpleado);
                        filtrarErroresLiquidacion.get(indice).getEmpleado().getPersona().setNombreCompleto(backUpEmpleado);
                    } else if (!filtrarErroresLiquidacion.get(indice).getEmpleado().getPersona().getNombreCompleto().equals(backUpEmpleado) && backUpEmpleado != null) {
                        filtrarErroresLiquidacion.get(indice).getEmpleado().getPersona().setNombreCompleto(backUpEmpleado);
                        System.err.println("CONTROLERRORESLIQUIDACION modificarLiquidacionesLogSinGuardar backUpEmpleado : " + backUpEmpleado);
                    }
                    if (filtrarErroresLiquidacion.get(indice).getVigenciaLocalizacion().getLocalizacion().getCentrocosto().getTipocentrocosto().getNombre() == null) {
                        filtrarErroresLiquidacion.get(indice).getVigenciaLocalizacion().getLocalizacion().getCentrocosto().getTipocentrocosto().setNombre(backUpTipoCentroCosto);
                        System.err.println("CONTROLERRORESLIQUIDACION modificarLiquidacionesLogSinGuardar backUpTipoCentroCosto : " + backUpTipoCentroCosto);
                    } else if (!filtrarErroresLiquidacion.get(indice).getVigenciaLocalizacion().getLocalizacion().getCentrocosto().getTipocentrocosto().getNombre().equals(backUpTipoCentroCosto) && backUpTipoCentroCosto != null) {
                        filtrarErroresLiquidacion.get(indice).getVigenciaLocalizacion().getLocalizacion().getCentrocosto().getTipocentrocosto().setNombre(backUpTipoCentroCosto);
                        System.err.println("CONTROLERRORESLIQUIDACION modificarLiquidacionesLogSinGuardar backUpTipoCentroCosto : " + backUpTipoCentroCosto);
                    }
                    if (filtrarErroresLiquidacion.get(indice).getConcepto().getDescripcion() == null) {
                        filtrarErroresLiquidacion.get(indice).getConcepto().setDescripcion(backUpConcepto);
                        System.err.println("CONTROLERRORESLIQUIDACION modificarLiquidacionesLogSinGuardar backUpConcepto : " + backUpConcepto);
                    } else if (!filtrarErroresLiquidacion.get(indice).getConcepto().getDescripcion().equals(backUpConcepto) && backUpConcepto != null) {
                        filtrarErroresLiquidacion.get(indice).getConcepto().setDescripcion(backUpConcepto);
                        System.err.println("CONTROLERRORESLIQUIDACION modificarLiquidacionesLogSinGuardar backUpConcepto : " + backUpConcepto);
                    }
                    if (filtrarErroresLiquidacion.get(indice).getFormula().getNombrelargo() == null) {
                        filtrarErroresLiquidacion.get(indice).getFormula().setNombrelargo(backUpFormula);
                        System.err.println("CONTROLERRORESLIQUIDACION modificarLiquidacionesLogSinGuardar backUpValor : " + backUpFormula);
                    } else if (!filtrarErroresLiquidacion.get(indice).getFormula().getNombrelargo().equals(backUpFormula) && backUpFormula != null) {
                        filtrarErroresLiquidacion.get(indice).getFormula().setNombrelargo(backUpFormula);
                        System.err.println("CONTROLERRORESLIQUIDACION modificarLiquidacionesLogSinGuardar backUpFormula : " + backUpFormula);
                    }
                    if (filtrarErroresLiquidacion.get(indice).getError() == null) {
                        filtrarErroresLiquidacion.get(indice).setError(backUpError);
                        System.err.println("CONTROLERRORESLIQUIDACION modificarLiquidacionesLogSinGuardar backUpError : " + backUpError);
                    } else if (!filtrarErroresLiquidacion.get(indice).getError().equals(backUpError) && backUpError != null) {
                        filtrarErroresLiquidacion.get(indice).setError(backUpError);
                        System.err.println("CONTROLERRORESLIQUIDACION modificarLiquidacionesLogSinGuardar backUpError : " + backUpError);
                    }
                    if (filtrarErroresLiquidacion.get(indice).getPaquete() == null) {
                        filtrarErroresLiquidacion.get(indice).setPaquete(backUpPaquete);
                        System.err.println("CONTROLERRORESLIQUIDACION modificarLiquidacionesLogSinGuardar getPaquete : " + backUpPaquete);
                    } else if (!filtrarErroresLiquidacion.get(indice).getPaquete().equals(backUpPaquete) && backUpPaquete != null) {
                        filtrarErroresLiquidacion.get(indice).setPaquete(backUpPaquete);
                        System.err.println("CONTROLERRORESLIQUIDACION modificarLiquidacionesLogSinGuardar getPaquete : " + backUpPaquete);
                    }
                    index = -1;
                    secRegistro = null;

                }

            }
        }
        RequestContext.getCurrentInstance().update("form:datosErroresLiquidacion");
    }

    public void asignarIndex(Integer indice, int LND, int dig) {
        try {
            System.out.println("\n ENTRE A ControlErroresLiquidacion.asignarIndex \n");
            index = indice;
            if (LND == 0) {
                tipoActualizacion = 0;
            } else if (LND == 1) {
                tipoActualizacion = 1;
                System.out.println("Tipo Actualizacion: " + tipoActualizacion);
            } else if (LND == 2) {
                tipoActualizacion = 2;
            }

        } catch (Exception e) {
            System.out.println("ERROR ControlErroresLiquidacion.asignarIndex ERROR======" + e.getMessage());
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    private String infoRegistro;

    public void cancelarModificacion() {
        if (bandera == 1) {
            //CERRAR FILTRADO
            FacesContext c = FacesContext.getCurrentInstance();
            fechaInicial = (Column) c.getViewRoot().findComponent("form:datosErroresLiquidacion:fechaInicial");
            fechaInicial.setFilterStyle("display: none; visibility: hidden;");
            fechaFinal = (Column) c.getViewRoot().findComponent("form:datosErroresLiquidacion:fechaFinal");
            fechaFinal.setFilterStyle("display: none; visibility: hidden;");
            empleado = (Column) c.getViewRoot().findComponent("form:datosErroresLiquidacion:empleado");
            empleado.setFilterStyle("display: none; visibility: hidden;");
            tipoCentroCosto = (Column) c.getViewRoot().findComponent("form:datosErroresLiquidacion:tipoCentroCosto");
            tipoCentroCosto.setFilterStyle("display: none; visibility: hidden;");
            concepto = (Column) c.getViewRoot().findComponent("form:datosErroresLiquidacion:concepto");
            concepto.setFilterStyle("display: none; visibility: hidden;");
            nombreLargo = (Column) c.getViewRoot().findComponent("form:datosErroresLiquidacion:nombreLargo");
            nombreLargo.setFilterStyle("display: none; visibility: hidden;");
            fecha = (Column) c.getViewRoot().findComponent("form:datosErroresLiquidacion:fecha");
            fecha.setFilterStyle("display: none; visibility: hidden;");
            error = (Column) c.getViewRoot().findComponent("form:datosErroresLiquidacion:error");
            error.setFilterStyle("display: none; visibility: hidden;");
            paquete = (Column) c.getViewRoot().findComponent("form:datosErroresLiquidacion:paquete");
            paquete.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosErroresLiquidacion");
            bandera = 0;
            filtrarErroresLiquidacion = null;
            tipoLista = 0;
            tamano = 270;
        }

        borrarErroresLiquidacion.clear();
        crearErroresLiquidacion.clear();
        modificarErroresLiquidacion.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listErroresLiquidacion = null;
        guardado = true;
        permitirIndex = true;
        getListErroresLiquidacion();
        RequestContext context = RequestContext.getCurrentInstance();
        if (listErroresLiquidacion == null || listErroresLiquidacion.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listErroresLiquidacion.size();
        }
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
        RequestContext.getCurrentInstance().update("form:datosErroresLiquidacion");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void salir() {
        if (bandera == 1) {
            //CERRAR FILTRADO
            FacesContext c = FacesContext.getCurrentInstance();
            fechaInicial = (Column) c.getViewRoot().findComponent("form:datosErroresLiquidacion:fechaInicial");
            fechaInicial.setFilterStyle("display: none; visibility: hidden;");
            fechaFinal = (Column) c.getViewRoot().findComponent("form:datosErroresLiquidacion:fechaFinal");
            fechaFinal.setFilterStyle("display: none; visibility: hidden;");
            empleado = (Column) c.getViewRoot().findComponent("form:datosErroresLiquidacion:empleado");
            empleado.setFilterStyle("display: none; visibility: hidden;");
            tipoCentroCosto = (Column) c.getViewRoot().findComponent("form:datosErroresLiquidacion:tipoCentroCosto");
            tipoCentroCosto.setFilterStyle("display: none; visibility: hidden;");
            concepto = (Column) c.getViewRoot().findComponent("form:datosErroresLiquidacion:concepto");
            concepto.setFilterStyle("display: none; visibility: hidden;");
            nombreLargo = (Column) c.getViewRoot().findComponent("form:datosErroresLiquidacion:nombreLargo");
            nombreLargo.setFilterStyle("display: none; visibility: hidden;");
            fecha = (Column) c.getViewRoot().findComponent("form:datosErroresLiquidacion:fecha");
            fecha.setFilterStyle("display: none; visibility: hidden;");
            error = (Column) c.getViewRoot().findComponent("form:datosErroresLiquidacion:error");
            error.setFilterStyle("display: none; visibility: hidden;");
            paquete = (Column) c.getViewRoot().findComponent("form:datosErroresLiquidacion:paquete");
            paquete.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosErroresLiquidacion");

            RequestContext.getCurrentInstance().update("form:datosErroresLiquidacion");
            bandera = 0;
            filtrarErroresLiquidacion = null;
            tipoLista = 0;
            tamano = 270;
        }

        borrarErroresLiquidacion.clear();
        crearErroresLiquidacion.clear();
        modificarErroresLiquidacion.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listErroresLiquidacion = null;
        guardado = true;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosErroresLiquidacion");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void borrandoErroresLiquidaciones() {

        if (index >= 0) {
            if (tipoLista == 0) {
                System.out.println("Entro a borrandoErroresLiquidaciones");
                if (!modificarErroresLiquidacion.isEmpty() && modificarErroresLiquidacion.contains(listErroresLiquidacion.get(index))) {
                    int modIndex = modificarErroresLiquidacion.indexOf(listErroresLiquidacion.get(index));
                    modificarErroresLiquidacion.remove(modIndex);
                    borrarErroresLiquidacion.add(listErroresLiquidacion.get(index));
                } else if (!crearErroresLiquidacion.isEmpty() && crearErroresLiquidacion.contains(listErroresLiquidacion.get(index))) {
                    int crearIndex = crearErroresLiquidacion.indexOf(listErroresLiquidacion.get(index));
                    crearErroresLiquidacion.remove(crearIndex);
                } else {
                    borrarErroresLiquidacion.add(listErroresLiquidacion.get(index));
                }
                listErroresLiquidacion.remove(index);
            }
            if (tipoLista == 1) {
                System.out.println("borrandoErroresLiquidaciones ");
                if (!modificarErroresLiquidacion.isEmpty() && modificarErroresLiquidacion.contains(filtrarErroresLiquidacion.get(index))) {
                    int modIndex = modificarErroresLiquidacion.indexOf(filtrarErroresLiquidacion.get(index));
                    modificarErroresLiquidacion.remove(modIndex);
                    borrarErroresLiquidacion.add(filtrarErroresLiquidacion.get(index));
                } else if (!crearErroresLiquidacion.isEmpty() && crearErroresLiquidacion.contains(filtrarErroresLiquidacion.get(index))) {
                    int crearIndex = crearErroresLiquidacion.indexOf(filtrarErroresLiquidacion.get(index));
                    crearErroresLiquidacion.remove(crearIndex);
                } else {
                    borrarErroresLiquidacion.add(filtrarErroresLiquidacion.get(index));
                }
                int VCIndex = listErroresLiquidacion.indexOf(filtrarErroresLiquidacion.get(index));
                listErroresLiquidacion.remove(VCIndex);
                filtrarErroresLiquidacion.remove(index);

            }
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosErroresLiquidacion");
            infoRegistro = "Cantidad de registros: " + listErroresLiquidacion.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");

            index = -1;
            secRegistro = null;

            if (guardado == true) {
                guardado = false;
            }
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }

    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            tamano = 250;
            fechaInicial = (Column) c.getViewRoot().findComponent("form:datosErroresLiquidacion:fechaInicial");
            fechaInicial.setFilterStyle("width: 85% !important;");
            fechaFinal = (Column) c.getViewRoot().findComponent("form:datosErroresLiquidacion:fechaFinal");
            fechaFinal.setFilterStyle("width: 85% !important;");
            empleado = (Column) c.getViewRoot().findComponent("form:datosErroresLiquidacion:empleado");
            empleado.setFilterStyle("width: 85% !important;");
            tipoCentroCosto = (Column) c.getViewRoot().findComponent("form:datosErroresLiquidacion:tipoCentroCosto");
            tipoCentroCosto.setFilterStyle("width: 85% !important;");
            concepto = (Column) c.getViewRoot().findComponent("form:datosErroresLiquidacion:concepto");
            concepto.setFilterStyle("width: 85% !important;");
            nombreLargo = (Column) c.getViewRoot().findComponent("form:datosErroresLiquidacion:nombreLargo");
            nombreLargo.setFilterStyle("width: 85% !important;");
            fecha = (Column) c.getViewRoot().findComponent("form:datosErroresLiquidacion:fecha");
            fecha.setFilterStyle("width: 85% !important;");
            error = (Column) c.getViewRoot().findComponent("form:datosErroresLiquidacion:error");
            error.setFilterStyle("width: 85% !important;");
            paquete = (Column) c.getViewRoot().findComponent("form:datosErroresLiquidacion:paquete");
            paquete.setFilterStyle("width: 85% !important;");
            RequestContext.getCurrentInstance().update("form:datosErroresLiquidacion");

            System.out.println("Activar");
            bandera = 1;
        } else if (bandera == 1) {
            System.out.println("Desactivar");
            tamano = 270;
            fechaInicial = (Column) c.getViewRoot().findComponent("form:datosErroresLiquidacion:fechaInicial");
            fechaInicial.setFilterStyle("display: none; visibility: hidden;");
            fechaFinal = (Column) c.getViewRoot().findComponent("form:datosErroresLiquidacion:fechaFinal");
            fechaFinal.setFilterStyle("display: none; visibility: hidden;");
            empleado = (Column) c.getViewRoot().findComponent("form:datosErroresLiquidacion:empleado");
            empleado.setFilterStyle("display: none; visibility: hidden;");
            tipoCentroCosto = (Column) c.getViewRoot().findComponent("form:datosErroresLiquidacion:tipoCentroCosto");
            tipoCentroCosto.setFilterStyle("display: none; visibility: hidden;");
            concepto = (Column) c.getViewRoot().findComponent("form:datosErroresLiquidacion:concepto");
            concepto.setFilterStyle("display: none; visibility: hidden;");
            nombreLargo = (Column) c.getViewRoot().findComponent("form:datosErroresLiquidacion:nombreLargo");
            nombreLargo.setFilterStyle("display: none; visibility: hidden;");
            fecha = (Column) c.getViewRoot().findComponent("form:datosErroresLiquidacion:fecha");
            fecha.setFilterStyle("display: none; visibility: hidden;");
            error = (Column) c.getViewRoot().findComponent("form:datosErroresLiquidacion:error");
            error.setFilterStyle("display: none; visibility: hidden;");
            paquete = (Column) c.getViewRoot().findComponent("form:datosErroresLiquidacion:paquete");
            paquete.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosErroresLiquidacion");

            RequestContext.getCurrentInstance().update("form:datosErroresLiquidacion");
            bandera = 0;
            filtrarErroresLiquidacion = null;
            tipoLista = 0;
        }
    }

    public void editarCelda() {
        if (index >= 0) {
            if (tipoLista == 0) {
                editarErroresLiquidacion = listErroresLiquidacion.get(index);
            }
            if (tipoLista == 1) {
                editarErroresLiquidacion = filtrarErroresLiquidacion.get(index);
            }

            RequestContext context = RequestContext.getCurrentInstance();
            System.out.println("Entro a editar... valor celda: " + cualCelda);
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaInicial");
                RequestContext.getCurrentInstance().execute("PF('editarFechaInicial').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaHasta");
                RequestContext.getCurrentInstance().execute("PF('editarFechaHasta').show()");
                cualCelda = -1;
            } else if (cualCelda == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarEmpleado");
                RequestContext.getCurrentInstance().execute("PF('editarEmpleado').show()");
                cualCelda = -1;
            } else if (cualCelda == 3) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarTipoCentroCosto");
                RequestContext.getCurrentInstance().execute("PF('editarTipoCentroCosto').show()");
                cualCelda = -1;
            } else if (cualCelda == 4) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarConcepto");
                RequestContext.getCurrentInstance().execute("PF('editarConcepto').show()");
                cualCelda = -1;
            } else if (cualCelda == 5) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarFormula");
                RequestContext.getCurrentInstance().execute("PF('editarFormula').show()");
                cualCelda = -1;
            } else if (cualCelda == 6) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaE");
                RequestContext.getCurrentInstance().execute("PF('editarFechaE').show()");
                cualCelda = -1;
            } else if (cualCelda == 7) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarErrorE");
                RequestContext.getCurrentInstance().execute("PF('editarErrorE').show()");
                cualCelda = -1;
            } else if (cualCelda == 8) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarPaqueteE");
                RequestContext.getCurrentInstance().execute("PF('editarPaqueteE').show()");
                cualCelda = -1;
            }

        }
        index = -1;
        secRegistro = null;
    }

    public void limpiarNuevoErroresLiquidacion() {
        nuevoErroresLiquidacion = new ErroresLiquidacion();
        nuevoErroresLiquidacion.setEmpleado(new Empleados());
        nuevoErroresLiquidacion.setConcepto(new Conceptos());
        nuevoErroresLiquidacion.setFormula(new Formulas());
        nuevoErroresLiquidacion.setVigenciaLocalizacion(new VigenciasLocalizaciones());
    }

    public void revisarDialogoGuardar() {

        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:confirmarGuardar");
        RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
    }

    public void revisarDialogoBorrarTodo() {
        if (listErroresLiquidacion != null) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:confirmarBorrarTodo");
            RequestContext.getCurrentInstance().execute("PF('confirmarBorrarTodo').show()");
        }
    }

    public void borrarTodosErroresLiquidacion() {

        RequestContext context = RequestContext.getCurrentInstance();
        administrarErroresLiquidacion.borrarTodosErroresLiquidacion();
        index = -1;
        secRegistro = null;
        guardado = true;
        borrarTodo = true;
        listErroresLiquidacion = null;
        getListErroresLiquidacion();
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("form:BORRARTODO");
        RequestContext.getCurrentInstance().update("form:datosErroresLiquidacion");
        infoRegistro = "Cantidad de registros: " + listErroresLiquidacion.size();
        RequestContext.getCurrentInstance().update("form:informacionRegistro");

    }

    public void guardarErroresLiquidacion() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (guardado == false) {
            System.out.println("Realizando guardarClasesPensiones");
            if (!borrarErroresLiquidacion.isEmpty()) {
                administrarErroresLiquidacion.borrarErroresLiquidaciones(borrarErroresLiquidacion);
                //mostrarBorrados
                registrosBorrados = borrarErroresLiquidacion.size();
                RequestContext.getCurrentInstance().update("form:mostrarBorrados");
                RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
                borrarErroresLiquidacion.clear();
            }
            System.out.println("Se guardaron los datos con exito");
            listErroresLiquidacion = null;
            RequestContext.getCurrentInstance().update("form:datosErroresLiquidacion");
            k = 0;
            guardado = true;
            FacesMessage msg = new FacesMessage("Información", "Se guardarón los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }
        index = -1;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");

    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosErroresLiquidacionExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "ERRORESLIQUIDACION", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosErroresLiquidacionExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "ERRORESLIQUIDACION", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        System.out.println("lol");
        if (!listErroresLiquidacion.isEmpty()) {
            if (secRegistro != null) {
                System.out.println("lol 2");
                int resultado = administrarRastros.obtenerTabla(secRegistro, "ERRORESLIQUIDACION"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
            } else {
                RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
            }
        } else {
            if (administrarRastros.verificarHistoricosTabla("ERRORESLIQUIDACION")) { // igual acá
                RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
            } else {
                RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
            }

        }
        index = -1;
    }

    //*/*/*/*/*/*/*/*/*/*-/-*//-*/-*/*/*-*/-*/-*/*/*/*/*/---/*/*/*/*/-*/-*/-*/-*/-*/
    public List<ErroresLiquidacion> getListErroresLiquidacion() {
        if (listErroresLiquidacion == null) {
            System.out.println("ControlErroresLiquidacion getListErroresLiquidacion");
            listErroresLiquidacion = administrarErroresLiquidacion.consultarErroresLiquidacionEmpleado(secuenciaEmpleado);
        }

        RequestContext context = RequestContext.getCurrentInstance();
        if (listErroresLiquidacion == null || listErroresLiquidacion.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
            borrarTodo = true;
        } else {
            infoRegistro = "Cantidad de registros: " + listErroresLiquidacion.size();
            borrarTodo = false;
        }
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
        RequestContext.getCurrentInstance().update("form:BORRARTODO");

        return listErroresLiquidacion;
    }

    public void setListErroresLiquidacion(List<ErroresLiquidacion> listErroresLiquidacion) {
        this.listErroresLiquidacion = listErroresLiquidacion;
    }

    public List<ErroresLiquidacion> getFiltrarErroresLiquidacion() {
        return filtrarErroresLiquidacion;
    }

    public void setFiltrarErroresLiquidacion(List<ErroresLiquidacion> filtrarErroresLiquidacion) {
        this.filtrarErroresLiquidacion = filtrarErroresLiquidacion;
    }

    public ErroresLiquidacion getNuevoErroresLiquidacion() {
        return nuevoErroresLiquidacion;
    }

    public void setNuevoErroresLiquidacion(ErroresLiquidacion nuevoErroresLiquidacion) {
        this.nuevoErroresLiquidacion = nuevoErroresLiquidacion;
    }

    public ErroresLiquidacion getEditarErroresLiquidacion() {
        return editarErroresLiquidacion;
    }

    public void setEditarErroresLiquidacion(ErroresLiquidacion editarErroresLiquidacion) {
        this.editarErroresLiquidacion = editarErroresLiquidacion;
    }

    public BigInteger getSecRegistro() {
        return secRegistro;
    }

    public void setSecRegistro(BigInteger secRegistro) {
        this.secRegistro = secRegistro;
    }

    public boolean isGuardado() {
        return guardado;
    }

    public void setGuardado(boolean guardado) {
        this.guardado = guardado;
    }

    public int getTamano() {
        return tamano;
    }

    public void setTamano(int tamano) {
        this.tamano = tamano;
    }

    public ErroresLiquidacion getErroresLiquidacionSeleccionado() {
        return erroresLiquidacionesSeleccionado;
    }

    public void setErroresLiquidacionSeleccionado(ErroresLiquidacion clasesPensionesSeleccionado) {
        this.erroresLiquidacionesSeleccionado = clasesPensionesSeleccionado;
    }

    public String getInfoRegistro() {
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }
    private String infoRegistroEmpleados;

    public boolean isAceptar() {
        return aceptar;
    }

    public void setAceptar(boolean aceptar) {
        this.aceptar = aceptar;
    }

    public int getRegistrosBorrados() {
        return registrosBorrados;
    }

    public void setRegistrosBorrados(int registrosBorrados) {
        this.registrosBorrados = registrosBorrados;
    }

    public boolean isBorrarTodo() {
        return borrarTodo;
    }

    public void setBorrarTodo(boolean borrarTodo) {
        this.borrarTodo = borrarTodo;
    }

}
