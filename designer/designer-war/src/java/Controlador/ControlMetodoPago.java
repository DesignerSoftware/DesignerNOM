/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;


import Entidades.MetodosPagos;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarMetodosPagosInterface;
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
public class ControlMetodoPago implements Serializable {

    @EJB
    AdministrarMetodosPagosInterface administrarMetodosPagos;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    List<MetodosPagos> listMetodosPagos;
    List<MetodosPagos> filtrarMetodosPagos;
    List<MetodosPagos> borrarMetodosPagos;
    List<MetodosPagos> crearMetodosPagos;
    List<MetodosPagos> modificarMetodosPagos;
    MetodosPagos editarMetodoPago;
    MetodosPagos nuevoMetodoPago;
    MetodosPagos duplicarMetodoPago;
    MetodosPagos metodoPagoSeleccionado;
    //otros
    private int cualCelda, tipoLista, index, tipoActualizacion, k, bandera;
    private BigInteger l;
    private boolean aceptar, guardado;
    //AutoCompletar
    private boolean permitirIndex;
    //RASTRO
    private BigInteger secRegistro;
    private Column codigo, descripcion, pago;
    //borrado
    private int registrosBorrados;
    private String mensajeValidacion;
    private Integer a;
    private int tamano;

    public ControlMetodoPago() {
        listMetodosPagos = null;
        crearMetodosPagos = new ArrayList<MetodosPagos>();
        modificarMetodosPagos = new ArrayList<MetodosPagos>();
        borrarMetodosPagos = new ArrayList<MetodosPagos>();
        permitirIndex = true;
        guardado = true;
        editarMetodoPago = new MetodosPagos();
        nuevoMetodoPago = new MetodosPagos();
        a = null;
        tamano = 270;
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarMetodosPagos.obtenerConexion(ses.getId());
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
            infoRegistro = "Cantidad de registros: " + filtrarMetodosPagos.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
        } catch (Exception e) {
            System.out.println("ERROR ControlMotiviosCambiosCargos eventoFiltrar ERROR===" + e.getMessage());
        }
    }
    private Integer backUpCodigo;
    private String backUpDescripcion;

    public void cambiarIndice(int indice, int celda) {
        System.err.println("TIPO LISTA = " + tipoLista);

        if (permitirIndex == true) {
            index = indice;
            cualCelda = celda;
            if (cualCelda == 0) {
                if (tipoLista == 0) {
                    backUpCodigo = listMetodosPagos.get(index).getCodigo();
                } else {
                    backUpCodigo = filtrarMetodosPagos.get(index).getCodigo();
                }
            } else if (cualCelda == 1) {
                if (tipoLista == 0) {
                    backUpDescripcion = listMetodosPagos.get(index).getDescripcion();
                } else {
                    backUpDescripcion = filtrarMetodosPagos.get(index).getDescripcion();
                }
            }
            secRegistro = listMetodosPagos.get(index).getSecuencia();
            System.out.println("Indice: " + index + " Celda: " + cualCelda);
        }

    }

    public void asignarIndex(Integer indice, int LND, int dig) {
        try {
            System.out.println("\n ENTRE A ControlMetodoPago.asignarIndex \n");
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
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 1) {
            //CERRAR FILTRADO
            codigo = (Column) c.getViewRoot().findComponent("form:datosMetodoPago:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosMetodoPago:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            pago = (Column) c.getViewRoot().findComponent("form:datosMetodoPago:pago");
            pago.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosMetodoPago");
            bandera = 0;
            filtrarMetodosPagos = null;
            tipoLista = 0;
        }

        borrarMetodosPagos.clear();
        crearMetodosPagos.clear();
        modificarMetodosPagos.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listMetodosPagos = null;
        guardado = true;
        permitirIndex = true;
        getListMetodosPagos();
        RequestContext context = RequestContext.getCurrentInstance();
        if (listMetodosPagos == null || listMetodosPagos.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listMetodosPagos.size();
        }
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
        RequestContext.getCurrentInstance().update("form:datosMetodoPago");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void salir() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 1) {
            //CERRAR FILTRADO
            codigo = (Column) c.getViewRoot().findComponent("form:datosMetodoPago:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosMetodoPago:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            pago = (Column) c.getViewRoot().findComponent("form:datosMetodoPago:pago");
            pago.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosMetodoPago");
            bandera = 0;
            filtrarMetodosPagos = null;
            tipoLista = 0;
        }

        borrarMetodosPagos.clear();
        crearMetodosPagos.clear();
        modificarMetodosPagos.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listMetodosPagos = null;
        guardado = true;
        permitirIndex = true;
        getListMetodosPagos();
        RequestContext context = RequestContext.getCurrentInstance();
        if (listMetodosPagos == null || listMetodosPagos.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listMetodosPagos.size();
        }
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
        RequestContext.getCurrentInstance().update("form:datosMetodoPago");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {

            tamano = 250;
            codigo = (Column) c.getViewRoot().findComponent("form:datosMetodoPago:codigo");
            codigo.setFilterStyle("width: 85% !important;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosMetodoPago:descripcion");
            descripcion.setFilterStyle("width: 85% !important;");
            pago = (Column) c.getViewRoot().findComponent("form:datosMetodoPago:pago");
            pago.setFilterStyle("width: 85% !important;");
            RequestContext.getCurrentInstance().update("form:datosMetodoPago");
            System.out.println("Activar");
            bandera = 1;
        } else if (bandera == 1) {
            tamano = 270;
            System.out.println("Desactivar");
            codigo = (Column) c.getViewRoot().findComponent("form:datosMetodoPago:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosMetodoPago:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            pago = (Column) c.getViewRoot().findComponent("form:datosMetodoPago:pago");
            pago.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosMetodoPago");
            bandera = 0;
            filtrarMetodosPagos = null;
            tipoLista = 0;
        }
    }

    public void modificarMetodosPagos(int indice, String confirmarCambio, String valorConfirmar) {
        System.err.println("ENTRE A MODIFICAR METODOS PAGOS");
        index = indice;

        int contador = 0;
        boolean banderita = false;

        RequestContext context = RequestContext.getCurrentInstance();
        System.err.println("TIPO LISTA = " + tipoLista);
        if (confirmarCambio.equalsIgnoreCase("N")) {
            System.err.println("ENTRE A MODIFICAR METODOS PAGOS, CONFIRMAR CAMBIO ES N");
            if (tipoLista == 0) {
                if (!crearMetodosPagos.contains(listMetodosPagos.get(indice))) {
                    if (listMetodosPagos.get(indice).getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        listMetodosPagos.get(indice).setCodigo(backUpCodigo);
                        banderita = false;
                    } else {
                        for (int j = 0; j < listMetodosPagos.size(); j++) {
                            if (j != indice) {
                                if (listMetodosPagos.get(indice).getCodigo() == listMetodosPagos.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            listMetodosPagos.get(indice).setCodigo(backUpCodigo);
                            banderita = false;
                        } else {
                            banderita = true;
                        }

                    }
                    if (listMetodosPagos.get(indice).getDescripcion().isEmpty()) {
                        listMetodosPagos.get(indice).setDescripcion(backUpDescripcion);
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                    }
                    if (listMetodosPagos.get(indice).getDescripcion().equals(" ")) {
                        listMetodosPagos.get(indice).setDescripcion(backUpDescripcion);
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                    }

                    if (banderita == true) {
                        if (modificarMetodosPagos.isEmpty()) {
                            modificarMetodosPagos.add(listMetodosPagos.get(indice));
                        } else if (!modificarMetodosPagos.contains(listMetodosPagos.get(indice))) {
                            modificarMetodosPagos.add(listMetodosPagos.get(indice));
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
                    if (listMetodosPagos.get(indice).getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        listMetodosPagos.get(indice).setCodigo(backUpCodigo);
                        banderita = false;
                    } else {
                        for (int j = 0; j < listMetodosPagos.size(); j++) {
                            if (j != indice) {
                                if (listMetodosPagos.get(indice).getCodigo() == listMetodosPagos.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            listMetodosPagos.get(indice).setCodigo(backUpCodigo);
                            banderita = false;
                        } else {
                            banderita = true;
                        }

                    }
                    if (listMetodosPagos.get(indice).getDescripcion().isEmpty()) {
                        listMetodosPagos.get(indice).setDescripcion(backUpDescripcion);
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                    }
                    if (listMetodosPagos.get(indice).getDescripcion().equals(" ")) {
                        listMetodosPagos.get(indice).setDescripcion(backUpDescripcion);
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                    }

                    if (banderita == true) {
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

                if (!crearMetodosPagos.contains(filtrarMetodosPagos.get(indice))) {
                    if (filtrarMetodosPagos.get(indice).getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        filtrarMetodosPagos.get(indice).setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < listMetodosPagos.size(); j++) {
                            if (j != indice) {
                                if (filtrarMetodosPagos.get(indice).getCodigo() == listMetodosPagos.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            filtrarMetodosPagos.get(indice).setCodigo(backUpCodigo);
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            banderita = false;
                        } else {
                            banderita = true;
                        }

                    }

                    if (filtrarMetodosPagos.get(indice).getDescripcion().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        filtrarMetodosPagos.get(indice).setDescripcion(backUpDescripcion);
                        banderita = false;
                    }
                    if (filtrarMetodosPagos.get(indice).getDescripcion().equals(" ")) {
                        filtrarMetodosPagos.get(indice).setDescripcion(backUpDescripcion);
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                    }

                    if (banderita == true) {
                        if (modificarMetodosPagos.isEmpty()) {
                            modificarMetodosPagos.add(filtrarMetodosPagos.get(indice));
                        } else if (!modificarMetodosPagos.contains(filtrarMetodosPagos.get(indice))) {
                            modificarMetodosPagos.add(filtrarMetodosPagos.get(indice));
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
                    if (filtrarMetodosPagos.get(indice).getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        filtrarMetodosPagos.get(indice).setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < listMetodosPagos.size(); j++) {
                            if (j != indice) {
                                if (filtrarMetodosPagos.get(indice).getCodigo() == listMetodosPagos.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            filtrarMetodosPagos.get(indice).setCodigo(backUpCodigo);
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            banderita = false;
                        } else {
                            banderita = true;
                        }

                    }

                    if (filtrarMetodosPagos.get(indice).getDescripcion().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        filtrarMetodosPagos.get(indice).setDescripcion(backUpDescripcion);
                        banderita = false;
                    }
                    if (filtrarMetodosPagos.get(indice).getDescripcion().equals(" ")) {
                        filtrarMetodosPagos.get(indice).setDescripcion(backUpDescripcion);
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                    }

                    if (banderita == true) {
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
            RequestContext.getCurrentInstance().update("form:datosMetodoPago");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }

    }
    private BigInteger verificarVigenciasFormasPagos;

    public void verificarBorrado() {
        System.out.println("Estoy en verificarBorrado");
        try {
            System.err.println("Control Secuencia de ControlTiposTallas ");
            if (tipoLista == 0) {
                verificarVigenciasFormasPagos = administrarMetodosPagos.verificarMetodosPagosVigenciasFormasPagos(listMetodosPagos.get(index).getSecuencia());
            } else {
                verificarVigenciasFormasPagos = administrarMetodosPagos.verificarMetodosPagosVigenciasFormasPagos(filtrarMetodosPagos.get(index).getSecuencia());
            }
            if (verificarVigenciasFormasPagos.equals(new BigInteger("0"))) {
                System.out.println("Borrado==0");
                borrandoMetodoPago();
            } else {
                System.out.println("Borrado>0");

                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:validacionBorrar");
                RequestContext.getCurrentInstance().execute("PF('validacionBorrar').show()");
                index = -1;

                verificarVigenciasFormasPagos = new BigInteger("-1");

            }
        } catch (Exception e) {
            System.err.println("ERROR ControlTiposTallas verificarBorrado ERROR " + e);
        }
    }

    public void borrandoMetodoPago() {

        if (index >= 0) {

            if (tipoLista == 0) {
                System.out.println("Entro a borrarMetodoPago");
                if (!modificarMetodosPagos.isEmpty() && modificarMetodosPagos.contains(listMetodosPagos.get(index))) {
                    int modIndex = modificarMetodosPagos.indexOf(listMetodosPagos.get(index));
                    modificarMetodosPagos.remove(modIndex);
                    borrarMetodosPagos.add(listMetodosPagos.get(index));
                } else if (!crearMetodosPagos.isEmpty() && crearMetodosPagos.contains(listMetodosPagos.get(index))) {
                    int crearIndex = crearMetodosPagos.indexOf(listMetodosPagos.get(index));
                    crearMetodosPagos.remove(crearIndex);
                } else {
                    borrarMetodosPagos.add(listMetodosPagos.get(index));
                }
                listMetodosPagos.remove(index);
            }
            if (tipoLista == 1) {
                System.out.println("borrarMetodoPago ");
                if (!modificarMetodosPagos.isEmpty() && modificarMetodosPagos.contains(filtrarMetodosPagos.get(index))) {
                    int modIndex = modificarMetodosPagos.indexOf(filtrarMetodosPagos.get(index));
                    modificarMetodosPagos.remove(modIndex);
                    borrarMetodosPagos.add(filtrarMetodosPagos.get(index));
                } else if (!crearMetodosPagos.isEmpty() && crearMetodosPagos.contains(filtrarMetodosPagos.get(index))) {
                    int crearIndex = crearMetodosPagos.indexOf(filtrarMetodosPagos.get(index));
                    crearMetodosPagos.remove(crearIndex);
                } else {
                    borrarMetodosPagos.add(filtrarMetodosPagos.get(index));
                }
                int VCIndex = listMetodosPagos.indexOf(filtrarMetodosPagos.get(index));
                listMetodosPagos.remove(VCIndex);
                filtrarMetodosPagos.remove(index);

            }
            RequestContext context = RequestContext.getCurrentInstance();

            infoRegistro = "Cantidad de registros: " + listMetodosPagos.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            RequestContext.getCurrentInstance().update("form:datosMetodoPago");
            index = -1;
            secRegistro = null;

            if (guardado == true) {
                guardado = false;
            }
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }

    }

    public void guardarMetodosPagos() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (guardado == false) {
            System.out.println("Realizando Operaciones Vigencias Localizacion");
            if (!borrarMetodosPagos.isEmpty()) {
                administrarMetodosPagos.borrarMetodosPagos(borrarMetodosPagos);
                //mostrarBorrados
                registrosBorrados = borrarMetodosPagos.size();
                RequestContext.getCurrentInstance().update("form:mostrarBorrados");
                RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
                borrarMetodosPagos.clear();
            }
            if (!crearMetodosPagos.isEmpty()) {
                administrarMetodosPagos.crearMetodosPagos(crearMetodosPagos);
                crearMetodosPagos.clear();
            }
            if (!modificarMetodosPagos.isEmpty()) {
                administrarMetodosPagos.modificarMetodosPagos(modificarMetodosPagos);
                modificarMetodosPagos.clear();
            }
            System.out.println("Se guardaron los datos con exito");
            listMetodosPagos = null;
            RequestContext.getCurrentInstance().update("form:datosMetodoPago");
            k = 0;
            FacesMessage msg = new FacesMessage("Información", "Se guardarón los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }
        index = -1;
        guardado = true;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");

    }

    public void editarCelda() {
        if (index >= 0) {
            if (tipoLista == 0) {
                editarMetodoPago = listMetodosPagos.get(index);
            }
            if (tipoLista == 1) {
                editarMetodoPago = filtrarMetodosPagos.get(index);
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

    public void agregarNuevoMetodoPago() {
        System.out.println("Agregar Metodo Pago");
        int contador = 0;
        int duplicados = 0;

        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        System.err.println("1");
        if (nuevoMetodoPago.getCodigo() == a) {
            mensajeValidacion = " *Codigo \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else {
            System.out.println("codigo Metodo: " + nuevoMetodoPago.getCodigo());

            for (int x = 0; x < listMetodosPagos.size(); x++) {
                if (listMetodosPagos.get(x).getCodigo() == nuevoMetodoPago.getCodigo()) {
                    duplicados++;
                }
            }
            System.err.println("2");
            System.out.println("Antes del if Duplicados eses igual  : " + duplicados);

            if (duplicados > 0) {
                mensajeValidacion = " *Que NO Hayan Codigos Repetidos \n";
                System.out.println("Mensaje validacion : " + mensajeValidacion);
            } else {
                System.out.println("bandera");
                contador++;
            }
        }
        if (nuevoMetodoPago.getDescripcion() == null || nuevoMetodoPago.getDescripcion().isEmpty() || nuevoMetodoPago.getDescripcion().equals(" ")) {
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
                codigo = (Column) c.getViewRoot().findComponent("form:datosMetodoPago:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosMetodoPago:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                pago = (Column) c.getViewRoot().findComponent("form:datosMetodoPago:pago");
                pago.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosMetodoPago");
                bandera = 0;
                filtrarMetodosPagos = null;
                tipoLista = 0;
            }
            System.out.println("Despues de la bandera");

            //AGREGAR REGISTRO A LA LISTA VIGENCIAS CARGOS EMPLEADO.
            k++;
            l = BigInteger.valueOf(k);
            nuevoMetodoPago.setSecuencia(l);
            System.err.println("nuevometodopago secuencia " + nuevoMetodoPago.getSecuencia());

            crearMetodosPagos.add(nuevoMetodoPago);
            listMetodosPagos.add(nuevoMetodoPago);
            nuevoMetodoPago = new MetodosPagos();
            infoRegistro = "Cantidad de registros: " + listMetodosPagos.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            RequestContext.getCurrentInstance().update("form:datosMetodoPago");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            System.out.println("Despues de la bandera guardado");

            RequestContext.getCurrentInstance().execute("PF('nuevoRegistroMetodosPagos').hide()");
            index = -1;
            secRegistro = null;
            System.out.println("Despues de nuevoRegistroMetodosPagos");

        } else {
            RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevaCentroCosto').show()");
            contador = 0;
        }
    }

    public void limpiarNuevoMetodosPagos() {
        System.out.println("limpiarNuevoMetodosPagos");
        nuevoMetodoPago = new MetodosPagos();
        secRegistro = null;
        index = -1;

    }

    public void duplicarMetodosPagos() {
        System.out.println("duplicarMetodosPagos");
        if (index >= 0) {
            duplicarMetodoPago = new MetodosPagos();
            k++;
            l = BigInteger.valueOf(k);

            if (tipoLista == 0) {
                duplicarMetodoPago.setSecuencia(null);
                duplicarMetodoPago.setCodigo(listMetodosPagos.get(index).getCodigo());
                duplicarMetodoPago.setDescripcion(listMetodosPagos.get(index).getDescripcion());
                duplicarMetodoPago.setPago(listMetodosPagos.get(index).getPago());
            }
            if (tipoLista == 1) {
                duplicarMetodoPago.setSecuencia(l);
                duplicarMetodoPago.setCodigo(filtrarMetodosPagos.get(index).getCodigo());
                duplicarMetodoPago.setDescripcion(filtrarMetodosPagos.get(index).getDescripcion());
                duplicarMetodoPago.setPago(filtrarMetodosPagos.get(index).getPago());
            }

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarMetodosPagos");
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroMetodosPagos').show()");
            index = -1;
            secRegistro = null;
        }
    }

    public void confirmarDuplicar() {
        System.out.println("Agregar Metodo Pago");
        int contador = 0;
        int duplicados = 0;

        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        System.err.println("1");
        if (duplicarMetodoPago.getCodigo() == a) {
            mensajeValidacion = " *Codigo \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else {
            System.out.println("codigo Metodo: " + duplicarMetodoPago.getCodigo());

            for (int x = 0; x < listMetodosPagos.size(); x++) {
                if (listMetodosPagos.get(x).getCodigo() == duplicarMetodoPago.getCodigo()) {
                    duplicados++;
                }
            }
            System.err.println("2");
            System.out.println("Antes del if Duplicados eses igual  : " + duplicados);

            if (duplicados > 0) {
                mensajeValidacion = " *Que NO Hayan Codigos Repetidos \n";
                System.out.println("Mensaje validacion : " + mensajeValidacion);
            } else {
                System.out.println("bandera");
                contador++;
            }
        }
        if (duplicarMetodoPago.getDescripcion() == null || duplicarMetodoPago.getDescripcion().isEmpty()) {
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
                codigo = (Column) c.getViewRoot().findComponent("form:datosMetodoPago:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosMetodoPago:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                pago = (Column) c.getViewRoot().findComponent("form:datosMetodoPago:pago");
                pago.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosMetodoPago");
                bandera = 0;
                filtrarMetodosPagos = null;
                tipoLista = 0;
            }
            System.out.println("Despues de la bandera");

            //AGREGAR REGISTRO A LA LISTA VIGENCIAS CARGOS EMPLEADO.
            k++;
            l = BigInteger.valueOf(k);
            duplicarMetodoPago.setSecuencia(l);
            System.err.println("nuevometodopago secuencia " + duplicarMetodoPago.getSecuencia());

            crearMetodosPagos.add(duplicarMetodoPago);
            listMetodosPagos.add(duplicarMetodoPago);
            nuevoMetodoPago = new MetodosPagos();
            infoRegistro = "Cantidad de registros: " + listMetodosPagos.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            RequestContext.getCurrentInstance().update("form:datosMetodoPago");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            System.out.println("Despues de la bandera guardado");

            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroMetodosPagos').hide()");
            index = -1;
            secRegistro = null;
            System.out.println("Despues de duplicarRegistroMetodosPagos");

        } else {
            RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevaCentroCosto').show()");
            contador = 0;
        }
    }

    public void limpiarduplicarMetodoPago() {
        duplicarMetodoPago = new MetodosPagos();
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosMetodoPagoExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "MetodosPagos", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosMetodoPagoExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "MetodosPagos", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        System.out.println("lol");
        if (!listMetodosPagos.isEmpty()) {
            if (secRegistro != null) {
                System.out.println("lol 2");
                int resultado = administrarRastros.obtenerTabla(secRegistro, "METODOSPAGOS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
            if (administrarRastros.verificarHistoricosTabla("METODOSPAGOS")) { // igual acá
                RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
            } else {
                RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
            }

        }
        index = -1;
    }
    private String infoRegistro;
//*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*

    public List<MetodosPagos> getListMetodosPagos() {
        if (listMetodosPagos == null) {
            listMetodosPagos = administrarMetodosPagos.consultarMetodosPagos();
            for (int i = 0; i < listMetodosPagos.size(); i++) {
                System.out.println(listMetodosPagos.get(i).getSecuencia());
            }
        }
        RequestContext context = RequestContext.getCurrentInstance();

        if (listMetodosPagos == null || listMetodosPagos.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listMetodosPagos.size();
        }
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
        return listMetodosPagos;
    }

    public void setListMetodosPagos(List<MetodosPagos> listMetodosPagos) {
        this.listMetodosPagos = listMetodosPagos;
    }

    public String getMensajeValidacion() {
        return mensajeValidacion;
    }

    public void setMensajeValidacion(String mensajeValidacion) {
        this.mensajeValidacion = mensajeValidacion;
    }

    public List<MetodosPagos> getFiltrarMetodosPagos() {
        return filtrarMetodosPagos;
    }

    public void setFiltrarMetodosPagos(List<MetodosPagos> filtrarMetodosPagos) {
        this.filtrarMetodosPagos = filtrarMetodosPagos;
    }

    public int getRegistrosBorrados() {
        return registrosBorrados;
    }

    public void setRegistrosBorrados(int registrosBorrados) {
        this.registrosBorrados = registrosBorrados;
    }

    public MetodosPagos getEditarMetodoPago() {
        return editarMetodoPago;
    }

    public void setEditarMetodoPago(MetodosPagos editarMetodoPago) {
        this.editarMetodoPago = editarMetodoPago;
    }

    public MetodosPagos getNuevoMetodoPago() {
        return nuevoMetodoPago;
    }

    public void setNuevoMetodoPago(MetodosPagos nuevoMetodoPago) {
        this.nuevoMetodoPago = nuevoMetodoPago;
    }

    public MetodosPagos getDuplicarMetodoPago() {
        return duplicarMetodoPago;
    }

    public void setDuplicarMetodoPago(MetodosPagos duplicarMetodoPago) {
        this.duplicarMetodoPago = duplicarMetodoPago;
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

    public MetodosPagos getMetodoPagoSeleccionado() {
        return metodoPagoSeleccionado;
    }

    public void setMetodoPagoSeleccionado(MetodosPagos metodoPagoSeleccionado) {
        this.metodoPagoSeleccionado = metodoPagoSeleccionado;
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
