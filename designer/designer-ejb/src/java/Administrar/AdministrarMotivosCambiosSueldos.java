/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import InterfaceAdministrar.AdministrarMotivosCambiosSueldosInterface;
import Entidades.MotivosCambiosSueldos;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaMotivosCambiosSueldosInterface;
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
public class AdministrarMotivosCambiosSueldos implements AdministrarMotivosCambiosSueldosInterface {

   private static Logger log = Logger.getLogger(AdministrarMotivosCambiosSueldos.class);

   @EJB
   PersistenciaMotivosCambiosSueldosInterface persistenciaMotivosCambiosSueldos;
   private MotivosCambiosSueldos motivoCambioSueldoSeleccionado;
   private MotivosCambiosSueldos motivoCambioSueldo;
   private List<MotivosCambiosSueldos> listMotivosCambiosCargos;
   /**
    * Enterprise JavaBean.<br>
    * Atributo que representa todo lo referente a la conexión del usuario que
    * está usando el aplicativo.
    */
   @EJB
   AdministrarSesionesInterface administrarSesiones;

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

   @Override
   public void obtenerConexion(String idSesion) { idSesionBck = idSesion;
      try {
         emf = administrarSesiones.obtenerConexionSesionEMF(idSesion);
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " obtenerConexion ERROR: " + e);
      }
   }

   @Override
   public void modificarMotivosCambiosSueldos(List<MotivosCambiosSueldos> listaMotivosCambiosSueldos) {
      try {
         for (int i = 0; i < listaMotivosCambiosSueldos.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaMotivosCambiosSueldos.editar(getEm(), listaMotivosCambiosSueldos.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".modificarMotivosCambiosSueldos() ERROR: " + e);
      }
   }

   @Override
   public void borrarMotivosCambiosSueldos(List<MotivosCambiosSueldos> listaMotivosCambiosSueldos) {
      try {
         for (int i = 0; i < listaMotivosCambiosSueldos.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaMotivosCambiosSueldos.borrar(getEm(), listaMotivosCambiosSueldos.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".borrarMotivosCambiosSueldos() ERROR: " + e);
      }
   }

   @Override
   public void crearMotivosCambiosSueldos(List<MotivosCambiosSueldos> listaMotivosCambiosSueldos) {
      try {
         for (int i = 0; i < listaMotivosCambiosSueldos.size(); i++) {
            log.warn("Administrar Creando...");
            persistenciaMotivosCambiosSueldos.crear(getEm(), listaMotivosCambiosSueldos.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".crearMotivosCambiosSueldos() ERROR: " + e);
      }
   }

   @Override
   public List<MotivosCambiosSueldos> consultarMotivosCambiosSueldos() {
      try {
         listMotivosCambiosCargos = persistenciaMotivosCambiosSueldos.buscarMotivosCambiosSueldos(getEm());
         return listMotivosCambiosCargos;
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarMotivosCambiosSueldos() ERROR: " + e);
         return null;
      }
   }

   @Override
   public MotivosCambiosSueldos consultarMotivoCambioCargo(BigInteger secMotivosCambiosSueldos) {
      try {
         motivoCambioSueldo = persistenciaMotivosCambiosSueldos.buscarMotivoCambioSueldoSecuencia(getEm(), secMotivosCambiosSueldos);
         return motivoCambioSueldo;
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarMotivoCambioCargo() ERROR: " + e);
         return null;
      }
   }

   @Override
   public BigInteger contarVigenciasSueldosMotivoCambioSueldo(BigInteger secuenciaMovitoCambioSueldo) {
      try {
         return persistenciaMotivosCambiosSueldos.verificarBorradoVigenciasSueldos(getEm(), secuenciaMovitoCambioSueldo);
      } catch (Exception e) {
         log.error("ERROR AdministrarMotivosCambiosSueldos verificarBorradoVS ERROR :" + e);
         return null;
      }
   }
}
