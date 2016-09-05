/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Operandos;
import Entidades.TiposFunciones;
import Exportar.ExportarPDF;
import Exportar.ExportarPDFTablasAnchas;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarRastrosInterface;
import InterfaceAdministrar.AdministrarTiposFuncionesInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
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
public class ControlTipoFuncion implements Serializable {

    @EJB
    AdministrarTiposFuncionesInterface administrarTiposFunciones;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    //Parametros que llegan
    private BigInteger secOperando;
    private String tOperando;
    private Operandos operando;
    //LISTA INFOREPORTES
    private List<TiposFunciones> listaTiposFunciones;
    private List<TiposFunciones> filtradosListaTiposFunciones;
    private TiposFunciones tipoFuncionSeleccionado;
    //L.O.V INFOREPORTES
    private List<TiposFunciones> lovlistaTiposFunciones;
    private List<TiposFunciones> lovfiltradoslistaTiposFunciones;
    private TiposFunciones operandosSeleccionado;
    //editar celda
    private TiposFunciones editarTiposFunciones;
    private boolean cambioEditor, aceptarEditar;
    private int cualCelda, tipoLista;
    //OTROS
    private boolean aceptar;
    private int index;
    private int tipoActualizacion; //Activo/Desactivo Crtl + F11
    private int bandera;
    private boolean permitirIndex;
    //RASTROS
    private BigInteger secRegistro;
    private boolean guardado, guardarOk;
    //Crear Novedades
    private List<TiposFunciones> listaTiposFuncionesCrear;
    public TiposFunciones nuevoTipoFuncion;
    public TiposFunciones duplicarTipoFuncion;
    private int k;
    private BigInteger l;
    private String mensajeValidacion;
    //Modificar Novedades
    private List<TiposFunciones> listaTiposFuncionesModificar;
    //Borrar Novedades
    private List<TiposFunciones> listaTiposFuncionesBorrar;
    //AUTOCOMPLETAR
    private String Modulo;
    //Columnas Tabla Ciudades
    private Column tiposFuncionesIniciales, tiposFuncionesFinales, tiposFuncionesObjetos;
    //ALTO SCROLL TABLA
    private String altoTabla;
    private boolean cambiosPagina;

