package Administrar;

import Entidades.Cargos;
import Entidades.Ciudades;
import Entidades.Demandas;
import Entidades.Direcciones;
import Entidades.Empleados;
import Entidades.Empresas;
import Entidades.Encargaturas;
import Entidades.EvalResultadosConv;
import Entidades.Familiares;
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

@Stateful
public class AdministrarEmpleadoIndividual implements AdministrarEmpleadoIndividualInterface {

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
        List<Telefonos> listaTelefonos;
        listaTelefonos = persistenciaTelefonos.telefonosPersona(em, secPersona);
        if (listaTelefonos != null && !listaTelefonos.isEmpty()) {
            return listaTelefonos.get(0);
        } else {
            return null;
        }
    }

    @Override
    public Direcciones primeraDireccionPersona(BigInteger secPersona) {
        List<Direcciones> listaDirecciones;
        listaDirecciones = persistenciaDirecciones.direccionPersona(em, secPersona);
        if (listaDirecciones != null && !listaDirecciones.isEmpty()) {
            return listaDirecciones.get(0);
        } else {
            return null;
        }
    }

    @Override
    public VigenciasEstadosCiviles estadoCivilPersona(BigInteger secPersona) {
        List<VigenciasEstadosCiviles> listaVigenciasEstadosCiviles;
        listaVigenciasEstadosCiviles = persistenciaVigenciasEstadosCiviles.consultarVigenciasEstadosCivilesPersona(em, secPersona);
        if (listaVigenciasEstadosCiviles != null && !listaVigenciasEstadosCiviles.isEmpty()) {
            return listaVigenciasEstadosCiviles.get(0);
        } else {
            return null;
        }
    }

    @Override
    public InformacionesAdicionales informacionAdicionalPersona(BigInteger secEmpleado) {
        List<InformacionesAdicionales> listaInformacionesAdicionales;
        listaInformacionesAdicionales = persistenciaInformacionesAdicionales.informacionAdicionalPersona(em, secEmpleado);
        if (listaInformacionesAdicionales != null && !listaInformacionesAdicionales.isEmpty()) {
            return listaInformacionesAdicionales.get(0);
        } else {
            return null;
        }
    }

    @Override
    public Encargaturas reemplazoPersona(BigInteger secEmpleado) {
        List<Encargaturas> listaEncargaturas;
        listaEncargaturas = persistenciaEncargaturas.reemplazoPersona(em, secEmpleado);
        if (listaEncargaturas != null && !listaEncargaturas.isEmpty()) {
            return listaEncargaturas.get(0);
        } else {
            return null;
        }
    }

    @Override
    public VigenciasFormales educacionPersona(BigInteger secPersona) {
        List<VigenciasFormales> listaVigenciasFormales;
        listaVigenciasFormales = persistenciaVigenciasFormales.educacionPersona(em, secPersona);
        if (listaVigenciasFormales != null && !listaVigenciasFormales.isEmpty()) {
            return listaVigenciasFormales.get(0);
        } else {
            return null;
        }
    }

    @Override
    public IdiomasPersonas idiomasPersona(BigInteger secPersona) {
        List<IdiomasPersonas> listaIdiomasPersonas;
        listaIdiomasPersonas = persistenciaIdiomasPersonas.idiomasPersona(em, secPersona);
        if (listaIdiomasPersonas != null && !listaIdiomasPersonas.isEmpty()) {
            return listaIdiomasPersonas.get(0);
        } else {
            return null;
        }
    }

    @Override
    public VigenciasProyectos proyectosPersona(BigInteger secEmpleado) {
        List<VigenciasProyectos> listaVigenciasProyectos;
        listaVigenciasProyectos = persistenciaVigenciasProyectos.proyectosEmpleado(em, secEmpleado);
        if (listaVigenciasProyectos != null && !listaVigenciasProyectos.isEmpty()) {
            return listaVigenciasProyectos.get(0);
        } else {
            return null;
        }
    }

    @Override
    public HvReferencias referenciasPersonalesPersona(BigInteger secHv) {
        List<HvReferencias> listaReferenciasPersonales;
        listaReferenciasPersonales = persistenciaHvReferencias.referenciasPersonalesPersona(em, secHv);
        if (listaReferenciasPersonales != null && !listaReferenciasPersonales.isEmpty()) {
            return listaReferenciasPersonales.get(0);
        } else {
            return null;
        }
    }

    @Override
    public HvReferencias referenciasFamiliaresPersona(BigInteger secHv) {
        List<HvReferencias> listaReferenciasPersonales;
        listaReferenciasPersonales = persistenciaHvReferencias.contarReferenciasFamiliaresPersona(em, secHv);
        if (listaReferenciasPersonales != null && !listaReferenciasPersonales.isEmpty()) {
            return listaReferenciasPersonales.get(0);
        } else {
            return null;
        }
    }

    @Override
    public HvExperienciasLaborales experienciaLaboralPersona(BigInteger secHv) {
        List<HvExperienciasLaborales> listaExperienciaLaboral;
        listaExperienciaLaboral = persistenciaHvExperienciasLaborales.experienciaLaboralPersona(em, secHv);
        if (listaExperienciaLaboral != null && !listaExperienciaLaboral.isEmpty()) {
            return listaExperienciaLaboral.get(0);
        } else {
            return null;
        }
    }

    @Override
    public VigenciasEventos eventosPersona(BigInteger secEmpl) {
        List<VigenciasEventos> listaVigenciasEventos;
        listaVigenciasEventos = persistenciaVigenciasEventos.eventosEmpleado(em, secEmpl);
        if (listaVigenciasEventos != null && !listaVigenciasEventos.isEmpty()) {
            return listaVigenciasEventos.get(0);
        } else {
            return null;
        }
    }

    @Override
    public VigenciasDeportes deportesPersona(BigInteger secPersona) {
        List<VigenciasDeportes> listaVigenciasDeportes;
        listaVigenciasDeportes = persistenciaVigenciasDeportes.deportePersona(em, secPersona);
        if (listaVigenciasDeportes != null && !listaVigenciasDeportes.isEmpty()) {
            return listaVigenciasDeportes.get(0);
        } else {
            return null;
        }
    }

    @Override
    public VigenciasAficiones aficionesPersona(BigInteger secPersona) {
        List<VigenciasAficiones> listaVigenciasAficiones;
        listaVigenciasAficiones = persistenciaVigenciasAficiones.aficionesPersona(em, secPersona);
        if (listaVigenciasAficiones != null && !listaVigenciasAficiones.isEmpty()) {
            return listaVigenciasAficiones.get(0);
        } else {
            return null;
        }
    }

    @Override
    public Familiares familiaresPersona(BigInteger secPersona) {
        List<Familiares> listaFamiliares;
        listaFamiliares = persistenciaFamiliares.familiaresPersona(em, secPersona);
        if (listaFamiliares != null && !listaFamiliares.isEmpty()) {
            return listaFamiliares.get(0);
        } else {
            return null;
        }
    }

    @Override
    public HvEntrevistas entrevistasPersona(BigInteger secHv) {
        List<HvEntrevistas> listaEntrevistas;
        listaEntrevistas = persistenciaHvEntrevistas.entrevistasPersona(em, secHv);
        if (listaEntrevistas != null && !listaEntrevistas.isEmpty()) {
            return listaEntrevistas.get(0);
        } else {
            return null;
        }
    }

    @Override
    public VigenciasIndicadores indicadoresPersona(BigInteger secEmpl) {
        List<VigenciasIndicadores> listaVigenciasIndicadores;
        listaVigenciasIndicadores = persistenciaVigenciasIndicadores.ultimosIndicadoresEmpleado(em, secEmpl);
        if (listaVigenciasIndicadores != null && !listaVigenciasIndicadores.isEmpty()) {
            return listaVigenciasIndicadores.get(0);
        } else {
            return null;
        }
    }

    @Override
    public Demandas demandasPersona(BigInteger secEmpl) {
        List<Demandas> listaDemandas;
        listaDemandas = persistenciaDemandas.demandasPersona(em, secEmpl);
        if (listaDemandas != null && !listaDemandas.isEmpty()) {
            return listaDemandas.get(0);
        } else {
            return null;
        }
    }

    @Override
    public VigenciasDomiciliarias visitasDomiciliariasPersona(BigInteger secPersona) {
        List<VigenciasDomiciliarias> listaVigenciasDomiciliarias;
        listaVigenciasDomiciliarias = persistenciaVigenciasDomiciliarias.visitasDomiciliariasPersona(em, secPersona);
        if (listaVigenciasDomiciliarias != null && !listaVigenciasDomiciliarias.isEmpty()) {
            return listaVigenciasDomiciliarias.get(0);
        } else {
            return null;
        }
    }

    @Override
    public EvalResultadosConv pruebasAplicadasPersona(BigInteger secEmpleado) {
        List<EvalResultadosConv> listaPruebasAplicadas;
        listaPruebasAplicadas = persistenciaEvalResultadosConv.pruebasAplicadasPersona(em, secEmpleado);
        if (listaPruebasAplicadas != null && !listaPruebasAplicadas.isEmpty()) {
            return listaPruebasAplicadas.get(0);
        } else {
            return null;
        }
    }

    //LOVS
    @Override
    public List<TiposDocumentos> tiposDocumentos() {
        List<TiposDocumentos> listaTiposDocumentos;
        listaTiposDocumentos = persistenciaTiposDocumentos.consultarTiposDocumentos(em);
        return listaTiposDocumentos;
    }

    @Override
    public List<Ciudades> ciudades() {
        List<Ciudades> listaCiudades;
        listaCiudades = persistenciaCiudades.consultarCiudades(em);
        return listaCiudades;
    }

    @Override
    public List<Cargos> cargos() {
        List<Cargos> listaCargos;
        listaCargos = persistenciaCargos.consultarCargos(em);
        return listaCargos;
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
            System.out.println("Error modificando. AdministrarEmpleadoIndividual.modificarEmpleado");
        }
    }

    @Override
    public void modificarHojaDeVida(HVHojasDeVida hojaVida) {
        try {
            persistenciaHVHojasDeVida.editar(em, hojaVida);
        } catch (Exception e) {
            System.out.println("Error modificando. AdministrarEmpleadoIndividual.modificarHojaDeVida " + e);
        }
    }

    @Override
    public void modificarPersona(Personas personas) {
        try {
            if (personas.getFactorrh().equals("")) {
                personas.setFactorrh(null);
            }
            if (personas.getGruposanguineo().equals("")) {
                personas.setGruposanguineo(null);
            }
            if (personas.getViviendapropia().equals("")) {
                personas.setViviendapropia(null);
            }
            persistenciaPersonas.editar(em, personas);
        } catch (Exception e) {
            System.out.println("Error modificando. AdministrarEmpleadoIndividual.modificarPersona");
        }
    }

    @Override
    public void actualizarFotoPersona(Personas persona) {
        try {
            //persistenciaPersonas.actualizarFotoPersona(em, identificacion);
            persona.setPathfoto("S");
            persistenciaPersonas.editar(em, persona);
        } catch (Exception e) {
            System.out.println("No se puede actalizar el estado de la Foto.");
        }
    }

    public Generales obtenerRutaFoto() {
        try {
            general = persistenciaGenerales.obtenerRutas(em);
            return general;
        } catch (Exception e) {
            System.out.println("Error en AdministrarEmpleadoIndividual.obtenerRutaFoto");
            return null;
        }
    }

    @Override
    public String fotoEmpleado(Empleados empleado) {
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
    }
}
