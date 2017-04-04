/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.TiposConstantes;
import InterfaceAdministrar.AdministrarTiposConstantesInterface;
import InterfacePersistencia.PersistenciaFormulasInterface;
import InterfacePersistencia.PersistenciaTiposConstantesInterface;
import java.math.BigInteger;
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
public class AdministrarTiposConstantes implements AdministrarTiposConstantesInterface{
    
    @EJB
    PersistenciaTiposConstantesInterface persistenciaTiposConstantes;
    @EJB
    PersistenciaFormulasInterface persistenciaFormulas;
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
    public List<TiposConstantes> buscarTiposConstantes(BigInteger secuenciaOperando) {
        List<TiposConstantes> listaTiposConstantes;
        listaTiposConstantes = persistenciaTiposConstantes.tiposConstantes(em, secuenciaOperando);
        return listaTiposConstantes;
    }

    @Override
    public void borrarTiposConstantes(TiposConstantes tiposConstantes) {
        persistenciaTiposConstantes.borrar(em, tiposConstantes);
    }

    @Override
    public void crearTiposConstantes(TiposConstantes tiposConstantes) {
        persistenciaTiposConstantes.crear(em, tiposConstantes);
    }

    @Override
    public void modificarTiposConstantes(TiposConstantes tiposConstantes) {
        persistenciaTiposConstantes.editar(em, tiposConstantes);

    }
    
}
