/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.MotivosRetiros;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarMotivosRetirosInterface;
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
public class ControlMotivosRetiros implements Serializable {

    @EJB
    AdministrarMotivosRetirosInterface administrarMotivosRetiros;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    private List<MotivosRetiros> listMotivosRetiros;
    private List<MotivosRetiros> filtrarMotivosRetiros;
    private List<MotivosRetiros> crearMotivosRetiros;
    private List<MotivosRetiros> modificarMotivosRetiros;
    private List<MotivosRetiros> borrarMotivosRetiros;
    private MotivosRetiros nuevoMotivosRetiros;
    private MotivosRetiros duplicarMotivosRetiros;
    private MotivosRetiros editarMotivosRetiros;
    private MotivosRetiros motivoRetiroSeleccionado;
    //otros
    private int cualCelda, tipoLista, tipoActualizacion, k, bandera;
    private BigInteger l;
    private boolean aceptar, guardado;
    //AutoCompletar
    private boolean permitirIndex, activarLov;
    //RASTRO
    private Column codigo, descripcion;
    //borrado
    private int registrosBorrados;
    private String mensajeValidacion;
    //filtrado table
    private int tamano;
    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

    private String infoRegistro;

    public ControlMotivosRetiros() {
        listMotivosRetiros = null;
        crearMotivosRetiros = new ArrayList<MotivosRetiros>();
        modificarMotivosRetiros = new ArrayList<MotivosRetiros>();
        borrarMotivosRetiros = new ArrayList<MotivosRetiros>();
        permitirIndex = true;
        editarMotivosRetiros = new MotivosRetiros();
        nuevoMotivosRetiros = new MotivosRetiros();
        duplicarMotivosRetiros = new MotivosRetiros();
        guardado = true;
        tamano = 270;
        activarLov = true;
        mapParametros.put("paginaAnterior", paginaAnterior);
    }

    public void recibirPaginaEntrante(String pagina) {
        paginaAnterior = pagina;
        getListMotivosRetiros();
        if (listMotivosRetiros != null) {
            if (!listMotivosRetiros.isEmpty()) {
                motivoRetiroSeleccionado = listMotivosRetiros.get(0);
            }
        }
    }

