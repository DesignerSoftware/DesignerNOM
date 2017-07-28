package Administrar;

import Entidades.Cargos;
import Entidades.Ciudades;
import Entidades.Demandas;
import Entidades.Departamentos;
import Entidades.Direcciones;
import Entidades.Empleados;
import Entidades.Encargaturas;
import Entidades.EvalResultadosConv;
//import Entidades.Familiares;
import Entidades.Generales;
import Entidades.HVHojasDeVida;
import Entidades.HvEntrevistas;
import Entidades.HvExperienciasLaborales;
import Entidades.HvReferencias;
import Entidades.IdiomasPersonas;
import Entidades.InformacionesAdicionales;
import Entidades.Personas;
import Entidades.Telefonos;
import Entidades.TiposDocumentos;
import Entidades.VigenciasAficiones;
import Entidades.VigenciasDeportes;
import Entidades.VigenciasDomiciliarias;
import Entidades.VigenciasEstadosCiviles;
import Entidades.VigenciasEventos;
import Entidades.VigenciasFormales;
import Entidades.VigenciasIndicadores;
import Entidades.VigenciasProyectos;
import InterfaceAdministrar.AdministrarEmpleadoIndividualInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaCargosInterface;
import InterfacePersistencia.PersistenciaCiudadesInterface;
import InterfacePersistencia.PersistenciaDemandasInterface;
import InterfacePersistencia.PersistenciaDireccionesInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaEncargaturasInterface;
import InterfacePersistencia.PersistenciaEvalResultadosConvInterface;
import InterfacePersistencia.PersistenciaFamiliaresInterface;
import InterfacePersistencia.PersistenciaGeneralesInterface;
import InterfacePersistencia.PersistenciaHVHojasDeVidaInterface;
import InterfacePersistencia.PersistenciaHvEntrevistasInterface;
import InterfacePersistencia.PersistenciaHvExperienciasLaboralesInterface;
import InterfacePersistencia.PersistenciaHvReferenciasInterface;
import InterfacePersistencia.PersistenciaIdiomasPersonasInterface;
import InterfacePersistencia.PersistenciaInformacionesAdicionalesInterface;
import InterfacePersistencia.PersistenciaPersonasInterface;
import InterfacePersistencia.PersistenciaTelefonosInterface;
import InterfacePersistencia.PersistenciaTiposDocumentosInterface;
import InterfacePersistencia.PersistenciaVigenciasAficionesInterface;
import InterfacePersistencia.PersistenciaVigenciasDeportesInterface;
import InterfacePersistencia.PersistenciaVigenciasDomiciliariasInterface;
import InterfacePersistencia.PersistenciaVigenciasEstadosCivilesInterface;
import InterfacePersistencia.PersistenciaVigenciasEventosInterface;
import InterfacePersistencia.PersistenciaVigenciasFormalesInterface;
import InterfacePersistencia.PersistenciaVigenciasIndicadoresInterface;
import InterfacePersistencia.PersistenciaVigenciasProyectosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;

@Stateful
public class AdministrarEmpleadoIndividual implements AdministrarEmpleadoIndividualInterface {

   private static Logger log = Logger.getLogger(AdministrarEmpleadoIndividual.class);

