/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Motivosmvrs;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarMotivosMvrsInterface;
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
public class ControlMotivosMvrs implements Serializable {

    @EJB
    AdministrarMotivosMvrsInterface administrarMotivosMvrs;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    private List<Motivosmvrs> listMotivosMvrs;
    private List<Motivosmvrs> filtrarMotivosMvrs;
    private List<Motivosmvrs> crearMotivoMvrs;
    private List<Motivosmvrs> modificarMotivoMvrs;
    private List<Motivosmvrs> borrarMotivoMvrs;
    private Motivosmvrs nuevoMotivoMvr;
    private Motivosmvrs duplicarMotivosMvrs;
    private Motivosmvrs editarMotivosMvrs;
    private Motivosmvrs motivoMvrSeleccionada;
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
    private Long borradoVC;
    private Integer backUpCodigo;
    private String backUpDescripcion;
    private int tamano;
    private String infoRegistro;
    private String paginaAnterior;

    public ControlMotivosMvrs() {
        listMotivosMvrs = null;
        crearMotivoMvrs = new ArrayList<Motivosmvrs>();
        modificarMotivoMvrs = new ArrayList<Motivosmvrs>();
        borrarMotivoMvrs = new ArrayList<Motivosmvrs>();
        permitirIndex = true;
        editarMotivosMvrs = new Motivosmvrs();
        nuevoMotivoMvr = new Motivosmvrs();
        duplicarMotivosMvrs = new Motivosmvrs();
        tamano = 270;
        guardado = true;
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarMotivosMvrs.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void recibirPaginaAnterior(String pagina) {
        paginaAnterior = pagina;
        System.out.println("ControlMotivosMvrs recibirPaginaAnterior pagina : " + paginaAnterior);
    }

    public String redireccionarPaginaAnterior() {
        System.out.println("ControlMotivosMvrs redireccionarPaginaAnterior pagina : " + paginaAnterior);
        return paginaAnterior;
    }

    public void eventoFiltrar() {
        try {
            System.out.println("\n ENTRE A ControlNormasLaborales.eventoFiltrar \n");
            if (tipoLista == 0) {
                tipoLista = 1;
            }
            RequestContext context = RequestContext.getCurrentInstance();
            infoRegistro = "Cantidad de registros: " + filtrarMotivosMvrs.size();
            context.update("form:informacionRegistro");
        } catch (Exception e) {
            System.out.println("ERROR ControlNormasLaborales eventoFiltrar ERROR===" + e.getMessage());
        }
    }

    public void cambiarIndice(int indice, int celda) {
        System.err.println("TIPO LISTA = " + tipoLista);

        if (permitirIndex == true) {
            index = indice;
            cualCelda = celda;
            if (cualCelda == 0) {
                if (tipoLista == 0) {
                    backUpCodigo = listMotivosMvrs.get(index).getCodigo();
                } else {
                    backUpCodigo = filtrarMotivosMvrs.get(index).getCodigo();
                }
                System.out.println("BACKUPCODIGO " + backUpCodigo);
            }
            if (cualCelda == 1) {
                if (tipoLista == 0) {
                    backUpDescripcion = listMotivosMvrs.get(index).getNombre();
                } else {
                    backUpDescripcion = filtrarMotivosMvrs.get(index).getNombre();
                }
                System.out.println("BACKUPDESCRIPCION " + backUpDescripcion);
            }
            secRegistro = listMotivosMvrs.get(index).getSecuencia();
        }
        System.out.println("Indice: " + index + " Celda: " + cualCelda);
    }

    public void asignarIndex(Integer indice, int LND, int dig) {
        try {
            System.out.println("\n ENTRE A ControlNormasLaborales.asignarIndex \n");
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
            System.out.println("ERROR ControlNormasLaborales.asignarIndex ERROR======" + e.getMessage());
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void listaValoresBoton() {
    }

    public void cancelarModificacion() {
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            //CERRAR FILTRADO
            codigo = (Column) c.getViewRoot().findComponent("form:datosMotivoMvr:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosMotivoMvr:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosMotivoMvr");
            bandera = 0;
            filtrarMotivosMvrs = null;
            tipoLista = 0;
        }

        borrarMotivoMvrs.clear();
        crearMotivoMvrs.clear();
        modificarMotivoMvrs.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listMotivosMvrs = null;
        guardado = true;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        if (listMotivosMvrs == null || listMotivosMvrs.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listMotivosMvrs.size();
        }
        context.update("form:informacionRegistro");
        context.update("form:datosMotivoMvr");
        context.update("form:ACEPTAR");
    }

    public void salir() {
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            //CERRAR FILTRADO
            codigo = (Column) c.getViewRoot().findComponent("form:datosMotivoMvr:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosMotivoMvr:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosMotivoMvr");
            bandera = 0;
            filtrarMotivosMvrs = null;
            tipoLista = 0;
        }

        borrarMotivoMvrs.clear();
        crearMotivoMvrs.clear();
        modificarMotivoMvrs.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listMotivosMvrs = null;
        guardado = true;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        if (listMotivosMvrs == null || listMotivosMvrs.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listMotivosMvrs.size();
        }
        context.update("form:informacionRegistro");
        context.update("form:datosMotivoMvr");
        context.update("form:ACEPTAR");
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            tamano = 250;
            codigo = (Column) c.getViewRoot().findComponent("form:datosMotivoMvr:codigo");
            codigo.setFilterStyle("width: 85%;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosMotivoMvr:descripcion");
            descripcion.setFilterStyle("width: 85%;");
            RequestContext.getCurrentInstance().update("form:datosMotivoMvr");
            System.out.println("Activar");
            bandera = 1;
        } else if (bandera == 1) {
            tamano = 270;
            System.out.println("Desactivar");
            codigo = (Column) c.getViewRoot().findComponent("form:datosMotivoMvr:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosMotivoMvr:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosMotivoMvr");
            bandera = 0;
            filtrarMotivosMvrs = null;
            tipoLista = 0;
        }
    }

