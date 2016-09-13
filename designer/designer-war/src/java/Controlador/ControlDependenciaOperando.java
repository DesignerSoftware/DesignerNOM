/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import utilidadesUI.PrimefacesContextUI;
import Entidades.DependenciasOperandos;
import Entidades.Operandos;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarDependenciasOperandosInterface;
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
public class ControlDependenciaOperando implements Serializable {

    @EJB
    AdministrarDependenciasOperandosInterface administrarDependenciasOperandos;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    //Parametros que llegan
    private BigInteger secOperando;
    private String tOperando;
    private Operandos operando;
    //LISTA INFOREPORTES
    private List<DependenciasOperandos> listaDependenciasOperandos;
    private List<DependenciasOperandos> filtradosListaDependenciasOperandos;
    //L.O.V INFOREPORTES
    private List<DependenciasOperandos> lovlistaDependenciasOperandos;
    private List<DependenciasOperandos> lovfiltradoslistaDependenciasOperandos;
    private DependenciasOperandos operandosSeleccionado;
    //editar celda
    private DependenciasOperandos editarDependenciasOperandos;
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
    private List<DependenciasOperandos> listaDependenciasOperandosCrear;
    public DependenciasOperandos nuevoDependenciaOperando;
    public DependenciasOperandos duplicarDependenciaOperando;
    private int k;
    private BigInteger l;
    private String mensajeValidacion;
    //Modificar Novedades
    private List<DependenciasOperandos> listaDependenciasOperandosModificar;
    //Borrar Novedades
    private List<DependenciasOperandos> listaDependenciasOperandosBorrar;
    //AUTOCOMPLETAR
    private String Operando;
    //Columnas Tabla Ciudades
    private Column dependenciasOperandosNombre, dependenciasOperandosCodigo, dependenciasOperandosConsecutivo;
    //ALTO SCROLL TABLA
    private String altoTabla;
    private boolean cambiosPagina;
    //L.O.V OPERANDOS
    private List<Operandos> lovListaOperandos;
    private List<Operandos> lovFiltradosListaOperandos;
    private Operandos seleccionOperandos;
    private String nombre;

