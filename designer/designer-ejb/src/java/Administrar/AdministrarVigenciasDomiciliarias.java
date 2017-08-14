/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.AdiestramientosF;
import Entidades.Cargos;
import Entidades.Ciudades;
import Entidades.Direcciones;
import Entidades.Empleados;
import Entidades.EstadosCiviles;
import Entidades.Familiares;
import Entidades.HVHojasDeVida;
import Entidades.HvExperienciasLaborales;
import Entidades.Instituciones;
import Entidades.MotivosRetiros;
import Entidades.Personas;
import Entidades.Profesiones;
import Entidades.SectoresEconomicos;
import Entidades.SoAntecedentes;
import Entidades.SoAntecedentesMedicos;
import Entidades.SoTiposAntecedentes;
import Entidades.Telefonos;
import Entidades.TiposDocumentos;
import Entidades.TiposEducaciones;
import Entidades.TiposFamiliares;
import Entidades.TiposTelefonos;
import Entidades.VigenciasDomiciliarias;
import Entidades.VigenciasEstadosCiviles;
import Entidades.VigenciasFormales;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfaceAdministrar.AdministrarVigenciasDomiciliariasInterface;
import InterfacePersistencia.PersistenciaAdiestramientosFInterface;
import InterfacePersistencia.PersistenciaCargosInterface;
import InterfacePersistencia.PersistenciaCiudadesInterface;
import InterfacePersistencia.PersistenciaDireccionesInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaEstadosCivilesInterface;
import InterfacePersistencia.PersistenciaFamiliaresInterface;
import InterfacePersistencia.PersistenciaHVHojasDeVidaInterface;
import InterfacePersistencia.PersistenciaHvExperienciasLaboralesInterface;
import InterfacePersistencia.PersistenciaInstitucionesInterface;
import InterfacePersistencia.PersistenciaMotivosRetirosInterface;
import InterfacePersistencia.PersistenciaPersonasInterface;
import InterfacePersistencia.PersistenciaProfesionesInterface;
import InterfacePersistencia.PersistenciaSectoresEconomicosInterface;
import InterfacePersistencia.PersistenciaSoAntecedentesInterface;
import InterfacePersistencia.PersistenciaSoAntecedentesMedicosInterface;
import InterfacePersistencia.PersistenciaSoTiposAntecedentesInterface;
import InterfacePersistencia.PersistenciaTelefonosInterface;
import InterfacePersistencia.PersistenciaTiposDocumentosInterface;
import InterfacePersistencia.PersistenciaTiposEducacionesInterface;
import InterfacePersistencia.PersistenciaTiposFamiliaresInterface;
import InterfacePersistencia.PersistenciaTiposTelefonosInterface;
import InterfacePersistencia.PersistenciaVigenciasDomiciliariasInterface;
import InterfacePersistencia.PersistenciaVigenciasEstadosCivilesInterface;
import InterfacePersistencia.PersistenciaVigenciasFormalesInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

/**
 *
 * @author user
 */
@Stateful
public class AdministrarVigenciasDomiciliarias implements AdministrarVigenciasDomiciliariasInterface {

   private static Logger log = Logger.getLogger(AdministrarVigenciasDomiciliarias.class);

   @EJB
   AdministrarSesionesInterface administrarSesiones;
   @EJB
   PersistenciaVigenciasDomiciliariasInterface persistenciaVigenciasDomiciliarias;
   @EJB
   PersistenciaCargosInterface persistenciaCargos;
   @EJB
   PersistenciaTiposEducacionesInterface persistenciaTiposEducaciones;
   @EJB
   PersistenciaProfesionesInterface persistenciaProfesiones;
   @EJB
   PersistenciaAdiestramientosFInterface persistenciaAdiestramientos;
   @EJB
   PersistenciaInstitucionesInterface persistenciaInstituciones;
   @EJB
   PersistenciaMotivosRetirosInterface persistenciaMotivosRetiros;
   @EJB
   PersistenciaTiposFamiliaresInterface persistenciaTiposFamiliares;
   @EJB
   PersistenciaPersonasInterface persistenciaPersonas;
   @EJB
   PersistenciaSoAntecedentesInterface persistenciaAntecedentes;
   @EJB
   PersistenciaSoTiposAntecedentesInterface persistenciaTiposAntecedentes;
   @EJB
   PersistenciaEstadosCivilesInterface persistenciaEstadosCiviles;
   @EJB
   PersistenciaTiposTelefonosInterface persistenciaTiposTelefonos;
   @EJB
   PersistenciaCiudadesInterface persistenciaCiudades;
   @EJB
   PersistenciaDireccionesInterface persistenciaDirecciones;
   @EJB
   PersistenciaTelefonosInterface persistenciaTelefonos;
   @EJB
   PersistenciaVigenciasEstadosCivilesInterface persistenciaVigEstadosCiviles;
   @EJB
   PersistenciaEmpleadoInterface persistenciaEmpleados;
   @EJB
   PersistenciaSoAntecedentesMedicosInterface persistenciaAntecdentesM;
   @EJB
   PersistenciaFamiliaresInterface persistenciaFamiliares;
   @EJB
   PersistenciaVigenciasFormalesInterface persistenciaVigenciasFormales;
   @EJB
   PersistenciaHVHojasDeVidaInterface persistenciahv;
   @EJB
   PersistenciaHvExperienciasLaboralesInterface persistenciaHvExp;
   @EJB
   PersistenciaSectoresEconomicosInterface persistenciaSectoresE;
   @EJB
   PersistenciaTiposDocumentosInterface persistenciaTipoDocumento;

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

