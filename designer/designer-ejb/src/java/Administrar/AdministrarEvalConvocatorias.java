/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Evalconvocatorias;
import InterfaceAdministrar.AdministrarEvalConvocatoriasInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaEvalConvocatoriasInterface;
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
public class AdministrarEvalConvocatorias implements AdministrarEvalConvocatoriasInterface {

   private static Logger log = Logger.getLogger(AdministrarEvalConvocatorias.class);

    @EJB
    AdministrarSesionesInterface administrarSesiones;
    @EJB
    PersistenciaEvalConvocatoriasInterface persistenciaevalconv;
    private EntityManager em;

    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    @Override
    public void crear(List<Evalconvocatorias> listCrear) {
        try {
            for (int i = 0; i < listCrear.size(); i++) {
                persistenciaevalconv.crear(em, listCrear.get(i));
            }
        } catch (Exception e) {
            log.warn("AdministrarEvalConvocatorias.crear : " + e.toString());
        }
    }

    @Override
    public void borrar(List<Evalconvocatorias> listBorrar) {
        try {
            for (int i = 0; i < listBorrar.size(); i++) {
                persistenciaevalconv.borrar(em, listBorrar.get(i));
            }
        } catch (Exception e) {
            log.warn("AdministrarEvalConvocatorias.borrar : " + e.toString());
        }
    }

    @Override
    public void editar(List<Evalconvocatorias> listModificar) {
        try {
            for (int i = 0; i < listModificar.size(); i++) {
                persistenciaevalconv.editar(em, listModificar.get(i));
            }
        } catch (Exception e) {
            log.warn("AdministrarEvalConvocatorias.editar : " + e.toString());
        }
    }

    @Override
    public List<Evalconvocatorias> buscarEvalConvocatorias(BigInteger secuenciaEmpleado) {
        List<Evalconvocatorias> listEvalConv = persistenciaevalconv.consultarEvalConvocatorias(em, secuenciaEmpleado);
        return listEvalConv;
    }

}
