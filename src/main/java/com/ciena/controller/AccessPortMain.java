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

    public static void main(String[] args) {
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
            dataBase = new Conexion.DBConnector();
            JSONObject contenidoObjetosTotales = Util.parseJSONFile(rutaDeArchivo);
            JSONObject objetoTopologyContext = Util.retonarListaPropiedadesAsociadasNodoHijo(contenidoObjetosTotales,
                    tapiContext, tapiTopology);
            JSONArray deviceArray = objetoTopologyContext.getJSONArray(device);
            JSONObject nodeContext = (JSONObject) deviceArray.get(0);
            if(nodeContext.has(accessPort)){
                JSONArray listaDeEquipment = nodeContext.getJSONArray(accessPort);
            }

            List<String> listaColumnas = Util.listaDeColumnasPadreArray(deviceArray, accessPort);
            insertoDiccionarioAccessPort = insertarDiccionarioAccessPort(listaColumnas, dataBase);
            insertoMatrizAccessPort = insertarMatrizAccessPort(listaColumnas, dataBase, deviceArray,accessPort);
            System.out.println("-------------Procesando ejecutado con exito: " + insertoDiccionarioAccessPort  + "/ "+ insertoMatrizAccessPort);
            analizo = insertoDiccionarioAccessPort && insertoMatrizAccessPort ? true : false;
        } catch (Exception e) {
            analizo = false;
            System.out.println("-------------Procesando con errores: " + e.getMessage());
            e.printStackTrace();
        }
        return analizo;
    }

    private boolean insertarMatrizAccessPort(List<String> listaDeColumnas, Conexion.DBConnector dataBase, JSONArray deviceArray, String accessPort) {
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
            exp_accessport.put("uuid_device","varchar(250) , foreign key (uuid_device) references exp_physical_device(uuid)");
            tablaAccessPort = Util.crearTablasGenericoMap(dataBase,"exp_physical_access_port",tablaAccessPort,exp_accessport);
            DBRecord record = tablaAccessPort.newRecord();
            for (Object objetosNode : deviceArray){
                JSONObject deviceUuid = (JSONObject) objetosNode;
                String columnaUuid = deviceUuid.get("uuid").toString();
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

    private boolean insertarDiccionarioAccessPort(List<String> listaDeColumnas, Conexion.DBConnector dataBase) {
        String[][] dicAccessPort = new String[][] { { "id", "int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY" },
                { "atribute_name", "varchar(250)" } };
        listaDeColumnas = listaDeColumnas.stream().distinct().collect(Collectors.toList());
        try {
            String nombreTabla = "dic_physical_access_port";
            System.out.println("	-------------Creando tabla: " + nombreTabla);
            tablaDicAccessPort = Util.crearTablasGenerico(dataBase, nombreTabla, tablaDicAccessPort, dicAccessPort);

            DBRecord recorre = tablaDicAccessPort.newRecord();
            for (String objetos : listaDeColumnas) {
                recorre = tablaDicAccessPort.newRecord();
                recorre.addField("atribute_name", objetos);
                tablaDicAccessPort.insert(recorre);
            }
        } catch (SQLException | ClassNotFoundException e) {
            return false;
        }
        return true;
    }

    public void insertarAccessPort(String lugarDelArchivo, String tapiContext,
                                   String physicalContext, String device, String accessport) throws SQLException, ClassNotFoundException {
        Map<String, String> exp_accessport = new HashMap<>();
        List<String> listaDeColumnas = new ArrayList<>();
        JSONArray evaluarAccessPort = null;
        try{
            JSONObject json = Util.parseJSONFile(lugarDelArchivo);
            JSONObject identifica = json.getJSONObject(tapiContext).getJSONObject(physicalContext);
            evaluarAccessPort = identifica.getJSONArray(device);
            JSONArray identificaElementos = identifica.getJSONArray(device);
            for(Object objetos : identificaElementos){
                JSONObject lineaDeElementos = (JSONObject) objetos;
                if(lineaDeElementos.has(accessport)){
                    for(Object objetosNode : lineaDeElementos.getJSONArray(accessport)){
                        JSONObject objectEvaluado = (JSONObject) objetosNode;
                        Map<String, Object> objectMap = objectEvaluado.toMap();
                        for (Map.Entry<String, Object> entry : objectMap.entrySet()){
                            listaDeColumnas.add(entry.getKey());
                        }
                    }
                }
            }
        }catch (Exception exception) {
            System.out.println("error:: " + exception.getMessage());
        }
        String[][] dicAccessPort = new String[][]{
                {
                        "id","int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY"
                },
                {
                        "atribute_name","varchar(250)"
                }
        };
        listaDeColumnas = listaDeColumnas.stream().distinct().collect(Collectors.toList());
        for (String objetos : listaDeColumnas){
            String nombreColumna = objetos.replaceAll("-","_").replaceAll(":","_");
            if(nombreColumna.equals("uuid")){
                exp_accessport.put(nombreColumna, "varchar(50) PRIMARY KEY ");
            }else{
                exp_accessport.put(nombreColumna, "MEDIUMTEXT");
            }
        }
        exp_accessport.put("uuid_device","varchar(250) , foreign key (uuid_device) references exp_physical_device(uuid)");
        dataBase = new Conexion.DBConnector();
        tablaDicAccessPort = Util.crearTablasGenerico(dataBase,"dic_access_port",tablaDicAccessPort,dicAccessPort);
        tablaAccessPort = Util.crearTablasGenericoMap(dataBase,"exp_physical_access_port",tablaAccessPort,exp_accessport);
        DBRecord recorre = tablaDicAccessPort.newRecord();
        for(String objetos : listaDeColumnas){
            recorre = tablaDicAccessPort.newRecord();
            try {
                recorre.addField("atribute_name", objetos);
                tablaDicAccessPort.insert(recorre);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        DBRecord record = tablaAccessPort.newRecord();
        for (Object objetosNode : evaluarAccessPort){
            JSONObject deviceUuid = (JSONObject) objetosNode;
            String columnaUuid = deviceUuid.get("uuid").toString();
            //HAS ES TENER, SI TIENE QUE ME LO EJECUTE SI NO ELSE
            //NUNCA VALIDAR ANTES
            if(deviceUuid.has(accessport)){
                JSONArray listAccesPort = deviceUuid.getJSONArray(accessport);
                for (Object objectEvaluado : listAccesPort){
                    JSONObject columnasDeObjeto = (JSONObject) objectEvaluado;
                    record = tablaAccessPort.newRecord();
                    insertarInformacion(record, columnasDeObjeto, listaDeColumnas, columnaUuid);
                }
            }
        }
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
