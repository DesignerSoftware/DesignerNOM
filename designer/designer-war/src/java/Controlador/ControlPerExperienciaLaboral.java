package Controlador;

import Entidades.Empleados;
import Entidades.HVHojasDeVida;
import Entidades.HvExperienciasLaborales;
import Entidades.MotivosRetiros;
import Entidades.SectoresEconomicos;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarPerExperienciaLaboralInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import ControlNavegacion.ControlListaNavegacion;
import java.util.Map;
import java.util.LinkedHashMap;
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
public class ControlPerExperienciaLaboral implements Serializable {

    @EJB
    AdministrarPerExperienciaLaboralInterface administrarPerExperienciaLaboral;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    //HvExperienciasLaborales
    private List<HvExperienciasLaborales> listExperienciaLaboralEmpl;
    private List<HvExperienciasLaborales> filtrarListExperienciaLaboralEmpl;
    private HvExperienciasLaborales experienciaTablaSeleccionada;
    //SectoresEconomicos
    private List<SectoresEconomicos> lovSectoresEconomicos;
    private SectoresEconomicos sectorSeleccionada;
    private List<SectoresEconomicos> lovSectoresEconomicosFiltrar;
    //MotivosRetiros
    private List<MotivosRetiros> lovMotivosRetiros;
    private MotivosRetiros motivoSeleccionado;
    private List<MotivosRetiros> lovMotivosRetirosFiltrar;
    //Tipo Actualizacion
    private int tipoActualizacion;
    //Activo/Desactivo VP Crtl + F11
    private int bandera;
    private HVHojasDeVida hojaVida;
    //Columnas Tabla VP
    private Column expEmpresa, expCargoDes, expJefe, expTelefono, expSectorEco, expMotivos, expFechaInicio, expFechaRetiro;
    //Otros
    private boolean aceptar;
    //modificar
    private List<HvExperienciasLaborales> listExperienciaLaboralModificar;
    private boolean guardado;
    //crear VP
    public HvExperienciasLaborales nuevaExperienciaLaboral;
    private BigInteger l;
    private int k;
    //borrar VL
    private List<HvExperienciasLaborales> listExperienciaLaboralBorrar;
    //editar celda
    private HvExperienciasLaborales editarExperienciaLaboral;
    //duplicar
    //Autocompletar
    private boolean permitirIndex;
    //Variables Autompletar
    private String sector, motivo;
    private int indexAux;
    private int cualCelda, tipoLista;
    private HvExperienciasLaborales duplicarExperienciaLaboral;
    private List<HvExperienciasLaborales> listExperienciaLaboralCrear;
    private BigInteger backUpSecRegistro;
    private String logrosAlcanzados;
    private Empleados empleado;
    private boolean readOnlyLogros;
    private boolean cambiosLogros, activarLov;
    private Date fechaIni, fechaFin;
    private String fechaDesdeText, fechaHastaText;
    ////
    private final SimpleDateFormat formatoFecha;
    private DataTable tablaC;
    //
    private String infoRegistro;
    private String altoTabla;
    private String infoRegistroSector, infoRegistroMotivo;
    private Date fechaParametro;
    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

    public ControlPerExperienciaLaboral() {
        altoTabla = "190";
        hojaVida = new HVHojasDeVida();
        cambiosLogros = true;
        readOnlyLogros = false;
        empleado = new Empleados();
        logrosAlcanzados = "";
        backUpSecRegistro = null;
        tipoLista = 0;
        //Otros
        aceptar = true;
        listExperienciaLaboralBorrar = new ArrayList<HvExperienciasLaborales>();
        k = 0;
        listExperienciaLaboralModificar = new ArrayList<HvExperienciasLaborales>();
        editarExperienciaLaboral = new HvExperienciasLaborales();
        tipoLista = 0;
        guardado = true;
        bandera = 0;
        permitirIndex = true;
        experienciaTablaSeleccionada = null;
        cualCelda = -1;
        lovMotivosRetiros = null;
        lovSectoresEconomicos = null;
        nuevaExperienciaLaboral = new HvExperienciasLaborales();
        nuevaExperienciaLaboral.setFechadesde(new Date());
        nuevaExperienciaLaboral.setSectoreconomico(new SectoresEconomicos());
        nuevaExperienciaLaboral.setMotivoretiro(new MotivosRetiros());
        listExperienciaLaboralCrear = new ArrayList<HvExperienciasLaborales>();
        formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        activarLov = true;
        mapParametros.put("paginaAnterior", paginaAnterior);
    }

    public void limpiarListasValor() {

    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarPerExperienciaLaboral.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

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
        String pagActual = "perexperiencialaboral";
        if (pag.equals("atras")) {
            pag = paginaAnterior;
            paginaAnterior = "nominaf";
            controlListaNavegacion.quitarPagina(pagActual);
        } else {
            controlListaNavegacion.guardarNavegacion(pagActual, pag);
            fc.getApplication().getNavigationHandler().handleNavigation(fc, null, pag);
//Map<String, Object> mapParaEnviar = new LinkedHashMap<String, Object>();
            //mapParaEnviar.put("paginaAnterior", pagActual);
            //mas Parametros
//         if (pag.equals("rastrotabla")) {
//           ControlRastro controlRastro = (ControlRastro) fc.getApplication().evaluateExpressionGet(fc, "#{controlRastro}", ControlRastro.class);
            //           controlRastro.recibirDatosTabla(conceptoSeleccionado.getSecuencia(), "Conceptos", pagActual);
            //      } else if (pag.equals("rastrotablaH")) {
            //       ControlRastro controlRastro = (ControlRastro) fc.getApplication().evaluateExpressionGet(fc, "#{controlRastro}", ControlRastro.class);
            //     controlRastro.historicosTabla("Conceptos", pagActual);
            //   pag = "rastrotabla";
            //}
        }
        limpiarListasValor();
    }

    public void recibirEmpleado(BigInteger secuencia) {
        empleado = administrarPerExperienciaLaboral.empleadoActual(secuencia);
        if (empleado.getPersona().getSecuencia() != null) {
            hojaVida = administrarPerExperienciaLaboral.obtenerHojaVidaPersona(empleado.getPersona().getSecuencia());
        }
        if (hojaVida != null) {
            getListExperienciaLaboralEmpl();
//            if (listExperienciaLaboralEmpl != null) {
//                logrosAlcanzados = listExperienciaLaboralEmpl.get(0).getAlcance();
//            }
            if (listExperienciaLaboralEmpl != null) {
                if (!listExperienciaLaboralEmpl.isEmpty()) {
                    experienciaTablaSeleccionada = listExperienciaLaboralEmpl.get(0);
                }
            }
        }

    }

