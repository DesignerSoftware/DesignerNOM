/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Enfoques;
import InterfaceAdministrar.AdministrarEnfoquesInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaEnfoquesInterface;
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
public class AdministrarEnfoques implements AdministrarEnfoquesInterface {

    @EJB
    PersistenciaEnfoquesInterface PersistenciaEnfoques;
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

    public void modificarEnfoques(List<Enfoques> listEnfoques) {
        for (int i = 0; i < listEnfoques.size(); i++) {
            System.out.println("Administrar Modificando...");
            PersistenciaEnfoques.editar(em,listEnfoques.get(i));
        }
    }

    public void borrarEnfoques(List<Enfoques> listEnfoques) {
        for (int i = 0; i < listEnfoques.size(); i++) {
            System.out.println("Administrar Borrando...");
            PersistenciaEnfoques.borrar(em,listEnfoques.get(i));
        }
    }

    public void crearEnfoques(List<Enfoques> listEnfoques) {
        for (int i = 0; i < listEnfoques.size(); i++) {
            System.out.println("Administrar Creando...");
            PersistenciaEnfoques.crear(em,listEnfoques.get(i));
        }
    }

    public List<Enfoques> consultarEnfoques() {
        List<Enfoques> listEnfoques;
        listEnfoques = PersistenciaEnfoques.buscarEnfoques(em);
        return listEnfoques;
    }

    public Enfoques consultarEnfoque(BigInteger secEnfoques) {
        Enfoques enfoques;
        enfoques = PersistenciaEnfoques.buscarEnfoque(em,secEnfoques);
        return enfoques;
    }

    public BigInteger verificarTiposDetalles(BigInteger secuenciaTiposAuxilios) {
        BigInteger verificarTiposDetalles = null;
        try {
            verificarTiposDetalles = PersistenciaEnfoques.contadorTiposDetalles(em,secuenciaTiposAuxilios);
        } catch (Exception e) {
            System.err.println("ERROR ADMINISTRAREVALPLANILLAS verificarTiposDetalles ERROR :" + e);
        } finally {
            return verificarTiposDetalles;
        }
    }
}
