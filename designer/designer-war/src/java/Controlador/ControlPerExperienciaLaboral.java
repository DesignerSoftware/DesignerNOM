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
    private List<SectoresEconomicos> listSectoresEconomicos;
    private SectoresEconomicos sectorSeleccionada;
    private List<SectoresEconomicos> filtrarListSectoresEconomicos;
    //MotivosRetiros
    private List<MotivosRetiros> listMotivosRetiros;
    private MotivosRetiros motivoSeleccionado;
    private List<MotivosRetiros> filtrarListMotivosRetiros;
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
    private Date fechaDesde;
    private Date fechaIni, fechaFin;
    private String fechaDesdeText, fechaHastaText;
    ////
    private final SimpleDateFormat formatoFecha;
    private DataTable tablaC;
    //
    private String infoRegistro;
    private String altoTabla;
    private String infoRegistroSector, infoRegistroMotivo;

    public ControlPerExperienciaLaboral() {
        altoTabla = "190";
        hojaVida = new HVHojasDeVida();
        fechaFin = new Date();
        fechaIni = new Date();
        fechaDesde = new Date();
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
        listMotivosRetiros = null;
        listSectoresEconomicos = null;
        nuevaExperienciaLaboral = new HvExperienciasLaborales();
        nuevaExperienciaLaboral.setSectoreconomico(new SectoresEconomicos());
        nuevaExperienciaLaboral.setMotivoretiro(new MotivosRetiros());
        listExperienciaLaboralCrear = new ArrayList<HvExperienciasLaborales>();
        formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        activarLov = true;

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

    public void recibirEmpleado(BigInteger secuencia) {
        empleado = administrarPerExperienciaLaboral.empleadoActual(secuencia);
        if (empleado.getPersona().getSecuencia() != null) {
            hojaVida = administrarPerExperienciaLaboral.obtenerHojaVidaPersona(empleado.getPersona().getSecuencia());
        }
        if (hojaVida != null) {
            getListExperienciaLaboralEmpl();
            if (listExperienciaLaboralEmpl != null) {
                int tam = listExperienciaLaboralEmpl.size();
                if (tam > 0) {
                    logrosAlcanzados = listExperienciaLaboralEmpl.get(0).getAlcance();
                }
                contarRegistros();
            }
        }
    }

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

    public boolean validarCamposRegistro(int i) {
        boolean retorno = true;
        if (i == 0) {
            HvExperienciasLaborales aux = null;
            if (tipoLista == 0) {
                aux = experienciaTablaSeleccionada;
            } else {
                aux = experienciaTablaSeleccionada;
            }
            if (aux.getSectoreconomico().getSecuencia() == null) {
                retorno = false;
            }
            if (fechaDesdeText.isEmpty()) {
                retorno = false;
            }
        } else if (i == 1) {
            if (nuevaExperienciaLaboral.getSectoreconomico().getSecuencia() == null) {
                retorno = false;
            }
            if (fechaDesdeText.isEmpty()) {
                retorno = false;
            }
        } else if (i == 2) {
            if (duplicarExperienciaLaboral.getSectoreconomico().getSecuencia() == null) {
                retorno = false;
            }
            if (fechaDesdeText.isEmpty()) {
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
            if (validarCamposRegistro(0) == true) {
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

                    experienciaTablaSeleccionada = null;
                    experienciaTablaSeleccionada = null;
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

                    experienciaTablaSeleccionada = null;
                    experienciaTablaSeleccionada = null;
                    fechaIni = null;
                    fechaFin = null;

                }
                RequestContext context = RequestContext.getCurrentInstance();
                context.update("form:datosExperiencia");
            } else {
                if (tipoLista == 0) {
                    experienciaTablaSeleccionada.setFechadesde(fechaDesde);
                }
                if (tipoLista == 1) {
                    int ind = listExperienciaLaboralEmpl.indexOf(experienciaTablaSeleccionada);
                    listExperienciaLaboralEmpl.get(ind).setFechadesde(fechaDesde);
                }

                fechaDesdeText = "";
                experienciaTablaSeleccionada = null;
                experienciaTablaSeleccionada = null;
                fechaIni = null;
                fechaFin = null;
                RequestContext context = RequestContext.getCurrentInstance();
                context.execute("errorIngresoReg.show()");
                context.update("form:datosExperiencia");
            }
        } else {
            if (tipoLista == 0) {
                experienciaTablaSeleccionada.setFechadesde(fechaIni);
                experienciaTablaSeleccionada.setFechahasta(fechaFin);
            }
            if (tipoLista == 1) {
                int ind = listExperienciaLaboralEmpl.indexOf(experienciaTablaSeleccionada);
                listExperienciaLaboralEmpl.get(ind).setFechadesde(fechaIni);
                listExperienciaLaboralEmpl.get(ind).setFechahasta(fechaFin);
            }
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("errorFechasIngresoReg.show()");
            context.update("form:datosExperiencia");
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
            if (listSectoresEconomicos != null) {
                for (int i = 0; i < listSectoresEconomicos.size(); i++) {
                    if (listSectoresEconomicos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
            }
            if (coincidencias == 1) {
                if (tipoLista == 0) {
                    experienciaTablaSeleccionada.setSectoreconomico(listSectoresEconomicos.get(indiceUnicoElemento));
                } else {
                    experienciaTablaSeleccionada.setSectoreconomico(listSectoresEconomicos.get(indiceUnicoElemento));
                }
                listSectoresEconomicos = null;
                getListSectoresEconomicos();

            } else {
                permitirIndex = false;
                context.update("form:SectorDialogo");
                context.execute("SectorDialogo.show()");
                tipoActualizacion = 0;
            }
        } else if (confirmarCambio.equalsIgnoreCase("MOTIVOS")) {
            if (!valorConfirmar.isEmpty()) {
                if (tipoLista == 0) {
                    experienciaTablaSeleccionada.getMotivoretiro().setNombre(motivo);
                } else {
                    experienciaTablaSeleccionada.getMotivoretiro().setNombre(motivo);
                }
                if (listMotivosRetiros != null) {
                    for (int i = 0; i < listMotivosRetiros.size(); i++) {
                        if (listMotivosRetiros.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                            indiceUnicoElemento = i;
                            coincidencias++;
                        }
                    }
                }
                if (coincidencias == 1) {
                    if (tipoLista == 0) {
                        experienciaTablaSeleccionada.setMotivoretiro(listMotivosRetiros.get(indiceUnicoElemento));
                    } else {
                        experienciaTablaSeleccionada.setMotivoretiro(listMotivosRetiros.get(indiceUnicoElemento));
                    }
                    listMotivosRetiros = null;
                    getListMotivosRetiros();

                } else {
                    permitirIndex = false;
                    context.update("form:MotivosDialogo");
                    context.execute("MotivosDialogo.show()");
                    tipoActualizacion = 0;
                }
            } else {
                coincidencias = 1;
                listMotivosRetiros = null;
                getListMotivosRetiros();
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

                experienciaTablaSeleccionada = null;
                experienciaTablaSeleccionada = null;
            } else {
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
                experienciaTablaSeleccionada = null;
                experienciaTablaSeleccionada = null;
            }

        }
        context.update("form:datosExperiencia");
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
            for (int i = 0; i < listSectoresEconomicos.size(); i++) {
                if (listSectoresEconomicos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevaExperienciaLaboral.setSectoreconomico(listSectoresEconomicos.get(indiceUnicoElemento));
                    context.update("formularioDialogos:nuevaSectorEP");
                } else if (tipoNuevo == 2) {
                    duplicarExperienciaLaboral.setSectoreconomico(listSectoresEconomicos.get(indiceUnicoElemento));
                    context.update("formularioDialogos:duplicarSectorEP");
                }
                listSectoresEconomicos = null;
                getListSectoresEconomicos();
            } else {
                context.update("form:SectorDialogo");
                context.execute("SectorDialogo.show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    context.update("formularioDialogos:nuevaSectorEP");
                } else if (tipoNuevo == 2) {
                    context.update("formularioDialogos:duplicarSectorEP");
                }
            }
        } else if (confirmarCambio.equalsIgnoreCase("MOTIVOS")) {
            if (!valorConfirmar.isEmpty()) {
                if (tipoNuevo == 1) {
                    nuevaExperienciaLaboral.getMotivoretiro().setNombre(motivo);
                } else if (tipoNuevo == 2) {
                    duplicarExperienciaLaboral.getMotivoretiro().setNombre(motivo);
                }
                for (int i = 0; i < listMotivosRetiros.size(); i++) {
                    if (listMotivosRetiros.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }

                if (coincidencias == 1) {
                    if (tipoNuevo == 1) {
                        nuevaExperienciaLaboral.setMotivoretiro(listMotivosRetiros.get(indiceUnicoElemento));
                        context.update("formularioDialogos:nuevaMotivoEP");
                    } else if (tipoNuevo == 2) {
                        duplicarExperienciaLaboral.setMotivoretiro(listMotivosRetiros.get(indiceUnicoElemento));
                        context.update("formularioDialogos:duplicarMotivoEP");
                    }
                    listMotivosRetiros = null;
                    getListMotivosRetiros();
                } else {
                    context.update("form:MotivosDialogo");
                    context.execute("MotivosDialogo.show()");
                    tipoActualizacion = tipoNuevo;
                    if (tipoNuevo == 1) {
                        context.update("formularioDialogos:nuevaMotivoEP");
                    } else if (tipoNuevo == 2) {
                        context.update("formularioDialogos:duplicarMotivoEP");
                    }
                }
            } else {
                listMotivosRetiros = null;
                getListMotivosRetiros();
                if (tipoNuevo == 1) {
                    nuevaExperienciaLaboral.setMotivoretiro(new MotivosRetiros());
                    context.update("formularioDialogos:nuevaMotivoEP");
                } else if (tipoNuevo == 2) {
                    duplicarExperienciaLaboral.setMotivoretiro(new MotivosRetiros());
                    context.update("formularioDialogos:duplicarMotivoEP");
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
            context.update("form:editarLogrosEP");
            cambiosLogros = false;
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
    }

    public void adicionCambiarIndiceModificar(HvExperienciasLaborales experienciaLaboral, int celda) {
        cambiarIndice(experienciaLaboral, celda);
        modificarExperiencia(experienciaLaboral);
        experienciaTablaSeleccionada = experienciaLaboral;
    }

    public void cambiarIndice(HvExperienciasLaborales experienciaLaboral, int celda) {
        if (cambiosLogros == true) {
            if (permitirIndex == true) {
                experienciaTablaSeleccionada = experienciaLaboral;
                cualCelda = celda;
                experienciaTablaSeleccionada.getSecuencia();
                if (tipoLista == 0) {
                    fechaFin = experienciaTablaSeleccionada.getFechahasta();
                    fechaIni = experienciaTablaSeleccionada.getFechadesde();
                    if (cualCelda == 1) {
                        if (experienciaTablaSeleccionada.getFechadesde() != null) {
                            fechaDesde = experienciaTablaSeleccionada.getFechadesde();
                        }
                    }
                    sector = experienciaTablaSeleccionada.getSectoreconomico().getDescripcion();
                    motivo = experienciaTablaSeleccionada.getMotivoretiro().getNombre();

                    logrosAlcanzados = experienciaTablaSeleccionada.getAlcance();
                } else {
                    int ind = listExperienciaLaboralEmpl.indexOf(experienciaTablaSeleccionada);
                    fechaFin = listExperienciaLaboralEmpl.get(ind).getFechahasta();
                    fechaIni = listExperienciaLaboralEmpl.get(ind).getFechadesde();
                    if (cualCelda == 1) {
                        if (listExperienciaLaboralEmpl.get(ind).getFechadesde() != null) {
                            fechaDesde = listExperienciaLaboralEmpl.get(ind).getFechadesde();
                        }
                    }
                    sector = experienciaTablaSeleccionada.getSectoreconomico().getDescripcion();
                    motivo = experienciaTablaSeleccionada.getMotivoretiro().getNombre();

                    logrosAlcanzados = experienciaTablaSeleccionada.getAlcance();
                }
                RequestContext context = RequestContext.getCurrentInstance();
                context.update("form:editarLogrosEP");
            }
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("confirmarGuardar.show()");
        }
    }

    public void guardadoGeneral() {
        guardarCambios();
    }

    public void guardarCambios() {
        RequestContext context = RequestContext.getCurrentInstance();
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
                contarRegistros();
                context.update("form:informacionRegistro");
                context.update("form:datosExperiencia");
                k = 0;
                experienciaTablaSeleccionada = null;
                experienciaTablaSeleccionada = null;
                cambiosLogros = true;

                guardado = true;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
                FacesMessage msg = new FacesMessage("Información", "Se gurdarón los datos con éxito");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                context.update("form:growl");
            }
        } catch (Exception e) {
            System.out.println("Error guardarCambios : " + e.toString());
            FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado, intente nuevamente");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            context.update("form:growl");
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
        listMotivosRetiros = null;
        listSectoresEconomicos = null;
        listExperienciaLaboralBorrar.clear();
        listExperienciaLaboralCrear.clear();
        listExperienciaLaboralModificar.clear();
        experienciaTablaSeleccionada = null;
        experienciaTablaSeleccionada = null;
        k = 0;
        listExperienciaLaboralEmpl = null;
        guardado = true;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext context = RequestContext.getCurrentInstance();
        getListExperienciaLaboralEmpl();
        contarRegistros();
        context.update("form:informacionRegistro");
        context.update("form:datosExperiencia");
        logrosAlcanzados = " ";
        context.update("form:editarLogrosEP");

        cambiosLogros = true;
        nuevaExperienciaLaboral = new HvExperienciasLaborales();
        nuevaExperienciaLaboral.setSectoreconomico(new SectoresEconomicos());
        nuevaExperienciaLaboral.setMotivoretiro(new MotivosRetiros());
        fechaFin = null;
        fechaIni = null;
    }

    public void editarCelda() {
//        if (cualCelda == 1 || cualCelda == 2) {
//            index = indexAux;
//        }
        if (experienciaTablaSeleccionada != null) {
            if (tipoLista == 0) {
                editarExperienciaLaboral = experienciaTablaSeleccionada;
            }
            if (tipoLista == 1) {
                editarExperienciaLaboral = experienciaTablaSeleccionada;
            }
            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCelda == 0) {
                context.update("formularioDialogos:editarEmpresaD");
                context.execute("editarEmpresaD.show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                context.update("formularioDialogos:editarFechaInicialD");
                context.execute("editarFechaInicialD.show()");
                cualCelda = -1;
            } else if (cualCelda == 2) {
                context.update("formularioDialogos:editarFechaFinalD");
                context.execute("editarFechaFinalD.show()");
                cualCelda = -1;
            } else if (cualCelda == 3) {
                context.update("formularioDialogos:editarCargoD");
                context.execute("editarCargoD.show()");
                cualCelda = -1;
            } else if (cualCelda == 4) {
                context.update("formularioDialogos:editarJefeD");
                context.execute("editarJefeD.show()");
                cualCelda = -1;
            } else if (cualCelda == 5) {
                context.update("formularioDialogos:editarTelefonoD");
                context.execute("editarTelefonoD.show()");
                cualCelda = -1;
            } else if (cualCelda == 6) {
                context.update("formularioDialogos:editarSectorD");
                context.execute("editarSectorD.show()");
                cualCelda = -1;
            } else if (cualCelda == 7) {
                context.update("formularioDialogos:editarMotivoD");
                context.execute("editarMotivoD.show()");
                cualCelda = -1;
            } else if (cualCelda == 8) {
                context.update("formularioDialogos:editarLogroD");
                context.execute("editarLogroD.show()");
                cualCelda = -1;
            }
        }
        experienciaTablaSeleccionada = null;
        experienciaTablaSeleccionada = null;
    }

    public void agregarNuevaE() {
        if (nuevaExperienciaLaboral.getFechadesde() != null) {
            fechaDesdeText = formatoFecha.format(nuevaExperienciaLaboral.getFechadesde());
        } else {
            fechaDesdeText = "";
        }
        boolean respuesta = validarFechasRegistro(1);
        if (respuesta == true) {
            if (validarCamposRegistro(1) == true) {
                //CERRAR FILTRADO
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
                //AGREGAR REGISTRO A LA LISTA VIGENCIAS
                k++;
                l = BigInteger.valueOf(k);
                nuevaExperienciaLaboral.setSecuencia(l);
                nuevaExperienciaLaboral.setHojadevida(hojaVida);
                listExperienciaLaboralEmpl.add(nuevaExperienciaLaboral);
                listExperienciaLaboralCrear.add(nuevaExperienciaLaboral);
                //
                nuevaExperienciaLaboral = new HvExperienciasLaborales();
                nuevaExperienciaLaboral.setSectoreconomico(new SectoresEconomicos());
                nuevaExperienciaLaboral.setMotivoretiro(new MotivosRetiros());
                limpiarNuevaE();
                //
                if (guardado == true) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
                experienciaTablaSeleccionada = null;
                experienciaTablaSeleccionada = null;
                RequestContext context = RequestContext.getCurrentInstance();
                modificarInfoRegistro(listExperienciaLaboralEmpl.size());
                context.update("form:informacionRegistro");
                context.update("form:datosExperiencia");
                context.execute("NuevoRegistro.hide()");
            } else {
                RequestContext context = RequestContext.getCurrentInstance();
                context.execute("errorIngresoReg.show()");
            }
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("errorFechasIngresoReg.show()");
        }
    }

    public void limpiarNuevaE() {
        nuevaExperienciaLaboral = new HvExperienciasLaborales();
        nuevaExperienciaLaboral.setSectoreconomico(new SectoresEconomicos());
        nuevaExperienciaLaboral.setMotivoretiro(new MotivosRetiros());

    }

    public void cancelarNuevaE() {
        nuevaExperienciaLaboral = new HvExperienciasLaborales();
        nuevaExperienciaLaboral.setSectoreconomico(new SectoresEconomicos());
        nuevaExperienciaLaboral.setMotivoretiro(new MotivosRetiros());

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
                    context.execute("seleccionarRegistro.show()");
                }
            } else {
                RequestContext context = RequestContext.getCurrentInstance();
                context.execute("seleccionarRegistro.show()");
            }
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("seleccionarRegistro.show()");
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
            context.update("formularioDialogos:duplicarEP");
            context.execute("DuplicarRegistro.show()");
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
            if (validarCamposRegistro(2) == true) {
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
                modificarInfoRegistro(listExperienciaLaboralEmpl.size());
                context.update("form:informacionRegistro");
                context.update("form:datosExperiencia");
                context.update("form:editarLogrosEP");
                context.execute("DuplicarRegistro.hide()");
            } else {
                RequestContext context = RequestContext.getCurrentInstance();
                context.execute("errorIngresoReg.show()");
            }
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("errorFechasIngresoReg.show()");
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
            context.execute("errorBorradoReg.show()");
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
            modificarInfoRegistro(listExperienciaLaboralEmpl.size());
            context.update("form:informacionRegistro");
            context.update("form:datosExperiencia");
            context.update("form:editarLogrosEP");
            experienciaTablaSeleccionada = null;
            experienciaTablaSeleccionada = null;
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
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
            expEmpresa.setFilterStyle("width: 85%;");
            expCargoDes = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expCargoDes");
            expCargoDes.setFilterStyle("width: 85%;");
            expJefe = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expJefe");
            expJefe.setFilterStyle("width: 85%;");
            expTelefono = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expTelefono");
            expTelefono.setFilterStyle("width: 85%;");
            expSectorEco = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expSectorEco");
            expSectorEco.setFilterStyle("width: 85%;");
            expMotivos = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expMotivos");
            expMotivos.setFilterStyle("width: 85%;");
            expFechaInicio = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expFechaInicio");
            expFechaInicio.setFilterStyle("width: 85%;");
            expFechaRetiro = (Column) c.getViewRoot().findComponent("form:datosExperiencia:expFechaRetiro");
            expFechaRetiro.setFilterStyle("width: 85%;");
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
        listMotivosRetiros = null;
        listSectoresEconomicos = null;
        guardado = true;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        tipoActualizacion = -1;
        fechaDesde = null;
        fechaDesdeText = "";
        fechaFin = null;
        fechaHastaText = "";
        fechaIni = null;
        logrosAlcanzados = "";
        hojaVida = null;
        cambiosLogros = true;
        permitirIndex = true;
        empleado = null;

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
            context.update("form:SectorDialogo");
            context.execute("SectorDialogo.show()");
        } else if (dlg == 2) {
            context.update("form:MotivosDialogo");
            context.execute("MotivosDialogo.show()");
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
            context.update("form:datosExperiencia");
        } else if (tipoActualizacion == 1) {
            nuevaExperienciaLaboral.setSectoreconomico(sectorSeleccionada);
            context.update("formularioDialogos:nuevaSectorEP");
        } else if (tipoActualizacion == 2) {
            duplicarExperienciaLaboral.setSectoreconomico(sectorSeleccionada);
            context.update("formularioDialogos:duplicarSectorEP");
        }
        filtrarListSectoresEconomicos = null;
        sectorSeleccionada = null;
        aceptar = true;
        experienciaTablaSeleccionada = null;
        experienciaTablaSeleccionada = null;
        tipoActualizacion = -1;
        /*
         context.update("form:SectorDialogo");
         context.update("form:lovSector");
         context.update("form:aceptarS");*/
        context.reset("form:lovSector:globalFilter");
        context.execute("lovSector.clearFilters()");
        context.execute("SectorDialogo.hide()");
    }

    public void cancelarCambioSector() {
        filtrarListSectoresEconomicos = null;
        sectorSeleccionada = null;
        aceptar = true;
        experienciaTablaSeleccionada = null;
        experienciaTablaSeleccionada = null;
        tipoActualizacion = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovSector:globalFilter");
        context.execute("lovSector.clearFilters()");
        context.execute("SectorDialogo.hide()");
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
            context.update("form:datosExperiencia");
        } else if (tipoActualizacion == 1) {
            nuevaExperienciaLaboral.setMotivoretiro(motivoSeleccionado);
            context.update("formularioDialogos:nuevaMotivoEP");
        } else if (tipoActualizacion == 2) {
            duplicarExperienciaLaboral.setMotivoretiro(motivoSeleccionado);
            context.update("formularioDialogos:duplicarMotivoEP");
        }
        filtrarListMotivosRetiros = null;
        motivoSeleccionado = null;
        aceptar = true;
        experienciaTablaSeleccionada = null;
        experienciaTablaSeleccionada = null;
        tipoActualizacion = -1;
        /*
         context.update("form:MotivosDialogo");
         context.update("form:lovMotivos");
         context.update("form:aceptarM");*/
        context.reset("form:lovMotivos:globalFilter");
        context.execute("lovMotivos.clearFilters()");
        context.execute("MotivosDialogo.hide()");
    }

    public void cancelarCambioMotivo() {
        filtrarListMotivosRetiros = null;
        motivoSeleccionado = null;
        aceptar = true;
        experienciaTablaSeleccionada = null;
        experienciaTablaSeleccionada = null;
        tipoActualizacion = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovMotivos:globalFilter");
        context.execute("lovMotivos.clearFilters()");
        context.execute("MotivosDialogo.hide()");
    }

    public void listaValoresBoton() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (experienciaTablaSeleccionada != null) {
            if (cualCelda == 6) {
                context.update("form:SectorDialogo");
                context.execute("SectorDialogo.show()");
                tipoActualizacion = 0;
            }
            if (cualCelda == 7) {
                context.update("form:MotivosDialogo");
                context.execute("MotivosDialogo.show()");
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
        experienciaTablaSeleccionada = null;
        experienciaTablaSeleccionada = null;
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
        experienciaTablaSeleccionada = null;
        experienciaTablaSeleccionada = null;
    }

    public void eventoFiltrar() {
        if (experienciaTablaSeleccionada != null) {
            if (tipoLista == 0) {
                tipoLista = 1;
            }
            RequestContext context = RequestContext.getCurrentInstance();
            modificarInfoRegistro(filtrarListExperienciaLaboralEmpl.size());
            context.update("form:informacionRegistro");
        }
    }

    public void modificarInfoRegistro(int valor) {
        infoRegistro = String.valueOf(valor);
    }

    public void contarRegistros() {
        if (listExperienciaLaboralEmpl != null) {
            modificarInfoRegistro(listExperienciaLaboralEmpl.size());
        } else {
            modificarInfoRegistro(0);
        }
    }

    public void eventoFiltrarMotivo() {
        modificarInfoRegistroMotivo(filtrarListMotivosRetiros.size());
        RequestContext.getCurrentInstance().update("info:infoRegistroMotivo");
    }

    public void eventoFiltrarSector() {
        modificarInfoRegistroSector(filtrarListSectoresEconomicos.size());
        RequestContext.getCurrentInstance().update("info:infoRegistroSector");
    }

    public void modificarInfoRegistroMotivo(int valor) {
        infoRegistroMotivo = String.valueOf(valor);
    }

    public void modificarInfoRegistroSector(int valor) {
        infoRegistroSector = String.valueOf(valor);
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
            if (administrarRastros.verificarHistoricosTabla("HVEXPERIENCIASLABORALES")) {
                context.execute("confirmarRastroHistorico.show()");
            } else {
                context.execute("errorRastroHistorico.show()");
            }

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

    public List<SectoresEconomicos> getListSectoresEconomicos() {
        try {
            if (listSectoresEconomicos == null) {
                listSectoresEconomicos = new ArrayList<SectoresEconomicos>();
                listSectoresEconomicos = administrarPerExperienciaLaboral.listSectoresEconomicos();
            }
            return listSectoresEconomicos;
        } catch (Exception e) {
            System.out.println("Error getListEmpresas " + e.toString());
            return null;
        }
    }

    public void setListSectoresEconomicos(List<SectoresEconomicos> setListSectoresEconomicos) {
        this.listSectoresEconomicos = setListSectoresEconomicos;
    }

    public SectoresEconomicos getSectorSeleccionada() {
        return sectorSeleccionada;
    }

    public void setSectorSeleccionada(SectoresEconomicos setSectorSeleccionada) {
        this.sectorSeleccionada = setSectorSeleccionada;
    }

    public List<SectoresEconomicos> getFiltrarListSectoresEconomicos() {
        return filtrarListSectoresEconomicos;
    }

    public void setFiltrarListSectoresEconomicos(List<SectoresEconomicos> setFiltrarListSectoresEconomicos) {
        this.filtrarListSectoresEconomicos = setFiltrarListSectoresEconomicos;
    }

    public List<MotivosRetiros> getListMotivosRetiros() {
        try {
            if (listMotivosRetiros == null) {
                listMotivosRetiros = new ArrayList<MotivosRetiros>();
                listMotivosRetiros = administrarPerExperienciaLaboral.listMotivosRetiros();
            }
            return listMotivosRetiros;
        } catch (Exception e) {
            System.out.println("Error getListPryClientes " + e.toString());
            return null;
        }
    }

    public void setListMotivosRetiros(List<MotivosRetiros> setListMotivosRetiros) {
        this.listMotivosRetiros = setListMotivosRetiros;
    }

    public MotivosRetiros getMotivoSeleccionado() {
        return motivoSeleccionado;
    }

    public void setMotivoSeleccionado(MotivosRetiros setMotivoSeleccionado) {
        this.motivoSeleccionado = setMotivoSeleccionado;
    }

    public List<MotivosRetiros> getFiltrarListMotivosRetiros() {
        return filtrarListMotivosRetiros;
    }

    public void setFiltrarListMotivosRetiros(List<MotivosRetiros> setFiltrarListMotivosRetiros) {
        this.filtrarListMotivosRetiros = setFiltrarListMotivosRetiros;
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
        getListExperienciaLaboralEmpl();
        if (listExperienciaLaboralEmpl != null) {
            int tam = listExperienciaLaboralEmpl.size();
            if (tam > 0) {
                experienciaTablaSeleccionada = listExperienciaLaboralEmpl.get(0);
            }
        }
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
        getListSectoresEconomicos();
        return infoRegistroSector;
    }

    public void setInfoRegistroSector(String infoRegistroSector) {
        this.infoRegistroSector = infoRegistroSector;
    }

    public String getInfoRegistroMotivo() {
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
