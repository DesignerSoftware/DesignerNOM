package Entidades;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Administrador
 */
@Entity
@Table(name = "EERSCABECERAS")
public class EersCabeceras implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "DOCUMENTO")
    private String documento;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "ESTADO")
    private String estado;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "TIPOEER")
    private String tipoeer;
    @Size(max = 1)
    @Column(name = "APROBADO")
    private String aprobado;
    @Size(max = 50)
    @Column(name = "COMENTARIO")
    private String comentario;
    @Column(name = "FECHAPAGO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechapago;
    @Size(max = 1)
    @Column(name = "PAGARPORFUERA")
    private String pagarporfuera;
    @Column(name = "HORAS")
    private Short horas;
    @Column(name = "MINUTOS")
    private Short minutos;
    @JoinColumn(name = "ESTRUCTURAAPRUEBA", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Estructuras estructuraaprueba;
    @JoinColumn(name = "EMPLEADO", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Empleados empleado;
    @JoinColumn(name = "EEROPCIONESTADO", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private EersOpcionesEstados eeropcionestado;
    @JoinColumn(name = "EERESTADO", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private EersEstados eerestado;

    public EersCabeceras() {
    }

    public EersCabeceras(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public EersCabeceras(BigInteger secuencia, String documento, String estado, String tipoeer) {
        this.secuencia = secuencia;
        this.documento = documento;
        this.estado = estado;
        this.tipoeer = tipoeer;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getTipoeer() {
        return tipoeer;
    }

    public void setTipoeer(String tipoeer) {
        this.tipoeer = tipoeer;
    }

    public String getAprobado() {
        return aprobado;
    }

    public void setAprobado(String aprobado) {
        this.aprobado = aprobado;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Date getFechapago() {
        return fechapago;
    }

    public void setFechapago(Date fechapago) {
        this.fechapago = fechapago;
    }

    public String getPagarporfuera() {
        return pagarporfuera;
    }

    public void setPagarporfuera(String pagarporfuera) {
        this.pagarporfuera = pagarporfuera;
    }

    public Short getHoras() {
        return horas;
    }

    public void setHoras(Short horas) {
        this.horas = horas;
    }

    public Short getMinutos() {
        return minutos;
    }

    public void setMinutos(Short minutos) {
        this.minutos = minutos;
    }
    
    public Estructuras getEstructuraaprueba() {
        return estructuraaprueba;
    }

    public void setEstructuraaprueba(Estructuras estructuraaprueba) {
        this.estructuraaprueba = estructuraaprueba;
    }

    public Empleados getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleados empleado) {
        this.empleado = empleado;
    }

    public EersOpcionesEstados getEeropcionestado() {
        return eeropcionestado;
    }

    public void setEeropcionestado(EersOpcionesEstados eeropcionestado) {
        this.eeropcionestado = eeropcionestado;
    }

    public EersEstados getEerestado() {
        return eerestado;
    }

    public void setEerestado(EersEstados eerestado) {
        this.eerestado = eerestado;
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
        if (!(object instanceof EersCabeceras)) {
            return false;
        }
        EersCabeceras other = (EersCabeceras) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.EersCabeceras[ secuencia=" + secuencia + " ]";
    }
    
}
