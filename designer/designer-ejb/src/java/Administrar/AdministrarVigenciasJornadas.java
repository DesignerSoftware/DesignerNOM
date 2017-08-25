package Administrar;

import Entidades.Empleados;
import Entidades.JornadasLaborales;
import Entidades.TiposDescansos;
import Entidades.VigenciasCompensaciones;
import Entidades.VigenciasJornadas;
import InterfaceAdministrar.AdministrarVigenciasJornadasInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaJornadasLaboralesInterface;
import InterfacePersistencia.PersistenciaTiposDescansosInterface;
import InterfacePersistencia.PersistenciaVigenciasCompensacionesInterface;
import InterfacePersistencia.PersistenciaVigenciasJornadasInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import InterfaceAdministrar.AdministrarSesionesInterface;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

/**
 *
 * @author AndresPineda
 */
@Stateful
public class AdministrarVigenciasJornadas implements AdministrarVigenciasJornadasInterface {

   private static Logger log = Logger.getLogger(AdministrarVigenciasJornadas.class);

   @EJB
   PersistenciaVigenciasJornadasInterface persistenciaVigenciasJornadas;
   @EJB
   PersistenciaJornadasLaboralesInterface persistenciaJornadasLaborales;
   @EJB
   PersistenciaTiposDescansosInterface persistenciaTiposDescansos;
   @EJB
   PersistenciaVigenciasCompensacionesInterface persistenciaVigenciasCompensaciones;
   @EJB
   PersistenciaEmpleadoInterface persistenciaEmpleado;
   /**
    * Enterprise JavaBean.<br>
    * Atributo que representa todo lo referente a la conexión del usuario que
    * está usando el aplicativo.
    */
   @EJB
   AdministrarSesionesInterface administrarSesiones;

   // VigenciasJornadas
//    List<VigenciasJornadas> listVigenciasJornadas;
//    VigenciasJornadas vigenciaJornada;
//    // VigenciasCompensaciones
//    List<VigenciasCompensaciones> listVigenciasCompensaciones;
//    VigenciasCompensaciones vigenciaCompensacion;
//    //JornadasLaborales
//    List<JornadasLaborales> listJornadasLaborales;
//    //TiposDescansos
//    List<TiposDescansos> listTiposDescansos;
//    //Empleados
//    Empleados empleado;
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
   public List<VigenciasJornadas> VigenciasJornadasEmpleado(BigInteger secEmpleado) {
      try {
         return persistenciaVigenciasJornadas.buscarVigenciasJornadasEmpleado(getEm(), secEmpleado);
      } catch (Exception e) {
         log.warn("Error en Administrar Vigencias Jornadas (VigenciasJornadasEmpleado) : " + e.toString());
         return null;
      }
   }

   @Override
   public void modificarVJ(List<VigenciasJornadas> listVJModificadas) {
      try {
         for (int i = 0; i < listVJModificadas.size(); i++) {
            if (listVJModificadas.get(i).getJornadatrabajo().getSecuencia() == null) {
               listVJModificadas.get(i).setJornadatrabajo(null);
            }
            if (listVJModificadas.get(i).getTipodescanso().getSecuencia() == null) {
               listVJModificadas.get(i).setTipodescanso(null);
            }
            persistenciaVigenciasJornadas.editar(getEm(), listVJModificadas.get(i));
         }
      } catch (Exception e) {
         log.warn("Error modificarVJ AdmiVigJor : " + e.toString());
      }
   }

   @Override
   public void borrarVJ(VigenciasJornadas vigenciasJornadas) {
      try {
         persistenciaVigenciasJornadas.borrar(getEm(), vigenciasJornadas);
      } catch (Exception e) {
         log.warn("Error borrarVJ AdmiVigJor : " + e.toString());
      }
   }

   @Override
   public void crearVJ(VigenciasJornadas vigenciasJornadas) {
      try {
         persistenciaVigenciasJornadas.crear(getEm(), vigenciasJornadas);
      } catch (Exception e) {
         log.warn("Error crearVJ AdmiVigJor : " + e.toString());
      }
   }

   @Override
   public List<VigenciasCompensaciones> VigenciasCompensacionesSecVigenciaTipoComp(String tipoC, BigInteger secVigencia) {
      try {
         return persistenciaVigenciasCompensaciones.buscarVigenciasCompensacionesVigenciayCompensacion(getEm(), tipoC, secVigencia);
      } catch (Exception e) {
         log.warn("Error en Administrar Vigencias Jornadas (VigenciasCompensacionesSecVigenciaTipoComp) : " + e.toString());
         return null;
      }
   }

   @Override
   public List<VigenciasCompensaciones> VigenciasCompensacionesSecVigencia(BigInteger secVigencia) {
      try {
         return persistenciaVigenciasCompensaciones.buscarVigenciasCompensacionesVigenciaSecuencia(getEm(), secVigencia);
      } catch (Exception e) {
         log.warn("Error en Administrar Vigencias Jornadas (VigenciasCompensacionesSecVigenciaTipoComp) : " + e.toString());
         return null;
      }
   }

   @Override
   public void modificarVC(List<VigenciasCompensaciones> listVCModificadas) {
      try {
         for (int i = 0; i < listVCModificadas.size(); i++) {
            persistenciaVigenciasCompensaciones.editar(getEm(), listVCModificadas.get(i));
         }
      } catch (Exception e) {
         log.warn("Error modificarVC AdmiVigJor : " + e.toString());
      }
   }

   @Override
   public void borrarVC(VigenciasCompensaciones vigenciasCompensaciones) {
      try {
         persistenciaVigenciasCompensaciones.borrar(getEm(), vigenciasCompensaciones);
      } catch (Exception e) {
         log.warn("Error borrarVC AdmiVigJor : " + e.toString());
      }
   }

   @Override
   public void crearVC(VigenciasCompensaciones vigenciasCompensaciones) {
      try {
         persistenciaVigenciasCompensaciones.crear(getEm(), vigenciasCompensaciones);
      } catch (Exception e) {
         log.warn("Error crearVC AdmiVigJor : " + e.toString());
      }
   }

   @Override
   public List<TiposDescansos> tiposDescansos() {
      try {
         return persistenciaTiposDescansos.consultarTiposDescansos(getEm());
      } catch (Exception e) {
         log.warn("Error tiposDescansos Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<JornadasLaborales> jornadasLaborales() {
      try {
         return persistenciaJornadasLaborales.buscarJornadasLaborales(getEm());
      } catch (Exception e) {
         log.warn("Error jornadasLaborales Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public Empleados buscarEmpleado(BigInteger secuencia) {
      try {
         return persistenciaEmpleado.buscarEmpleadoSecuencia(getEm(), secuencia);
      } catch (Exception e) {
         log.warn("Error buscarEmpleado Adm : " + e.toString());
         return null;
      }
   }
}
