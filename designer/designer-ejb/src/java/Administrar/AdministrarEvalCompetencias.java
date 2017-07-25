/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.EvalCompetencias;
import InterfaceAdministrar.AdministrarEvalCompetenciasInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaEvalCompetenciasInterface;
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
public class AdministrarEvalCompetencias implements AdministrarEvalCompetenciasInterface {

   private static Logger log = Logger.getLogger(AdministrarEvalCompetencias.class);

    @EJB
    PersistenciaEvalCompetenciasInterface persistenciaEvalCompetencias;

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
    public void modificarEvalCompetencias(List<EvalCompetencias> listEvalCompetencias) {
        for (int i = 0; i < listEvalCompetencias.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaEvalCompetencias.editar(em,listEvalCompetencias.get(i));
        }
    }

    @Override
    public void borrarEvalCompetencias(List<EvalCompetencias> listEvalCompetencias) {
        for (int i = 0; i < listEvalCompetencias.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaEvalCompetencias.borrar(em,listEvalCompetencias.get(i));
        }
    }

    @Override
    public void crearEvalCompetencias(List<EvalCompetencias> listEvalCompetencias) {
        for (int i = 0; i < listEvalCompetencias.size(); i++) {
            log.warn("Administrar Creando...");
            persistenciaEvalCompetencias.crear(em,listEvalCompetencias.get(i));
        }
    }

    @Override
    public List<EvalCompetencias> consultarEvalCompetencias() {
        List<EvalCompetencias> listEvalCompetencias;
        listEvalCompetencias = persistenciaEvalCompetencias.buscarEvalCompetencias(em);
        return listEvalCompetencias;
    }

    @Override
    public EvalCompetencias consultarEvalCompetencia(BigInteger secTipoEmpresa) {
        EvalCompetencias evalCompetencias;
        evalCompetencias = persistenciaEvalCompetencias.buscarEvalCompetencia(em,secTipoEmpresa);
        return evalCompetencias;
    }

    @Override
    public BigInteger verificarCompetenciasCargos(BigInteger secuenciaCompetenciasCargos) {
        BigInteger verificadorCompetenciasCargos = null;
        try {
            log.error("Secuencia Borrado Competencias Cargos" + secuenciaCompetenciasCargos);
            verificadorCompetenciasCargos = persistenciaEvalCompetencias.contadorCompetenciasCargos(em,secuenciaCompetenciasCargos);
        } catch (Exception e) {
            log.error("ERROR AdministrarEvalCompetencias verificarBorradoCompetenciasCargos ERROR :" + e);
        } finally {
            return verificadorCompetenciasCargos;
        }
    }

}
