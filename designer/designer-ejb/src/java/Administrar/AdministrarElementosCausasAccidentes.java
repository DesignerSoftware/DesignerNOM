/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Administrar;

import Entidades.ElementosCausasAccidentes;
import InterfaceAdministrar.AdministrarElementosCausasAccidentesInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaElementosCausasAccidentesInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;

/**
 * Clase Stateful. <br>
 * Clase encargada de realizar las operaciones lógicas para la pantalla
 * 'ElementosCausasAccidentes'.
 *
 * @author betelgeuse
 */
@Stateful
public class AdministrarElementosCausasAccidentes implements AdministrarElementosCausasAccidentesInterface {

    //--------------------------------------------------------------------------
    //ATRIBUTOS
    //--------------------------------------------------------------------------    
    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia
     * 'persistenciaElementosCausasAccidentes'.
     */
    @EJB
    PersistenciaElementosCausasAccidentesInterface persistenciaElementosCausasAccidentes;
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
    public void modificarElementosCausasAccidentes(List<ElementosCausasAccidentes> listaElementosCausasAccidentes) {
        ElementosCausasAccidentes elementosCausasAccidentesSeleccionada;
        for (int i = 0; i < listaElementosCausasAccidentes.size(); i++) {
            System.out.println("Administrar Modificando...");
            elementosCausasAccidentesSeleccionada = listaElementosCausasAccidentes.get(i);
            persistenciaElementosCausasAccidentes.editar(em,elementosCausasAccidentesSeleccionada);
        }
    }

    @Override
    public void borrarElementosCausasAccidentes(List<ElementosCausasAccidentes> listaElementosCausasAccidentes) {
        for (int i = 0; i < listaElementosCausasAccidentes.size(); i++) {
            System.out.println("Borrando...");
            persistenciaElementosCausasAccidentes.borrar(em,listaElementosCausasAccidentes.get(i));
        }
    }

    @Override
    public void crearElementosCausasAccidentes(List<ElementosCausasAccidentes> listaElementosCausasAccidentes) {
        for (int i = 0; i < listaElementosCausasAccidentes.size(); i++) {
            System.out.println("Creando...");
            persistenciaElementosCausasAccidentes.crear(em,listaElementosCausasAccidentes.get(i));
        }
    }

    @Override
    public List<ElementosCausasAccidentes> consultarElementosCausasAccidentes() {
        List<ElementosCausasAccidentes> listElementosCausasAccidentes = persistenciaElementosCausasAccidentes.buscarElementosCausasAccidentes(em);
        return listElementosCausasAccidentes;
    }

    @Override
    public ElementosCausasAccidentes consultarElementoCausaAccidente(BigInteger secElementosCausasAccidentes) {
        ElementosCausasAccidentes elementosCausasAccidentes = persistenciaElementosCausasAccidentes.buscarElementoCausaAccidente(em,secElementosCausasAccidentes);
        return elementosCausasAccidentes;
    }

    @Override
    public BigInteger contarSoAccidentesCausa(BigInteger secTiposAuxilios) {
        BigInteger contadorSoAccidentes = null;
        try {
            contadorSoAccidentes = persistenciaElementosCausasAccidentes.contadorSoAccidentes(em,secTiposAuxilios);
        } catch (Exception e) {
            System.err.println("ERROR ADMINISTRARELEMENTOSCAUSASACCIDENTES contadorSoAccidentes ERROR :" + e);
        } finally {
            return contadorSoAccidentes;
        }
    }

    @Override
    public BigInteger contarSoAccidentesMedicosElementoCausaAccidente(BigInteger secTiposAuxilios) {
        BigInteger contadorSoAccidentesMedicos = null;
        try {
            contadorSoAccidentesMedicos = persistenciaElementosCausasAccidentes.contadorSoAccidentesMedicos(em,secTiposAuxilios);
        } catch (Exception e) {
            System.err.println("ERROR ADMINISTRARELEMENTOSCAUSASACCIDENTES contadorSoAccidentesMedicos ERROR :" + e);
        } finally {
            return contadorSoAccidentesMedicos;
        }
    }

    @Override
    public BigInteger contarSoIndicadoresFrElementoCausaAccidente(BigInteger secTiposAuxilios) {
        BigInteger contadorSoIndicadoresFr = null;
        try {
            contadorSoIndicadoresFr = persistenciaElementosCausasAccidentes.contadorSoIndicadoresFr(em,secTiposAuxilios);
        } catch (Exception e) {
            System.err.println("ERROR ADMINISTRARELEMENTOSCAUSASACCIDENTES contadorSoIndicadoresFr ERROR :" + e);
        } finally {
            return contadorSoIndicadoresFr;
        }
    }
}
