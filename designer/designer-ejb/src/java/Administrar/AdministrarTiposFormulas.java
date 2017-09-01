/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Formulas;
import Entidades.TiposFormulas;
import InterfaceAdministrar.AdministrarTiposFormulasInterface;
import InterfacePersistencia.PersistenciaFormulasInterface;
import InterfacePersistencia.PersistenciaTiposFormulasInterface;
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
public class AdministrarTiposFormulas implements AdministrarTiposFormulasInterface {

   private static Logger log = Logger.getLogger(AdministrarTiposFormulas.class);

   @EJB
   PersistenciaTiposFormulasInterface persistenciaTiposFormulas;
   @EJB
   PersistenciaFormulasInterface persistenciaFormulas;
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
   public List<TiposFormulas> buscarTiposFormulas(BigInteger secuenciaOperando) {
      List<TiposFormulas> listaTiposFormulas;
      listaTiposFormulas = persistenciaTiposFormulas.tiposFormulas(getEm(), secuenciaOperando);
      return listaTiposFormulas;
   }

   @Override
   public void borrarTiposFormulas(TiposFormulas tiposFormulas) {
      persistenciaTiposFormulas.borrar(getEm(), tiposFormulas);
   }

   @Override
   public void crearTiposFormulas(TiposFormulas tiposFormulas) {
      persistenciaTiposFormulas.crear(getEm(), tiposFormulas);
   }

   @Override
   public void modificarTiposFormulas(TiposFormulas tiposFormulas) {
      persistenciaTiposFormulas.editar(getEm(), tiposFormulas);

   }

   public List<Formulas> lovFormulas() {
      List<Formulas> listaFormulas;
      listaFormulas = persistenciaFormulas.buscarFormulas(getEm());
      return listaFormulas;
   }
}
