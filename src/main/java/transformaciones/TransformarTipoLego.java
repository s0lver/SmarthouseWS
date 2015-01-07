package transformaciones;

import modelos.TiposlegoEntity;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import java.util.Collection;

public class TransformarTipoLego {
    public static JsonArrayBuilder transformarEnListaJSON(Collection<TiposlegoEntity> tipoLego){
        JsonArrayBuilder creadorLegos = Json.createArrayBuilder();

        for (TiposlegoEntity tipoLegoActual: tipoLego) {
            JsonObjectBuilder creadorTipoLego = transformarEnTipoLegoJSON(tipoLegoActual);
            creadorLegos.add(creadorTipoLego);
        }

        return creadorLegos;
    }

    public static JsonObjectBuilder transformarEnTipoLegoJSON(TiposlegoEntity tipoLego){
        JsonObjectBuilder creadorTipoLego = Json.createObjectBuilder();
        creadorTipoLego.add("id", tipoLego.getId());
        creadorTipoLego.add("descripcion", tipoLego.getDescripcion());

        return creadorTipoLego;
    }

}