    public void recibirParametros(Map<String, Object> map) {
        mapParametros = map;
        paginaAnterior = (String) mapParametros.get("paginaAnterior");
        getListMotivosRetiros();
        if (listMotivosRetiros != null) {
            if (!listMotivosRetiros.isEmpty()) {
                motivoRetiroSeleccionado = listMotivosRetiros.get(0);
            }
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
        } else {
            String pagActual = "motivoretiro";
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
            administrarMotivosRetiros.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public String retornarPagina() {
        return paginaAnterior;
    }

    public void cambiarIndice(MotivosRetiros motivo, int celda) {
        if (permitirIndex == true) {
            motivoRetiroSeleccionado = motivo;
            cualCelda = celda;
            if (cualCelda == 0) {
                motivoRetiroSeleccionado.getCodigo();
            } else if (cualCelda == 1) {
                motivoRetiroSeleccionado.getNombre();
            }
        }
        motivoRetiroSeleccionado.getSecuencia();
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void cancelarModificacion() {
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            //CERRAR FILTRADO
            codigo = (Column) c.getViewRoot().findComponent("form:datosMotivosRetiros:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosMotivosRetiros:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosMotivosRetiros");
            bandera = 0;
            filtrarMotivosRetiros = null;
            tipoLista = 0;
        }

        borrarMotivosRetiros.clear();
        crearMotivosRetiros.clear();
        modificarMotivosRetiros.clear();
        motivoRetiroSeleccionado = null;
        k = 0;
        listMotivosRetiros = null;
        guardado = true;
        permitirIndex = true;
        getListMotivosRetiros();
        RequestContext context = RequestContext.getCurrentInstance();
        contarRegistros();
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
        RequestContext.getCurrentInstance().update("form:datosMotivosRetiros");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void salir() {
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            //CERRAR FILTRADO
            codigo = (Column) c.getViewRoot().findComponent("form:datosMotivosRetiros:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosMotivosRetiros:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosMotivosRetiros");
            bandera = 0;
            filtrarMotivosRetiros = null;
            tipoLista = 0;
        }

        borrarMotivosRetiros.clear();
        crearMotivosRetiros.clear();
        modificarMotivosRetiros.clear();
        motivoRetiroSeleccionado = null;
        k = 0;
        listMotivosRetiros = null;
        guardado = true;
        permitirIndex = true;
        getListMotivosRetiros();
        RequestContext context = RequestContext.getCurrentInstance();
        contarRegistros();
        RequestContext.getCurrentInstance().update("form:datosMotivosRetiros");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        navegar("atras");
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();

        if (bandera == 0) {
            tamano = 250;
            codigo = (Column) c.getViewRoot().findComponent("form:datosMotivosRetiros:codigo");
            codigo.setFilterStyle("width: 85% !important;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosMotivosRetiros:descripcion");
            descripcion.setFilterStyle("width: 85% !important;");
            RequestContext.getCurrentInstance().update("form:datosMotivosRetiros");
            System.out.println("Activar");
            bandera = 1;
        } else if (bandera == 1) {
            System.out.println("Desactivar");
            tamano = 270;
            codigo = (Column) c.getViewRoot().findComponent("form:datosMotivosRetiros:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosMotivosRetiros:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosMotivosRetiros");
            bandera = 0;
            filtrarMotivosRetiros = null;
            tipoLista = 0;
        }
    }

    public void modificarMotivosRetiros(MotivosRetiros motivo, String confirmarCambio, String valorConfirmar) {
        motivoRetiroSeleccionado = motivo;

        int contador = 0;
        boolean banderita = false;
        Integer a;
        a = null;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("N")) {
            if (tipoLista == 0) {
                if (!crearMotivosRetiros.contains(motivoRetiroSeleccionado)) {
                    if (motivoRetiroSeleccionado.getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        motivoRetiroSeleccionado.setCodigo(motivoRetiroSeleccionado.getCodigo());
                    } else {
                        for (int j = 0; j < listMotivosRetiros.size(); j++) {
                            if (motivoRetiroSeleccionado.getCodigo().equals(listMotivosRetiros.get(j).getCodigo())) {
                                contador++;
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            motivoRetiroSeleccionado.setCodigo(motivoRetiroSeleccionado.getCodigo());
                            banderita = false;
                        } else {
                            banderita = true;
                        }

                    }
                    if (motivoRetiroSeleccionado.getNombre() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        motivoRetiroSeleccionado.setNombre(motivoRetiroSeleccionado.getNombre());
                        banderita = false;
                    }
                    if (motivoRetiroSeleccionado.getNombre().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        motivoRetiroSeleccionado.setNombre(motivoRetiroSeleccionado.getNombre());
                    }

                    if (banderita == true) {
                        if (modificarMotivosRetiros.isEmpty()) {
                            modificarMotivosRetiros.add(motivoRetiroSeleccionado);
                        } else if (!modificarMotivosRetiros.contains(motivoRetiroSeleccionado)) {
                            modificarMotivosRetiros.add(motivoRetiroSeleccionado);
                        }
                        if (guardado == true) {
                            guardado = false;
                        }

                    } else {
                        RequestContext.getCurrentInstance().update("form:validacionModificar");
                        RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");
                    }
                } else {
                    if (motivoRetiroSeleccionado.getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        motivoRetiroSeleccionado.setCodigo(motivoRetiroSeleccionado.getCodigo());
                    } else {
                        for (int j = 0; j < listMotivosRetiros.size(); j++) {
                            if (motivoRetiroSeleccionado.getCodigo().equals(listMotivosRetiros.get(j).getCodigo())) {
                                contador++;
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            motivoRetiroSeleccionado.setCodigo(motivoRetiroSeleccionado.getCodigo());
                            banderita = false;
                        } else {
                            banderita = true;
                        }

                    }
                    if (motivoRetiroSeleccionado.getNombre() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        motivoRetiroSeleccionado.setNombre(motivoRetiroSeleccionado.getNombre());
                        banderita = false;
                    }
                    if (motivoRetiroSeleccionado.getNombre().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        motivoRetiroSeleccionado.setNombre(motivoRetiroSeleccionado.getNombre());
                    }

                    if (banderita == true) {
                        if (modificarMotivosRetiros.isEmpty()) {
                            modificarMotivosRetiros.add(motivoRetiroSeleccionado);
                        } else if (!modificarMotivosRetiros.contains(motivoRetiroSeleccionado)) {
                            modificarMotivosRetiros.add(motivoRetiroSeleccionado);
                        }
                        if (guardado == true) {
                            guardado = false;
                        }

                    } else {
                        RequestContext.getCurrentInstance().update("form:validacionModificar");
                        RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");
                    }
                }
            } else if (!crearMotivosRetiros.contains(motivoRetiroSeleccionado)) {
                if (motivoRetiroSeleccionado.getCodigo() == a) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    motivoRetiroSeleccionado.setCodigo(motivoRetiroSeleccionado.getCodigo());
                    banderita = false;
                } else {
                    for (int j = 0; j < filtrarMotivosRetiros.size(); j++) {
                        if (motivoRetiroSeleccionado.getCodigo().equals(filtrarMotivosRetiros.get(j).getCodigo())) {
                            contador++;
                        }
                    }

                    if (contador > 0) {
                        mensajeValidacion = "CODIGOS REPETIDOS";
                        motivoRetiroSeleccionado.setCodigo(motivoRetiroSeleccionado.getCodigo());
                        banderita = false;
                    } else {
                        banderita = true;
                    }

                }

                if (motivoRetiroSeleccionado.getNombre() == null) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                    motivoRetiroSeleccionado.setNombre(motivoRetiroSeleccionado.getNombre());
                }
                if (motivoRetiroSeleccionado.getNombre().isEmpty()) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                    motivoRetiroSeleccionado.setNombre(motivoRetiroSeleccionado.getNombre());
                }

                if (banderita == true) {
                    if (modificarMotivosRetiros.isEmpty()) {
                        modificarMotivosRetiros.add(motivoRetiroSeleccionado);
                    } else if (!modificarMotivosRetiros.contains(motivoRetiroSeleccionado)) {
                        modificarMotivosRetiros.add(motivoRetiroSeleccionado);
                    }
                    if (guardado == true) {
                        guardado = false;
                    }

                } else {
                    RequestContext.getCurrentInstance().update("form:validacionModificar");
                    RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");
                }
            } else {
                if (motivoRetiroSeleccionado.getCodigo() == a) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    motivoRetiroSeleccionado.setCodigo(motivoRetiroSeleccionado.getCodigo());
                    banderita = false;
                } else {
                    for (int j = 0; j < filtrarMotivosRetiros.size(); j++) {
                        if (motivoRetiroSeleccionado.getCodigo().equals(filtrarMotivosRetiros.get(j).getCodigo())) {
                            contador++;
                        }
                    }
                    if (contador > 0) {
                        mensajeValidacion = "CODIGOS REPETIDOS";
                        motivoRetiroSeleccionado.setCodigo(motivoRetiroSeleccionado.getCodigo());
                        banderita = false;
                    } else {
                        banderita = true;
                    }

                }
                if (motivoRetiroSeleccionado.getNombre() == null) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                    motivoRetiroSeleccionado.setNombre(motivoRetiroSeleccionado.getNombre());
                }

                if (motivoRetiroSeleccionado.getNombre().isEmpty()) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                    motivoRetiroSeleccionado.setNombre(motivoRetiroSeleccionado.getNombre());
                }

