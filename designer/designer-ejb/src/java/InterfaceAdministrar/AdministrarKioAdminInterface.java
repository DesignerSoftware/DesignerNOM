/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.ConexionesKioskos;
import Entidades.Empleados;
import Entidades.VigenciasDeportes;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author user
 */
public interface AdministrarKioAdminInterface {
 public void obtenerConexion(String idSesion);
  public List<Empleados> listEmpleadosCK();
  public ConexionesKioskos listCK(BigInteger secEmpleado);
  public void editarCK(List<ConexionesKioskos> ck);
  public void resetUsuario(BigInteger secEmpleado);
  public void unlockUsuario(BigInteger secEmpleado);
}
