/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import InterfaceAdministrar.AdministrarTiposExamenesInterface;
import Entidades.TiposExamenes;
import InterfacePersistencia.PersistenciaTiposExamenesInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import InterfaceAdministrar.AdministrarSesionesInterface;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;

/**
 *
 * @author user
 */
@Stateful
public class AdministrarTiposExamenes implements AdministrarTiposExamenesInterface {

   private static Logger log = Logger.getLogger(AdministrarTiposExamenes.class);

    @EJB
    PersistenciaTiposExamenesInterface persistenciaTiposExamenes;
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
    public void modificarTiposExamenes(List<TiposExamenes> listaTiposExamenes) {
        for (int i = 0; i < listaTiposExamenes.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaTiposExamenes.editar(em, listaTiposExamenes.get(i));
        }
    }

    public void borrarTiposExamenes(List<TiposExamenes> listaTiposExamenes) {
        for (int i = 0; i < listaTiposExamenes.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaTiposExamenes.borrar(em, listaTiposExamenes.get(i));
        }
    }

    public void crearTiposExamenes(List<TiposExamenes> listaTiposExamenes) {
        for (int i = 0; i < listaTiposExamenes.size(); i++) {
            log.warn("Administrar crear...");
            persistenciaTiposExamenes.crear(em, listaTiposExamenes.get(i));
        }
    }

    public List<TiposExamenes> consultarTiposExamenes() {
        List<TiposExamenes> listTiposTallas;
        listTiposTallas = persistenciaTiposExamenes.buscarTiposExamenes(em);
        return listTiposTallas;
    }

    public TiposExamenes consultarTipoExamen(BigInteger secTipoEmpresa) {
        TiposExamenes tiposTallas;
        tiposTallas = persistenciaTiposExamenes.buscarTipoExamen(em, secTipoEmpresa);
        return tiposTallas;
    }

    public BigInteger contarTiposExamenesCargosTipoExamen(BigInteger secuenciaTiposExamenesCargos) {
        BigInteger verificadorTiposExamenesCargos;
        try {
            log.error("Secuencia Borrado Elementos" + secuenciaTiposExamenesCargos);
            return verificadorTiposExamenesCargos = persistenciaTiposExamenes.contadorTiposExamenesCargos(em, secuenciaTiposExamenesCargos);
        } catch (Exception e) {
            log.error("ERROR AdministrarTiposExamenes verificarBorradoTiposExamenesCargos ERROR :" + e);
            return null;
        }
    }

    public BigInteger contarVigenciasExamenesMedicosTipoExamen(BigInteger secuenciaVigenciasExamenesMedicos) {
        try {
            BigInteger verificadorVigenciasExamenesMedicos;
            log.error("Secuencia Borrado Vigencias Tallas" + secuenciaVigenciasExamenesMedicos);
            return verificadorVigenciasExamenesMedicos = persistenciaTiposExamenes.contadorVigenciasExamenesMedicos(em, secuenciaVigenciasExamenesMedicos);
        } catch (Exception e) {
            log.error("ERROR AdministrarTiposExamenes verificarBorradoVigenciasExamenesMedicos ERROR :" + e);
            return null;
        }
    }
}
