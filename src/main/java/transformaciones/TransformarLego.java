package transformaciones;

import modelos.LegosEntity;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import java.util.Collection;

public class TransformarLego {
    public static JsonArrayBuilder transformarEnListaJSON(Collection<LegosEntity> legos){
        JsonArrayBuilder creadorLegos = Json.createArrayBuilder();

        for (LegosEntity legoActual: legos) {
            JsonObjectBuilder creadorLego = transformarEnLegoJSON(legoActual);
            creadorLegos.add(creadorLego);
        }

        return creadorLegos;
    }

    public static JsonObjectBuilder transformarEnLegoJSON(LegosEntity lego){
        JsonObjectBuilder creadorLego = Json.createObjectBuilder();
        creadorLego.add("id", lego.getId());
        creadorLego.add("idTipoLego", lego.getIdTipoLego());
        creadorLego.add("mac", lego.getMac());
        creadorLego.add("tipo", lego.getTiposlegoByIdTipoLego().getDescripcion());

        return creadorLego;
    }
}
