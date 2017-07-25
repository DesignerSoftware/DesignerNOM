/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Empleados;
import Entidades.EvalResultadosConv;
import InterfaceAdministrar.AdministrarEvalResultadosConvInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaEvalResultadosConvInterface;
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
public class AdministrarEvalResultadosConv implements AdministrarEvalResultadosConvInterface {

   private static Logger log = Logger.getLogger(AdministrarEvalResultadosConv.class);

    @EJB
    AdministrarSesionesInterface administrarSesiones;
    @EJB
    PersistenciaEvalResultadosConvInterface persistenciarevalresconv;
    @EJB
    PersistenciaEmpleadoInterface persistenciaEmpleado;
    private EntityManager em;

    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    @Override
    public void crear(List<EvalResultadosConv> listCrear) {
        try {
            for (int i = 0; i < listCrear.size(); i++) {
                persistenciarevalresconv.crear(em, listCrear.get(i));
            }
        } catch (Exception e) {
            log.warn("AdministrarEvalConvocatorias.crear : " + e.toString());
        }
    }

    @Override
    public void borrar(List<EvalResultadosConv> listBorrar) {
        try {
            for (int i = 0; i < listBorrar.size(); i++) {
                persistenciarevalresconv.borrar(em, listBorrar.get(i));
            }
        } catch (Exception e) {
            log.warn("AdministrarEvalConvocatorias.borrar : " + e.toString());
        }
    }

    @Override
    public void editar(List<EvalResultadosConv> listModificar) {
        try {
            for (int i = 0; i < listModificar.size(); i++) {
                persistenciarevalresconv.editar(em, listModificar.get(i));
            }
        } catch (Exception e) {
            log.warn("AdministrarEvalConvocatorias.editar : " + e.toString());
        }
    }

    @Override
    public List<EvalResultadosConv> buscarEvalResultadosConvocatorias(BigInteger secEmpleado) {
        List<EvalResultadosConv> listevalresconv = persistenciarevalresconv.consultarEvalResultadosConvocatorias(em, secEmpleado);
        return listevalresconv;
    }

    @Override
    public Empleados empleadoActual(BigInteger secuenciaP) {
        try {
            Empleados retorno = persistenciaEmpleado.buscarEmpleado(em, secuenciaP);
            return retorno;
        } catch (Exception e) {
            log.warn("Error empleadoActual Admi : " + e.toString());
            return null;
        }
    }

}
