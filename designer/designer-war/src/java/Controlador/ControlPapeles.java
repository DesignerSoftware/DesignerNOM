/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Papeles;
import Entidades.Empresas;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarPapelesInterface;
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
public class ControlPapeles implements Serializable {

   private static Logger log = Logger.getLogger(ControlPapeles.class);

    @EJB
    AdministrarPapelesInterface administrarPapeles;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    private int cualCelda, tipoLista, index, tipoActualizacion, k, bandera;
    private BigInteger l;
    private boolean aceptar, guardado;
    //AutoCompletar
    private boolean permitirIndex;
    //RASTRO
    private BigInteger secRegistro;
    //borrado
    private int registrosBorrados;
    private String mensajeValidacion;
//EMPRESA
    private List<Empresas> lovEmpresas;
    private List<Empresas> lovEmpresasFiltrar;
    private Empresas empresaSeleccionada;
    private int banderaModificacionEmpresa;
    private int indiceEmpresaMostrada;
//LISTA CENTRO COSTO
    private List<Papeles> listPapelesPorEmpresa;
    private List<Papeles> listPapelesPorEmpresaBoton;
    private List<Papeles> filtrarPapeles;
    private List<Papeles> crearPapeles;
    private List<Papeles> modificarPapeles;
    private List<Papeles> borrarPapeles;
    private Papeles nuevoPapel;
    private Papeles duplicarPapel;
    private Papeles editarPapel;
    private Papeles papelinho;

    private int tamano;
    private Column codigoCC, nombrePapel,
            codigoAT;

    //AUTOCOMPLETAR
    private String grupoTipoPapelAutocompletar;
    private List<Papeles> filterPapelesPorEmpresa;
    private String nuevoTipoCCAutoCompletar;
    private Empresas backUpEmpresaActual;

    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();
    private Papeles PapelesPorEmpresaSeleccionado;
    private boolean banderaSeleccionPapelesPorEmpresa;

    public ControlPapeles() {
        permitirIndex = true;
        lovEmpresas = null;
        empresaSeleccionada = null;
        indiceEmpresaMostrada = 0;
        listPapelesPorEmpresa = null;
        listPapelesPorEmpresaBoton = null;
        crearPapeles = new ArrayList<Papeles>();
        modificarPapeles = new ArrayList<Papeles>();
        borrarPapeles = new ArrayList<Papeles>();
        editarPapel = new Papeles();
        nuevoPapel = new Papeles();
        duplicarPapel = new Papeles();
        aceptar = true;
        lovEmpresasFiltrar = null;
        guardado = true;
        banderaSeleccionPapelesPorEmpresa = false;
        tamano = 270;
        mapParametros.put("paginaAnterior", paginaAnterior);
    }

    public void limpiarListasValor() {
        lovEmpresas = null;
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
            administrarPapeles.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            log.error("Error postconstruct " + this.getClass().getName() + ":  ", e);
            log.error("Causa: " + e.getCause());
        }
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
        String pagActual = "papel";
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

    public void eventoFiltrar() {
        try {
            log.info("\n ENTRE A CONTROLPAPELES.eventoFiltrar \n");
            if (tipoLista == 0) {
                tipoLista = 1;
            }
            RequestContext context = RequestContext.getCurrentInstance();
            infoRegistro = "Cantidad de registros: " + filtrarPapeles.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
        } catch (Exception e) {
            log.warn("Error CONTROLPAPELES eventoFiltrar ERROR===" + e.getMessage());
        }
    }
    private String backUpCodigo;
    private String backUpDescripcion;
    private String backUpCodigoAlternativo;

    public void cambiarIndice(int indice, int celda) {
        log.error("BETA CENTRO COSTO TIPO LISTA = " + tipoLista);
        log.error("PERMITIR INDEX = " + permitirIndex);

        if (permitirIndex == true) {
            index = indice;
            cualCelda = celda;
            if (tipoLista == 0) {
                if (cualCelda == 0) {
                    backUpCodigo = listPapelesPorEmpresa.get(indice).getCodigo();
                } else if (cualCelda == 1) {
                    backUpDescripcion = listPapelesPorEmpresa.get(indice).getDescripcion();
                } else if (cualCelda == 2) {
                    backUpCodigoAlternativo = listPapelesPorEmpresa.get(indice).getCodigoalternativo();
                }
            } else if (cualCelda == 0) {
                backUpCodigo = filtrarPapeles.get(indice).getCodigo();
            } else if (cualCelda == 1) {
                backUpDescripcion = filtrarPapeles.get(indice).getDescripcion();
            } else if (cualCelda == 2) {
                backUpCodigoAlternativo = filtrarPapeles.get(indice).getCodigoalternativo();
            }

            log.error("CAMBIAR INDICE CUALCELDA = " + cualCelda);
            secRegistro = listPapelesPorEmpresa.get(index).getSecuencia();
            log.error("Sec Registro = " + secRegistro);
        }
        log.info("Indice: " + index + " Celda: " + cualCelda);
    }