    @EJB
    PersistenciaHVHojasDeVidaInterface persistenciaHVHojasDeVida;
    @EJB
    PersistenciaTelefonosInterface persistenciaTelefonos;
    @EJB
    PersistenciaDireccionesInterface persistenciaDirecciones;
    @EJB
    PersistenciaVigenciasEstadosCivilesInterface persistenciaVigenciasEstadosCiviles;
    @EJB
    PersistenciaInformacionesAdicionalesInterface persistenciaInformacionesAdicionales;
    @EJB
    PersistenciaEncargaturasInterface persistenciaEncargaturas;
    @EJB
    PersistenciaVigenciasFormalesInterface persistenciaVigenciasFormales;
    @EJB
    PersistenciaIdiomasPersonasInterface persistenciaIdiomasPersonas;
    @EJB
    PersistenciaVigenciasProyectosInterface persistenciaVigenciasProyectos;
    @EJB
    PersistenciaHvReferenciasInterface persistenciaHvReferencias;
    @EJB
    PersistenciaHvExperienciasLaboralesInterface persistenciaHvExperienciasLaborales;
    @EJB
    PersistenciaVigenciasEventosInterface persistenciaVigenciasEventos;
    @EJB
    PersistenciaVigenciasDeportesInterface persistenciaVigenciasDeportes;
    @EJB
    PersistenciaVigenciasAficionesInterface persistenciaVigenciasAficiones;
    @EJB
    PersistenciaFamiliaresInterface persistenciaFamiliares;
    @EJB
    PersistenciaHvEntrevistasInterface persistenciaHvEntrevistas;
    @EJB
    PersistenciaVigenciasIndicadoresInterface persistenciaVigenciasIndicadores;
    @EJB
    PersistenciaDemandasInterface persistenciaDemandas;
    @EJB
    PersistenciaVigenciasDomiciliariasInterface persistenciaVigenciasDomiciliarias;
    @EJB
    PersistenciaEvalResultadosConvInterface persistenciaEvalResultadosConv;
    @EJB
    PersistenciaTiposDocumentosInterface persistenciaTiposDocumentos;
    @EJB
    PersistenciaCiudadesInterface persistenciaCiudades;
    @EJB
    PersistenciaCargosInterface persistenciaCargos;
    @EJB
    PersistenciaEmpleadoInterface persistenciaEmpleado;
    @EJB
    PersistenciaPersonasInterface persistenciaPersonas;
    @EJB
    PersistenciaGeneralesInterface persistenciaGenerales;
    /**
     * Enterprise JavaBean.<br>
     * Atributo que representa todo lo referente a la conexión del usuario que
     * está usando el aplicativo.
     */
    @EJB
    AdministrarSesionesInterface administrarSesiones;

    private EntityManager em;
    private Generales general;

    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    @Override
    public HVHojasDeVida hvHojaDeVidaPersona(BigInteger secPersona) {
        return persistenciaHVHojasDeVida.hvHojaDeVidaPersona(em, secPersona);
    }

    @Override
    public Telefonos primerTelefonoPersona(BigInteger secPersona) {
        try {
            List<Telefonos> listaTelefonos;
            listaTelefonos = persistenciaTelefonos.telefonosPersona(em, secPersona);
            if (listaTelefonos != null && !listaTelefonos.isEmpty()) {
                return listaTelefonos.get(0);
            } else {
                return null;
            }
        } catch (Exception e) {
            log.warn("error en Primer Telefono Persona :" + e.getMessage());
            return null;
        }
    }

    @Override
    public Direcciones primeraDireccionPersona(BigInteger secPersona) {
        try {
            List<Direcciones> listaDirecciones;
            listaDirecciones = persistenciaDirecciones.direccionPersona(em, secPersona);
            if (listaDirecciones != null && !listaDirecciones.isEmpty()) {
                return listaDirecciones.get(0);
            } else {
                return null;
            }
        } catch (Exception e) {
            log.warn("error en Primera Dirección Persona : " + e.getMessage());
            return null;
        }
    }

    @Override
    public VigenciasEstadosCiviles estadoCivilPersona(BigInteger secPersona) {
        try {
            List<VigenciasEstadosCiviles> listaVigenciasEstadosCiviles;
            listaVigenciasEstadosCiviles = persistenciaVigenciasEstadosCiviles.consultarVigenciasEstadosCivilesPersona(em, secPersona);
            if (listaVigenciasEstadosCiviles != null && !listaVigenciasEstadosCiviles.isEmpty()) {
                return listaVigenciasEstadosCiviles.get(0);
            } else {
                return null;
            }
        } catch (Exception e) {
            log.warn("error es estadoCivilPersona : " + e.getMessage());
            return null;
        }

    }

    @Override
    public InformacionesAdicionales informacionAdicionalPersona(BigInteger secEmpleado) {
        try {
            List<InformacionesAdicionales> listaInformacionesAdicionales;
            listaInformacionesAdicionales = persistenciaInformacionesAdicionales.informacionAdicionalPersona(em, secEmpleado);
            if (listaInformacionesAdicionales != null && !listaInformacionesAdicionales.isEmpty()) {
                return listaInformacionesAdicionales.get(0);
            } else {
                return null;
            }
        } catch (Exception e) {
            log.warn("error en informaciónAdicionalPersona : " + e.getMessage());
            return null;
        }
    }

