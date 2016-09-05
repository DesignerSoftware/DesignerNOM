/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Ciudades;
import Entidades.Direcciones;
import Entidades.Empleados;
import Entidades.Personas;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarCiudadesInterface;
import InterfaceAdministrar.AdministrarDireccionesInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
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

@ManagedBean
@SessionScoped
public class ControlPerDirecciones implements Serializable {

    @EJB
    AdministrarDireccionesInterface administrarDirecciones;
    @EJB
    AdministrarCiudadesInterface administrarCiudades;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    //LISTA TELEFONOS
    private List<Direcciones> listaDirecciones;
    private List<Direcciones> filtradosListaDirecciones;
    private Direcciones direccionSeleccionada;
    //SECUENCIA DE LA PERSONA
    private BigInteger secuenciaPersona;
    private Personas persona;
    //L.O.V CIUDADES
    private List<Ciudades> listaCiudades;
    private List<Ciudades> filtradoslistaCiudades;
    private Ciudades seleccionCiudades;
    //AUTOCOMPLETAR
    private String Ciudad;
    //OTROS
    private boolean aceptar;
    private int tipoActualizacion; //Activo/Desactivo Crtl + F11
    private int bandera;
    private boolean permitirIndex;
    //Columnas Tabla Ciudades
    private Column dFecha, dUbicacionPrincipal, dDescripcionUbicacionPrincipal, dUbicacionSecundaria;
    private Column dDescripcionUbicacionSecundaria, dInterior, dCiudad, dTipoVivienda, dHipoteca, dDireccionAlternativa;
    //editar celda
    private Direcciones editarDireccion;
    private boolean cambioEditor, aceptarEditar;
    private int cualCelda, tipoLista;
    //Modificar Ciudades
    private List<Direcciones> listaDireccionesModificar;
    private boolean guardado, guardarOk;
    //Crear Direccion
    public Direcciones nuevaDireccion;
    private List<Direcciones> listaDireccionesCrear;
    private BigInteger l;
    private int k;
    private String mensajeValidacion;
    //Borrar Ciudad
    private List<Direcciones> listaDireccionesBorrar;
    //Duplicar
    private Direcciones duplicarDireccion;
    private String altoTabla;
    private Empleados empleado;
    private String infoRegistro, infoRegistroCiudades;
    private boolean activarLOV;
    private DataTable tablaC;

