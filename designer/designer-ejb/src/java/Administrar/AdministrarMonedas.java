/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Monedas;
import InterfaceAdministrar.AdministrarMonedasInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaMonedasInterface;
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
public class AdministrarMonedas implements AdministrarMonedasInterface {

   private static Logger log = Logger.getLogger(AdministrarMonedas.class);

    @EJB
    PersistenciaMonedasInterface persistenciaMonedas;
    private Monedas monedaSeleccionado;
    private Monedas monedas;
    private List<Monedas> listMonedas;
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
    public void modificarMonedas(List<Monedas> listMonedasModificadas) {
        for (int i = 0; i < listMonedasModificadas.size(); i++) {
            log.warn("Administrar Modificando...");
            monedaSeleccionado = listMonedasModificadas.get(i);
            persistenciaMonedas.editar(em, monedaSeleccionado);
        }
    }

    @Override
    public void borrarMonedas(Monedas monedas) {
        persistenciaMonedas.borrar(em, monedas);
    }

    @Override
    public void crearMonedas(Monedas monedas) {
        persistenciaMonedas.crear(em, monedas);
    }

    @Override
    public List<Monedas> consultarMonedas() {
        listMonedas = persistenciaMonedas.consultarMonedas(em);
        return listMonedas;
    }

    @Override
    public Monedas consultarMoneda(BigInteger secMoneda) {
        monedas = persistenciaMonedas.consultarMoneda(em, secMoneda);
        return monedas;
    }

    @Override
    public BigInteger verificarMonedasProyecto(BigInteger secuenciaIdiomas) {
        BigInteger verificadorProyectos = null;
        try {
            log.error("Secuencia Borrado Proyecto" + secuenciaIdiomas);
            verificadorProyectos = persistenciaMonedas.contadorProyectos(em, secuenciaIdiomas);
        } catch (Exception e) {
            log.error("ERROR AdministrarMonedas verificarBorradoProyecto ERROR :" + e);
        } finally {
            return verificadorProyectos;
        }
    }
}
