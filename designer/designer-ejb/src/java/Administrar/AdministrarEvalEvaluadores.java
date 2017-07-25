/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.EvalEvaluadores;
import InterfaceAdministrar.AdministrarEvalEvaluadoresInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaEvalEvaluadoresInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;

/**
 *
 * @author user
 */
@Stateless
public class AdministrarEvalEvaluadores implements AdministrarEvalEvaluadoresInterface {

   private static Logger log = Logger.getLogger(AdministrarEvalEvaluadores.class);

    @EJB
    PersistenciaEvalEvaluadoresInterface persistenciaEvalEvaluadores;
    /**
     * Enterprise JavaBean.<br>
     * Atributo que representa todo lo referente a la conexión del usuario que
     * está usando el aplicativo.
     */
    @EJB
    AdministrarSesionesInterface administrarSesiones;

    private EntityManager em;

    private EvalEvaluadores valEvaluadoresSeleccionado;
    private EvalEvaluadores valEvaluadores;
    private List<EvalEvaluadores> listEvalEvaluadores;

    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    @Override
    public void modificarEvalEvaluadores(List<EvalEvaluadores> listEvalEvaluadores) {
        for (int i = 0; i < listEvalEvaluadores.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaEvalEvaluadores.editar(em,listEvalEvaluadores.get(i));
        }
    }

    @Override
    public void borrarEvalEvaluadores(List<EvalEvaluadores> listEvalEvaluadores) {
        for (int i = 0; i < listEvalEvaluadores.size(); i++) {
            log.warn("Administrar Borrar...");
            persistenciaEvalEvaluadores.borrar(em,listEvalEvaluadores.get(i));
        }
    }

    @Override
    public void crearEvalEvaluadores(List<EvalEvaluadores> listEvalEvaluadores) {
        for (int i = 0; i < listEvalEvaluadores.size(); i++) {
            log.warn("Administrar Crear...");
            persistenciaEvalEvaluadores.crear(em,listEvalEvaluadores.get(i));
        }
    }

    @Override
    public List<EvalEvaluadores> consultarEvalEvaluadores() {
        listEvalEvaluadores = persistenciaEvalEvaluadores.buscarEvalEvaluadores(em);
        return listEvalEvaluadores;
    }

    @Override
    public EvalEvaluadores consultarEvalEvaluador(BigInteger secEvalEvaluadores) {
        valEvaluadores = persistenciaEvalEvaluadores.buscarEvalEvaluador(em,secEvalEvaluadores);
        return valEvaluadores;
    }

    @Override
    public BigInteger verificarEvalPruebas(BigInteger secuenciaMovitoCambioCargo) {
        BigInteger verificadorVP = new BigInteger("-1");
        try {
            verificadorVP = persistenciaEvalEvaluadores.verificarBorradoEvalPruebas(em,secuenciaMovitoCambioCargo);
        } catch (Exception e) {
            log.error("ERROR AdministrarEvalEvaluadores verificarBorradoVC ERROR :" + e);
        } finally {
            return verificadorVP;
        }
    }
}
