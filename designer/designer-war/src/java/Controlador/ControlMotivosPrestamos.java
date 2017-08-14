/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.MotivosPrestamos;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarMotivosPrestamosInterface;
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
public class ControlMotivosPrestamos implements Serializable {

   private static Logger log = Logger.getLogger(ControlMotivosPrestamos.class);

    @EJB
    AdministrarMotivosPrestamosInterface administrarMotivosPrestamos;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    private List<MotivosPrestamos> listMotivosPrestamos;
    private List<MotivosPrestamos> filtrarMotivosPrestamos;
    private List<MotivosPrestamos> crearMotivosPrestamos;
    private List<MotivosPrestamos> modificarMotivosPrestamos;
    private List<MotivosPrestamos> borrarMotivosPrestamos;
    private MotivosPrestamos nuevoMotivoPrestamo;
    private MotivosPrestamos duplicarMotivoPrestamo;
    private MotivosPrestamos editarMotivoPrestamo;
    //otros
    private int cualCelda, tipoLista, index, tipoActualizacion, k, bandera;
    private BigInteger l;
    private boolean aceptar, guardado;
    //AutoCompletar
    private boolean permitirIndex;
    //RASTRO
    private BigInteger secRegistro;
    private Column codigo, descripcion;
    //borrado
    private int registrosBorrados;
    private String mensajeValidacion;
    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

    public ControlMotivosPrestamos() {
        listMotivosPrestamos = null;
        crearMotivosPrestamos = new ArrayList<MotivosPrestamos>();
        modificarMotivosPrestamos = new ArrayList<MotivosPrestamos>();
        borrarMotivosPrestamos = new ArrayList<MotivosPrestamos>();
        permitirIndex = true;
        editarMotivoPrestamo = new MotivosPrestamos();
        nuevoMotivoPrestamo = new MotivosPrestamos();
        duplicarMotivoPrestamo = new MotivosPrestamos();
        guardado = true;
        mapParametros.put("paginaAnterior", paginaAnterior);
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
        String pagActual = "motivoprestamo";
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

    public void limpiarListasValor() {

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
            administrarMotivosPrestamos.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            log.error("Error postconstruct " + this.getClass().getName() + ": " + e);
            log.error("Causa: " + e.getCause());
        }
    }

    public void eventoFiltrar() {
        try {
            log.info("\n ENTRE A CONTROLMOTIVOSPRESTAMOS EVENTOFILTRAR \n");
            if (tipoLista == 0) {
                tipoLista = 1;
            }
        } catch (Exception e) {
            log.warn("Error CONTROLMOTIVOSPRESTAMOS EVENTOFILTRAR  ERROR===" + e.getMessage());
        }
    }

    public void cambiarIndice(int indice, int celda) {
        log.error("TIPO LISTA = " + tipoLista);

        if (permitirIndex == true) {
            index = indice;
            cualCelda = celda;
            secRegistro = listMotivosPrestamos.get(index).getSecuencia();

        }
        log.info("Indice: " + index + " Celda: " + cualCelda);
    }

