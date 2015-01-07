package clientes;

import tamps.cinvestav.se.listenerEventos.ws.Ordenes;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.MalformedURLException;
import java.net.URL;

public class RegistradorOrdenes {
    public static String registrarOrden(int idLego, int idRecurso, int accion) {
        String respuesta = "";

        try {
            URL url = new URL("http://localhost:9997/ws/ordenes?wsdl");

            //1st argument service URI, refer to wsdl document above
            //2nd argument is service name, refer to wsdl document above
            QName qname = new QName("http://ws.listenerEventos.se.cinvestav.tamps/", "OrdenesImplService");

            Service service = Service.create(url, qname);

            Ordenes ordenesListener = service.getPort(Ordenes.class);

            respuesta = ordenesListener.agregarOrden(idLego, idRecurso, accion);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return respuesta;
    }
}
