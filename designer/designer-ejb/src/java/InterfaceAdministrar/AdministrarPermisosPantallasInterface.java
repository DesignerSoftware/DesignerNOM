/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.ObjetosBloques;
import Entidades.ObjetosDB;
import Entidades.PermisosPantallas;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author user
 */
public interface AdministrarPermisosPantallasInterface {

    public void obtenerConexion(String idSesion);

    public String modificarPermiso(PermisosPantallas permiso);

    public String crearPermiso(PermisosPantallas permiso);

    public String borrarPermiso(PermisosPantallas permiso);

    public List<ObjetosBloques> lovObjetosBloques();

    public List<PermisosPantallas> consultarPermisosPantallas(BigInteger secPerfil);

}
