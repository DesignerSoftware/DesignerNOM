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

   private EntityManager em;

   @Override
   public void obtenerConexion(String idSesion) {
      em = administrarSesiones.obtenerConexionSesion(idSesion);
   }

   @Override
   public void crear(List<TempProrrateosProy> listaTempPP) {
      for (int i = 0; i < listaTempPP.size(); i++) {
         persistenciaTempProrrateosProy.crear(em, listaTempPP.get(i));
      }
   }

   @Override
   public void editar(TempProrrateosProy tempPP) {
      persistenciaTempProrrateosProy.editar(em, tempPP);
   }

   @Override
   public void borrar(TempProrrateosProy tempPP) {
      persistenciaTempProrrateosProy.borrar(em, tempPP);
   }

   @Override
   public void borrarRegistrosTempProrrateosProy(String usuarioBD) {
      persistenciaTempProrrateosProy.borrarRegistrosTempProrrateosProy(em, usuarioBD);
   }

   @Override
   public List<TempProrrateosProy> obtenerTempProrrateosProy(String usuarioBD) {
      return persistenciaTempProrrateosProy.obtenerTempProrrateosProy(em, usuarioBD);
   }

   @Override
   public List<String> obtenerDocumentosSoporteCargados() {
      return persistenciaTempProrrateosProy.obtenerDocumentosSoporteCargados(em);
   }

   @Override
   public ActualUsuario actualUsuario() {
      return persistenciaActualUsuario.actualUsuarioBD(em);
   }

   @Override
//   public void cargarTempProrrateosProy(String fechaInicial, BigInteger secEmpresa) {
   public void cargarTempProrrateosProy() {
//      SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
//      String fechaR = formatoFecha.format(fechaInicial);
      persistenciaTempProrrateosProy.cargarTempProrrateosProy(em);
   }

   @Override
   public int reversarTempProrrateosProy(ActualUsuario usuarioBD, String documentoSoporte) {
      return persistenciaTempProrrateosProy.reversarTempProrrateosProy(em, usuarioBD.getAlias(), documentoSoporte);
   }

   @Override
   public String consultarRuta() {
      try {
         Generales general = persistenciaGenerales.obtenerRutas(em);
         return general.getUbicareportes();
      } catch (Exception e) {
         log.warn("ERROR AdministrarArchivoPlanoProyecto.consultarRuta() e : " + e);
         return "C:\\DesignerRHN\\Reportes\\ArchivosPlanos\\";
      }
   }

}
