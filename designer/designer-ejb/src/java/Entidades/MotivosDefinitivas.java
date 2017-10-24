package Entidades;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 *
 * @author user
 */
@Entity
@Table(name = "MOTIVOSDEFINITIVAS")
public class MotivosDefinitivas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CODIGO")
    private Integer codigo;
    @Column(name = "NOMBRE")
    private String nombre;
    @Column(name = "RETIRO")
    private String retiro;
    @Column(name = "CAMBIOREGIMEN")
    private String cambioregimen;
//    @Column(name = "CATEDRATICOSEMESTRAL")
//    private String catedraticosemestral;
    @Transient
    private String variableRetiro;
    @Transient
    private String variableCambioRegimen;
//    @Transient
//    private String variableCatedratico;

//    public String getVariableCatedratico() {
//        if (variableCatedratico == null) {
//            if (catedraticosemestral == null) {
//                variableCatedratico = " ";
//            } else if (catedraticosemestral.equalsIgnoreCase("S")) {
//                variableCatedratico = "SI";
//            } else if (catedraticosemestral.equalsIgnoreCase("N")) {
//                variableCatedratico = "NO";
//            }
//        }
//        return variableCatedratico;
//    }
//
//    public void setVariableCatedratico(String variableCatedratico) {
//        this.variableCatedratico = variableCatedratico;
//    }

    public String getVariableCambioRegimen() {
        if (variableCambioRegimen == null) {
            if (cambioregimen == null) {
                variableCambioRegimen = " ";
            } else if (cambioregimen.equalsIgnoreCase("S")) {
                variableCambioRegimen = "SI";
            } else if (cambioregimen.equalsIgnoreCase("N")) {
                variableCambioRegimen = "NO";
            }
        }
        return variableCambioRegimen;
    }

    public void setVariableCambioRegimen(String variableCambioRegimen) {
        this.variableCambioRegimen = variableCambioRegimen;
    }

    public String getVariableRetiro() {
        if (variableRetiro == null) {
            if (retiro == null) {
                variableRetiro = " ";
            } else if (retiro.equalsIgnoreCase("S")) {
                variableRetiro = "SI";
            } else if (retiro.equalsIgnoreCase("N")) {
                variableRetiro = "NO";

            }
        }
        return variableRetiro;
    }

    public void setVariableRetiro(String variableRetiro) {
        this.variableRetiro = variableRetiro;
    }

    public MotivosDefinitivas() {
    }

    public MotivosDefinitivas(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public MotivosDefinitivas(BigInteger secuencia, Integer codigo, String nombre) {
        this.secuencia = secuencia;
        this.codigo = codigo;
        this.nombre = nombre;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRetiro() {
        return retiro;
    }

    public void setRetiro(String retiro) {
        this.retiro = retiro;
    }

    public String getCambioregimen() {
        return cambioregimen;
    }

    public void setCambioregimen(String cambioregimen) {
        this.cambioregimen = cambioregimen;
    }

//    public String getCatedraticosemestral() {
//        return catedraticosemestral;
//    }
//
//    public void setCatedraticosemestral(String catedraticosemestral) {
//        this.catedraticosemestral = catedraticosemestral;
//    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (secuencia != null ? secuencia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MotivosDefinitivas)) {
            return false;
        }
        MotivosDefinitivas other = (MotivosDefinitivas) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Motivosdefinitivas[ secuencia=" + secuencia + " ]";
    }

}