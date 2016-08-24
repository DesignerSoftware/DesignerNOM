/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Causasausentismos;
import Entidades.Clasesausentismos;
import Entidades.Diagnosticoscategorias;
import Entidades.Empleados;
import Entidades.EnfermeadadesProfesionales;
import Entidades.Enfermedades;
import Entidades.Ibcs;
import Entidades.Soaccidentes;
import Entidades.Soausentismos;
import Entidades.SoausentismosProrrogas;
import Entidades.Terceros;
import Entidades.Tiposausentismos;
import java.math.BigInteger;
import java.util.List;

public interface AdministrarSoausentismosInterface {

    /**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
    
    public void borrarAusentismos(Soausentismos ausentismos);

    public void crearAusentismos(Soausentismos ausentismos);

    public void modificarAusentismos(List<Soausentismos> listaAusentismosModificar);

    public List<Soausentismos> ausentismosEmpleado(BigInteger secuenciaEmpleado);

    public List<Empleados> lovEmpleados();

    public List<Tiposausentismos> lovTiposAusentismos();

    public List<Clasesausentismos> lovClasesAusentismos();

    public List<Causasausentismos> lovCausasAusentismos();

    public List<Ibcs> empleadosIBCS(BigInteger secuenciaEmpleado);

    public List<EnfermeadadesProfesionales> empleadosEP(BigInteger secuenciaEmpleado);

    public List<Soaccidentes> lovAccidentes(BigInteger secuenciaEmpleado);

    public List<Terceros> lovTerceros();

    public List<Diagnosticoscategorias> lovDiagnosticos();

    public List<Enfermedades> enfermedades();

    public String mostrarProrroga(BigInteger secuenciaProrroga);

    public String mostrarRelacion(BigInteger secuenciaAusentismo);

    public List<Soausentismos> lovProrrogas(BigInteger secEmpleado, BigInteger secuenciaCausa, BigInteger secuenciaAusentismo);
}
