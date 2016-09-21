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
        nuevaDireccion.setTipoppal("C");
        nuevaDireccion.setTiposecundario("K");
        nuevaDireccion.setTipovivienda("CASA");
        nuevaDireccion.setHipoteca("N");
        listaDireccionesBorrar = new ArrayList<Direcciones>();
        listaDireccionesCrear = new ArrayList<Direcciones>();
        listaDireccionesModificar = new ArrayList<Direcciones>();
        //INICIALIZAR LOVS
        listaCiudades = new ArrayList<Ciudades>();
        direccionSeleccionada = null;
        listaDirecciones = null;
        k = 0;
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
    }

    public void cambiarIndice(Direcciones direccion, int celda) {
        if (permitirIndex == true) {
            direccionSeleccionada = direccion;
            cualCelda = celda;
            if (tipoLista == 0) {
                direccionSeleccionada.getSecuencia();
                deshabilitarBotonLOV();
                if (cualCelda == 6) {
                    contarRegistroCiudades();
                    Ciudad = direccionSeleccionada.getCiudad().getNombre();
                    habilitarBotonLOV();
                }
            } else {
                direccionSeleccionada.getSecuencia();
                deshabilitarBotonLOV();
                if (cualCelda == 6) {
                   contarRegistroCiudades();
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
                RequestContext.getCurrentInstance().update("formularioDialogos:pregunta");
                RequestContext.getCurrentInstance().execute("PF('pregunta').show()");
            }
        } else if (tipoNuevo == 2) {
            if (duplicarDireccion.getTipoppal() != null && duplicarDireccion.getPpal() != null && duplicarDireccion.getSecundario() != null && duplicarDireccion.getTiposecundario() != null) {
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("formularioDialogos:pregunta");
                RequestContext.getCurrentInstance().execute("PF('pregunta').show()");
            }
        }
    }

    public void copiarDireccion(int tipoNuevo) {
        if (tipoNuevo == 1) {
            nuevaDireccion.setDireccionalternativa(nuevaDireccion.getEstadoTipoPpal() + (" ") + nuevaDireccion.getPpal() + (" ") + nuevaDireccion.getEstadoTipoSecundario() + (" ") + nuevaDireccion.getSecundario());
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaDireccion");
        } else {
            duplicarDireccion.setDireccionalternativa(nuevaDireccion.getTipoppal() + (" ") + duplicarDireccion.getPpal() + (" ") + duplicarDireccion.getTiposecundario() + (" ") + duplicarDireccion.getSecundario());
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarDireccion");
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
            contarRegistros();
            deshabilitarBotonLOV();
            RequestContext.getCurrentInstance().update("form:infoRegistro");
            RequestContext.getCurrentInstance().update("form:datosDireccionesPersona");
            nuevaDireccion = new Direcciones();
            nuevaDireccion.setCiudad(new Ciudades());
            nuevaDireccion.setFechavigencia(new Date());
            nuevaDireccion.setTipoppal("C");
            nuevaDireccion.setTiposecundario("K");
            nuevaDireccion.setTipovivienda("CASA");
            nuevaDireccion.setHipoteca("N");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroDireccion').hide()");
        } else {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevaDireccion");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevaDireccion').show()");
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

            } else if (!listaDireccionesCrear.contains(direccionSeleccionada)) {

                if (listaDireccionesModificar.isEmpty()) {
                    listaDireccionesModificar.add(direccionSeleccionada);
                } else if (!listaDireccionesModificar.contains(direccionSeleccionada)) {
                    listaDireccionesModificar.add(direccionSeleccionada);
                }
                if (guardado == true) {
                    guardado = false;
                }
            }
            RequestContext.getCurrentInstance().update("form:datosDireccionesPersona");
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
                RequestContext.getCurrentInstance().update("formularioDialogos:ciudadesDialogo");
                RequestContext.getCurrentInstance().execute("PF('ciudadesDialogo').show()");
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
            } else if (!listaDireccionesCrear.contains(direccionSeleccionada)) {

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
        RequestContext.getCurrentInstance().update("form:datosDireccionesPersona");
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
            RequestContext.getCurrentInstance().update("form:datosDireccionesPersona");
        } else if (tipoActualizacion == 1) {
            nuevaDireccion.setCiudad(seleccionCiudades);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaDireccion");
        } else if (tipoActualizacion == 2) {
            System.out.println(seleccionCiudades.getNombre());
            duplicarDireccion.setCiudad(seleccionCiudades);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarDireccion");
        }
        filtradoslistaCiudades = null;
        seleccionCiudades = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("formularioDialogos:LOVCiudades:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVCiudades').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('ciudadesDialogo').hide()");
        //RequestContext.getCurrentInstance().update("formularioDialogos:LOVCiudades");
    }

    public void activarCtrlF11() {
        System.out.println("TipoLista= " + tipoLista);
        FacesContext c = FacesContext.getCurrentInstance();

        if (bandera == 0) {

            System.out.println("Activar");
            System.out.println("TipoLista= " + tipoLista);
            dFecha = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dFecha");
            dFecha.setFilterStyle("width: 85% !important");
            dUbicacionPrincipal = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dUbicacionPrincipal");
            dUbicacionPrincipal.setFilterStyle("width: 85% !important");
            dDescripcionUbicacionPrincipal = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dDescripcionUbicacionPrincipal");
            dDescripcionUbicacionPrincipal.setFilterStyle("width: 85% !important");
            dUbicacionSecundaria = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dUbicacionSecundaria");
            dUbicacionSecundaria.setFilterStyle("width: 85% !important");
            dDescripcionUbicacionSecundaria = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dDescripcionUbicacionSecundaria");
            dDescripcionUbicacionSecundaria.setFilterStyle("width: 85% !important");
            dInterior = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dInterior");
            dInterior.setFilterStyle("width: 85% !important");
            dCiudad = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dCiudad");
            dCiudad.setFilterStyle("width: 85% !important");
            dTipoVivienda = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dTipoVivienda");
            dTipoVivienda.setFilterStyle("width: 85% !important");
            dHipoteca = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dHipoteca");
            dHipoteca.setFilterStyle("width: 85% !important");
            dDireccionAlternativa = (Column) c.getViewRoot().findComponent("form:datosDireccionesPersona:dDireccionAlternativa");
            dDireccionAlternativa.setFilterStyle("width: 85% !important");
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
        nuevaDireccion.setFechavigencia(new Date());
        nuevaDireccion.setTipoppal("C");
        nuevaDireccion.setTiposecundario("K");
        nuevaDireccion.setTipovivienda("CASA");
        nuevaDireccion.setHipoteca("N");
    }

    //LISTA DE VALORES DINAMICA
    public void listaValoresBoton() {
        if (direccionSeleccionada != null) {
            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCelda == 6) {
                habilitarBotonLOV();
                contarRegistroCiudades();
                RequestContext.getCurrentInstance().update("formularioDialogos:ciudadesDialogo");
                RequestContext.getCurrentInstance().execute("PF('ciudadesDialogo').show()");
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
       contarRegistroCiudades();
        RequestContext.getCurrentInstance().update("formularioDialogos:ciudadesDialogo");
        RequestContext.getCurrentInstance().execute("PF('ciudadesDialogo').show()");
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
                RequestContext.getCurrentInstance().update("formularioDialogos:editarFecha");
                RequestContext.getCurrentInstance().execute("PF('editarFecha').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                deshabilitarBotonLOV();
                RequestContext.getCurrentInstance().update("formularioDialogos:editarUbicacionesPrincipales");
                RequestContext.getCurrentInstance().execute("PF('editarUbicacionesPrincipales').show()");
                cualCelda = -1;
            } else if (cualCelda == 2) {
                deshabilitarBotonLOV();
                RequestContext.getCurrentInstance().update("formularioDialogos:editarDescripcionesUbicacionesPrincipales");
                RequestContext.getCurrentInstance().execute("PF('editarDescripcionesUbicacionesPrincipales').show()");
                cualCelda = -1;
            } else if (cualCelda == 3) {
                deshabilitarBotonLOV();
                RequestContext.getCurrentInstance().update("formularioDialogos:editarUbicacionesSecundarias");
                RequestContext.getCurrentInstance().execute("PF('editarUbicacionesSecundarias').show()");
                cualCelda = -1;
            } else if (cualCelda == 4) {
                deshabilitarBotonLOV();
                RequestContext.getCurrentInstance().update("formularioDialogos:editarDescripcionesUbicacionesSecundarias");
                RequestContext.getCurrentInstance().execute("PF('editarDescripcionesUbicacionesSecundarias').show()");
                cualCelda = -1;
            } else if (cualCelda == 5) {
                deshabilitarBotonLOV();
                RequestContext.getCurrentInstance().update("formularioDialogos:editarBarrios");
                RequestContext.getCurrentInstance().execute("PF('editarBarrios').show()");
                cualCelda = -1;
            } else if (cualCelda == 6) {
                habilitarBotonLOV();
                RequestContext.getCurrentInstance().update("formularioDialogos:editarCiudades");
                RequestContext.getCurrentInstance().execute("PF('editarCiudades').show()");
                cualCelda = -1;
            } else if (cualCelda == 7) {
                deshabilitarBotonLOV();
                RequestContext.getCurrentInstance().update("formularioDialogos:editarTiposViviendas");
                RequestContext.getCurrentInstance().execute("PF('editarTiposViviendas').show()");
                cualCelda = -1;
            } else if (cualCelda == 8) {
                deshabilitarBotonLOV();
                RequestContext.getCurrentInstance().update("formularioDialogos:editarHipotecas");
                RequestContext.getCurrentInstance().execute("PF('editarHipotecas').show()");
                cualCelda = -1;
            } else if (cualCelda == 9) {
                deshabilitarBotonLOV();
                RequestContext.getCurrentInstance().update("formularioDialogos:editarDireccionesAlternativas");
                RequestContext.getCurrentInstance().execute("PF('editarDireccionesAlternativas').show()");
                cualCelda = -1;
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
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

            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarDireccion");
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroDireccion').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void confirmarDuplicar() {
        int pasa = 0;
        int pasaA = 0;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        FacesContext c = FacesContext.getCurrentInstance();
        if (duplicarDireccion.getFechavigencia() == null) {
            mensajeValidacion = " Campo fecha vacío \n";
            pasa++;

        }

        System.out.println("fecha direccion " + duplicarDireccion.getFechavigencia() + "\n");
        System.out.println("secuencia direccion " + duplicarDireccion.getSecuencia() + "\n");
        for (int i = 0; i < listaDirecciones.size(); i++) {
            if ((listaDirecciones.get(i).getSecuencia().equals(duplicarDireccion.getSecuencia())) && ((listaDirecciones.get(i).getFechavigencia().equals(duplicarDireccion.getFechavigencia())))) {// && !(duplicarTelefono.getFechavigencia().before(listaTelefonos.get(i).getFechavigencia())))) {
                RequestContext.getCurrentInstance().update("formularioDialogos:existeDireccion");
                RequestContext.getCurrentInstance().execute("PF('existeDireccion').show()");
                pasaA++;
            }
            if (pasa != 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevaDireccion");
                RequestContext.getCurrentInstance().execute("PF('validacionNuevaDireccion').show()");
            }
        }
//
        if (pasa == 0 && pasaA == 0) {

            listaDirecciones.add(duplicarDireccion);
            listaDireccionesCrear.add(duplicarDireccion);
            direccionSeleccionada = duplicarDireccion;
            getListaDirecciones();
           contarRegistros();
            RequestContext.getCurrentInstance().update("form:datosDireccionesPersona");
            RequestContext.getCurrentInstance().update("form:infoRegistro");
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
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroDireccion').hide()");
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
            RequestContext.getCurrentInstance().update("form:datosDireccionesPersona");
            guardado = true;
            permitirIndex = true;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
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
        RequestContext.getCurrentInstance().execute("PF('LOVCiudades').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('ciudadesDialogo').hide()");
        RequestContext.getCurrentInstance().update("formularioDialogos:ciudadesDialogo");
        RequestContext.getCurrentInstance().update("form:LOVCiudades");
        RequestContext.getCurrentInstance().update("form:aceptarC");

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
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("form:datosDireccionesPersona");
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
                RequestContext.getCurrentInstance().update("formularioDialogos:nuevaCiudad");
            } else if (tipoNuevo == 2) {
                duplicarDireccion.setCiudad(seleccionCiudades);
                RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCiudad");
            }
            listaCiudades.clear();
            getListaCiudades();
        } else {
            RequestContext.getCurrentInstance().update("formularioDialogos:ciudadesDialogo");
            RequestContext.getCurrentInstance().execute("PF('ciudadesDialogo').show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:nuevaCiudad");
            } else if (tipoNuevo == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCiudad");
            }
        }
    }

    public void llamarLovCiudad(int tipoN) {
        if (tipoN == 1) {
            tipoActualizacion = 1;
        } else if (tipoN == 2) {
            tipoActualizacion = 2;
        }
        contarRegistroCiudades();
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("formularioDialogos:ciudadesDialogo");
        RequestContext.getCurrentInstance().execute("PF('ciudadesDialogo').show()");
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
            RequestContext.getCurrentInstance().update("form:datosDireccionesPersona");
            direccionSeleccionada = null;

            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
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
        } else if (administrarRastros.verificarHistoricosTabla("DIRECCIONES")) {
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
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
        } else if (estadoHipoteca.equals("SI")) {
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
        } else if (estadoHipoteca.equals("SI")) {
            duplicarDireccion.setHipoteca("S");
        } else {
            duplicarDireccion.setTipovivienda("N");
        }
        RequestContext.getCurrentInstance().update("formularioDialogos:duplicarHipoteca");
    }

    public void eventoFiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        contarRegistros();
    }

    public void contarRegistroCiudades() {
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroCiudades");
    }

    public void contarRegistros() {
        RequestContext.getCurrentInstance().update("form:infoRegistro");
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
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosDireccionesPersona");
        infoRegistro = String.valueOf(tabla.getRowCount());
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
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:LOVCiudades");
        infoRegistroCiudades = String.valueOf(tabla.getRowCount());
        return infoRegistroCiudades;
    }

    public void setInfoRegistroCiudades(String infoRegistroCiudades) {
        this.infoRegistroCiudades = infoRegistroCiudades;
    }

}
