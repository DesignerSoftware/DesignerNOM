/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Conceptos;
import Entidades.ConceptosRetroactivos;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaConceptosInterface;
import InterfacePersistencia.PersistenciaConceptosRetroactivosInterface;
import InterfaceAdministrar.AdministrarConceptosRetroactivosInterface;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;

/**
 *
 * @author user
 */
@Stateful
public class AdministrarConceptosRetroactivos implements AdministrarConceptosRetroactivosInterface {

   private static Logger log = Logger.getLogger(AdministrarConceptosRetroactivos.class);

    @EJB
    AdministrarSesionesInterface administrarSesiones;
    @EJB
    PersistenciaConceptosRetroactivosInterface persistenciaConceptosRetroactivos;
    @EJB
    PersistenciaConceptosInterface persistenciaConceptos;
    private EntityManager em;

    //--------------------------------------------------------------------------
    //MÉTODOS
    //--------------------------------------------------------------------------
    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    public void crearConceptosRetroactivos(List<ConceptosRetroactivos> lista) {
        for (int j = 0; j < lista.size(); j++) {
            persistenciaConceptosRetroactivos.crear(em, lista.get(j));
        }
    }

    public void borrarConceptosRetroactivos(List<ConceptosRetroactivos> lista) {
        for (int j = 0; j < lista.size(); j++) {
            persistenciaConceptosRetroactivos.borrar(em, lista.get(j));
        }
    }

    public void modificarConceptosRetroactivos(List<ConceptosRetroactivos> lista) {
        for (int j = 0; j < lista.size(); j++) {
            persistenciaConceptosRetroactivos.editar(em, lista.get(j));
        }
    }

    @Override
    public List<ConceptosRetroactivos> consultarConceptosRetroactivos() {
        List<ConceptosRetroactivos> lista = persistenciaConceptosRetroactivos.buscarConceptosRetroactivos(em);
        return lista;
    }

    public List<Conceptos> consultarLOVConceptos() {
        List<Conceptos> lista = persistenciaConceptos.buscarConceptos(em);
        return lista;
    }
    public List<Conceptos> consultarLOVConceptosRetro() {
        List<Conceptos> lista = persistenciaConceptos.buscarConceptos(em);
        return lista;
    }
}
