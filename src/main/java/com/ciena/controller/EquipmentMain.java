package com.ciena.controller;

import com.ciena.controller.dao.Conexion;
import com.ciena.controller.dao.DBRecord;
import com.ciena.controller.dao.DBTable;
import org.json.JSONArray;
import org.json.JSONObject;
import util.Util;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EquipmentMain {
    private Conexion.DBConnector dataBase;
    private static DBTable tablaDicEquipment;
    private static DBTable tablaEquipment;

    public EquipmentMain() throws SQLException, ClassNotFoundException {
        dataBase = new Conexion.DBConnector();
        tablaEquipment = dataBase.deleteTableIfExsist("exp_physical_equipment");
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        EquipmentMain equipmentMain = new EquipmentMain();
        equipmentMain.analizarInformacionEquipment("D:\\archivos\\objetociena.json","tapi-common:context",
                "tapi-equipment:physical-context","device","equipment");

    }
    public Boolean analizarInformacionEquipment(String rutaDeArchivo, String tapiContext, String tapiPhysical,
                                                String device, String equipment) {
        boolean analizo = false;
        boolean insertoDiccionarioEquipment = false;
        boolean insertoMatrizEquipment = false;
        System.out.println("-------------Procesando informacion de: " + equipment + "------- \n");
        try {

            JSONObject contenidoObjetosTotales = Util.parseJSONFile(rutaDeArchivo);
            JSONObject objetoTapiPhysical = Util.retonarListaPropiedadesAsociadasNodoHijo(contenidoObjetosTotales,
                    tapiContext, tapiPhysical);
            JSONArray deviceArray = objetoTapiPhysical.getJSONArray(device);
            JSONObject nodeContext = (JSONObject) deviceArray.get(0);
            JSONArray listaDeEquipment = nodeContext.getJSONArray(equipment);

            List<String> listaColumnas = Util.listaDeColumnasPadreArray(deviceArray, equipment);

            String tablaReferencia = "exp_physical_device";
            String columnaRefencia = "uuid";
            String nombreDeColumna = "uuid_device";

            insertoMatrizEquipment = insertarMatrizEquipment(listaColumnas, dataBase, deviceArray,equipment,tablaReferencia,columnaRefencia,nombreDeColumna);
            insertoDiccionarioEquipment = insertarDiccionarioEquipment(listaColumnas, dataBase,tablaReferencia,columnaRefencia,nombreDeColumna);
            System.out.println("-------------Procesando ejecutado con exito: " + insertoDiccionarioEquipment  + "/ "+ insertoMatrizEquipment);
            analizo = insertoDiccionarioEquipment && insertoMatrizEquipment ? true : false;
        } catch (Exception e) {
            analizo = false;
            System.out.println("-------------Procesando con errores: " + e.getMessage());
            e.printStackTrace();
        }
        return analizo;
    }

    private boolean insertarMatrizEquipment(List<String> listaDeColumnas, Conexion.DBConnector dataBase, JSONArray deviceArray, String equipment, String tablaReferencia, String columnaRefencia, String nombreDeColumna) {
        Map<String, String> exp_equipment = new HashMap<>();
        for (String objectos : listaDeColumnas) {
            String nombreColumna = objectos.replaceAll("-", "_").replaceAll(":", "_");
            if (nombreColumna.equals("uuid")) {
                exp_equipment.put(nombreColumna, "varchar(50) primary key");
            } else {
                exp_equipment.put(nombreColumna, "MEDIUMTEXT");
            }
        }
        try{
            exp_equipment.put(nombreDeColumna,"varchar(250) , FOREIGN KEY (uuid_device) REFERENCES exp_physical_device(uuid)");
            tablaEquipment = Util.crearTablasGenericoMap(dataBase,"exp_physical_device_equipment",tablaEquipment,exp_equipment);
            DBRecord record = tablaEquipment.newRecord();
            for (Object objetosDevice : deviceArray){
                JSONObject device = (JSONObject) objetosDevice;
                String columnaUuid = device.get(columnaRefencia).toString();
                JSONArray listEquipment = device.getJSONArray(equipment);
                for (Object objectEvaluado : listEquipment){
                    JSONObject objectEvaluadoDeJson = (JSONObject) objectEvaluado;
                    Map<String, Object> objectMap = objectEvaluadoDeJson.toMap();
                    record = tablaEquipment.newRecord();
                    for (Map.Entry<String, Object> entry : objectMap.entrySet()){
                        if (listaDeColumnas.stream().filter(x -> entry.getKey().equals(x)).findFirst().isPresent()){
                            record.addField(entry.getKey().replaceAll("-","_").replaceAll(":","_"), entry.getValue().toString());
                        } else {
                            record.addField(entry.getKey().replaceAll("-","_").replaceAll(":","_"), null);
                        }
                    }
                    try {
                        record.addField(nombreDeColumna, columnaUuid);
                        tablaEquipment.insert(record);

                    } catch (SQLException exception) {
                        exception.printStackTrace();
                    }
                }
            }
        }catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private boolean insertarDiccionarioEquipment(List<String> listaDeColumnas, Conexion.DBConnector dataBase, String tablaReferencia, String columnaRefencia, String nombreDeColumna) {
        String[][] dicDevice = new String[][] { { "id", "int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY" },
                { "atribute_name", "varchar(250)" },{ "flag_fk","int(11)"},{"fk_foreign_object_name","varchar(250)"},
                {"fk_foreign_object_name_atribute","varchar(250)"} };
        listaDeColumnas = listaDeColumnas.stream().distinct().collect(Collectors.toList());
        try {
            String nombreTabla = "dic_physical_device_equipment";
            System.out.println("	-------------Creando tabla: " + nombreTabla);
            tablaDicEquipment = Util.crearTablasGenerico(dataBase, nombreTabla, tablaDicEquipment, dicDevice);

            DBRecord recorre = tablaDicEquipment.newRecord();
            for (String objetos : listaDeColumnas) {
                recorre = tablaDicEquipment.newRecord();
                recorre.addField("atribute_name", objetos);
                recorre.addField("flag_fk",0);
                tablaDicEquipment.insert(recorre);

            }
            recorre = tablaDicEquipment.newRecord();
            recorre.addField("atribute_name",nombreDeColumna);
            recorre.addField("flag_fk", 1);
            recorre.addField("fk_foreign_object_name",tablaReferencia);
            recorre.addField("fk_foreign_object_name_atribute",columnaRefencia);
            tablaDicEquipment.insert(recorre);
        } catch (SQLException | ClassNotFoundException e) {
            return false;
        }
        return true;
    }

}