    @Override
    public Encargaturas reemplazoPersona(BigInteger secEmpleado) {
        try {
            List<Encargaturas> listaEncargaturas;
            listaEncargaturas = persistenciaEncargaturas.reemplazoPersona(em, secEmpleado);
            if (listaEncargaturas != null && !listaEncargaturas.isEmpty()) {
                return listaEncargaturas.get(0);
            } else {
                return null;
            }
        } catch (Exception e) {
            log.warn("error en reemplazoPersona : " + e.getMessage());
            return null;
        }

    }

    @Override
    public VigenciasFormales educacionPersona(BigInteger secPersona) {
        try {
            List<VigenciasFormales> listaVigenciasFormales;
            listaVigenciasFormales = persistenciaVigenciasFormales.educacionPersona(em, secPersona);
            if (listaVigenciasFormales != null && !listaVigenciasFormales.isEmpty()) {
                return listaVigenciasFormales.get(0);
            } else {
                return null;
            }
        } catch (Exception e) {
            log.warn("errro en educacionPersona : " + e.getMessage());
            return null;
        }
    }

    @Override
    public IdiomasPersonas idiomasPersona(BigInteger secPersona) {
        try {
            List<IdiomasPersonas> listaIdiomasPersonas;
            listaIdiomasPersonas = persistenciaIdiomasPersonas.idiomasPersona(em, secPersona);
            if (listaIdiomasPersonas != null && !listaIdiomasPersonas.isEmpty()) {
                return listaIdiomasPersonas.get(0);
            } else {
                return null;
            }
        } catch (Exception e) {
            log.warn("error en idiomasPersona : " + e.getMessage());
            return null;
        }
    }

    @Override
    public VigenciasProyectos proyectosPersona(BigInteger secEmpleado) {
        try {
            List<VigenciasProyectos> listaVigenciasProyectos;
            listaVigenciasProyectos = persistenciaVigenciasProyectos.proyectosEmpleado(em, secEmpleado);
            if (listaVigenciasProyectos != null && !listaVigenciasProyectos.isEmpty()) {
                return listaVigenciasProyectos.get(0);
            } else {
                return null;
            }
        } catch (Exception e) {
            log.warn("error en proyectosPersona  :" + e.getMessage());
            return null;
        }
    }

    @Override
    public HvReferencias referenciasPersonalesPersona(BigInteger secHv) {
        try {
            List<HvReferencias> listaReferenciasPersonales;
            listaReferenciasPersonales = persistenciaHvReferencias.referenciasPersonalesPersona(em, secHv);
            if (listaReferenciasPersonales != null && !listaReferenciasPersonales.isEmpty()) {
                return listaReferenciasPersonales.get(0);
            } else {
                return null;
            }
        } catch (Exception e) {
            log.warn("error en referenciasPersonalesPersona : " + e.getMessage());
            return null;
        }
    }

    @Override
    public HvReferencias referenciasFamiliaresPersona(BigInteger secHv) {
        try {
            List<HvReferencias> listaReferenciasPersonales;
            listaReferenciasPersonales = persistenciaHvReferencias.contarReferenciasFamiliaresPersona(em, secHv);
            if (listaReferenciasPersonales != null && !listaReferenciasPersonales.isEmpty()) {
                return listaReferenciasPersonales.get(0);
            } else {
                return null;
            }
        } catch (Exception e) {
            log.warn("error en referenciasFamiliaresPersona  : " + e.getMessage());
            return null;
        }
    }

    @Override
    public HvExperienciasLaborales experienciaLaboralPersona(BigInteger secHv) {
        try {
            List<HvExperienciasLaborales> listaExperienciaLaboral;
            listaExperienciaLaboral = persistenciaHvExperienciasLaborales.experienciaLaboralPersona(em, secHv);
            if (listaExperienciaLaboral != null && !listaExperienciaLaboral.isEmpty()) {
                return listaExperienciaLaboral.get(0);
            } else {
                return null;
            }
        } catch (Exception e) {
            log.warn("error en experienciaLaboralPersona : " + e.getMessage());
            return null;
        }
    }

