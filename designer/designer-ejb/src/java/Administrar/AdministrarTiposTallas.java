/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import InterfaceAdministrar.AdministrarTiposTallasInterface;
import Entidades.TiposTallas;
import InterfacePersistencia.PersistenciaTiposTallasInterface;
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
 * @author user
 */
@Stateful
public class AdministrarTiposTallas implements AdministrarTiposTallasInterface {

   private static Logger log = Logger.getLogger(AdministrarTiposTallas.class);

   @EJB
   PersistenciaTiposTallasInterface persistenciaTiposTallas;
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
   public void modificarTiposTallas(List<TiposTallas> listTiposTallas) {
      try {
         for (int i = 0; i < listTiposTallas.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaTiposTallas.editar(getEm(), listTiposTallas.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".modificarTiposTallas() ERROR: " + e);
      }
   }

   @Override
   public void borrarTiposTallas(List<TiposTallas> listTiposTallas) {
      try {
         for (int i = 0; i < listTiposTallas.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaTiposTallas.borrar(getEm(), listTiposTallas.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".borrarTiposTallas() ERROR: " + e);
      }
   }

   @Override
   public void crearTiposTallas(List<TiposTallas> listTiposTallas) {
      try {
         for (int i = 0; i < listTiposTallas.size(); i++) {
            log.warn("Administrar Creando...");
            persistenciaTiposTallas.crear(getEm(), listTiposTallas.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".crearTiposTallas() ERROR: " + e);
      }
   }

   @Override
   public List<TiposTallas> consultarTiposTallas() {
      try {
         return persistenciaTiposTallas.buscarTiposTallas(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarTiposTallas() ERROR: " + e);
         return null;
      }
   }

   @Override
   public TiposTallas consultarTipoTalla(BigInteger secTipoEmpresa) {
      try {
         return persistenciaTiposTallas.buscarTipoTalla(getEm(), secTipoEmpresa);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarTipoTalla() ERROR: " + e);
         return null;
      }
   }

   @Override
   public BigInteger contarElementosTipoTalla(BigInteger secuenciaElementos) {
      try {
         log.error("Secuencia Borrado Elementos" + secuenciaElementos);
         return persistenciaTiposTallas.contadorElementos(getEm(), secuenciaElementos);
      } catch (Exception e) {
         log.error("ERROR AdministrarTiposTallas verificarBorradoElementos ERROR :" + e);
         return null;
      }
   }

   @Override
   public BigInteger contarVigenciasTallasTipoTalla(BigInteger secuenciaVigenciasTallas) {
      try {
         log.error("Secuencia Borrado Vigencias Tallas" + secuenciaVigenciasTallas);
         return persistenciaTiposTallas.contadorVigenciasTallas(getEm(), secuenciaVigenciasTallas);
      } catch (Exception e) {
         log.error("ERROR AdministrarTiposTallas verificarBorradoVigenciasTallas ERROR :" + e);
         return null;
      }
   }
}