    /*
    public boolean validarFechasRegistro(int i) {
        

        boolean retorno = true;
        if (i == 0) {
            HvExperienciasLaborales temporal = new HvExperienciasLaborales();
            if (tipoLista == 0) {
                temporal = experienciaTablaSeleccionada;
            }
            if (tipoLista == 1) {
                int ind = listExperienciaLaboralEmpl.indexOf(experienciaTablaSeleccionada);
                temporal = listExperienciaLaboralEmpl.get(ind);
            }
            if (temporal.getFechadesde() != null && temporal.getFechahasta() != null) {
                if (temporal.getFechadesde().before(temporal.getFechahasta())) {
                    retorno = true;
                } else {
                    retorno = false;
                }
            }
            if (temporal.getFechadesde() != null && temporal.getFechahasta() == null) {
                retorno = true;
            }
            if (temporal.getFechadesde() == null && temporal.getFechahasta() == null) {
                retorno = false;
            }
        }
        if (i == 1) {
            if (nuevaExperienciaLaboral.getFechadesde() != null && nuevaExperienciaLaboral.getFechahasta() != null) {
                if (nuevaExperienciaLaboral.getFechadesde().before(nuevaExperienciaLaboral.getFechahasta())) {
                    retorno = true;
                } else {
                    retorno = false;
                }
            }
            if (nuevaExperienciaLaboral.getFechadesde() != null && nuevaExperienciaLaboral.getFechahasta() == null) {
                retorno = true;
            }
            if (nuevaExperienciaLaboral.getFechadesde() == null && nuevaExperienciaLaboral.getFechahasta() == null) {
                retorno = false;
            }
        }
        if (i == 2) {
            if (duplicarExperienciaLaboral.getFechadesde() != null && duplicarExperienciaLaboral.getFechahasta() != null) {
                if (duplicarExperienciaLaboral.getFechadesde().before(duplicarExperienciaLaboral.getFechahasta())) {
                    retorno = true;
                } else {
                    retorno = false;
                }
            }
            if (duplicarExperienciaLaboral.getFechadesde() != null && duplicarExperienciaLaboral.getFechahasta() == null) {
                retorno = true;
            }
            if (duplicarExperienciaLaboral.getFechadesde() == null && duplicarExperienciaLaboral.getFechahasta() == null) {
                retorno = false;
            }
        }
        return retorno;
    }
     */
    public boolean validarFechasRegistro(int i) {
        fechaParametro = new Date();
        fechaParametro.setYear(0);
        fechaParametro.setMonth(1);
        fechaParametro.setDate(1);
        boolean retorno = true;
        if (i == 0) {
            HvExperienciasLaborales auxiliar = null;
            if (tipoLista == 0) {
                auxiliar = experienciaTablaSeleccionada;
            }
            if (tipoLista == 1) {
                auxiliar = experienciaTablaSeleccionada;
            }
            if (auxiliar.getFechahasta() != null) {
                if (auxiliar.getFechadesde().after(fechaParametro) && auxiliar.getFechadesde().before(auxiliar.getFechahasta())) {
                    retorno = true;
                } else {
                    retorno = false;
                    RequestContext.getCurrentInstance().execute("PF('errorFechas').show()");
                }
            }
            if (auxiliar.getFechahasta() == null) {
                if (auxiliar.getFechadesde().after(fechaParametro)) {
                    retorno = true;
                } else {
                    retorno = false;
                }
            }
        }
        if (i == 1) {
            if (nuevaExperienciaLaboral.getFechahasta() != null) {
                if (nuevaExperienciaLaboral.getFechadesde().after(fechaParametro) && nuevaExperienciaLaboral.getFechadesde().before(nuevaExperienciaLaboral.getFechahasta())) {
                    retorno = true;
                } else {
                    RequestContext.getCurrentInstance().execute("PF('errorFechas').show()");
                    retorno = false;
                }
            } else if (nuevaExperienciaLaboral.getFechadesde().after(fechaParametro)) {
                retorno = true;
            } else {
                retorno = false;
            }
        }
        if (i == 2) {
            if (duplicarExperienciaLaboral.getFechahasta() != null) {
                if (duplicarExperienciaLaboral.getFechadesde().after(fechaParametro) && duplicarExperienciaLaboral.getFechadesde().before(duplicarExperienciaLaboral.getFechahasta())) {
                    retorno = true;
                } else {
                    retorno = false;
                    RequestContext.getCurrentInstance().execute("PF('errorFechas').show()");
                }
            } else if (duplicarExperienciaLaboral.getFechadesde().after(fechaParametro)) {
                retorno = true;
            } else {
                retorno = false;
            }
        }
        return retorno;
    }

    public void modificarExperiencia(HvExperienciasLaborales experienciaLaboral) {
        experienciaTablaSeleccionada = experienciaLaboral;
        if (tipoLista == 0) {
            if (experienciaTablaSeleccionada.getFechadesde() != null) {
                fechaDesdeText = formatoFecha.format(experienciaTablaSeleccionada.getFechadesde());
            } else {
                fechaDesdeText = "";
            }
        }
        if (tipoLista == 1) {
            int ind = listExperienciaLaboralEmpl.indexOf(experienciaTablaSeleccionada);
            int aux = ind;
            if (listExperienciaLaboralEmpl.get(aux).getFechadesde() != null) {
                fechaDesdeText = formatoFecha.format(listExperienciaLaboralEmpl.get(aux).getFechadesde());
            } else {
                fechaDesdeText = "";
            }
        }
        boolean retorno = validarFechasRegistro(0);
        if (retorno == true) {
//            if (validarCamposRegistro(0) == true) {
            if (tipoLista == 0) {
                experienciaTablaSeleccionada.setAlcance(logrosAlcanzados);
                if (!listExperienciaLaboralCrear.contains(experienciaTablaSeleccionada)) {
                    if (listExperienciaLaboralModificar.isEmpty()) {
                        listExperienciaLaboralModificar.add(experienciaTablaSeleccionada);
                    } else if (!listExperienciaLaboralModificar.contains(experienciaTablaSeleccionada)) {
                        listExperienciaLaboralModificar.add(experienciaTablaSeleccionada);
                    }
                    if (guardado == true) {
                        guardado = false;
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");
                    }
                }

                fechaIni = null;
                fechaFin = null;

            } else {
                int ind = listExperienciaLaboralEmpl.indexOf(experienciaTablaSeleccionada);
                listExperienciaLaboralEmpl.get(ind).setAlcance(logrosAlcanzados);
                if (!listExperienciaLaboralCrear.contains(experienciaTablaSeleccionada)) {
                    if (listExperienciaLaboralModificar.isEmpty()) {
                        listExperienciaLaboralModificar.add(experienciaTablaSeleccionada);
                    } else if (!listExperienciaLaboralModificar.contains(experienciaTablaSeleccionada)) {
                        listExperienciaLaboralModificar.add(experienciaTablaSeleccionada);
                    }
                    if (guardado == true) {
                        guardado = false;
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");
                    }
                }

                fechaIni = null;
                fechaFin = null;

            }
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosExperiencia");
            RequestContext.getCurrentInstance().update("form:editarLogrosEP");
//            } 
//            else {
//                if (tipoLista == 0) {
//                    experienciaTablaSeleccionada.setFechadesde(fechaDesde);
//                }
//                if (tipoLista == 1) {
//                    int ind = listExperienciaLaboralEmpl.indexOf(experienciaTablaSeleccionada);
//                    listExperienciaLaboralEmpl.get(ind).setFechadesde(fechaDesde);
//                }
//
//                fechaDesdeText = "";
//                fechaIni = null;
//                fechaFin = null;
//                RequestContext context = RequestContext.getCurrentInstance();
//                RequestContext.getCurrentInstance().execute("PF('errorIngresoReg').show()");
//                RequestContext.getCurrentInstance().update("form:datosExperiencia");
//            }
        } else {
            if (tipoLista == 0) {
            }
            if (tipoLista == 1) {
                int ind = listExperienciaLaboralEmpl.indexOf(experienciaTablaSeleccionada);
                listExperienciaLaboralEmpl.get(ind).setFechadesde(fechaIni);
                listExperienciaLaboralEmpl.get(ind).setFechahasta(fechaFin);
            }
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('errorFechasIngresoReg').show()");
            RequestContext.getCurrentInstance().update("form:datosExperiencia");
            fechaIni = null;
            fechaFin = null;
        }
    }

