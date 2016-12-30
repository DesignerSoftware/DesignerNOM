/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Bancos;
import InterfaceAdministrar.AdministrarBancosInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaBancosInterface;
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
public class AdministrarBancos implements AdministrarBancosInterface {

    @EJB
    PersistenciaBancosInterface persistenciaBancos;
    @EJB
    AdministrarSesionesInterface administrarSesiones;

    private EntityManager em;

    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    @Override
    public void modificarBanco(List<Bancos> listaModificar) {
        for (int i = 0; i < listaModificar.size(); i++) {
            persistenciaBancos.editar(em, listaModificar.get(i));
        }
    }

    @Override
    public void borrarBanco(List<Bancos> listaBorrar) {
        for (int i = 0; i < listaBorrar.size(); i++) {
            persistenciaBancos.borrar(em, listaBorrar.get(i));
        }
    }

    @Override
    public void crearBanco(List<Bancos> listaCrear) {
        try{
        for (int i = 0; i < listaCrear.size(); i++) {
            persistenciaBancos.crear(em, listaCrear.get(i));
        }
        }catch(Exception e){
            System.out.println("error en AdministrarBancos.crearBanco() " + e.toString());   
        }
    }

    @Override
    public List<Bancos> consultarBancos() {
        List<Bancos> listaBancos = persistenciaBancos.buscarBancos(em);
        return listaBancos;
    }

    @Override
    public Bancos consultarBancosPorSecuencia(BigInteger secuencia) {
        Bancos banco = persistenciaBancos.buscarBancosPorSecuencia(em, secuencia);
        return banco;        
    }
}
