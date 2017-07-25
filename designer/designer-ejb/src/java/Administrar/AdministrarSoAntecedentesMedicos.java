/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.SoAntecedentes;
import Entidades.SoAntecedentesMedicos;
import Entidades.SoTiposAntecedentes;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfaceAdministrar.AdministrarSoAntecedentesMedicosInterface;
import InterfacePersistencia.PersistenciaSoAntecedentesInterface;
import InterfacePersistencia.PersistenciaSoAntecedentesMedicosInterface;
import InterfacePersistencia.PersistenciaSoTiposAntecedentesInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;

/**
 *
 * @author user
 */
@Stateful
public class AdministrarSoAntecedentesMedicos implements AdministrarSoAntecedentesMedicosInterface {

   private static Logger log = Logger.getLogger(AdministrarSoAntecedentesMedicos.class);

    @EJB
    AdministrarSesionesInterface administrarSesiones;
    @EJB
    PersistenciaSoAntecedentesMedicosInterface PersistenciaAntecedentesM;
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
    public void modificarAntecedentesM(List<SoAntecedentesMedicos> listaModificar) {
        for (int i = 0; i < listaModificar.size(); i++) {
            PersistenciaAntecedentesM.editar(em, listaModificar.get(i));
        }
    }

    @Override
    public void crearAntecedentesM(List<SoAntecedentesMedicos> listaCrear) {
        for (int i = 0; i < listaCrear.size(); i++) {
            PersistenciaAntecedentesM.crear(em, listaCrear.get(i));
        }
    }

    @Override
    public void borrarAntecedentesM(List<SoAntecedentesMedicos> listaBorrar) {
        for (int i = 0; i < listaBorrar.size(); i++) {
            PersistenciaAntecedentesM.borrar(em, listaBorrar.get(i));
        }
    }

    @Override
    public List<SoTiposAntecedentes> consultarTiposAntecedentes() {
         List<SoTiposAntecedentes> listTiposAntecedentes = persistenciaTiposAntecedentes.listaTiposAntecedentes(em);
        return listTiposAntecedentes;
    }

    @Override
    public List<SoAntecedentes> consultarAntecedentes(BigInteger secTipoAntecedente) {
        List<SoAntecedentes> listAntecedentes = persistenciaAntecedentes.lovAntecedentes(em, secTipoAntecedente);
        return listAntecedentes;
    }

    @Override
    public List<SoAntecedentesMedicos> consultarAntecedentesMedicos(BigInteger secPersona) {
        List<SoAntecedentesMedicos> listAntecedentesM = PersistenciaAntecedentesM.listaAntecedentesMedicos(em, secPersona);
        return listAntecedentesM;
    }
}
