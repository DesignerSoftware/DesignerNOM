/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Conceptos;
import Entidades.ConceptosRedondeos;
import Entidades.TiposRedondeos;
import InterfaceAdministrar.AdministrarConceptosRedondeosInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaConceptosInterface;
import InterfacePersistencia.PersistenciaConceptosRedondeosInterface;
import InterfacePersistencia.PersistenciaTiposRedondeosInterface;
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
public class AdministrarConceptosRedondeos implements AdministrarConceptosRedondeosInterface {

   private static Logger log = Logger.getLogger(AdministrarConceptosRedondeos.class);

    @EJB
    PersistenciaConceptosRedondeosInterface persistenciaConceptosRedondeos;
    @EJB
    PersistenciaConceptosInterface persistenciaConceptos;
    @EJB
    PersistenciaTiposRedondeosInterface persistenciaTiposRedondeos;

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

//Conceptos Redondeos
    @Override
    public void borrarConceptosRedondeos(ConceptosRedondeos conceptosRedondeos) {
        persistenciaConceptosRedondeos.borrar(em,conceptosRedondeos);
    }

    @Override
    public void crearConceptosRedondeos(ConceptosRedondeos conceptosRedondeos) {
        persistenciaConceptosRedondeos.crear(em,conceptosRedondeos);
    }

    @Override
    public void modificarConceptosRedondeos(List<ConceptosRedondeos> listaConceptosRedondeosModificar) {
        for (int i = 0; i < listaConceptosRedondeosModificar.size(); i++) {
            log.warn("Modificando...");
            persistenciaConceptosRedondeos.editar(em,listaConceptosRedondeosModificar.get(i));
        }
    }

    public List<ConceptosRedondeos> consultarConceptosRedondeos() {
        try {
            List<ConceptosRedondeos> actual = persistenciaConceptosRedondeos.buscarConceptosRedondeos(em);
            return actual;
        } catch (Exception e) {
            log.warn("Error consultarVigenciasRetenciones: " + e.toString());
            return null;
        }
    }

    //LOV Conceptos
    @Override
    public List<Conceptos> lovConceptos() {
        try {
            List<Conceptos> actual = persistenciaConceptos.buscarConceptos(em);
            return actual;
        } catch (Exception e) {
            log.warn("Error lovConceptos: " + e.toString());
            return null;
        }
    }

    //LOV Tipos Redondeos
    @Override
    public List<TiposRedondeos> lovTiposRedondeos() {
        try {
            List<TiposRedondeos> actual = persistenciaTiposRedondeos.buscarTiposRedondeos(em);
            return actual;
        } catch (Exception e) {
            log.warn("Error lovTiposRedondeos: " + e.toString());
            return null;
        }
    }

}