    public void modificarExperiencia(HvExperienciasLaborales experienciaLaboral, String confirmarCambio, String valorConfirmar) {
        experienciaTablaSeleccionada = experienciaLaboral;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("SECTORES")) {
            if (tipoLista == 0) {
                experienciaTablaSeleccionada.getSectoreconomico().setDescripcion(sector);
            } else {
                experienciaTablaSeleccionada.getSectoreconomico().setDescripcion(sector);
            }
            if (lovSectoresEconomicos != null) {
                for (int i = 0; i < lovSectoresEconomicos.size(); i++) {
                    if (lovSectoresEconomicos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
            }
            if (coincidencias == 1) {
                if (tipoLista == 0) {
                    experienciaTablaSeleccionada.setSectoreconomico(lovSectoresEconomicos.get(indiceUnicoElemento));
                } else {
                    experienciaTablaSeleccionada.setSectoreconomico(lovSectoresEconomicos.get(indiceUnicoElemento));
                }
                lovSectoresEconomicos = null;
                getLovSectoresEconomicos();

            } else {
                permitirIndex = false;
                RequestContext.getCurrentInstance().update("form:SectorDialogo");
                RequestContext.getCurrentInstance().execute("PF('SectorDialogo').show()");
                tipoActualizacion = 0;
            }
        } else if (confirmarCambio.equalsIgnoreCase("MOTIVOS")) {
            if (!valorConfirmar.isEmpty()) {
                if (tipoLista == 0) {
                    experienciaTablaSeleccionada.getMotivoretiro().setNombre(motivo);
                } else {
                    experienciaTablaSeleccionada.getMotivoretiro().setNombre(motivo);
                }
                if (lovMotivosRetiros != null) {
                    for (int i = 0; i < lovMotivosRetiros.size(); i++) {
                        if (lovMotivosRetiros.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                            indiceUnicoElemento = i;
                            coincidencias++;
                        }
                    }
                }
                if (coincidencias == 1) {
                    if (tipoLista == 0) {
                        experienciaTablaSeleccionada.setMotivoretiro(lovMotivosRetiros.get(indiceUnicoElemento));
                    } else {
                        experienciaTablaSeleccionada.setMotivoretiro(lovMotivosRetiros.get(indiceUnicoElemento));
                    }
                    lovMotivosRetiros = null;
                    getLovMotivosRetiros();

                } else {
                    permitirIndex = false;
                    RequestContext.getCurrentInstance().update("form:MotivosDialogo");
                    RequestContext.getCurrentInstance().execute("PF('MotivosDialogo').show()");
                    tipoActualizacion = 0;
                }
            } else {
                coincidencias = 1;
                lovMotivosRetiros = null;
                getLovMotivosRetiros();
                if (tipoLista == 0) {
                    experienciaTablaSeleccionada.setMotivoretiro(new MotivosRetiros());
                } else {
                    experienciaTablaSeleccionada.setMotivoretiro(new MotivosRetiros());
                }
            }
        }
        if (coincidencias == 1) {
            if (tipoLista == 0) {
                if (!listExperienciaLaboralCrear.contains(experienciaTablaSeleccionada)) {

                    if (listExperienciaLaboralModificar.isEmpty()) {
                        listExperienciaLaboralModificar.add(experienciaTablaSeleccionada);
                    } else if (!listExperienciaLaboralModificar.contains(experienciaTablaSeleccionada)) {
                        listExperienciaLaboralModificar.add(experienciaTablaSeleccionada);
                    }
                    if (guardado == true) {
                        guardado = false;
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");
                    }
                }

            } else if (!listExperienciaLaboralCrear.contains(experienciaTablaSeleccionada)) {

                if (listExperienciaLaboralModificar.isEmpty()) {
                    listExperienciaLaboralModificar.add(experienciaTablaSeleccionada);
                } else if (!listExperienciaLaboralModificar.contains(experienciaTablaSeleccionada)) {
                    listExperienciaLaboralModificar.add(experienciaTablaSeleccionada);
                }
                if (guardado == true) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
            }

        }
        RequestContext.getCurrentInstance().update("form:datosExperiencia");
        RequestContext.getCurrentInstance().update("form:editarLogrosEP");
    }

    public void valoresBackupAutocompletarExperiencia(int tipoNuevo, String Campo) {

        if (Campo.equals("SECTORES")) {
            if (tipoNuevo == 1) {
                sector = nuevaExperienciaLaboral.getSectoreconomico().getDescripcion();
            } else if (tipoNuevo == 2) {
                sector = duplicarExperienciaLaboral.getSectoreconomico().getDescripcion();
            }
        } else if (Campo.equals("MOTIVOS")) {
            if (tipoNuevo == 1) {
                motivo = nuevaExperienciaLaboral.getMotivoretiro().getNombre();
            } else if (tipoNuevo == 2) {
                motivo = duplicarExperienciaLaboral.getMotivoretiro().getNombre();
            }
        }
    }

    public void autocompletarNuevoyDuplicadoExperiencia(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("SECTORES")) {
            if (tipoNuevo == 1) {
                nuevaExperienciaLaboral.getSectoreconomico().setDescripcion(sector);
            } else if (tipoNuevo == 2) {
                duplicarExperienciaLaboral.getSectoreconomico().setDescripcion(sector);
            }
            for (int i = 0; i < lovSectoresEconomicos.size(); i++) {
                if (lovSectoresEconomicos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevaExperienciaLaboral.setSectoreconomico(lovSectoresEconomicos.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevaSectorEP");
                } else if (tipoNuevo == 2) {
                    duplicarExperienciaLaboral.setSectoreconomico(lovSectoresEconomicos.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarSectorEP");
                }
                lovSectoresEconomicos = null;
                getLovSectoresEconomicos();
            } else {
                RequestContext.getCurrentInstance().update("form:SectorDialogo");
                RequestContext.getCurrentInstance().execute("PF('SectorDialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevaSectorEP");
                } else if (tipoNuevo == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarSectorEP");
                }
            }
        } else if (confirmarCambio.equalsIgnoreCase("MOTIVOS")) {
            if (!valorConfirmar.isEmpty()) {
                if (tipoNuevo == 1) {
                    nuevaExperienciaLaboral.getMotivoretiro().setNombre(motivo);
                } else if (tipoNuevo == 2) {
                    duplicarExperienciaLaboral.getMotivoretiro().setNombre(motivo);
                }
                for (int i = 0; i < lovMotivosRetiros.size(); i++) {
                    if (lovMotivosRetiros.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }

                if (coincidencias == 1) {
                    if (tipoNuevo == 1) {
                        nuevaExperienciaLaboral.setMotivoretiro(lovMotivosRetiros.get(indiceUnicoElemento));
                        RequestContext.getCurrentInstance().update("formularioDialogos:nuevaMotivoEP");
                    } else if (tipoNuevo == 2) {
                        duplicarExperienciaLaboral.setMotivoretiro(lovMotivosRetiros.get(indiceUnicoElemento));
                        RequestContext.getCurrentInstance().update("formularioDialogos:duplicarMotivoEP");
                    }
                    lovMotivosRetiros = null;
                    getLovMotivosRetiros();
                } else {
                    RequestContext.getCurrentInstance().update("form:MotivosDialogo");
                    RequestContext.getCurrentInstance().execute("PF('MotivosDialogo').show()");
                    tipoActualizacion = tipoNuevo;
                    if (tipoNuevo == 1) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:nuevaMotivoEP");
                    } else if (tipoNuevo == 2) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:duplicarMotivoEP");
                    }
                }
            } else {
                lovMotivosRetiros = null;
                getLovMotivosRetiros();
                if (tipoNuevo == 1) {
                    nuevaExperienciaLaboral.setMotivoretiro(new MotivosRetiros());
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevaMotivoEP");
                } else if (tipoNuevo == 2) {
                    duplicarExperienciaLaboral.setMotivoretiro(new MotivosRetiros());
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarMotivoEP");
                }
            }
        }
    }

    public void posicionLogros(int cel) {
        if (permitirIndex == true) {
            if (experienciaTablaSeleccionada != null) {
                cualCelda = cel;
                if (tipoLista == 0) {
                    experienciaTablaSeleccionada.getSecuencia();
                } else {
                    experienciaTablaSeleccionada.getSecuencia();
                }
            }
        }
    }