    public void modificandoPapel(int indice, String confirmarCambio, String valorConfirmar) {

        log.error("ENTRE A MODIFICAR CENTROCOSTO");
        index = indice;
        banderaModificacionEmpresa = 1;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        boolean banderita = false;
        boolean banderita1 = false;
        int contador = 0;
        Short a;
        a = null;
        RequestContext context = RequestContext.getCurrentInstance();
        log.error("TIPO LISTA = " + tipoLista);
        if (confirmarCambio.equalsIgnoreCase("N")) {
            log.error("ENTRE A MODIFICAR CENTROCOSTO, CONFIRMAR CAMBIO ES N");
            if (tipoLista == 0) {
                if (!crearPapeles.contains(listPapelesPorEmpresa.get(indice))) {
                    if (listPapelesPorEmpresa.get(indice).getCodigo().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        listPapelesPorEmpresa.get(indice).setCodigo(backUpCodigo);
                        banderita = false;
                    } else if (listPapelesPorEmpresa.get(indice).getCodigo().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listPapelesPorEmpresa.get(indice).setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < listPapelesPorEmpresa.size(); j++) {
                            if (j != indice) {
                                if (listPapelesPorEmpresa.get(indice).getCodigo().equals(listPapelesPorEmpresa.get(j).getCodigo())) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            listPapelesPorEmpresa.get(indice).setCodigo(backUpCodigo);
                            banderita = false;
                        } else {
                            banderita = true;
                        }

                    }

                    if (listPapelesPorEmpresa.get(indice).getDescripcion().isEmpty()) {
                        listPapelesPorEmpresa.get(indice).setDescripcion(backUpDescripcion);
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita1 = false;
                    } else if (listPapelesPorEmpresa.get(indice).getDescripcion().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita1 = false;
                        listPapelesPorEmpresa.get(indice).setDescripcion(backUpDescripcion);

                    } else {
                        banderita1 = true;
                    }

                    if (banderita == true && banderita1 == true) {
                        if (modificarPapeles.isEmpty()) {
                            modificarPapeles.add(listPapelesPorEmpresa.get(indice));
                        } else if (!modificarPapeles.contains(listPapelesPorEmpresa.get(indice))) {
                            modificarPapeles.add(listPapelesPorEmpresa.get(indice));
                        }
                        if (guardado == true) {
                            guardado = false;

                        }
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");

                    } else {
                        RequestContext.getCurrentInstance().update("form:validacionModificar");
                        RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");
                    }
                    index = -1;
                    secRegistro = null;
                }
            } else if (!crearPapeles.contains(filtrarPapeles.get(indice))) {
                if (filtrarPapeles.get(indice).getCodigo().isEmpty()) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                    filtrarPapeles.get(indice).setCodigo(backUpCodigo);
                } else if (filtrarPapeles.get(indice).getCodigo().equals(" ")) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                    filtrarPapeles.get(indice).setCodigo(backUpCodigo);
                } else {
                    for (int j = 0; j < listPapelesPorEmpresa.size(); j++) {
                        if (j != indice) {
                            if (filtrarPapeles.get(indice).getCodigo().equals(listPapelesPorEmpresa.get(j).getCodigo())) {
                                contador++;
                            }
                        }
                    }
                    for (int j = 0; j < filtrarPapeles.size(); j++) {
                        if (j != indice) {
                            if (filtrarPapeles.get(indice).getCodigo().equals(filtrarPapeles.get(j).getCodigo())) {
                                contador++;
                            }
                        }
                    }
                    if (contador > 0) {
                        filtrarPapeles.get(indice).setCodigo(backUpCodigo);
                        mensajeValidacion = "CODIGOS REPETIDOS";
                        banderita = false;
                    } else {
                        banderita = true;
                    }
                }

                if (filtrarPapeles.get(indice).getDescripcion().isEmpty()) {
                    filtrarPapeles.get(indice).setDescripcion(backUpDescripcion);
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita1 = false;
                }
                if (filtrarPapeles.get(indice).getDescripcion().equals(" ")) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    filtrarPapeles.get(indice).setDescripcion(backUpDescripcion);
                    banderita1 = false;
                } else {
                    banderita1 = true;
                }

                if (banderita == true && banderita1 == true) {
                    if (modificarPapeles.isEmpty()) {
                        modificarPapeles.add(filtrarPapeles.get(indice));
                    } else if (!modificarPapeles.contains(filtrarPapeles.get(indice))) {
                        modificarPapeles.add(filtrarPapeles.get(indice));
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
            RequestContext.getCurrentInstance().update("form:datosPapeles");
        }
        RequestContext.getCurrentInstance().update("form:datosPapeles");

    }
    private String infoRegistro;

    public void cancelarModificacion() {
        try {
            log.info("entre a CONTROLPAPELES.cancelarModificacion");
            FacesContext c = FacesContext.getCurrentInstance();
            if (bandera == 1) {
                //CERRAR FILTRADO
                //0
                codigoCC = (Column) c.getViewRoot().findComponent("form:datosPapeles:codigoCC");
                codigoCC.setFilterStyle("display: none; visibility: hidden;");
                //1
                nombrePapel = (Column) c.getViewRoot().findComponent("form:datosPapeles:nombrePapel");
                nombrePapel.setFilterStyle("display: none; visibility: hidden;");
                codigoAT = (Column) c.getViewRoot().findComponent("form:datosPapeles:codigoAT");
                codigoAT.setFilterStyle("display: none; visibility: hidden;");

                bandera = 0;
                filtrarPapeles = null;
                tipoLista = 0;
            }

            borrarPapeles.clear();
            crearPapeles.clear();
            modificarPapeles.clear();
            index = -1;
            k = 0;
            listPapelesPorEmpresa = null;
            guardado = true;
            permitirIndex = true;
            getListPapelesPorEmpresa();
            RequestContext context = RequestContext.getCurrentInstance();
            if (listPapelesPorEmpresa == null || listPapelesPorEmpresa.isEmpty()) {
                infoRegistro = "Cantidad de registros: 0 ";
            } else {
                infoRegistro = "Cantidad de registros: " + listPapelesPorEmpresa.size();
            }
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            banderaModificacionEmpresa = 0;
            if (banderaModificacionEmpresa == 0) {
                cambiarEmpresa();
            }
            RequestContext.getCurrentInstance().update("form:datosPapeles");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        } catch (Exception E) {
            log.warn("Error CONTROLPAPELES.ModificarModificacion ERROR====================" + E.getMessage());
        }
    }

