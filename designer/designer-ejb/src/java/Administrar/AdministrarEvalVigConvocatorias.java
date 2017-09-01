/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Evalvigconvocatorias;
import InterfaceAdministrar.AdministrarEvalVigConvocatoriasInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaEvalVigConvocatoriasInterface;
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
public class AdministrarEvalVigConvocatorias implements AdministrarEvalVigConvocatoriasInterface {

   private static Logger log = Logger.getLogger(AdministrarEvalVigConvocatorias.class);

   @EJB
   AdministrarSesionesInterface administrarSesiones;
   @EJB
   PersistenciaEvalVigConvocatoriasInterface persistenciaevalvigconv;
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
   public void crear(List<Evalvigconvocatorias> listCrear) {
      try {
         for (int i = 0; i < listCrear.size(); i++) {
            persistenciaevalvigconv.crear(getEm(), listCrear.get(i));
         }
      } catch (Exception e) {
         log.warn("AdministrarEvalVigConvocatorias.crear : " + e.toString());
      }
   }

   @Override
   public void borrar(List<Evalvigconvocatorias> listBorrar) {
      try {
         for (int i = 0; i < listBorrar.size(); i++) {
            persistenciaevalvigconv.borrar(getEm(), listBorrar.get(i));
         }
      } catch (Exception e) {
         log.warn("AdministrarEvalVigConvocatorias.borrar : " + e.toString());
      }

   }

   @Override
   public void editar(List<Evalvigconvocatorias> listModificar) {
      try {
         for (int i = 0; i < listModificar.size(); i++) {
            persistenciaevalvigconv.editar(getEm(), listModificar.get(i));
         }
      } catch (Exception e) {
         log.warn("AdministrarEvalVigConvocatorias.editar : " + e.toString());
      }
   }

   @Override
   public List<Evalvigconvocatorias> buscarEvalVigConvocatorias() {
      try {
         return persistenciaevalvigconv.consultarEvalConvocatorias(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

}
