package expresscorreos.model;

public class Oficina {
    private String idO;
    private String NombreM;
    private String idCC;

    public Oficina(String idO, String nombreM, String idCC) {
        this.idO = idO;
        NombreM = nombreM;
        this.idCC = idCC;
    }

    public String getIdO() {
        return idO;
    }

    public String getNombreM() {
        return NombreM;
    }

    public String getIdCC() {
        return idCC;
    }
}
