/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.UsuariosFiltros;
import InterfacePersistencia.PersistenciaUsuariosFiltrosInterface;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
public interface AdministrarUsuariosFiltrosInterface {
    
   public void obtenerConexion(String idSesion);

    public List<UsuariosFiltros> consultarUsuariosFiltros(BigInteger secUsuarioEstructura);

    public void crearUsuarioFiltro(List<UsuariosFiltros> listCrear);

    public void modificarUsuarioFiltro(List<UsuariosFiltros> listModificar);

    public void borrarUsuarioFiltro(List<UsuariosFiltros> listBorrar);   
    
    public BigDecimal contarUsuariosFiltros(BigInteger secUsuarioEstructura);
    
    public void crearFiltroUsuario(BigInteger secuenciaUsuarioVista);
}
