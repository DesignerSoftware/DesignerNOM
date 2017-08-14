/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.EmpresasOpcionesKioskos;
import InterfaceAdministrar.AdministrarEmpresasOpcionesKioskosInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaEmpresasOpcionesKioskosInterface;
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
public class AdministrarEmpresasOpcionesKioskos implements AdministrarEmpresasOpcionesKioskosInterface {

   private static Logger log = Logger.getLogger(AdministrarEmpresasOpcionesKioskos.class);

   @EJB
   PersistenciaEmpresasOpcionesKioskosInterface persistenciaEmpresasOK;
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
   public void modificarEmpresasOpcionesKioskos(List<EmpresasOpcionesKioskos> lista) {
      try {
         for (int i = 0; i < lista.size(); i++) {
            persistenciaEmpresasOK.editar(getEm(), lista.get(i));
         }
      } catch (Exception e) {
         log.warn("error en modificarEmpresasOpcionesKioskos admi : " + e.getMessage());
      }
   }

   @Override
   public void borrarEmpresasOpcionesKioskos(List<EmpresasOpcionesKioskos> lista) {
      try {
         for (int i = 0; i < lista.size(); i++) {
            persistenciaEmpresasOK.borrar(getEm(), lista.get(i));
         }
      } catch (Exception e) {
         log.warn("error en borrarEmpresasOpcionesKioskos admi : " + e.getMessage());
      }
   }

   @Override
   public void crearEmpresasOpcionesKioskos(List<EmpresasOpcionesKioskos> lista) {
      try {
         for (int i = 0; i < lista.size(); i++) {
            persistenciaEmpresasOK.crear(getEm(), lista.get(i));
         }
      } catch (Exception e) {
         log.warn("error en crearEmpresasOpcionesKioskos admi : " + e.getMessage());
      }
   }

   @Override
   public List<EmpresasOpcionesKioskos> consultarEmpresasOpcionesKioskos() {
      try {
         return persistenciaEmpresasOK.consultarEmpresaOpKioskos(getEm());
      } catch (Exception e) {
         log.warn("error en consultarEmpresasOpcionesKioskos : " + e.getMessage());
         return null;
      }
   }

}
