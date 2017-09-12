/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.ActualUsuario;
import Entidades.Generales;
import Entidades.TempProrrateosProy;
import InterfaceAdministrar.AdministrarArchivoPlanoProyectoInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaActualUsuarioInterface;
import InterfacePersistencia.PersistenciaGeneralesInterface;
import InterfacePersistencia.PersistenciaTempProrrateosProyInterface;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

@Stateful
@Local
public class AdministrarArchivoPlanoProyecto implements AdministrarArchivoPlanoProyectoInterface {

   private static Logger log = Logger.getLogger(AdministrarArchivoPlanoProyecto.class);

   @EJB
   PersistenciaTempProrrateosProyInterface persistenciaTempProrrateosProy;
   @EJB
   PersistenciaGeneralesInterface persistenciaGenerales;
   @EJB
   PersistenciaActualUsuarioInterface persistenciaActualUsuario;
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
   public void crear(List<TempProrrateosProy> listaTempPP) {
      try {
         for (int i = 0; i < listaTempPP.size(); i++) {
            persistenciaTempProrrateosProy.crear(getEm(), listaTempPP.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".crear() ERROR: " + e);
      }
   }

   @Override
   public void editar(TempProrrateosProy tempPP) {
      try {
         persistenciaTempProrrateosProy.editar(getEm(), tempPP);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".editar() ERROR: " + e);
      }
   }

   @Override
   public void borrar(TempProrrateosProy tempPP) {
      try {
         persistenciaTempProrrateosProy.borrar(getEm(), tempPP);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".borrar() ERROR: " + e);
      }
   }

   @Override
   public void borrarRegistrosTempProrrateosProy(String usuarioBD) {
      try {
         persistenciaTempProrrateosProy.borrarRegistrosTempProrrateosProy(getEm(), usuarioBD);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".borrarRegistrosTempProrrateosProy() ERROR: " + e);
      }
   }

   @Override
   public List<TempProrrateosProy> obtenerTempProrrateosProy(String usuarioBD) {
      try {
         return persistenciaTempProrrateosProy.obtenerTempProrrateosProy(getEm(), usuarioBD);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".obtenerTempProrrateosProy() ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<String> obtenerDocumentosSoporteCargados() {
      try {
         return persistenciaTempProrrateosProy.obtenerDocumentosSoporteCargados(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".obtenerDocumentosSoporteCargados() ERROR: " + e);
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
//   public void cargarTempProrrateosProy(String fechaInicial, BigInteger secEmpresa) {
   public void cargarTempProrrateosProy() {
//      SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
//      String fechaR = formatoFecha.format(fechaInicial);
      try {
         persistenciaTempProrrateosProy.cargarTempProrrateosProy(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".cargarTempProrrateosProy() ERROR: " + e);
      }
   }

   @Override
   public int reversarTempProrrateosProy(ActualUsuario usuarioBD, String documentoSoporte) {
      try {
         return persistenciaTempProrrateosProy.reversarTempProrrateosProy(getEm(), usuarioBD.getAlias(), documentoSoporte);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".reversarTempProrrateosProy() ERROR: " + e);
         return 0;
      }
   }

   @Override
   public String consultarRuta() {
      try {
         Generales general = persistenciaGenerales.obtenerRutas(getEm());
         return general.getUbicareportes();
      } catch (Exception e) {
         log.warn("ERROR AdministrarArchivoPlanoProyecto.consultarRuta() e : " + e);
         return "C:\\DesignerRHN\\Reportes\\ArchivosPlanos\\";
      }
   }

}
