/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import InterfaceAdministrar.AdministrarProfesionesInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaProfesionesInterface;
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
public class AdministrarProfesiones implements AdministrarProfesionesInterface {

   private static Logger log = Logger.getLogger(AdministrarProfesiones.class);

   @EJB
   PersistenciaProfesionesInterface persistenciaprofesiones;
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
   public List<Entidades.Profesiones> Profesiones() {
      try {
         return persistenciaprofesiones.profesiones(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<Entidades.Profesiones> lovProfesiones() {
      try {
         return persistenciaprofesiones.profesiones(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public void crear(List<Entidades.Profesiones> listaCrear) {
      try {
         for (int i = 0; i < listaCrear.size(); i++) {
            persistenciaprofesiones.crear(getEm(), listaCrear.get(i));
         }
      } catch (Exception e) {
         log.warn("Error en AdministrarProfesiones.crear : " + e.toString());
      }
   }

   @Override
   public void editar(List<Entidades.Profesiones> listaEditar) {
      try {
         for (int i = 0; i < listaEditar.size(); i++) {
            persistenciaprofesiones.editar(getEm(), listaEditar.get(i));
         }
      } catch (Exception e) {
         log.warn("Error en AdministrarProfesiones.editar : " + e.toString());
      }
   }

   @Override
   public void borrar(List<Entidades.Profesiones> listaBorrar) {
      try {
         for (int i = 0; i < listaBorrar.size(); i++) {
            persistenciaprofesiones.borrar(getEm(), listaBorrar.get(i));
         }
      } catch (Exception e) {
         log.warn("Error en AdministrarProfesiones.borrar : " + e.toString());
      }
   }

   // Add business logic below. (Right-click in editor and choose
   // "Insert Code > Add Business Method")
}
