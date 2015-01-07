package transformaciones;

import modelos.EventosEntity;
import modelos.LegosEntity;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import java.text.SimpleDateFormat;
import java.util.Collection;

public class TransformarEvento {
    public static JsonArrayBuilder transformarEnListaJSON(Collection<EventosEntity> eventos){
        JsonArrayBuilder creadorEventos = Json.createArrayBuilder();

        for (EventosEntity eventoActual: eventos) {
            JsonObjectBuilder creadorEvento = transformarEnEventoJSON(eventoActual);
            creadorEventos.add(creadorEvento);
        }

        return creadorEventos;
    }

    public static JsonObjectBuilder transformarEnEventoJSON(EventosEntity evento){
        JsonObjectBuilder creadorEvento = Json.createObjectBuilder();
        creadorEvento.add("id", evento.getId());

        creadorEvento.add("idLego", evento.getLegosrecursosByIdLegoRecurso().getIdLego());
        creadorEvento.add("tipoLego", evento.getLegosrecursosByIdLegoRecurso().getLegosByIdLego().getTiposlegoByIdTipoLego().getDescripcion());

        creadorEvento.add("idRecurso", evento.getLegosrecursosByIdLegoRecurso().getIdRecurso());
        creadorEvento.add("recurso", evento.getLegosrecursosByIdLegoRecurso().getRecursosByIdRecurso().getDescripcion());

        creadorEvento.add("sentido", evento.getSentido());

        String stringTs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(evento.getTimestamp());
        creadorEvento.add("fecha", stringTs);

        return creadorEvento;
    }
}