    @Override
    public VigenciasEventos eventosPersona(BigInteger secEmpl) {
        try {
            List<VigenciasEventos> listaVigenciasEventos;
            listaVigenciasEventos = persistenciaVigenciasEventos.eventosEmpleado(em, secEmpl);
            if (listaVigenciasEventos != null && !listaVigenciasEventos.isEmpty()) {
                return listaVigenciasEventos.get(0);
            } else {
                return null;
            }
        } catch (Exception e) {
            log.warn("error en eventosPersona : " + e.getMessage());
            return null;
        }
    }

    @Override
    public VigenciasDeportes deportesPersona(BigInteger secPersona) {
        try {
            List<VigenciasDeportes> listaVigenciasDeportes;
            listaVigenciasDeportes = persistenciaVigenciasDeportes.deportePersona(em, secPersona);
            if (listaVigenciasDeportes != null && !listaVigenciasDeportes.isEmpty()) {
                return listaVigenciasDeportes.get(0);
            } else {
                return null;
            }
        } catch (Exception e) {
            log.warn("error en deportesPersona : " + e.getMessage());
            return null;
        }
    }

    @Override
    public VigenciasAficiones aficionesPersona(BigInteger secPersona) {
        try {
            List<VigenciasAficiones> listaVigenciasAficiones;
            listaVigenciasAficiones = persistenciaVigenciasAficiones.aficionesPersona(em, secPersona);
            if (listaVigenciasAficiones != null && !listaVigenciasAficiones.isEmpty()) {
                return listaVigenciasAficiones.get(0);
            } else {
                return null;
            }
        } catch (Exception e) {
            log.warn("error en aficionesPersona  : " + e.getMessage());
            return null;
        }
    }

    @Override
    public String consultaFamiliaresPersona(BigInteger secPersona) {
        String resultado = " ";
        try {
            resultado = persistenciaFamiliares.consultaFamiliar(em, secPersona);
            return resultado;
        } catch (Exception e) {
            log.warn("error en consultaFamiliaresPersona : " + e.getMessage());
            return resultado;
        }
    }

    @Override
    public HvEntrevistas entrevistasPersona(BigInteger secHv) {
        try {
            List<HvEntrevistas> listaEntrevistas;
            listaEntrevistas = persistenciaHvEntrevistas.entrevistasPersona(em, secHv);
            if (listaEntrevistas != null && !listaEntrevistas.isEmpty()) {
                return listaEntrevistas.get(0);
            } else {
                return null;
            }
        } catch (Exception e) {
            log.warn("error en entrevistasPersona : " + e.getMessage());
            return null;
        }
    }

    @Override
    public VigenciasIndicadores indicadoresPersona(BigInteger secEmpl) {
        try {
            List<VigenciasIndicadores> listaVigenciasIndicadores;
            listaVigenciasIndicadores = persistenciaVigenciasIndicadores.ultimosIndicadoresEmpleado(em, secEmpl);
            if (listaVigenciasIndicadores != null && !listaVigenciasIndicadores.isEmpty()) {
                return listaVigenciasIndicadores.get(0);
            } else {
                return null;
            }
        } catch (Exception e) {
            log.warn("error en Indicadores Persona : " + e.getMessage());
            return null;
        }
    }

    @Override
    public Demandas demandasPersona(BigInteger secEmpl) {
        try {
            List<Demandas> listaDemandas;
            listaDemandas = persistenciaDemandas.demandasPersona(em, secEmpl);
            if (listaDemandas != null && !listaDemandas.isEmpty()) {
                return listaDemandas.get(0);
            } else {
                return null;
            }
        } catch (Exception e) {
            log.warn("error en demanda Persona : " + e.getMessage());
            return null;
        }
    }

