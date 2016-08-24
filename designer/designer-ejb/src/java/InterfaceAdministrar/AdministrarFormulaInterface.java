package InterfaceAdministrar;

import Entidades.Formulas;
import java.math.BigInteger;
import java.util.List;

public interface AdministrarFormulaInterface {

    /**
     * Método encargado de obtener el Entity Manager el cual tiene asociado la
     * sesion del usuario que utiliza el aplicativo.
     *
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);

    public List<Formulas> formulas();

    public void modificar(List<Formulas> listFormulasModificadas);

    public void borrar(Formulas formula);

    public void crear(Formulas formula);

    public void clonarFormula(String nombreCortoOrigen, String nombreCortoClon, String nombreLargoClon, String observacionClon);

    public void operandoFormula(BigInteger secFormula);

    public Formulas buscarFormulaSecuencia(BigInteger secuencia);
}
