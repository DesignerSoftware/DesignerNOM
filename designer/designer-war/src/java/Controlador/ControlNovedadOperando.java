/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import utilidadesUI.PrimefacesContextUI;
import Entidades.NovedadesOperandos;
import Entidades.Operandos;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarNovedadesOperandosInterface;
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
public class ControlNovedadOperando implements Serializable {

    @EJB
    AdministrarNovedadesOperandosInterface administrarNovedadesOperandos;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    //Parametros que llegan
    private BigInteger secOperando;
    private String tOperando;
    private Operandos operando;
    //LISTA INFOREPORTES
    private List<NovedadesOperandos> listaNovedadesOperandos;
    private List<NovedadesOperandos> filtradosListaNovedadesOperandos;
    //L.O.V INFOREPORTES
    private List<NovedadesOperandos> lovlistaNovedadesOperandos;
    private List<NovedadesOperandos> lovfiltradoslistaNovedadesOperandos;
    private NovedadesOperandos operandosSeleccionado;
    //editar celda
    private NovedadesOperandos editarNovedadesOperandos;
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
    private List<NovedadesOperandos> listaNovedadesOperandosCrear;
    public NovedadesOperandos nuevoNovedadOperando;
    public NovedadesOperandos duplicarNovedadOperando;
    private int k;
    private BigInteger l;
    private String mensajeValidacion;
    //Modificar Novedades
    private List<NovedadesOperandos> listaNovedadesOperandosModificar;
    //Borrar Novedades
    private List<NovedadesOperandos> listaNovedadesOperandosBorrar;
    //AUTOCOMPLETAR
    private String Operando;
    //Columnas Tabla Ciudades
    private Column novedadesOperandosNombre;
    //ALTO SCROLL TABLA
    private String altoTabla;
    private boolean cambiosPagina;
    //L.O.V OPERANDOS
    private List<Operandos> lovListaOperandos;
    private List<Operandos> lovFiltradosListaOperandos;
    private Operandos seleccionOperandos;

    public ControlNovedadOperando() {
        cambiosPagina = true;
        nuevoNovedadOperando = new NovedadesOperandos();
        permitirIndex = true;
        permitirIndex = true;
        aceptar = true;
        secRegistro = null;
        guardado = true;
        tipoLista = 0;
        listaNovedadesOperandosBorrar = new ArrayList<NovedadesOperandos>();
        listaNovedadesOperandosCrear = new ArrayList<NovedadesOperandos>();
        listaNovedadesOperandosModificar = new ArrayList<NovedadesOperandos>();
        altoTabla = "245";
        duplicarNovedadOperando = new NovedadesOperandos();
        lovListaOperandos = null;
    }
    
    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarNovedadesOperandos.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    //UBICACION CELDA
    public void cambiarIndice(int indice, int celda) {
        if (permitirIndex == true) {
            index = indice;
            cualCelda = celda;
            if (tipoLista == 0) {
                secRegistro = listaNovedadesOperandos.get(index).getSecuencia();
            } else {
                secRegistro = filtradosListaNovedadesOperandos.get(index).getSecuencia();
            }
        }
    }

    public void recibirDatosOperando(BigInteger secuenciaOperando, Operandos operandoRegistro) {
        secOperando = secuenciaOperando;
        operando = operandoRegistro;
        System.out.println("secOperando " + secOperando + "operando" + operando);
        listaNovedadesOperandos = null;
        getListaNovedadesOperandos();
    }

