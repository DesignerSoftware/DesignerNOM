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
@Table(name = "GRUPOSCONCEPTOS")
public class GruposConceptos implements Serializable {

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
    @Basic(optional = false)
    @NotNull
    @Size(max = 50)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Size(max = 1)
    @Column(name = "FUNDAMENTAL")
    private String fundamental;
    @Transient
    private String strCodigo;
    @Transient
    private String estadoFundamental;

    public GruposConceptos() {
    }

    public GruposConceptos(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public GruposConceptos(BigInteger secuencia, Integer codigo, String descripcion) {
        this.secuencia = secuencia;
        this.codigo = codigo;
        this.descripcion = descripcion;
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

    public String getStrCodigo() {
        getCodigo();
        if (codigo != null) {
            if (codigo > 0) {
                strCodigo = String.valueOf(codigo);
            }
        }
        return strCodigo;
    }

    public void setStrCodigo(String strCodigo) {
        if (strCodigo != null) {
            if (!strCodigo.isEmpty()) {
                codigo = Integer.parseInt(strCodigo);
            } else {
                codigo = 0;
            }
        } else {
            codigo = 0;
        }
        this.strCodigo = strCodigo;
    }

    public String getDescripcion() {

        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFundamental() {

        if (fundamental == null) {
            fundamental = "N";
        }

        return fundamental;
    }

    public void setFundamental(String fundamental) {
        this.fundamental = fundamental;
    }

    public String getEstadoFundamental() {
        if (estadoFundamental == null) {
            if (fundamental == null || fundamental.equals("N")) {
                estadoFundamental = "NO ES PERSONALIZABLE";

            } else if (fundamental.equalsIgnoreCase("S")) {
                estadoFundamental = "SI ES PERSONALIZABLE";
            }

        }
        return estadoFundamental;
    }

    public void setEstadoFundamental(String estadoFundamental) {
        this.estadoFundamental = estadoFundamental;
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
        if (!(object instanceof GruposConceptos)) {
            return false;
        }
        GruposConceptos other = (GruposConceptos) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Gruposconceptos[ secuencia=" + secuencia + " ]";
    }
}
