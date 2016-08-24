/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.TiposEducaciones;
import InterfaceAdministrar.AdministrarTiposEducacionesInterface;
import InterfacePersistencia.PersistenciaTiposEducacionesInterface;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import InterfaceAdministrar.AdministrarSesionesInterface;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
@Stateful
public class AdministrarTiposEducaciones implements AdministrarTiposEducacionesInterface{
    
   @EJB
   PersistenciaTiposEducacionesInterface persistenciaTiposEducaciones;
   /**
     * Enterprise JavaBean.<br>
     * Atributo que representa todo lo referente a la conexión del usuario que
     * está usando el aplicativo.
     */
    @EJB
    AdministrarSesionesInterface administrarSesiones;
    
    private EntityManager em;
    
    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }
     
    @Override
    public List<TiposEducaciones> TiposEducaciones(){
        List<TiposEducaciones> listaTiposEducaciones;
        listaTiposEducaciones = persistenciaTiposEducaciones.tiposEducaciones(em);
        return listaTiposEducaciones;
    }
    
    @Override
    public List<TiposEducaciones>  lovTiposEducaciones(){
        return persistenciaTiposEducaciones.tiposEducaciones(em);
    }
  }
