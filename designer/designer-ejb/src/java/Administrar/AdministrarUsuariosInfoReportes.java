/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Inforeportes;
import Entidades.Usuarios;
import Entidades.UsuariosInforeportes;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfaceAdministrar.AdministrarUsuariosInfoReportesInterface;
import InterfacePersistencia.PersistenciaUsuariosInfoReportesInterface;
import java.math.BigInteger;
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
public class AdministrarUsuariosInfoReportes implements AdministrarUsuariosInfoReportesInterface {

   private static Logger log = Logger.getLogger(AdministrarUsuariosInfoReportes.class);

    @EJB
    AdministrarSesionesInterface administrarSesiones;
    @EJB
    PersistenciaUsuariosInfoReportesInterface persistenciaUsuariosIR;

    private EntityManager em;

    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    @Override
    public void crear(List<UsuariosInforeportes> lista) {
        for (int i = 0; i < lista.size(); i++) {
            persistenciaUsuariosIR.crear(em, lista.get(i));
        }
    }

    @Override
    public void editar(List<UsuariosInforeportes> lista) {
        for (int i = 0; i < lista.size(); i++) {
            persistenciaUsuariosIR.editar(em, lista.get(i));
        }
    }

    @Override
    public void borrar(List<UsuariosInforeportes> lista) {
        for (int i = 0; i < lista.size(); i++) {
            persistenciaUsuariosIR.borrar(em, lista.get(i));
        }
    }

    @Override
    public List<UsuariosInforeportes> listaUsuariosIR(BigInteger secUsuario) {
        List<UsuariosInforeportes> lista = persistenciaUsuariosIR.listaUsuariosIR(em, secUsuario);
        return lista;
    }

    @Override
    public List<Inforeportes> lovIR() {
        List<Inforeportes> lovIR = persistenciaUsuariosIR.lovIR(em);
        return lovIR;
    }

    @Override
    public List<Usuarios> listaUsuarios() {
        List<Usuarios> listaUsuarios = persistenciaUsuariosIR.listaUsuarios(em);
        return listaUsuarios;
    }

    @Override
    public Long getTotalRegistros(BigInteger secUsuario) {
        return persistenciaUsuariosIR.getTotalRegistros(em, secUsuario);
    }

    @Override
    public List<UsuariosInforeportes> getFind(int firstRow, int max, BigInteger secUsuario) {
        return persistenciaUsuariosIR.getFind(em, firstRow, max, secUsuario);
    }

    @Override
    public List<UsuariosInforeportes> getBuscarIR(int firstRow, int max, BigInteger secUsuarioIR) {
       return persistenciaUsuariosIR.getBuscarUIR(em, firstRow, max, secUsuarioIR);
    }

    @Override
    public Long getTotalRegistrosBuscar(BigInteger secUsuario) {
       return persistenciaUsuariosIR.getTotalRegistrosBuscar(em, secUsuario);
    }

}