    public ControlDependenciaOperando() {
        cambiosPagina = true;
        nuevoDependenciaOperando = new DependenciasOperandos();
        permitirIndex = true;
        permitirIndex = true;
        aceptar = true;
        secRegistro = null;
        guardado = true;
        tipoLista = 0;
        listaDependenciasOperandosBorrar = new ArrayList<DependenciasOperandos>();
        listaDependenciasOperandosCrear = new ArrayList<DependenciasOperandos>();
        listaDependenciasOperandosModificar = new ArrayList<DependenciasOperandos>();
        altoTabla = "245";
        duplicarDependenciaOperando = new DependenciasOperandos();
        lovListaOperandos = null;
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarDependenciasOperandos.obtenerConexion(ses.getId());
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
                secRegistro = listaDependenciasOperandos.get(index).getSecuencia();
            } else {
                secRegistro = filtradosListaDependenciasOperandos.get(index).getSecuencia();
            }
        }
    }

    public void recibirDatosOperando(BigInteger secuenciaOperando, Operandos operandoSeleccionado) {
        secOperando = secuenciaOperando;
        operando = operandoSeleccionado;
        System.out.println("secOperando " + secOperando + "operando" + operando);
        listaDependenciasOperandos = null;
        getListaDependenciasOperandos();
    }

    //AUTOCOMPLETAR
    public void modificarDependenciasOperandos(int indice, String confirmarCambio, String valorConfirmar) {
        index = indice;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;

        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("N")) {
            if (tipoLista == 0) {
                if (!listaDependenciasOperandosCrear.contains(listaDependenciasOperandos.get(index))) {

                    if (listaDependenciasOperandosModificar.isEmpty()) {
                        listaDependenciasOperandosModificar.add(listaDependenciasOperandos.get(index));
                    } else if (!listaDependenciasOperandosModificar.contains(listaDependenciasOperandos.get(index))) {
                        listaDependenciasOperandosModificar.add(listaDependenciasOperandos.get(index));
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
                if (!listaDependenciasOperandosCrear.contains(filtradosListaDependenciasOperandos.get(index))) {

                    if (listaDependenciasOperandosCrear.isEmpty()) {
                        listaDependenciasOperandosCrear.add(filtradosListaDependenciasOperandos.get(index));
                    } else if (!listaDependenciasOperandosCrear.contains(filtradosListaDependenciasOperandos.get(index))) {
                        listaDependenciasOperandosCrear.add(filtradosListaDependenciasOperandos.get(index));
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
            RequestContext.getCurrentInstance().update("form:datosDependenciasOperandos");
        } else if (confirmarCambio.equalsIgnoreCase("OPERANDO")) {
            if (tipoLista == 0) {
                listaDependenciasOperandos.get(indice).getOperando().setNombre(Operando);
            } else {
                filtradosListaDependenciasOperandos.get(indice).getOperando().setNombre(Operando);
            }

            for (int i = 0; i < listaDependenciasOperandos.size(); i++) {
                if (listaDependenciasOperandos.get(i).getOperando().getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoLista == 0) {
                    listaDependenciasOperandos.get(indice).setOperando(lovListaOperandos.get(indiceUnicoElemento));
                } else {
                    filtradosListaDependenciasOperandos.get(indice).setOperando(lovListaOperandos.get(indiceUnicoElemento));
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
            System.out.println("Operando en asignar Index" + operando);
            RequestContext.getCurrentInstance().update("formularioDialogos:operandosDialogo");
            PrimefacesContextUI.ejecutar("PF('operandosDialogo').show()");
        }

    }

    //LISTA DE VALORES DINAMICA
    public void listaValoresBoton() {
        if (index >= 0) {
            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("form:operandosDialogo");
                PrimefacesContextUI.ejecutar("PF('operandosDialogo').show()");
                tipoActualizacion = 0;
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
            dependenciasOperandosCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDependenciasOperandos:dependenciasOperandosCodigo");
            dependenciasOperandosCodigo.setFilterStyle("width: 85%");
            dependenciasOperandosNombre = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDependenciasOperandos:dependenciasOperandosNombre");
            dependenciasOperandosNombre.setFilterStyle("width: 85%");
            dependenciasOperandosConsecutivo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDependenciasOperandos:dependenciasOperandosConsecutivo");
            dependenciasOperandosConsecutivo.setFilterStyle("width: 85%");
            RequestContext.getCurrentInstance().update("form:datosDependenciasOperandos");
            bandera = 1;
            tipoLista = 1;
        } else if (bandera == 1) {
            altoTabla = "245";
            System.out.println("Desactivar");
            System.out.println("TipoLista= " + tipoLista);
            dependenciasOperandosCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDependenciasOperandos:dependenciasOperandosCodigo");
            dependenciasOperandosCodigo.setFilterStyle("display: none; visibility: hidden;");
            dependenciasOperandosNombre = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDependenciasOperandos:dependenciasOperandosNombre");
            dependenciasOperandosNombre.setFilterStyle("display: none; visibility: hidden;");
            dependenciasOperandosConsecutivo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDependenciasOperandos:dependenciasOperandosConsecutivo");
            dependenciasOperandosConsecutivo.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosDependenciasOperandos");
            bandera = 0;
            filtradosListaDependenciasOperandos = null;
            tipoLista = 0;
        }
    }

    public void cancelarModificacion() {
        if (bandera == 1) {
            //CERRAR FILTRADO
            altoTabla = "245";
            System.out.println("Desactivar");
            System.out.println("TipoLista= " + tipoLista);
            dependenciasOperandosCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDependenciasOperandos:dependenciasOperandosCodigo");
            dependenciasOperandosCodigo.setFilterStyle("display: none; visibility: hidden;");
            dependenciasOperandosNombre = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDependenciasOperandos:dependenciasOperandosNombre");
            dependenciasOperandosNombre.setFilterStyle("display: none; visibility: hidden;");
            dependenciasOperandosConsecutivo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDependenciasOperandos:dependenciasOperandosConsecutivo");
            dependenciasOperandosConsecutivo.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosDependenciasOperandos");
            bandera = 0;
            filtradosListaDependenciasOperandos = null;
            tipoLista = 0;
        }

        listaDependenciasOperandosBorrar.clear();
        listaDependenciasOperandosCrear.clear();
        listaDependenciasOperandosModificar.clear();
        lovListaOperandos.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listaDependenciasOperandos = null;
        getListaDependenciasOperandos();
        guardado = true;
        permitirIndex = true;
        cambiosPagina = true;

        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("form:datosDependenciasOperandos");
    }

    //MOSTRAR DATOS CELDA
    public void editarCelda() {
        if (index >= 0) {
            if (tipoLista == 0) {
                editarDependenciasOperandos = listaDependenciasOperandos.get(index);
            }
            if (tipoLista == 1) {
                editarDependenciasOperandos = filtradosListaDependenciasOperandos.get(index);
            }

            RequestContext context = RequestContext.getCurrentInstance();
            System.out.println("Entro a editar... valor celda: " + cualCelda);
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarNombre");
                PrimefacesContextUI.ejecutar("PF('editarNombre').show()");
                cualCelda = -1;
            } else if (cualCelda == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarConsecutivo");
                PrimefacesContextUI.ejecutar("PF('editarConsecutivo').show()");
            }

        }
        index = -1;
        secRegistro = null;
    }

    //EXPORTAR
    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosDependenciasOperandosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "DependenciasOperandosPDF", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosDependenciasOperandosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "DependenciasOperandosXLS", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    //LIMPIAR NUEVO REGISTRO CIUDAD
    public void limpiarNuevoDependenciasOperandos() {
        nuevoDependenciaOperando = new DependenciasOperandos();
        index = -1;
        secRegistro = null;
    }

    public void limpiarduplicarDependenciasOperandos() {
        duplicarDependenciaOperando = new DependenciasOperandos();
        index = -1;
        secRegistro = null;
    }

    public void valoresBackupAutocompletar(int tipoNuevo, String Campo) {
        if (Campo.equals("OPERANDO")) {
            if (tipoNuevo == 1) {
                Operando = nuevoDependenciaOperando.getOperando().getNombre();
            } else if (tipoNuevo == 2) {
                Operando = duplicarDependenciaOperando.getOperando().getNombre();
            }
        }

    }

    public void autocompletarNuevoyDuplicado(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("OPERANDO")) {
            if (tipoNuevo == 1) {
                nuevoDependenciaOperando.getOperando().setNombre(Operando);
            } else if (tipoNuevo == 2) {
                duplicarDependenciaOperando.getOperando().setNombre(Operando);
            }
            for (int i = 0; i < lovListaOperandos.size(); i++) {
                if (lovListaOperandos.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevoDependenciaOperando.setOperando(lovListaOperandos.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoNombre");
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoCodigo");

                } else if (tipoNuevo == 2) {
                    duplicarDependenciaOperando.setOperando(lovListaOperandos.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarNombre");
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCodigo");

                }
                lovListaOperandos.clear();
                getLovListaOperandos();
            } else {
                RequestContext.getCurrentInstance().update("form:operandosDialogo");
                PrimefacesContextUI.ejecutar("PF('operandosDialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoCodigo");
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoNombre");
                } else if (tipoNuevo == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCodigo");
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarNombre");
                }
            }
        }
    }

    //DUPLICAR Operando
    public void duplicarNO() {
        if (index >= 0) {
            duplicarDependenciaOperando = new DependenciasOperandos();
            k++;
            l = BigInteger.valueOf(k);

            if (tipoLista == 0) {
                duplicarDependenciaOperando.setSecuencia(l);
                duplicarDependenciaOperando.setOperando(listaDependenciasOperandos.get(index).getOperando());
                duplicarDependenciaOperando.setConsecutivo(listaDependenciasOperandos.get(index).getConsecutivo());
                duplicarDependenciaOperando.setCodigo(listaDependenciasOperandos.get(index).getCodigo());
                duplicarDependenciaOperando.setDescripcion(listaDependenciasOperandos.get(index).getDescripcion());
            }
            if (tipoLista == 1) {
                duplicarDependenciaOperando.setSecuencia(l);
                duplicarDependenciaOperando.setOperando(filtradosListaDependenciasOperandos.get(index).getOperando());
                duplicarDependenciaOperando.setConsecutivo(filtradosListaDependenciasOperandos.get(index).getConsecutivo());
                duplicarDependenciaOperando.setCodigo(filtradosListaDependenciasOperandos.get(index).getCodigo());
                duplicarDependenciaOperando.setDescripcion(filtradosListaDependenciasOperandos.get(index).getDescripcion());

            }

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarDependenciaOperando");
            PrimefacesContextUI.ejecutar("PF('DuplicarDependenciaOperando').show()");
            index = -1;
            secRegistro = null;
        }
    }

    //GUARDAR
    public void guardarCambiosDependenciasOperandos() {
        if (guardado == false) {
            System.out.println("Realizando Operaciones Novedades");

            if (!listaDependenciasOperandosBorrar.isEmpty()) {
                for (int i = 0; i < listaDependenciasOperandosBorrar.size(); i++) {
                    System.out.println("Borrando..." + listaDependenciasOperandosBorrar.size());
                    administrarDependenciasOperandos.borrarDependenciasOperandos(listaDependenciasOperandosBorrar.get(i));
                }
                System.out.println("Entra");
                listaDependenciasOperandosBorrar.clear();
            }

            if (!listaDependenciasOperandosCrear.isEmpty()) {
                for (int i = 0; i < listaDependenciasOperandosCrear.size(); i++) {
                    System.out.println("Creando...");
                    administrarDependenciasOperandos.crearDependenciasOperandos(listaDependenciasOperandosCrear.get(i));
                }
                System.out.println("LimpiaLista");
                listaDependenciasOperandosCrear.clear();
            }
            if (!listaDependenciasOperandosModificar.isEmpty()) {
                for (int i = 0; i < listaDependenciasOperandosModificar.size(); i++) {
                    administrarDependenciasOperandos.modificarDependenciasOperandos(listaDependenciasOperandosModificar.get(i));
                }
                listaDependenciasOperandosModificar.clear();
            }

            System.out.println("Se guardaron los datos con exito");
            listaDependenciasOperandos = null;

            RequestContext context = RequestContext.getCurrentInstance();
            cambiosPagina = true;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosDependenciasOperandos");
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
        if (!listaDependenciasOperandos.isEmpty()) {
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
                listaDependenciasOperandos.get(index).setDescripcion(seleccionOperandos.getNombre());
                listaDependenciasOperandos.get(index).setCodigo(seleccionOperandos.getCodigo());
                if (!listaDependenciasOperandosCrear.contains(listaDependenciasOperandos.get(index))) {
                    if (listaDependenciasOperandosModificar.isEmpty()) {
                        listaDependenciasOperandosModificar.add(listaDependenciasOperandos.get(index));
                    } else if (!listaDependenciasOperandosModificar.contains(listaDependenciasOperandos.get(index))) {
                        listaDependenciasOperandosModificar.add(listaDependenciasOperandos.get(index));
                    }
                }
            } else {
                filtradosListaDependenciasOperandos.get(index).setDescripcion(seleccionOperandos.getNombre());
                filtradosListaDependenciasOperandos.get(index).setCodigo(seleccionOperandos.getCodigo());
                if (!listaDependenciasOperandosCrear.contains(filtradosListaDependenciasOperandos.get(index))) {
                    if (listaDependenciasOperandosModificar.isEmpty()) {
                        listaDependenciasOperandosModificar.add(filtradosListaDependenciasOperandos.get(index));
                    } else if (!listaDependenciasOperandosModificar.contains(filtradosListaDependenciasOperandos.get(index))) {
                        listaDependenciasOperandosModificar.add(filtradosListaDependenciasOperandos.get(index));
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
            }
            permitirIndex = true;
            cambiosPagina = false;

            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosDependenciasOperandos");
        } else if (tipoActualizacion == 1) {
            System.out.println("LAWWWWWWL");
            nuevoDependenciaOperando.setCodigo(seleccionOperandos.getCodigo());
            nuevoDependenciaOperando.setDescripcion(seleccionOperandos.getNombre());
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoCodigo");
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoNombre");
        } else if (tipoActualizacion == 2) {
            System.out.println("ENTRO DUPLICAR");
            duplicarDependenciaOperando.setCodigo(seleccionOperandos.getCodigo());
            duplicarDependenciaOperando.setDescripcion(seleccionOperandos.getNombre());
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCodigo");
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarNombre");

        }
        System.out.println("listaDependenciasOperandos.size()" + listaDependenciasOperandos.size());
        filtradosListaDependenciasOperandos = null;
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

    public void agregarNuevoDependenciaOperando() {
        int pasa = 0;
        int pasa2 = 0;
        mensajeValidacion = new String();

        RequestContext context = RequestContext.getCurrentInstance();

        if (nuevoDependenciaOperando.getCodigo() == 0) {
            mensajeValidacion = mensajeValidacion + " * Codigo\n";
            pasa++;
        }

        if (nuevoDependenciaOperando.getDescripcion().equals(" ") || nuevoDependenciaOperando.getDescripcion() == null) {
            mensajeValidacion = mensajeValidacion + " * Nombre\n";
            pasa++;
        }

        if (pasa != 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevoDependenciaOperando");
            PrimefacesContextUI.ejecutar("PF('validacionNuevoDependenciaOperando').show()");
        }

        if (pasa == 0) {
            if (bandera == 1) {
                //CERRAR FILTRADO
                altoTabla = "245";
                System.out.println("Desactivar");
                System.out.println("TipoLista= " + tipoLista);
                dependenciasOperandosCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDependenciasOperandos:dependenciasOperandosCodigo");
                dependenciasOperandosCodigo.setFilterStyle("display: none; visibility: hidden;");
                dependenciasOperandosNombre = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDependenciasOperandos:dependenciasOperandosNombre");
                dependenciasOperandosNombre.setFilterStyle("display: none; visibility: hidden;");
                dependenciasOperandosConsecutivo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDependenciasOperandos:dependenciasOperandosConsecutivo");
                dependenciasOperandosConsecutivo.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosDependenciasOperandos");
                bandera = 0;
                filtradosListaDependenciasOperandos = null;
                tipoLista = 0;
            }
            //AGREGAR REGISTRO A LA LISTA NOVEDADES .
            k++;
            l = BigInteger.valueOf(k);
            nuevoDependenciaOperando.setSecuencia(l);
            System.out.println("Operando: " + operando);
            nuevoDependenciaOperando.setOperando(operando);

            cambiosPagina = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            listaDependenciasOperandosCrear.add(nuevoDependenciaOperando);
            listaDependenciasOperandos.add(nuevoDependenciaOperando);
            nuevoDependenciaOperando = new DependenciasOperandos();
            RequestContext.getCurrentInstance().update("form:datosDependenciasOperandos");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:aceptar");
            }
            PrimefacesContextUI.ejecutar("PF('NuevoDependenciaOperando').hide()");
            index = -1;
            secRegistro = null;
        }
    }

    //BORRAR CIUDADES
    public void borrarDependenciaOperando() {

        if (index >= 0) {
            if (tipoLista == 0) {
                if (!listaDependenciasOperandosModificar.isEmpty() && listaDependenciasOperandosModificar.contains(listaDependenciasOperandos.get(index))) {
                    int modIndex = listaDependenciasOperandosModificar.indexOf(listaDependenciasOperandos.get(index));
                    listaDependenciasOperandosModificar.remove(modIndex);
                    listaDependenciasOperandosBorrar.add(listaDependenciasOperandos.get(index));
                } else if (!listaDependenciasOperandosCrear.isEmpty() && listaDependenciasOperandosCrear.contains(listaDependenciasOperandos.get(index))) {
                    int crearIndex = listaDependenciasOperandosCrear.indexOf(listaDependenciasOperandos.get(index));
                    listaDependenciasOperandosCrear.remove(crearIndex);
                } else {
                    listaDependenciasOperandosBorrar.add(listaDependenciasOperandos.get(index));
                }
                listaDependenciasOperandos.remove(index);
            }

            if (tipoLista == 1) {
                if (!listaDependenciasOperandosModificar.isEmpty() && listaDependenciasOperandosModificar.contains(filtradosListaDependenciasOperandos.get(index))) {
                    int modIndex = listaDependenciasOperandosModificar.indexOf(filtradosListaDependenciasOperandos.get(index));
                    listaDependenciasOperandosModificar.remove(modIndex);
                    listaDependenciasOperandosBorrar.add(filtradosListaDependenciasOperandos.get(index));
                } else if (!listaDependenciasOperandosCrear.isEmpty() && listaDependenciasOperandosCrear.contains(filtradosListaDependenciasOperandos.get(index))) {
                    int crearIndex = listaDependenciasOperandosCrear.indexOf(filtradosListaDependenciasOperandos.get(index));
                    listaDependenciasOperandosCrear.remove(crearIndex);
                } else {
                    listaDependenciasOperandosBorrar.add(filtradosListaDependenciasOperandos.get(index));
                }
                int CIndex = listaDependenciasOperandos.indexOf(filtradosListaDependenciasOperandos.get(index));
                listaDependenciasOperandos.remove(CIndex);
                filtradosListaDependenciasOperandos.remove(index);
                System.out.println("Realizado");
            }

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosDependenciasOperandos");
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
            dependenciasOperandosCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDependenciasOperandos:dependenciasOperandosCodigo");
            dependenciasOperandosCodigo.setFilterStyle("display: none; visibility: hidden;");
            dependenciasOperandosNombre = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDependenciasOperandos:dependenciasOperandosNombre");
            dependenciasOperandosNombre.setFilterStyle("display: none; visibility: hidden;");
            dependenciasOperandosConsecutivo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDependenciasOperandos:dependenciasOperandosConsecutivo");
            dependenciasOperandosConsecutivo.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosDependenciasOperandos");
            bandera = 0;
            filtradosListaDependenciasOperandos = null;
            tipoLista = 0;
        }
        listaDependenciasOperandosBorrar.clear();
        listaDependenciasOperandosCrear.clear();
        listaDependenciasOperandosModificar.clear();
        lovListaOperandos.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listaDependenciasOperandos = null;
        guardado = true;
        permitirIndex = true;

    }

    public void confirmarDuplicar() {
        int pasa = 0;
        mensajeValidacion = new String();
        RequestContext context = RequestContext.getCurrentInstance();

        if (duplicarDependenciaOperando.getCodigo() == 0) {
            mensajeValidacion = mensajeValidacion + " * Codigo\n";
            pasa++;
        }

        if (duplicarDependenciaOperando.getDescripcion().equals(" ") || duplicarDependenciaOperando.getDescripcion() == null) {
            mensajeValidacion = mensajeValidacion + " * Nombre\n";
            pasa++;
        }

        if (pasa != 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevoDependenciaOperando");
            PrimefacesContextUI.ejecutar("PF('validacionNuevoDependenciaOperando').show()");
        }

        if (pasa == 0) {
            if (bandera == 1) {
                altoTabla = "245";
                System.out.println("Desactivar");
                System.out.println("TipoLista= " + tipoLista);
                dependenciasOperandosCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDependenciasOperandos:dependenciasOperandosCodigo");
                dependenciasOperandosCodigo.setFilterStyle("display: none; visibility: hidden;");
                dependenciasOperandosNombre = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDependenciasOperandos:dependenciasOperandosNombre");
                dependenciasOperandosNombre.setFilterStyle("display: none; visibility: hidden;");
                dependenciasOperandosConsecutivo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDependenciasOperandos:dependenciasOperandosConsecutivo");
                dependenciasOperandosConsecutivo.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosDependenciasOperandos");
                bandera = 0;
                filtradosListaDependenciasOperandos = null;
                tipoLista = 0;
            }
            cambiosPagina = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            duplicarDependenciaOperando.setOperando(operando);
            listaDependenciasOperandos.add(duplicarDependenciaOperando);
            listaDependenciasOperandosCrear.add(duplicarDependenciaOperando);

            index = -1;
            if (guardado == true) {
                guardado = false;
                //RequestContext.getCurrentInstance().update("form:aceptar");
            }
            RequestContext.getCurrentInstance().update("form:datosDependenciasOperandos");
            duplicarDependenciaOperando = new DependenciasOperandos();
            RequestContext.getCurrentInstance().update("formularioDialogos:DuplicarDependenciaOperando");
            PrimefacesContextUI.ejecutar("PF('DuplicarDependenciaOperando').hide()");
        }
    }

    //Getter & Setter
    public List<DependenciasOperandos> getListaDependenciasOperandos() {
        if (listaDependenciasOperandos == null) {
            System.out.println("secOperando" + secOperando);
            listaDependenciasOperandos = administrarDependenciasOperandos.buscarDependenciasOperandos(secOperando);
            getNombre();
            for (int i = 0; i < listaDependenciasOperandos.size(); i++) {
                nombre = administrarDependenciasOperandos.nombreOperandos(listaDependenciasOperandos.get(i).getCodigo());
                listaDependenciasOperandos.get(i).setDescripcion(nombre);
                System.out.println("Nombre: " + nombre);
            }

        }
        return listaDependenciasOperandos;
    }

    public void setListaDependenciasOperandos(List<DependenciasOperandos> listaDependenciasOperandos) {
        this.listaDependenciasOperandos = listaDependenciasOperandos;
    }

    public List<DependenciasOperandos> getFiltradosListaDependenciasOperandos() {
        return filtradosListaDependenciasOperandos;
    }

    public void setFiltradosListaDependenciasOperandos(List<DependenciasOperandos> filtradosListaDependenciasOperandos) {
        this.filtradosListaDependenciasOperandos = filtradosListaDependenciasOperandos;
    }

    public DependenciasOperandos getEditarDependenciasOperandos() {
        return editarDependenciasOperandos;
    }

    public void setEditarDependenciasOperandos(DependenciasOperandos editarDependenciasOperandos) {
        this.editarDependenciasOperandos = editarDependenciasOperandos;
    }

    public String getNombre() {

        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public DependenciasOperandos getNuevoDependenciaOperando() {
        return nuevoDependenciaOperando;
    }

    public void setNuevoDependenciaOperando(DependenciasOperandos nuevoDependenciaOperando) {
        this.nuevoDependenciaOperando = nuevoDependenciaOperando;
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

    public DependenciasOperandos getDuplicarDependenciaOperando() {
        return duplicarDependenciaOperando;
    }

    public void setDuplicarDependenciaOperando(DependenciasOperandos duplicarDependenciaOperando) {
        this.duplicarDependenciaOperando = duplicarDependenciaOperando;
    }

    public List<Operandos> getLovListaOperandos() {
        if (lovListaOperandos == null) {
            lovListaOperandos = administrarDependenciasOperandos.buscarOperandos();
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