    public void asignarIndex(Integer indice, int LND, int dig) {
        try {
            log.info("\n ENTRE A CONTROLMOTIVOSPRESTAMOS ASIGNAR INDEX \n");
            index = indice;
            if (LND == 0) {
                tipoActualizacion = 0;
            } else if (LND == 1) {
                tipoActualizacion = 1;
                log.info("TIPO ACTUALIZACION : " + tipoActualizacion);
            } else if (LND == 2) {
                tipoActualizacion = 2;
            }

        } catch (Exception e) {
            log.warn("Error CONTROLMOTIVOSPRESTAMOS ASIGNAR INDEX ERROR======" + e.getMessage());
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void listaValoresBoton() {
    }

    public void cancelarModificacion() {
        if (bandera == 1) {
            //CERRAR FILTRADO
            codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoReemplazo:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoReemplazo:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTipoReemplazo");
            bandera = 0;
            filtrarMotivosPrestamos = null;
            tipoLista = 0;
        }

        borrarMotivosPrestamos.clear();
        crearMotivosPrestamos.clear();
        modificarMotivosPrestamos.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listMotivosPrestamos = null;
        guardado = true;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosTipoReemplazo");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void activarCtrlF11() {
        if (bandera == 0) {

            codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoReemplazo:codigo");
            codigo.setFilterStyle("width: 85% !important;");
            descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoReemplazo:descripcion");
            descripcion.setFilterStyle("width: 85% !important;");
            RequestContext.getCurrentInstance().update("form:datosTipoReemplazo");
            log.info("Activar");
            bandera = 1;
        } else if (bandera == 1) {
            log.info("Desactivar");
            codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoReemplazo:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoReemplazo:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTipoReemplazo");
            bandera = 0;
            filtrarMotivosPrestamos = null;
            tipoLista = 0;
        }
    }

    public void modificandoMotivoPrestamos(int indice, String confirmarCambio, String valorConfirmar) {
        log.error("ENTRE A MODIFICAR MOTIVOSPRESTAMOS");
        index = indice;

        int contador = 0;
        int contadorGuardar = 0;
        boolean banderita = false;
        Integer a;
        a = null;
        RequestContext context = RequestContext.getCurrentInstance();
        log.error("TIPO LISTA = " + tipoLista);
        if (confirmarCambio.equalsIgnoreCase("N")) {
            log.error("ENTRE A MODIFICAR MOTIVOPRESTAMO, CONFIRMAR CAMBIO ES N");
            if (tipoLista == 0) {
                if (!crearMotivosPrestamos.contains(listMotivosPrestamos.get(indice))) {
                    if (listMotivosPrestamos.get(indice).getCodigo() == a || listMotivosPrestamos.get(indice).getCodigo().equals(null)) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                    } else {
                        for (int j = 0; j < listMotivosPrestamos.size(); j++) {
                            if (j != indice) {
                                if (listMotivosPrestamos.get(indice).getCodigo().equals(listMotivosPrestamos.get(j).getCodigo())) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            banderita = false;
                        } else {
                            contadorGuardar++;
                        }

                    }
                    if (listMotivosPrestamos.get(indice).getNombre().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                    } else if (listMotivosPrestamos.get(indice).getNombre().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                    } else {
                        contadorGuardar++;
                    }
                    if (listMotivosPrestamos.get(indice).getNombre() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                    } else if (listMotivosPrestamos.get(indice).getNombre().equals("")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                    } else {
                        contadorGuardar++;
                    }
                    if (contadorGuardar == 3) {
                        if (modificarMotivosPrestamos.isEmpty()) {
                            modificarMotivosPrestamos.add(listMotivosPrestamos.get(indice));
                        } else if (!modificarMotivosPrestamos.contains(listMotivosPrestamos.get(indice))) {
                            modificarMotivosPrestamos.add(listMotivosPrestamos.get(indice));
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
            } else if (!crearMotivosPrestamos.contains(filtrarMotivosPrestamos.get(indice))) {
                if (filtrarMotivosPrestamos.get(indice).getCodigo() == a) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                } else {
                    log.error("codigo ingresado " + filtrarMotivosPrestamos.get(indice).getCodigo());
                    for (int j = 0; j < listMotivosPrestamos.size(); j++) {
                        if (j != indice) {
                            if (filtrarMotivosPrestamos.get(indice).getCodigo().equals(listMotivosPrestamos.get(j).getCodigo())) {
                                log.error("Contador 1... " + contador);
                                log.error("lista" + listMotivosPrestamos.get(indice).getCodigo());
                                log.error("filtrar " + filtrarMotivosPrestamos.get(indice).getCodigo());
                                contador++;
                            }
                        }
                    }

                    //log.error("Contador 1... " + contador);
                    for (int j = 0; j < filtrarMotivosPrestamos.size(); j++) {
                        if (j != indice) {
                            if (filtrarMotivosPrestamos.get(indice).getCodigo().equals(filtrarMotivosPrestamos.get(j).getCodigo())) {
                                contador++;
                            }
                        }
                    }

                    log.error("Contador " + contador);
                    if (contador > 0) {
                        mensajeValidacion = "CODIGOS REPETIDOS";
                        banderita = false;
                    } else {
                        contadorGuardar++;
                    }

                }

                if (filtrarMotivosPrestamos.get(indice).getNombre().isEmpty()) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                } else if (filtrarMotivosPrestamos.get(indice).getNombre().equals(" ")) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                } else {
                    contadorGuardar++;
                }
                if (filtrarMotivosPrestamos.get(indice).getNombre() == null) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                } else if (filtrarMotivosPrestamos.get(indice).getNombre().equals("")) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                } else {
                    contadorGuardar++;
                }
                if (contadorGuardar == 3) {
                    if (modificarMotivosPrestamos.isEmpty()) {
                        modificarMotivosPrestamos.add(filtrarMotivosPrestamos.get(indice));
                    } else if (!modificarMotivosPrestamos.contains(filtrarMotivosPrestamos.get(indice))) {
                        modificarMotivosPrestamos.add(filtrarMotivosPrestamos.get(indice));
                    }
                    if (guardado == true) {
                        guardado = false;
                    }
                    contador = 0;
                } else {
                    RequestContext.getCurrentInstance().update("form:validacionModificar");
                    RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");
                    contador = 0;
                    cancelarModificacion();
                }
                index = -1;
                secRegistro = null;
            }
            RequestContext.getCurrentInstance().update("form:datosTipoReemplazo");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }

    }