    public void modificarLogros() {
        if (experienciaTablaSeleccionada != null) {
            modificarExperiencia(experienciaTablaSeleccionada);
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:editarLogrosEP");
            cambiosLogros = false;
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
    }

    public void habilitarBotonLov() {
        activarLov = false;
        RequestContext.getCurrentInstance().update("form:listaValores");
    }

    public void deshabilitarBotonLov() {
        activarLov = true;
        RequestContext.getCurrentInstance().update("form:listaValores");
    }

    public void adicionCambiarIndiceModificar(HvExperienciasLaborales experienciaLaboral, int celda) {
        cambiarIndice(experienciaLaboral, celda);
        modificarExperiencia(experienciaLaboral);
        experienciaTablaSeleccionada = experienciaLaboral;
    }

    public void cambiarIndice(HvExperienciasLaborales experienciaLaboral, int celda) {
        if (permitirIndex == true) {
            experienciaTablaSeleccionada = experienciaLaboral;
            cualCelda = celda;
            if (cualCelda == 0) {
                deshabilitarBotonLov();
                experienciaTablaSeleccionada.getEmpresa();
            } else if (cualCelda == 1) {
                deshabilitarBotonLov();
                experienciaTablaSeleccionada.getFechadesde();
            } else if (cualCelda == 2) {
                deshabilitarBotonLov();
                experienciaTablaSeleccionada.getFechahasta();
            } else if (cualCelda == 3) {
                deshabilitarBotonLov();
                experienciaTablaSeleccionada.getCargo();
            } else if (cualCelda == 4) {
                deshabilitarBotonLov();
                experienciaTablaSeleccionada.getJefeinmediato();
            } else if (cualCelda == 5) {
                deshabilitarBotonLov();
                experienciaTablaSeleccionada.getTelefono();
            } else if (cualCelda == 6) {
                habilitarBotonLov();
                sector = experienciaTablaSeleccionada.getSectoreconomico().getDescripcion();
            } else if (cualCelda == 7) {
                motivo = experienciaTablaSeleccionada.getMotivoretiro().getNombre();
            } else if (cualCelda == 8) {
                habilitarBotonLov();
                if (experienciaTablaSeleccionada != null) {
                    logrosAlcanzados = experienciaTablaSeleccionada.getAlcance();
                }
            }
            RequestContext.getCurrentInstance().update("form:editarLogrosEP");
        }
    }

    public void guardarYSalir() {
        guardarCambios();
        salir();
    }

    public void guardadoGeneral() {
        guardarCambios();
    }

    public void guardarCambios() {
        try {
            if (guardado == false) {
                if (!listExperienciaLaboralBorrar.isEmpty()) {
                    administrarPerExperienciaLaboral.borrarExperienciaLaboral(listExperienciaLaboralBorrar);
                    listExperienciaLaboralBorrar.clear();
                }
                if (!listExperienciaLaboralCrear.isEmpty()) {
                    administrarPerExperienciaLaboral.crearExperienciaLaboral(listExperienciaLaboralCrear);
                    listExperienciaLaboralCrear.clear();
                }
                if (!listExperienciaLaboralModificar.isEmpty()) {
                    administrarPerExperienciaLaboral.editarExperienciaLaboral(listExperienciaLaboralModificar);
                    listExperienciaLaboralModificar.clear();
                }
                listExperienciaLaboralEmpl = null;
                getListExperienciaLaboralEmpl();
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
                k = 0;
//                cambiosLogros = true;
                FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                RequestContext.getCurrentInstance().update("form:growl");
                contarRegistros();
                experienciaTablaSeleccionada = null;
            }
            guardado = true;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosExperiencia");
        } catch (Exception e) {
            System.out.println("Error guardarCambios : " + e.toString());
            FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado, intente nuevamente");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }
    }

    public void cancelarModificacion() {
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            altoTabla = "190";
            expEmpresa = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expEmpresa");
            expEmpresa.setFilterStyle("display: none; visibility: hidden;");
            expCargoDes = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expCargoDes");
            expCargoDes.setFilterStyle("display: none; visibility: hidden;");
            expJefe = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expJefe");
            expJefe.setFilterStyle("display: none; visibility: hidden;");
            expTelefono = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expTelefono");
            expTelefono.setFilterStyle("display: none; visibility: hidden;");
            expSectorEco = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expSectorEco");
            expSectorEco.setFilterStyle("display: none; visibility: hidden;");
            expMotivos = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expMotivos");
            expMotivos.setFilterStyle("display: none; visibility: hidden;");
            expFechaInicio = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expFechaInicio");
            expFechaInicio.setFilterStyle("display: none; visibility: hidden;");
            expFechaRetiro = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expFechaRetiro");
            expFechaRetiro.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosExperiencia");
            bandera = 0;
            filtrarListExperienciaLaboralEmpl = null;
            tipoLista = 0;
        }
        lovMotivosRetiros = null;
        lovSectoresEconomicos = null;
        listExperienciaLaboralBorrar.clear();
        listExperienciaLaboralCrear.clear();
        listExperienciaLaboralModificar.clear();
        experienciaTablaSeleccionada = null;
        k = 0;
        listExperienciaLaboralEmpl = null;
        guardado = true;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        getListExperienciaLaboralEmpl();
        contarRegistros();
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
        RequestContext.getCurrentInstance().update("form:datosExperiencia");
//        logrosAlcanzados = " ";
        RequestContext.getCurrentInstance().update("form:editarLogrosEP");

        cambiosLogros = true;
        nuevaExperienciaLaboral = new HvExperienciasLaborales();
        nuevaExperienciaLaboral.setSectoreconomico(new SectoresEconomicos());
        nuevaExperienciaLaboral.setMotivoretiro(new MotivosRetiros());
        fechaFin = null;
        fechaIni = null;
    }

    public void modificacionesFechas(HvExperienciasLaborales explaboral, int c) {
        HvExperienciasLaborales auxiliar = null;
        if (tipoLista == 0) {
            auxiliar = experienciaTablaSeleccionada;
        }
        if (tipoLista == 1) {
            auxiliar = experienciaTablaSeleccionada;
        }
        if (auxiliar.getFechadesde() != null) {
            boolean retorno = false;
            if (auxiliar.getFechahasta() == null) {
                retorno = true;
            }
            if (auxiliar.getFechahasta() != null) {
                experienciaTablaSeleccionada = explaboral;
                retorno = validarFechasRegistro(0);
            }
            if (retorno == true) {
                cambiarIndice(explaboral, c);
                modificarExperiencia(explaboral);
            } else {
                if (tipoLista == 0) {
                    experienciaTablaSeleccionada.setFechahasta(fechaFin);
                    experienciaTablaSeleccionada.setFechadesde(fechaIni);
                }
                if (tipoLista == 1) {
                    experienciaTablaSeleccionada.setFechahasta(fechaFin);
                    experienciaTablaSeleccionada.setFechadesde(fechaIni);

                }
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:datosExperiencia");
                RequestContext.getCurrentInstance().execute("PF('form:errorFechas').show()");
            }
        } else {
            if (tipoLista == 0) {
                experienciaTablaSeleccionada.setFechadesde(fechaIni);
            }
            if (tipoLista == 1) {
                experienciaTablaSeleccionada.setFechadesde(fechaIni);

            }
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosExperiencia");
            RequestContext.getCurrentInstance().execute("PF('errorRegNew').show()");
        }
    }

    public void editarCelda() {
        if (experienciaTablaSeleccionada != null) {
            if (tipoLista == 0) {
                editarExperienciaLaboral = experienciaTablaSeleccionada;
            }
            if (tipoLista == 1) {
                editarExperienciaLaboral = experienciaTablaSeleccionada;
            }
            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarEmpresaD");
                RequestContext.getCurrentInstance().execute("PF('editarEmpresaD').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaInicialD");
                RequestContext.getCurrentInstance().execute("PF('editarFechaInicialD').show()");
                cualCelda = -1;
            } else if (cualCelda == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaFinalD");
                RequestContext.getCurrentInstance().execute("PF('editarFechaFinalD').show()");
                cualCelda = -1;
            } else if (cualCelda == 3) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarCargoD");
                RequestContext.getCurrentInstance().execute("PF('editarCargoD').show()");
                cualCelda = -1;
            } else if (cualCelda == 4) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarJefeD");
                RequestContext.getCurrentInstance().execute("PF('editarJefeD').show()");
                cualCelda = -1;
            } else if (cualCelda == 5) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarTelefonoD");
                RequestContext.getCurrentInstance().execute("PF('editarTelefonoD').show()");
                cualCelda = -1;
            } else if (cualCelda == 6) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarSectorD");
                RequestContext.getCurrentInstance().execute("PF('editarSectorD').show()");
                cualCelda = -1;
            } else if (cualCelda == 7) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarMotivoD");
                RequestContext.getCurrentInstance().execute("PF('editarMotivoD').show()");
                cualCelda = -1;
            } else if (cualCelda == 8) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarLogroD");
                RequestContext.getCurrentInstance().execute("PF('editarLogroD').show()");
                cualCelda = -1;
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void agregarNuevaE() {
        try {
            if (nuevaExperienciaLaboral.getFechadesde() != null && nuevaExperienciaLaboral.getMotivoretiro() != null) {
                if (validarFechasRegistro(1) == true) {
                    if (bandera == 1) {
                        FacesContext c = FacesContext.getCurrentInstance();
                        altoTabla = "190";
                        expEmpresa = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expEmpresa");
                        expEmpresa.setFilterStyle("display: none; visibility: hidden;");
                        expCargoDes = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expCargoDes");
                        expCargoDes.setFilterStyle("display: none; visibility: hidden;");
                        expJefe = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expJefe");
                        expJefe.setFilterStyle("display: none; visibility: hidden;");
                        expTelefono = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expTelefono");
                        expTelefono.setFilterStyle("display: none; visibility: hidden;");
                        expSectorEco = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expSectorEco");
                        expSectorEco.setFilterStyle("display: none; visibility: hidden;");
                        expMotivos = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expMotivos");
                        expMotivos.setFilterStyle("display: none; visibility: hidden;");
                        expFechaInicio = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expFechaInicio");
                        expFechaInicio.setFilterStyle("display: none; visibility: hidden;");
                        expFechaRetiro = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expFechaRetiro");
                        expFechaRetiro.setFilterStyle("display: none; visibility: hidden;");
                        RequestContext.getCurrentInstance().update("form:datosExperiencia");
                        bandera = 0;
                        filtrarListExperienciaLaboralEmpl = null;
                        tipoLista = 0;
                    }
                    k++;
                    l = BigInteger.valueOf(k);
                    nuevaExperienciaLaboral.setSecuencia(l);
                    nuevaExperienciaLaboral.setHojadevida(hojaVida);
                    listExperienciaLaboralEmpl.add(nuevaExperienciaLaboral);
                    listExperienciaLaboralCrear.add(nuevaExperienciaLaboral);
                    experienciaTablaSeleccionada = nuevaExperienciaLaboral;
                    limpiarNuevaE();
                    getListExperienciaLaboralEmpl();
                    contarRegistros();
//                    RequestContext.getCurrentInstance().update("form:informacionRegistro");
                    RequestContext.getCurrentInstance().update("form:datosExperiencia");
                    RequestContext.getCurrentInstance().execute("PF('NuevoRegistro').hide()");
                    RequestContext.getCurrentInstance().update("form:editarLogrosEP");

                    if (guardado == true) {
                        guardado = false;
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");
                    }
                }
            } else {
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().execute("PF('errorFechasIngresoReg').show()");
            }
        } catch (Exception e) {
            System.out.println("error en controlPerexperiencialaboral.agregarnueva e : " + e.getMessage());
        }
    }

    public void limpiarNuevaE() {
        nuevaExperienciaLaboral = new HvExperienciasLaborales();
        nuevaExperienciaLaboral.setSectoreconomico(new SectoresEconomicos());
        nuevaExperienciaLaboral.setMotivoretiro(new MotivosRetiros());
        nuevaExperienciaLaboral.setFechadesde(new Date());
    }

    public void cancelarNuevaE() {
        nuevaExperienciaLaboral = new HvExperienciasLaborales();
        nuevaExperienciaLaboral.setSectoreconomico(new SectoresEconomicos());
        nuevaExperienciaLaboral.setMotivoretiro(new MotivosRetiros());
        nuevaExperienciaLaboral.setFechadesde(new Date());
    }

    public void verificarDuplicarExperiencia() {
        if (experienciaTablaSeleccionada != null) {
            if (listExperienciaLaboralEmpl != null) {
                int tam = 0;
                if (tipoLista == 0) {
                    tam = listExperienciaLaboralEmpl.size();
                } else {
                    tam = filtrarListExperienciaLaboralEmpl.size();
                }
                if (tam > 0) {
                    duplicarVigenciaE();
                } else {
                    RequestContext context = RequestContext.getCurrentInstance();
                    RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
                }
            } else {
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
            }
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void duplicarVigenciaE() {
        if (experienciaTablaSeleccionada != null) {
            duplicarExperienciaLaboral = new HvExperienciasLaborales();
            if (tipoLista == 0) {
                duplicarExperienciaLaboral.setAlcance(experienciaTablaSeleccionada.getAlcance());
                duplicarExperienciaLaboral.setCargo(experienciaTablaSeleccionada.getCargo());
                duplicarExperienciaLaboral.setEmpresa(experienciaTablaSeleccionada.getEmpresa());
                duplicarExperienciaLaboral.setFechadesde(experienciaTablaSeleccionada.getFechadesde());
                duplicarExperienciaLaboral.setFechahasta(experienciaTablaSeleccionada.getFechahasta());
                duplicarExperienciaLaboral.setHojadevida(experienciaTablaSeleccionada.getHojadevida());
                duplicarExperienciaLaboral.setJefeinmediato(experienciaTablaSeleccionada.getJefeinmediato());
                duplicarExperienciaLaboral.setMotivoretiro(experienciaTablaSeleccionada.getMotivoretiro());
                duplicarExperienciaLaboral.setSectoreconomico(experienciaTablaSeleccionada.getSectoreconomico());
                duplicarExperienciaLaboral.setTelefono(experienciaTablaSeleccionada.getTelefono());
            }
            if (tipoLista == 1) {
                duplicarExperienciaLaboral.setAlcance(experienciaTablaSeleccionada.getAlcance());
                duplicarExperienciaLaboral.setCargo(experienciaTablaSeleccionada.getCargo());
                duplicarExperienciaLaboral.setEmpresa(experienciaTablaSeleccionada.getEmpresa());
                duplicarExperienciaLaboral.setFechadesde(experienciaTablaSeleccionada.getFechadesde());
                duplicarExperienciaLaboral.setFechahasta(experienciaTablaSeleccionada.getFechahasta());
                duplicarExperienciaLaboral.setHojadevida(experienciaTablaSeleccionada.getHojadevida());
                duplicarExperienciaLaboral.setJefeinmediato(experienciaTablaSeleccionada.getJefeinmediato());
                duplicarExperienciaLaboral.setMotivoretiro(experienciaTablaSeleccionada.getMotivoretiro());
                duplicarExperienciaLaboral.setSectoreconomico(experienciaTablaSeleccionada.getSectoreconomico());
                duplicarExperienciaLaboral.setTelefono(experienciaTablaSeleccionada.getTelefono());
            }

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEP");
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistro').show()");
            experienciaTablaSeleccionada = null;
            experienciaTablaSeleccionada = null;
        }
    }

    public void confirmarDuplicarE() {
        if (duplicarExperienciaLaboral.getFechadesde() != null) {
            fechaDesdeText = formatoFecha.format(duplicarExperienciaLaboral.getFechadesde());
        } else {
            fechaDesdeText = "";
        }
        boolean respuesta = validarFechasRegistro(2);
        if (respuesta == true) {
//            if (validarCamposRegistro(2) == true) {
            k++;
            l = BigInteger.valueOf(k);
            duplicarExperienciaLaboral.setSecuencia(l);
            duplicarExperienciaLaboral.setHojadevida(hojaVida);
            listExperienciaLaboralEmpl.add(duplicarExperienciaLaboral);
            listExperienciaLaboralCrear.add(duplicarExperienciaLaboral);
            experienciaTablaSeleccionada = null;
            experienciaTablaSeleccionada = null;
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                altoTabla = "190";
                expEmpresa = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expEmpresa");
                expEmpresa.setFilterStyle("display: none; visibility: hidden;");
                expCargoDes = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expCargoDes");
                expCargoDes.setFilterStyle("display: none; visibility: hidden;");
                expJefe = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expJefe");
                expJefe.setFilterStyle("display: none; visibility: hidden;");
                expTelefono = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expTelefono");
                expTelefono.setFilterStyle("display: none; visibility: hidden;");
                expSectorEco = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expSectorEco");
                expSectorEco.setFilterStyle("display: none; visibility: hidden;");
                expMotivos = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expMotivos");
                expMotivos.setFilterStyle("display: none; visibility: hidden;");
                expFechaInicio = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expFechaInicio");
                expFechaInicio.setFilterStyle("display: none; visibility: hidden;");
                expFechaRetiro = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expFechaRetiro");
                expFechaRetiro.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosExperiencia");
                bandera = 0;
                filtrarListExperienciaLaboralEmpl = null;
                tipoLista = 0;
            }
            duplicarExperienciaLaboral = new HvExperienciasLaborales();
            limpiarduplicarE();
            RequestContext context = RequestContext.getCurrentInstance();
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            RequestContext.getCurrentInstance().update("form:datosExperiencia");
            RequestContext.getCurrentInstance().update("form:editarLogrosEP");
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistro').hide()");
//            } else {
//                RequestContext context = RequestContext.getCurrentInstance();
//                RequestContext.getCurrentInstance().execute("PF('errorIngresoReg').show()");
//            }
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('errorFechasIngresoReg').show()");
        }
    }

    public void limpiarduplicarE() {
        duplicarExperienciaLaboral = new HvExperienciasLaborales();
        duplicarExperienciaLaboral.setSectoreconomico(new SectoresEconomicos());
        duplicarExperienciaLaboral.setMotivoretiro(new MotivosRetiros());
    }

    public void cancelarDuplicadoE() {

        duplicarExperienciaLaboral = new HvExperienciasLaborales();
        duplicarExperienciaLaboral.setSectoreconomico(new SectoresEconomicos());
        duplicarExperienciaLaboral.setMotivoretiro(new MotivosRetiros());
    }

    public void validarBorradoExperiencia() {
        if (logrosAlcanzados.isEmpty()) {
            if (experienciaTablaSeleccionada != null) {
                borrarE();
            }
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('errorBorradoReg').show()");
        }
    }

    public void borrarE() {
        if (experienciaTablaSeleccionada != null) {
            if (!listExperienciaLaboralModificar.isEmpty() && listExperienciaLaboralModificar.contains(experienciaTablaSeleccionada)) {
                int modIndex = listExperienciaLaboralModificar.indexOf(experienciaTablaSeleccionada);
                listExperienciaLaboralModificar.remove(modIndex);
                listExperienciaLaboralBorrar.add(experienciaTablaSeleccionada);
            } else if (!listExperienciaLaboralCrear.isEmpty() && listExperienciaLaboralCrear.contains(experienciaTablaSeleccionada)) {
                int crearIndex = listExperienciaLaboralCrear.indexOf(experienciaTablaSeleccionada);
                listExperienciaLaboralCrear.remove(crearIndex);
            } else {
                listExperienciaLaboralBorrar.add(experienciaTablaSeleccionada);
            }
            listExperienciaLaboralEmpl.remove(experienciaTablaSeleccionada);
            if (tipoLista == 1) {
                filtrarListExperienciaLaboralEmpl.remove(experienciaTablaSeleccionada);
            }
            RequestContext context = RequestContext.getCurrentInstance();
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            RequestContext.getCurrentInstance().update("form:datosExperiencia");
            RequestContext.getCurrentInstance().update("form:editarLogrosEP");
            experienciaTablaSeleccionada = null;
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }

    }

    public void activarCtrlF11() {
        filtradoExperiencia();
    }

    public void filtradoExperiencia() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            altoTabla = "170";
            expEmpresa = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expEmpresa");
            expEmpresa.setFilterStyle("width: 85% !important;");
            expCargoDes = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expCargoDes");
            expCargoDes.setFilterStyle("width: 85% !important;");
            expJefe = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expJefe");
            expJefe.setFilterStyle("width: 85% !important;");
            expTelefono = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expTelefono");
            expTelefono.setFilterStyle("width: 85% !important;");
            expSectorEco = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expSectorEco");
            expSectorEco.setFilterStyle("width: 85% !important;");
            expMotivos = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expMotivos");
            expMotivos.setFilterStyle("width: 85% !important;");
            expFechaInicio = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expFechaInicio");
            expFechaInicio.setFilterStyle("width: 85% !important;");
            expFechaRetiro = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expFechaRetiro");
            expFechaRetiro.setFilterStyle("width: 85% !important;");
            RequestContext.getCurrentInstance().update("form:datosExperiencia");
            tipoLista = 1;
            bandera = 1;
        } else if (bandera == 1) {
            altoTabla = "190";
            expEmpresa = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expEmpresa");
            expEmpresa.setFilterStyle("display: none; visibility: hidden;");
            expCargoDes = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expCargoDes");
            expCargoDes.setFilterStyle("display: none; visibility: hidden;");
            expJefe = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expJefe");
            expJefe.setFilterStyle("display: none; visibility: hidden;");
            expTelefono = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expTelefono");
            expTelefono.setFilterStyle("display: none; visibility: hidden;");
            expSectorEco = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expSectorEco");
            expSectorEco.setFilterStyle("display: none; visibility: hidden;");
            expMotivos = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expMotivos");
            expMotivos.setFilterStyle("display: none; visibility: hidden;");

            expFechaInicio = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expFechaInicio");
            expFechaInicio.setFilterStyle("display: none; visibility: hidden;");
            expFechaRetiro = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expFechaRetiro");
            expFechaRetiro.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosExperiencia");
            bandera = 0;
            filtrarListExperienciaLaboralEmpl = null;
            tipoLista = 0;
        }
    }

    public void salir() {
        limpiarListasValor();
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            altoTabla = "190";
            expEmpresa = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expEmpresa");
            expEmpresa.setFilterStyle("display: none; visibility: hidden;");
            expCargoDes = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expCargoDes");
            expCargoDes.setFilterStyle("display: none; visibility: hidden;");
            expJefe = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expJefe");
            expJefe.setFilterStyle("display: none; visibility: hidden;");
            expTelefono = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expTelefono");
            expTelefono.setFilterStyle("display: none; visibility: hidden;");
            expSectorEco = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expSectorEco");
            expSectorEco.setFilterStyle("display: none; visibility: hidden;");
            expMotivos = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expMotivos");
            expMotivos.setFilterStyle("display: none; visibility: hidden;");
            expFechaInicio = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expFechaInicio");
            expFechaInicio.setFilterStyle("display: none; visibility: hidden;");
            expFechaRetiro = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expFechaRetiro");
            expFechaRetiro.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosExperiencia");
            bandera = 0;
            filtrarListExperienciaLaboralEmpl = null;
            tipoLista = 0;
        }
        listExperienciaLaboralBorrar.clear();
        listExperienciaLaboralCrear.clear();
        listExperienciaLaboralModificar.clear();
        experienciaTablaSeleccionada = null;
        experienciaTablaSeleccionada = null;
        k = 0;
        listExperienciaLaboralEmpl = null;
        lovMotivosRetiros = null;
        lovSectoresEconomicos = null;
        guardado = true;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        tipoActualizacion = -1;
        fechaDesdeText = "";
        fechaFin = null;
        fechaHastaText = "";
        fechaIni = null;
        logrosAlcanzados = "";
        hojaVida = null;
        cambiosLogros = true;
        permitirIndex = true;
        empleado = null;
        navegar("atras");
    }

    public void asignarIndex(HvExperienciasLaborales experienciaLaboral, int dlg, int LND) {
        RequestContext context = RequestContext.getCurrentInstance();
        if (LND == 0) {
            experienciaTablaSeleccionada = experienciaLaboral;
            tipoActualizacion = 0;
        } else if (LND == 1) {
            tipoActualizacion = 1;
        } else if (LND == 2) {
            tipoActualizacion = 2;
        }
        if (dlg == 1) {
            contarRegistroSector();
            RequestContext.getCurrentInstance().update("form:SectorDialogo");
            RequestContext.getCurrentInstance().execute("PF('SectorDialogo').show()");
        } else if (dlg == 2) {
            contarRegistroMotivo();
            RequestContext.getCurrentInstance().update("form:MotivosDialogo");
            RequestContext.getCurrentInstance().execute("PF('MotivosDialogo').show()");
        }
    }

    public void actualizarSector() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                experienciaTablaSeleccionada.setSectoreconomico(sectorSeleccionada);
                if (!listExperienciaLaboralCrear.contains(experienciaTablaSeleccionada)) {
                    if (listExperienciaLaboralModificar.isEmpty()) {
                        listExperienciaLaboralModificar.add(experienciaTablaSeleccionada);
                    } else if (!listExperienciaLaboralModificar.contains(experienciaTablaSeleccionada)) {
                        listExperienciaLaboralModificar.add(experienciaTablaSeleccionada);
                    }
                }
                if (guardado == true) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
                permitirIndex = true;

            } else {
                experienciaTablaSeleccionada.setSectoreconomico(sectorSeleccionada);
                if (!listExperienciaLaboralCrear.contains(experienciaTablaSeleccionada)) {
                    if (listExperienciaLaboralModificar.isEmpty()) {
                        listExperienciaLaboralModificar.add(experienciaTablaSeleccionada);
                    } else if (!listExperienciaLaboralModificar.contains(experienciaTablaSeleccionada)) {
                        listExperienciaLaboralModificar.add(experienciaTablaSeleccionada);
                    }
                }
                if (guardado == true) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }

                permitirIndex = true;

            }
            RequestContext.getCurrentInstance().update("form:datosExperiencia");
        } else if (tipoActualizacion == 1) {
            nuevaExperienciaLaboral.setSectoreconomico(sectorSeleccionada);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaSectorEP");
        } else if (tipoActualizacion == 2) {
            duplicarExperienciaLaboral.setSectoreconomico(sectorSeleccionada);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarSectorEP");
        }
        lovSectoresEconomicosFiltrar = null;
        sectorSeleccionada = null;
        aceptar = true;
        experienciaTablaSeleccionada = null;
        experienciaTablaSeleccionada = null;
        tipoActualizacion = -1;
        /*
         RequestContext.getCurrentInstance().update("form:SectorDialogo");
         RequestContext.getCurrentInstance().update("form:lovSector");
         RequestContext.getCurrentInstance().update("form:aceptarS");*/
        context.reset("form:lovSector:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovSector').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('SectorDialogo').hide()");
    }

    public void cancelarCambioSector() {
        lovSectoresEconomicosFiltrar = null;
        sectorSeleccionada = null;
        aceptar = true;
        experienciaTablaSeleccionada = null;
        experienciaTablaSeleccionada = null;
        tipoActualizacion = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovSector:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovSector').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('SectorDialogo').hide()");
    }

    public void actualizarMotivo() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                experienciaTablaSeleccionada.setMotivoretiro(motivoSeleccionado);
                if (!listExperienciaLaboralCrear.contains(experienciaTablaSeleccionada)) {
                    if (listExperienciaLaboralModificar.isEmpty()) {
                        listExperienciaLaboralModificar.add(experienciaTablaSeleccionada);
                    } else if (!listExperienciaLaboralModificar.contains(experienciaTablaSeleccionada)) {
                        listExperienciaLaboralModificar.add(experienciaTablaSeleccionada);
                    }
                }
            } else {
                experienciaTablaSeleccionada.setMotivoretiro(motivoSeleccionado);
                if (!listExperienciaLaboralCrear.contains(experienciaTablaSeleccionada)) {
                    if (listExperienciaLaboralModificar.isEmpty()) {
                        listExperienciaLaboralModificar.add(experienciaTablaSeleccionada);
                    } else if (!listExperienciaLaboralModificar.contains(experienciaTablaSeleccionada)) {
                        listExperienciaLaboralModificar.add(experienciaTablaSeleccionada);
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            permitirIndex = true;
            RequestContext.getCurrentInstance().update("form:datosExperiencia");
        } else if (tipoActualizacion == 1) {
            nuevaExperienciaLaboral.setMotivoretiro(motivoSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaMotivoEP");
        } else if (tipoActualizacion == 2) {
            duplicarExperienciaLaboral.setMotivoretiro(motivoSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarMotivoEP");
        }
        lovMotivosRetirosFiltrar = null;
        motivoSeleccionado = null;
        aceptar = true;
        experienciaTablaSeleccionada = null;
        experienciaTablaSeleccionada = null;
        tipoActualizacion = -1;
        /*
         RequestContext.getCurrentInstance().update("form:MotivosDialogo");
         RequestContext.getCurrentInstance().update("form:lovMotivos");
         RequestContext.getCurrentInstance().update("form:aceptarM");*/
        context.reset("form:lovMotivos:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovMotivos').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('MotivosDialogo').hide()");
    }

    public void cancelarCambioMotivo() {
        lovMotivosRetirosFiltrar = null;
        motivoSeleccionado = null;
        aceptar = true;
        experienciaTablaSeleccionada = null;
        experienciaTablaSeleccionada = null;
        tipoActualizacion = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovMotivos:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovMotivos').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('MotivosDialogo').hide()");
    }

    public void listaValoresBoton() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (experienciaTablaSeleccionada != null) {
            if (cualCelda == 6) {
                RequestContext.getCurrentInstance().update("form:SectorDialogo");
                RequestContext.getCurrentInstance().execute("PF('SectorDialogo').show()");
                tipoActualizacion = 0;
            }
            if (cualCelda == 7) {
                RequestContext.getCurrentInstance().update("form:MotivosDialogo");
                RequestContext.getCurrentInstance().execute("PF('MotivosDialogo').show()");
                tipoActualizacion = 0;
            }
        }

    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void validarExportPDF() throws IOException {
        exportPDF_E();
    }

    public void exportPDF_E() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosExperienciaExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "ExperienciasLaboralesPDF", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void verificarExportXLS() throws IOException {
        exportXLS_E();
    }

    public void exportXLS_E() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosExperienciaExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "ExperienciasLaboralesXLS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void eventoFiltrar() {
        if (experienciaTablaSeleccionada != null) {
            if (tipoLista == 0) {
                tipoLista = 1;
            }
            contarRegistros();
        }
    }

    public void contarRegistros() {
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
    }

    public void contarRegistroMotivo() {
        RequestContext.getCurrentInstance().update("form:infoRegistroMotivo");
    }

    public void contarRegistroSector() {
        RequestContext.getCurrentInstance().update("form:infoRegistroSector");
    }

    //RASTRO - COMPROBAR SI LA TABLA TIENE RASTRO ACTIVO
    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (listExperienciaLaboralEmpl != null) {
            if (experienciaTablaSeleccionada != null) {
                int resultado = administrarRastros.obtenerTabla(experienciaTablaSeleccionada.getSecuencia(), "HVEXPERIENCIASLABORALES");
                backUpSecRegistro = experienciaTablaSeleccionada.getSecuencia();
                experienciaTablaSeleccionada = null;
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
        } else if (administrarRastros.verificarHistoricosTabla("HVEXPERIENCIASLABORALES")) {
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
        experienciaTablaSeleccionada = null;
    }

    //GET - SET 
    public List<HvExperienciasLaborales> getListExperienciaLaboralEmpl() {
        try {
            if (listExperienciaLaboralEmpl == null) {
                if (hojaVida.getSecuencia() != null) {
                    listExperienciaLaboralEmpl = administrarPerExperienciaLaboral.listExperienciasLaboralesSecuenciaEmpleado(hojaVida.getSecuencia());
                    if (listExperienciaLaboralEmpl != null) {
                        for (int i = 0; i < listExperienciaLaboralEmpl.size(); i++) {
                            if (listExperienciaLaboralEmpl.get(i).getSectoreconomico() == null) {
                                listExperienciaLaboralEmpl.get(i).setSectoreconomico(new SectoresEconomicos());
                            }
                            if (listExperienciaLaboralEmpl.get(i).getMotivoretiro() == null) {
                                listExperienciaLaboralEmpl.get(i).setMotivoretiro(new MotivosRetiros());
                            }
                        }
                    }
                }
            }
            return listExperienciaLaboralEmpl;
        } catch (Exception e) {
            System.out.println("Error en getListProyectos : " + e.toString());
            return null;
        }
    }

    public void setListExperienciaLaboralEmpl(List<HvExperienciasLaborales> setListExperienciaLaboralEmpl) {
        this.listExperienciaLaboralEmpl = setListExperienciaLaboralEmpl;
    }

    public List<HvExperienciasLaborales> getFiltrarListExperienciaLaboralEmpl() {
        return filtrarListExperienciaLaboralEmpl;
    }

    public void setFiltrarListExperienciaLaboralEmpl(List<HvExperienciasLaborales> setFiltrarListExperienciaLaboralEmpl) {
        this.filtrarListExperienciaLaboralEmpl = setFiltrarListExperienciaLaboralEmpl;
    }

    public List<SectoresEconomicos> getLovSectoresEconomicos() {
        try {
            if (lovSectoresEconomicos == null) {
                lovSectoresEconomicos = new ArrayList<SectoresEconomicos>();
                lovSectoresEconomicos = administrarPerExperienciaLaboral.listSectoresEconomicos();
            }
            return lovSectoresEconomicos;
        } catch (Exception e) {
            System.out.println("Error getListEmpresas " + e.toString());
            return null;
        }
    }

    public void setLovSectoresEconomicos(List<SectoresEconomicos> setListSectoresEconomicos) {
        this.lovSectoresEconomicos = setListSectoresEconomicos;
    }

    public SectoresEconomicos getSectorSeleccionada() {
        return sectorSeleccionada;
    }

    public void setSectorSeleccionada(SectoresEconomicos setSectorSeleccionada) {
        this.sectorSeleccionada = setSectorSeleccionada;
    }

    public List<SectoresEconomicos> getLovSectoresEconomicosFiltrar() {
        return lovSectoresEconomicosFiltrar;
    }

    public void setLovSectoresEconomicosFiltrar(List<SectoresEconomicos> setFiltrarListSectoresEconomicos) {
        this.lovSectoresEconomicosFiltrar = setFiltrarListSectoresEconomicos;
    }

    public List<MotivosRetiros> getLovMotivosRetiros() {
        try {
            if (lovMotivosRetiros == null) {
                lovMotivosRetiros = new ArrayList<MotivosRetiros>();
                lovMotivosRetiros = administrarPerExperienciaLaboral.listMotivosRetiros();
            }
            return lovMotivosRetiros;
        } catch (Exception e) {
            System.out.println("Error getListPryClientes " + e.toString());
            return null;
        }
    }

    public void setLovMotivosRetiros(List<MotivosRetiros> setListMotivosRetiros) {
        this.lovMotivosRetiros = setListMotivosRetiros;
    }

    public MotivosRetiros getMotivoSeleccionado() {
        return motivoSeleccionado;
    }

    public void setMotivoSeleccionado(MotivosRetiros setMotivoSeleccionado) {
        this.motivoSeleccionado = setMotivoSeleccionado;
    }

    public List<MotivosRetiros> getLovMotivosRetirosFiltrar() {
        return lovMotivosRetirosFiltrar;
    }

    public void setLovMotivosRetirosFiltrar(List<MotivosRetiros> setFiltrarListMotivosRetiros) {
        this.lovMotivosRetirosFiltrar = setFiltrarListMotivosRetiros;
    }

    public List<HvExperienciasLaborales> getListExperienciaLaboralModificar() {
        return listExperienciaLaboralModificar;
    }

    public void setListExperienciaLaboralModificar(List<HvExperienciasLaborales> setListExperienciaLaboralModificar) {
        this.listExperienciaLaboralModificar = setListExperienciaLaboralModificar;
    }

    public HvExperienciasLaborales getNuevaExperienciaLaboral() {
        return nuevaExperienciaLaboral;
    }

    public void setNuevaExperienciaLaboral(HvExperienciasLaborales setNuevaExperienciaLaboral) {
        this.nuevaExperienciaLaboral = setNuevaExperienciaLaboral;
    }

    public List<HvExperienciasLaborales> getListExperienciaLaboralBorrar() {
        return listExperienciaLaboralBorrar;
    }

    public void setListExperienciaLaboralBorrar(List<HvExperienciasLaborales> setListExperienciaLaboralBorrar) {
        this.listExperienciaLaboralBorrar = setListExperienciaLaboralBorrar;
    }

    public HvExperienciasLaborales getEditarExperienciaLaboral() {
        return editarExperienciaLaboral;
    }

    public void setEditarExperienciaLaboral(HvExperienciasLaborales setEditarExperienciaLaboral) {
        this.editarExperienciaLaboral = setEditarExperienciaLaboral;
    }

    public HvExperienciasLaborales getDuplicarExperienciaLaboral() {
        return duplicarExperienciaLaboral;
    }

    public void setDuplicarExperienciaLaboral(HvExperienciasLaborales setDuplicarExperienciaLaboral) {
        this.duplicarExperienciaLaboral = setDuplicarExperienciaLaboral;
    }

    public List<HvExperienciasLaborales> getListExperienciaLaboralCrear() {
        return listExperienciaLaboralCrear;
    }

    public void setListExperienciaLaboralCrear(List<HvExperienciasLaborales> setListExperienciaLaboralCrear) {
        this.listExperienciaLaboralCrear = setListExperienciaLaboralCrear;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public void setAceptar(boolean aceptar) {
        this.aceptar = aceptar;
    }

    public BigInteger getBackUpSecRegistro() {
        return backUpSecRegistro;
    }

    public void setBackUpSecRegistro(BigInteger backUpSecRegistro) {
        this.backUpSecRegistro = backUpSecRegistro;
    }

    public String getLogrosAlcanzados() {
        if (experienciaTablaSeleccionada != null) {
            logrosAlcanzados = experienciaTablaSeleccionada.getAlcance();
            RequestContext.getCurrentInstance().update("form:editarLogrosEP");
        }
        return logrosAlcanzados;
    }

    public void setLogrosAlcanzados(String logrosAlcanzados) {
        this.logrosAlcanzados = logrosAlcanzados;
    }

    public boolean isReadOnlyLogros() {
        if (listExperienciaLaboralEmpl.isEmpty()) {
            readOnlyLogros = true;
        } else {
            readOnlyLogros = false;
        }
        return readOnlyLogros;
    }

    public void setReadOnlyLogros(boolean readOnlyLogros) {
        this.readOnlyLogros = readOnlyLogros;
    }

    public Empleados getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleados empleado) {
        this.empleado = empleado;
    }

    public String getFechaDesdeText() {
        if (experienciaTablaSeleccionada != null) {
            if (experienciaTablaSeleccionada.getFechadesde() != null) {
                fechaDesdeText = formatoFecha.format(experienciaTablaSeleccionada.getFechadesde());
            } else {
                fechaDesdeText = "";
            }
        } else {
            fechaDesdeText = "";
        }
        return fechaDesdeText;
    }

    public void setFechaDesdeText(String fechaDesdeText) {
        this.fechaDesdeText = fechaDesdeText;
    }

    public String getFechaHastaText() {
        return fechaHastaText;
    }

    public void setFechaHastaText(String fechaHastaText) {
        this.fechaHastaText = fechaHastaText;
    }

    public HvExperienciasLaborales getExperienciaTablaSeleccionada() {
        RequestContext.getCurrentInstance().update("form:editarLogrosEP");
        return experienciaTablaSeleccionada;
    }

    public void setExperienciaTablaSeleccionada(HvExperienciasLaborales experienciaTablaSeleccionada) {
        this.experienciaTablaSeleccionada = experienciaTablaSeleccionada;
    }

    public boolean isGuardado() {
        return guardado;
    }

    public void setGuardado(boolean guardado) {
        this.guardado = guardado;
    }

    public String getInfoRegistro() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosExperiencia");
        infoRegistro = String.valueOf(tabla.getRowCount());
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

    public String getAltoTabla() {
        return altoTabla;
    }

    public void setAltoTabla(String altoTabla) {
        this.altoTabla = altoTabla;
    }

    public String getInfoRegistroSector() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovSector");
        infoRegistroSector = String.valueOf(tabla.getRowCount());
        return infoRegistroSector;
    }

    public void setInfoRegistroSector(String infoRegistroSector) {
        this.infoRegistroSector = infoRegistroSector;
    }

    public String getInfoRegistroMotivo() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovMotivos");
        infoRegistroMotivo = String.valueOf(tabla.getRowCount());
        return infoRegistroMotivo;
    }

    public void setInfoRegistroMotivo(String infoRegistroMotivo) {
        this.infoRegistroMotivo = infoRegistroMotivo;
    }

    public boolean isActivarLov() {
        return activarLov;
    }

    public void setActivarLov(boolean activarLov) {
        this.activarLov = activarLov;
    }

}
