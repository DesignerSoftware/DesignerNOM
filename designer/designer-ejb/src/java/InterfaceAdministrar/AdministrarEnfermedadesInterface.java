/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Enfermedades;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author user
 */
@Local
public interface AdministrarEnfermedadesInterface {

    public String modificarEnfermedades(Enfermedades enfermedad);

    public String borrarEnfermedades(Enfermedades enfermedad);

    public String crearEnfermedades(Enfermedades enfermedad);

    public Enfermedades consultarEnfermedad(BigInteger secEnfermedades);

    public List<Enfermedades> consultarEnfermedades();

    public BigInteger verificarAusentimos(BigInteger secEnfermedades);

    public BigInteger verificarDetallesLicencias(BigInteger secEnfermedades);

    public BigInteger verificarEnfermedadesPadecidas(BigInteger secEnfermedades);

    public BigInteger verificarSoAusentismos(BigInteger secEnfermedades);

    public BigInteger verificarSoRevisionesSistemas(BigInteger secEnfermedades);

    public void obtenerConexion(String idSesion);
}
