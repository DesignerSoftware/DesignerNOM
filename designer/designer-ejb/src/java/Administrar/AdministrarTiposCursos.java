/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import InterfaceAdministrar.AdministrarTiposCursosInterface;
import Entidades.TiposCursos;
import InterfacePersistencia.PersistenciaTiposCursosInterface;
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
public class AdministrarTiposCursos implements AdministrarTiposCursosInterface {

   private static Logger log = Logger.getLogger(AdministrarTiposCursos.class);

    @EJB
    PersistenciaTiposCursosInterface persistenciaTiposCursos;
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
    public void modificarTiposCursos(List<TiposCursos> listaTiposCursos) {
        for (int i = 0; i < listaTiposCursos.size(); i++) {
            persistenciaTiposCursos.editar(em, listaTiposCursos.get(i));
        }
    }

    @Override
    public void borrarTiposCursos(List<TiposCursos> listaTiposCursos) {
        for (int i = 0; i < listaTiposCursos.size(); i++) {
            persistenciaTiposCursos.borrar(em, listaTiposCursos.get(i));
        }
    }

    @Override
    public void crearTiposCursos(List<TiposCursos> listaTiposCursos) {
        for (int i = 0; i < listaTiposCursos.size(); i++) {
            persistenciaTiposCursos.crear(em, listaTiposCursos.get(i));
        }
    }

    @Override
    public List<TiposCursos> consultarTiposCursos() {
        List<TiposCursos> listaTiposCursos;
        listaTiposCursos = persistenciaTiposCursos.consultarTiposCursos(em);
        return listaTiposCursos;
    }

    @Override
    public TiposCursos consultarTipoIndicador(BigInteger secMotivoDemanda) {
        TiposCursos tiposIndicadores;
        tiposIndicadores = persistenciaTiposCursos.consultarTipoCurso(em, secMotivoDemanda);
        return tiposIndicadores;
    }

    @Override
    public BigInteger contarCursosTipoCurso(BigInteger secuenciaVigenciasIndicadores) {
        BigInteger contarCursosTipoCurso = null;

        try {
            log.error("Secuencia Vigencias Indicadores " + secuenciaVigenciasIndicadores);
            contarCursosTipoCurso = persistenciaTiposCursos.contarCursosTipoCurso(em, secuenciaVigenciasIndicadores);
        } catch (Exception e) {
            log.error("ERROR AdmnistrarTiposCursos contarCursosTipoCurso ERROR :" + e);
        } finally {
            return contarCursosTipoCurso;
        }
    }
}
