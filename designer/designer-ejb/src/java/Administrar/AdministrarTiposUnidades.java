/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.TiposUnidades;
import InterfaceAdministrar.AdministrarTiposUnidadesInterface;
import InterfacePersistencia.PersistenciaTiposUnidadesInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import InterfaceAdministrar.AdministrarSesionesInterface;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

/**
 *
 * @author user
 */
@Stateless
public class AdministrarTiposUnidades implements AdministrarTiposUnidadesInterface {

   private static Logger log = Logger.getLogger(AdministrarTiposUnidades.class);

   @EJB
   PersistenciaTiposUnidadesInterface persistenciaTiposUnidades;
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
   public void modificarTiposUnidades(List<TiposUnidades> listaTiposUnidades) {
      try {
         for (int i = 0; i < listaTiposUnidades.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaTiposUnidades.editar(getEm(), listaTiposUnidades.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void borrarTiposUnidades(List<TiposUnidades> listaTiposUnidades) {
      try {
         for (int i = 0; i < listaTiposUnidades.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaTiposUnidades.borrar(getEm(), listaTiposUnidades.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void crearTiposUnidades(List<TiposUnidades> listaTiposUnidades) {
      try {
         for (int i = 0; i < listaTiposUnidades.size(); i++) {
            log.warn("Administrar Creando...");
            persistenciaTiposUnidades.crear(getEm(), listaTiposUnidades.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   public List<TiposUnidades> consultarTiposUnidades() {
      try {
         return persistenciaTiposUnidades.consultarTiposUnidades(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public TiposUnidades consultarTipoIndicador(BigInteger secMotivoDemanda) {
      try {
         return persistenciaTiposUnidades.consultarTipoUnidad(getEm(), secMotivoDemanda);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public BigInteger contarUnidadesTipoUnidad(BigInteger secuenciaVigenciasIndicadores) {
      try {
         log.error("Secuencia Vigencias Indicadores " + secuenciaVigenciasIndicadores);
         return persistenciaTiposUnidades.contarUnidadesTipoUnidad(getEm(), secuenciaVigenciasIndicadores);
      } catch (Exception e) {
         log.error("ERROR AdmnistrarTiposUnidades contarUnidadesTipoUnidad ERROR :" + e);
         return null;
      }
   }
}
