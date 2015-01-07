package transformaciones;

import modelos.HorariosEntity;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

/**
 * Created by Rafael on 15/12/2014.
 */
public class TransformarHorario {
    public static JsonArrayBuilder transformarEnListaJSON(Collection<HorariosEntity> horarios){
        JsonArrayBuilder creadorEventos = Json.createArrayBuilder();

        for (HorariosEntity horarioActual: horarios) {
            JsonObjectBuilder creadorEvento = transformarEnHorarioJSON(horarioActual);
            creadorEventos.add(creadorEvento);
        }

        return creadorEventos;
    }

    public static JsonObjectBuilder transformarEnHorarioJSON(HorariosEntity horario) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        JsonObjectBuilder creadorHorario = Json.createObjectBuilder();

        creadorHorario.add("id", horario.getId());
        creadorHorario.add("idRecurso", horario.getLegosrecursosByIdLegoRecurso().getIdRecurso());
        creadorHorario.add("idLego", horario.getLegosrecursosByIdLegoRecurso().getIdLego());
        creadorHorario.add("idLegoRecurso", horario.getLegosrecursosByIdLegoRecurso().getId());

        Date horaJava = new Date(horario.getHoraInicio().getTime());
        creadorHorario.add("horaInicio", sdf.format(horaJava));

        horaJava = new Date(horario.getHoraFin().getTime());
        creadorHorario.add("horaFin", sdf.format(horaJava));

        return creadorHorario;
    }
}
