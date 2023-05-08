package ru.trips.service.attractions.common;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.DefaultIndexedColorMap;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Набор стилей для отчётов.
 */
public class ExcelStyles {

    /**
     * шрифт Times New Roman.
     */
    public static final String TIMES_NEW_ROMAN = "Times New Roman";

    /**
     * Стиль для титула.
     *
     * @param workbook - {@link XSSFWorkbook}
     * @return CellStyle - {@link CellStyle}
     */
    public CellStyle createTitleStyle(XSSFWorkbook workbook) {
        final XSSFCellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(
            new XSSFColor(new byte[]{(byte) 224, (byte) 240, (byte) 255}, new DefaultIndexedColorMap()));
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        var font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 12);
        font.setFontName(TIMES_NEW_ROMAN);
        style.setFont(font);
        style.setWrapText(true);
        setFullBorderToStyle(style);
        return style;
    }

    /**
     * Стиль заголовка для карточек.
     *
     * @param workbook - {@link XSSFWorkbook}
     * @return CellStyle - {@link CellStyle}
     */
    public CellStyle createCardTitleStyle(XSSFWorkbook workbook) {
        final XSSFCellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(new XSSFColor(new byte[]{(byte) 48, (byte) 84, (byte) 150},
            new DefaultIndexedColorMap()));
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        var font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 13);
        font.setColor(new XSSFColor(new byte[]{(byte) 255, (byte) 255, (byte) 255},
            new DefaultIndexedColorMap()));
        font.setFontName(TIMES_NEW_ROMAN);
        style.setFont(font);
        style.setWrapText(true);
        setFullBorderToStyle(style);
        return style;
    }

    /**
     * Стиль для сабтитула.
     *
     * @param workbook - {@link XSSFWorkbook}
     * @return CellStyle - {@link CellStyle}
     */
    public CellStyle createSubTitleStyle(XSSFWorkbook workbook) {
        final XSSFCellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(
            new XSSFColor(new byte[]{(byte) 237, (byte) 244, (byte) 255}, new DefaultIndexedColorMap()));
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        var font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 12);
        font.setFontName(TIMES_NEW_ROMAN);
        style.setFont(font);
        style.setWrapText(true);
        return style;
    }

    /**
     * Стиль для ячеек заголовка.
     *
     * @param workbook - {@link XSSFWorkbook}
     * @return CellStyle - {@link CellStyle}
     */
    public static CellStyle createHeaderStyle(XSSFWorkbook workbook) {
        final XSSFCellStyle style = workbook.createCellStyle();

        style.setFillForegroundColor(
            new XSSFColor(new byte[]{(byte) 243, (byte) 242, (byte) 242}, new DefaultIndexedColorMap()));
        style.setWrapText(true);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        setFullBorderToStyle(style);
        var font = workbook.createFont();
        font.setBold(true);
        font.setFontName(TIMES_NEW_ROMAN);
        style.setFont(font);
        return style;
    }

    /**
     * Стиль для ячеек с данными.
     *
     * @param workbook - {@link XSSFWorkbook}
     * @return CellStyle - {@link CellStyle}
     */
    public static CellStyle createCellStyle(XSSFWorkbook workbook) {
        final XSSFCellStyle style = workbook.createCellStyle();
        style.setWrapText(true);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        var font = workbook.createFont();
        font.setFontName(TIMES_NEW_ROMAN);
        style.setFont(font);
        return style;
    }

    /**
     * Стиль для нижнего колонтитула.
     *
     * @param workbook - {@link XSSFWorkbook}
     * @return CellStyle - {@link CellStyle}
     */
    public CellStyle createFooterStyle(XSSFWorkbook workbook) {
        final XSSFCellStyle style = workbook.createCellStyle();

        style.setFillForegroundColor(
            new XSSFColor(new byte[]{(byte) 243, (byte) 242, (byte) 242}, new DefaultIndexedColorMap()));
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.RIGHT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        setFullBorderToStyle(style);
        var font = workbook.createFont();
        font.setFontName(TIMES_NEW_ROMAN);
        style.setFont(font);
        return style;
    }

    private static void setFullBorderToStyle(XSSFCellStyle style) {
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
    }
}
