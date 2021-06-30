package ru.netology;

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

    private static List<Employee> parseCSV(String[] columnMapping, String fileName) {
        List<Employee> list = null;
        try (CSVReader csvReader = new CSVReader(new FileReader(fileName))) {
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);
            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(csvReader)
                    .withMappingStrategy(strategy)
                    .build();
            list = csv.parse();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    private static String listToJson(List<Employee> list) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Type listType = new TypeToken<List<Employee>>() {}.getType();
        String json = gson.toJson(list, listType);

        return json;
    }

    private static void writeString(String json, String fileOutput) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(fileOutput);
            writer.append(json);
            writer.close();
            System.out.println("Записан успешно!");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static List<Employee> parseXML(String fileXML) {
        List<Employee> list = new ArrayList<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(fileXML));

            Node root = doc.getDocumentElement();
            NodeList nodeList = root.getChildNodes();

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (Node.ELEMENT_NODE == node.getNodeType()) {

                    NodeList nodeListInn = node.getChildNodes();
                    long id = 0;
                    String firstName ="";
                    String lastName ="";
                    String country ="";
                    int age = 0;

                    for (int x = 0; x < nodeListInn.getLength(); x++) {
                        Node nodeInn = nodeListInn.item(x);

                        if (Node.ELEMENT_NODE == nodeInn.getNodeType()) {
                            if(nodeInn.getNodeName().equals("id") )
                                id = Integer.parseInt (nodeInn.getTextContent());
                            if(nodeInn.getNodeName().equals("firstName"))
                                firstName = nodeInn.getTextContent();
                            if(nodeInn.getNodeName().equals("lastName"))
                                lastName = nodeInn.getTextContent();
                            if(nodeInn.getNodeName().equals("country"))
                                country = nodeInn.getTextContent();
                            if(nodeInn.getNodeName().equals("age"))
                                age = Integer.parseInt (nodeInn.getTextContent());
                        }
                    }
                    Employee employee = new Employee(id, firstName, lastName, country, age);
                    list.add(employee);
                }
            }

        } catch (ParserConfigurationException ex) {
            ex.printStackTrace(System.out);
        } catch (SAXException ex) {
            ex.printStackTrace(System.out);
        } catch (IOException ex) {
            ex.printStackTrace(System.out);
        }

        return list;
    }


    public static void main(String[] args) {

        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileCSV = "C:/Users/reshetnakam/IdeaProjects/CSV-JSON/src/main/java/ru/netology/data.csv";
        String fileOutput = "C:/Users/reshetnakam/IdeaProjects/CSV-JSON/src/main/java/ru/netology/data.json";
        String fileXML = "C:/Users/reshetnakam/IdeaProjects/CSV-JSON/src/main/java/ru/netology/data.xml";
        String fileOutput2 = "C:/Users/reshetnakam/IdeaProjects/CSV-JSON/src/main/java/ru/netology/data2.json";

        //CSV-JSON
        List<Employee> list = parseCSV(columnMapping, fileCSV);
        String json = listToJson(list);
        writeString(json, fileOutput);

        //XML-JSON
        List<Employee> list2 = parseXML(fileXML);
        String json2 = listToJson(list2);
        writeString(json2, fileOutput2);

    }
}