package ClasesAyuda;

import Entidades.Empleados;
import Entidades.HVHojasDeVida;
import Entidades.Personas;
import java.io.Serializable;

public class EmpleadoIndividualExportar implements Serializable {

    private Empleados empleado;
    private Personas persona;
    private HVHojasDeVida hojaDeVida;

    public EmpleadoIndividualExportar(Empleados empleado, Personas persona, HVHojasDeVida hojaDeVida) {
        this.empleado = empleado;
        this.persona = persona;
        this.hojaDeVida = hojaDeVida;
    }

    public Empleados getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleados empleado) {
        this.empleado = empleado;
    }

    public HVHojasDeVida getHojaDeVida() {
        return hojaDeVida;
    }

    public void setHojaDeVida(HVHojasDeVida hojaDeVida) {
        this.hojaDeVida = hojaDeVida;
    }

    public Personas getPersona() {
        return persona;
    }

    public void setPersona(Personas persona) {
        this.persona = persona;
    }    
    
}
