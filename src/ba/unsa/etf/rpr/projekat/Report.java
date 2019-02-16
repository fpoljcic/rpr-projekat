package ba.unsa.etf.rpr.projekat;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import net.sf.jasperreports.swing.JRViewer;

import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

public class Report extends JFrame {
    private static final long serialVersionUID = 1L;

    private JasperReport setConfig(Connection conn, Student student, HashMap<String, Object> parameters) throws JRException {
        if (conn == null || student == null)
            throw new IllegalArgumentException("Parametri ne mogu biti null");
        String reportSrcFile = getClass().getResource("/reports/student.jrxml").getFile();
        String reportsDir = getClass().getResource("/reports/").getFile();
        JasperDesign jasperDesign = JRXmlLoader.load(reportSrcFile);

        JRDesignQuery newQuery = new JRDesignQuery();
        newQuery.setText("SELECT NAME, CODE, ECTS, FIRST_NAME || ' ' || LAST_NAME, POINTS, TO_CHAR(GRADE_DATE, 'DD.MM.YYYY'), SCORE " +
                "FROM SUBJECT, GRADE, STUDENT, PROFESSOR, PERSON " +
                "WHERE SUBJECT.ID = SUBJECT_ID AND STUDENT.ID = STUDENT_ID AND GRADE.PROFESSOR_ID = PROFESSOR.ID AND " +
                "PROFESSOR.ID = PERSON.ID AND SCORE IS NOT NULL AND " +
                "STUDENT.ID = " + student.getId() + " ORDER BY GRADE_DATE");
        jasperDesign.setQuery(newQuery);


        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

        // Fields for resources path
        parameters.put("reportsDirPath", reportsDir);
        parameters.put("name", student.getFirstName() + " " + student.getLastName());
        parameters.put("organizationName", "Elektrotehnički fakultet, Sarajevo");
        ArrayList<HashMap<String, Object>> list = new ArrayList<>();
        list.add(parameters);
        return jasperReport;
    }

    public void showStudentReport(Connection conn, Student student) throws JRException {
        HashMap<String, Object> parameters = new HashMap<>();
        JasperReport jasperReport = setConfig(conn, student, parameters);

        JasperPrint print = JasperFillManager.fillReport(jasperReport, parameters, conn);
        JRViewer viewer = new JRViewer(print);
        viewer.setOpaque(true);
        viewer.setVisible(true);
        this.add(viewer);
        this.setSize(700, 500);
        this.setVisible(true);
    }

    public void saveStudentReport(String path, Connection conn, Student student) throws IOException, JRException {
        HashMap<String, Object> parameters = new HashMap<>();
        JasperReport jasperReport = setConfig(conn, student, parameters);

        JasperPrint print = JasperFillManager.fillReport(jasperReport, parameters, conn);
        File file = new File(path);
        OutputStream output = new FileOutputStream(file);
        if (path.contains(".pdf"))
            JasperExportManager.exportReportToPdfStream(print, output);
        else if (path.contains(".docx")) {
            JRDocxExporter exporter = new JRDocxExporter();
            exporter.setExporterInput(new SimpleExporterInput(print));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(file));
            exporter.exportReport();
        } else if (path.contains(".xslx")) {
            JRXlsxExporter exporter = new JRXlsxExporter();
            exporter.setExporterInput(new SimpleExporterInput(print));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(file));
            SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
            configuration.setOnePagePerSheet(true);
            exporter.setConfiguration(configuration);
            exporter.exportReport();
        }
        output.close();
    }
}
