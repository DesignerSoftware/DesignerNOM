/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import utilidadesUI.PrimefacesContextUI;
import Entidades.Monedas;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarMonedasInterface;
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
public class ControlMonedas implements Serializable {

    @EJB
    AdministrarMonedasInterface administrarMonedas;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    private List<Monedas> listMonedas;
    private List<Monedas> filtrarMonedas;
    private List<Monedas> crearMonedas;
    private List<Monedas> modificarMonedas;
    private List<Monedas> borrarMonedas;
    private Monedas nuevoMoneda;
    private Monedas duplicarMoneda;
    private Monedas editarMoneda;
    private Monedas monedaSeleccionada;
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
    private BigInteger proyectos;
    private String a;
    private String backUpCodigo;
    private String backUpMoneda;
    private int tamano;

    public ControlMonedas() {
        listMonedas = null;
        crearMonedas = new ArrayList<Monedas>();
        modificarMonedas = new ArrayList<Monedas>();
        borrarMonedas = new ArrayList<Monedas>();
        permitirIndex = true;
        editarMoneda = new Monedas();
        nuevoMoneda = new Monedas();
        duplicarMoneda = new Monedas();
        a = null;
        tamano = 270;
        guardado = true;
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarMonedas.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void eventoFiltrar() {
        try {
            System.out.println("\n ENTRE A ControlMonedas.eventoFiltrar \n");
            if (tipoLista == 0) {
                tipoLista = 1;
            }
            RequestContext context = RequestContext.getCurrentInstance();
            infoRegistro = "Cantidad de registros: " + filtrarMonedas.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
        } catch (Exception e) {
            System.out.println("ERROR ControlMonedas eventoFiltrar ERROR===" + e.getMessage());
        }
    }

    public void cambiarIndice(int indice, int celda) {
        System.err.println("TIPO LISTA = " + tipoLista);

        if (permitirIndex == true) {
            index = indice;
            cualCelda = celda;
            if (cualCelda == 0) {
                if (tipoLista == 0) {
                    backUpCodigo = listMonedas.get(index).getCodigo();
                } else {
                    backUpCodigo = filtrarMonedas.get(index).getCodigo();
                }
            }
            if (cualCelda == 1) {
                if (tipoLista == 0) {
                    backUpMoneda = listMonedas.get(index).getNombre();
                } else {
                    backUpMoneda = filtrarMonedas.get(index).getNombre();
                }
            }
            secRegistro = listMonedas.get(index).getSecuencia();

        }
        System.out.println("Indice: " + index + " Celda: " + cualCelda);
    }

    public void asignarIndex(Integer indice, int LND, int dig) {
        try {
            System.out.println("\n ENTRE A ControlMonedas.asignarIndex \n");
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
            System.out.println("ERROR ControlMonedas.asignarIndex ERROR======" + e.getMessage());
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
            codigo = (Column) c.getViewRoot().findComponent("form:datosMoneda:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosMoneda:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosMoneda");
            bandera = 0;
            filtrarMonedas = null;
            tipoLista = 0;
        }

        borrarMonedas.clear();
        crearMonedas.clear();
        modificarMonedas.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listMonedas = null;
        guardado = true;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        getListMonedas();
        if (listMonedas == null || listMonedas.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listMonedas.size();
        }
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
        RequestContext.getCurrentInstance().update("form:datosMoneda");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void salir() {
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            //CERRAR FILTRADO
            codigo = (Column) c.getViewRoot().findComponent("form:datosMoneda:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosMoneda:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosMoneda");
            bandera = 0;
            filtrarMonedas = null;
            tipoLista = 0;
        }

        borrarMonedas.clear();
        crearMonedas.clear();
        modificarMonedas.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listMonedas = null;
        guardado = true;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        getListMonedas();
        if (listMonedas == null || listMonedas.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listMonedas.size();
        }
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
        RequestContext.getCurrentInstance().update("form:datosMoneda");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            tamano = 250;
            codigo = (Column) c.getViewRoot().findComponent("form:datosMoneda:codigo");
            codigo.setFilterStyle("width: 85%;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosMoneda:descripcion");
            descripcion.setFilterStyle("width: 85%;");
            RequestContext.getCurrentInstance().update("form:datosMoneda");
            System.out.println("Activar");
            bandera = 1;
        } else if (bandera == 1) {
            tamano = 270;
            System.out.println("Desactivar");
            codigo = (Column) c.getViewRoot().findComponent("form:datosMoneda:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosMoneda:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosMoneda");
            bandera = 0;
            filtrarMonedas = null;
            tipoLista = 0;
        }
    }

