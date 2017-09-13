/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.SoTiposAntecedentes;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfaceAdministrar.AdministrarSoTiposAntecedentesInterface;
import InterfacePersistencia.PersistenciaSoTiposAntecedentesInterface;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

@Stateful
public class AdministrarSoTiposAntecedentes implements AdministrarSoTiposAntecedentesInterface {

   private static Logger log = Logger.getLogger(AdministrarSoTiposAntecedentes.class);

   @EJB
   AdministrarSesionesInterface administrarSesiones;
   @EJB
   PersistenciaSoTiposAntecedentesInterface persistenciaTiposAntecedentes;
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
   public void modificarTipoAntecedente(List<SoTiposAntecedentes> listaModificar) {
      try {
         for (int i = 0; i < listaModificar.size(); i++) {
            persistenciaTiposAntecedentes.editar(getEm(), listaModificar.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".modificarTipoAntecedente() ERROR: " + e);
      }
   }

   @Override
   public void crearTipoAntecedente(List<SoTiposAntecedentes> listaCrear) {
      try {
         for (int i = 0; i < listaCrear.size(); i++) {
            persistenciaTiposAntecedentes.crear(getEm(), listaCrear.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".crearTipoAntecedente() ERROR: " + e);
      }
   }

   @Override
   public void borrarTipoAntecedente(List<SoTiposAntecedentes> listaBorrar) {
      try {
         for (int i = 0; i < listaBorrar.size(); i++) {
            persistenciaTiposAntecedentes.borrar(getEm(), listaBorrar.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".borrarTipoAntecedente() ERROR: " + e);
      }
   }

   @Override
   public List<SoTiposAntecedentes> consultarTiposAntecedentes() {
      try {
         return persistenciaTiposAntecedentes.listaTiposAntecedentes(getEm());
      } catch (Exception e) {
         log.warn("error en consultarTiposAntecedentes : " + e.toString());
         return null;
      }
   }
}
