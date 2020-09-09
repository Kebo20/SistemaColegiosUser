/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author kevin
 */
@Entity
@Table(name = "matricula")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Matricula.findAll", query = "SELECT m FROM Matricula m")
    , @NamedQuery(name = "Matricula.findById", query = "SELECT m FROM Matricula m WHERE m.id = :id")
    , @NamedQuery(name = "Matricula.findByFecha", query = "SELECT m FROM Matricula m WHERE m.fecha = :fecha")
    , @NamedQuery(name = "Matricula.findByDescripcion", query = "SELECT m FROM Matricula m WHERE m.descripcion = :descripcion")})
public class Matricula implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "fecha")
    private String fecha;
    @Basic(optional = false)
    @Column(name = "descripcion")
    private String descripcion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idmatricula")
    private List<Matriculaclase> matriculaclaseList;
    @JoinColumn(name = "idalumno", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Alumno idalumno;
    @JoinColumn(name = "idano", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Ano idano;
    @JoinColumn(name = "idseccion", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Seccion idseccion;

    public Matricula() {
    }

    public Matricula(Integer id) {
        this.id = id;
    }

    public Matricula(Integer id, String fecha, String descripcion) {
        this.id = id;
        this.fecha = fecha;
        this.descripcion = descripcion;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @XmlTransient
    public List<Matriculaclase> getMatriculaclaseList() {
        return matriculaclaseList;
    }

    public void setMatriculaclaseList(List<Matriculaclase> matriculaclaseList) {
        this.matriculaclaseList = matriculaclaseList;
    }

    public Alumno getIdalumno() {
        return idalumno;
    }

    public void setIdalumno(Alumno idalumno) {
        this.idalumno = idalumno;
    }

    public Ano getIdano() {
        return idano;
    }

    public void setIdano(Ano idano) {
        this.idano = idano;
    }

    public Seccion getIdseccion() {
        return idseccion;
    }

    public void setIdseccion(Seccion idseccion) {
        this.idseccion = idseccion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Matricula)) {
            return false;
        }
        Matricula other = (Matricula) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Matricula[ id=" + id + " ]";
    }
    
}
