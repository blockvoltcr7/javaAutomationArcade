package JSONWerk;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.*;

/**
 * ExcelToJson1 class is responsible for converting Excel (.xlsx) files to JSON format.
 * It reads an Excel file, processes each sheet, and creates a JSON structure
 * representing the data from all sheets.
 */
public class ExcelToJson1 {

    /**
     * Main method to execute the Excel to JSON conversion process.
     *
     * @param args Command line arguments (not used)
     */
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

    /**
     * Converts an Excel file to a Map representation suitable for JSON conversion.
     *
     * @param excelFilePath Path to the Excel file
     * @return A Map where keys are sheet names and values are lists of row data
     * @throws IOException If there's an error reading the Excel file
     */
    private static Map<String, List<Map<String, Object>>> convertExcelToJson(String excelFilePath) throws IOException {
        Map<String, List<Map<String, Object>>> dataset = new HashMap<>();

        try (Workbook workbook = new XSSFWorkbook(new File(excelFilePath))) {
            // Iterate through each sheet in the workbook
            for (Sheet sheet : workbook) {
                List<Map<String, Object>> sheetData = new ArrayList<>();
                Iterator<Row> rowIterator = sheet.iterator();

                // Get column names from the first row (header row)
                Row headerRow = rowIterator.next();
                List<String> columnNames = new ArrayList<>();
                for (Cell cell : headerRow) {
                    columnNames.add(cell.getStringCellValue());
                }

                // Process data rows
                int rowNumber = 1;
                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();
                    if (isRowEmpty(row)) {
                        break; // Stop processing if an empty row is encountered
                    }

                    Map<String, Object> rowData = new LinkedHashMap<>();
                    // Iterate through each cell in the row
                    for (int i = 0; i < columnNames.size(); i++) {
                        Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                        rowData.put(columnNames.get(i), getCellValue(cell));
                    }

                    // Add row number to the row data
                    rowData.put("rowNumber", rowNumber);
                    sheetData.add(rowData);
                    rowNumber++;
                }

                // Add the sheet data to the dataset
                dataset.put(sheet.getSheetName(), sheetData);
            }
        } catch (InvalidFormatException e) {
            throw new RuntimeException("Invalid Excel file format", e);
        }

        return dataset;
    }

    /**
     * Extracts the value from a cell based on its type.
     *
     * @param cell The cell to extract value from
     * @return The value of the cell as an Object
     */
    private static Object getCellValue(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                // Check if the numeric value is a date
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    return String.valueOf(cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                // Evaluate the formula and return the result as a string
                FormulaEvaluator evaluator = cell.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
                CellValue cellValue = evaluator.evaluate(cell);
                return cellValue.formatAsString();
            default:
                return ""; // Return empty string for blank or error cells
        }
    }

    /**
     * Checks if a row is empty.
     *
     * @param row The row to check
     * @return True if the row is empty, false otherwise
     */
    private static boolean isRowEmpty(Row row) {
        for (int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++) {
            Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                return false;
            }
        }
        return true;
    }

    /**
     * Writes the dataset to a JSON file with a unique name.
     *
     * @param dataset The dataset to write
     * @param outputDirectory The directory to write the JSON file to
     * @throws IOException If there's an error writing the file
     */
    private static void writeJsonToFile(Map<String, List<Map<String, Object>>> dataset, String outputDirectory) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String uniqueFileName = outputDirectory + "/dataset_" + UUID.randomUUID().toString() + ".json";

        // Create the JSON structure with "dataset" as the root key
        Map<String, Object> jsonStructure = Collections.singletonMap("dataset", dataset);

        // Write the JSON to a file
        mapper.writeValue(new File(uniqueFileName), jsonStructure);
        System.out.println("JSON file created: " + uniqueFileName);
    }
}