    public ControlPerDirecciones() {
        permitirIndex = true;
        //  secuenciaPersona = BigInteger.valueOf(10668967);
        aceptar = true;
        tipoLista = 0;
        nuevaDireccion = new Direcciones();
        nuevaDireccion.setCiudad(new Ciudades());
        nuevaDireccion.setFechavigencia(new Date());
        listaDireccionesBorrar = new ArrayList<Direcciones>();
        listaDireccionesCrear = new ArrayList<Direcciones>();
        listaDireccionesModificar = new ArrayList<Direcciones>();
        //INICIALIZAR LOVS
        listaCiudades = new ArrayList<Ciudades>();
        direccionSeleccionada = null;
        listaDirecciones = null;
        k = 0;
        nuevaDireccion.setTipoppal("C");
        nuevaDireccion.setTiposecundario("K");
        nuevaDireccion.setTipovivienda("CASA");
        nuevaDireccion.setHipoteca("N");
        altoTabla = "270";
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarDirecciones.obtenerConexion(ses.getId());
            administrarCiudades.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void recibirEmpleado(BigInteger secuencia) {
        listaDirecciones = null;
        empleado = administrarDirecciones.empleadoActual(secuencia);
        getListaDirecciones();
        deshabilitarBotonLOV();
        if (listaDirecciones == null || listaDirecciones.isEmpty()) {
            direccionSeleccionada = null;
        } else {
            direccionSeleccionada = listaDirecciones.get(0);
        }
        contarRegistros();
    }

    public void cambiarIndice(Direcciones direccion, int celda) {
        if (permitirIndex == true) {
            direccionSeleccionada = direccion;
            cualCelda = celda;
            if (tipoLista == 0) {
                direccionSeleccionada.getSecuencia();
                deshabilitarBotonLOV();
                if (cualCelda == 6) {
                    modificarInfoRegistroCiudades(listaCiudades.size());
                    Ciudad = direccionSeleccionada.getCiudad().getNombre();
                    habilitarBotonLOV();
                }
            } else {
                direccionSeleccionada.getSecuencia();
                deshabilitarBotonLOV();
                if (cualCelda == 6) {
                    modificarInfoRegistroCiudades(listaCiudades.size());
                    Ciudad = direccionSeleccionada.getCiudad().getNombre();
                    habilitarBotonLOV();
                }
            }
        }
    }

    public void pregunta(int tipoNuevo) {
        if (tipoNuevo == 1) {
            if (nuevaDireccion.getTipoppal() != null && nuevaDireccion.getPpal() != null && nuevaDireccion.getSecundario() != null && nuevaDireccion.getTiposecundario() != null) {
                RequestContext context = RequestContext.getCurrentInstance();
                context.update("formularioDialogos:pregunta");
                context.execute("pregunta.show()");
            }
        } else if (tipoNuevo == 2) {
            if (duplicarDireccion.getTipoppal() != null && duplicarDireccion.getPpal() != null && duplicarDireccion.getSecundario() != null && duplicarDireccion.getTiposecundario() != null) {
                RequestContext context = RequestContext.getCurrentInstance();
                context.update("formularioDialogos:pregunta");
                context.execute("pregunta.show()");
            }
        }
    }

    public void copiarDireccion(int tipoNuevo) {
        if (tipoNuevo == 1) {
            nuevaDireccion.setDireccionalternativa(nuevaDireccion.getEstadoTipoPpal() + (" ") + nuevaDireccion.getPpal() + (" ") + nuevaDireccion.getEstadoTipoSecundario() + (" ") + nuevaDireccion.getSecundario());
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("formularioDialogos:nuevaDireccion");
        } else {
            duplicarDireccion.setDireccionalternativa(nuevaDireccion.getTipoppal() + (" ") + duplicarDireccion.getPpal() + (" ") + duplicarDireccion.getTiposecundario() + (" ") + duplicarDireccion.getSecundario());
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("formularioDialogos:duplicarDireccion");
        }

    }

    //CREAR DIRECCION
    public void agregarNuevaDireccion() {
        int pasa = 0;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        FacesContext c = FacesContext.getCurrentInstance();
        if (nuevaDireccion.getFechavigencia() == null) {
            mensajeValidacion = " * Fecha Vigencia\n";
            pasa++;
        }
        if (nuevaDireccion.getTipoppal() == null) {
            mensajeValidacion = " * Ubicacion Principal\n";
            pasa++;
        }
        if (nuevaDireccion.getPpal() == null) {
            mensajeValidacion = " * Descripcion U. Principal\n";
            pasa++;
        }
        if (nuevaDireccion.getTiposecundario() == null) {
            mensajeValidacion = " * Ubicacion Secundaria\n";
            pasa++;
        }
        if (nuevaDireccion.getCiudad().getNombre().equals(" ")) {
            mensajeValidacion = " * Ciudad \n";
            pasa++;
        }

        if (nuevaDireccion.getInterior() == null) {
            mensajeValidacion = "* Barrio \n";
            pasa++;
        }

        if (pasa == 0) {
            if (bandera == 1) {
                dFecha = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dFecha");
                dFecha.setFilterStyle("display: none; visibility: hidden;");
                dUbicacionPrincipal = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dUbicacionPrincipal");
                dUbicacionPrincipal.setFilterStyle("display: none; visibility: hidden;");
                dDescripcionUbicacionPrincipal = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dDescripcionUbicacionPrincipal");
                dDescripcionUbicacionPrincipal.setFilterStyle("display: none; visibility: hidden;");
                dUbicacionSecundaria = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dUbicacionSecundaria");
                dUbicacionSecundaria.setFilterStyle("display: none; visibility: hidden;");
                dDescripcionUbicacionSecundaria = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dDescripcionUbicacionSecundaria");
                dDescripcionUbicacionSecundaria.setFilterStyle("display: none; visibility: hidden;");
                dInterior = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dInterior");
                dInterior.setFilterStyle("display: none; visibility: hidden;");
                dCiudad = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dCiudad");
                dCiudad.setFilterStyle("display: none; visibility: hidden;");
                dTipoVivienda = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dTipoVivienda");
                dTipoVivienda.setFilterStyle("display: none; visibility: hidden;");
                dHipoteca = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dHipoteca");
                dHipoteca.setFilterStyle("display: none; visibility: hidden;");
                dDireccionAlternativa = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dDireccionAlternativa");
                dDireccionAlternativa.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosDireccionesPersona");
                bandera = 0;
                filtradosListaDirecciones = null;
                tipoLista = 0;
                altoTabla = "270";
            }
            k++;
            l = BigInteger.valueOf(k);
            nuevaDireccion.setSecuencia(l);
            nuevaDireccion.setPersona(empleado.getPersona());
            listaDireccionesCrear.add(nuevaDireccion);
            if (listaDirecciones == null) {
                listaDirecciones = new ArrayList<Direcciones>();
            }
                listaDirecciones.add(nuevaDireccion);
            System.out.print("Lista direcciones");
            System.out.println(listaDirecciones.size());

            direccionSeleccionada = nuevaDireccion;
            getListaDirecciones();
            modificarInfoRegistro(listaDirecciones.size());
            deshabilitarBotonLOV();
            context.update("form:infoRegistro");
            context.update("form:datosDireccionesPersona");
            nuevaDireccion = new Direcciones();
            nuevaDireccion.setCiudad(new Ciudades());
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            context.execute("NuevoRegistroDireccion.hide()");
        } else {
            context.update("formularioDialogos:validacionNuevaDireccion");
            context.execute("validacionNuevaDireccion.show()");
        }
    }

    //AUTOCOMPLETAR
    public void modificarDirecciones(Direcciones direccion, String confirmarCambio, String valorConfirmar) {
        direccionSeleccionada = direccion;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("N")) {
            if (tipoLista == 0) {
                if (!listaDireccionesCrear.contains(direccionSeleccionada)) {

                    if (listaDireccionesModificar.isEmpty()) {
                        listaDireccionesModificar.add(direccionSeleccionada);
                    } else if (!listaDireccionesModificar.contains(direccionSeleccionada)) {
                        listaDireccionesModificar.add(direccionSeleccionada);
                    }
                    if (guardado == true) {
                        guardado = false;
                    }
                }

            } else {
                if (!listaDireccionesCrear.contains(direccionSeleccionada)) {

                    if (listaDireccionesModificar.isEmpty()) {
                        listaDireccionesModificar.add(direccionSeleccionada);
                    } else if (!listaDireccionesModificar.contains(direccionSeleccionada)) {
                        listaDireccionesModificar.add(direccionSeleccionada);
                    }
                    if (guardado == true) {
                        guardado = false;
                    }
                }
            }
            context.update("form:datosDireccionesPersona");
        } else if (confirmarCambio.equalsIgnoreCase("CIUDAD")) {
            if (tipoLista == 0) {
                direccionSeleccionada.getCiudad().setNombre(Ciudad);
            } else {
                direccionSeleccionada.getCiudad().setNombre(Ciudad);
            }

            for (int i = 0; i < listaCiudades.size(); i++) {
                if (listaCiudades.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoLista == 0) {
                    direccionSeleccionada.setCiudad(listaCiudades.get(indiceUnicoElemento));
                } else {
                    direccionSeleccionada.setCiudad(listaCiudades.get(indiceUnicoElemento));
                }
                listaCiudades.clear();
                getListaCiudades();
            } else {
                permitirIndex = false;
                context.update("formularioDialogos:ciudadesDialogo");
                context.execute("ciudadesDialogo.show()");
                tipoActualizacion = 0;
            }
        }
        if (coincidencias == 1) {
            if (tipoLista == 0) {
                if (!listaDireccionesCrear.contains(direccionSeleccionada)) {
                    if (listaDireccionesModificar.isEmpty()) {
                        listaDireccionesModificar.add(direccionSeleccionada);
                    } else if (!listaDireccionesModificar.contains(direccionSeleccionada)) {
                        listaDireccionesModificar.add(direccionSeleccionada);
                    }
                    if (guardado == true) {
                        guardado = false;
                    }
                }
            } else {
                if (!listaDireccionesCrear.contains(direccionSeleccionada)) {

                    if (listaDireccionesModificar.isEmpty()) {
                        listaDireccionesModificar.add(direccionSeleccionada);
                    } else if (!listaDireccionesModificar.contains(direccionSeleccionada)) {
                        listaDireccionesModificar.add(direccionSeleccionada);
                    }
                    if (guardado == true) {
                        guardado = false;
                    }
                }
            }
        }
        context.update("form:datosDireccionesPersona");
    }

