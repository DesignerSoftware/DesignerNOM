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
@Table(name = "EMPRESASBANCOS")
public class EmpresasBancos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Column(name = "NUMEROCUENTA")
    private String numerocuenta;
    @Column(name = "TIPOCUENTA")
    private String tipocuenta;
    @JoinColumn(name = "EMPRESA", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Empresas empresa;
    @JoinColumn(name = "BANCO", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Bancos banco;
    @JoinColumn(name = "CIUDAD", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Ciudades ciudad;
    @Transient
    private String trTipoCuenta;

    public EmpresasBancos() {
    }

    public EmpresasBancos(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public String getNumerocuenta() {
        return numerocuenta;
    }

    public void setNumerocuenta(String numerocuenta) {
        this.numerocuenta = numerocuenta;
    }

    public String getTipocuenta() {
        return tipocuenta;
    }

    public void setTipocuenta(String tipocuenta) {
        //System.out.println("ENTIDAD EMPRESASBANCOS SET TIPOCUENTA " + tipocuenta);
        this.tipocuenta = tipocuenta;
    }

    public String getTrTipoCuenta() {
        getTipocuenta();
        if (tipocuenta == null) {
            trTipoCuenta = " ";
        } else {
            if (tipocuenta.equalsIgnoreCase("c")) {
                trTipoCuenta = "Corriente";
            }
            if (tipocuenta.equalsIgnoreCase("a")) {
                trTipoCuenta = "Ahorros";
            }
        }
        System.out.println("trTipoCuenta : " + trTipoCuenta);
        return trTipoCuenta;
    }

    public void setTrTipoCuenta(String trTipoCuenta) {
        this.trTipoCuenta = trTipoCuenta;
        if (this.trTipoCuenta.equalsIgnoreCase("ahorro")) {
            System.out.println("EmpresasBancos setTrTipoCuenta a ");
            setTipocuenta("a");
        }
        if (this.trTipoCuenta.equalsIgnoreCase("corriente")) {
            System.out.println("EmpresasBancos setTrTipoCuenta c ");
            setTipocuenta("c");
        }
    }

    public Empresas getEmpresa() {
        if(empresa == null){
            empresa = new Empresas();
        }
        return empresa;
    }

    public void setEmpresa(Empresas empresa) {
        this.empresa = empresa;
    }

    public Bancos getBanco() {
        if(banco == null){
            banco = new Bancos();
        }
        return banco;
    }

    public void setBanco(Bancos banco) {
        this.banco = banco;
    }

    public Ciudades getCiudad() {
        if(ciudad == null){
            ciudad = new Ciudades();
        }
        return ciudad;
    }

    public void setCiudad(Ciudades ciudad) {
        this.ciudad = ciudad;
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
        if (!(object instanceof EmpresasBancos)) {
            return false;
        }
        EmpresasBancos other = (EmpresasBancos) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.EmpresasBancos[ secuencia=" + secuencia + " ]";
    }

}
