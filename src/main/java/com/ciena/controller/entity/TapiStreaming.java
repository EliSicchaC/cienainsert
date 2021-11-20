package com.ciena.controller.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class TapiStreaming {
        @JsonProperty("available-stream")
        private List<AvailableStream> available_stream;
        @JsonProperty("supported-stream-type")
        private List<StreamType>supported_stream_type;

        public TapiStreaming(List<AvailableStream> available_stream, List<StreamType> supported_stream_type) {
                this.available_stream = available_stream;
                this.supported_stream_type = supported_stream_type;
        }
        public TapiStreaming(){
        }

        public List<AvailableStream> getAvailable_stream() {
                return available_stream;
        }

        public void setAvailable_stream(List<AvailableStream> available_stream) {
                this.available_stream = available_stream;
        }

        public List<StreamType> getSupported_stream_type() {
                return supported_stream_type;
        }

        public void setSupported_stream_type(List<StreamType> supported_stream_type) {
                this.supported_stream_type = supported_stream_type;
        }
}
