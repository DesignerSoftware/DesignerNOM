package Entidades;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "FORMULAS")
public class Formulas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Basic(optional = false)
    @Column(name = "NOMBRECORTO")
    private String nombrecorto;
    @Basic(optional = false)
    @Column(name = "NOMBRELARGO")
    private String nombrelargo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "TIPO")
    private String tipo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8)
    @Column(name = "ESTADO")
    private String estado;
    @Size(max = 2000)
    @Column(name = "OBSERVACIONES")
    private String observaciones;
    @Size(max = 1)
    @Column(name = "FUNDAMENTAL")
    private String fundamental;
    @Size(max = 1)
    @Column(name = "PERIODICIDADINDEPENDIENTE")
    private String periodicidadindependiente;
    @Transient
    private boolean periodicidadFormula;
    @Transient
    private String nombresFormula;

    public Formulas() {
    }

    public Formulas(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public Formulas(BigInteger secuencia, String nombrecorto, String nombrelargo, String tipo, String estado) {
        this.secuencia = secuencia;
        this.nombrecorto = nombrecorto;
        this.nombrelargo = nombrelargo;
        this.tipo = tipo;
        this.estado = estado;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public String getNombrecorto() {
        return nombrecorto;
    }

    public void setNombrecorto(String nombrecorto) {
        if (nombrecorto != null) {
            this.nombrecorto = nombrecorto.toUpperCase();
        } else {
            this.nombrecorto = nombrecorto;
        }
    }

    public String getNombrelargo() {
        return nombrelargo;
    }

    public void setNombrelargo(String nombrelargo) {
        if (nombrelargo != null) {
            this.nombrelargo = nombrelargo.toUpperCase();
        } else {
            this.nombrelargo = nombrelargo;
        }
    }

    public String getTipo() {
        if (tipo == null) {
            tipo = "FINAL";
        }
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getEstado() {
        if (estado == null) {
            estado = "ACTIVO";
        }
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getObservaciones() {
        if (observaciones == null) {
            observaciones = " ";
        }
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        if (observaciones != null) {
            this.observaciones = observaciones.toUpperCase();
        } else {
            this.observaciones = observaciones;
        }
    }

    public String getFundamental() {
        return fundamental;
    }

    public void setFundamental(String fundamental) {
        this.fundamental = fundamental;
    }

    public String getPeriodicidadindependiente() {
        return periodicidadindependiente;
    }

    public void setPeriodicidadindependiente(String periodicidadindependiente) {
        this.periodicidadindependiente = periodicidadindependiente;
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
        if (!(object instanceof Formulas)) {
            return false;
        }
        Formulas other = (Formulas) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Formulas[ secuencia=" + secuencia + " ]";
    }

    public boolean isPeriodicidadFormula() {
        if (periodicidadFormula == false) {
            if (periodicidadindependiente == null) {
                periodicidadFormula = false;
            } else {
                if (periodicidadindependiente.equalsIgnoreCase("S")) {
                    periodicidadFormula = true;
                } else if (periodicidadindependiente.equalsIgnoreCase("N")) {
                    periodicidadFormula = false;
                }
            }
        }
        return periodicidadFormula;
    }

    public void setPeriodicidadFormula(boolean periodicidadFormula) {
        this.periodicidadFormula = periodicidadFormula;
    }

    public String getNombresFormula() {
        if (nombresFormula == null) {
            if (nombrecorto != null && nombrelargo != null) {
                nombresFormula = nombrecorto + " - " + nombrelargo;
            }
        }
        return nombresFormula;
    }

    public void setNombresFormula(String nombresFormula) {
        this.nombresFormula = nombresFormula;
    }
}
