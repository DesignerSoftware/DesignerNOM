package Administrar;

import Entidades.Empleados;
import Entidades.VWVacaPendientesEmpleados;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfaceAdministrar.AdministrarVWVacaPendientesEmpleadosInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaSolucionesNodosInterface;
import InterfacePersistencia.PersistenciaVWVacaPendientesEmpleadosInterface;
import InterfacePersistencia.PersistenciaVigenciasTiposContratosInterface;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

/**
 *
 * @author AndresPineda
 */
@Stateless
public class AdministrarVWVacaPendientesEmpleados implements AdministrarVWVacaPendientesEmpleadosInterface {

   private static Logger log = Logger.getLogger(AdministrarVWVacaPendientesEmpleados.class);

   @EJB
   PersistenciaVWVacaPendientesEmpleadosInterface persistenciaVWVacaPendientesEmpleados;
   @EJB
   PersistenciaEmpleadoInterface persistenciaEmpleado;
   @EJB
   PersistenciaSolucionesNodosInterface persistenciaSolucionesNodos;
   @EJB
   PersistenciaVigenciasTiposContratosInterface persistenciaVigenciasTiposContratos;
   /**
    * Enterprise JavaBean.<br>
    * Atributo que representa todo lo referente a la conexión del usuario que
    * está usando el aplicativo.
    */
   @EJB
   AdministrarSesionesInterface administrarSesiones;

//    Empleados empleado;
//    List<VWVacaPendientesEmpleados> vacaciones;
//    BigDecimal unidades;
   private EntityManagerFactory emf;
   private EntityManager em; private String idSesionBck;

   private EntityManager getEm() {
      try {
         if (this.emf != null) { if (this.em != null) {
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
   //

   @Override
   public void obtenerConexion(String idSesion) { idSesionBck = idSesion;
      try {
         emf = administrarSesiones.obtenerConexionSesionEMF(idSesion);
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " obtenerConexion ERROR: " + e);
      }
   }

   @Override
   public void crearVacaPendiente(VWVacaPendientesEmpleados vaca) {
      try {
         persistenciaVWVacaPendientesEmpleados.crear(getEm(), vaca);
      } catch (Exception e) {
         log.warn("Error en crearVacaPeniente Admi : " + e.toString());
      }
   }

   @Override
   public void editarVacaPendiente(VWVacaPendientesEmpleados vaca) {
      try {
         persistenciaVWVacaPendientesEmpleados.editar(getEm(), vaca);
      } catch (Exception e) {
         log.warn("Error en editarVacaPendiente Admi : " + e.toString());
      }
   }

   @Override
   public void borrarVacaPendiente(VWVacaPendientesEmpleados vaca) {
      try {
         persistenciaVWVacaPendientesEmpleados.borrar(getEm(), vaca);
      } catch (Exception e) {
         log.warn("Error en borrarVacaPendiente Admi : " + e.toString());
      }
   }

   @Override
   public List<VWVacaPendientesEmpleados> vacaPendientesPendientes(Empleados empl, Date fechaingreso) {
      try {
         return persistenciaVWVacaPendientesEmpleados.vacaEmpleadoPendientes(getEm(), empl.getSecuencia(), fechaingreso);
      } catch (Exception e) {
         log.warn("Error en vacaPendientesMayorCero Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<VWVacaPendientesEmpleados> vacaPendientesDisfrutadas(Empleados empl, Date fechaingreso) {
      try {
         return persistenciaVWVacaPendientesEmpleados.vacaEmpleadoDisfrutadas(getEm(), empl.getSecuencia(), fechaingreso);
      } catch (Exception e) {
         log.warn("Error en vacaPendientesIgualCero Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public Empleados obtenerEmpleado(BigInteger secuencia) {
      try {
         return persistenciaEmpleado.buscarEmpleado(getEm(), secuencia);
      } catch (Exception e) {
         log.warn("Error en obtener empleado Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public BigDecimal diasProvisionadosEmpleado(Empleados empl, Date fechaContratacion) {
      try {
         return persistenciaSolucionesNodos.diasProvisionados(getEm(), empl.getSecuencia(), fechaContratacion);
      } catch (Exception e) {
         log.warn("Error en diasProvisionadosEmpleado Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public Date obtenerFechaFinalContratacionEmpleado(BigInteger secEmpleado) {
      try {
         return persistenciaVigenciasTiposContratos.fechaFinalContratacionVacaciones(getEm(), secEmpleado);
      } catch (Exception e) {
         log.warn("Error obtenerFechaFinalContratacionEmpleado Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<VWVacaPendientesEmpleados> vacaPendientesPendientesAnterioresContratos(Empleados empl) {
      try {
         return persistenciaVWVacaPendientesEmpleados.vacaEmpleadoPendientesAnterioresContratos(getEm(), empl.getSecuencia());
      } catch (Exception e) {
         log.warn("Error en vacaPendientesIgualCero Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<VWVacaPendientesEmpleados> vacaPendientesDisfrutadasAnterioresContratos(Empleados empl) {
      try {
         return persistenciaVWVacaPendientesEmpleados.vacaEmpleadoDisfrutadasAnterioresContratos(getEm(), empl.getSecuencia());
      } catch (Exception e) {
         log.warn("Error en vacaPendientesIgualCero Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public Date obtenerFechaMaxContrato(BigInteger secEmpleado) {
      try {
         return persistenciaVigenciasTiposContratos.fechaMaxContrato(getEm(), secEmpleado);
      } catch (Exception e) {
         log.warn("Error obtenerFechaFinalContratacionEmpleado Admi : " + e.toString());
         return null;
      }
   }
}
