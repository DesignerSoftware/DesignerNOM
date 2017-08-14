/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.AdiestramientosF;
import Entidades.Empleados;
import Entidades.Instituciones;
import Entidades.Personas;
import Entidades.Profesiones;
import Entidades.TiposEducaciones;
import Entidades.VigenciasFormales;
import InterfaceAdministrar.AdministrarVigenciasFormalesInterface;
import InterfacePersistencia.PersistenciaAdiestramientosFInterface;
import InterfacePersistencia.PersistenciaInstitucionesInterface;
import InterfacePersistencia.PersistenciaPersonasInterface;
import InterfacePersistencia.PersistenciaProfesionesInterface;
import InterfacePersistencia.PersistenciaTiposEducacionesInterface;
import InterfacePersistencia.PersistenciaVigenciasFormalesInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

@Stateful
public class AdministrarVigenciasFormales implements AdministrarVigenciasFormalesInterface {

   private static Logger log = Logger.getLogger(AdministrarVigenciasFormales.class);

   @EJB
   PersistenciaVigenciasFormalesInterface persistenciaVigenciasFormales;
   @EJB
   PersistenciaPersonasInterface persistenciaPersonas;
   @EJB
   PersistenciaTiposEducacionesInterface persistenciaTiposEducaciones;
   @EJB
   PersistenciaProfesionesInterface persistenciaProfesiones;
   @EJB
   PersistenciaInstitucionesInterface persistenciaInstituciones;
   @EJB
   PersistenciaAdiestramientosFInterface persistenciaAdiestramientosF;
   @EJB
   PersistenciaEmpleadoInterface persistenciaEmpleado;
   /**
    * Enterprise JavaBean.<br>
    * Atributo que representa todo lo referente a la conexión del usuario que
    * está usando el aplicativo.
    */
   @EJB
   AdministrarSesionesInterface administrarSesiones;

   private VigenciasFormales vF;
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
   public List<VigenciasFormales> vigenciasFormalesPersona(BigInteger secPersona) {
      try {
         return persistenciaVigenciasFormales.vigenciasFormalesPersona(getEm(), secPersona);
      } catch (Exception e) {
         log.error("Error AdministrarVigenciasFormales.vigenciasFormalesPersona " + e);
         return null;
      }
   }

   @Override
   public Personas encontrarPersona(BigInteger secPersona) {
      try {
         return persistenciaPersonas.buscarPersonaSecuencia(getEm(), secPersona);
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   //Listas de Valores Educacion, Profesion, Instituciones, Adiestramiento
   @Override
   public List<TiposEducaciones> lovTiposEducaciones() {
      try {
         return persistenciaTiposEducaciones.tiposEducaciones(getEm());
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<Profesiones> lovProfesiones() {
      try {
         return persistenciaProfesiones.profesiones(getEm());
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<Instituciones> lovInstituciones() {
      try {
         return persistenciaInstituciones.instituciones(getEm());
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<AdiestramientosF> lovAdiestramientosF() {
      try {
         return persistenciaAdiestramientosF.adiestramientosF(getEm());
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

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
            persistenciaVigenciasFormales.editar(getEm(), listaVigenciasFormalesModificar.get(i));
         }
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
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
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void crearVigenciaFormal(List<VigenciasFormales> listaVigenciasFormalesCrear) {
      try {
         for (int i = 0; i < listaVigenciasFormalesCrear.size(); i++) {
            log.warn("Creando...");
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
               listaVigenciasFormalesCrear.get(i).setAcargo("N");
               listaVigenciasFormalesCrear.get(i).setAdiestramientof(null);
            } else {
               listaVigenciasFormalesCrear.get(i).setAcargo("S");
            }
            if (listaVigenciasFormalesCrear.get(i).getNumerotarjeta() != null) {
               listaVigenciasFormalesCrear.get(i).setTarjetaprofesional("S");
            } else {
               listaVigenciasFormalesCrear.get(i).setTarjetaprofesional("N");
            }
            persistenciaVigenciasFormales.crear(getEm(), listaVigenciasFormalesCrear.get(i));
         }
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public Empleados empleadoActual(BigInteger secuenciaE) {
      try {
         return persistenciaEmpleado.buscarEmpleado(getEm(), secuenciaE);
      } catch (Exception e) {
         log.warn("Error empleadoActual Admi : " + e.toString());
         return null;
      }
   }

}
