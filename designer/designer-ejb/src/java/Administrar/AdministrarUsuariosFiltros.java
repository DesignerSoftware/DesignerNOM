/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.UsuariosFiltros;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfaceAdministrar.AdministrarUsuariosFiltrosInterface;
import InterfacePersistencia.PersistenciaUsuariosFiltrosInterface;
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
public class AdministrarUsuariosFiltros implements AdministrarUsuariosFiltrosInterface {

    @EJB
    AdministrarSesionesInterface administrarSesiones;
    @EJB
    PersistenciaUsuariosFiltrosInterface persistenciaUsuariosFiltros;

    private EntityManager em;

    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    @Override
    public List<UsuariosFiltros> consultarUsuariosFiltros(BigInteger secUsuarioEstructura) {
        System.out.println("Administrar.AdministrarUsuariosFiltros.consultarUsuariosFiltros()" + secUsuarioEstructura);
       List<UsuariosFiltros> lista = persistenciaUsuariosFiltros.consultarUsuariosFiltros(em, secUsuarioEstructura);
       return lista;
    }

    @Override
    public void crearUsuarioFiltro(List<UsuariosFiltros> listCrear) {
        for (int i = 0; i < listCrear.size(); i++) {
           persistenciaUsuariosFiltros.crear(em, listCrear.get(i));
        }
    }

    @Override
    public void modificarUsuarioFiltro(List<UsuariosFiltros> listModificar) {
        for (int i = 0; i < listModificar.size(); i++) {
           persistenciaUsuariosFiltros.editar(em, listModificar.get(i));
        }
    }

    @Override
    public void borrarUsuarioFiltro(List<UsuariosFiltros> listBorrar) {
       for (int i = 0; i < listBorrar.size(); i++) {
           persistenciaUsuariosFiltros.borrar(em, listBorrar.get(i));
        }
    }

}
