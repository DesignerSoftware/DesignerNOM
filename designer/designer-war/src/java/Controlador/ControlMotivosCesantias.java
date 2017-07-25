/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.MotivosCesantias;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarMotivosCesantiasInterface;
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
public class ControlMotivosCesantias implements Serializable {

   private static Logger log = Logger.getLogger(ControlMotivosCesantias.class);

    @EJB
    AdministrarMotivosCesantiasInterface administrarMotivosCesantias;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    private List<MotivosCesantias> listMotivosCesantias;
    private List<MotivosCesantias> filtrarMotivosCesantias;
    private List<MotivosCesantias> crearMotivosCesantias;
    private List<MotivosCesantias> modificarMotivosCesantias;
    private List<MotivosCesantias> borrarMotivosCesantias;
    private MotivosCesantias nuevoMotivoCesantia;
    private MotivosCesantias duplicarMotivoCesantia;
    private MotivosCesantias editarMotivoCesantia;
    private MotivosCesantias motivoCesantiaSeleccionado;
    //otros
    private int cualCelda, tipoLista, tipoActualizacion, k, bandera;
    private BigInteger l;
    private boolean aceptar, guardado;
    //AutoCompletar
    private boolean permitirIndex;
    //RASTRO
    private Column codigo, descripcion;
    //borrado
    private int registrosBorrados;
    private String mensajeValidacion, infoRegistro, altoTabla;
    private boolean activarLov;
    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

    public ControlMotivosCesantias() {
        listMotivosCesantias = null;
        crearMotivosCesantias = new ArrayList<MotivosCesantias>();
        modificarMotivosCesantias = new ArrayList<MotivosCesantias>();
        borrarMotivosCesantias = new ArrayList<MotivosCesantias>();
        permitirIndex = true;
        editarMotivoCesantia = new MotivosCesantias();
        nuevoMotivoCesantia = new MotivosCesantias();
        duplicarMotivoCesantia = new MotivosCesantias();
        guardado = true;
        activarLov = true;
        mapParametros.put("paginaAnterior", paginaAnterior);
    }

    public void recibirPaginaEntrante(String pagina) {
        paginaAnterior = pagina;
        getListMotivosCesantias();
        if (!listMotivosCesantias.isEmpty()) {
            motivoCesantiaSeleccionado = listMotivosCesantias.get(0);
        }
    }

    public void recibirParametros(Map<String, Object> map) {
        mapParametros = map;
        paginaAnterior = (String) mapParametros.get("paginaAnterior");
        getListMotivosCesantias();
        if (!listMotivosCesantias.isEmpty()) {
            motivoCesantiaSeleccionado = listMotivosCesantias.get(0);
        }
    }

    //Reemplazar la funcion volverAtras, retornarPagina, Redirigir.....Atras.etc
    public void navegar(String pag) {
        FacesContext fc = FacesContext.getCurrentInstance();
        ControlListaNavegacion controlListaNavegacion = (ControlListaNavegacion) fc.getApplication().evaluateExpressionGet(fc, "#{controlListaNavegacion}", ControlListaNavegacion.class);
        String pagActual = "motivocesantia";
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

    public void limpiarListasValor() {

    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarMotivosCesantias.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            log.error("Error postconstruct " + this.getClass().getName() + ": " + e);
            log.error("Causa: " + e.getCause());
        }
    }

    public String volverPagAnterior() {
        return paginaAnterior;
    }

    public void cambiarIndice(MotivosCesantias motivo, int celda) {
        log.error("TIPO LISTA = " + tipoLista);

        if (permitirIndex == true) {
            motivoCesantiaSeleccionado = motivo;
            cualCelda = celda;
            motivoCesantiaSeleccionado.getSecuencia();

        }
    }

