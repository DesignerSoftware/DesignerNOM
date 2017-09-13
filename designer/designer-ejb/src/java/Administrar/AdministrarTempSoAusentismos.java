/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import ClasesAyuda.ResultadoBorrarTodoNovedades;
import Entidades.ActualUsuario;
import Entidades.Generales;
import Entidades.TempSoAusentismos;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfaceAdministrar.AdministrarTempSoAusentismosInterface;
import InterfacePersistencia.PersistenciaActualUsuarioInterface;
import InterfacePersistencia.PersistenciaGeneralesInterface;
import InterfacePersistencia.PersistenciaParametrosEstructurasInterface;
import InterfacePersistencia.PersistenciaTempSoAusentismosInterface;
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
public class AdministrarTempSoAusentismos implements AdministrarTempSoAusentismosInterface {

   private static Logger log = Logger.getLogger(AdministrarTempSoAusentismos.class);

   @EJB
   PersistenciaActualUsuarioInterface persistenciaActualUsuario;
   @EJB
   PersistenciaTempSoAusentismosInterface persistenciaTempSoAusentismos;
   @EJB
   AdministrarSesionesInterface administrarSesiones;
   @EJB
   PersistenciaGeneralesInterface persistenciaGenerales;
   @EJB
   PersistenciaParametrosEstructurasInterface persistenciaParametrosEstructuras;

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
   public void crearTempSoAusentismos(List<TempSoAusentismos> listaTempSoAusentismos) {
      try {
         for (int i = 0; i < listaTempSoAusentismos.size(); i++) {
            persistenciaTempSoAusentismos.crear(getEm(), listaTempSoAusentismos.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".crearTempSoAusentismos() ERROR: " + e);
      }
   }

   @Override
   public void modificarTempSoAusentismos(TempSoAusentismos tempNovedades) {
      try {
         persistenciaTempSoAusentismos.editar(getEm(), tempNovedades);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".modificarTempSoAusentismos() ERROR: " + e);
      }
   }

   @Override
   public void borrarTempSoAusentismos(TempSoAusentismos tempNovedades) {
      try {
         persistenciaTempSoAusentismos.borrar(getEm(), tempNovedades);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".borrarTempSoAusentismos() ERROR: " + e);
      }
   }

   @Override
   public void borrarRegistrosTempSoAusentismos(String usuarioBD) {
      persistenciaTempSoAusentismos.borrarRegistrosTempNovedades(getEm(), usuarioBD);
      try {
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".borrarRegistrosTempSoAusentismos() ERROR: " + e);
      }
   }

   @Override
   public List<TempSoAusentismos> consultarTempSoAusentismos(String usuarioBD) {
      try {
         return persistenciaTempSoAusentismos.obtenerTempAusentismos(getEm(), usuarioBD);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarTempSoAusentismos() ERROR: " + e);
         return null;
      }
   }

   @Override
   public ActualUsuario actualUsuario() {
      try {
         return persistenciaActualUsuario.actualUsuarioBD(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".actualUsuario() ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<String> consultarDocumentosSoporteCargadosUsuario(String usuarioBD) {
      try {
         return persistenciaTempSoAusentismos.obtenerDocumentosSoporteCargados(getEm(), usuarioBD);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarDocumentosSoporteCargadosUsuario() ERROR: " + e);
         return null;
      }
   }
//
//   @Override
//   public void cargarTempSoAusentismos(Date fechaReporte, String nombreCorto, String usarFormula) {
//      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//   }

   @Override
   public int reversarNovedades(ActualUsuario usuarioBD, String documentoSoporte) {
      try {
         return persistenciaTempSoAusentismos.reversarTempAusentismos(getEm(), usuarioBD.getAlias(), documentoSoporte);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".reversarNovedades() ERROR: " + e);
         return 0;
      }
   }

   @Override
   public String consultarRuta() {
      try {
         Generales general = persistenciaGenerales.obtenerRutas(getEm());
         return general.getUbicareportes();
      } catch (Exception e) {
         log.warn("ERROR Administrar.AdministrarCargueArchivos.consultarRuta() e : " + e);
         return "C:\\DesignerRHN\\Reportes\\ArchivosPlanos\\";
      }
   }

   @Override
   public ResultadoBorrarTodoNovedades borrarTodo(ActualUsuario usuarioBD, List<String> documentosSoporte) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

   @Override
   public BigInteger consultarParametrosEmpresa(String usuarioBD) {
      try {
         return persistenciaParametrosEstructuras.buscarEmpresaParametros(getEm(), usuarioBD);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarParametrosEmpresa() ERROR: " + e);
         return null;
      }
   }

}
