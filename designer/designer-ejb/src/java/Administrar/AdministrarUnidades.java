/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Administrar;

import Entidades.TiposUnidades;
import Entidades.Unidades;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfaceAdministrar.AdministrarUnidadesInterface;
import InterfacePersistencia.PersistenciaTiposUnidadesInterface;
import InterfacePersistencia.PersistenciaUnidadesInterface;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
@Stateful
public class AdministrarUnidades implements AdministrarUnidadesInterface{

    @EJB
    PersistenciaUnidadesInterface persistenciaUnidades;
    @EJB
    PersistenciaTiposUnidadesInterface persistenciaTiposUnidades;
    @EJB
    AdministrarSesionesInterface administrarSesiones;

    private EntityManager em;
    
    // Metodos
    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }
    
    
    public List<Unidades> consultarUnidades() {
        List<Unidades> listaUnidades;
        listaUnidades = persistenciaUnidades.consultarUnidades(em);
        return listaUnidades;
    }

    @Override
    public List<TiposUnidades> consultarTiposUnidades() {
        List<TiposUnidades> listaTiposUnidades;
        listaTiposUnidades = persistenciaTiposUnidades.consultarTiposUnidades(em);
        return listaTiposUnidades;
    }
    
    @Override
    public void modificarUnidades(List<Unidades> listaUnidades) {
        Unidades c;
        for (int i = 0; i < listaUnidades.size(); i++) {
            System.out.println("Modificando...");
            if (listaUnidades.get(i).getCodigo().equals(null)) {
                listaUnidades.get(i).setCodigo(null);
                c = listaUnidades.get(i);
            } else if (listaUnidades.get(i).getTipounidad().getSecuencia() == null) {
                listaUnidades.get(i).setTipounidad(null);      
                c = listaUnidades.get(i);
            } else {
                c = listaUnidades.get(i);
            }
            persistenciaUnidades.editar(em,c);
        }
    }

    @Override
    public void borrarUnidades(List<Unidades> listaUnidades) {
        for (int i = 0; i < listaUnidades.size(); i++) {
            System.out.println("Borrando...");
            if (listaUnidades.get(i).getCodigo().equals(null)) {
                listaUnidades.get(i).setCodigo(null);
                persistenciaUnidades.borrar(em,listaUnidades.get(i));
            } else if (listaUnidades.get(i).getTipounidad().getSecuencia() == null) {
                listaUnidades.get(i).setTipounidad(null);                
            } else {
                persistenciaUnidades.borrar(em,listaUnidades.get(i));
            }
        }
    }

    @Override
    public void crearUnidades(List<Unidades> listaUnidades) {
        for (int i = 0; i < listaUnidades.size(); i++) {
            System.out.println("Creando...");
            if (listaUnidades.get(i).getCodigo().equals(null)) {

                listaUnidades.get(i).setCodigo(null);
                persistenciaUnidades.crear(em,listaUnidades.get(i));
            } else if (listaUnidades.get(i).getTipounidad().getSecuencia() == null) {
                listaUnidades.get(i).setTipounidad(null);                
            } else {
                persistenciaUnidades.crear(em,listaUnidades.get(i));
            }
        }
    }
    
}
