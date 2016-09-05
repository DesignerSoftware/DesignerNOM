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
import javax.ejb.EJB;
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
public class ControlMotivosPrestamos implements Serializable {

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
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarMotivosPrestamos.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }
    
    public void eventoFiltrar() {
        try {
            System.out.println("\n ENTRE A CONTROLMOTIVOSPRESTAMOS EVENTOFILTRAR \n");
            if (tipoLista == 0) {
                tipoLista = 1;
            }
        } catch (Exception e) {
            System.out.println("ERROR CONTROLMOTIVOSPRESTAMOS EVENTOFILTRAR  ERROR===" + e.getMessage());
        }
    }

    public void cambiarIndice(int indice, int celda) {
        System.err.println("TIPO LISTA = " + tipoLista);

        if (permitirIndex == true) {
            index = indice;
            cualCelda = celda;
            secRegistro = listMotivosPrestamos.get(index).getSecuencia();

        }
        System.out.println("Indice: " + index + " Celda: " + cualCelda);
    }

    public void asignarIndex(Integer indice, int LND, int dig) {
        try {
            System.out.println("\n ENTRE A CONTROLMOTIVOSPRESTAMOS ASIGNAR INDEX \n");
            index = indice;
            if (LND == 0) {
                tipoActualizacion = 0;
            } else if (LND == 1) {
                tipoActualizacion = 1;
                System.out.println("TIPO ACTUALIZACION : " + tipoActualizacion);
            } else if (LND == 2) {
                tipoActualizacion = 2;
            }

        } catch (Exception e) {
            System.out.println("ERROR CONTROLMOTIVOSPRESTAMOS ASIGNAR INDEX ERROR======" + e.getMessage());
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
        context.update("form:datosTipoReemplazo");
        context.update("form:ACEPTAR");
    }

    public void activarCtrlF11() {
        if (bandera == 0) {

            codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoReemplazo:codigo");
            codigo.setFilterStyle("width: 85%;");
            descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoReemplazo:descripcion");
            descripcion.setFilterStyle("width: 85%;");
            RequestContext.getCurrentInstance().update("form:datosTipoReemplazo");
            System.out.println("Activar");
            bandera = 1;
        } else if (bandera == 1) {
            System.out.println("Desactivar");
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
        System.err.println("ENTRE A MODIFICAR MOTIVOSPRESTAMOS");
        index = indice;

        int contador = 0;
        int contadorGuardar = 0;
        boolean banderita = false;
        Integer a;
        a = null;
        RequestContext context = RequestContext.getCurrentInstance();
        System.err.println("TIPO LISTA = " + tipoLista);
        if (confirmarCambio.equalsIgnoreCase("N")) {
            System.err.println("ENTRE A MODIFICAR MOTIVOPRESTAMO, CONFIRMAR CAMBIO ES N");
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
                        context.update("form:validacionModificar");
                        context.execute("validacionModificar.show()");
                        cancelarModificacion();
                    }
                    index = -1;
                    secRegistro = null;
                }
            } else {

                if (!crearMotivosPrestamos.contains(filtrarMotivosPrestamos.get(indice))) {
                    if (filtrarMotivosPrestamos.get(indice).getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                    } else {
                        System.err.println("codigo ingresado " + filtrarMotivosPrestamos.get(indice).getCodigo());
                        for (int j = 0; j < listMotivosPrestamos.size(); j++) {
                            if (j != indice) {
                                if (filtrarMotivosPrestamos.get(indice).getCodigo().equals(listMotivosPrestamos.get(j).getCodigo())) {
                                    System.err.println("Contador 1... " + contador);
                                    System.err.println("lista" + listMotivosPrestamos.get(indice).getCodigo());
                                    System.err.println("filtrar " + filtrarMotivosPrestamos.get(indice).getCodigo());
                                    contador++;
                                }
                            }
                        }

                        //System.err.println("Contador 1... " + contador);
                        for (int j = 0; j < filtrarMotivosPrestamos.size(); j++) {
                            if (j != indice) {
                                if (filtrarMotivosPrestamos.get(indice).getCodigo().equals(filtrarMotivosPrestamos.get(j).getCodigo())) {
                                    contador++;
                                }
                            }
                        }

                        System.err.println("Contador " + contador);
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
                        context.update("form:validacionModificar");
                        context.execute("validacionModificar.show()");
                        contador = 0;
                        cancelarModificacion();
                    }
                    index = -1;
                    secRegistro = null;
                }

            }
            context.update("form:datosTipoReemplazo");
            context.update("form:ACEPTAR");
        }

    }

    public void borrandoMotivosPrestamos() {

        if (index >= 0) {
            if (tipoLista == 0) {
                System.out.println("Entro a borrandoMotivosPrestamos");
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
                System.out.println("borrandoMotivosPrestamos");
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
            context.update("form:datosTipoReemplazo");
            context.update("form:ACEPTAR");
            index = -1;
            secRegistro = null;

        }

    }

    private BigInteger verificarEerPrestamos;

    public void verificarBorrado() {
        System.out.println("ESTOY EN VERIFICAR BORRADO tipoLista " + tipoLista);
        try {
            System.out.println("secuencia borrado : " + listMotivosPrestamos.get(index).getSecuencia());
            if (tipoLista == 0) {
                System.out.println("secuencia borrado : " + listMotivosPrestamos.get(index).getSecuencia());
                verificarEerPrestamos = administrarMotivosPrestamos.verificarEersPrestamosMotivoPrestamo(listMotivosPrestamos.get(index).getSecuencia());
            } else {
                System.out.println("secuencia borrado : " + filtrarMotivosPrestamos.get(index).getSecuencia());
                verificarEerPrestamos = administrarMotivosPrestamos.verificarEersPrestamosMotivoPrestamo(filtrarMotivosPrestamos.get(index).getSecuencia());
            }
            if (!verificarEerPrestamos.equals(new BigInteger("0"))) {
                System.out.println("Borrado>0");

                RequestContext context = RequestContext.getCurrentInstance();
                context.update("form:validacionBorrar");
                context.execute("validacionBorrar.show()");
                index = -1;

                verificarEerPrestamos = new BigInteger("-1");

            } else {
                System.out.println("Borrado==0");
                borrandoMotivosPrestamos();
            }
        } catch (Exception e) {
            System.err.println("ERROR ControlTiposCertificados verificarBorrado ERROR " + e);
        }
    }

    public void revisarDialogoGuardar() {

        if (!borrarMotivosPrestamos.isEmpty() || !crearMotivosPrestamos.isEmpty() || !modificarMotivosPrestamos.isEmpty()) {
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("form:confirmarGuardar");
            context.execute("confirmarGuardar.show()");
        }

    }

    public void guardarMotivoPrestamo() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (guardado == false) {
            System.out.println("Realizando MotivoPrestamo");
            if (!borrarMotivosPrestamos.isEmpty()) {
                administrarMotivosPrestamos.borrarMotivosPrestamos(borrarMotivosPrestamos);
                //mostrarBorrados
                registrosBorrados = borrarMotivosPrestamos.size();
                context.update("form:mostrarBorrados");
                context.execute("mostrarBorrados.show()");
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
            System.out.println("Se guardaron los datos con exito");
            listMotivosPrestamos = null;
            guardado = true;
            context.update("form:datosTipoReemplazo");
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

    public void agregarNuevoMotivosPrestamos() {
        System.out.println("agregarNuevoMotivosPrestamos");
        int contador = 0;
        int duplicados = 0;

        Integer a = 0;
        a = null;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        if (nuevoMotivoPrestamo.getCodigo() == a) {
            mensajeValidacion = " *Debe Tener Un Codigo \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else {
            System.out.println("codigo en Motivo Cambio Cargo: " + nuevoMotivoPrestamo.getCodigo());

            for (int x = 0; x < listMotivosPrestamos.size(); x++) {
                if (listMotivosPrestamos.get(x).getCodigo() == nuevoMotivoPrestamo.getCodigo()) {
                    duplicados++;
                }
            }
            System.out.println("Antes del if Duplicados eses igual  : " + duplicados);

            if (duplicados > 0) {
                mensajeValidacion = " *Que NO hayan codigos repetidos \n";
                System.out.println("Mensaje validacion : " + mensajeValidacion);
            } else {
                System.out.println("bandera");
                contador++;
            }
        }
        if (nuevoMotivoPrestamo.getNombre() == (null)) {
            mensajeValidacion = mensajeValidacion + " *Debe tener un Nombre Motivo Préstamo \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("bandera");
            contador++;

        }

        System.out.println("contador " + contador);

        if (contador == 2) {
            if (bandera == 1) {
                //CERRAR FILTRADO
                System.out.println("Desactivar");
                codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoReemplazo:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoReemplazo:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosTipoReemplazo");
                bandera = 0;
                filtrarMotivosPrestamos = null;
                tipoLista = 0;
            }
            System.out.println("Despues de la bandera");

            k++;
            l = BigInteger.valueOf(k);
            nuevoMotivoPrestamo.setSecuencia(l);

            crearMotivosPrestamos.add(nuevoMotivoPrestamo);

            listMotivosPrestamos.add(nuevoMotivoPrestamo);
            nuevoMotivoPrestamo = new MotivosPrestamos();
            context.update("form:datosTipoReemplazo");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }

            context.execute("nuevoRegistroTiposReemplazos.hide()");
            index = -1;
            secRegistro = null;

        } else {
            context.update("form:validacionNuevaCentroCosto");
            context.execute("validacionNuevaCentroCosto.show()");
            contador = 0;
        }
    }

    public void limpiarNuevoMotivosPrestamos() {
        System.out.println("limpiarNuevoMotivosPrestamos");
        nuevoMotivoPrestamo = new MotivosPrestamos();
        secRegistro = null;
        index = -1;

    }

    //------------------------------------------------------------------------------
    public void duplicandoMotivosPrestamos() {
        System.out.println("duplicandoMotivosPrestamos");
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
            context.update("formularioDialogos:duplicarTTR");
            context.execute("duplicarRegistroTiposReemplazos.show()");
            index = -1;
            secRegistro = null;
        }
    }

    public void confirmarDuplicar() {
        System.err.println("ESTOY EN CONFIRMAR DUPLICAR MOTIVOSPRESTAMOS");
        int contador = 0;
        mensajeValidacion = " ";
        int duplicados = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        Integer a = 0;
        a = null;

        if (duplicarMotivoPrestamo.getCodigo() == a) {
            mensajeValidacion = mensajeValidacion + "   * Codigo \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else {
            for (int x = 0; x < listMotivosPrestamos.size(); x++) {
                if (listMotivosPrestamos.get(x).getCodigo() == duplicarMotivoPrestamo.getCodigo()) {
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
        if (duplicarMotivoPrestamo.getNombre() == null) {
            mensajeValidacion = mensajeValidacion + "   * Un Nombre Motivo Préstamo \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("Bandera : ");
            contador++;
        }

        if (contador == 2) {

            System.out.println("Datos Duplicando: " + duplicarMotivoPrestamo.getSecuencia() + "  " + duplicarMotivoPrestamo.getCodigo());
            if (crearMotivosPrestamos.contains(duplicarMotivoPrestamo)) {
                System.out.println("Ya lo contengo.");
            }
            listMotivosPrestamos.add(duplicarMotivoPrestamo);
            crearMotivosPrestamos.add(duplicarMotivoPrestamo);
            context.update("form:datosTipoReemplazo");
            index = -1;
            secRegistro = null;
            if (guardado == true) {
                guardado = false;
                context.update("form:ACEPTAR");
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
            RequestContext.getCurrentInstance().execute("duplicarRegistroTiposReemplazos.hide()");

        } else {
            contador = 0;
            context.update("form:validacionDuplicarVigencia");
            context.execute("validacionDuplicarVigencia.show()");
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
        System.out.println("lol");
        if (!listMotivosPrestamos.isEmpty()) {
            if (secRegistro != null) {
                System.out.println("lol 2");
                int resultado = administrarRastros.obtenerTabla(secRegistro, "MOTIVOSPRESTAMOS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
            if (administrarRastros.verificarHistoricosTabla("MOTIVOSPRESTAMOS")) { // igual acá
                context.execute("confirmarRastroHistorico.show()");
            } else {
                context.execute("errorRastroHistorico.show()");
            }

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
