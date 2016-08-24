/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.PryRoles;
import InterfaceAdministrar.AdministrarPryRolesInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaPryRolesInterface;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
@Stateful

public class AdministrarPryRoles implements AdministrarPryRolesInterface {
    @EJB
    PersistenciaPryRolesInterface persistenciaPryRoles;
        /**
     * Enterprise JavaBean.<br>
     * Atributo que representa todo lo referente a la conexión del usuario que
     * está usando el aplicativo.
     */
    @EJB
    AdministrarSesionesInterface administrarSesiones;

    private EntityManager em;

    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }
    
    @Override
    public List<PryRoles> PryRoles(){
        List<PryRoles> listaPryRoles;
        listaPryRoles = persistenciaPryRoles.pryroles(em);
        return listaPryRoles;
    }

    @Override
    public List<PryRoles>  lovPryRoles(){
        return persistenciaPryRoles.pryroles(em);
    }


}

