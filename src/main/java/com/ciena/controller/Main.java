package com.ciena.controller;

import com.ciena.controller.entity.*;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        // CONFIGURAMOS LA CLASE MAPPER CON ALGUNOS PARAMETROS PARA EVITAR ERRORES DE SITAXIS
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        // FAIL_ON_UNKNOWN_PROPERTIES QUE FALLE SI NO ENCUENTRA PROPIEDADES ? FALSE -- NO, QUE SIGA
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // ACCEPT_SINGLE_VALUE_AS_ARRAY QUE ACEPTE ARREGLOS -- TRUE, QUE SI ACEPTE
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

        // LEER EL ARCHIVO JSON PARA ELLO USAMOS LA CLASE FILE QUE ME PIDE UNA RUTA DONDE ESTA EL ARCHIVO
        File archivoJson = new File("D:\\archivos\\objetociena.json");

        // PARA PODER TRANSFORMAR EL ARCHIVO A OBJETOS USAMOS EL READVALUE DE MAPPER, QUE ME PIDE EL ARCHIVO Y LA CLASE QUE ME SERVIRA PARA ALMACENAR LOS DATOS DEL ARCHIVO
        ObjetosPrincipales principal = mapper.readValue(archivoJson, ObjetosPrincipales.class);

        //PARA PROBAR SE ESTA IMPRIMIENDO UN CAMPO DEL JSON CON EL OBJETO principal previamente creado
        System.out.println("INFORMACION: " +  principal.getTapi_common_context().getUuid());

        System.out.println("--------------------IMPRIMIR service-interface-point-------- \n");
        Main m = new Main(); // SE CREA ESTE OBJETO PARA PODER ACCEDER A LOS METODOS CREADOS
        // COMO ES UN ARCHIVO GRANDE, NECESITAMOS VALIDAR SI TODA LA INFO SE HA MAPEADO BIEN
        // PARA ESO CREO UN METODO imprimirServiceInterface que me va a IMPRIMIR TODO LO QUE TIENE  ESA LISTA List<ServiceInterface>(VER EL JSON0)

        //m.imprimirServiceInterface(principal.getTapi_common_context().getService_interface_point());
        //m.imprimirTapiEquipment(principal.getTapi_common_context().getTapi_equipment_physical_context());
        //m.imprimirName(principal.getTapi_common_context().getGetName());
        //m.imprimirTapiStreaming(principal.getTapi_common_context().getTapi_streaming_stream_context());
        m.imprimirTapiTopology(principal.getTapi_common_context().getTapi_topology_topology_context());
    }
    private void imprimirTapiTopology(TapiTopology tapiTopology){
        System.out.println("  --- nw-topology-service ---  ");
        System.out.println(" -- uuid: " + tapiTopology.getNw_topology_service().getUuid());
        System.out.println("  --- topology ---  ");
        for (TopologyObject topology2 : tapiTopology.getNw_topology_service().getTopology()){
            System.out.println(" -- topology-uuid: " + topology2.getTopology_uuid());
        }
        for (Name name : tapiTopology.getNw_topology_service().getName()){
            System.out.println("  --- Name ---  ");
            System.out.println(" -- value-name: " + name.getValue_name());
            System.out.println(" -- value: " + name.getValue());
        }

        for(Topology topology : tapiTopology.getTopology()){
            System.out.println(" -- uuid: " + topology.getUuid());
            System.out.println("  --- Node --- ");

            for (Node node : topology.getNode()){
                System.out.println(" -- uuid: " + node.getUuid());
                System.out.println(" -- lifecycle-state: " + node.getLifecycle_state());
                for (Name name : node.getName()){
                    System.out.println("  --- Name ---  ");
                    System.out.println(" -- value-name: " + name.getValue_name());
                    System.out.println(" -- value: " + name.getValue());
                }
                System.out.println(" -- operational-state: " + node.getOperational_state());
                System.out.println(" --- owned-node-edge-point -- ");
                for (OwnedNode ownedNode : node.getOwned_node_edge_point()){
                    System.out.println(" -- uuid: " + ownedNode.getUuid());
                    System.out.println(" -- termination-state: " + ownedNode.getTermination_state());
                    System.out.println(" -- termination-direction: " + ownedNode.getTermination_direction());
                    System.out.println(" -- layer-protocol-name: " + ownedNode.getLayer_protocol_name());
                    System.out.println(" -- lifecycle-state: " + ownedNode.getLifecycle_state());
                    for (Name name : ownedNode.getName()){
                        System.out.println("  --- Name ---  ");
                        System.out.println("    -- value-name: " + name.getValue_name());
                        System.out.println("    -- value: " + name.getValue());
                    }
                    System.out.println(" -- operational-state: " + ownedNode.getOperational_state());
                    System.out.println("  --- tapi-equipment:supporting-access-port --- ");
                    System.out.println("   --- access-port --- ");
                    if (null != ownedNode.getTapi_equipment_supporting_access_port() && null != ownedNode.getTapi_equipment_supporting_access_port().getAccess_port()){
                        System.out.println(" -- access-port-uuid: " + ownedNode.getTapi_equipment_supporting_access_port().getAccess_port().getAccess_port_uuid());
                        System.out.println(" -- device-uuid: " + ownedNode.getTapi_equipment_supporting_access_port().getAccess_port().getDevice_uuid());
                    }
                    System.out.println(" --- supported-cep-layer-protocol-qualifier --- ");
                    for (String supportedlayer : ownedNode.getSupported_cep_layer_protocol_qualifier()){
                        System.out.println("        --- " + supportedlayer);
                    }
                    System.out.println(" -- administrative-state: " + ownedNode.getAdministrative_state());
                    System.out.println(" --- tapi-photonic-media:media-channel-node-edge-point-spec --- ");
                    System.out.println("   -- mc-pool -- ");
                    if (null != ownedNode.getTapi_photonic_media_media_channel_node_edge_point_spec() &&
                            null != ownedNode.getTapi_photonic_media_media_channel_node_edge_point_spec().getMc_pool()
                    ){
                        if(null != ownedNode.getTapi_photonic_media_media_channel_node_edge_point_spec().getMc_pool().getSupportable_spectrum())
                            for (Spectrum spectrum : ownedNode.getTapi_photonic_media_media_channel_node_edge_point_spec().getMc_pool().getSupportable_spectrum()){
                                System.out.println(" -- upper-frequency: " + spectrum.getUpper_frequency());
                                System.out.println(" -- lower-frequency: " + spectrum.getLower_frequency());
                                System.out.println(" --- frequency-constraint --- ");
                                System.out.println("  -- adjustment-granularity: " + spectrum.getFrequency_constraint().getAdjustment_granularity());
                                System.out.println("  -- grid-type: " + spectrum.getFrequency_constraint().getGrid_type());
                            }
                        if(null != ownedNode.getTapi_photonic_media_media_channel_node_edge_point_spec().getMc_pool().getAvailable_spectrum())
                            for (Spectrum available : ownedNode.getTapi_photonic_media_media_channel_node_edge_point_spec().getMc_pool().getAvailable_spectrum()){
                                System.out.println(" -- upper-frequency: " + available.getUpper_frequency());
                                System.out.println(" -- lower-frequency: " + available.getLower_frequency());
                                System.out.println(" --- frequency-constraint --- ");
                                System.out.println("  -- adjustment-granularity: " + available.getFrequency_constraint().getAdjustment_granularity());
                                System.out.println("  -- grid-type: " + available.getFrequency_constraint().getGrid_type());

                            }
                    }


                }
            }
        }

    }
    private void imprimirTapiStreaming(TapiStreaming tapiStreaming){
        if(null != tapiStreaming) {
            for (AvailableStream availableStream : tapiStreaming.getAvailable_stream()) {
                System.out.println(" --- uuid: " + availableStream.getUuid());
                System.out.println(" ---- supported-stream-type --- ");
                System.out.println(" -- supported-stream-type-uuid: " + availableStream.getSupported_stream_type().getSupported_stream_type_uuid());
                System.out.println(" --- stream-state: " + availableStream.getStream_state());
                System.out.println(" --- stream-id: " + availableStream.getStream_id());
                System.out.println(" --- connection-protocol: " + availableStream.getConnection_protocol());
                System.out.println(" --- connection-address: " + availableStream.getConnection_address());
                for (Name name : availableStream.getName()) {
                    System.out.println(" -- Name -- ");
                    System.out.println(" --- value-name: " + name.getValue_name());
                    System.out.println(" --- value: " + name.getValue());
                }
            }
        }
        if(null != tapiStreaming) {
            for (StreamType streamType : tapiStreaming.getSupported_stream_type()) {
                System.out.println(" --- uuid: " + streamType.getUudi());
                System.out.println(" --- record-retention: " + streamType.getRecord_retention());
                System.out.println(" --- stream-type-name: " + streamType.getStream_type_name());
                System.out.println(" --- log-storage-strategy: " + streamType.getLog_storage_strategy());
                System.out.println(" --- segment-size: " + streamType.getSegment_size());
                System.out.println(" --- log-record-strategy: " + streamType.getLog_record_strategy());
                System.out.println("   --- record-content -- ");
                for (String record : streamType.getRecord_content()) {
                    System.out.println("        --- " + record);
                }
                for (Name name : streamType.getName()) {
                    System.out.println(" -- Name -- ");
                    System.out.println(" --- value-name: " + name.getValue_name());
                    System.out.println(" --- value: " + name.getValue());
                }
                System.out.println("   --- connection-protocol-details --- ");
                for (String allowed : streamType.getConnection_protocol_details().getAllowed_connection_protocols()) {
                    System.out.println("        --- " + allowed);
                }
            }
        }
    }
    private void imprimirName(List<Name> parametroName){
        for (Name name : parametroName){
            System.out.println(" --- Name --- ");
            System.out.println("  -- value-name: " + name.getValue_name());
            System.out.println("  -- value: " + name.getValue());

        }
    }

    private void imprimirTapiEquipment(TapiEquipment tapiEquipment){
        for (Device device : tapiEquipment.getDevice()){
            System.out.println(" -- uuid: " + device.getUuid());
            for (Equipment equipment : device.getEquipment()){
                System.out.println(" -- uuid: " + equipment.getUuid());
                for (Name name : equipment.getName()){
                    System.out.println(" -- value-name: " + name.getValue_name());
                    System.out.println(" -- value: " + name.getValue());
                }
                System.out.println(" -- expected-equipment -- ");
                for (ExpectedEquipment expectedEquipment : equipment.getExpected_equipment()){
                    System.out.println(" --common-equipment --");
                    System.out.println(" -- equipment-type: " + expectedEquipment.getCommon_equipment_properties().getEquipment_type_version());
                    System.out.println(" -- manufacturer-name: " + expectedEquipment.getCommon_equipment_properties().getManufacturer_name());
                    System.out.println(" -- equipment-type-name: " + expectedEquipment.getCommon_equipment_properties().getEquipment_type_name());
                }
                System.out.println(" -- Actual-Equipment -- ");
                System.out.println("    -- common-equipment -- ");
                System.out.println("      -- equipment-type: " + equipment.getActual_equipment().getCommon_equipment_properties().getEquipment_type_version());
                System.out.println("      -- manufacturer: " + equipment.getActual_equipment().getCommon_equipment_properties().getManufacturer_name());
                System.out.println("      -- equipment-name: " + equipment.getActual_equipment().getCommon_equipment_properties().getEquipment_type_name());
                System.out.println(" -- category: " + equipment.getCategory());
                System.out.println(" -- is-expected: " + equipment.isIs_expected_actual_mismatch());

            }
            for (Name name : device.getName()){
                System.out.println(" -- Name --");
                System.out.println("  -- value-name: " + name.getValue_name());
                System.out.println("  -- value: " + name.getValue());
            }
        }
        System.out.println(" -- uuid: " + tapiEquipment.getUuid());
        for (Name name : tapiEquipment.getName()){
            System.out.println(" -- Name --");
            System.out.println("   -- value-name: " + name.getValue_name());
            System.out.println("   -- value: " + name.getValue());
        }
    }

    // SE CREA UN METODO QUE ME IMPRIMIRA TODO LO QUE TIENE UNA LISTA DE ServiceInterface, ASI COMO ESTE, DEBES CREAR UN METODO POR CADA CLASE
    // PARA VALIDAR TODO EL ARCHIVO
    private void imprimirServiceInterface(List<ServiceInterface> parametroServiceInterface){
        // COMO ES UNA LISTA,  NECESITAMOS RECORRERLO, PARA ELLO USAMOS UN FOR
        // EL FOR SE COMPONE DE 2 ELEMENTOS EL PRIMERO (ServiceInterface service :) ES LA CLASE QUE  POR CADA ITERASION ME ALMACENA LOS DATOS
        // EL SEGUNDO ES LA LISTA QUE VOY A RECORRER, QUE EN ESTE CASO ES EL PARAMETRO parametroServiceInterface
        for(ServiceInterface service :  parametroServiceInterface){
            // PINTAMOS TODAS LAS PROPIEDADES QUE TIENE MI CLASE, A MODO DE EJEMPLO PINTE EL PRIMERO, SERIA IDEA PINTES TODOS
            System.out.println("   --- uuid: " +  service.getUuid() + "");
            System.out.println("   --- Supported_layer_protocol_qualifier -- ");
            for(String dato : service.getSupported_layer_protocol_qualifier()){
                System.out.println("        --- " + dato);
            }
            System.out.println("   --- LifecycleState: " +  service.getLifecycle_state() + "");
            System.out.println("   --- total-potential-capacity -- ");
            System.out.println("     --- total-size:  ");
            System.out.println("       --- unit: " + service.getTotal_potential_capacity().getTotal_size().getUnit());
            System.out.println("       --- value: " + service.getTotal_potential_capacity().getTotal_size().getValue());
            System.out.println("   ---LayerProtocol: " + service.getLayer_protocol_name());
            System.out.println("   ---AdministrativeState: " + service.getAdministrative_state());
            System.out.println("   --- available-capacity -- ");
            if(null != service.getAvailable_capacity()){
                System.out.println("     --- total-size:  ");
                System.out.println("       --- unit: " + service.getAvailable_capacity().getTotal_size().getUnit());
                System.out.println("       --- value: " + service.getAvailable_capacity().getTotal_size().getValue());
            }
            System.out.println("   --- direction: " + service.getDirection());
            System.out.println("   --- operational-state: " + service.getOperational_state());
            System.out.println("   --- Name --- ");
            for(Name name : service.getName()){
                System.out.println("    -- value-name: " + name.getValue_name());
                System.out.println("    -- value: " + name.getValue());
            }
        }
    }
}
