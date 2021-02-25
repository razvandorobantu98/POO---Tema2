
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Clasa Entity
 * <p>
 * (Am folosit aceasta clasa atat pentru obiectele de tip Entity cat si pentru 
 * obiectele de tip Instance)
 * @author Dorobantu Razvan Florin - 325CB
 */
public class Entity implements Comparable<Entity> {
    
    /**
     * Tipul entitatii/instantei
     */
    String name;
    
    /**
     * Numarul de atribute
     */
    int nr_attributes;
    
    /**
     * Factorul de replicare
     */
    int rf;
    
    /**
     * Lista cu numele atributelor
     */
    ArrayList<String> attributes;
    
    /**
     * Lista cu tipurile atributelor
     */
    ArrayList<String> attributes_type;
    
    /**
     * Lista cu valorile atributelor
     * <p>
     * (Doar instantele o vor avea. In cazul entitatilor va fi nula.)
     */
    ArrayList<String> attributes_value;
    
    /**
     * Timestamp-ul (doar pentru instante) 
     */
    int timestamp;
    
    /**
     * Numarul ce retine la ce timestamp s-a ajuns
     */
    static int t_idx = 0;
    
    /**
     * Constructorul clasei Entity
     * @param nume tipul
     * @param rf factorul de replicare 
     * @param nr_attributes numarul de atribute
     */
    public Entity(String nume, int rf, int nr_attributes)
    {
        this.name = nume;
        this.nr_attributes = nr_attributes;
        this.rf = rf;
        this.timestamp = ++t_idx;
        attributes = new ArrayList<>();
        attributes_type = new ArrayList<>();
        attributes_value = new ArrayList<>();
    }
    
    /**
     * Metoda care adauga un atribut unei entitati/instante
     * @param attribute numele atributului
     * @param type tipul atributului
     */
    public void addAttribute(String attribute, String type)
    {
        attributes.add(attribute);
        attributes_type.add(type);
    }
    
    /**
     * Metoda care adauga valoarea unui atribut al unei instante
     * @param value valoarea atributului
     */
    public void addAttributeValue(String value)
    {
        attributes_value.add(value);
    }
    
    /**
     * Metoda care returneaza numarul de atribute
     * @return numarul de atribute
     */
    public int getAttributesNr()
    {
        return nr_attributes;
    }
    
    /**
     * Metoda care creeaza o clona a entitatii
     * @return clona entitatii
     */
    public Entity cloneEntity()
    {
        Entity ent = new Entity(this.name, this.rf, this.nr_attributes);
        for(int i = 0; i < this.nr_attributes; i++)
        {
            ent.addAttribute(this.attributes.get(i), this.attributes_type.get(i));
        }
        return ent;
    }
    
    /**
     * Metoda care creeaza o clona  a instantei
     * @return clona instantei
     */
    public Entity cloneInstance()
    {
        Entity inst = new Entity(this.name, this.rf, this.nr_attributes);
        inst.setTimestamp(this.timestamp);
        for(int i = 0; i < this.nr_attributes; i++)
        {
            inst.addAttribute(this.attributes.get(i), this.attributes_type.get(i));
            inst.addAttributeValue(this.attributes_value.get(i));
        }
        return inst;
    }
    
    /**
     * Metoda care seteaza un timestamp
     * @param timestp timestampul la care s-a ajuns
     */
    public void setTimestamp(int timestp)
    {
        timestamp = timestp;
    }
    
    /**
     * Metoda care returneaza factorul de replicare
     * @return factorul de replicare
     */
    public int getRf()
    {
        return rf;
    }
    
    /**
     * Metoda care afiseaza detalii despre instanta
     * @param pr PrintWriter care scrie in fisier
     */
    public void display(PrintWriter pr)
    {
        pr.print(name);
        this.displayAttributes(pr);
    }
    
    /**
     * Metoda care afiseaza atributele unei instante 
     * @param pr PrintWriter care scrie in fisier
     */
    public void displayAttributes(PrintWriter pr)
    {
        for(int i = 0; i < attributes.size(); i++)
        {
            if(attributes_type.get(i).equals("Float"))
            {
                double number = Double.parseDouble(attributes_value.get(i));
                DecimalFormat form = new DecimalFormat("#.##");
                pr.print(" " + attributes.get(i) + ":" + form.format(number));
            }
            else
                pr.print(" " + attributes.get(i) + ":" + attributes_value.get(i));
        }
        pr.println();
    }
    
    /**
     * Metoda care returneaza tipul entitatii/instantei
     * @return tipul
     */
    public String getType()
    {
        return name;
    }
    
    /**
     * Metoda care returneaza cheia entitatii/instantei
     * @return cheia
     */
    public String getKey()
    {
        return attributes_value.get(0);
    }
    
    /**
     * Metoda care actualizeaza valoarea unui atribut
     * @param attribute numele atributului
     * @param value valoarea atributului
     */
    public void update(String attribute, String value)
    {
        for(int i = 0; i < nr_attributes; i++)
        {
            if(attributes.get(i).equals(attribute))
            {
                attributes_value.set(i, value);
            }
        }
    }
    
    /**
     * Metoda care actualizeaza timestampul
     */
    public void updateTimestamp()
    {
        timestamp = ++t_idx;
    }
    
    /**
     * Metoda care returneaza timestmap-ul entitatii
     * @return timestamp
     */
    public int getTimestamp()
    {
        return timestamp;
    }
    
    @Override
    /**
     * Metoda care compara timestampul a doua instante
     */
    public int compareTo(Entity ent)
    {
        int tsp1 = ent.getTimestamp();
        int tsp2 = this.timestamp;
        
        if(tsp1 > tsp2)
            return 1;
        else if(tsp1 < tsp2)
            return -1;
        else return 0;
    }
    
}
