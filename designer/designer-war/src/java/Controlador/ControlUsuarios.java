package Controlador;

import Administrar.AdministrarUsuarios;
import Entidades.Pantallas;
import Entidades.Perfiles;
import Entidades.Personas;
import Entidades.Usuarios;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarRastrosInterface;
import InterfaceAdministrar.AdministrarUsuariosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
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
import utilidadesUI.PrimefacesContextUI;

/**
 *
 * @author Administrador
 */
@ManagedBean
@SessionScoped
public class ControlUsuarios implements Serializable {

    @EJB
    AdministrarUsuariosInterface administrarUsuario;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    private List<Usuarios> listaUsuarios;
    private List<Usuarios> filtrarUsuarios;
    private List<Usuarios> listaUsuariosCrear;
    private String mensajeValidacion;
    private String mensajeV;
    private String mensajeContra;
    private List<Usuarios> listaUsuariosModificar;
    private List<Usuarios> listaUsuariosBorrar;
    //LISTA DE VALORES DE PERSONAS!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    private List<Personas> lovPersonas;
    private List<Personas> lovFiltradoPersonas;
    private Personas personasSeleccionado;
    //LISTA DE VALORES DE PERFILES!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    private List<Perfiles> lovPerfiles;
    private List<Perfiles> lovFiltradoPerfiles;
    private Perfiles perfilesSeleccionado;
    //LISTA DE VALORES DE PANTALLAS!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    private List<Pantallas> lovPantallas;
    private List<Pantallas> lovFiltradoPantallas;
    private Pantallas pantallasSeleccionado;
    //NUEVO, DUPLICADO EDITAR Y SELECCIONADA
    private Usuarios nuevaUsuarios;
    private Usuarios duplicarUsuarios;
    private Usuarios eliminarUsuarios;
    private Usuarios clonarUsuarios;
    private Usuarios editarUsuarios;
    private Usuarios usuariosSeleccionado;
    private BigInteger secRegistro;
    public String altoTabla;
    public String infoRegistroPersonas, infoRegistroPerfiles, infoRegistroPantallas;
    //CLON
    private List<Usuarios> lovUsuarioAlias;
    private List<Usuarios> lovfiltrarUsuarioAlias;
    private String auxClon;
    //AutoCompletar
    private boolean permitirIndex;
    private String persona, perfil, pantalla;
    //Tabla a Imprimir
    private String tablaImprimir, nombreArchivo;
    private Column usuarioPersona, usuarioPerfil, usuarioAlias, usuarioPantallaInicio, usuarioActivo;
    public String infoRegistro;
    ///////////////////////////////////////////////
    //////////PRUEBAS UNITARIAS COMPONENTES////////
    ///////////////////////////////////////////////
    public boolean buscador;
    public String paginaAnterior;
    public String alisin;
    //otros
    private int cualCelda, tipoLista, index, tipoActualizacion, k, bandera;
    private BigInteger l;
    private boolean aceptar, guardado;

    public ControlUsuarios() {

        permitirIndex = true;
        aceptar = true;
        tipoLista = 0;
        listaUsuarios = null;
        lovUsuarioAlias = null;
        listaUsuariosCrear = new ArrayList<Usuarios>();
        listaUsuariosModificar = new ArrayList<Usuarios>();
        listaUsuariosBorrar = new ArrayList<Usuarios>();
        lovPersonas = null;
        lovPerfiles = null;
        lovPantallas = null;
        lovUsuarioAlias = null;

        cualCelda = -1;
        tipoLista = 0;
        nuevaUsuarios = new Usuarios();
        nuevaUsuarios.setPersona(new Personas());
        nuevaUsuarios.setPerfil(new Perfiles());
        nuevaUsuarios.setPantallainicio(new Pantallas());
        duplicarUsuarios = new Usuarios();
        eliminarUsuarios = new Usuarios();
        clonarUsuarios = new Usuarios();
        duplicarUsuarios.setPersona(new Personas());
        duplicarUsuarios.setPerfil(new Perfiles());
        duplicarUsuarios.setPantallainicio(new Pantallas());
        secRegistro = null;
        k = 0;
        auxClon = "";
        altoTabla = "270";
        guardado = true;
        buscador = false;
        tablaImprimir = ":formExportar:datosUsuariosExportar";
        nombreArchivo = "UsuariosXML";

    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarUsuario.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
            getListaUsuarios();
            if (listaUsuarios != null) {
                infoRegistro = "Cantidad de registros : " + listaUsuarios.size();
            } else {
                infoRegistro = "Cantidad de registros : 0";
            }
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void recibirPaginaEntrante(String pagina) {
        paginaAnterior = pagina;
    }

    public String redirigir() {
        return paginaAnterior;
    }

    public void activarAceptar() {
        aceptar = false;
    }

    //ACTIVAR F11
    public void activarCtrlF11() {
        System.out.println("TipoLista= " + tipoLista);
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            System.out.println("Activar");
            usuarioPersona = (Column) c.getViewRoot().findComponent("form:datosUsuarios:persona");
            usuarioPersona.setFilterStyle("width: 85%");
            usuarioPerfil = (Column) c.getViewRoot().findComponent("form:datosUsuarios:perfil");
            usuarioPerfil.setFilterStyle("width: 85%");
            usuarioAlias = (Column) c.getViewRoot().findComponent("form:datosUsuarios:alias");
            usuarioAlias.setFilterStyle("width: 85%");
            usuarioPantallaInicio = (Column) c.getViewRoot().findComponent("form:datosUsuarios:pantalla");
            usuarioPantallaInicio.setFilterStyle("width: 85%");
            altoTabla = "250";
            PrimefacesContextUI.actualizar("form:datosUsuarios");
            bandera = 1;
            tipoLista = 1;
            System.out.println("TipoLista= " + tipoLista);
        } else if (bandera == 1) {
            System.out.println("Desactivar");
            usuarioPersona = (Column) c.getViewRoot().findComponent("form:datosUsuarios:persona");
            usuarioPersona.setFilterStyle("display: none; visibility: hidden;");
            System.out.println("persona");
            usuarioPerfil = (Column) c.getViewRoot().findComponent("form:datosUsuarios:perfil");
            usuarioPerfil.setFilterStyle("display: none; visibility: hidden;");
            System.out.println("perfil");
            usuarioAlias = (Column) c.getViewRoot().findComponent("form:datosUsuarios:alias");
            usuarioAlias.setFilterStyle("display: none; visibility: hidden;");
            System.out.println("alias");
            usuarioPantallaInicio = (Column) c.getViewRoot().findComponent("form:datosUsuarios:pantalla");
            System.out.println("pantalla1");
            usuarioPantallaInicio.setFilterStyle("display: none; visibility: hidden;");
            System.out.println("pantalla2");
            PrimefacesContextUI.actualizar("form:datosUsuarios");
            altoTabla = "270";
            bandera = 0;
            filtrarUsuarios = null;
            tipoLista = 0;
            System.out.println("TipoLista= " + tipoLista);

        }
    }

