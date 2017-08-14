package Administrar;

import Entidades.Ciudades;
import Entidades.Empleados;
import Entidades.MotivosContratos;
import Entidades.TiposContratos;
import Entidades.VigenciasTiposContratos;
import InterfaceAdministrar.AdministrarVigenciasTiposContratosInterface;
import InterfacePersistencia.PersistenciaCiudadesInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaMotivosContratosInterface;
import InterfacePersistencia.PersistenciaTiposContratosInterface;
import InterfacePersistencia.PersistenciaVigenciasTiposContratosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import InterfaceAdministrar.AdministrarSesionesInterface;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

@Stateful
public class AdministrarVigenciasTiposContratos implements AdministrarVigenciasTiposContratosInterface {

   private static Logger log = Logger.getLogger(AdministrarVigenciasTiposContratos.class);

   @EJB
   PersistenciaVigenciasTiposContratosInterface persistenciaVigenciasTiposContratos;
   @EJB
   PersistenciaEmpleadoInterface persistenciaEmpleado;
   @EJB
   PersistenciaCiudadesInterface persistenciaCiudades;
   @EJB
   PersistenciaMotivosContratosInterface persistenciaMotivosContratos;
   @EJB
   PersistenciaTiposContratosInterface persistenciaTiposContratos;
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

   @Override
   public void obtenerConexion(String idSesion) {
      try {
         emf = administrarSesiones.obtenerConexionSesionEMF(idSesion);
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " obtenerConexion ERROR: " + e);
      }
   }

   @Override
   public List<VigenciasTiposContratos> vigenciasTiposContratosEmpleado(BigInteger secEmpleado) {
      try {
         return persistenciaVigenciasTiposContratos.buscarVigenciaTipoContratoEmpleado(getEm(), secEmpleado);
      } catch (Exception e) {
         log.warn("Error :/");
         return null;
      }
   }

   @Override
   public void modificarVTC(List<VigenciasTiposContratos> listVTCModificadas) {
      try {
         for (int i = 0; i < listVTCModificadas.size(); i++) {
            log.warn("Modificando...");
            if (listVTCModificadas.get(i).getCiudad().getSecuencia() == null) {
               listVTCModificadas.get(i).setCiudad(null);
            }
            persistenciaVigenciasTiposContratos.editar(getEm(), listVTCModificadas.get(i));
         }
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void borrarVTC(VigenciasTiposContratos vigenciasTipoContrato) {
      try {
         persistenciaVigenciasTiposContratos.borrar(getEm(), vigenciasTipoContrato);
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void crearVTC(VigenciasTiposContratos vigenciasTipoContrato) {
      try {
         persistenciaVigenciasTiposContratos.crear(getEm(), vigenciasTipoContrato);
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
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
   public List<Ciudades> ciudades() {
      try {
         return persistenciaCiudades.consultarCiudades(getEm());
      } catch (Exception e) {
         return null;
      }
   }

   @Override
   public List<MotivosContratos> motivosContratos() {
      try {
         return persistenciaMotivosContratos.motivosContratos(getEm());
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
