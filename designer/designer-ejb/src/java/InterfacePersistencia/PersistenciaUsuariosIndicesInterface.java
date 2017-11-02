/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.Indices;
import Entidades.Usuarios;
import Entidades.UsuariosIndices;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
public interface PersistenciaUsuariosIndicesInterface {

    public String crear(EntityManager em, UsuariosIndices usuarioI);

    public String editar(EntityManager em, UsuariosIndices usuarioI);

    public String borrar(EntityManager em, UsuariosIndices usuarioI);

    public List<Indices> lovIndices(EntityManager em);

    public List<Usuarios> listaUsuarios(EntityManager em);

    public List<UsuariosIndices> listaUsuariosIndices(EntityManager em,BigInteger secUsuario);
}
