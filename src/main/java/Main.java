import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";
        List<Employee> list = parseCSV(columnMapping, fileName);
        listToJson(list);

        String fileName2 = "data.xml";
        List<Employee> list2 = parseXML(fileName2);
        listToJson(list2);
    }

    public static List<Employee> parseCSV(String[] columnMapping, String fileName){
        try(CSVReader reader = new CSVReader(new FileReader(String.valueOf(fileName)))){
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);

            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(reader)
                    .withMappingStrategy(strategy)
                    .build();
            return csv.parse();
        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public static List<Employee> parseXML (String fileName) throws IOException, SAXException, ParserConfigurationException {
        List<Employee> staff = new ArrayList<>();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new File(fileName));
        doc.getDocumentElement().normalize();
        System.out.println("Root element " + doc.getDocumentElement().getNodeName());
        NodeList nodeLst = doc.getElementsByTagName("employee");
        System.out.println("Information of all employees");

        for (int s = 0; s < nodeLst.getLength(); s++) {
            Node fstNode = nodeLst.item(s);
            if (fstNode.getNodeType() == Node.ELEMENT_NODE) {

                Element fstElmnt = (Element) fstNode;
                NodeList fstNmElmntLst = fstElmnt.getElementsByTagName("id");
                Element fstNmElmnt = (Element) fstNmElmntLst.item(0);
                NodeList fstNm = fstNmElmnt.getChildNodes();
                String ID = ((Node) fstNm.item(0)).getNodeValue();
                System.out.println("id : " + ID);

                //

                NodeList lstNmElmntLst2 = fstElmnt.getElementsByTagName("firstName");
                Element lstNmElmnt2 = (Element) lstNmElmntLst2.item(0);
                NodeList lstNm2 = lstNmElmnt2.getChildNodes();
                String FirstName = ((Node) lstNm2.item(0)).getNodeValue();
                System.out.println("firstName : " + FirstName);
                //

                NodeList lstNmElmntLst3 = fstElmnt.getElementsByTagName("lastName");
                Element lstNmElmnt3 = (Element) lstNmElmntLst3.item(0);
                NodeList lstNm3 = lstNmElmnt3.getChildNodes();
                String LastName = ((Node) lstNm3.item(0)).getNodeValue();
                System.out.println("lastName : " + LastName);
                //

                NodeList lstNmElmntLst4 = fstElmnt.getElementsByTagName("country");
                Element lstNmElmnt4 = (Element) lstNmElmntLst4.item(0);
                NodeList lstNm4 = lstNmElmnt4.getChildNodes();
                String Country = ((Node) lstNm4.item(0)).getNodeValue();
                System.out.println("country : " + Country);
                //

                NodeList lstNmElmntLst5 = fstElmnt.getElementsByTagName("age");
                Element lstNmElmnt5 = (Element) lstNmElmntLst5.item(0);
                NodeList lstNm5 = lstNmElmnt5.getChildNodes();
                String Age = ((Node) lstNm5.item(0)).getNodeValue();
                System.out.println("age : " + Age);

                Employee employee = new Employee(Long.parseLong(ID), FirstName, LastName, Country, Integer.parseInt(Age));
                staff.add(employee);

            }
        }
        return staff;
    }

    public static void listToJson(List<Employee> staff){
        Type listType = new TypeToken<List<Employee>>() {}.getType();
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();
        String json = gson.toJson(staff, listType);
        writeString(json);
    }

    public static void writeString(String json){
        try (FileWriter file = new FileWriter("data2.json")){
            file.write(json);
            file.flush();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
