package InterfaceAdministrar;

import Entidades.ClasesRiesgos;
import Entidades.Empleados;
import Entidades.VigenciasCargos;
import Entidades.VwTiposEmpleados;
import java.math.BigInteger;
import java.util.List;

public interface AdministrarVigenciasCargosInterface {

    /**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);

    public List<VigenciasCargos> consultarTodo();

    public VigenciasCargos consultarPorSecuencia(BigInteger secuenciaVC);

    public List<VigenciasCargos> vigenciasEmpleado(BigInteger secEmpleado);

    public void editarVigenciaCargo(VigenciasCargos vigencia);

    public void modificarVC(List<VigenciasCargos> listVCModificadas);

    public void borrarVC(VigenciasCargos vigenciasCargos);

    public void crearVC(VigenciasCargos vigenciasCargos);

    public Empleados buscarEmpleado(BigInteger secuencia);

    public void salir();

    public List<VwTiposEmpleados> FiltrarTipoTrabajador();
    
    public List<ClasesRiesgos> lovClasesRiesgos();
}
