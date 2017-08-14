/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Diagnosticoscapitulos;
import Entidades.Diagnosticoscategorias;
import Entidades.Diagnosticossecciones;
import InterfaceAdministrar.AdministrarDiagnosticosCategoriasInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaDiagnosticosCategoriasInterface;
import java.math.BigDecimal;
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
public class AdministrarDiagnosticosCategorias implements AdministrarDiagnosticosCategoriasInterface {

   private static Logger log = Logger.getLogger(AdministrarDiagnosticosCategorias.class);

   @EJB
   AdministrarSesionesInterface administrarSesiones;
   @EJB
   PersistenciaDiagnosticosCategoriasInterface persistenciaDiagnosticos;
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
         try {
            emf = administrarSesiones.obtenerConexionSesionEMF(idSesion);
         } catch (Exception e) {
            log.fatal(this.getClass().getSimpleName() + " obtenerConexion ERROR: " + e);
         }
      } catch (Exception e) {
         log.warn("error AdministrarDiagnosticosCategorias.obtenerConexion : " + e.toString());
      }
   }

   @Override
   public void crearDiagnosticoCategoria(List<Diagnosticoscategorias> categorias) {
      try {
         for (int i = 0; i < categorias.size(); i++) {
            persistenciaDiagnosticos.crear(getEm(), categorias.get(i));
         }
      } catch (Exception e) {
         log.warn("Error AdministrarDiagnosticosCategorias.crearDiagnosticoCategoria :" + e.toString());
      }
   }

   @Override
   public void editarDiagnosticoCategoria(List<Diagnosticoscategorias> categorias) {
      try {
         for (int i = 0; i < categorias.size(); i++) {
            persistenciaDiagnosticos.editar(getEm(), categorias.get(i));
         }
      } catch (Exception e) {
         log.warn("Error AdministrarDiagnosticosCategorias.editarDiagnosticoCategoria :" + e.toString());
      }
   }

   @Override
   public void borrarDiagnosticoCategoria(List<Diagnosticoscategorias> categorias) {
      try {
         for (int i = 0; i < categorias.size(); i++) {
            persistenciaDiagnosticos.borrar(getEm(), categorias.get(i));
         }
      } catch (Exception e) {
         log.warn("Error AdministrarDiagnosticosCategorias.borrarDiagnosticoCategoria :" + e.toString());
      }
   }

   @Override
   public List<Diagnosticoscategorias> consultarDiagnosticoCategoria(BigDecimal secSeccion) {
      try {
         return persistenciaDiagnosticos.buscarCategorias(getEm(), secSeccion);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<Diagnosticoscapitulos> consultarDiagnosticoCapitulo() {
      try {
         return persistenciaDiagnosticos.buscarCapitulo(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public void crearDiagnosticoCapitulo(List<Diagnosticoscapitulos> capitulo) {
      try {
         for (int i = 0; i < capitulo.size(); i++) {
            persistenciaDiagnosticos.crearCapitulo(getEm(), capitulo.get(i));
         }
      } catch (Exception e) {
         log.warn("Error AdministrarDiagnosticosCategorias.crearDiagnosticoCapitulo :" + e.toString());
      }
   }

   @Override
   public void editarDiagnosticoCapitulo(List<Diagnosticoscapitulos> capitulo) {
      try {
         for (int i = 0; i < capitulo.size(); i++) {
            persistenciaDiagnosticos.editarCapitulo(getEm(), capitulo.get(i));
         }
      } catch (Exception e) {
         log.warn("Error AdministrarDiagnosticosCategorias.editarDiagnosticoCapitulo :" + e.toString());
      }
   }

   @Override
   public void borrarDiagnosticoCapitulo(List<Diagnosticoscapitulos> capitulo) {
      try {
         for (int i = 0; i < capitulo.size(); i++) {
            persistenciaDiagnosticos.borrarCapitulo(getEm(), capitulo.get(i));
         }
      } catch (Exception e) {
         log.warn("Error AdministrarDiagnosticosCategorias.borrarDiagnosticoCapitulo :" + e.toString());
      }
   }

   @Override
   public List<Diagnosticossecciones> consultarDiagnosticoSeccion(BigDecimal secCapitulo) {
      try {
         return persistenciaDiagnosticos.buscarSeccion(getEm(), secCapitulo);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public void crearDiagnosticoSeccion(List<Diagnosticossecciones> seccion) {
      try {
         for (int i = 0; i < seccion.size(); i++) {
            persistenciaDiagnosticos.crearSeccion(getEm(), seccion.get(i));
         }
      } catch (Exception e) {
         log.warn("Error AdministrarDiagnosticosCategorias.crearDiagnosticoSección :" + e.toString());
      }
   }

   @Override
   public void editarDiagnosticoSeccion(List<Diagnosticossecciones> seccion) {
      try {
         for (int i = 0; i < seccion.size(); i++) {
            persistenciaDiagnosticos.editarSeccion(getEm(), seccion.get(i));
         }
      } catch (Exception e) {
         log.warn("Error AdministrarDiagnosticosCategorias.editarDiagnosticoSección :" + e.toString());
      }
   }

   @Override
   public void borrarDiagnosticoSeccion(List<Diagnosticossecciones> seccion) {
      try {
         for (int i = 0; i < seccion.size(); i++) {
            persistenciaDiagnosticos.borrarSeccion(getEm(), seccion.get(i));
         }
      } catch (Exception e) {
         log.warn("Error AdministrarDiagnosticosCategorias.borrarDiagnosticoSección :" + e.toString());
      }
   }

}
