package Administrar;

import Entidades.ClasesRiesgos;
import Entidades.Empleados;
import Entidades.Papeles;
//import Entidades.VWActualesTiposTrabajadores;
import Entidades.VigenciasCargos;
import Entidades.VwTiposEmpleados;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfaceAdministrar.AdministrarVigenciasCargosInterface;
import InterfacePersistencia.PersistenciaClasesRiesgosInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaEmpresasInterface;
import InterfacePersistencia.PersistenciaPapelesInterface;
import InterfacePersistencia.PersistenciaVigenciasArpsInterface;
//import InterfacePersistencia.PersistenciaVWActualesTiposTrabajadoresInterface;
import InterfacePersistencia.PersistenciaVigenciasCargosInterface;
import InterfacePersistencia.PersistenciaVwTiposEmpleadosInterface;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;

/**
 *
 * @author Hugo Sin y -Felipphe-
 */
@Stateful
public class AdministrarVigenciasCargos implements AdministrarVigenciasCargosInterface {

   private static Logger log = Logger.getLogger(AdministrarVigenciasCargos.class);

    @EJB
    PersistenciaVigenciasCargosInterface persistenciaVigenciasCargos;
    @EJB
    PersistenciaEmpleadoInterface persistenciaEmpleado;
    @EJB
    PersistenciaVwTiposEmpleadosInterface persistenciaTiposEmpleados;
    @EJB
    PersistenciaClasesRiesgosInterface persistenciaClasesRiesgos;
    @EJB
    PersistenciaVigenciasArpsInterface persistenciaVigenciaArp;
    @EJB
    PersistenciaPapelesInterface persistenciaPapeles;
    @EJB
    PersistenciaEmpresasInterface persistenciaEmpresas;
    //PersistenciaVWActualesTiposTrabajadoresInterface persistenciaVWActualesTiposTrabajadores;
    /**
     * Enterprise JavaBean.<br>
     * Atributo que representa todo lo referente a la conexión del usuario que
     * está usando el aplicativo.
     */
    @EJB
    AdministrarSesionesInterface administrarSesiones;

    private List<VigenciasCargos> vigenciasCargos;
    public List<VwTiposEmpleados> tipoEmpleadoLista;
    private VigenciasCargos vc;
    private Empleados empleado;
    private SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
    private EntityManager em;

    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    /*
     public AdministrarVigenciasCargos() {
     persistenciaVigenciasCargos = new PersistenciaVigenciasCargos();
     }
     */

    @Override
    public List<VigenciasCargos> consultarTodo() {
        try {
            vigenciasCargos = persistenciaVigenciasCargos.buscarVigenciasCargos(em);
        } catch (Exception e) {
            vigenciasCargos = null;
        }
        return vigenciasCargos;
    }

    @Override
    public VigenciasCargos consultarPorSecuencia(BigInteger secuenciaVC) {
        try {
            vc = persistenciaVigenciasCargos.buscarVigenciaCargo(em, secuenciaVC);
        } catch (Exception e) {
            vc = null;
        }
        return vc;
    }

    @Override
    public List<VigenciasCargos> vigenciasEmpleado(BigInteger secEmpleado) {
        try {
            vigenciasCargos = persistenciaVigenciasCargos.buscarVigenciasCargosEmpleado(em, secEmpleado);
        } catch (Exception e) {
            vigenciasCargos = null;
        }
        return vigenciasCargos;
    }

    @Override
    public void editarVigenciaCargo(VigenciasCargos vigencia) {
        try {
            log.warn("administrarEmplVig editarVig: editar Vigencia = " + vigencia.getSecuencia());
            persistenciaVigenciasCargos.editar(em, vigencia);
        } catch (Exception ex) {
            log.warn("administrarEmplVig editarVig: FALLO EN EL EDITAR");
        }
    }

    public void modificarVC(List<VigenciasCargos> listVCModificadas) {
        for (int i = 0; i < listVCModificadas.size(); i++) {
            log.warn("Modificando...");
            if (listVCModificadas.get(i).getEmpleadojefe() != null && listVCModificadas.get(i).getEmpleadojefe().getSecuencia() == null) {
                listVCModificadas.get(i).setEmpleadojefe(null);
                vc = listVCModificadas.get(i);
                persistenciaVigenciasCargos.editar(em, vc);
            } else {
                vc = listVCModificadas.get(i);
                persistenciaVigenciasCargos.editar(em, vc);
            }

        }
    }

    public void borrarVC(VigenciasCargos vigenciasCargos) {
        try {
            persistenciaVigenciasCargos.borrar(em, vigenciasCargos);
        } catch (Exception e) {
            log.warn("Error" + e);
        }

    }

    public void crearVC(VigenciasCargos vigenciasCargos) {
        persistenciaVigenciasCargos.crear(em, vigenciasCargos);
    }

    public Empleados buscarEmpleado(BigInteger secuencia) {
        try {
            empleado = persistenciaEmpleado.buscarEmpleadoSecuencia(em, secuencia);
            return empleado;
        } catch (Exception e) {
            empleado = null;
            return empleado;
        }
    }

    public List<VwTiposEmpleados> FiltrarTipoTrabajador() {

        try {
            //tipoEmpleadoLista = persistenciaVWActualesTiposTrabajadores.FiltrarTipoTrabajador(em, "ACTIVO");
            tipoEmpleadoLista = persistenciaTiposEmpleados.buscarTiposEmpleadosPorTipo(em, "ACTIVO");
            return tipoEmpleadoLista;
        } catch (Exception e) {
            tipoEmpleadoLista = null;
            return tipoEmpleadoLista;
        }
    }

    @Remove
    public void salir() {
        vigenciasCargos = null;
    }

    @Override
    public List<ClasesRiesgos> lovClasesRiesgos() {
        try {
            List<ClasesRiesgos> listaClasesRiesgos = persistenciaClasesRiesgos.consultarListaClasesRiesgos(em);
            return listaClasesRiesgos;
        } catch (Exception e) {
            log.warn("error en AdministrarVigenciasCargos.lovClasesRiesgos : " + e.toString());
            return null;
        }
    }

    @Override
    public List<Papeles> lovPapeles(BigInteger secEmpresa) {
        try {
            log.warn("secuencia de la empresa en lovPapeles : " + secEmpresa);
            List<Papeles> lovPapeles  = persistenciaPapeles.consultarPapelesEmpresa(em, secEmpresa);
            return lovPapeles;
        } catch (Exception e) {
            log.warn("error en AdministrarVigenciasCargos.lovPapeles: " + e.toString());
            return null;
        }
    }

    @Override
    public BigDecimal consultarEmpresaPorEmpl(BigInteger secEmpl) {
       try{
        BigDecimal sec = persistenciaEmpresas.consultarEmpresaPorEmpleado(em, secEmpl);
        return sec;
       }catch(Exception e){
           log.warn("error en AdministrarVigenciasCargos.consultarEmpresaPorEmpl : " + e.toString());
           return null;
       }
    }
}