    public void asignarIndex(MotivosCesantias motivo, int LND, int dig) {
        try {
            log.info("\n ENTRE A CONTROLMOTIVOSCESANTIAS ASIGNAR INDEX \n");
            motivoCesantiaSeleccionado = motivo;
            if (LND == 0) {
                tipoActualizacion = 0;
            } else if (LND == 1) {
                tipoActualizacion = 1;
                log.info("TIPO ACTUALIZACION : " + tipoActualizacion);
            } else if (LND == 2) {
                tipoActualizacion = 2;
            }

        } catch (Exception e) {
            log.warn("Error CONTROLMOTIVOSCESANTIAS ASIGNAR INDEX ERROR = " + e);
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void listaValoresBoton() {
    }

    public void salir() {
        cancelarModificacion();
        navegar("atras");
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
            filtrarMotivosCesantias = null;
            tipoLista = 0;
        }

        borrarMotivosCesantias.clear();
        crearMotivosCesantias.clear();
        modificarMotivosCesantias.clear();
        motivoCesantiaSeleccionado = null;
        k = 0;
        listMotivosCesantias = null;
        getListMotivosCesantias();
        contarRegistros();
        guardado = true;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosTipoReemplazo");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void activarCtrlF11() {
        if (bandera == 0) {

            codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoReemplazo:codigo");
            codigo.setFilterStyle("width: 85% !important");
            descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoReemplazo:descripcion");
            descripcion.setFilterStyle("width: 85% !important");
            RequestContext.getCurrentInstance().update("form:datosTipoReemplazo");
            log.info("Activar");
            bandera = 1;
            altoTabla = "280";
        } else if (bandera == 1) {
            log.info("Desactivar");
            codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoReemplazo:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoReemplazo:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTipoReemplazo");
            bandera = 0;
            filtrarMotivosCesantias = null;
            tipoLista = 0;
            altoTabla = "300";
        }
    }

    public void modificandoMotivoCensantia(MotivosCesantias motivo, String confirmarCambio, String valorConfirmar) {
        log.error("ENTRE A MODIFICAR MOTIVOSCESANTIA");
        motivoCesantiaSeleccionado = motivo;

        int contador = 0;
        int contadorGuardar = 0;
        boolean banderita = false;
        Integer a;
        a = null;
        RequestContext context = RequestContext.getCurrentInstance();
        log.error("TIPO LISTA = " + tipoLista);
        if (confirmarCambio.equalsIgnoreCase("N")) {
            log.error("ENTRE A MODIFICAR MOTIVOEMBARGOS, CONFIRMAR CAMBIO ES N");

            if (!crearMotivosCesantias.contains(motivoCesantiaSeleccionado)) {
                if (motivoCesantiaSeleccionado.getCodigo() == a) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                }
                if (motivoCesantiaSeleccionado.getNombre() == null) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                } else if (motivoCesantiaSeleccionado.getNombre().equals("")) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                } else {
                    contadorGuardar++;
                }

                if (modificarMotivosCesantias.isEmpty()) {
                    modificarMotivosCesantias.add(motivoCesantiaSeleccionado);
                } else if (!modificarMotivosCesantias.contains(motivoCesantiaSeleccionado)) {
                    modificarMotivosCesantias.add(motivoCesantiaSeleccionado);
                }
                if (guardado == true) {
                    guardado = false;
                }
            }
        }
        RequestContext.getCurrentInstance().update("form:datosTipoReemplazo");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    private BigInteger verificarEerPrestamos;

    public void verificarBorrado() {
        try {
            log.info("ESTOY EN VERIFICAR BORRADO tipoLista " + tipoLista);
            log.info("secuencia borrado : " + motivoCesantiaSeleccionado.getSecuencia());
            if (tipoLista == 0) {
                log.info("secuencia borrado : " + motivoCesantiaSeleccionado.getSecuencia());
                verificarEerPrestamos = administrarMotivosCesantias.contarNovedadesSistemasMotivoCesantia(motivoCesantiaSeleccionado.getSecuencia());
            } else {
                log.info("secuencia borrado : " + motivoCesantiaSeleccionado.getSecuencia());
                verificarEerPrestamos = administrarMotivosCesantias.contarNovedadesSistemasMotivoCesantia(motivoCesantiaSeleccionado.getSecuencia());
            }
            if (!verificarEerPrestamos.equals(new BigInteger("0"))) {
                log.info("Borrado>0");

                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:validacionBorrar");
                RequestContext.getCurrentInstance().execute("PF('validacionBorrar').show()");
                motivoCesantiaSeleccionado = null;

                verificarEerPrestamos = new BigInteger("-1");

            } else {
                log.info("Borrado==0");
                borrandoMotivosCesantias();
            }
        } catch (Exception e) {
            log.error("ERROR ControlTiposCertificados verificarBorrado ERROR " + e);
        }
    }

    public void borrandoMotivosCesantias() {

        if (motivoCesantiaSeleccionado != null) {
            log.info("Entro a borrandoMotivosCesantias");
            if (!modificarMotivosCesantias.isEmpty() && modificarMotivosCesantias.contains(motivoCesantiaSeleccionado)) {
                int modIndex = modificarMotivosCesantias.indexOf(motivoCesantiaSeleccionado);
                modificarMotivosCesantias.remove(modIndex);
                borrarMotivosCesantias.add(motivoCesantiaSeleccionado);
            } else if (!crearMotivosCesantias.isEmpty() && crearMotivosCesantias.contains(motivoCesantiaSeleccionado)) {
                int crearIndex = crearMotivosCesantias.indexOf(motivoCesantiaSeleccionado);
                crearMotivosCesantias.remove(crearIndex);
            } else {
                borrarMotivosCesantias.add(motivoCesantiaSeleccionado);
            }
            listMotivosCesantias.remove(motivoCesantiaSeleccionado);
            if (tipoLista == 1) {
                filtrarMotivosCesantias.remove(motivoCesantiaSeleccionado);

            }
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosTipoReemplazo");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            motivoCesantiaSeleccionado = null;
            contarRegistros();

            if (guardado == true) {
                guardado = false;
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }

    }

    public void revisarDialogoGuardar() {

        if (!borrarMotivosCesantias.isEmpty() || !crearMotivosCesantias.isEmpty() || !modificarMotivosCesantias.isEmpty()) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:confirmarGuardar");
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
        }

    }

    public void guardarMotivoCesantia() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (guardado == false) {
            log.info("REALIZANDO MOTIVOCESANTIA");
            if (!borrarMotivosCesantias.isEmpty()) {
                administrarMotivosCesantias.borrarMotivosCesantias(borrarMotivosCesantias);
                //mostrarBorrados
                registrosBorrados = borrarMotivosCesantias.size();
                RequestContext.getCurrentInstance().update("form:mostrarBorrados");
                RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
                borrarMotivosCesantias.clear();
            }
            if (!crearMotivosCesantias.isEmpty()) {
                administrarMotivosCesantias.crearMotivosCesantias(crearMotivosCesantias);

                crearMotivosCesantias.clear();
            }
            if (!modificarMotivosCesantias.isEmpty()) {
                administrarMotivosCesantias.modificarMotivosCesantias(modificarMotivosCesantias);
                modificarMotivosCesantias.clear();
            }
            log.info("Se guardaron los datos con exito");
            listMotivosCesantias = null;
            guardado = true;
            RequestContext.getCurrentInstance().update("form:datosTipoReemplazo");
            k = 0;
        }
        motivoCesantiaSeleccionado = null;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");

    }

    public void editarCelda() {
        if (motivoCesantiaSeleccionado != null) {
            if (tipoLista == 0) {
                editarMotivoCesantia = motivoCesantiaSeleccionado;
            }
            if (tipoLista == 1) {
                editarMotivoCesantia = motivoCesantiaSeleccionado;
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
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void agregarNuevoMotivosCesantias() {
        log.info("agregarNuevoMotivosCesantias");
        int contador = 0;
        int duplicados = 0;

        Integer a = 0;
        a = null;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        if (nuevoMotivoCesantia.getCodigo() == a) {
            mensajeValidacion = " *Debe Tener Un Codigo \n";
            log.info("Mensaje validacion : " + mensajeValidacion);
        } else {
            log.info("codigo en Motivo Cambio Cargo: " + nuevoMotivoCesantia.getCodigo());

            for (int x = 0; x < listMotivosCesantias.size(); x++) {
                if (listMotivosCesantias.get(x).getCodigo() == nuevoMotivoCesantia.getCodigo()) {
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
        if (nuevoMotivoCesantia.getNombre() == (null)) {
            mensajeValidacion = mensajeValidacion + " *Debe tener una descripción \n";
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
                filtrarMotivosCesantias = null;
                tipoLista = 0;
            }
            log.info("Despues de la bandera");

            k++;
            l = BigInteger.valueOf(k);
            nuevoMotivoCesantia.setSecuencia(l);
            crearMotivosCesantias.add(nuevoMotivoCesantia);
            listMotivosCesantias.add(nuevoMotivoCesantia);
            motivoCesantiaSeleccionado = nuevoMotivoCesantia;
            contarRegistros();
            nuevoMotivoCesantia = new MotivosCesantias();
            RequestContext.getCurrentInstance().update("form:datosTipoReemplazo");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }

            RequestContext.getCurrentInstance().execute("PF('nuevoRegistroTiposReemplazos').hide()");

        } else {
            RequestContext.getCurrentInstance().update("form:validacionNuevoMotivo");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevoMotivo').show()");
            contador = 0;
        }
    }

    public void limpiarNuevoMotivosCesantias() {
        nuevoMotivoCesantia = new MotivosCesantias();

    }

    public void duplicandoMotivosCesantias() {
        log.info("duplicandoMotivosCesantias");
        if (motivoCesantiaSeleccionado != null) {
            duplicarMotivoCesantia = new MotivosCesantias();
            k++;
            l = BigInteger.valueOf(k);

            if (tipoLista == 0) {
                duplicarMotivoCesantia.setSecuencia(l);
                duplicarMotivoCesantia.setCodigo(motivoCesantiaSeleccionado.getCodigo());
                duplicarMotivoCesantia.setNombre(motivoCesantiaSeleccionado.getNombre());
            }
            if (tipoLista == 1) {
                duplicarMotivoCesantia.setSecuencia(l);
                duplicarMotivoCesantia.setCodigo(motivoCesantiaSeleccionado.getCodigo());
                duplicarMotivoCesantia.setNombre(motivoCesantiaSeleccionado.getNombre());
            }

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTTR");
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroTiposReemplazos').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void confirmarDuplicar() {
        log.error("ESTOY EN CONFIRMAR DUPLICAR MOTIVOSCESANTIAS");
        int contador = 0;
        mensajeValidacion = " ";
        int duplicados = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        Integer a = 0;
        a = null;

        if (duplicarMotivoCesantia.getCodigo() == a) {
            mensajeValidacion = mensajeValidacion + "   * Codigo \n";
            log.info("Mensaje validacion : " + mensajeValidacion);
        } else {
            for (int x = 0; x < listMotivosCesantias.size(); x++) {
                if (listMotivosCesantias.get(x).getCodigo() == duplicarMotivoCesantia.getCodigo()) {
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
        if (duplicarMotivoCesantia.getNombre() == null) {
            mensajeValidacion = mensajeValidacion + "   * Una descripción \n";
            log.info("Mensaje validacion : " + mensajeValidacion);

        } else {
            log.info("Bandera : ");
            contador++;
        }

        if (contador == 2) {

            log.info("Datos Duplicando: " + duplicarMotivoCesantia.getSecuencia() + "  " + duplicarMotivoCesantia.getCodigo());
            if (crearMotivosCesantias.contains(duplicarMotivoCesantia)) {
                log.info("Ya lo contengo.");
            }
            listMotivosCesantias.add(duplicarMotivoCesantia);
            crearMotivosCesantias.add(duplicarMotivoCesantia);
            motivoCesantiaSeleccionado = duplicarMotivoCesantia;
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:datosTipoReemplazo");
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
                filtrarMotivosCesantias = null;
                tipoLista = 0;

            }
            duplicarMotivoCesantia = new MotivosCesantias();
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroTiposReemplazos').hide()");

        } else {
            contador = 0;
            RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
            RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
        }
    }

    public void limpiarDuplicarMotivosCesantias() {
        duplicarMotivoCesantia = new MotivosCesantias();
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTipoReemplazoExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "MOTIVOSCENSANTIAS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTipoReemplazoExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "MOTIVOSCENSANTIAS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        log.info("lol");
        if (motivoCesantiaSeleccionado != null) {
            log.info("lol 2");
            int resultado = administrarRastros.obtenerTabla(motivoCesantiaSeleccionado.getSecuencia(), "MOTIVOSCENSANTIAS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
        } else if (administrarRastros.verificarHistoricosTabla("MOTIVOSCENSANTIAS")) { // igual acá
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
    }

    public void eventoFiltrar() {
        try {
            log.info("\n ENTRE A CONTROLMOTIVOSCESANTIAS EVENTOFILTRAR \n");
            if (tipoLista == 0) {
                tipoLista = 1;
            }
            contarRegistros();
        } catch (Exception e) {
            log.error("ERROR CONTROLMOTIVOSCESANTIAS EVENTOFILTRAR  ERROR =" + e.getMessage());
        }
    }

    public void contarRegistros() {
        RequestContext.getCurrentInstance().update("form:infoRegistro");
    }

    //--------///////////////////////---------------------*****//*/*/*/*/*/-****----
    public List<MotivosCesantias> getListMotivosCesantias() {
        if (listMotivosCesantias == null) {
            listMotivosCesantias = administrarMotivosCesantias.consultarMotivosCesantias();
        }
        return listMotivosCesantias;
    }

    public void setListMotivosCesantias(List<MotivosCesantias> listMotivosCesantias) {
        this.listMotivosCesantias = listMotivosCesantias;
    }

    public List<MotivosCesantias> getFiltrarMotivosCesantias() {
        return filtrarMotivosCesantias;
    }

    public void setFiltrarMotivosCesantias(List<MotivosCesantias> filtrarMotivosCesantias) {
        this.filtrarMotivosCesantias = filtrarMotivosCesantias;
    }

    public MotivosCesantias getNuevoMotivoCesantia() {
        return nuevoMotivoCesantia;
    }

    public void setNuevoMotivoCesantia(MotivosCesantias nuevoMotivoCesantia) {
        this.nuevoMotivoCesantia = nuevoMotivoCesantia;
    }

    public MotivosCesantias getDuplicarMotivoCesantia() {
        return duplicarMotivoCesantia;
    }

    public void setDuplicarMotivoCesantia(MotivosCesantias duplicarMotivoCesantia) {
        this.duplicarMotivoCesantia = duplicarMotivoCesantia;
    }

    public MotivosCesantias getEditarMotivoCesantia() {
        return editarMotivoCesantia;
    }

    public void setEditarMotivoCesantia(MotivosCesantias editarMotivoCesantia) {
        this.editarMotivoCesantia = editarMotivoCesantia;
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

    public boolean isGuardado() {
        return guardado;
    }

    public void setGuardado(boolean guardado) {
        this.guardado = guardado;
    }

    public String getPaginaAnterior() {
        return paginaAnterior;
    }

    public void setPaginaAnterior(String paginaAnterior) {
        this.paginaAnterior = paginaAnterior;
    }

    public MotivosCesantias getMotivoCesantiaSeleccionado() {
        return motivoCesantiaSeleccionado;
    }

    public void setMotivoCesantiaSeleccionado(MotivosCesantias motivoCesantiaSeleccionado) {
        this.motivoCesantiaSeleccionado = motivoCesantiaSeleccionado;
    }

    public String getInfoRegistro() {

        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosTipoReemplazo");
        infoRegistro = String.valueOf(tabla.getRowCount());
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

    public boolean isActivarLov() {
        return activarLov;
    }

    public void setActivarLov(boolean activarLov) {
        this.activarLov = activarLov;
    }

    public String getAltoTabla() {
        return altoTabla;
    }

    public void setAltoTabla(String altoTabla) {
        this.altoTabla = altoTabla;
    }

}
