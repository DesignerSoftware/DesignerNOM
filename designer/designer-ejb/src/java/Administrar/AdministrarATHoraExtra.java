package Administrar;

import Entidades.Empleados;
import Entidades.Estructuras;
import Entidades.MotivosTurnos;
import Entidades.TurnosEmpleados;
import Entidades.VWEstadosExtras;
import InterfaceAdministrar.AdministrarATHoraExtraInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaEstructurasInterface;
import InterfacePersistencia.PersistenciaMotivosTurnosInterface;
import InterfacePersistencia.PersistenciaTurnosEmpleadosInterface;
import InterfacePersistencia.PersistenciaVWEstadosExtrasInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

/**
 *
 * @author Administrador
 */
@Stateful
public class AdministrarATHoraExtra implements AdministrarATHoraExtraInterface {

   private static Logger log = Logger.getLogger(AdministrarATHoraExtra.class);

   @EJB
   PersistenciaEmpleadoInterface persistenciaEmpleado;
   @EJB
   PersistenciaTurnosEmpleadosInterface persistenciaTurnosEmpleados;
   @EJB
   PersistenciaMotivosTurnosInterface persistenciaMotivosTurnos;
   @EJB
   PersistenciaEstructurasInterface persistenciaEstructuras;
   @EJB
   PersistenciaVWEstadosExtrasInterface persistenciaVWEstadosExtras;
   @EJB
   AdministrarSesionesInterface administrarSesiones;

   private EntityManagerFactory emf;
   private EntityManager em;
   private String idSesionBck;

   private EntityManager getEm() {
      try {
         if (this.emf != null) {
            if (this.em != null) {
               if (this.em.isOpen()) {
                  this.em.close();
               }
            }
         } else {
            this.emf = administrarSesiones.obtenerConexionSesionEMF(idSesionBck);
         }
         this.em = emf.createEntityManager();
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " getEm() ERROR : " + e);
      }
      return this.em;
   }

   //--------------------------------------------------------------------------
   //MÉTODOS
   //--------------------------------------------------------------------------
   @Override
   public void obtenerConexion(String idSesion) {
      idSesionBck = idSesion;
      try {
         emf = administrarSesiones.obtenerConexionSesionEMF(idSesion);
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " obtenerConexion ERROR: " + e);
      }
   }

   @Override
   public List<Empleados> buscarEmpleados() {
      try {
         List<Empleados> lista = persistenciaEmpleado.buscarEmpleadosATHoraExtra(getEm());
         return lista;
      } catch (Exception e) {
         log.warn("Error buscarEmpleados Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<TurnosEmpleados> buscarTurnosEmpleadosPorEmpleado(BigInteger secuencia) {
      try {
         List<TurnosEmpleados> lista = persistenciaTurnosEmpleados.buscarTurnosEmpleadosPorEmpleado(getEm(), secuencia);
         return lista;
      } catch (Exception e) {
         log.warn("Error buscarTurnosEmpleadosPorEmpleado Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public void crearTurnosEmpleados(List<TurnosEmpleados> listaTR) {
      try {
         for (int i = 0; i < listaTR.size(); i++) {
            if (listaTR.get(i).getMotivoturno().getSecuencia() == null) {
               listaTR.get(i).setMotivoturno(null);
            }
            if (listaTR.get(i).getEstructuraaprueba().getSecuencia() == null) {
               listaTR.get(i).setEstructuraaprueba(null);
            }
            persistenciaTurnosEmpleados.crear(getEm(), listaTR.get(i));
         }
      } catch (Exception e) {
         log.warn("Error crearTurnosEmpleados Admi : " + e.toString());
      }
   }

   @Override
   public void editarTurnosEmpleados(List<TurnosEmpleados> listaTR) {
      try {
         for (int i = 0; i < listaTR.size(); i++) {
            if (listaTR.get(i).getMotivoturno().getSecuencia() == null) {
               listaTR.get(i).setMotivoturno(null);
            }
            if (listaTR.get(i).getEstructuraaprueba().getSecuencia() == null) {
               listaTR.get(i).setEstructuraaprueba(null);
            }
            persistenciaTurnosEmpleados.editar(getEm(), listaTR.get(i));
         }
      } catch (Exception e) {
         log.warn("Error editarTurnosEmpleados Admi : " + e.toString());
      }
   }

   @Override
   public void borrarTurnosEmpleados(List<TurnosEmpleados> listaTR) {
      try {
         for (int i = 0; i < listaTR.size(); i++) {
            if (listaTR.get(i).getMotivoturno().getSecuencia() == null) {
               listaTR.get(i).setMotivoturno(null);
            }
            if (listaTR.get(i).getEstructuraaprueba().getSecuencia() == null) {
               listaTR.get(i).setEstructuraaprueba(null);
            }
            persistenciaTurnosEmpleados.borrar(getEm(), listaTR.get(i));
         }
      } catch (Exception e) {
         log.warn("Error borrarTurnosEmpleados Admi : " + e.toString());
      }
   }

   @Override
   public List<VWEstadosExtras> buscarDetallesHorasExtrasPorTurnoEmpleado(BigInteger secuencia) {
      try {
         return persistenciaVWEstadosExtras.buscarVWEstadosExtras(getEm(), secuencia);
      } catch (Exception e) {
         log.warn("Error buscarDetallesHorasExtrasPorTurnoEmpleado Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<MotivosTurnos> lovMotivosTurnos() {
      try {
         return persistenciaMotivosTurnos.consultarMotivosTurnos(getEm());
      } catch (Exception e) {
         log.warn("Error lovMotivosTurnos Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<Estructuras> lovEstructuras() {
      try {
         return persistenciaEstructuras.consultarEstructurasTurnoEmpleado(getEm());
      } catch (Exception e) {
         log.warn("Error lovEstructuras Admi : " + e.toString());
         return null;
      }
   }

}
