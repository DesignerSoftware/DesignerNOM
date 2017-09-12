/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Declarantes;
import Entidades.RetencionesMinimas;
import Entidades.TarifaDeseo;
import InterfaceAdministrar.AdministrarDeclarantesInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaDeclarantesInterface;
import InterfacePersistencia.PersistenciaRetencionesMinimasInterface;
import InterfacePersistencia.PersistenciaTarifaDeseoInterface;
import java.math.BigInteger;
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
public class AdministrarDeclarantes implements AdministrarDeclarantesInterface {

   private static Logger log = Logger.getLogger(AdministrarDeclarantes.class);

   @EJB
   PersistenciaDeclarantesInterface persistenciaDeclarantes;
   @EJB
   PersistenciaRetencionesMinimasInterface persistenciaRetencionesMinimas;
   @EJB
   PersistenciaTarifaDeseoInterface persistenciaTarifaDeseo;
   /**
    * Enterprise JavaBean.<br>
    * Atributo que representa todo lo referente a la conexión del usuario que
    * está usando el aplicativo.
    */
   @EJB
   AdministrarSesionesInterface administrarSesiones;

   private EntityManagerFactory emf;
   private EntityManager em;
   private String idSesionBck;

   private EntityManager getEm() {
      try {
         if (this.emf != null) {
            if (this.em != null) {
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

   List<Declarantes> declarantesLista;
   List<TarifaDeseo> retencionesLista;
   List<RetencionesMinimas> retencionesMinimasLista;
   Declarantes declarantes;

   @Override
   public void obtenerConexion(String idSesion) {
      idSesionBck = idSesion;
      try {
         emf = administrarSesiones.obtenerConexionSesionEMF(idSesion);
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " obtenerConexion ERROR: " + e);
      }
   }

   @Override
   public List<Declarantes> declarantesPersona(BigInteger secPersona) {
      try {
         declarantesLista = persistenciaDeclarantes.buscarDeclarantesPersona(getEm(), secPersona);
      } catch (Exception e) {
         log.warn("Error en Administrar Declarantes (declarantesPersona)");
         declarantesLista = null;
      }
      return declarantesLista;
   }

   @Override
   public void modificarDeclarantes(List<Declarantes> listaDeclarantesModificados) {
      try {
         for (int i = 0; i < listaDeclarantesModificados.size(); i++) {
            if (listaDeclarantesModificados.get(i).getRetenciondeseada() == null) {
               listaDeclarantesModificados.get(i).setRetenciondeseada(null);
            }

            if (listaDeclarantesModificados.get(i).getRetencionminima().getSecuencia() == null) {
               listaDeclarantesModificados.get(i).setRetencionminima(null);
            }
            log.warn("Modificando...");

            persistenciaDeclarantes.editar(getEm(), listaDeclarantesModificados.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".modificarDeclarantes() ERROR: " + e);
      }
   }

   @Override
   public void borrarDeclarantes(Declarantes declarantes) {
      try {
         persistenciaDeclarantes.borrar(getEm(), declarantes);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".borrarDeclarantes() ERROR: " + e);
      }
   }

   @Override
   public void crearDeclarantes(Declarantes declarantes) {
      try {
         persistenciaDeclarantes.crear(getEm(), declarantes);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".crearDeclarantes() ERROR: " + e);
      }
   }

   public List<TarifaDeseo> retencionesMinimas() {
      try {
         return persistenciaTarifaDeseo.retenciones(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".retencionesMinimas() ERROR: " + e);
         return null;
      }
   }

   public List<RetencionesMinimas> retencionesMinimasLista() {
      try {
         return persistenciaRetencionesMinimas.retenciones(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".retencionesMinimasLista() ERROR: " + e);
         return null;
      }
   }

}