   @Override
   public void obtenerConexion(String idSesion) {
      try {
         emf = administrarSesiones.obtenerConexionSesionEMF(idSesion);
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " obtenerConexion ERROR: " + e);
      }
   }

   @Override
   public void modificarVigencia(List<VigenciasDomiciliarias> listaModificar) {
      for (int i = 0; i < listaModificar.size(); i++) {
         persistenciaVigenciasDomiciliarias.editar(getEm(), listaModificar.get(i));
      }
   }

   @Override
   public void crearVigencia(List<VigenciasDomiciliarias> listaCrear) {
      for (int i = 0; i < listaCrear.size(); i++) {
         persistenciaVigenciasDomiciliarias.crear(getEm(), listaCrear.get(i));
      }
   }

   @Override
   public void borrarVigencia(List<VigenciasDomiciliarias> listaBorrar) {
      try {
         for (int i = 0; i < listaBorrar.size(); i++) {
            persistenciaVigenciasDomiciliarias.borrar(getEm(), listaBorrar.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public List<VigenciasDomiciliarias> vigenciasDomiciliariasporPersona(BigInteger secPersona) {
      try {
         return persistenciaVigenciasDomiciliarias.visitasDomiciliariasPersona(getEm(), secPersona);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<MotivosRetiros> lovMotivosRetiros() {
      try {
         return persistenciaMotivosRetiros.consultarMotivosRetiros(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<EstadosCiviles> lovVigenciasEstadosCiviles() {
      try {
         return persistenciaEstadosCiviles.consultarEstadosCiviles(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<TiposTelefonos> lovTiposTelefonos() {
      try {
         return persistenciaTiposTelefonos.tiposTelefonos(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<Ciudades> lovCiudades() {
      try {
         return persistenciaCiudades.lovCiudades(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<Cargos> lovCargos() {
      try {
         return persistenciaCargos.lovCargos(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<Direcciones> direccionesPersona(BigInteger secPersona) {
      try {
         return persistenciaDirecciones.listaDireccionPersona(getEm(), secPersona);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public Telefonos telefonoActualPersona(BigInteger secPersona) {
      try {
         return persistenciaTelefonos.telefonoActual(getEm(), secPersona);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public VigenciasEstadosCiviles estadoCivilActualPersona(BigInteger secPersona) {
      try {
         return persistenciaVigEstadosCiviles.estadoCivilActual(getEm(), secPersona);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public Empleados buscarEmpleado(BigInteger secPersona) {
      try {
         return persistenciaEmpleados.buscarEmpleadoSecuenciaPersona(getEm(), secPersona);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   ////metodos direcciones
   @Override
   public List<Direcciones> consultarDireccionesPersona(BigInteger secPersona) {
      try {
         return persistenciaDirecciones.listaDireccionPersona(getEm(), secPersona);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public void crearDirecciones(List<Direcciones> listaCrear) {
      try {
         for (int i = 0; i < listaCrear.size(); i++) {
            persistenciaDirecciones.crear(getEm(), listaCrear.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void borrarDirecciones(List<Direcciones> listaBorrar) {
      try {
         for (int i = 0; i < listaBorrar.size(); i++) {
            persistenciaDirecciones.borrar(getEm(), listaBorrar.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void modificarDirecciones(List<Direcciones> listaModificar) {
      try {
         for (int i = 0; i < listaModificar.size(); i++) {
            persistenciaDirecciones.editar(getEm(), listaModificar.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   ///métodos teléfonos
   @Override
   public List<Telefonos> telefonosPersona(BigInteger secPersona) {
      try {
         return persistenciaTelefonos.telefonosPersona(getEm(), secPersona);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public void crearTelefonos(Telefonos telefono) {
      try {
         persistenciaTelefonos.crear(getEm(), telefono);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void borrarTelefonos(Telefonos telefono) {
      try {
         persistenciaTelefonos.borrar(getEm(), telefono);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void modificarTelefonos(List<Telefonos> listModificar) {
      try {
         for (int i = 0; i < listModificar.size(); i++) {
            persistenciaTelefonos.editar(getEm(), listModificar.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   ///métodos estados civiles
   @Override
   public List<VigenciasEstadosCiviles> estadosCivilesPersona(BigInteger secPersona) {
      try {
         return persistenciaVigEstadosCiviles.consultarVigenciasEstadosCivilesPorPersona(getEm(), secPersona);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public void modificarVigenciasEstadosCiviles(List<VigenciasEstadosCiviles> listaVigenciasEstadosCiviles) {
      try {
         for (int i = 0; i < listaVigenciasEstadosCiviles.size(); i++) {
            persistenciaVigEstadosCiviles.editar(getEm(), listaVigenciasEstadosCiviles.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void borrarVigenciasEstadosCiviles(List<VigenciasEstadosCiviles> listaVigenciasEstadosCiviles) {
      try {
         for (int i = 0; i < listaVigenciasEstadosCiviles.size(); i++) {
            persistenciaVigEstadosCiviles.borrar(getEm(), listaVigenciasEstadosCiviles.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void crearVigenciasEstadosCiviles(List<VigenciasEstadosCiviles> listaVigenciasEstadosCiviles) {
      try {
         for (int i = 0; i < listaVigenciasEstadosCiviles.size(); i++) {
            persistenciaVigEstadosCiviles.crear(getEm(), listaVigenciasEstadosCiviles.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

/// métodos antecedentes médicos
   @Override
   public List<SoAntecedentes> lovAntecedentes(BigInteger secTipoAntecedente) {
      try {
         return persistenciaAntecedentes.lovAntecedentes(getEm(), secTipoAntecedente);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<SoTiposAntecedentes> lovTiposAntecedentes() {
      try {
         return persistenciaTiposAntecedentes.lovTiposAntecedentes(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<SoAntecedentesMedicos> buscarAntecedentesMedicos(BigInteger secPersona) {
      try {
         return persistenciaAntecdentesM.listaAntecedentesMedicos(getEm(), secPersona);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public void crearAntecedenteM(List<SoAntecedentesMedicos> listaCrear) {
      try {
         for (int i = 0; i < listaCrear.size(); i++) {
            if (listaCrear.get(i).getAntecedente() == null) {
               listaCrear.get(i).setAntecedente(new SoAntecedentes());
            }

            if (listaCrear.get(i).getEmpleado() == null) {
               listaCrear.get(i).setEmpleado(new Empleados());
            }
            persistenciaAntecdentesM.crear(getEm(), listaCrear.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void borrarAntecedenteM(List<SoAntecedentesMedicos> listaBorrar) {
      try {
         for (int i = 0; i < listaBorrar.size(); i++) {
            persistenciaAntecdentesM.borrar(getEm(), listaBorrar.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void modificarAntecedenteM(List<SoAntecedentesMedicos> listaModificar) {
      try {
         for (int i = 0; i < listaModificar.size(); i++) {
            persistenciaAntecdentesM.editar(getEm(), listaModificar.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   // métodos vigencias formales
   @Override
   public void modificarVigenciaFormal(List<VigenciasFormales> listaVigenciasFormalesModificar) {
      try {
         for (int i = 0; i < listaVigenciasFormalesModificar.size(); i++) {
            log.warn("Modificando...");
            if (listaVigenciasFormalesModificar.get(i).getTipoeducacion().getSecuencia() == null) {
               listaVigenciasFormalesModificar.get(i).setTipoeducacion(null);
            }
            if (listaVigenciasFormalesModificar.get(i).getProfesion().getSecuencia() == null) {
               listaVigenciasFormalesModificar.get(i).setProfesion(null);
            }
            if (listaVigenciasFormalesModificar.get(i).getInstitucion().getSecuencia() == null) {
               listaVigenciasFormalesModificar.get(i).setInstitucion(null);
            }
            if (listaVigenciasFormalesModificar.get(i).getAdiestramientof().getSecuencia() == null) {
               listaVigenciasFormalesModificar.get(i).setAdiestramientof(null);
            }
            persistenciaVigenciasFormales.editar(getEm(), listaVigenciasFormalesModificar.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void borrarVigenciaFormal(List<VigenciasFormales> listaVigenciasFormalesBorrar) {
      try {
         for (int i = 0; i < listaVigenciasFormalesBorrar.size(); i++) {
            log.warn("Borrando...");
            if (listaVigenciasFormalesBorrar.get(i).getTipoeducacion().getSecuencia() == null) {
               listaVigenciasFormalesBorrar.get(i).setTipoeducacion(null);
            }
            if (listaVigenciasFormalesBorrar.get(i).getProfesion().getSecuencia() == null) {
               listaVigenciasFormalesBorrar.get(i).setProfesion(null);
            }
            if (listaVigenciasFormalesBorrar.get(i).getInstitucion().getSecuencia() == null) {
               listaVigenciasFormalesBorrar.get(i).setInstitucion(null);
            }
            persistenciaVigenciasFormales.borrar(getEm(), listaVigenciasFormalesBorrar.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void crearVigenciaFormal(List<VigenciasFormales> listaVigenciasFormalesCrear) {
      try {
         for (int i = 0; i < listaVigenciasFormalesCrear.size(); i++) {
            log.warn("Modificando...");
            if (listaVigenciasFormalesCrear.get(i).getTipoeducacion().getSecuencia() == null) {
               listaVigenciasFormalesCrear.get(i).setTipoeducacion(null);
            }
            if (listaVigenciasFormalesCrear.get(i).getProfesion().getSecuencia() == null) {
               listaVigenciasFormalesCrear.get(i).setProfesion(null);
            }
            if (listaVigenciasFormalesCrear.get(i).getInstitucion().getSecuencia() == null) {
               listaVigenciasFormalesCrear.get(i).setInstitucion(null);
            }
            if (listaVigenciasFormalesCrear.get(i).getAdiestramientof().getSecuencia() == null) {
               listaVigenciasFormalesCrear.get(i).setAdiestramientof(null);
            }
            if (listaVigenciasFormalesCrear.get(i).getNumerotarjeta() != null) {
               listaVigenciasFormalesCrear.get(i).setTarjetaprofesional("S");
            } else {
               listaVigenciasFormalesCrear.get(i).setTarjetaprofesional("N");
            }

            if (listaVigenciasFormalesCrear.get(i).getAdiestramientof().getDescripcion() != null) {
               listaVigenciasFormalesCrear.get(i).setAcargo("S");
            } else {
               listaVigenciasFormalesCrear.get(i).setAcargo("N");
            }
            persistenciaVigenciasFormales.crear(getEm(), listaVigenciasFormalesCrear.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public List<TiposEducaciones> lovTiposEducaciones() {
      try {
         return persistenciaTiposEducaciones.tiposEducaciones(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<Profesiones> lovProfesiones() {
      try {
         return persistenciaProfesiones.profesiones(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<AdiestramientosF> lovAdiestramientos() {
      try {
         return persistenciaAdiestramientos.adiestramientosF(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<Instituciones> lovInstituciones() {
      try {
         return persistenciaInstituciones.lovInstituciones(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<VigenciasFormales> vigenciasFormalesPersona(BigInteger secPersona) {
      try {
         return persistenciaVigenciasFormales.vigenciasFormalesPersona(getEm(), secPersona);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }
//////métodos exp laboral

   @Override
   public void crearExperienciaLaboral(List<HvExperienciasLaborales> listHEL) {
      try {
         for (int i = 0; i < listHEL.size(); i++) {
            if (listHEL.get(i).getMotivoretiro().getSecuencia() == null) {
               listHEL.get(i).setMotivoretiro(null);
            }
            if (listHEL.get(i).getSectoreconomico().getSecuencia() == null) {
               listHEL.get(i).setSectoreconomico(null);
            }

            if (listHEL.get(i).getHojadevida() == null) {
               listHEL.get(i).setHojadevida(new HVHojasDeVida());
            }

            persistenciaHvExp.crear(getEm(), listHEL.get(i));
         }
      } catch (Exception e) {
         log.warn("Error crearExperienciaLaboral Admi : " + e.toString());
      }
   }

   @Override
   public void editarExperienciaLaboral(List<HvExperienciasLaborales> listHEL) {
      try {
         for (int i = 0; i < listHEL.size(); i++) {
            if (listHEL.get(i).getMotivoretiro().getSecuencia() == null) {
               listHEL.get(i).setMotivoretiro(null);
            }
            if (listHEL.get(i).getSectoreconomico().getSecuencia() == null) {
               listHEL.get(i).setSectoreconomico(null);
            }

            if (listHEL.get(i).getHojadevida() == null) {
               listHEL.get(i).setHojadevida(new HVHojasDeVida());
            }

            persistenciaHvExp.editar(getEm(), listHEL.get(i));
         }
      } catch (Exception e) {
         log.warn("Error editarExperienciaLaboral Admi : " + e.toString());
      }
   }

   @Override
   public void borrarExperienciaLaboral(List<HvExperienciasLaborales> listHEL) {
      try {
         for (int i = 0; i < listHEL.size(); i++) {
            if (listHEL.get(i).getSectoreconomico().getSecuencia() == null) {
               listHEL.get(i).setSectoreconomico(null);
            }
            persistenciaHvExp.borrar(getEm(), listHEL.get(i));
         }
      } catch (Exception e) {
         log.warn("Error borrarExperienciaLaboral Admi : " + e.toString());
      }
   }

   @Override
   public HVHojasDeVida obtenerHojaVidaPersona(BigInteger secuencia) {
      try {
         return persistenciahv.hvHojaDeVidaPersona(getEm(), secuencia);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<HvExperienciasLaborales> experienciasLaboralesEmpleado(BigInteger secuencia) {
      try {
         return persistenciaHvExp.experienciasLaboralesSecuenciaEmpleado(getEm(), secuencia);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<SectoresEconomicos> lovSectoresEconomicos() {
      try {
         return persistenciaSectoresE.buscarSectoresEconomicos(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   ///métodos familiares
   @Override
   public List<TiposFamiliares> lovTiposFamiliares() {
      try {
         return persistenciaTiposFamiliares.buscarTiposFamiliares(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<Personas> lovPersonas() {
      try {
         return persistenciaPersonas.consultarPersonas(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public Personas encontrarPersona(BigInteger secpersona) {
      try {
         return persistenciaPersonas.buscarPersona(getEm(), secpersona);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<Familiares> buscarFamiliares(BigInteger secPersona) {
      try {
         return persistenciaFamiliares.familiaresPersona(getEm(), secPersona);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public void crearPersona(Personas persona) {
      try {
         persistenciaPersonas.crear(getEm(), persona);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public List<TiposDocumentos> consultarTiposDocumentos() {
      try {
         return persistenciaTipoDocumento.consultarTiposDocumentos(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public void modificarFamiliares(List<Familiares> listaModificar) {
      try {
         for (int i = 0; i < listaModificar.size(); i++) {
            if (listaModificar.get(i).getPersona() == null) {
               listaModificar.get(i).setPersona(new Personas());
            }
            log.warn("Administrar Modificando...");
            persistenciaFamiliares.editar(getEm(), listaModificar.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void borrarFamiliares(List<Familiares> listaBorrar) {
      try {
         for (int i = 0; i < listaBorrar.size(); i++) {
            if (listaBorrar.get(i).getPersona() == null) {
               listaBorrar.get(i).setPersona(new Personas());
            }
            log.warn("Administrar Borrando...");
            persistenciaFamiliares.borrar(getEm(), listaBorrar.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void crearFamilares(List<Familiares> listaCrear) {
      try {
         for (int i = 0; i < listaCrear.size(); i++) {
            log.warn("Administrar Creando...");
            if (listaCrear.get(i).getPersona() == null) {
               listaCrear.get(i).setPersona(new Personas());
            }
            persistenciaFamiliares.crear(getEm(), listaCrear.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public VigenciasDomiciliarias vigenciaDomiciliariaActual(BigInteger secPersona) {
      try {
         return persistenciaVigenciasDomiciliarias.actualVisitaDomiciliariaPersona(getEm(), secPersona);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public void editarHojadeVida(List<HVHojasDeVida> listaEditar) {
      try {
         for (int i = 0; i < listaEditar.size(); i++) {
            persistenciahv.editar(getEm(), listaEditar.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void editarPersona(List<Personas> listaEditar) {
      try {
         for (int i = 0; i < listaEditar.size(); i++) {
            persistenciaPersonas.editar(getEm(), listaEditar.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

}
