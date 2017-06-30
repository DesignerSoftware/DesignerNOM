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

/**
 *
 * @author user
 */
@Stateful
public class AdministrarTempSoAusentismos implements AdministrarTempSoAusentismosInterface {

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

   private EntityManager em;

   @Override
   public void obtenerConexion(String idSesion) {
      em = administrarSesiones.obtenerConexionSesion(idSesion);
   }

   @Override
   public void crearTempSoAusentismos(List<TempSoAusentismos> listaTempSoAusentismos) {
      for (int i = 0; i < listaTempSoAusentismos.size(); i++) {
         persistenciaTempSoAusentismos.crear(em, listaTempSoAusentismos.get(i));
      }
   }

   @Override
   public void modificarTempSoAusentismos(TempSoAusentismos tempNovedades) {
      persistenciaTempSoAusentismos.editar(em, tempNovedades);
   }

   @Override
   public void borrarTempSoAusentismos(TempSoAusentismos tempNovedades) {
      persistenciaTempSoAusentismos.borrar(em, tempNovedades);
   }

   @Override
   public void borrarRegistrosTempSoAusentismos(String usuarioBD) {
      persistenciaTempSoAusentismos.borrarRegistrosTempNovedades(em, usuarioBD);
   }

   @Override
   public List<TempSoAusentismos> consultarTempSoAusentismos(String usuarioBD) {
      return persistenciaTempSoAusentismos.obtenerTempAusentismos(em, usuarioBD);
   }

   @Override
   public ActualUsuario actualUsuario() {
      return persistenciaActualUsuario.actualUsuarioBD(em);
   }

   @Override
   public List<String> consultarDocumentosSoporteCargadosUsuario(String usuarioBD) {
      return persistenciaTempSoAusentismos.obtenerDocumentosSoporteCargados(em, usuarioBD);
   }
//
//   @Override
//   public void cargarTempSoAusentismos(Date fechaReporte, String nombreCorto, String usarFormula) {
//      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//   }

   @Override
   public int reversarNovedades(ActualUsuario usuarioBD, String documentoSoporte) {
      return persistenciaTempSoAusentismos.reversarTempAusentismos(em, usuarioBD.getAlias(), documentoSoporte);
   }

   @Override
   public String consultarRuta() {
      try {
         Generales general = persistenciaGenerales.obtenerRutas(em);
         return general.getUbicareportes();
      } catch (Exception e) {
         System.out.println("ERROR Administrar.AdministrarCargueArchivos.consultarRuta() e : " + e);
         return "C:\\DesignerRHN\\Reportes\\ArchivosPlanos\\";
      }
   }

   @Override
   public ResultadoBorrarTodoNovedades borrarTodo(ActualUsuario usuarioBD, List<String> documentosSoporte) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

   @Override
   public BigInteger consultarParametrosEmpresa(String usuarioBD) {
      return persistenciaParametrosEstructuras.buscarEmpresaParametros(em, usuarioBD);
   }

}
