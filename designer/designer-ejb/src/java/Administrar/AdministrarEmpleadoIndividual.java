package Administrar;

import Entidades.Cargos;
import Entidades.Ciudades;
import Entidades.Demandas;
import Entidades.Departamentos;
import Entidades.Direcciones;
import Entidades.Empleados;
import Entidades.Encargaturas;
import Entidades.EvalResultadosConv;
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
import javax.persistence.EntityManagerFactory;
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

   private EntityManagerFactory emf;
   private EntityManager em;

   private EntityManager getEm() {
      try {
         if (this.em != null) {
            if (this.em.isOpen()) {
               this.em.close();
            }
         }
         this.em = emf.createEntityManager();
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " getEm() ERROR : " + e);
      }
      return this.em;
   }
   private Generales general;

   @Override
   public void obtenerConexion(String idSesion) {
      try {
         emf = administrarSesiones.obtenerConexionSesionEMF(idSesion);
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " obtenerConexion ERROR: " + e);
      }
   }

   @Override
   public HVHojasDeVida hvHojaDeVidaPersona(BigInteger secPersona) {
      try {
         return persistenciaHVHojasDeVida.hvHojaDeVidaPersona(getEm(), secPersona);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public Telefonos primerTelefonoPersona(BigInteger secPersona) {
      try {
         List<Telefonos> listaTelefonos = persistenciaTelefonos.telefonosPersona(getEm(), secPersona);
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
         List<Direcciones> listaDirecciones = persistenciaDirecciones.direccionPersona(getEm(), secPersona);
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
         List<VigenciasEstadosCiviles> listaVigenciasEstadosCiviles = persistenciaVigenciasEstadosCiviles.consultarVigenciasEstadosCivilesPersona(getEm(), secPersona);
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
         List<InformacionesAdicionales> listaInformacionesAdicionales = persistenciaInformacionesAdicionales.informacionAdicionalPersona(getEm(), secEmpleado);
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
         List<Encargaturas> listaEncargaturas = persistenciaEncargaturas.reemplazoPersona(getEm(), secEmpleado);
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
         List<VigenciasFormales> listaVigenciasFormales = persistenciaVigenciasFormales.educacionPersona(getEm(), secPersona);
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
         List<IdiomasPersonas> listaIdiomasPersonas = persistenciaIdiomasPersonas.idiomasPersona(getEm(), secPersona);
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
         List<VigenciasProyectos> listaVigenciasProyectos = persistenciaVigenciasProyectos.proyectosEmpleado(getEm(), secEmpleado);
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
         List<HvReferencias> listaReferenciasPersonales = persistenciaHvReferencias.referenciasPersonalesPersona(getEm(), secHv);
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
         List<HvReferencias> listaReferenciasPersonales = persistenciaHvReferencias.contarReferenciasFamiliaresPersona(getEm(), secHv);
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
         List<HvExperienciasLaborales> listaExperienciaLaboral = persistenciaHvExperienciasLaborales.experienciaLaboralPersona(getEm(), secHv);
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
         List<VigenciasEventos> listaVigenciasEventos = persistenciaVigenciasEventos.eventosEmpleado(getEm(), secEmpl);
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
         List<VigenciasDeportes> listaVigenciasDeportes = persistenciaVigenciasDeportes.deportePersona(getEm(), secPersona);
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
         List<VigenciasAficiones> listaVigenciasAficiones = persistenciaVigenciasAficiones.aficionesPersona(getEm(), secPersona);
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
      try {
         return persistenciaFamiliares.consultaFamiliar(getEm(), secPersona);
      } catch (Exception e) {
         log.warn("error en consultaFamiliaresPersona : " + e.getMessage());
         return " ";
      }
   }

   @Override
   public HvEntrevistas entrevistasPersona(BigInteger secHv) {
      try {
         List<HvEntrevistas> listaEntrevistas = persistenciaHvEntrevistas.entrevistasPersona(getEm(), secHv);
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
         List<VigenciasIndicadores> listaVigenciasIndicadores = persistenciaVigenciasIndicadores.ultimosIndicadoresEmpleado(getEm(), secEmpl);
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
         List<Demandas> listaDemandas = persistenciaDemandas.demandasPersona(getEm(), secEmpl);
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
         List<VigenciasDomiciliarias> listaVigenciasDomiciliarias = persistenciaVigenciasDomiciliarias.visitasDomiciliariasPersona(getEm(), secPersona);
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
         List<EvalResultadosConv> listaPruebasAplicadas = persistenciaEvalResultadosConv.pruebasAplicadasPersona(getEm(), secEmpleado);
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
         return persistenciaTiposDocumentos.consultarTiposDocumentos(getEm());
      } catch (Exception e) {
         log.warn("error en tiposDocumentos  : " + e.getMessage());
         return null;
      }
   }

   @Override
   public List<Ciudades> ciudades() {
      try {
         return persistenciaCiudades.consultarCiudades(getEm());
      } catch (Exception e) {
         log.warn("error en ciudades : " + e.getMessage());
         return null;
      }
   }

   @Override
   public List<Cargos> cargos() {
      try {
         return persistenciaCargos.consultarCargos(getEm());
      } catch (Exception e) {
         log.warn("Error Administrar.AdministrarEmpleadoIndividual.cargos() : " + e.getMessage());
         return null;
      }
   }

   @Override
   public Empleados buscarEmpleado(BigInteger secuencia) {
      try {
         return persistenciaEmpleado.buscarEmpleadoSecuencia(getEm(), secuencia);
      } catch (Exception e) {
         return null;
      }
   }

   @Override
   public void modificarEmpleado(Empleados empleado) {
      try {
         persistenciaEmpleado.editar(getEm(), empleado);
      } catch (Exception e) {
         log.warn("Error modificando. AdministrarEmpleadoIndividual.modificarEmpleado");
      }
   }

   @Override
   public void modificarHojaDeVida(HVHojasDeVida hojaVida) {
      try {
         persistenciaHVHojasDeVida.editar(getEm(), hojaVida);
      } catch (Exception e) {
         log.warn("Error modificando. AdministrarEmpleadoIndividual.modificarHojaDeVida " + e);
      }
   }

   @Override
   public void modificarPersona(Personas personas) {
      try {
         persistenciaPersonas.editar(getEm(), personas);
      } catch (Exception e) {
         log.warn("Error modificando. AdministrarEmpleadoIndividual.modificarPersona : " + e.getMessage());
      }
   }

   @Override
   public void actualizarFotoPersona(Personas persona) {
      try {
         //persistenciaPersonas.actualizarFotoPersona(getEm(), identificacion);
         persona.setPathfoto("S");
         persistenciaPersonas.editar(getEm(), persona);
      } catch (Exception e) {
         log.warn("No se puede actalizar el estado de la Foto.");
      }
   }

   public Generales obtenerRutaFoto() {
      try {
         general = persistenciaGenerales.obtenerRutas(getEm());
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
         general = persistenciaGenerales.obtenerRutas(getEm());
         if (general != null) {
            if (empleado.getPathfotoPersona() == null || empleado.getPathfotoPersona().equalsIgnoreCase("N")) {
               rutaFoto = general.getPathfoto() + "sinFoto.jpg";
            } else {
               rutaFoto = general.getPathfoto() + empleado.getNumeroDocumentoPersona() + ".jpg";
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
         return persistenciaPersonas.buscarPersonaSecuencia(getEm(), secPersona);
      } catch (Exception e) {
         log.warn("error en encontrarPersona : " + e.getMessage());
         return null;
      }
   }

   @Override
   public Personas obtenerPersonaPorEmpleado(BigInteger secEmpleado) {
      try {
         return persistenciaPersonas.buscarPersonaPorEmpleado(em, secEmpleado);
      } catch (Exception e) {
         log.warn("error en obtenerPersonaPorEmpleado : " + e.getMessage());
         return null;
      }
   }

   @Override
   public String consultarPrimerTelefonoPersona(BigInteger secPersona) {
      try {
         return persistenciaTelefonos.consultarUltimoTelefono(getEm(), secPersona);
      } catch (Exception e) {
         log.warn("error en consultarPrimerTelefonoPersona : " + e.getMessage());
         return " ";
      }
   }

   @Override
   public String consultarPrimeraDireccionPersona(BigInteger secPersona) {
      try {
         return persistenciaDirecciones.consultarPrimeraDireccion(getEm(), secPersona);
      } catch (Exception e) {
         log.warn("error en consultarPrimeraDireccionPersona");
         return " ";
      }
   }

   @Override
   public String consultarPrimerEstadoCivilPersona(BigInteger secPersona) {
      try {
         return persistenciaVigenciasEstadosCiviles.consultarPrimerEstadoCivil(getEm(), secPersona);
      } catch (Exception e) {
         log.warn("error en consultarPrimerEstadoCivilPersona : " + e.getMessage());
         return " ";
      }
   }

   @Override
   public String consultarPrimeraInformacionAd(BigInteger secPersona) {
      try {
         return persistenciaInformacionesAdicionales.primeraInformacionAdicional(getEm(), secPersona);
      } catch (Exception e) {
         log.warn("error en consultarPrimeraInformacionAd : " + e.getMessage());
         return " ";
      }
   }

   @Override
   public String consultarPrimerReemplazo(BigInteger secPersona) {
      try {
         String reemplazo = persistenciaEncargaturas.primeraEncargatura(getEm(), secPersona);
         if (reemplazo == null) {
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
         return persistenciaVigenciasFormales.primeraVigenciaFormal(getEm(), secPersona);
      } catch (Exception e) {
         log.warn("error en consultarPrimeraVigenciaFormal  : " + e.getMessage());
         return " ";
      }
   }

   @Override
   public String consultarPimerIdioma(BigInteger secPersona) {
      try {
         return persistenciaIdiomasPersonas.primerIdioma(getEm(), secPersona);
      } catch (Exception e) {
         log.warn("error en consultarPrimerIdioma : " + e.getMessage());
         return " ";
      }
   }

   @Override
   public String consultarPrimerProyecto(BigInteger secPersona) {
      try {
         return persistenciaVigenciasProyectos.primerProyecto(getEm(), secPersona);
      } catch (Exception e) {
         log.warn("error en consultarPrimerProyecto : " + e.getMessage());
         return " ";
      }
   }

   @Override
   public String consultarPrimerEvento(BigInteger secPersona) {
      try {
         return persistenciaVigenciasEventos.primerEvento(getEm(), secPersona);
      } catch (Exception e) {
         log.warn("error en consultarPrimerEvento : " + e.getMessage());
         return " ";
      }
   }

   @Override
   public String consultarPrimerDeporte(BigInteger secPersona) {
      try {
         return persistenciaVigenciasDeportes.consultarPrimerDeporte(getEm(), secPersona);
      } catch (Exception e) {
         log.warn("error en consultarPrimerDeporte : " + e.getMessage());
         return " ";
      }
   }

   @Override
   public String consultarPrimeraAficion(BigInteger secPersona) {
      try {
         return persistenciaVigenciasAficiones.primeraAficion(getEm(), secPersona);
      } catch (Exception e) {
         log.warn("error en consultarPrimeraAfición : " + e.getMessage());
         return " ";
      }
   }

   @Override
   public String consultarPrimerFamiliar(BigInteger secPersona) {
      try {
         return persistenciaFamiliares.consultarPrimerFamiliar(getEm(), secPersona);
      } catch (Exception e) {
         log.warn("error en consultarPrimerFamiliar : " + e.getMessage());
         return " ";
      }
   }

   @Override
   public String consultarPrimerIndicador(BigInteger secPersona) {
      try {
         return persistenciaVigenciasIndicadores.primeraVigenciaIndicador(getEm(), secPersona);
      } catch (Exception e) {
         log.warn("error en consultarPrimerIndicador : " + e.getMessage());
         return " ";
      }
   }

   @Override
   public String consultarPrimeraDemanda(BigInteger Persona) {
      try {
         return persistenciaDemandas.primeraDemanda(getEm(), Persona);
      } catch (Exception e) {
         log.warn("error en consultarPrimeraDemanda : " + e.getMessage());
         return " ";
      }
   }

   @Override
   public String consultarPrimeraVisita(BigInteger secPersona) {
      try {
         return persistenciaVigenciasDomiciliarias.primeraVigenciaDomiciliaria(getEm(), secPersona);
      } catch (Exception e) {
         log.warn("error en consultarPrimeraVisita : " + e.getMessage());
         return " ";
      }
   }

   @Override
   public String consultarPrimeraEntrevista(BigInteger secHV) {
      try {
         return persistenciaHvEntrevistas.consultarPrimeraEnterevista(getEm(), secHV);
      } catch (Exception e) {
         log.warn("error en consultarPrimeraEntrevista : " + e.getMessage());
         return " ";
      }
   }

   @Override
   public String consultarPrimeraReferenciaF(BigInteger secHV) {
      try {
         return persistenciaHvReferencias.primeraReferenciaFamiliar(getEm(), secHV);
      } catch (Exception e) {
         log.warn("error en consultarPrimeraReferenciaF : " + e.getMessage());
         return " ";
      }
   }

   @Override
   public String consultarPrimeraReferenciaP(BigInteger secHV) {
      try {
         return persistenciaHvReferencias.primeraReferenciaPersonal(getEm(), secHV);
      } catch (Exception e) {
         log.warn("error en consultarPrimeraReferenciaP : " + e.getMessage());
         return " ";
      }
   }

   @Override
   public String consultarPrimeraExpLaboral(BigInteger secHv) {
      try {
         return persistenciaHvExperienciasLaborales.primeraExpLaboral(getEm(), secHv);
      } catch (Exception e) {
         log.warn("error en consultarPrimeraExpLaboral : " + e.getMessage());
         return " ";
      }
   }

   @Override
   public String consultarPrimeraPrueba(BigInteger secPersona) {
      try {
         return persistenciaEvalResultadosConv.primerPruebaAplicada(getEm(), secPersona);
      } catch (Exception e) {
         log.warn("error en consultarPrimeraPrueba : " + e.getMessage());
         return " ";
      }
   }
}
