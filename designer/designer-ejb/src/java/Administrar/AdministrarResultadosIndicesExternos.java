/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.IndicesExternos;
import Entidades.ResultadosIndicesExternos;
import InterfaceAdministrar.AdministrarResultadosIndicesExternosInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaResultadosIndicesExternosInterface;
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
public class AdministrarResultadosIndicesExternos implements AdministrarResultadosIndicesExternosInterface {

   private static Logger log = Logger.getLogger(AdministrarResultadosIndicesExternos.class);

   @EJB
   AdministrarSesionesInterface administrarSesiones;
   @EJB
   PersistenciaResultadosIndicesExternosInterface persistenciaIndicesExternos;

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
   public void crearResultado(List<ResultadosIndicesExternos> listaCrear) {
      try {
         for (int i = 0; i < listaCrear.size(); i++) {
            persistenciaIndicesExternos.crear(getEm(), listaCrear.get(i));
         }
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void modificarResultado(List<ResultadosIndicesExternos> listaModificar) {
      try {
         for (int i = 0; i < listaModificar.size(); i++) {
            persistenciaIndicesExternos.editar(getEm(), listaModificar.get(i));
         }
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void borrarResultado(List<ResultadosIndicesExternos> listaBorrar) {
      try {
         for (int i = 0; i < listaBorrar.size(); i++) {
            persistenciaIndicesExternos.borrar(getEm(), listaBorrar.get(i));
         }
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public List<IndicesExternos> consultarIndicesExternos() {
      try {
         return persistenciaIndicesExternos.buscarIndicesExternos(getEm());
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<ResultadosIndicesExternos> consultarResultadosIndicesExternos() {
      try {
         return persistenciaIndicesExternos.buscarResultadosIndicesExternos(getEm());
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }
}
