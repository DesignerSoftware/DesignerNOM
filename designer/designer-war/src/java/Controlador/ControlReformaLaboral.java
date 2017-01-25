package Controlador;


import Entidades.DetallesReformasLaborales;
import Entidades.ReformasLaborales;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarRastrosInterface;
import InterfaceAdministrar.AdministrarReformasLaboralesInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
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
 * @author PROYECTO01
 */
@ManagedBean
@SessionScoped
public class ControlReformaLaboral implements Serializable {

    @EJB
    AdministrarReformasLaboralesInterface administrarReformaLaboral;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    //
    private List<ReformasLaborales> listaReformasLaborales;
    private List<ReformasLaborales> filtrarListaReformasLaborales;
    ///////
    private List<DetallesReformasLaborales> listaDetallesReformasLaborales;
    private List<DetallesReformasLaborales> filtrarListaDetallesReformasLaborales;
    //Activo/Desactivo Crtl + F11
    private int bandera, banderaDetalle;
    //Columnas Tabla VC
    private Column reformaCodigo, reformaNombre, reformaIntegral;
    private Column detalleTipoPago, detalleFactor;
    //Otros
    private boolean aceptar;
    private int index, indexDetalle, indexAux;
    //modificar
    private List<ReformasLaborales> listReformasLaboralesModificar;
    private List<DetallesReformasLaborales> listDetallesReformasLaboralesModificar;
    private boolean guardado, guardadoDetalles;
    //crear 
    private ReformasLaborales nuevoReformaLaboral;
    private DetallesReformasLaborales nuevoDetalleReformaLaboral;
    private List<ReformasLaborales> listReformasLaboralesCrear;
    private List<DetallesReformasLaborales> listDetallesReformasLaboralesCrear;
    private BigInteger l;
    private int k;
    //borrar 
    private List<ReformasLaborales> listReformasLaboralesBorrar;
    private List<DetallesReformasLaborales> listDetallesReformasLaboralesBorrar;
    //editar celda
    private ReformasLaborales editarReformaLaboral;
    private DetallesReformasLaborales editarDetalleReformaLaboral;
    private int cualCelda, tipoLista, cualCeldaDetalles, tipoListaDetalles;
    //duplicar
    private ReformasLaborales duplicarReformaLaboral;
    private DetallesReformasLaborales duplicarDetalleReformaLaboral;
    private BigInteger secRegistro, secRegistroDetalles;
    private BigInteger backUpSecRegistro, backUpSecRegistroDetalles;
    private String msnConfirmarRastro, msnConfirmarRastroHistorico;
    private BigInteger backUp;
    private String nombreTablaRastro;
    private String nombreXML, nombreTabla;
    //////////////////////
    private int tipoActualizacion;
    private short auxCodigoReformaLaboral;
    private String auxNombreReformaLaboral;
    //
    private boolean cambiosPagina;
    private String altoTablaReforma, altoTablaDetalles;
    //
    private String nombreReformaClonar;
    private short codigoReformaClonar;
    //
    private List<ReformasLaborales> lovReformasLaborales;
    private List<ReformasLaborales> filtrarLovReformasLaborales;
    private ReformasLaborales reformaLaboralSeleccionado;
    //
    private ReformasLaborales reformaLaboralAClonar;
    //
    private String auxNombreClonar;
    private short auxCodigoClonar;
    //
    private String auxTipoPagoDetalle;
    private BigDecimal auxFactorDetalle;
    //
    private ReformasLaborales reformaActualTabla;
    private DetallesReformasLaborales detalleActualTabla;
    //
    private String infoRegistroReforma;

    public ControlReformaLaboral() {
        reformaActualTabla = new ReformasLaborales();
        detalleActualTabla = new DetallesReformasLaborales();
        reformaLaboralAClonar = new ReformasLaborales();
        reformaLaboralSeleccionado = new ReformasLaborales();
        lovReformasLaborales = null;
        altoTablaReforma = "157";
        altoTablaDetalles = "135";
        cambiosPagina = true;
        tipoActualizacion = -1;
        indexDetalle = -1;
        backUpSecRegistro = null;
        listaReformasLaborales = null;
        aceptar = true;
        listDetallesReformasLaboralesModificar = new ArrayList<DetallesReformasLaborales>();
        listDetallesReformasLaboralesBorrar = new ArrayList<DetallesReformasLaborales>();
        listDetallesReformasLaboralesCrear = new ArrayList<DetallesReformasLaborales>();
        //
        listReformasLaboralesCrear = new ArrayList<ReformasLaborales>();
        listReformasLaboralesBorrar = new ArrayList<ReformasLaborales>();
        listReformasLaboralesModificar = new ArrayList<ReformasLaborales>();
        k = 0;
        editarReformaLaboral = new ReformasLaborales();
        editarDetalleReformaLaboral = new DetallesReformasLaborales();
        cualCelda = -1;
        cualCeldaDetalles = -1;
        tipoListaDetalles = 0;
        tipoLista = 0;
        guardado = true;
        guardadoDetalles = true;
        nuevoReformaLaboral = new ReformasLaborales();
        nuevoDetalleReformaLaboral = new DetallesReformasLaborales();
        duplicarDetalleReformaLaboral = new DetallesReformasLaborales();
        duplicarReformaLaboral = new ReformasLaborales();
        secRegistro = null;
        secRegistroDetalles = null;
        backUpSecRegistroDetalles = null;
        bandera = 0;
        banderaDetalle = 0;
    }

       private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();
   mapParametros.put ("paginaAnterior", paginaAnterior);
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
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarReformaLaboral.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void inicializarPagina() {
        listaDetallesReformasLaborales = null;
        listaReformasLaborales = null;
        getListaReformasLaborales();
        int tam = listaReformasLaborales.size();
        if (tam >= 1) {
            index = 0;
            reformaActualTabla = listaReformasLaborales.get(0);
            getListaDetallesReformasLaborales();
        }
    }

    public void posicionRegistroTablaReforma() {
        int pos = 0;
        if (tipoLista == 0) {
            pos = listaReformasLaborales.indexOf(reformaActualTabla);
        }
        if (tipoListaDetalles == 1) {
            pos = filtrarListaReformasLaborales.indexOf(reformaActualTabla);
        }
        index = pos;
        indexDetalle = -1;
        listaDetallesReformasLaborales = null;
        getListaDetallesReformasLaborales();
        detalleActualTabla = new DetallesReformasLaborales();
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosDetalleReformaLaboral");
        if (banderaDetalle == 1) {
            altoTablaDetalles = "135";
            detalleFactor = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDetalleReformaLaboral:detalleFactor");
            detalleFactor.setFilterStyle("display: none; visibility: hidden;");
            detalleTipoPago = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDetalleReformaLaboral:detalleTipoPago");
            detalleTipoPago.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosDetalleReformaLaboral");
            banderaDetalle = 0;
            filtrarListaDetallesReformasLaborales = null;
            tipoListaDetalles = 0;
        }
    }

    public void posicionRegistroTablaDetalle() {
        int pos = 0;
        if (tipoListaDetalles == 0) {
            pos = listaDetallesReformasLaborales.indexOf(detalleActualTabla);
        }
        if (tipoListaDetalles == 1) {
            pos = filtrarListaDetallesReformasLaborales.indexOf(detalleActualTabla);
        }
        indexDetalle = pos;
        if (bandera == 1) {
            altoTablaReforma = "157";
            reformaCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosReformaLaboral:reformaCodigo");
            reformaCodigo.setFilterStyle("display: none; visibility: hidden;");
            reformaNombre = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosReformaLaboral:reformaNombre");
            reformaNombre.setFilterStyle("display: none; visibility: hidden;");
            reformaIntegral = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosReformaLaboral:reformaIntegral");
            reformaIntegral.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosReformaLaboral");
            bandera = 0;
            filtrarListaReformasLaborales = null;
            tipoLista = 0;
        }
    }

    public boolean validarCamposNulosReformaLaboral(int i) {
        boolean retorno = true;
        if (i == 0) {
            ReformasLaborales aux = new ReformasLaborales();
            if (tipoLista == 0) {
                aux = listaReformasLaborales.get(index);
            }
            if (tipoLista == 1) {
                aux = filtrarListaReformasLaborales.get(index);
            }
            if (aux.getCodigo() >= 0 || aux.getNombre().isEmpty()) {
                retorno = false;
            }
        }
        if (i == 1) {
            if (nuevoReformaLaboral.getCodigo() >= 0 || nuevoReformaLaboral.getNombre().isEmpty()) {
                retorno = false;
            }
        }
        if (i == 2) {
            if (duplicarReformaLaboral.getCodigo() >= 0 || duplicarReformaLaboral.getNombre().isEmpty()) {
                retorno = false;
            }
        }
        return retorno;
    }

