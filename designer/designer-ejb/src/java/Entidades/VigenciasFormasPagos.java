package Entidades;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author user
 */
@Entity
@Table(name = "VIGENCIASFORMASPAGOS")
public class VigenciasFormasPagos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHAVIGENCIA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechavigencia;
    @Size(max = 1)
    @Column(name = "TIPOCUENTA")
    private String tipocuenta;
    @Size(max = 25)
    @Column(name = "CUENTA")
    private String cuenta;
    @Size(max = 50)
    @Column(name = "CUENTANOMBRE")
    private String cuentanombre;
    @Size(max = 20)
    @Column(name = "CUENTADOCUMENTO")
    private String cuentadocumento;
    @Column(name = "FECHACUENTA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechacuenta;
    @JoinColumn(name = "SUCURSAL", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Sucursales sucursal;
    @JoinColumn(name = "FORMAPAGO", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Periodicidades formapago;
    @JoinColumn(name = "MONEDA", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Monedas moneda;
    @JoinColumn(name = "METODOPAGO", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private MetodosPagos metodopago;
    @JoinColumn(name = "EMPLEADO", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Empleados empleado;

    public VigenciasFormasPagos() {
    }

    public VigenciasFormasPagos(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public VigenciasFormasPagos(BigInteger secuencia, Date fechavigencia) {
        this.secuencia = secuencia;
        this.fechavigencia = fechavigencia;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public Date getFechavigencia() {
        return fechavigencia;
    }

    public void setFechavigencia(Date fechavigencia) {
        this.fechavigencia = fechavigencia;
    }

    public String getTipocuenta() {
        return tipocuenta;
    }

    public void setTipocuenta(String tipocuenta) {
        this.tipocuenta = tipocuenta;
    }

    public String getCuenta() {
        return cuenta;
    }

    public void setCuenta(String cuenta) {
        this.cuenta = cuenta;
    }

    public String getCuentanombre() {
        return cuentanombre;
    }

    public void setCuentanombre(String cuentanombre) {
        this.cuentanombre = cuentanombre;
    }

    public String getCuentadocumento() {
        return cuentadocumento;
    }

    public void setCuentadocumento(String cuentadocumento) {
        this.cuentadocumento = cuentadocumento;
    }

    public Date getFechacuenta() {
        return fechacuenta;
    }

    public void setFechacuenta(Date fechacuenta) {
        this.fechacuenta = fechacuenta;
    }

    public Sucursales getSucursal() {
        if (sucursal == null) {
            sucursal = new Sucursales();
        }
        return sucursal;
    }

    public void setSucursal(Sucursales sucursal) {
        this.sucursal = sucursal;
    }

    public Periodicidades getFormapago() {
        return formapago;
    }

    public void setFormapago(Periodicidades formapago) {
        this.formapago = formapago;
    }

    public Monedas getMoneda() {
        return moneda;
    }

    public void setMoneda(Monedas moneda) {
        this.moneda = moneda;
    }

    public MetodosPagos getMetodopago() {
        return metodopago;
    }

    public void setMetodopago(MetodosPagos metodopago) {
        this.metodopago = metodopago;
    }

    public Empleados getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleados empleado) {
        this.empleado = empleado;
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
        if (!(object instanceof VigenciasFormasPagos)) {
            return false;
        }
        VigenciasFormasPagos other = (VigenciasFormasPagos) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Vigenciasformaspagos[ secuencia=" + secuencia + " ]";
    }
}