    //METODOS L.O.V CIUDADES
    public void actualizarCiudades() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                direccionSeleccionada.setCiudad(seleccionCiudades);
                if (!listaDireccionesCrear.contains(direccionSeleccionada)) {
                    if (listaDireccionesModificar.isEmpty()) {
                        listaDireccionesModificar.add(direccionSeleccionada);
                    } else if (!listaDireccionesModificar.contains(direccionSeleccionada)) {
                        listaDireccionesModificar.add(direccionSeleccionada);
                    }
                }
            } else {
                direccionSeleccionada.setCiudad(seleccionCiudades);
                if (!listaDireccionesCrear.contains(direccionSeleccionada)) {
                    if (listaDireccionesModificar.isEmpty()) {
                        listaDireccionesModificar.add(direccionSeleccionada);
                    } else if (!listaDireccionesModificar.contains(direccionSeleccionada)) {
                        listaDireccionesModificar.add(direccionSeleccionada);
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            permitirIndex = true;
            context.update("form:datosDireccionesPersona");
        } else if (tipoActualizacion == 1) {
            nuevaDireccion.setCiudad(seleccionCiudades);
            context.update("formularioDialogos:nuevaDireccion");
        } else if (tipoActualizacion == 2) {
            System.out.println(seleccionCiudades.getNombre());
            duplicarDireccion.setCiudad(seleccionCiudades);
            context.update("formularioDialogos:duplicarDireccion");
        }
        filtradoslistaCiudades = null;
        seleccionCiudades = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("formularioDialogos:LOVCiudades:globalFilter");
        context.execute("LOVCiudades.clearFilters()");
        context.execute("ciudadesDialogo.hide()");
        //context.update("formularioDialogos:LOVCiudades");
    }

    public void activarCtrlF11() {
        System.out.println("TipoLista= " + tipoLista);
        FacesContext c = FacesContext.getCurrentInstance();

        if (bandera == 0) {

            System.out.println("Activar");
            System.out.println("TipoLista= " + tipoLista);
            dFecha = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dFecha");
            dFecha.setFilterStyle("width: 85%");
            dUbicacionPrincipal = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dUbicacionPrincipal");
            dUbicacionPrincipal.setFilterStyle("width: 85%");
            dDescripcionUbicacionPrincipal = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dDescripcionUbicacionPrincipal");
            dDescripcionUbicacionPrincipal.setFilterStyle("width: 85%");
            dUbicacionSecundaria = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dUbicacionSecundaria");
            dUbicacionSecundaria.setFilterStyle("width: 85%");
            dDescripcionUbicacionSecundaria = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dDescripcionUbicacionSecundaria");
            dDescripcionUbicacionSecundaria.setFilterStyle("width: 85%");
            dInterior = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dInterior");
            dInterior.setFilterStyle("width: 85%");
            dCiudad = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dCiudad");
            dCiudad.setFilterStyle("width: 85%");
            dTipoVivienda = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dTipoVivienda");
            dTipoVivienda.setFilterStyle("width: 85%");
            dHipoteca = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dHipoteca");
            dHipoteca.setFilterStyle("width: 85%");
            dDireccionAlternativa = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dDireccionAlternativa");
            dDireccionAlternativa.setFilterStyle("width: 85%");
            altoTabla = "250";
            RequestContext.getCurrentInstance().update("form:datosDireccionesPersona");
            bandera = 1;
            tipoLista = 1;
        } else if (bandera == 1) {
            System.out.println("Desactivar");
            System.out.println("TipoLista= " + tipoLista);
            dFecha = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dFecha");
            dFecha.setFilterStyle("display: none; visibility: hidden;");
            dUbicacionPrincipal = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dUbicacionPrincipal");
            dUbicacionPrincipal.setFilterStyle("display: none; visibility: hidden;");
            dDescripcionUbicacionPrincipal = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dDescripcionUbicacionPrincipal");
            dDescripcionUbicacionPrincipal.setFilterStyle("display: none; visibility: hidden;");
            dUbicacionSecundaria = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dUbicacionSecundaria");
            dUbicacionSecundaria.setFilterStyle("display: none; visibility: hidden;");
            dDescripcionUbicacionSecundaria = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dDescripcionUbicacionSecundaria");
            dDescripcionUbicacionSecundaria.setFilterStyle("display: none; visibility: hidden;");
            dInterior = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dInterior");
            dInterior.setFilterStyle("display: none; visibility: hidden;");
            dCiudad = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dCiudad");
            dCiudad.setFilterStyle("display: none; visibility: hidden;");
            dTipoVivienda = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dTipoVivienda");
            dTipoVivienda.setFilterStyle("display: none; visibility: hidden;");
            dHipoteca = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dHipoteca");
            dHipoteca.setFilterStyle("display: none; visibility: hidden;");
            dDireccionAlternativa = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dDireccionAlternativa");
            dDireccionAlternativa.setFilterStyle("display: none; visibility: hidden;");
            altoTabla = "270";
            RequestContext.getCurrentInstance().update("form:datosDireccionesPersona");
            bandera = 0;
            filtradosListaDirecciones = null;
            tipoLista = 0;
        }
    }

    //LIMPIAR NUEVO REGISTRO DIRECCION
    public void limpiarNuevaDireccion() {
        nuevaDireccion = new Direcciones();
        nuevaDireccion.setCiudad(new Ciudades());
        nuevaDireccion.getCiudad().setNombre(" ");
    }

    //LISTA DE VALORES DINAMICA
    public void listaValoresBoton() {
        if (direccionSeleccionada != null) {
            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCelda == 6) {
                habilitarBotonLOV();
                modificarInfoRegistroCiudades(listaCiudades.size());
                context.update("formularioDialogos:ciudadesDialogo");
                context.execute("ciudadesDialogo.show()");
                tipoActualizacion = 0;
            }
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void asignarIndex(Direcciones direccion) {
        direccionSeleccionada = direccion;
        RequestContext context = RequestContext.getCurrentInstance();
        modificarInfoRegistroCiudades(listaCiudades.size());
        context.update("formularioDialogos:ciudadesDialogo");
        context.execute("ciudadesDialogo.show()");
        tipoActualizacion = 0;
    }

    //MOSTRAR DATOS CELDA
    public void editarCelda() {
        if (direccionSeleccionada != null) {
            if (tipoLista == 0) {
                editarDireccion = direccionSeleccionada;
            }
            if (tipoLista == 1) {
                editarDireccion = direccionSeleccionada;
            }

            RequestContext context = RequestContext.getCurrentInstance();
            System.out.println("Entro a editar... valor celda: " + cualCelda);
            if (cualCelda == 0) {
                deshabilitarBotonLOV();
                context.update("formularioDialogos:editarFecha");
                context.execute("editarFecha.show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                deshabilitarBotonLOV();
                context.update("formularioDialogos:editarUbicacionesPrincipales");
                context.execute("editarUbicacionesPrincipales.show()");
                cualCelda = -1;
            } else if (cualCelda == 2) {
                deshabilitarBotonLOV();
                context.update("formularioDialogos:editarDescripcionesUbicacionesPrincipales");
                context.execute("editarDescripcionesUbicacionesPrincipales.show()");
                cualCelda = -1;
            } else if (cualCelda == 3) {
                deshabilitarBotonLOV();
                context.update("formularioDialogos:editarUbicacionesSecundarias");
                context.execute("editarUbicacionesSecundarias.show()");
                cualCelda = -1;
            } else if (cualCelda == 4) {
                deshabilitarBotonLOV();
                context.update("formularioDialogos:editarDescripcionesUbicacionesSecundarias");
                context.execute("editarDescripcionesUbicacionesSecundarias.show()");
                cualCelda = -1;
            } else if (cualCelda == 5) {
                deshabilitarBotonLOV();
                context.update("formularioDialogos:editarBarrios");
                context.execute("editarBarrios.show()");
                cualCelda = -1;
            } else if (cualCelda == 6) {
                habilitarBotonLOV();
                context.update("formularioDialogos:editarCiudades");
                context.execute("editarCiudades.show()");
                cualCelda = -1;
            } else if (cualCelda == 7) {
                deshabilitarBotonLOV();
                context.update("formularioDialogos:editarTiposViviendas");
                context.execute("editarTiposViviendas.show()");
                cualCelda = -1;
            } else if (cualCelda == 8) {
                deshabilitarBotonLOV();
                context.update("formularioDialogos:editarHipotecas");
                context.execute("editarHipotecas.show()");
                cualCelda = -1;
            } else if (cualCelda == 9) {
                deshabilitarBotonLOV();
                context.update("formularioDialogos:editarDireccionesAlternativas");
                context.execute("editarDireccionesAlternativas.show()");
                cualCelda = -1;
            }
        } else {
            RequestContext.getCurrentInstance().execute("seleccionarRegistro.show()");
        }
    }

    //DUPLICAR DIRECCION
    public void duplicarD() {
        if (direccionSeleccionada != null) {
            duplicarDireccion = new Direcciones();
            k++;
            l = BigInteger.valueOf(k);

            if (tipoLista == 0) {
                duplicarDireccion.setSecuencia(l);
                duplicarDireccion.setFechavigencia(direccionSeleccionada.getFechavigencia());
                duplicarDireccion.setTipoppal(direccionSeleccionada.getTipoppal());
                duplicarDireccion.setPpal(direccionSeleccionada.getPpal());
                duplicarDireccion.setTiposecundario(direccionSeleccionada.getTiposecundario());
                duplicarDireccion.setSecundario(direccionSeleccionada.getSecundario());
                duplicarDireccion.setInterior(direccionSeleccionada.getInterior());
                duplicarDireccion.setCiudad(direccionSeleccionada.getCiudad());
                duplicarDireccion.setHipoteca(direccionSeleccionada.getHipoteca());
                duplicarDireccion.setDireccionalternativa(direccionSeleccionada.getDireccionalternativa());
            }
            if (tipoLista == 1) {
                duplicarDireccion.setSecuencia(l);
                duplicarDireccion.setFechavigencia(direccionSeleccionada.getFechavigencia());
                duplicarDireccion.setTipoppal(direccionSeleccionada.getTipoppal());
                duplicarDireccion.setPpal(direccionSeleccionada.getPpal());
                duplicarDireccion.setTiposecundario(direccionSeleccionada.getTiposecundario());
                duplicarDireccion.setSecundario(direccionSeleccionada.getSecundario());
                duplicarDireccion.setInterior(direccionSeleccionada.getInterior());
                duplicarDireccion.setCiudad(direccionSeleccionada.getCiudad());
                duplicarDireccion.setTipovivienda(direccionSeleccionada.getTipovivienda());
                duplicarDireccion.setHipoteca(direccionSeleccionada.getHipoteca());
                duplicarDireccion.setDireccionalternativa(direccionSeleccionada.getDireccionalternativa());
                altoTabla = "270";
            }
            RequestContext context = RequestContext.getCurrentInstance();
            duplicarDireccion.setPersona(persona);
            listaDireccionesCrear.add(duplicarDireccion);
            listaDirecciones.add(duplicarDireccion);
            direccionSeleccionada = duplicarDireccion;

            context.update("formularioDialogos:duplicarDireccion");
            context.execute("DuplicarRegistroDireccion.show()");
        } else {
            RequestContext.getCurrentInstance().execute("seleccionarRegistro.show()");
        }
    }

    public void confirmarDuplicar(){
        int pasa = 0;
        int pasaA = 0;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
      FacesContext c = FacesContext.getCurrentInstance();
        if (duplicarDireccion.getFechavigencia() == null) {
            mensajeValidacion = " Campo fecha vacío \n";
            pasa++;

        }

        System.out.println("fecha direccion "+ duplicarDireccion.getFechavigencia() +"\n");
        System.out.println("secuencia direccion "+ duplicarDireccion.getSecuencia()+ "\n");
        for (int i = 0; i < listaDirecciones.size(); i++) {
            if ((listaDirecciones.get(i).getSecuencia().equals(duplicarDireccion.getSecuencia())) && ((listaDirecciones.get(i).getFechavigencia().equals(duplicarDireccion.getFechavigencia())))) {// && !(duplicarTelefono.getFechavigencia().before(listaTelefonos.get(i).getFechavigencia())))) {
                context.update("formularioDialogos:existeDireccion");
                context.execute("existeDireccion.show()");
                pasaA++;
            }
            if (pasa != 0) {
                context.update("formularioDialogos:validacionNuevaDireccion");
                context.execute("validacionNuevaDireccion.show()");
            }
        }
//
        if (pasa == 0 && pasaA == 0) {
            
            listaDirecciones.add(duplicarDireccion);
            listaDireccionesCrear.add(duplicarDireccion);
            direccionSeleccionada = duplicarDireccion;
            getListaDirecciones();
            modificarInfoRegistro(listaDirecciones.size());
            context.update("form:datosDireccionesPersona");
            context.update("form:infoRegistro");
            RequestContext.getCurrentInstance().update("form:datosDireccionesPersona");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            if (bandera == 1) {
            System.out.println("Desactivar");
            System.out.println("TipoLista= " + tipoLista);
            dFecha = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dFecha");
            dFecha.setFilterStyle("display: none; visibility: hidden;");
            dUbicacionPrincipal = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dUbicacionPrincipal");
            dUbicacionPrincipal.setFilterStyle("display: none; visibility: hidden;");
            dDescripcionUbicacionPrincipal = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dDescripcionUbicacionPrincipal");
            dDescripcionUbicacionPrincipal.setFilterStyle("display: none; visibility: hidden;");
            dUbicacionSecundaria = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dUbicacionSecundaria");
            dUbicacionSecundaria.setFilterStyle("display: none; visibility: hidden;");
            dDescripcionUbicacionSecundaria = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dDescripcionUbicacionSecundaria");
            dDescripcionUbicacionSecundaria.setFilterStyle("display: none; visibility: hidden;");
            dInterior = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dInterior");
            dInterior.setFilterStyle("display: none; visibility: hidden;");
            dCiudad = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dCiudad");
            dCiudad.setFilterStyle("display: none; visibility: hidden;");
            dTipoVivienda = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dTipoVivienda");
            dTipoVivienda.setFilterStyle("display: none; visibility: hidden;");
            dHipoteca = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dHipoteca");
            dHipoteca.setFilterStyle("display: none; visibility: hidden;");
            dDireccionAlternativa = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dDireccionAlternativa");
            dDireccionAlternativa.setFilterStyle("display: none; visibility: hidden;");
            altoTabla = "270";
            RequestContext.getCurrentInstance().update("form:datosDireccionesPersona");
            bandera = 0;
            filtradosListaDirecciones = null;
            tipoLista = 0;
            }
            duplicarDireccion = new Direcciones();
            RequestContext.getCurrentInstance().execute("DuplicarRegistroDireccion.hide()");
        }
    }
    
    //GUARDAR
    public void guardarCambiosDireccion() {
        if (guardado == false) {
            if (!listaDireccionesBorrar.isEmpty()) {
                administrarDirecciones.borrarDirecciones(listaDireccionesBorrar);
                listaDireccionesBorrar.clear();
            }
            if (!listaDireccionesCrear.isEmpty()) {
                administrarDirecciones.crearDirecciones(listaDireccionesCrear);
                listaDireccionesCrear.clear();
            }
            if (!listaDireccionesModificar.isEmpty()) {
                administrarDirecciones.modificarDirecciones(listaDireccionesModificar);
                listaDireccionesModificar.clear();
            }
            System.out.println("Se guardaron los datos con exito");
            listaDirecciones = null;
            getListaDirecciones();
            contarRegistros();
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("form:datosDireccionesPersona");
            guardado = true;
            permitirIndex = true;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            FacesMessage msg = new FacesMessage("Información", "Se gurdarón los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            context.update("form:growl");
            // k = 0;
        }
        direccionSeleccionada = null;
    }

    public void salir() {
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            dFecha = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dFecha");
            dFecha.setFilterStyle("display: none; visibility: hidden;");
            dUbicacionPrincipal = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dUbicacionPrincipal");
            dUbicacionPrincipal.setFilterStyle("display: none; visibility: hidden;");
            dDescripcionUbicacionPrincipal = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dDescripcionUbicacionPrincipal");
            dDescripcionUbicacionPrincipal.setFilterStyle("display: none; visibility: hidden;");
            dUbicacionSecundaria = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dUbicacionSecundaria");
            dUbicacionSecundaria.setFilterStyle("display: none; visibility: hidden;");
            dDescripcionUbicacionSecundaria = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dDescripcionUbicacionSecundaria");
            dDescripcionUbicacionSecundaria.setFilterStyle("display: none; visibility: hidden;");
            dInterior = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dInterior");
            dInterior.setFilterStyle("display: none; visibility: hidden;");
            dCiudad = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dCiudad");
            dCiudad.setFilterStyle("display: none; visibility: hidden;");
            dTipoVivienda = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dTipoVivienda");
            dTipoVivienda.setFilterStyle("display: none; visibility: hidden;");
            dHipoteca = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dHipoteca");
            dHipoteca.setFilterStyle("display: none; visibility: hidden;");
            dDireccionAlternativa = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dDireccionAlternativa");
            dDireccionAlternativa.setFilterStyle("display: none; visibility: hidden;");
            altoTabla = "270";
            RequestContext.getCurrentInstance().update("form:datosDireccionesPersona");
            bandera = 0;
            filtradosListaDirecciones = null;
            tipoLista = 0;
        }
        listaDireccionesBorrar.clear();
        listaDireccionesCrear.clear();
        listaDireccionesModificar.clear();
        direccionSeleccionada = null;
        //    k = 0;
        listaDirecciones = null;
        guardado = true;
        contarRegistros();
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        permitirIndex = true;
    }

    public void cancelarCambioCiudades() {
        filtradoslistaCiudades = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:LOVCiudades:globalFilter");
        context.execute("LOVCiudades.clearFilters()");
        context.execute("ciudadesDialogo.hide()");
        context.update("formularioDialogos:ciudadesDialogo");
        context.update("form:LOVCiudades");
        context.update("form:aceptarC");

    }

    public void cancelarModificacion() {
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            //CERRAR FILTRADO
            dFecha = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dFecha");
            dFecha.setFilterStyle("display: none; visibility: hidden;");
            dUbicacionPrincipal = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dUbicacionPrincipal");
            dUbicacionPrincipal.setFilterStyle("display: none; visibility: hidden;");
            dDescripcionUbicacionPrincipal = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dDescripcionUbicacionPrincipal");
            dDescripcionUbicacionPrincipal.setFilterStyle("display: none; visibility: hidden;");
            dUbicacionSecundaria = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dUbicacionSecundaria");
            dUbicacionSecundaria.setFilterStyle("display: none; visibility: hidden;");
            dDescripcionUbicacionSecundaria = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dDescripcionUbicacionSecundaria");
            dDescripcionUbicacionSecundaria.setFilterStyle("display: none; visibility: hidden;");
            dInterior = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dInterior");
            dInterior.setFilterStyle("display: none; visibility: hidden;");
            dCiudad = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dCiudad");
            dCiudad.setFilterStyle("display: none; visibility: hidden;");
            dTipoVivienda = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dTipoVivienda");
            dTipoVivienda.setFilterStyle("display: none; visibility: hidden;");
            dHipoteca = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dHipoteca");
            dHipoteca.setFilterStyle("display: none; visibility: hidden;");
            dDireccionAlternativa = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dDireccionAlternativa");
            dDireccionAlternativa.setFilterStyle("display: none; visibility: hidden;");
            altoTabla = "270";
            RequestContext.getCurrentInstance().update("form:datosDireccionesPersona");
            bandera = 0;
            filtradosListaDirecciones = null;
            tipoLista = 0;

        }

        listaDireccionesBorrar.clear();
        listaDireccionesCrear.clear();
        listaDireccionesModificar.clear();
        k = 0;
        listaDirecciones = null;
        direccionSeleccionada = null;
        contarRegistros();
        guardado = true;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form:ACEPTAR");
        context.update("form:datosDireccionesPersona");
    }

    public void limpiarDuplicarDireccion() {
        duplicarDireccion = new Direcciones();
    }

    public void valoresBackupAutocompletar(int tipoNuevo) {

        if (tipoNuevo == 1) {
            Ciudad = nuevaDireccion.getCiudad().getNombre();
        } else if (tipoNuevo == 2) {
            Ciudad = duplicarDireccion.getCiudad().getNombre();
        }
    }

    public void autocompletarNuevoyDuplicado(String valorConfirmar, int tipoNuevo) {
        int coincidencias = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoNuevo == 1) {
            nuevaDireccion.getCiudad().setNombre(Ciudad);
        } else if (tipoNuevo == 2) {
            duplicarDireccion.getCiudad().setNombre(Ciudad);
        }
        for (int i = 0; i < listaCiudades.size(); i++) {
            if (listaCiudades.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                //indiceUnicoElemento = i;
                coincidencias++;
            }
        }
        if (coincidencias == 1) {
            if (tipoNuevo == 1) {
                nuevaDireccion.setCiudad(seleccionCiudades);
                context.update("formularioDialogos:nuevaCiudad");
            } else if (tipoNuevo == 2) {
                duplicarDireccion.setCiudad(seleccionCiudades);
                context.update("formularioDialogos:duplicarCiudad");
            }
            listaCiudades.clear();
            getListaCiudades();
        } else {
            context.update("formularioDialogos:ciudadesDialogo");
            context.execute("ciudadesDialogo.show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
                context.update("formularioDialogos:nuevaCiudad");
            } else if (tipoNuevo == 2) {
                context.update("formularioDialogos:duplicarCiudad");
            }
        }
    }

    public void llamarLovCiudad(int tipoN) {
        if (tipoN == 1) {
            tipoActualizacion = 1;
        } else if (tipoN == 2) {
            tipoActualizacion = 2;
        }
        modificarInfoRegistroCiudades(listaCiudades.size());
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("formularioDialogos:ciudadesDialogo");
        context.execute("ciudadesDialogo.show()");
    }

    //BORRAR DIRECCIONES
    public void borrarDirecciones() {

        if (direccionSeleccionada != null) {
            if (!listaDireccionesModificar.isEmpty() && listaDireccionesModificar.contains(direccionSeleccionada)) {
                int modIndex = listaDireccionesModificar.indexOf(direccionSeleccionada);
                listaDireccionesModificar.remove(modIndex);
                listaDireccionesBorrar.add(direccionSeleccionada);
            } else if (!listaDireccionesCrear.isEmpty() && listaDireccionesCrear.contains(direccionSeleccionada)) {
                int crearIndex = listaDireccionesCrear.indexOf(direccionSeleccionada);
                listaDireccionesCrear.remove(crearIndex);
            } else {
                listaDireccionesBorrar.add(direccionSeleccionada);
            }
            listaDirecciones.remove(direccionSeleccionada);

            if (tipoLista == 1) {

                filtradosListaDirecciones.remove(direccionSeleccionada);

            }

            RequestContext context = RequestContext.getCurrentInstance();
            context.update("form:datosDireccionesPersona");
            direccionSeleccionada = null;

            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        } else {
            RequestContext.getCurrentInstance().execute("seleccionarRegistro.show()");
        }
    }

    //EXPORTAR
    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosDireccionesExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "DireccionesPDF", false, false, "UTF-8", null, null);
        context.responseComplete();
        direccionSeleccionada = null;
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosDireccionesExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "DireccionesXLS", false, false, "UTF-8", null, null);
        context.responseComplete();
        direccionSeleccionada = null;
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
            if (direccionSeleccionada != null) {
                int resultado = administrarRastros.obtenerTabla(direccionSeleccionada.getSecuencia(), "DIRECCIONES");
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
            }
         else {
            if (administrarRastros.verificarHistoricosTabla("DIRECCIONES")) {
                context.execute("confirmarRastroHistorico.show()");
            } else {
                context.execute("errorRastroHistorico.show()");
            }

        }
        direccionSeleccionada = null;
    }

    public void seleccionarTipoPpal(String estadoTipoPpal, Direcciones direccion, int celda) {
        if (tipoLista == 0) {
            if (estadoTipoPpal.equals("CALLE")) {
                direccionSeleccionada.setTipoppal("C");
            } else if (estadoTipoPpal.equals("CARRERA")) {
                direccionSeleccionada.setTipoppal("K");
            } else if (estadoTipoPpal.equals("AVENIDA CALLE")) {
                direccionSeleccionada.setTipoppal("A");
            } else if (estadoTipoPpal.equals("AVENIDA CARRERA")) {
                direccionSeleccionada.setTipoppal("M");
            } else if (estadoTipoPpal.equals("DIAGONAL")) {
                direccionSeleccionada.setTipoppal("D");
            } else if (estadoTipoPpal.equals("TRANSVERSAL")) {
                direccionSeleccionada.setTipoppal("T");
            } else if (estadoTipoPpal.equals("OTROS")) {
                direccionSeleccionada.setTipoppal("O");
            }

            if (!listaDireccionesCrear.contains(direccionSeleccionada)) {
                if (listaDireccionesModificar.isEmpty()) {
                    listaDireccionesModificar.add(direccionSeleccionada);
                } else if (!listaDireccionesModificar.contains(direccionSeleccionada)) {
                    listaDireccionesModificar.add(direccionSeleccionada);
                }
            }
        } else {
            if (estadoTipoPpal.equals("CALLE")) {
                direccionSeleccionada.setTipoppal("C");
            } else if (estadoTipoPpal.equals("CARRERA")) {
                direccionSeleccionada.setTipoppal("K");
            } else if (estadoTipoPpal.equals("AVENIDA CALLE")) {
                direccionSeleccionada.setTipoppal("A");
            } else if (estadoTipoPpal.equals("AVENIDA CARRERA")) {
                direccionSeleccionada.setTipoppal("M");
            } else if (estadoTipoPpal.equals("DIAGONAL")) {
                direccionSeleccionada.setTipoppal("D");
            } else if (estadoTipoPpal.equals("TRANSVERSAL")) {
                direccionSeleccionada.setTipoppal("T");
            } else if (estadoTipoPpal.equals("OTROS")) {
                direccionSeleccionada.setTipoppal("O");
            }

            if (!listaDireccionesCrear.contains(direccionSeleccionada)) {
                if (listaDireccionesModificar.isEmpty()) {
                    listaDireccionesModificar.add(direccionSeleccionada);
                } else if (!listaDireccionesModificar.contains(direccionSeleccionada)) {
                    listaDireccionesModificar.add(direccionSeleccionada);
                }
            }
        }
        if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
        RequestContext.getCurrentInstance().update("form:datosDireccionesPersona");
    }

    public void seleccionarTipoPpalNuevaDireccion(String estadoTipoPpal, int tipoNuevo) {
        if (tipoNuevo == 1) {
            if (estadoTipoPpal.equals("CALLE")) {
                nuevaDireccion.setTipoppal("C");
            } else if (estadoTipoPpal.equals("CARRERA")) {
                nuevaDireccion.setTipoppal("K");
            } else if (estadoTipoPpal.equals("AVENIDA CALLE")) {
                nuevaDireccion.setTipoppal("A");
            } else if (estadoTipoPpal.equals("AVENIDA CARRERA")) {
                nuevaDireccion.setTipoppal("M");
            } else if (estadoTipoPpal.equals("DIAGONAL")) {
                nuevaDireccion.setTipoppal("D");
            } else if (estadoTipoPpal.equals("TRANSVERSAL")) {
                nuevaDireccion.setTipoppal("T");
            } else if (estadoTipoPpal.equals("OTROS")) {
                nuevaDireccion.setTipoppal("O");
            }
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaUbicacionPrincipal");
        } else {
            if (estadoTipoPpal.equals("CALLE")) {
                duplicarDireccion.setTipoppal("C");
            } else if (estadoTipoPpal.equals("CARRERA")) {
                duplicarDireccion.setTipoppal("K");
            } else if (estadoTipoPpal.equals("AVENIDA CALLE")) {
                duplicarDireccion.setTipoppal("A");
            } else if (estadoTipoPpal.equals("AVENIDA CARRERA")) {
                duplicarDireccion.setTipoppal("M");
            } else if (estadoTipoPpal.equals("DIAGONAL")) {
                duplicarDireccion.setTipoppal("D");
            } else if (estadoTipoPpal.equals("TRANSVERSAL")) {
                duplicarDireccion.setTipoppal("T");
            } else if (estadoTipoPpal.equals("OTROS")) {
                duplicarDireccion.setTipoppal("O");
            }
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarUbicacionPrincipal");
        }

    }

    public void seleccionarTipoSecundario(String estadoTipoSecundario, int indice, int celda) {
        if (tipoLista == 0) {
            if (estadoTipoSecundario.equals("CALLE")) {
                direccionSeleccionada.setTiposecundario("C");
            } else if (estadoTipoSecundario.equals("CARRERA")) {
                direccionSeleccionada.setTiposecundario("K");
            } else if (estadoTipoSecundario.equals("AVENIDA CALLE")) {
                direccionSeleccionada.setTiposecundario("A");
            } else if (estadoTipoSecundario.equals("AVENIDA CARRERA")) {
                direccionSeleccionada.setTiposecundario("M");
            } else if (estadoTipoSecundario.equals("DIAGONAL")) {
                direccionSeleccionada.setTiposecundario("D");
            } else if (estadoTipoSecundario.equals("TRANSVERSAL")) {
                direccionSeleccionada.setTiposecundario("T");
            } else if (estadoTipoSecundario.equals("OTROS")) {
                direccionSeleccionada.setTiposecundario("O");
            }

            if (!listaDireccionesCrear.contains(direccionSeleccionada)) {
                if (listaDireccionesModificar.isEmpty()) {
                    listaDireccionesModificar.add(direccionSeleccionada);
                } else if (!listaDireccionesModificar.contains(direccionSeleccionada)) {
                    listaDireccionesModificar.add(direccionSeleccionada);
                }
            }
        } else {
            if (estadoTipoSecundario.equals("CALLE")) {
                filtradosListaDirecciones.get(indice).setTiposecundario("C");
            } else if (estadoTipoSecundario.equals("CARRERA")) {
                filtradosListaDirecciones.get(indice).setTiposecundario("K");
            } else if (estadoTipoSecundario.equals("AVENIDA CALLE")) {
                filtradosListaDirecciones.get(indice).setTiposecundario("A");
            } else if (estadoTipoSecundario.equals("AVENIDA CARRERA")) {
                filtradosListaDirecciones.get(indice).setTiposecundario("M");
            } else if (estadoTipoSecundario.equals("DIAGONAL")) {
                filtradosListaDirecciones.get(indice).setTiposecundario("D");
            } else if (estadoTipoSecundario.equals("TRANSVERSAL")) {
                filtradosListaDirecciones.get(indice).setTiposecundario("T");
            } else if (estadoTipoSecundario.equals("OTROS")) {
                filtradosListaDirecciones.get(indice).setTiposecundario("O");
            }

            if (!listaDireccionesCrear.contains(filtradosListaDirecciones.get(indice))) {
                if (listaDireccionesModificar.isEmpty()) {
                    listaDireccionesModificar.add(filtradosListaDirecciones.get(indice));
                } else if (!listaDireccionesModificar.contains(filtradosListaDirecciones.get(indice))) {
                    listaDireccionesModificar.add(filtradosListaDirecciones.get(indice));
                }
            }
        }
        if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
        RequestContext.getCurrentInstance().update("form:datosDireccionesPersona");
    }

    public void seleccionarTipoSecundarioNuevaDireccion(String estadoTipoSecundario, int tipoNuevo) {
        if (tipoNuevo == 1) {
            if (estadoTipoSecundario.equals("CALLE")) {
                nuevaDireccion.setTiposecundario("C");
            } else if (estadoTipoSecundario.equals("CARRERA")) {
                nuevaDireccion.setTiposecundario("K");
            } else if (estadoTipoSecundario.equals("AVENIDA CALLE")) {
                nuevaDireccion.setTiposecundario("A");
            } else if (estadoTipoSecundario.equals("AVENIDA CARRERA")) {
                nuevaDireccion.setTiposecundario("M");
            } else if (estadoTipoSecundario.equals("DIAGONAL")) {
                nuevaDireccion.setTiposecundario("D");
            } else if (estadoTipoSecundario.equals("TRANSVERSAL")) {
                nuevaDireccion.setTiposecundario("T");
            } else if (estadoTipoSecundario.equals("OTROS")) {
                nuevaDireccion.setTiposecundario("O");
            }
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaUbicacionSecundaria");
        } else {
            if (estadoTipoSecundario.equals("CALLE")) {
                duplicarDireccion.setTiposecundario("C");
            } else if (estadoTipoSecundario.equals("CARRERA")) {
                duplicarDireccion.setTiposecundario("K");
            } else if (estadoTipoSecundario.equals("AVENIDA CALLE")) {
                duplicarDireccion.setTiposecundario("A");
            } else if (estadoTipoSecundario.equals("AVENIDA CARRERA")) {
                duplicarDireccion.setTiposecundario("M");
            } else if (estadoTipoSecundario.equals("DIAGONAL")) {
                duplicarDireccion.setTiposecundario("D");
            } else if (estadoTipoSecundario.equals("TRANSVERSAL")) {
                duplicarDireccion.setTiposecundario("T");
            } else if (estadoTipoSecundario.equals("OTROS")) {
                duplicarDireccion.setTiposecundario("O");
            }
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarUbicacionSecundaria");
        }

    }

    public void seleccionarTipoVivienda(String estadoTipoVivienda, int indice, int celda) {
        if (tipoLista == 0) {
            if (estadoTipoVivienda.equals("CASA")) {
                direccionSeleccionada.setTipovivienda("CASA");
            } else if (estadoTipoVivienda.equals("APARTAMENTO")) {
                direccionSeleccionada.setTipovivienda("APARTAMENTO");
            } else if (estadoTipoVivienda.equals("FINCA")) {
                direccionSeleccionada.setTipovivienda("FINCA");
            }

            if (!listaDireccionesCrear.contains(direccionSeleccionada)) {
                if (listaDireccionesModificar.isEmpty()) {
                    listaDireccionesModificar.add(direccionSeleccionada);
                } else if (!listaDireccionesModificar.contains(direccionSeleccionada)) {
                    listaDireccionesModificar.add(direccionSeleccionada);
                }
            }
        } else {
            if (estadoTipoVivienda.equals("CASA")) {
                filtradosListaDirecciones.get(indice).setTipovivienda("CASA");
            } else if (estadoTipoVivienda.equals("APARTAMENTO")) {
                filtradosListaDirecciones.get(indice).setTipovivienda("APARTAMENTO");
            } else if (estadoTipoVivienda.equals("FINCA")) {
                filtradosListaDirecciones.get(indice).setTipovivienda("FINCA");
            }

            if (!listaDireccionesCrear.contains(filtradosListaDirecciones.get(indice))) {
                if (listaDireccionesModificar.isEmpty()) {
                    listaDireccionesModificar.add(filtradosListaDirecciones.get(indice));
                } else if (!listaDireccionesModificar.contains(filtradosListaDirecciones.get(indice))) {
                    listaDireccionesModificar.add(filtradosListaDirecciones.get(indice));
                }
            }
        }
        if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
        RequestContext.getCurrentInstance().update("form:datosDireccionesPersona");
    }

    public void seleccionarTipoViviendaNuevaDireccion(String estadoTipoVivienda, int tipoNuevo) {
        if (tipoNuevo == 1) {
            if (estadoTipoVivienda.equals("FINCA")) {
                nuevaDireccion.setTipovivienda("FINCA");
            } else if (estadoTipoVivienda.equals("CASA")) {
                nuevaDireccion.setTipovivienda("CASA");
            } else if (estadoTipoVivienda.equals("APARTAMENTO")) {
                nuevaDireccion.setTipovivienda("APARTAMENTO");
            }
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTipoVivienda");
        } else {
            if (estadoTipoVivienda.equals("FINCA")) {
                duplicarDireccion.setTipovivienda("FINCA");
            } else if (estadoTipoVivienda.equals("CASA")) {
                duplicarDireccion.setTipovivienda("CASA");
            } else if (estadoTipoVivienda.equals("APARTAMENTO")) {
                duplicarDireccion.setTipovivienda("APARTAMENTO");
            }
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTipoVivienda");
        }
    }

    public void seleccionarHipoteca(String estadoHipoteca, int indice, int celda) {
        if (tipoLista == 0) {
            if (estadoHipoteca.equals("SI")) {
                direccionSeleccionada.setTipovivienda("S");
            } else if (estadoHipoteca.equals("NO")) {
                direccionSeleccionada.setTiposecundario("N");
            }

            if (!listaDireccionesCrear.contains(direccionSeleccionada)) {
                if (listaDireccionesModificar.isEmpty()) {
                    listaDireccionesModificar.add(direccionSeleccionada);
                } else if (!listaDireccionesModificar.contains(direccionSeleccionada)) {
                    listaDireccionesModificar.add(direccionSeleccionada);
                }
            }
        } else {
            if (estadoHipoteca.equals("SI")) {
                filtradosListaDirecciones.get(indice).setTiposecundario("S");
            } else if (estadoHipoteca.equals("NO")) {
                filtradosListaDirecciones.get(indice).setTiposecundario("N");

                if (!listaDireccionesCrear.contains(filtradosListaDirecciones.get(indice))) {
                    if (listaDireccionesModificar.isEmpty()) {
                        listaDireccionesModificar.add(filtradosListaDirecciones.get(indice));
                    } else if (!listaDireccionesModificar.contains(filtradosListaDirecciones.get(indice))) {
                        listaDireccionesModificar.add(filtradosListaDirecciones.get(indice));
                    }
                }
            }
        }
        if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
        RequestContext.getCurrentInstance().update("form:datosDireccionesPersona");
    }

    public void seleccionarHipotecaNuevaDireccion(String estadoHipoteca, int tipoNuevo) {
        if (tipoNuevo == 1) {
            if (estadoHipoteca.equals("SI")) {
                nuevaDireccion.setHipoteca("S");
            } else {
                nuevaDireccion.setHipoteca("N");
            }

            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaHipoteca");
        } else {
            if (estadoHipoteca.equals("SI")) {
                duplicarDireccion.setHipoteca("S");
            } else {
                duplicarDireccion.setTipovivienda("N");
            }
        }
        RequestContext.getCurrentInstance().update("formularioDialogos:duplicarHipoteca");
    }

    public void eventoFiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        direccionSeleccionada = null;
        modificarInfoRegistro(filtradosListaDirecciones.size());
        RequestContext.getCurrentInstance().update("form:infoRegistro");
    }

    public void eventoFiltrarCiudades() {
        modificarInfoRegistroCiudades(filtradoslistaCiudades.size());
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroCiudades");
    }

    public void modificarInfoRegistro(int valor) {
        infoRegistro = String.valueOf(valor);
    }

    public void modificarInfoRegistroCiudades(int valor) {
        infoRegistroCiudades = String.valueOf(valor);
    }

    public void contarRegistros() {
        if (listaDirecciones != null) {
            modificarInfoRegistro(listaDirecciones.size());
        } else {
            modificarInfoRegistro(0);
        }
    }

    public void recordarSeleccion() {
        if (direccionSeleccionada != null) {
            FacesContext c = FacesContext.getCurrentInstance();
            tablaC = (DataTable) c.getViewRoot().findComponent("form:datosDireccionesPersona");
            tablaC.setSelection(direccionSeleccionada);
        }
    }

    public void habilitarBotonLOV() {
        activarLOV = false;
        RequestContext.getCurrentInstance().update("form:listaValores");
    }

    public void deshabilitarBotonLOV() {
        activarLOV = true;
        RequestContext.getCurrentInstance().update("form:listaValores");
    }

