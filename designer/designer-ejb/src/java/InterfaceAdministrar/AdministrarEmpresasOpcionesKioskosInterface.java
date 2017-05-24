/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.EmpresasOpcionesKioskos;
import java.util.List;

/**
 *
 * @author user
 */
public interface AdministrarEmpresasOpcionesKioskosInterface {

    public void obtenerConexion(String idSesion);

    public void modificarEmpresasOpcionesKioskos(List<EmpresasOpcionesKioskos> lista);

    public void borrarEmpresasOpcionesKioskos(List<EmpresasOpcionesKioskos> lista);

    public void crearEmpresasOpcionesKioskos(List<EmpresasOpcionesKioskos> lista);

    public List<EmpresasOpcionesKioskos> consultarEmpresasOpcionesKioskos();
}
