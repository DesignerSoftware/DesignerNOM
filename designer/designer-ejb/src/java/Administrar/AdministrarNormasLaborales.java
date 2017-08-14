/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import InterfaceAdministrar.AdministrarNormasLaboralesInterface;
import Entidades.NormasLaborales;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaNormasLaboralesInterface;
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
public class AdministrarNormasLaborales implements AdministrarNormasLaboralesInterface {

   private static Logger log = Logger.getLogger(AdministrarNormasLaborales.class);

   @EJB
   PersistenciaNormasLaboralesInterface persistenciaNormasLaborales;
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
   public void modificarNormasLaborales(List<NormasLaborales> listaNormasLaborales) {
      try {
         for (int i = 0; i < listaNormasLaborales.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaNormasLaborales.editar(getEm(), listaNormasLaborales.get(i));
         }
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void borrarNormasLaborales(List<NormasLaborales> listaNormasLaborales) {
      try {
         for (int i = 0; i < listaNormasLaborales.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaNormasLaborales.borrar(getEm(), listaNormasLaborales.get(i));
         }
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void crearNormasLaborales(List<NormasLaborales> listaNormasLaborales) {
      try {
         for (int i = 0; i < listaNormasLaborales.size(); i++) {
            log.warn("Administrar Creando...");
            persistenciaNormasLaborales.crear(getEm(), listaNormasLaborales.get(i));
         }
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public List<NormasLaborales> consultarNormasLaborales() {
      try {
         return persistenciaNormasLaborales.consultarNormasLaborales(getEm());
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public NormasLaborales consultarMotivoContrato(BigInteger secNormasLaborales) {
      try {
         return persistenciaNormasLaborales.consultarNormaLaboral(getEm(), secNormasLaborales);
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public BigInteger contarVigenciasNormasEmpleadoNormaLaboral(BigInteger secuenciaNormasLaborales) {
      try {
         return persistenciaNormasLaborales.contarVigenciasNormasEmpleadosNormaLaboral(getEm(), secuenciaNormasLaborales);
      } catch (Exception e) {
         log.error("ERROR AdministrarNormasLaborales verificarBorradoVNE ERROR :" + e);
         return null;
      }
   }
}