    public void salir() {
        limpiarListasValor();
        try {
            log.info("entre a CONTROLPAPELES.cancelarModificacion");
            FacesContext c = FacesContext.getCurrentInstance();
            if (bandera == 1) {
                //CERRAR FILTRADO
                //0
                codigoCC = (Column) c.getViewRoot().findComponent("form:datosPapeles:codigoCC");
                codigoCC.setFilterStyle("display: none; visibility: hidden;");
                //1
                nombrePapel = (Column) c.getViewRoot().findComponent("form:datosPapeles:nombrePapel");
                nombrePapel.setFilterStyle("display: none; visibility: hidden;");
                codigoAT = (Column) c.getViewRoot().findComponent("form:datosPapeles:codigoAT");
                codigoAT.setFilterStyle("display: none; visibility: hidden;");

                bandera = 0;
                filtrarPapeles = null;
                tipoLista = 0;
            }

            borrarPapeles.clear();
            crearPapeles.clear();
            modificarPapeles.clear();
            index = -1;
            k = 0;
            listPapelesPorEmpresa = null;
            guardado = true;
            permitirIndex = true;
            getListPapelesPorEmpresa();
            RequestContext context = RequestContext.getCurrentInstance();
            if (listPapelesPorEmpresa == null || listPapelesPorEmpresa.isEmpty()) {
                infoRegistro = "Cantidad de registros: 0 ";
            } else {
                infoRegistro = "Cantidad de registros: " + listPapelesPorEmpresa.size();
            }
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            banderaModificacionEmpresa = 0;
            if (banderaModificacionEmpresa == 0) {
                cambiarEmpresa();
            }
            RequestContext.getCurrentInstance().update("form:datosPapeles");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        } catch (Exception E) {
            log.warn("Error CONTROLPAPELES.ModificarModificacion ERROR====================" + E.getMessage());
        }
    }