    //AUTOCOMPLETAR
    public void modificarNovedadesOperandos(int indice, String confirmarCambio, String valorConfirmar) {
        index = indice;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;

        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("N")) {
            if (tipoLista == 0) {
                if (!listaNovedadesOperandosCrear.contains(listaNovedadesOperandos.get(index))) {

                    if (listaNovedadesOperandosModificar.isEmpty()) {
                        listaNovedadesOperandosModificar.add(listaNovedadesOperandos.get(index));
                    } else if (!listaNovedadesOperandosModificar.contains(listaNovedadesOperandos.get(index))) {
                        listaNovedadesOperandosModificar.add(listaNovedadesOperandos.get(index));
                    }
                    if (guardado == true) {
                        guardado = false;
                    }
                    cambiosPagina = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
                index = -1;
                secRegistro = null;
            } else {
                if (!listaNovedadesOperandosCrear.contains(filtradosListaNovedadesOperandos.get(index))) {

                    if (listaNovedadesOperandosCrear.isEmpty()) {
                        listaNovedadesOperandosCrear.add(filtradosListaNovedadesOperandos.get(index));
                    } else if (!listaNovedadesOperandosCrear.contains(filtradosListaNovedadesOperandos.get(index))) {
                        listaNovedadesOperandosCrear.add(filtradosListaNovedadesOperandos.get(index));
                    }
                    if (guardado == true) {
                        guardado = false;
                    }
                    cambiosPagina = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
                index = -1;
                secRegistro = null;
            }
            RequestContext.getCurrentInstance().update("form:datosNovedadesOperandos");
        } else if (confirmarCambio.equalsIgnoreCase("OPERANDO")) {
            if (tipoLista == 0) {
                listaNovedadesOperandos.get(indice).getOperando().setNombre(Operando);
            } else {
                filtradosListaNovedadesOperandos.get(indice).getOperando().setNombre(Operando);
            }

            for (int i = 0; i < lovListaOperandos.size(); i++) {
                if (lovListaOperandos.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoLista == 0) {
                    listaNovedadesOperandos.get(indice).setOperando(lovListaOperandos.get(indiceUnicoElemento));
                } else {
                    filtradosListaNovedadesOperandos.get(indice).setOperando(lovListaOperandos.get(indiceUnicoElemento));
                }
                lovListaOperandos.clear();
                getLovListaOperandos();
            } else {
                permitirIndex = false;
                RequestContext.getCurrentInstance().update("formularioDialogos:operandosDialogo");
                PrimefacesContextUI.ejecutar("PF('operandosDialogo').show()");
                tipoActualizacion = 0;
            }
        }
    }

    public void asignarIndex(Integer indice, int dlg, int LND) {

        index = indice;
        RequestContext context = RequestContext.getCurrentInstance();

        if (LND == 0) {
            tipoActualizacion = 0;
        } else if (LND == 1) {
            tipoActualizacion = 1;
            index = -1;
            secRegistro = null;
            System.out.println("Tipo Actualizacion: " + tipoActualizacion);
        } else if (LND == 2) {
            index = -1;
            secRegistro = null;
            tipoActualizacion = 2;
        }
        if (dlg == 0) {
            lovListaOperandos.clear();
            lovListaOperandos.add(operando);
            System.out.println("Operando en asignar Index" + operando);
            RequestContext.getCurrentInstance().update("formularioDialogos:operandosDialogo");
            PrimefacesContextUI.ejecutar("PF('operandosDialogo').show()");
        }

    }

    //LISTA DE VALORES DINAMICA
    public void listaValoresBoton() {
        if (index >= 0) {
            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCelda == 2) {
                RequestContext.getCurrentInstance().update("form:formulasDialogo");
                PrimefacesContextUI.ejecutar("PF('formulasDialogo').show()");
                tipoActualizacion = 0;
            }
        }
    }

    public void valoresBackupAutocompletar(int tipoNuevo, String Campo) {
        if (Campo.equals("OPERANDO")) {
            if (tipoNuevo == 1) {
                Operando = nuevoNovedadOperando.getOperando().getNombre();
            } else if (tipoNuevo == 2) {
                Operando = duplicarNovedadOperando.getOperando().getNombre();
            }
        }

    }

