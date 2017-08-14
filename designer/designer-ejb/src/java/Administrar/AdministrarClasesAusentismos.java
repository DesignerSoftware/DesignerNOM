/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Clasesausentismos;
import Entidades.Tiposausentismos;
import InterfaceAdministrar.AdministrarClasesAusentismosInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaClasesAusentismosInterface;
import InterfacePersistencia.PersistenciaTiposAusentismosInterface;
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
public class AdministrarClasesAusentismos implements AdministrarClasesAusentismosInterface {

   private static Logger log = Logger.getLogger(AdministrarClasesAusentismos.class);

   @EJB
   PersistenciaClasesAusentismosInterface persistenciaClasesAusentismos;
   @EJB
   PersistenciaTiposAusentismosInterface PersistenciaTiposAusentismos;
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

   public void modificarClasesAusentismos(List<Clasesausentismos> listClasesAusentismos) {
      try {
         for (int i = 0; i < listClasesAusentismos.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaClasesAusentismos.editar(getEm(), listClasesAusentismos.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   public void borrarClasesAusentismos(List<Clasesausentismos> listClasesAusentismos) {
      try {
         for (int i = 0; i < listClasesAusentismos.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaClasesAusentismos.borrar(getEm(), listClasesAusentismos.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   public void crearClasesAusentismos(List<Clasesausentismos> listClasesAusentismos) {
      try {
         for (int i = 0; i < listClasesAusentismos.size(); i++) {
            log.warn("Administrar Creando...");
            persistenciaClasesAusentismos.crear(getEm(), listClasesAusentismos.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public List<Clasesausentismos> consultarClasesAusentismos() {
      try {
         List<Clasesausentismos> listClasesAusentismos = persistenciaClasesAusentismos.buscarClasesAusentismos(getEm());
         return listClasesAusentismos;
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   public BigInteger contarCausasAusentismosClaseAusentismo(BigInteger secuenciaClasesAusentismos) {

      try {
         log.error("Secuencia Borrado Elementos" + secuenciaClasesAusentismos);
         return persistenciaClasesAusentismos.contadorCausasAusentismosClaseAusentismo(getEm(), secuenciaClasesAusentismos);
      } catch (Exception e) {
         log.error("ERROR AdministrarClasesAusentismos contarCausasAusentismosClaseAusentismo ERROR :" + e);
         return null;
      }
   }

   public BigInteger contarSoAusentismosClaseAusentismo(BigInteger secuenciaClasesAusentismos) {
      try {
         log.error("Secuencia Borrado Elementos" + secuenciaClasesAusentismos);
         return persistenciaClasesAusentismos.contadorSoAusentismosClaseAusentismo(getEm(), secuenciaClasesAusentismos);
      } catch (Exception e) {
         log.error("ERROR AdministrarClasesAusentismos contarSoAusentismosClaseAusentismo ERROR :" + e);
         return null;
      }
   }

   public List<Tiposausentismos> consultarLOVTiposAusentismos() {
      try {
         List<Tiposausentismos> listTiposAusentismos = null;
         listTiposAusentismos = PersistenciaTiposAusentismos.consultarTiposAusentismos(getEm());
         return listTiposAusentismos;
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }
}
