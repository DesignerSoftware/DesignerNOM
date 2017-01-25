package Controlador;


import Entidades.ActualUsuario;
import Entidades.Cuadrillas;
import Entidades.Empleados;
import Entidades.ParametrosTiempos;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarATParametroTiempoInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;import ControlNavegacion.ControlListaNavegacion;
import java.util.Map;
import java.util.LinkedHashMap;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.export.Exporter;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Administrador
 */
@ManagedBean
@SessionScoped
public class ControlATParametroTiempo implements Serializable {

    @EJB
    AdministrarATParametroTiempoInterface administrarATParametroTiempo;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    private ActualUsuario actualUsuario;
    private ParametrosTiempos parametroTiempoUsuarioBD;
    private List<ParametrosTiempos> listaParametrosTiemposExport;
    //
    private String auxParametroCuadrilla;
    private BigDecimal auxParametroEmplDesde, auxParametroEmplHasta;
    private Date auxParametroFechaDesde, auxParametroFechaHasta;
    private String auxParametroHoraDesde, auxParametroHoraHasta;
    private int posicionParametroTiempo;
    private boolean permitirIndex;
    private BigInteger secRegistro;
    private BigInteger backUpSecRegistro;
    private Date fechaParametro;

    private List<Cuadrillas> lovCuadrillas;
    private List<Cuadrillas> filtrarLovCuadrillas;
    private Cuadrillas cuadrillaSeleccionada;
    private String infoRegistroCuadrilla;

    private List<Empleados> lovEmpleados;
    private List<Empleados> filtrarLovEmpleados;
    private Empleados empleadoSeleccionado;
    private String infoRegistroEmpleado;

    private int tipoArchivoReporte;

    //
    private String paginaAnterior;
    private boolean aceptar, guardado;
    //
    private String mensajeBtnLiquidarTiempos;
    //
    private String fechaHastaTiempo, fechaDesdeTiempo;
    private String editarFechaHastaTiempo, editarFechaDesdeTiempo;

    public ControlATParametroTiempo() {
        aceptar = true;
        guardado = true;

        permitirIndex = true;
        posicionParametroTiempo = -1;
        tipoArchivoReporte = 1;
        secRegistro = null;

        parametroTiempoUsuarioBD = null;
        actualUsuario = null;

        lovCuadrillas = null;
        lovEmpleados = null;

        cuadrillaSeleccionada = new Cuadrillas();
        empleadoSeleccionado = new Empleados();

    }

       private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>(); mapParametros.put ("paginaAnterior", paginaAnterior);
   public void recibirPaginaEntrante(String pagina) {
      paginaAnterior = pagina;
      //inicializarCosas(); Inicializar cosas de ser necesario
   }

   public void recibirParametros(Map<String, Object> map) {
      mapParametros = map;
      paginaAnterior = (String) mapParametros.get("paginaAnterior");
      //inicializarCosas(); Inicializar cosas de ser necesario
   }
      
   //Reemplazar la funcion volverAtras, retornarPagina, Redirigir.....Atras.etc
    public void navegar(String pag) {
      FacesContext fc = FacesContext.getCurrentInstance();
      ControlListaNavegacion controlListaNavegacion = (ControlListaNavegacion) fc.getApplication().evaluateExpressionGet(fc, "#{controlListaNavegacion}", ControlListaNavegacion.class);
      if (pag.equals("atras")) {
         pag = paginaAnterior;
         paginaAnterior = "nominaf";
         controlListaNavegacion.quitarPagina();
      } else {
         String pagActual = "cargo"XXX;
        //Map<String, Object> mapParaEnviar = new LinkedHashMap<String, Object>();
         //mapParametros.put("paginaAnterior", pagActual);
         //mas Parametros
//         if (pag.equals("rastrotabla")) {
//           ControlRastro controlRastro = (ControlRastro) fc.getApplication().evaluateExpressionGet(fc, "#{controlRastro}", ControlRastro.class);
 //           controlRastro.recibirDatosTabla(conceptoSeleccionado.getSecuencia(), "Conceptos", pagActual);
   //      } else if (pag.equals("rastrotablaH")) {
     //       ControlRastro controlRastro = (ControlRastro) fc.getApplication().evaluateExpressionGet(fc, "#{controlRastro}", ControlRastro.class);
       //     controlRastro.historicosTabla("Conceptos", pagActual);
         //   pag = "rastrotabla";
   //}
         controlListaNavegacion.adicionarPagina(pagActual);
      }
      fc.getApplication().getNavigationHandler().handleNavigation(fc, null, pag);
    }

   @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarATParametroTiempo.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void recibirPaginaEntrante(String page) {
        paginaAnterior = page;
        actualUsuario = null;
        getActualUsuario();
        parametroTiempoUsuarioBD = null;
        getParametroTiempoUsuarioBD();
    }