    public void borrandoMotivosPrestamos() {

        if (index >= 0) {
            if (tipoLista == 0) {
                log.info("Entro a borrandoMotivosPrestamos");
                if (!modificarMotivosPrestamos.isEmpty() && modificarMotivosPrestamos.contains(listMotivosPrestamos.get(index))) {
                    int modIndex = modificarMotivosPrestamos.indexOf(listMotivosPrestamos.get(index));
                    modificarMotivosPrestamos.remove(modIndex);
                    borrarMotivosPrestamos.add(listMotivosPrestamos.get(index));
                } else if (!crearMotivosPrestamos.isEmpty() && crearMotivosPrestamos.contains(listMotivosPrestamos.get(index))) {
                    int crearIndex = crearMotivosPrestamos.indexOf(listMotivosPrestamos.get(index));
                    crearMotivosPrestamos.remove(crearIndex);
                } else {
                    borrarMotivosPrestamos.add(listMotivosPrestamos.get(index));
                }
                listMotivosPrestamos.remove(index);
            }
            if (tipoLista == 1) {
                log.info("borrandoMotivosPrestamos");
                if (!modificarMotivosPrestamos.isEmpty() && modificarMotivosPrestamos.contains(filtrarMotivosPrestamos.get(index))) {
                    int modIndex = modificarMotivosPrestamos.indexOf(filtrarMotivosPrestamos.get(index));
                    modificarMotivosPrestamos.remove(modIndex);
                    borrarMotivosPrestamos.add(filtrarMotivosPrestamos.get(index));
                } else if (!crearMotivosPrestamos.isEmpty() && crearMotivosPrestamos.contains(filtrarMotivosPrestamos.get(index))) {
                    int crearIndex = crearMotivosPrestamos.indexOf(filtrarMotivosPrestamos.get(index));
                    crearMotivosPrestamos.remove(crearIndex);
                } else {
                    borrarMotivosPrestamos.add(filtrarMotivosPrestamos.get(index));
                }
                int VCIndex = listMotivosPrestamos.indexOf(filtrarMotivosPrestamos.get(index));
                listMotivosPrestamos.remove(VCIndex);
                filtrarMotivosPrestamos.remove(index);

            }
            if (guardado == true) {
                guardado = false;
            }
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosTipoReemplazo");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            index = -1;
            secRegistro = null;

        }

    }

    private BigInteger verificarEerPrestamos;

