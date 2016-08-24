/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.TiposReemplazos;
import InterfaceAdministrar.AdministrarTiposReemplazosInterface;
import InterfacePersistencia.PersistenciaTiposReemplazosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import InterfaceAdministrar.AdministrarSesionesInterface;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
@Stateful
public class AdministrarTiposReemplazos implements AdministrarTiposReemplazosInterface {

    @EJB
    PersistenciaTiposReemplazosInterface persistenciaTiposReemplazos;
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
    public void modificarTiposReemplazos(List<TiposReemplazos> listaTiposReemplazos) {
        for (int i = 0; i < listaTiposReemplazos.size(); i++) {
            System.out.println("Administrar Modificando...");
            persistenciaTiposReemplazos.editar(em, listaTiposReemplazos.get(i));
        }
    }

    @Override
    public void borrarTiposReemplazos(List<TiposReemplazos> listaTiposReemplazos) {
        for (int i = 0; i < listaTiposReemplazos.size(); i++) {
            System.out.println("Administrar Borrando...");
            persistenciaTiposReemplazos.borrar(em, listaTiposReemplazos.get(i));
        }
    }

    @Override
    public void crearTiposReemplazos(List<TiposReemplazos> listaTiposReemplazos) {
        for (int i = 0; i < listaTiposReemplazos.size(); i++) {
            System.out.println("Administrar Creando...");
            persistenciaTiposReemplazos.crear(em, listaTiposReemplazos.get(i));
        }
    }

    @Override
    public List<TiposReemplazos> consultarTiposReemplazos() {
        List<TiposReemplazos> listTiposReemplazos;
        listTiposReemplazos = persistenciaTiposReemplazos.buscarTiposReemplazos(em);
        return listTiposReemplazos;
    }

    @Override
    public TiposReemplazos consultarTipoReemplazo(BigInteger secMotivoDemanda) {
        TiposReemplazos tiposReemplazo;
        tiposReemplazo = persistenciaTiposReemplazos.buscarTipoReemplazo(em, secMotivoDemanda);
        return tiposReemplazo;
    }

    @Override
    public BigInteger contarEncargaturasTipoReemplazo(BigInteger secuenciaTiposReemplazos) {
        BigInteger verificarBorradoEncargaturas = null;
        try {
            System.out.println("Secuencia Vigencias Indicadores " + secuenciaTiposReemplazos);
            return verificarBorradoEncargaturas = persistenciaTiposReemplazos.contadorEncargaturas(em, secuenciaTiposReemplazos);
        } catch (Exception e) {
            System.err.println("ERROR ADMINISTRARTIPOSREEMPLAZOS VERIFICARBORRADOENCARGATURAS ERROR :" + e);

            return verificarBorradoEncargaturas;
        }
    }

    @Override
    public BigInteger contarProgramacionesTiemposTipoReemplazo(BigInteger secuenciaTiposReemplazos) {
        BigInteger verificarBorradoProgramacionesTiempos = null;
        try {
            System.out.println("Secuencia Vigencias Indicadores " + secuenciaTiposReemplazos);
            return verificarBorradoProgramacionesTiempos = persistenciaTiposReemplazos.contadorProgramacionesTiempos(em, secuenciaTiposReemplazos);
        } catch (Exception e) {
            System.err.println("ERROR ADMINISTRARTIPOSREEMPLAZOS VERIFICARBORRADOPROGRAMACIONESTIEMPOS ERROR :" + e);

            return verificarBorradoProgramacionesTiempos;
        }
    }

    @Override
    public BigInteger contarReemplazosTipoReemplazo(BigInteger secuenciaTiposReemplazos) {
        BigInteger verificarBorradoReemplazos = null;
        try {
            System.out.println("Secuencia Vigencias Indicadores " + secuenciaTiposReemplazos);
            return verificarBorradoReemplazos = persistenciaTiposReemplazos.contadorReemplazos(em, secuenciaTiposReemplazos);
        } catch (Exception e) {
            System.err.println("ERROR ADMINISTRARTIPOSREEMPLAZOS VERIFICARBORRADOREEMPLAZOS ERROR :" + e);

            return verificarBorradoReemplazos;
        }
    }

    @Override
    public List<TiposReemplazos> consultarLOVTiposReemplazos() {
        return persistenciaTiposReemplazos.buscarTiposReemplazos(em);
    }
}
