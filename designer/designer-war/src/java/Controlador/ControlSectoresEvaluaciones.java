/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import utilidadesUI.PrimefacesContextUI;
import Entidades.SectoresEvaluaciones;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarSectoresEvaluacionesInterface;
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
public class ControlSectoresEvaluaciones implements Serializable {

    @EJB
    AdministrarSectoresEvaluacionesInterface administrarSectoresEvaluaciones;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    private List<SectoresEvaluaciones> listSectoresEvaluaciones;
    private List<SectoresEvaluaciones> filtrarSectoresEvaluaciones;
    private List<SectoresEvaluaciones> crearSectoresEvaluaciones;
    private List<SectoresEvaluaciones> modificarSectoresEvaluaciones;
    private List<SectoresEvaluaciones> borrarSectoresEvaluaciones;
    private SectoresEvaluaciones nuevoSectoresEvaluaciones;
    private SectoresEvaluaciones duplicarSectoresEvaluaciones;
    private SectoresEvaluaciones editarSectoresEvaluaciones;
    private SectoresEvaluaciones sectoresEvaluacionesSeleccionada;
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
    //filtrado table
    private int tamano;
    private String backUpDescripcion;
    private String infoRegistro;
    private Integer backUpCodigo;

    public ControlSectoresEvaluaciones() {
        listSectoresEvaluaciones = null;
        crearSectoresEvaluaciones = new ArrayList<SectoresEvaluaciones>();
        modificarSectoresEvaluaciones = new ArrayList<SectoresEvaluaciones>();
        borrarSectoresEvaluaciones = new ArrayList<SectoresEvaluaciones>();
        permitirIndex = true;
        editarSectoresEvaluaciones = new SectoresEvaluaciones();
        nuevoSectoresEvaluaciones = new SectoresEvaluaciones();
        duplicarSectoresEvaluaciones = new SectoresEvaluaciones();
        guardado = true;
        tamano = 270;
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarSectoresEvaluaciones.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void eventoFiltrar() {
        try {
            System.out.println("\n ENTRE A ControlSectoresEvaluaciones.eventoFiltrar \n");
            if (tipoLista == 0) {
                tipoLista = 1;
            }
            RequestContext context = RequestContext.getCurrentInstance();
            infoRegistro = "Cantidad de registros: " + filtrarSectoresEvaluaciones.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
        } catch (Exception e) {
            System.out.println("ERROR ControlSectoresEvaluaciones eventoFiltrar ERROR===" + e.getMessage());
        }
    }

    public void cambiarIndice(int indice, int celda) {
        System.err.println("TIPO LISTA = " + tipoLista);

        if (permitirIndex == true) {
            index = indice;
            cualCelda = celda;
            secRegistro = listSectoresEvaluaciones.get(index).getSecuencia();
            if (cualCelda == 0) {
                if (tipoLista == 0) {
                    backUpCodigo = listSectoresEvaluaciones.get(index).getCodigo();
                } else {
                    backUpCodigo = filtrarSectoresEvaluaciones.get(index).getCodigo();
                }
            }
            if (cualCelda == 1) {
                if (tipoLista == 0) {
                    backUpDescripcion = listSectoresEvaluaciones.get(index).getDescripcion();
                } else {
                    backUpDescripcion = filtrarSectoresEvaluaciones.get(index).getDescripcion();
                }
            }

        }
        System.out.println("Indice: " + index + " Celda: " + cualCelda);
    }

    public void asignarIndex(Integer indice, int LND, int dig) {
        try {
            System.out.println("\n ENTRE A ControlSectoresEvaluaciones.asignarIndex \n");
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
            System.out.println("ERROR ControlSectoresEvaluaciones.asignarIndex ERROR======" + e.getMessage());
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void listaValoresBoton() {
    }

    public void cancelarModificacion() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 1) {
            //CERRAR FILTRADO
            codigo = (Column) c.getViewRoot().findComponent("form:datosSectoresEvaluaciones:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosSectoresEvaluaciones:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosSectoresEvaluaciones");
            bandera = 0;
            filtrarSectoresEvaluaciones = null;
            tipoLista = 0;
        }

        borrarSectoresEvaluaciones.clear();
        crearSectoresEvaluaciones.clear();
        modificarSectoresEvaluaciones.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listSectoresEvaluaciones = null;
        guardado = true;
        permitirIndex = true;
        getListSectoresEvaluaciones();
        RequestContext context = RequestContext.getCurrentInstance();
        if (listSectoresEvaluaciones == null || listSectoresEvaluaciones.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listSectoresEvaluaciones.size();
        }
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
        RequestContext.getCurrentInstance().update("form:datosSectoresEvaluaciones");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void salir() {
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            //CERRAR FILTRADO
            codigo = (Column) c.getViewRoot().findComponent("form:datosSectoresEvaluaciones:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosSectoresEvaluaciones:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosSectoresEvaluaciones");
            bandera = 0;
            filtrarSectoresEvaluaciones = null;
            tipoLista = 0;
        }

        borrarSectoresEvaluaciones.clear();
        crearSectoresEvaluaciones.clear();
        modificarSectoresEvaluaciones.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listSectoresEvaluaciones = null;
        guardado = true;
        permitirIndex = true;
        getListSectoresEvaluaciones();
        RequestContext context = RequestContext.getCurrentInstance();
        if (listSectoresEvaluaciones == null || listSectoresEvaluaciones.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listSectoresEvaluaciones.size();
        }
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
        RequestContext.getCurrentInstance().update("form:datosSectoresEvaluaciones");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            tamano = 250;
            codigo = (Column) c.getViewRoot().findComponent("form:datosSectoresEvaluaciones:codigo");
            codigo.setFilterStyle("width: 85%;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosSectoresEvaluaciones:descripcion");
            descripcion.setFilterStyle("width: 85%;");
            RequestContext.getCurrentInstance().update("form:datosSectoresEvaluaciones");
            System.out.println("Activar");
            bandera = 1;
        } else if (bandera == 1) {
            System.out.println("Desactivar");
            tamano = 270;
            codigo = (Column) c.getViewRoot().findComponent("form:datosSectoresEvaluaciones:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosSectoresEvaluaciones:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosSectoresEvaluaciones");
            bandera = 0;
            filtrarSectoresEvaluaciones = null;
            tipoLista = 0;
        }
    }

