/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Evalvigconvocatorias;
import InterfaceAdministrar.AdministrarEvalVigConvocatoriasInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaEvalVigConvocatoriasInterface;
import Persistencia.PersistenciaEvalVigConvocatorias;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
@Stateful
public class AdministrarEvalVigConvocatorias implements AdministrarEvalVigConvocatoriasInterface {

    @EJB
    AdministrarSesionesInterface administrarSesiones;
    @EJB
    PersistenciaEvalVigConvocatoriasInterface persistenciaevalvigconv;
    private EntityManager em;

    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    @Override
    public void crear(List<Evalvigconvocatorias> listCrear) {
        try {
            for (int i = 0; i < listCrear.size(); i++) {
                persistenciaevalvigconv.crear(em, listCrear.get(i));
            }
        } catch (Exception e) {
            System.out.println("AdministrarEvalVigConvocatorias.crear : " + e.toString());
        }
    }

    @Override
    public void borrar(List<Evalvigconvocatorias> listBorrar) {
        try {
            for (int i = 0; i < listBorrar.size(); i++) {
                persistenciaevalvigconv.borrar(em, listBorrar.get(i));
            }
        } catch (Exception e) {
            System.out.println("AdministrarEvalVigConvocatorias.borrar : " + e.toString());
        }

    }

    @Override
    public void editar(List<Evalvigconvocatorias> listModificar) {
        try {
            for (int i = 0; i < listModificar.size(); i++) {
                persistenciaevalvigconv.editar(em, listModificar.get(i));
            }
        } catch (Exception e) {
            System.out.println("AdministrarEvalVigConvocatorias.editar : " + e.toString());
        }
    }

    @Override
    public List<Evalvigconvocatorias> buscarEvalVigConvocatorias() {
        List<Evalvigconvocatorias> listevalvigconv = persistenciaevalvigconv.consultarEvalConvocatorias(em);
        return listevalvigconv;
    }

}