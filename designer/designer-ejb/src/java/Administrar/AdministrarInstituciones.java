/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Instituciones;
import InterfaceAdministrar.AdministrarInstitucionesInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaInstitucionesInterface;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;

@Stateful
public class AdministrarInstituciones implements AdministrarInstitucionesInterface{

    @EJB
    PersistenciaInstitucionesInterface persistenciaInstituciones;
    
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
    public List<Instituciones> Instituciones(){
        List<Instituciones> listaInstituciones;
        listaInstituciones = persistenciaInstituciones.instituciones(em);
        return listaInstituciones;
    }

    @Override
    public List<Instituciones>  lovInstituciones(){
        return persistenciaInstituciones.instituciones(em);
    }
}