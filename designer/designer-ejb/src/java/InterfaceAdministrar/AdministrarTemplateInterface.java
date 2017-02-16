package InterfaceAdministrar;

import Entidades.ActualUsuario;
import Entidades.DetallesEmpresas;
import Entidades.ParametrosEstructuras;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.ejb.Local;

/**
 *
 * @author Administrador
 */
@Local
public interface AdministrarTemplateInterface {

    public boolean obtenerConexion(String idSesion);

    public ActualUsuario consultarActualUsuario();

    public void cerrarSession(String idSesion);

    public String logoEmpresa();

    public String rutaFotoUsuario();

    public DetallesEmpresas consultarDetalleEmpresaUsuario();

    public ParametrosEstructuras consultarParametrosUsuario();

    public String consultarNombrePerfil();

    public BigDecimal consultarSMLV();

    public BigDecimal consultarAuxTrans();

    public BigDecimal consultarUVT();

    public BigDecimal consultarMinIBC();

    public BigDecimal consultarTopeSegSocial();
}
