/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.Inforeportes;
import Entidades.Usuarios;
import Entidades.UsuariosInforeportes;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
public interface PersistenciaUsuariosInfoReportesInterface {

    public void crear(EntityManager em, UsuariosInforeportes usuarioIR);

    public void editar(EntityManager em, UsuariosInforeportes usuarioIR);

    public void borrar(EntityManager em, UsuariosInforeportes usuarioIR);

    public List<UsuariosInforeportes> listaUsuariosIR(EntityManager em, BigInteger secUsuario);

    public List<Inforeportes> lovIR(EntityManager em);

    public List<Usuarios> listaUsuarios(EntityManager em);
    
    public Long getTotalRegistros(EntityManager em,BigInteger secUsuario);

    public List<UsuariosInforeportes> getFind(EntityManager em, final int firstRow, final int max,BigInteger secUsuario);
    
    public List<UsuariosInforeportes> getBuscarUIR(EntityManager em, final int firstRow, final int max,BigInteger secUsuarioIR);

    public Long getTotalRegistrosBuscar(EntityManager em,BigInteger secUsuarioIR);
}
