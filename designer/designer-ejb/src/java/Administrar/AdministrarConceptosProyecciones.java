/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Conceptos;
import Entidades.ConceptosProyecciones;
import InterfaceAdministrar.AdministrarConceptosProyeccionesInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaConceptosInterface;
import InterfacePersistencia.PersistenciaConceptosProyeccionesInterface;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
@Stateful
public class AdministrarConceptosProyecciones implements AdministrarConceptosProyeccionesInterface {

    @EJB
    AdministrarSesionesInterface administrarSesiones;
    @EJB
    PersistenciaConceptosProyeccionesInterface persistenciaConceptosProyecciones;
    @EJB
    PersistenciaConceptosInterface persistenciaConceptos;
    private EntityManager em;

    //--------------------------------------------------------------------------
    //MÉTODOS
    //--------------------------------------------------------------------------
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    public void crearConceptosProyecciones(List<ConceptosProyecciones> lista) {
        for (int j = 0; j < lista.size(); j++) {
            persistenciaConceptosProyecciones.crear(em, lista.get(j));
        }
    }

    public void borrarConceptosProyecciones(List<ConceptosProyecciones> lista) {
        for (int j = 0; j < lista.size(); j++) {
            persistenciaConceptosProyecciones.borrar(em, lista.get(j));
        }
    }

    public void modificarConceptosProyecciones(List<ConceptosProyecciones> lista) {
        for (int j = 0; j < lista.size(); j++) {
            persistenciaConceptosProyecciones.editar(em, lista.get(j));
        }
    }

    public List<ConceptosProyecciones> consultarConceptosProyecciones() {
        List<ConceptosProyecciones> lista = persistenciaConceptosProyecciones.buscarConceptosProyecciones(em);
        return lista;
    }
    
    public List<Conceptos> consultarLOVConceptos(){
    List<Conceptos> lista = persistenciaConceptos.buscarConceptos(em);
    return lista;
    }
}
