package expresscorreos.model;

public class Coche {
    private String matricula;
    private float capacidad;
    private String idO;

    public Coche(String matricula, float capacidad, String idO) {
        this.matricula = matricula;
        this.capacidad = capacidad;
        this.idO = idO;
    }

    public String getMatricula() {
        return matricula;
    }

    public float getCapacidad() {
        return capacidad;
    }

    public String getIdO() {
        return idO;
    }

    @Override
    public String toString() {
        return "Matrícula: " + matricula + " | Capacidad: " + capacidad;
    }
}
