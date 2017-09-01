package Entidades;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "PARAMETROSCONTABLES")
public class ParametrosContables implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "EMPRESA_CODIGO")
    private Short empresaCodigo;
    @Column(name = "MONEDA_CODIGO")
    private Short monedaCodigo;
    @Column(name = "FECHAINICIALCONTABILIZACION")
    //@Temporal(TemporalType.TIMESTAMP)
    @Temporal(TemporalType.DATE)
    private Date fechainicialcontabilizacion;
    @Column(name = "FECHAFINALCONTABILIZACION")
    //@Temporal(TemporalType.TIMESTAMP)
    @Temporal(TemporalType.DATE)
    private Date fechafinalcontabilizacion;
    @Size(max = 30)
    @Column(name = "USUARIO")
    private String usuario;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "ARCHIVO")
    private String archivo;
    @Size(max = 4000)
    @Column(name = "RESPUESTAXML")
    private String respuestaxml;
    @Column(name = "FECHAGENERACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechageneracion;
    @Size(max = 15)
    @Column(name = "ESTADO")
    private String estado;
    @Size(max = 200)
    @Column(name = "COMENTARIO")
    private String comentario;
    @Size(max = 4)
    @Column(name = "DOCUMENTO_CONTABLE")
    private String documentoContable;
    @Column(name = "ULTIMONUMERODOCUMENTO")
    private BigInteger ultimonumerodocumento;
    @Column(name = "CODIGOEMPLEADODESDE")
    private BigDecimal codigoempleadodesde;
    @Column(name = "CODIGOEMPLEADOHASTA")
    private BigDecimal codigoempleadohasta;
    @Size(max = 10)
    @Column(name = "DOCUMENTOALTERNATIVO")
    private String documentoalternativo;
    @Size(max = 10)
    @Column(name = "DESTINO")
    private String destino;
    @Size(max = 20)
    @Column(name = "CHEQUERA")
    private String chequera;
    @Column(name = "FECHACONTABILIZACION")
//    @Temporal(TemporalType.TIMESTAMP)
    @Temporal(TemporalType.DATE)
    private Date fechacontabilizacion;
    @JoinColumn(name = "PROCESO", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Procesos proceso;
    @Transient
    private Empresas empresaRegistro;
    @Transient
    private String strCodigoEmpleadoDesde;
    @Transient
    private String strCodigoEmpleadoHasta;

    public ParametrosContables() {
    }

    public ParametrosContables(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public ParametrosContables(BigInteger secuencia, Short empresaCodigo, String archivo) {
        this.secuencia = secuencia;
        this.empresaCodigo = empresaCodigo;
        this.archivo = archivo;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public Short getEmpresaCodigo() {
        return empresaCodigo;
    }

    public void setEmpresaCodigo(Short empresaCodigo) {
        this.empresaCodigo = empresaCodigo;
    }

    public Empresas getEmpresaRegistro() {
        return empresaRegistro;
    }

    public void setEmpresaRegistro(Empresas empresaRegistro) {
        this.empresaRegistro = empresaRegistro;
    }

    public Short getMonedaCodigo() {
        return monedaCodigo;
    }

    public void setMonedaCodigo(Short monedaCodigo) {
        this.monedaCodigo = monedaCodigo;
    }

    public Date getFechainicialcontabilizacion() {
        return fechainicialcontabilizacion;
    }

    public void setFechainicialcontabilizacion(Date fechainicialcontabilizacion) {
        this.fechainicialcontabilizacion = fechainicialcontabilizacion;
    }

    public Date getFechafinalcontabilizacion() {
        return fechafinalcontabilizacion;
    }

    public void setFechafinalcontabilizacion(Date fechafinalcontabilizacion) {
        this.fechafinalcontabilizacion = fechafinalcontabilizacion;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }

    public String getRespuestaxml() {
        return respuestaxml;
    }

    public void setRespuestaxml(String respuestaxml) {
        this.respuestaxml = respuestaxml;
    }

    public Date getFechageneracion() {
        return fechageneracion;
    }

    public void setFechageneracion(Date fechageneracion) {
        this.fechageneracion = fechageneracion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getDocumentoContable() {
        return documentoContable;
    }

    public void setDocumentoContable(String documentoContable) {
        this.documentoContable = documentoContable;
    }

    public BigInteger getUltimonumerodocumento() {
        return ultimonumerodocumento;
    }

    public void setUltimonumerodocumento(BigInteger ultimonumerodocumento) {
        this.ultimonumerodocumento = ultimonumerodocumento;
    }

    public String getStrCodigoEmpleadoDesde() {
        getCodigoempleadodesde();
        if (codigoempleadodesde != null) {
            strCodigoEmpleadoDesde = codigoempleadodesde.toString();
        } else {
            strCodigoEmpleadoDesde = "";
        }
        return strCodigoEmpleadoDesde;
    }

    public void setStrCodigoEmpleadoDesde(String strCodigoEmpleadoDesde) {
        this.strCodigoEmpleadoDesde = strCodigoEmpleadoDesde;
    }

    public String getStrCodigoEmpleadoHasta() {
        getCodigoempleadohasta();
        if (codigoempleadohasta != null) {
            strCodigoEmpleadoHasta = codigoempleadohasta.toString();
        } else {
            strCodigoEmpleadoHasta = "";
        }
        return strCodigoEmpleadoHasta;
    }

    public void setStrCodigoEmpleadoHasta(String strCodigoEmpleadoHasta) {
        this.strCodigoEmpleadoHasta = strCodigoEmpleadoHasta;
    }

    public BigDecimal getCodigoempleadodesde() {
        return codigoempleadodesde;
    }

    public void setCodigoempleadodesde(BigDecimal codigoempleadodesde) {
        this.codigoempleadodesde = codigoempleadodesde;
    }

    public BigDecimal getCodigoempleadohasta() {
        return codigoempleadohasta;
    }

    public void setCodigoempleadohasta(BigDecimal codigoempleadohasta) {
        this.codigoempleadohasta = codigoempleadohasta;
    }

    public String getDocumentoalternativo() {
        return documentoalternativo;
    }

    public void setDocumentoalternativo(String documentoalternativo) {
        this.documentoalternativo = documentoalternativo;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getChequera() {
        return chequera;
    }

    public void setChequera(String chequera) {
        this.chequera = chequera;
    }

    public Date getFechacontabilizacion() {
        return fechacontabilizacion;
    }

    public void setFechacontabilizacion(Date fechacontabilizacion) {
        this.fechacontabilizacion = fechacontabilizacion;
    }

    public Procesos getProceso() {
        if(proceso == null){
            proceso = new Procesos();
        }
        return proceso;
    }

    public void setProceso(Procesos proceso) {
        this.proceso = proceso;
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
        if (!(object instanceof ParametrosContables)) {
            return false;
        }
        ParametrosContables other = (ParametrosContables) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.ParametrosContables[ secuencia=" + secuencia + " ]";
    }

}
