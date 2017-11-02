/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.Escenarios;
import Entidades.Usuarios;
import Entidades.UsuariosEscenarios;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
public interface PersistenciaUsuariosEscenariosInterface {

    public String crear(EntityManager em, UsuariosEscenarios usuarioE);

    public String editar(EntityManager em, UsuariosEscenarios usuarioE);

    public String borrar(EntityManager em, UsuariosEscenarios usuarioE);

    public List<UsuariosEscenarios> listaUsuariosEscenarios(EntityManager em);

    public List<Escenarios> lovEscenarios(EntityManager em);

    public List<Usuarios> listaUsuarios(EntityManager em);

}
