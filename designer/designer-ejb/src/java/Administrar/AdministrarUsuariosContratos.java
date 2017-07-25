/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.UsuariosContratos;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfaceAdministrar.AdministrarUsuariosContratosInterface;
import InterfacePersistencia.PersistenciaUsuariosContratosInterface;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;

/**
 *
 * @author user
 */
@Stateful
public class AdministrarUsuariosContratos implements AdministrarUsuariosContratosInterface {

   private static Logger log = Logger.getLogger(AdministrarUsuariosContratos.class);

    @EJB
    AdministrarSesionesInterface administrarSesiones;
    @EJB
    PersistenciaUsuariosContratosInterface persistenciaUsuariosContratos;

    private EntityManager em;
    
    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    @Override
    public List<UsuariosContratos> consultarUsuariosC() {
         List<UsuariosContratos> lista = persistenciaUsuariosContratos.buscarUsuariosContratos(em);
        return lista;
    }

    @Override
    public void modificarUsuarioC(List<UsuariosContratos> listaUsuarios) {
        for (int i = 0; i < listaUsuarios.size(); i++) {
            persistenciaUsuariosContratos.editar(em, listaUsuarios.get(i));
        }
    }

    @Override
    public void borrarUsuarioC(List<UsuariosContratos> listaUsuarios) {
        for (int i = 0; i < listaUsuarios.size(); i++) {
            persistenciaUsuariosContratos.borrar(em, listaUsuarios.get(i));
        }
    }

    @Override
    public void crearUsuarioC(List<UsuariosContratos> listaUsuarios) {
       for (int i = 0; i < listaUsuarios.size(); i++) {
            persistenciaUsuariosContratos.crear(em, listaUsuarios.get(i));
        }
    }

}