    public void modificarMotivosMvrs(int indice, String confirmarCambio, String valorConfirmar) {
        System.err.println("ENTRE A MODIFICAR Motivos Mvrs");
        index = indice;

        int contador = 0;
        boolean banderita = false;
        Integer a;
        a = null;
        RequestContext context = RequestContext.getCurrentInstance();
        System.err.println("TIPO LISTA = " + tipoLista + " INDICE : " + indice);
        if (confirmarCambio.equalsIgnoreCase("N")) {
            System.err.println("ENTRE A MODIFICAR Motivos Mvrs, CONFIRMAR CAMBIO ES N");
            if (tipoLista == 0) {
                if (!crearMotivoMvrs.contains(listMotivosMvrs.get(indice))) {
                    if (listMotivosMvrs.get(indice).getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listMotivosMvrs.get(indice).setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < listMotivosMvrs.size(); j++) {
                            if (j != indice) {
                                if (listMotivosMvrs.get(indice).getCodigo().equals(listMotivosMvrs.get(j).getCodigo())) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            listMotivosMvrs.get(indice).setCodigo(backUpCodigo);
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            banderita = false;
                        } else {
                            banderita = true;
                        }

                    }
                    if (listMotivosMvrs.get(indice).getNombre()==null) {
                        listMotivosMvrs.get(indice).setNombre(backUpDescripcion);
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                    }
                    if (listMotivosMvrs.get(indice).getNombre().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        listMotivosMvrs.get(indice).setNombre(backUpDescripcion);
                        banderita = false;
                    }

                    if (banderita == true) {
                        if (modificarMotivoMvrs.isEmpty()) {
                            modificarMotivoMvrs.add(listMotivosMvrs.get(indice));
                        } else if (!modificarMotivoMvrs.contains(listMotivosMvrs.get(indice))) {
                            modificarMotivoMvrs.add(listMotivosMvrs.get(indice));
                        }
                        if (guardado == true) {
                            guardado = false;
                            context.update("form:ACEPTAR");
                        }

                    } else {
                        context.update("form:validacionModificar");
                        context.execute("validacionModificar.show()");
                    }
                    index = -1;
                    secRegistro = null;
                } else {
                    if (listMotivosMvrs.get(indice).getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listMotivosMvrs.get(indice).setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < listMotivosMvrs.size(); j++) {
                            if (j != indice) {
                                if (listMotivosMvrs.get(indice).getCodigo().equals(listMotivosMvrs.get(j).getCodigo())) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            listMotivosMvrs.get(indice).setCodigo(backUpCodigo);
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            banderita = false;
                        } else {
                            banderita = true;
                        }

                    }
                    if (listMotivosMvrs.get(indice).getNombre()==null) {
                        listMotivosMvrs.get(indice).setNombre(backUpDescripcion);
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                    }
                    if (listMotivosMvrs.get(indice).getNombre().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        listMotivosMvrs.get(indice).setNombre(backUpDescripcion);
                        banderita = false;
                    }

                    if (banderita == true) {
                        if (guardado == true) {
                            guardado = false;
                            context.update("form:ACEPTAR");
                        }

                    } else {
                        context.update("form:validacionModificar");
                        context.execute("validacionModificar.show()");
                    }
                    index = -1;
                    secRegistro = null;
                }
            } else {

                if (!crearMotivoMvrs.contains(filtrarMotivosMvrs.get(indice))) {
                    if (filtrarMotivosMvrs.get(indice).getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        filtrarMotivosMvrs.get(indice).setCodigo(backUpCodigo);
                        banderita = false;
                    } else {
                        for (int j = 0; j < filtrarMotivosMvrs.size(); j++) {
                            if (j != indice) {
                                if (filtrarMotivosMvrs.get(indice).getCodigo().equals(filtrarMotivosMvrs.get(j).getCodigo())) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            filtrarMotivosMvrs.get(indice).setCodigo(backUpCodigo);
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            banderita = false;
                        } else {
                            banderita = true;
                        }

                    }

                    if (filtrarMotivosMvrs.get(indice).getNombre()==null) {
                        filtrarMotivosMvrs.get(indice).setNombre(backUpDescripcion);
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                    }
                    if (filtrarMotivosMvrs.get(indice).getNombre().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        filtrarMotivosMvrs.get(indice).setNombre(backUpDescripcion);
                        banderita = false;
                    }

                    if (banderita == true) {
                        if (modificarMotivoMvrs.isEmpty()) {
                            modificarMotivoMvrs.add(filtrarMotivosMvrs.get(indice));
                        } else if (!modificarMotivoMvrs.contains(filtrarMotivosMvrs.get(indice))) {
                            modificarMotivoMvrs.add(filtrarMotivosMvrs.get(indice));
                        }
                        if (guardado == true) {
                            guardado = false;
                            context.update("form:ACEPTAR");
                        }

                    } else {
                        context.update("form:validacionModificar");
                        context.execute("validacionModificar.show()");
                    }
                    index = -1;
                    secRegistro = null;
                } else {
                    if (filtrarMotivosMvrs.get(indice).getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        filtrarMotivosMvrs.get(indice).setCodigo(backUpCodigo);
                        banderita = false;
                    } else {
                        for (int j = 0; j < filtrarMotivosMvrs.size(); j++) {
                            if (j != indice) {
                                if (filtrarMotivosMvrs.get(indice).getCodigo().equals(filtrarMotivosMvrs.get(j).getCodigo())) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            filtrarMotivosMvrs.get(indice).setCodigo(backUpCodigo);
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            banderita = false;
                        } else {
                            banderita = true;
                        }

                    }
                    if (filtrarMotivosMvrs.get(indice).getNombre()==null) {
                        filtrarMotivosMvrs.get(indice).setNombre(backUpDescripcion);
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                    }

                    if (filtrarMotivosMvrs.get(indice).getNombre().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        filtrarMotivosMvrs.get(indice).setNombre(backUpDescripcion);
                        banderita = false;
                    }

                    if (banderita == true) {

                        if (guardado == true) {
                            guardado = false;
                            context.update("form:ACEPTAR");
                        }

                    } else {
                        context.update("form:validacionModificar");
                        context.execute("validacionModificar.show()");
                    }
                    index = -1;
                    secRegistro = null;
                }

            }
            context.update("form:datosMotivoMvr");
        }

    }

    public void borrarMotivosMvrs() {

        if (index >= 0) {

            if (tipoLista == 0) {
                System.out.println("Entro a borrarNormasLaborales");
                if (!modificarMotivoMvrs.isEmpty() && modificarMotivoMvrs.contains(listMotivosMvrs.get(index))) {
                    int modIndex = modificarMotivoMvrs.indexOf(listMotivosMvrs.get(index));
                    modificarMotivoMvrs.remove(modIndex);
                    borrarMotivoMvrs.add(listMotivosMvrs.get(index));
                } else if (!crearMotivoMvrs.isEmpty() && crearMotivoMvrs.contains(listMotivosMvrs.get(index))) {
                    int crearIndex = crearMotivoMvrs.indexOf(listMotivosMvrs.get(index));
                    crearMotivoMvrs.remove(crearIndex);
                } else {
                    borrarMotivoMvrs.add(listMotivosMvrs.get(index));
                }
                listMotivosMvrs.remove(index);
            }
            if (tipoLista == 1) {
                System.out.println("borrarMotivosMvrs ");
                if (!modificarMotivoMvrs.isEmpty() && modificarMotivoMvrs.contains(filtrarMotivosMvrs.get(index))) {
                    int modIndex = modificarMotivoMvrs.indexOf(filtrarMotivosMvrs.get(index));
                    modificarMotivoMvrs.remove(modIndex);
                    borrarMotivoMvrs.add(filtrarMotivosMvrs.get(index));
                } else if (!crearMotivoMvrs.isEmpty() && crearMotivoMvrs.contains(filtrarMotivosMvrs.get(index))) {
                    int crearIndex = crearMotivoMvrs.indexOf(filtrarMotivosMvrs.get(index));
                    crearMotivoMvrs.remove(crearIndex);
                } else {
                    borrarMotivoMvrs.add(filtrarMotivosMvrs.get(index));
                }
                int VCIndex = listMotivosMvrs.indexOf(filtrarMotivosMvrs.get(index));
                listMotivosMvrs.remove(VCIndex);
                filtrarMotivosMvrs.remove(index);

            }
            RequestContext context = RequestContext.getCurrentInstance();
            infoRegistro = "Cantidad de registros: " + listMotivosMvrs.size();
            context.update("form:informacionRegistro");
            context.update("form:datosMotivoMvr");
            index = -1;
            secRegistro = null;

            if (guardado == true) {
                guardado = false;
                context.update("form:ACEPTAR");
            }
        }

    }

    /* public void verificarBorrado() {
     System.out.println("Estoy en verificarBorrado");
     try {
     borradoVC = administrarNormasLaborales.verificarBorradoVNE(listNormasLaborales.get(index).getSecuencia());
     if (borradoVC.intValue() == 0) {
     System.out.println("Borrado==0");
     borrarNormasLaborales();
     }
     if (borradoVC.intValue() != 0) {
     System.out.println("Borrado>0");

     RequestContext context = RequestContext.getCurrentInstance();
     context.update("form:validacionBorrar");
     context.execute("validacionBorrar.show()");
     index = -1;
     borradoVC = new Long(-1);
     }

     } catch (Exception e) {
     System.err.println("ERROR ControlNormasLaborales verificarBorrado ERROR " + e);
     }
     }
     */
    public void guardarMotivosMvrs() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (guardado == false) {
            System.out.println("Realizando Motivos Mvrs");
            if (!borrarMotivoMvrs.isEmpty()) {
                administrarMotivosMvrs.borrarMotivosMvrs(borrarMotivoMvrs);
                //mostrarBorrados
                registrosBorrados = borrarMotivoMvrs.size();
                context.update("form:mostrarBorrados");
                context.execute("mostrarBorrados.show()");
                borrarMotivoMvrs.clear();
            }
            if (!crearMotivoMvrs.isEmpty()) {
                administrarMotivosMvrs.crearMotivosMvrs(crearMotivoMvrs);
                crearMotivoMvrs.clear();
            }
            if (!modificarMotivoMvrs.isEmpty()) {
                administrarMotivosMvrs.modificarMotivosMvrs(modificarMotivoMvrs);
                modificarMotivoMvrs.clear();
            }
            System.out.println("Se guardaron los datos con exito");
            listMotivosMvrs = null;
            context.update("form:datosMotivoMvr");
            k = 0;
            FacesMessage msg = new FacesMessage("Información", "Se gurdarón los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            context.update("form:growl");
            guardado = true;
        }
        index = -1;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");

    }

    public void editarCelda() {
        if (index >= 0) {
            if (tipoLista == 0) {
                editarMotivosMvrs = listMotivosMvrs.get(index);
            }
            if (tipoLista == 1) {
                editarMotivosMvrs = filtrarMotivosMvrs.get(index);
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

    public void agregarNuevoMotivoMvrs() {
        System.out.println("Agregar Norma Laboral");
        int contador = 0;
        int duplicados = 0;

        Integer a = 0;
        a = null;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        if (nuevoMotivoMvr.getCodigo() == a) {
            mensajeValidacion = " *Codigo \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else {
            System.out.println("codigo en Motivo Cambio Cargo: " + nuevoMotivoMvr.getCodigo());

            for (int x = 0; x < listMotivosMvrs.size(); x++) {
                if (listMotivosMvrs.get(x).getCodigo() == nuevoMotivoMvr.getCodigo()) {
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
        if (nuevoMotivoMvr.getNombre() == (null) || nuevoMotivoMvr.getNombre().equals(" ") || nuevoMotivoMvr.getNombre().isEmpty()) {
            mensajeValidacion = mensajeValidacion + " *Descripcion \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("bandera");
            contador++;

        }

        System.out.println("contador " + contador);

        if (contador == 2) {
            FacesContext c = FacesContext.getCurrentInstance();
            if (bandera == 1) {
                //CERRAR FILTRADO
                System.out.println("Desactivar");
                codigo = (Column) c.getViewRoot().findComponent("form:datosMotivoMvr:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosMotivoMvr:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosMotivoMvr");
                bandera = 0;
                filtrarMotivosMvrs = null;
                tipoLista = 0;
            }
            System.out.println("Despues de la bandera");

            k++;
            l = BigInteger.valueOf(k);
            nuevoMotivoMvr.setSecuencia(l);

            crearMotivoMvrs.add(nuevoMotivoMvr);

            listMotivosMvrs.add(nuevoMotivoMvr);
            nuevoMotivoMvr = new Motivosmvrs();
            infoRegistro = "Cantidad de registros: " + listMotivosMvrs.size();
            context.update("form:informacionRegistro");
            context.update("form:datosMotivoMvr");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }

            context.execute("nuevoRegistroMotivoMvrs.hide()");
            index = -1;
            secRegistro = null;

        } else {
            context.update("form:validacionNuevaCentroCosto");
            context.execute("validacionNuevaCentroCosto.show()");
            contador = 0;
        }
    }

    public void limpiarNuevoMotivoMvrs() {
        System.out.println("limpiarNuevoMotivoMvrs");
        nuevoMotivoMvr = new Motivosmvrs();
        secRegistro = null;
        index = -1;

    }

    //------------------------------------------------------------------------------
    public void duplicarMotivoMvrs() {
        System.out.println("duplicarMotivoMvr");
        if (index >= 0) {
            duplicarMotivosMvrs = new Motivosmvrs();
            k++;
            l = BigInteger.valueOf(k);

            if (tipoLista == 0) {
                duplicarMotivosMvrs.setSecuencia(l);
                duplicarMotivosMvrs.setCodigo(listMotivosMvrs.get(index).getCodigo());
                duplicarMotivosMvrs.setNombre(listMotivosMvrs.get(index).getNombre());
            }
            if (tipoLista == 1) {
                duplicarMotivosMvrs.setSecuencia(l);
                duplicarMotivosMvrs.setCodigo(filtrarMotivosMvrs.get(index).getCodigo());
                duplicarMotivosMvrs.setNombre(filtrarMotivosMvrs.get(index).getNombre());
            }

            RequestContext context = RequestContext.getCurrentInstance();
            context.update("formularioDialogos:duplicarMotivoMvr");
            context.execute("duplicarRegistroMotivosMvr.show()");
            index = -1;
            secRegistro = null;
        }
    }

    public void confirmarDuplicar() {
        System.err.println("ESTOY EN CONFIRMAR DUPLICAR CONTROLMOTIVOMVRS");
        int contador = 0;
        mensajeValidacion = " ";
        int duplicados = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        Integer a = 0;
        a = null;
        System.err.println("ConfirmarDuplicar codigo " + duplicarMotivosMvrs.getCodigo());
        System.err.println("ConfirmarDuplicar Descripcion " + duplicarMotivosMvrs.getNombre());

        if (duplicarMotivosMvrs.getCodigo() == a) {
            mensajeValidacion = mensajeValidacion + "   *Codigo \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else {
            for (int x = 0; x < listMotivosMvrs.size(); x++) {
                if (listMotivosMvrs.get(x).getCodigo() == duplicarMotivosMvrs.getCodigo()) {
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
        if (duplicarMotivosMvrs.getNombre() == (null) || duplicarMotivosMvrs.getNombre().isEmpty()) {
            mensajeValidacion = mensajeValidacion + "   *Descripcion \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("Bandera : ");
            contador++;
        }

        if (contador == 2) {

            System.out.println("Datos Duplicando: " + duplicarMotivosMvrs.getSecuencia() + "  " + duplicarMotivosMvrs.getCodigo());
            if (crearMotivoMvrs.contains(duplicarMotivosMvrs)) {
                System.out.println("Ya lo contengo.");
            }
            listMotivosMvrs.add(duplicarMotivosMvrs);
            crearMotivoMvrs.add(duplicarMotivosMvrs);
            context.update("form:datosMotivoMvr");
            index = -1;
            secRegistro = null;
            if (guardado == true) {
                guardado = false;
                context.update("form:ACEPTAR");
            }
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                codigo = (Column) c.getViewRoot().findComponent("form:datosMotivoMvr:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosMotivoMvr:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosMotivoMvr");
                bandera = 0;
                filtrarMotivosMvrs = null;
                tipoLista = 0;
            }
            duplicarMotivosMvrs = new Motivosmvrs();
            infoRegistro = "Cantidad de registros: " + listMotivosMvrs.size();
            context.update("form:informacionRegistro");
            RequestContext.getCurrentInstance().execute("duplicarRegistroMotivosMvr.hide()");

        } else {
            contador = 0;
            context.update("form:validacionDuplicarVigencia");
            context.execute("validacionDuplicarVigencia.show()");
        }
    }

    public void limpiarDuplicarMotivosMvrs() {
        duplicarMotivosMvrs = new Motivosmvrs();
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosMotivoMvrExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "MotivoMvr", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosMotivoMvrExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "MotivoMvr", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        System.out.println("lol");
        if (!listMotivosMvrs.isEmpty()) {
            if (secRegistro != null) {
                System.out.println("lol 2");
                int resultado = administrarRastros.obtenerTabla(secRegistro, "MOTIVOSMVRS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
            if (administrarRastros.verificarHistoricosTabla("MOTIVOSMVRS")) { // igual acá
                context.execute("confirmarRastroHistorico.show()");
            } else {
                context.execute("errorRastroHistorico.show()");
            }

        }
        index = -1;
    }

    //-----------------------------------------------------------------------------   
    public List<Motivosmvrs> getListMotivosMvrs() {
        if (listMotivosMvrs == null) {
            listMotivosMvrs = administrarMotivosMvrs.consultarMotivosMvrs();
        }
        RequestContext context = RequestContext.getCurrentInstance();
        if (listMotivosMvrs == null || listMotivosMvrs.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listMotivosMvrs.size();
        }
        context.update("form:informacionRegistro");
        return listMotivosMvrs;
    }

    public void setListMotivosMvrs(List<Motivosmvrs> listMotivosMvrs) {
        this.listMotivosMvrs = listMotivosMvrs;
    }

    public List<Motivosmvrs> getFiltrarMotivosMvrs() {
        return filtrarMotivosMvrs;
    }

    public void setFiltrarMotivosMvrs(List<Motivosmvrs> filtrarMotivosMvrs) {
        this.filtrarMotivosMvrs = filtrarMotivosMvrs;
    }

    public Motivosmvrs getDuplicarMotivosMvrs() {
        return duplicarMotivosMvrs;
    }

    public void setDuplicarMotivosMvrs(Motivosmvrs duplicarMotivosMvrs) {
        this.duplicarMotivosMvrs = duplicarMotivosMvrs;
    }

    public Motivosmvrs getEditarMotivosMvrs() {
        return editarMotivosMvrs;
    }

    public void setEditarMotivosMvrs(Motivosmvrs editarMotivosMvrs) {
        this.editarMotivosMvrs = editarMotivosMvrs;
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

    public Motivosmvrs getNuevoMotivoMvr() {
        return nuevoMotivoMvr;
    }

    public void setNuevoMotivoMvr(Motivosmvrs nuevoMotivoMvr) {
        this.nuevoMotivoMvr = nuevoMotivoMvr;
    }

    public BigInteger getSecRegistro() {
        return secRegistro;
    }

    public void setSecRegistro(BigInteger secRegistro) {
        this.secRegistro = secRegistro;
    }

    public int getTamano() {
        return tamano;
    }

    public void setTamano(int tamano) {
        this.tamano = tamano;
    }

    public Motivosmvrs getMotivoMvrSeleccionada() {
        return motivoMvrSeleccionada;
    }

    public void setMotivoMvrSeleccionada(Motivosmvrs motivoMvrSeleccionada) {
        this.motivoMvrSeleccionada = motivoMvrSeleccionada;
    }

    public boolean isGuardado() {
        return guardado;
    }

    public void setGuardado(boolean guardado) {
        this.guardado = guardado;
    }

    public String getInfoRegistro() {
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

}
