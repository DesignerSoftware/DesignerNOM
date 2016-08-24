package Entidades;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "ENCARGATURAS")
public class Encargaturas implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHAINICIAL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechainicial;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHAFINAL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechafinal;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHAPAGO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechapago;
    @JoinColumn(name = "TIPOREEMPLAZO", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private TiposReemplazos tiporeemplazo;
    @JoinColumn(name = "MOTIVOREEMPLAZO", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private MotivosReemplazos motivoreemplazo;
    @JoinColumn(name = "ESTRUCTURA", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Estructuras estructura;
    @JoinColumn(name = "REEMPLAZADO", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Empleados reemplazado;
    @JoinColumn(name = "EMPLEADO", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Empleados empleado;
    @JoinColumn(name = "CARGO", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Cargos cargo;

    public Encargaturas() {
    }

    public Encargaturas(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public Encargaturas(BigInteger secuencia, Date fechainicial, Date fechafinal, Date fechapago) {
        this.secuencia = secuencia;
        this.fechainicial = fechainicial;
        this.fechafinal = fechafinal;
        this.fechapago = fechapago;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public Date getFechainicial() {
        return fechainicial;
    }

    public void setFechainicial(Date fechainicial) {
        this.fechainicial = fechainicial;
    }

    public Date getFechafinal() {
        return fechafinal;
    }

    public void setFechafinal(Date fechafinal) {
        this.fechafinal = fechafinal;
    }

    public Date getFechapago() {
        return fechapago;
    }

    public void setFechapago(Date fechapago) {
        this.fechapago = fechapago;
    }

    public TiposReemplazos getTiporeemplazo() {
        if(tiporeemplazo == null){
            tiporeemplazo = new TiposReemplazos();
            tiporeemplazo.setNombre(" ");
            
        }
        return tiporeemplazo;
    }

    public void setTiporeemplazo(TiposReemplazos tiporeemplazo) {
        this.tiporeemplazo = tiporeemplazo;
    }

    public MotivosReemplazos getMotivoreemplazo() {
        if(motivoreemplazo == null){
            motivoreemplazo = new MotivosReemplazos();
            motivoreemplazo.setNombre(" ");
            
        }
        return motivoreemplazo;
    }

    public void setMotivoreemplazo(MotivosReemplazos motivoreemplazo) {
        this.motivoreemplazo = motivoreemplazo;
    }

    public Estructuras getEstructura() {
        if(estructura == null){
            estructura = new Estructuras();
            estructura.setNombre(" ");
            
        }
        return estructura;
    }

    public void setEstructura(Estructuras estructura) {
        this.estructura = estructura;
    }

    public Empleados getReemplazado() {
        if(reemplazado == null){
            reemplazado = new Empleados();
            
        }
        
        return reemplazado;
    }

    public void setReemplazado(Empleados reemplazado) {
        this.reemplazado = reemplazado;
    }

    public Empleados getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleados empleado) {
        this.empleado = empleado;
    }

    public Cargos getCargo() {
        if(cargo == null){
            cargo = new Cargos();
            cargo.setNombre(" ");
            
        }
        
        return cargo;
    }

    public void setCargo(Cargos cargo) {
        this.cargo = cargo;
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
        if (!(object instanceof Encargaturas)) {
            return false;
        }
        Encargaturas other = (Encargaturas) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Encargaturas[ secuencia=" + secuencia + " ]";
    }
    
}
