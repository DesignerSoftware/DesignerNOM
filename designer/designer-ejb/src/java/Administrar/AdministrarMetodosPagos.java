/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.MetodosPagos;
import InterfaceAdministrar.AdministrarMetodosPagosInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaMetodosPagosInterface;
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
public class AdministrarMetodosPagos implements AdministrarMetodosPagosInterface {

   private static Logger log = Logger.getLogger(AdministrarMetodosPagos.class);

    @EJB
    PersistenciaMetodosPagosInterface persistenciaMetodosPagos;
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
    public void modificarMetodosPagos(List<MetodosPagos> listaMetodosPagos) {
        for (int i = 0; i < listaMetodosPagos.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaMetodosPagos.editar(em, listaMetodosPagos.get(i));
        }
    }

    @Override
    public void borrarMetodosPagos(List<MetodosPagos> listaMetodosPagos) {
        for (int i = 0; i < listaMetodosPagos.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaMetodosPagos.borrar(em, listaMetodosPagos.get(i));
        }
    }

    @Override
    public void crearMetodosPagos(List<MetodosPagos> listaMetodosPagos) {
        for (int i = 0; i < listaMetodosPagos.size(); i++) {
            log.warn("Administrar Creando...");
            persistenciaMetodosPagos.crear(em, listaMetodosPagos.get(i));
        }
    }

    @Override
    public List<MetodosPagos> consultarMetodosPagos() {
        List<MetodosPagos> listMetodosPagos;
        listMetodosPagos = persistenciaMetodosPagos.buscarMetodosPagos(em);
        return listMetodosPagos;
    }

    @Override
    public MetodosPagos consultarMetodoPago(BigInteger secMetodosPagos) {
        MetodosPagos metodosPago;
        metodosPago = persistenciaMetodosPagos.buscarMetodosPagosEmpleado(em, secMetodosPagos);
        return metodosPago;
    }

    @Override
    public BigInteger verificarMetodosPagosVigenciasFormasPagos(BigInteger secuenciaMetodoPago) {
        BigInteger verificarVigenciasFormasPagos = null;
        try {
            verificarVigenciasFormasPagos = persistenciaMetodosPagos.contadorvigenciasformaspagos(em, secuenciaMetodoPago);
        } catch (Exception e) {
            log.error("ERROR ADMINISTRARMETODOSPAGOS VERIFICARVIGENCIASFORMASPAGOS ERROR " + e);
        } finally {
            return verificarVigenciasFormasPagos;
        }
    }
}
