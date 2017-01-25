/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;


import Entidades.Indices;
import Entidades.TiposIndices;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarIndicesInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
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
 * @author user
 */
@ManagedBean
@SessionScoped
public class ControlIndices implements Serializable {

    @EJB
    AdministrarIndicesInterface administrarIndices;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    private List<Indices> listIndices;
    private List<Indices> filtrarIndices;
    private List<Indices> crearIndices;
    private List<Indices> modificarIndices;
    private List<Indices> borrarIndices;
    private Indices nuevoIndices;
    private Indices duplicarIndices;
    private Indices editarIndices;
    private Indices hvReferencia1Seleccionada;
    //otros
    private int cualCelda, tipoLista, index, tipoActualizacion, k, bandera;
    private BigInteger l;
    private boolean aceptar, guardado;
    //AutoCompletar
    private boolean permitirIndex;
    //RASTRO
    private BigInteger secRegistro;
    private Column codigo,
            descripcion,
            tipoindice,
            porcentajeestan,
            objetivo,
            dividendo,
            divisor;
    //borrado
    private int registrosBorrados;
    private String mensajeValidacion;
    //autocompletar
    private String tipoIndice;
    private List<TiposIndices> listaClavesAjustes;
    private List<TiposIndices> filtradoTiposIndices;
    private TiposIndices tipoIndiceSeleccionado;
    private String nuevoParentesco;
    private String infoRegistro;
    private String infoRegistroParentesco;
    private int tamano;
    private Short backUpCodigo;

