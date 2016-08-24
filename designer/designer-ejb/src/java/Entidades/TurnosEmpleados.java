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
@Table(name = "TURNOSEMPLEADOS")
public class TurnosEmpleados implements Serializable {
    
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
    @Column(name = "FECHASISTEMA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechasistema;
    @Size(max = 1)
    @Column(name = "PROCESADO")
    private String procesado;
    @Size(max = 50)
    @Column(name = "COMENTARIO")
    private String comentario;
    @Column(name = "DESCUENTAHORASALIMENTACION")
    private Short descuentahorasalimentacion;
    @Column(name = "PAGAVALESALIMENTACION")
    private Short pagavalesalimentacion;
    @Column(name = "FECHAPAGO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechapago;
    @Size(max = 1)
    @Column(name = "PAGASOLOVALE")
    private String pagasolovale;
    @Size(max = 20)
    @Column(name = "DOCUMENTOSOPORTE")
    private String documentosoporte;
    @JoinColumn(name = "TURNOROTATIVO", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Turnosrotativos turnorotativo;
    @JoinColumn(name = "MOTIVOTURNO", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private MotivosTurnos motivoturno;
    @JoinColumn(name = "ESTRUCTURAAPRUEBA", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Estructuras estructuraaprueba;
    @JoinColumn(name = "EMPLEADO", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Empleados empleado;

    public TurnosEmpleados() {
    }

    public TurnosEmpleados(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public TurnosEmpleados(BigInteger secuencia, Date fechainicial, Date fechafinal) {
        this.secuencia = secuencia;
        this.fechainicial = fechainicial;
        this.fechafinal = fechafinal;
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

    public Date getFechasistema() {
        return fechasistema;
    }

    public void setFechasistema(Date fechasistema) {
        this.fechasistema = fechasistema;
    }

    public String getProcesado() {
        return procesado;
    }

    public void setProcesado(String procesado) {
        this.procesado = procesado;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Short getDescuentahorasalimentacion() {
        return descuentahorasalimentacion;
    }

    public void setDescuentahorasalimentacion(Short descuentahorasalimentacion) {
        this.descuentahorasalimentacion = descuentahorasalimentacion;
    }

    public Short getPagavalesalimentacion() {
        return pagavalesalimentacion;
    }

    public void setPagavalesalimentacion(Short pagavalesalimentacion) {
        this.pagavalesalimentacion = pagavalesalimentacion;
    }

    public Date getFechapago() {
        return fechapago;
    }

    public void setFechapago(Date fechapago) {
        this.fechapago = fechapago;
    }

    public String getPagasolovale() {
        return pagasolovale;
    }

    public void setPagasolovale(String pagasolovale) {
        this.pagasolovale = pagasolovale;
    }

    public String getDocumentosoporte() {
        return documentosoporte;
    }

    public void setDocumentosoporte(String documentosoporte) {
        this.documentosoporte = documentosoporte;
    }

    public Turnosrotativos getTurnorotativo() {
        return turnorotativo;
    }

    public void setTurnorotativo(Turnosrotativos turnorotativo) {
        this.turnorotativo = turnorotativo;
    }

    public MotivosTurnos getMotivoturno() {
        return motivoturno;
    }

    public void setMotivoturno(MotivosTurnos motivoturno) {
        this.motivoturno = motivoturno;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (secuencia != null ? secuencia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TurnosEmpleados)) {
            return false;
        }
        TurnosEmpleados other = (TurnosEmpleados) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.TurnosEmpleados[ secuencia=" + secuencia + " ]";
    }
    
}
