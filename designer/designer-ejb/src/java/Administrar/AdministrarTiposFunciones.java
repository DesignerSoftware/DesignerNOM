/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.TiposFunciones;
import InterfaceAdministrar.AdministrarTiposFuncionesInterface;
import InterfacePersistencia.PersistenciaTiposFuncionesInterface;
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
public class AdministrarTiposFunciones implements AdministrarTiposFuncionesInterface {

   private static Logger log = Logger.getLogger(AdministrarTiposFunciones.class);

   @EJB
   PersistenciaTiposFuncionesInterface persistenciaTiposFunciones;
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
   public List<TiposFunciones> buscarTiposFunciones(BigInteger secuenciaOperando) {
      try {
         return persistenciaTiposFunciones.tiposFunciones(getEm(), secuenciaOperando);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public void borrarTiposFunciones(TiposFunciones tiposFunciones) {
      try {
         persistenciaTiposFunciones.borrar(getEm(), tiposFunciones);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void crearTiposFunciones(TiposFunciones tiposFunciones) {
      try {
         persistenciaTiposFunciones.crear(getEm(), tiposFunciones);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void modificarTiposFunciones(List<TiposFunciones> listaTiposFuncionesModificar) {
      try {
         for (int i = 0; i < listaTiposFuncionesModificar.size(); i++) {
            log.warn("Modificando...");
            if (listaTiposFuncionesModificar.get(i).getFechafinal() == null) {
               listaTiposFuncionesModificar.get(i).setFechafinal(null);
            }
            if (listaTiposFuncionesModificar.get(i).getNombreobjeto() == null) {
               listaTiposFuncionesModificar.get(i).setNombreobjeto(null);
            }
            persistenciaTiposFunciones.editar(getEm(), listaTiposFuncionesModificar.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }
}