    //EVENTO FILTRAR
    public void eventofiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        RequestContext context = RequestContext.getCurrentInstance();
        infoRegistro = "Cantidad de registros: " + filtrarUsuarios.size();
        PrimefacesContextUI.actualizar("form:informacionRegistro");
    }

    //UBICACION CELDA
    public void cambiarIndice(int indice, int celda) {
        if (permitirIndex == true) {
            index = indice;
            usuariosSeleccionado = listaUsuarios.get(index);
            cualCelda = celda;
            tablaImprimir = ":formExportar:datosUsuarioExportar";
            nombreArchivo = "UsuariosXML";
            if (tipoLista == 0) {
                alisin = listaUsuarios.get(index).getAlias();
                secRegistro = listaUsuarios.get(index).getSecuencia();
                if (cualCelda == 0) {
                    persona = listaUsuarios.get(index).getPersona().getNombreCompleto();
                }
                if (cualCelda == 1) {
                    perfil = listaUsuarios.get(index).getPerfil().getDescripcion();
                }
                if (cualCelda == 3) {
                    pantalla = listaUsuarios.get(index).getPantallainicio().getNombre();
                }
            } else {
                secRegistro = filtrarUsuarios.get(index).getSecuencia();
                alisin = filtrarUsuarios.get(index).getAlias();
                if (cualCelda == 0) {
                    persona = filtrarUsuarios.get(index).getPersona().getNombreCompleto();
                }
                if (cualCelda == 1) {
                    perfil = filtrarUsuarios.get(index).getPerfil().getDescripcion();
                }
                if (cualCelda == 3) {
                    pantalla = filtrarUsuarios.get(index).getPantallainicio().getNombre();
                }
            }
        }
    }

    //MOSTRAR DATOS CELDA
    public void editarCelda() {
        if (index >= 0) {
            if (tipoLista == 0) {
                editarUsuarios = listaUsuarios.get(index);
            }
            if (tipoLista == 1) {
                editarUsuarios = filtrarUsuarios.get(index);
            }

            RequestContext context = RequestContext.getCurrentInstance();
            System.out.println("Entro a editar... valor celda: " + cualCelda);
            if (cualCelda == 0) {
                PrimefacesContextUI.actualizar("formularioDialogos:editarPersona");
                PrimefacesContextUI.ejecutar("PF('editarPersona').show()");
                cualCelda = -1;
            }
            if (cualCelda == 1) {
                PrimefacesContextUI.actualizar("formularioDialogos:editarPerfil");
                PrimefacesContextUI.ejecutar("PF('editarPerfil').show()");
                cualCelda = -1;
            }
            if (cualCelda == 2) {
                PrimefacesContextUI.actualizar("formularioDialogos:editarAlias");
                PrimefacesContextUI.ejecutar("PF('editarAlias').show()");
                cualCelda = -1;
            }
            if (cualCelda == 3) {
                PrimefacesContextUI.actualizar("formularioDialogos:editarPantalla");
                PrimefacesContextUI.ejecutar("PF('editarPantalla').show()");
                cualCelda = -1;
            }
        }
        index = -1;
        secRegistro = null;
    }

    //MOSTRAR L.O.V PERSONAS
    public void actualizarPersonas() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                listaUsuarios.get(index).setPersona(personasSeleccionado);
                if (!listaUsuariosCrear.contains(listaUsuarios.get(index))) {
                    if (listaUsuariosModificar.isEmpty()) {
                        listaUsuariosModificar.add(listaUsuarios.get(index));
                    } else if (!listaUsuariosModificar.contains(listaUsuarios.get(index))) {
                        listaUsuariosModificar.add(listaUsuarios.get(index));
                    }
                }
            } else {
                filtrarUsuarios.get(index).setPersona(personasSeleccionado);
                if (!listaUsuariosCrear.contains(filtrarUsuarios.get(index))) {
                    if (listaUsuariosModificar.isEmpty()) {
                        listaUsuariosModificar.add(filtrarUsuarios.get(index));
                    } else if (!listaUsuariosModificar.contains(filtrarUsuarios.get(index))) {
                        listaUsuariosModificar.add(filtrarUsuarios.get(index));
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
                PrimefacesContextUI.actualizar("form:ACEPTAR");

            }
            permitirIndex = true;
            PrimefacesContextUI.actualizar("form:datosUsuarios");
        } else if (tipoActualizacion == 1) {
            nuevaUsuarios.setPersona(personasSeleccionado);
            PrimefacesContextUI.actualizar("formularioDialogos:nuevaUsuario");
        } else if (tipoActualizacion == 2) {
            duplicarUsuarios.setPersona(personasSeleccionado);
            PrimefacesContextUI.actualizar("formularioDialogos:duplicarUsuario");
        }
        lovFiltradoPersonas = null;
        personasSeleccionado = null;
        aceptar = true;
        index = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("formularioDialogos:LOVPersonas:globalFilter");
        PrimefacesContextUI.ejecutar("PF('LOVPersonas').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('personasDialogo').hide()");
        infoRegistroPersonas = "Cantidad de registros: " + lovPersonas.size();
        
    }

    //ASIGNAR INDEX PARA DIALOGOS COMUNES (LND = LISTA - NUEVO - DUPLICADO)
    public void asignarIndexPersona(Integer indice, int dlg, int LND) {
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
            PrimefacesContextUI.actualizar("formularioDialogos:personasDialogo");
            PrimefacesContextUI.ejecutar("PF('personasDialogo').show()");
            infoRegistroPersonas = "Cantidad de registros: " + lovPersonas.size();
            PrimefacesContextUI.actualizar("formularioDialogos:infoRegistroPersonas");
        }
    }

    public void cancelarCambioPersona() {
        lovFiltradoPersonas = null;
        personasSeleccionado = null;
        aceptar = true;
        index = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:LOVPersonas:globalFilter");
        PrimefacesContextUI.ejecutar("PF('LOVPersonas').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('personasDialogo').hide()");
    }

    //MOSTRAR L.O.V PERFILES
    public void actualizarPerfiles() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                listaUsuarios.get(index).setPerfil(perfilesSeleccionado);
                if (!listaUsuariosCrear.contains(listaUsuarios.get(index))) {
                    if (listaUsuariosModificar.isEmpty()) {
                        listaUsuariosModificar.add(listaUsuarios.get(index));
                    } else if (!listaUsuariosModificar.contains(listaUsuarios.get(index))) {
                        listaUsuariosModificar.add(listaUsuarios.get(index));
                    }
                }
            } else {
                filtrarUsuarios.get(index).setPerfil(perfilesSeleccionado);
                if (!listaUsuariosCrear.contains(filtrarUsuarios.get(index))) {
                    if (listaUsuariosModificar.isEmpty()) {
                        listaUsuariosModificar.add(filtrarUsuarios.get(index));
                    } else if (!listaUsuariosModificar.contains(filtrarUsuarios.get(index))) {
                        listaUsuariosModificar.add(filtrarUsuarios.get(index));
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
                PrimefacesContextUI.actualizar("form:ACEPTAR");

            }
            permitirIndex = true;
            PrimefacesContextUI.actualizar("form:datosUsuarios");
        } else if (tipoActualizacion == 1) {
            nuevaUsuarios.setPerfil(perfilesSeleccionado);
            PrimefacesContextUI.actualizar("formularioDialogos:nuevaUsuario");
        } else if (tipoActualizacion == 2) {
            duplicarUsuarios.setPerfil(perfilesSeleccionado);
            PrimefacesContextUI.actualizar("formularioDialogos:duplicarUsuario");
        }
        lovFiltradoPerfiles = null;
        perfilesSeleccionado = null;
        aceptar = true;
        index = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("formularioDialogos:LOVPerfiles:globalFilter");
        PrimefacesContextUI.ejecutar("PF('LOVPerfiles').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('perfilesDialogo').hide()");
        infoRegistroPerfiles = "Cantidad de registros: " + lovPerfiles.size();
    }

    //ASIGNAR INDEX PARA DIALOGOS COMUNES (LND = LISTA - NUEVO - DUPLICADO)
    public void asignarIndexPerfil(Integer indice, int dlg, int LND) {
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
            PrimefacesContextUI.actualizar("formularioDialogos:perfilesDialogo");
            PrimefacesContextUI.ejecutar("PF('perfilesDialogo').show()");
            infoRegistroPerfiles = "Cantidad de registros: " + lovPerfiles.size();
            PrimefacesContextUI.actualizar("formularioDialogos:infoRegistroPerfiles");
        }
    }

    public void lovUsuarios() {
        RequestContext context = RequestContext.getCurrentInstance();
        getLovUsuarioAlias();
        PrimefacesContextUI.actualizar("formularioDialogos:aliasDialogo");
        PrimefacesContextUI.ejecutar("PF('aliasDialogo').show()");
    }

    public void cancelarCambioAlias() {
        lovFiltradoPantallas = null;
        usuariosSeleccionado = null;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:LOVUsuariosAlias:globalFilter");
        PrimefacesContextUI.ejecutar("PF('LOVUsuariosAlias').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('aliasDialogo').hide()");
    }

    public void cancelarCambioPerfil() {
        lovFiltradoPerfiles = null;
        perfilesSeleccionado = null;
        aceptar = true;
        index = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        permitirIndex = true;RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:LOVPerfiles:globalFilter");
        PrimefacesContextUI.ejecutar("PF('LOVPerfiles').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('perfilesDialogo').hide()");
    }

    //MOSTRAR L.O.V PANTALLAS
    public void actualizarPantallas() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                listaUsuarios.get(index).setPantallainicio(pantallasSeleccionado);
                if (!listaUsuariosCrear.contains(listaUsuarios.get(index))) {
                    if (listaUsuariosModificar.isEmpty()) {
                        listaUsuariosModificar.add(listaUsuarios.get(index));
                    } else if (!listaUsuariosModificar.contains(listaUsuarios.get(index))) {
                        listaUsuariosModificar.add(listaUsuarios.get(index));
                    }
                }
            } else {
                filtrarUsuarios.get(index).setPantallainicio(pantallasSeleccionado);
                if (!listaUsuariosCrear.contains(filtrarUsuarios.get(index))) {
                    if (listaUsuariosModificar.isEmpty()) {
                        listaUsuariosModificar.add(filtrarUsuarios.get(index));
                    } else if (!listaUsuariosModificar.contains(filtrarUsuarios.get(index))) {
                        listaUsuariosModificar.add(filtrarUsuarios.get(index));
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
                PrimefacesContextUI.actualizar("form:ACEPTAR");

            }
            permitirIndex = true;
            PrimefacesContextUI.actualizar("form:datosUsuarios");
        } else if (tipoActualizacion == 1) {
            nuevaUsuarios.setPantallainicio(pantallasSeleccionado);
            PrimefacesContextUI.actualizar("formularioDialogos:nuevaUsuario");
        } else if (tipoActualizacion == 2) {
            duplicarUsuarios.setPantallainicio(pantallasSeleccionado);
            PrimefacesContextUI.actualizar("formularioDialogos:duplicarUsuario");
        }
        lovFiltradoPantallas = null;
        pantallasSeleccionado = null;
        aceptar = true;
        index = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("formularioDialogos:LOVPantallas:globalFilter");
        PrimefacesContextUI.ejecutar("PF('LOVPantallas').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('pantallasDialogo').hide()");
        infoRegistroPantallas = "Cantidad de registros: " + lovPantallas.size();
    }

    //ASIGNAR INDEX PARA DIALOGOS COMUNES (LND = LISTA - NUEVO - DUPLICADO)
    public void asignarIndexPantalla(Integer indice, int dlg, int LND) {
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
            PrimefacesContextUI.actualizar("formularioDialogos:pantallasDialogo");
            PrimefacesContextUI.ejecutar("PF('pantallasDialogo').show()");
            infoRegistroPantallas = "Cantidad de registros: " + lovPantallas.size();
            PrimefacesContextUI.actualizar("formularioDialogos:infoRegistroPantallas");
        }
    }

    public void cancelarCambioPantalla() {
        lovFiltradoPantallas = null;
        pantallasSeleccionado = null;
        aceptar = true;
        index = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        permitirIndex = true;RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:LOVPantallas:globalFilter");
        PrimefacesContextUI.ejecutar("PF('LOVPantallas').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('pantallasDialogo').hide()");
    }

    //LISTA DE VALORES DINAMICA
    public void listaValoresBoton() {
        if (index >= 0) {
            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCelda == 0) {
                PrimefacesContextUI.actualizar("formularioDialogos:personasDialogo");
                PrimefacesContextUI.ejecutar("PF('personasDialogo').show()");
                tipoActualizacion = 0;
            } else if (cualCelda == 1) {
                PrimefacesContextUI.actualizar("formularioDialogos:perfilesDialogo");
                PrimefacesContextUI.ejecutar("PF('perfilesDialogo').show()");
                tipoActualizacion = 0;
            } else if (cualCelda == 3) {
                PrimefacesContextUI.actualizar("formularioDialogos:pantallasDialogo");
                PrimefacesContextUI.ejecutar("PF('pantallasDialogo').show()");
                tipoActualizacion = 0;
            }
        }
    }

    //AUTOCOMPLETAR
    public void modificarUsuarios(int indice, String confirmarCambio, String valorConfirmar) {
        index = indice;
        int pasa = 0;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("N")) {

            if (tipoLista == 0) {

                if (!listaUsuariosCrear.contains(listaUsuarios.get(indice))) {
                    if (listaUsuarios.get(indice).getAlias() == null || listaUsuarios.get(indice).getAlias().equals("")) {
                        System.out.println("entra2");
                        pasa++;
                    }
                    if (pasa == 0) {
                        if (listaUsuariosModificar.isEmpty()) {
                            listaUsuariosModificar.add(listaUsuarios.get(indice));
                        } else if (!listaUsuariosModificar.contains(listaUsuarios.get(indice))) {
                            listaUsuariosModificar.add(listaUsuarios.get(indice));
                        }

                        if (guardado == true) {
                            guardado = false;
                            PrimefacesContextUI.actualizar("form:ACEPTAR");
                        }
                    } else if (pasa != 0) {
                        listaUsuarios.get(indice).setAlias(alisin);
                        PrimefacesContextUI.actualizar("formularioDialogos:validacionAlias");
                        PrimefacesContextUI.ejecutar("PF('validacionAlias').show()");
                    }
                }
                index = -1;
                secRegistro = null;
            } else {
                if (!listaUsuariosCrear.contains(filtrarUsuarios.get(indice))) {
                    if (filtrarUsuarios.get(indice).getAlias() == null || filtrarUsuarios.get(indice).getAlias().equals("")) {
                        System.out.println("entra2");
                        pasa++;
                    }
                    if (pasa == 0) {

                        if (listaUsuariosCrear.isEmpty()) {
                            listaUsuariosCrear.add(filtrarUsuarios.get(indice));
                        } else if (!listaUsuariosCrear.contains(filtrarUsuarios.get(indice))) {
                            listaUsuariosCrear.add(filtrarUsuarios.get(indice));
                        }
                        if (guardado == true) {
                            guardado = false;
                            PrimefacesContextUI.actualizar("form:ACEPTAR");
                        } else if (pasa > 1) {
                            filtrarUsuarios.get(indice).setAlias(alisin);
                            PrimefacesContextUI.actualizar("formularioDialogos:validacionAlias");
                            PrimefacesContextUI.ejecutar("PF('validacionAlias').show()");
                        }
                    }
                }
                index = -1;
                secRegistro = null;
            }
            PrimefacesContextUI.actualizar("form:datosUsuarios");

        } else if (confirmarCambio.equalsIgnoreCase("PERSONAS")) {
            System.out.println("si está entrando personas");
            if (tipoLista == 0) {
                listaUsuarios.get(indice).getPersona().setNombreCompleto(persona);
                System.out.println("persona antes de colocar vacio: " + persona);
            } else {
                filtrarUsuarios.get(indice).getPersona().setNombreCompleto(persona);
            }
            for (int i = 0; i < lovPersonas.size(); i++) {
                if (lovPersonas.get(i).getNombreCompleto().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoLista == 0) {
                    listaUsuarios.get(indice).setPersona(lovPersonas.get(indiceUnicoElemento));
                } else {
                    filtrarUsuarios.get(indice).setPersona(lovPersonas.get(indiceUnicoElemento));
                }
                lovPersonas.clear();
                getLovPersonas();
            } else {
                permitirIndex = false;
                PrimefacesContextUI.actualizar("formularioDialogos:personasDialogo");
                PrimefacesContextUI.ejecutar("PF('personasDialogo').show()");
                tipoActualizacion = 0;
            }
        } else if (confirmarCambio.equalsIgnoreCase("PERFILES")) {
            System.out.println("si está entrando perfiles");
            if (tipoLista == 0) {
                listaUsuarios.get(indice).getPerfil().setDescripcion(perfil);
                System.out.println("perfil antes de colocar vacio: " + perfil);
            } else {
                filtrarUsuarios.get(indice).getPerfil().setDescripcion(perfil);
            }
            for (int i = 0; i < lovPerfiles.size(); i++) {
                if (lovPerfiles.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoLista == 0) {
                    listaUsuarios.get(indice).setPerfil(lovPerfiles.get(indiceUnicoElemento));
                } else {
                    filtrarUsuarios.get(indice).setPerfil(lovPerfiles.get(indiceUnicoElemento));
                }
                lovPersonas.clear();
                getLovPersonas();
            } else {
                permitirIndex = false;
                PrimefacesContextUI.actualizar("formularioDialogos:perfilesDialogo");
                PrimefacesContextUI.ejecutar("PF('perfilesDialogo').show()");
                tipoActualizacion = 0;
            }
        } else if (confirmarCambio.equalsIgnoreCase("PANTALLAS")) {
            System.out.println("si està entrando pantallas");
            if (tipoLista == 0) {
                listaUsuarios.get(indice).getPantallainicio().setNombre(pantalla);
                System.out.println("pantalla antes de colocar vacio: " + pantalla);
            } else {
                filtrarUsuarios.get(indice).getPantallainicio().setNombre(pantalla);
            }
            for (int i = 0; i < lovPantallas.size(); i++) {
                if (lovPantallas.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoLista == 0) {
                    listaUsuarios.get(indice).setPantallainicio(lovPantallas.get(indiceUnicoElemento));
                } else {
                    filtrarUsuarios.get(indice).setPantallainicio(lovPantallas.get(indiceUnicoElemento));
                }
                lovPantallas.clear();
                getLovPantallas();
            } else {
                permitirIndex = false;
                PrimefacesContextUI.actualizar("formularioDialogos:pantallasDialogo");
                PrimefacesContextUI.ejecutar("PF('pantallasDialogo').show()");
                tipoActualizacion = 0;
            }
        }
        if (coincidencias == 1) {
            if (tipoLista == 0) {
                if (!listaUsuariosCrear.contains(listaUsuarios.get(indice))) {
                    if (listaUsuariosModificar.isEmpty()) {
                        listaUsuariosModificar.add(listaUsuarios.get(indice));
                    } else if (!listaUsuariosModificar.contains(listaUsuarios.get(indice))) {
                        listaUsuariosModificar.add(listaUsuarios.get(indice));
                    }
                    if (guardado == true) {
                        guardado = false;
                        PrimefacesContextUI.actualizar("form:ACEPTAR");

                    }
                }
                index = -1;
                secRegistro = null;
            } else {
                if (!listaUsuariosCrear.contains(filtrarUsuarios.get(indice))) {

                    if (listaUsuariosModificar.isEmpty()) {
                        listaUsuariosModificar.add(filtrarUsuarios.get(indice));
                    } else if (!listaUsuariosModificar.contains(filtrarUsuarios.get(indice))) {
                        listaUsuariosModificar.add(filtrarUsuarios.get(indice));
                    }
                    if (guardado == true) {
                        guardado = false;
                        PrimefacesContextUI.actualizar("form:ACEPTAR");

                    }
                }
                index = -1;
                secRegistro = null;
            }
        }
        PrimefacesContextUI.actualizar("form:datosUsuarios");
    }

    //EXPORTAR
    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosUsuariosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "UsuariosPDF", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosUsuariosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "UsuariosXLS", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    //LIMPIAR NUEVO REGISTRO USUARIO
    public void limpiarNuevaUsuario() {
        nuevaUsuarios = new Usuarios();
        nuevaUsuarios.setPersona(new Personas());
        nuevaUsuarios.getPersona().setNombreCompleto(" ");
        nuevaUsuarios.setPerfil(new Perfiles());
        nuevaUsuarios.getPerfil().setDescripcion(" ");
        nuevaUsuarios.setPantallainicio(new Pantallas());
        nuevaUsuarios.getPantallainicio().setNombre(" ");
        index = -1;
        secRegistro = null;
    }

    //LIMPIAR DUPLICAR
    public void limpiarDuplicarUsuario() {
        duplicarUsuarios = new Usuarios();
    }

    //GUARDAR
    public void guardarCambiosUsuario() {
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            if (guardado == false) {
                System.out.println("Realizando Operaciones Usuarios");
                if (!listaUsuariosBorrar.isEmpty()) {
                    administrarUsuario.borrarUsuarios(listaUsuariosBorrar);
                    System.out.println("Entra");
                    listaUsuariosBorrar.clear();
                }
                if (!listaUsuariosCrear.isEmpty()) {
                    administrarUsuario.crearUsuarios(listaUsuariosCrear);
                    listaUsuariosCrear.clear();
                }
                if (!listaUsuariosModificar.isEmpty()) {
                    administrarUsuario.modificarUsuarios(listaUsuariosModificar);
                    listaUsuariosModificar.clear();
                }
                System.out.println("Se guardaron los datos con exito");
                listaUsuarios = null;
                getListaUsuarios();
                if (listaUsuarios != null && !listaUsuarios.isEmpty()) {
                    usuariosSeleccionado = listaUsuarios.get(0);
                    infoRegistro = "Cantidad de registros: " + listaUsuarios.size();
                } else {
                    infoRegistro = "Cantidad de registros: 0";
                }
                PrimefacesContextUI.actualizar("form:informacionRegistro");
                PrimefacesContextUI.actualizar("form:datosUsuarios");
                guardado = true;
                permitirIndex = true;
                FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                PrimefacesContextUI.actualizar("form:growl");
                PrimefacesContextUI.actualizar("form:ACEPTAR");
                k = 0;
                index = -1;
                secRegistro = null;
            }
        } catch (Exception e) {
            FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado, intente nuevamente.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            PrimefacesContextUI.actualizar("form:growl");
        }
    }

    //BORRAR USUARIO 
    public void borrarUsuario() {

        if (index >= 0) {
            if (tipoLista == 0) {
                if (!listaUsuariosModificar.isEmpty() && listaUsuariosModificar.contains(listaUsuarios.get(index))) {
                    int modIndex = listaUsuariosModificar.indexOf(listaUsuarios.get(index));
                    listaUsuariosModificar.remove(modIndex);
                    listaUsuariosBorrar.add(listaUsuarios.get(index));
                } else if (!listaUsuariosCrear.isEmpty() && listaUsuariosCrear.contains(listaUsuarios.get(index))) {
                    int crearIndex = listaUsuariosCrear.indexOf(listaUsuarios.get(index));
                    listaUsuariosCrear.remove(crearIndex);
                } else {
                    listaUsuariosBorrar.add(listaUsuarios.get(index));
                }
                listaUsuarios.remove(index);
                infoRegistro = "Cantidad de registros: " + listaUsuarios.size();
            }

            if (tipoLista == 1) {
                if (!listaUsuariosModificar.isEmpty() && listaUsuariosModificar.contains(filtrarUsuarios.get(index))) {
                    int modIndex = listaUsuariosModificar.indexOf(filtrarUsuarios.get(index));
                    listaUsuariosModificar.remove(modIndex);
                    listaUsuariosBorrar.add(filtrarUsuarios.get(index));
                } else if (!listaUsuariosCrear.isEmpty() && listaUsuariosCrear.contains(filtrarUsuarios.get(index))) {
                    int crearIndex = listaUsuariosCrear.indexOf(filtrarUsuarios.get(index));
                    listaUsuariosCrear.remove(crearIndex);
                } else {
                    listaUsuariosBorrar.add(filtrarUsuarios.get(index));
                }
                int CIndex = listaUsuarios.indexOf(filtrarUsuarios.get(index));
                listaUsuarios.remove(CIndex);
                filtrarUsuarios.remove(index);
                infoRegistro = "Cantidad de registros: " + filtrarUsuarios.size();
            }

            RequestContext context = RequestContext.getCurrentInstance();
            PrimefacesContextUI.actualizar("form:datosUsuarios");
            PrimefacesContextUI.actualizar("form:informacionRegistro");
            index = -1;
            secRegistro = null;

            if (guardado == true) {
                guardado = false;
                PrimefacesContextUI.actualizar("form:ACEPTAR");
            }
        }
    }

    //AUTOCOMPLETAR LISTAS DE VALORES PERSONAS
    public void valoresBackupAutocompletarPersonas(int tipoNuevo) {
        if (tipoNuevo == 1) {
            persona = nuevaUsuarios.getPersona().getNombreCompleto();
        } else if (tipoNuevo == 2) {
            persona = duplicarUsuarios.getPersona().getNombreCompleto();
        }
    }

    public void llamarLovPersonas(int tipoN) {
        if (tipoN == 1) {
            tipoActualizacion = 1;
        } else if (tipoN == 2) {
            tipoActualizacion = 2;
        }
        RequestContext context = RequestContext.getCurrentInstance();
        PrimefacesContextUI.actualizar("formularioDialogos:personasDialogo");
        PrimefacesContextUI.ejecutar("PF('personasDialogo').show()");
    }

    //AUTOCOMPLETAR NUEVO Y DUPLICADO PERSONAS
    public void autocompletarNuevoyDuplicadoPersona(String valorConfirmar, int tipoNuevo) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoNuevo == 1) {
            nuevaUsuarios.getPersona().setNombreCompleto(persona);
        } else if (tipoNuevo == 2) {
            duplicarUsuarios.getPersona().setNombreCompleto(persona);
        }
        for (int i = 0; i < lovPersonas.size(); i++) {
            if (lovPersonas.get(i).getNombreCompleto().startsWith(valorConfirmar.toUpperCase())) {
                indiceUnicoElemento = i;
                coincidencias++;
            }
        }
        if (coincidencias == 1) {
            if (tipoNuevo == 1) {
                nuevaUsuarios.setPersona(lovPersonas.get(indiceUnicoElemento));
                PrimefacesContextUI.actualizar("formularioDialogos:nuevoPersona");
            } else if (tipoNuevo == 2) {
                duplicarUsuarios.setPersona(lovPersonas.get(indiceUnicoElemento));
                PrimefacesContextUI.actualizar("formularioDialogos:duplicarPersona");
            }
            lovPersonas.clear();
            getLovPersonas();
        } else {
            PrimefacesContextUI.actualizar("form:personasDialogo");
            PrimefacesContextUI.ejecutar("PF('personasDialogo').show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
                PrimefacesContextUI.actualizar("formularioDialogos:nuevoPersona");
            } else if (tipoNuevo == 2) {
                PrimefacesContextUI.actualizar("formularioDialogos:duplicarPersona");
            }
        }
    }

    //AUTOCOMPLETAR LISTAS DE VALORES PERFILES
    public void valoresBackupAutocompletarPerfiles(int tipoNuevo) {
        if (tipoNuevo == 1) {
            perfil = nuevaUsuarios.getPerfil().getDescripcion();
        } else if (tipoNuevo == 2) {
            perfil = duplicarUsuarios.getPerfil().getDescripcion();
        }
    }

    public void llamarLovPerfiles(int tipoN) {
        if (tipoN == 1) {
            tipoActualizacion = 1;
        } else if (tipoN == 2) {
            tipoActualizacion = 2;
        }
        RequestContext context = RequestContext.getCurrentInstance();
        PrimefacesContextUI.actualizar("formularioDialogos:perfilesDialogo");
        PrimefacesContextUI.ejecutar("PF('perfilesDialogo').show()");
    }

    //AUTOCOMPLETAR NUEVO Y DUPLICADO PERFILES
    public void autocompletarNuevoyDuplicadoPerfil(String valorConfirmar, int tipoNuevo) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoNuevo == 1) {
            nuevaUsuarios.getPerfil().setDescripcion(perfil);
        } else if (tipoNuevo == 2) {
            duplicarUsuarios.getPerfil().setDescripcion(perfil);
        }
        for (int i = 0; i < lovPerfiles.size(); i++) {
            if (lovPerfiles.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                indiceUnicoElemento = i;
                coincidencias++;
            }
        }
        if (coincidencias == 1) {
            if (tipoNuevo == 1) {
                nuevaUsuarios.setPerfil(lovPerfiles.get(indiceUnicoElemento));
                PrimefacesContextUI.actualizar("formularioDialogos:nuevoPerfil");
            } else if (tipoNuevo == 2) {
                duplicarUsuarios.setPerfil(lovPerfiles.get(indiceUnicoElemento));
                PrimefacesContextUI.actualizar("formularioDialogos:duplicarPerfil");
            }
            lovPerfiles.clear();
            getLovPerfiles();
        } else {
            PrimefacesContextUI.actualizar("form:perfilesDialogo");
            PrimefacesContextUI.ejecutar("PF('perfilesDialogo').show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
                PrimefacesContextUI.actualizar("formularioDialogos:nuevoPerfil");
            } else if (tipoNuevo == 2) {
                PrimefacesContextUI.actualizar("formularioDialogos:duplicarPerfil");
            }
        }
    }

    //AUTOCOMPLETAR LISTAS DE VALORES PANTALLAS
    public void valoresBackupAutocompletarPantallas(int tipoNuevo) {
        if (tipoNuevo == 1) {
            pantalla = nuevaUsuarios.getPantallainicio().getNombre();
        } else if (tipoNuevo == 2) {
            pantalla = duplicarUsuarios.getPantallainicio().getNombre();
        }
    }

    public void llamarLovPantallas(int tipoN) {
        if (tipoN == 1) {
            tipoActualizacion = 1;
        } else if (tipoN == 2) {
            tipoActualizacion = 2;
        }
        RequestContext context = RequestContext.getCurrentInstance();
        PrimefacesContextUI.actualizar("formularioDialogos:pantallasDialogo");
        PrimefacesContextUI.ejecutar("PF('pantallasDialogo').show()");
    }

    //AUTOCOMPLETAR NUEVO Y DUPLICADO PANTALLAS
    public void autocompletarNuevoyDuplicadoPantalla(String valorConfirmar, int tipoNuevo) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoNuevo == 1) {
            nuevaUsuarios.getPantallainicio().setNombre(pantalla);
        } else if (tipoNuevo == 2) {
            duplicarUsuarios.getPantallainicio().setNombre(pantalla);
        }
        for (int i = 0; i < lovPantallas.size(); i++) {
            if (lovPantallas.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                indiceUnicoElemento = i;
                coincidencias++;
            }
        }
        if (coincidencias == 1) {
            if (tipoNuevo == 1) {
                nuevaUsuarios.setPantallainicio(lovPantallas.get(indiceUnicoElemento));
                PrimefacesContextUI.actualizar("formularioDialogos:nuevoPantalla");
            } else if (tipoNuevo == 2) {
                duplicarUsuarios.setPantallainicio(lovPantallas.get(indiceUnicoElemento));
                PrimefacesContextUI.actualizar("formularioDialogos:duplicarPantalla");
            }
            lovPerfiles.clear();
            getLovPerfiles();
        } else {
            PrimefacesContextUI.actualizar("form:pantallasDialogo");
            PrimefacesContextUI.ejecutar("PF('pantallasDialogo').show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
                PrimefacesContextUI.actualizar("formularioDialogos:nuevoPantalla");
            } else if (tipoNuevo == 2) {
                PrimefacesContextUI.actualizar("formularioDialogos:duplicarPantalla");
            }
        }
    }

    // Agregar Nueva Usuario
    public void agregarNuevaUsuario() {

        RequestContext context = RequestContext.getCurrentInstance();
        int pasa = 0;
        int pasas = 0;
        mensajeValidacion = " ";

        if (nuevaUsuarios.getAlias() == null) {
            System.out.println("entra2");
            mensajeValidacion = mensajeValidacion + "   * Alias\n";
            pasa++;
        }
        if (nuevaUsuarios.getPersona().getNombreCompleto() == null || nuevaUsuarios.getPersona().getNombreCompleto().equals("")) {
            System.out.println("entra3");
            mensajeValidacion = mensajeValidacion + "   * persona\n";
            pasa++;
        }
        if (nuevaUsuarios.getPerfil().getDescripcion() == null || nuevaUsuarios.getPerfil().getDescripcion().equals("")) {
            System.out.println("entra4");
            mensajeValidacion = mensajeValidacion + "   * perfil\n";
            pasa++;
        }
        if (nuevaUsuarios.getPantallainicio().getNombre() == null || nuevaUsuarios.getPantallainicio().getNombre().equals("")) {
            System.out.println("entra5");
            mensajeValidacion = mensajeValidacion + "   * pantalla\n";
            pasa++;
        }
        if (nuevaUsuarios.getPersona().getNombreCompleto() != null) {
            System.out.println("entra1");
            for (int i = 0; i < listaUsuarios.size(); i++) {
                // if (listaUsuarios.get(i).getPersona().getNombreCompleto() != null){
                if (nuevaUsuarios.getPersona().getNombreCompleto().equals(listaUsuarios.get(i).getPersona().getNombreCompleto())) {
                    pasas++;
                    System.out.println("i= " + i);
                    PrimefacesContextUI.actualizar("formularioDialogos:validacionPersona");
                    PrimefacesContextUI.ejecutar("PF('validacionPersona').show()");
                }

            }
        }
        if (pasa == 0 && pasas == 0) {
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();

                System.out.println("Desactivar");
                System.out.println("TipoLista= " + tipoLista);
                usuarioPersona = (Column) c.getViewRoot().findComponent("form:datosUsuarios:persona");
                usuarioPersona.setFilterStyle("display: none; visibility: hidden;");
                usuarioPerfil = (Column) c.getViewRoot().findComponent("form:datosUsuarios:perfil");
                usuarioPerfil.setFilterStyle("display: none; visibility: hidden;");
                usuarioAlias = (Column) c.getViewRoot().findComponent("form:datosUsuarios:alias");
                usuarioAlias.setFilterStyle("display: none; visibility: hidden;");
                usuarioPantallaInicio = (Column) c.getViewRoot().findComponent("form:datosUsuarios:pantallainicio");
                usuarioPantallaInicio.setFilterStyle("display: none; visibility: hidden;");
                altoTabla = "115";
                PrimefacesContextUI.actualizar("form:datosUsuarios");
                bandera = 0;
                filtrarUsuarios = null;
                tipoLista = 0;

            }
            //AGREGAR REGISTRO A LA LISTA USUARIOS
            k++;
            l = BigInteger.valueOf(k);
            nuevaUsuarios.setSecuencia(l);

            if (nuevaUsuarios.isEstadoActivo() == true) {
                nuevaUsuarios.setActivo("S");
            } else if (nuevaUsuarios.isEstadoActivo() == false) {
                nuevaUsuarios.setActivo("N");
            }

            listaUsuariosCrear.add(nuevaUsuarios);
            listaUsuarios.add(nuevaUsuarios);
            infoRegistro = "Cantidad de registros: " + listaUsuarios.size();
            PrimefacesContextUI.actualizar("form:infoRegistro");
            nuevaUsuarios = new Usuarios();
            PrimefacesContextUI.actualizar("form:datosUsuarios");
            if (guardado == true) {
                guardado = false;
                PrimefacesContextUI.actualizar("form:ACEPTAR");
            }
            System.out.println("SE ESTÀ CERRANDO? YA VEREMOS");
            PrimefacesContextUI.actualizar("formularioDialogos:NuevoRegistroUsuario");
            PrimefacesContextUI.ejecutar("PF('NuevoRegistroUsuario').hide()");
            index = -1;
            secRegistro = null;
        } else if (pasa > 0) {
            PrimefacesContextUI.actualizar("formularioDialogos:validacionNuevaUsuario");
            PrimefacesContextUI.ejecutar("PF('validacionNuevaUsuario').show()");
        }
    }

    public void duplicarUsuario() {
        if (index >= 0) {
            duplicarUsuarios = new Usuarios();

            if (tipoLista == 0) {
                duplicarUsuarios.setPersona(listaUsuarios.get(index).getPersona());
                duplicarUsuarios.setPerfil(listaUsuarios.get(index).getPerfil());
                duplicarUsuarios.setAlias(listaUsuarios.get(index).getAlias());
                duplicarUsuarios.setPantallainicio(listaUsuarios.get(index).getPantallainicio());
                duplicarUsuarios.setActivo(listaUsuarios.get(index).getActivo());
            }
            if (tipoLista == 1) {
                duplicarUsuarios.setPersona(filtrarUsuarios.get(index).getPersona());
                duplicarUsuarios.setPerfil(filtrarUsuarios.get(index).getPerfil());
                duplicarUsuarios.setAlias(filtrarUsuarios.get(index).getAlias());
                duplicarUsuarios.setPantallainicio(filtrarUsuarios.get(index).getPantallainicio());
                duplicarUsuarios.setActivo(filtrarUsuarios.get(index).getActivo());
            }

            RequestContext context = RequestContext.getCurrentInstance();
            PrimefacesContextUI.actualizar("formularioDialogos:duplicarUsuario");
            PrimefacesContextUI.ejecutar("PF('DuplicarRegistroUsuario').show()");
            index = -1;
            secRegistro = null;
        }
    }

    public void confirmarDuplicar() {

        //int pasaA = 0;
        int pasa = 0;
        int pasas = 0;
        k++;
        l = BigInteger.valueOf(k);
        duplicarUsuarios.setSecuencia(l);
        RequestContext context = RequestContext.getCurrentInstance();

        if (duplicarUsuarios.getPersona().getNombreCompleto() != null) {
            System.out.println("entra1");
            for (int i = 0; i < listaUsuarios.size(); i++) {
                if (duplicarUsuarios.getPersona().getNombreCompleto() != null) {
                    if (duplicarUsuarios.getPersona().getNombreCompleto().equals(listaUsuarios.get(i).getPersona().getNombreCompleto())) {
                        pasas++;
                        PrimefacesContextUI.actualizar("formularioDialogos:validacionPersona");
                        PrimefacesContextUI.ejecutar("PF('validacionPersona').show()");
                    }
                }
            }
        }
        if (duplicarUsuarios.getAlias() == null) {
            System.out.println("entra2");
            mensajeValidacion = mensajeValidacion + "   * Alias\n";
            pasa++;
        }
        if (duplicarUsuarios.getPersona().getNombreCompleto() == null || duplicarUsuarios.getPersona().getNombreCompleto().equals("")) {
            System.out.println("entra3");
            mensajeValidacion = mensajeValidacion + "   * persona\n";
            pasa++;
        }
        if (duplicarUsuarios.getPerfil().getDescripcion() == null || duplicarUsuarios.getPerfil().getDescripcion().equals("")) {
            System.out.println("entra4");
            mensajeValidacion = mensajeValidacion + "   * perfil\n";
            pasa++;
        }
        if (duplicarUsuarios.getPantallainicio().getNombre() == null || duplicarUsuarios.getPantallainicio().getNombre().equals("")) {
            System.out.println("entra5");
            mensajeValidacion = mensajeValidacion + "   * pantalla\n";
            pasa++;
        }

        if (pasa == 0 && pasas == 1) {
            index = -1;
            secRegistro = null;
            if (guardado == true) {
                guardado = false;
                PrimefacesContextUI.actualizar("form:ACEPTAR");
            }
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                System.out.println("Desactivar");
                usuarioPersona = (Column) c.getViewRoot().findComponent("form:datosUsuarios:persona");
                usuarioPersona.setFilterStyle("display: none; visibility: hidden;");
                usuarioPerfil = (Column) c.getViewRoot().findComponent("form:datosUsuarios:perfil");
                usuarioPerfil.setFilterStyle("display: none; visibility: hidden;");
                usuarioAlias = (Column) c.getViewRoot().findComponent("form:datosUsuarios:alias");
                usuarioAlias.setFilterStyle("display: none; visibility: hidden;");
                usuarioPantallaInicio = (Column) c.getViewRoot().findComponent("form:datosUsuarios:pantallainicio");
                usuarioPantallaInicio.setFilterStyle("display: none; visibility: hidden;");
                PrimefacesContextUI.actualizar("form:datosUsuarios");
                altoTabla = "270";
                bandera = 0;
                filtrarUsuarios = null;
                System.out.println("TipoLista= " + tipoLista);
                tipoLista = 0;
            }

            listaUsuarios.add(duplicarUsuarios);
            listaUsuariosCrear.add(duplicarUsuarios);
            PrimefacesContextUI.actualizar("form:datosUsuarios");
            duplicarUsuarios = new Usuarios();
            infoRegistro = "Cantidad de registros: " + listaUsuarios.size();
            PrimefacesContextUI.actualizar("form:informacionRegistro");

            PrimefacesContextUI.actualizar("formularioDialogos:duplicarUsuario");
            PrimefacesContextUI.ejecutar("PF('DuplicarRegistroUsuario').hide()");

        } else if (pasa > 0) {
            PrimefacesContextUI.actualizar("formularioDialogos:validacionNuevaUsuario");
            PrimefacesContextUI.ejecutar("PF('validacionNuevaUsuario').show()");
        }

    }

    //VERIFICAR RASTRO
    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        System.out.println("lol");
        if (!listaUsuarios.isEmpty()) {
            if (secRegistro != null) {
                System.out.println("lol 2");
                int resultado = administrarRastros.obtenerTabla(secRegistro, "USUARIOS");
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
            if (administrarRastros.verificarHistoricosTabla("USUARIOS")) {
                PrimefacesContextUI.ejecutar("PF('confirmarRastroHistorico').show()");
            } else {
                PrimefacesContextUI.ejecutar("PF('errorRastroHistorico').show()");
            }
        }
        index = -1;
    }

    //REFRESCAR LA PAGINA, CANCELAR MODIFICACION SI NO SE A GUARDADO
    public void cancelarModificacion() {
        if (bandera == 1) {
            //CERRAR FILTRADO
            FacesContext c = FacesContext.getCurrentInstance();
            System.out.println("Desactivar");
            usuarioPersona = (Column) c.getViewRoot().findComponent("form:datosUsuarios:persona");
            usuarioPersona.setFilterStyle("display: none; visibility: hidden;");
            usuarioPerfil = (Column) c.getViewRoot().findComponent("form:datosUsuarios:perfil");
            usuarioPerfil.setFilterStyle("display: none; visibility: hidden;");
            usuarioAlias = (Column) c.getViewRoot().findComponent("form:datosUsuarios:alias");
            usuarioAlias.setFilterStyle("display: none; visibility: hidden;");
            usuarioPantallaInicio = (Column) c.getViewRoot().findComponent("form:datosUsuarios:pantallainicio");
            usuarioPantallaInicio.setFilterStyle("display: none; visibility: hidden;");
            usuarioActivo = (Column) c.getViewRoot().findComponent("form:datosUsuarios:activo");
            usuarioActivo.setFilterStyle("display: none; visibility: hidden;");
            PrimefacesContextUI.actualizar("form:datosUsuarios");
            altoTabla = "270";
            bandera = 0;
            filtrarUsuarios = null;
            tipoLista = 0;
            System.out.println("TipoLista= " + tipoLista);
        }
        listaUsuariosBorrar.clear();
        listaUsuariosCrear.clear();
        listaUsuariosModificar.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listaUsuarios = null;
        lovUsuarioAlias = new ArrayList<Usuarios>();
        auxClon = "";

        getListaUsuarios();
        if (listaUsuarios != null && !listaUsuarios.isEmpty()) {
            usuariosSeleccionado = listaUsuarios.get(0);
            infoRegistro = "Cantidad de registros: " + listaUsuarios.size();
        } else {
            infoRegistro = "Cantidad de registros: 0";
        }
        guardado = true;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        PrimefacesContextUI.actualizar("form:datosUsuarios");
        PrimefacesContextUI.actualizar("form:informacionRegistro");
        PrimefacesContextUI.actualizar("form:aliasNombreClon");
        PrimefacesContextUI.ejecutar("PF('aliasNombreClon').show()");
    }

    //MÉTODO SALIR DE LA PAGINA ACTUAL
    public void salir() {
        if (bandera == 1) {
            //CERRAR FILTRADO
            FacesContext c = FacesContext.getCurrentInstance();
            System.out.println("Desactivar");
            usuarioPersona = (Column) c.getViewRoot().findComponent("form:datosUsuarios:persona");
            usuarioPersona.setFilterStyle("display: none; visibility: hidden;");
            usuarioPerfil = (Column) c.getViewRoot().findComponent("form:datosUsuarios:perfil");
            usuarioPerfil.setFilterStyle("display: none; visibility: hidden;");
            usuarioAlias = (Column) c.getViewRoot().findComponent("form:datosUsuarios:alias");
            usuarioAlias.setFilterStyle("display: none; visibility: hidden;");
            usuarioPantallaInicio = (Column) c.getViewRoot().findComponent("form:datosUsuarios:pantallainicio");
            usuarioPantallaInicio.setFilterStyle("display: none; visibility: hidden;");
            usuarioActivo = (Column) c.getViewRoot().findComponent("form:datosUsuarios:activo");
            usuarioActivo.setFilterStyle("display: none; visibility: hidden;");
            PrimefacesContextUI.actualizar("form:datosUsuarios");
            altoTabla = "270";
            bandera = 0;
            filtrarUsuarios = null;
            tipoLista = 0;
            System.out.println("TipoLista= " + tipoLista);
        }
        listaUsuariosBorrar.clear();
        listaUsuariosCrear.clear();
        listaUsuariosModificar.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listaUsuarios = null;
        auxClon = "";
        guardado = true;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        PrimefacesContextUI.actualizar("form:datosUsuarios");
        PrimefacesContextUI.actualizar("form:informacionRegistro");
        PrimefacesContextUI.actualizar("form:aliasNombreClon");
        PrimefacesContextUI.ejecutar("PF('aliasNombreClon').show()");
    }

    public void crearUsuario() {
        RequestContext context = RequestContext.getCurrentInstance();
        String mensaje = "";
        String mensaje2 = "";
        Integer exeC = null;
        Integer exeC2 = null;
        if (index >= 0) {
            if (tipoLista == 0) {
                System.out.println("alias: " + usuariosSeleccionado.getAlias());
                System.out.println("perfil1: " + usuariosSeleccionado.getPerfil().getDescripcion());
                System.out.println("perfil2: " + usuariosSeleccionado.getPerfil());
                exeC = administrarUsuario.crearUsuariosBD(usuariosSeleccionado.getAlias());
                exeC2 = administrarUsuario.CrearUsuarioPerfilBD(usuariosSeleccionado.getAlias(), usuariosSeleccionado.getPerfil().getDescripcion());

                if (exeC != null) {
                    mensaje = "Creando el nuevo Usuario...";
                    FacesMessage msg = new FacesMessage("Información", mensaje);
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                    PrimefacesContextUI.actualizar("form:growl");
                    exeC = null;
                    if (exeC2 != null) {
                        mensaje2 = "Creando el nuevo Perfil...";
                        FacesMessage msg2 = new FacesMessage("Información", mensaje);
                        FacesContext.getCurrentInstance().addMessage(null, msg);
                        PrimefacesContextUI.actualizar("form:growl");
                        exeC2 = null;
                    } else {
                        mensaje2 = "Excepción no tratada... "
                                + "Error al crear el Perfil";
                        FacesMessage msg2 = new FacesMessage("Información", mensaje);
                        FacesContext.getCurrentInstance().addMessage(null, msg);
                        PrimefacesContextUI.actualizar("form:growl");
                    }
                } else {
                    mensaje = "Excepción no tratada... "
                            + "Error al crear el Usuario";
                    FacesMessage msg = new FacesMessage("Información", mensaje);
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                    PrimefacesContextUI.actualizar("form:growl");
                    if (exeC2 == null) {
                        mensaje2 = "Excepción no tratada... "
                                + "Error al crear el Perfil";
                        FacesMessage msg2 = new FacesMessage("Información", mensaje);
                        FacesContext.getCurrentInstance().addMessage(null, msg);
                        PrimefacesContextUI.actualizar("form:growl");
                    }
                }

            }
            if (tipoLista == 1) {
                exeC = administrarUsuario.crearUsuariosBD(usuariosSeleccionado.getAlias());
                exeC2 = administrarUsuario.CrearUsuarioPerfilBD(usuariosSeleccionado.getAlias(), usuariosSeleccionado.getPerfil().getDescripcion());

                if (exeC != null) {
                    mensaje = "Creando el nuevo Usuario...";
                    FacesMessage msg = new FacesMessage("Información", mensaje);
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                    PrimefacesContextUI.actualizar("form:growl");
                    exeC = null;
                    if (exeC2 != null) {
                        mensaje2 = "Creando el nuevo Perfil...";
                        FacesMessage msg2 = new FacesMessage("Información", mensaje);
                        FacesContext.getCurrentInstance().addMessage(null, msg);
                        PrimefacesContextUI.actualizar("form:growl");
                        exeC2 = null;
                    } else {
                        mensaje2 = "Excepción no tratada... "
                                + "Error al crear el Perfil";
                        FacesMessage msg2 = new FacesMessage("Información", mensaje);
                        FacesContext.getCurrentInstance().addMessage(null, msg);
                        PrimefacesContextUI.actualizar("form:growl");
                    }
                } else {
                    mensaje = "Excepción no tratada... "
                            + "Error al crear el Usuario";
                    FacesMessage msg = new FacesMessage("Información", mensaje);
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                    PrimefacesContextUI.actualizar("form:growl");
                    if (exeC2 == null) {
                        mensaje2 = "Excepción no tratada... "
                                + "Error al crear el Perfil";
                        FacesMessage msg2 = new FacesMessage("Información", mensaje);
                        FacesContext.getCurrentInstance().addMessage(null, msg);
                        PrimefacesContextUI.actualizar("form:growl");
                        exeC2 = null;
                    }
                }
            }
            index = -1;
            secRegistro = null;
        }

    }

    public void eliminarUsuarioValidacion() {

        if (index >= 0) {
            if (tipoLista == 0) {
                eliminarUsuarios.setPersona(usuariosSeleccionado.getPersona());
                eliminarUsuarios.setPerfil(usuariosSeleccionado.getPerfil());
                eliminarUsuarios.setAlias(usuariosSeleccionado.getAlias());
                eliminarUsuarios.setPantallainicio(usuariosSeleccionado.getPantallainicio());
                eliminarUsuarios.setActivo(usuariosSeleccionado.getActivo());
                mensajeV = usuariosSeleccionado.getAlias();
            }
            if (tipoLista == 1) {
                eliminarUsuarios.setPersona(usuariosSeleccionado.getPersona());
                eliminarUsuarios.setPerfil(usuariosSeleccionado.getPerfil());
                eliminarUsuarios.setAlias(usuariosSeleccionado.getAlias());
                eliminarUsuarios.setPantallainicio(usuariosSeleccionado.getPantallainicio());
                eliminarUsuarios.setActivo(usuariosSeleccionado.getActivo());
                mensajeV = usuariosSeleccionado.getAlias();
            }
            RequestContext context = RequestContext.getCurrentInstance();
            System.out.println("entro aqui");
            PrimefacesContextUI.actualizar("formularioDialogos:validacionEliminar");
            PrimefacesContextUI.ejecutar("PF('validacionEliminar').show()");
            index = -1;
            secRegistro = null;
        }
    }

    public void eliminarUsuarioBD() {
        System.out.println("aksjdhaksjbdkas");
        RequestContext context = RequestContext.getCurrentInstance();
        String mensaje = "";
        String mensaje2 = "";
        Integer exeE = null;
        Integer exeE2 = null;
        /*eliminarUsuarios.getPersona().getNombreCompleto();
         eliminarUsuarios.getPerfil().getDescripcion();
         eliminarUsuarios.getAlias();
         eliminarUsuarios.getPantallainicio().getNombre();
         eliminarUsuarios.getActivo();*/
        System.out.println("alias: " + eliminarUsuarios.getAlias());
        exeE = administrarUsuario.eliminarUsuariosBD(eliminarUsuarios.getAlias());
        exeE2 = administrarUsuario.eliminarUsuarioTotalBD(eliminarUsuarios.getAlias());
        System.out.println("si está haciendo algo");        
        if (exeE != null) {
            mensaje = "Borrando el Usuario...";
            FacesMessage msg = new FacesMessage("Información", mensaje);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            PrimefacesContextUI.actualizar("form:growl");
            exeE = null;
        } else {
            mensaje = "Excepción no tratada... Error al borrar el Usuario";
            FacesMessage msg = new FacesMessage("Información", mensaje);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            PrimefacesContextUI.actualizar("form:growl");
        }
        if (exeE2 != null) {
            mensaje2 = "Usuario Borrado";
            FacesMessage msg = new FacesMessage("Información", mensaje);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            PrimefacesContextUI.actualizar("form:growl");
            exeE2 = null;
        } else {
            mensaje2 = "Excepción no tratada... "
                    + "No se puedo suprimir el registro maestro al existir registros detalle coincidentes";
            FacesMessage msg = new FacesMessage("Información", mensaje);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            PrimefacesContextUI.actualizar("form:growl");
        }

        index = -1;
        secRegistro = null;

    }

    public void asignarAliasClon() {
        auxClon = usuariosSeleccionado.getAlias();
        System.out.println("esto es: " + auxClon);
        System.out.println("estos es2: " + getAuxClon());
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:LOVUsuariosAlias:globalFilter");
        PrimefacesContextUI.ejecutar("PF('LOVUsuariosAlias').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('aliasDialogo').hide()");
        PrimefacesContextUI.actualizar("form:aliasNombreClon");
    }

    public void dfghjkl() {
        System.out.println("asgAJSG");
    }

    public void usuarioClonarBD() {
        Integer exeA = null;
        System.out.println("prueba 1 auxclon: " + getAuxClon());
        System.out.println("esto deberia cogerlo pero no: " + auxClon);
        //getAuxClon();
        System.out.println("En usaurio clonar auxclon es: " + auxClon);

        RequestContext context = RequestContext.getCurrentInstance();
        if (auxClon.equals("")) {

            PrimefacesContextUI.actualizar("formularioDialogos:validacionClon");
            PrimefacesContextUI.ejecutar("PF('validacionClon').show()");

        } else if (!auxClon.equals("")) {
            System.out.println("alias a clonar: " + auxClon);
            if (index >= 0) {
                if (tipoLista == 0) {
                    System.out.println("alias: " + usuariosSeleccionado.getAlias());
                    System.out.println("aliasclon: " + auxClon);
                    exeA = administrarUsuario.clonarUsuariosBD(usuariosSeleccionado.getAlias(), auxClon, usuariosSeleccionado.getSecuencia());
                    if (exeA != null) {
                        FacesMessage msg = new FacesMessage("Información", "Reportes Clonados");
                        FacesContext.getCurrentInstance().addMessage(null, msg);
                        PrimefacesContextUI.actualizar("form:growl");
                        exeA = null;
                    } else {
                        FacesMessage msg = new FacesMessage("Información", "Excepción no tratada");
                        FacesContext.getCurrentInstance().addMessage(null, msg);
                        PrimefacesContextUI.actualizar("form:growl");
                    }
                }
                if (tipoLista == 1) {
                    exeA = administrarUsuario.clonarUsuariosBD(usuariosSeleccionado.getAlias(), auxClon, usuariosSeleccionado.getSecuencia());
                    if (exeA != null) {
                        FacesMessage msg = new FacesMessage("Información", "Reportes Clonados");
                        FacesContext.getCurrentInstance().addMessage(null, msg);
                        PrimefacesContextUI.actualizar("form:growl");
                    } else {
                        FacesMessage msg = new FacesMessage("Información", "Excepción no tratada");
                        FacesContext.getCurrentInstance().addMessage(null, msg);
                        PrimefacesContextUI.actualizar("form:growl");
                    }
                }
                index = -1;
                secRegistro = null;

            }
        }
    }

    public void desbloquearUsuario() {
        Integer exeD = null;
        RequestContext context = RequestContext.getCurrentInstance();
        if (index >= 0) {
            if (tipoLista == 0) {
                System.out.println("alias para desbloquear: " + usuariosSeleccionado.getAlias());
                exeD = administrarUsuario.desbloquearUsuariosBD(usuariosSeleccionado.getAlias());
                if (exeD != null) {
                    FacesMessage msg = new FacesMessage("Información", "Usuario Desbloqueado");
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                    PrimefacesContextUI.actualizar("form:growl");
                    exeD = null;
                } else {
                    FacesMessage msg = new FacesMessage("Información", "Excepción no tratada");
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                    PrimefacesContextUI.actualizar("form:growl");
                }
            }
            if (tipoLista == 1) {
                exeD = administrarUsuario.desbloquearUsuariosBD(usuariosSeleccionado.getAlias());
                if (exeD != null) {
                    FacesMessage msg = new FacesMessage("Información", "Usuario Desbloqueado");
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                    PrimefacesContextUI.actualizar("form:growl");
                    exeD = null;
                } else {
                    FacesMessage msg = new FacesMessage("Información", "Excepción no tratada");
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                    PrimefacesContextUI.actualizar("form:growl");
                }
            }
            index = -1;
            secRegistro = null;

        }

    }

    public void resetearUsuario() {
        String fecha = "";
        String ayuda = "";
        Integer exeR = null;
        Calendar cal = Calendar.getInstance();
        if (cal.get(cal.MONTH) == 0){
            ayuda = "01";
        }
        if (cal.get(cal.MONTH) == 1){
            ayuda = "02";
        }
        if (cal.get(cal.MONTH) == 2){
            ayuda = "03";
        }
        if (cal.get(cal.MONTH) == 3){
            ayuda = "04";
        }
        if (cal.get(cal.MONTH) == 4){
            ayuda = "05";
        }
        if (cal.get(cal.MONTH) == 5){
            ayuda = "06";
        }
        if (cal.get(cal.MONTH) == 6){
            ayuda = "07";
        }
        if (cal.get(cal.MONTH) == 7){
            ayuda = "08";
        }
        if (cal.get(cal.MONTH) == 8){
            ayuda = "09";
        }
        if (cal.get(cal.MONTH) == 9){
            ayuda = "10";
        }
        if (cal.get(cal.MONTH) == 10){
            ayuda = "11";
        }
        if (cal.get(cal.MONTH) == 11){
            ayuda = "12";
        }
        RequestContext context = RequestContext.getCurrentInstance();
        if (cal.get(cal.DATE) < 10) {
            fecha = "0" + cal.get(cal.DATE) + ayuda + cal.get(cal.HOUR_OF_DAY) + cal.get(cal.MINUTE);
            System.out.println("esta es la fecha de hoy1: " + fecha);            
        } else if (cal.get(cal.DATE) > 10) {
            fecha = cal.get(cal.DATE) + ayuda + cal.get(cal.HOUR_OF_DAY) + cal.get(cal.MINUTE);
            System.out.println("esta es la fecha de hoy2: " + fecha);
        } 
        if (index >= 0) {
            if (tipoLista == 0) {
                System.out.println("alias para desbloquear: " + usuariosSeleccionado.getAlias());
                System.out.println("esta es la fecha de hoy22222: " + fecha);
                exeR = administrarUsuario.restaurarUsuariosBD(usuariosSeleccionado.getAlias(), fecha);
                if (exeR != null) {
                    mensajeContra = usuariosSeleccionado.getAlias() + "_" + fecha;
                    PrimefacesContextUI.actualizar("formularioDialogos:contrasenaNueva");
                    PrimefacesContextUI.ejecutar("PF('contrasenaNueva').show()");
                    exeR = null;
                } else {
                    FacesMessage msg = new FacesMessage("Información", "Excepción no tratada");
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                    PrimefacesContextUI.actualizar("form:growl");
                }
            }
            if (tipoLista == 1) {
                exeR = administrarUsuario.restaurarUsuariosBD(usuariosSeleccionado.getAlias(), fecha);
                if (exeR != null) {
                    mensajeContra = usuariosSeleccionado.getAlias() + "_" + fecha;
                    PrimefacesContextUI.actualizar("formularioDialogos:contrasenaNueva");
                    PrimefacesContextUI.ejecutar("PF('contrasenaNueva').show()");
                    exeR = null;
                } else {
                    FacesMessage msg = new FacesMessage("Información", "Excepción no tratada");
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                    PrimefacesContextUI.actualizar("form:growl");
                }
            }
            index = -1;
            secRegistro = null;

        }

    }

    //GETTER AND SETTER
    public List<Usuarios> getListaUsuarios() {
        if (listaUsuarios == null) {
            listaUsuarios = administrarUsuario.consultarUsuarios();
        }

        return listaUsuarios;
    }

    public void setListaUsuarios(List<Usuarios> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
    }

    public List<Usuarios> getListaUsuariosCrear() {
        return listaUsuariosCrear;
    }

    public void setListaUsuariosCrear(List<Usuarios> listaUsuariosCrear) {
        this.listaUsuariosCrear = listaUsuariosCrear;
    }

    public List<Usuarios> getListaUsuariosModificar() {
        return listaUsuariosModificar;
    }

    public void setListaUsuariosModificar(List<Usuarios> listaUsuariosModificar) {
        this.listaUsuariosModificar = listaUsuariosModificar;
    }

    public List<Usuarios> getListaUsuariosBorrar() {
        return listaUsuariosBorrar;
    }

    public void setListaUsuariosBorrar(List<Usuarios> listaUsuariosBorrar) {
        this.listaUsuariosBorrar = listaUsuariosBorrar;
    }

    public List<Personas> getLovPersonas() {
        lovPersonas = administrarUsuario.consultarPersonas();
        RequestContext context = RequestContext.getCurrentInstance();

        if (lovPersonas == null || lovPersonas.isEmpty()) {
            infoRegistroPersonas = "Cantidad de registros: 0 ";
        } else {
            infoRegistroPersonas = "Cantidad de registros: " + lovPersonas.size();
        }
        PrimefacesContextUI.actualizar("formularioDialogos:infoRegistroPersonas");
        return lovPersonas;
    }

    public void setLovPersonas(List<Personas> lovPersonas) {
        this.lovPersonas = lovPersonas;
    }

    public List<Personas> getLovFiltradoPersonas() {
        return lovFiltradoPersonas;
    }

    public void setLovFiltradoPersonas(List<Personas> lovFiltradoPersonas) {
        this.lovFiltradoPersonas = lovFiltradoPersonas;
    }

    public List<Perfiles> getLovPerfiles() {
        lovPerfiles = administrarUsuario.consultarPerfiles();
        RequestContext context = RequestContext.getCurrentInstance();

        if (lovPerfiles == null || lovPerfiles.isEmpty()) {
            infoRegistroPerfiles = "Cantidad de registros: 0 ";
        } else {
            infoRegistroPerfiles = "Cantidad de registros: " + lovPerfiles.size();
        }
        PrimefacesContextUI.actualizar("formularioDialogos:infoRegistroPerfiles");
        return lovPerfiles;
    }

    public void setLovPerfiles(List<Perfiles> lovPerfiles) {
        this.lovPerfiles = lovPerfiles;
    }

    public List<Perfiles> getLovFiltradoPerfiles() {
        return lovFiltradoPerfiles;
    }

    public void setLovFiltradoPerfiles(List<Perfiles> lovFiltradoPerfiles) {
        this.lovFiltradoPerfiles = lovFiltradoPerfiles;
    }

    public List<Pantallas> getLovPantallas() {
        lovPantallas = administrarUsuario.consultarPantallas();
        RequestContext context = RequestContext.getCurrentInstance();

        if (lovPantallas == null || lovPantallas.isEmpty()) {
            infoRegistroPantallas = "Cantidad de registros: 0 ";
        } else {
            infoRegistroPantallas = "Cantidad de registros: " + lovPantallas.size();
        }
        PrimefacesContextUI.actualizar("formularioDialogos:infoRegistroPantallas");
        return lovPantallas;
    }

    public void setLovPantallas(List<Pantallas> lovPantallas) {
        this.lovPantallas = lovPantallas;
    }

    public List<Pantallas> getLovFiltradoPantallas() {
        return lovFiltradoPantallas;
    }

    public void setLovFiltradoPantallas(List<Pantallas> lovFiltradoPantallas) {
        this.lovFiltradoPantallas = lovFiltradoPantallas;
    }

    public List<Usuarios> getListaUsuariosClon() {
        return lovUsuarioAlias;
    }

    public void setListaUsuariosClon(List<Usuarios> listaUsuariosClon) {
        this.lovUsuarioAlias = listaUsuariosClon;
    }

    public List<Usuarios> getLovUsuarioAlias() {
        lovUsuarioAlias = administrarUsuario.consultarUsuarios();
        if (lovUsuarioAlias == null || lovUsuarioAlias.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + lovUsuarioAlias.size();
        }
        return lovUsuarioAlias;
    }

    public void setLovUsuarioAlias(List<Usuarios> lovUsuarioAlias) {
        this.lovUsuarioAlias = lovUsuarioAlias;
    }

    public List<Usuarios> getLovfiltrarUsuarioAlias() {
        return lovfiltrarUsuarioAlias;
    }

    public void setLovfiltrarUsuarioAlias(List<Usuarios> lovfiltrarUsuarioAlias) {
        this.lovfiltrarUsuarioAlias = lovfiltrarUsuarioAlias;
    }

    public String getAuxClon() {
        return auxClon;
    }

    public void setAuxClon(String auxClon) {
        this.auxClon = auxClon;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public void setAceptar(boolean aceptar) {
        this.aceptar = aceptar;
    }

    public boolean isGuardado() {
        return guardado;
    }

    public void setGuardado(boolean guardado) {
        this.guardado = guardado;
    }

    public List<Usuarios> getFiltrarUsuarios() {
        return filtrarUsuarios;
    }

    public void setFiltrarUsuarios(List<Usuarios> filtrarUsuarios) {
        this.filtrarUsuarios = filtrarUsuarios;
    }

    public Personas getPersonasSeleccionado() {
        return personasSeleccionado;
    }

    public void setPersonasSeleccionado(Personas personasSeleccionado) {
        this.personasSeleccionado = personasSeleccionado;
    }

    public Perfiles getPerfilesSeleccionado() {
        return perfilesSeleccionado;
    }

    public void setPerfilesSeleccionado(Perfiles perfilesSeleccionado) {
        this.perfilesSeleccionado = perfilesSeleccionado;
    }

    public Pantallas getPantallasSeleccionado() {
        return pantallasSeleccionado;
    }

    public void setPantallasSeleccionado(Pantallas pantallasSeleccionado) {
        this.pantallasSeleccionado = pantallasSeleccionado;
    }

    public Usuarios getNuevaUsuarios() {
        return nuevaUsuarios;
    }

    public void setNuevaUsuarios(Usuarios nuevaUsuarios) {
        this.nuevaUsuarios = nuevaUsuarios;
    }

    public Usuarios getDuplicarUsuarios() {
        return duplicarUsuarios;
    }

    public void setDuplicarUsuarios(Usuarios duplicarUsuarios) {
        this.duplicarUsuarios = duplicarUsuarios;
    }

    public Usuarios getEliminarUsuarios() {
        return eliminarUsuarios;
    }

    public void setEliminarUsuarios(Usuarios eliminarUsuarios) {
        this.eliminarUsuarios = eliminarUsuarios;
    }

    public Usuarios getClonarUsuario() {
        return clonarUsuarios;
    }

    public void setClonarUsuario(Usuarios clonarUsuario) {
        this.clonarUsuarios = clonarUsuario;
    }

    public Usuarios getUsuariosSeleccionado() {
        return usuariosSeleccionado;
    }

    public void setUsuariosSeleccionado(Usuarios usuariosSeleccionado) {
        this.usuariosSeleccionado = usuariosSeleccionado;
    }

    public Usuarios getClonarUsuarios() {
        return clonarUsuarios;
    }

    public void setClonarUsuarios(Usuarios clonarUsuarios) {
        this.clonarUsuarios = clonarUsuarios;
    }

    public String getAltoTabla() {
        return altoTabla;
    }

    public void setAltoTabla(String altoTabla) {
        this.altoTabla = altoTabla;
    }

    public String getInfoRegistroPersonas() {
        return infoRegistroPersonas;
    }

    public void setInfoRegistroPersonas(String infoRegistroPersonas) {
        this.infoRegistroPersonas = infoRegistroPersonas;
    }

    public String getInfoRegistroPerfiles() {
        return infoRegistroPerfiles;
    }

    public void setInfoRegistroPerfiles(String infoRegistroPerfiles) {
        this.infoRegistroPerfiles = infoRegistroPerfiles;
    }

    public String getInfoRegistroPantallas() {
        return infoRegistroPantallas;
    }

    public void setInfoRegistroPantallas(String infoRegistroPantallas) {
        this.infoRegistroPantallas = infoRegistroPantallas;
    }

    public String getTablaImprimir() {
        return tablaImprimir;
    }

    public void setTablaImprimir(String tablaImprimir) {
        this.tablaImprimir = tablaImprimir;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public String getInfoRegistro() {
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

    public String getPaginaAnterior() {
        return paginaAnterior;
    }

    public void setPaginaAnterior(String paginaAnterior) {
        this.paginaAnterior = paginaAnterior;
    }

    public String getMensajeValidacion() {
        return mensajeValidacion;
    }

    public void setMensajeValidacion(String mensajeValidacion) {
        this.mensajeValidacion = mensajeValidacion;
    }

    public String getMensajeV() {
        return mensajeV;
    }

    public void setMensajeV(String mensajeV) {
        this.mensajeV = mensajeV;
    }

    public String getMensajeContra() {
        return mensajeContra;
    }

    public void setMensajeContra(String mensajeContra) {
        this.mensajeContra = mensajeContra;
    }

    public Usuarios getEditarUsuarios() {
        return editarUsuarios;
    }

    public void setEditarUsuarios(Usuarios editarUsuarios) {
        this.editarUsuarios = editarUsuarios;
    }

    public BigInteger getSecRegistro() {
        return secRegistro;
    }

    public void setSecRegistro(BigInteger secRegistro) {
        this.secRegistro = secRegistro;
    }

}
