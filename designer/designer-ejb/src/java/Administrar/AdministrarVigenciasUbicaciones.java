package Administrar;

import Entidades.Empleados;
import Entidades.UbicacionesGeograficas;
import Entidades.VigenciasUbicaciones;
import InterfaceAdministrar.AdministrarVigenciasUbicacionesInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaUbicacionesGeograficasInterface;
import InterfacePersistencia.PersistenciaVigenciasUbicacionesInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import InterfaceAdministrar.AdministrarSesionesInterface;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

@Stateful
public class AdministrarVigenciasUbicaciones implements AdministrarVigenciasUbicacionesInterface {

   private static Logger log = Logger.getLogger(AdministrarVigenciasUbicaciones.class);

   @EJB
   PersistenciaUbicacionesGeograficasInterface persistenciaUbicacionesGeograficas;
   @EJB
   PersistenciaVigenciasUbicacionesInterface persistenciaVigenciasUbicaciones;
   @EJB
   PersistenciaEmpleadoInterface persistenciaEmpleado;
   /**
    * Enterprise JavaBean.<br>
    * Atributo que representa todo lo referente a la conexión del usuario que
    * está usando el aplicativo.
    */
   @EJB
   AdministrarSesionesInterface administrarSesiones;

   private VigenciasUbicaciones vu;
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
   public List<VigenciasUbicaciones> vigenciasUbicacionesEmpleado(BigInteger secEmpleado) {
      try {
         return persistenciaVigenciasUbicaciones.buscarVigenciaUbicacionesEmpleado(getEm(), secEmpleado);
      } catch (Exception e) {
         log.warn("Error en Administrar Vigencias Ubiaciones (vigenciasUbicacionesEmpleado)");
         return null;
      }
   }

   @Override
   public void modificarVU(List<VigenciasUbicaciones> listVUModificadas) {
      try {
         for (int i = 0; i < listVUModificadas.size(); i++) {
            log.warn("Modificando...");
            vu = listVUModificadas.get(i);
            persistenciaVigenciasUbicaciones.editar(getEm(), vu);
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void borrarVU(VigenciasUbicaciones vigenciasUbicaciones) {
      try {
         persistenciaVigenciasUbicaciones.borrar(getEm(), vigenciasUbicaciones);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void crearVU(VigenciasUbicaciones vigenciasUbicaciones) {
      try {
         persistenciaVigenciasUbicaciones.crear(getEm(), vigenciasUbicaciones);
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
   public List<UbicacionesGeograficas> ubicacionesGeograficas() {
      try {
         return persistenciaUbicacionesGeograficas.consultarUbicacionesGeograficas(getEm());
      } catch (Exception e) {
         return null;
      }
   }
}