    public String redirigir() {
        return paginaAnterior;
    }

    public void editarCelda() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (posicionParametroTiempo == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarParametroCuadrilla");
            RequestContext.getCurrentInstance().execute("PF('editarParametroCuadrilla').show()");
        }
        if (posicionParametroTiempo == 2) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarParametroFechaDesde");
            RequestContext.getCurrentInstance().execute("PF('editarParametroFechaDesde').show()");
        }
        if (posicionParametroTiempo == 3) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarParametroFechaHasta");
            RequestContext.getCurrentInstance().execute("PF('editarParametroFechaHasta').show()");
        }
        if (posicionParametroTiempo == 4) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarParametroEmplDesde");
            RequestContext.getCurrentInstance().execute("PF('editarParametroEmplDesde').show()");
        }
        if (posicionParametroTiempo == 5) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarParametroEmplHasta");
            RequestContext.getCurrentInstance().execute("PF('editarParametroEmplHasta').show()");
        }
        if (posicionParametroTiempo == 6) {
            editarFechaDesdeTiempo = fechaDesdeTiempo;
            RequestContext.getCurrentInstance().update("formularioDialogos:editarParametroHoraDesde");
            RequestContext.getCurrentInstance().execute("PF('editarParametroHoraDesde').show()");
        }
        if (posicionParametroTiempo == 7) {
            editarFechaHastaTiempo = fechaHastaTiempo;
            RequestContext.getCurrentInstance().update("formularioDialogos:editarParametroHoraHasta");
            RequestContext.getCurrentInstance().execute("PF('editarParametroHoraHasta').show()");
        }
        posicionParametroTiempo = -1;
    }

    public void guardarCambios() {
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            if (guardado == false) {
                administrarATParametroTiempo.modificarParametroTiempo(parametroTiempoUsuarioBD);
                cancelarModificaciones();
                FacesMessage msg = new FacesMessage("Información", "Se guardarón los datos con éxito");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                RequestContext.getCurrentInstance().update("form:growl");
            }
        } catch (Exception e) {
            System.out.println("Error guardarCambios Controlador : " + e.toString());
            FacesMessage msg = new FacesMessage("Información", "Ocurrio un error en el guardado, intente nuevamente");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }
    }

    public void cancelarModificaciones() {
        actualUsuario = null;
        getActualUsuario();
        parametroTiempoUsuarioBD = null;
        getParametroTiempoUsuarioBD();
        aceptar = true;
        guardado = true;
        permitirIndex = true;
        posicionParametroTiempo = -1;
        tipoArchivoReporte = 1;
        secRegistro = null;
        RequestContext.getCurrentInstance().update("form:PanelTotal");
    }

    public void cambiarIndiceParametro(int i) {
        if (permitirIndex == true) {
            posicionParametroTiempo = i;
            secRegistro = parametroTiempoUsuarioBD.getSecuencia();
            auxParametroCuadrilla = parametroTiempoUsuarioBD.getCuadrilla().getDescripcion();
            auxParametroEmplDesde = parametroTiempoUsuarioBD.getCodigoempleadodesde();
            auxParametroEmplHasta = parametroTiempoUsuarioBD.getCodigoempleadohasta();
            auxParametroFechaDesde = parametroTiempoUsuarioBD.getFechadesde();
            auxParametroFechaHasta = parametroTiempoUsuarioBD.getFechahasta();
            auxParametroHoraDesde = fechaDesdeTiempo;
            auxParametroHoraHasta = fechaHastaTiempo;
        }
    }

    public void asignarIndex(int indice, int dialogo) {
        posicionParametroTiempo = indice;
        RequestContext context = RequestContext.getCurrentInstance();
        if (dialogo == 0) {
            RequestContext.getCurrentInstance().update("formCuadrilla:CuadrillaDialogo");
            RequestContext.getCurrentInstance().execute("PF('CuadrillaDialogo').show()");
        }
        if (dialogo == 1) {
            RequestContext.getCurrentInstance().update("formEmpleado:EmpleadoDialogo");
            RequestContext.getCurrentInstance().execute("PF('EmpleadoDialogo').show()");
        }
        if (dialogo == 2) {
            RequestContext.getCurrentInstance().update("formEmpleado:EmpleadoDialogo");
            RequestContext.getCurrentInstance().execute("PF('EmpleadoDialogo').show()");
        }
    }

    public void listaValoresBoton() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (posicionParametroTiempo == 0) {
            RequestContext.getCurrentInstance().update("formCuadrilla:CuadrillaDialogo");
            RequestContext.getCurrentInstance().execute("PF('CuadrillaDialogo').show()");
        }
        if (posicionParametroTiempo == 4) {
            RequestContext.getCurrentInstance().update("formEmpleado:EmpleadoDialogo");
            RequestContext.getCurrentInstance().execute("PF('EmpleadoDialogo').show()");
        }
        if (posicionParametroTiempo == 5) {
            RequestContext.getCurrentInstance().update("formEmpleado:EmpleadoDialogo");
            RequestContext.getCurrentInstance().execute("PF('EmpleadoDialogo').show()");
        }
        posicionParametroTiempo = -1;
    }

    public void modificarParametroTiempo() {
        if (guardado == true) {
            guardado = false;
        }
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void pruebaMask(String dato, int tipoFecha) {
        int valorHora, valorMin;
        char[] aCaracteres = dato.toCharArray();
        String var1, var2, var3, var4;
        var1 = aCaracteres[0] + "";
        var2 = aCaracteres[1] + "";
        var3 = aCaracteres[3] + "";
        var4 = aCaracteres[4] + "";
        String hora = "", min = "";
        hora = var1 + var2;
        min = var3 + var4;
        valorHora = Integer.parseInt(hora);
        valorMin = Integer.parseInt(min);
        RequestContext context = RequestContext.getCurrentInstance();
        if ((valorHora >= 0 && valorHora <= 23) && (valorMin >= 0 && valorMin <= 59)) {
            if (tipoFecha == 1) {
                if (guardado == true) {
                    guardado = false;
                }
                parametroTiempoUsuarioBD.getFechadesde().setHours(valorHora);
                parametroTiempoUsuarioBD.getFechadesde().setMinutes(valorMin);
                RequestContext.getCurrentInstance().update("form:editarTiempoFechaDesdeParametro");
            }
            if (tipoFecha == 2) {
                if (guardado == true) {
                    guardado = false;
                }
                parametroTiempoUsuarioBD.getFechahasta().setHours(valorHora);
                parametroTiempoUsuarioBD.getFechahasta().setMinutes(valorMin);
                RequestContext.getCurrentInstance().update("form:editarTiempoFechaHastaParametro");
            }
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        } else {
            if (tipoFecha == 1) {
                fechaDesdeTiempo = auxParametroHoraDesde;
                RequestContext.getCurrentInstance().update("form:editarTiempoFechaDesdeParametro");
            }
            if (tipoFecha == 2) {
                fechaHastaTiempo = auxParametroHoraHasta;
                RequestContext.getCurrentInstance().update("form:editarTiempoFechaHastaParametro");
            }
            RequestContext.getCurrentInstance().execute("PF('errorHorasFechas').show()");
        }
    }

    public void modificarParametroTiempo(int indice, String confirmarCambio, String valorConfirmar) {
        posicionParametroTiempo = indice;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equals("CUADRILLA")) {
            if (!valorConfirmar.isEmpty()) {
                parametroTiempoUsuarioBD.getCuadrilla().setDescripcion(auxParametroCuadrilla);
                for (int i = 0; i < lovCuadrillas.size(); i++) {
                    if (lovCuadrillas.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    parametroTiempoUsuarioBD.setCuadrilla(lovCuadrillas.get(indiceUnicoElemento));
                    lovCuadrillas.clear();
                    getLovCuadrillas();
                    if (guardado == true) {
                        guardado = false;
                    }
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                } else {
                    permitirIndex = false;
                    RequestContext.getCurrentInstance().update("formCuadrilla:CuadrillaDialogo");
                    RequestContext.getCurrentInstance().execute("PF('CuadrillaDialogo').show()");
                }
            } else {
                parametroTiempoUsuarioBD.setCuadrilla(new Cuadrillas());
                if (guardado == true) {
                    guardado = false;
                }
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            RequestContext.getCurrentInstance().update("form:editarCuadrillaParametro");
        } else if (confirmarCambio.equals("EMPLDESDE")) {
            if (!valorConfirmar.isEmpty()) {
                parametroTiempoUsuarioBD.setCodigoempleadodesde(auxParametroEmplDesde);
                for (int i = 0; i < lovEmpleados.size(); i++) {
                    if (lovEmpleados.get(i).getCodigoempleadoSTR().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    parametroTiempoUsuarioBD.setCodigoempleadodesde(lovEmpleados.get(indiceUnicoElemento).getCodigoempleado());
                    lovEmpleados.clear();
                    getLovEmpleados();
                    if (guardado == true) {
                        guardado = false;
                    }
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                } else {
                    permitirIndex = false;
                    RequestContext.getCurrentInstance().update("formEmpleado:EmpleadoDialogo");
                    RequestContext.getCurrentInstance().execute("PF('EmpleadoDialogo').show()");
                }
            } else {
                parametroTiempoUsuarioBD.setCodigoempleadodesde(new BigDecimal("0"));
                if (guardado == true) {
                    guardado = false;
                }
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            RequestContext.getCurrentInstance().update("form:editarEmplDesdeParametro");
        } else if (confirmarCambio.equals("EMPLHASTA")) {
            if (!valorConfirmar.isEmpty()) {
                parametroTiempoUsuarioBD.setCodigoempleadohasta(auxParametroEmplHasta);
                for (int i = 0; i < lovEmpleados.size(); i++) {
                    if (lovEmpleados.get(i).getCodigoempleadoSTR().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    parametroTiempoUsuarioBD.setCodigoempleadohasta(lovEmpleados.get(indiceUnicoElemento).getCodigoempleado());
                    lovEmpleados.clear();
                    getLovEmpleados();
                    if (guardado == true) {
                        guardado = false;
                    }
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                } else {
                    permitirIndex = false;
                    RequestContext.getCurrentInstance().update("formEmpleado:EmpleadoDialogo");
                    RequestContext.getCurrentInstance().execute("PF('EmpleadoDialogo').show()");
                }
            } else {
                parametroTiempoUsuarioBD.setCodigoempleadohasta(new BigDecimal("9999999999999999999999999999999"));
                if (guardado == true) {
                    guardado = false;
                }
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            RequestContext.getCurrentInstance().update("form:editarEmplHastaParametro");
        }
    }

    public void modificarFechasParametro(int i) {
        RequestContext context = RequestContext.getCurrentInstance();
        if (parametroTiempoUsuarioBD.getFechadesde() != null && parametroTiempoUsuarioBD.getFechahasta() != null) {
            boolean validar = validarFechasParametros();
            if (validar == true) {
                cambiarIndiceParametro(i);
                modificarParametroTiempo();
                int horaDesde = parametroTiempoUsuarioBD.getFechadesde().getHours();
                int minDesde = parametroTiempoUsuarioBD.getFechadesde().getMinutes();
                String txtHoraDesde = "";
                String txtMinDesde = "";
                if (horaDesde >= 0 && horaDesde <= 9) {
                    txtHoraDesde = "0" + String.valueOf(horaDesde);
                } else {
                    txtHoraDesde = String.valueOf(horaDesde);
                }
                if (minDesde >= 0 && minDesde <= 9) {
                    txtMinDesde = "0" + String.valueOf(minDesde);
                } else {
                    txtMinDesde = String.valueOf(minDesde);
                }
                fechaDesdeTiempo = "";
                fechaDesdeTiempo = txtHoraDesde + txtMinDesde;
                ////////////
                int horaHasta = parametroTiempoUsuarioBD.getFechahasta().getHours();
                int minHasta = parametroTiempoUsuarioBD.getFechahasta().getMinutes();
                String txtHoraHasta = "";
                String txtMinHasta = "";
                if (horaHasta >= 0 && horaHasta <= 9) {
                    txtHoraHasta = "0" + String.valueOf(horaHasta);
                } else {
                    txtHoraHasta = String.valueOf(horaHasta);
                }
                if (minHasta >= 0 && minHasta <= 9) {
                    txtMinHasta = "0" + String.valueOf(minHasta);
                } else {
                    txtMinHasta = String.valueOf(minHasta);
                }
                fechaHastaTiempo = "";
                fechaHastaTiempo = txtHoraHasta + txtMinHasta;
                RequestContext.getCurrentInstance().update("form:editarFechaHastaParametro");
                RequestContext.getCurrentInstance().update("form:editarTiempoFechaHastaParametro");
                RequestContext.getCurrentInstance().update("form:editarFechaDesdeParametro");
                RequestContext.getCurrentInstance().update("form:editarTiempoFechaDesdeParametro");
            } else {
                parametroTiempoUsuarioBD.setFechadesde(auxParametroFechaDesde);
                parametroTiempoUsuarioBD.setFechahasta(auxParametroFechaHasta);
                RequestContext.getCurrentInstance().update("form:editarFechaHastaParametro");
                RequestContext.getCurrentInstance().update("form:editarTiempoFechaHastaParametro");
                RequestContext.getCurrentInstance().update("form:editarFechaDesdeParametro");
                RequestContext.getCurrentInstance().update("form:editarTiempoFechaDesdeParametro");
                RequestContext.getCurrentInstance().execute("PF('errorFechasParametro').show()");
            }
        } else {
            parametroTiempoUsuarioBD.setFechadesde(auxParametroFechaDesde);
            parametroTiempoUsuarioBD.setFechahasta(auxParametroFechaHasta);
            RequestContext.getCurrentInstance().update("form:editarFechaHastaParametro");
            RequestContext.getCurrentInstance().update("form:editarTiempoFechaHastaParametro");
            RequestContext.getCurrentInstance().update("form:editarFechaDesdeParametro");
            RequestContext.getCurrentInstance().update("form:editarTiempoFechaDesdeParametro");
            RequestContext.getCurrentInstance().execute("PF('errorFechasPKG').show()");
        }
    }

    public boolean validarFechasParametros() {
        fechaParametro = new Date();
        fechaParametro.setYear(0);
        fechaParametro.setMonth(1);
        fechaParametro.setDate(1);
        boolean retorno = true;
        if (parametroTiempoUsuarioBD.getFechadesde().after(fechaParametro) && parametroTiempoUsuarioBD.getFechahasta().after(fechaParametro)) {
            if (parametroTiempoUsuarioBD.getFechahasta().after(parametroTiempoUsuarioBD.getFechadesde())) {
                retorno = true;
            } else {
                retorno = false;
            }
        } else {
            retorno = false;
        }
        return retorno;
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void actualizarEmpleado() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (posicionParametroTiempo == 4) {
            parametroTiempoUsuarioBD.setCodigoempleadodesde(empleadoSeleccionado.getCodigoempleado());
        }
        if (posicionParametroTiempo == 5) {
            parametroTiempoUsuarioBD.setCodigoempleadohasta(empleadoSeleccionado.getCodigoempleado());
        }

        modificarParametroTiempo();

        filtrarLovEmpleados = null;
        empleadoSeleccionado = null;
        aceptar = true;

        posicionParametroTiempo = -1;
        /*
         RequestContext.getCurrentInstance().update("formEmpleado:EmpleadoDialogo");
         RequestContext.getCurrentInstance().update("form:lovEmpleado");
         RequestContext.getCurrentInstance().update("form:aceptarE");*/
        context.reset("form:lovEmpleado:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovEmpleado').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('EmpleadoDialogo').hide()");
    }

    public void cancelarCambioEmpleado() {
        filtrarLovEmpleados = null;
        empleadoSeleccionado = null;
        aceptar = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovEmpleado:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovEmpleado').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('EmpleadoDialogo').hide()");
    }

    public void actualizarCuadrilla() {
        RequestContext context = RequestContext.getCurrentInstance();

        parametroTiempoUsuarioBD.setCuadrilla(cuadrillaSeleccionada);

        modificarParametroTiempo();

        filtrarLovCuadrillas = null;
        cuadrillaSeleccionada = null;
        aceptar = true;

        posicionParametroTiempo = -1;
        /*
         RequestContext.getCurrentInstance().update("formCuadrilla:CuadrillaDialogo");
         RequestContext.getCurrentInstance().update("form:lovCuadrilla");
         RequestContext.getCurrentInstance().update("form:aceptarC");*/
        context.reset("form:lovCuadrilla:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovCuadrilla').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('CuadrillaDialogo').hide()");
    }

    public void cancelarCambioCuadrilla() {
        filtrarLovCuadrillas = null;
        cuadrillaSeleccionada = null;
        aceptar = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovCuadrilla:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovCuadrilla').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('CuadrillaDialogo').hide()");
    }

    public void actionBtnProgramarTurno() {
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            if (parametroTiempoUsuarioBD.getFechadesde() != null && parametroTiempoUsuarioBD.getFechahasta() != null) {
                guardarCambios();
                administrarATParametroTiempo.ejecutarPKG_INSERTARCUADRILLA(parametroTiempoUsuarioBD.getCuadrilla().getSecuencia(), parametroTiempoUsuarioBD.getFechadesde(), parametroTiempoUsuarioBD.getFechahasta());
                FacesMessage msg = new FacesMessage("Información", "La ejecucion del boton fue realizada con exito");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                RequestContext.getCurrentInstance().update("form:growl");
            } else {
                RequestContext.getCurrentInstance().execute("PF('errorFechasPKG').show()");
            }
        } catch (Exception e) {
            System.out.println("Error actionBtnProgramarTurno Controlador : " + e.toString());
            FacesMessage msg = new FacesMessage("Información", "Ocurrio un error en la ejecucion, intente nuevamente");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }
    }

    public void actionBtnSimularMarcacion() {
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            if (parametroTiempoUsuarioBD.getFechadesde() != null && parametroTiempoUsuarioBD.getFechahasta() != null) {
                guardarCambios();
                administrarATParametroTiempo.ejecutarPKG_SIMULARTURNOSEMPLEADOS(parametroTiempoUsuarioBD.getFechadesde(), parametroTiempoUsuarioBD.getFechahasta(), parametroTiempoUsuarioBD.getCodigoempleadodesde(), parametroTiempoUsuarioBD.getCodigoempleadohasta());
                FacesMessage msg = new FacesMessage("Información", "La ejecucion del boton fue realizada con exito");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                RequestContext.getCurrentInstance().update("form:growl");
            } else {
                RequestContext.getCurrentInstance().execute("errorFechasPKG').show()");
            }
        } catch (Exception e) {
            System.out.println("Error actionBtnSimularMarcacion Controlador : " + e.toString());
            FacesMessage msg = new FacesMessage("Información", "Ocurrio un error en la ejecucion, intente nuevamente");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }
    }

    public void actionBtnLiquidarTiempos() {
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            if (parametroTiempoUsuarioBD.getFechadesde() != null && parametroTiempoUsuarioBD.getFechahasta() != null) {
                Date fechaIni = null, fechaFin = null;
                fechaIni = administrarATParametroTiempo.obtenerFechaInicialMinTurnosEmpleados();
                fechaFin = administrarATParametroTiempo.obtenerFechaFinalMaxTurnosEmpleados();
                DateFormat df = DateFormat.getDateInstance();
                String FI = df.format(fechaIni);
                String FF = df.format(fechaFin);
                mensajeBtnLiquidarTiempos = "La minima fecha marcada sin procesar es: " + FI + " y la maxima fecha marcada sin procesar es: " + FF;
                RequestContext.getCurrentInstance().update("form:mensajeLiquidarTiempos");
                RequestContext.getCurrentInstance().execute("PF('mensajeLiquidarTiempos').show()");
            } else {
                RequestContext.getCurrentInstance().execute("PF('errorFechasPKG').show()");
            }
        } catch (Exception e) {
            System.out.println("Error actionBtnSimularMarcacion Controlador : " + e.toString());

        }
    }

    public void actionFinalizarLiquidarTiempos() {
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            administrarATParametroTiempo.ejecutarPKG_LIQUIDAR(parametroTiempoUsuarioBD.getFechadesde(), parametroTiempoUsuarioBD.getFechahasta(), parametroTiempoUsuarioBD.getCodigoempleadodesde(), parametroTiempoUsuarioBD.getCodigoempleadohasta(), parametroTiempoUsuarioBD.getFormaliquidacion());
            guardarCambios();
            FacesMessage msg = new FacesMessage("Información", "La ejecucion del boton fue realizada con exito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        } catch (Exception e) {
            System.out.println("Error actionFinalizarLiquidarTiempos Controlador : " + e.toString());
            FacesMessage msg = new FacesMessage("Información", "Ocurrio un error en la ejecucion, intente nuevamente");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }
    }

    public void actionBtnDesprogramarTurno() {
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            if (parametroTiempoUsuarioBD.getFechadesde() != null && parametroTiempoUsuarioBD.getFechahasta() != null) {
                guardarCambios();
                administrarATParametroTiempo.ejecutarPKG_EliminarProgramacion(fechaParametro, fechaParametro);
                FacesMessage msg = new FacesMessage("Información", "La ejecucion del boton fue realizada con exito");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                RequestContext.getCurrentInstance().update("form:growl");
            } else {
                RequestContext.getCurrentInstance().execute("errorFechasPKG').show()");
            }
        } catch (Exception e) {
            System.out.println("Error actionBtnDesprogramarTurno Controlador : " + e.toString());
            FacesMessage msg = new FacesMessage("Información", "Ocurrio un error en la ejecucion, intente nuevamente");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }
    }

    public void actionBtnEliminarSimulacion() {
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            if (parametroTiempoUsuarioBD.getFechadesde() != null && parametroTiempoUsuarioBD.getFechahasta() != null) {
                guardarCambios();
                administrarATParametroTiempo.ejecutarPKG_ELIMINARSIMULACION(parametroTiempoUsuarioBD.getCuadrilla().getSecuencia(), parametroTiempoUsuarioBD.getFechadesde(), parametroTiempoUsuarioBD.getFechahasta(), parametroTiempoUsuarioBD.getCodigoempleadodesde(), parametroTiempoUsuarioBD.getCodigoempleadohasta());
                FacesMessage msg = new FacesMessage("Información", "La ejecucion del boton fue realizada con exito");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                RequestContext.getCurrentInstance().update("form:growl");
            } else {
                RequestContext.getCurrentInstance().execute("errorFechasPKG').show()");
            }
        } catch (Exception e) {
            System.out.println("Error actionBtnDesprogramarTurno Controlador : " + e.toString());
            FacesMessage msg = new FacesMessage("Información", "Ocurrio un error en la ejecucion, intente nuevamente");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }
    }

    public void actionBtnDesliquidarTiempos() {
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            if (parametroTiempoUsuarioBD.getFechadesde() != null && parametroTiempoUsuarioBD.getFechahasta() != null) {
                int contador = administrarATParametroTiempo.ejecutarPKG_CONTARNOVEDADESLIQ(parametroTiempoUsuarioBD.getFechadesde(), parametroTiempoUsuarioBD.getFechahasta(), parametroTiempoUsuarioBD.getCodigoempleadodesde(), parametroTiempoUsuarioBD.getCodigoempleadohasta());
                if (contador > 0) {
                    RequestContext.getCurrentInstance().execute("errorDesliquidar').show()");
                } else {
                    guardarCambios();
                    administrarATParametroTiempo.ejecutarPKG_ELIMINARLIQUIDACION(parametroTiempoUsuarioBD.getFechadesde(), parametroTiempoUsuarioBD.getFechahasta(), parametroTiempoUsuarioBD.getCodigoempleadodesde(), parametroTiempoUsuarioBD.getCodigoempleadohasta());
                    FacesMessage msg = new FacesMessage("Información", "La ejecucion del boton fue realizada con exito");
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                    RequestContext.getCurrentInstance().update("form:growl");
                }
            } else {
                RequestContext.getCurrentInstance().execute("errorFechasPKG').show()");
            }
        } catch (Exception e) {
            System.out.println("Error actionBtnDesliquidarTiempos Controlador : " + e.toString());
            FacesMessage msg = new FacesMessage("Información", "Ocurrio un error en la ejecucion, intente nuevamente");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        getListaParametrosTiemposExport();
        if (!listaParametrosTiemposExport.isEmpty()) {
            if (secRegistro != null) {
                int resultado = administrarRastros.obtenerTabla(secRegistro, "PARAMETROSTIEMPOS");
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
            if (administrarRastros.verificarHistoricosTabla("PARAMETROSTIEMPOS")) {
                RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
            } else {
                RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
            }

        }
        posicionParametroTiempo = -1;
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosParametroTiempoExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "ParametrosTiempos_PDF", false, false, "UTF-8", null, null);
        context.responseComplete();
        posicionParametroTiempo = -1;
        secRegistro = null;
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosParametroTiempoExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "ParametrosTiempos_XLS", false, false, "UTF-8", null, null);
        context.responseComplete();
        posicionParametroTiempo = -1;
        secRegistro = null;
    }

    //GET - SET
    public ActualUsuario getActualUsuario() {
        if (actualUsuario == null) {
            actualUsuario = administrarATParametroTiempo.obtenerActualUsuario();
        }
        return actualUsuario;
    }

    public void setActualUsuario(ActualUsuario actualUsuario) {
        this.actualUsuario = actualUsuario;
    }

    public ParametrosTiempos getParametroTiempoUsuarioBD() {
        if (parametroTiempoUsuarioBD == null) {
            if (actualUsuario.getSecuencia() != null) {
                parametroTiempoUsuarioBD = administrarATParametroTiempo.consultarParametrosTiemposPorUsarioBD(actualUsuario.getAlias());
                if (parametroTiempoUsuarioBD != null) {
                    if (parametroTiempoUsuarioBD.getCuadrilla() == null) {
                        parametroTiempoUsuarioBD.setCuadrilla(new Cuadrillas());
                    }
                    int horaDesde = parametroTiempoUsuarioBD.getFechadesde().getHours();
                    int minDesde = parametroTiempoUsuarioBD.getFechadesde().getMinutes();
                    String txtHoraDesde = "";
                    String txtMinDesde = "";
                    if (horaDesde >= 0 && horaDesde <= 9) {
                        txtHoraDesde = "0" + String.valueOf(horaDesde);
                    } else {
                        txtHoraDesde = String.valueOf(horaDesde);
                    }
                    if (minDesde >= 0 && minDesde <= 9) {
                        txtMinDesde = "0" + String.valueOf(minDesde);
                    } else {
                        txtMinDesde = String.valueOf(minDesde);
                    }
                    fechaDesdeTiempo = "";
                    fechaDesdeTiempo = txtHoraDesde + txtMinDesde;
                    ////////////
                    int horaHasta = parametroTiempoUsuarioBD.getFechahasta().getHours();
                    int minHasta = parametroTiempoUsuarioBD.getFechahasta().getMinutes();
                    String txtHoraHasta = "";
                    String txtMinHasta = "";
                    if (horaHasta >= 0 && horaHasta <= 9) {
                        txtHoraHasta = "0" + String.valueOf(horaHasta);
                    } else {
                        txtHoraHasta = String.valueOf(horaHasta);
                    }
                    if (minHasta >= 0 && minHasta <= 9) {
                        txtMinHasta = "0" + String.valueOf(minHasta);
                    } else {
                        txtMinHasta = String.valueOf(minHasta);
                    }
                    fechaHastaTiempo = "";
                    fechaHastaTiempo = txtHoraHasta + txtMinHasta;
                }

            }
        }
        return parametroTiempoUsuarioBD;
    }

    public void setParametroTiempoUsuarioBD(ParametrosTiempos parametroTiempoUsuarioBD) {
        this.parametroTiempoUsuarioBD = parametroTiempoUsuarioBD;
    }

    public List<Cuadrillas> getLovCuadrillas() {
        lovCuadrillas = administrarATParametroTiempo.lovCuadrillas();
        return lovCuadrillas;
    }

    public void setLovCuadrillas(List<Cuadrillas> lovCuadrillas) {
        this.lovCuadrillas = lovCuadrillas;
    }

    public List<Cuadrillas> getFiltrarLovCuadrillas() {
        return filtrarLovCuadrillas;
    }

    public void setFiltrarLovCuadrillas(List<Cuadrillas> filtrarLovCuadrillas) {
        this.filtrarLovCuadrillas = filtrarLovCuadrillas;
    }

    public Cuadrillas getCuadrillaSeleccionada() {
        return cuadrillaSeleccionada;
    }

    public void setCuadrillaSeleccionada(Cuadrillas cuadrillaSeleccionada) {
        this.cuadrillaSeleccionada = cuadrillaSeleccionada;
    }

    public List<Empleados> getLovEmpleados() {
        lovEmpleados = administrarATParametroTiempo.lovEmpleados();
        return lovEmpleados;
    }

    public void setLovEmpleados(List<Empleados> lovEmpleados) {
        this.lovEmpleados = lovEmpleados;
    }

    public List<Empleados> getFiltrarLovEmpleados() {
        return filtrarLovEmpleados;
    }

    public void setFiltrarLovEmpleados(List<Empleados> filtrarLovEmpleados) {
        this.filtrarLovEmpleados = filtrarLovEmpleados;
    }

    public Empleados getEmpleadoSeleccionado() {
        return empleadoSeleccionado;
    }

    public void setEmpleadoSeleccionado(Empleados empleadoSeleccionado) {
        this.empleadoSeleccionado = empleadoSeleccionado;
    }

    public String getInfoRegistroCuadrilla() {
        return infoRegistroCuadrilla;
    }

    public void setInfoRegistroCuadrilla(String infoRegistroCuadrilla) {
        this.infoRegistroCuadrilla = infoRegistroCuadrilla;
    }

    public String getInfoRegistroEmpleado() {
        return infoRegistroEmpleado;
    }

    public void setInfoRegistroEmpleado(String infoRegistroEmpleado) {
        this.infoRegistroEmpleado = infoRegistroEmpleado;
    }

    public int getTipoArchivoReporte() {
        return tipoArchivoReporte;
    }

    public void setTipoArchivoReporte(int tipoArchivoReporte) {
        this.tipoArchivoReporte = tipoArchivoReporte;
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

    public List<ParametrosTiempos> getListaParametrosTiemposExport() {
        listaParametrosTiemposExport = null;
        listaParametrosTiemposExport = new ArrayList<ParametrosTiempos>();
        parametroTiempoUsuarioBD.setHoraFechaDesde(fechaDesdeTiempo);
        parametroTiempoUsuarioBD.setHoraFechaHasta(fechaHastaTiempo);
        listaParametrosTiemposExport.add(parametroTiempoUsuarioBD);
        return listaParametrosTiemposExport;
    }

    public void setListaParametrosTiemposExport(List<ParametrosTiempos> listaParametrosTiemposExport) {
        this.listaParametrosTiemposExport = listaParametrosTiemposExport;
    }

    public String getMensajeBtnLiquidarTiempos() {
        return mensajeBtnLiquidarTiempos;
    }

    public void setMensajeBtnLiquidarTiempos(String mensajeBtnLiquidarTiempos) {
        this.mensajeBtnLiquidarTiempos = mensajeBtnLiquidarTiempos;
    }

    public String getFechaHastaTiempo() {
        return fechaHastaTiempo;
    }

    public void setFechaHastaTiempo(String pruebaTiempo) {
        this.fechaHastaTiempo = pruebaTiempo;
    }

    public String getFechaDesdeTiempo() {
        return fechaDesdeTiempo;
    }

    public void setFechaDesdeTiempo(String fechaDesdeTiempo) {
        this.fechaDesdeTiempo = fechaDesdeTiempo;
    }

    public BigInteger getBackUpSecRegistro() {
        return backUpSecRegistro;
    }

    public void setBackUpSecRegistro(BigInteger backUpSecRegistro) {
        this.backUpSecRegistro = backUpSecRegistro;
    }

    public String getEditarFechaHastaTiempo() {
        return editarFechaHastaTiempo;
    }

    public void setEditarFechaHastaTiempo(String editarFechaHastaTiempo) {
        this.editarFechaHastaTiempo = editarFechaHastaTiempo;
    }

    public String getEditarFechaDesdeTiempo() {
        return editarFechaDesdeTiempo;
    }

    public void setEditarFechaDesdeTiempo(String editarFechaDesdeTiempo) {
        this.editarFechaDesdeTiempo = editarFechaDesdeTiempo;
    }

}
