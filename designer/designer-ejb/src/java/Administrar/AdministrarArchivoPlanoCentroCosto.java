/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.ActualUsuario;
import Entidades.Generales;
import Entidades.TempProrrateos;
import InterfaceAdministrar.AdministrarArchivoPlanoCentroCostoInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaActualUsuarioInterface;
import InterfacePersistencia.PersistenciaGeneralesInterface;
import InterfacePersistencia.PersistenciaTempProrrateosInterface;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;

@Stateful
@Local
public class AdministrarArchivoPlanoCentroCosto implements AdministrarArchivoPlanoCentroCostoInterface {

   @EJB
   PersistenciaTempProrrateosInterface persistenciaTempProrrateos;
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
   public void crear(List<TempProrrateos> listaTempPP) {
      for (int i = 0; i < listaTempPP.size(); i++) {
         persistenciaTempProrrateos.crear(em, listaTempPP.get(i));
      }
   }

   @Override
   public void editar(TempProrrateos tempPP) {
      persistenciaTempProrrateos.editar(em, tempPP);
   }

   @Override
   public void borrar(TempProrrateos tempPP) {
      persistenciaTempProrrateos.borrar(em, tempPP);
   }

   @Override
   public void borrarRegistrosTempProrrateos(String usuarioBD) {
      persistenciaTempProrrateos.borrarRegistrosTempProrrateos(em, usuarioBD);
   }

   @Override
   public List<TempProrrateos> obtenerTempProrrateos(String usuarioBD) {
      return persistenciaTempProrrateos.obtenerTempProrrateos(em, usuarioBD);
   }

   @Override
   public List<String> obtenerDocumentosSoporteCargados() {
      return persistenciaTempProrrateos.obtenerDocumentosSoporteCargados(em);
   }

   @Override
   public ActualUsuario actualUsuario() {
      return persistenciaActualUsuario.actualUsuarioBD(em);
   }

   @Override
//   public void cargarTempProrrateos(String fechaInicial, BigInteger secEmpresa) {
   public void cargarTempProrrateos() {
//      SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
//      String fechaR = formatoFecha.format(fechaInicial);
      persistenciaTempProrrateos.cargarTempProrrateos(em);
   }

   @Override
   public int reversarTempProrrateos(ActualUsuario usuarioBD, String documentoSoporte) {
      return persistenciaTempProrrateos.reversarTempProrrateos(em, usuarioBD.getAlias(), documentoSoporte);
   }

   @Override
   public String consultarRuta() {
      try {
         Generales general = persistenciaGenerales.obtenerRutas(em);
         return general.getUbicareportes();
      } catch (Exception e) {
         System.out.println("ERROR AdministrarArchivoPlanoCentroCosto.consultarRuta() e: " + e);
         return "C:\\DesignerRHN\\Reportes\\ArchivosPlanos\\";
      }
   }

}
