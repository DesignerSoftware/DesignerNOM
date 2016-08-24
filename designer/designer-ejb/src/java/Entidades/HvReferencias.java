package Entidades;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "HVREFERENCIAS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "HvReferencias.findAll", query = "SELECT h FROM HvReferencias h")})
public class HvReferencias implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Size(max = 100)
    @Column(name = "NOMBREPERSONA")
    private String nombrepersona;
    @Column(name = "TELEFONO")
    private Long telefono;
    @Column(name = "CELULAR")
    private Long celular;
    @Size(max = 50)
    @Column(name = "CARGO")
    private String cargo;
    @Size(max = 20)
    @Column(name = "TIPO")
    private String tipo;
    @JoinColumn(name = "PARENTESCO", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private TiposFamiliares parentesco;
    @JoinColumn(name = "HOJADEVIDA", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private HVHojasDeVida hojadevida;

    public HvReferencias() {
    }

    public HvReferencias(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public HvReferencias(BigInteger secuencia, String nombrepersona) {
        this.secuencia = secuencia;
        this.nombrepersona = nombrepersona;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public String getNombrepersona() {
        return nombrepersona;
    }

    public void setNombrepersona(String nombrepersona) {
        this.nombrepersona = nombrepersona.toUpperCase();
    }

    public Long getTelefono() {
        return telefono;
    }

    public void setTelefono(Long telefono) {
        this.telefono = telefono;
    }

    public Long getCelular() {
        return celular;
    }

    public void setCelular(Long celular) {
        this.celular = celular;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo.toUpperCase();
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

public TiposFamiliares getParentesco() {
        if (parentesco == null) {
            parentesco = new TiposFamiliares();
        }
        return parentesco;
    }

    public void setParentesco(TiposFamiliares parentesco) {
        this.parentesco = parentesco;
    }

    public HVHojasDeVida getHojadevida() {
        return hojadevida;
    }

    public void setHojadevida(HVHojasDeVida hojadevida) {
        this.hojadevida = hojadevida;
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
        if (!(object instanceof HvReferencias)) {
            return false;
        }
        HvReferencias other = (HvReferencias) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.HvReferencias[ secuencia=" + secuencia + " ]";
    }

}