    public void asignarIndex(Integer indice, int LND, int dig) {
        try {
            log.info("\n ENTRE A CONTROLPAPELES.asignarIndex \n");
            index = indice;
            RequestContext context = RequestContext.getCurrentInstance();

            if (LND == 0) {
                tipoActualizacion = 0;
            } else if (LND == 1) {
                tipoActualizacion = 1;
                log.info("Tipo Actualizacion: " + tipoActualizacion);
            } else if (LND == 2) {
                tipoActualizacion = 2;
            }
        } catch (Exception e) {
            log.warn("Error CONTROLPAPELES.asignarIndex ERROR======" + e.getMessage());
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void seleccionPapelesPorEmpresa() {
        try {
            RequestContext context = RequestContext.getCurrentInstance();

            if (guardado == true) {
                listPapelesPorEmpresa.clear();
                log.error("seleccionPapelesPorEmpresa " + PapelesPorEmpresaSeleccionado.getDescripcion());
                listPapelesPorEmpresa.add(PapelesPorEmpresaSeleccionado);
                log.error("listPapelesPorEmpresa tamaño " + listPapelesPorEmpresa.size());
                log.error("listPapelesPorEmpresa nombre " + listPapelesPorEmpresa.get(0).getDescripcion());
                PapelesPorEmpresaSeleccionado = null;
                filterPapelesPorEmpresa = null;
                aceptar = true;
                banderaModificacionEmpresa = 1;
                RequestContext.getCurrentInstance().update("form:datosPapeles");
                RequestContext.getCurrentInstance().execute("PF('buscarPapelesDialogo').hide()");
                context.reset("formularioDialogos:lovPapeles:globalFilter");
            }
            /*else {
             log.error("listPapelesPorEmpresa tamaño " + listPapelesPorEmpresa.size());
             log.error("listPapelesPorEmpresa nombre " + listPapelesPorEmpresa.get(0).getDescripcion());
             banderaSeleccionPapelesPorEmpresa = true;
             RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
             PapelesPorEmpresaSeleccionado = null;
             listPapelesPorEmpresa.clear();
             log.error("seleccionPapelesPorEmpresa " + PapelesPorEmpresaSeleccionado.getDescripcion());
             listPapelesPorEmpresa.add(PapelesPorEmpresaSeleccionado);
             filterPapelesPorEmpresa = null;
             aceptar = true;
             banderaModificacionEmpresa = 0;
             RequestContext.getCurrentInstance().execute("PF('buscarPapelesDialogo').hide()");
             context.reset("formularioDialogos:lovPapeles:globalFilter");
             }*/

        } catch (Exception e) {
            log.warn("Error CONTROLPAPELES.seleccionaVigencia ERROR====" + e.getMessage());
        }
    }

    public void cancelarSeleccionPapelPorEmpresa() {
        try {
            RequestContext context = RequestContext.getCurrentInstance();
            PapelesPorEmpresaSeleccionado = null;
            filterPapelesPorEmpresa = null;
            aceptar = true;
            index = -1;
            tipoActualizacion = -1;
            RequestContext.getCurrentInstance().update("form:ACEPTARNCC");

        } catch (Exception e) {
            log.warn("Error CONTROLPAPELES.cancelarSeleccionVigencia ERROR====" + e.getMessage());
        }
    }

    public void limpiarNuevoPapeles() {
        log.info("\n ENTRE A CONTROLPAPELES.limpiarNuevoPapeles \n");
        try {
            nuevoPapel = new Papeles();
            index = -1;
        } catch (Exception e) {
            log.warn("Error CONTROLPAPELES.LimpiarNuevoPapeles ERROR=============================" + e.getMessage());
        }
    }

    public void agregarNuevoPapeles() {
        log.info("\n ENTRE A CONTROLPAPELES.agregarNuevoPapeles \n");
        try {
            int contador = 0;
            mensajeValidacion = " ";
            int duplicados = 0;
            RequestContext context = RequestContext.getCurrentInstance();

            banderaModificacionEmpresa = 1;
            if (nuevoPapel.getCodigo() == null) {
                mensajeValidacion = mensajeValidacion + "   * Un codigo \n";
                log.info("Mensaje validacion : " + mensajeValidacion);

            } else if (nuevoPapel.getCodigo().isEmpty()) {
                mensajeValidacion = mensajeValidacion + "   * Un codigo \n";
                log.info("Mensaje validacion : " + mensajeValidacion);

            } else if (nuevoPapel.getCodigo().equals(" ")) {
                mensajeValidacion = mensajeValidacion + "   * Un codigo \n";

            } else {
                log.info("codigo en Motivo Cambio Cargo: " + nuevoPapel.getCodigo());

                for (int x = 0; x < listPapelesPorEmpresa.size(); x++) {
                    if (listPapelesPorEmpresa.get(x).getCodigo().equals(nuevoPapel.getCodigo())) {
                        duplicados++;
                    }
                }
                log.info("Antes del if Duplicados eses igual  : " + duplicados);

                if (duplicados > 0) {
                    mensajeValidacion = " *Que NO hayan codigos repetidos \n";
                    log.info("Mensaje validacion : " + mensajeValidacion);
                } else {
                    log.info("bandera");
                    contador++;
                }
            }
            if (nuevoPapel.getDescripcion() == null) {
                mensajeValidacion = mensajeValidacion + "   * una descripción \n";
                log.info("Mensaje validacion : " + mensajeValidacion);

            } else if (nuevoPapel.getDescripcion().isEmpty()) {
                mensajeValidacion = mensajeValidacion + "   * una descripción \n";
                log.info("Mensaje validacion : " + mensajeValidacion);

            } else if (nuevoPapel.getDescripcion().equals(" ")) {
                mensajeValidacion = mensajeValidacion + "   * una descripción \n";

            } else {
                log.info("Bandera : ");
                contador++;
            }

            if (contador == 2) {
                k++;
                l = BigInteger.valueOf(k);
                nuevoPapel.setSecuencia(l);
                log.error("AGREGAR CODIGO " + nuevoPapel.getCodigo());
                log.error("AGREGAR DESCRIPCION" + nuevoPapel.getDescripcion());
                nuevoPapel.setEmpresa(empresaSeleccionada);
                if (crearPapeles.contains(nuevoPapel)) {
                    log.info("Ya lo contengo.");
                } else {
                    crearPapeles.add(nuevoPapel);

                }
                listPapelesPorEmpresa.add(nuevoPapel);
                RequestContext.getCurrentInstance().update("form:datosPapeles");
                infoRegistro = "Cantidad de registros: " + listPapelesPorEmpresa.size();
                RequestContext.getCurrentInstance().update("form:informacionRegistro");
                nuevoPapel = new Papeles();
                // index = -1;
                secRegistro = null;
                if (guardado == true) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
                if (bandera == 1) {
                    FacesContext c = FacesContext.getCurrentInstance();
                    codigoCC = (Column) c.getViewRoot().findComponent("form:datosPapeles:codigoCC");
                    codigoCC.setFilterStyle("display: none; visibility: hidden;");
                    nombrePapel = (Column) c.getViewRoot().findComponent("form:datosPapeles:nombrePapel");
                    nombrePapel.setFilterStyle("display: none; visibility: hidden;");
                    codigoAT = (Column) c.getViewRoot().findComponent("form:datosPapeles:codigoAT");
                    codigoAT.setFilterStyle("display: none; visibility: hidden;");
                    RequestContext.getCurrentInstance().update("form:datosPapeles");
                    tamano = 270;

                    bandera = 0;
                    filtrarPapeles = null;
                    tipoLista = 0;
                }
                mensajeValidacion = " ";
                RequestContext.getCurrentInstance().execute("PF('NuevoRegistroPapeles').hide()");

            } else {
                contador = 0;
                RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
                RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
            }

        } catch (Exception e) {
            log.warn("Error CONTROLPAPELES.agregarNuevoPapeles ERROR===========================" + e.getMessage());
        }
    }

    public void mostrarDialogoListaEmpresas() {
        RequestContext context = RequestContext.getCurrentInstance();
        index = -1;
        RequestContext.getCurrentInstance().execute("PF('buscarPapelesDialogo').show()");
    }

    public void duplicandoPapeles() {
        try {
            banderaModificacionEmpresa = 1;
            log.info("\n ENTRE A CONTROLPAPELES.DUPLICARPAPELES INDEX : " + index);

            if (index >= 0) {
                log.info("\n ENTRE A CONTROLPAPELES.DUPLICARPAPELES TIPOLISTA : " + tipoLista);

                duplicarPapel = new Papeles();
                k++;
                l = BigInteger.valueOf(k);
                if (tipoLista == 0) {
                    duplicarPapel.setSecuencia(l);
                    duplicarPapel.setEmpresa(listPapelesPorEmpresa.get(index).getEmpresa());
                    duplicarPapel.setCodigo(listPapelesPorEmpresa.get(index).getCodigo());
                    duplicarPapel.setDescripcion(listPapelesPorEmpresa.get(index).getDescripcion());
                    duplicarPapel.setCodigoalternativo(listPapelesPorEmpresa.get(index).getCodigoalternativo());
                }
                if (tipoLista == 1) {

                    duplicarPapel.setSecuencia(l);
                    duplicarPapel.setEmpresa(filtrarPapeles.get(index).getEmpresa());
                    duplicarPapel.setCodigo(filtrarPapeles.get(index).getCodigo());
                    duplicarPapel.setDescripcion(filtrarPapeles.get(index).getDescripcion());
                    duplicarPapel.setCodigoalternativo(filtrarPapeles.get(index).getCodigoalternativo());

                }

                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("formularioDialogos:duplicarPapeles");
                RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroPapeles').show()");
                index = -1;
            }
        } catch (Exception e) {
            log.warn("Error CONTROLPAPELES.DuplicarPapeles ERROR===============" + e.getMessage());
        }
    }

    public void limpiarDuplicarPapeles() {
        log.info("\n ENTRE A CONTROLPAPELES.limpiarDuplicarPapeles \n");
        try {
            duplicarPapel = new Papeles();
        } catch (Exception e) {
            log.warn("Error CONTROLPAPELES.limpiarDuplicarPapeles ERROR========" + e.getMessage());
        }

    }

    public void confirmarDuplicar() {
        log.error("ESTOY EN CONFIRMAR DUPLICAR CONTROLTIPOSCENTROSCOSTOS");
        int contador = 0;
        mensajeValidacion = " ";
        int duplicados = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        Short a = 0;
        a = null;

        if (duplicarPapel.getCodigo().isEmpty()) {
            mensajeValidacion = mensajeValidacion + "   * Un codigo \n";
            log.info("Mensaje validacion : " + mensajeValidacion);

        } else if (duplicarPapel.getCodigo().equals(" ")) {
            mensajeValidacion = mensajeValidacion + "   * Un codigo \n";

        } else {
            log.info("codigo en Motivo Cambio Cargo: " + duplicarPapel.getCodigo());

            for (int x = 0; x < listPapelesPorEmpresa.size(); x++) {
                if (listPapelesPorEmpresa.get(x).getCodigo().equals(duplicarPapel.getCodigo())) {
                    duplicados++;
                }
            }
            log.info("Antes del if Duplicados eses igual  : " + duplicados);

            if (duplicados > 0) {
                mensajeValidacion = " *Que NO hayan codigos repetidos \n";
                log.info("Mensaje validacion : " + mensajeValidacion);
            } else {
                log.info("bandera");
                contador++;
            }

        }
        if (duplicarPapel.getDescripcion().isEmpty()) {
            mensajeValidacion = mensajeValidacion + "   * una descripción \n";
            log.info("Mensaje validacion : " + mensajeValidacion);

        } else if (duplicarPapel.getDescripcion().equals(" ")) {
            mensajeValidacion = mensajeValidacion + "   * una descripción \n";

        } else {
            log.info("Bandera : ");
            contador++;
        }

        if (contador == 2) {
            log.error("DUPLICAR CODIGO " + duplicarPapel.getCodigo());

            log.error("DUPLICAR DESCRIPCION " + duplicarPapel.getDescripcion());
            if (crearPapeles.contains(duplicarPapel)) {
                log.info("Ya lo contengo.");
            } else {
                listPapelesPorEmpresa.add(duplicarPapel);
            }
            crearPapeles.add(duplicarPapel);
            RequestContext.getCurrentInstance().update("form:datosPapeles");
            infoRegistro = "Cantidad de registros: " + listPapelesPorEmpresa.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            index = -1;
            secRegistro = null;
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            if (bandera == 1) {
                //CERRAR FILTRADO
                FacesContext c = FacesContext.getCurrentInstance();

                codigoCC = (Column) c.getViewRoot().findComponent("form:datosPapeles:codigoCC");
                codigoCC.setFilterStyle("display: none; visibility: hidden;");
                //1
                nombrePapel = (Column) c.getViewRoot().findComponent("form:datosPapeles:nombrePapel");
                nombrePapel.setFilterStyle("display: none; visibility: hidden;");
                codigoAT = (Column) c.getViewRoot().findComponent("form:datosPapeles:codigoAT");
                codigoAT.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosPapeles");
                bandera = 0;
                filtrarPapeles = null;
                tipoLista = 0;
                tamano = 270;

            }
            duplicarPapel = new Papeles();
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroPapeles').hide()");
            mensajeValidacion = " ";
            banderaModificacionEmpresa = 1;

        } else {
            contador = 0;
            RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
            RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
        }
    }

    public void verificarBorrado() {
        log.info("Estoy en verificarBorrado");
        log.info("TIPOLISTA = " + tipoLista);
        BigInteger contarVigenciasCargosPapel;

        try {
            if (tipoLista == 0) {
                contarVigenciasCargosPapel = administrarPapeles.contarVigenciasCargosPapel(listPapelesPorEmpresa.get(index).getSecuencia());
            } else {
                contarVigenciasCargosPapel = administrarPapeles.contarVigenciasCargosPapel(filtrarPapeles.get(index).getSecuencia());

            }
            if (contarVigenciasCargosPapel.equals(new BigInteger("0"))) {
                log.info("Borrado==0");
                borrandoPapel();
            } else {

                log.info("Borrado>0");
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:validacionBorrar");
                RequestContext.getCurrentInstance().execute("PF('validacionBorrar').show()");
                index = -1;
                contarVigenciasCargosPapel = new BigInteger("-1");
            }
        } catch (Exception e) {
            log.error("ERROR CONTROL BETA CENTROS COSTOS verificarBorrado ERROR  ", e);
        }
    }

    public void borrandoPapel() {
        try {
            banderaModificacionEmpresa = 1;
            if (index >= 0) {
                if (tipoLista == 0) {
                    if (!modificarPapeles.isEmpty() && modificarPapeles.contains(listPapelesPorEmpresa.get(index))) {
                        int modIndex = modificarPapeles.indexOf(listPapelesPorEmpresa.get(index));
                        modificarPapeles.remove(modIndex);
                        borrarPapeles.add(listPapelesPorEmpresa.get(index));
                    } else if (!crearPapeles.isEmpty() && crearPapeles.contains(listPapelesPorEmpresa.get(index))) {
                        int crearIndex = crearPapeles.indexOf(listPapelesPorEmpresa.get(index));
                        crearPapeles.remove(crearIndex);
                    } else {

                        borrarPapeles.add(listPapelesPorEmpresa.get(index));
                    }
                    listPapelesPorEmpresa.remove(index);
                }
                if (tipoLista == 1) {
                    if (!modificarPapeles.isEmpty() && modificarPapeles.contains(filtrarPapeles.get(index))) {
                        log.info("\n 6 ENTRE A CONTROLPAPELES.borrarPapel tipolista==1 try if if if filtrarPapeles.get(index).getCodigo()" + filtrarPapeles.get(index).getCodigo());

                        int modIndex = modificarPapeles.indexOf(filtrarPapeles.get(index));
                        modificarPapeles.remove(modIndex);
                        borrarPapeles.add(filtrarPapeles.get(index));
                    } else if (!crearPapeles.isEmpty() && crearPapeles.contains(filtrarPapeles.get(index))) {
                        log.info("\n 7 ENTRE A CONTROLPAPELES.borrarPapel tipolista==1 try if if if filtrarPapeles.get(index).getCodigo()" + filtrarPapeles.get(index).getCodigo());
                        int crearIndex = crearPapeles.indexOf(filtrarPapeles.get(index));
                        crearPapeles.remove(crearIndex);
                    } else {
                        log.info("\n 8 ENTRE A CONTROLPAPELES.borrarPapel tipolista==1 try if if if filtrarPapeles.get(index).getCodigo()" + filtrarPapeles.get(index).getCodigo());
                        borrarPapeles.add(filtrarPapeles.get(index));
                    }
                    int VCIndex = listPapelesPorEmpresa.indexOf(filtrarPapeles.get(index));
                    listPapelesPorEmpresa.remove(VCIndex);
                    filtrarPapeles.remove(index);
                }

                RequestContext context = RequestContext.getCurrentInstance();
                index = -1;
                log.error("verificar Borrado " + guardado);
                if (guardado == true) {
                    guardado = false;
                }
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
                RequestContext.getCurrentInstance().update("form:datosPapeles");
                infoRegistro = "Cantidad de registros: " + listPapelesPorEmpresa.size();
                RequestContext.getCurrentInstance().update("form:informacionRegistro");
            }
        } catch (Exception e) {
            log.warn("Error CONTROLPAPELES.BorrarPapel ERROR=====================" + e.getMessage());
        }
    }

    public void guardarCambiosPapel() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (guardado == false) {
            log.info("Realizando Operaciones Vigencias Localizacion");
            if (!borrarPapeles.isEmpty()) {
                administrarPapeles.borrarPapeles(borrarPapeles);
                //mostrarBorrados
                registrosBorrados = borrarPapeles.size();
                RequestContext.getCurrentInstance().update("form:mostrarBorrados");
                RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
                borrarPapeles.clear();
            }
            if (!crearPapeles.isEmpty()) {
                administrarPapeles.crearPapeles(crearPapeles);
                crearPapeles.clear();
            }
            if (!modificarPapeles.isEmpty()) {
                administrarPapeles.modificarPapeles(modificarPapeles);
                modificarPapeles.clear();
            }
            log.info("Se guardaron los datos con exito");
            listPapelesPorEmpresa = null;
            RequestContext.getCurrentInstance().update("form:datosTipoPapel");
            k = 0;
            guardado = true;
            FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            if (banderaModificacionEmpresa == 0) {
                cambiarEmpresa();
                banderaModificacionEmpresa = 1;

            }
            if (banderaSeleccionPapelesPorEmpresa == true) {
                listPapelesPorEmpresaBoton = null;
                getListPapelesPorEmpresaBoton();
                index = -1;
                RequestContext.getCurrentInstance().update("formularioDialogos:lovPapeles");
                RequestContext.getCurrentInstance().execute("PF('buscarPapelesDialogo').show()");
                banderaSeleccionPapelesPorEmpresa = false;
            }
        }
        index = -1;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        banderaModificacionEmpresa = 0;
    }

    public void cancelarCambios() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (banderaModificacionEmpresa == 0) {
            empresaSeleccionada = backUpEmpresaActual;
            RequestContext.getCurrentInstance().update("formularioDialogos:lovEmpresas");
            banderaModificacionEmpresa = 1;
        }

    }