//GETTER & SETTER
    public List<Direcciones> getListaDirecciones() {

        if (listaDirecciones == null) {
            if (empleado.getPersona().getSecuencia() != null) {
                listaDirecciones = administrarDirecciones.consultarDireccionesPersona(empleado.getPersona().getSecuencia());
            }
        }
        return listaDirecciones;
    }

    public void setListaDirecciones(List<Direcciones> listaDirecciones) {
        this.listaDirecciones = listaDirecciones;
    }

    public List<Direcciones> getFiltradosListaDirecciones() {
        return filtradosListaDirecciones;
    }

    public void setFiltradosListaDirecciones(List<Direcciones> filtradosListaDirecciones) {
        this.filtradosListaDirecciones = filtradosListaDirecciones;
    }

    public BigInteger getSecuenciaPersona() {
        return secuenciaPersona;
    }

    public void setSecuenciaPersona(BigInteger secuenciaPersona) {
        this.secuenciaPersona = secuenciaPersona;
    }

    public Personas getPersona() {
        if (persona == null) {
            persona = administrarDirecciones.consultarPersona(secuenciaPersona);
        }
        return persona;
    }

    public void setPersona(Personas persona) {
        this.persona = persona;
    }

    public List<Ciudades> getListaCiudades() {
        if (listaCiudades.isEmpty()) {
            listaCiudades = administrarCiudades.consultarCiudades();
        }
        return listaCiudades;
    }

    public void setListaCiudades(List<Ciudades> listaCiudades) {
        this.listaCiudades = listaCiudades;
    }

    public List<Ciudades> getFiltradoslistaCiudades() {
        return filtradoslistaCiudades;
    }

    public void setFiltradoslistaCiudades(List<Ciudades> filtradoslistaCiudades) {
        this.filtradoslistaCiudades = filtradoslistaCiudades;
    }

    public Ciudades getSeleccionCiudades() {
        return seleccionCiudades;
    }

    public void setSeleccionCiudades(Ciudades seleccionCiudades) {
        this.seleccionCiudades = seleccionCiudades;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public void setAceptar(boolean aceptar) {
        this.aceptar = aceptar;
    }

    public Direcciones getEditarDireccion() {
        return editarDireccion;
    }

    public void setEditarDireccion(Direcciones editarDireccion) {
        this.editarDireccion = editarDireccion;
    }

    public Direcciones getDuplicarDireccion() {
        return duplicarDireccion;
    }

    public void setDuplicarDireccion(Direcciones duplicarDireccion) {
        this.duplicarDireccion = duplicarDireccion;
    }

    public Direcciones getNuevaDireccion() {
        return nuevaDireccion;
    }

    public void setNuevaDireccion(Direcciones nuevaDireccion) {
        this.nuevaDireccion = nuevaDireccion;
    }

    public String getMensajeValidacion() {
        return mensajeValidacion;
    }

    public void setMensajeValidacion(String mensajeValidacion) {
        this.mensajeValidacion = mensajeValidacion;
    }

    public Direcciones getDireccionSeleccionada() {
        return direccionSeleccionada;
    }

    public void setDireccionSeleccionada(Direcciones direccionSeleccionada) {
        this.direccionSeleccionada = direccionSeleccionada;
    }

    public String getAltoTabla() {
        return altoTabla;
    }

    public void setAltoTabla(String altoTabla) {
        this.altoTabla = altoTabla;
    }

    public boolean isGuardado() {
        return guardado;
    }

    public void setGuardado(boolean guardado) {
        this.guardado = guardado;
    }

    public Empleados getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleados empleado) {
        this.empleado = empleado;
    }

    public String getInfoRegistro() {
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

    public boolean isActivarLOV() {
        return activarLOV;
    }

    public void setActivarLOV(boolean activarLOV) {
        this.activarLOV = activarLOV;
    }

    public String getInfoRegistroCiudades() {
        return infoRegistroCiudades;
    }

    public void setInfoRegistroCiudades(String infoRegistroCiudades) {
        this.infoRegistroCiudades = infoRegistroCiudades;
    }

}
