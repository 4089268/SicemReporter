package com.nerus.reporter.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Value("${report.storage.path}")
    private String reportStoragePath;

    @PostMapping
    public ResponseEntity<String> generateReport(@RequestBody List<Map<String, Object>> requestData) {
        try {
            // Load Jasper template
            String templatePath = "path/to/your/SampleReporte.jrxml"; // Update with actual path
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(new File(templatePath));

            // Data source for the report
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(requestData);

            // Parameters for the report (if needed)
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("createdBy", "Report API");

            // Generate the report
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

            // Save the report to a file
            String fileName = UUID.randomUUID() + ".pdf";
            String filePath = reportStoragePath + File.separator + fileName;

            try (OutputStream outputStream = new FileOutputStream(filePath)) {
                JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
            }

            // Return the URL of the generated report
            String reportUrl = "http://your-domain.com/reports/" + fileName;
            return ResponseEntity.ok(reportUrl);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error generating report: " + e.getMessage());
        }
    }
}
