/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.UsuariosTiposSueldos;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfaceAdministrar.AdministrarUsuariosTiposSueldosInterface;
import InterfacePersistencia.PersistenciaUsuariosTiposSueldosInterface;
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
public class AdministrarUsuariosTiposSueldos implements AdministrarUsuariosTiposSueldosInterface {

   private static Logger log = Logger.getLogger(AdministrarUsuariosTiposSueldos.class);

    @EJB
    AdministrarSesionesInterface administrarSesiones;
    @EJB
    PersistenciaUsuariosTiposSueldosInterface persistenciaUsuariosTiposSueldos;

    private EntityManager em;

    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    @Override
    public List<UsuariosTiposSueldos> consultarUsuariosTS() {
        List<UsuariosTiposSueldos> lista = persistenciaUsuariosTiposSueldos.buscarUsuariosTiposSueldos(em);
        return lista;
    }

    @Override
    public void modificarUsuariosTS(List<UsuariosTiposSueldos> listaUsuarios) {
        for (int i = 0; i < listaUsuarios.size(); i++) {
            persistenciaUsuariosTiposSueldos.editar(em, listaUsuarios.get(i));
        }
    }

    @Override
    public void borrarUsuariosTS(List<UsuariosTiposSueldos> listaUsuarios) {
        for (int i = 0; i < listaUsuarios.size(); i++) {
            persistenciaUsuariosTiposSueldos.borrar(em, listaUsuarios.get(i));
        }
    }

    @Override
    public void crearUsuariosTS(List<UsuariosTiposSueldos> listaUsuarios) {
        for (int i = 0; i < listaUsuarios.size(); i++) {
            persistenciaUsuariosTiposSueldos.crear(em, listaUsuarios.get(i));
        }
    }

}
