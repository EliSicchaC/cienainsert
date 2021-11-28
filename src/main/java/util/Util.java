package util;

import com.ciena.controller.dao.Conexion;
import com.ciena.controller.dao.DBTable;
import com.ciena.controller.entity.Name;
import com.ciena.controller.entity.ObjetosPrincipales;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public  class Util {
    public static ObjetosPrincipales getObjetosPrincipales(String rutaDeArchivo) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        // CONFIGURAMOS LA CLASE MAPPER CON ALGUNOS PARAMETROS PARA EVITAR ERRORES DE SITAXIS
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        // FAIL_ON_UNKNOWN_PROPERTIES QUE FALLE SI NO ENCUENTRA PROPIEDADES ? FALSE -- NO, QUE SIGA
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // ACCEPT_SINGLE_VALUE_AS_ARRAY QUE ACEPTE ARREGLOS -- TRUE, QUE SI ACEPTE
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

        // LEER EL ARCHIVO JSON PARA ELLO USAMOS LA CLASE FILE QUE ME PIDE UNA RUTA DONDE ESTA EL ARCHIVO
        File archivoJson = new File(rutaDeArchivo);

        // PARA PODER TRANSFORMAR EL ARCHIVO A OBJETOS USAMOS EL READVALUE DE MAPPER, QUE ME PIDE EL ARCHIVO Y LA CLASE QUE ME SERVIRA PARA ALMACENAR LOS DATOS DEL ARCHIVO
        ObjetosPrincipales principal = mapper.readValue(archivoJson, ObjetosPrincipales.class);
        return principal;
    }
    // REUTILIZANDO NAME
    public static String generarNames(List<Name> listaDeNames) {
        String nameStringValue = "";
        for (Name name: listaDeNames) {
            nameStringValue = nameStringValue + name.getValue_name() + " : " + name.getValue() + " \n";
        }
        return nameStringValue;
    }

    public static DBTable crearTablasGenerico(Conexion.DBConnector dataBase, String nombreTabla, DBTable tabla, String[][] fields ) throws SQLException, ClassNotFoundException {
        //PASO 1 ELIMINAR LA TABLA ANTERIOR
        tabla = dataBase.deleteTableIfExsist(nombreTabla);
        tabla.createTable(fields);
        return tabla;
    }
}