    public ControlIndices() {
        listIndices = null;
        crearIndices = new ArrayList<Indices>();
        modificarIndices = new ArrayList<Indices>();
        borrarIndices = new ArrayList<Indices>();
        permitirIndex = true;
        guardado = true;
        editarIndices = new Indices();
        nuevoIndices = new Indices();
        nuevoIndices.setTipoindice(new TiposIndices());
        duplicarIndices = new Indices();
        duplicarIndices.setTipoindice(new TiposIndices());
        listaClavesAjustes = null;
        filtradoTiposIndices = null;
        tipoLista = 0;
        tamano = 270;
        aceptar = true;

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
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarIndices.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void eventoFiltrar() {
        try {
            System.out.println("\n ENTRE A ControlIndices.eventoFiltrar \n");
            if (tipoLista == 0) {
                tipoLista = 1;
            }
            RequestContext context = RequestContext.getCurrentInstance();
            infoRegistro = "Cantidad de registros: " + filtrarIndices.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
        } catch (Exception e) {
            System.out.println("ERROR ControlIndices eventoFiltrar ERROR===" + e.getMessage());
        }
    }
    private BigDecimal backUpPorcentaje;

    public void cambiarIndice(int indice, int celda) {
        System.err.println("TIPO LISTA = " + tipoLista);

        if (permitirIndex == true) {
            index = indice;
            cualCelda = celda;
            secRegistro = listIndices.get(index).getSecuencia();

            if (cualCelda == 0) {
                if (tipoLista == 0) {
                    backUpCodigo = listIndices.get(index).getCodigo();
                } else {
                    backUpCodigo = filtrarIndices.get(index).getCodigo();
                }
            }

            if (cualCelda == 2) {
                if (tipoLista == 0) {
                    tipoIndice = listIndices.get(index).getTipoindice().getDescripcion();
                } else {
                    tipoIndice = filtrarIndices.get(index).getTipoindice().getDescripcion();
                }
                System.out.println("Cambiar Indice tipoIndice : " + tipoIndice);
            }
            if (cualCelda == 3) {
                if (tipoLista == 0) {
                    backUpPorcentaje = listIndices.get(index).getPorcentajeestandar();
                } else {
                    backUpPorcentaje = filtrarIndices.get(index).getPorcentajeestandar();
                }
                System.out.println("Cambiar Indice backUpPorcentaje : " + backUpPorcentaje);
            }

        }
        System.out.println("Indice: " + index + " Celda: " + cualCelda);
    }

    public void asignarIndex(Integer indice, int LND, int dig) {
        try {
            System.out.println("\n ENTRE A ControlIndices.asignarIndex \n");
            RequestContext context = RequestContext.getCurrentInstance();

            index = indice;
            if (LND == 0) {
                tipoActualizacion = 0;
            } else if (LND == 1) {
                tipoActualizacion = 1;
                System.out.println("Tipo Actualizacion: " + tipoActualizacion);
            } else if (LND == 2) {
                tipoActualizacion = 2;
            }
            if (dig == 2) {
                RequestContext.getCurrentInstance().update("form:tiposindicesDialogo");
                RequestContext.getCurrentInstance().execute("PF('tiposindicesDialogo').show()");
                dig = -1;
            }

        } catch (Exception e) {
            System.out.println("ERROR ControlIndices.asignarIndex ERROR======" + e.getMessage());
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void listaValoresBoton() {
        if (index >= 0) {
            RequestContext context = RequestContext.getCurrentInstance();

            if (cualCelda == 2) {
                RequestContext.getCurrentInstance().update("form:tiposindicesDialogo");
                RequestContext.getCurrentInstance().execute("PF('tiposindicesDialogo').show()");
                tipoActualizacion = 0;
            }

        }
    }

    public void cancelarModificacion() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 1) {
            //CERRAR FILTRADO

            codigo = (Column) c.getViewRoot().findComponent("form:datosIndices:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosIndices:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            tipoindice = (Column) c.getViewRoot().findComponent("form:datosIndices:tipoindice");
            tipoindice.setFilterStyle("display: none; visibility: hidden;");
            porcentajeestan = (Column) c.getViewRoot().findComponent("form:datosIndices:porcentajeestan");
            porcentajeestan.setFilterStyle("display: none; visibility: hidden;");
            objetivo = (Column) c.getViewRoot().findComponent("form:datosIndices:objetivo");
            objetivo.setFilterStyle("display: none; visibility: hidden;");
            dividendo = (Column) c.getViewRoot().findComponent("form:datosIndices:dividendo");
            dividendo.setFilterStyle("display: none; visibility: hidden;");
            divisor = (Column) c.getViewRoot().findComponent("form:datosIndices:divisor");
            divisor.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosIndices");
            bandera = 0;
            filtrarIndices = null;
            tipoLista = 0;
        }

        borrarIndices.clear();
        crearIndices.clear();
        modificarIndices.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        tamano = 270;
        listIndices = null;
        guardado = true;
        permitirIndex = true;
        getListIndices();
        RequestContext context = RequestContext.getCurrentInstance();
        if (listIndices == null || listIndices.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listIndices.size();
        }
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
        RequestContext.getCurrentInstance().update("form:datosIndices");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void salir() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 1) {
            //CERRAR FILTRADO
            codigo = (Column) c.getViewRoot().findComponent("form:datosIndices:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosIndices:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            tipoindice = (Column) c.getViewRoot().findComponent("form:datosIndices:tipoindice");
            tipoindice.setFilterStyle("display: none; visibility: hidden;");
            porcentajeestan = (Column) c.getViewRoot().findComponent("form:datosIndices:porcentajeestan");
            porcentajeestan.setFilterStyle("display: none; visibility: hidden;");
            objetivo = (Column) c.getViewRoot().findComponent("form:datosIndices:objetivo");
            objetivo.setFilterStyle("display: none; visibility: hidden;");
            dividendo = (Column) c.getViewRoot().findComponent("form:datosIndices:dividendo");
            dividendo.setFilterStyle("display: none; visibility: hidden;");
            divisor = (Column) c.getViewRoot().findComponent("form:datosIndices:divisor");
            divisor.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosIndices");
            bandera = 0;
            filtrarIndices = null;
            tipoLista = 0;
        }

        borrarIndices.clear();
        crearIndices.clear();
        modificarIndices.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listIndices = null;
        guardado = true;
        permitirIndex = true;
        getListIndices();
        RequestContext context = RequestContext.getCurrentInstance();
        if (listIndices == null || listIndices.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listIndices.size();
        }
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
        RequestContext.getCurrentInstance().update("form:datosIndices");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            tamano = 250;
            codigo = (Column) c.getViewRoot().findComponent("form:datosIndices:codigo");
            codigo.setFilterStyle("width: 85% !important");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosIndices:descripcion");
            descripcion.setFilterStyle("width: 85% !important");
            tipoindice = (Column) c.getViewRoot().findComponent("form:datosIndices:tipoindice");
            tipoindice.setFilterStyle("width: 85% !important");
            porcentajeestan = (Column) c.getViewRoot().findComponent("form:datosIndices:porcentajeestan");
            porcentajeestan.setFilterStyle("width: 85% !important");
            objetivo = (Column) c.getViewRoot().findComponent("form:datosIndices:objetivo");
            objetivo.setFilterStyle("width: 85% !important");
            dividendo = (Column) c.getViewRoot().findComponent("form:datosIndices:dividendo");
            dividendo.setFilterStyle("width: 85% !important");
            divisor = (Column) c.getViewRoot().findComponent("form:datosIndices:divisor");
            divisor.setFilterStyle("width: 85% !important");
            RequestContext.getCurrentInstance().update("form:datosIndices");
            System.out.println("Activar");
            bandera = 1;
        } else if (bandera == 1) {
            tamano = 270;
            System.out.println("Desactivar");
            codigo = (Column) c.getViewRoot().findComponent("form:datosIndices:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosIndices:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            tipoindice = (Column) c.getViewRoot().findComponent("form:datosIndices:tipoindice");
            tipoindice.setFilterStyle("display: none; visibility: hidden;");
            porcentajeestan = (Column) c.getViewRoot().findComponent("form:datosIndices:porcentajeestan");
            porcentajeestan.setFilterStyle("display: none; visibility: hidden;");
            objetivo = (Column) c.getViewRoot().findComponent("form:datosIndices:objetivo");
            objetivo.setFilterStyle("display: none; visibility: hidden;");
            dividendo = (Column) c.getViewRoot().findComponent("form:datosIndices:dividendo");
            dividendo.setFilterStyle("display: none; visibility: hidden;");
            divisor = (Column) c.getViewRoot().findComponent("form:datosIndices:divisor");
            divisor.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosIndices");
            bandera = 0;
            filtrarIndices = null;
            tipoLista = 0;
        }
    }

    /*   public void modificandoHvReferencia(int indice, String confirmarCambio, String valorConfirmar) {
     System.err.println("ENTRE A MODIFICAR HV Referencia");
     index = indice;

     int contador = 0;
     boolean banderita = false;
     Short a;
     a = null;
     RequestContext context = RequestContext.getCurrentInstance();
     System.err.println("TIPO LISTA = " + tipoLista);
     if (confirmarCambio.equalsIgnoreCase("N")) {
     System.err.println("ENTRE A MODIFICAR HvReferencia, CONFIRMAR CAMBIO ES N");
     if (tipoLista == 0) {
     if (!crearIndicesFamiliares.contains(listIndices.get(indice))) {

     if (listIndices.get(indice).getNombrepersona().isEmpty()) {
     mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
     banderita = false;
     } else if (listIndices.get(indice).getNombrepersona().equals(" ")) {
     mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
     banderita = false;
     } else {
     banderita = true;
     }

     if (banderita == true) {
     if (modificarIndicesFamiliares.isEmpty()) {
     modificarIndicesFamiliares.add(listIndices.get(indice));
     } else if (!modificarIndicesFamiliares.contains(listIndices.get(indice))) {
     modificarIndicesFamiliares.add(listIndices.get(indice));
     }
     if (guardado == true) {
     guardado = false;
     }

     } else {
     RequestContext.getCurrentInstance().update("form:validacionModificar");
     RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");
     cancelarModificacion();
     }
     index = -1;
     secRegistro = null;
     }
     } else {

     if (!crearIndicesFamiliares.contains(filtrarIndices.get(indice))) {
     if (filtrarIndices.get(indice).getNombrepersona().isEmpty()) {
     mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
     banderita = false;
     }
     if (filtrarIndices.get(indice).getNombrepersona().equals(" ")) {
     mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
     banderita = false;
     }

     if (banderita == true) {
     if (modificarIndicesFamiliares.isEmpty()) {
     modificarIndicesFamiliares.add(filtrarIndices.get(indice));
     } else if (!modificarIndicesFamiliares.contains(filtrarIndices.get(indice))) {
     modificarIndicesFamiliares.add(filtrarIndices.get(indice));
     }
     if (guardado == true) {
     guardado = false;
     }

     } else {
     RequestContext.getCurrentInstance().update("form:validacionModificar");
     RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");
     cancelarModificacion();
     }
     index = -1;
     secRegistro = null;
     }

     }
     RequestContext.getCurrentInstance().update("form:datosIndices");
     }

     }
     */
    /**
     *
     * @param indice donde se encuentra posicionado
     * @param confirmarCambio nombre de la columna
     * @param valorConfirmar valor ingresado
     */
    public void modificarIndice(int indice, String confirmarCambio, String valorConfirmar) {
        index = indice;
        int coincidencias = 0;
        int indiceUnicoElemento = 0, pass = 0;
        int contador = 0;
        BigInteger contadorBD;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("N")) {
            System.err.println("ENTRE A MODIFICAR HvReferencia, CONFIRMAR CAMBIO ES N");
            if (tipoLista == 0) {
                if (!crearIndices.contains(listIndices.get(indice))) {

                    if (listIndices.get(indice).getPorcentajeestandar() == null) {
                        pass++;

                    } else {
                        if (listIndices.get(indice).getPorcentajeestandar().intValue() >= 0 && listIndices.get(indice).getPorcentajeestandar().intValue() <= 100) {
                            pass++;
                        } else {
                            mensajeValidacion = "El porcentaje debe estar entre 0 y 100";
                            listIndices.get(indice).setPorcentajeestandar(backUpPorcentaje);
                        }
                    }
                    if (pass == 1) {
                        if (modificarIndices.isEmpty()) {
                            modificarIndices.add(listIndices.get(indice));
                        } else if (!modificarIndices.contains(listIndices.get(indice))) {
                            modificarIndices.add(listIndices.get(indice));
                        }
                        if (guardado == true) {
                            guardado = false;
                        }

                    } else {
                        RequestContext.getCurrentInstance().update("form:validacionModificar");
                        RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");
                    }
                    index = -1;
                    secRegistro = null;
                } else {
                    if (listIndices.get(indice).getPorcentajeestandar() == null) {
                        pass++;

                    } else {
                        if (listIndices.get(indice).getPorcentajeestandar().intValue() >= 0 && listIndices.get(indice).getPorcentajeestandar().intValue() <= 100) {
                            pass++;
                        } else {
                            mensajeValidacion = "El porcentaje debe estar entre 0 y 100";
                            listIndices.get(indice).setPorcentajeestandar(backUpPorcentaje);
                        }
                    }

                    if (pass == 1) {

                        if (guardado == true) {
                            guardado = false;
                        }

                    } else {
                        RequestContext.getCurrentInstance().update("form:validacionModificar");
                        RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");
                    }
                    index = -1;
                    secRegistro = null;
                }
            } else {

                if (!crearIndices.contains(filtrarIndices.get(indice))) {
                    if (filtrarIndices.get(indice).getPorcentajeestandar() == null) {
                        pass++;

                    } else {
                        if (filtrarIndices.get(indice).getPorcentajeestandar().intValue() >= 0 && filtrarIndices.get(indice).getPorcentajeestandar().intValue() <= 100) {
                            pass++;
                        } else {
                            mensajeValidacion = "El porcentaje debe estar entre 0 y 100";
                            filtrarIndices.get(indice).setPorcentajeestandar(backUpPorcentaje);
                        }
                    }
                    if (pass == 1) {
                        if (modificarIndices.isEmpty()) {
                            modificarIndices.add(filtrarIndices.get(indice));
                        } else if (!modificarIndices.contains(filtrarIndices.get(indice))) {
                            modificarIndices.add(filtrarIndices.get(indice));
                        }
                        if (guardado == true) {
                            guardado = false;
                        }

                    } else {
                        RequestContext.getCurrentInstance().update("form:validacionModificar");
                        RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");

                    }
                    index = -1;
                    secRegistro = null;
                } else {
                    if (filtrarIndices.get(indice).getPorcentajeestandar() == null) {
                        pass++;

                    } else {
                        if (filtrarIndices.get(indice).getPorcentajeestandar().intValue() >= 0 && filtrarIndices.get(indice).getPorcentajeestandar().intValue() <= 100) {
                            pass++;
                        } else {
                            mensajeValidacion = "El porcentaje debe estar entre 0 y 100";
                            filtrarIndices.get(indice).setPorcentajeestandar(backUpPorcentaje);
                        }
                    }
                    if (pass == 1) {

                        if (guardado == true) {
                            guardado = false;
                        }

                    } else {
                        RequestContext.getCurrentInstance().update("form:validacionModificar");
                        RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");

                    }
                    index = -1;
                    secRegistro = null;
                }

            }
            RequestContext.getCurrentInstance().update("form:datosIndices");
        } else if (confirmarCambio.equalsIgnoreCase("TIPOSINDICES")) {
            if (!listIndices.get(indice).getTipoindice().getDescripcion().equals("")) {
                if (tipoLista == 0) {
                    listIndices.get(indice).getTipoindice().setDescripcion(tipoIndice);

                } else {
                    filtrarIndices.get(indice).getTipoindice().setDescripcion(tipoIndice);
                }

                for (int i = 0; i < listaClavesAjustes.size(); i++) {
                    if (listaClavesAjustes.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }

                if (coincidencias == 1) {
                    if (tipoLista == 0) {
                        listIndices.get(indice).setTipoindice(listaClavesAjustes.get(indiceUnicoElemento));
                    } else {
                        filtrarIndices.get(indice).setTipoindice(listaClavesAjustes.get(indiceUnicoElemento));
                    }
                    listaClavesAjustes.clear();
                    listaClavesAjustes = null;
                    getListaTiposIndices();

                } else {
                    permitirIndex = false;
                    RequestContext.getCurrentInstance().update("form:tiposindicesDialogo");
                    RequestContext.getCurrentInstance().execute("PF('tiposindicesDialogo').show()");
                    tipoActualizacion = 0;
                }
            } else {
                System.out.println("PUSE UN VACIO");
                listIndices.get(indice).getTipoindice().setDescripcion(tipoIndice);
                listIndices.get(indice).setTipoindice(new TiposIndices());
                coincidencias = 1;
            }

            if (coincidencias == 1) {
                if (tipoLista == 0) {
                    if (!crearIndices.contains(listIndices.get(indice))) {

                        if (modificarIndices.isEmpty()) {
                            modificarIndices.add(listIndices.get(indice));
                        } else if (!modificarIndices.contains(listIndices.get(indice))) {
                            modificarIndices.add(listIndices.get(indice));
                        }
                        if (guardado == true) {
                            guardado = false;
                        }
                        RequestContext.getCurrentInstance().update("form:datosIndices");
                    }
                    index = -1;
                    secRegistro = null;
                } else {
                    if (!crearIndices.contains(filtrarIndices.get(indice))) {

                        if (modificarIndices.isEmpty()) {
                            modificarIndices.add(filtrarIndices.get(indice));
                        } else if (!modificarIndices.contains(filtrarIndices.get(indice))) {
                            modificarIndices.add(filtrarIndices.get(indice));
                        }
                        if (guardado == true) {
                            guardado = false;
                        }
                    }
                    index = -1;
                    secRegistro = null;
                }
            }

            RequestContext.getCurrentInstance().update("form:datosIndices");

        }
        if (confirmarCambio.equalsIgnoreCase("CODIGO")) {
            System.err.println("ENTRE A MODIFICAR HvReferencia, CONFIRMAR CAMBIO ES N");
            if (tipoLista == 0) {
                if (!crearIndices.contains(listIndices.get(indice))) {

                    if (listIndices.get(indice).getCodigo() == null) {
                        mensajeValidacion = "CODIGO VACIO";
                        listIndices.get(indice).setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < listIndices.size(); j++) {
                            if (j != indice) {
                                if (listIndices.get(indice).getCodigo().equals(listIndices.get(j).getCodigo())) {
                                    contador++;
                                }
                            }
                        }
                        contadorBD = administrarIndices.contarCodigosRepetidosIndices(listIndices.get(indice).getCodigo());
                        System.out.println("ControlIndices modificarIndices ContadorDB : " + contadorBD.intValue());
                        if (contador > 0 || !contadorBD.equals(new BigInteger("0"))) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            listIndices.get(indice).setCodigo(backUpCodigo);
                        } else {
                            pass++;

                        }
                    }
                    if (pass == 1) {
                        if (modificarIndices.isEmpty()) {
                            modificarIndices.add(listIndices.get(indice));
                        } else if (!modificarIndices.contains(listIndices.get(indice))) {
                            modificarIndices.add(listIndices.get(indice));
                        }
                        if (guardado == true) {
                            guardado = false;
                        }

                    } else {
                        RequestContext.getCurrentInstance().update("form:validacionModificar");
                        RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");
                    }
                    index = -1;
                    secRegistro = null;
                } else {
                    if (listIndices.get(indice).getCodigo() == null) {
                        mensajeValidacion = "CODIGO VACIO";
                        listIndices.get(indice).setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < listIndices.size(); j++) {
                            if (j != indice) {
                                if (listIndices.get(indice).getCodigo().equals(listIndices.get(j).getCodigo())) {
                                    contador++;
                                }
                            }
                        }
                        contadorBD = administrarIndices.contarCodigosRepetidosIndices(listIndices.get(indice).getCodigo());
                        if (contador > 0 || !contadorBD.equals(new BigInteger("0"))) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            listIndices.get(indice).setCodigo(backUpCodigo);
                        } else {
                            pass++;

                        }
                    }

                    if (pass == 1) {

                        if (guardado == true) {
                            guardado = false;
                        }

                    } else {
                        RequestContext.getCurrentInstance().update("form:validacionModificar");
                        RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");
                    }
                    index = -1;
                    secRegistro = null;
                }
            } else {

                if (!crearIndices.contains(filtrarIndices.get(indice))) {
                    if (filtrarIndices.get(indice).getCodigo() == null) {
                        mensajeValidacion = "CODIGO VACIO";
                        filtrarIndices.get(indice).setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < listIndices.size(); j++) {
                            if (j != indice) {
                                if (filtrarIndices.get(indice).getCodigo().equals(filtrarIndices.get(j).getCodigo())) {
                                    contador++;
                                }
                            }
                        }
                        contadorBD = administrarIndices.contarCodigosRepetidosIndices(filtrarIndices.get(indice).getCodigo());
                        if (contador > 0 || !contadorBD.equals(new BigInteger("0"))) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            filtrarIndices.get(indice).setCodigo(backUpCodigo);
                        } else {
                            pass++;

                        }
                    }
                    if (pass == 1) {
                        if (modificarIndices.isEmpty()) {
                            modificarIndices.add(filtrarIndices.get(indice));
                        } else if (!modificarIndices.contains(filtrarIndices.get(indice))) {
                            modificarIndices.add(filtrarIndices.get(indice));
                        }
                        if (guardado == true) {
                            guardado = false;
                        }

                    } else {
                        RequestContext.getCurrentInstance().update("form:validacionModificar");
                        RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");

                    }
                    index = -1;
                    secRegistro = null;
                } else {
                    if (filtrarIndices.get(indice).getCodigo() == null) {
                        mensajeValidacion = "CODIGO VACIO";
                        filtrarIndices.get(indice).setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < listIndices.size(); j++) {
                            if (j != indice) {
                                if (filtrarIndices.get(indice).getCodigo().equals(filtrarIndices.get(j).getCodigo())) {
                                    contador++;
                                }
                            }
                        }
                        contadorBD = administrarIndices.contarCodigosRepetidosIndices(filtrarIndices.get(indice).getCodigo());
                        if (contador > 0 || !contadorBD.equals(new BigInteger("0"))) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            filtrarIndices.get(indice).setCodigo(backUpCodigo);
                        } else {
                            pass++;

                        }
                    }
                    if (pass == 1) {

                        if (guardado == true) {
                            guardado = false;
                        }

                    } else {
                        RequestContext.getCurrentInstance().update("form:validacionModificar");
                        RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");

                    }
                    index = -1;
                    secRegistro = null;
                }

            }
            RequestContext.getCurrentInstance().update("form:datosIndices");
        }
        RequestContext.getCurrentInstance().update("form:datosIndices");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void actualizarTipoIndice() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                listIndices.get(index).setTipoindice(tipoIndiceSeleccionado);

                if (!crearIndices.contains(listIndices.get(index))) {
                    if (modificarIndices.isEmpty()) {
                        modificarIndices.add(listIndices.get(index));
                    } else if (!modificarIndices.contains(listIndices.get(index))) {
                        modificarIndices.add(listIndices.get(index));
                    }
                }
            } else {
                filtrarIndices.get(index).setTipoindice(tipoIndiceSeleccionado);

                if (!crearIndices.contains(filtrarIndices.get(index))) {
                    if (modificarIndices.isEmpty()) {
                        modificarIndices.add(filtrarIndices.get(index));
                    } else if (!modificarIndices.contains(filtrarIndices.get(index))) {
                        modificarIndices.add(filtrarIndices.get(index));
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
            }
            permitirIndex = true;
            // RequestContext.getCurrentInstance().update("form:datosIndices");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        } else if (tipoActualizacion == 1) {
            nuevoIndices.setTipoindice(tipoIndiceSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevooHvReferenciaLab");
        } else if (tipoActualizacion == 2) {
            duplicarIndices.setTipoindice(tipoIndiceSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarRRL");
        }
        filtradoTiposIndices = null;
        tipoIndiceSeleccionado = null;
        aceptar = true;
        index = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("form:lovTiposIndices:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovTiposIndices').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('tiposindicesDialogo').hide()");
        //RequestContext.getCurrentInstance().update("form:lovTiposIndices");
        RequestContext.getCurrentInstance().update("form:datosIndices");
    }

    public void cancelarCambioTiposIndices() {
        if (index >= 0) {
            listIndices.get(index).setTipoindice(tipoIndiceSeleccionado);
        }
        filtradoTiposIndices = null;
        tipoIndiceSeleccionado = null;
        aceptar = true;
        index = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        permitirIndex = true;
    }

    public void borrandoIndices() {

        if (index >= 0) {
            if (tipoLista == 0) {
                System.out.println("Entro a borrandoIndices");
                if (!modificarIndices.isEmpty() && modificarIndices.contains(listIndices.get(index))) {
                    int modIndex = modificarIndices.indexOf(listIndices.get(index));
                    modificarIndices.remove(modIndex);
                    borrarIndices.add(listIndices.get(index));
                } else if (!crearIndices.isEmpty() && crearIndices.contains(listIndices.get(index))) {
                    int crearIndex = crearIndices.indexOf(listIndices.get(index));
                    crearIndices.remove(crearIndex);
                } else {
                    borrarIndices.add(listIndices.get(index));
                }
                listIndices.remove(index);
            }
            if (tipoLista == 1) {
                System.out.println("borrandoIndices ");
                if (!modificarIndices.isEmpty() && modificarIndices.contains(filtrarIndices.get(index))) {
                    int modIndex = modificarIndices.indexOf(filtrarIndices.get(index));
                    modificarIndices.remove(modIndex);
                    borrarIndices.add(filtrarIndices.get(index));
                } else if (!crearIndices.isEmpty() && crearIndices.contains(filtrarIndices.get(index))) {
                    int crearIndex = crearIndices.indexOf(filtrarIndices.get(index));
                    crearIndices.remove(crearIndex);
                } else {
                    borrarIndices.add(filtrarIndices.get(index));
                }
                int VCIndex = listIndices.indexOf(filtrarIndices.get(index));
                listIndices.remove(VCIndex);
                filtrarIndices.remove(index);

            }
            RequestContext context = RequestContext.getCurrentInstance();
            if (listIndices == null || listIndices.isEmpty()) {
                infoRegistro = "Cantidad de registros: 0 ";
            } else {
                infoRegistro = "Cantidad de registros: " + listIndices.size();
            }
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            RequestContext.getCurrentInstance().update("form:datosIndices");
            index = -1;
            secRegistro = null;

            if (guardado == true) {
                guardado = false;
            }
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }

    }

    public void verificarBorrado() {
        System.out.println("Estoy en verificarBorrado");
        BigInteger competenciasCargos;
        BigInteger contarResultadosIndicesDetallesIndice;
        BigInteger contarResultadosIndicesIndice;
        BigInteger contarResultadosIndicesSoportesIndice;
        BigInteger contarUsuariosIndicesIndice;
        try {
            System.err.println("Control Secuencia de ControlIndices ");
            competenciasCargos = administrarIndices.contarParametrosIndicesIndice(listIndices.get(index).getSecuencia());
            contarResultadosIndicesDetallesIndice = administrarIndices.contarResultadosIndicesDetallesIndice(listIndices.get(index).getSecuencia());
            contarResultadosIndicesIndice = administrarIndices.contarResultadosIndicesIndice(listIndices.get(index).getSecuencia());
            contarResultadosIndicesSoportesIndice = administrarIndices.contarResultadosIndicesSoportesIndice(listIndices.get(index).getSecuencia());
            contarUsuariosIndicesIndice = administrarIndices.contarUsuariosIndicesIndice(listIndices.get(index).getSecuencia());

            if (competenciasCargos.equals(new BigInteger("0"))
                    && contarResultadosIndicesDetallesIndice.equals(new BigInteger("0"))
                    && contarResultadosIndicesIndice.equals(new BigInteger("0"))
                    && contarResultadosIndicesSoportesIndice.equals(new BigInteger("0"))
                    && contarUsuariosIndicesIndice.equals(new BigInteger("0"))) {
                System.out.println("Borrado==0");
                borrandoIndices();
            } else {
                System.out.println("Borrado>0");

                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:validacionBorrar");
                RequestContext.getCurrentInstance().execute("PF('validacionBorrar').show()");
                index = -1;

            }
        } catch (Exception e) {
            System.err.println("ERROR ControlIndices verificarBorrado ERROR " + e);
        }
    }

    public void revisarDialogoGuardar() {

        if (!borrarIndices.isEmpty() || !crearIndices.isEmpty() || !modificarIndices.isEmpty()) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:confirmarGuardar");
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
        }

    }

    public void guardarIndice() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (guardado == false) {
            System.out.println("Realizando guardarIndice");
            if (!borrarIndices.isEmpty()) {
                for (int i = 0; i < borrarIndices.size(); i++) {
                    System.out.println("Borrando...");
                    if (borrarIndices.get(i).getTipoindice().getSecuencia() == null) {
                        borrarIndices.get(i).setTipoindice(null);
                    }
                }
                administrarIndices.borrarIndices(borrarIndices);
                //mostrarBorrados
                registrosBorrados = borrarIndices.size();
                RequestContext.getCurrentInstance().update("form:mostrarBorrados");
                RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
                borrarIndices.clear();
            }
            if (!crearIndices.isEmpty()) {
                for (int i = 0; i < crearIndices.size(); i++) {

                    if (crearIndices.get(i).getTipoindice().getSecuencia() == null) {
                        crearIndices.get(i).setTipoindice(null);
                    }
                }
                System.out.println("Creando...");
                administrarIndices.crearIndices(crearIndices);
                crearIndices.clear();
            }
            if (!modificarIndices.isEmpty()) {
                for (int i = 0; i < modificarIndices.size(); i++) {
                    if (modificarIndices.get(i).getTipoindice().getSecuencia() == null) {
                        modificarIndices.get(i).setTipoindice(null);
                    }
                }
                administrarIndices.modificarIndices(modificarIndices);
                modificarIndices.clear();
            }
            System.out.println("Se guardaron los datos con exito");
            listIndices = null;
            RequestContext.getCurrentInstance().update("form:datosIndices");
            FacesMessage msg = new FacesMessage("Información", "Se guardarón los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");

            k = 0;
        }
        index = -1;
        guardado = true;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");

    }

    public void editarCelda() {
        if (index >= 0) {
            if (tipoLista == 0) {
                editarIndices = listIndices.get(index);
            }
            if (tipoLista == 1) {
                editarIndices = filtrarIndices.get(index);
            }
            RequestContext context = RequestContext.getCurrentInstance();
            System.out.println("Entro a editar... valor celda: " + cualCelda);
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editCodigo");
                RequestContext.getCurrentInstance().execute("PF('editCodigo').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editDescripcion");
                RequestContext.getCurrentInstance().execute("PF('editDescripcion').show()");
                cualCelda = -1;

            } else if (cualCelda == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editTipoIndice");
                RequestContext.getCurrentInstance().execute("PF('editTipoIndice').show()");
                cualCelda = -1;

            } else if (cualCelda == 3) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editPorcentajeEstandar");
                RequestContext.getCurrentInstance().execute("PF('editPorcentajeEstandar').show()");
                cualCelda = -1;

            } else if (cualCelda == 4) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editObjetivo");
                RequestContext.getCurrentInstance().execute("PF('editObjetivo').show()");
                cualCelda = -1;
            } else if (cualCelda == 5) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editDividendo");
                RequestContext.getCurrentInstance().execute("PF('editDividendo').show()");
                cualCelda = -1;
            } else if (cualCelda == 6) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editDivisor");
                RequestContext.getCurrentInstance().execute("PF('editDivisor').show()");
                cualCelda = -1;
            }

        }
        index = -1;
        secRegistro = null;
    }

    public void valoresBackupAutocompletar(int tipoNuevo, String Campo) {

        if (Campo.equalsIgnoreCase("TIPOSINDICES")) {
            if (tipoNuevo == 1) {
                nuevoParentesco = nuevoIndices.getTipoindice().getDescripcion();
            } else if (tipoNuevo == 2) {
                nuevoParentesco = duplicarIndices.getTipoindice().getDescripcion();
            }
        }
        System.err.println("NUEVO PARENTESCO " + nuevoParentesco);
    }

    public void autocompletarNuevoyDuplicado(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("TIPOSINDICES")) {
            if (!nuevoIndices.getTipoindice().getDescripcion().equals("")) {
                if (tipoNuevo == 1) {
                    nuevoIndices.getTipoindice().setDescripcion(nuevoParentesco);
                }
                for (int i = 0; i < listaClavesAjustes.size(); i++) {
                    if (listaClavesAjustes.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
            } else {

                if (tipoNuevo == 1) {
                    nuevoIndices.setTipoindice(new TiposIndices());
                    coincidencias = 1;
                }
                coincidencias = 1;
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevoIndices.setTipoindice(listaClavesAjustes.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoNombreSucursal");
                }
                listaClavesAjustes.clear();
                listaClavesAjustes = null;
                getListaTiposIndices();
            } else {
                RequestContext.getCurrentInstance().update("form:tiposindicesDialogo");
                RequestContext.getCurrentInstance().execute("PF('tiposindicesDialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoNombreSucursal");
                } else if (tipoNuevo == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarNombreSucursal");
                }
            }
        }
    }

    public void autocompletarDuplicado(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("TIPOSINDICES")) {
            if (!duplicarIndices.getTipoindice().getDescripcion().equals("")) {
                if (tipoNuevo == 2) {
                    duplicarIndices.getTipoindice().setDescripcion(nuevoParentesco);
                }
                for (int i = 0; i < listaClavesAjustes.size(); i++) {
                    if (listaClavesAjustes.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    if (tipoNuevo == 2) {
                        duplicarIndices.setTipoindice(listaClavesAjustes.get(indiceUnicoElemento));
                        RequestContext.getCurrentInstance().update("formularioDialogos:duplicarNombreSucursal");

                    }
                    listaClavesAjustes.clear();
                    listaClavesAjustes = null;
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarNombreSucursal");
                    getListaTiposIndices();
                } else {
                    RequestContext.getCurrentInstance().update("form:tiposindicesDialogo");
                    RequestContext.getCurrentInstance().execute("PF('tiposindicesDialogo').show()");
                    tipoActualizacion = tipoNuevo;
                    if (tipoNuevo == 2) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:duplicarNombreSucursal");
                    }
                }
            } else {
                if (tipoNuevo == 2) {
                    duplicarIndices.setTipoindice(new TiposIndices());
                    System.out.println("NUEVO PARENTESCO " + nuevoParentesco);
                    if (tipoLista == 0) {
                        if (index >= 0) {
                            listIndices.get(index).getTipoindice().setDescripcion(nuevoParentesco);
                            System.err.println("tipo lista" + tipoLista);
                            System.err.println("Secuencia Parentesco " + listIndices.get(index).getTipoindice().getSecuencia());
                        }
                    } else if (tipoLista == 1) {
                        filtrarIndices.get(index).getTipoindice().setDescripcion(nuevoParentesco);
                    }

                }
            }

            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarNombreSucursal");

        }
    }

    public void asignarVariableTiposIndicesNuevo(int tipoNuevo) {
        if (tipoNuevo == 0) {
            tipoActualizacion = 1;
        }
        if (tipoNuevo == 1) {
            tipoActualizacion = 2;
        }
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:tiposindicesDialogo");
        RequestContext.getCurrentInstance().execute("PF('tiposindicesDialogo').show()");
    }

    public void agregarNuevoIndices() {
        System.out.println("agregarNuevoIndices");
        int contador = 0;
        int pass = 0;
        BigInteger contadorBD;
        Short a = 0;
        a = null;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();

        if (nuevoIndices.getCodigo() == null) {
            mensajeValidacion = mensajeValidacion + "   *Codigo \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            for (int j = 0; j < listIndices.size(); j++) {
                if (nuevoIndices.getCodigo().equals(listIndices.get(j).getCodigo())) {
                    contador++;
                }

            }
            contadorBD = administrarIndices.contarCodigosRepetidosIndices(nuevoIndices.getCodigo());
            if (contador > 0 || !contadorBD.equals(new BigInteger("0"))) {
                mensajeValidacion = "*Codigo Repetidos";
                System.out.println("mensaje validación : " + mensajeValidacion);
                contador++;
            }
            if (contador == 0) {
                pass++;
            }

        }
        if (nuevoIndices.getPorcentajeestandar() != null) {
            if (nuevoIndices.getPorcentajeestandar().intValueExact() >= 0 && nuevoIndices.getPorcentajeestandar().intValueExact() <= 100) {
                pass++;
            } else {
                mensajeValidacion = mensajeValidacion + "*Porcentaje Estandar debe estar entre 0 y 100";
            }
        } else {
            pass++;
        }

        if (pass == 2)  {
            FacesContext c = FacesContext.getCurrentInstance();
            if (bandera == 1) {
                //CERRAR FILTRADO
                System.out.println("Desactivar");
                codigo = (Column) c.getViewRoot().findComponent("form:datosIndices:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosIndices:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                tipoindice = (Column) c.getViewRoot().findComponent("form:datosIndices:tipoindice");
                tipoindice.setFilterStyle("display: none; visibility: hidden;");
                porcentajeestan = (Column) c.getViewRoot().findComponent("form:datosIndices:porcentajeestan");
                porcentajeestan.setFilterStyle("display: none; visibility: hidden;");
                objetivo = (Column) c.getViewRoot().findComponent("form:datosIndices:objetivo");
                objetivo.setFilterStyle("display: none; visibility: hidden;");
                dividendo = (Column) c.getViewRoot().findComponent("form:datosIndices:dividendo");
                dividendo.setFilterStyle("display: none; visibility: hidden;");
                divisor = (Column) c.getViewRoot().findComponent("form:datosIndices:divisor");
                divisor.setFilterStyle("display: none; visibility: hidden;");

                RequestContext.getCurrentInstance().update("form:datosIndices");
                bandera = 0;
                filtrarIndices = null;
                tipoLista = 0;
            }
            System.out.println("Despues de la bandera");

            k++;
            l = BigInteger.valueOf(k);
            nuevoIndices.setSecuencia(l);

            crearIndices.add(nuevoIndices);
            listIndices.add(nuevoIndices);
            nuevoIndices = new Indices();
            nuevoIndices.setTipoindice(new TiposIndices());
            RequestContext.getCurrentInstance().update("form:datosIndices");
            infoRegistro = "Cantidad de registros: " + listIndices.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }

            RequestContext.getCurrentInstance().execute("PF('nuevoRegistroIndices').hide()");
            index = -1;
            secRegistro = null;

        } else {
            RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevaCentroCosto').show()");
            contador = 0;
        }
    }

    public void limpiarNuevoIndices() {
        System.out.println("limpiarNuevoIndices");
        nuevoIndices = new Indices();
        nuevoIndices.setTipoindice(new TiposIndices());
        secRegistro = null;
        index = -1;

    }

    //------------------------------------------------------------------------------
    public void duplicandoIndices() {
        System.out.println("duplicandoIndices");
        if (index >= 0) {
            duplicarIndices = new Indices();
            k++;
            l = BigInteger.valueOf(k);

            if (tipoLista == 0) {
                duplicarIndices.setSecuencia(l);
                duplicarIndices.setCodigo(listIndices.get(index).getCodigo());
                duplicarIndices.setDescripcion(listIndices.get(index).getDescripcion());
                duplicarIndices.setTipoindice(listIndices.get(index).getTipoindice());
                duplicarIndices.setPorcentajeestandar(listIndices.get(index).getPorcentajeestandar());
                duplicarIndices.setObjetivo(listIndices.get(index).getObjetivo());
                duplicarIndices.setDividendo(listIndices.get(index).getDividendo());
                duplicarIndices.setDivisor(listIndices.get(index).getDivisor());
            }
            if (tipoLista == 1) {
                duplicarIndices.setSecuencia(l);
                duplicarIndices.setCodigo(filtrarIndices.get(index).getCodigo());
                duplicarIndices.setDescripcion(filtrarIndices.get(index).getDescripcion());
                duplicarIndices.setTipoindice(filtrarIndices.get(index).getTipoindice());
                duplicarIndices.setPorcentajeestandar(filtrarIndices.get(index).getPorcentajeestandar());
                duplicarIndices.setObjetivo(filtrarIndices.get(index).getObjetivo());
                duplicarIndices.setDividendo(filtrarIndices.get(index).getDividendo());
                duplicarIndices.setDivisor(filtrarIndices.get(index).getDivisor());
            }

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarRRL");
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroIndices').show()");
            secRegistro = null;
        }
    }

    public void confirmarDuplicar() {
        System.err.println("ESTOY EN CONFIRMAR DUPLICAR INDICES");
        int contador = 0;
        int pass = 0;
        BigInteger contadorBD;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        if (duplicarIndices.getCodigo() == null) {
            mensajeValidacion = mensajeValidacion + "   *Codigo \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            for (int j = 0; j < listIndices.size(); j++) {
                if (duplicarIndices.getCodigo().equals(listIndices.get(j).getCodigo())) {
                    contador++;
                }

            }
            contadorBD = administrarIndices.contarCodigosRepetidosIndices(duplicarIndices.getCodigo());
            if (contador > 0 || !contadorBD.equals(new BigInteger("0"))) {
                mensajeValidacion = "*Codigo Repetidos";
                System.out.println("mensaje validación : " + mensajeValidacion);
                contador++;
            }
            if (contador == 0) {
                pass++;
            }

        }
        if (duplicarIndices.getPorcentajeestandar() != null) {
            if (duplicarIndices.getPorcentajeestandar().intValueExact() >= 0 && duplicarIndices.getPorcentajeestandar().intValueExact() <= 100) {
                pass++;
            } else {
                mensajeValidacion = mensajeValidacion + "*Porcentaje Estandar debe estar entre 0 y 100";
            }
        } else {
            pass++;
        }

        if (pass == 2) {

            if (crearIndices.contains(duplicarIndices)) {
                System.out.println("Ya lo contengo.");
            } else {
                crearIndices.add(duplicarIndices);
            }
            listIndices.add(duplicarIndices);
            RequestContext.getCurrentInstance().update("form:datosIndices");
            index = -1;
            secRegistro = null;

            if (guardado == true) {
                guardado = false;
            }
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            infoRegistro = "Cantidad de registros: " + listIndices.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");

            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                codigo = (Column) c.getViewRoot().findComponent("form:datosIndices:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosIndices:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                tipoindice = (Column) c.getViewRoot().findComponent("form:datosIndices:tipoindice");
                tipoindice.setFilterStyle("display: none; visibility: hidden;");
                porcentajeestan = (Column) c.getViewRoot().findComponent("form:datosIndices:porcentajeestan");
                porcentajeestan.setFilterStyle("display: none; visibility: hidden;");
                objetivo = (Column) c.getViewRoot().findComponent("form:datosIndices:objetivo");
                objetivo.setFilterStyle("display: none; visibility: hidden;");
                dividendo = (Column) c.getViewRoot().findComponent("form:datosIndices:dividendo");
                dividendo.setFilterStyle("display: none; visibility: hidden;");
                divisor = (Column) c.getViewRoot().findComponent("form:datosIndices:divisor");
                divisor.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosIndices");
                bandera = 0;
                filtrarIndices = null;
                tipoLista = 0;
            }
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroIndices').hide()");
            duplicarIndices = new Indices();

        } else {
            contador = 0;
            RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
            RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
        }
    }

    public void limpiarDuplicarIndices() {
        duplicarIndices = new Indices();
        duplicarIndices.setTipoindice(new TiposIndices());
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosIndicesExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "INDICES", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosIndicesExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "INDICES", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        System.out.println("lol");
        if (!listIndices.isEmpty()) {
            if (secRegistro != null) {
                System.out.println("lol 2");
                int resultado = administrarRastros.obtenerTabla(secRegistro, "INDICES"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
            if (administrarRastros.verificarHistoricosTabla("INDICES")) { // igual acá
                RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
            } else {
                RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
            }

        }
        index = -1;
    }

    //*/*/*/*/*/*/*/*/*/*-/-*//-*/-*/*/*-*/-*/-*/*/*/*/*/---/*/*/*/*/-*/-*/-*/-*/-*/
    public List<Indices> getListIndices() {
        if (listIndices == null) {
            listIndices = administrarIndices.mostrarIndices();
        }
        for (int z = 0; z < listIndices.size(); z++) {
            if (listIndices.get(z).getTipoindice() == null) {
                listIndices.get(z).setTipoindice(new TiposIndices());
            }
        }
        RequestContext context = RequestContext.getCurrentInstance();
        if (listIndices == null || listIndices.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listIndices.size();
        }
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
        return listIndices;
    }

    public void setListIndices(List<Indices> listIndices) {
        this.listIndices = listIndices;
    }

    public List<Indices> getFiltrarIndices() {
        return filtrarIndices;
    }

    public void setFiltrarIndices(List<Indices> filtrarIndices) {
        this.filtrarIndices = filtrarIndices;
    }

    public List<Indices> getCrearIndices() {
        return crearIndices;
    }

    public void setCrearIndices(List<Indices> crearIndices) {
        this.crearIndices = crearIndices;
    }

    public Indices getNuevoIndices() {
        return nuevoIndices;
    }

    public void setNuevoIndices(Indices nuevoIndices) {
        this.nuevoIndices = nuevoIndices;
    }

    public Indices getDuplicarIndices() {
        return duplicarIndices;
    }

    public void setDuplicarIndices(Indices duplicarIndices) {
        this.duplicarIndices = duplicarIndices;
    }

    public BigInteger getSecRegistro() {
        return secRegistro;
    }

    public void setSecRegistro(BigInteger secRegistro) {
        this.secRegistro = secRegistro;
    }

    public int getRegistrosBorrados() {
        return registrosBorrados;
    }

    public void setRegistrosBorrados(int registrosBorrados) {
        this.registrosBorrados = registrosBorrados;
    }

    public List<TiposIndices> getListaTiposIndices() {
        if (listaClavesAjustes == null) {
            listaClavesAjustes = administrarIndices.consultarLOVTiposIndices();
        }
        RequestContext context = RequestContext.getCurrentInstance();
        if (listaClavesAjustes == null || listaClavesAjustes.isEmpty()) {
            infoRegistroParentesco = "Cantidad de registros: 0 ";
        } else {
            infoRegistroParentesco = "Cantidad de registros: " + listaClavesAjustes.size();
        }
        RequestContext.getCurrentInstance().update("form:infoRegistroParentesco");
        return listaClavesAjustes;
    }

    public void setListaTiposIndices(List<TiposIndices> listaTiposIndices) {
        this.listaClavesAjustes = listaTiposIndices;
    }

    public List<TiposIndices> getFiltradoTiposIndices() {
        return filtradoTiposIndices;
    }

    public void setFiltradoTiposIndices(List<TiposIndices> filtradoTiposIndices) {
        this.filtradoTiposIndices = filtradoTiposIndices;
    }

    public String getMensajeValidacion() {
        return mensajeValidacion;
    }

    public void setMensajeValidacion(String mensajeValidacion) {
        this.mensajeValidacion = mensajeValidacion;
    }

    public Indices getEditarIndices() {
        return editarIndices;
    }

    public void setEditarIndices(Indices editarIndices) {
        this.editarIndices = editarIndices;
    }

    public boolean isGuardado() {
        return guardado;
    }

    public void setGuardado(boolean guardado) {
        this.guardado = guardado;
    }

    public Indices getIndicesSeleccionada() {
        return hvReferencia1Seleccionada;
    }

    public void setIndicesSeleccionada(Indices hvReferencia1Seleccionada) {
        this.hvReferencia1Seleccionada = hvReferencia1Seleccionada;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public void setAceptar(boolean aceptar) {
        this.aceptar = aceptar;
    }

    public String getInfoRegistro() {
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

    public String getInfoRegistroParentesco() {
        return infoRegistroParentesco;
    }

    public void setInfoRegistroParentesco(String infoRegistroParentesco) {
        this.infoRegistroParentesco = infoRegistroParentesco;
    }

    public int getTamano() {
        return tamano;
    }

    public void setTamano(int tamano) {
        this.tamano = tamano;
    }

    public TiposIndices getTipoIndiceSeleccionado() {
        return tipoIndiceSeleccionado;
    }

    public void setTipoIndiceSeleccionado(TiposIndices tipoIndiceSeleccionado) {
        this.tipoIndiceSeleccionado = tipoIndiceSeleccionado;
    }

}
