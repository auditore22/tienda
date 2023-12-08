package com.tienda.service.impl;

import com.tienda.service.ReporteService;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import javax.swing.text.html.parser.Entity;
import java.io.*;
import java.sql.SQLException;
import java.util.Map;

@Service
public class ReporteServiceImpl implements ReporteService {

    @Autowired
    private DataSource dataSource;
    @Override
    public ResponseEntity<Resource> generaReporte(String reporte,
                                                  Map<String, Object> parametros,
                                                  String tipo) throws IOException {
        String estilo = tipo.equalsIgnoreCase("vPdf")?"inline; ":"attachment; ";
        String reportePath="reportes";
        ByteArrayOutputStream salida = new ByteArrayOutputStream();

        ClassPathResource fuente = new ClassPathResource(
          reportePath + File.separator + reporte + ".jasper");

        InputStream elReporte = fuente.getInputStream();

        try {
            var reporteJasper = JasperFillManager.fillReport(
                    elReporte,
                    parametros,
                    dataSource.getConnection());
            MediaType mediaType = null;
            String archivoSalida = null;

            switch (tipo){
                case "pdf","vPdf" -> {
                    JasperExportManager.exportReportToPdfStream(
                            reporteJasper, salida);
                    mediaType = mediaType.APPLICATION_PDF;
                    archivoSalida = reporte + ".pdf";
                }
                case "Xls" -> {
                    JRXlsxExporter paraExcel = new JRXlsxExporter();

                    paraExcel.setExporterInput(
                            new SimpleExporterInput(reporteJasper));
                    paraExcel.setExporterOutput(
                            new SimpleOutputStreamExporterOutput(salida));

                    SimpleXlsxReportConfiguration configuracion = new SimpleXlsxReportConfiguration();
                    configuracion.setDetectCellType(true);
                    configuracion.setCollapseRowSpan(true);

                    paraExcel.setConfiguration(configuracion);
                    paraExcel.exportReport();

                    mediaType = mediaType.APPLICATION_OCTET_STREAM;
                    archivoSalida = reporte + ".xlsx";
                }
                case "Csv" -> {
                    JRCsvExporter paraCSV= new JRCsvExporter();

                    paraCSV.setExporterInput(
                            new SimpleExporterInput(reporteJasper));
                    paraCSV.setExporterOutput(
                            new SimpleWriterExporterOutput(salida));

                    paraCSV.exportReport();

                    mediaType = mediaType.TEXT_PLAIN;
                    archivoSalida = reporte + ".csv";
                }
            }

            byte[]data=salida.toByteArray();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Disposition",
                    estilo + "filename=\"" + archivoSalida + "\"");
            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentLength(data.length)
                    .contentType(mediaType)
                    .body(new InputStreamResource(
                                    new ByteArrayInputStream(data)));
        } catch (JRException | SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
