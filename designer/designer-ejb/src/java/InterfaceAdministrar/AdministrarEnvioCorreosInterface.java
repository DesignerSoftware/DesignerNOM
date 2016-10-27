package InterfaceAdministrar;

import Entidades.Empleados;
import Entidades.Inforeportes;
import Entidades.envioCorreos;
import java.math.BigInteger;
import java.util.List;

/**
 * Interface encargada de determinar las operaciones lógicas necesarias para la
 * pantalla 'EnvioCorreos'.
 *
 */
public interface AdministrarEnvioCorreosInterface {

    public void obtenerConexion(String idSesion);

    public List<envioCorreos> consultarEnvioCorreos(BigInteger reporte);

    public Inforeportes consultarPorSecuencia(BigInteger envio);

//    public List<Empleados> consultarEmpleados(BigInteger reporte);
    public void editarEnvioCorreos(envioCorreos listaEC);

    public void modificarEC(List<envioCorreos> listECModificadas);

    public void borrarEnvioCorreos(envioCorreos listaEC);
}
