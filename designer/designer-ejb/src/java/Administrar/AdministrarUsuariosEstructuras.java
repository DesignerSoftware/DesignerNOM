/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Empresas;
import Entidades.Estructuras;
import Entidades.Usuarios;
import Entidades.UsuariosEstructuras;
import Entidades.UsuariosVistas;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaEmpresasInterface;
import InterfacePersistencia.PersistenciaEstructurasInterface;
import InterfacePersistencia.PersistenciaUsuariosEstructurasInterface;
import InterfacePersistencia.PersistenciaUsuariosInterface;
import InterfacePersistencia.PersistenciaUsuariosVistasInterface;
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
public class AdministrarUsuariosEstructuras implements AdministrarUsuariosEstructurasInterface {

    @EJB
    PersistenciaUsuariosInterface persistenciaUsuarios;
    @EJB
    PersistenciaEmpresasInterface persistenciaEmpresas;
    @EJB
    PersistenciaEstructurasInterface persistenciaEstructuras;
    @EJB
    AdministrarSesionesInterface administrarSesiones;
    @EJB
    PersistenciaUsuariosEstructurasInterface  persistenciaUsuariosEstructuras;
    @EJB
    PersistenciaUsuariosVistasInterface persistenciaUsuariosVistas;
    
    private EntityManager em;

    @Override
    public void obtenerConexion(String idSesion) {
          em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    @Override
    public List<UsuariosEstructuras> consultarUsuariosEstructuras(BigInteger secUsuario) {
       List<UsuariosEstructuras> lista = persistenciaUsuariosEstructuras.consultarUsuariosEstructuras(em,secUsuario);
       return lista;
    }

    @Override
    public void crearUsuarioEstructura(List<UsuariosEstructuras> listCrear) {
        for (int i = 0; i < listCrear.size(); i++) {
           persistenciaUsuariosEstructuras.crear(em, listCrear.get(i));
        }
    }

    @Override
    public void modificarUsuarioEstructura(List<UsuariosEstructuras> listModificar) {
        for (int i = 0; i < listModificar.size(); i++) {
           persistenciaUsuariosEstructuras.editar(em, listModificar.get(i));
        }
    }

    @Override
    public void borrarUsuarioEstructura(List<UsuariosEstructuras> listBorrar) {
       for (int i = 0; i < listBorrar.size(); i++) {
           persistenciaUsuariosEstructuras.borrar(em, listBorrar.get(i));
        }
    }

    @Override
    public List<Usuarios> lovUsuarios() {
       List<Usuarios> lovUsuarios = persistenciaUsuarios.buscarUsuarios(em);
       return lovUsuarios;
    }

    @Override
    public List<Estructuras> lovEstructuras() {
        List<Estructuras> listaE = persistenciaEstructuras.buscarEstructuras(em);
        return listaE;
    }

    @Override
    public List<Empresas> lovEmpresas() {
        List<Empresas> lovEmpresas = persistenciaEmpresas.consultarEmpresas(em);
        return lovEmpresas;
    }

    @Override
    public List<UsuariosVistas> listaUsuariosVistas() {
       List<UsuariosVistas> listVUsuarios = persistenciaUsuariosVistas.buscarUsuariosVistas(em);
       return listVUsuarios;
    }

    @Override
    public void crearUsuarioVista(List<UsuariosVistas> listCrear) {
       for (int i = 0; i < listCrear.size(); i++) {
           persistenciaUsuariosVistas.crear(em, listCrear.get(i));
        }
    }

    @Override
    public void modificarUsuarioVista(List<UsuariosVistas> listModificar) {
     for (int i = 0; i < listModificar.size(); i++) {
           persistenciaUsuariosVistas.editar(em, listModificar.get(i));
        }
    }

    @Override
    public void borrarUsuarioVista(List<UsuariosVistas> listBorrar) {
       for (int i = 0; i < listBorrar.size(); i++) {
           persistenciaUsuariosVistas.borrar(em, listBorrar.get(i));
        }
    }

}