    @Override
    public VigenciasDomiciliarias visitasDomiciliariasPersona(BigInteger secPersona) {
        try {
            List<VigenciasDomiciliarias> listaVigenciasDomiciliarias;
            listaVigenciasDomiciliarias = persistenciaVigenciasDomiciliarias.visitasDomiciliariasPersona(em, secPersona);
            if (listaVigenciasDomiciliarias != null && !listaVigenciasDomiciliarias.isEmpty()) {
                return listaVigenciasDomiciliarias.get(0);
            } else {
                return null;
            }
        } catch (Exception e) {
            log.warn("error en visitasDomiciliariasPersona : " + e.getMessage());
            return null;
        }
    }

    @Override
    public EvalResultadosConv pruebasAplicadasPersona(BigInteger secEmpleado) {
        try {
            List<EvalResultadosConv> listaPruebasAplicadas;
            listaPruebasAplicadas = persistenciaEvalResultadosConv.pruebasAplicadasPersona(em, secEmpleado);
            if (listaPruebasAplicadas != null && !listaPruebasAplicadas.isEmpty()) {
                return listaPruebasAplicadas.get(0);
            } else {
                return null;
            }
        } catch (Exception e) {
            log.warn("error en pruebasAplicadasPersona : " + e.getMessage());
            return null;
        }
    }

