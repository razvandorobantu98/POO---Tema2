
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Clasa Tema2 (Main)
 * @author Dorobantu Razvan Florin - 325CB
 */
public class Tema2 {
    
    public static void main(String[] args)
    {
        //Fisierul input
        String FILE_INPUT = args[0];
        
        //Fisierul output
        String FILE_OUTPUT = FILE_INPUT + "_out";
        
        //Baza de date
        DataBase data_base = null;
        
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_INPUT));
                FileWriter fileWriter = new FileWriter(FILE_OUTPUT);
                PrintWriter printWriter = new PrintWriter(fileWriter);) 
        {
            String currentLine;
            String[] tokens;
            
            //CITESTE PRIMA LINIE
            if(br.ready())
            {
                currentLine = br.readLine();
                tokens = currentLine.split(" ");
                
                if(tokens[0].equals("CREATEDB"))
                {
                    //CREEAZA BAZA DE DATE
                    String nume = tokens[1];
                    int nr_nodes = Integer.parseInt(tokens[2]);
                    int max_capacity = Integer.parseInt(tokens[3]);
                    
                    data_base = new DataBase(nume, nr_nodes, max_capacity);
                }
            }
            
            //CITESTE URMATOARELE LINII
            while(br.ready() ==  true && data_base != null)
            {
                currentLine = br.readLine();
                tokens = currentLine.split(" ");
                
                if(tokens[0].equals("CREATE"))
                {
                    //CREEAZA O NOUA ENTITATE
                    String name = tokens[1];
                    int rf = Integer.parseInt(tokens[2]);
                    int nr_attributes = Integer.parseInt(tokens[3]);
                    Entity ent = new Entity(name, rf, nr_attributes);
                    
                    for(int i = 0; i < nr_attributes; i++)
                    {
                        String attribute_name = tokens[4 + 2 * i];
                        String attribute_type = tokens[5 + 2 * i];
                        ent.addAttribute(attribute_name, attribute_type);
                    }
                    data_base.addEntity(ent);
                }
                else if(tokens[0].equals("INSERT"))
                {
                    //CREEAZA O INSTANTA
                    String type = tokens[1];
                    int idx = data_base.findEntity(type);
                    
                    Entity ent = data_base.getEntity(idx);
                    Entity instance = ent.cloneEntity();
                    int nr_attributes = ent.getAttributesNr();
                    
                    for(int i = 0; i < nr_attributes; i++)
                    {
                        instance.addAttributeValue(tokens[2 + i]);
                    }
                    int idx_max_node = data_base.FreeNode();
                    
                    //ADAUGA INSTANTA IN BAZA DE DATE
                    data_base.addInNode(instance, idx_max_node);
                }
                else if(tokens[0].equals("SNAPSHOTDB"))
                {
                    //AFISEAZA BAZA DE DATE
                    data_base.displayAll(printWriter);
                }
                else if(tokens[0].equals("UPDATE"))
                {
                    //MODIFICA VALOAREA ATRIBUTULUI UNEI INSTANTE
                    String type = tokens[1];
                    String key = tokens[2];
                    String attribute_name = tokens[3];
                    String attribute_value = tokens[4];
                    
                    data_base.updateInstance(type, key, attribute_name, 
                            attribute_value);
                    
                    //SORTEAZA NODURILE
                    data_base.sortNodes();
                }
                else if(tokens[0].equals("DELETE"))
                {
                    //STERGE O INSTANTA DIN BAZA DE DATE
                    String type = tokens[1];
                    String key = tokens[2];
                    
                    data_base.deleteInstance(type, key, printWriter);
                }
                else if(tokens[0].equals("GET"))
                {
                    //AFISEAZA INFORMATII DESPRE O INSTANTA
                    String type = tokens[1];
                    String key = tokens[2];
                    
                    data_base.getInstance(type, key, printWriter);
                }
            }
            printWriter.close();
            
        }
        catch (IOException e) {
            e.getStackTrace();
	}
        
    }
    
}
