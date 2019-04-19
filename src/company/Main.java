package company;

import Data.CreateJSONFile_Parts;
import Data.Struct;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

public class Main {


    static void HP_comp()
    {
        final String url="https://store.hp.com/us/en/vwa/laptops/segm=Home?jumpid=ma_lt_featured_na_6_181216";
        try {
            final Document document= Jsoup.connect(url).get();
            for(Element row : document.select(
                    "div.specsContent")){
                final String ticker=row.select("div.pdtName").text();
                //final String price =row.select("div.specsContent").text();
                //System.out.println(price);
                System.out.println("\n\n*********************************");

                System.out.println("Laptop: " + ticker);

                System.out.println("*********************************");
                for(Element att : row.select(
                        "div.pdtSpecsAttr")){
                    final String attri=att.select("div.attr").text();
                    final String valatt=att.select("div.attrVal").text();
                    if(attri.equals("Operating system")||attri.equals("Storage") ||attri.equals("Processor and graphics")||attri.equals("Memory")) {
                        System.out.println(att.select("div.attr").text() + ": " + att.select("div.attrVal").text());
                    }
                }
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }



    public static void main(String[] args) {
        List<Laptop> LaptopArray = new ArrayList<Laptop>();
        List<String> graphics= new ArrayList<String>();
        List<Struct> CPU_List = new ArrayList<Struct>();
        List<Struct> GPU_List = new ArrayList<Struct>();

        //HP_comp();
        Lenovo.Find_Laptops(LaptopArray);
        Acer.Find_Laptops(LaptopArray);
        //Dell.FindDellLaptops(LaptopArray);
        //LG.FindLGLaptops(LaptopArray);
        //CreateJSONFile.writeList(LaptopArray);
        //printAllLeptops(LaptopArray);
        GPU_List = Data.PartsData.Parse_Data("https://www.videocardbenchmark.net/gpu_list.php");
        CPU_List = Data.PartsData.Parse_Data("https://www.cpubenchmark.net/cpu_list.php");
        CreateJSONFile_Parts.WriteToJsonFile(CPU_List,"cpu_list.json");
        CreateJSONFile_Parts.WriteToJsonFile(GPU_List,"gpu_list.json");
        //printAllExist(LaptopArray,CPU_List,"CPU");
        printAllExist(LaptopArray,GPU_List,"GPU");
        //printAllExist(LaptopArray,GPU_List);
    }

    private static void printAllLeptops(List<Laptop> laptopArray) {
        for (Laptop laptop : laptopArray)
        {
            laptop.printLaptop();
        }
    }

    private static void printAllExist(List<Laptop> laptopArray,List<Struct> PartsList,String whichPart) {
        int count;
        List<Laptop> notFound=new ArrayList<Laptop>();
        List<Laptop> Found=new ArrayList<Laptop>();
        List<String> allreadyPrinted = new ArrayList<String>();

        for (Laptop laptop : laptopArray)
        {
            count = 0;
            if(laptop.getGpu().getModel().equals("Radeon R5 Graphics"))
            {
                System.out.println(laptop.getUrl_model());
            }
//            System.out.println("######################################\n");
//            if(whichPart.equals("CPU"))
//            {
//                System.out.println(laptop.getProcessor().getManufacture() + " "
//                    + laptop.getProcessor().getModel());
//            }
//            else
//            {
//                System.out.println(laptop.getGpu().getManufacture() + " "
//                        + laptop.getGpu().getModel());
//            }
//            System.out.println("#######\n");
            for(Struct part : PartsList) {
                if(whichPart.equals("CPU")) {
                    if (part.getName().contains(laptop.getProcessor().getModel())) {
                        count += 1;
                        //System.out.println(part.getName() + "............" + part.getPlace());
                    }
                }
                else
                {
                    if (part.getName().contains(laptop.getGpu().getModel())) {
                        count += 1;
                        //System.out.println(part.getName() + "............" + part.getPlace());
                    }
                }

            }
            //System.out.println("######################################\n");
            if(count==0)
                notFound.add(laptop);
            else
                Found.add(laptop);

        }
        boolean isther;
        for(Laptop laptop : notFound)
        {
            isther=false;
            for(String not : allreadyPrinted)
            {
                if(whichPart.equals("CPU")) {
                    if (laptop.getProcessor().getModel().equals(not)) {
                        isther = true;
                    }
                }
                else
                {
                    if (laptop.getGpu().getModel().equals(not)) {
                        isther = true;
                    }
                }
            }
            if(!isther) {
                if(whichPart.equals("CPU")) {
                    allreadyPrinted.add(laptop.getProcessor().getModel());
                    System.out.println(laptop.getProcessor().getModel());
                }
                else
                {
                    allreadyPrinted.add(laptop.getGpu().getModel());
                    System.out.println(laptop.getGpu().getModel());
                }
            }
        }
    }
}
