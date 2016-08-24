/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Administrar;

import Entidades.Departamentos;
import Entidades.Paises;
import InterfaceAdministrar.AdministrarDepartamentosInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaDepartamentosInterface;
import InterfacePersistencia.PersistenciaPaisesInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;

/**
 * Clase Stateful. <br>
 * Clase encargada de realizar las operaciones lógicas para la pantalla
 * 'Departamentos'.
 *
 * @author betelgeuse
 */
@Stateful
public class AdministrarDepartamentos implements AdministrarDepartamentosInterface {

    //--------------------------------------------------------------------------
    //ATRIBUTOS
    //--------------------------------------------------------------------------    
    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia
     * 'persistenciaDepartamentos'.
     */
    @EJB
    PersistenciaDepartamentosInterface persistenciaDepartamentos;
    @EJB
    PersistenciaPaisesInterface persistenciaPaises;
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

    public void modificarDepartamentos(List<Departamentos> listaDepartamentos) {
        for (int i = 0; i < listaDepartamentos.size(); i++) {
            System.out.println("Administrar Modificando...");
            persistenciaDepartamentos.editar(em,listaDepartamentos.get(i));
        }
    }

    public void borrarDepartamentos(List<Departamentos> listaDepartamentos) {
        for (int i = 0; i < listaDepartamentos.size(); i++) {
            System.out.println("Administrar Borrando...");
            persistenciaDepartamentos.borrar(em,listaDepartamentos.get(i));
        }
    }

    public void crearDepartamentos(List<Departamentos> listaDepartamentos) {
        for (int i = 0; i < listaDepartamentos.size(); i++) {
            System.out.println("Administrar Creando...");
            persistenciaDepartamentos.crear(em,listaDepartamentos.get(i));
        }
    }

    public List<Departamentos> consultarDepartamentos() {
        List<Departamentos> listaDepartamentos;
        listaDepartamentos = persistenciaDepartamentos.consultarDepartamentos(em);
        return listaDepartamentos;
    }

    public Departamentos consultarTipoIndicador(BigInteger secMotivoDemanda) {
        Departamentos tiposIndicadores;
        tiposIndicadores = persistenciaDepartamentos.consultarDepartamento(em,secMotivoDemanda);
        return tiposIndicadores;
    }

    public List<Paises> consultarLOVPaises() {
        List<Paises> listLOVPaises;
        listLOVPaises = persistenciaPaises.consultarPaises(em);
        return listLOVPaises;
    }

    public BigInteger contarBienProgramacionesDepartamento(BigInteger secuenciaVigenciasIndicadores) {
        BigInteger contarBienProgramacionesDepartamento = null;

        try {
            contarBienProgramacionesDepartamento = persistenciaDepartamentos.contarBienProgramacionesDepartamento(em,secuenciaVigenciasIndicadores);
        } catch (Exception e) {
            System.err.println("ERROR AdministrarDepartamenos contarBienProgramacionesDepartamento ERROR :" + e);
        } finally {
            return contarBienProgramacionesDepartamento;
        }
    }

    public BigInteger contarCapModulosDepartamento(BigInteger secuenciaVigenciasIndicadores) {
        BigInteger contarBienProgramacionesDepartamento = null;
        try {
            contarBienProgramacionesDepartamento = persistenciaDepartamentos.contarCapModulosDepartamento(em,secuenciaVigenciasIndicadores);
        } catch (Exception e) {
            System.err.println("ERROR AdministrarDepartamenos contarCapModulosDepartamento ERROR :" + e);
        } finally {
            return contarBienProgramacionesDepartamento;
        }
    }

    public BigInteger contarSoAccidentesMedicosDepartamento(BigInteger secuenciaVigenciasIndicadores) {
        BigInteger contarBienProgramacionesDepartamento = null;
        try {
            contarBienProgramacionesDepartamento = persistenciaDepartamentos.contarSoAccidentesMedicosDepartamento(em,secuenciaVigenciasIndicadores);
        } catch (Exception e) {
            System.err.println("ERROR AdministrarDepartamenos contarSoAccidentesMedicosDepartamento ERROR :" + e);
        } finally {
            return contarBienProgramacionesDepartamento;
        }
    }

    public BigInteger contarCiudadesDepartamento(BigInteger secuenciaVigenciasIndicadores) {
        BigInteger contarBienProgramacionesDepartamento = null;
        try {
            contarBienProgramacionesDepartamento = persistenciaDepartamentos.contarCiudadesDepartamento(em,secuenciaVigenciasIndicadores);
        } catch (Exception e) {
            System.err.println("ERROR AdministrarDepartamenos contarCiudadesDepartamento ERROR :" + e);
        } finally {
            return contarBienProgramacionesDepartamento;
        }
    }

}
