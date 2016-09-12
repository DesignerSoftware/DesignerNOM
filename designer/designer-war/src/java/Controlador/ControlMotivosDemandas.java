/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import InterfaceAdministrar.AdministrarMotivosDemandasInterface;
import Entidades.MotivosDemandas;
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
import utilidadesUI.PrimefacesContextUI;

/**
 *
 * @author user
 */
@ManagedBean
@SessionScoped
public class ControlMotivosDemandas implements Serializable {

    @EJB
    AdministrarMotivosDemandasInterface administrarMotivosDemandas;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    private List<MotivosDemandas> listMotivosDemandas;
    private List<MotivosDemandas> filtrarMotivosDemandas;
    private List<MotivosDemandas> crearMotivoContratos;
    private List<MotivosDemandas> modificarMotivoContrato;
    private List<MotivosDemandas> borrarMotivoContrato;
    private MotivosDemandas nuevoMotivoContrato;
    private MotivosDemandas duplicarMotivoContrato;
    private MotivosDemandas editarMotivoContrato;
    private MotivosDemandas motivoDemandaSeleccionado;
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
    private BigInteger contarDemandasMotivoDemanda;
    private int tamano;

    private Integer backUpCodigo;
    private String backUpDescripcion;
    private String infoRegistro;

    public ControlMotivosDemandas() {

        listMotivosDemandas = null;
        crearMotivoContratos = new ArrayList<MotivosDemandas>();
        modificarMotivoContrato = new ArrayList<MotivosDemandas>();
        borrarMotivoContrato = new ArrayList<MotivosDemandas>();
        permitirIndex = true;
        editarMotivoContrato = new MotivosDemandas();
        nuevoMotivoContrato = new MotivosDemandas();
        duplicarMotivoContrato = new MotivosDemandas();
        guardado = true;
        tamano = 270;
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarMotivosDemandas.obtenerConexion(ses.getId());
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
            infoRegistro = "Cantidad de registros: " + filtrarMotivosDemandas.size();
            PrimefacesContextUI.actualizar("form:informacionRegistro");
        } catch (Exception e) {
            System.out.println("ERROR ControlMotiviosCambiosCargos eventoFiltrar ERROR===" + e.getMessage());
        }
    }

    public void cambiarIndice(int indice, int celda) {
        System.err.println("TIPO LISTA = " + tipoLista);

        if (permitirIndex == true) {
            index = indice;
            cualCelda = celda;
            if (tipoLista == 0) {
                if (cualCelda == 0) {
                    backUpCodigo = listMotivosDemandas.get(index).getCodigo();
                    System.out.println(" backUpCodigo : " + backUpCodigo);
                } else if (cualCelda == 1) {
                    backUpDescripcion = listMotivosDemandas.get(index).getDescripcion();
                    System.out.println(" backUpDescripcion : " + backUpDescripcion);
                }
                secRegistro = listMotivosDemandas.get(index).getSecuencia();
            } else {
                if (cualCelda == 0) {
                    backUpCodigo = filtrarMotivosDemandas.get(index).getCodigo();
                    System.out.println(" backUpCodigo : " + backUpCodigo);

                } else if (cualCelda == 1) {
                    backUpDescripcion = filtrarMotivosDemandas.get(index).getDescripcion();
                    System.out.println(" backUpDescripcion : " + backUpDescripcion);

                }
                secRegistro = filtrarMotivosDemandas.get(index).getSecuencia();
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
        if (bandera == 1) {
            //CERRAR FILTRADO
            FacesContext c = FacesContext.getCurrentInstance();

            codigo = (Column) c.getViewRoot().findComponent("form:datosMotivoContrato:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosMotivoContrato:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            PrimefacesContextUI.actualizar("form:datosMotivoContrato");
            bandera = 0;
            filtrarMotivosDemandas = null;
            tipoLista = 0;
            tamano = 270;
        }

        borrarMotivoContrato.clear();
        crearMotivoContratos.clear();
        modificarMotivoContrato.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listMotivosDemandas = null;
        guardado = true;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        PrimefacesContextUI.actualizar("form:datosMotivoContrato");
        if (listMotivosDemandas == null || listMotivosDemandas.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listMotivosDemandas.size();
        }
        PrimefacesContextUI.actualizar("form:informacionRegistro");
        PrimefacesContextUI.actualizar("form:ACEPTAR");
    }

    public void salir() {
        if (bandera == 1) {
            //CERRAR FILTRADO
            FacesContext c = FacesContext.getCurrentInstance();

            codigo = (Column) c.getViewRoot().findComponent("form:datosMotivoContrato:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosMotivoContrato:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            PrimefacesContextUI.actualizar("form:datosMotivoContrato");
            bandera = 0;
            filtrarMotivosDemandas = null;
            tipoLista = 0;
            tamano = 270;
        }

        borrarMotivoContrato.clear();
        crearMotivoContratos.clear();
        modificarMotivoContrato.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listMotivosDemandas = null;
        guardado = true;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        PrimefacesContextUI.actualizar("form:datosMotivoContrato");
        if (listMotivosDemandas == null || listMotivosDemandas.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listMotivosDemandas.size();
        }
        PrimefacesContextUI.actualizar("form:informacionRegistro");
        PrimefacesContextUI.actualizar("form:ACEPTAR");
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            tamano = 250;
            codigo = (Column) c.getViewRoot().findComponent("form:datosMotivoContrato:codigo");
            codigo.setFilterStyle("width: 85%;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosMotivoContrato:descripcion");
            descripcion.setFilterStyle("width: 85%;");
            PrimefacesContextUI.actualizar("form:datosMotivoContrato");
            System.out.println("Activar");
            bandera = 1;
        } else if (bandera == 1) {
            tamano = 270;
            System.out.println("Desactivar");
            codigo = (Column) c.getViewRoot().findComponent("form:datosMotivoContrato:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosMotivoContrato:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            PrimefacesContextUI.actualizar("form:datosMotivoContrato");
            bandera = 0;
            filtrarMotivosDemandas = null;
            tipoLista = 0;
        }
        PrimefacesContextUI.actualizar("form:datosMotivoContrato");
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
                if (!crearMotivoContratos.contains(listMotivosDemandas.get(indice))) {
                    if (listMotivosDemandas.get(indice).getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listMotivosDemandas.get(indice).setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < listMotivosDemandas.size(); j++) {
                            if (j != indice) {
                                if (listMotivosDemandas.get(indice).getCodigo() == listMotivosDemandas.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            banderita = false;
                            listMotivosDemandas.get(indice).setCodigo(backUpCodigo);

                        } else {
                            banderita = true;
                        }

                    }
                    if (listMotivosDemandas.get(indice).getDescripcion().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listMotivosDemandas.get(indice).setDescripcion(backUpDescripcion);

                    }
                    if (listMotivosDemandas.get(indice).getDescripcion().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listMotivosDemandas.get(indice).setDescripcion(backUpDescripcion);

                    }

                    if (banderita == true) {
                        if (modificarMotivoContrato.isEmpty()) {
                            modificarMotivoContrato.add(listMotivosDemandas.get(indice));
                        } else if (!modificarMotivoContrato.contains(listMotivosDemandas.get(indice))) {
                            modificarMotivoContrato.add(listMotivosDemandas.get(indice));
                        }
                        if (guardado == true) {
                            guardado = false;
                        }

                    } else {
                        PrimefacesContextUI.actualizar("form:validacionModificar");
                        PrimefacesContextUI.ejecutar("PF('validacionModificar').show()");
                    }
                    index = -1;
                    secRegistro = null;
                } else {
                    if (listMotivosDemandas.get(indice).getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listMotivosDemandas.get(indice).setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < listMotivosDemandas.size(); j++) {
                            if (j != indice) {
                                if (listMotivosDemandas.get(indice).getCodigo() == listMotivosDemandas.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            banderita = false;
                            listMotivosDemandas.get(indice).setCodigo(backUpCodigo);

                        } else {
                            banderita = true;
                        }

                    }
                    if (listMotivosDemandas.get(indice).getDescripcion().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listMotivosDemandas.get(indice).setDescripcion(backUpDescripcion);

                    }
                    if (listMotivosDemandas.get(indice).getDescripcion().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listMotivosDemandas.get(indice).setDescripcion(backUpDescripcion);

                    }

                    if (banderita == true) {

                        if (guardado == true) {
                            guardado = false;
                        }

                    } else {
                        PrimefacesContextUI.actualizar("form:validacionModificar");
                        PrimefacesContextUI.ejecutar("PF('validacionModificar').show()");
                    }
                    index = -1;
                    secRegistro = null;

                }
            } else {

                if (!crearMotivoContratos.contains(filtrarMotivosDemandas.get(indice))) {
                    if (filtrarMotivosDemandas.get(indice).getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        filtrarMotivosDemandas.get(indice).setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < listMotivosDemandas.size(); j++) {
                            if (j != indice) {
                                if (filtrarMotivosDemandas.get(indice).getCodigo() == listMotivosDemandas.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            banderita = false;
                            filtrarMotivosDemandas.get(indice).setCodigo(backUpCodigo);
                        } else {
                            banderita = true;
                        }

                    }

                    if (filtrarMotivosDemandas.get(indice).getDescripcion().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        filtrarMotivosDemandas.get(indice).setDescripcion(backUpDescripcion);
                    }
                    if (filtrarMotivosDemandas.get(indice).getDescripcion().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        filtrarMotivosDemandas.get(indice).setDescripcion(backUpDescripcion);
                    }

                    if (banderita == true) {
                        if (modificarMotivoContrato.isEmpty()) {
                            modificarMotivoContrato.add(filtrarMotivosDemandas.get(indice));
                        } else if (!modificarMotivoContrato.contains(filtrarMotivosDemandas.get(indice))) {
                            modificarMotivoContrato.add(filtrarMotivosDemandas.get(indice));
                        }
                        if (guardado == true) {
                            guardado = false;
                        }

                    } else {
                        PrimefacesContextUI.actualizar("form:validacionModificar");
                        PrimefacesContextUI.ejecutar("PF('validacionModificar').show()");
                    }
                    index = -1;
                    secRegistro = null;
                } else {
                    if (filtrarMotivosDemandas.get(indice).getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        filtrarMotivosDemandas.get(indice).setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < listMotivosDemandas.size(); j++) {
                            if (j != indice) {
                                if (filtrarMotivosDemandas.get(indice).getCodigo() == listMotivosDemandas.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            banderita = false;
                            filtrarMotivosDemandas.get(indice).setCodigo(backUpCodigo);
                        } else {
                            banderita = true;
                        }

                    }

                    if (filtrarMotivosDemandas.get(indice).getDescripcion().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        filtrarMotivosDemandas.get(indice).setDescripcion(backUpDescripcion);
                    }
                    if (filtrarMotivosDemandas.get(indice).getDescripcion().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        filtrarMotivosDemandas.get(indice).setDescripcion(backUpDescripcion);
                    }

                    if (banderita == true) {
                        if (guardado == true) {
                            guardado = false;
                        }

                    } else {
                        PrimefacesContextUI.actualizar("form:validacionModificar");
                        PrimefacesContextUI.ejecutar("PF('validacionModificar').show()");
                    }
                    index = -1;
                    secRegistro = null;

                }

            }
            PrimefacesContextUI.actualizar("form:datosMotivoContrato");
            PrimefacesContextUI.actualizar("form:ACEPTAR");
        }

    }

    public void borrarMotivosDemandas() {

        if (index >= 0) {

            if (tipoLista == 0) {
                System.out.println("Entro a borrarMotivosDemandas");
                if (!modificarMotivoContrato.isEmpty() && modificarMotivoContrato.contains(listMotivosDemandas.get(index))) {
                    int modIndex = modificarMotivoContrato.indexOf(listMotivosDemandas.get(index));
                    modificarMotivoContrato.remove(modIndex);
                    borrarMotivoContrato.add(listMotivosDemandas.get(index));
                } else if (!crearMotivoContratos.isEmpty() && crearMotivoContratos.contains(listMotivosDemandas.get(index))) {
                    int crearIndex = crearMotivoContratos.indexOf(listMotivosDemandas.get(index));
                    crearMotivoContratos.remove(crearIndex);
                } else {
                    borrarMotivoContrato.add(listMotivosDemandas.get(index));
                }
                listMotivosDemandas.remove(index);
            }
            if (tipoLista == 1) {
                System.out.println("borrarMotivosDemandas ");
                if (!modificarMotivoContrato.isEmpty() && modificarMotivoContrato.contains(filtrarMotivosDemandas.get(index))) {
                    int modIndex = modificarMotivoContrato.indexOf(filtrarMotivosDemandas.get(index));
                    modificarMotivoContrato.remove(modIndex);
                    borrarMotivoContrato.add(filtrarMotivosDemandas.get(index));
                } else if (!crearMotivoContratos.isEmpty() && crearMotivoContratos.contains(filtrarMotivosDemandas.get(index))) {
                    int crearIndex = crearMotivoContratos.indexOf(filtrarMotivosDemandas.get(index));
                    crearMotivoContratos.remove(crearIndex);
                } else {
                    borrarMotivoContrato.add(filtrarMotivosDemandas.get(index));
                }
                int VCIndex = listMotivosDemandas.indexOf(filtrarMotivosDemandas.get(index));
                listMotivosDemandas.remove(VCIndex);
                filtrarMotivosDemandas.remove(index);

            }
            RequestContext context = RequestContext.getCurrentInstance();
            PrimefacesContextUI.actualizar("form:datosMotivoContrato");
            infoRegistro = "Cantidad de registros: " + listMotivosDemandas.size();
            PrimefacesContextUI.actualizar("form:informacionRegistro");
            index = -1;
            secRegistro = null;

            if (guardado == true) {
                guardado = false;
            }
            PrimefacesContextUI.actualizar("form:ACEPTAR");
        }

    }

    public void verificarBorrado() {
        System.out.println("Estoy en verificarBorrado");
        try {
            if (tipoLista == 0) {
                contarDemandasMotivoDemanda = administrarMotivosDemandas.contarDemandasMotivoDemanda(listMotivosDemandas.get(index).getSecuencia());
            } else {
                contarDemandasMotivoDemanda = administrarMotivosDemandas.contarDemandasMotivoDemanda(filtrarMotivosDemandas.get(index).getSecuencia());
            }
            if (contarDemandasMotivoDemanda.equals(new BigInteger("0"))) {
                System.out.println("Borrado==0");
                borrarMotivosDemandas();
            } else {
                System.out.println("Borrado>0");
                RequestContext context = RequestContext.getCurrentInstance();
                PrimefacesContextUI.actualizar("form:validacionBorrar");
                PrimefacesContextUI.ejecutar("PF('validacionBorrar').show()");
                index = -1;
                contarDemandasMotivoDemanda = new BigInteger("-1");
            }

        } catch (Exception e) {
            System.err.println("ERROR ControlMotivosDemandas verificarBorrado ERROR " + e);
        }
    }

    public void guardarMotivosDemandas() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (guardado == false) {
            System.out.println("Realizando Motivos Contratos");
            if (!borrarMotivoContrato.isEmpty()) {
                System.out.println("Borrando...");
                administrarMotivosDemandas.borrarMotivosDemandas(borrarMotivoContrato);
                registrosBorrados = borrarMotivoContrato.size();
                PrimefacesContextUI.actualizar("form:mostrarBorrados");
                PrimefacesContextUI.ejecutar("PF('mostrarBorrados').show()");
                borrarMotivoContrato.clear();
            }
            if (!crearMotivoContratos.isEmpty()) {
                administrarMotivosDemandas.crearMotivosDemandas(crearMotivoContratos);
            }
            crearMotivoContratos.clear();

            if (!modificarMotivoContrato.isEmpty()) {
                administrarMotivosDemandas.modificarMotivosDemandas(modificarMotivoContrato);
                modificarMotivoContrato.clear();
            }
            System.out.println("Se guardaron los datos con exito");
            listMotivosDemandas = null;
            PrimefacesContextUI.actualizar("form:datosMotivoContrato");
            FacesMessage msg = new FacesMessage("Información", "Se gurdarón los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            PrimefacesContextUI.actualizar("form:growl");
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
                editarMotivoContrato = listMotivosDemandas.get(index);
            }
            if (tipoLista == 1) {
                editarMotivoContrato = filtrarMotivosDemandas.get(index);
            }

            RequestContext context = RequestContext.getCurrentInstance();
            System.out.println("Entro a editar... valor celda: " + cualCelda);
            if (cualCelda == 0) {
                PrimefacesContextUI.actualizar("formularioDialogos:editCodigo");
                PrimefacesContextUI.ejecutar("PF('editCodigo').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                PrimefacesContextUI.actualizar("formularioDialogos:editDescripcion");
                PrimefacesContextUI.ejecutar("PF('editDescripcion').show()");
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

            for (int x = 0; x < listMotivosDemandas.size(); x++) {
                if (listMotivosDemandas.get(x).getCodigo() == nuevoMotivoContrato.getCodigo()) {
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
        if (nuevoMotivoContrato.getDescripcion() == (null)) {
            mensajeValidacion = mensajeValidacion + " *Descripcion \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else if (nuevoMotivoContrato.getDescripcion().isEmpty()) {
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
                PrimefacesContextUI.actualizar("form:datosMotivoContrato");
                bandera = 0;
                filtrarMotivosDemandas = null;
                tipoLista = 0;
                tamano = 270;
            }
            System.out.println("Despues de la bandera");

            //AGREGAR REGISTRO A LA LISTA VIGENCIAS CARGOS EMPLEADO.
            k++;
            l = BigInteger.valueOf(k);
            nuevoMotivoContrato.setSecuencia(l);

            crearMotivoContratos.add(nuevoMotivoContrato);

            listMotivosDemandas.add(nuevoMotivoContrato);
            nuevoMotivoContrato = new MotivosDemandas();

            PrimefacesContextUI.actualizar("form:datosMotivoContrato");
            infoRegistro = "Cantidad de registros: " + listMotivosDemandas.size();
            PrimefacesContextUI.actualizar("form:informacionRegistro");
            if (guardado == true) {
                guardado = false;
                PrimefacesContextUI.actualizar("form:ACEPTAR");
            }
            System.out.println("Despues de la bandera guardado");

            PrimefacesContextUI.ejecutar("PF('nuevoRegistroMotivoContratos').hide()");
            index = -1;
            secRegistro = null;
            System.out.println("Despues de nuevoRegistroMotivoContratos");

        } else {
            PrimefacesContextUI.actualizar("form:validacionNuevaCentroCosto");
            PrimefacesContextUI.ejecutar("PF('validacionNuevaCentroCosto').show()");
            contador = 0;
        }
    }

    public void limpiarNuevoMotivoContratos() {
        System.out.println("limpiarnuevoMotivoContrato");
        nuevoMotivoContrato = new MotivosDemandas();
        secRegistro = null;
        index = -1;

    }

    //------------------------------------------------------------------------------
    public void duplicarMotivosDemandas() {
        System.out.println("duplicarMotivosCambiosCargos");
        if (index >= 0) {
            duplicarMotivoContrato = new MotivosDemandas();
            k++;
            l = BigInteger.valueOf(k);

            if (tipoLista == 0) {
                duplicarMotivoContrato.setSecuencia(l);
                duplicarMotivoContrato.setCodigo(listMotivosDemandas.get(index).getCodigo());
                duplicarMotivoContrato.setDescripcion(listMotivosDemandas.get(index).getDescripcion());
            }
            if (tipoLista == 1) {
                duplicarMotivoContrato.setSecuencia(l);
                duplicarMotivoContrato.setCodigo(filtrarMotivosDemandas.get(index).getCodigo());
                duplicarMotivoContrato.setDescripcion(filtrarMotivosDemandas.get(index).getDescripcion());
            }

            RequestContext context = RequestContext.getCurrentInstance();
            PrimefacesContextUI.actualizar("formularioDialogos:duplicarMotivosCambiosCargos");
            PrimefacesContextUI.ejecutar("PF('duplicarRegistroMotivosDemandas').show()");
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
        System.err.println("ConfirmarDuplicar nombre " + duplicarMotivoContrato.getDescripcion());

        if (duplicarMotivoContrato.getCodigo() == a) {
            mensajeValidacion = mensajeValidacion + "   *Codigo \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else {
            for (int x = 0; x < listMotivosDemandas.size(); x++) {
                if (listMotivosDemandas.get(x).getCodigo() == duplicarMotivoContrato.getCodigo()) {
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
        if (duplicarMotivoContrato.getDescripcion() == null) {
            mensajeValidacion = mensajeValidacion + "   *Descripcion \n";
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
            listMotivosDemandas.add(duplicarMotivoContrato);
            crearMotivoContratos.add(duplicarMotivoContrato);
            PrimefacesContextUI.actualizar("form:datosMotivoContrato");
            infoRegistro = "Cantidad de registros: " + listMotivosDemandas.size();
            PrimefacesContextUI.actualizar("form:informacionRegistro");
            index = -1;
            secRegistro = null;
            if (guardado == true) {
                guardado = false;
                PrimefacesContextUI.actualizar("form:ACEPTAR");
            }
            if (bandera == 1) {
                //CERRAR FILTRADO
                FacesContext c = FacesContext.getCurrentInstance();
                tamano = 270;
                codigo = (Column) c.getViewRoot().findComponent("form:datosMotivoContrato:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosMotivoContrato:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                PrimefacesContextUI.actualizar("form:datosMotivoContrato");
                bandera = 0;
                filtrarMotivosDemandas = null;
                tipoLista = 0;
            }
            duplicarMotivoContrato = new MotivosDemandas();
            PrimefacesContextUI.ejecutar("PF('duplicarRegistroMotivosDemandas').hide()");

        } else {
            contador = 0;
            PrimefacesContextUI.actualizar("form:validacionDuplicarVigencia");
            PrimefacesContextUI.ejecutar("PF('validacionDuplicarVigencia').show()");
        }
    }

    public void limpiarduplicarMotivosDemandas() {
        duplicarMotivoContrato = new MotivosDemandas();
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosMotivoContratoExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "MotivosDemandasPDF", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosMotivoContratoExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "MotivosDemandasXLS", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        System.out.println("lol");
        if (!listMotivosDemandas.isEmpty()) {
            if (secRegistro != null) {
                System.out.println("lol 2");
                int resultado = administrarRastros.obtenerTabla(secRegistro, "MOTIVOSDEMANDAS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
                System.out.println("resultado: " + resultado);
                if (resultado == 1) {
                    PrimefacesContextUI.ejecutar("PF('errorObjetosDB').show()");
                } else if (resultado == 2) {
                    PrimefacesContextUI.ejecutar("PF('confirmarRastro').show()");
                } else if (resultado == 3) {
                    PrimefacesContextUI.ejecutar("PF('errorRegistroRastro').show()");
                } else if (resultado == 4) {
                    PrimefacesContextUI.ejecutar("PF('errorTablaConRastro').show()");
                } else if (resultado == 5) {
                    PrimefacesContextUI.ejecutar("PF('errorTablaSinRastro').show()");
                }
            } else {
                PrimefacesContextUI.ejecutar("PF('seleccionarRegistro').show()");
            }
        } else {
            if (administrarRastros.verificarHistoricosTabla("MOTIVOSDEMANDAS")) { // igual acá
                PrimefacesContextUI.ejecutar("PF('confirmarRastroHistorico').show()");
            } else {
                PrimefacesContextUI.ejecutar("PF('errorRastroHistorico').show()");
            }

        }
        index = -1;
    }

//-----------------------//---------------//----------------------//------------
    public List<MotivosDemandas> getListMotivosDemandas() {
        if (listMotivosDemandas == null) {
            listMotivosDemandas = administrarMotivosDemandas.consultarMotivosDemandas();
        }
        RequestContext context = RequestContext.getCurrentInstance();

        if (listMotivosDemandas == null || listMotivosDemandas.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listMotivosDemandas.size();
        }
        PrimefacesContextUI.actualizar("form:informacionRegistro");
        return listMotivosDemandas;
    }

    public void setListMotivosDemandas(List<MotivosDemandas> listMotivosDemandas) {
        this.listMotivosDemandas = listMotivosDemandas;
    }

    public List<MotivosDemandas> getFiltrarMotivosDemandas() {
        return filtrarMotivosDemandas;
    }

    public void setFiltrarMotivosDemandas(List<MotivosDemandas> filtrarMotivosDemandas) {
        this.filtrarMotivosDemandas = filtrarMotivosDemandas;
    }

    public MotivosDemandas getNuevoMotivoContrato() {
        return nuevoMotivoContrato;
    }

    public void setNuevoMotivoContrato(MotivosDemandas nuevoMotivoContrato) {
        this.nuevoMotivoContrato = nuevoMotivoContrato;
    }

    public MotivosDemandas getEditarMotivoContrato() {
        return editarMotivoContrato;
    }

    public void setEditarMotivoContrato(MotivosDemandas editarMotivoContrato) {
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

    public MotivosDemandas getDuplicarMotivoContrato() {
        return duplicarMotivoContrato;
    }

    public void setDuplicarMotivoContrato(MotivosDemandas duplicarMotivoContrato) {
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

    public MotivosDemandas getMotivoDemandaSeleccionado() {
        return motivoDemandaSeleccionado;
    }

    public void setMotivoDemandaSeleccionado(MotivosDemandas motivoDemandaSeleccionado) {
        this.motivoDemandaSeleccionado = motivoDemandaSeleccionado;
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
