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
@Table(name = "clase")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Clase.findAll", query = "SELECT c FROM Clase c")
    , @NamedQuery(name = "Clase.findById", query = "SELECT c FROM Clase c WHERE c.id = :id")})
public class Clase implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idclase")
    private List<Calificacion> calificacionList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idclase")
    private List<Horario> horarioList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idclase")
    private List<Agendaclase> agendaclaseList;
    @JoinColumn(name = "idano", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Ano idano;
    @JoinColumn(name = "idcurso", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Curso idcurso;
    @JoinColumn(name = "idprofesor", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Profesor idprofesor;
    @JoinColumn(name = "idseccion", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Seccion idseccion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idclase")
    private List<Matriculaclase> matriculaclaseList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idclase")
    private List<Archivo> archivoList;

    public Clase() {
    }

    public Clase(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @XmlTransient
    public List<Calificacion> getCalificacionList() {
        return calificacionList;
    }

    public void setCalificacionList(List<Calificacion> calificacionList) {
        this.calificacionList = calificacionList;
    }

    @XmlTransient
    public List<Horario> getHorarioList() {
        return horarioList;
    }

    public void setHorarioList(List<Horario> horarioList) {
        this.horarioList = horarioList;
    }

    @XmlTransient
    public List<Agendaclase> getAgendaclaseList() {
        return agendaclaseList;
    }

    public void setAgendaclaseList(List<Agendaclase> agendaclaseList) {
        this.agendaclaseList = agendaclaseList;
    }

    public Ano getIdano() {
        return idano;
    }

    public void setIdano(Ano idano) {
        this.idano = idano;
    }

    public Curso getIdcurso() {
        return idcurso;
    }

    public void setIdcurso(Curso idcurso) {
        this.idcurso = idcurso;
    }

    public Profesor getIdprofesor() {
        return idprofesor;
    }

    public void setIdprofesor(Profesor idprofesor) {
        this.idprofesor = idprofesor;
    }

    public Seccion getIdseccion() {
        return idseccion;
    }

    public void setIdseccion(Seccion idseccion) {
        this.idseccion = idseccion;
    }

    @XmlTransient
    public List<Matriculaclase> getMatriculaclaseList() {
        return matriculaclaseList;
    }

    public void setMatriculaclaseList(List<Matriculaclase> matriculaclaseList) {
        this.matriculaclaseList = matriculaclaseList;
    }

    @XmlTransient
    public List<Archivo> getArchivoList() {
        return archivoList;
    }

    public void setArchivoList(List<Archivo> archivoList) {
        this.archivoList = archivoList;
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
        if (!(object instanceof Clase)) {
            return false;
        }
        Clase other = (Clase) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Clase[ id=" + id + " ]";
    }
    
}
