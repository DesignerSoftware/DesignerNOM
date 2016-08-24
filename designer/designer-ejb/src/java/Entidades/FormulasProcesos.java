package Entidades;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author PROYECTO01
 */
@Entity
@Table(name = "FORMULASPROCESOS")
public class FormulasProcesos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Size(max = 1)
    @Column(name = "PERIODICIDADINDEPENDIENTE")
    private String periodicidadindependiente;
    @JoinColumn(name = "PROCESO", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Procesos proceso;
    @JoinColumn(name = "FORMULA", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Formulas formula;
    @Transient
    private boolean checkPeriodicidad;
    @Transient
    private String strPeriodicidad;

    public FormulasProcesos() {
    }

    public FormulasProcesos(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public String getPeriodicidadindependiente() {
        if (periodicidadindependiente == null) {
            periodicidadindependiente = "N";
        }
        return periodicidadindependiente;
    }

    public void setPeriodicidadindependiente(String periodicidadindependiente) {
        this.periodicidadindependiente = periodicidadindependiente;
    }

    public Procesos getProceso() {
        return proceso;
    }

    public void setProceso(Procesos proceso) {
        this.proceso = proceso;
    }

    public Formulas getFormula() {
        return formula;
    }

    public void setFormula(Formulas formula) {
        this.formula = formula;
    }

    public boolean isCheckPeriodicidad() {
        getPeriodicidadindependiente();
        if (periodicidadindependiente == null || periodicidadindependiente.equalsIgnoreCase("N")) {
            checkPeriodicidad = false;
        } else {
            checkPeriodicidad = true;
        }
        return checkPeriodicidad;
    }

    public void setCheckPeriodicidad(boolean checkPeriodicidad) {
        if (checkPeriodicidad == false) {
            periodicidadindependiente = "N";
        } else {
            periodicidadindependiente = "S";
        }
        this.checkPeriodicidad = checkPeriodicidad;
    }

    public String getStrPeriodicidad() {
        if (periodicidadindependiente == null || periodicidadindependiente.equalsIgnoreCase("N")) {
            strPeriodicidad = "NO";
        } else {
            strPeriodicidad = "SI";
        }
        return strPeriodicidad;
    }

    public void setStrPeriodicidad(String strPeriodicidad) {
        this.strPeriodicidad = strPeriodicidad;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (secuencia != null ? secuencia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FormulasProcesos)) {
            return false;
        }
        FormulasProcesos other = (FormulasProcesos) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.FormulasProcesos[ secuencia=" + secuencia + " ]";
    }

}
