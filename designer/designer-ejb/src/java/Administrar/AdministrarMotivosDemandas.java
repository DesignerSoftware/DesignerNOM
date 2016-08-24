/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import InterfaceAdministrar.AdministrarMotivosDemandasInterface;
import Entidades.MotivosDemandas;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaMotivosDemandasInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
@Stateful
public class AdministrarMotivosDemandas implements AdministrarMotivosDemandasInterface {

    @EJB
    PersistenciaMotivosDemandasInterface persistenciaMotivosDemandas;
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
    public void modificarMotivosDemandas(List<MotivosDemandas> listMotivosDemandas) {
        for (int i = 0; i < listMotivosDemandas.size(); i++) {
            System.out.println("Administrar Modificando...");
            persistenciaMotivosDemandas.editar(em, listMotivosDemandas.get(i));
        }
    }

    @Override
    public void borrarMotivosDemandas(List<MotivosDemandas> listMotivosDemandas) {
        for (int i = 0; i < listMotivosDemandas.size(); i++) {
            System.out.println("Administrar Borrando...");
            persistenciaMotivosDemandas.borrar(em, listMotivosDemandas.get(i));
        }
    }

    @Override
    public void crearMotivosDemandas(List<MotivosDemandas> listMotivosDemandas) {
        for (int i = 0; i < listMotivosDemandas.size(); i++) {
            System.out.println("Administrar Creando...");
            persistenciaMotivosDemandas.crear(em, listMotivosDemandas.get(i));
        }
    }

    @Override
    public List<MotivosDemandas> consultarMotivosDemandas() {
        List<MotivosDemandas> listMotivoDemanda;
        listMotivoDemanda = persistenciaMotivosDemandas.buscarMotivosDemandas(em);
        return listMotivoDemanda;
    }

    @Override
    public MotivosDemandas consultarMotivoDemanda(BigInteger secMotivoDemanda) {
        MotivosDemandas motivoDemanda;
        motivoDemanda = persistenciaMotivosDemandas.buscarMotivoDemanda(em, secMotivoDemanda);
        return motivoDemanda;
    }

    @Override
    public BigInteger contarDemandasMotivoDemanda(BigInteger secuenciaEventos) {
        BigInteger verificadorDemanda = null;

        try {
            System.err.println("Secuencia Motivo Demanda " + secuenciaEventos);
            verificadorDemanda = persistenciaMotivosDemandas.contadorDemandas(em, secuenciaEventos);
        } catch (Exception e) {
            System.err.println("ERROR AdmnistrarMotivoDemanda verificarBorradoDemanda ERROR :" + e);
        } finally {
            return verificadorDemanda;
        }
    }
}
