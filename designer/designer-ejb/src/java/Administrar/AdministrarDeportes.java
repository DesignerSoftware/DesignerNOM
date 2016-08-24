/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Administrar;

import Entidades.Deportes;
import InterfaceAdministrar.AdministrarDeportesInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaDeportesInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;

/**
 * Clase Stateful. <br>
 * Clase encargada de realizar las operaciones lógicas para la pantalla
 * 'Deportes'.
 *
 * @author betelgeuse
 */
@Stateful
public class AdministrarDeportes implements AdministrarDeportesInterface {

    //--------------------------------------------------------------------------
    //ATRIBUTOS
    //--------------------------------------------------------------------------    

    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia
     * 'persistenciaDeportes'.
     */
    @EJB
    PersistenciaDeportesInterface persistenciaDeportes;
    /**
     * Enterprise JavaBean.<br>
     * Atributo que representa todo lo referente a la conexión del usuario que
     * está usando el aplicativo.
     */
    @EJB
    AdministrarSesionesInterface administrarSesiones;

    private EntityManager em;

    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    //--------------------------------------------------------------------------
    //MÉTODOS
    //--------------------------------------------------------------------------  
    @Override
    public void modificarDeportes(List<Deportes> listDeportesModificadas) {
        for (int i = 0; i < listDeportesModificadas.size(); i++) {
            System.out.println("Administrar Modificando...");
            Deportes deporteSeleccionado = listDeportesModificadas.get(i);
            persistenciaDeportes.editar(em,deporteSeleccionado);
        }
    }

    @Override
    public void borrarDeportes(List<Deportes> listaDeportes) {
        for (int i = 0; i < listaDeportes.size(); i++) {
            System.out.println("Borrando...");
            persistenciaDeportes.borrar(em,listaDeportes.get(i));
        }
    }

    @Override
    public void crearDeportes(List<Deportes> listaDeportes) {
        for (int i = 0; i < listaDeportes.size(); i++) {
            System.out.println("Creando...");
            persistenciaDeportes.crear(em,listaDeportes.get(i));
        }
    }

    @Override
    public List<Deportes> consultarDeportes() {
        List<Deportes> listDeportes = persistenciaDeportes.buscarDeportes(em);
        return listDeportes;
    }

    @Override
    public Deportes consultarDeporte(BigInteger secDeportes) {
        Deportes deportes = persistenciaDeportes.buscarDeporte(em,secDeportes);
        return deportes;
    }

    @Override
    public BigInteger contarVigenciasDeportesDeporte(BigInteger secDeporte) {
        BigInteger verificarBorradoVigenciasDeportes = null;
        try {
            verificarBorradoVigenciasDeportes = persistenciaDeportes.verificarBorradoVigenciasDeportes(em,secDeporte);
        } catch (Exception e) {
            System.err.println("ERROR ADMINISTRARDEPORTES verificarBorradoVigenciasDeportes ERROR :" + e);
        } finally {
            return verificarBorradoVigenciasDeportes;
        }
    }

    @Override
    public BigInteger contarPersonasDeporte(BigInteger secDeporte) {
        BigInteger contadorDeportesPersonas = null;
        try {
            contadorDeportesPersonas = persistenciaDeportes.contadorDeportesPersonas(em,secDeporte);
        } catch (Exception e) {
            System.err.println("ERROR ADMINISTRARDEPORTES contadorDeportesPersonas ERROR :" + e);
        } finally {
            return contadorDeportesPersonas;
        }
    }

    @Override
    public BigInteger contarParametrosInformesDeportes(BigInteger secDeporte) {
        BigInteger contadorParametrosInformes = null;
        try {
            contadorParametrosInformes = persistenciaDeportes.contadorParametrosInformes(em,secDeporte);
        } catch (Exception e) {
            System.err.println("ERROR ADMINISTRARDEPORTES contadorParametrosInformes ERROR :" + e);
        } finally {
            return contadorParametrosInformes;
        }
    }
}