    public boolean validarCamposNulosDetalleReformaLaboral(int i) {
        boolean retorno = true;
        if (i == 0) {
            DetallesReformasLaborales aux = new DetallesReformasLaborales();
            if (tipoLista == 0) {
                aux = listaDetallesReformasLaborales.get(indexDetalle);
            }
            if (tipoLista == 1) {
                aux = filtrarListaDetallesReformasLaborales.get(indexDetalle);
            }
            if (aux.getFactor() == null || aux.getTipopago().isEmpty()) {
                retorno = false;
            }
        }
        if (i == 1) {
            if (nuevoDetalleReformaLaboral.getFactor() == null || nuevoDetalleReformaLaboral.getTipopago().isEmpty()) {
                retorno = false;
            }
        }
        if (i == 2) {
            if (duplicarDetalleReformaLaboral.getFactor() == null || duplicarDetalleReformaLaboral.getTipopago().isEmpty()) {
                retorno = false;
            }
        }
        return retorno;
    }

    public void procesoModificacionReformaLaboral(int i) {
        index = i;
        boolean respuesta = validarCamposNulosReformaLaboral(0);
        if (respuesta == true) {
            modificarReformaLaboral(i);
        } else {
            if (tipoLista == 0) {
                listaReformasLaborales.get(index).setCodigo(auxCodigoReformaLaboral);
                listaReformasLaborales.get(index).setNombre(auxNombreReformaLaboral);
            }
            if (tipoLista == 1) {
                filtrarListaReformasLaborales.get(index).setCodigo(auxCodigoReformaLaboral);
                filtrarListaReformasLaborales.get(index).setNombre(auxNombreReformaLaboral);
            }
            index = -1;
            secRegistro = null;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosReformaLaboral");
            RequestContext.getCurrentInstance().execute("PF('errorDatosNullReformaLaboral').show()");
        }
    }

    public void modificarReformaLaboral(int indice) {
        int tamDes = 0;
        if (tipoLista == 0) {
            tamDes = listaReformasLaborales.get(indice).getNombre().length();
        }
        if (tipoLista == 1) {
            tamDes = filtrarListaReformasLaborales.get(indice).getNombre().length();
        }
        if (tamDes >= 1 && tamDes <= 30) {
            if (tipoLista == 0) {
                String textM = listaReformasLaborales.get(indice).getNombre().toUpperCase();
                listaReformasLaborales.get(indice).setNombre(textM);
                if (!listReformasLaboralesCrear.contains(listaReformasLaborales.get(indice))) {
                    if (listReformasLaboralesModificar.isEmpty()) {
                        listReformasLaboralesModificar.add(listaReformasLaborales.get(indice));
                    } else if (!listReformasLaboralesModificar.contains(listaReformasLaborales.get(indice))) {
                        listReformasLaboralesModificar.add(listaReformasLaborales.get(indice));
                    }
                    if (guardado == true) {
                        guardado = false;
                    }
                }
            }
            if (tipoLista == 1) {
                String textM = filtrarListaReformasLaborales.get(indice).getNombre().toUpperCase();
                filtrarListaReformasLaborales.get(indice).setNombre(textM);
                if (!listReformasLaboralesCrear.contains(filtrarListaReformasLaborales.get(indice))) {
                    if (listReformasLaboralesModificar.isEmpty()) {
                        listReformasLaboralesModificar.add(filtrarListaReformasLaborales.get(indice));
                    } else if (!listReformasLaboralesModificar.contains(filtrarListaReformasLaborales.get(indice))) {
                        listReformasLaboralesModificar.add(filtrarListaReformasLaborales.get(indice));
                    }
                    if (guardado == true) {
                        guardado = false;
                        //RequestContext.getCurrentInstance().update("form:ACEPTAR");
                    }
                }
            }
            index = -1;
            secRegistro = null;
            cambiosPagina = false;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosReformaLaboral");
        } else {
            if (tipoLista == 0) {
                listaReformasLaborales.get(indice).setNombre(auxNombreReformaLaboral);
            }
            if (tipoLista == 1) {
                filtrarListaReformasLaborales.get(indice).setNombre(auxNombreReformaLaboral);
            }
            index = -1;
            secRegistro = null;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosReformaLaboral");
            RequestContext.getCurrentInstance().execute("PF('errorNombreReformaLaboral').show()");
        }

    }

    public void procesoModificacionDetalleReformaLaboral(int i) {
        index = i;
        boolean respuesta = validarCamposNulosDetalleReformaLaboral(0);
        if (respuesta == true) {
            modificarDetalleReformaLaboral(i);
        } else {
            if (tipoLista == 0) {
                listaDetallesReformasLaborales.get(indexDetalle).setFactor(auxFactorDetalle);
                listaDetallesReformasLaborales.get(indexDetalle).setTipopago(auxTipoPagoDetalle);
            }
            if (tipoLista == 1) {
                filtrarListaDetallesReformasLaborales.get(indexDetalle).setFactor(auxFactorDetalle);
                filtrarListaDetallesReformasLaborales.get(indexDetalle).setTipopago(auxTipoPagoDetalle);
            }
            indexDetalle = -1;
            secRegistroDetalles = null;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosReformaLaboral");
            RequestContext.getCurrentInstance().execute("PF('errorDatosNullDetalleReforma').show()");
        }
    }

    public void modificarDetalleReformaLaboral(int indice) {
        int tam = 0;
        if (tipoLista == 0) {
            tam = listaDetallesReformasLaborales.get(indice).getTipopago().length();
        }
        if (tipoLista == 1) {
            tam = filtrarListaDetallesReformasLaborales.get(indice).getTipopago().length();
        }
        if (tam >= 1 && tam <= 10) {
            if (tipoListaDetalles == 0) {
                if (!listDetallesReformasLaboralesCrear.contains(listaDetallesReformasLaborales.get(indice))) {
                    if (listDetallesReformasLaboralesModificar.isEmpty()) {
                        listDetallesReformasLaboralesModificar.add(listaDetallesReformasLaborales.get(indice));
                    } else if (!listDetallesReformasLaboralesModificar.contains(listaDetallesReformasLaborales.get(indice))) {
                        listDetallesReformasLaboralesModificar.add(listaDetallesReformasLaborales.get(indice));
                    }
                    if (guardadoDetalles == true) {
                        guardadoDetalles = false;
                    }
                }
            }
            if (tipoListaDetalles == 1) {
                if (!listDetallesReformasLaboralesCrear.contains(filtrarListaDetallesReformasLaborales.get(indice))) {
                    if (listDetallesReformasLaboralesModificar.isEmpty()) {
                        listDetallesReformasLaboralesModificar.add(filtrarListaDetallesReformasLaborales.get(indice));
                    } else if (!listDetallesReformasLaboralesModificar.contains(filtrarListaDetallesReformasLaborales.get(indice))) {
                        listDetallesReformasLaboralesModificar.add(filtrarListaDetallesReformasLaborales.get(indice));
                    }
                    if (guardadoDetalles == true) {
                        guardadoDetalles = false;
                        //RequestContext.getCurrentInstance().update("form:ACEPTAR");
                    }
                }
            }
            indexDetalle = -1;
            secRegistroDetalles = null;
            cambiosPagina = false;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosDetalleReformaLaboral");
        } else {
            if (tipoLista == 0) {
                listaDetallesReformasLaborales.get(indice).setTipopago(auxTipoPagoDetalle);
            }
            if (tipoLista == 1) {
                filtrarListaDetallesReformasLaborales.get(indice).setTipopago(auxTipoPagoDetalle);
            }
            indexDetalle = -1;
            secRegistroDetalles = null;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosReformaLaboral");
            RequestContext.getCurrentInstance().execute("PF('errorTipoPagoDetalleReforma').show()");
        }
    }

    public void cambiarIndice(int indice, int celda) {
        if (guardadoDetalles == true) {
            index = indice;
            cualCelda = celda;
            if (tipoLista == 0) {
                //reformaActualTabla = listaReformasLaborales.get(index);
                auxCodigoReformaLaboral = listaReformasLaborales.get(index).getCodigo();
                secRegistro = listaReformasLaborales.get(index).getSecuencia();
                auxNombreReformaLaboral = listaReformasLaborales.get(index).getNombre();
            }
            if (tipoLista == 1) {
                //reformaActualTabla = filtrarListaReformasLaborales.get(index);
                auxCodigoReformaLaboral = filtrarListaReformasLaborales.get(index).getCodigo();
                secRegistro = filtrarListaReformasLaborales.get(index).getSecuencia();
                auxNombreReformaLaboral = filtrarListaReformasLaborales.get(index).getNombre();
            }
            indexAux = indice;
            indexDetalle = -1;
            listaDetallesReformasLaborales = null;
            getListaDetallesReformasLaborales();
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosDetalleReformaLaboral");
            if (banderaDetalle == 1) {
                altoTablaDetalles = "135";
                detalleFactor = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDetalleReformaLaboral:detalleFactor");
                detalleFactor.setFilterStyle("display: none; visibility: hidden;");
                detalleTipoPago = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDetalleReformaLaboral:detalleTipoPago");
                detalleTipoPago.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosDetalleReformaLaboral");
                banderaDetalle = 0;
                filtrarListaDetallesReformasLaborales = null;
                tipoListaDetalles = 0;
            }
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
        }
    }

