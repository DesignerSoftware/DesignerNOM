/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.MotivosCambiosCargos;
import InterfaceAdministrar.AdministrarMotivosCambiosCargosInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaMotivosCambiosCargosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

/**
 *
 * @author Administrator
 */
@Stateful
public class AdministrarMotivosCambiosCargos implements AdministrarMotivosCambiosCargosInterface {

   private static Logger log = Logger.getLogger(AdministrarMotivosCambiosCargos.class);

   @EJB
   PersistenciaMotivosCambiosCargosInterface persistenciaMotivosCambiosCargos;

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
   public List<MotivosCambiosCargos> consultarMotivosCambiosCargos() {
      try {
         return persistenciaMotivosCambiosCargos.buscarMotivosCambiosCargos(getEm());
      } catch (Exception e) {
         log.warn("AdministrarMotivosCambiosCargos.consultarMotivosCambiosCargos.");
         log.warn("Excepcion.");
         log.warn(e);
         return null;
      }
   }

   @Override
   public MotivosCambiosCargos consultarMotivoCambioCargo(BigInteger secuenciaMCC) {
      try {
         return persistenciaMotivosCambiosCargos.buscarMotivoCambioCargo(getEm(), secuenciaMCC);
      } catch (Exception e) {
         return null;
      }
   }

   @Override
   public void modificarMotivosCambiosCargos(List<MotivosCambiosCargos> listaMotivosCambiosCargos) {
      try {
         for (int i = 0; i < listaMotivosCambiosCargos.size(); i++) {
            log.warn("Administrar Modificando");
            persistenciaMotivosCambiosCargos.editar(getEm(), listaMotivosCambiosCargos.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void borrarMotivosCambiosCargos(List<MotivosCambiosCargos> listaMotivosCambiosCargos) {
      try {
         for (int i = 0; i < listaMotivosCambiosCargos.size(); i++) {
            log.warn("Administrar Borrando");
            persistenciaMotivosCambiosCargos.borrar(getEm(), listaMotivosCambiosCargos.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void crearMotivosCambiosCargos(List<MotivosCambiosCargos> listaMotivosCambiosCargos) {
      try {
         for (int i = 0; i < listaMotivosCambiosCargos.size(); i++) {
            log.warn("Administrar Creando");
            persistenciaMotivosCambiosCargos.crear(getEm(), listaMotivosCambiosCargos.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public BigInteger contarVigenciasCargosMotivoCambioCargo(BigInteger secuenciaMovitoCambioCargo) {
      try {
         return persistenciaMotivosCambiosCargos.verificarBorradoVigenciasCargos(getEm(), secuenciaMovitoCambioCargo);
      } catch (Exception e) {
         log.warn("AdministrarMotivosCambiosCargos.verificarBorradoVC.");
         log.error("Excepcion.");
         log.warn(e);
         return null;
      }
   }
}