    public void autocompletarNuevoyDuplicado(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("OPERANDO")) {
            if (tipoNuevo == 1) {
                nuevoNovedadOperando.getOperando().setNombre(Operando);
            } else if (tipoNuevo == 2) {
                duplicarNovedadOperando.getOperando().setNombre(Operando);
            }
            for (int i = 0; i < lovListaOperandos.size(); i++) {
                if (lovListaOperandos.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevoNovedadOperando.setOperando(lovListaOperandos.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoOperando");

                } else if (tipoNuevo == 2) {
                    duplicarNovedadOperando.setOperando(lovListaOperandos.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarOperando");

                }
                lovListaOperandos.clear();
                getLovListaOperandos();
            } else {
                RequestContext.getCurrentInstance().update("form:operandosDialogo");
                PrimefacesContextUI.ejecutar("PF('operandosDialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoOperando");
                } else if (tipoNuevo == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarOperando");
                }
            }
        }
    }

    public void eventoFiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void activarCtrlF11() {

        if (bandera == 0) {
            altoTabla = "225";
            novedadesOperandosNombre = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosNovedadesOperandos:novedadesOperandosNombre");
            novedadesOperandosNombre.setFilterStyle("width: 85% !important;");
            RequestContext.getCurrentInstance().update("form:datosNovedadesOperandos");
            bandera = 1;
            tipoLista = 1;
        } else if (bandera == 1) {
            altoTabla = "245";
            System.out.println("Desactivar");
            System.out.println("TipoLista= " + tipoLista);
            novedadesOperandosNombre = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosNovedadesOperandos:novedadesOperandosNombre");
            novedadesOperandosNombre.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosNovedadesOperandos");
            bandera = 0;
            filtradosListaNovedadesOperandos = null;
            tipoLista = 0;
        }
    }

    public void cancelarModificacion() {
        if (bandera == 1) {
            //CERRAR FILTRADO
            altoTabla = "245";
            System.out.println("Desactivar");
            System.out.println("TipoLista= " + tipoLista);
            novedadesOperandosNombre = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosNovedadesOperandos:novedadesOperandosNombre");
            novedadesOperandosNombre.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosNovedadesOperandos");
            bandera = 0;
            filtradosListaNovedadesOperandos = null;
            tipoLista = 0;
        }

        listaNovedadesOperandosBorrar.clear();
        listaNovedadesOperandosCrear.clear();
        listaNovedadesOperandosModificar.clear();
        lovListaOperandos.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listaNovedadesOperandos = null;
        getListaNovedadesOperandos();
        guardado = true;
        permitirIndex = true;
        cambiosPagina = true;

        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("form:datosNovedadesOperandos");
    }

