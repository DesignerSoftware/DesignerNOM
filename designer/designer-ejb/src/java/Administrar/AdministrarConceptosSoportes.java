/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Conceptos;
import Entidades.ConceptosSoportes;
import Entidades.Operandos;
import InterfaceAdministrar.AdministrarConceptosSoportesInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaConceptosInterface;
import InterfacePersistencia.PersistenciaConceptosSoportesInterface;
import InterfacePersistencia.PersistenciaOperandosInterface;
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
public class AdministrarConceptosSoportes implements AdministrarConceptosSoportesInterface {

   private static Logger log = Logger.getLogger(AdministrarConceptosSoportes.class);
//--------------------------------------------------------------------------
    //ATRIBUTOS
    //--------------------------------------------------------------------------    

    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia
     * 'persistenciaConceptosSoportes'.
     */
    @EJB
    PersistenciaConceptosSoportesInterface persistenciaConceptosSoportes;
    @EJB
    PersistenciaOperandosInterface persistenciaOperandos;
    @EJB
    PersistenciaConceptosInterface persistenciaConceptos;
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

    public void modificarConceptosSoportes(List<ConceptosSoportes> listaConceptosSoportes) {
        for (int i = 0; i < listaConceptosSoportes.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaConceptosSoportes.editar(em, listaConceptosSoportes.get(i));
        }
    }

    public void borrarConceptosSoportes(List<ConceptosSoportes> listaConceptosSoportes) {
        for (int i = 0; i < listaConceptosSoportes.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaConceptosSoportes.borrar(em, listaConceptosSoportes.get(i));
        }
    }

    public void crearConceptosSoportes(List<ConceptosSoportes> listaConceptosSoportes) {
        for (int i = 0; i < listaConceptosSoportes.size(); i++) {
            persistenciaConceptosSoportes.crear(em, listaConceptosSoportes.get(i));
        }
    }

    @Override
    public List<ConceptosSoportes> consultarConceptosSoportes() {
        List<ConceptosSoportes> listaConceptosSoportes;
        listaConceptosSoportes = persistenciaConceptosSoportes.consultarConceptosSoportes(em);
        return listaConceptosSoportes;
    }

    public List<Operandos> consultarLOVOperandos() {
        List<Operandos> listLOVOperandos;
        listLOVOperandos = persistenciaOperandos.buscarOperandos(em);
        return listLOVOperandos;
    }

    @Override
    public List<Operandos> consultarLOVOperandosPorConcepto(BigInteger secConceptoSoporte) {
        List<Operandos> listLOVOperandos;
        listLOVOperandos = persistenciaOperandos.operandoPorConceptoSoporte(em, secConceptoSoporte);
        return listLOVOperandos;
    }

    public List<Conceptos> consultarLOVConceptos() {
        List<Conceptos> listLOVConceptos;
        listLOVConceptos = persistenciaConceptos.buscarConceptos(em);
        return listLOVConceptos;
    }

    public BigInteger contarConceptosOperandos(BigInteger concepto, BigInteger operando) {
        BigInteger contarConceptosOperandos = persistenciaConceptosSoportes.consultarConceptoSoporteConceptoOperador(em, concepto, operando);
        return contarConceptosOperandos;
    }
}