                if (banderita == true) {

                    if (guardado == true) {
                        guardado = false;
                    }

                } else {
                    RequestContext.getCurrentInstance().update("form:validacionModificar");
                    RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");
                }
            }
            RequestContext.getCurrentInstance().update("form:datosMotivosRetiros");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
    }

    public void borrandoMotivosRetiros() {

        if (motivoRetiroSeleccionado != null) {
            System.out.println("Entro a borrandoMotivosRetiros");
            if (!modificarMotivosRetiros.isEmpty() && modificarMotivosRetiros.contains(motivoRetiroSeleccionado)) {
                int modIndex = modificarMotivosRetiros.indexOf(motivoRetiroSeleccionado);
                modificarMotivosRetiros.remove(modIndex);
                borrarMotivosRetiros.add(motivoRetiroSeleccionado);
            } else if (!crearMotivosRetiros.isEmpty() && crearMotivosRetiros.contains(motivoRetiroSeleccionado)) {
                int crearIndex = crearMotivosRetiros.indexOf(motivoRetiroSeleccionado);
                crearMotivosRetiros.remove(crearIndex);
            } else {
                borrarMotivosRetiros.add(motivoRetiroSeleccionado);
            }
            listMotivosRetiros.remove(motivoRetiroSeleccionado);
            if (tipoLista == 1) {
                filtrarMotivosRetiros.remove(motivoRetiroSeleccionado);

            }
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosMotivosRetiros");
            contarRegistros();
            motivoRetiroSeleccionado = null;

            if (guardado == true) {
                guardado = false;
            }
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }

    }

    public void revisarDialogoGuardar() {

        if (!borrarMotivosRetiros.isEmpty() || !crearMotivosRetiros.isEmpty() || !modificarMotivosRetiros.isEmpty()) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:confirmarGuardar");
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
        }

    }

    public void guardarMotivosRetiros() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (guardado == false) {
            System.out.println("Realizando guardarMotivosRetiros");
            if (!borrarMotivosRetiros.isEmpty()) {
                administrarMotivosRetiros.borrarMotivosRetiros(borrarMotivosRetiros);
                //mostrarBorrados
                registrosBorrados = borrarMotivosRetiros.size();
                RequestContext.getCurrentInstance().update("form:mostrarBorrados");
                RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
                borrarMotivosRetiros.clear();
            }
            if (!modificarMotivosRetiros.isEmpty()) {
                administrarMotivosRetiros.modificarMotivosRetiros(modificarMotivosRetiros);
                modificarMotivosRetiros.clear();
            }
            if (!crearMotivosRetiros.isEmpty()) {
                administrarMotivosRetiros.crearMotivosRetiros(crearMotivosRetiros);
                crearMotivosRetiros.clear();
            }
            System.out.println("Se guardaron los datos con exito");
            listMotivosRetiros = null;
            RequestContext.getCurrentInstance().update("form:datosMotivosRetiros");
            k = 0;
            guardado = true;
            FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }
        motivoRetiroSeleccionado = null;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");

    }

    public void editarCelda() {
        if (motivoRetiroSeleccionado != null) {
            if (tipoLista == 0) {
                editarMotivosRetiros = motivoRetiroSeleccionado;
            }
            if (tipoLista == 1) {
                editarMotivosRetiros = motivoRetiroSeleccionado;
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
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void agregarNuevoMotivosRetiros() {
        System.out.println("agregarNuevoMotivosRetiros");
        int contador = 0;
        int duplicados = 0;

        Integer a = 0;
        a = null;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        if (nuevoMotivosRetiros.getCodigo() == a) {
            mensajeValidacion = " *Codigo \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else {
            System.out.println("codigo en Motivo Cambio Cargo: " + nuevoMotivosRetiros.getCodigo());

            for (int x = 0; x < listMotivosRetiros.size(); x++) {
                if (listMotivosRetiros.get(x).getCodigo().equals(nuevoMotivosRetiros.getCodigo())) {
                    duplicados++;
                }
            }
            System.out.println("Antes del if Duplicados eses igual  : " + duplicados);

            if (duplicados > 0) {
                mensajeValidacion = " *Que NO Hayan Codigos Repetidos \n";
                System.out.println("Mensaje validacion : " + mensajeValidacion);
            } else {
                System.out.println("bandera");
                contador++;
            }
        }
        if (nuevoMotivosRetiros.getNombre() == null) {
            mensajeValidacion = mensajeValidacion + " *Descripcion \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("bandera");
            contador++;

        }

        System.out.println("contador " + contador);

        if (contador == 2) {
            if (bandera == 1) {
                //CERRAR FILTRADO
                FacesContext c = FacesContext.getCurrentInstance();
                System.out.println("Desactivar");
                codigo = (Column) c.getViewRoot().findComponent("form:datosMotivosRetiros:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosMotivosRetiros:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosMotivosRetiros");
                bandera = 0;
                filtrarMotivosRetiros = null;
                tipoLista = 0;
            }
            System.out.println("Despues de la bandera");

            k++;
            l = BigInteger.valueOf(k);
            nuevoMotivosRetiros.setSecuencia(l);
            crearMotivosRetiros.add(nuevoMotivosRetiros);
            listMotivosRetiros.add(nuevoMotivosRetiros);
            motivoRetiroSeleccionado = nuevoMotivosRetiros;
            nuevoMotivosRetiros = new MotivosRetiros();
            RequestContext.getCurrentInstance().update("form:datosMotivosRetiros");
            contarRegistros();
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            RequestContext.getCurrentInstance().execute("PF('nuevoRegistroMotivosRetiros').hide()");
        } else {
            RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevaCentroCosto').show()");
            contador = 0;
        }
    }

    public void limpiarNuevoMotivosRetiros() {
        nuevoMotivosRetiros = new MotivosRetiros();

    }

    //------------------------------------------------------------------------------
    public void duplicandoMotivosRetiros() {
        System.out.println("duplicandoMotivosRetiros");
        if (motivoRetiroSeleccionado != null) {
            duplicarMotivosRetiros = new MotivosRetiros();
            k++;
            l = BigInteger.valueOf(k);

            if (tipoLista == 0) {
                duplicarMotivosRetiros.setSecuencia(l);
                duplicarMotivosRetiros.setCodigo(motivoRetiroSeleccionado.getCodigo());
                duplicarMotivosRetiros.setNombre(motivoRetiroSeleccionado.getNombre());
            }
            if (tipoLista == 1) {
                duplicarMotivosRetiros.setSecuencia(l);
                duplicarMotivosRetiros.setCodigo(motivoRetiroSeleccionado.getCodigo());
                duplicarMotivosRetiros.setNombre(motivoRetiroSeleccionado.getNombre());
            }

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTE");
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroMotivosRetiros').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void confirmarDuplicar() {
        int contador = 0;
        mensajeValidacion = " ";
        int duplicados = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        Integer a = 0;
        a = null;
        if (duplicarMotivosRetiros.getCodigo() == a) {
            mensajeValidacion = mensajeValidacion + "   *Codigo \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else {
            for (int x = 0; x < listMotivosRetiros.size(); x++) {
                if (listMotivosRetiros.get(x).getCodigo().equals(duplicarMotivosRetiros.getCodigo())) {
                    duplicados++;
                }
            }
            if (duplicados > 0) {
                mensajeValidacion = " *Que NO Existan Codigo Repetidos \n";
                System.out.println("Mensaje validacion : " + mensajeValidacion);
            } else {
                System.out.println("bandera");
                contador++;
                duplicados = 0;
            }
        }
        if (duplicarMotivosRetiros.getNombre() == null) {
            mensajeValidacion = mensajeValidacion + "   *Descripcion \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("Bandera : ");
            contador++;
        }

        if (contador == 2) {

            System.out.println("Datos Duplicando: " + duplicarMotivosRetiros.getSecuencia() + "  " + duplicarMotivosRetiros.getCodigo());
            if (crearMotivosRetiros.contains(duplicarMotivosRetiros)) {
                System.out.println("Ya lo contengo.");
            }
            listMotivosRetiros.add(duplicarMotivosRetiros);
            crearMotivosRetiros.add(duplicarMotivosRetiros);
            motivoRetiroSeleccionado = duplicarMotivosRetiros;
            RequestContext.getCurrentInstance().update("form:datosMotivosRetiros");
            if (guardado == true) {
                guardado = false;
            }
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            contarRegistros();
            if (bandera == 1) {
                //CERRAR FILTRADO
                FacesContext c = FacesContext.getCurrentInstance();
                codigo = (Column) c.getViewRoot().findComponent("form:datosMotivosRetiros:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosMotivosRetiros:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosMotivosRetiros");
                bandera = 0;
                filtrarMotivosRetiros = null;
                tipoLista = 0;
            }
            duplicarMotivosRetiros = new MotivosRetiros();
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroMotivosRetiros').hide()");

        } else {
            contador = 0;
            RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
            RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
        }
    }

    public void limpiarDuplicarMotivosRetiros() {
        duplicarMotivosRetiros = new MotivosRetiros();
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosMotivosRetirosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "MOTIVOSRETIROS", false, false, "UTF-8", null, null);
        context.responseComplete();
        motivoRetiroSeleccionado = null;
        motivoRetiroSeleccionado = null;
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosMotivosRetirosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "MOTIVOSRETIROS", false, false, "UTF-8", null, null);
        context.responseComplete();
        motivoRetiroSeleccionado = null;
        motivoRetiroSeleccionado = null;
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        System.out.println("lol");
        if (motivoRetiroSeleccionado != null) {
            System.out.println("lol 2");
            int resultado = administrarRastros.obtenerTabla(motivoRetiroSeleccionado.getSecuencia(), "MOTIVOSRETIROS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
        } else if (administrarRastros.verificarHistoricosTabla("MOTIVOSRETIROS")) { // igual acá
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
        motivoRetiroSeleccionado = null;
    }

    public void eventoFiltrar() {
        try {
            if (tipoLista == 0) {
                tipoLista = 1;
            }
            contarRegistros();
        } catch (Exception e) {
            System.out.println("ERROR ControlMotivosRetiros eventoFiltrar ERROR===" + e.getMessage());
        }
    }

    public void contarRegistros() {
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
    }

    //*/*/*/*/*/*/*/*/*/*-/-*//-*/-*/*/*-*/-*/-*/*/*/*/*/---/*/*/*/*/-*/-*/-*/-*/-*/
    public List<MotivosRetiros> getListMotivosRetiros() {
        if (listMotivosRetiros == null) {
            listMotivosRetiros = administrarMotivosRetiros.consultarMotivosRetiros();
        }
        return listMotivosRetiros;
    }

    public void setListMotivosRetiros(List<MotivosRetiros> listMotivosRetiros) {
        this.listMotivosRetiros = listMotivosRetiros;
    }

    public List<MotivosRetiros> getFiltrarMotivosRetiros() {
        return filtrarMotivosRetiros;
    }

    public void setFiltrarMotivosRetiros(List<MotivosRetiros> filtrarMotivosRetiros) {
        this.filtrarMotivosRetiros = filtrarMotivosRetiros;
    }

    public MotivosRetiros getNuevoMotivosRetiros() {
        return nuevoMotivosRetiros;
    }

    public void setNuevoMotivosRetiros(MotivosRetiros nuevoMotivosRetiros) {
        this.nuevoMotivosRetiros = nuevoMotivosRetiros;
    }

    public MotivosRetiros getDuplicarMotivosRetiros() {
        return duplicarMotivosRetiros;
    }

    public void setDuplicarMotivosRetiros(MotivosRetiros duplicarMotivosRetiros) {
        this.duplicarMotivosRetiros = duplicarMotivosRetiros;
    }

    public MotivosRetiros getEditarMotivosRetiros() {
        return editarMotivosRetiros;
    }

    public void setEditarMotivosRetiros(MotivosRetiros editarMotivosRetiros) {
        this.editarMotivosRetiros = editarMotivosRetiros;
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

    public int getTamano() {
        return tamano;
    }

    public void setTamano(int tamano) {
        this.tamano = tamano;
    }

    public MotivosRetiros getMotivoRetiroSelecciodo() {
        return motivoRetiroSeleccionado;
    }

    public void setMotivoRetiroSelecciodo(MotivosRetiros motivoRetiroSeleccionado) {
        this.motivoRetiroSeleccionado = motivoRetiroSeleccionado;
    }

    public String getInfoRegistro() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosMotivosRetiros");
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

}
