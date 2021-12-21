package com.ciena.controller;

import com.ciena.controller.dao.Conexion;
import com.ciena.controller.dao.DBRecord;
import com.ciena.controller.dao.DBTable;
import org.json.JSONArray;
import org.json.JSONObject;
import util.Util;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AccessPortMain {
    private Conexion.DBConnector dataBase;
    private static DBTable tablaDicAccessPort;
    private static DBTable tablaAccessPort;

    public AccessPortMain() throws SQLException, ClassNotFoundException {
        dataBase = new Conexion.DBConnector();
        tablaAccessPort = dataBase.deleteTableIfExsist("exp_physical_device_access_port");
    }
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        AccessPortMain main = new AccessPortMain();
        main.analizarInformacionAccessPort("D:\\archivos\\objetociena.json","tapi-common:context",
                "tapi-equipment:physical-context","device","access-port");
    }
    public Boolean analizarInformacionAccessPort(String rutaDeArchivo, String tapiContext, String tapiTopology,
                                                String device, String accessPort) {
        boolean analizo = false;
        boolean insertoDiccionarioAccessPort = false;
        boolean insertoMatrizAccessPort = false;
        System.out.println("-------------Procesando informacion de: " + accessPort + "------- \n");
        try {

            JSONObject contenidoObjetosTotales = Util.parseJSONFile(rutaDeArchivo);
            JSONObject objetoTopologyContext = Util.retonarListaPropiedadesAsociadasNodoHijo(contenidoObjetosTotales,
                    tapiContext, tapiTopology);
            JSONArray deviceArray = objetoTopologyContext.getJSONArray(device);
            JSONObject accessContext = (JSONObject) deviceArray.get(0);
            if(accessContext.has(accessPort)){
                JSONArray listaDeEquipment = accessContext.getJSONArray(accessPort);
            }

            List<String> listaColumnas = Util.columnasNoEncontradas(deviceArray, accessPort);

            String tablaReferencia = "exp_physical_device_access_port";
            String columnaRefencia = "uuid";
            String nombreDeColumna = "uuid_device";

            insertoMatrizAccessPort = insertarMatrizAccessPort(listaColumnas, dataBase, deviceArray,accessPort,tablaReferencia,columnaRefencia,nombreDeColumna);
            insertoDiccionarioAccessPort = insertarDiccionarioAccessPort(listaColumnas, dataBase,tablaReferencia,columnaRefencia,nombreDeColumna);
            System.out.println("-------------Procesando ejecutado con exito: " + insertoDiccionarioAccessPort  + "/ "+ insertoMatrizAccessPort);
            analizo = insertoDiccionarioAccessPort && insertoMatrizAccessPort ? true : false;
        } catch (Exception e) {
            analizo = false;
            System.out.println("-------------Procesando con errores: " + e.getMessage());
            e.printStackTrace();
        }
        return analizo;
    }

    private boolean insertarMatrizAccessPort(List<String> listaDeColumnas, Conexion.DBConnector dataBase, JSONArray deviceArray, String accessPort, String tablaReferencia, String columnaRefencia, String nombreDeColumna) {
        Map<String, String> exp_accessport = new HashMap<>();
        for (String objectos : listaDeColumnas) {
            String nombreColumna = objectos.replaceAll("-", "_").replaceAll(":", "_");
            if (nombreColumna.equals("uuid")) {
                exp_accessport.put(nombreColumna, "varchar(50) primary key");
            } else {
                exp_accessport.put(nombreColumna, "MEDIUMTEXT");
            }
        }
        try{
            exp_accessport.put(nombreDeColumna,"varchar(250) , foreign key (uuid_device) references exp_physical_device(uuid)");
            tablaAccessPort = Util.crearTablasGenericoMap(dataBase,"exp_physical_device_access_port",tablaAccessPort,exp_accessport);
            DBRecord record = tablaAccessPort.newRecord();
            for (Object objetosNode : deviceArray){
                JSONObject deviceUuid = (JSONObject) objetosNode;
                String columnaUuid = deviceUuid.get(columnaRefencia).toString();
                if(deviceUuid.has(accessPort)){
                    JSONArray listAccesPort = deviceUuid.getJSONArray(accessPort);
                    for (Object objectEvaluado : listAccesPort){
                        JSONObject columnasDeObjeto = (JSONObject) objectEvaluado;
                        record = tablaAccessPort.newRecord();
                        insertarInformacion(record, columnasDeObjeto, listaDeColumnas, columnaUuid);
                    }
                }
            }
        }catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private boolean insertarDiccionarioAccessPort(List<String> listaDeColumnas, Conexion.DBConnector dataBase, String tablaReferencia, String columnaRefencia, String nombreDeColumna) {
        String[][] dicAccessPort = new String[][] { { "id", "int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY" },
                { "atribute_name", "varchar(250)" },{ "flag_fk","int(11)"},{"fk_foreign_object_name","varchar(250)"},
                {"fk_foreign_object_name_atribute","varchar(250)"} };
        listaDeColumnas = listaDeColumnas.stream().distinct().collect(Collectors.toList());
        try {
            String nombreTabla = "dic_physical_device_access_port";
            System.out.println("	-------------Creando tabla: " + nombreTabla);
            tablaDicAccessPort = Util.crearTablasGenerico(dataBase, nombreTabla, tablaDicAccessPort, dicAccessPort);

            DBRecord recorre = tablaDicAccessPort.newRecord();
            for (String objetos : listaDeColumnas) {
                recorre = tablaDicAccessPort.newRecord();
                recorre.addField("atribute_name", objetos);
                recorre.addField("flag_fk",0);
                tablaDicAccessPort.insert(recorre);
            }
            recorre = tablaDicAccessPort.newRecord();
            recorre.addField("atribute_name",nombreDeColumna);
            recorre.addField("flag_fk", 1);
            recorre.addField("fk_foreign_object_name",tablaReferencia);
            recorre.addField("fk_foreign_object_name_atribute",columnaRefencia);
            tablaDicAccessPort.insert(recorre);
        } catch (SQLException | ClassNotFoundException e) {
            return false;
        }
        return true;
    }

    private void insertarInformacion(DBRecord record,JSONObject objetos,List<String>listaDeColumnas,
                                     String columnaUuid){
        Map<String, Object> objectMap = objetos.toMap();
        record = tablaAccessPort.newRecord();
        for (Map.Entry<String, Object> entry : objectMap.entrySet()){
            if (listaDeColumnas.stream().filter(x -> entry.getKey().equals(x)).findFirst().isPresent()){
                record.addField(entry.getKey().replaceAll("-","_").replaceAll(":","_"), entry.getValue().toString());
            } else {
                record.addField(entry.getKey().replaceAll("-","_").replaceAll(":","_"), null);
            }
        }
        try {
            record.addField("uuid_device", columnaUuid);
            tablaAccessPort.insert(record);

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}
