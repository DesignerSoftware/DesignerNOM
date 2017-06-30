/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.ActualUsuario;
import Entidades.TempProrrateos;
import java.util.List;

public interface AdministrarArchivoPlanoCentroCostoInterface {
   
   public void obtenerConexion(String idSesion);

   public void crear(List<TempProrrateos> listaTempPP);

   public void editar(TempProrrateos tempPP);

   public void borrar(TempProrrateos tempPP);

   public void borrarRegistrosTempProrrateos(String usuarioBD);

   public List<TempProrrateos> obtenerTempProrrateos(String usuarioBD);

   public List<String> obtenerDocumentosSoporteCargados();

//   public void cargarTempProrrateos(String fechaInicial, BigInteger secEmpresa);
   public void cargarTempProrrateos();

   public int reversarTempProrrateos(ActualUsuario usuarioBD, String documentoSoporte);

   public String consultarRuta();

   public ActualUsuario actualUsuario();

}