    public void verificarBorrado() {
        log.info("ESTOY EN VERIFICAR BORRADO tipoLista " + tipoLista);
        try {
            log.info("secuencia borrado : " + listMotivosPrestamos.get(index).getSecuencia());
            if (tipoLista == 0) {
                log.info("secuencia borrado : " + listMotivosPrestamos.get(index).getSecuencia());
                verificarEerPrestamos = administrarMotivosPrestamos.verificarEersPrestamosMotivoPrestamo(listMotivosPrestamos.get(index).getSecuencia());
            } else {
                log.info("secuencia borrado : " + filtrarMotivosPrestamos.get(index).getSecuencia());
                verificarEerPrestamos = administrarMotivosPrestamos.verificarEersPrestamosMotivoPrestamo(filtrarMotivosPrestamos.get(index).getSecuencia());
            }
            if (!verificarEerPrestamos.equals(new BigInteger("0"))) {
                log.info("Borrado>0");

                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:validacionBorrar");
                RequestContext.getCurrentInstance().execute("PF('validacionBorrar').show()");
                index = -1;

                verificarEerPrestamos = new BigInteger("-1");

            } else {
                log.info("Borrado==0");
                borrandoMotivosPrestamos();
            }
        } catch (Exception e) {
            log.error("ERROR ControlTiposCertificados verificarBorrado ERROR " + e);
        }
    }

