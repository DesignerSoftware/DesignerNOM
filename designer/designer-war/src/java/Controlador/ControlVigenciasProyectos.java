/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Cargos;
import Entidades.Empleados;
import Entidades.Proyectos;
import Entidades.PryRoles;
import Entidades.VigenciasProyectos;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarProyectosInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import InterfaceAdministrar.AdministrarVigenciasProyectosInterface;
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

@ManagedBean
@SessionScoped
public class ControlVigenciasProyectos implements Serializable {

    @EJB
    AdministrarVigenciasProyectosInterface administrarVigenciasProyectos;
    @EJB
    AdministrarProyectosInterface administrarProyectos;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    //SECUENCIA DE LA PERSONA
    private BigInteger secuenciaEmpleado;
    private Empleados empleado;
    //LISTA VIGENCIAS FORMALES
    private List<VigenciasProyectos> listaVigenciasProyectos;
    private List<VigenciasProyectos> filtradosListaVigenciasProyectos;
    private VigenciasProyectos vigenciaProyectoSeleccionado;
    //Columnas Tabla Vigencias Proyectos
    private Column vPFechasIniciales, vPFechasFinales, vPProyectos, vPPryRoles, vPCargos, vPCantidadPersonas;
    //L.O.V Proyectos
    private List<Proyectos> listaProyectos;
    private List<Proyectos> filtradoslistaProyectos;
    private Proyectos seleccionProyectos;
    //L.O.V PRYROLES
    private List<PryRoles> listaPryRoles;
    private List<PryRoles> filtradoslistaPryRoles;
    private PryRoles seleccionPryRoles;
    //L.O.V CARGOS
    private List<Cargos> listaCargos;
    private List<Cargos> filtradoslistaCargos;
    private Cargos seleccionCargos;
    //OTROS
    private boolean aceptar;
    private int index;
    private int tipoActualizacion; //Activo/Desactivo Crtl + F11
    private int bandera;
    private boolean permitirIndex;
    //editar celda
    private VigenciasProyectos editarVigenciasProyectos;
    private boolean cambioEditor, aceptarEditar;
    private int cualCelda, tipoLista;
    //Crear Vigencias Proyectos
    private List<VigenciasProyectos> listaVigenciasProyectosCrear;
    public VigenciasProyectos nuevaVigenciaProyectos;
    private int k;
    private BigInteger l;
    private String mensajeValidacion;
    //Modificar Vigencias Proyectos
    private List<VigenciasProyectos> listaVigenciasProyectosModificar;
    private boolean guardado, guardarOk;
    //Borrar Vigencias Proyectos
    private List<VigenciasProyectos> listaVigenciasProyectosBorrar;
    //RASTRO
    private BigInteger secRegistro;
    //AUTOCOMPLETAR
    private String FechaInicial, Proyecto, PryRol, Cargo;
    //Duplicar
    private VigenciasProyectos duplicarVigenciaProyectos;
    //Consultas sobre detalles
    private Proyectos proyectoParametro;
    private String clienteParametroProyecto;
    private String plataformaParametroProyecto;
    private String altoTabla;

    public ControlVigenciasProyectos() {
        permitirIndex = true;
        //secuenciaEmpleado = BigInteger.valueOf(10661474);
        aceptar = true;
        listaVigenciasProyectosBorrar = new ArrayList<VigenciasProyectos>();
        listaVigenciasProyectosCrear = new ArrayList<VigenciasProyectos>();
        listaVigenciasProyectosModificar = new ArrayList<VigenciasProyectos>();
        guardado = true;
        listaProyectos = new ArrayList<Proyectos>();
        listaPryRoles = new ArrayList<PryRoles>();
        listaCargos = new ArrayList<Cargos>();
        secRegistro = null;
        //Crear VC
        nuevaVigenciaProyectos = new VigenciasProyectos();
        nuevaVigenciaProyectos.setProyecto(new Proyectos());
        nuevaVigenciaProyectos.setPryRol(new PryRoles());
        nuevaVigenciaProyectos.setPryCargoproyecto(new Cargos());
        proyectoParametro = new Proyectos();
        index = 0;
        altoTabla = "115";
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarVigenciasProyectos.obtenerConexion(ses.getId());
            administrarProyectos.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct ControlVigenciasCargos: " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void recibirEmpleado(BigInteger secEmpleado) {
        secuenciaEmpleado = secEmpleado;
        empleado = null;
        getEmpleado();
        listaVigenciasProyectos = null;
        getListaVigenciasProyectos();
        listaProyectos = null;
        getListaProyectos();
        listaPryRoles = null;
        getListaPryRoles();
        listaCargos = null;
        getListaCargos();
        aceptar = true;
    }

