/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import utilidadesUI.PrimefacesContextUI;
import Entidades.TiposEmbargos;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarRastrosInterface;
import InterfaceAdministrar.AdministrarTiposEmbargosInterface;
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
public class ControlTiposEmbargos implements Serializable {

    @EJB
    AdministrarTiposEmbargosInterface administrarTiposEmbargos;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    private List<TiposEmbargos> listTiposEmbargos;
    private List<TiposEmbargos> filtrarTiposEmbargos;
    private List<TiposEmbargos> borrarTiposEmbargos;
    private List<TiposEmbargos> modificarTiposEmbargos;
    private List<TiposEmbargos> crearTiposEmbargos;
    private TiposEmbargos editarTipoEmbargo;
    private TiposEmbargos nuevoTipoEmbargo;
    private TiposEmbargos duplicarTipoEmbargo;
    private String mensajeValidacion;
    private int cualCelda, tipoLista, index, tipoActualizacion, k, bandera;
    private BigInteger l;
    private boolean aceptar, guardado;
    //AutoCompletar
    private boolean permitirIndex;
    //RASTRO
    private BigInteger secRegistro;
    private Column codigo, descripcion, manejaSaldoPromedio;
    //borrado
    private int registrosBorrados;
    private Integer a;
    /*
     prueba
     */
    private boolean prueba;

    public boolean isPrueba() {
        return prueba;
    }

    public void setPrueba(Boolean prueba) {
        this.prueba = prueba;
    }

