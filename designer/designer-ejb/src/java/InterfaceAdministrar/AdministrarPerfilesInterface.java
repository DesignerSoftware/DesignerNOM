/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Perfiles;
import Entidades.PermisosObjetosDB;
import Entidades.PermisosPantallas;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author user
 */

public interface AdministrarPerfilesInterface {

    public void obtenerConexion(String idSesion);
    public void modificarPerfiles(List<Perfiles> listaPerfiles);
    public void borrarPerfiles(List<Perfiles> listaPerfiles);
    public void crearPerfiles(List<Perfiles> listaPerfiles);
    public List<Perfiles> consultarPerfiles();
    public List<PermisosPantallas> consultarPermisosPantallas(BigInteger secPerfil);
    public void crearPermisoPantalla(List<PermisosPantallas> permisop);
    public void editarPermisoPantalla(List<PermisosPantallas> permisop);
    public void borrarPermisoPantalla(List<PermisosPantallas> permisop);
    public List<PermisosObjetosDB> consultarPermisosObjetos(BigInteger secPerfil);
    public void crearPermisoObjeto(List<PermisosObjetosDB> permisosOb);
    public void editarPermisoObjeto(List<PermisosObjetosDB> permisosOb);
    public void borrarPermisoObjeto(List<PermisosObjetosDB> permisosOb);
    public void ejcutarPKGRecrearPerfil(String descripcion,String pwd);
    public void ejcutarPKGEliminarPerfil(String descripcion);
    public Perfiles consultarPerfilUsuario();
    public void clonarPantallas(String nomPerfil);
    public void clonarPermisosObjetos(String nomPerfil);

}
