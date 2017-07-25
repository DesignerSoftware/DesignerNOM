/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.SoTiposAntecedentes;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfaceAdministrar.AdministrarSoTiposAntecedentesInterface;
import InterfacePersistencia.PersistenciaSoTiposAntecedentesInterface;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;

@Stateful
public class AdministrarSoTiposAntecedentes implements AdministrarSoTiposAntecedentesInterface {

   private static Logger log = Logger.getLogger(AdministrarSoTiposAntecedentes.class);

    @EJB
    AdministrarSesionesInterface administrarSesiones;
    @EJB
    PersistenciaSoTiposAntecedentesInterface persistenciaTiposAntecedentes;
    private EntityManager em;

    @Override
    public void obtenerConexion(String idSesion) {
         em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    @Override
    public void modificarTipoAntecedente(List<SoTiposAntecedentes> listaModificar) {
        for (int i = 0; i < listaModificar.size(); i++) {
            persistenciaTiposAntecedentes.editar(em, listaModificar.get(i));
        }
    }

    @Override
    public void crearTipoAntecedente(List<SoTiposAntecedentes> listaCrear) {
        for (int i = 0; i < listaCrear.size(); i++) {
            persistenciaTiposAntecedentes.crear(em, listaCrear.get(i));
        }
    }

    @Override
    public void borrarTipoAntecedente(List<SoTiposAntecedentes> listaBorrar) {
        for (int i = 0; i < listaBorrar.size(); i++) {
            persistenciaTiposAntecedentes.borrar(em, listaBorrar.get(i));
        }
    }

    @Override
    public List<SoTiposAntecedentes> consultarTiposAntecedentes() {
        try{
         List<SoTiposAntecedentes> lovTiposAntecedentes = persistenciaTiposAntecedentes.listaTiposAntecedentes(em);
        return lovTiposAntecedentes;
        } catch(Exception e){
            log.warn("error en consultarTiposAntecedentes : " + e.toString());
            return null;
        }
    }
}
