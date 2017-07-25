/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.FormulasContratosEntidades;
import Entidades.Formulascontratos;
import Entidades.TiposEntidades;
import InterfaceAdministrar.AdministrarFormulasContratosEntidadesInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaFormulasContratosEntidadesInterface;
import InterfacePersistencia.PersistenciaFormulasContratosInterface;
import InterfacePersistencia.PersistenciaTiposEntidadesInterface;
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
public class AdministrarFormulasContratosEntidades implements AdministrarFormulasContratosEntidadesInterface {

   private static Logger log = Logger.getLogger(AdministrarFormulasContratosEntidades.class);
//ATRIBUTOS
    //--------------------------------------------------------------------------    

    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia
     * 'persistenciaFormulasContratosEntidades'.
     */
    @EJB
    PersistenciaFormulasContratosEntidadesInterface persistenciaFormulasContratosEntidades;
    @EJB
    PersistenciaTiposEntidadesInterface persistenciaTiposEntidades;
    @EJB
    PersistenciaFormulasContratosInterface persistenciaFormulas;
    /**
     * Enterprise JavaBean.<br>
     * Atributo que representa todo lo referente a la conexión del usuario que
     * está usando el aplicativo.
     */
    @EJB
    AdministrarSesionesInterface administrarSesiones;

    private EntityManager em;
    //--------------------------------------------------------------------------
    //MÉTODOS
    //--------------------------------------------------------------------------

    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    public void modificarFormulasContratosEntidades(List<FormulasContratosEntidades> listaFormulasContratosEntidades) {
        for (int i = 0; i < listaFormulasContratosEntidades.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaFormulasContratosEntidades.editar(em,listaFormulasContratosEntidades.get(i));
        }
    }

    public void borrarFormulasContratosEntidades(List<FormulasContratosEntidades> listaFormulasContratosEntidades) {
        for (int i = 0; i < listaFormulasContratosEntidades.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaFormulasContratosEntidades.borrar(em,listaFormulasContratosEntidades.get(i));
        }
    }

    public void crearFormulasContratosEntidades(List<FormulasContratosEntidades> listaFormulasContratosEntidades) {
        for (int i = 0; i < listaFormulasContratosEntidades.size(); i++) {
            persistenciaFormulasContratosEntidades.crear(em,listaFormulasContratosEntidades.get(i));
        }
    }

    @Override
    public List<FormulasContratosEntidades> consultarFormulasContratosEntidades() {
        List<FormulasContratosEntidades> LOVTiposEntidades;
        return LOVTiposEntidades = persistenciaFormulasContratosEntidades.consultarFormulasContratosEntidades(em);
    }

    @Override
    public List<FormulasContratosEntidades> consultarFormulasContratosEntidadesPorFormulaContrato(BigInteger secFormulaContrato) {
        List<FormulasContratosEntidades> LOVTiposEntidades;
        return LOVTiposEntidades = persistenciaFormulasContratosEntidades.consultarFormulasContratosEntidadesPorFormulaContrato(em,secFormulaContrato);
    }

    public List<TiposEntidades> consultarLOVTiposEntidades() {
        List<TiposEntidades> LOVTiposEntidades;
        return LOVTiposEntidades = persistenciaTiposEntidades.buscarTiposEntidades(em);
    }

    @Override
    public Formulascontratos consultarFormulaDeFormulasContratosEntidades(BigInteger secFormulaContrato) {
        Formulascontratos formulaConcepto = persistenciaFormulas.formulasContratosParaContratoFormulasContratosEntidades(em,secFormulaContrato);
        return formulaConcepto;
    }

}
