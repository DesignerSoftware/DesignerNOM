/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Perfiles;
import Entidades.PermisosObjetosDB;
import Entidades.PermisosPantallas;
import InterfaceAdministrar.AdministrarPerfilesInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaPerfilesInterface;
import InterfacePersistencia.PersistenciaPermisosObjetosDBInterface;
import InterfacePersistencia.PersistenciaPermisosPantallasInterface;
import java.math.BigDecimal;
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
public class AdministrarPerfiles implements AdministrarPerfilesInterface {

    @EJB
    PersistenciaPerfilesInterface persistenciaPerfiles;
    @EJB
    PersistenciaPermisosPantallasInterface persistenciaPermisosPantallas;
    @EJB
    PersistenciaPermisosObjetosDBInterface persistenciaPermisosDB;
    @EJB
    AdministrarSesionesInterface administrarSesiones;

    private EntityManager em;

    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    @Override
    public void modificarPerfiles(List<Perfiles> listaPerfiles) {
        for (int i = 0; i < listaPerfiles.size(); i++) {
            persistenciaPerfiles.editar(em, listaPerfiles.get(i));
        }
    }

    @Override
    public void borrarPerfiles(List<Perfiles> listaPerfiles) {
        for (int i = 0; i < listaPerfiles.size(); i++) {
            persistenciaPerfiles.borrar(em, listaPerfiles.get(i));
        }
    }

    @Override
    public void crearPerfiles(List<Perfiles> listaPerfiles) {
        for (int i = 0; i < listaPerfiles.size(); i++) {
            persistenciaPerfiles.crear(em, listaPerfiles.get(i));
        }
    }

    @Override
    public List<Perfiles> consultarPerfiles() {
        List<Perfiles> listaPerfiles = persistenciaPerfiles.consultarPerfilesAdmon(em);
        return listaPerfiles;
    }

    @Override
    public List<PermisosPantallas> consultarPermisosPantallas(BigInteger secPerfil) {
        List<PermisosPantallas> lista = persistenciaPermisosPantallas.consultarPermisosPorPerfil(em, secPerfil);
        return lista;
    }

    @Override
    public void crearPermisoPantalla(List<PermisosPantallas> permisop) {
        for (int i = 0; i < permisop.size(); i++) {
            persistenciaPermisosPantallas.crear(em, permisop.get(i));
        }
    }

    @Override
    public void editarPermisoPantalla(List<PermisosPantallas> permisop) {
        for (int i = 0; i < permisop.size(); i++) {
            persistenciaPermisosPantallas.editar(em, permisop.get(i));
        }
    }

    @Override
    public void borrarPermisoPantalla(List<PermisosPantallas> permisop) {
        for (int i = 0; i < permisop.size(); i++) {
            persistenciaPermisosPantallas.borrar(em, permisop.get(i));
        }
    }

    @Override
    public List<PermisosObjetosDB> consultarPermisosObjetos(BigInteger secPerfil) {
        List<PermisosObjetosDB> lista = persistenciaPermisosDB.consultarPermisosPorPerfil(em, secPerfil);
        return lista;
    }

    @Override
    public void crearPermisoObjeto(List<PermisosObjetosDB> permisosOb) {
        for (int i = 0; i < permisosOb.size(); i++) {
            persistenciaPermisosDB.crear(em, permisosOb.get(i));
        }
    }

    @Override
    public void editarPermisoObjeto(List<PermisosObjetosDB> permisosOb) {
        for (int i = 0; i < permisosOb.size(); i++) {
            persistenciaPermisosDB.editar(em, permisosOb.get(i));
        }
    }

    @Override
    public void borrarPermisoObjeto(List<PermisosObjetosDB> permisosOb) {
        for (int i = 0; i < permisosOb.size(); i++) {
            persistenciaPermisosDB.borrar(em, permisosOb.get(i));
        }
    }

    @Override
    public void ejcutarPKGRecrearPerfil(String descripcion, String pwd) {
        try {
            persistenciaPerfiles.ejecutarPKGRecrearPerfil(em, descripcion, pwd);
        } catch (Exception e) {
            System.out.println("Error ejcutarPKGUbicarnuevointercon_total admi: " + e.getMessage());
        }
    }

    @Override
    public void ejcutarPKGEliminarPerfil(String descripcion) {
        try {
            persistenciaPerfiles.ejecutarPKGEliminarPerfil(em, descripcion);
        } catch (Exception e) {
            System.out.println("Error ejcutarPKGUbicarnuevointercon_total admi: " + e.getMessage());
        }
    }

    @Override
    public Perfiles consultarPerfilUsuario() {
        Perfiles perfil = persistenciaPerfiles.consultarPerfilPorUsuario(em);
        return perfil;
    }

    @Override
    public void clonarPantallas(String nomPerfil) {
        try {
            persistenciaPerfiles.clonarPantallas(em, nomPerfil);
        } catch (Exception e) {
            System.out.println("Error en administrar.clonarPantallas() : " + e.getMessage());
        }
    }

    @Override
    public void clonarPermisosObjetos(String nomPerfil) {
        try {
            persistenciaPerfiles.clonarPermisosObjetos(em, nomPerfil);
        } catch (Exception e) {
            System.out.println("Error en administrar.clonarPermisosObjetos() : " + e.getMessage());
        }
    }

}