    public void cambiarIndiceDetalle(int indice, int celda) {
        indexDetalle = indice;
        index = -1;
        cualCeldaDetalles = celda;
        if (tipoListaDetalles == 0) {
            //detalleActualTabla = listaDetallesReformasLaborales.get(indexDetalle);
            secRegistroDetalles = listaDetallesReformasLaborales.get(indexDetalle).getSecuencia();
            auxFactorDetalle = listaDetallesReformasLaborales.get(indexDetalle).getFactor();
            auxTipoPagoDetalle = listaDetallesReformasLaborales.get(indexDetalle).getTipopago();
        }
        if (tipoListaDetalles == 1) {
            //detalleActualTabla = filtrarListaDetallesReformasLaborales.get(indexDetalle);
            secRegistroDetalles = filtrarListaDetallesReformasLaborales.get(indexDetalle).getSecuencia();
            auxFactorDetalle = filtrarListaDetallesReformasLaborales.get(indexDetalle).getFactor();
            auxTipoPagoDetalle = filtrarListaDetallesReformasLaborales.get(indexDetalle).getTipopago();
        }
        if (bandera == 1) {
            altoTablaReforma = "157";
            reformaCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosReformaLaboral:reformaCodigo");
            reformaCodigo.setFilterStyle("display: none; visibility: hidden;");
            reformaNombre = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosReformaLaboral:reformaNombre");
            reformaNombre.setFilterStyle("display: none; visibility: hidden;");
            reformaIntegral = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosReformaLaboral:reformaIntegral");
            reformaIntegral.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosReformaLaboral");
            bandera = 0;
            filtrarListaReformasLaborales = null;
            tipoLista = 0;
        }
    }
    //GUARDAR

    public void guardarYSalir() {
        guardarGeneral();
        salir();
    }

    public void cancelarYSalir() {
        cancelarModificacionGeneral();
        salir();
    }

