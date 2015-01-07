package transformaciones;

import modelos.LegosEntity;
import modelos.RecursosEntity;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import java.util.Collection;

public class TransformarRecurso {
    public static JsonArrayBuilder transformarEnListaJSON(Collection<RecursosEntity> recursos){
        JsonArrayBuilder creadorLegos = Json.createArrayBuilder();

        for (RecursosEntity recursoActual : recursos) {
            JsonObjectBuilder creadorLego = transformarEnRecursoJSON(recursoActual);
            creadorLegos.add(creadorLego);
        }

        return creadorLegos;
    }

    public static JsonObjectBuilder transformarEnRecursoJSON(RecursosEntity recurso){
        JsonObjectBuilder creadorRecurso = Json.createObjectBuilder();
        creadorRecurso.add("id", recurso.getId());
        creadorRecurso.add("descripcion", recurso.getDescripcion());

        return creadorRecurso;
    }
}
