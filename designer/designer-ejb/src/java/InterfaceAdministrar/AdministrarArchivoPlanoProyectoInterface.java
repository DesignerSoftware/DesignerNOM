/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.ActualUsuario;
import Entidades.TempProrrateosProy;
import java.util.List;

public interface AdministrarArchivoPlanoProyectoInterface {

   public void obtenerConexion(String idSesion);

   public void crear(List<TempProrrateosProy> listaTempPP);

   public void editar(TempProrrateosProy tempPP);

   public void borrar(TempProrrateosProy tempPP);

   public void borrarRegistrosTempProrrateosProy(String usuarioBD);

   public List<TempProrrateosProy> obtenerTempProrrateosProy(String usuarioBD);

   public List<String> obtenerDocumentosSoporteCargados();

//   public void cargarTempProrrateosProy(String fechaInicial, BigInteger secEmpresa);
   public void cargarTempProrrateosProy();

   public int reversarTempProrrateosProy(ActualUsuario usuarioBD, String documentoSoporte);

   public String consultarRuta();

   public ActualUsuario actualUsuario();

}
