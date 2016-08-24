package Entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 *
 * @author user
 */
@Entity
@Table(name = "RETENCIONES")
public class Retenciones implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "PORCENTAJE")
    private BigDecimal porcentaje;
    @Basic(optional = false)
    @NotNull
    @Column(name = "VALOR")
    private BigDecimal valor;
    @Basic(optional = false)
    @NotNull
    @Column(name = "VALORMAXIMO")
    private BigDecimal valormaximo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "VALORMINIMO")
    private BigDecimal valorminimo;
    @Column(name = "ADICIONAUVT")
    private BigInteger adicionauvt;
    @JoinColumn(name = "VIGENCIA", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private VigenciasRetenciones vigencia;

    public Retenciones() {
    }

    public Retenciones(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public Retenciones(BigInteger secuencia, BigDecimal porcentaje, BigDecimal valor, BigDecimal valormaximo, BigDecimal valorminimo) {
        this.secuencia = secuencia;
        this.porcentaje = porcentaje;
        this.valor = valor;
        this.valormaximo = valormaximo;
        this.valorminimo = valorminimo;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public BigDecimal getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(BigDecimal porcentaje) {
        this.porcentaje = porcentaje;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public BigDecimal getValormaximo() {
        return valormaximo;
    }

    public void setValormaximo(BigDecimal valormaximo) {
        this.valormaximo = valormaximo;
    }

    public BigDecimal getValorminimo() {
        return valorminimo;
    }

    public void setValorminimo(BigDecimal valorminimo) {
        this.valorminimo = valorminimo;
    }

    public BigInteger getAdicionauvt() {
        return adicionauvt;
    }

    public void setAdicionauvt(BigInteger adicionauvt) {
        this.adicionauvt = adicionauvt;
    }

    public VigenciasRetenciones getVigencia() {
        return vigencia;
    }

    public void setVigencia(VigenciasRetenciones vigencia) {
        this.vigencia = vigencia;
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
        if (!(object instanceof Retenciones)) {
            return false;
        }
        Retenciones other = (Retenciones) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Retenciones[ secuencia=" + secuencia + " ]";
    }
    
}