    //----------------------
    public ControlTiposEmbargos() {
        listTiposEmbargos = null;
        crearTiposEmbargos = new ArrayList<TiposEmbargos>();
        modificarTiposEmbargos = new ArrayList<TiposEmbargos>();
        borrarTiposEmbargos = new ArrayList<TiposEmbargos>();
        nuevoTipoEmbargo = new TiposEmbargos();
        nuevoTipoEmbargo.getManejaSaldoPromedio();
        duplicarTipoEmbargo = new TiposEmbargos();
        permitirIndex = true;
        a = null;
        guardado = true;
    }
    
    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarTiposEmbargos.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct "+ this.getClass().getName() +": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }
    
    public void eventoFiltrar() {
        try {
            System.out.println("\n ENTRE A CONTROLTIPOSEMBARGOS EVENTO FILTRAR \n");
            if (tipoLista == 0) {
                tipoLista = 1;
            }
        } catch (Exception e) {
            System.out.println("ERROR CONTROLTIPOSEMBARGOS EVENTO FILTRAR ERROR===" + e.getMessage());
        }
    }

    public void cambiarIndice(int indice, int celda) {
        System.err.println("TIPO LISTA = " + tipoLista);

        if (permitirIndex == true) {
            index = indice;
            cualCelda = celda;
            secRegistro = listTiposEmbargos.get(index).getSecuencia();
        }
        System.err.println("Indice: " + index + " Celda: " + cualCelda);
    }

    /**
     *
     * @param indice
     * @param LND
     * @param dig muestra el dialogo respectivo
     */
    public void asignarIndex(Integer indice, int LND, int dig) {
        try {
            System.out.println("\n ENTRE A CONTROLTIPOSEMBARGOS ASIGNARINDEX \n");
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
            System.out.println("ERROR CONTROLTIPOSEMBARGOS ASIGNARINDEX ERROR = " + e.getMessage());
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void listaValoresBoton() {
    }

    public void cancelarModificacion() {
        System.err.println("CANCELARMODIFICACION");
        if (bandera == 1) {
            //CERRAR FILTRADO
            codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposEmbargos:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposEmbargos:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            manejaSaldoPromedio = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposEmbargos:manejaSaldoPromedio");
            manejaSaldoPromedio.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTiposEmbargos");
            bandera = 0;
            filtrarTiposEmbargos = null;
            tipoLista = 0;
        }

        borrarTiposEmbargos.clear();
        crearTiposEmbargos.clear();
        modificarTiposEmbargos.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listTiposEmbargos = null;
        guardado = true;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosTiposEmbargos");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");

    }

    public void activarCtrlF11() {
        if (bandera == 0) {

            codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposEmbargos:codigo");
            codigo.setFilterStyle("width: 85% !important;");
            descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposEmbargos:descripcion");
            descripcion.setFilterStyle("width: 85% !important;");
            manejaSaldoPromedio = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposEmbargos:manejaSaldoPromedio");
            manejaSaldoPromedio.setFilterStyle("width: 85% !important;");
            RequestContext.getCurrentInstance().update("form:datosTiposEmbargos");
            System.out.println("Activar");
            bandera = 1;
        } else if (bandera == 1) {
            System.out.println("Desactivar");
            codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposEmbargos:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposEmbargos:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            manejaSaldoPromedio = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposEmbargos:manejaSaldoPromedio");
            manejaSaldoPromedio.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTiposEmbargos");
            bandera = 0;
            filtrarTiposEmbargos = null;
            tipoLista = 0;
        }
    }

    public void modificandoManejaSaldo(int indice, boolean cambio) {
        System.out.println("modificandoManejaSaldo cambio = " + cambio);
        System.out.println("modificandoManejaSaldo indice = " + indice);
        System.err.println("cambio = " + cambio);

        listTiposEmbargos.get(indice).setManejaSaldoPromedio(cambio);
        if (listTiposEmbargos.get(indice).getManejaSaldoPromedio() == true) {
            listTiposEmbargos.get(indice).setManejaSaldo("S");
        } else {
            listTiposEmbargos.get(indice).setManejaSaldo("N");
        }

        modificandoTiposEmbargos(indice, "N", "N");
    }

    public void modificandoTiposEmbargos(int indice, String confirmarCambio, String valorConfirmar) {
        System.err.println("ENTRE A MODIFICAR MOTIVOS CAMBIOS SUELDOS");
        index = indice;

        int contador = 0;
        int contadorGuardar = 0;
        boolean banderita = false;

        RequestContext context = RequestContext.getCurrentInstance();
        System.err.println("TIPO LISTA = " + tipoLista);
        if (confirmarCambio.equalsIgnoreCase("N")) {
            System.err.println("ENTRE A MODIFICANDO TIPOSEMBARGOS, CONFIRMAR CAMBIO ES N");
            if (tipoLista == 0) {
                if (!crearTiposEmbargos.contains(listTiposEmbargos.get(indice))) {
                    if (listTiposEmbargos.get(indice).getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                    } else {
                        for (int j = 0; j < listTiposEmbargos.size(); j++) {
                            if (j != indice) {
                                if (listTiposEmbargos.get(indice).getCodigo() == listTiposEmbargos.get(j).getCodigo()) {
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
                    if (listTiposEmbargos.get(indice).getDescripcion().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                    } else if (listTiposEmbargos.get(indice).getDescripcion().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                    } else {
                        contadorGuardar++;
                    }

                    if (contadorGuardar == 2) {
                        if (modificarTiposEmbargos.isEmpty()) {
                            modificarTiposEmbargos.add(listTiposEmbargos.get(indice));
                        } else if (!modificarTiposEmbargos.contains(listTiposEmbargos.get(indice))) {
                            modificarTiposEmbargos.add(listTiposEmbargos.get(indice));
                        }
                        if (guardado == true) {
                            guardado = false;
                        }

                    } else {
                        RequestContext.getCurrentInstance().update("form:validacionModificar");
                        PrimefacesContextUI.ejecutar("PF('validacionModificar').show()");
                        cancelarModificacion();
                    }
                    index = -1;
                    secRegistro = null;
                }
            } else {

                if (!crearTiposEmbargos.contains(filtrarTiposEmbargos.get(indice))) {
                    if (filtrarTiposEmbargos.get(indice).getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                    } else {
                        for (int j = 0; j < listTiposEmbargos.size(); j++) {
                            if (filtrarTiposEmbargos.get(indice).getCodigo().equals(listTiposEmbargos.get(j).getCodigo())) {
                                contador++;
                            }
                        }

                        for (int j = 0; j < filtrarTiposEmbargos.size(); j++) {
                            if (j == indice) {
                                if (filtrarTiposEmbargos.get(indice).getCodigo().equals(filtrarTiposEmbargos.get(j).getCodigo())) {
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

                    if (filtrarTiposEmbargos.get(indice).getDescripcion().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                    } else if (filtrarTiposEmbargos.get(indice).getDescripcion().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                    } else {
                        contadorGuardar++;
                    }
                    if (filtrarTiposEmbargos.get(indice).getManejaSaldoPromedio() == true) {
                        listTiposEmbargos.get(indice).setManejaSaldo("S");
                    }
                    if (filtrarTiposEmbargos.get(indice).getManejaSaldoPromedio() == false) {
                        listTiposEmbargos.get(indice).setManejaSaldo("N");
                    }

                    if (contadorGuardar == 2) {
                        if (modificarTiposEmbargos.isEmpty()) {
                            modificarTiposEmbargos.add(filtrarTiposEmbargos.get(indice));
                        } else if (!modificarTiposEmbargos.contains(filtrarTiposEmbargos.get(indice))) {
                            modificarTiposEmbargos.add(filtrarTiposEmbargos.get(indice));
                        }
                        if (guardado == true) {
                            guardado = false;
                        }

                    } else {
                        RequestContext.getCurrentInstance().update("form:validacionModificar");
                        PrimefacesContextUI.ejecutar("PF('validacionModificar').show()");
                        cancelarModificacion();
                    }
                    index = -1;
                    secRegistro = null;
                }

            }
            RequestContext.getCurrentInstance().update("form:datosTiposEmbargos");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }

    }
    private BigInteger verificarDiasLaborales;
    private BigInteger verificarExtrasRecargos;

    public void verificarBorrado() {
        System.out.println("ESTOY EN VERIFICAR BORRADO");
        try {
            verificarDiasLaborales = administrarTiposEmbargos.contarDiasLaboralesTipoEmbargo(listTiposEmbargos.get(index).getSecuencia());
            verificarExtrasRecargos = administrarTiposEmbargos.contarExtrasRecargosTipoEmbargo(listTiposEmbargos.get(index).getSecuencia());

            if (!verificarDiasLaborales.equals(new BigInteger("0")) || !verificarExtrasRecargos.equals(new BigInteger("0"))) {
                System.out.println("Borrado>0");

                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:validacionBorrar");
                PrimefacesContextUI.ejecutar("PF('validacionBorrar').show()");
                index = -1;

                verificarDiasLaborales = new BigInteger("-1");
                verificarExtrasRecargos = new BigInteger("-1");

            } else {
                System.out.println("Borrado==0");
                borrandoTiposEmbargos();
            }
        } catch (Exception e) {
            System.err.println("ERROR ControlTiposCertificados verificarBorrado ERROR " + e);
        }
    }

    public void borrandoTiposEmbargos() {

        if (index >= 0) {

            if (tipoLista == 0) {
                System.out.println("Entro a borrandoTiposEmbargos");
                if (!modificarTiposEmbargos.isEmpty() && modificarTiposEmbargos.contains(listTiposEmbargos.get(index))) {
                    int modIndex = modificarTiposEmbargos.indexOf(listTiposEmbargos.get(index));
                    modificarTiposEmbargos.remove(modIndex);
                    borrarTiposEmbargos.add(listTiposEmbargos.get(index));
                } else if (!crearTiposEmbargos.isEmpty() && crearTiposEmbargos.contains(listTiposEmbargos.get(index))) {
                    int crearIndex = crearTiposEmbargos.indexOf(listTiposEmbargos.get(index));
                    crearTiposEmbargos.remove(crearIndex);
                } else {
                    borrarTiposEmbargos.add(listTiposEmbargos.get(index));
                }
                listTiposEmbargos.remove(index);
            }
            if (tipoLista == 1) {
                System.out.println("borrandoTiposEmbargos");
                if (!modificarTiposEmbargos.isEmpty() && modificarTiposEmbargos.contains(filtrarTiposEmbargos.get(index))) {
                    int modIndex = modificarTiposEmbargos.indexOf(filtrarTiposEmbargos.get(index));
                    modificarTiposEmbargos.remove(modIndex);
                    borrarTiposEmbargos.add(filtrarTiposEmbargos.get(index));
                } else if (!crearTiposEmbargos.isEmpty() && crearTiposEmbargos.contains(filtrarTiposEmbargos.get(index))) {
                    int crearIndex = crearTiposEmbargos.indexOf(filtrarTiposEmbargos.get(index));
                    crearTiposEmbargos.remove(crearIndex);
                } else {
                    borrarTiposEmbargos.add(filtrarTiposEmbargos.get(index));
                }
                int VCIndex = listTiposEmbargos.indexOf(filtrarTiposEmbargos.get(index));
                listTiposEmbargos.remove(VCIndex);
                filtrarTiposEmbargos.remove(index);

            }
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosTiposEmbargos");
            index = -1;
            secRegistro = null;

            if (guardado == true) {
                guardado = false;
            }
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }

    }

    public void editarCelda() {
        if (index >= 0) {
            if (tipoLista == 0) {
                editarTipoEmbargo = listTiposEmbargos.get(index);
            }
            if (tipoLista == 1) {
                editarTipoEmbargo = filtrarTiposEmbargos.get(index);
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

    public void agregarNuevoTiposEmbargos() {
        System.out.println("agregarNuevoTiposEmbargos");
        int contador = 0;
        int duplicados = 0;

        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        if (nuevoTipoEmbargo.getCodigo() == a) {
            mensajeValidacion = " *Debe tener un código \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else {
            System.out.println("codigo en Motivo Cambio Cargo: " + nuevoTipoEmbargo.getCodigo());

            for (int x = 0; x < listTiposEmbargos.size(); x++) {
                if (listTiposEmbargos.get(x).getCodigo() == nuevoTipoEmbargo.getCodigo()) {
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
        if (nuevoTipoEmbargo.getDescripcion() == null) {
            mensajeValidacion = mensajeValidacion + " *Debe tener una  descripción \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("bandera");
            contador++;

        }
        if (nuevoTipoEmbargo.getManejaSaldoPromedio() == null) {
            nuevoTipoEmbargo.setManejaSaldoPromedio(true);
            nuevoTipoEmbargo.setManejaSaldo("S");
        }
        if (nuevoTipoEmbargo.getManejaSaldoPromedio() == true) {
            System.err.println("Sueldo Promedio S");
            nuevoTipoEmbargo.setManejaSaldo("S");
        }
        if (nuevoTipoEmbargo.getManejaSaldoPromedio() == false) {
            System.err.println("Sueldo Promedio N");
            nuevoTipoEmbargo.setManejaSaldo("N");
        }

        System.out.println("contador " + contador);

        if (contador == 2) {
            if (bandera == 1) {
                //CERRAR FILTRADO
                System.out.println("Desactivar");

                codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposEmbargos:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposEmbargos:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                manejaSaldoPromedio = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposEmbargos:manejaSaldoPromedio");
                manejaSaldoPromedio.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosTiposEmbargos");
                bandera = 0;
                filtrarTiposEmbargos = null;

                tipoLista = 0;
            }
            System.out.println("Despues de la bandera");

            //AGREGAR REGISTRO A LA LISTA  SUELDOS .
            k++;
            l = BigInteger.valueOf(k);
            nuevoTipoEmbargo.setSecuencia(l);

            crearTiposEmbargos.add(nuevoTipoEmbargo);

            listTiposEmbargos.add(nuevoTipoEmbargo);
            nuevoTipoEmbargo = new TiposEmbargos();
            nuevoTipoEmbargo.getManejaSaldoPromedio();

            RequestContext.getCurrentInstance().update("form:datosTiposEmbargos");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }

            PrimefacesContextUI.ejecutar("PF('nuevoRegistroTiposEmbargos').hide()");
            index = -1;
            secRegistro = null;

        } else {
            RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
            PrimefacesContextUI.ejecutar("PF('validacionNuevaCentroCosto').show()");
            contador = 0;
        }
    }

    public void limpiarNuevoTiposEmbargos() {
        System.out.println("limpiarNuevoTiposEmbargos");
        nuevoTipoEmbargo = new TiposEmbargos();
        nuevoTipoEmbargo.getManejaSaldoPromedio();

        secRegistro = null;
        index = -1;

    }

    public void duplicandoTiposEmbargos() {
        System.out.println("duplicarTiposEmbargos");
        if (index >= 0) {
            duplicarTipoEmbargo = new TiposEmbargos();
            k++;
            l = BigInteger.valueOf(k);

            if (tipoLista == 0) {
                duplicarTipoEmbargo.setSecuencia(l);
                duplicarTipoEmbargo.setCodigo(listTiposEmbargos.get(index).getCodigo());
                duplicarTipoEmbargo.setDescripcion(listTiposEmbargos.get(index).getDescripcion());
                duplicarTipoEmbargo.setManejaSaldo(listTiposEmbargos.get(index).getManejaSaldo());
                duplicarTipoEmbargo.setManejaSaldoPromedio(listTiposEmbargos.get(index).getManejaSaldoPromedio());
            }
            if (tipoLista == 1) {
                duplicarTipoEmbargo.setSecuencia(l);
                duplicarTipoEmbargo.setCodigo(filtrarTiposEmbargos.get(index).getCodigo());
                duplicarTipoEmbargo.setDescripcion(filtrarTiposEmbargos.get(index).getDescripcion());
                duplicarTipoEmbargo.setManejaSaldo(filtrarTiposEmbargos.get(index).getManejaSaldo());
                duplicarTipoEmbargo.setManejaSaldoPromedio(filtrarTiposEmbargos.get(index).getManejaSaldoPromedio());

            }

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTiposEmbargos");
            PrimefacesContextUI.ejecutar("PF('duplicarRegistroTiposEmbargos').show()");
            index = -1;
            secRegistro = null;
        }
    }

    public void confirmarDuplicar() {
        System.err.println("ESTOY EN CONFIRMAR DUPLICAR");
        int contador = 0;
        mensajeValidacion = " ";
        int duplicados = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        System.err.println("ConfirmarDuplicar codigo " + duplicarTipoEmbargo.getCodigo());
        System.err.println("ConfirmarDuplicar nombre " + duplicarTipoEmbargo.getDescripcion());
        System.err.println("ConfirmarDuplicar Sueldo Promedio " + duplicarTipoEmbargo.getManejaSaldo());

        if (duplicarTipoEmbargo.getCodigo() == a) {
            mensajeValidacion = mensajeValidacion + "   * Codigo \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else {
            for (int x = 0; x < listTiposEmbargos.size(); x++) {
                if (listTiposEmbargos.get(x).getCodigo() == duplicarTipoEmbargo.getCodigo()) {
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
        if (duplicarTipoEmbargo.getDescripcion().isEmpty()) {
            mensajeValidacion = mensajeValidacion + "   * Un Descripción \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("Bandera : ");
            contador++;
        }
        if (duplicarTipoEmbargo.getManejaSaldoPromedio() == true) {
            System.err.println("Sueldo Promedio S");
            duplicarTipoEmbargo.setManejaSaldo("S");
        }
        if (duplicarTipoEmbargo.getManejaSaldoPromedio() == false) {
            System.err.println("Sueldo Promedio N");
            duplicarTipoEmbargo.setManejaSaldo("N");
        }

        if (contador == 2) {

            System.out.println("Datos Duplicando: " + duplicarTipoEmbargo.getSecuencia() + "  " + duplicarTipoEmbargo.getCodigo());
            if (crearTiposEmbargos.contains(duplicarTipoEmbargo)) {
                System.out.println("Ya lo contengo.");
            }
            listTiposEmbargos.add(duplicarTipoEmbargo);
            crearTiposEmbargos.add(duplicarTipoEmbargo);
            RequestContext.getCurrentInstance().update("form:datosTiposEmbargos");
            index = -1;
            secRegistro = null;
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            if (bandera == 1) {
                //CERRAR FILTRADO
                codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposEmbargos:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposEmbargos:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                manejaSaldoPromedio = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposEmbargos:manejaSaldoPromedio");
                manejaSaldoPromedio.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosTiposEmbargos");
                bandera = 0;
                filtrarTiposEmbargos = null;
                tipoLista = 0;
            }
            duplicarTipoEmbargo = new TiposEmbargos();
            PrimefacesContextUI.ejecutar("PF('duplicarRegistroTiposEmbargos').hide()");

        } else {
            contador = 0;
            RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
            PrimefacesContextUI.ejecutar("PF('validacionDuplicarVigencia').show()");
        }
    }

    public void limpiarDuplicarTiposEmbargos() {
        duplicarTipoEmbargo = new TiposEmbargos();
    }

    public void guardarTiposEmbargos() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (guardado == false) {
            System.out.println("Realizando Operaciones Vigencias Localizacion");
            if (!borrarTiposEmbargos.isEmpty()) {
                administrarTiposEmbargos.borrarTiposPrestamos(borrarTiposEmbargos);
                //mostrarBorrados
                registrosBorrados = borrarTiposEmbargos.size();
                RequestContext.getCurrentInstance().update("form:mostrarBorrados");
                PrimefacesContextUI.ejecutar("PF('mostrarBorrados').show()");
                borrarTiposEmbargos.clear();
            }
            if (!crearTiposEmbargos.isEmpty()) {
                administrarTiposEmbargos.crearTiposPrestamos(crearTiposEmbargos);
                crearTiposEmbargos.clear();
            }
            if (!modificarTiposEmbargos.isEmpty()) {
                administrarTiposEmbargos.modificarTiposPrestamos(modificarTiposEmbargos);
                modificarTiposEmbargos.clear();
            }
            System.out.println("Se guardaron los datos con exito");
            listTiposEmbargos = null;
            RequestContext.getCurrentInstance().update("form:datosTiposEmbargos");
            k = 0;
            if (guardado == false) {
                guardado = true;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");

            }
        }
        index = -1;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");

    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTiposEmbargosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "TIPOSEMBARGOS", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTiposEmbargosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "TIPOSEMBARGOS", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        System.out.println("lol");
        if (!listTiposEmbargos.isEmpty()) {
            if (secRegistro != null) {
                System.out.println("lol 2");
                int resultado = administrarRastros.obtenerTabla(secRegistro, "TIPOSEMBARGOS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
            if (administrarRastros.verificarHistoricosTabla("TIPOSEMBARGOS")) { // igual acá
                PrimefacesContextUI.ejecutar("PF('confirmarRastroHistorico').show()");
            } else {
                PrimefacesContextUI.ejecutar("PF('errorRastroHistorico').show()");
            }

        }
        index = -1;
    }

//---------//----------------//------------//----------------//-----------//--------------------------//-----
    public List<TiposEmbargos> getListTiposEmbargos() {
        if (listTiposEmbargos == null) {
            listTiposEmbargos = administrarTiposEmbargos.consultarTiposPrestamos();
        }
        return listTiposEmbargos;
    }

    public void setListTiposEmbargos(List<TiposEmbargos> listTiposEmbargos) {
        this.listTiposEmbargos = listTiposEmbargos;
    }

    public List<TiposEmbargos> getFiltrarTiposEmbargos() {
        return filtrarTiposEmbargos;
    }

    public void setFiltrarTiposEmbargos(List<TiposEmbargos> filtrarTiposEmbargos) {
        this.filtrarTiposEmbargos = filtrarTiposEmbargos;
    }

    public TiposEmbargos getEditarTipoEmbargo() {
        return editarTipoEmbargo;
    }

    public void setEditarTipoEmbargo(TiposEmbargos editarTipoEmbargo) {
        this.editarTipoEmbargo = editarTipoEmbargo;
    }

    public TiposEmbargos getNuevoTipoEmbargo() {
        return nuevoTipoEmbargo;
    }

    public void setNuevoTipoEmbargo(TiposEmbargos nuevoTipoEmbargo) {
        this.nuevoTipoEmbargo = nuevoTipoEmbargo;
    }

    public TiposEmbargos getDuplicarTipoEmbargo() {
        return duplicarTipoEmbargo;
    }

    public void setDuplicarTipoEmbargo(TiposEmbargos duplicarTipoEmbargo) {
        this.duplicarTipoEmbargo = duplicarTipoEmbargo;
    }

    public String getMensajeValidacion() {
        return mensajeValidacion;
    }

    public void setMensajeValidacion(String mensajeValidacion) {
        this.mensajeValidacion = mensajeValidacion;
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

    public boolean isGuardado() {
        return guardado;
    }

    public void setGuardado(boolean guardado) {
        this.guardado = guardado;
    }

}
