/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.UsuariosEstructuras;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
public interface PersistenciaUsuariosEstructurasInterface {

    public void crear(EntityManager em, UsuariosEstructuras usuarioEstructura);

    public void editar(EntityManager em, UsuariosEstructuras usuarioEstructura);

    public void borrar(EntityManager em, UsuariosEstructuras usuarioEstructura);
    
    public List<UsuariosEstructuras> consultarUsuariosEstructuras(EntityManager em ,BigInteger secUsuario);
}
