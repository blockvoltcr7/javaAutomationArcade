package JSONWerk;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.*;

public class ExcelToJson1 {

    public static void main(String[] args) {
        String excelFilePath = "src/test/java/JSONWerk/excel/excel_data.xlsx";
        String outputDirectory = "src/test/java/JSONWerk/output";

        try {
            Map<String, List<Map<String, Object>>> dataset = convertExcelToJson(excelFilePath);
            writeJsonToFile(dataset, outputDirectory);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Map<String, List<Map<String, Object>>> convertExcelToJson(String excelFilePath) throws IOException {
        Map<String, List<Map<String, Object>>> dataset = new HashMap<>();

        try (Workbook workbook = new XSSFWorkbook(new File(excelFilePath))) {
            for (Sheet sheet : workbook) {
                List<Map<String, Object>> sheetData = new ArrayList<>();
                Iterator<Row> rowIterator = sheet.iterator();

                // Get column names from the first row
                Row headerRow = rowIterator.next();
                List<String> columnNames = new ArrayList<>();
                for (Cell cell : headerRow) {
                    columnNames.add(cell.getStringCellValue());
                }

                // Process data rows
                int rowNumber = 1;
                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();
                    Map<String, Object> rowData = new LinkedHashMap<>();

                    for (int i = 0; i < columnNames.size(); i++) {
                        Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                        rowData.put(columnNames.get(i), getCellValue(cell));
                    }

                    rowData.put("rowNumber", rowNumber);
                    sheetData.add(rowData);
                    rowNumber++;
                }

                dataset.put(sheet.getSheetName(), sheetData);
            }
        } catch (InvalidFormatException e) {
            throw new RuntimeException(e);
        }

        return dataset;
    }

    private static Object getCellValue(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue();
                } else {
                    return cell.getNumericCellValue();
                }
            case BOOLEAN:
                return cell.getBooleanCellValue();
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }

    private static void writeJsonToFile(Map<String, List<Map<String, Object>>> dataset, String outputDirectory) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String uniqueFileName = outputDirectory + "/dataset_" + UUID.randomUUID().toString() + ".json";
        mapper.writeValue(new File(uniqueFileName), Collections.singletonMap("dataset", dataset));
        System.out.println("JSON file created: " + uniqueFileName);
    }
}