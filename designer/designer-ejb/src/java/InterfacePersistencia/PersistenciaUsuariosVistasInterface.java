/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package InterfacePersistencia;

import Entidades.UsuariosVistas;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'usuariosvistas' 
 * de la base de datos.
 * @author betelgeuse
 */

public interface PersistenciaUsuariosVistasInterface {
    
    public List<UsuariosVistas> buscarUsuariosVistas(EntityManager em);
    public void crear(EntityManager em,UsuariosVistas usuariosVistas);
    public void editar(EntityManager em,UsuariosVistas usuariosVistas);
    public void borrar(EntityManager em,UsuariosVistas usuariosVistas);
    public Integer crearUsuarioVista(EntityManager em, BigInteger objeto);
    
}
