/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.SoAntecedentes;
import Entidades.SoTiposAntecedentes;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfaceAdministrar.AdministrarSoAntecedentesInterface;
import InterfacePersistencia.PersistenciaSoAntecedentesInterface;
import InterfacePersistencia.PersistenciaSoTiposAntecedentesInterface;
import Persistencia.PersistenciaSoAntecedentes;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;

@Stateful
public class AdministrarSoAntecedentes implements AdministrarSoAntecedentesInterface {

    @EJB
    AdministrarSesionesInterface administrarSesiones;
    @EJB
    PersistenciaSoAntecedentesInterface persistenciaAntecedentes;
    @EJB
    PersistenciaSoTiposAntecedentesInterface persistenciaTiposAntecedentes;
    private EntityManager em;

    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    @Override
    public void modificarAntecedente(List<SoAntecedentes> listaModificar) {
        for (int i = 0; i < listaModificar.size(); i++) {
            persistenciaAntecedentes.editar(em, listaModificar.get(i));
        }
    }

    @Override
    public void crearAntecedente(List<SoAntecedentes> listaCrear) {
        for (int i = 0; i < listaCrear.size(); i++) {
            persistenciaAntecedentes.crear(em, listaCrear.get(i));
        }
    }

    @Override
    public void borrarAntecedente(List<SoAntecedentes> listaBorrar) {
        for (int i = 0; i < listaBorrar.size(); i++) {
            persistenciaAntecedentes.borrar(em, listaBorrar.get(i));
        }
    }

    @Override
    public List<SoAntecedentes> consultarAntecedentesPorTipo(BigInteger secTipoAntecedente) {
        try{
        List<SoAntecedentes> listAntecedentes = persistenciaAntecedentes.lovAntecedentes(em, secTipoAntecedente);
        return listAntecedentes;
        }catch(Exception e){
            System.out.println("error en consultarAntecedentesPorTipo : " + e.toString());
            return null;    
        }
    }

    @Override
    public List<SoAntecedentes> consultarAntecedentes() {
        try {
            List<SoAntecedentes> listAntecedentes = persistenciaAntecedentes.listaAntecedentes(em);
            return listAntecedentes;
        } catch (Exception e) {
            System.out.println("error en consultarAntecedentes : " + e.toString());
            return null;
        }

    }

    @Override
    public List<SoTiposAntecedentes> consultarTiposAntecedentes() {
        try {
            List<SoTiposAntecedentes> lovTiposAntecedentes = persistenciaTiposAntecedentes.listaTiposAntecedentes(em);
            return lovTiposAntecedentes;
        } catch (Exception e) {
            System.out.println("error en consultarTiposAntecedentes");
            return null;
        }
    }

}
