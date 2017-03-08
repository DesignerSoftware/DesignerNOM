/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.UsuariosFiltros;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
public interface PersistenciaUsuariosFiltrosInterface {

    public void crear(EntityManager em, UsuariosFiltros usuarioF);

    public void editar(EntityManager em, UsuariosFiltros usuarioF);

    public void borrar(EntityManager em, UsuariosFiltros usuarioF);

    public List<UsuariosFiltros> consultarUsuariosFiltros(EntityManager em, BigInteger secUsuarioEstructura);
    
    public BigDecimal contarUsuariosFiltros(EntityManager em, BigInteger secUsuarioEstructura);
    
    public void crearFiltroUsuario(EntityManager em, BigInteger secUsuarioVista);
    
}