    public void guardarGeneral() {
        if (guardado == false || guardadoDetalles == false) {
            if (guardado == false) {
                guardarCambiosReformaLaboral();
            }
            if (guardadoDetalles == false) {
                guardarCambiosDetalleReformaLaboral();
            }
            cambiosPagina = true;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
    }

    public void guardarCambiosReformaLaboral() {
        try {
            if (!listReformasLaboralesBorrar.isEmpty()) {
                for (int i = 0; i < listReformasLaboralesBorrar.size(); i++) {
                    administrarReformaLaboral.borrarReformaLaboral(listReformasLaboralesBorrar);
                }
                listReformasLaboralesBorrar.clear();
            }
            if (!listReformasLaboralesCrear.isEmpty()) {
                for (int i = 0; i < listReformasLaboralesCrear.size(); i++) {
                    administrarReformaLaboral.crearReformaLaboral(listReformasLaboralesCrear);
                }
                listReformasLaboralesCrear.clear();
            }
            if (!listReformasLaboralesModificar.isEmpty()) {
                administrarReformaLaboral.editarReformaLaboral(listReformasLaboralesModificar);
                listReformasLaboralesModificar.clear();
            }
            listaReformasLaborales = null;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosReformaLaboral");
            guardado = true;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            k = 0;

            index = -1;
            secRegistro = null;
            FacesMessage msg = new FacesMessage("Información", "Los datos de Reforma Laboral se guardaron con Éxito.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        } catch (Exception e) {
            System.out.println("Error guardarCambiosReformaLaboral : " + e.toString());
            FacesMessage msg = new FacesMessage("Información", "Ocurrio un error en el guardado de Reforma Laboral, intente nuevamente.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }
    }

    public void guardarCambiosDetalleReformaLaboral() {
        try {
            if (!listDetallesReformasLaboralesBorrar.isEmpty()) {
                for (int i = 0; i < listDetallesReformasLaboralesBorrar.size(); i++) {
                    administrarReformaLaboral.borrarDetalleReformaLaboral(listDetallesReformasLaboralesBorrar);
                }
                listDetallesReformasLaboralesBorrar.clear();
            }
            if (!listDetallesReformasLaboralesCrear.isEmpty()) {
                for (int i = 0; i < listDetallesReformasLaboralesCrear.size(); i++) {
                    administrarReformaLaboral.crearDetalleReformaLaboral(listDetallesReformasLaboralesCrear);
                }
                listDetallesReformasLaboralesCrear.clear();
            }
            if (!listDetallesReformasLaboralesModificar.isEmpty()) {
                administrarReformaLaboral.editarDetalleReformaLaboral(listDetallesReformasLaboralesModificar);
                listDetallesReformasLaboralesModificar.clear();
            }
            listaDetallesReformasLaborales = null;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosDetalleReformaLaboral");
            guardadoDetalles = true;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            k = 0;

            indexDetalle = -1;
            secRegistroDetalles = null;
            FacesMessage msg = new FacesMessage("Información", "Los datos de Detalle Reforma Laboral se guardaron con Éxito.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        } catch (Exception e) {
            System.out.println("Error guardarCambiosDetalleReformaLaboral : " + e.toString());
            FacesMessage msg = new FacesMessage("Información", "Ocurrio un error en el guardado de Detalle Reforma Laboral, intente nuevamente.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }
    }
    //CANCELAR MODIFICACIONES

    /**
     * Cancela las modificaciones realizas en la pagina
     */
    public void cancelarModificacionGeneral() {
        if (guardado == false) {
            cancelarModificacionReformaLaboral();
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosReformaLaboral");
        }
        if (guardadoDetalles == false) {
            cancelarModificacionDetalleReformaLaboral();
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosDetalleReformaLaboral");
        }
        cancelarProcesoClonado();
        cambiosPagina = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void cancelarProcesoClonado() {
        codigoReformaClonar = 0;
        nombreReformaClonar = " ";
        reformaLaboralAClonar = new ReformasLaborales();
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:codigoReformaLaboralClonar");
        RequestContext.getCurrentInstance().update("form:nombreReformaLaboralClonar");
        RequestContext.getCurrentInstance().update("form:codigoReformaLaboralClonarBase");
        RequestContext.getCurrentInstance().update("form:nombreReformaLaboralClonarBase");
    }

    public void cancelarModificacionReformaLaboral() {
        if (bandera == 1) {
            altoTablaReforma = "157";
            reformaCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosReformaLaboral:reformaCodigo");
            reformaCodigo.setFilterStyle("display: none; visibility: hidden;");
            reformaNombre = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosReformaLaboral:reformaNombre");
            reformaNombre.setFilterStyle("display: none; visibility: hidden;");
            reformaIntegral = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosReformaLaboral:reformaIntegral");
            reformaIntegral.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosReformaLaboral");
            bandera = 0;
            filtrarListaReformasLaborales = null;
            tipoLista = 0;
        }
        listReformasLaboralesBorrar.clear();
        listReformasLaboralesCrear.clear();
        listReformasLaboralesModificar.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listaReformasLaborales = null;
        guardado = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosReformaLaboral");
    }

    public void cancelarModificacionDetalleReformaLaboral() {
        if (banderaDetalle == 1) {
            altoTablaDetalles = "135";
            detalleFactor = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDetalleReformaLaboral:detalleFactor");
            detalleFactor.setFilterStyle("display: none; visibility: hidden;");
            detalleTipoPago = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDetalleReformaLaboral:detalleTipoPago");
            detalleTipoPago.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosDetalleReformaLaboral");
            banderaDetalle = 0;
            filtrarListaDetallesReformasLaborales = null;
            tipoListaDetalles = 0;
        }
        listDetallesReformasLaboralesBorrar.clear();
        listDetallesReformasLaboralesCrear.clear();
        listDetallesReformasLaboralesModificar.clear();
        indexDetalle = -1;
        secRegistroDetalles = null;
        k = 0;
        listaDetallesReformasLaborales = null;
        guardadoDetalles = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosDetalleReformaLaboral");
    }

    public void editarCelda() {
        if (index >= 0) {
            if (tipoLista == 0) {
                editarReformaLaboral = listaReformasLaborales.get(index);
            }
            if (tipoLista == 1) {
                editarReformaLaboral = filtrarListaReformasLaborales.get(index);
            }
            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarCodigoReformaLaboralD");
                RequestContext.getCurrentInstance().execute("PF('editarCodigoReformaLaboralD').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarNombreReformaLaboralD");
                RequestContext.getCurrentInstance().execute("PF('editarNombreReformaLaboralD').show()");
                cualCelda = -1;
            }
            index = -1;
            secRegistro = null;
        }
        if (indexDetalle >= 0) {
            if (tipoListaDetalles == 0) {
                editarDetalleReformaLaboral = listaDetallesReformasLaborales.get(indexDetalle);
            }
            if (tipoListaDetalles == 1) {
                editarDetalleReformaLaboral = listaDetallesReformasLaborales.get(indexDetalle);
            }
            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCeldaDetalles == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarTipoPagoDetalleD");
                RequestContext.getCurrentInstance().execute("PF('editarTipoPagoDetalleD').show()");
                cualCeldaDetalles = -1;
            } else if (cualCeldaDetalles == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarFactorDetalleD");
                RequestContext.getCurrentInstance().execute("PF('editarFactorDetalleD').show()");
                cualCeldaDetalles = -1;
            }
            indexDetalle = -1;
            secRegistroDetalles = null;
        }

    }

    public void dialogoNuevoRegistro() {
        RequestContext context = RequestContext.getCurrentInstance();
        int tam1 = listaReformasLaborales.size();
        int tam2 = listaDetallesReformasLaborales.size();
        if (tam1 == 0 || tam2 == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:verificarNuevoRegistro");
            RequestContext.getCurrentInstance().execute("PF('verificarNuevoRegistro').show()");
        } else {
            if (index >= 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroReformaLaboral");
                RequestContext.getCurrentInstance().execute("PF('NuevoRegistroReformaLaboral').show()");
            }
            if (indexDetalle >= 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroDetalleReformaLaboral");
                RequestContext.getCurrentInstance().execute("PF('NuevoRegistroDetalleReformaLaboral').show()");
            }
        }
    }

    //CREAR 
    public void agregarNuevoReformaLaboral() {
        boolean respueta = validarCamposNulosReformaLaboral(1);
        if (respueta == true) {
            int tamDes = 0;
            tamDes = nuevoReformaLaboral.getNombre().length();
            if (tamDes >= 1 && tamDes <= 30) {
                if (bandera == 1) {
                    altoTablaReforma = "157";
                    reformaCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosReformaLaboral:reformaCodigo");
                    reformaCodigo.setFilterStyle("display: none; visibility: hidden;");
                    reformaNombre = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosReformaLaboral:reformaNombre");
                    reformaNombre.setFilterStyle("display: none; visibility: hidden;");
                    reformaIntegral = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosReformaLaboral:reformaIntegral");
                    reformaIntegral.setFilterStyle("display: none; visibility: hidden;");
                    RequestContext.getCurrentInstance().update("form:datosReformaLaboral");
                    bandera = 0;
                    filtrarListaReformasLaborales = null;
                    tipoLista = 0;
                }

                k++;
                l = BigInteger.valueOf(k);
                String text = nuevoReformaLaboral.getNombre().toUpperCase();
                nuevoReformaLaboral.setNombre(text);
                nuevoReformaLaboral.setSecuencia(l);
                listReformasLaboralesCrear.add(nuevoReformaLaboral);
                listaReformasLaborales.add(nuevoReformaLaboral);
                nuevoReformaLaboral = new ReformasLaborales();
                cambiosPagina = false;
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
                RequestContext.getCurrentInstance().update("form:datosReformaLaboral");
                RequestContext.getCurrentInstance().execute("PF('NuevoRegistroReformaLaboral').hide()");
                if (guardado == true) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
                index = -1;
                secRegistro = null;
            } else {
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().execute("PF('errorNombreReformaLaboral').show()");
            }
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('errorDatosNullReformaLaboral').show()");
        }
    }

    public void agregarNuevoDetalleReformaLaboral() {
        boolean respueta = validarCamposNulosDetalleReformaLaboral(1);
        if (respueta == true) {
            int tamDes = 0;
            tamDes = nuevoDetalleReformaLaboral.getTipopago().length();
            if (tamDes >= 1 && tamDes <= 10) {
                if (banderaDetalle == 1) {
                    altoTablaDetalles = "135";
                    detalleFactor = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDetalleReformaLaboral:detalleFactor");
                    detalleFactor.setFilterStyle("display: none; visibility: hidden;");
                    detalleTipoPago = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDetalleReformaLaboral:detalleTipoPago");
                    detalleTipoPago.setFilterStyle("display: none; visibility: hidden;");
                    RequestContext.getCurrentInstance().update("form:datosDetalleReformaLaboral");
                    banderaDetalle = 0;
                    filtrarListaDetallesReformasLaborales = null;
                    tipoListaDetalles = 0;
                }
                k++;
                l = BigInteger.valueOf(k);
                nuevoDetalleReformaLaboral.setSecuencia(l);
                if (tipoLista == 0) {
                    nuevoDetalleReformaLaboral.setReformalaboral(listaReformasLaborales.get(indexAux));
                }
                if (tipoLista == 1) {
                    nuevoDetalleReformaLaboral.setReformalaboral(filtrarListaReformasLaborales.get(indexAux));
                }
                if (listaDetallesReformasLaborales.size() == 0) {
                    listaDetallesReformasLaborales = new ArrayList<DetallesReformasLaborales>();
                }
                listDetallesReformasLaboralesCrear.add(nuevoDetalleReformaLaboral);
                listaDetallesReformasLaborales.add(nuevoDetalleReformaLaboral);
                cambiosPagina = false;
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
                index = indexAux;
                RequestContext.getCurrentInstance().update("form:datosDetalleReformaLaboral");
                RequestContext.getCurrentInstance().execute("PF('NuevoRegistroDetalleReformaLaboral').hide()");
                nuevoDetalleReformaLaboral = new DetallesReformasLaborales();
                if (guardadoDetalles == true) {
                    guardadoDetalles = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
                indexDetalle = -1;
                secRegistroDetalles = null;
            } else {
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().execute("PF('errorTipoPagoDetalleReforma').show()");
            }
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('errorDatosNullDetalleReforma').show()");
        }
    }
    //LIMPIAR NUEVO REGISTRO

    /**
     */
    public void limpiarNuevaReformaLaboral() {
        nuevoReformaLaboral = new ReformasLaborales();
        index = -1;
        secRegistro = null;
    }

    public void limpiarNuevaDetalleReformaLaboral() {
        nuevoDetalleReformaLaboral = new DetallesReformasLaborales();
        indexDetalle = -1;
        secRegistroDetalles = null;
    }

    //DUPLICAR VC
    /**
     */
    public void verificarRegistroDuplicar() {
        if (index >= 0) {
            duplicarReformaLaboralM();
        }
        if (indexDetalle >= 0) {
            duplicarDetalleReformaLaboralM();
        }
    }

    public void duplicarReformaLaboralM() {
        if (index >= 0) {
            duplicarReformaLaboral = new ReformasLaborales();
            k++;
            l = BigInteger.valueOf(k);
            if (tipoLista == 0) {
                duplicarReformaLaboral.setSecuencia(l);
                duplicarReformaLaboral.setCodigo(listaReformasLaborales.get(index).getCodigo());
                duplicarReformaLaboral.setNombre(listaReformasLaborales.get(index).getNombre());
                duplicarReformaLaboral.setIntegral(listaReformasLaborales.get(index).getIntegral());
            }
            if (tipoLista == 1) {
                duplicarReformaLaboral.setSecuencia(l);
                duplicarReformaLaboral.setCodigo(filtrarListaReformasLaborales.get(index).getCodigo());
                duplicarReformaLaboral.setNombre(filtrarListaReformasLaborales.get(index).getNombre());
                duplicarReformaLaboral.setIntegral(filtrarListaReformasLaborales.get(index).getIntegral());
            }
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:DuplicarRegistroReformaLaboral");
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroReformaLaboral').show()");
            index = -1;
            secRegistro = null;
        }
    }

    public void duplicarDetalleReformaLaboralM() {
        if (indexDetalle >= 0) {
            duplicarDetalleReformaLaboral = new DetallesReformasLaborales();

            if (tipoListaDetalles == 0) {
                duplicarDetalleReformaLaboral.setFactor(listaDetallesReformasLaborales.get(indexDetalle).getFactor());
                duplicarDetalleReformaLaboral.setTipopago(listaDetallesReformasLaborales.get(indexDetalle).getTipopago());
            }
            if (tipoListaDetalles == 1) {
                duplicarDetalleReformaLaboral.setFactor(filtrarListaDetallesReformasLaborales.get(indexDetalle).getFactor());
                duplicarDetalleReformaLaboral.setTipopago(filtrarListaDetallesReformasLaborales.get(indexDetalle).getTipopago());
            }
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:DuplicarRegistroDetalleReformaLaboral");
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroDetalleReformaLaboral').show()");
            indexDetalle = -1;
            secRegistroDetalles = null;
        }
    }

    /**
     * Metodo que confirma el duplicado y actualiza los datos de la tabla Sets
     */
    public void confirmarDuplicarReformaLaboral() {
        boolean respueta = validarCamposNulosReformaLaboral(2);
        if (respueta == true) {
            int tamDes = 0;
            tamDes = nuevoReformaLaboral.getNombre().length();
            if (tamDes >= 1 && tamDes <= 30) {
                k++;
                l = BigInteger.valueOf(k);
                duplicarDetalleReformaLaboral.setSecuencia(l);
                if (duplicarReformaLaboral.getNombre() != null) {
                    String text = duplicarReformaLaboral.getNombre().toUpperCase();
                    duplicarReformaLaboral.setNombre(text);
                }
                listaReformasLaborales.add(duplicarReformaLaboral);
                listReformasLaboralesCrear.add(duplicarReformaLaboral);
                cambiosPagina = false;
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
                RequestContext.getCurrentInstance().update("form:datosReformaLaboral");
                RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroReformaLaboral').hide()");
                index = -1;
                secRegistro = null;
                if (guardado == true) {
                    guardado = false;
                    //RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
                if (bandera == 1) {
                    altoTablaReforma = "157";
                    reformaCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosReformaLaboral:reformaCodigo");
                    reformaCodigo.setFilterStyle("display: none; visibility: hidden;");
                    reformaNombre = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosReformaLaboral:reformaNombre");
                    reformaNombre.setFilterStyle("display: none; visibility: hidden;");
                    reformaIntegral = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosReformaLaboral:reformaIntegral");
                    reformaIntegral.setFilterStyle("display: none; visibility: hidden;");
                    RequestContext.getCurrentInstance().update("form:datosReformaLaboral");
                    bandera = 0;
                    filtrarListaReformasLaborales = null;
                    tipoLista = 0;
                }
                duplicarReformaLaboral = new ReformasLaborales();
            } else {
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().execute("PF('errorNombreReformaLaboral').show()");
            }
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('errorDatosNullReformaLaboral').show()");
        }
    }

    public void confirmarDuplicarDetalleReformaLaboral() {
        boolean respueta = validarCamposNulosDetalleReformaLaboral(2);
        if (respueta == true) {
            int tamDes = 0;
            tamDes = nuevoDetalleReformaLaboral.getTipopago().length();
            if (tamDes >= 1 && tamDes <= 10) {
                if (tipoLista == 0) {
                    duplicarDetalleReformaLaboral.setReformalaboral(listaReformasLaborales.get(indexAux));
                }
                if (tipoLista == 1) {
                    duplicarDetalleReformaLaboral.setReformalaboral(filtrarListaReformasLaborales.get(indexAux));
                }
                listaDetallesReformasLaborales.add(duplicarDetalleReformaLaboral);
                listDetallesReformasLaboralesCrear.add(duplicarDetalleReformaLaboral);
                cambiosPagina = false;
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
                RequestContext.getCurrentInstance().update("form:datosDetalleReformaLaboral");
                RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroDetalleReformaLaboral').hide()");
                indexDetalle = -1;
                secRegistroDetalles = null;
                if (guardadoDetalles == true) {
                    guardadoDetalles = false;
                    //RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
                if (banderaDetalle == 1) {
                    altoTablaDetalles = "135";
                    detalleFactor = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDetalleReformaLaboral:detalleFactor");
                    detalleFactor.setFilterStyle("display: none; visibility: hidden;");
                    detalleTipoPago = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDetalleReformaLaboral:detalleTipoPago");
                    detalleTipoPago.setFilterStyle("display: none; visibility: hidden;");
                    RequestContext.getCurrentInstance().update("form:datosDetalleReformaLaboral");
                    banderaDetalle = 0;
                    filtrarListaDetallesReformasLaborales = null;
                    tipoListaDetalles = 0;
                }
                duplicarDetalleReformaLaboral = new DetallesReformasLaborales();
            } else {
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().execute("PF('errorTipoPagoDetalleReforma').show()");
            }
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('errorDatosNullDetalleReforma').show()");
        }
    }

    //LIMPIAR DUPLICAR
    /**
     * Metodo que limpia los datos de un duplicar Set
     */
    public void limpiarDuplicarReformaLaboral() {
        duplicarReformaLaboral = new ReformasLaborales();
    }

    public void limpiarDuplicarDetalleReformaLaboral() {
        duplicarDetalleReformaLaboral = new DetallesReformasLaborales();
    }

    public void limpiarMSNRastros() {
        msnConfirmarRastro = "";
        msnConfirmarRastroHistorico = "";
        nombreTablaRastro = "";
    }

    //BORRAR VC
    /**
     */
    public void verificarRegistroBorrar() {
        if (index >= 0) {
            if (listaDetallesReformasLaborales.isEmpty()) {
                borrarReformaLaboral();
            } else {
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().execute("PF('errorBorrarRegistro').show()");
            }
        }
        if (indexDetalle >= 0) {
            borrarDetalleReformaLaboral();
        }
    }

    public void borrarReformaLaboral() {
        if (index >= 0) {
            if (tipoLista == 0) {
                if (!listReformasLaboralesModificar.isEmpty() && listReformasLaboralesModificar.contains(listaReformasLaborales.get(index))) {
                    int modIndex = listReformasLaboralesModificar.indexOf(listaReformasLaborales.get(index));
                    listReformasLaboralesModificar.remove(modIndex);
                    listReformasLaboralesBorrar.add(listaReformasLaborales.get(index));
                } else if (!listReformasLaboralesCrear.isEmpty() && listReformasLaboralesCrear.contains(listaReformasLaborales.get(index))) {
                    int crearIndex = listReformasLaboralesCrear.indexOf(listaReformasLaborales.get(index));
                    listReformasLaboralesCrear.remove(crearIndex);
                } else {
                    listReformasLaboralesBorrar.add(listaReformasLaborales.get(index));
                }
                listaReformasLaborales.remove(index);
            }
            if (tipoLista == 1) {
                if (!listReformasLaboralesModificar.isEmpty() && listReformasLaboralesModificar.contains(filtrarListaReformasLaborales.get(index))) {
                    int modIndex = listReformasLaboralesModificar.indexOf(filtrarListaReformasLaborales.get(index));
                    listReformasLaboralesModificar.remove(modIndex);
                    listReformasLaboralesBorrar.add(filtrarListaReformasLaborales.get(index));
                } else if (!listReformasLaboralesCrear.isEmpty() && listReformasLaboralesCrear.contains(filtrarListaReformasLaborales.get(index))) {
                    int crearIndex = listReformasLaboralesCrear.indexOf(filtrarListaReformasLaborales.get(index));
                    listReformasLaboralesCrear.remove(crearIndex);
                } else {
                    listReformasLaboralesBorrar.add(filtrarListaReformasLaborales.get(index));
                }
                int VCIndex = listaReformasLaborales.indexOf(filtrarListaReformasLaborales.get(index));
                listaReformasLaborales.remove(VCIndex);
                filtrarListaReformasLaborales.remove(index);
            }

            cambiosPagina = false;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosReformaLaboral");
            index = -1;
            secRegistro = null;

            if (guardado == true) {
                guardado = false;
                //RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        }
    }

    public void borrarDetalleReformaLaboral() {
        if (indexDetalle >= 0) {
            if (tipoListaDetalles == 0) {
                if (!listDetallesReformasLaboralesModificar.isEmpty() && listDetallesReformasLaboralesModificar.contains(listaDetallesReformasLaborales.get(indexDetalle))) {
                    int modIndex = listDetallesReformasLaboralesModificar.indexOf(listaDetallesReformasLaborales.get(indexDetalle));
                    listDetallesReformasLaboralesModificar.remove(modIndex);
                    listDetallesReformasLaboralesBorrar.add(listaDetallesReformasLaborales.get(indexDetalle));
                } else if (!listDetallesReformasLaboralesCrear.isEmpty() && listDetallesReformasLaboralesCrear.contains(listaDetallesReformasLaborales.get(indexDetalle))) {
                    int crearIndex = listDetallesReformasLaboralesCrear.indexOf(listaDetallesReformasLaborales.get(indexDetalle));
                    listDetallesReformasLaboralesCrear.remove(crearIndex);
                } else {
                    listDetallesReformasLaboralesBorrar.add(listaDetallesReformasLaborales.get(indexDetalle));
                }
                listaDetallesReformasLaborales.remove(indexDetalle);
            }
            if (tipoListaDetalles == 1) {
                if (!listDetallesReformasLaboralesModificar.isEmpty() && listDetallesReformasLaboralesModificar.contains(filtrarListaDetallesReformasLaborales.get(indexDetalle))) {
                    int modIndex = listDetallesReformasLaboralesModificar.indexOf(filtrarListaDetallesReformasLaborales.get(indexDetalle));
                    listDetallesReformasLaboralesModificar.remove(modIndex);
                    listDetallesReformasLaboralesBorrar.add(filtrarListaDetallesReformasLaborales.get(indexDetalle));
                } else if (!listDetallesReformasLaboralesCrear.isEmpty() && listDetallesReformasLaboralesCrear.contains(filtrarListaDetallesReformasLaborales.get(indexDetalle))) {
                    int crearIndex = listDetallesReformasLaboralesCrear.indexOf(filtrarListaDetallesReformasLaborales.get(indexDetalle));
                    listDetallesReformasLaboralesCrear.remove(crearIndex);
                } else {
                    listDetallesReformasLaboralesBorrar.add(filtrarListaDetallesReformasLaborales.get(indexDetalle));
                }
                int VCIndex = listaDetallesReformasLaborales.indexOf(filtrarListaDetallesReformasLaborales.get(indexDetalle));
                listaDetallesReformasLaborales.remove(VCIndex);
                filtrarListaDetallesReformasLaborales.remove(indexDetalle);
            }

            cambiosPagina = false;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosDetalleReformaLaboral");
            indexDetalle = -1;
            secRegistroDetalles = null;

            if (guardadoDetalles == true) {
                guardadoDetalles = false;
                //RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        }
    }
    //CTRL + F11 ACTIVAR/DESACTIVAR

    /**
     * Metodo que activa el filtrado por medio de la opcion en el tollbar o por
     * medio de la tecla Crtl+F11
     */
    public void activarCtrlF11() {
        if (index >= 0) {
            if (bandera == 0) {
                altoTablaReforma = "137";
                reformaCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosReformaLaboral:reformaCodigo");
                reformaCodigo.setFilterStyle("width: 85% !important;");
                reformaNombre = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosReformaLaboral:reformaNombre");
                reformaNombre.setFilterStyle("width: 85% !important;");
                reformaIntegral = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosReformaLaboral:reformaIntegral");
                reformaIntegral.setFilterStyle("width: 85% !important;");
                RequestContext.getCurrentInstance().update("form:datosReformaLaboral");
                bandera = 1;
            } else if (bandera == 1) {
                altoTablaReforma = "157";
                reformaCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosReformaLaboral:reformaCodigo");
                reformaCodigo.setFilterStyle("display: none; visibility: hidden;");
                reformaNombre = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosReformaLaboral:reformaNombre");
                reformaNombre.setFilterStyle("display: none; visibility: hidden;");
                reformaIntegral = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosReformaLaboral:reformaIntegral");
                reformaIntegral.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosReformaLaboral");
                bandera = 0;
                filtrarListaReformasLaborales = null;
                tipoLista = 0;
            }
        }
        if (indexDetalle >= 0) {
            if (banderaDetalle == 0) {
                altoTablaDetalles = "113";
                detalleFactor = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDetalleReformaLaboral:detalleFactor");
                detalleFactor.setFilterStyle("width: 85% !important;");
                detalleTipoPago = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDetalleReformaLaboral:detalleTipoPago");
                detalleTipoPago.setFilterStyle("width: 85% !important;");
                RequestContext.getCurrentInstance().update("form:datosDetalleReformaLaboral");
                banderaDetalle = 1;
            } else if (banderaDetalle == 1) {
                altoTablaDetalles = "135";
                detalleFactor = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDetalleReformaLaboral:detalleFactor");
                detalleFactor.setFilterStyle("display: none; visibility: hidden;");
                detalleTipoPago = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDetalleReformaLaboral:detalleTipoPago");
                detalleTipoPago.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosDetalleReformaLaboral");
                banderaDetalle = 0;
                filtrarListaDetallesReformasLaborales = null;
                tipoListaDetalles = 0;
            }
        }
    }

    //SALIR
    /**
     * Metodo que cierra la sesion y limpia los datos en la pagina
     */
    public void salir() {
        if (bandera == 1) {
            altoTablaReforma = "157";
            reformaCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosReformaLaboral:reformaCodigo");
            reformaCodigo.setFilterStyle("display: none; visibility: hidden;");
            reformaNombre = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosReformaLaboral:reformaNombre");
            reformaNombre.setFilterStyle("display: none; visibility: hidden;");
            reformaIntegral = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosReformaLaboral:reformaIntegral");
            reformaIntegral.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosReformaLaboral");
            bandera = 0;
            filtrarListaReformasLaborales = null;
            tipoLista = 0;
        }
        if (banderaDetalle == 1) {
            altoTablaDetalles = "135";
            detalleFactor = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDetalleReformaLaboral:detalleFactor");
            detalleFactor.setFilterStyle("display: none; visibility: hidden;");
            detalleTipoPago = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDetalleReformaLaboral:detalleTipoPago");
            detalleTipoPago.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosDetalleReformaLaboral");
            banderaDetalle = 0;
            filtrarListaDetallesReformasLaborales = null;
            tipoListaDetalles = 0;
        }

        listReformasLaboralesBorrar.clear();
        listReformasLaboralesCrear.clear();
        listReformasLaboralesModificar.clear();
        listDetallesReformasLaboralesBorrar.clear();
        listDetallesReformasLaboralesCrear.clear();
        listDetallesReformasLaboralesModificar.clear();
        index = -1;
        indexAux = -1;
        indexDetalle = -1;
        secRegistroDetalles = null;
        secRegistro = null;
        k = 0;
        listaReformasLaborales = null;
        listaDetallesReformasLaborales = null;
        guardado = true;
        guardadoDetalles = true;
        cambiosPagina = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        reformaActualTabla = new ReformasLaborales();
        detalleActualTabla = new DetallesReformasLaborales();
    }

    /**
     * Metodo que activa el boton aceptar de la pantalla y dialogos
     */
    public void activarAceptar() {
        aceptar = false;
    }
    //EXPORTAR

    public String exportXML() {
        if (index >= 0) {
            nombreTabla = ":formExportarRL:datosReformaLaboralExportar";
            nombreXML = "ReformasLaborales_XML";
        }
        if (indexDetalle >= 0) {
            nombreTabla = ":formExportarDRL:datosDetalleReformaLaboralExportar";
            nombreXML = "DetallesReformasLaborales_XML";
        }
        return nombreTabla;
    }

    /**
     * Metodo que exporta datos a PDF
     *
     * @throws IOException Excepcion de In-Out de datos
     */
    public void validarExportPDF() throws IOException {
        if (index >= 0) {
            exportPDF_RL();
        }
        if (indexDetalle >= 0) {
            exportPDF_DRL();
        }
    }

    public void exportPDF_RL() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarRL:datosReformaLaboralExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "ReformasLaborales_PDF", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void exportPDF_DRL() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarDRL:datosDetalleReformaLaboralExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "DetallesReformasLaborales_PDF", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    /**
     * Metodo que exporta datos a XLS
     *
     * @throws IOException Excepcion de In-Out de datos
     */
    public void validarExportXLS() throws IOException {
        if (index >= 0) {
            exportXLS_RL();
        }
        if (indexDetalle >= 0) {
            exportXLS_DRL();
        }
    }

    public void exportXLS_RL() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarRL:datosReformaLaboralExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "ReformasLaborales_XLS", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void exportXLS_DRL() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarDRL:datosDetalleReformaLaboralExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "DetallesReformasLaborales_XLS", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }
    //EVENTO FILTRAR

    /**
     * Evento que cambia la lista reala a la filtrada
     */
    public void eventoFiltrar() {
        if (index >= 0) {
            if (tipoLista == 0) {
                tipoLista = 1;
            }
        }
        if (indexDetalle >= 0) {
            if (tipoListaDetalles == 0) {
                tipoListaDetalles = 1;
            }
        }
    }
    //RASTRO - COMPROBAR SI LA TABLA TIENE RASTRO ACTIVO

    public void verificarRastro() {
        if (listaDetallesReformasLaborales == null || listaReformasLaborales == null) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('verificarRastrosTablas').show()");
        } else {
            if (index >= 0) {
                verificarRastroReformaLaboral();
                index = -1;
            }
            if (indexDetalle >= 0) {
                verificarRastroDetalleReformaLaboral();
                indexDetalle = -1;
            }
        }
    }

    public void verificarRastroReformaLaboral() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (listaReformasLaborales != null) {
            if (secRegistro != null) {
                int resultado = administrarRastros.obtenerTabla(secRegistro, "REFORMASLABORALES");
                backUpSecRegistro = secRegistro;
                backUp = secRegistro;
                secRegistro = null;
                if (resultado == 1) {
                    RequestContext.getCurrentInstance().execute("PF('errorObjetosDB').show()");
                } else if (resultado == 2) {
                    nombreTablaRastro = "ReformasLaborales";
                    msnConfirmarRastro = "La tabla REFORMASLABORALES tiene rastros para el registro seleccionado, ¿desea continuar?";
                    RequestContext.getCurrentInstance().update("form:msnConfirmarRastro");
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
            if (administrarRastros.verificarHistoricosTabla("REFORMASLABORALES")) {
                nombreTablaRastro = "ReformasLaborales";
                msnConfirmarRastroHistorico = "La tabla REFORMASLABORALES tiene rastros historicos, ¿Desea continuar?";
                RequestContext.getCurrentInstance().update("form:confirmarRastroHistorico");
                RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
            } else {
                RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
            }
        }
        index = -1;
    }

    public void verificarRastroDetalleReformaLaboral() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (listaDetallesReformasLaborales != null) {
            if (secRegistroDetalles != null) {
                int resultado = administrarRastros.obtenerTabla(secRegistroDetalles, "DETALLESREFORMASLABORALES");
                backUpSecRegistroDetalles = secRegistroDetalles;
                backUp = secRegistroDetalles;
                secRegistroDetalles = null;
                if (resultado == 1) {
                    RequestContext.getCurrentInstance().execute("PF('errorObjetosDB').show()");
                } else if (resultado == 2) {
                    nombreTablaRastro = "DetallesReformasLaborales";
                    msnConfirmarRastro = "La tabla DETALLESREFORMASLABORALES tiene rastros para el registro seleccionado, ¿desea continuar?";
                    RequestContext.getCurrentInstance().update("form:msnConfirmarRastro");
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
            if (administrarRastros.verificarHistoricosTabla("DETALLESREFORMASLABORALES")) {
                nombreTablaRastro = "DetallesReformasLaborales";
                msnConfirmarRastroHistorico = "La tabla DETALLESREFORMASLABORALES tiene rastros historicos, ¿Desea continuar?";
                RequestContext.getCurrentInstance().update("form:confirmarRastroHistorico");
                RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
            } else {
                RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
            }
        }
        index = -1;
    }

    public boolean validarCodigoNuevoClonado() {
        boolean retorno = true;
        int conteo = 0;
        for (int i = 0; i < lovReformasLaborales.size(); i++) {
            if (lovReformasLaborales.get(i).getCodigo() == codigoReformaClonar) {
                conteo++;
            }
        }
        if (conteo > 0) {
            retorno = false;
        }
        return retorno;
    }

    public void clonarReformaLaboral() {
        if ((nombreReformaClonar.isEmpty()) || (codigoReformaClonar <= 0) || (reformaLaboralAClonar.getSecuencia() == null)) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:errorClonadoReforma");
            RequestContext.getCurrentInstance().execute("PF('errorClonadoReforma').show()");
        } else {
            if (validarCodigoNuevoClonado() == true) {
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:continuarOperacionClonado");
                RequestContext.getCurrentInstance().execute("PF('continuarOperacionClonado').show()");
            } else {
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:errorCodigoClonado");
                RequestContext.getCurrentInstance().execute("PF('errorCodigoClonado').show()");
            }
        }
    }

    public void dispararDialogoClonarReformaLaboral() {
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:ReformaLaboralDialogo");
        RequestContext.getCurrentInstance().execute("PF('ReformaLaboralDialogo').show()");
    }

    public void posicionReformaLaboralClonar() {
        if (guardado == true) {
            auxCodigoClonar = reformaLaboralAClonar.getCodigo();
            auxNombreClonar = reformaLaboralAClonar.getNombre();
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
        }
    }

    public void seleccionarReformaLaboralClonar() {
        reformaLaboralAClonar = reformaLaboralSeleccionado;
        lovReformasLaborales.clear();
        getLovReformasLaborales();
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:codigoReformaLaboralClonarBase");
        RequestContext.getCurrentInstance().update("form:nombreReformaLaboralClonarBase");
        reformaLaboralSeleccionado = new ReformasLaborales();
        filtrarLovReformasLaborales = null;/*
         RequestContext.getCurrentInstance().update("form:ReformaLaboralDialogo");
         RequestContext.getCurrentInstance().update("form:lovReformaLaboral");
         RequestContext.getCurrentInstance().update("form:aceptarRL");*/

        context.reset("form:lovReformaLaboral:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovReformaLaboral').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('ReformaLaboralDialogo').hide()");
    }

    public void cancelarReformaLaboralClonar() {
        reformaLaboralSeleccionado = new ReformasLaborales();
        filtrarLovReformasLaborales = null;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovReformaLaboral:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovReformaLaboral').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('ReformaLaboralDialogo').hide()");
    }

    public void autoCompletarSeleccionarReformaLaboralClonar(String valorConfirmar, int tipoAutoCompletar) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoAutoCompletar == 0) {
            short num = Short.parseShort(valorConfirmar);
            for (int i = 0; i < lovReformasLaborales.size(); i++) {
                if (lovReformasLaborales.get(i).getCodigo() == num) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                reformaLaboralAClonar = lovReformasLaborales.get(indiceUnicoElemento);
                lovReformasLaborales.clear();
                getLovReformasLaborales();
            } else {
                reformaLaboralAClonar.setCodigo(auxCodigoClonar);
                reformaLaboralAClonar.setNombre(auxNombreClonar);
                RequestContext.getCurrentInstance().update("form:codigoReformaLaboralClonarBase");
                RequestContext.getCurrentInstance().update("form:nombreReformaLaboralClonarBase");
                RequestContext.getCurrentInstance().update("form:ReformaLaboralDialogo");
                RequestContext.getCurrentInstance().execute("PF('ReformaLaboralDialogo').show()");
            }
        }
        if (tipoAutoCompletar == 1) {
            for (int i = 0; i < lovReformasLaborales.size(); i++) {
                if (lovReformasLaborales.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                reformaLaboralAClonar = lovReformasLaborales.get(indiceUnicoElemento);
                lovReformasLaborales.clear();
                getLovReformasLaborales();
            } else {
                reformaLaboralAClonar.setCodigo(auxCodigoClonar);
                reformaLaboralAClonar.setNombre(auxNombreClonar);
                RequestContext.getCurrentInstance().update("form:codigoReformaLaboralClonarBase");
                RequestContext.getCurrentInstance().update("form:nombreReformaLaboralClonarBase");
                RequestContext.getCurrentInstance().update("form:TipoDiaDialogo");
                RequestContext.getCurrentInstance().execute("PF('TipoDiaDialogo').show()");
            }
        }
    }

    //GETTERS AND SETTERS
    public List<ReformasLaborales> getListaReformasLaborales() {
        try {
            if (listaReformasLaborales == null) {
                listaReformasLaborales = new ArrayList<ReformasLaborales>();
                listaReformasLaborales = administrarReformaLaboral.listaReformasLaborales();
                return listaReformasLaborales;
            } else {
                return listaReformasLaborales;
            }
        } catch (Exception e) {
            System.out.println("Error...!! getListaReformasLaborales " + e.toString());
            return null;
        }
    }

    public void setListaReformasLaborales(List<ReformasLaborales> setListaReformasLaborales) {
        this.listaReformasLaborales = setListaReformasLaborales;
    }

    public List<ReformasLaborales> getFiltrarListaReformasLaborales() {
        return filtrarListaReformasLaborales;
    }

    public void setFiltrarListaReformasLaborales(List<ReformasLaborales> setFiltrarListaReformasLaborales) {
        this.filtrarListaReformasLaborales = setFiltrarListaReformasLaborales;
    }

    public ReformasLaborales getNuevoReformaLaboral() {
        return nuevoReformaLaboral;
    }

    public void setNuevoReformaLaboral(ReformasLaborales setNuevoReformaLaboral) {
        this.nuevoReformaLaboral = setNuevoReformaLaboral;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public ReformasLaborales getEditarReformaLaboral() {
        return editarReformaLaboral;
    }

    public void setEditarReformaLaboral(ReformasLaborales setEditarReformaLaboral) {
        this.editarReformaLaboral = setEditarReformaLaboral;
    }

    public ReformasLaborales getDuplicarReformaLaboral() {
        return duplicarReformaLaboral;
    }

    public void setDuplicarReformaLaboral(ReformasLaborales setDuplicarReformaLaboral) {
        this.duplicarReformaLaboral = setDuplicarReformaLaboral;
    }

    public BigInteger getSecRegistro() {
        return secRegistro;
    }

    public void setSecRegistro(BigInteger secRegistro) {
        this.secRegistro = secRegistro;
    }

    public BigInteger getBackUpSecRegistro() {
        return backUpSecRegistro;
    }

    public void setBackUpSecRegistro(BigInteger setBackUpSecRegistro) {
        this.backUpSecRegistro = setBackUpSecRegistro;
    }

    public List<DetallesReformasLaborales> getListaDetallesReformasLaborales() {
        if (listaDetallesReformasLaborales == null) {
            listaDetallesReformasLaborales = new ArrayList<DetallesReformasLaborales>();
            if (index >= 0) {
                if (tipoLista == 0) {
                    listaDetallesReformasLaborales = administrarReformaLaboral.listaDetalleReformasLaborales(listaReformasLaborales.get(index).getSecuencia());
                }
                if (tipoLista == 1) {
                    listaDetallesReformasLaborales = administrarReformaLaboral.listaDetalleReformasLaborales(filtrarListaReformasLaborales.get(index).getSecuencia());
                }
            }
        }
        return listaDetallesReformasLaborales;
    }

    public void setListaDetallesReformasLaborales(List<DetallesReformasLaborales> setListaDetallesReformasLaborales) {
        this.listaDetallesReformasLaborales = setListaDetallesReformasLaborales;
    }

    public List<DetallesReformasLaborales> getFiltrarListaDetallesReformasLaborales() {
        return filtrarListaDetallesReformasLaborales;
    }

    public void setFiltrarListaDetallesReformasLaborales(List<DetallesReformasLaborales> setFiltrarListaDetallesReformasLaborales) {
        this.filtrarListaDetallesReformasLaborales = setFiltrarListaDetallesReformasLaborales;
    }

    public List<ReformasLaborales> getListReformasLaboralesModificar() {
        return listReformasLaboralesModificar;
    }

    public void setListReformasLaboralesModificar(List<ReformasLaborales> setListReformasLaboralesModificar) {
        this.listReformasLaboralesModificar = setListReformasLaboralesModificar;
    }

    public List<ReformasLaborales> getListReformasLaboralesCrear() {
        return listReformasLaboralesCrear;
    }

    public void setListReformasLaboralesCrear(List<ReformasLaborales> setListReformasLaboralesCrear) {
        this.listReformasLaboralesCrear = setListReformasLaboralesCrear;
    }

    public List<ReformasLaborales> getListReformasLaboralesBorrar() {
        return listReformasLaboralesBorrar;
    }

    public void setListReformasLaboralesBorrar(List<ReformasLaborales> setListReformasLaboralesBorrar) {
        this.listReformasLaboralesBorrar = setListReformasLaboralesBorrar;
    }

    public List<DetallesReformasLaborales> getListDetallesReformasLaboralesModificar() {
        return listDetallesReformasLaboralesModificar;
    }

    public void setListDetallesReformasLaboralesModificar(List<DetallesReformasLaborales> setListDetallesReformasLaboralesModificar) {
        this.listDetallesReformasLaboralesModificar = setListDetallesReformasLaboralesModificar;
    }

    public DetallesReformasLaborales getNuevoDetalleReformaLaboral() {
        return nuevoDetalleReformaLaboral;
    }

    public void setNuevoDetalleReformaLaboral(DetallesReformasLaborales setNuevoDetalleReformaLaboral) {
        this.nuevoDetalleReformaLaboral = setNuevoDetalleReformaLaboral;
    }

    public List<DetallesReformasLaborales> getListDetallesReformasLaboralesCrear() {
        return listDetallesReformasLaboralesCrear;
    }

    public void setListDetallesReformasLaboralesCrear(List<DetallesReformasLaborales> setListDetallesReformasLaboralesCrear) {
        this.listDetallesReformasLaboralesCrear = setListDetallesReformasLaboralesCrear;
    }

    public List<DetallesReformasLaborales> getListDetallesReformasLaboralesBorrar() {
        return listDetallesReformasLaboralesBorrar;
    }

    public void setListDetallesReformasLaboralesBorrar(List<DetallesReformasLaborales> setListDetallesReformasLaboralesBorrar) {
        this.listDetallesReformasLaboralesBorrar = setListDetallesReformasLaboralesBorrar;
    }

    public DetallesReformasLaborales getEditarDetalleReformaLaboral() {
        return editarDetalleReformaLaboral;
    }

    public void setEditarDetalleReformaLaboral(DetallesReformasLaborales setEditarDetalleReformaLaboral) {
        this.editarDetalleReformaLaboral = setEditarDetalleReformaLaboral;
    }

    public DetallesReformasLaborales getDuplicarDetalleReformaLaboral() {
        return duplicarDetalleReformaLaboral;
    }

    public void setDuplicarDetalleReformaLaboral(DetallesReformasLaborales setDuplicarDetalleReformaLaboral) {
        this.duplicarDetalleReformaLaboral = setDuplicarDetalleReformaLaboral;
    }

    public BigInteger getSecRegistroDetalles() {
        return secRegistroDetalles;
    }

    public void setSecRegistroDetalles(BigInteger setSecRegistroDetalles) {
        this.secRegistroDetalles = setSecRegistroDetalles;
    }

    public BigInteger getBackUpSecRegistroDetalles() {
        return backUpSecRegistroDetalles;
    }

    public void setBackUpSecRegistroDetalles(BigInteger setBackUpSecRegistroDetalles) {
        this.backUpSecRegistroDetalles = setBackUpSecRegistroDetalles;
    }

    public String getMsnConfirmarRastro() {
        return msnConfirmarRastro;
    }

    public void setMsnConfirmarRastro(String msnConfirmarRastro) {
        this.msnConfirmarRastro = msnConfirmarRastro;
    }

    public String getMsnConfirmarRastroHistorico() {
        return msnConfirmarRastroHistorico;
    }

    public void setMsnConfirmarRastroHistorico(String msnConfirmarRastroHistorico) {
        this.msnConfirmarRastroHistorico = msnConfirmarRastroHistorico;
    }

    public BigInteger getBackUp() {
        return backUp;
    }

    public void setBackUp(BigInteger backUp) {
        this.backUp = backUp;
    }

    public String getNombreTablaRastro() {
        return nombreTablaRastro;
    }

    public void setNombreTablaRastro(String nombreTablaRastro) {
        this.nombreTablaRastro = nombreTablaRastro;
    }

    public String getNombreXML() {
        return nombreXML;
    }

    public void setNombreXML(String nombreXML) {
        this.nombreXML = nombreXML;
    }

    public String getNombreTabla() {
        return nombreTabla;
    }

    public void setNombreTabla(String nombreTabla) {
        this.nombreTabla = nombreTabla;
    }

    public boolean isCambiosPagina() {
        return cambiosPagina;
    }

    public void setCambiosPagina(boolean cambiosPagina) {
        this.cambiosPagina = cambiosPagina;
    }

    public String getAltoTablaReforma() {
        return altoTablaReforma;
    }

    public void setAltoTablaReforma(String setAltoTablaReforma) {
        this.altoTablaReforma = setAltoTablaReforma;
    }

    public String getAltoTablaDetalles() {
        return altoTablaDetalles;
    }

    public void setAltoTablaDetalles(String setAltoTablaDetalles) {
        this.altoTablaDetalles = setAltoTablaDetalles;
    }

    public short getCodigoReformaClonar() {
        return codigoReformaClonar;
    }

    public void setCodigoReformaClonar(short setCodigoReformaClonar) {
        this.codigoReformaClonar = setCodigoReformaClonar;
    }

    public String getNombreReformaClonar() {
        return nombreReformaClonar;
    }

    public void setNombreReformaClonar(String setNombreReformaClonar) {
        this.nombreReformaClonar = setNombreReformaClonar.toUpperCase();
    }

    public List<ReformasLaborales> getLovReformasLaborales() {
        lovReformasLaborales = administrarReformaLaboral.listaReformasLaborales();
        return lovReformasLaborales;
    }

    public void setLovReformasLaborales(List<ReformasLaborales> setLovReformasLaborales) {
        this.lovReformasLaborales = setLovReformasLaborales;
    }

    public List<ReformasLaborales> getFiltrarLovReformasLaborales() {
        return filtrarLovReformasLaborales;
    }

    public void setFiltrarLovReformasLaborales(List<ReformasLaborales> setFiltrarLovReformasLaborales) {
        this.filtrarLovReformasLaborales = setFiltrarLovReformasLaborales;
    }

    public ReformasLaborales getReformaLaboralSeleccionado() {
        return reformaLaboralSeleccionado;
    }

    public void setReformaLaboralSeleccionado(ReformasLaborales setReformaLaboralSeleccionado) {
        this.reformaLaboralSeleccionado = setReformaLaboralSeleccionado;
    }

    public ReformasLaborales getReformaLaboralAClonar() {
        return reformaLaboralAClonar;
    }

    public void setReformaLaboralAClonar(ReformasLaborales setReformaLaboralAClonar) {
        this.reformaLaboralAClonar = setReformaLaboralAClonar;
    }

    public ReformasLaborales getReformaActualTabla() {
        return reformaActualTabla;
    }

    public void setReformaActualTabla(ReformasLaborales reformaActualTabla) {
        this.reformaActualTabla = reformaActualTabla;
    }

    public DetallesReformasLaborales getDetalleActualTabla() {
        getListaDetallesReformasLaborales();
        if (listaDetallesReformasLaborales != null) {
            int tam = listaDetallesReformasLaborales.size();
            if (tam > 0) {
                detalleActualTabla = listaDetallesReformasLaborales.get(0);
            }
        }
        return detalleActualTabla;
    }

    public void setDetalleActualTabla(DetallesReformasLaborales detalleActualTabla) {
        this.detalleActualTabla = detalleActualTabla;
    }

    public String getInfoRegistroReforma() {
        getLovReformasLaborales();
        if (lovReformasLaborales != null) {
            infoRegistroReforma = "Cantidad de registros : " + lovReformasLaborales.size();
        } else {
            infoRegistroReforma = "Cantidad de registros : 0";
        }
        return infoRegistroReforma;
    }

    public void setInfoRegistroReforma(String infoRegistroReforma) {
        this.infoRegistroReforma = infoRegistroReforma;
    }

}
