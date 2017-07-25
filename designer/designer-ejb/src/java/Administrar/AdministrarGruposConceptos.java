/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Conceptos;
import Entidades.GruposConceptos;
import Entidades.VigenciasGruposConceptos;
import InterfaceAdministrar.AdministrarGruposConceptosInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaConceptosInterface;
import InterfacePersistencia.PersistenciaGruposConceptosInterface;
import InterfacePersistencia.PersistenciaVigenciasGruposConceptosInterface;
import java.math.BigInteger;
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
public class AdministrarGruposConceptos implements AdministrarGruposConceptosInterface {

   private static Logger log = Logger.getLogger(AdministrarGruposConceptos.class);

    @EJB
    PersistenciaGruposConceptosInterface persistenciaGruposConceptos;
    @EJB
    PersistenciaVigenciasGruposConceptosInterface persistenciaVigenciasGruposConceptos;
    @EJB
    PersistenciaConceptosInterface persistenciaConceptos;
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
    public List<GruposConceptos> buscarGruposConceptos() {
        try {
            return persistenciaGruposConceptos.buscarGruposConceptos(em);
        } catch (Exception e) {
            log.error("Error AdministrarVigenciasFormales.vigenciasFormalesPersona " + e);
            return null;
        }
    }

    @Override
    public void modificarGruposConceptos(List<GruposConceptos> listaGruposConceptosModificar) {
        for (int i = 0; i < listaGruposConceptosModificar.size(); i++) {
            log.warn("Modificando...");
            if (listaGruposConceptosModificar.get(i).getFundamental() == null) {
                listaGruposConceptosModificar.get(i).setFundamental(null);
            }
            persistenciaGruposConceptos.editar(em, listaGruposConceptosModificar.get(i));
        }
    }

    @Override
    public void borrarGruposConceptos(GruposConceptos gruposConceptos) {
        persistenciaGruposConceptos.borrar(em, gruposConceptos);
    }

    @Override
    public void crearGruposConceptos(GruposConceptos gruposConceptos) {
        persistenciaGruposConceptos.crear(em, gruposConceptos);
    }

//Vigencias Grupos Conceptos
    @Override
    public void modificarVigenciaGruposConceptos(List<VigenciasGruposConceptos> listaVigenciasGruposConceptosModificar) {
        for (int i = 0; i < listaVigenciasGruposConceptosModificar.size(); i++) {
            log.warn("Modificando...");
            persistenciaVigenciasGruposConceptos.editar(em, listaVigenciasGruposConceptosModificar.get(i));
        }
    }

    @Override
    public void borrarVigenciaGruposConceptos(VigenciasGruposConceptos vigenciasGruposConceptos) {
        persistenciaVigenciasGruposConceptos.borrar(em, vigenciasGruposConceptos);
    }

    @Override
    public void crearVigenciaGruposConceptos(VigenciasGruposConceptos vigenciasGruposConceptos) {
        persistenciaVigenciasGruposConceptos.crear(em, vigenciasGruposConceptos);
    }

    @Override
    public List<VigenciasGruposConceptos> buscarVigenciasGruposConceptos(BigInteger secuencia) {
        try {
            return persistenciaVigenciasGruposConceptos.listVigenciasGruposConceptosPorGrupoConcepto(em, secuencia);
        } catch (Exception e) {
            log.error("Error AdministrarVigenciasFormales.vigenciasFormalesPersona " + e);
            return null;
        }
    }

    //LOV DE ABAJO
    public List<Conceptos> lovConceptos() {
        try {
            return persistenciaConceptos.conceptoEmpresa(em);
        } catch (Exception e) {
            log.error("Error AdministrarVigenciasFormales.vigenciasFormalesPersona " + e);
            return null;
        }
    }

}
