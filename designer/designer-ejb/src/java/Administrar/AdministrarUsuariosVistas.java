/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.ObjetosDB;
import Entidades.UsuariosVistas;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfaceAdministrar.AdministrarUsuariosVistasInterface;
import InterfacePersistencia.PersistenciaObjetosDBInterface;
import InterfacePersistencia.PersistenciaUsuariosVistasInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;

/**
 *
 * @author Administrador
 */
@Stateful
public class AdministrarUsuariosVistas implements AdministrarUsuariosVistasInterface {

    @EJB
    PersistenciaUsuariosVistasInterface persistenciaUsuariosVistas;
    @EJB
    PersistenciaObjetosDBInterface persistenciaObjetoDB;
    @EJB
    AdministrarSesionesInterface administrarSesiones;

    private EntityManager em;

    // Metodos
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    public List<UsuariosVistas> consultarUsuariosVistas() {
        List<UsuariosVistas> listaUsuariosVistas;
        listaUsuariosVistas = persistenciaUsuariosVistas.buscarUsuariosVistas(em);
        return listaUsuariosVistas;
    }

    public List<ObjetosDB> consultarObjetosDB() {
        List<ObjetosDB> listaObjetosDB;
        listaObjetosDB = persistenciaObjetoDB.consultarObjetoDB(em);
        return listaObjetosDB;
    }

    @Override
    public void modificarUsuariosVistas(List<UsuariosVistas> listaUsuariosVistas) {
        for (int i = 0; i < listaUsuariosVistas.size(); i++) {
            if (listaUsuariosVistas.get(i).getObjetodb().getSecuencia() == null) {
                System.out.println("Modificando...UsuariosVistas.");
                listaUsuariosVistas.get(i).setObjetodb(null);
                persistenciaUsuariosVistas.editar(em, listaUsuariosVistas.get(i));
            } else if (listaUsuariosVistas.get(i).getAlias().equals(null)) {
                listaUsuariosVistas.get(i).setAlias(null);
            } else {
                persistenciaUsuariosVistas.editar(em, listaUsuariosVistas.get(i));
            }
        }
    }

    @Override
    public void borrarUsuariosVistas(List<UsuariosVistas> listaUsuariosVistas) {
        for (int i = 0; i < listaUsuariosVistas.size(); i++) {
            System.out.println("Borrando...UsuariosVistas.");
            if (listaUsuariosVistas.get(i).getObjetodb().getSecuencia() == null) {
                listaUsuariosVistas.get(i).setObjetodb(null);
                persistenciaUsuariosVistas.borrar(em, listaUsuariosVistas.get(i));
            } else if (listaUsuariosVistas.get(i).getAlias().equals(null)) {
                listaUsuariosVistas.get(i).setAlias(null);
            } else {
                persistenciaUsuariosVistas.editar(em, listaUsuariosVistas.get(i));
            }
        }
    }

    @Override
    public void crearUsuariosVistas(List<UsuariosVistas> listaUsuariosVistas) {
        for (int i = 0; i < listaUsuariosVistas.size(); i++) {
            System.out.println("Creando... UsuariosVistas..");
            if (listaUsuariosVistas.get(i).getObjetodb().getSecuencia() == null) {
                listaUsuariosVistas.get(i).setObjetodb(null);
                persistenciaUsuariosVistas.crear(em, listaUsuariosVistas.get(i));
            } else if (listaUsuariosVistas.get(i).getAlias() == null) {
                listaUsuariosVistas.get(i).setAlias(null);
            } else {
                persistenciaUsuariosVistas.editar(em, listaUsuariosVistas.get(i));
            }
        }
    }

    @Override
    public Integer crearUsuarioVistaDB(BigInteger objeto) {
        Integer exe = null;
        try {
           exe = persistenciaUsuariosVistas.crearUsuarioVista(em, objeto);
           return exe;
        } catch (Exception e) {
            System.out.println("Error crearUsuarioVistaDB Admi : " + e.toString());
            return null;
        }
    }

}
