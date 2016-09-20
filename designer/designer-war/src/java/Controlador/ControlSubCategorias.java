/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;


import Entidades.SubCategorias;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarSubCategoriasInterface;
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
public class ControlSubCategorias implements Serializable {

    @EJB
    AdministrarSubCategoriasInterface administrarSubCategorias;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    private List<SubCategorias> listSubCategorias;
    private List<SubCategorias> filtrarSubCategorias;
    private List<SubCategorias> crearSubCategorias;
    private List<SubCategorias> modificarSubCategorias;
    private List<SubCategorias> borrarSubCategorias;
    private SubCategorias nuevoSubCategoria;
    private SubCategorias duplicarSubCategoria;
    private SubCategorias editarSubCategoria;
    private SubCategorias subCategoriaSeleccionada;
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

    private Integer backUpCodigo;
    private String backUpDescripcion;

    public ControlSubCategorias() {
        listSubCategorias = null;
        crearSubCategorias = new ArrayList<SubCategorias>();
        modificarSubCategorias = new ArrayList<SubCategorias>();
        borrarSubCategorias = new ArrayList<SubCategorias>();
        permitirIndex = true;
        editarSubCategoria = new SubCategorias();
        nuevoSubCategoria = new SubCategorias();
        duplicarSubCategoria = new SubCategorias();
        guardado = true;
        tamano = 270;
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarSubCategorias.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void eventoFiltrar() {
        try {
            System.out.println("\n ENTRE A ControlSubCategorias.eventoFiltrar \n");
            if (tipoLista == 0) {
                tipoLista = 1;
            }
            RequestContext context = RequestContext.getCurrentInstance();
            infoRegistro = "Cantidad de registros: " + filtrarSubCategorias.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
        } catch (Exception e) {
            System.out.println("ERROR ControlSubCategorias eventoFiltrar ERROR===" + e.getMessage());
        }
    }

    public void cambiarIndice(int indice, int celda) {
        System.err.println("TIPO LISTA = " + tipoLista);

        if (permitirIndex == true) {
            index = indice;
            cualCelda = celda;
            if (tipoLista == 0) {
                if (cualCelda == 0) {
                    backUpCodigo = listSubCategorias.get(indice).getCodigo();
                } else if (cualCelda == 1) {
                    backUpDescripcion = listSubCategorias.get(indice).getDescripcion();
                }
            } else {
                if (cualCelda == 0) {
                    backUpCodigo = filtrarSubCategorias.get(indice).getCodigo();
                } else if (cualCelda == 1) {
                    backUpDescripcion = filtrarSubCategorias.get(indice).getDescripcion();
                }
            }
            secRegistro = listSubCategorias.get(index).getSecuencia();

        }
        System.out.println("Indice: " + index + " Celda: " + cualCelda);
    }

    public void asignarIndex(Integer indice, int LND, int dig) {
        try {
            System.out.println("\n ENTRE A ControlSubCategorias.asignarIndex \n");
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
            System.out.println("ERROR ControlSubCategorias.asignarIndex ERROR======" + e.getMessage());
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void listaValoresBoton() {
    }
    private String infoRegistro;

    public void cancelarModificacion() {
        if (bandera == 1) {
            //CERRAR FILTRADO
            FacesContext c = FacesContext.getCurrentInstance();
            codigo = (Column) c.getViewRoot().findComponent("form:datosSubCategoria:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosSubCategoria:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosSubCategoria");
            bandera = 0;
            filtrarSubCategorias = null;
            tipoLista = 0;
        }

        borrarSubCategorias.clear();
        crearSubCategorias.clear();
        modificarSubCategorias.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        tamano = 270;
        listSubCategorias = null;
        guardado = true;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        if (listSubCategorias == null || listSubCategorias.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listSubCategorias.size();
        }
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
        RequestContext.getCurrentInstance().update("form:datosSubCategoria");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void salir() {
        if (bandera == 1) {
            //CERRAR FILTRADO
            FacesContext c = FacesContext.getCurrentInstance();
            codigo = (Column) c.getViewRoot().findComponent("form:datosSubCategoria:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosSubCategoria:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosSubCategoria");
            bandera = 0;
            filtrarSubCategorias = null;
            tipoLista = 0;
        }

        borrarSubCategorias.clear();
        crearSubCategorias.clear();
        modificarSubCategorias.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        tamano = 270;
        listSubCategorias = null;
        guardado = true;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        if (listSubCategorias == null || listSubCategorias.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listSubCategorias.size();
        }
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
        RequestContext.getCurrentInstance().update("form:datosSubCategoria");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();

        if (bandera == 0) {
            tamano = 250;
            codigo = (Column) c.getViewRoot().findComponent("form:datosSubCategoria:codigo");
            codigo.setFilterStyle("width: 85% !important;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosSubCategoria:descripcion");
            descripcion.setFilterStyle("width: 85% !important;");
            RequestContext.getCurrentInstance().update("form:datosSubCategoria");
            System.out.println("Activar");
            bandera = 1;
        } else if (bandera == 1) {
            System.out.println("Desactivar");
            tamano = 270;
            codigo = (Column) c.getViewRoot().findComponent("form:datosSubCategoria:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosSubCategoria:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosSubCategoria");
            bandera = 0;
            filtrarSubCategorias = null;
            tipoLista = 0;
        }
    }

    public void modificarSubCategoria(int indice, String confirmarCambio, String valorConfirmar) {
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
                if (!crearSubCategorias.contains(listSubCategorias.get(indice))) {
                    if (listSubCategorias.get(indice).getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        listSubCategorias.get(indice).setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < listSubCategorias.size(); j++) {
                            if (j != indice) {
                                if (listSubCategorias.get(indice).getCodigo() == listSubCategorias.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            listSubCategorias.get(indice).setCodigo(backUpCodigo);
                        } else {
                            pass++;
                        }

                    }
                    if (listSubCategorias.get(indice).getDescripcion() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        listSubCategorias.get(indice).setDescripcion(backUpDescripcion);
                    } else if (listSubCategorias.get(indice).getDescripcion().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        listSubCategorias.get(indice).setDescripcion(backUpDescripcion);
                    } else {
                        pass++;
                    }

                    if (pass == 2) {
                        if (modificarSubCategorias.isEmpty()) {
                            modificarSubCategorias.add(listSubCategorias.get(indice));
                        } else if (!modificarSubCategorias.contains(listSubCategorias.get(indice))) {
                            modificarSubCategorias.add(listSubCategorias.get(indice));
                        }
                        if (guardado == true) {
                            guardado = false;
                        }

                    } else {
                        RequestContext.getCurrentInstance().update("form:validacionModificar");
                        RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");
                    }
                    index = -1;
                    secRegistro = null;
                } else {

                    if (listSubCategorias.get(indice).getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        listSubCategorias.get(indice).setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < listSubCategorias.size(); j++) {
                            if (j != indice) {
                                if (listSubCategorias.get(indice).getCodigo() == listSubCategorias.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            listSubCategorias.get(indice).setCodigo(backUpCodigo);
                        } else {
                            pass++;
                        }

                    }
                    if (listSubCategorias.get(indice).getDescripcion() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        listSubCategorias.get(indice).setDescripcion(backUpDescripcion);
                    } else if (listSubCategorias.get(indice).getDescripcion().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        listSubCategorias.get(indice).setDescripcion(backUpDescripcion);
                    } else {
                        pass++;
                    }

                    if (pass == 2) {
                        if (guardado == true) {
                            guardado = false;
                        }

                    } else {
                        RequestContext.getCurrentInstance().update("form:validacionModificar");
                        RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");
                    }
                    index = -1;
                    secRegistro = null;
                }
            } else {

                if (!crearSubCategorias.contains(filtrarSubCategorias.get(indice))) {
                    if (filtrarSubCategorias.get(indice).getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        filtrarSubCategorias.get(indice).setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < filtrarSubCategorias.size(); j++) {
                            if (j != indice) {
                                if (filtrarSubCategorias.get(indice).getCodigo() == listSubCategorias.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }
                        for (int j = 0; j < listSubCategorias.size(); j++) {
                            if (j != indice) {
                                if (filtrarSubCategorias.get(indice).getCodigo() == listSubCategorias.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            filtrarSubCategorias.get(indice).setCodigo(backUpCodigo);
                        } else {
                            pass++;
                        }

                    }

                    if (filtrarSubCategorias.get(indice).getDescripcion() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        filtrarSubCategorias.get(indice).setDescripcion(backUpDescripcion);
                    } else if (filtrarSubCategorias.get(indice).getDescripcion().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        filtrarSubCategorias.get(indice).setDescripcion(backUpDescripcion);
                    } else {
                        pass++;
                    }

                    if (pass == 2) {
                        if (modificarSubCategorias.isEmpty()) {
                            modificarSubCategorias.add(filtrarSubCategorias.get(indice));
                        } else if (!modificarSubCategorias.contains(filtrarSubCategorias.get(indice))) {
                            modificarSubCategorias.add(filtrarSubCategorias.get(indice));
                        }
                        if (guardado == true) {
                            guardado = false;
                        }

                    } else {
                        RequestContext.getCurrentInstance().update("form:validacionModificar");
                        RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");
                    }
                    index = -1;
                    secRegistro = null;
                } else {
                    if (filtrarSubCategorias.get(indice).getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        filtrarSubCategorias.get(indice).setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < filtrarSubCategorias.size(); j++) {
                            if (j != indice) {
                                if (filtrarSubCategorias.get(indice).getCodigo() == listSubCategorias.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }
                        for (int j = 0; j < listSubCategorias.size(); j++) {
                            if (j != indice) {
                                if (filtrarSubCategorias.get(indice).getCodigo() == listSubCategorias.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            filtrarSubCategorias.get(indice).setCodigo(backUpCodigo);
                        } else {
                            pass++;
                        }

                    }

                    if (filtrarSubCategorias.get(indice).getDescripcion() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        filtrarSubCategorias.get(indice).setDescripcion(backUpDescripcion);
                    } else if (filtrarSubCategorias.get(indice).getDescripcion().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        filtrarSubCategorias.get(indice).setDescripcion(backUpDescripcion);
                    } else {
                        pass++;
                    }

                    if (pass == 2) {
                        if (guardado == true) {
                            guardado = false;
                        }

                    } else {
                        RequestContext.getCurrentInstance().update("form:validacionModificar");
                        RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");
                    }
                    index = -1;
                    secRegistro = null;
                }

            }
            RequestContext.getCurrentInstance().update("form:datosSubCategoria");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }

    }

    public void borrandoSubCategorias() {

        if (index >= 0) {
            if (tipoLista == 0) {
                System.out.println("Entro a borrandoSubCategorias");
                if (!modificarSubCategorias.isEmpty() && modificarSubCategorias.contains(listSubCategorias.get(index))) {
                    int modIndex = modificarSubCategorias.indexOf(listSubCategorias.get(index));
                    modificarSubCategorias.remove(modIndex);
                    borrarSubCategorias.add(listSubCategorias.get(index));
                } else if (!crearSubCategorias.isEmpty() && crearSubCategorias.contains(listSubCategorias.get(index))) {
                    int crearIndex = crearSubCategorias.indexOf(listSubCategorias.get(index));
                    crearSubCategorias.remove(crearIndex);
                } else {
                    borrarSubCategorias.add(listSubCategorias.get(index));
                }
                listSubCategorias.remove(index);
            }
            if (tipoLista == 1) {
                System.out.println("borrandoSubCategorias ");
                if (!modificarSubCategorias.isEmpty() && modificarSubCategorias.contains(filtrarSubCategorias.get(index))) {
                    int modIndex = modificarSubCategorias.indexOf(filtrarSubCategorias.get(index));
                    modificarSubCategorias.remove(modIndex);
                    borrarSubCategorias.add(filtrarSubCategorias.get(index));
                } else if (!crearSubCategorias.isEmpty() && crearSubCategorias.contains(filtrarSubCategorias.get(index))) {
                    int crearIndex = crearSubCategorias.indexOf(filtrarSubCategorias.get(index));
                    crearSubCategorias.remove(crearIndex);
                } else {
                    borrarSubCategorias.add(filtrarSubCategorias.get(index));
                }
                int VCIndex = listSubCategorias.indexOf(filtrarSubCategorias.get(index));
                listSubCategorias.remove(VCIndex);
                filtrarSubCategorias.remove(index);

            }
            RequestContext context = RequestContext.getCurrentInstance();
            if (listSubCategorias == null || listSubCategorias.isEmpty()) {
                infoRegistro = "Cantidad de registros: 0 ";
            } else {
                infoRegistro = "Cantidad de registros: " + listSubCategorias.size();
            }
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            RequestContext.getCurrentInstance().update("form:datosSubCategoria");
            index = -1;
            secRegistro = null;

            if (guardado == true) {
                guardado = false;
            }
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }

    }

    public void verificarBorrado() {
        System.out.println("Estoy en verificarBorrado");
        BigInteger contaEvalConvocatoriasSubCategoria;

        try {
            System.err.println("Control Secuencia de ControlSubCategorias ");
            if (tipoLista == 0) {
                contaEvalConvocatoriasSubCategoria = administrarSubCategorias.contarEscalafones(listSubCategorias.get(index).getSecuencia());
            } else {
                contaEvalConvocatoriasSubCategoria = administrarSubCategorias.contarEscalafones(filtrarSubCategorias.get(index).getSecuencia());
            }
            if (contaEvalConvocatoriasSubCategoria.equals(new BigInteger("0"))) {
                System.out.println("Borrado==0");
                borrandoSubCategorias();
            } else {
                System.out.println("Borrado>0");

                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:validacionBorrar");
                RequestContext.getCurrentInstance().execute("PF('validacionBorrar').show()");
                index = -1;
                contaEvalConvocatoriasSubCategoria = new BigInteger("-1");

            }
        } catch (Exception e) {
            System.err.println("ERROR ControlSubCategorias verificarBorrado ERROR " + e);
        }
    }

    public void revisarDialogoGuardar() {

        if (!borrarSubCategorias.isEmpty() || !crearSubCategorias.isEmpty() || !modificarSubCategorias.isEmpty()) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:confirmarGuardar");
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
        }

    }

    public void guardarSubCategorias() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (guardado == false) {
            System.out.println("Realizando guardarSubCategorias");
            if (!borrarSubCategorias.isEmpty()) {
                administrarSubCategorias.borrarSubCategorias(borrarSubCategorias);

                //mostrarBorrados
                registrosBorrados = borrarSubCategorias.size();
                RequestContext.getCurrentInstance().update("form:mostrarBorrados");
                RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
                borrarSubCategorias.clear();
            }
            if (!crearSubCategorias.isEmpty()) {
                administrarSubCategorias.crearSubCategorias(crearSubCategorias);
                crearSubCategorias.clear();
            }
            if (!modificarSubCategorias.isEmpty()) {
                administrarSubCategorias.modificarSubCategorias(modificarSubCategorias);
                modificarSubCategorias.clear();
            }
            System.out.println("Se guardaron los datos con exito");
            listSubCategorias = null;
            RequestContext.getCurrentInstance().update("form:datosSubCategoria");
            k = 0;
            guardado = true;
            FacesMessage msg = new FacesMessage("Información", "Se gurdarón los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }
        index = -1;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");

    }

    public void editarCelda() {
        if (index >= 0) {
            if (tipoLista == 0) {
                editarSubCategoria = listSubCategorias.get(index);
            }
            if (tipoLista == 1) {
                editarSubCategoria = filtrarSubCategorias.get(index);
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

    public void agregarNuevoSubCategorias() {
        System.out.println("agregarNuevoSubCategorias");
        int contador = 0;
        int duplicados = 0;

        Integer a = 0;
        a = null;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        if (nuevoSubCategoria.getCodigo() == a) {
            mensajeValidacion = " *Codigo \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else {
            System.out.println("codigo en Motivo Cambio Cargo: " + nuevoSubCategoria.getCodigo());

            for (int x = 0; x < listSubCategorias.size(); x++) {
                if (listSubCategorias.get(x).getCodigo() == nuevoSubCategoria.getCodigo()) {
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
        if (nuevoSubCategoria.getDescripcion() == null) {
            mensajeValidacion = mensajeValidacion + " *Descripción \n";
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
                tamano = 270;

                System.out.println("Desactivar");
                codigo = (Column) c.getViewRoot().findComponent("form:datosSubCategoria:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosSubCategoria:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosSubCategoria");
                bandera = 0;
                filtrarSubCategorias = null;
                tipoLista = 0;
            }
            System.out.println("Despues de la bandera");

            k++;
            l = BigInteger.valueOf(k);
            nuevoSubCategoria.setSecuencia(l);

            crearSubCategorias.add(nuevoSubCategoria);

            listSubCategorias.add(nuevoSubCategoria);
            nuevoSubCategoria = new SubCategorias();
            RequestContext.getCurrentInstance().update("form:datosSubCategoria");

            infoRegistro = "Cantidad de registros: " + listSubCategorias.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }

            RequestContext.getCurrentInstance().execute("PF('nuevoRegistroSubCategorias').hide()");
            index = -1;
            secRegistro = null;

        } else {
            RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevaCentroCosto').show()");
            contador = 0;
        }
    }

    public void limpiarNuevoSubCategorias() {
        System.out.println("limpiarNuevoSubCategorias");
        nuevoSubCategoria = new SubCategorias();
        secRegistro = null;
        index = -1;

    }

    //------------------------------------------------------------------------------
    public void duplicandoSubCategorias() {
        System.out.println("duplicandoSubCategorias");
        if (index >= 0) {
            duplicarSubCategoria = new SubCategorias();
            k++;
            l = BigInteger.valueOf(k);

            if (tipoLista == 0) {
                duplicarSubCategoria.setSecuencia(l);
                duplicarSubCategoria.setCodigo(listSubCategorias.get(index).getCodigo());
                duplicarSubCategoria.setDescripcion(listSubCategorias.get(index).getDescripcion());
            }
            if (tipoLista == 1) {
                duplicarSubCategoria.setSecuencia(l);
                duplicarSubCategoria.setCodigo(filtrarSubCategorias.get(index).getCodigo());
                duplicarSubCategoria.setDescripcion(filtrarSubCategorias.get(index).getDescripcion());
            }

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTE");
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroSubCategorias').show()");
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
        Integer a = 0;
        a = null;
        System.err.println("ConfirmarDuplicar codigo " + duplicarSubCategoria.getCodigo());
        System.err.println("ConfirmarDuplicar Descripcion " + duplicarSubCategoria.getDescripcion());

        if (duplicarSubCategoria.getCodigo() == a) {
            mensajeValidacion = mensajeValidacion + "   *Codigo \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else {
            for (int x = 0; x < listSubCategorias.size(); x++) {
                if (listSubCategorias.get(x).getCodigo() == duplicarSubCategoria.getCodigo()) {
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
        if (duplicarSubCategoria.getDescripcion() == null) {
            mensajeValidacion = mensajeValidacion + "   *Descripcion \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("Bandera : ");
            contador++;
        }

        if (contador == 2) {

            System.out.println("Datos Duplicando: " + duplicarSubCategoria.getSecuencia() + "  " + duplicarSubCategoria.getCodigo());
            if (crearSubCategorias.contains(duplicarSubCategoria)) {
                System.out.println("Ya lo contengo.");
            }
            listSubCategorias.add(duplicarSubCategoria);
            crearSubCategorias.add(duplicarSubCategoria);
            RequestContext.getCurrentInstance().update("form:datosSubCategoria");
            index = -1;
            secRegistro = null;
            if (guardado == true) {
                guardado = false;
            }
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            infoRegistro = "Cantidad de registros: " + listSubCategorias.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                tamano = 270;

                //CERRAR FILTRADO
                codigo = (Column) c.getViewRoot().findComponent("form:datosSubCategoria:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosSubCategoria:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosSubCategoria");
                bandera = 0;
                filtrarSubCategorias = null;
                tipoLista = 0;
            }
            duplicarSubCategoria = new SubCategorias();
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroSubCategorias').hide()");

        } else {
            contador = 0;
            RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
            RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
        }
    }

    public void limpiarDuplicarSubCategorias() {
        duplicarSubCategoria = new SubCategorias();
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosSubCategoriaExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "SUBCATEGORIAS", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosSubCategoriaExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "SUBCATEGORIAS", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        System.out.println("lol");
        if (!listSubCategorias.isEmpty()) {
            if (secRegistro != null) {
                System.out.println("lol 2");
                int resultado = administrarRastros.obtenerTabla(secRegistro, "SUBCATEGORIAS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
            if (administrarRastros.verificarHistoricosTabla("SUBCATEGORIAS")) { // igual acá
                RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
            } else {
                RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
            }

        }
        index = -1;
    }

    //*/*/*/*/*/*/*/*/*/*-/-*//-*/-*/*/*-*/-*/-*/*/*/*/*/---/*/*/*/*/-*/-*/-*/-*/-*/
    public List<SubCategorias> getListSubCategorias() {
        if (listSubCategorias == null) {
            listSubCategorias = administrarSubCategorias.consultarSubCategorias();
        }
        RequestContext context = RequestContext.getCurrentInstance();
        if (listSubCategorias == null || listSubCategorias.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listSubCategorias.size();
        }
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
        return listSubCategorias;
    }

    public void setListSubCategorias(List<SubCategorias> listSubCategorias) {
        this.listSubCategorias = listSubCategorias;
    }

    public List<SubCategorias> getFiltrarSubCategorias() {
        return filtrarSubCategorias;
    }

    public void setFiltrarSubCategorias(List<SubCategorias> filtrarSubCategorias) {
        this.filtrarSubCategorias = filtrarSubCategorias;
    }

    public SubCategorias getNuevoSubCategoria() {
        return nuevoSubCategoria;
    }

    public void setNuevoSubCategoria(SubCategorias nuevoSubCategoria) {
        this.nuevoSubCategoria = nuevoSubCategoria;
    }

    public SubCategorias getDuplicarSubCategoria() {
        return duplicarSubCategoria;
    }

    public void setDuplicarSubCategoria(SubCategorias duplicarSubCategoria) {
        this.duplicarSubCategoria = duplicarSubCategoria;
    }

    public SubCategorias getEditarSubCategoria() {
        return editarSubCategoria;
    }

    public void setEditarSubCategoria(SubCategorias editarSubCategoria) {
        this.editarSubCategoria = editarSubCategoria;
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

    public SubCategorias getSubCategoriaSeleccionada() {
        return subCategoriaSeleccionada;
    }

    public void setSubCategoriaSeleccionada(SubCategorias subCategoriaSeleccionada) {
        this.subCategoriaSeleccionada = subCategoriaSeleccionada;
    }

    public String getInfoRegistro() {
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

}