    //AUTOCOMPLETAR
    public void modificarVigenciasProyectos(int indice, String confirmarCambio, String valorConfirmar) {
        index = indice;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("N")) {
            if (tipoLista == 0) {
                if (!listaVigenciasProyectosCrear.contains(listaVigenciasProyectos.get(indice))) {

                    if (listaVigenciasProyectosModificar.isEmpty()) {
                        listaVigenciasProyectosModificar.add(listaVigenciasProyectos.get(indice));
                    } else if (!listaVigenciasProyectosModificar.contains(listaVigenciasProyectos.get(indice))) {
                        listaVigenciasProyectosModificar.add(listaVigenciasProyectos.get(indice));
                    }
                    if (guardado == true) {
                        guardado = false;
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");

                    }
                }
                index = -1;
                secRegistro = null;

            } else {
                if (!listaVigenciasProyectosCrear.contains(filtradosListaVigenciasProyectos.get(indice))) {

                    if (listaVigenciasProyectosModificar.isEmpty()) {
                        listaVigenciasProyectosModificar.add(filtradosListaVigenciasProyectos.get(indice));
                    } else if (!listaVigenciasProyectosModificar.contains(filtradosListaVigenciasProyectos.get(indice))) {
                        listaVigenciasProyectosModificar.add(filtradosListaVigenciasProyectos.get(indice));
                    }
                    if (guardado == true) {
                        guardado = false;
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");

                    }
                }
                index = -1;
                secRegistro = null;
            }
            context.update("form:datosVigenciasProyectosPersona");
        } else if (confirmarCambio.equalsIgnoreCase("PROYECTO")) {
            if (tipoLista == 0) {
                listaVigenciasProyectos.get(indice).getProyecto().setNombreproyecto(Proyecto);
            } else {
                filtradosListaVigenciasProyectos.get(indice).getProyecto().setNombreproyecto(Proyecto);
            }

            for (int i = 0; i < listaProyectos.size(); i++) {
                if (listaProyectos.get(i).getNombreproyecto().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoLista == 0) {
                    listaVigenciasProyectos.get(indice).setProyecto(listaProyectos.get(indiceUnicoElemento));
                } else {
                    filtradosListaVigenciasProyectos.get(indice).setProyecto(listaProyectos.get(indiceUnicoElemento));
                }
                listaProyectos.clear();
                getListaProyectos();
            } else {
                permitirIndex = false;
                context.update("formularioDialogos:proyectosDialogo");
                context.execute("proyectosDialogo.show()");
                tipoActualizacion = 0;
            }
        } else if (confirmarCambio.equalsIgnoreCase("PRYROL")) {
            if (tipoLista == 0) {
                listaVigenciasProyectos.get(indice).getPryRol().setDescripcion(PryRol);
            } else {
                filtradosListaVigenciasProyectos.get(indice).getPryRol().setDescripcion(PryRol);
            }
            for (int i = 0; i < listaPryRoles.size(); i++) {
                if (listaPryRoles.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoLista == 0) {
                    listaVigenciasProyectos.get(indice).setPryRol(listaPryRoles.get(indiceUnicoElemento));
                } else {
                    filtradosListaVigenciasProyectos.get(indice).setPryRol(listaPryRoles.get(indiceUnicoElemento));
                }
                listaPryRoles.clear();
                getListaPryRoles();
            } else {
                permitirIndex = false;
                context.update("formularioDialogos:pryRolesDialogo");
                context.execute("pryRolesDialogo.show()");
                tipoActualizacion = 0;
            }
        } else if (confirmarCambio.equalsIgnoreCase("CARGO")) {
            if (tipoLista == 0) {
                listaVigenciasProyectos.get(indice).getPryCargoproyecto().setNombre(Cargo);
            } else {
                filtradosListaVigenciasProyectos.get(indice).getPryCargoproyecto().setNombre(Cargo);
            }
            for (int i = 0; i < listaPryRoles.size(); i++) {
                if (listaPryRoles.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoLista == 0) {
                    listaVigenciasProyectos.get(indice).setPryCargoproyecto(listaCargos.get(indiceUnicoElemento));
                } else {
                    filtradosListaVigenciasProyectos.get(indice).setPryCargoproyecto(listaCargos.get(indiceUnicoElemento));
                }
                listaCargos.clear();
                getListaCargos();
            } else {
                permitirIndex = false;
                context.update("formularioDialogos:cargosDialogo");
                context.execute("cargosDialogo.show()");
                tipoActualizacion = 0;
            }
        }
        if (coincidencias == 1) {
            if (tipoLista == 0) {
                if (!listaVigenciasProyectosCrear.contains(listaVigenciasProyectos.get(indice))) {
                    if (listaVigenciasProyectosModificar.isEmpty()) {
                        listaVigenciasProyectosModificar.add(listaVigenciasProyectos.get(indice));
                    } else if (!listaVigenciasProyectosModificar.contains(listaVigenciasProyectos.get(indice))) {
                        listaVigenciasProyectosModificar.add(listaVigenciasProyectos.get(indice));
                    }
                    if (guardado == true) {
                        guardado = false;
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");

                    }
                }
                index = -1;
                secRegistro = null;
            } else {
                if (!listaVigenciasProyectosCrear.contains(filtradosListaVigenciasProyectos.get(indice))) {

                    if (listaVigenciasProyectosModificar.isEmpty()) {
                        listaVigenciasProyectosModificar.add(filtradosListaVigenciasProyectos.get(indice));
                    } else if (!listaVigenciasProyectosModificar.contains(filtradosListaVigenciasProyectos.get(indice))) {
                        listaVigenciasProyectosModificar.add(filtradosListaVigenciasProyectos.get(indice));
                    }
                    if (guardado == true) {
                        guardado = false;
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");

                    }
                }
                index = -1;
                secRegistro = null;
            }
        }
        context.update("form:datosVigenciasProyectosPersona");
    }