    public void modificarMoneda(int indice, String confirmarCambio, String valorConfirmar) {
        System.err.println("ENTRE A MODIFICAR MONEDA");
        index = indice;

        int contador = 0, pass = 0;

        RequestContext context = RequestContext.getCurrentInstance();
        System.err.println("TIPO LISTA = " + tipoLista);
        if (confirmarCambio.equalsIgnoreCase("N")) {
            System.err.println("ENTRE A MODIFICAR IDIOMA, CONFIRMAR CAMBIO ES N");
            if (tipoLista == 0) {
                if (!crearMonedas.contains(listMonedas.get(indice))) {
                    if (listMonedas.get(indice).getCodigo() == null || listMonedas.get(indice).getCodigo().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        listMonedas.get(indice).setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < listMonedas.size(); j++) {
                            if (j != indice) {
                                if (listMonedas.get(indice).getCodigo().equals(listMonedas.get(j).getCodigo())) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            listMonedas.get(indice).setCodigo(backUpCodigo);
                        } else {
                            pass++;
                        }

                    }
                    if (listMonedas.get(indice).getNombre() == null) {
                        listMonedas.get(indice).setNombre(backUpMoneda);
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    } else if (listMonedas.get(indice).getNombre().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        listMonedas.get(indice).setNombre(backUpMoneda);

                    } else {
                        pass++;
                    }

                    if (pass == 2) {
                        if (modificarMonedas.isEmpty()) {
                            modificarMonedas.add(listMonedas.get(indice));
                        } else if (!modificarMonedas.contains(listMonedas.get(indice))) {
                            modificarMonedas.add(listMonedas.get(indice));
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
                    if (listMonedas.get(indice).getCodigo() == null || listMonedas.get(indice).getCodigo().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        listMonedas.get(indice).setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < listMonedas.size(); j++) {
                            if (j != indice) {
                                if (listMonedas.get(indice).getCodigo().equals(listMonedas.get(j).getCodigo())) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            listMonedas.get(indice).setCodigo(backUpCodigo);
                        } else {
                            pass++;
                        }

                    }
                    if (listMonedas.get(indice).getNombre() == null) {
                        listMonedas.get(indice).setNombre(backUpMoneda);
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    } else if (listMonedas.get(indice).getNombre().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        listMonedas.get(indice).setNombre(backUpMoneda);

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

                if (!crearMonedas.contains(filtrarMonedas.get(indice))) {
                    if (filtrarMonedas.get(indice).getCodigo() == null || filtrarMonedas.get(indice).getCodigo().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        filtrarMonedas.get(indice).setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < listMonedas.size(); j++) {
                            if (j != indice) {
                                if (filtrarMonedas.get(indice).getCodigo() == listMonedas.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }

                        if (contador > 0) {
                            filtrarMonedas.get(indice).setCodigo(backUpCodigo);
                            mensajeValidacion = "CODIGOS REPETIDOS";
                        } else {
                            pass++;
                        }

                    }

                    if (filtrarMonedas.get(indice).getNombre() == null) {
                        filtrarMonedas.get(indice).setNombre(backUpMoneda);
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    } else if (filtrarMonedas.get(indice).getNombre().isEmpty()) {
                        filtrarMonedas.get(indice).setNombre(backUpMoneda);
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    } else {
                        pass++;
                    }

                    if (pass == 2) {
                        if (modificarMonedas.isEmpty()) {
                            modificarMonedas.add(filtrarMonedas.get(indice));
                        } else if (!modificarMonedas.contains(filtrarMonedas.get(indice))) {
                            modificarMonedas.add(filtrarMonedas.get(indice));
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
                    if (filtrarMonedas.get(indice).getCodigo() == null || filtrarMonedas.get(indice).getCodigo().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        filtrarMonedas.get(indice).setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < listMonedas.size(); j++) {
                            if (j != indice) {
                                if (listMonedas.get(indice).getCodigo() == listMonedas.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }

                        if (contador > 0) {
                            filtrarMonedas.get(indice).setCodigo(backUpCodigo);
                            mensajeValidacion = "CODIGOS REPETIDOS";
                        } else {
                            pass++;
                        }

                    }
                    if (filtrarMonedas.get(indice).getNombre() == null) {
                        filtrarMonedas.get(indice).setNombre(backUpMoneda);
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    } else if (filtrarMonedas.get(indice).getNombre().isEmpty()) {
                        filtrarMonedas.get(indice).setNombre(backUpMoneda);
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
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
            RequestContext.getCurrentInstance().update("form:datosMoneda");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }

    }

    public void borrandoMonedas() {

        if (index >= 0) {
            if (tipoLista == 0) {
                System.out.println("Entro a borrarandoMonedas");
                if (!modificarMonedas.isEmpty() && modificarMonedas.contains(listMonedas.get(index))) {
                    int modIndex = modificarMonedas.indexOf(listMonedas.get(index));
                    modificarMonedas.remove(modIndex);
                    borrarMonedas.add(listMonedas.get(index));
                } else if (!crearMonedas.isEmpty() && crearMonedas.contains(listMonedas.get(index))) {
                    int crearIndex = crearMonedas.indexOf(listMonedas.get(index));
                    crearMonedas.remove(crearIndex);
                } else {
                    borrarMonedas.add(listMonedas.get(index));
                }
                listMonedas.remove(index);
            }
            if (tipoLista == 1) {
                System.out.println("borrarandoMonedas ");
                if (!modificarMonedas.isEmpty() && modificarMonedas.contains(filtrarMonedas.get(index))) {
                    int modIndex = modificarMonedas.indexOf(filtrarMonedas.get(index));
                    modificarMonedas.remove(modIndex);
                    borrarMonedas.add(filtrarMonedas.get(index));
                } else if (!crearMonedas.isEmpty() && crearMonedas.contains(filtrarMonedas.get(index))) {
                    int crearIndex = crearMonedas.indexOf(filtrarMonedas.get(index));
                    crearMonedas.remove(crearIndex);
                } else {
                    borrarMonedas.add(filtrarMonedas.get(index));
                }
                int VCIndex = listMonedas.indexOf(filtrarMonedas.get(index));
                listMonedas.remove(VCIndex);
                filtrarMonedas.remove(index);

            }
            RequestContext context = RequestContext.getCurrentInstance();
            infoRegistro = "Cantidad de registros: " + listMonedas.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            RequestContext.getCurrentInstance().update("form:datosMoneda");
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
        try {
            System.err.println("Control Secuencia de ControlMonedas ");
            if (tipoLista == 0) {
                proyectos = administrarMonedas.verificarMonedasProyecto(listMonedas.get(index).getSecuencia());
            } else {
                proyectos = administrarMonedas.verificarMonedasProyecto(filtrarMonedas.get(index).getSecuencia());
            }
            if (proyectos.equals(new BigInteger("0"))) {
                System.out.println("Borrado==0");
                borrandoMonedas();
            } else {
                System.out.println("Borrado>0");

                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:validacionBorrar");
                PrimefacesContextUI.ejecutar("PF('validacionBorrar').show()");
                index = -1;

                proyectos = new BigInteger("-1");

            }
        } catch (Exception e) {
            System.err.println("ERROR ControlIdiomas verificarBorrado ERROR " + e);
        }
    }

    public void revisarDialogoGuardar() {

        if (!borrarMonedas.isEmpty() || !crearMonedas.isEmpty() || !modificarMonedas.isEmpty()) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:confirmarGuardar");
            PrimefacesContextUI.ejecutar("PF('confirmarGuardar').show()");
        }

    }

    public void guardarMonedas() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (guardado == false) {
            System.out.println("Realizando guardarMonedas");
            if (!borrarMonedas.isEmpty()) {
                for (int i = 0; i < borrarMonedas.size(); i++) {
                    System.out.println("Borrando...");
                    administrarMonedas.borrarMonedas(borrarMonedas.get(i));
                }
                //mostrarBorrados
                registrosBorrados = borrarMonedas.size();
                RequestContext.getCurrentInstance().update("form:mostrarBorrados");
                PrimefacesContextUI.ejecutar("PF('mostrarBorrados').show()");
                borrarMonedas.clear();
            }
            if (!crearMonedas.isEmpty()) {
                for (int i = 0; i < crearMonedas.size(); i++) {

                    System.out.println("Creando...");
                    administrarMonedas.crearMonedas(crearMonedas.get(i));

                }
                crearMonedas.clear();
            }
            if (!modificarMonedas.isEmpty()) {
                administrarMonedas.modificarMonedas(modificarMonedas);
                modificarMonedas.clear();
            }
            System.out.println("Se guardaron los datos con exito");
            listMonedas = null;
            RequestContext.getCurrentInstance().update("form:datosMoneda");
            k = 0;
            FacesMessage msg = new FacesMessage("Información", "Se gurdarón los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            guardado = true;
        }
        index = -1;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");

    }

    public void editarCelda() {
        if (index >= 0) {
            if (tipoLista == 0) {
                editarMoneda = listMonedas.get(index);
            }
            if (tipoLista == 1) {
                editarMoneda = filtrarMonedas.get(index);
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

    public void agregarNuevoMonedas() {
        System.out.println("agregarNuevoMonedas");
        int contador = 0;
        int duplicados = 0;

        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        if (nuevoMoneda.getCodigo() == null || nuevoMoneda.getCodigo().isEmpty()) {
            mensajeValidacion = " *Codigo \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else {
            System.out.println("codigo en Motivo Cambio Cargo: " + nuevoMoneda.getCodigo());

            for (int x = 0; x < listMonedas.size(); x++) {
                if (listMonedas.get(x).getCodigo().equals(nuevoMoneda.getCodigo())) {
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
        if (nuevoMoneda.getNombre() == null || nuevoMoneda.getNombre().isEmpty()) {
            mensajeValidacion = mensajeValidacion + " *Moneda \n";
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
                codigo = (Column) c.getViewRoot().findComponent("form:datosMoneda:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosMoneda:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosMoneda");
                bandera = 0;
                filtrarMonedas = null;
                tipoLista = 0;
            }
            System.out.println("Despues de la bandera");

            k++;
            l = BigInteger.valueOf(k);
            nuevoMoneda.setSecuencia(l);

            crearMonedas.add(nuevoMoneda);

            listMonedas.add(nuevoMoneda);
            nuevoMoneda = new Monedas();
            infoRegistro = "Cantidad de registros: " + listMonedas.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            RequestContext.getCurrentInstance().update("form:datosMoneda");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }

            PrimefacesContextUI.ejecutar("PF('nuevoRegistroMoneda').hide()");
            index = -1;
            secRegistro = null;

        } else {
            RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
            PrimefacesContextUI.ejecutar("PF('validacionNuevaCentroCosto').show()");
            contador = 0;
        }
    }

    public void limpiarNuevoMonedas() {
        System.out.println("limpiarNuevoMonedas");
        nuevoMoneda = new Monedas();
        secRegistro = null;
        index = -1;

    }

    //------------------------------------------------------------------------------
    public void duplicandoMonedas() {
        System.out.println("duplicandoMonedas");
        if (index >= 0) {
            duplicarMoneda = new Monedas();
            k++;
            l = BigInteger.valueOf(k);

            if (tipoLista == 0) {
                duplicarMoneda.setSecuencia(l);
                duplicarMoneda.setCodigo(listMonedas.get(index).getCodigo());
                duplicarMoneda.setNombre(listMonedas.get(index).getNombre());
            }
            if (tipoLista == 1) {
                duplicarMoneda.setSecuencia(l);
                duplicarMoneda.setCodigo(filtrarMonedas.get(index).getCodigo());
                duplicarMoneda.setNombre(filtrarMonedas.get(index).getNombre());
            }

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarMO");
            PrimefacesContextUI.ejecutar("PF('duplicarRegistroMoneda').show()");
            index = -1;
            secRegistro = null;
        }
    }

    public void confirmarDuplicar() {
        System.err.println("ESTOY EN CONFIRMAR DUPLICAR MONEDAS");
        int contador = 0;
        mensajeValidacion = " ";
        int duplicados = 0;
        RequestContext context = RequestContext.getCurrentInstance();

        System.err.println("ConfirmarDuplicar codigo " + duplicarMoneda.getCodigo());
        System.err.println("ConfirmarDuplicar Descripcion " + duplicarMoneda.getNombre());

        if (duplicarMoneda.getCodigo() == null || duplicarMoneda.getCodigo().isEmpty()) {
            mensajeValidacion = mensajeValidacion + "   * Codigo \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else {
            for (int x = 0; x < listMonedas.size(); x++) {
                if (listMonedas.get(x).getCodigo().equals(duplicarMoneda.getCodigo())) {
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
        if (duplicarMoneda.getNombre() == null || duplicarMoneda.getNombre().isEmpty()) {
            mensajeValidacion = mensajeValidacion + "   * Un Moneda \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("Bandera : ");
            contador++;
        }

        if (contador == 2) {

            System.out.println("Datos Duplicando: " + duplicarMoneda.getSecuencia() + "  " + duplicarMoneda.getCodigo());
            if (crearMonedas.contains(duplicarMoneda)) {
                System.out.println("Ya lo contengo.");
            }
            listMonedas.add(duplicarMoneda);
            crearMonedas.add(duplicarMoneda);
            RequestContext.getCurrentInstance().update("form:datosMoneda");
            index = -1;
            secRegistro = null;
            if (guardado == true) {
                guardado = false;
            }
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                codigo = (Column) c.getViewRoot().findComponent("form:datosMoneda:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosMoneda:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosMoneda");
                bandera = 0;
                filtrarMonedas = null;
                tipoLista = 0;
            }
            duplicarMoneda = new Monedas();
            infoRegistro = "Cantidad de registros: " + listMonedas.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            PrimefacesContextUI.ejecutar("PF('duplicarRegistroMoneda').hide()");

        } else {
            contador = 0;
            RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
            PrimefacesContextUI.ejecutar("PF('validacionDuplicarVigencia').show()");
        }
    }

    public void limpiarDuplicarMonedas() {
        duplicarMoneda = new Monedas();
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosMonedaExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "MONEDAS", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosMonedaExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "MONEDAS", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        System.out.println("lol");
        if (!listMonedas.isEmpty()) {
            if (secRegistro != null) {
                System.out.println("lol 2");
                int resultado = administrarRastros.obtenerTabla(secRegistro, "MONEDAS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
            if (administrarRastros.verificarHistoricosTabla("MONEDAS")) { // igual acá
                PrimefacesContextUI.ejecutar("PF('confirmarRastroHistorico').show()");
            } else {
                PrimefacesContextUI.ejecutar("PF('errorRastroHistorico').show()");
            }

        }
        index = -1;
    }
    private String infoRegistro;

    //*/*/*/*/*/*/*/*/*/*-/-*//-*/-*/*/*-*/-*/-*/*/*/*/*/---/*/*/*/*/-*/-*/-*/-*/-*/
    public List<Monedas> getListMonedas() {
        if (listMonedas == null) {
            listMonedas = administrarMonedas.consultarMonedas();
        }
        RequestContext context = RequestContext.getCurrentInstance();

        if (listMonedas == null || listMonedas.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listMonedas.size();
        }
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
        return listMonedas;
    }

    public void setListMonedas(List<Monedas> listMonedas) {
        this.listMonedas = listMonedas;
    }

    public List<Monedas> getModificarMonedas() {
        return modificarMonedas;
    }

    public void setModificarMonedas(List<Monedas> modificarMonedas) {
        this.modificarMonedas = modificarMonedas;
    }

    public Monedas getNuevoMoneda() {
        return nuevoMoneda;
    }

    public void setNuevoMoneda(Monedas nuevoMoneda) {
        this.nuevoMoneda = nuevoMoneda;
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

    public List<Monedas> getFiltrarMonedas() {
        return filtrarMonedas;
    }

    public void setFiltrarMonedas(List<Monedas> filtrarMonedas) {
        this.filtrarMonedas = filtrarMonedas;
    }

    public Monedas getEditarMoneda() {
        return editarMoneda;
    }

    public void setEditarMoneda(Monedas editarMoneda) {
        this.editarMoneda = editarMoneda;
    }

    public Monedas getDuplicarMoneda() {
        return duplicarMoneda;
    }

    public void setDuplicarMoneda(Monedas duplicarMoneda) {
        this.duplicarMoneda = duplicarMoneda;
    }

    public boolean isGuardado() {
        return guardado;
    }

    public void setGuardado(boolean guardado) {
        this.guardado = guardado;
    }

    public Monedas getMonedaSeleccionada() {
        return monedaSeleccionada;
    }

    public void setMonedaSeleccionada(Monedas monedaSeleccionada) {
        this.monedaSeleccionada = monedaSeleccionada;
    }

    public String getInfoRegistro() {
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

    public int getTamano() {
        return tamano;
    }

    public void setTamano(int tamano) {
        this.tamano = tamano;
    }

}
