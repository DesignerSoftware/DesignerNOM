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
@Table(name = "CLAVES_SAP")
public class ClavesSap implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Size(max = 2)
    @Column(name = "CLAVE")
    private String clave;
    @Size(max = 1)
    @Column(name = "CLASIFICACION")
    private String clasificacion;
    @Size(max = 1)
    @Column(name = "NATURALEZA")
    private String naturaleza;    
    @JoinColumn(name = "CLAVEAJUSTE", referencedColumnName = "SECUENCIA")
    private ClavesSap claveAjuste;
    @Transient
    private String clasificacionTransient;
    @Transient
    private String naturalezaTransient;

    public ClavesSap() {
    }

    public ClavesSap(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        if (clave != null) {
            this.clave = clave.toUpperCase();
        } else {
            this.clave = clave;
        }
    }

    public String getClasificacion() {
        return clasificacion;
    }

    public void setClasificacion(String clasificacion) {
        this.clasificacion = clasificacion;
    }

    public String getNaturaleza() {
        return naturaleza;
    }

    public void setNaturaleza(String naturaleza) {
        this.naturaleza = naturaleza;
    }

    public ClavesSap getClaveAjuste() {
        return claveAjuste;
    }

    public void setClaveAjuste(ClavesSap claveAjuste) {
        this.claveAjuste = claveAjuste;
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
        if (!(object instanceof ClavesSap)) {
            return false;
        }
        ClavesSap other = (ClavesSap) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.ClavesSap[ secuencia=" + secuencia + " ]";
    }

    public String getClasificacionTransient() {
        if (clasificacion == null) {
            clasificacionTransient = null;
        } else if (clasificacion.equalsIgnoreCase("D")) {
            clasificacionTransient = "DEUDORES";
        } else if (clasificacion.equalsIgnoreCase("S")) {
            clasificacionTransient = "STOCKS";
        } else if (clasificacion.equalsIgnoreCase("K")) {
            clasificacionTransient = "ACREEDOR";
        } else if (clasificacion.equalsIgnoreCase("A")) {
            clasificacionTransient = "ACTVOS FIJOS";
        } else if (clasificacion.equalsIgnoreCase("M")) {
            clasificacionTransient = "INVENTARIOS";
        }
        return clasificacionTransient;
    }

    public void setClasificacionTransient(String clasificacionTransient) {
        if (clasificacionTransient.equals(" ")) {
            clasificacion = null;
        } else if (clasificacionTransient.equalsIgnoreCase("DEUDORES")) {
            clasificacion = "D";
        } else if (clasificacionTransient.equalsIgnoreCase("STOCKS")) {
            clasificacion = "S";
        } else if (clasificacionTransient.equalsIgnoreCase("ACREEDOR")) {
            clasificacion = "K";
        } else if (clasificacionTransient.equalsIgnoreCase("ACTVOS FIJOS")) {
            clasificacion = "A";
        } else if (clasificacionTransient.equalsIgnoreCase("INVENTARIOS")) {
            clasificacion = "M";
        }
        this.clasificacionTransient = clasificacionTransient;
    }

    public String getNaturalezaTransient() {
        if (naturaleza == null) {
            naturalezaTransient = null;
        } else if (naturaleza.equalsIgnoreCase("D")) {
            naturalezaTransient = "DEBITO";
        } else if (naturaleza.equalsIgnoreCase("C")) {
            naturalezaTransient = "CREDITO";
        }
        return naturalezaTransient;
    }

    public void setNaturalezaTransient(String naturalezaTransient) {
        if (naturalezaTransient.equals(" ")) {
            naturaleza = null;
        } else if (naturalezaTransient.equalsIgnoreCase("DEBITO")) {
            naturaleza = "D";
        } else if (naturalezaTransient.equalsIgnoreCase("CREDITO")) {
            naturaleza = "C";
        }
        this.naturalezaTransient = naturalezaTransient;
    }

}
