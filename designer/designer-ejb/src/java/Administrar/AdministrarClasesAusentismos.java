/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Clasesausentismos;
import Entidades.Tiposausentismos;
import InterfaceAdministrar.AdministrarClasesAusentismosInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaClasesAusentismosInterface;
import InterfacePersistencia.PersistenciaTiposAusentismosInterface;
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
public class AdministrarClasesAusentismos implements AdministrarClasesAusentismosInterface {

   private static Logger log = Logger.getLogger(AdministrarClasesAusentismos.class);

    @EJB
    PersistenciaClasesAusentismosInterface persistenciaClasesAusentismos;
    @EJB
    PersistenciaTiposAusentismosInterface PersistenciaTiposAusentismos;
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

    public void modificarClasesAusentismos(List<Clasesausentismos> listClasesAusentismos) {
        for (int i = 0; i < listClasesAusentismos.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaClasesAusentismos.editar(em,listClasesAusentismos.get(i));
        }
    }

    public void borrarClasesAusentismos(List<Clasesausentismos> listClasesAusentismos) {
        for (int i = 0; i < listClasesAusentismos.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaClasesAusentismos.borrar(em,listClasesAusentismos.get(i));
        }
    }

    public void crearClasesAusentismos(List<Clasesausentismos> listClasesAusentismos) {
        for (int i = 0; i < listClasesAusentismos.size(); i++) {
            log.warn("Administrar Creando...");
            persistenciaClasesAusentismos.crear(em,listClasesAusentismos.get(i));
        }
    }

    @Override
    public List<Clasesausentismos> consultarClasesAusentismos() {

        List<Clasesausentismos> listClasesAusentismos = persistenciaClasesAusentismos.buscarClasesAusentismos(em);
        return listClasesAusentismos;
    }

    public BigInteger contarCausasAusentismosClaseAusentismo(BigInteger secuenciaClasesAusentismos) {
        BigInteger verificadorHvReferencias = null;

        try {
            log.error("Secuencia Borrado Elementos" + secuenciaClasesAusentismos);
            verificadorHvReferencias = persistenciaClasesAusentismos.contadorCausasAusentismosClaseAusentismo(em,secuenciaClasesAusentismos);
        } catch (Exception e) {
            log.error("ERROR AdministrarClasesAusentismos contarCausasAusentismosClaseAusentismo ERROR :" + e);
        } finally {
            return verificadorHvReferencias;
        }
    }

    public BigInteger contarSoAusentismosClaseAusentismo(BigInteger secuenciaClasesAusentismos) {
        BigInteger verificadorHvReferencias = null;

        try {
            log.error("Secuencia Borrado Elementos" + secuenciaClasesAusentismos);
            verificadorHvReferencias = persistenciaClasesAusentismos.contadorSoAusentismosClaseAusentismo(em,secuenciaClasesAusentismos);
        } catch (Exception e) {
            log.error("ERROR AdministrarClasesAusentismos contarSoAusentismosClaseAusentismo ERROR :" + e);
        } finally {
            return verificadorHvReferencias;
        }
    }

    public List<Tiposausentismos> consultarLOVTiposAusentismos() {
        List<Tiposausentismos> listTiposAusentismos = null;
        listTiposAusentismos = PersistenciaTiposAusentismos.consultarTiposAusentismos(em);
        return listTiposAusentismos;

    }
}
