/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.ObjetosJsf;
import Entidades.Perfiles;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaObjetosJsfInterface;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import InterfaceAdministrar.AdministrarPermisosObjetosJsfInterface;
import InterfacePersistencia.PersistenciaPerfilesInterface;
import InterfacePersistencia.PersistenciaUsuariosInterface;

/**
 *
 * @author user
 */
@Stateful
public class AdministrarPermisosObjetosJsf implements AdministrarPermisosObjetosJsfInterface {

    @EJB
    AdministrarSesionesInterface administrarSesiones;
    @EJB
    PersistenciaObjetosJsfInterface persistenciaObjetosJsf;
    @EJB
    PersistenciaPerfilesInterface persistenciaPerfiles;
    private EntityManager em;

    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    @Override
    public List<ObjetosJsf> consultarEnable(String nomPantalla) {
        String perfil = consultarPerfilUsuario();
        List<ObjetosJsf> enable = persistenciaObjetosJsf.consultarEnableObjetoJsf(em, perfil, nomPantalla);
        return enable;
    }

    private String consultarPerfilUsuario() {
        Perfiles perfil = persistenciaPerfiles.consultarPerfilPorUsuario(em);
        String aux = perfil.getDescripcion();
        return aux;
    }
}
