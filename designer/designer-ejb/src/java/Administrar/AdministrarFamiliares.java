/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Familiares;
import InterfaceAdministrar.AdministrarFamiliaresInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaFamiliaresInterface;
import java.math.BigDecimal;
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
public class AdministrarFamiliares implements AdministrarFamiliaresInterface {

    @EJB
    AdministrarSesionesInterface administrarSesiones;
    @EJB
    PersistenciaFamiliaresInterface persistenciaFamiliares;
    private EntityManager em;

    @Override
    public void obtenerConexion(String idSesion) {
         em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    @Override
    public void modificarFamiliares(List<Familiares> listaModificar) {
        for (int i = 0; i < listaModificar.size(); i++) {
            System.out.println("Administrar Modificando...");
            persistenciaFamiliares.editar(em,listaModificar.get(i));
        }
    }

    @Override
    public void borrarFamiliares(List<Familiares> listaBorrar) {
        for (int i = 0; i < listaBorrar.size(); i++) {
            System.out.println("Administrar Borrando...");
            persistenciaFamiliares.borrar(em,listaBorrar.get(i));
        }
    }

    @Override
    public void crearFamilares(List<Familiares> listaCrear) {
        for (int i = 0; i < listaCrear.size(); i++) {
            System.out.println("Administrar Creando...");
            persistenciaFamiliares.crear(em,listaCrear.get(i));
        }
    }

    @Override
    public List<Familiares> consultarFamiliares(BigInteger secuenciaEmp) {
        List<Familiares> listaFamiliares;
        listaFamiliares = persistenciaFamiliares.familiaresPersona(em,secuenciaEmp);
        return listaFamiliares;
    }

}
