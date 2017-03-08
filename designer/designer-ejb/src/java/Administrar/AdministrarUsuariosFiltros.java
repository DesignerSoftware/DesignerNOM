/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.UsuariosEstructuras;
import Entidades.UsuariosFiltros;
import Entidades.UsuariosVistas;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfaceAdministrar.AdministrarUsuariosFiltrosInterface;
import InterfacePersistencia.PersistenciaUsuariosFiltrosInterface;
import java.math.BigDecimal;
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
public class AdministrarUsuariosFiltros implements AdministrarUsuariosFiltrosInterface {

    @EJB
    AdministrarSesionesInterface administrarSesiones;
    @EJB
    PersistenciaUsuariosFiltrosInterface persistenciaUsuariosFiltros;

    private EntityManager em;

    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    @Override
    public List<UsuariosFiltros> consultarUsuariosFiltros(BigInteger secUsuarioEstructura) {
       List<UsuariosFiltros> lista = persistenciaUsuariosFiltros.consultarUsuariosFiltros(em, secUsuarioEstructura);
       return lista;
    }

    @Override
    public void crearUsuarioFiltro(List<UsuariosFiltros> listCrear) {
        for (int i = 0; i < listCrear.size(); i++) {
            if(listCrear.get(i).getUsuarioestructura().getSecuencia() == null){
                listCrear.get(i).setUsuarioestructura(new UsuariosEstructuras());
            }
            if(listCrear.get(i).getUsuariovista().getSecuencia() == null){
                listCrear.get(i).setUsuariovista(new UsuariosVistas());
            }
            
           persistenciaUsuariosFiltros.crear(em, listCrear.get(i));
        }
    }

    @Override
    public void modificarUsuarioFiltro(List<UsuariosFiltros> listModificar) {
        for (int i = 0; i < listModificar.size(); i++) {
            if(listModificar.get(i).getUsuarioestructura().getSecuencia() == null){
                listModificar.get(i).setUsuarioestructura(new UsuariosEstructuras());
            }
            if(listModificar.get(i).getUsuariovista().getSecuencia() == null){
                listModificar.get(i).setUsuariovista(new UsuariosVistas());
            }
           persistenciaUsuariosFiltros.editar(em, listModificar.get(i));
        }
    }

    @Override
    public void borrarUsuarioFiltro(List<UsuariosFiltros> listBorrar) {
       for (int i = 0; i < listBorrar.size(); i++) {
           if(listBorrar.get(i).getUsuarioestructura().getSecuencia() == null){
                listBorrar.get(i).setUsuarioestructura(new UsuariosEstructuras());
            }
            if(listBorrar.get(i).getUsuariovista().getSecuencia() == null){
                listBorrar.get(i).setUsuariovista(new UsuariosVistas());
            }
           persistenciaUsuariosFiltros.borrar(em, listBorrar.get(i));
        }
    }

    @Override
    public BigDecimal contarUsuariosFiltros(BigInteger secUsuarioEstructura) {
       BigDecimal count = persistenciaUsuariosFiltros.contarUsuariosFiltros(em, secUsuarioEstructura);
       return count;
    }

    @Override
    public void crearFiltroUsuario(BigInteger secuenciaUsuarioVista) {
       persistenciaUsuariosFiltros.crearFiltroUsuario(em, secuenciaUsuarioVista);
    }

}
