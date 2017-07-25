/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Administrar;

import Entidades.TiposUnidades;
import InterfaceAdministrar.AdministrarTiposUnidadesInterface;
import InterfacePersistencia.PersistenciaTiposUnidadesInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import InterfaceAdministrar.AdministrarSesionesInterface;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;

/**
 *
 * @author user
 */
@Stateless
public class AdministrarTiposUnidades implements AdministrarTiposUnidadesInterface {

   private static Logger log = Logger.getLogger(AdministrarTiposUnidades.class);

    @EJB
    PersistenciaTiposUnidadesInterface persistenciaTiposUnidades;
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
    public void modificarTiposUnidades(List<TiposUnidades> listaTiposUnidades) {
        for (int i = 0; i < listaTiposUnidades.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaTiposUnidades.editar(em, listaTiposUnidades.get(i));
        }
    }

    @Override
    public void borrarTiposUnidades(List<TiposUnidades> listaTiposUnidades) {
        for (int i = 0; i < listaTiposUnidades.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaTiposUnidades.borrar(em, listaTiposUnidades.get(i));
        }
    }

    @Override
    public void crearTiposUnidades(List<TiposUnidades> listaTiposUnidades) {
        for (int i = 0; i < listaTiposUnidades.size(); i++) {
            log.warn("Administrar Creando...");
            persistenciaTiposUnidades.crear(em, listaTiposUnidades.get(i));
        }
    }

    public List<TiposUnidades> consultarTiposUnidades() {
        List<TiposUnidades> listaTiposUnidades;
        listaTiposUnidades = persistenciaTiposUnidades.consultarTiposUnidades(em);
        return listaTiposUnidades;
    }

    @Override
    public TiposUnidades consultarTipoIndicador(BigInteger secMotivoDemanda) {
        TiposUnidades tiposIndicadores;
        tiposIndicadores = persistenciaTiposUnidades.consultarTipoUnidad(em, secMotivoDemanda);
        return tiposIndicadores;
    }

    @Override
    public BigInteger contarUnidadesTipoUnidad(BigInteger secuenciaVigenciasIndicadores) {
        BigInteger contarCursosTipoCurso = null;

        try {
            log.error("Secuencia Vigencias Indicadores " + secuenciaVigenciasIndicadores);
            contarCursosTipoCurso = persistenciaTiposUnidades.contarUnidadesTipoUnidad(em, secuenciaVigenciasIndicadores);
        } catch (Exception e) {
            log.error("ERROR AdmnistrarTiposUnidades contarUnidadesTipoUnidad ERROR :" + e);
        } finally {
            return contarCursosTipoCurso;
        }
    }
}
