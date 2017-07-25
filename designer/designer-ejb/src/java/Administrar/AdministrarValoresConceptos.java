/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Conceptos;
import Entidades.ValoresConceptos;
import InterfaceAdministrar.AdministrarValoresConceptosInterface;
import InterfacePersistencia.PersistenciaConceptosInterface;
import InterfacePersistencia.PersistenciaValoresConceptosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import InterfaceAdministrar.AdministrarSesionesInterface;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;

/**
 *
 * @author user
 */
@Stateful
public class AdministrarValoresConceptos implements AdministrarValoresConceptosInterface {

   private static Logger log = Logger.getLogger(AdministrarValoresConceptos.class);

    //--------------------------------------------------------------------------
    //ATRIBUTOS
    //--------------------------------------------------------------------------    
    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia
     * 'persistenciaValoresConceptos'.
     */
    @EJB
    PersistenciaValoresConceptosInterface persistenciaValoresConceptos;
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
    
    @Override
    public void modificarValoresConceptos(List<ValoresConceptos> listaValoresConceptos) {
        for (int i = 0; i < listaValoresConceptos.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaValoresConceptos.editar(em, listaValoresConceptos.get(i));
        }
    }

    @Override
    public void borrarValoresConceptos(List<ValoresConceptos> listaValoresConceptos) {
        for (int i = 0; i < listaValoresConceptos.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaValoresConceptos.borrar(em, listaValoresConceptos.get(i));
        }
    }

    @Override
    public void crearValoresConceptos(List<ValoresConceptos> listaValoresConceptos) {
        for (int i = 0; i < listaValoresConceptos.size(); i++) {
            persistenciaValoresConceptos.crear(em, listaValoresConceptos.get(i));
        }
    }

    @Override
    public List<ValoresConceptos> consultarValoresConceptos() {
        List<ValoresConceptos> listaValoresConceptos;
        listaValoresConceptos = persistenciaValoresConceptos.consultarValoresConceptos(em);
        return listaValoresConceptos;
    }

    @Override
    public List<Conceptos> consultarLOVConceptos() {
        List<Conceptos> listLOVConceptos;
        listLOVConceptos = persistenciaConceptos.buscarConceptos(em);
        return listLOVConceptos;
    }

    @Override
    public BigInteger contarConceptoValorConcepto(BigInteger concepto) {
        BigInteger consulta;

        try {
            consulta = persistenciaValoresConceptos.consultarConceptoValorConcepto(em, concepto);
            return consulta;
        } catch (Exception e) {
            log.error("ERROR ADMINISTRARVALORESCONCEPTOS CONSULTARCONCEPTOVALORCONCEPTO ERROR : " + e);
            return null;
        }

    }
}
