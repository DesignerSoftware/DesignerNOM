/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;


import Entidades.Enfoques;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarEnfoquesInterface;
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
public class ControlEnfoques implements Serializable {

    @EJB
    AdministrarEnfoquesInterface administrarEnfoques;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    private List<Enfoques> listEnfoques;
    private List<Enfoques> filtrarEnfoques;
    private List<Enfoques> crearEnfoques;
    private List<Enfoques> modificarEnfoques;
    private List<Enfoques> borrarEnfoques;
    private Enfoques nuevoEnfoque;
    private Enfoques duplicarEnfoque;
    private Enfoques editarEnfoque;
    private Enfoques enfoqueSeleccionado;
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
    private int tamano;

    private String backUpDescripcion;
    private Integer backUpCodigo;
    private String infoRegistro;

    public ControlEnfoques() {
        listEnfoques = null;
        crearEnfoques = new ArrayList<Enfoques>();
        modificarEnfoques = new ArrayList<Enfoques>();
        borrarEnfoques = new ArrayList<Enfoques>();
        permitirIndex = true;
        editarEnfoque = new Enfoques();
        nuevoEnfoque = new Enfoques();
        duplicarEnfoque = new Enfoques();
        tamano = 270;
        guardado = true;
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarEnfoques.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void eventoFiltrar() {
        try {
            System.out.println("\n ENTRE A CONTROLENFOQUES.eventoFiltrar \n");
            if (tipoLista == 0) {
                tipoLista = 1;
            }
            RequestContext context = RequestContext.getCurrentInstance();
            infoRegistro = "Cantidad de registros: " + filtrarEnfoques.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
        } catch (Exception e) {
            System.out.println("ERROR CONTROLENFOQUES eventoFiltrar ERROR===" + e.getMessage());
        }
    }

    public void cambiarIndice(int indice, int celda) {
        System.err.println("TIPO LISTA = " + tipoLista);

        if (permitirIndex == true) {
            index = indice;
            cualCelda = celda;
            secRegistro = listEnfoques.get(index).getSecuencia();
            if (cualCelda == 0) {
                if (tipoLista == 0) {
                    backUpCodigo = listEnfoques.get(index).getCodigo();
                } else {
                    backUpCodigo = filtrarEnfoques.get(index).getCodigo();
                }
            }
            if (cualCelda == 1) {
                if (tipoLista == 0) {
                    backUpDescripcion = listEnfoques.get(index).getDescripcion();
                } else {
                    backUpDescripcion = filtrarEnfoques.get(index).getDescripcion();
                }
            }
        }
        System.out.println("Indice: " + index + " Celda: " + cualCelda);
    }

    public void asignarIndex(Integer indice, int LND, int dig) {
        try {
            System.out.println("\n ENTRE A ControlMotivosLocalizaciones.asignarIndex \n");
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
            System.out.println("ERROR ControlMotivosLocalizaciones.asignarIndex ERROR======" + e.getMessage());
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
            codigo = (Column) c.getViewRoot().findComponent("form:datosEnfoque:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosEnfoque:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosEnfoque");
            bandera = 0;
            filtrarEnfoques = null;
            tipoLista = 0;
        }

        borrarEnfoques.clear();
        crearEnfoques.clear();
        modificarEnfoques.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listEnfoques = null;
        guardado = true;
        permitirIndex = true;
        getListEnfoques();
        RequestContext context = RequestContext.getCurrentInstance();
        if (listEnfoques == null || listEnfoques.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listEnfoques.size();
        }
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
        RequestContext.getCurrentInstance().update("form:datosEnfoque");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void salir() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 1) {
            //CERRAR FILTRADO
            codigo = (Column) c.getViewRoot().findComponent("form:datosEnfoque:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosEnfoque:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosEnfoque");
            bandera = 0;
            filtrarEnfoques = null;
            tipoLista = 0;
        }

        borrarEnfoques.clear();
        crearEnfoques.clear();
        modificarEnfoques.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listEnfoques = null;
        guardado = true;
        permitirIndex = true;
        getListEnfoques();
        RequestContext context = RequestContext.getCurrentInstance();
        if (listEnfoques == null || listEnfoques.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listEnfoques.size();
        }
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
        RequestContext.getCurrentInstance().update("form:datosEnfoque");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            tamano = 250;
            codigo = (Column) c.getViewRoot().findComponent("form:datosEnfoque:codigo");
            codigo.setFilterStyle("width: 85% !important;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosEnfoque:descripcion");
            descripcion.setFilterStyle("width: 85% !important;");
            RequestContext.getCurrentInstance().update("form:datosEnfoque");
            System.out.println("Activar");
            bandera = 1;
        } else if (bandera == 1) {
            System.out.println("Desactivar");
            codigo = (Column) c.getViewRoot().findComponent("form:datosEnfoque:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosEnfoque:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosEnfoque");
            bandera = 0;
            filtrarEnfoques = null;
            tipoLista = 0;
            tamano = 270;
        }
    }

    public void modificarEnfoques(int indice, String confirmarCambio, String valorConfirmar) {
        System.err.println("ENTRE A MODIFICAR MOTIVOS LOCALIZACIONES");
        index = indice;

        int contador = 0;
        boolean banderita = false;
        Integer a;
        a = null;
        RequestContext context = RequestContext.getCurrentInstance();
        System.err.println("TIPO LISTA = " + tipoLista);
        if (confirmarCambio.equalsIgnoreCase("N")) {
            System.err.println("ENTRE A MODIFICAR MOTIVOS LOCALIZACIONES, CONFIRMAR CAMBIO ES N");
            if (tipoLista == 0) {
                if (!crearEnfoques.contains(listEnfoques.get(indice))) {
                    if (listEnfoques.get(indice).getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listEnfoques.get(indice).setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < listEnfoques.size(); j++) {
                            if (j != indice) {
                                if (listEnfoques.get(indice).getCodigo() == listEnfoques.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            listEnfoques.get(indice).setCodigo(backUpCodigo);
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            banderita = false;
                        } else {
                            banderita = true;
                        }

                    }
                    if (listEnfoques.get(indice).getDescripcion().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listEnfoques.get(indice).setDescripcion(backUpDescripcion);
                    }
                    if (listEnfoques.get(indice).getDescripcion().equals(" ")) {
                        banderita = false;
                        listEnfoques.get(indice).setDescripcion(backUpDescripcion);
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    }
                    if (banderita == true) {
                        if (modificarEnfoques.isEmpty()) {
                            modificarEnfoques.add(listEnfoques.get(indice));
                        } else if (!modificarEnfoques.contains(listEnfoques.get(indice))) {
                            modificarEnfoques.add(listEnfoques.get(indice));
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
                } else {
                    if (listEnfoques.get(indice).getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listEnfoques.get(indice).setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < listEnfoques.size(); j++) {
                            if (j != indice) {
                                if (listEnfoques.get(indice).getCodigo() == listEnfoques.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            listEnfoques.get(indice).setCodigo(backUpCodigo);
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            banderita = false;
                        } else {
                            banderita = true;
                        }

                    }
                    if (listEnfoques.get(indice).getDescripcion().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listEnfoques.get(indice).setDescripcion(backUpDescripcion);
                    }
                    if (listEnfoques.get(indice).getDescripcion().equals(" ")) {
                        banderita = false;
                        listEnfoques.get(indice).setDescripcion(backUpDescripcion);
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    }
                    if (banderita == true) {
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
            } else {

                if (!crearEnfoques.contains(filtrarEnfoques.get(indice))) {
                    if (filtrarEnfoques.get(indice).getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        filtrarEnfoques.get(indice).setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < listEnfoques.size(); j++) {
                            if (j != indice) {
                                if (filtrarEnfoques.get(indice).getCodigo() == listEnfoques.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }
                        for (int j = 0; j < filtrarEnfoques.size(); j++) {
                            if (j != indice) {
                                if (filtrarEnfoques.get(indice).getCodigo() == filtrarEnfoques.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            filtrarEnfoques.get(indice).setCodigo(backUpCodigo);
                            banderita = false;
                        } else {
                            banderita = true;
                        }

                    }
                    if (filtrarEnfoques.get(indice).getDescripcion().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        filtrarEnfoques.get(indice).setDescripcion(backUpDescripcion);
                    }
                    if (filtrarEnfoques.get(indice).getDescripcion().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        filtrarEnfoques.get(indice).setDescripcion(backUpDescripcion);
                        banderita = false;
                    }

                    if (banderita == true) {
                        if (modificarEnfoques.isEmpty()) {
                            modificarEnfoques.add(filtrarEnfoques.get(indice));
                        } else if (!modificarEnfoques.contains(filtrarEnfoques.get(indice))) {
                            modificarEnfoques.add(filtrarEnfoques.get(indice));
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
                } else {
                    if (filtrarEnfoques.get(indice).getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        filtrarEnfoques.get(indice).setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < listEnfoques.size(); j++) {
                            if (j != indice) {
                                if (filtrarEnfoques.get(indice).getCodigo() == listEnfoques.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }
                        for (int j = 0; j < filtrarEnfoques.size(); j++) {
                            if (j != indice) {
                                if (filtrarEnfoques.get(indice).getCodigo() == filtrarEnfoques.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            filtrarEnfoques.get(indice).setCodigo(backUpCodigo);
                            banderita = false;
                        } else {
                            banderita = true;
                        }

                    }
                    if (filtrarEnfoques.get(indice).getDescripcion().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        filtrarEnfoques.get(indice).setDescripcion(backUpDescripcion);
                    }
                    if (filtrarEnfoques.get(indice).getDescripcion().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        filtrarEnfoques.get(indice).setDescripcion(backUpDescripcion);
                        banderita = false;
                    }

                    if (banderita == true) {

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

            }
            RequestContext.getCurrentInstance().update("form:datosEnfoque");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }

    }

    public void borrandoEnfoques() {

        if (index >= 0) {

            if (tipoLista == 0) {
                System.out.println("Entro a borrardatosEnfoques");
                if (!modificarEnfoques.isEmpty() && modificarEnfoques.contains(listEnfoques.get(index))) {
                    int modIndex = modificarEnfoques.indexOf(listEnfoques.get(index));
                    modificarEnfoques.remove(modIndex);
                    borrarEnfoques.add(listEnfoques.get(index));
                } else if (!crearEnfoques.isEmpty() && crearEnfoques.contains(listEnfoques.get(index))) {
                    int crearIndex = crearEnfoques.indexOf(listEnfoques.get(index));
                    crearEnfoques.remove(crearIndex);
                } else {
                    borrarEnfoques.add(listEnfoques.get(index));
                }
                listEnfoques.remove(index);
            }
            if (tipoLista == 1) {
                System.out.println("borrardatosEnfoques ");
                if (!modificarEnfoques.isEmpty() && modificarEnfoques.contains(filtrarEnfoques.get(index))) {
                    int modIndex = modificarEnfoques.indexOf(filtrarEnfoques.get(index));
                    modificarEnfoques.remove(modIndex);
                    borrarEnfoques.add(filtrarEnfoques.get(index));
                } else if (!crearEnfoques.isEmpty() && crearEnfoques.contains(filtrarEnfoques.get(index))) {
                    int crearIndex = crearEnfoques.indexOf(filtrarEnfoques.get(index));
                    crearEnfoques.remove(crearIndex);
                } else {
                    borrarEnfoques.add(filtrarEnfoques.get(index));
                }
                int VCIndex = listEnfoques.indexOf(filtrarEnfoques.get(index));
                listEnfoques.remove(VCIndex);
                filtrarEnfoques.remove(index);

            }
            RequestContext context = RequestContext.getCurrentInstance();
            infoRegistro = "Cantidad de registros: " + listEnfoques.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            if (guardado == true) {
                guardado = false;
            }
            RequestContext.getCurrentInstance().update("form:datosEnfoque");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            index = -1;
            secRegistro = null;
        }

    }

    private BigInteger verificarTablasAuxilios;

    public void verificarBorrado() {
        try {
            System.out.println("ESTOY EN VERIFICAR BORRADO tipoLista " + tipoLista);
            System.out.println("secuencia borrado : " + listEnfoques.get(index).getSecuencia());
            if (tipoLista == 0) {
                System.out.println("secuencia borrado : " + listEnfoques.get(index).getSecuencia());
                verificarTablasAuxilios = administrarEnfoques.verificarTiposDetalles(listEnfoques.get(index).getSecuencia());
            } else {
                System.out.println("secuencia borrado : " + filtrarEnfoques.get(index).getSecuencia());
                verificarTablasAuxilios = administrarEnfoques.verificarTiposDetalles(filtrarEnfoques.get(index).getSecuencia());
            }
            if (!verificarTablasAuxilios.equals(new BigInteger("0"))) {
                System.out.println("Borrado>0");

                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:validacionBorrar");
                RequestContext.getCurrentInstance().execute("PF('validacionBorrar').show()");
                index = -1;

                verificarTablasAuxilios = new BigInteger("-1");

            } else {
                System.out.println("Borrado==0");
                borrandoEnfoques();
            }
        } catch (Exception e) {
            System.err.println("ERROR CONTROLENFOQUES verificarBorrado ERROR " + e);
        }
    }

    public void guardarEnfoques() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (guardado == false) {
            System.out.println("Realizando Operaciones Motivos Localizacion");
            if (!borrarEnfoques.isEmpty()) {
                administrarEnfoques.borrarEnfoques(borrarEnfoques);
                registrosBorrados = borrarEnfoques.size();
                RequestContext.getCurrentInstance().update("form:mostrarBorrados");
                RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
                borrarEnfoques.clear();
            }
            if (!crearEnfoques.isEmpty()) {
                administrarEnfoques.crearEnfoques(crearEnfoques);
                crearEnfoques.clear();
            }
            if (!modificarEnfoques.isEmpty()) {
                administrarEnfoques.modificarEnfoques(modificarEnfoques);
                modificarEnfoques.clear();
            }
            System.out.println("Se guardaron los datos con exito");
            listEnfoques = null;
            FacesMessage msg = new FacesMessage("Información", "Se gurdarón los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            RequestContext.getCurrentInstance().update("form:datosEnfoque");
            k = 0;
            guardado = true;
        }
        index = -1;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");

    }

    public void editarCelda() {
        if (index >= 0) {
            if (tipoLista == 0) {
                editarEnfoque = listEnfoques.get(index);
            }
            if (tipoLista == 1) {
                editarEnfoque = filtrarEnfoques.get(index);
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

        }
        index = -1;
        secRegistro = null;
    }

    public void agregarNuevoEnfoque() {
        System.out.println("Agregar Motivo Localizacion");
        int contador = 0;
        int duplicados = 0;

        Integer a = 0;
        a = null;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        if (nuevoEnfoque.getCodigo() == a) {
            mensajeValidacion = " *Codigo \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else {
            System.out.println("codigo en Motivo Cambio Cargo: " + nuevoEnfoque.getCodigo());

            for (int x = 0; x < listEnfoques.size(); x++) {
                if (listEnfoques.get(x).getCodigo() == nuevoEnfoque.getCodigo()) {
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
        if (nuevoEnfoque.getDescripcion() == (null)) {
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
                codigo = (Column) c.getViewRoot().findComponent("form:datosEnfoque:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosEnfoque:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosEnfoque");
                bandera = 0;
                filtrarEnfoques = null;
                tipoLista = 0;
            }
            System.out.println("Despues de la bandera");

            k++;
            l = BigInteger.valueOf(k);
            nuevoEnfoque.setSecuencia(l);

            crearEnfoques.add(nuevoEnfoque);

            listEnfoques.add(nuevoEnfoque);
            nuevoEnfoque = new Enfoques();

            RequestContext.getCurrentInstance().update("form:datosEnfoque");
            infoRegistro = "Cantidad de registros: " + listEnfoques.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            System.out.println("Despues de la bandera guardado");

            RequestContext.getCurrentInstance().execute("PF('nuevoRegistroEnfoque').hide()");
            index = -1;
            secRegistro = null;
            System.out.println("Despues de nuevoRegistroEnfoque");
            nuevoEnfoque = new Enfoques();
        } else {
            RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevaCentroCosto').show()");
            contador = 0;
        }
    }

    public void limpiarNuevoEnfoque() {
        System.out.println("limpiarnuevoEnfoques");
        nuevoEnfoque = new Enfoques();
        secRegistro = null;
        index = -1;

    }

    //------------------------------------------------------------------------------
    public void duplicarEnfoques() {
        System.out.println("duplicarEnfoques");
        if (index >= 0) {
            duplicarEnfoque = new Enfoques();
            k++;
            l = BigInteger.valueOf(k);

            if (tipoLista == 0) {
                duplicarEnfoque.setSecuencia(l);
                duplicarEnfoque.setCodigo(listEnfoques.get(index).getCodigo());
                duplicarEnfoque.setDescripcion(listEnfoques.get(index).getDescripcion());
            }
            if (tipoLista == 1) {
                duplicarEnfoque.setSecuencia(l);
                duplicarEnfoque.setCodigo(filtrarEnfoques.get(index).getCodigo());
                duplicarEnfoque.setDescripcion(filtrarEnfoques.get(index).getDescripcion());
            }

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEnfoques");
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroEnfoque').show()");
            index = -1;
            secRegistro = null;
        }
    }

    public void confirmarDuplicar() {
        System.err.println("ESTOY EN CONFIRMAR DUPLICAR ControlEnfoques");
        int contador = 0;
        mensajeValidacion = " ";
        int duplicados = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        Integer a = 0;
        a = null;
        System.err.println("ConfirmarDuplicar codigo " + duplicarEnfoque.getCodigo());
        System.err.println("ConfirmarDuplicar descripcion " + duplicarEnfoque.getDescripcion());

        if (duplicarEnfoque.getCodigo() == a) {
            mensajeValidacion = mensajeValidacion + "   *Codigo \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else {
            for (int x = 0; x < listEnfoques.size(); x++) {
                if (listEnfoques.get(x).getCodigo() == duplicarEnfoque.getCodigo()) {
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
        if (duplicarEnfoque.getDescripcion().isEmpty()) {
            mensajeValidacion = mensajeValidacion + "   *Descripcion \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("Bandera : ");
            contador++;
        }

        if (contador == 2) {

            System.out.println("Datos Duplicando: " + duplicarEnfoque.getSecuencia() + "  " + duplicarEnfoque.getCodigo());
            if (crearEnfoques.contains(duplicarEnfoque)) {
                System.out.println("Ya lo contengo.");
            }
            listEnfoques.add(duplicarEnfoque);
            crearEnfoques.add(duplicarEnfoque);
            RequestContext.getCurrentInstance().update("form:datosEnfoque");
            infoRegistro = "Cantidad de registros: " + listEnfoques.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            index = -1;
            secRegistro = null;
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                codigo = (Column) c.getViewRoot().findComponent("form:datosEnfoque:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosEnfoque:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosEnfoque");
                bandera = 0;
                filtrarEnfoques = null;
                tipoLista = 0;
            }
            duplicarEnfoque = new Enfoques();
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroEnfoque').hide()");

        } else {
            contador = 0;
            RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
            RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
        }
    }

    public void limpiarduplicarEnfoque() {
        duplicarEnfoque = new Enfoques();
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosEnfoqueExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "Enfoque", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosEnfoqueExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "Enfoque", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        System.out.println("lol");
        if (!listEnfoques.isEmpty()) {
            if (secRegistro != null) {
                System.out.println("lol 2");
                int resultado = administrarRastros.obtenerTabla(secRegistro, "ENFOQUES"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
            if (administrarRastros.verificarHistoricosTabla("ENFOQUES")) { // igual acá
                RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
            } else {
                RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
            }

        }
        index = -1;
    }

    //--/-*/-*/-*/-*/*/*/*/*/****/////-----*****/////-*/-*/*/*/-*/-*/-*/-*/-*/-*/-*/
    public List<Enfoques> getListEnfoques() {
        if (listEnfoques == null) {
            listEnfoques = administrarEnfoques.consultarEnfoques();
        }
        RequestContext context = RequestContext.getCurrentInstance();
        if (listEnfoques == null || listEnfoques.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listEnfoques.size();
        }
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
        return listEnfoques;
    }

    public void setListEnfoques(List<Enfoques> listEnfoques) {
        this.listEnfoques = listEnfoques;
    }

    public List<Enfoques> getFiltrarEnfoques() {
        return filtrarEnfoques;
    }

    public void setFiltrarEnfoques(List<Enfoques> filtrarEnfoques) {
        this.filtrarEnfoques = filtrarEnfoques;
    }

    public Enfoques getDuplicarEnfoque() {
        return duplicarEnfoque;
    }

    public void setDuplicarEnfoque(Enfoques duplicarEnfoque) {
        this.duplicarEnfoque = duplicarEnfoque;
    }

    public Enfoques getEditarEnfoque() {
        return editarEnfoque;
    }

    public void setEditarEnfoque(Enfoques editarEnfoque) {
        this.editarEnfoque = editarEnfoque;
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

    public Enfoques getNuevoEnfoque() {
        return nuevoEnfoque;
    }

    public void setNuevoEnfoque(Enfoques nuevoEnfoque) {
        this.nuevoEnfoque = nuevoEnfoque;
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

    public Enfoques getEnfoqueSeleccionado() {
        return enfoqueSeleccionado;
    }

    public void setEnfoqueSeleccionado(Enfoques enfoqueSeleccionado) {
        this.enfoqueSeleccionado = enfoqueSeleccionado;
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