    public void activarCtrlF11() {
        log.info("\n ENTRE A CONTROLPAPELES.activarCtrlF11 \n");

        try {
            FacesContext c = FacesContext.getCurrentInstance();

            if (bandera == 0) {
                log.info("Activar");
                codigoCC = (Column) c.getViewRoot().findComponent("form:datosPapeles:codigoCC");
                codigoCC.setFilterStyle("width: 85% !important;");
                nombrePapel = (Column) c.getViewRoot().findComponent("form:datosPapeles:nombrePapel");
                nombrePapel.setFilterStyle("width: 85% !important;");
                codigoAT = (Column) c.getViewRoot().findComponent("form:datosPapeles:codigoAT");
                codigoAT.setFilterStyle("width: 85% !important;");
                RequestContext.getCurrentInstance().update("form:datosPapeles");
                bandera = 1;
                tamano = 250;

            } else if (bandera == 1) {
                log.info("Desactivar");
                //0
                codigoCC = (Column) c.getViewRoot().findComponent("form:datosPapeles:codigoCC");
                codigoCC.setFilterStyle("display: none; visibility: hidden;");
                nombrePapel = (Column) c.getViewRoot().findComponent("form:datosPapeles:nombrePapel");
                nombrePapel.setFilterStyle("display: none; visibility: hidden;");
                codigoAT = (Column) c.getViewRoot().findComponent("form:datosPapeles:codigoAT");
                codigoAT.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosPapeles");
                bandera = 0;
                filtrarPapeles = null;
                tipoLista = 0;
                tamano = 270;

            }
        } catch (Exception e) {

            log.warn("Error CONTROLPAPELES.activarCtrlF11 ERROR====================" + e.getMessage());
        }
    }

//--------------------------------------------------------------------------
    //METODOS MANIPULAR EMPRESA MOSTRADA
    //--------------------------------------------------------------------------
    public void cambiarEmpresaSeleccionada(int updown) {
        try {
            log.error("CONTROL CAMBIO EMPRESA BETA");
            if (banderaModificacionEmpresa == 1) {
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().execute("PF('confirmarCambioEmpresa').show()");
            } else if (banderaModificacionEmpresa == 0) {
                getLovEmpresas();
                for (int i = 0; i < lovEmpresas.size(); i++) {
                    log.info("CONTROLPAPELES.cambiarEmpresaSeleccionada: empresa: " + i + " nombre: " + lovEmpresas.get(i).getNombre());
                }
                log.info("CONTROLPAPELES.cambiarEmpresaSeleccionada: Entra a cambiar la empresa seleccionada");
                int temp = indiceEmpresaMostrada;
                log.info("CONTROLPAPELES.cambiarEmpresaSeleccionada: temp = " + temp);
                if (updown == 1) {
                    temp--;
                    log.info("CONTROLPAPELES.cambiarEmpresaSeleccionada: Arriba_ temp = " + temp + " lista: " + lovEmpresas.size());
                    if (temp >= 0 && temp < lovEmpresas.size()) {
                        indiceEmpresaMostrada = temp;
                        empresaSeleccionada = getLovEmpresas().get(indiceEmpresaMostrada);
                        getListPapelesPorEmpresaBoton();
                        log.info("CONTROLPAPELES.cambiarEmpresaSeleccionada: empresaSeleccionada = " + empresaSeleccionada.getNombre());
                        listPapelesPorEmpresa = administrarPapeles.consultarPapelesPorEmpresa(empresaSeleccionada.getSecuencia());
                        log.info("CONTROLPAPELES.cambiarEmpresaSeleccionada: Empresa cambio a: " + empresaSeleccionada.getNombre());
                        RequestContext context = RequestContext.getCurrentInstance();
                        RequestContext.getCurrentInstance().update("form:nombreEmpresa");
                        RequestContext.getCurrentInstance().update("form:nitEmpresa");
                        RequestContext.getCurrentInstance().update("form:datosPapeles");
                        RequestContext.getCurrentInstance().update("formularioDialogos:buscarPapelesDialogo");
                    }
                } else {
                    temp++;
                    log.info("CONTROLPAPELES.cambiarEmpresaSeleccionada: Abajo_ temp = " + temp + " lista: " + lovEmpresas.size());
                    if (temp >= 0 && temp < lovEmpresas.size()) {
                        indiceEmpresaMostrada = temp;
                        empresaSeleccionada = getLovEmpresas().get(indiceEmpresaMostrada);
                        getListPapelesPorEmpresaBoton();
                        log.info("CONTROLPAPELES.cambiarEmpresaSeleccionada: empresaSeleccionada = " + empresaSeleccionada.getNombre());
                        listPapelesPorEmpresa = administrarPapeles.consultarPapelesPorEmpresa(empresaSeleccionada.getSecuencia());
                        log.info("CONTROLPAPELES.cambiarEmpresaSeleccionada: Empresa cambio a: " + empresaSeleccionada.getNombre());
                        RequestContext context = RequestContext.getCurrentInstance();
                        RequestContext.getCurrentInstance().update("form:nombreEmpresa");
                        RequestContext.getCurrentInstance().update("form:nitEmpresa");
                        RequestContext.getCurrentInstance().update("form:datosPapeles");
                        RequestContext.getCurrentInstance().update("formularioDialogos:buscarPapelesDialogo");
                    }

                }
            }

        } catch (Exception e) {
            log.warn("Error CONTROLPAPELES.cambiarEmpresaSeleccionada ERROR======" + e.getMessage());
        }

    }