    public void modificarSectoresEvaluaciones(int indice, String confirmarCambio, String valorConfirmar) {
        System.err.println("ENTRE A MODIFICAR SUB CATEGORIA");
        index = indice;

        int contador = 0, pass = 0;
        Integer a;
        a = null;
        RequestContext context = RequestContext.getCurrentInstance();
        System.err.println("TIPO LISTA = " + tipoLista);
        if (confirmarCambio.equalsIgnoreCase("N")) {
            System.err.println("ENTRE A MODIFICAR EMPRESAS, CONFIRMAR CAMBIO ES N");
            if (tipoLista == 0) {
                if (!crearSectoresEvaluaciones.contains(listSectoresEvaluaciones.get(indice))) {
                    if (listSectoresEvaluaciones.get(indice).getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        listSectoresEvaluaciones.get(indice).setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < listSectoresEvaluaciones.size(); j++) {
                            if (j != indice) {
                                if (listSectoresEvaluaciones.get(indice).getCodigo() == listSectoresEvaluaciones.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            listSectoresEvaluaciones.get(indice).setCodigo(backUpCodigo);
                        } else {
                            pass++;
                        }

                    }
                    if (listSectoresEvaluaciones.get(indice).getDescripcion() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        listSectoresEvaluaciones.get(indice).setDescripcion(backUpDescripcion);
                    } else if (listSectoresEvaluaciones.get(indice).getDescripcion().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        listSectoresEvaluaciones.get(indice).setDescripcion(backUpDescripcion);

                    } else {
                        pass++;
                    }
                    if (pass == 2) {
                        if (modificarSectoresEvaluaciones.isEmpty()) {
                            modificarSectoresEvaluaciones.add(listSectoresEvaluaciones.get(indice));
                        } else if (!modificarSectoresEvaluaciones.contains(listSectoresEvaluaciones.get(indice))) {
                            modificarSectoresEvaluaciones.add(listSectoresEvaluaciones.get(indice));
                        }
                        if (guardado == true) {
                            guardado = false;
                        }

                    } else {
                        RequestContext.getCurrentInstance().update("form:validacionModificar");
                        PrimefacesContextUI.ejecutar("PF('validacionModificar').show()");
                    }
                    index = -1;
                    secRegistro = null;
                } else {
                    if (listSectoresEvaluaciones.get(indice).getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        listSectoresEvaluaciones.get(indice).setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < listSectoresEvaluaciones.size(); j++) {
                            if (j != indice) {
                                if (listSectoresEvaluaciones.get(indice).getCodigo() == listSectoresEvaluaciones.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            listSectoresEvaluaciones.get(indice).setCodigo(backUpCodigo);
                        } else {
                            pass++;
                        }

                    }
                    if (listSectoresEvaluaciones.get(indice).getDescripcion() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        listSectoresEvaluaciones.get(indice).setDescripcion(backUpDescripcion);
                    } else if (listSectoresEvaluaciones.get(indice).getDescripcion().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        listSectoresEvaluaciones.get(indice).setDescripcion(backUpDescripcion);

                    } else {
                        pass++;
                    }
                    if (pass == 2) {

                        if (guardado == true) {
                            guardado = false;
                        }

                    } else {
                        RequestContext.getCurrentInstance().update("form:validacionModificar");
                        PrimefacesContextUI.ejecutar("PF('validacionModificar').show()");
                    }
                    index = -1;
                    secRegistro = null;
                }
            } else {

                if (!crearSectoresEvaluaciones.contains(filtrarSectoresEvaluaciones.get(indice))) {
                    if (filtrarSectoresEvaluaciones.get(indice).getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        filtrarSectoresEvaluaciones.get(indice).setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < filtrarSectoresEvaluaciones.size(); j++) {
                            if (j != indice) {
                                if (filtrarSectoresEvaluaciones.get(indice).getCodigo() == filtrarSectoresEvaluaciones.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }
                        for (int j = 0; j < listSectoresEvaluaciones.size(); j++) {
                            if (j != indice) {
                                if (filtrarSectoresEvaluaciones.get(indice).getCodigo() == listSectoresEvaluaciones.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            filtrarSectoresEvaluaciones.get(indice).setCodigo(backUpCodigo);
                        } else {
                            pass++;
                        }

                    }

                    if (filtrarSectoresEvaluaciones.get(indice).getDescripcion() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        filtrarSectoresEvaluaciones.get(indice).setDescripcion(backUpDescripcion);
                    } else if (filtrarSectoresEvaluaciones.get(indice).getDescripcion().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        filtrarSectoresEvaluaciones.get(indice).setDescripcion(backUpDescripcion);
                    } else {
                        pass++;
                    }

                    if (pass == 2) {
                        if (modificarSectoresEvaluaciones.isEmpty()) {
                            modificarSectoresEvaluaciones.add(filtrarSectoresEvaluaciones.get(indice));
                        } else if (!modificarSectoresEvaluaciones.contains(filtrarSectoresEvaluaciones.get(indice))) {
                            modificarSectoresEvaluaciones.add(filtrarSectoresEvaluaciones.get(indice));
                        }
                        if (guardado == true) {
                            guardado = false;
                        }

                    } else {
                        RequestContext.getCurrentInstance().update("form:validacionModificar");
                        PrimefacesContextUI.ejecutar("PF('validacionModificar').show()");
                    }
                    index = -1;
                    secRegistro = null;
                } else {

                    if (filtrarSectoresEvaluaciones.get(indice).getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        filtrarSectoresEvaluaciones.get(indice).setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < filtrarSectoresEvaluaciones.size(); j++) {
                            if (j != indice) {
                                if (filtrarSectoresEvaluaciones.get(indice).getCodigo() == filtrarSectoresEvaluaciones.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }
                        for (int j = 0; j < listSectoresEvaluaciones.size(); j++) {
                            if (j != indice) {
                                if (filtrarSectoresEvaluaciones.get(indice).getCodigo() == listSectoresEvaluaciones.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            filtrarSectoresEvaluaciones.get(indice).setCodigo(backUpCodigo);
                        } else {
                            pass++;
                        }

                    }

                    if (filtrarSectoresEvaluaciones.get(indice).getDescripcion() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        filtrarSectoresEvaluaciones.get(indice).setDescripcion(backUpDescripcion);
                    } else if (filtrarSectoresEvaluaciones.get(indice).getDescripcion().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        filtrarSectoresEvaluaciones.get(indice).setDescripcion(backUpDescripcion);
                    } else {
                        pass++;
                    }

                    if (pass == 2) {

                        if (guardado == true) {
                            guardado = false;
                        }

                    } else {
                        RequestContext.getCurrentInstance().update("form:validacionModificar");
                        PrimefacesContextUI.ejecutar("PF('validacionModificar').show()");
                    }
                    index = -1;
                    secRegistro = null;

                }

            }
            RequestContext.getCurrentInstance().update("form:datosSectoresEvaluaciones");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }

    }

    public void borrandoSectoresEvaluaciones() {

        if (index >= 0) {
            if (tipoLista == 0) {
                System.out.println("Entro a borrandoSectoresEvaluaciones");
                if (!modificarSectoresEvaluaciones.isEmpty() && modificarSectoresEvaluaciones.contains(listSectoresEvaluaciones.get(index))) {
                    int modIndex = modificarSectoresEvaluaciones.indexOf(listSectoresEvaluaciones.get(index));
                    modificarSectoresEvaluaciones.remove(modIndex);
                    borrarSectoresEvaluaciones.add(listSectoresEvaluaciones.get(index));
                } else if (!crearSectoresEvaluaciones.isEmpty() && crearSectoresEvaluaciones.contains(listSectoresEvaluaciones.get(index))) {
                    int crearIndex = crearSectoresEvaluaciones.indexOf(listSectoresEvaluaciones.get(index));
                    crearSectoresEvaluaciones.remove(crearIndex);
                } else {
                    borrarSectoresEvaluaciones.add(listSectoresEvaluaciones.get(index));
                }
                listSectoresEvaluaciones.remove(index);
            }
            if (tipoLista == 1) {
                System.out.println("borrandoSectoresEvaluaciones ");
                if (!modificarSectoresEvaluaciones.isEmpty() && modificarSectoresEvaluaciones.contains(filtrarSectoresEvaluaciones.get(index))) {
                    int modIndex = modificarSectoresEvaluaciones.indexOf(filtrarSectoresEvaluaciones.get(index));
                    modificarSectoresEvaluaciones.remove(modIndex);
                    borrarSectoresEvaluaciones.add(filtrarSectoresEvaluaciones.get(index));
                } else if (!crearSectoresEvaluaciones.isEmpty() && crearSectoresEvaluaciones.contains(filtrarSectoresEvaluaciones.get(index))) {
                    int crearIndex = crearSectoresEvaluaciones.indexOf(filtrarSectoresEvaluaciones.get(index));
                    crearSectoresEvaluaciones.remove(crearIndex);
                } else {
                    borrarSectoresEvaluaciones.add(filtrarSectoresEvaluaciones.get(index));
                }
                int VCIndex = listSectoresEvaluaciones.indexOf(filtrarSectoresEvaluaciones.get(index));
                listSectoresEvaluaciones.remove(VCIndex);
                filtrarSectoresEvaluaciones.remove(index);

            }
            RequestContext context = RequestContext.getCurrentInstance();
            if (listSectoresEvaluaciones == null || listSectoresEvaluaciones.isEmpty()) {
                infoRegistro = "Cantidad de registros: 0 ";
            } else {
                infoRegistro = "Cantidad de registros: " + listSectoresEvaluaciones.size();
            }
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            RequestContext.getCurrentInstance().update("form:datosSectoresEvaluaciones");
            index = -1;
            secRegistro = null;

            if (guardado == true) {
                guardado = false;
            }
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }

    }

    public void revisarDialogoGuardar() {

        if (!borrarSectoresEvaluaciones.isEmpty() || !crearSectoresEvaluaciones.isEmpty() || !modificarSectoresEvaluaciones.isEmpty()) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:confirmarGuardar");
            PrimefacesContextUI.ejecutar("PF('confirmarGuardar').show()");
        }

    }

    public void guardarSectoresEvaluaciones() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (guardado == false) {
            System.out.println("Realizando guardarSectoresEvaluaciones");
            if (!borrarSectoresEvaluaciones.isEmpty()) {
                administrarSectoresEvaluaciones.borrarSectoresEvaluaciones(borrarSectoresEvaluaciones);
                //mostrarBorrados
                registrosBorrados = borrarSectoresEvaluaciones.size();
                RequestContext.getCurrentInstance().update("form:mostrarBorrados");
                PrimefacesContextUI.ejecutar("PF('mostrarBorrados').show()");
                borrarSectoresEvaluaciones.clear();
            }
            if (!modificarSectoresEvaluaciones.isEmpty()) {
                administrarSectoresEvaluaciones.modificarSectoresEvaluaciones(modificarSectoresEvaluaciones);
                modificarSectoresEvaluaciones.clear();
            }
            if (!crearSectoresEvaluaciones.isEmpty()) {
                administrarSectoresEvaluaciones.crearSectoresEvaluaciones(crearSectoresEvaluaciones);
                crearSectoresEvaluaciones.clear();
            }
            System.out.println("Se guardaron los datos con exito");
            listSectoresEvaluaciones = null;
            FacesMessage msg = new FacesMessage("Información", "Se gurdarón los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            RequestContext.getCurrentInstance().update("form:datosSectoresEvaluaciones");
            k = 0;
            guardado = true;
        }
        index = -1;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");

    }

    public void editarCelda() {
        if (index >= 0) {
            if (tipoLista == 0) {
                editarSectoresEvaluaciones = listSectoresEvaluaciones.get(index);
            }
            if (tipoLista == 1) {
                editarSectoresEvaluaciones = filtrarSectoresEvaluaciones.get(index);
            }

            RequestContext context = RequestContext.getCurrentInstance();
            System.out.println("Entro a editar... valor celda: " + cualCelda);
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editCodigo");
                PrimefacesContextUI.ejecutar("PF('editCodigo').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editDescripcion");
                PrimefacesContextUI.ejecutar("PF('editDescripcion').show()");
                cualCelda = -1;
            }

        }
        index = -1;
        secRegistro = null;
    }

    public void agregarNuevoSectoresEvaluaciones() {
        System.out.println("agregarNuevoSectoresEvaluaciones");
        int contador = 0;
        int duplicados = 0;
        Integer a;
        a = null;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        if (nuevoSectoresEvaluaciones.getCodigo() == a) {
            mensajeValidacion = " *Código \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else {
            System.out.println("codigo en Motivo Cambio Cargo: " + nuevoSectoresEvaluaciones.getCodigo());

            for (int x = 0; x < listSectoresEvaluaciones.size(); x++) {
                if (listSectoresEvaluaciones.get(x).getCodigo() == nuevoSectoresEvaluaciones.getCodigo()) {
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
        if (nuevoSectoresEvaluaciones.getDescripcion() == null) {
            mensajeValidacion = mensajeValidacion + " *Descripción \n";
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
                codigo = (Column) c.getViewRoot().findComponent("form:datosSectoresEvaluaciones:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosSectoresEvaluaciones:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosSectoresEvaluaciones");
                bandera = 0;
                filtrarSectoresEvaluaciones = null;
                tipoLista = 0;
            }
            System.out.println("Despues de la bandera");

            k++;
            l = BigInteger.valueOf(k);
            nuevoSectoresEvaluaciones.setSecuencia(l);

            crearSectoresEvaluaciones.add(nuevoSectoresEvaluaciones);

            listSectoresEvaluaciones.add(nuevoSectoresEvaluaciones);
            nuevoSectoresEvaluaciones = new SectoresEvaluaciones();
            RequestContext.getCurrentInstance().update("form:datosSectoresEvaluaciones");

            infoRegistro = "Cantidad de registros: " + listSectoresEvaluaciones.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }

            PrimefacesContextUI.ejecutar("PF('nuevoRegistroSectoresEvaluaciones').hide()");
            index = -1;
            secRegistro = null;

        } else {
            RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
            PrimefacesContextUI.ejecutar("PF('validacionNuevaCentroCosto').show()");
        }
    }

    public void limpiarNuevoSectoresEvaluaciones() {
        System.out.println("limpiarNuevoSectoresEvaluaciones");
        nuevoSectoresEvaluaciones = new SectoresEvaluaciones();
        secRegistro = null;
        index = -1;

    }

    //------------------------------------------------------------------------------
    public void duplicandoSectoresEvaluaciones() {
        System.out.println("duplicandoSectoresEvaluaciones");
        if (index >= 0) {
            duplicarSectoresEvaluaciones = new SectoresEvaluaciones();
            k++;
            l = BigInteger.valueOf(k);

            if (tipoLista == 0) {
                duplicarSectoresEvaluaciones.setSecuencia(l);
                duplicarSectoresEvaluaciones.setCodigo(listSectoresEvaluaciones.get(index).getCodigo());
                duplicarSectoresEvaluaciones.setDescripcion(listSectoresEvaluaciones.get(index).getDescripcion());
            }
            if (tipoLista == 1) {
                duplicarSectoresEvaluaciones.setSecuencia(l);
                duplicarSectoresEvaluaciones.setCodigo(filtrarSectoresEvaluaciones.get(index).getCodigo());
                duplicarSectoresEvaluaciones.setDescripcion(filtrarSectoresEvaluaciones.get(index).getDescripcion());
            }

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTE");
            PrimefacesContextUI.ejecutar("PF('duplicarRegistroSectoresEvaluaciones').show()");
            index = -1;
            secRegistro = null;
        }
    }

    public void confirmarDuplicar() {
        System.err.println("ESTOY EN CONFIRMAR DUPLICAR TIPOS EMPRESAS");
        int contador = 0;
        mensajeValidacion = " ";
        int duplicados = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        Integer a;
        a = null;
        System.err.println("ConfirmarDuplicar codigo " + duplicarSectoresEvaluaciones.getCodigo());
        System.err.println("ConfirmarDuplicar Descripcion " + duplicarSectoresEvaluaciones.getDescripcion());

        if (duplicarSectoresEvaluaciones.getCodigo() == a) {
            mensajeValidacion = mensajeValidacion + "   *Codigo \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else {
            for (int x = 0; x < listSectoresEvaluaciones.size(); x++) {
                if (listSectoresEvaluaciones.get(x).getCodigo() == duplicarSectoresEvaluaciones.getCodigo()) {
                    duplicados++;
                }
            }
            if (duplicados > 0) {
                mensajeValidacion = " *Que NO Existan Codigo Repetidos \n";
                System.out.println("Mensaje validacion : " + mensajeValidacion);
            } else {
                System.out.println("bandera");
                contador++;
            }
        }
        if (duplicarSectoresEvaluaciones.getDescripcion() == null) {
            mensajeValidacion = mensajeValidacion + "   *Descripcion \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("Bandera : ");
            contador++;
        }

        if (contador == 2) {

            System.out.println("Datos Duplicando: " + duplicarSectoresEvaluaciones.getSecuencia() + "  " + duplicarSectoresEvaluaciones.getCodigo());
            if (crearSectoresEvaluaciones.contains(duplicarSectoresEvaluaciones)) {
                System.out.println("Ya lo contengo.");
            }
            listSectoresEvaluaciones.add(duplicarSectoresEvaluaciones);
            crearSectoresEvaluaciones.add(duplicarSectoresEvaluaciones);
            RequestContext.getCurrentInstance().update("form:datosSectoresEvaluaciones");
            index = -1;
            secRegistro = null;
            if (guardado == true) {
                guardado = false;
            }
            infoRegistro = "Cantidad de registros: " + listSectoresEvaluaciones.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                codigo = (Column) c.getViewRoot().findComponent("form:datosSectoresEvaluaciones:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosSectoresEvaluaciones:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosSectoresEvaluaciones");
                bandera = 0;
                filtrarSectoresEvaluaciones = null;
                tipoLista = 0;
            }
            duplicarSectoresEvaluaciones = new SectoresEvaluaciones();
            PrimefacesContextUI.ejecutar("PF('duplicarRegistroSectoresEvaluaciones').hide()");

        } else {
            RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
            PrimefacesContextUI.ejecutar("PF('validacionDuplicarVigencia').show()");
        }
    }

    public void limpiarDuplicarSectoresEvaluaciones() {
        duplicarSectoresEvaluaciones = new SectoresEvaluaciones();
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosSectoresEvaluacionesExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "SECTORESEVALUACIONES", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosSectoresEvaluacionesExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "SECTORESEVALUACIONES", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        System.out.println("lol");
        if (!listSectoresEvaluaciones.isEmpty()) {
            if (secRegistro != null) {
                System.out.println("lol 2");
                int resultado = administrarRastros.obtenerTabla(secRegistro, "SECTORESEVALUACIONES"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
            if (administrarRastros.verificarHistoricosTabla("SECTORESEVALUACIONES")) { // igual acá
                PrimefacesContextUI.ejecutar("PF('confirmarRastroHistorico').show()");
            } else {
                PrimefacesContextUI.ejecutar("PF('errorRastroHistorico').show()");
            }

        }
        index = -1;
    }

    //*/*/*/*/*/*/*/*/*/*-/-*//-*/-*/*/*-*/-*/-*/*/*/*/*/---/*/*/*/*/-*/-*/-*/-*/-*/
    public List<SectoresEvaluaciones> getListSectoresEvaluaciones() {
        if (listSectoresEvaluaciones == null) {
            listSectoresEvaluaciones = administrarSectoresEvaluaciones.consultarSectoresEvaluaciones();
        }
        RequestContext context = RequestContext.getCurrentInstance();
        if (listSectoresEvaluaciones == null || listSectoresEvaluaciones.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listSectoresEvaluaciones.size();
        }
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
        return listSectoresEvaluaciones;
    }

    public void setListSectoresEvaluaciones(List<SectoresEvaluaciones> listSectoresEvaluaciones) {
        this.listSectoresEvaluaciones = listSectoresEvaluaciones;
    }

    public List<SectoresEvaluaciones> getFiltrarSectoresEvaluaciones() {
        return filtrarSectoresEvaluaciones;
    }

    public void setFiltrarSectoresEvaluaciones(List<SectoresEvaluaciones> filtrarSectoresEvaluaciones) {
        this.filtrarSectoresEvaluaciones = filtrarSectoresEvaluaciones;
    }

    public SectoresEvaluaciones getNuevoSectoresEvaluaciones() {
        return nuevoSectoresEvaluaciones;
    }

    public void setNuevoSectoresEvaluaciones(SectoresEvaluaciones nuevoSectoresEvaluaciones) {
        this.nuevoSectoresEvaluaciones = nuevoSectoresEvaluaciones;
    }

    public SectoresEvaluaciones getDuplicarSectoresEvaluaciones() {
        return duplicarSectoresEvaluaciones;
    }

    public void setDuplicarSectoresEvaluaciones(SectoresEvaluaciones duplicarSectoresEvaluaciones) {
        this.duplicarSectoresEvaluaciones = duplicarSectoresEvaluaciones;
    }

    public SectoresEvaluaciones getEditarSectoresEvaluaciones() {
        return editarSectoresEvaluaciones;
    }

    public void setEditarSectoresEvaluaciones(SectoresEvaluaciones editarSectoresEvaluaciones) {
        this.editarSectoresEvaluaciones = editarSectoresEvaluaciones;
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

    public SectoresEvaluaciones getSectoresEvaluacionesSeleccionada() {
        return sectoresEvaluacionesSeleccionada;
    }

    public void setSectoresEvaluacionesSeleccionada(SectoresEvaluaciones sectoresEvaluacionesSeleccionada) {
        this.sectoresEvaluacionesSeleccionada = sectoresEvaluacionesSeleccionada;
    }

    public String getInfoRegistro() {
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

}