    public void revisarDialogoGuardar() {

        if (!borrarMotivosPrestamos.isEmpty() || !crearMotivosPrestamos.isEmpty() || !modificarMotivosPrestamos.isEmpty()) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:confirmarGuardar");
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
        }

    }

    public void guardarMotivoPrestamo() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (guardado == false) {
            log.info("Realizando MotivoPrestamo");
            if (!borrarMotivosPrestamos.isEmpty()) {
                administrarMotivosPrestamos.borrarMotivosPrestamos(borrarMotivosPrestamos);
                //mostrarBorrados
                registrosBorrados = borrarMotivosPrestamos.size();
                RequestContext.getCurrentInstance().update("form:mostrarBorrados");
                RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
                borrarMotivosPrestamos.clear();
            }
            if (!crearMotivosPrestamos.isEmpty()) {
                administrarMotivosPrestamos.crearMotivosPrestamos(crearMotivosPrestamos);

                crearMotivosPrestamos.clear();
            }
            if (!modificarMotivosPrestamos.isEmpty()) {
                administrarMotivosPrestamos.modificarMotivosPrestamos(modificarMotivosPrestamos);
                modificarMotivosPrestamos.clear();
            }
            log.info("Se guardaron los datos con exito");
            listMotivosPrestamos = null;
            guardado = true;
            RequestContext.getCurrentInstance().update("form:datosTipoReemplazo");
            k = 0;
        }
        index = -1;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");

    }

    public void editarCelda() {
        if (index >= 0) {
            if (tipoLista == 0) {
                editarMotivoPrestamo = listMotivosPrestamos.get(index);
            }
            if (tipoLista == 1) {
                editarMotivoPrestamo = filtrarMotivosPrestamos.get(index);
            }

            RequestContext context = RequestContext.getCurrentInstance();
            log.info("Entro a editar... valor celda: " + cualCelda);
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editCodigo");
                RequestContext.getCurrentInstance().execute("PF('editCodigo').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editDescripcion");
                RequestContext.getCurrentInstance().execute("PF('editDescripcion').show()");
                cualCelda = -1;

            }

        }
        index = -1;
        secRegistro = null;
    }

    public void agregarNuevoMotivosPrestamos() {
        log.info("agregarNuevoMotivosPrestamos");
        int contador = 0;
        int duplicados = 0;

        Integer a = 0;
        a = null;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        if (nuevoMotivoPrestamo.getCodigo() == a) {
            mensajeValidacion = " *Debe Tener Un Codigo \n";
            log.info("Mensaje validacion : " + mensajeValidacion);
        } else {
            log.info("codigo en Motivo Cambio Cargo: " + nuevoMotivoPrestamo.getCodigo());

            for (int x = 0; x < listMotivosPrestamos.size(); x++) {
                if (listMotivosPrestamos.get(x).getCodigo() == nuevoMotivoPrestamo.getCodigo()) {
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
        if (nuevoMotivoPrestamo.getNombre() == (null)) {
            mensajeValidacion = mensajeValidacion + " *Debe tener un Nombre Motivo Préstamo \n";
            log.info("Mensaje validacion : " + mensajeValidacion);

        } else {
            log.info("bandera");
            contador++;

        }

        log.info("contador " + contador);

        if (contador == 2) {
            if (bandera == 1) {
                //CERRAR FILTRADO
                log.info("Desactivar");
                codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoReemplazo:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoReemplazo:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosTipoReemplazo");
                bandera = 0;
                filtrarMotivosPrestamos = null;
                tipoLista = 0;
            }
            log.info("Despues de la bandera");

            k++;
            l = BigInteger.valueOf(k);
            nuevoMotivoPrestamo.setSecuencia(l);

            crearMotivosPrestamos.add(nuevoMotivoPrestamo);

            listMotivosPrestamos.add(nuevoMotivoPrestamo);
            nuevoMotivoPrestamo = new MotivosPrestamos();
            RequestContext.getCurrentInstance().update("form:datosTipoReemplazo");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }

            RequestContext.getCurrentInstance().execute("PF('nuevoRegistroTiposReemplazos').hide()");
            index = -1;
            secRegistro = null;

        } else {
            RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevaCentroCosto').show()");
            contador = 0;
        }
    }

    public void limpiarNuevoMotivosPrestamos() {
        log.info("limpiarNuevoMotivosPrestamos");
        nuevoMotivoPrestamo = new MotivosPrestamos();
        secRegistro = null;
        index = -1;

    }

    //------------------------------------------------------------------------------
    public void duplicandoMotivosPrestamos() {
        log.info("duplicandoMotivosPrestamos");
        if (index >= 0) {
            duplicarMotivoPrestamo = new MotivosPrestamos();
            k++;
            l = BigInteger.valueOf(k);

            if (tipoLista == 0) {
                duplicarMotivoPrestamo.setSecuencia(l);
                duplicarMotivoPrestamo.setCodigo(listMotivosPrestamos.get(index).getCodigo());
                duplicarMotivoPrestamo.setNombre(listMotivosPrestamos.get(index).getNombre());
            }
            if (tipoLista == 1) {
                duplicarMotivoPrestamo.setSecuencia(l);
                duplicarMotivoPrestamo.setCodigo(filtrarMotivosPrestamos.get(index).getCodigo());
                duplicarMotivoPrestamo.setNombre(filtrarMotivosPrestamos.get(index).getNombre());
            }

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTTR");
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroTiposReemplazos').show()");
            index = -1;
            secRegistro = null;
        }
    }

    public void confirmarDuplicar() {
        log.error("ESTOY EN CONFIRMAR DUPLICAR MOTIVOSPRESTAMOS");
        int contador = 0;
        mensajeValidacion = " ";
        int duplicados = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        Integer a = 0;
        a = null;

        if (duplicarMotivoPrestamo.getCodigo() == a) {
            mensajeValidacion = mensajeValidacion + "   * Codigo \n";
            log.info("Mensaje validacion : " + mensajeValidacion);
        } else {
            for (int x = 0; x < listMotivosPrestamos.size(); x++) {
                if (listMotivosPrestamos.get(x).getCodigo() == duplicarMotivoPrestamo.getCodigo()) {
                    duplicados++;
                }
            }
            if (duplicados > 0) {
                mensajeValidacion = " *Que NO Existan Codigo Repetidos \n";
                log.info("Mensaje validacion : " + mensajeValidacion);
            } else {
                log.info("bandera");
                contador++;
                duplicados = 0;
            }
        }
        if (duplicarMotivoPrestamo.getNombre() == null) {
            mensajeValidacion = mensajeValidacion + "   * Un Nombre Motivo Préstamo \n";
            log.info("Mensaje validacion : " + mensajeValidacion);

        } else {
            log.info("Bandera : ");
            contador++;
        }

        if (contador == 2) {

            log.info("Datos Duplicando: " + duplicarMotivoPrestamo.getSecuencia() + "  " + duplicarMotivoPrestamo.getCodigo());
            if (crearMotivosPrestamos.contains(duplicarMotivoPrestamo)) {
                log.info("Ya lo contengo.");
            }
            listMotivosPrestamos.add(duplicarMotivoPrestamo);
            crearMotivosPrestamos.add(duplicarMotivoPrestamo);
            RequestContext.getCurrentInstance().update("form:datosTipoReemplazo");
            index = -1;
            secRegistro = null;
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            if (bandera == 1) {
                //CERRAR FILTRADO
                codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoReemplazo:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoReemplazo:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosTipoReemplazo");
                bandera = 0;
                filtrarMotivosPrestamos = null;
                tipoLista = 0;
            }
            duplicarMotivoPrestamo = new MotivosPrestamos();
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroTiposReemplazos').hide()");

        } else {
            contador = 0;
            RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
            RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
        }
    }

    public void limpiarDuplicarMotivosPrestamos() {
        duplicarMotivoPrestamo = new MotivosPrestamos();
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTipoReemplazoExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "MOTIVOSPRESTAMOS", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTipoReemplazoExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "MOTIVOSPRESTAMOS", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        log.info("lol");
        if (!listMotivosPrestamos.isEmpty()) {
            if (secRegistro != null) {
                log.info("lol 2");
                int resultado = administrarRastros.obtenerTabla(secRegistro, "MOTIVOSPRESTAMOS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
        } else if (administrarRastros.verificarHistoricosTabla("MOTIVOSPRESTAMOS")) { // igual acá
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
        index = -1;
    }

    //--------///////////////////////---------------------*****//*/*/*/*/*/-****----
    public List<MotivosPrestamos> getListMotivosPrestamos() {
        if (listMotivosPrestamos == null) {
            listMotivosPrestamos = administrarMotivosPrestamos.mostrarMotivosPrestamos();
        }
        return listMotivosPrestamos;
    }

    public void setListMotivosPrestamos(List<MotivosPrestamos> listMotivosPrestamos) {
        this.listMotivosPrestamos = listMotivosPrestamos;
    }

    public List<MotivosPrestamos> getFiltrarMotivosPrestamos() {
        return filtrarMotivosPrestamos;
    }

    public void setFiltrarMotivosPrestamos(List<MotivosPrestamos> filtrarMotivosPrestamos) {
        this.filtrarMotivosPrestamos = filtrarMotivosPrestamos;
    }

    public MotivosPrestamos getNuevoMotivoPrestamo() {
        return nuevoMotivoPrestamo;
    }

    public void setNuevoMotivoPrestamo(MotivosPrestamos nuevoMotivoPrestamo) {
        this.nuevoMotivoPrestamo = nuevoMotivoPrestamo;
    }

    public MotivosPrestamos getDuplicarMotivoPrestamo() {
        return duplicarMotivoPrestamo;
    }

    public void setDuplicarMotivoPrestamo(MotivosPrestamos duplicarMotivoPrestamo) {
        this.duplicarMotivoPrestamo = duplicarMotivoPrestamo;
    }

    public MotivosPrestamos getEditarMotivoPrestamo() {
        return editarMotivoPrestamo;
    }

    public void setEditarMotivoPrestamo(MotivosPrestamos editarMotivoPrestamo) {
        this.editarMotivoPrestamo = editarMotivoPrestamo;
    }

    public boolean isGuardado() {
        return guardado;
    }

    public void setGuardado(boolean guardado) {
        this.guardado = guardado;
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

    public String getMensajeValidacion() {
        return mensajeValidacion;
    }

    public void setMensajeValidacion(String mensajeValidacion) {
        this.mensajeValidacion = mensajeValidacion;
    }

}