    public void editarCelda() {
        try {
            log.info("\n ENTRE A editarCelda INDEX  " + index);
            if (index >= 0) {
                log.info("\n ENTRE AeditarCelda TIPOLISTA " + tipoLista);
                if (tipoLista == 0) {
                    editarPapel = listPapelesPorEmpresa.get(index);
                }
                if (tipoLista == 1) {
                    editarPapel = filtrarPapeles.get(index);
                }
                RequestContext context = RequestContext.getCurrentInstance();
                log.info("CONTROLPAPELES: Entro a editar... valor celda: " + cualCelda);
                if (cualCelda == 0) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarCCC");
                    RequestContext.getCurrentInstance().execute("PF('editarCCC').show()");
                    cualCelda = -1;
                } else if (cualCelda == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarNCC");
                    RequestContext.getCurrentInstance().execute("PF('editarNCC').show()");
                    cualCelda = -1;
                } else if (cualCelda == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarTCC");
                    RequestContext.getCurrentInstance().execute("PF('editarTCC').show()");
                    cualCelda = -1;
                } else if (cualCelda == 3) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarMO");
                    RequestContext.getCurrentInstance().execute("PF('editarMO').show()");
                    cualCelda = -1;
                } else if (cualCelda == 4) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarCAT");
                    RequestContext.getCurrentInstance().execute("PF('editarCAT').show()");
                    cualCelda = -1;
                } else if (cualCelda == 5) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarO");
                    RequestContext.getCurrentInstance().execute("PF('editarO').show()");
                    cualCelda = -1;
                } else if (cualCelda == 6) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarCCTT");
                    RequestContext.getCurrentInstance().execute("PF('editarCCTT').show()");
                    cualCelda = -1;
                } else if (cualCelda == 7) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarD");
                    RequestContext.getCurrentInstance().execute("PF('editarD').show()");
                    cualCelda = -1;
                }
            }
            index = -1;
        } catch (Exception E) {
            log.warn("Error CONTROLPAPELES.editarCelDa ERROR=====================" + E.getMessage());
        }
    }

    public void listaValoresBoton() {
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosPapelesExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "Papeles", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
    }

    /**
     *
     * @throws IOException
     */
    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosPapelesExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "Papeles", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        log.info("lol");
        if (!listPapelesPorEmpresa.isEmpty()) {
            if (secRegistro != null) {
                log.info("lol 2");
                int resultado = administrarRastros.obtenerTabla(secRegistro, "CENTROSCOSTOS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
            } else {
                RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
            }
        } else if (administrarRastros.verificarHistoricosTabla("CENTROSCOSTOS")) { // igual acá
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
        index = -1;
    }

    public void lovEmpresas() {
        index = -1;
        secRegistro = null;
        cualCelda = -1;
        RequestContext.getCurrentInstance().execute("PF('EmpresasDialogo').show()");
    }

    public void cambiarEmpresa() {
        RequestContext context = RequestContext.getCurrentInstance();
        log.error("Cambiar empresa  GUARDADO = " + guardado);
        log.error("Cambiar empresa  GUARDADO = " + empresaSeleccionada.getNombre());
        if (guardado == true) {
            RequestContext.getCurrentInstance().update("form:nombreEmpresa");
            RequestContext.getCurrentInstance().update("form:nitEmpresa");
            getListPapelesPorEmpresa();
            getListPapelesPorEmpresaBoton();
            lovEmpresasFiltrar = null;
            listPapelesPorEmpresa = null;
            aceptar = true;
            context.reset("formularioDialogos:lovEmpresas:globalFilter");
            RequestContext.getCurrentInstance().execute("PF('lovEmpresas').clearFilters()");
            RequestContext.getCurrentInstance().execute("PF('EmpresasDialogo').hide()");
            //RequestContext.getCurrentInstance().update("formularioDialogos:lovEmpresas");
            backUpEmpresaActual = empresaSeleccionada;
            banderaModificacionEmpresa = 0;
            RequestContext.getCurrentInstance().update("form:datosPapeles");
            RequestContext.getCurrentInstance().update("formularioDialogos:lovPapeles");

        } else {
            banderaModificacionEmpresa = 0;
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
        }
    }

    public void cancelarCambioEmpresa() {
        lovEmpresasFiltrar = null;
        banderaModificacionEmpresa = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        index = -1;
        context.reset("formularioDialogos:lovEmpresas:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovEmpresas').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('EmpresasDialogo').hide()");
    }
//-----------------------------------------------------------------------------**

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

    public String getMensajeValidacion() {
        return mensajeValidacion;
    }

    public void setMensajeValidacion(String mensajeValidacion) {
        this.mensajeValidacion = mensajeValidacion;
    }
    private String infoRegistroEmpresas;

    public List<Empresas> getLovEmpresas() {
        try {
            if (lovEmpresas == null) {
                lovEmpresas = administrarPapeles.consultarEmpresas();
                if (!lovEmpresas.isEmpty()) {
                    empresaSeleccionada = lovEmpresas.get(0);
                    backUpEmpresaActual = empresaSeleccionada;
                }
            }
            RequestContext context = RequestContext.getCurrentInstance();
            if (lovEmpresas == null || lovEmpresas.isEmpty()) {
                infoRegistroEmpresas = "Cantidad de registros: 0 ";
            } else {
                infoRegistroEmpresas = "Cantidad de registros: " + lovEmpresas.size();
            }
            RequestContext.getCurrentInstance().update("form:infoRegistroEmpresas");
            return lovEmpresas;
        } catch (Exception e) {
            log.info("ERRO LISTA EMPRESAS  ", e);
            return null;
        }
    }

    public void setLovEmpresas(List<Empresas> lovEmpresas) {
        this.lovEmpresas = lovEmpresas;
    }

    public List<Empresas> getLovEmpresasFiltrar() {
        return lovEmpresasFiltrar;
    }

    public void setLovEmpresasFiltrar(List<Empresas> lovEmpresasFiltrar) {
        this.lovEmpresasFiltrar = lovEmpresasFiltrar;
    }

    public Empresas getEmpresaSeleccionada() {
        try {
            if (empresaSeleccionada == null) {
                getLovEmpresas();
                return empresaSeleccionada;
            }
        } catch (Exception e) {
            log.warn("Error CONTROLPAPELES.getEmpresaSeleccionada ERROR  ", e);
        } finally {
            return empresaSeleccionada;
        }
    }

    public void setEmpresaSeleccionada(Empresas empresaSeleccionada) {
        this.empresaSeleccionada = empresaSeleccionada;
    }

    public List<Papeles> getListPapelesPorEmpresa() {
        try {
            if (empresaSeleccionada == null) {
                getEmpresaSeleccionada();
                if (listPapelesPorEmpresa == null) {
                    listPapelesPorEmpresa = administrarPapeles.consultarPapelesPorEmpresa(empresaSeleccionada.getSecuencia());
                } else {
                    log.info(".-.");
                }
            } else if (listPapelesPorEmpresa == null) {
                listPapelesPorEmpresa = administrarPapeles.consultarPapelesPorEmpresa(empresaSeleccionada.getSecuencia());
            }
            RequestContext context = RequestContext.getCurrentInstance();
            if (listPapelesPorEmpresa == null || listPapelesPorEmpresa.isEmpty()) {
                infoRegistro = "Cantidad de registros: 0 ";
            } else {
                infoRegistro = "Cantidad de registros: " + listPapelesPorEmpresa.size();
            }
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            return listPapelesPorEmpresa;
        } catch (Exception e) {
            log.error("ERROR CONTROLPAPELES GETLISTAPAPELESPOREMPRESA ERROR :  ", e);
            return null;
        }
    }

    public List<Papeles> getListPapelesPorEmpresaBoton() {
        try {
            if (listPapelesPorEmpresaBoton == null) {
                //listPapelesPorEmpresaBoton = administrarPapeles.consultarPapelesPorEmpresa(empresaSeleccionada.getSecuencia());
                listPapelesPorEmpresaBoton = listPapelesPorEmpresa;
            }
            return listPapelesPorEmpresaBoton;
        } catch (Exception e) {
            log.info("ControlCentrosCosto: Error al recibir los Papeles de la empresa seleccionada /n" + e.getMessage());
            return null;
        }
    }

    public void setListPapelesPorEmpresaBoton(List<Papeles> listPapelesPorEmpresaBoton) {
        this.listPapelesPorEmpresaBoton = listPapelesPorEmpresaBoton;
    }

    public void setListPapelesPorEmpresa(List<Papeles> listPapelesPorEmpresa) {
        this.listPapelesPorEmpresa = listPapelesPorEmpresa;
    }

    public List<Papeles> getFiltrarPapeles() {
        return filtrarPapeles;
    }

    public void setFiltrarPapeles(List<Papeles> filtrarPapeles) {
        this.filtrarPapeles = filtrarPapeles;
    }

    public Papeles getNuevoPapel() {
        if (nuevoPapel == null) {
            nuevoPapel = new Papeles();
        }
        return nuevoPapel;
    }

    public void setNuevoPapel(Papeles nuevoPapel) {
        this.nuevoPapel = nuevoPapel;
    }

    public Papeles getDuplicarPapel() {
        return duplicarPapel;
    }

    public void setDuplicarPapel(Papeles duplicarPapel) {
        this.duplicarPapel = duplicarPapel;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public void setAceptar(boolean aceptar) {
        this.aceptar = aceptar;
    }

    public List<Papeles> getFilterPapelesPorEmpresa() {
        return filterPapelesPorEmpresa;
    }

    public void setFilterPapelesPorEmpresa(List<Papeles> filterPapelesPorEmpresa) {
        this.filterPapelesPorEmpresa = filterPapelesPorEmpresa;
    }

    public Papeles getPapelesPorEmpresaSeleccionado() {
        return PapelesPorEmpresaSeleccionado;
    }

    public void setPapelesPorEmpresaSeleccionado(Papeles PapelesPorEmpresaSeleccionado) {
        this.PapelesPorEmpresaSeleccionado = PapelesPorEmpresaSeleccionado;
    }

    public Papeles getEditarPapel() {
        return editarPapel;
    }

    public void setEditarPapel(Papeles editarPapel) {
        this.editarPapel = editarPapel;
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

    public Papeles getPapelinho() {
        return papelinho;
    }

    public void setPapelinho(Papeles papelinho) {
        this.papelinho = papelinho;
    }

    public String getInfoRegistro() {
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

    public String getInfoRegistroEmpresas() {
        return infoRegistroEmpresas;
    }

    public void setInfoRegistroEmpresas(String infoRegistroEmpresas) {
        this.infoRegistroEmpresas = infoRegistroEmpresas;
    }

}