//Ubicacion Celda.
    public void cambiarIndice(int indice, int celda) {
        if (permitirIndex == true) {

            index = indice;
            cualCelda = celda;
            //tablaImprimir = ":formExportar:datosVigenciasFormalesExportar";
            //nombreArchivo = "VigenciasFormalesXML";
            //RequestContext context = RequestContext.getCurrentInstance();
            //context.update("form:exportarXML");
            getProyectoParametro();
            if (proyectoParametro != null) {
                plataformaParametroProyecto = null;
                if (proyectoParametro.getPryPlataforma() != null) {
                    plataformaParametroProyecto = proyectoParametro.getPryPlataforma().getDescripcion() + " - " + proyectoParametro.getPryPlataforma().getObservacion();
                }
                RequestContext context = RequestContext.getCurrentInstance();
                context.update("formularioDetalles:plataforma");
            }
            if (proyectoParametro != null) {
                getClienteParametroProyecto();
            }

            if (tipoLista == 0) {
                secRegistro = listaVigenciasProyectos.get(index).getSecuencia();
                if (cualCelda == 2) {
                    Proyecto = listaVigenciasProyectos.get(index).getProyecto().getNombreproyecto();
                } else if (cualCelda == 3) {
                    PryRol = listaVigenciasProyectos.get(index).getPryRol().getDescripcion();
                } else if (cualCelda == 4) {
                    Cargo = listaVigenciasProyectos.get(index).getPryCargoproyecto().getNombre();
                }
            } else {
                secRegistro = filtradosListaVigenciasProyectos.get(index).getSecuencia();
                if (cualCelda == 2) {
                    Proyecto = filtradosListaVigenciasProyectos.get(index).getProyecto().getNombreproyecto();
                } else if (cualCelda == 3) {
                    PryRol = filtradosListaVigenciasProyectos.get(index).getPryRol().getDescripcion();
                } else if (cualCelda == 4) {
                    Cargo = filtradosListaVigenciasProyectos.get(index).getPryCargoproyecto().getNombre();
                }
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
            context.update("form:proyectosDialogo");
            context.execute("proyectosDialogo.show()");
        } else if (dlg == 1) {
            context.update("form:pryRolesDialogo");
            context.execute("pryRolesDialogo.show()");
        } else if (dlg == 2) {
            context.update("form:cargosDialogo");
            context.execute("cargosDialogo.show()");
        }

    }

    public void activarCtrlF11() {

        FacesContext c = FacesContext.getCurrentInstance();

        System.out.println("TipoLista= " + tipoLista);
        if (bandera == 0) {
            System.out.println("Activar");
            System.out.println("TipoLista= " + tipoLista);
            vPFechasIniciales = (Column) c.getViewRoot().findComponent("form:datosVigenciasProyectosPersona:vPFechasIniciales");
            vPFechasIniciales.setFilterStyle("width: 85%;");
            vPFechasFinales = (Column) c.getViewRoot().findComponent("form:datosVigenciasProyectosPersona:vPFechasFinales");
            vPFechasFinales.setFilterStyle("");
            vPProyectos = (Column) c.getViewRoot().findComponent("form:datosVigenciasProyectosPersona:vPProyectos");
            vPProyectos.setFilterStyle("width: 85%;");
            vPPryRoles = (Column) c.getViewRoot().findComponent("form:datosVigenciasProyectosPersona:vPPryRoles");
            vPPryRoles.setFilterStyle("width: 85%;");
            vPCargos = (Column) c.getViewRoot().findComponent("form:datosVigenciasProyectosPersona:vPCargos");
            vPCargos.setFilterStyle("width: 85%;");
            vPCantidadPersonas = (Column) c.getViewRoot().findComponent("form:datosVigenciasProyectosPersona:vPCantidadPersonas");
            vPCantidadPersonas.setFilterStyle("width: 85%;");
            altoTabla = "95";
            RequestContext.getCurrentInstance().update("form:datosVigenciasProyectosPersona");
            bandera = 1;
            tipoLista = 1;
        } else if (bandera == 1) {
            System.out.println("Desactivar");
            System.out.println("TipoLista= " + tipoLista);
            vPFechasIniciales = (Column) c.getViewRoot().findComponent("form:datosVigenciasProyectosPersona:vPFechasIniciales");
            vPFechasIniciales.setFilterStyle("display: none; visibility: hidden;");
            vPFechasFinales = (Column) c.getViewRoot().findComponent("form:datosVigenciasProyectosPersona:vPFechasFinales");
            vPFechasFinales.setFilterStyle("display: none; visibility: hidden;");
            vPProyectos = (Column) c.getViewRoot().findComponent("form:datosVigenciasProyectosPersona:vPProyectos");
            vPProyectos.setFilterStyle("display: none; visibility: hidden;");
            vPPryRoles = (Column) c.getViewRoot().findComponent("form:datosVigenciasProyectosPersona:vPPryRoles");
            vPPryRoles.setFilterStyle("display: none; visibility: hidden;");
            vPCargos = (Column) c.getViewRoot().findComponent("form:datosVigenciasProyectosPersona:vPCargos");
            vPCargos.setFilterStyle("display: none; visibility: hidden;");
            vPCantidadPersonas = (Column) c.getViewRoot().findComponent("form:datosVigenciasProyectosPersona:vPCantidadPersonas");
            vPCantidadPersonas.setFilterStyle("display: none; visibility: hidden;");
            altoTabla = "115";

            RequestContext.getCurrentInstance().update("form:datosVigenciasProyectosPersona");
            bandera = 0;
            filtradosListaVigenciasProyectos = null;
            tipoLista = 0;
        }
    }

    //LISTA DE VALORES DINAMICA
    public void listaValoresBoton() {
        if (index >= 0) {
            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCelda == 2) {
                context.update("formularioDialogos:proyectosDialogo");
                context.execute("proyectosDialogo.show()");
                tipoActualizacion = 0;
            } else if (cualCelda == 3) {
                context.update("formularioDialogos:pryRolesDialogo");
                context.execute("pryRolesDialogo.show()");
                tipoActualizacion = 0;
            } else if (cualCelda == 4) {
                context.update("formularioDialogos:cargosDialogo");
                context.execute("cargosDialogo.show()");
                tipoActualizacion = 0;
            }
        }
    }

    //EXPORTAR
    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosVigenciasProyectosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "VigenciasProyectosPDF", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosVigenciasProyectosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "VigenciasProyectosXLS", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    //LIMPIAR NUEVO REGISTRO VIGENCIA PROYECTO
    public void limpiarNuevaVigenciaProyecto() {
        nuevaVigenciaProyectos = new VigenciasProyectos();
        nuevaVigenciaProyectos.setProyecto(new Proyectos());
        nuevaVigenciaProyectos.setPryRol(new PryRoles());
        nuevaVigenciaProyectos.setPryCargoproyecto(new Cargos());
        index = -1;
        secRegistro = null;
    }

    public void valoresBackupAutocompletar(int tipoNuevo, String Campo) {
        if (Campo.equals("PROYECTO")) {
            if (tipoNuevo == 1) {
                Proyecto = nuevaVigenciaProyectos.getProyecto().getNombreproyecto();
            } else if (tipoNuevo == 2) {
                Proyecto = duplicarVigenciaProyectos.getProyecto().getNombreproyecto();
            }
        } else if (Campo.equals("PRYROL")) {
            if (tipoNuevo == 1) {
                PryRol = nuevaVigenciaProyectos.getPryRol().getDescripcion();
            } else if (tipoNuevo == 2) {
                PryRol = duplicarVigenciaProyectos.getPryRol().getDescripcion();
            }
        } else if (Campo.equals("CARGO")) {
            if (tipoNuevo == 1) {
                Cargo = nuevaVigenciaProyectos.getPryCargoproyecto().getNombre();
            } else if (tipoNuevo == 2) {
                Cargo = duplicarVigenciaProyectos.getPryCargoproyecto().getNombre();
            }
        }
    }

    //CREAR VIGENCIA PROYECTO
    public void agregarNuevaVigenciaProyecto() {
        int pasa = 0;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();

        if (nuevaVigenciaProyectos.getFechainicial() == null) {
            System.out.println("Entro a Fecha");
            mensajeValidacion = " * Fecha \n";
            pasa++;
        }
        if (nuevaVigenciaProyectos.getProyecto().getNombreproyecto().equals(" ")) {
            System.out.println("Entro a Proyecto");
            mensajeValidacion = mensajeValidacion + " * Proyecto\n";
            pasa++;
        }
        if (nuevaVigenciaProyectos.getPryRol().getDescripcion().equals(" ")) {
            System.out.println("Entro a Rol");
            mensajeValidacion = mensajeValidacion + " * Rol\n";
            pasa++;
        }
        if (nuevaVigenciaProyectos.getPryCargoproyecto().getNombre().equals(" ")) {
            System.out.println("Entro a Cargo");
            mensajeValidacion = mensajeValidacion + " * Cargo\n";
            pasa++;
        }
        if (pasa == 0) {
            if (bandera == 1) {
                System.out.println("Desactivar");
                System.out.println("TipoLista= " + tipoLista);
                FacesContext c = FacesContext.getCurrentInstance();

                vPFechasIniciales = (Column) c.getViewRoot().findComponent("form:datosVigenciasProyectosPersona:vPFechasIniciales");
                vPFechasIniciales.setFilterStyle("display: none; visibility: hidden;");
                vPFechasFinales = (Column) c.getViewRoot().findComponent("form:datosVigenciasProyectosPersona:vPFechasFinales");
                vPFechasFinales.setFilterStyle("display: none; visibility: hidden;");
                vPProyectos = (Column) c.getViewRoot().findComponent("form:datosVigenciasProyectosPersona:vPProyectos");
                vPProyectos.setFilterStyle("display: none; visibility: hidden;");
                vPPryRoles = (Column) c.getViewRoot().findComponent("form:datosVigenciasProyectosPersona:vPPryRoles");
                vPPryRoles.setFilterStyle("display: none; visibility: hidden;");
                vPCargos = (Column) c.getViewRoot().findComponent("form:datosVigenciasProyectosPersona:vPCargos");
                vPCargos.setFilterStyle("display: none; visibility: hidden;");
                vPCantidadPersonas = (Column) c.getViewRoot().findComponent("form:datosVigenciasProyectosPersona:vPCantidadPersonas");
                vPCantidadPersonas.setFilterStyle("display: none; visibility: hidden;");
                altoTabla = "115";
                RequestContext.getCurrentInstance().update("form:datosVigenciasProyectosPersona");
                bandera = 0;
                filtradosListaVigenciasProyectos = null;
                tipoLista = 0;

            }
            //AGREGAR REGISTRO A LA LISTA VIGENCIAS PROYECTOS.
            k++;
            l = BigInteger.valueOf(k);
            nuevaVigenciaProyectos.setSecuencia(l);
            nuevaVigenciaProyectos.setEmpleado(empleado);
            if (nuevaVigenciaProyectos.getProyecto().getSecuencia() == null) {
                nuevaVigenciaProyectos.setProyecto(null);
            }
            listaVigenciasProyectosCrear.add(nuevaVigenciaProyectos);

            listaVigenciasProyectos.add(nuevaVigenciaProyectos);
            nuevaVigenciaProyectos = new VigenciasProyectos();
            nuevaVigenciaProyectos.setPryRol(new PryRoles());
            nuevaVigenciaProyectos.setProyecto(new Proyectos());
            nuevaVigenciaProyectos.setPryCargoproyecto(new Cargos());
            context.update("form:datosVigenciasProyectosPersona");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            context.execute("NuevoRegistroVigenciaProyecto.hide()");
            index = -1;
            secRegistro = null;
        } else {
            context.update("formularioDialogos:validacionNuevaVigenciaProyecto");
            context.execute("validacionNuevaVigenciaProyecto.show()");
        }
    }

    //DUPLICAR VIGENCIA PROYECTO
    public void duplicarVP() {
        if (index >= 0) {
            duplicarVigenciaProyectos = new VigenciasProyectos();
            k++;
            l = BigInteger.valueOf(k);

            if (tipoLista == 0) {
                duplicarVigenciaProyectos.setSecuencia(l);
                duplicarVigenciaProyectos.setEmpleado(listaVigenciasProyectos.get(index).getEmpleado());
                duplicarVigenciaProyectos.setFechainicial(listaVigenciasProyectos.get(index).getFechainicial());
                duplicarVigenciaProyectos.setFechafinal(listaVigenciasProyectos.get(index).getFechafinal());
                duplicarVigenciaProyectos.setProyecto(listaVigenciasProyectos.get(index).getProyecto());
                duplicarVigenciaProyectos.setPryRol(listaVigenciasProyectos.get(index).getPryRol());
                duplicarVigenciaProyectos.setPryCargoproyecto(listaVigenciasProyectos.get(index).getPryCargoproyecto());
                duplicarVigenciaProyectos.setCantidadpersonaacargo(listaVigenciasProyectos.get(index).getCantidadpersonaacargo());
            }
            if (tipoLista == 1) {
                duplicarVigenciaProyectos.setSecuencia(l);
                duplicarVigenciaProyectos.setEmpleado(filtradosListaVigenciasProyectos.get(index).getEmpleado());
                duplicarVigenciaProyectos.setFechainicial(filtradosListaVigenciasProyectos.get(index).getFechainicial());
                duplicarVigenciaProyectos.setFechafinal(filtradosListaVigenciasProyectos.get(index).getFechafinal());
                duplicarVigenciaProyectos.setProyecto(filtradosListaVigenciasProyectos.get(index).getProyecto());
                duplicarVigenciaProyectos.setPryRol(filtradosListaVigenciasProyectos.get(index).getPryRol());
                duplicarVigenciaProyectos.setPryCargoproyecto(filtradosListaVigenciasProyectos.get(index).getPryCargoproyecto());
                duplicarVigenciaProyectos.setCantidadpersonaacargo(filtradosListaVigenciasProyectos.get(index).getCantidadpersonaacargo());
            }

            RequestContext context = RequestContext.getCurrentInstance();
            context.update("formularioDialogos:duplicarVigenciaProyecto");
            context.execute("DuplicarRegistroVigenciaProyecto.show()");
            index = -1;
            secRegistro = null;
        }
    }

    public void confirmarDuplicar() {

        listaVigenciasProyectos.add(duplicarVigenciaProyectos);
        listaVigenciasProyectosCrear.add(duplicarVigenciaProyectos);
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form:datosVigenciasProyectosPersona");
        index = -1;
        if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
        if (bandera == 1) {
            //CERRAR FILTRADO
            FacesContext c = FacesContext.getCurrentInstance();
            vPFechasIniciales = (Column) c.getViewRoot().findComponent("form:datosVigenciasProyectosPersona:vPFechasIniciales");
            vPFechasIniciales.setFilterStyle("display: none; visibility: hidden;");
            vPFechasFinales = (Column) c.getViewRoot().findComponent("form:datosVigenciasProyectosPersona:vPFechasFinales");
            vPFechasFinales.setFilterStyle("display: none; visibility: hidden;");
            vPProyectos = (Column) c.getViewRoot().findComponent("form:datosVigenciasProyectosPersona:vPProyectos");
            vPProyectos.setFilterStyle("display: none; visibility: hidden;");
            vPPryRoles = (Column) c.getViewRoot().findComponent("form:datosVigenciasProyectosPersona:vPPryRoles");
            vPPryRoles.setFilterStyle("display: none; visibility: hidden;");
            vPCargos = (Column) c.getViewRoot().findComponent("form:datosVigenciasProyectosPersona:vPCargos");
            vPCargos.setFilterStyle("display: none; visibility: hidden;");
            vPCantidadPersonas = (Column) c.getViewRoot().findComponent("form:datosVigenciasProyectosPersona:vPCantidadPersonas");
            vPCantidadPersonas.setFilterStyle("display: none; visibility: hidden;");
            altoTabla = "115";
            RequestContext.getCurrentInstance().update("form:datosVigenciasProyectosPersona");
            bandera = 0;
            filtradosListaVigenciasProyectos = null;
            tipoLista = 0;
        }
        duplicarVigenciaProyectos = new VigenciasProyectos();
        context.update("formularioDialogos:DuplicarRegistroVigenciaProyecto");
        context.execute("DuplicarRegistroVigenciaProyecto.hide()");
    }
    //LIMPIAR DUPLICAR

    /**
     * Metodo que limpia los datos de un duplicar Vigencia Proyecto
     */
    public void limpiarduplicarVigenciaProyectos() {
        duplicarVigenciaProyectos = new VigenciasProyectos();
        duplicarVigenciaProyectos.setProyecto(new Proyectos());
        duplicarVigenciaProyectos.setPryCargoproyecto(new Cargos());
        duplicarVigenciaProyectos.setPryRol(new PryRoles());
    }

    //BORRAR VIGENCIA PROYECTO
    public void borrarVigenciasProyectos() {

        if (index >= 0) {
            if (tipoLista == 0) {
                if (!listaVigenciasProyectosModificar.isEmpty() && listaVigenciasProyectosModificar.contains(listaVigenciasProyectos.get(index))) {
                    int modIndex = listaVigenciasProyectosModificar.indexOf(listaVigenciasProyectos.get(index));
                    listaVigenciasProyectosModificar.remove(modIndex);
                    listaVigenciasProyectosBorrar.add(listaVigenciasProyectos.get(index));
                } else if (!listaVigenciasProyectosCrear.isEmpty() && listaVigenciasProyectosCrear.contains(listaVigenciasProyectos.get(index))) {
                    int crearIndex = listaVigenciasProyectosCrear.indexOf(listaVigenciasProyectos.get(index));
                    listaVigenciasProyectosCrear.remove(crearIndex);
                } else {
                    listaVigenciasProyectosBorrar.add(listaVigenciasProyectos.get(index));
                }
                listaVigenciasProyectos.remove(index);
            }

            if (tipoLista == 1) {
                if (!listaVigenciasProyectosModificar.isEmpty() && listaVigenciasProyectosModificar.contains(filtradosListaVigenciasProyectos.get(index))) {
                    int modIndex = listaVigenciasProyectosModificar.indexOf(filtradosListaVigenciasProyectos.get(index));
                    listaVigenciasProyectosModificar.remove(modIndex);
                    listaVigenciasProyectosBorrar.add(filtradosListaVigenciasProyectos.get(index));
                } else if (!listaVigenciasProyectosCrear.isEmpty() && listaVigenciasProyectosCrear.contains(filtradosListaVigenciasProyectos.get(index))) {
                    int crearIndex = listaVigenciasProyectosCrear.indexOf(filtradosListaVigenciasProyectos.get(index));
                    listaVigenciasProyectosCrear.remove(crearIndex);
                } else {
                    listaVigenciasProyectosBorrar.add(filtradosListaVigenciasProyectos.get(index));
                }
                int CIndex = listaVigenciasProyectos.indexOf(filtradosListaVigenciasProyectos.get(index));
                listaVigenciasProyectos.remove(CIndex);
                filtradosListaVigenciasProyectos.remove(index);
                System.out.println("Realizado");
            }

            RequestContext context = RequestContext.getCurrentInstance();
            context.update("form:datosVigenciasProyectosPersona");
            index = -1;
            secRegistro = null;

            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        }
    }

    //CANCELAR MODIFICACIONES
    public void cancelarModificacion() {
        if (bandera == 1) {
            System.out.println("Desactivar");
            System.out.println("TipoLista= " + tipoLista);
            FacesContext c = FacesContext.getCurrentInstance();

            vPFechasIniciales = (Column) c.getViewRoot().findComponent("form:datosVigenciasProyectosPersona:vPFechasIniciales");
            vPFechasIniciales.setFilterStyle("display: none; visibility: hidden;");
            vPFechasFinales = (Column) c.getViewRoot().findComponent("form:datosVigenciasProyectosPersona:vPFechasFinales");
            vPFechasFinales.setFilterStyle("display: none; visibility: hidden;");
            vPProyectos = (Column) c.getViewRoot().findComponent("form:datosVigenciasProyectosPersona:vPProyectos");
            vPProyectos.setFilterStyle("display: none; visibility: hidden;");
            vPPryRoles = (Column) c.getViewRoot().findComponent("form:datosVigenciasProyectosPersona:vPPryRoles");
            vPPryRoles.setFilterStyle("display: none; visibility: hidden;");
            vPCargos = (Column) c.getViewRoot().findComponent("form:datosVigenciasProyectosPersona:vPCargos");
            vPCargos.setFilterStyle("display: none; visibility: hidden;");
            vPCantidadPersonas = (Column) c.getViewRoot().findComponent("form:datosVigenciasProyectosPersona:vPCantidadPersonas");
            vPCantidadPersonas.setFilterStyle("display: none; visibility: hidden;");
            altoTabla = "115";
            RequestContext.getCurrentInstance().update("form:datosVigenciasProyectosPersona");
            bandera = 0;
            filtradosListaVigenciasProyectos = null;
            tipoLista = 0;
        }

        listaVigenciasProyectosBorrar.clear();
        listaVigenciasProyectosCrear.clear();
        listaVigenciasProyectosModificar.clear();
        index = -1;
        secRegistro = null;
//        k = 0;
        listaVigenciasProyectos = null;
        guardado = true;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form:datosVigenciasProyectosPersona");
    }

    public void salir() {

        if (bandera == 1) {
            System.out.println("Desactivar");
            System.out.println("TipoLista= " + tipoLista);
            FacesContext c = FacesContext.getCurrentInstance();

            vPFechasIniciales = (Column) c.getViewRoot().findComponent("form:datosVigenciasProyectosPersona:vPFechasIniciales");
            vPFechasIniciales.setFilterStyle("display: none; visibility: hidden;");
            vPFechasFinales = (Column) c.getViewRoot().findComponent("form:datosVigenciasProyectosPersona:vPFechasFinales");
            vPFechasFinales.setFilterStyle("display: none; visibility: hidden;");
            vPProyectos = (Column) c.getViewRoot().findComponent("form:datosVigenciasProyectosPersona:vPProyectos");
            vPProyectos.setFilterStyle("display: none; visibility: hidden;");
            vPPryRoles = (Column) c.getViewRoot().findComponent("form:datosVigenciasProyectosPersona:vPPryRoles");
            vPPryRoles.setFilterStyle("display: none; visibility: hidden;");
            vPCargos = (Column) c.getViewRoot().findComponent("form:datosVigenciasProyectosPersona:vPCargos");
            vPCargos.setFilterStyle("display: none; visibility: hidden;");
            vPCantidadPersonas = (Column) c.getViewRoot().findComponent("form:datosVigenciasProyectosPersona:vPCantidadPersonas");
            vPCantidadPersonas.setFilterStyle("display: none; visibility: hidden;");
            altoTabla = "115";
            RequestContext.getCurrentInstance().update("form:datosVigenciasProyectosPersona");
            bandera = 0;
            filtradosListaVigenciasProyectos = null;
            tipoLista = 0;
        }

        listaVigenciasProyectosBorrar.clear();
        listaVigenciasProyectosCrear.clear();
        listaVigenciasProyectosModificar.clear();
        index = -1;
        secRegistro = null;
        //  k = 0;
        listaVigenciasProyectos = null;
        guardado = true;
        permitirIndex = true;

    }

    //GUARDAR
    public void guardarCambiosVigenciasProyectos() {
        if (guardado == false) {
            System.out.println("Realizando Operaciones Vigencias Proyectos");
            if (!listaVigenciasProyectosBorrar.isEmpty()) {
                for (int i = 0; i < listaVigenciasProyectosBorrar.size(); i++) {
                    System.out.println("Borrando...");
                    if (listaVigenciasProyectosBorrar.get(i).getProyecto().getSecuencia() == null) {
                        listaVigenciasProyectosBorrar.get(i).setProyecto(null);
                        administrarVigenciasProyectos.borrarVigenciaProyecto(listaVigenciasProyectosBorrar.get(i));
                    } else {
                        if (listaVigenciasProyectosBorrar.get(i).getPryRol().getSecuencia() == null) {
                            listaVigenciasProyectosBorrar.get(i).setPryRol(null);
                            administrarVigenciasProyectos.borrarVigenciaProyecto(listaVigenciasProyectosBorrar.get(i));
                        } else {
                            administrarVigenciasProyectos.borrarVigenciaProyecto(listaVigenciasProyectosBorrar.get(i));
                        }

                    }

                }
                System.out.println("Entra");
                listaVigenciasProyectosBorrar.clear();
            }

            if (!listaVigenciasProyectosCrear.isEmpty()) {
                for (int i = 0; i < listaVigenciasProyectosCrear.size(); i++) {
                    System.out.println("Creando...");
                    if (listaVigenciasProyectosCrear.get(i).getProyecto().getSecuencia() == null) {
                        listaVigenciasProyectosCrear.get(i).setProyecto(null);
                        administrarVigenciasProyectos.crearVigenciaProyecto(listaVigenciasProyectosCrear.get(i));
                    } else {
                        if (listaVigenciasProyectosCrear.get(i).getPryRol().getSecuencia() == null) {
                            listaVigenciasProyectosCrear.get(i).setPryRol(null);
                            administrarVigenciasProyectos.crearVigenciaProyecto(listaVigenciasProyectosCrear.get(i));
                        } else {
                            administrarVigenciasProyectos.crearVigenciaProyecto(listaVigenciasProyectosCrear.get(i));

                        }

                    }
                }
                System.out.println("LimpiaLista");
                listaVigenciasProyectosCrear.clear();
            }
            if (!listaVigenciasProyectosModificar.isEmpty()) {
                administrarVigenciasProyectos.modificarVigenciaProyecto(listaVigenciasProyectosModificar);
                listaVigenciasProyectosModificar.clear();
            }
            System.out.println("Se guardaron los datos con exito");
            listaVigenciasProyectos = null;
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("form:datosVigenciasProyectosPersona");
            guardado = true;
            permitirIndex = true;
            FacesMessage msg = new FacesMessage("Información", "Se gurdarón los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            context.update("form:growl");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            //  k = 0;
        }
        System.out.println("Tamaño lista: " + listaVigenciasProyectosCrear.size());
        System.out.println("Valor k: " + k);
        index = -1;
        secRegistro = null;
    }

    public void autocompletarNuevoyDuplicado(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("PROYECTO")) {
            if (tipoNuevo == 1) {
                nuevaVigenciaProyectos.getProyecto().setNombreproyecto(Proyecto);
            } else if (tipoNuevo == 2) {
                duplicarVigenciaProyectos.getProyecto().setNombreproyecto(Proyecto);
            }
            for (int i = 0; i < listaProyectos.size(); i++) {
                if (listaProyectos.get(i).getNombreproyecto().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevaVigenciaProyectos.setProyecto(listaProyectos.get(indiceUnicoElemento));
                    context.update("formularioDialogos:nuevoProyecto");
                } else if (tipoNuevo == 2) {
                    duplicarVigenciaProyectos.setProyecto(listaProyectos.get(indiceUnicoElemento));
                    context.update("formularioDialogos:duplicarProyecto");
                }
                listaProyectos.clear();
                getListaProyectos();
            } else {
                context.update("form:proyectosDialogo");
                context.execute("proyectosDialogo.show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    context.update("formularioDialogos:nuevoProyecto");
                } else if (tipoNuevo == 2) {
                    context.update("formularioDialogos:duplicarProyecto");
                }
            }
        } else if (confirmarCambio.equalsIgnoreCase("PRYROL")) {
            if (tipoNuevo == 1) {
                nuevaVigenciaProyectos.getPryRol().setDescripcion(PryRol);
            } else if (tipoNuevo == 2) {
                duplicarVigenciaProyectos.getPryRol().setDescripcion(PryRol);
            }

            for (int i = 0; i < listaPryRoles.size(); i++) {
                if (listaPryRoles.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevaVigenciaProyectos.setPryRol(listaPryRoles.get(indiceUnicoElemento));
                    context.update("formularioDialogos:nuevoPryRol");
                } else if (tipoNuevo == 2) {
                    duplicarVigenciaProyectos.setPryRol(listaPryRoles.get(indiceUnicoElemento));
                    context.update("formularioDialogos:duplicarPryRol");
                }
                listaPryRoles.clear();
                getListaPryRoles();
            } else {
                context.update("form:pryRolesDialogo");
                context.execute("pryRolesDialogo.show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    context.update("formularioDialogos:nuevoPryRol");
                } else if (tipoNuevo == 2) {
                    context.update("formularioDialogos:duplicarPryRol");
                }
            }
        } else if (confirmarCambio.equalsIgnoreCase("CARGO")) {
            if (tipoNuevo == 1) {
                nuevaVigenciaProyectos.getPryCargoproyecto().setNombre(Cargo);
            } else if (tipoNuevo == 2) {
                duplicarVigenciaProyectos.getPryCargoproyecto().setNombre(Cargo);
            }

            for (int i = 0; i < listaCargos.size(); i++) {
                if (listaCargos.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevaVigenciaProyectos.setPryCargoproyecto(listaCargos.get(indiceUnicoElemento));
                    context.update("formularioDialogos:nuevoCargo");
                } else if (tipoNuevo == 2) {
                    duplicarVigenciaProyectos.setPryCargoproyecto(listaCargos.get(indiceUnicoElemento));
                    context.update("formularioDialogos:duplicarCargo");
                }
                listaCargos.clear();
                getListaCargos();
            } else {
                context.update("form:cargosDialogo");
                context.execute("cargosDialogo.show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    context.update("formularioDialogos:nuevoCargo");
                } else if (tipoNuevo == 2) {
                    context.update("formularioDialogos:duplicarCargo");
                }
            }

        }
    }

    //MOSTRAR DATOS CELDA
    public void editarCelda() {
        if (index >= 0) {
            if (tipoLista == 0) {
                editarVigenciasProyectos = listaVigenciasProyectos.get(index);
            }
            if (tipoLista == 1) {
                editarVigenciasProyectos = filtradosListaVigenciasProyectos.get(index);
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
                context.update("formularioDialogos:editarProyectos");
                context.execute("editarProyectos.show()");
                cualCelda = -1;
            } else if (cualCelda == 3) {
                context.update("formularioDialogos:editarPryRoles");
                context.execute("editarPryRoles.show()");
                cualCelda = -1;
            } else if (cualCelda == 4) {
                context.update("formularioDialogos:editarCargos");
                context.execute("editarCargos.show()");
                cualCelda = -1;
            } else if (cualCelda == 5) {
                context.update("formularioDialogos:editarCantidadPersonas");
                context.execute("editarCantidadPersonas.show()");
                cualCelda = -1;
            }
        }
        index = -1;
        secRegistro = null;
    }

    public void actualizarProyectos() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                listaVigenciasProyectos.get(index).setProyecto(seleccionProyectos);
                if (!listaVigenciasProyectosCrear.contains(listaVigenciasProyectos.get(index))) {
                    if (listaVigenciasProyectosModificar.isEmpty()) {
                        listaVigenciasProyectosModificar.add(listaVigenciasProyectos.get(index));
                    } else if (!listaVigenciasProyectosModificar.contains(listaVigenciasProyectos.get(index))) {
                        listaVigenciasProyectosModificar.add(listaVigenciasProyectos.get(index));
                    }
                }
            } else {
                filtradosListaVigenciasProyectos.get(index).setProyecto(seleccionProyectos);
                if (!listaVigenciasProyectosCrear.contains(filtradosListaVigenciasProyectos.get(index))) {
                    if (listaVigenciasProyectosModificar.isEmpty()) {
                        listaVigenciasProyectosModificar.add(filtradosListaVigenciasProyectos.get(index));
                    } else if (!listaVigenciasProyectosModificar.contains(filtradosListaVigenciasProyectos.get(index))) {
                        listaVigenciasProyectosModificar.add(filtradosListaVigenciasProyectos.get(index));
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");

            }
            permitirIndex = true;
            context.update("form:datosVigenciasProyectosPersona");
        } else if (tipoActualizacion == 1) {
            nuevaVigenciaProyectos.setProyecto(seleccionProyectos);
            context.update("formularioDialogos:nuevaVigenciaProyecto");
        } else if (tipoActualizacion == 2) {
            duplicarVigenciaProyectos.setProyecto(seleccionProyectos);
            context.update("formularioDialogos:duplicarVigenciaProyecto");
        }
        filtradoslistaProyectos = null;
        seleccionProyectos = null;
        aceptar = true;
        index = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("formularioDialogos:LOVProyectos:globalFilter");
        context.execute("LOVProyectos.clearFilters()");
        context.execute("proyectosDialogo.hide()");
        //context.update("formularioDialogos:LOVProyectos");
    }

    public void actualizarPryRoles() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                listaVigenciasProyectos.get(index).setPryRol(seleccionPryRoles);
                if (!listaVigenciasProyectosCrear.contains(listaVigenciasProyectos.get(index))) {
                    if (listaVigenciasProyectosModificar.isEmpty()) {
                        listaVigenciasProyectosModificar.add(listaVigenciasProyectos.get(index));
                    } else if (!listaVigenciasProyectosModificar.contains(listaVigenciasProyectos.get(index))) {
                        listaVigenciasProyectosModificar.add(listaVigenciasProyectos.get(index));
                    }
                }
            } else {
                filtradosListaVigenciasProyectos.get(index).setPryRol(seleccionPryRoles);
                if (!listaVigenciasProyectosCrear.contains(filtradosListaVigenciasProyectos.get(index))) {
                    if (listaVigenciasProyectosModificar.isEmpty()) {
                        listaVigenciasProyectosModificar.add(filtradosListaVigenciasProyectos.get(index));
                    } else if (!listaVigenciasProyectosModificar.contains(filtradosListaVigenciasProyectos.get(index))) {
                        listaVigenciasProyectosModificar.add(filtradosListaVigenciasProyectos.get(index));
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");

            }
            permitirIndex = true;
            context.update("form:datosVigenciasProyectosPersona");
        } else if (tipoActualizacion == 1) {
            nuevaVigenciaProyectos.setPryRol(seleccionPryRoles);
            context.update("formularioDialogos:nuevaVigenciaProyecto");
        } else if (tipoActualizacion == 2) {
            duplicarVigenciaProyectos.setPryRol(seleccionPryRoles);
            context.update("formularioDialogos:duplicarVigenciaProyecto");

        }
        filtradoslistaPryRoles = null;
        seleccionPryRoles = null;
        aceptar = true;
        index = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("formularioDialogos:LOVPryRoles:globalFilter");
        context.execute("LOVPryRoles.clearFilters()");
        context.execute("pryRolesDialogo.hide()");
        //context.update("formularioDialogos:LOVPryRoles");
    }

    public void actualizarCargos() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                listaVigenciasProyectos.get(index).setPryCargoproyecto(seleccionCargos);
                if (!listaVigenciasProyectosCrear.contains(listaVigenciasProyectos.get(index))) {
                    if (listaVigenciasProyectosModificar.isEmpty()) {
                        listaVigenciasProyectosModificar.add(listaVigenciasProyectos.get(index));
                    } else if (!listaVigenciasProyectosModificar.contains(listaVigenciasProyectos.get(index))) {
                        listaVigenciasProyectosModificar.add(listaVigenciasProyectos.get(index));
                    }
                }
            } else {
                filtradosListaVigenciasProyectos.get(index).setPryCargoproyecto(seleccionCargos);
                if (!listaVigenciasProyectosCrear.contains(filtradosListaVigenciasProyectos.get(index))) {
                    if (listaVigenciasProyectosModificar.isEmpty()) {
                        listaVigenciasProyectosModificar.add(filtradosListaVigenciasProyectos.get(index));
                    } else if (!listaVigenciasProyectosModificar.contains(filtradosListaVigenciasProyectos.get(index))) {
                        listaVigenciasProyectosModificar.add(filtradosListaVigenciasProyectos.get(index));
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");

            }
            permitirIndex = true;
            context.update("form:datosVigenciasProyectosPersona");
        } else if (tipoActualizacion == 1) {
            nuevaVigenciaProyectos.setPryCargoproyecto(seleccionCargos);
            context.update("formularioDialogos:nuevaVigenciaProyecto");
        } else if (tipoActualizacion == 2) {
            duplicarVigenciaProyectos.setPryCargoproyecto(seleccionCargos);
            context.update("formularioDialogos:duplicarVigenciaProyecto");

        }
        filtradoslistaPryRoles = null;
        seleccionPryRoles = null;
        aceptar = true;
        index = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("formularioDialogos:LOVCargos:globalFilter");
        context.execute("LOVCargos.clearFilters()");
        context.execute("cargosDialogo.hide()");
        //context.update("formularioDialogos:LOVCargos");
    }

    public void cancelarCambioProyectos() {
        filtradoslistaProyectos = null;
        seleccionProyectos = null;
        aceptar = true;
        index = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:LOVProyectos:globalFilter");
        context.execute("LOVProyectos.clearFilters()");
        context.execute("proyectosDialogo.hide()");
    }

    public void cancelarCambioPryRoles() {
        filtradoslistaPryRoles = null;
        seleccionPryRoles = null;
        aceptar = true;
        index = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:LOVPryRoles:globalFilter");
        context.execute("LOVPryRoles.clearFilters()");
        context.execute("pryRolesDialogo.hide()");
    }

    public void cancelarCambioCargo() {
        filtradoslistaCargos = null;
        seleccionCargos = null;
        aceptar = true;
        index = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:LOVCargos:globalFilter");
        context.execute("LOVCargos.clearFilters()");
        context.execute("cargosDialogo.hide()");
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        System.out.println("lol");
        if (!listaVigenciasProyectos.isEmpty()) {
            if (secRegistro != null) {
                System.out.println("lol 2");
                int resultado = administrarRastros.obtenerTabla(secRegistro, "VIGENCIASPROYECTOS");
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
            if (administrarRastros.verificarHistoricosTabla("VIGENCIASPROYECTOS")) {
                context.execute("confirmarRastroHistorico.show()");
            } else {
                context.execute("errorRastroHistorico.show()");
            }

        }
        index = -1;
    }

    //EVENTO FILTRAR
    public void eventoFiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    //Getter And Setter
    public BigInteger getSecuenciaEmpleado() {
        return secuenciaEmpleado;
    }

    public void setSecuenciaEmpleado(BigInteger secuenciaEmpleado) {
        this.secuenciaEmpleado = secuenciaEmpleado;
    }

    public Empleados getEmpleado() {
        if (empleado == null) {
            empleado = administrarVigenciasProyectos.encontrarEmpleado(secuenciaEmpleado);
        }

        return empleado;
    }

    public void setEmpleado(Empleados empleado) {
        this.empleado = empleado;
    }

    public List<VigenciasProyectos> getListaVigenciasProyectos() {
        if (listaVigenciasProyectos == null) {
            listaVigenciasProyectos = administrarVigenciasProyectos.vigenciasProyectosEmpleado(secuenciaEmpleado);
        }
        return listaVigenciasProyectos;
    }

    public void setListaVigenciasProyectos(List<VigenciasProyectos> listaVigenciasProyectos) {
        this.listaVigenciasProyectos = listaVigenciasProyectos;
    }

    public List<VigenciasProyectos> getFiltradosListaVigenciasProyectos() {
        return filtradosListaVigenciasProyectos;
    }

    public void setFiltradosListaVigenciasProyectos(List<VigenciasProyectos> filtradosListaVigenciasProyectos) {
        this.filtradosListaVigenciasProyectos = filtradosListaVigenciasProyectos;
    }

    public List<Proyectos> getListaProyectos() {
        if (listaProyectos == null) {
            listaProyectos = administrarVigenciasProyectos.lovProyectos();
        }
        return listaProyectos;
    }

    public void setListaProyectos(List<Proyectos> listaProyectos) {
        this.listaProyectos = listaProyectos;
    }

    public List<PryRoles> getListaPryRoles() {
        if (listaPryRoles == null) {
            listaPryRoles = administrarVigenciasProyectos.lovPryRoles();
        }

        return listaPryRoles;
    }

    public void setListaPryRoles(List<PryRoles> listaPryRoles) {
        this.listaPryRoles = listaPryRoles;
    }

    public List<Cargos> getListaCargos() {
        if (listaCargos == null) {
            listaCargos = administrarVigenciasProyectos.lovCargos();
        }
        return listaCargos;
    }

    public void setListaCargos(List<Cargos> listaCargos) {
        this.listaCargos = listaCargos;
    }

    public List<Proyectos> getFiltradoslistaProyectos() {
        return filtradoslistaProyectos;
    }

    public void setFiltradoslistaProyectos(List<Proyectos> filtradoslistaProyectos) {
        this.filtradoslistaProyectos = filtradoslistaProyectos;
    }

    public List<PryRoles> getFiltradoslistaPryRoles() {
        return filtradoslistaPryRoles;
    }

    public void setFiltradoslistaPryRoles(List<PryRoles> filtradoslistaPryRoles) {
        this.filtradoslistaPryRoles = filtradoslistaPryRoles;
    }

    public List<Cargos> getFiltradoslistaCargos() {
        return filtradoslistaCargos;
    }

    public void setFiltradoslistaCargos(List<Cargos> filtradoslistaCargos) {
        this.filtradoslistaCargos = filtradoslistaCargos;
    }

    public Proyectos getSeleccionProyectos() {
        return seleccionProyectos;
    }

    public void setSeleccionProyectos(Proyectos seleccionProyectos) {
        this.seleccionProyectos = seleccionProyectos;
    }

    public PryRoles getSeleccionPryRoles() {
        return seleccionPryRoles;
    }

    public void setSeleccionPryRoles(PryRoles seleccionPryRoles) {
        this.seleccionPryRoles = seleccionPryRoles;
    }

    public Cargos getSeleccionCargos() {
        return seleccionCargos;
    }

    public void setSeleccionCargos(Cargos seleccionCargos) {
        this.seleccionCargos = seleccionCargos;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public void setAceptar(boolean aceptar) {
        this.aceptar = aceptar;
    }

    public VigenciasProyectos getNuevaVigenciaProyectos() {
        return nuevaVigenciaProyectos;
    }

    public void setNuevaVigenciaProyectos(VigenciasProyectos nuevaVigenciaProyectos) {
        this.nuevaVigenciaProyectos = nuevaVigenciaProyectos;
    }

    public String getMensajeValidacion() {
        return mensajeValidacion;
    }

    public void setMensajeValidacion(String mensajeValidacion) {
        this.mensajeValidacion = mensajeValidacion;
    }

    public VigenciasProyectos getDuplicarVigenciaProyectos() {
        return duplicarVigenciaProyectos;
    }

    public void setDuplicarVigenciaProyectos(VigenciasProyectos duplicarVigenciaProyectos) {
        this.duplicarVigenciaProyectos = duplicarVigenciaProyectos;
    }

    public VigenciasProyectos getEditarVigenciasProyectos() {
        return editarVigenciasProyectos;
    }

    public void setEditarVigenciasProyectos(VigenciasProyectos editarVigenciasProyectos) {
        this.editarVigenciasProyectos = editarVigenciasProyectos;
    }

    public Proyectos getProyectoParametro() {
        if (index >= 0) {
            if (!listaVigenciasProyectos.isEmpty()) {
                proyectoParametro = administrarVigenciasProyectos.buscarProyectoPorNombreVigencia(listaVigenciasProyectos.get(index).getProyecto().getNombreproyecto());
                /*RequestContext context = RequestContext.getCurrentInstance();
                 context.update("formularioDialogos:pryRolesDialogo");
                 context.execute("pryRolesDialogo.show()");*/
                RequestContext context = RequestContext.getCurrentInstance();
                context.update("formularioDetalles:nombreProyecto");
                context.update("formularioDetalles:tipoMoneda");
                context.update("formularioDetalles:cliente");
                context.update("formularioDetalles:plataforma");
                context.update("formularioDetalles:totalPersonas");
                context.update("formularioDetalles:detalleProyecto");
                context.update("formularioDetalles:fechaInicial");
                context.update("formularioDetalles:monto");
                context.update("formularioDetalles:fechaFinal");
                context.update("formularioDetalles:codigoProyecto");
            }
        }
        return proyectoParametro;
    }

    public void setProyectoParametro(Proyectos proyectoParametro) {
        this.proyectoParametro = proyectoParametro;
    }

    public String getClienteParametroProyecto() {
        if (proyectoParametro.getSecuencia() != null) {
            clienteParametroProyecto = proyectoParametro.getPryCliente().getNombre() + "/" + proyectoParametro.getPryCliente().getDireccion() + "/" + proyectoParametro.getPryCliente().getTelefono() + " - " + proyectoParametro.getPryCliente().getContacto();
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("formularioDetalles:cliente");
        }
        return clienteParametroProyecto;
    }

    public void setClienteParametroProyecto(String clienteParametroProyecto) {
        this.clienteParametroProyecto = clienteParametroProyecto;
    }

    public String getPlataformaParametroProyecto() {
        return plataformaParametroProyecto;
    }

    public void setPlataformaParametroProyecto(String plataformaParametroProyecto) {
        this.plataformaParametroProyecto = plataformaParametroProyecto;
    }

    public BigInteger getSecRegistro() {
        return secRegistro;
    }

    public void setSecRegistro(BigInteger secRegistro) {
        this.secRegistro = secRegistro;
    }

    public VigenciasProyectos getVigenciaProyectoSeleccionado() {
        return vigenciaProyectoSeleccionado;
    }

    public void setVigenciaProyectoSeleccionado(VigenciasProyectos vigenciaProyectoSeleccionado) {
        this.vigenciaProyectoSeleccionado = vigenciaProyectoSeleccionado;
    }

    public boolean isGuardado() {
        return guardado;
    }

    public void setGuardado(boolean guardado) {
        this.guardado = guardado;
    }

    public String getAltoTabla() {
        return altoTabla;
    }

    public void setAltoTabla(String altoTabla) {
        this.altoTabla = altoTabla;
    }

}