    //LOVS
    @Override
    public List<TiposDocumentos> tiposDocumentos() {
        try {
            List<TiposDocumentos> listaTiposDocumentos;
            listaTiposDocumentos = persistenciaTiposDocumentos.consultarTiposDocumentos(em);
            return listaTiposDocumentos;
        } catch (Exception e) {
            log.warn("error en tiposDocumentos  : " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Ciudades> ciudades() {
        try {
            List<Ciudades> listaCiudades;
            listaCiudades = persistenciaCiudades.consultarCiudades(em);
            return listaCiudades;
        } catch (Exception e) {
            log.warn("error en ciudades : " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Cargos> cargos() {
        try {
            List<Cargos> listaCargos;
            listaCargos = persistenciaCargos.consultarCargos(em);
            return listaCargos;
        } catch (Exception e) {
            log.warn("Error Administrar.AdministrarEmpleadoIndividual.cargos() : " + e.getMessage());
            return null;
        }
    }

    @Override
    public Empleados buscarEmpleado(BigInteger secuencia) {
        Empleados empleado;
        try {
            empleado = persistenciaEmpleado.buscarEmpleadoSecuencia(em, secuencia);
            return empleado;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void modificarEmpleado(Empleados empleado) {
        try {
            persistenciaEmpleado.editar(em, empleado);
        } catch (Exception e) {
            log.warn("Error modificando. AdministrarEmpleadoIndividual.modificarEmpleado");
        }
    }

    @Override
    public void modificarHojaDeVida(HVHojasDeVida hojaVida) {
        try {
            persistenciaHVHojasDeVida.editar(em, hojaVida);
        } catch (Exception e) {
            log.warn("Error modificando. AdministrarEmpleadoIndividual.modificarHojaDeVida " + e);
        }
    }

    @Override
    public void modificarPersona(Personas personas) {
        try {
            if (personas.getCiudaddocumento() == null) {
                    personas.setCiudaddocumento(new Ciudades());
                    personas.getCiudaddocumento().setDepartamento(new Departamentos());
            }

            persistenciaPersonas.editar(em, personas);
        } catch (Exception e) {
            log.warn("Error modificando. AdministrarEmpleadoIndividual.modificarPersona : "  + e.getMessage() );
        }
    }

    @Override
    public void actualizarFotoPersona(Personas persona) {
        try {
            //persistenciaPersonas.actualizarFotoPersona(em, identificacion);
            persona.setPathfoto("S");
            persistenciaPersonas.editar(em, persona);
        } catch (Exception e) {
            log.warn("No se puede actalizar el estado de la Foto.");
        }
    }

    public Generales obtenerRutaFoto() {
        try {
            general = persistenciaGenerales.obtenerRutas(em);
            return general;
        } catch (Exception e) {
            log.warn("Error en AdministrarEmpleadoIndividual.obtenerRutaFoto");
            return null;
        }
    }

    @Override
    public String fotoEmpleado(Empleados empleado) {
        try {
            String rutaFoto;
            general = persistenciaGenerales.obtenerRutas(em);
            if (general != null) {
                if (empleado.getPersona().getPathfoto() == null || empleado.getPersona().getPathfoto().equalsIgnoreCase("N")) {
                    rutaFoto = general.getPathfoto() + "sinFoto.jpg";
                } else {
                    rutaFoto = general.getPathfoto() + empleado.getPersona().getNumerodocumento() + ".jpg";
                }
                return rutaFoto;
            } else {
                return null;
            }
        } catch (Exception e) {
            log.warn("Error en foto empleado" + e.getMessage());
            return null;
        }
    }

    @Override
    public Personas encontrarPersona(BigInteger secPersona) {
        try {
            return persistenciaPersonas.buscarPersonaSecuencia(em, secPersona);
        } catch (Exception e) {
            log.warn("error en encontrarPersona : " + e.getMessage());
            return null;
        }
    }

    @Override
    public Personas obtenerPersonaPorEmpleado(BigInteger secPersona) {
        try {
            return persistenciaPersonas.buscarPersonaSecuencia(em, secPersona);
        } catch (Exception e) {
            log.warn("error en obtenerPersonaPorEmpleado : " + e.getMessage());
            return null;
        }
    }

    @Override
    public String consultarPrimerTelefonoPersona(BigInteger secPersona) {
        try {
            String telefono = persistenciaTelefonos.consultarUltimoTelefono(em, secPersona);
            return telefono;
        } catch (Exception e) {
            log.warn("error en consultarPrimerTelefonoPersona : " + e.getMessage());
           return " ";
        }
    }

    @Override
    public String consultarPrimeraDireccionPersona(BigInteger secPersona) {
        try {
            String direccion = persistenciaDirecciones.consultarPrimeraDireccion(em, secPersona);
            return direccion;
        } catch (Exception e) {
            log.warn("error en consultarPrimeraDireccionPersona");
           return " ";
        }
    }

    @Override
    public String consultarPrimerEstadoCivilPersona(BigInteger secPersona) {
        try {
            String estadoCivil = persistenciaVigenciasEstadosCiviles.consultarPrimerEstadoCivil(em, secPersona);
            return estadoCivil;
        } catch (Exception e) {
            log.warn("error en consultarPrimerEstadoCivilPersona : " + e.getMessage());
            return " ";
        }
    }

    @Override
    public String consultarPrimeraInformacionAd(BigInteger secPersona) {
        try {
            String infoAd = persistenciaInformacionesAdicionales.primeraInformacionAdicional(em, secPersona);
            return infoAd;
        } catch (Exception e) {
            log.warn("error en consultarPrimeraInformacionAd : " + e.getMessage());
            return " ";
        }
    }

    @Override
    public String consultarPrimerReemplazo(BigInteger secPersona) {
        try {
            String reemplazo = persistenciaEncargaturas.primeraEncargatura(em, secPersona);
            if(reemplazo == null){
                reemplazo = " ";
            }
            return reemplazo;
        } catch (Exception e) {
            log.warn("error en consultarPrimerReemplazo : " + e.getMessage());
            return " ";
        }
    }

    @Override
    public String consultarPrimeraVigenciaFormal(BigInteger secPersona) {
        try {
            String educacion = persistenciaVigenciasFormales.primeraVigenciaFormal(em, secPersona);
            return educacion;
        } catch (Exception e) {
            log.warn("error en consultarPrimeraVigenciaFormal  : " + e.getMessage());
            return " ";
        }
    }

    @Override
    public String consultarPimerIdioma(BigInteger secPersona) {
        try {
            String idioma = persistenciaIdiomasPersonas.primerIdioma(em, secPersona);
            return idioma;
        } catch (Exception e) {
            log.warn("error en consultarPrimerIdioma : " + e.getMessage());
            return " ";
        }
    }

    @Override
    public String consultarPrimerProyecto(BigInteger secPersona) {
        try {
            String proyecto = persistenciaVigenciasProyectos.primerProyecto(em, secPersona);
            return proyecto;
        } catch (Exception e) {
            log.warn("error en consultarPrimerProyecto : " + e.getMessage());
           return " ";
        }
    }

    @Override
    public String consultarPrimerEvento(BigInteger secPersona) {
        try {
            String evento = persistenciaVigenciasEventos.primerEvento(em, secPersona);
            return evento;
        } catch (Exception e) {
            log.warn("error en consultarPrimerEvento : " + e.getMessage());
            return " ";
        }
    }

    @Override
    public String consultarPrimerDeporte(BigInteger secPersona) {
        try {
            String deporte = persistenciaVigenciasDeportes.consultarPrimerDeporte(em, secPersona);
            return deporte;
        } catch (Exception e) {
            log.warn("error en consultarPrimerDeporte : " + e.getMessage());
            return " ";
        }
    }

    @Override
    public String consultarPrimeraAficion(BigInteger secPersona) {
        try {
            String aficion = persistenciaVigenciasAficiones.primeraAficion(em, secPersona);
            return aficion;
        } catch (Exception e) {
            log.warn("error en consultarPrimeraAfición : " + e.getMessage());
            return " ";
        }
    }

    @Override
    public String consultarPrimerFamiliar(BigInteger secPersona) {
        try {
            String familiar = persistenciaFamiliares.consultarPrimerFamiliar(em, secPersona);
            return familiar;
        } catch (Exception e) {
            log.warn("error en consultarPrimerFamiliar : " + e.getMessage());
            return " ";
        }
    }

    @Override
    public String consultarPrimerIndicador(BigInteger secPersona) {
        try {
            String indicador = persistenciaVigenciasIndicadores.primeraVigenciaIndicador(em, secPersona);
            return indicador;
        } catch (Exception e) {
            log.warn("error en consultarPrimerIndicador : " + e.getMessage());
            return " ";
        }
    }

    @Override
    public String consultarPrimeraDemanda(BigInteger Persona) {
        try {
            String demanda = persistenciaDemandas.primeraDemanda(em, Persona);
            return demanda;
        } catch (Exception e) {
            log.warn("error en consultarPrimeraDemanda : " + e.getMessage());
            return " ";
        }
    }

    @Override
    public String consultarPrimeraVisita(BigInteger secPersona) {
        try {
            String visita = persistenciaVigenciasDomiciliarias.primeraVigenciaDomiciliaria(em, secPersona);
            return visita;
        } catch (Exception e) {
            log.warn("error en consultarPrimeraVisita : " + e.getMessage());
            return " ";
        }
    }

    @Override
    public String consultarPrimeraEntrevista(BigInteger secHV) {
        try {
            String entrevista = persistenciaHvEntrevistas.consultarPrimeraEnterevista(em, secHV);
            return entrevista;
        } catch (Exception e) {
            log.warn("error en consultarPrimeraEntrevista : " + e.getMessage());
            return " ";
        }
    }

    @Override
    public String consultarPrimeraReferenciaF(BigInteger secHV) {
        try {
            String referenciaF = persistenciaHvReferencias.primeraReferenciaFamiliar(em, secHV);
            return referenciaF;
        } catch (Exception e) {
            log.warn("error en consultarPrimeraReferenciaF : " + e.getMessage());
            return " ";
        }
    }

    @Override
    public String consultarPrimeraReferenciaP(BigInteger secHV) {
        try {
            String referenciaP = persistenciaHvReferencias.primeraReferenciaPersonal(em, secHV);
            return referenciaP;
        } catch (Exception e) {
            log.warn("error en consultarPrimeraReferenciaP : " + e.getMessage());
            return " ";
        }
    }

    @Override
    public String consultarPrimeraExpLaboral(BigInteger secHv) {
        try {
            String experiencia = persistenciaHvExperienciasLaborales.primeraExpLaboral(em, secHv);
            return experiencia;
        } catch (Exception e) {
            log.warn("error en consultarPrimeraExpLaboral : " + e.getMessage());
            return " ";
        }
    }

    @Override
    public String consultarPrimeraPrueba(BigInteger secPersona) {
        try {
            String prueba = persistenciaEvalResultadosConv.primerPruebaAplicada(em, secPersona);
            return prueba;
        } catch (Exception e) {
            log.warn("error en consultarPrimeraPrueba : " + e.getMessage());
            return " ";
        }
    }
}
