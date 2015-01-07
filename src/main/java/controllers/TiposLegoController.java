package controllers;

import administradores.AdministradorTiposLego;
import iadministradores.IAdministradorTiposLego;
import modelos.TiposlegoEntity;
import org.hibernate.Session;
import transformaciones.TransformarTipoLego;
import util.SessionUtil;

import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.xml.ws.WebServiceException;
import java.util.Collection;

@Path("/tiposLego")
public class TiposLegoController {
    private IAdministradorTiposLego administradorTiposLego;
    private Session sesion;

    public TiposLegoController() {
        this.sesion = SessionUtil.getSession();
        administradorTiposLego = new AdministradorTiposLego(sesion);
    }

    @Path("/{idTipoLego}")
    @GET
    @Produces("application/json")
    public JsonObject lego(@PathParam("idTipoLego") int idLego) {
        TiposlegoEntity tipoLego = administradorTiposLego.buscarPorId(idLego);
        JsonObjectBuilder creadorTipoLego = null;

        if (null != tipoLego) {
            creadorTipoLego = TransformarTipoLego.transformarEnTipoLegoJSON(tipoLego);
        }

        sesion.close();

        if (null != tipoLego) return creadorTipoLego.build();

        throw new WebServiceException(String.format("Lego con id %d no encontrado", idLego));
    }

    @Path("/todos")
    @GET
    @Produces("application/json")
    public JsonArray todos() {
        Collection<TiposlegoEntity> tiposLego = administradorTiposLego.lista();

        JsonArrayBuilder creador = TransformarTipoLego.transformarEnListaJSON(tiposLego);

        sesion.close();

        return creador.build();
    }
}
