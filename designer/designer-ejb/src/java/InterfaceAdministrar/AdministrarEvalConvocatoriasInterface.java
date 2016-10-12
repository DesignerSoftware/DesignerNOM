/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Evalconvocatorias;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author user
 */
public interface AdministrarEvalConvocatoriasInterface {
 
    public void obtenerConexion(String idSesion);
   public void crear(List<Evalconvocatorias> listCrear);
   public void borrar(List<Evalconvocatorias> listBorrar);
   public void editar(List<Evalconvocatorias> listModificar);
   public List<Evalconvocatorias> buscarEvalConvocatorias(BigInteger secuenciaEmpleado);
}
