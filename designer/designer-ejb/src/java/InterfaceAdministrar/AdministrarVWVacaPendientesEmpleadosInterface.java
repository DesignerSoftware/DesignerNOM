package InterfaceAdministrar;

import Entidades.Empleados;
import Entidades.VWVacaPendientesEmpleados;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

/**
 *
 * @author user
 */
public interface AdministrarVWVacaPendientesEmpleadosInterface {

    /**
     * Método encargado de obtener el Entity Manager el cual tiene asociado la
     * sesion del usuario que utiliza el aplicativo.
     *
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);

    public void crearVacaPendiente(VWVacaPendientesEmpleados vaca);

    public void editarVacaPendiente(VWVacaPendientesEmpleados vaca);

    public void borrarVacaPendiente(VWVacaPendientesEmpleados vaca);

    public List<VWVacaPendientesEmpleados> vacaPendientesPendientes(Empleados empl, Date fechaingreso);

    public List<VWVacaPendientesEmpleados> vacaPendientesDisfrutadas(Empleados empl, Date fechaingreso);

    public Empleados obtenerEmpleado(BigInteger secuencia);

    public BigDecimal diasProvisionadosEmpleado(Empleados empl);

    public Date obtenerFechaFinalContratacionEmpleado(BigInteger secEmpleado);

    public List<VWVacaPendientesEmpleados> vacaPendientesPendientesAnterioresContratos(Empleados empl);

    public List<VWVacaPendientesEmpleados> vacaPendientesDisfrutadasAnterioresContratos(Empleados empl);

}
