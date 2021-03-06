package InterfaceAdministrar;

import Entidades.ConfiguracionCorreo;
import Entidades.Empleados;
import Entidades.EnvioCorreos;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

/**
 * Interface encargada de determinar las operaciones lógicas necesarias para la
 * pantalla 'EnvioCorreos'.
 *
 */
public interface AdministrarEnvioCorreosInterface {

    public void obtenerConexion(String idSesion);

    public List<Empleados> correoCodigoEmpleado(BigDecimal emplDesde, BigDecimal emplHasta);
    
    public List<String> correos();

    public boolean comprobarConfigCorreo(BigInteger secuenciaEmpresa);

    /**
     *
     * @param secEmpresa
     * @param destinatario
     * @param asunto
     * @param mensaje
     * @param pathAdjunto
     * @param paramResultado
     * @return
     */
    public boolean enviarCorreo(BigInteger secEmpresa, String destinatario, String asunto, String mensaje, String pathAdjunto, String[] paramResultado);

    public BigInteger empresaAsociada();

    public ConfiguracionCorreo consultarRemitente(BigInteger secEmpresa);
    
    public void insertarRegistroEnvios(EnvioCorreos envioCorreo);
}
