/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import InterfaceAdministrar.AdministrarMotivosContratosInterface;
import Entidades.MotivosContratos;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
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
public class ControlMotivosContratos implements Serializable {

    @EJB
    AdministrarMotivosContratosInterface administrarMotivosContratos;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    private List<MotivosContratos> listMotivosContratos;
    private List<MotivosContratos> filtrarMotivosContratos;
    private List<MotivosContratos> crearMotivoContratos;
    private List<MotivosContratos> modificarMotivoContrato;
    private List<MotivosContratos> borrarMotivoContrato;
    private MotivosContratos nuevoMotivoContrato;
    private MotivosContratos duplicarMotivoContrato;
    private MotivosContratos editarMotivoContrato;
    private MotivosContratos motivoContratoSeleccionado;
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
    private BigInteger borradoVC;
    private int tamano;

    private Integer backUpCodigo;
    private String backUpDescripcion;
    private String infoRegistro;
    public String paginaAnterior;

    public ControlMotivosContratos() {

        listMotivosContratos = null;
        crearMotivoContratos = new ArrayList<MotivosContratos>();
        modificarMotivoContrato = new ArrayList<MotivosContratos>();
        borrarMotivoContrato = new ArrayList<MotivosContratos>();
        permitirIndex = true;
        editarMotivoContrato = new MotivosContratos();
        nuevoMotivoContrato = new MotivosContratos();
        duplicarMotivoContrato = new MotivosContratos();
        guardado = true;
        tamano = 270;
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarMotivosContratos.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void eventoFiltrar() {
        try {
            System.out.println("\n ENTRE A ControlMotiviosCambiosCargos.eventoFiltrar \n");
            if (tipoLista == 0) {
                tipoLista = 1;
            }
            RequestContext context = RequestContext.getCurrentInstance();
            //infoRegistro = "Cantidad de registros: " + filtrarMotivosContratos.size();
            modificarInfoRegistro(filtrarMotivosContratos.size());
            context.update("form:informacionRegistro");
        } catch (Exception e) {
            System.out.println("ERROR ControlMotiviosCambiosCargos eventoFiltrar ERROR===" + e.getMessage());
        }
    }

    public void recibirPaginaEntrante(String pagina) {
        paginaAnterior = pagina;
    }

    public String redirigir() {
        return paginaAnterior;
    }

    public void cambiarIndice(int indice, int celda) {
        System.err.println("TIPO LISTA = " + tipoLista);

        if (permitirIndex == true) {
            index = indice;
            cualCelda = celda;
            if (tipoLista == 0) {
                if (cualCelda == 0) {
                    backUpCodigo = listMotivosContratos.get(index).getCodigo();
                    System.out.println(" backUpCodigo : " + backUpCodigo);
                } else if (cualCelda == 1) {
                    backUpDescripcion = listMotivosContratos.get(index).getNombre();
                    System.out.println(" backUpDescripcion : " + backUpDescripcion);
                }
                secRegistro = listMotivosContratos.get(index).getSecuencia();
            } else {
                if (cualCelda == 0) {
                    backUpCodigo = filtrarMotivosContratos.get(index).getCodigo();
                    System.out.println(" backUpCodigo : " + backUpCodigo);

                } else if (cualCelda == 1) {
                    backUpDescripcion = filtrarMotivosContratos.get(index).getNombre();
                    System.out.println(" backUpDescripcion : " + backUpDescripcion);

                }
                secRegistro = filtrarMotivosContratos.get(index).getSecuencia();
            }

        }
        System.out.println("Indice: " + index + " Celda: " + cualCelda);
    }

    public void asignarIndex(Integer indice, int LND, int dig) {
        try {
            System.out.println("\n ENTRE A ControlMotiviosCambiosCargos.asignarIndex \n");
            index = indice;
            RequestContext context = RequestContext.getCurrentInstance();
            if (LND == 0) {
                tipoActualizacion = 0;
            } else if (LND == 1) {
                tipoActualizacion = 1;
                System.out.println("Tipo Actualizacion: " + tipoActualizacion);
            } else if (LND == 2) {
                tipoActualizacion = 2;
            }

        } catch (Exception e) {
            System.out.println("ERROR ControlMotiviosCambiosCargos.asignarIndex ERROR======" + e.getMessage());
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void listaValoresBoton() {
    }

    public void cancelarModificacion() {
        System.out.println("ControlMotivosContratos cancelarMOdificacion");
        if (bandera == 1) {
            //CERRAR FILTRADO
            FacesContext c = FacesContext.getCurrentInstance();

            codigo = (Column) c.getViewRoot().findComponent("form:datosMotivoContrato:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosMotivoContrato:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosMotivoContrato");
            bandera = 0;
            filtrarMotivosContratos = null;
            tipoLista = 0;
            tamano = 270;
        }

        borrarMotivoContrato.clear();
        crearMotivoContratos.clear();
        modificarMotivoContrato.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listMotivosContratos = null;
        guardado = true;
        permitirIndex = true;
        getListMotivosContratos();
        RequestContext context = RequestContext.getCurrentInstance();
        if (listMotivosContratos == null || listMotivosContratos.isEmpty()) {
            //infoRegistro = "Cantidad de registros: 0 ";
            modificarInfoRegistro(0);
        } else {
            //infoRegistro = "Cantidad de registros: " + listMotivosContratos.size();
            modificarInfoRegistro(listMotivosContratos.size());
        }
        context.update("form:datosMotivoContrato");
        context.update("form:informacionRegistro");
        context.update("form:ACEPTAR");
    }

    public void salir() {
        if (bandera == 1) {
            //CERRAR FILTRADO
            FacesContext c = FacesContext.getCurrentInstance();

            codigo = (Column) c.getViewRoot().findComponent("form:datosMotivoContrato:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosMotivoContrato:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosMotivoContrato");
            bandera = 0;
            filtrarMotivosContratos = null;
            tipoLista = 0;
            tamano = 270;
        }

        borrarMotivoContrato.clear();
        crearMotivoContratos.clear();
        modificarMotivoContrato.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listMotivosContratos = null;
        guardado = true;
        permitirIndex = true;
        getListMotivosContratos();
        RequestContext context = RequestContext.getCurrentInstance();
        if (listMotivosContratos == null || listMotivosContratos.isEmpty()) {
            //infoRegistro = "Cantidad de registros: 0 ";
            modificarInfoRegistro(0);
        } else {
            //infoRegistro = "Cantidad de registros: " + listMotivosContratos.size();
            modificarInfoRegistro(listMotivosContratos.size());
        }
        context.update("form:datosMotivoContrato");
        context.update("form:informacionRegistro");
        context.update("form:ACEPTAR");
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            tamano = 246;
            codigo = (Column) c.getViewRoot().findComponent("form:datosMotivoContrato:codigo");
            codigo.setFilterStyle("width: 200px");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosMotivoContrato:descripcion");
            descripcion.setFilterStyle("width: 200px");
            RequestContext.getCurrentInstance().update("form:datosMotivoContrato");
            System.out.println("Activar");
            bandera = 1;
        } else if (bandera == 1) {
            tamano = 270;
            System.out.println("Desactivar");
            codigo = (Column) c.getViewRoot().findComponent("form:datosMotivoContrato:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosMotivoContrato:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosMotivoContrato");
            bandera = 0;
            filtrarMotivosContratos = null;
            tipoLista = 0;
        }
        RequestContext.getCurrentInstance().update("form:datosMotivoContrato");
    }

    public void modificarMotivosContrato(int indice, String confirmarCambio, String valorConfirmar) {
        System.err.println("ENTRE A MODIFICAR MOTIVOS CONTRATO");
        index = indice;

        int contador = 0;
        boolean banderita = false;
        Integer a;
        a = null;
        RequestContext context = RequestContext.getCurrentInstance();
        System.err.println("TIPO LISTA = " + tipoLista);
        if (confirmarCambio.equalsIgnoreCase("N")) {
            System.err.println("ENTRE A MODIFICAR MOTIVOS CONTRATOS, CONFIRMAR CAMBIO ES N");
            if (tipoLista == 0) {
                if (!crearMotivoContratos.contains(listMotivosContratos.get(indice))) {
                    if (listMotivosContratos.get(indice).getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listMotivosContratos.get(indice).setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < listMotivosContratos.size(); j++) {
                            if (j != indice) {
                                if (listMotivosContratos.get(indice).getCodigo() == listMotivosContratos.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            banderita = false;
                            listMotivosContratos.get(indice).setCodigo(backUpCodigo);

                        } else {
                            banderita = true;
                        }

                    }
                    if (listMotivosContratos.get(indice).getNombre().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listMotivosContratos.get(indice).setNombre(backUpDescripcion);

                    }
                    if (listMotivosContratos.get(indice).getNombre().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listMotivosContratos.get(indice).setNombre(backUpDescripcion);

                    }

                    if (banderita == true) {
                        if (modificarMotivoContrato.isEmpty()) {
                            modificarMotivoContrato.add(listMotivosContratos.get(indice));
                        } else if (!modificarMotivoContrato.contains(listMotivosContratos.get(indice))) {
                            modificarMotivoContrato.add(listMotivosContratos.get(indice));
                        }
                        if (guardado == true) {
                            guardado = false;
                        }

                    } else {
                        context.update("form:validacionModificar");
                        context.execute("validacionModificar.show()");
                    }
                    index = -1;
                    secRegistro = null;
                } else {
                    if (listMotivosContratos.get(indice).getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listMotivosContratos.get(indice).setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < listMotivosContratos.size(); j++) {
                            if (j != indice) {
                                if (listMotivosContratos.get(indice).getCodigo() == listMotivosContratos.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            banderita = false;
                            listMotivosContratos.get(indice).setCodigo(backUpCodigo);

                        } else {
                            banderita = true;
                        }

                    }
                    if (listMotivosContratos.get(indice).getNombre().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listMotivosContratos.get(indice).setNombre(backUpDescripcion);

                    }
                    if (listMotivosContratos.get(indice).getNombre().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listMotivosContratos.get(indice).setNombre(backUpDescripcion);

                    }

                    if (banderita == true) {

                        if (guardado == true) {
                            guardado = false;
                        }

                    } else {
                        context.update("form:validacionModificar");
                        context.execute("validacionModificar.show()");
                    }
                    index = -1;
                    secRegistro = null;

                }
            } else {

                if (!crearMotivoContratos.contains(filtrarMotivosContratos.get(indice))) {
                    if (filtrarMotivosContratos.get(indice).getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        filtrarMotivosContratos.get(indice).setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < listMotivosContratos.size(); j++) {
                            if (j != indice) {
                                if (filtrarMotivosContratos.get(indice).getCodigo() == listMotivosContratos.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            banderita = false;
                            filtrarMotivosContratos.get(indice).setCodigo(backUpCodigo);
                        } else {
                            banderita = true;
                        }

                    }

                    if (filtrarMotivosContratos.get(indice).getNombre().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        filtrarMotivosContratos.get(indice).setNombre(backUpDescripcion);
                    }
                    if (filtrarMotivosContratos.get(indice).getNombre().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        filtrarMotivosContratos.get(indice).setNombre(backUpDescripcion);
                    }

                    if (banderita == true) {
                        if (modificarMotivoContrato.isEmpty()) {
                            modificarMotivoContrato.add(filtrarMotivosContratos.get(indice));
                        } else if (!modificarMotivoContrato.contains(filtrarMotivosContratos.get(indice))) {
                            modificarMotivoContrato.add(filtrarMotivosContratos.get(indice));
                        }
                        if (guardado == true) {
                            guardado = false;
                        }

                    } else {
                        context.update("form:validacionModificar");
                        context.execute("validacionModificar.show()");
                    }
                    index = -1;
                    secRegistro = null;
                } else {
                    if (filtrarMotivosContratos.get(indice).getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        filtrarMotivosContratos.get(indice).setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < listMotivosContratos.size(); j++) {
                            if (j != indice) {
                                if (filtrarMotivosContratos.get(indice).getCodigo() == listMotivosContratos.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            banderita = false;
                            filtrarMotivosContratos.get(indice).setCodigo(backUpCodigo);
                        } else {
                            banderita = true;
                        }

                    }

                    if (filtrarMotivosContratos.get(indice).getNombre().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        filtrarMotivosContratos.get(indice).setNombre(backUpDescripcion);
                    }
                    if (filtrarMotivosContratos.get(indice).getNombre().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        filtrarMotivosContratos.get(indice).setNombre(backUpDescripcion);
                    }

                    if (banderita == true) {
                        if (guardado == true) {
                            guardado = false;
                        }

                    } else {
                        context.update("form:validacionModificar");
                        context.execute("validacionModificar.show()");
                    }
                    index = -1;
                    secRegistro = null;

                }

            }
            context.update("form:datosMotivoContrato");
            context.update("form:ACEPTAR");
        }

    }

    public void borrarMotivosContratos() {

        if (index >= 0) {

            if (tipoLista == 0) {
                System.out.println("Entro a borrarMotivosContratos");
                if (!modificarMotivoContrato.isEmpty() && modificarMotivoContrato.contains(listMotivosContratos.get(index))) {
                    int modIndex = modificarMotivoContrato.indexOf(listMotivosContratos.get(index));
                    modificarMotivoContrato.remove(modIndex);
                    borrarMotivoContrato.add(listMotivosContratos.get(index));
                } else if (!crearMotivoContratos.isEmpty() && crearMotivoContratos.contains(listMotivosContratos.get(index))) {
                    int crearIndex = crearMotivoContratos.indexOf(listMotivosContratos.get(index));
                    crearMotivoContratos.remove(crearIndex);
                } else {
                    borrarMotivoContrato.add(listMotivosContratos.get(index));
                }
                listMotivosContratos.remove(index);
            }
            if (tipoLista == 1) {
                System.out.println("borrarMotivosContratos ");
                if (!modificarMotivoContrato.isEmpty() && modificarMotivoContrato.contains(filtrarMotivosContratos.get(index))) {
                    int modIndex = modificarMotivoContrato.indexOf(filtrarMotivosContratos.get(index));
                    modificarMotivoContrato.remove(modIndex);
                    borrarMotivoContrato.add(filtrarMotivosContratos.get(index));
                } else if (!crearMotivoContratos.isEmpty() && crearMotivoContratos.contains(filtrarMotivosContratos.get(index))) {
                    int crearIndex = crearMotivoContratos.indexOf(filtrarMotivosContratos.get(index));
                    crearMotivoContratos.remove(crearIndex);
                } else {
                    borrarMotivoContrato.add(filtrarMotivosContratos.get(index));
                }
                int VCIndex = listMotivosContratos.indexOf(filtrarMotivosContratos.get(index));
                listMotivosContratos.remove(VCIndex);
                filtrarMotivosContratos.remove(index);

            }
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("form:datosMotivoContrato");
           //infoRegistro = "Cantidad de registros: " + listMotivosContratos.size();
            modificarInfoRegistro(listMotivosContratos.size());
            context.update("form:informacionRegistro");
            index = -1;
            secRegistro = null;

            if (guardado == true) {
                guardado = false;
            }
            context.update("form:ACEPTAR");
        }

    }

    public void verificarBorrado() {
        System.out.println("Estoy en verificarBorrado");
        try {
            if (tipoLista == 0) {
                borradoVC = administrarMotivosContratos.contarVigenciasTiposContratosMotivoContrato(listMotivosContratos.get(index).getSecuencia());
            } else {
                borradoVC = administrarMotivosContratos.contarVigenciasTiposContratosMotivoContrato(filtrarMotivosContratos.get(index).getSecuencia());
            }
            if (borradoVC.equals(new BigInteger("0"))) {
                System.out.println("Borrado==0");
                borrarMotivosContratos();
            } else {
                System.out.println("Borrado>0");
                RequestContext context = RequestContext.getCurrentInstance();
                context.update("form:validacionBorrar");
                context.execute("validacionBorrar.show()");
                index = -1;
                borradoVC = new BigInteger("-1");
            }

        } catch (Exception e) {
            System.err.println("ERROR ControlMotivosContratos verificarBorrado ERROR " + e);
        }
    }

    public void guardarMotivosContratos() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (guardado == false) {
            System.out.println("Realizando Motivos Contratos");
            if (!borrarMotivoContrato.isEmpty()) {
                System.out.println("Borrando...");
                administrarMotivosContratos.borrarMotivosContratos(borrarMotivoContrato);
                registrosBorrados = borrarMotivoContrato.size();
                context.update("form:mostrarBorrados");
                context.execute("mostrarBorrados.show()");
                borrarMotivoContrato.clear();
            }
            if (!crearMotivoContratos.isEmpty()) {
                administrarMotivosContratos.crearMotivosContratos(crearMotivoContratos);
            }
            crearMotivoContratos.clear();

            if (!modificarMotivoContrato.isEmpty()) {
                administrarMotivosContratos.modificarMotivosContratos(modificarMotivoContrato);
                modificarMotivoContrato.clear();
            }
            System.out.println("Se guardaron los datos con exito");
            listMotivosContratos = null;
            context.update("form:datosMotivoContrato");
            FacesMessage msg = new FacesMessage("Información", "Se gurdarón los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            context.update("form:growl");
            k = 0;
            guardado = true;
        }
        index = -1;

        RequestContext.getCurrentInstance()
                .update("form:ACEPTAR");

    }

    public void editarCelda() {
        if (index >= 0) {
            if (tipoLista == 0) {
                editarMotivoContrato = listMotivosContratos.get(index);
            }
            if (tipoLista == 1) {
                editarMotivoContrato = filtrarMotivosContratos.get(index);
            }

            RequestContext context = RequestContext.getCurrentInstance();
            System.out.println("Entro a editar... valor celda: " + cualCelda);
            if (cualCelda == 0) {
                context.update("formularioDialogos:editCodigo");
                context.execute("editCodigo.show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                context.update("formularioDialogos:editDescripcion");
                context.execute("editDescripcion.show()");
                cualCelda = -1;
            }

        }
        index = -1;
        secRegistro = null;
    }

    public void agregarNuevoMotivoContrato() {
        System.out.println("Agregar Motivo Contrato");
        int contador = 0;
        int duplicados = 0;

        Integer a = 0;
        a = null;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        if (nuevoMotivoContrato.getCodigo() == a) {
            mensajeValidacion = " *Codigo \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else {
            System.out.println("codigo en Motivo Cambio Cargo: " + nuevoMotivoContrato.getCodigo());

            for (int x = 0; x < listMotivosContratos.size(); x++) {
                if (listMotivosContratos.get(x).getCodigo() == nuevoMotivoContrato.getCodigo()) {
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
        if (nuevoMotivoContrato.getNombre() == (null)) {
            mensajeValidacion = mensajeValidacion + " *Descripcion \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else if (nuevoMotivoContrato.getNombre().isEmpty()) {
            mensajeValidacion = mensajeValidacion + " *Descripcion \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("bandera");
            contador++;

        }

        System.out.println("contador " + contador);

        if (contador == 2) {
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                System.out.println("Desactivar");
                codigo = (Column) c.getViewRoot().findComponent("form:datosMotivoContrato:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosMotivoContrato:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosMotivoContrato");
                bandera = 0;
                filtrarMotivosContratos = null;
                tipoLista = 0;
                tamano = 270;
            }
            System.out.println("Despues de la bandera");

            //AGREGAR REGISTRO A LA LISTA VIGENCIAS CARGOS EMPLEADO.
            k++;
            l = BigInteger.valueOf(k);
            nuevoMotivoContrato.setSecuencia(l);

            crearMotivoContratos.add(nuevoMotivoContrato);

            listMotivosContratos.add(nuevoMotivoContrato);
            nuevoMotivoContrato = new MotivosContratos();

            context.update("form:datosMotivoContrato");
            //infoRegistro = "Cantidad de registros: " + listMotivosContratos.size();
            modificarInfoRegistro(listMotivosContratos.size());
            context.update("form:informacionRegistro");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            System.out.println("Despues de la bandera guardado");

            context.execute("nuevoRegistroMotivoContratos.hide()");
            index = -1;
            secRegistro = null;
            System.out.println("Despues de nuevoRegistroMotivoContratos");

        } else {
            context.update("form:validacionNuevaCentroCosto");
            context.execute("validacionNuevaCentroCosto.show()");
            contador = 0;
        }
    }

    public void limpiarNuevoMotivoContratos() {
        System.out.println("limpiarnuevoMotivoContrato");
        nuevoMotivoContrato = new MotivosContratos();
        secRegistro = null;
        index = -1;

    }

    //------------------------------------------------------------------------------
    public void duplicarMotivosContratos() {
        System.out.println("duplicarMotivosCambiosCargos");
        if (index >= 0) {
            duplicarMotivoContrato = new MotivosContratos();
            k++;
            l = BigInteger.valueOf(k);

            if (tipoLista == 0) {
                duplicarMotivoContrato.setSecuencia(l);
                duplicarMotivoContrato.setCodigo(listMotivosContratos.get(index).getCodigo());
                duplicarMotivoContrato.setNombre(listMotivosContratos.get(index).getNombre());
            }
            if (tipoLista == 1) {
                duplicarMotivoContrato.setSecuencia(l);
                duplicarMotivoContrato.setCodigo(filtrarMotivosContratos.get(index).getCodigo());
                duplicarMotivoContrato.setNombre(filtrarMotivosContratos.get(index).getNombre());
            }

            RequestContext context = RequestContext.getCurrentInstance();
            context.update("formularioDialogos:duplicarMotivosCambiosCargos");
            context.execute("duplicarRegistroMotivosContratos.show()");
            index = -1;
            secRegistro = null;
        }
    }

    public void confirmarDuplicar() {
        System.err.println("ESTOY EN CONFIRMAR DUPLICAR CONTROLTIPOSCENTROSCOSTOS");
        int contador = 0;
        mensajeValidacion = " ";
        int duplicados = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        Integer a = 0;
        a = null;
        System.err.println("ConfirmarDuplicar codigo " + duplicarMotivoContrato.getCodigo());
        System.err.println("ConfirmarDuplicar nombre " + duplicarMotivoContrato.getNombre());

        if (duplicarMotivoContrato.getCodigo() == a) {
            mensajeValidacion = mensajeValidacion + "   *Codigo \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else {
            for (int x = 0; x < listMotivosContratos.size(); x++) {
                if (listMotivosContratos.get(x).getCodigo() == duplicarMotivoContrato.getCodigo()) {
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
        if (duplicarMotivoContrato.getNombre() == null) {
            mensajeValidacion = mensajeValidacion + "   *Nombre \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("Bandera : ");
            contador++;
        }

        if (contador == 2) {

            System.out.println("Datos Duplicando: " + duplicarMotivoContrato.getSecuencia() + "  " + duplicarMotivoContrato.getCodigo());
            if (crearMotivoContratos.contains(duplicarMotivoContrato)) {
                System.out.println("Ya lo contengo.");
            }
            listMotivosContratos.add(duplicarMotivoContrato);
            crearMotivoContratos.add(duplicarMotivoContrato);
            context.update("form:datosMotivoContrato");
            //infoRegistro = "Cantidad de registros: " + listMotivosContratos.size();
            modificarInfoRegistro(listMotivosContratos.size());
            context.update("form:informacionRegistro");
            index = -1;
            secRegistro = null;
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            if (bandera == 1) {
                //CERRAR FILTRADO
                FacesContext c = FacesContext.getCurrentInstance();
                tamano = 270;
                codigo = (Column) c.getViewRoot().findComponent("form:datosMotivoContrato:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosMotivoContrato:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosMotivoContrato");
                bandera = 0;
                filtrarMotivosContratos = null;
                tipoLista = 0;
            }
            duplicarMotivoContrato = new MotivosContratos();
            RequestContext.getCurrentInstance().execute("duplicarRegistroMotivosContratos.hide()");

        } else {
            contador = 0;
            context.update("form:validacionDuplicarVigencia");
            context.execute("validacionDuplicarVigencia.show()");
        }
    }

    public void limpiarduplicarMotivosContratos() {
        duplicarMotivoContrato = new MotivosContratos();
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosMotivoContratoExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "MotivosContratosPDF", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosMotivoContratoExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "MotivosContratosXLS", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        System.out.println("lol");
        if (!listMotivosContratos.isEmpty()) {
            if (secRegistro != null) {
                System.out.println("lol 2");
                int resultado = administrarRastros.obtenerTabla(secRegistro, "MOTIVOSCONTRATOS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
                System.out.println("resultado: " + resultado);
                if (resultado == 1) {
                    context.execute("errorObjetosDB.show()");
                } else if (resultado == 2) {
                    context.execute("confirmarRastro.show()");
                } else if (resultado == 3) {
                    context.execute("errorRegistroRastro.show()");
                } else if (resultado == 4) {
                    context.execute("errorTablaConRastro.show()");
                } else if (resultado == 5) {
                    context.execute("errorTablaSinRastro.show()");
                }
            } else {
                context.execute("seleccionarRegistro.show()");
            }
        } else {
            if (administrarRastros.verificarHistoricosTabla("MOTIVOSCONTRATOS")) { // igual acá
                context.execute("confirmarRastroHistorico.show()");
            } else {
                context.execute("errorRastroHistorico.show()");
            }

        }
        index = -1;
    }
    
    private void modificarInfoRegistro(int valor) {
        infoRegistro = String.valueOf(valor);
        System.out.println("infoRegistro: " + infoRegistro);
    }

//-----------------------//---------------//----------------------//------------
    public List<MotivosContratos> getListMotivosContratos() {
        
         if (listMotivosContratos == null) {
            listMotivosContratos = administrarMotivosContratos.consultarMotivosContratos();
            RequestContext context = RequestContext.getCurrentInstance();
            if (listMotivosContratos == null || listMotivosContratos.isEmpty()) {
                infoRegistro = "0";
            } else {
                infoRegistro = String.valueOf(listMotivosContratos.size());
            }
            context.update("form:informacionRegistro");
        }
        return listMotivosContratos;
    }

    public void setListMotivosContratos(List<MotivosContratos> listMotivosContratos) {
        this.listMotivosContratos = listMotivosContratos;
    }

    public List<MotivosContratos> getFiltrarMotivosContratos() {
        return filtrarMotivosContratos;
    }

    public void setFiltrarMotivosContratos(List<MotivosContratos> filtrarMotivosContratos) {
        this.filtrarMotivosContratos = filtrarMotivosContratos;
    }

    public MotivosContratos getNuevoMotivoContrato() {
        return nuevoMotivoContrato;
    }

    public void setNuevoMotivoContrato(MotivosContratos nuevoMotivoContrato) {
        this.nuevoMotivoContrato = nuevoMotivoContrato;
    }

    public MotivosContratos getEditarMotivoContrato() {
        return editarMotivoContrato;
    }

    public void setEditarMotivoContrato(MotivosContratos editarMotivoContrato) {
        this.editarMotivoContrato = editarMotivoContrato;
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

    public MotivosContratos getDuplicarMotivoContrato() {
        return duplicarMotivoContrato;
    }

    public void setDuplicarMotivoContrato(MotivosContratos duplicarMotivoContrato) {
        this.duplicarMotivoContrato = duplicarMotivoContrato;
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

    public MotivosContratos getMotivoContratoSeleccionado() {
        return motivoContratoSeleccionado;
    }

    public void setMotivoContratoSeleccionado(MotivosContratos motivoContratoSeleccionado) {
        this.motivoContratoSeleccionado = motivoContratoSeleccionado;
    }

    public int getTamano() {
        return tamano;
    }

    public void setTamano(int tamano) {
        this.tamano = tamano;
    }

    public String getInfoRegistro() {
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

}
