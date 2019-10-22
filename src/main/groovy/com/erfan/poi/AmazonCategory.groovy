package com.erfan.poi

import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook

class AmazonCategory {

    private static final String FILE_NAME = "C://Users/Erfan/Downloads/au_lighting_browse_tree_guide.xls";
    public static String[] name;
    public static int dataSize;

    public static Map xyz = [:]

    public static Boolean isExist(Map amazonCategories, String key){
        try{
            if (amazonCategories.get(key)){
                return true
            }
        }catch (Exception e){}
        return false
    }

    public static Map mapTo(String[] cats, Integer nodeId,  Map amazonCategories = [:]){
        String cat = cats[0]
        if (!isExist(amazonCategories, cat)){
            amazonCategories.put(cat, [
                    "nodeId" : nodeId,
                    "name": cat,
                    "dbId": 0,
                    "child": [:],
            ])
        }else{
            if (cats.length > 1){
                amazonCategories.get(cat).put("child", mapTo(Arrays.copyOfRange(cats, 1, cats.length), nodeId, amazonCategories.get(cat).child))
            }
        }
       return amazonCategories
    }



    public static void main(String[] args) {

        def myMap = [:]
        println(myMap.getClass())
        Integer nodeId = 0
        try {
            def categoryDetails = { [:].withDefault { owner.call() } }()
            FileInputStream excelFile = new FileInputStream(new File(FILE_NAME));
            Workbook workbook = new HSSFWorkbook(excelFile);
            Sheet datatypeSheet = workbook.getSheetAt(1);
            Iterator<Row> iterator = datatypeSheet.iterator();
            iterator.next()
            while (iterator.hasNext()) {

                Row currentRow = iterator.next();
                Iterator<Cell> cellIterator = currentRow.iterator();
                while (cellIterator.hasNext()) {
                    Cell currentCell = cellIterator.next();

                    if (currentCell.getColumnIndex() == 0) {
                        //getCellTypeEnum shown as deprecated for version 3.15
                        //getCellTypeEnum ill be renamed to getCellType starting from version 4.0
                        if (currentCell.getCellTypeEnum() == CellType.NUMERIC) {
//                            System.out.print(currentCell.getNumericCellValue() + " ");
                            nodeId = new BigDecimal(currentCell.getNumericCellValue())
                        }
                    }
                    if (currentCell.getColumnIndex() == 1) {
                        if (currentCell.getCellTypeEnum() == CellType.STRING) {
//                            System.out.print(currentCell.getStringCellValue() + " ");
                            name = currentCell.getStringCellValue().split("/")
                            xyz =  mapTo(name, nodeId, xyz)
                            println(xyz)

                        }
                    }

                    categoryDetails.child.name = "hello"
                    categoryDetails.dbId = 0

                }

                System.out.println(xyz);

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(xyz);
    }


}
