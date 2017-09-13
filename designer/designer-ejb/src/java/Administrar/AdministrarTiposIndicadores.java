/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import InterfaceAdministrar.AdministrarTiposIndicadoresInterface;
import Entidades.TiposIndicadores;
import InterfacePersistencia.PersistenciaTiposIndicadoresInterface;
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
public class AdministrarTiposIndicadores implements AdministrarTiposIndicadoresInterface {

   private static Logger log = Logger.getLogger(AdministrarTiposIndicadores.class);

   @EJB
   PersistenciaTiposIndicadoresInterface persistenciaTiposIndicadores;
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
   public void modificarTiposIndicadores(List<TiposIndicadores> listTiposIndicadores) {
      try {
         for (int i = 0; i < listTiposIndicadores.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaTiposIndicadores.editar(getEm(), listTiposIndicadores.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".modificarTiposIndicadores() ERROR: " + e);
      }
   }

   @Override
   public void borrarTiposIndicadores(List<TiposIndicadores> listTiposIndicadores) {
      try {
         for (int i = 0; i < listTiposIndicadores.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaTiposIndicadores.borrar(getEm(), listTiposIndicadores.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".borrarTiposIndicadores() ERROR: " + e);
      }
   }

   @Override
   public void crearTiposIndicadores(List<TiposIndicadores> listTiposIndicadores) {
      try {
         for (int i = 0; i < listTiposIndicadores.size(); i++) {
            log.warn("Administrar Creando...");
            persistenciaTiposIndicadores.crear(getEm(), listTiposIndicadores.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".crearTiposIndicadores() ERROR: " + e);
      }
   }

   @Override
   public List<TiposIndicadores> consultarTiposIndicadores() {
      try {
         return persistenciaTiposIndicadores.buscarTiposIndicadores(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarTiposIndicadores() ERROR: " + e);
         return null;
      }
   }

   @Override
   public TiposIndicadores consultarTipoIndicador(BigInteger secMotivoDemanda) {
      try {
         return persistenciaTiposIndicadores.buscarTiposIndicadoresSecuencia(getEm(), secMotivoDemanda);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarTipoIndicador() ERROR: " + e);
         return null;
      }
   }

   @Override
   public BigInteger contarVigenciasIndicadoresTipoIndicador(BigInteger secuenciaVigenciasIndicadores) {
      try {
         log.error("Secuencia Vigencias Indicadores " + secuenciaVigenciasIndicadores);
         return persistenciaTiposIndicadores.contadorVigenciasIndicadores(getEm(), secuenciaVigenciasIndicadores);
      } catch (Exception e) {
         log.error("ERROR AdmnistrarTiposIndicadores verificarBorradoVigenciasIndicadores ERROR :" + e);
         return null;
      }
   }
}
