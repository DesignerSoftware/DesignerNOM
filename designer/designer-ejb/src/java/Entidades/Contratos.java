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
@Table(name = "CONTRATOS")
public class Contratos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Column(name = "CODIGO")
    private Short codigo;
    @Basic(optional = false)
    @NotNull
    @Size(max = 50)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Size(max = 10)
    @Column(name = "ESTADO")
    private String estado;
    @Lob
    @Size(max = 0)
    @Column(name = "CONVENCION")
    private String convencion;
    @Size(max = 1)
    @Column(name = "RELACIONCONECOPETROL")
    private String relacionconecopetrol;
    @JoinColumn(name = "TIPOCOTIZANTE", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private TiposCotizantes tipocotizante;
    @JoinColumn(name = "CONTRATOPADRE", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Contratos contratopadre;
    @JoinColumn(name = "CONTRATOHIJO", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Contratos contratohijo;
    @Transient
    private String informacionContrato;

    public Contratos() {
    }

    public Contratos(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public Contratos(BigInteger secuencia, String descripcion, String estado) {
        this.secuencia = secuencia;
        this.descripcion = descripcion;
        this.estado = estado;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public Short getCodigo() {
        return codigo;
    }

    public void setCodigo(Short codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstado() {
        if (estado == null) {
            estado = "INACTIVO";
        }
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getConvencion() {
        return convencion;
    }

    public void setConvencion(String convencion) {
        this.convencion = convencion;
    }

    public String getRelacionconecopetrol() {
        return relacionconecopetrol;
    }

    public void setRelacionconecopetrol(String relacionconecopetrol) {
        this.relacionconecopetrol = relacionconecopetrol;
    }

    public TiposCotizantes getTipocotizante() {
        if (tipocotizante == null) {
            tipocotizante = new TiposCotizantes();
        }
        return tipocotizante;
    }

    public void setTipocotizante(TiposCotizantes tipocotizante) {
        this.tipocotizante = tipocotizante;
    }

    public Contratos getContratopadre() {
        return contratopadre;
    }

    public void setContratopadre(Contratos contratopadre) {
        this.contratopadre = contratopadre;
    }

    public Contratos getContratohijo() {
        return contratohijo;
    }

    public void setContratohijo(Contratos contratohijo) {
        this.contratohijo = contratohijo;
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
        if (!(object instanceof Contratos)) {
            return false;
        }
        Contratos other = (Contratos) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Contratos[ secuencia=" + secuencia + " ]";
    }

    public String getInformacionContrato() {
        if (informacionContrato == null) {
            if (codigo != null && descripcion != null) {
                informacionContrato = codigo + " - " + descripcion;
            }
        }
        return informacionContrato;
    }

    public void setInformacionContrato(String informacionContrato) {
        this.informacionContrato = informacionContrato;
    }
}
