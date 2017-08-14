package Administrar;

import Entidades.Contratos;
import Entidades.Empleados;
import Entidades.TiposContratos;
import Entidades.VigenciasContratos;
import InterfaceAdministrar.AdministrarVigenciasContratosInterface;
import InterfacePersistencia.PersistenciaContratosInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaTiposContratosInterface;
import InterfacePersistencia.PersistenciaVigenciasContratosInterface;
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

public class AdministrarVigenciasContratos implements AdministrarVigenciasContratosInterface {

   private static Logger log = Logger.getLogger(AdministrarVigenciasContratos.class);

   @EJB
   PersistenciaContratosInterface persistenciaContratos;
   @EJB
   PersistenciaEmpleadoInterface persistenciaEmpleado;
   @EJB
   PersistenciaTiposContratosInterface persistenciaTiposContratos;
   @EJB
   PersistenciaVigenciasContratosInterface persistenciaVigenciasContratos;
   /**
    * Enterprise JavaBean.<br>
    * Atributo que representa todo lo referente a la conexión del usuario que
    * está usando el aplicativo.
    */
   @EJB
   AdministrarSesionesInterface administrarSesiones;

//   List<VigenciasContratos> vigenciasContratos;
//   VigenciasContratos vigenciaContrato;
//   Empleados empleado;
//   List<Contratos> contratos;
//   List<TiposContratos> tiposContratos;
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
   public List<VigenciasContratos> VigenciasContratosEmpleado(BigInteger secEmpleado) {
      try {
         return persistenciaVigenciasContratos.buscarVigenciaContratoEmpleado(getEm(), secEmpleado);
      } catch (Exception e) {
         log.warn("Error en Administrar Vigencias Contratos (VigenciasContratosEmpleado)");
         return null;
      }
   }

   @Override
   public void modificarVC(List<VigenciasContratos> listVCModificadas) {
      try {
         for (int i = 0; i < listVCModificadas.size(); i++) {
            log.warn("Modificando...");
            persistenciaVigenciasContratos.editar(getEm(), listVCModificadas.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void borrarVC(VigenciasContratos vigenciasContratos) {
      try {
         persistenciaVigenciasContratos.borrar(getEm(), vigenciasContratos);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void crearVC(VigenciasContratos vigenciasContratos) {
      try {
         persistenciaVigenciasContratos.crear(getEm(), vigenciasContratos);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
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
   public List<Contratos> contratos() {
      try {
         return persistenciaContratos.buscarContratos(getEm());
      } catch (Exception e) {
         return null;
      }
   }

   @Override
   public List<TiposContratos> tiposContratos() {
      try {
         return persistenciaTiposContratos.tiposContratos(getEm());
      } catch (Exception e) {
         return null;
      }
   }

}
