package com.tienda.service;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.Map;

public interface ReporteService {
        public ResponseEntity<Resource> generaReporte(
                String reporte,
                Map<String, Object> parametros,
                String tipo) throws IOException;
}
