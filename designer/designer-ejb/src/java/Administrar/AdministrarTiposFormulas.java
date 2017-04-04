/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Formulas;
import Entidades.TiposFormulas;
import InterfaceAdministrar.AdministrarTiposFormulasInterface;
import InterfacePersistencia.PersistenciaFormulasInterface;
import InterfacePersistencia.PersistenciaTiposFormulasInterface;
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
public class AdministrarTiposFormulas implements AdministrarTiposFormulasInterface{
    
    @EJB
    PersistenciaTiposFormulasInterface persistenciaTiposFormulas;
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
    public List<TiposFormulas> buscarTiposFormulas(BigInteger secuenciaOperando) {
        List<TiposFormulas> listaTiposFormulas;
        listaTiposFormulas = persistenciaTiposFormulas.tiposFormulas(em, secuenciaOperando);
        return listaTiposFormulas;
    }

    @Override
    public void borrarTiposFormulas(TiposFormulas tiposFormulas) {
        persistenciaTiposFormulas.borrar(em, tiposFormulas);
    }

    @Override
    public void crearTiposFormulas(TiposFormulas tiposFormulas) {
        persistenciaTiposFormulas.crear(em, tiposFormulas);
    }

    @Override
    public void modificarTiposFormulas(TiposFormulas tiposFormulas) {
        persistenciaTiposFormulas.editar(em, tiposFormulas);

    }
    
    public List<Formulas> lovFormulas(){
        List<Formulas> listaFormulas;
        listaFormulas = persistenciaFormulas.buscarFormulas(em);
        return listaFormulas;
    }
}
