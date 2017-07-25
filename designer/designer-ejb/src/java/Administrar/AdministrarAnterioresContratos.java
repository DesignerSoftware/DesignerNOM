/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.AnterioresContratos;
import Entidades.Cargos;
import InterfaceAdministrar.AdministrarAnterioresContratosInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaAnterioresContratosInterface;
import InterfacePersistencia.PersistenciaCargosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.ejb.Local;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;

/**
 *
 * @author user
 */
@Stateful
@Local
public class AdministrarAnterioresContratos implements AdministrarAnterioresContratosInterface {

   private static Logger log = Logger.getLogger(AdministrarAnterioresContratos.class);

    @EJB
    AdministrarSesionesInterface administrarSesiones;
    @EJB
    PersistenciaAnterioresContratosInterface persistenciaAnterioresContrato;
    @EJB
    PersistenciaCargosInterface persistenciaCargos;
    private EntityManager em;

    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    @Override
    public void crearAnteriorContrato(List<AnterioresContratos> listaCrear) {
        try {
            for (int i = 0; i < listaCrear.size(); i++) {
                persistenciaAnterioresContrato.crear(em, listaCrear.get(i));
            }
        } catch (Exception e) {
            log.warn("Errror crearAnteriorContrato admi : " + e.toString());
        }
    }

    @Override
    public void editarAnteriorContrato(List<AnterioresContratos> listaModificar) {
        try {
            for (int i = 0; i < listaModificar.size(); i++) {
                persistenciaAnterioresContrato.editar(em, listaModificar.get(i));
            }
        } catch (Exception e) {
            log.warn("Errror editarAnteriorContrato admi : " + e.toString());
        }
    }

    @Override
    public void borrarAnteriorContrato(List<AnterioresContratos> listaBorrar) {
        try {
            for (int i = 0; i < listaBorrar.size(); i++) {
                persistenciaAnterioresContrato.borrar(em, listaBorrar.get(i));
            }
        } catch (Exception e) {
            log.warn("Errror borrarAnteriorContrato admi : " + e.toString());
        }
    }

    @Override
    public List<AnterioresContratos> listaAnterioresContratos(BigInteger secPersona) {
        try {
            List<AnterioresContratos> listaAC = persistenciaAnterioresContrato.anterioresContratosPersona(em, secPersona);
            return listaAC;
        } catch (Exception e) {
            log.warn("Error listaAnterioresContratos.admi :" + e.toString());
            return null;
        }
    }

    @Override
    public List<Cargos> lovCargos() {
        try {
            List<Cargos> lovCargos = persistenciaCargos.consultarCargos(em);
            return lovCargos;
        } catch (Exception e) {
            log.warn("Error lovCargos Admi " + e.getMessage());
            return null;
        }
    }

}