    public ControlTipoFuncion() {
        cambiosPagina = true;
        nuevoTipoFuncion = new TiposFunciones();
        nuevoTipoFuncion.setFechainicial(new Date());
        permitirIndex = true;

        permitirIndex = true;
        aceptar = true;
        secRegistro = null;
        guardado = true;
        tipoLista = 0;
        listaTiposFuncionesBorrar = new ArrayList<TiposFunciones>();
        listaTiposFuncionesCrear = new ArrayList<TiposFunciones>();
        listaTiposFuncionesModificar = new ArrayList<TiposFunciones>();
        altoTabla = "270";
        duplicarTipoFuncion = new TiposFunciones();
    }
    
    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarTiposFunciones.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct "+ this.getClass().getName() +": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }
    
    //UBICACION CELDA
    public void cambiarIndice(int indice, int celda) {
        if (permitirIndex == true) {
            index = indice;
            cualCelda = celda;
            if (tipoLista == 0) {
                secRegistro = listaTiposFunciones.get(index).getSecuencia();
            } else {
                secRegistro = filtradosListaTiposFunciones.get(index).getSecuencia();
            }
        }
    }

    public void recibirDatosOperando(BigInteger secuenciaOperando, String tipoOperando, Operandos operandoRegistro) {
        secOperando = secuenciaOperando;
        tOperando = tipoOperando;

        operando = operandoRegistro;
        listaTiposFunciones = null;
        getListaTiposFunciones();

    }

    //AUTOCOMPLETAR
    public void modificarTiposFunciones(int indice, String confirmarCambio, String valorConfirmar) {
        index = indice;

        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("N")) {
            if (tipoLista == 0) {
                if (!listaTiposFuncionesCrear.contains(listaTiposFunciones.get(index))) {

                    if (listaTiposFuncionesModificar.isEmpty()) {
                        listaTiposFuncionesModificar.add(listaTiposFunciones.get(index));
                    } else if (!listaTiposFuncionesModificar.contains(listaTiposFunciones.get(index))) {
                        listaTiposFuncionesModificar.add(listaTiposFunciones.get(index));
                    }
                    if (guardado == true) {
                        guardado = false;
                        context.update("form:ACEPTAR");

                    }
                }
                index = -1;
                secRegistro = null;
            } else {
                if (!listaTiposFuncionesCrear.contains(filtradosListaTiposFunciones.get(index))) {

                    if (listaTiposFuncionesCrear.isEmpty()) {
                        listaTiposFuncionesCrear.add(filtradosListaTiposFunciones.get(index));
                    } else if (!listaTiposFuncionesCrear.contains(filtradosListaTiposFunciones.get(index))) {
                        listaTiposFuncionesCrear.add(filtradosListaTiposFunciones.get(index));
                    }
                    if (guardado == true) {
                        guardado = false;
                        context.update("form:ACEPTAR");
                    }
                }
                index = -1;
                secRegistro = null;
            }
            context.update("form:datosTiposFunciones");
        }
    }

    public void eventoFiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
    }

    public void confirmarDuplicar() {
        int pasa = 0;
        mensajeValidacion = new String();
        RequestContext context = RequestContext.getCurrentInstance();

        if (duplicarTipoFuncion.getFechainicial() == null) {
            mensajeValidacion = mensajeValidacion + " * Fecha Inicial\n";
            pasa++;
        }

        if (pasa != 0) {
            context.update("formularioDialogos:validacionNuevoTipoFuncion");
            context.execute("validacionNuevoTipoFuncion.show()");
        }

        if (pasa == 0) {
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();

                altoTabla = "270";
                tiposFuncionesIniciales = (Column) c.getViewRoot().findComponent("form:datosTiposFunciones:tiposFuncionesIniciales");
                tiposFuncionesIniciales.setFilterStyle("display: none; visibility: hidden;");
                tiposFuncionesFinales = (Column) c.getViewRoot().findComponent("form:datosTiposFunciones:tiposFuncionesFinales");
                tiposFuncionesFinales.setFilterStyle("display: none; visibility: hidden;");
                tiposFuncionesObjetos = (Column) c.getViewRoot().findComponent("form:datosTiposFunciones:tiposFuncionesObjetos");
                tiposFuncionesObjetos.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosTiposFunciones");
                bandera = 0;
                filtradosListaTiposFunciones = null;
                tipoLista = 0;
            }

            context.update("form:ACEPTAR");
            //Falta Ponerle el Operando al cual se agregará
            duplicarTipoFuncion.setOperando(operando);
            listaTiposFunciones.add(duplicarTipoFuncion);
            listaTiposFuncionesCrear.add(duplicarTipoFuncion);

            index = -1;
            if (guardado == true) {
                guardado = false;
                context.update("form:ACEPTAR");
            }
            context.update("form:datosTiposFunciones");
            duplicarTipoFuncion = new TiposFunciones();
            context.update("formularioDialogos:DuplicarTipoFuncion");
            context.execute("DuplicarTipoFuncion.hide()");
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();

        if (bandera == 0) {
            altoTabla = "250";
            tiposFuncionesIniciales = (Column) c.getViewRoot().findComponent("form:datosTiposFunciones:tiposFuncionesIniciales");
            tiposFuncionesIniciales.setFilterStyle("width: 85%;");
            tiposFuncionesFinales = (Column) c.getViewRoot().findComponent("form:datosTiposFunciones:tiposFuncionesFinales");
            tiposFuncionesFinales.setFilterStyle("width: 85%;");
            tiposFuncionesObjetos = (Column) c.getViewRoot().findComponent("form:datosTiposFunciones:tiposFuncionesObjetos");
            tiposFuncionesObjetos.setFilterStyle("width: 85%;");
            RequestContext.getCurrentInstance().update("form:datosTiposFunciones");
            bandera = 1;
            tipoLista = 1;
        } else if (bandera == 1) {
            altoTabla = "270";
            System.out.println("Desactivar");
            System.out.println("TipoLista= " + tipoLista);
            tiposFuncionesIniciales = (Column) c.getViewRoot().findComponent("form:datosTiposFunciones:tiposFuncionesIniciales");
            tiposFuncionesIniciales.setFilterStyle("display: none; visibility: hidden;");
            tiposFuncionesFinales = (Column) c.getViewRoot().findComponent("form:datosTiposFunciones:tiposFuncionesFinales");
            tiposFuncionesFinales.setFilterStyle("display: none; visibility: hidden;");
            tiposFuncionesObjetos = (Column) c.getViewRoot().findComponent("form:datosTiposFunciones:tiposFuncionesObjetos");
            tiposFuncionesObjetos.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTiposFunciones");
            bandera = 0;
            filtradosListaTiposFunciones = null;
            tipoLista = 0;
        }
    }

    public void cancelarModificacion() {
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();

            //CERRAR FILTRADO
            altoTabla = "270";
            System.out.println("Desactivar");
            System.out.println("TipoLista= " + tipoLista);
            tiposFuncionesIniciales = (Column) c.getViewRoot().findComponent("form:datosTiposFunciones:tiposFuncionesIniciales");
            tiposFuncionesIniciales.setFilterStyle("display: none; visibility: hidden;");
            tiposFuncionesFinales = (Column) c.getViewRoot().findComponent("form:datosTiposFunciones:tiposFuncionesFinales");
            tiposFuncionesFinales.setFilterStyle("display: none; visibility: hidden;");
            tiposFuncionesObjetos = (Column) c.getViewRoot().findComponent("form:datosTiposFunciones:tiposFuncionesObjetos");
            tiposFuncionesObjetos.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTiposFunciones");
            bandera = 0;
            filtradosListaTiposFunciones = null;
            tipoLista = 0;
        }

        listaTiposFuncionesBorrar.clear();
        listaTiposFuncionesCrear.clear();
        listaTiposFuncionesModificar.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listaTiposFunciones = null;
        guardado = true;
        permitirIndex = true;
        cambiosPagina = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form:ACEPTAR");
        context.update("form:datosTiposFunciones");
    }

    //MOSTRAR DATOS CELDA
    public void editarCelda() {
        if (index >= 0) {
            if (tipoLista == 0) {
                editarTiposFunciones = listaTiposFunciones.get(index);
            }
            if (tipoLista == 1) {
                editarTiposFunciones = filtradosListaTiposFunciones.get(index);
            }

            RequestContext context = RequestContext.getCurrentInstance();
            System.out.println("Entro a editar... valor celda: " + cualCelda);
            if (cualCelda == 0) {
                context.update("formularioDialogos:editarFechasIniciales");
                context.execute("editarFechasIniciales.show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                context.update("formularioDialogos:editarFechasFinales");
                context.execute("editarFechasFinales.show()");
                cualCelda = -1;
            } else if (cualCelda == 2) {
                context.update("formularioDialogos:editarObjetos");
                context.execute("editarObjetos.show()");
                cualCelda = -1;
            }
        }
        index = -1;
        secRegistro = null;
    }

    //EXPORTAR
    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTiposFuncionesExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "TiposFuncionesPDF", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTiposFuncionesExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "TiposFuncionesXLS", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    //LIMPIAR NUEVO REGISTRO CIUDAD
    public void limpiarNuevoTiposFunciones() {
        nuevoTipoFuncion = new TiposFunciones();
        index = -1;
        secRegistro = null;
    }

    public void limpiarduplicarTiposFunciones() {
        duplicarTipoFuncion = new TiposFunciones();
        index = -1;
        secRegistro = null;
    }

    //DUPLICAR Operando
    public void duplicarTF() {
        if (index >= 0) {
            duplicarTipoFuncion = new TiposFunciones();
            k++;
            l = BigInteger.valueOf(k);

            if (tipoLista == 0) {
                duplicarTipoFuncion.setSecuencia(l);
                duplicarTipoFuncion.setNombreobjeto(listaTiposFunciones.get(index).getNombreobjeto());
                duplicarTipoFuncion.setFechainicial(listaTiposFunciones.get(index).getFechainicial());
                duplicarTipoFuncion.setFechafinal(listaTiposFunciones.get(index).getFechafinal());
                duplicarTipoFuncion.setOperando(listaTiposFunciones.get(index).getOperando());
            }
            if (tipoLista == 1) {
                duplicarTipoFuncion.setSecuencia(l);
                duplicarTipoFuncion.setNombreobjeto(filtradosListaTiposFunciones.get(index).getNombreobjeto());
                duplicarTipoFuncion.setFechainicial(filtradosListaTiposFunciones.get(index).getFechainicial());
                duplicarTipoFuncion.setFechafinal(filtradosListaTiposFunciones.get(index).getFechafinal());
                duplicarTipoFuncion.setOperando(filtradosListaTiposFunciones.get(index).getOperando());

            }

            RequestContext context = RequestContext.getCurrentInstance();
            context.update("formularioDialogos:duplicarTipoFuncion");
            context.execute("DuplicarTipoFuncion.show()");
            index = -1;
            secRegistro = null;
        }
    }

    //GUARDAR
    public void guardarCambiosTiposFunciones() {
        if (guardado == false) {
            System.out.println("Realizando Operaciones Novedades");

            if (!listaTiposFuncionesBorrar.isEmpty()) {
                for (int i = 0; i < listaTiposFuncionesBorrar.size(); i++) {
                    System.out.println("Borrando..." + listaTiposFuncionesBorrar.size());
                    if (listaTiposFuncionesBorrar.get(i).getFechafinal() == null) {
                        listaTiposFuncionesBorrar.get(i).setFechafinal(null);
                    }

                    if (listaTiposFuncionesBorrar.get(i).getNombreobjeto() == null) {
                        listaTiposFuncionesBorrar.get(i).setNombreobjeto(null);
                    }

                    administrarTiposFunciones.borrarTiposFunciones(listaTiposFuncionesBorrar.get(i));
                }
                System.out.println("Entra");
                listaTiposFuncionesBorrar.clear();
            }

            if (!listaTiposFuncionesCrear.isEmpty()) {
                for (int i = 0; i < listaTiposFuncionesCrear.size(); i++) {
                    System.out.println("Creando...");
                    if (listaTiposFuncionesCrear.get(i).getFechafinal() == null) {
                        listaTiposFuncionesCrear.get(i).setFechafinal(null);
                    }

                    if (listaTiposFuncionesCrear.get(i).getNombreobjeto() == null) {
                        listaTiposFuncionesCrear.get(i).setNombreobjeto(null);
                    }
                    administrarTiposFunciones.crearTiposFunciones(listaTiposFuncionesCrear.get(i));
                }
                System.out.println("LimpiaLista");
                listaTiposFuncionesCrear.clear();
            }
            if (!listaTiposFuncionesModificar.isEmpty()) {
                administrarTiposFunciones.modificarTiposFunciones(listaTiposFuncionesModificar);
                listaTiposFuncionesModificar.clear();
            }

            System.out.println("Se guardaron los datos con exito");
            listaTiposFunciones = null;

            RequestContext context = RequestContext.getCurrentInstance();
            context.update("form:datosTiposFunciones");
            guardado = true;
            permitirIndex = true;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            context.update("form:growl");
            //  k = 0;
        }
        index = -1;
        secRegistro = null;
    }

//RASTROS 
    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (!listaTiposFunciones.isEmpty()) {
            if (secRegistro != null) {
                int result = administrarRastros.obtenerTabla(secRegistro, "TIPOSFUNCIONES");
                System.out.println("resultado: " + result);
                if (result == 1) {
                    context.execute("errorObjetosDB.show()");
                } else if (result == 2) {
                    context.execute("confirmarRastro.show()");
                } else if (result == 3) {
                    context.execute("errorRegistroRastro.show()");
                } else if (result == 4) {
                    context.execute("errorTablaConRastro.show()");
                } else if (result == 5) {
                    context.execute("errorTablaSinRastro.show()");
                }
            } else {
                context.execute("seleccionarRegistro.show()");
            }
        } else {
            if (administrarRastros.verificarHistoricosTabla("TIPOSFUNCIONES")) {
                context.execute("confirmarRastroHistorico.show()");
            } else {
                context.execute("errorRastroHistorico.show()");
            }
        }
        index = -1;
    }

    public void agregarNuevoTipoFuncion() {
        int pasa = 0;
        int pasa2 = 0;
        mensajeValidacion = new String();

        RequestContext context = RequestContext.getCurrentInstance();

        if (nuevoTipoFuncion.getFechainicial() == null) {
            mensajeValidacion = mensajeValidacion + " * Fecha Inicial\n";
            pasa++;
        }

        if (nuevoTipoFuncion.getFechafinal().before(nuevoTipoFuncion.getFechainicial())) {
            context.update("formularioDialogos:errorFechas");
            context.execute("errorFechas.show()");
            pasa2++;
        }

        if (pasa != 0) {
            context.update("formularioDialogos:validacionNuevoTipoFuncion");
            context.execute("validacionNuevoTipoFuncion.show()");
        }

        if (pasa == 0 && pasa2 == 0) {
            if (bandera == 1) {
                //CERRAR FILTRADO
                FacesContext c = FacesContext.getCurrentInstance();

                altoTabla = "270";
                System.out.println("Desactivar");
                System.out.println("TipoLista= " + tipoLista);
                tiposFuncionesIniciales = (Column) c.getViewRoot().findComponent("form:datosTiposFunciones:tiposFuncionesIniciales");
                tiposFuncionesIniciales.setFilterStyle("display: none; visibility: hidden;");
                tiposFuncionesFinales = (Column) c.getViewRoot().findComponent("form:datosTiposFunciones:tiposFuncionesFinales");
                tiposFuncionesFinales.setFilterStyle("display: none; visibility: hidden;");
                tiposFuncionesObjetos = (Column) c.getViewRoot().findComponent("form:datosTiposFunciones:tiposFuncionesObjetos");
                tiposFuncionesObjetos.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosTiposFunciones");
                bandera = 0;
                filtradosListaTiposFunciones = null;
                tipoLista = 0;
            }
            //AGREGAR REGISTRO A LA LISTA NOVEDADES .
            k++;
            l = BigInteger.valueOf(k);
            nuevoTipoFuncion.setSecuencia(l);
            System.out.println("Operando: " + operando);
            nuevoTipoFuncion.setOperando(operando);

            context.update("form:ACEPTAR");
            //Falta Agregar el operando al cual se va a adicionar
            listaTiposFuncionesCrear.add(nuevoTipoFuncion);
            listaTiposFunciones.add(nuevoTipoFuncion);
            nuevoTipoFuncion = new TiposFunciones();
            context.update("form:datosTiposFunciones");
            if (guardado == true) {
                guardado = false;
                context.update("form:ACEPTAR");
            }
            context.execute("NuevoTipoFuncion.hide()");
            index = -1;
            secRegistro = null;
        }
    }

    //BORRAR CIUDADES
    public void borrarTipoFuncion() {

        if (index >= 0) {
            if (tipoLista == 0) {
                if (!listaTiposFuncionesModificar.isEmpty() && listaTiposFuncionesModificar.contains(listaTiposFunciones.get(index))) {
                    int modIndex = listaTiposFuncionesModificar.indexOf(listaTiposFunciones.get(index));
                    listaTiposFuncionesModificar.remove(modIndex);
                    listaTiposFuncionesBorrar.add(listaTiposFunciones.get(index));
                } else if (!listaTiposFuncionesCrear.isEmpty() && listaTiposFuncionesCrear.contains(listaTiposFunciones.get(index))) {
                    int crearIndex = listaTiposFuncionesCrear.indexOf(listaTiposFunciones.get(index));
                    listaTiposFuncionesCrear.remove(crearIndex);
                } else {
                    listaTiposFuncionesBorrar.add(listaTiposFunciones.get(index));
                }
                listaTiposFunciones.remove(index);
            }

            if (tipoLista == 1) {
                if (!listaTiposFuncionesModificar.isEmpty() && listaTiposFuncionesModificar.contains(filtradosListaTiposFunciones.get(index))) {
                    int modIndex = listaTiposFuncionesModificar.indexOf(filtradosListaTiposFunciones.get(index));
                    listaTiposFuncionesModificar.remove(modIndex);
                    listaTiposFuncionesBorrar.add(filtradosListaTiposFunciones.get(index));
                } else if (!listaTiposFuncionesCrear.isEmpty() && listaTiposFuncionesCrear.contains(filtradosListaTiposFunciones.get(index))) {
                    int crearIndex = listaTiposFuncionesCrear.indexOf(filtradosListaTiposFunciones.get(index));
                    listaTiposFuncionesCrear.remove(crearIndex);
                } else {
                    listaTiposFuncionesBorrar.add(filtradosListaTiposFunciones.get(index));
                }
                int CIndex = listaTiposFunciones.indexOf(filtradosListaTiposFunciones.get(index));
                listaTiposFunciones.remove(CIndex);
                filtradosListaTiposFunciones.remove(index);
                System.out.println("Realizado");
            }

            RequestContext context = RequestContext.getCurrentInstance();
            context.update("form:datosTiposFunciones");
            index = -1;
            secRegistro = null;

            if (guardado == true) {
                guardado = false;
                context.update("form:ACEPTAR");
            }
        }
    }

    public void salir() {
        if (bandera == 1) {
            //CERRAR FILTRADO
            altoTabla = "270";
            FacesContext c = FacesContext.getCurrentInstance();

            System.out.println("Desactivar");
            System.out.println("TipoLista= " + tipoLista);
            tiposFuncionesIniciales = (Column) c.getViewRoot().findComponent("form:datosTiposFunciones:tiposFuncionesIniciales");
            tiposFuncionesIniciales.setFilterStyle("display: none; visibility: hidden;");
            tiposFuncionesFinales = (Column) c.getViewRoot().findComponent("form:datosTiposFunciones:tiposFuncionesFinales");
            tiposFuncionesFinales.setFilterStyle("display: none; visibility: hidden;");
            tiposFuncionesObjetos = (Column) c.getViewRoot().findComponent("form:datosTiposFunciones:tiposFuncionesObjetos");
            tiposFuncionesObjetos.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTiposFunciones");
            bandera = 0;
            filtradosListaTiposFunciones = null;
            tipoLista = 0;
        }
        listaTiposFuncionesBorrar.clear();
        listaTiposFuncionesCrear.clear();
        listaTiposFuncionesModificar.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listaTiposFunciones = null;
        guardado = true;
        permitirIndex = true;

    }

    //Getter & Setter
    public List<TiposFunciones> getListaTiposFunciones() {
        if (listaTiposFunciones == null) {
            System.out.println("secOperando" + secOperando);
            System.out.println("tOperando" + tOperando);
            System.out.println("operando seleccionado" + operando);
            listaTiposFunciones = administrarTiposFunciones.buscarTiposFunciones(secOperando, tOperando);
        }
        return listaTiposFunciones;
    }

    public void setListaTiposFunciones(List<TiposFunciones> listaTiposFunciones) {
        this.listaTiposFunciones = listaTiposFunciones;
    }

    public List<TiposFunciones> getFiltradosListaTiposFunciones() {
        return filtradosListaTiposFunciones;
    }

    public void setFiltradosListaTiposFunciones(List<TiposFunciones> filtradosListaTiposFunciones) {
        this.filtradosListaTiposFunciones = filtradosListaTiposFunciones;
    }

    public TiposFunciones getEditarTiposFunciones() {
        return editarTiposFunciones;
    }

    public void setEditarTiposFunciones(TiposFunciones editarTiposFunciones) {
        this.editarTiposFunciones = editarTiposFunciones;
    }

    public boolean isAceptarEditar() {
        return aceptarEditar;
    }

    public void setAceptarEditar(boolean aceptarEditar) {
        this.aceptarEditar = aceptarEditar;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public void setAceptar(boolean aceptar) {
        this.aceptar = aceptar;
    }

    public BigInteger getSecRegistro() {
        return secRegistro;
    }

    public void setSecRegistro(BigInteger secRegistro) {
        this.secRegistro = secRegistro;
    }

    public TiposFunciones getNuevoTipoFuncion() {
        return nuevoTipoFuncion;
    }

    public void setNuevoTipoFuncion(TiposFunciones nuevoTipoFuncion) {
        this.nuevoTipoFuncion = nuevoTipoFuncion;
    }

    public String getMensajeValidacion() {
        return mensajeValidacion;
    }

    public void setMensajeValidacion(String mensajeValidacion) {
        this.mensajeValidacion = mensajeValidacion;
    }

    public String getAltoTabla() {
        return altoTabla;
    }

    public void setAltoTabla(String altoTabla) {
        this.altoTabla = altoTabla;
    }

    public BigInteger getSecOperando() {
        return secOperando;
    }

    public void setSecOperando(BigInteger secOperando) {
        this.secOperando = secOperando;
    }

    public String gettOperando() {
        return tOperando;
    }

    public void settOperando(String tOperando) {
        this.tOperando = tOperando;
    }

    public Operandos getOperando() {
        return operando;
    }

    public void setOperando(Operandos operando) {
        this.operando = operando;
    }

    public boolean isCambiosPagina() {
        return cambiosPagina;
    }

    public void setCambiosPagina(boolean cambiosPagina) {
        this.cambiosPagina = cambiosPagina;
    }

    public TiposFunciones getDuplicarTipoFuncion() {
        return duplicarTipoFuncion;
    }

    public void setDuplicarTipoFuncion(TiposFunciones duplicarTipoFuncion) {
        this.duplicarTipoFuncion = duplicarTipoFuncion;
    }

    public TiposFunciones getTipoFuncionSeleccionado() {
        return tipoFuncionSeleccionado;
    }

    public void setTipoFuncionSeleccionado(TiposFunciones tipoFuncionSeleccionado) {
        this.tipoFuncionSeleccionado = tipoFuncionSeleccionado;
    }

    public boolean isGuardado() {
        return guardado;
    }

    public void setGuardado(boolean guardado) {
        this.guardado = guardado;
    }

}