    //MOSTRAR DATOS CELDA
    public void editarCelda() {
        if (index >= 0) {
            if (tipoLista == 0) {
                editarNovedadesOperandos = listaNovedadesOperandos.get(index);
            }
            if (tipoLista == 1) {
                editarNovedadesOperandos = filtradosListaNovedadesOperandos.get(index);
            }

            RequestContext context = RequestContext.getCurrentInstance();
            System.out.println("Entro a editar... valor celda: " + cualCelda);
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarOperandos");
                PrimefacesContextUI.ejecutar("PF('editarOperandos').show()");
                cualCelda = -1;
            }
        }
        index = -1;
        secRegistro = null;
    }

    //EXPORTAR
    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosNovedadesOperandosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "NovedadesOperandosPDF", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosNovedadesOperandosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "NovedadesOperandosXLS", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    //LIMPIAR NUEVO REGISTRO CIUDAD
    public void limpiarNuevoNovedadesOperandos() {
        nuevoNovedadOperando = new NovedadesOperandos();
        index = -1;
        secRegistro = null;
    }

    public void limpiarduplicarNovedadesOperandos() {
        duplicarNovedadOperando = new NovedadesOperandos();
        index = -1;
        secRegistro = null;
    }

    //DUPLICAR Operando
    public void duplicarNO() {
        if (index >= 0) {
            duplicarNovedadOperando = new NovedadesOperandos();
            k++;
            l = BigInteger.valueOf(k);

            if (tipoLista == 0) {
                duplicarNovedadOperando.setSecuencia(l);
                duplicarNovedadOperando.setOperando(listaNovedadesOperandos.get(index).getOperando());
            }
            if (tipoLista == 1) {
                duplicarNovedadOperando.setSecuencia(l);
                duplicarNovedadOperando.setOperando(filtradosListaNovedadesOperandos.get(index).getOperando());

            }

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarNovedadOperando");
            PrimefacesContextUI.ejecutar("PF('DuplicarNovedadOperando').show()");
            index = -1;
            secRegistro = null;
        }
    }

    //GUARDAR
    public void guardarCambiosNovedadesOperandos() {
        if (guardado == false) {
            System.out.println("Realizando Operaciones Novedades");

            if (!listaNovedadesOperandosBorrar.isEmpty()) {
                for (int i = 0; i < listaNovedadesOperandosBorrar.size(); i++) {
                    System.out.println("Borrando..." + listaNovedadesOperandosBorrar.size());
                    administrarNovedadesOperandos.borrarNovedadesOperandos(listaNovedadesOperandosBorrar.get(i));
                }
                System.out.println("Entra");
                listaNovedadesOperandosBorrar.clear();
            }

            if (!listaNovedadesOperandosCrear.isEmpty()) {
                for (int i = 0; i < listaNovedadesOperandosCrear.size(); i++) {
                    System.out.println("Creando...");
                    administrarNovedadesOperandos.crearNovedadesOperandos(listaNovedadesOperandosCrear.get(i));
                }
                System.out.println("LimpiaLista");
                listaNovedadesOperandosCrear.clear();
            }
            if (!listaNovedadesOperandosModificar.isEmpty()) {
                for (int i = 0; i < listaNovedadesOperandosModificar.size(); i++) {
                    administrarNovedadesOperandos.modificarNovedadesOperandos(listaNovedadesOperandosModificar.get(i));
                }
                listaNovedadesOperandosModificar.clear();
            }

            System.out.println("Se guardaron los datos con exito");
            listaNovedadesOperandos = null;

            RequestContext context = RequestContext.getCurrentInstance();
            cambiosPagina = true;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosNovedadesOperandos");
            guardado = true;
            permitirIndex = true;
            RequestContext.getCurrentInstance().update("form:aceptar");
            //  k = 0;
        }
        index = -1;
        secRegistro = null;
    }

    //RASTROS 
    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (!listaNovedadesOperandos.isEmpty()) {
            if (secRegistro != null) {
                int result = administrarRastros.obtenerTabla(secRegistro, "NOVEDADESOPERANDOS");
                System.out.println("resultado: " + result);
                if (result == 1) {
                    PrimefacesContextUI.ejecutar("PF('errorObjetosDB').show()");
                } else if (result == 2) {
                    PrimefacesContextUI.ejecutar("PF('confirmarRastro').show()");
                } else if (result == 3) {
                    PrimefacesContextUI.ejecutar("PF('errorRegistroRastro').show()");
                } else if (result == 4) {
                    PrimefacesContextUI.ejecutar("PF('errorTablaConRastro').show()");
                } else if (result == 5) {
                    PrimefacesContextUI.ejecutar("PF('errorTablaSinRastro').show()");
                }
            } else {
                PrimefacesContextUI.ejecutar("PF('seleccionarRegistro').show()");
            }
        } else {
            if (administrarRastros.verificarHistoricosTabla("NOVEDADESOPERANDOS")) {
                PrimefacesContextUI.ejecutar("PF('confirmarRastroHistorico').show()");
            } else {
                PrimefacesContextUI.ejecutar("PF('errorRastroHistorico').show()");
            }
        }
        index = -1;
    }

    public void actualizarOperando() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                listaNovedadesOperandos.get(index).setOperando(seleccionOperandos);
                if (!listaNovedadesOperandosCrear.contains(listaNovedadesOperandos.get(index))) {
                    if (listaNovedadesOperandosModificar.isEmpty()) {
                        listaNovedadesOperandosModificar.add(listaNovedadesOperandos.get(index));
                    } else if (!listaNovedadesOperandosModificar.contains(listaNovedadesOperandos.get(index))) {
                        listaNovedadesOperandosModificar.add(listaNovedadesOperandos.get(index));
                    }
                }
            } else {
                filtradosListaNovedadesOperandos.get(index).setOperando(seleccionOperandos);
                if (!listaNovedadesOperandosCrear.contains(filtradosListaNovedadesOperandos.get(index))) {
                    if (listaNovedadesOperandosModificar.isEmpty()) {
                        listaNovedadesOperandosModificar.add(filtradosListaNovedadesOperandos.get(index));
                    } else if (!listaNovedadesOperandosModificar.contains(filtradosListaNovedadesOperandos.get(index))) {
                        listaNovedadesOperandosModificar.add(filtradosListaNovedadesOperandos.get(index));
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
            }
            permitirIndex = true;
            cambiosPagina = false;

            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosNovedadesOperandos");
        } else if (tipoActualizacion == 1) {
            nuevoNovedadOperando.setOperando(seleccionOperandos);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoOperando");
        } else if (tipoActualizacion == 2) {
            duplicarNovedadOperando.setOperando(seleccionOperandos);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarOperando");

        }
        filtradosListaNovedadesOperandos = null;
        seleccionOperandos = null;
        aceptar = true;
        index = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("formularioDialogos:LOVOperandos:globalFilter");
        PrimefacesContextUI.ejecutar("PF('LOVOperandos').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('operandosDialogo').hide()");
        //RequestContext.getCurrentInstance().update("formularioDialogos:LOVOperandos");
    }

    public void cancelarCambioOperandos() {
        lovFiltradosListaOperandos = null;
        seleccionOperandos = null;
        aceptar = true;
        index = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:LOVOperandos:globalFilter");
        PrimefacesContextUI.ejecutar("PF('LOVOperandos').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('operandosDialogo').hide()");
    }

    public void agregarNuevoNovedadOperando() {
        int pasa = 0;
        int pasa2 = 0;
        mensajeValidacion = new String();

        RequestContext context = RequestContext.getCurrentInstance();
        System.out.println("nuevoNovedadOperando.getOperando().getNombre()" + nuevoNovedadOperando.getOperando().getNombre());
        if (nuevoNovedadOperando.getOperando().getNombre().equals(" ")) {
            mensajeValidacion = mensajeValidacion + " * Nombre\n";
            pasa++;
        }

        for (int i = 0; i < listaNovedadesOperandos.size(); i++) {
            if (nuevoNovedadOperando.getOperando().getNombre().equals(listaNovedadesOperandos.get(i).getOperando().getNombre())) {
                RequestContext.getCurrentInstance().update("formularioDialogos:operandorecalculado");
                PrimefacesContextUI.ejecutar("PF('operandorecalculado').show()");
                pasa2++;
            }

        }

        if (pasa != 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevoNovedadOperando");
            PrimefacesContextUI.ejecutar("PF('validacionNuevoNovedadOperando').show()");
        }

        if (pasa == 0 && pasa2 == 0) {
            if (bandera == 1) {
                //CERRAR FILTRADO
                altoTabla = "245";
                System.out.println("Desactivar");
                System.out.println("TipoLista= " + tipoLista);
                novedadesOperandosNombre = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosNovedadesOperandos:novedadesOperandosNombre");
                novedadesOperandosNombre.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosNovedadesOperandos");
                bandera = 0;
                filtradosListaNovedadesOperandos = null;
                tipoLista = 0;
            }
            //AGREGAR REGISTRO A LA LISTA NOVEDADES .
            k++;
            l = BigInteger.valueOf(k);
            nuevoNovedadOperando.setSecuencia(l);
            System.out.println("Operando: " + operando);
            nuevoNovedadOperando.setOperando(operando);

            cambiosPagina = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            listaNovedadesOperandosCrear.add(nuevoNovedadOperando);
            listaNovedadesOperandos.add(nuevoNovedadOperando);
            nuevoNovedadOperando = new NovedadesOperandos();
            RequestContext.getCurrentInstance().update("form:datosNovedadesOperandos");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:aceptar");
            }

            PrimefacesContextUI.ejecutar("PF('NuevoNovedadOperando').hide()");
            index = -1;
            secRegistro = null;
        }
    }

    //BORRAR CIUDADES
    public void borrarNovedadOperando() {

        if (index >= 0) {
            if (tipoLista == 0) {
                if (!listaNovedadesOperandosModificar.isEmpty() && listaNovedadesOperandosModificar.contains(listaNovedadesOperandos.get(index))) {
                    int modIndex = listaNovedadesOperandosModificar.indexOf(listaNovedadesOperandos.get(index));
                    listaNovedadesOperandosModificar.remove(modIndex);
                    listaNovedadesOperandosBorrar.add(listaNovedadesOperandos.get(index));
                } else if (!listaNovedadesOperandosCrear.isEmpty() && listaNovedadesOperandosCrear.contains(listaNovedadesOperandos.get(index))) {
                    int crearIndex = listaNovedadesOperandosCrear.indexOf(listaNovedadesOperandos.get(index));
                    listaNovedadesOperandosCrear.remove(crearIndex);
                } else {
                    listaNovedadesOperandosBorrar.add(listaNovedadesOperandos.get(index));
                }
                listaNovedadesOperandos.remove(index);
            }

            if (tipoLista == 1) {
                if (!listaNovedadesOperandosModificar.isEmpty() && listaNovedadesOperandosModificar.contains(filtradosListaNovedadesOperandos.get(index))) {
                    int modIndex = listaNovedadesOperandosModificar.indexOf(filtradosListaNovedadesOperandos.get(index));
                    listaNovedadesOperandosModificar.remove(modIndex);
                    listaNovedadesOperandosBorrar.add(filtradosListaNovedadesOperandos.get(index));
                } else if (!listaNovedadesOperandosCrear.isEmpty() && listaNovedadesOperandosCrear.contains(filtradosListaNovedadesOperandos.get(index))) {
                    int crearIndex = listaNovedadesOperandosCrear.indexOf(filtradosListaNovedadesOperandos.get(index));
                    listaNovedadesOperandosCrear.remove(crearIndex);
                } else {
                    listaNovedadesOperandosBorrar.add(filtradosListaNovedadesOperandos.get(index));
                }
                int CIndex = listaNovedadesOperandos.indexOf(filtradosListaNovedadesOperandos.get(index));
                listaNovedadesOperandos.remove(CIndex);
                filtradosListaNovedadesOperandos.remove(index);
                System.out.println("Realizado");
            }

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosNovedadesOperandos");
            index = -1;
            secRegistro = null;

            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:aceptar");
            }
        }
    }

    public void salir() {
        if (bandera == 1) {
            //CERRAR FILTRADO
            altoTabla = "245";
            System.out.println("Desactivar");
            System.out.println("TipoLista= " + tipoLista);
            novedadesOperandosNombre = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosNovedadesOperandos:novedadesOperandosNombre");
            novedadesOperandosNombre.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosNovedadesOperandos");
            bandera = 0;
            filtradosListaNovedadesOperandos = null;
            tipoLista = 0;
        }
        listaNovedadesOperandosBorrar.clear();
        listaNovedadesOperandosCrear.clear();
        listaNovedadesOperandosModificar.clear();
        lovListaOperandos.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listaNovedadesOperandos = null;
        guardado = true;
        permitirIndex = true;

    }

    public void confirmarDuplicar() {
        int pasa = 0;
        mensajeValidacion = new String();
        RequestContext context = RequestContext.getCurrentInstance();

        if (duplicarNovedadOperando.getOperando().getNombre() == null) {
            mensajeValidacion = mensajeValidacion + " * Nombre\n";
            pasa++;
        }

        if (pasa != 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevoNovedadOperando");
            PrimefacesContextUI.ejecutar("PF('validacionNuevoNovedadOperando').show()");
        }

        if (pasa == 0) {
            if (bandera == 1) {
                altoTabla = "245";
                System.out.println("Desactivar");
                System.out.println("TipoLista= " + tipoLista);
                novedadesOperandosNombre = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosNovedadesOperandos:novedadesOperandosNombre");
                novedadesOperandosNombre.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosNovedadesOperandos");
                bandera = 0;
                filtradosListaNovedadesOperandos = null;
                tipoLista = 0;
            }
            cambiosPagina = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            //Falta Ponerle el Operando al cual se agregará
            duplicarNovedadOperando.setOperando(operando);
            listaNovedadesOperandos.add(duplicarNovedadOperando);
            listaNovedadesOperandosCrear.add(duplicarNovedadOperando);

            index = -1;
            if (guardado == true) {
                guardado = false;
                //RequestContext.getCurrentInstance().update("form:aceptar");
            }
            RequestContext.getCurrentInstance().update("form:datosNovedadesOperandos");
            duplicarNovedadOperando = new NovedadesOperandos();
            RequestContext.getCurrentInstance().update("formularioDialogos:DuplicarNovedadOperando");
            PrimefacesContextUI.ejecutar("PF('DuplicarNovedadOperando').hide()");
        }
    }

    //Getter & Setter
    public List<NovedadesOperandos> getListaNovedadesOperandos() {
        if (listaNovedadesOperandos == null) {
            System.out.println("secOperando" + secOperando);
            listaNovedadesOperandos = administrarNovedadesOperandos.buscarNovedadesOperandos(secOperando);
        }
        return listaNovedadesOperandos;
    }

    public void setListaNovedadesOperandos(List<NovedadesOperandos> listaNovedadesOperandos) {
        this.listaNovedadesOperandos = listaNovedadesOperandos;
    }

    public List<NovedadesOperandos> getFiltradosListaNovedadesOperandos() {
        return filtradosListaNovedadesOperandos;
    }

    public void setFiltradosListaNovedadesOperandos(List<NovedadesOperandos> filtradosListaNovedadesOperandos) {
        this.filtradosListaNovedadesOperandos = filtradosListaNovedadesOperandos;
    }

    public NovedadesOperandos getEditarNovedadesOperandos() {
        return editarNovedadesOperandos;
    }

    public void setEditarNovedadesOperandos(NovedadesOperandos editarNovedadesOperandos) {
        this.editarNovedadesOperandos = editarNovedadesOperandos;
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

    public NovedadesOperandos getNuevoNovedadOperando() {
        return nuevoNovedadOperando;
    }

    public void setNuevoNovedadOperando(NovedadesOperandos nuevoNovedadOperando) {
        this.nuevoNovedadOperando = nuevoNovedadOperando;
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

    public NovedadesOperandos getDuplicarNovedadOperando() {
        return duplicarNovedadOperando;
    }

    public void setDuplicarNovedadOperando(NovedadesOperandos duplicarNovedadOperando) {
        this.duplicarNovedadOperando = duplicarNovedadOperando;
    }

    public List<Operandos> getLovListaOperandos() {
        if (lovListaOperandos == null) {
            lovListaOperandos = administrarNovedadesOperandos.buscarOperandos();
        }
        return lovListaOperandos;
    }

    public void setLovListaOperandos(List<Operandos> lovListaOperandos) {
        this.lovListaOperandos = lovListaOperandos;
    }

    public List<Operandos> getLovFiltradosListaOperandos() {
        return lovFiltradosListaOperandos;
    }

    public void setLovFiltradosListaOperandos(List<Operandos> lovFiltradosListaOperandos) {
        this.lovFiltradosListaOperandos = lovFiltradosListaOperandos;
    }

    public Operandos getSeleccionOperandos() {
        return seleccionOperandos;
    }

    public void setSeleccionOperandos(Operandos seleccionOperandos) {
        this.seleccionOperandos = seleccionOperandos;
    }

}
