
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Clasa DataBase
 * @author Dorobantu Razvan Florin - 325CB
 */
public class DataBase {
    
    /**
     * Numele bazei de date
     */
    String name;
    
    /**
     * Numarul de noduri
     */
    int nr_nodes;
    
    /**
     * Capacitatea maxima a unui nod
     */
    int max_capacity;
    
    /**
     * Lista de noduri
     */
    ArrayList<ArrayList<Entity>> nodes;
    
    /**
     * Lista de entitati
     */
    ArrayList<Entity> entities;
    
    /**
     * Constructorul clasei DataBase
     * @param name numele bazei de date
     * @param nr_nodes numarul de noduri
     * @param max_capacity capacitatea maxima a unui nod
     */
    public DataBase(String name, int nr_nodes, int max_capacity)
    {
        this.name = name;
        this.nr_nodes = nr_nodes;
        this.max_capacity = max_capacity;
        entities = new ArrayList<>();
        
        this.nodes = new ArrayList<>(nr_nodes);
        for(int i = 0; i < nr_nodes; i++)
        {
            nodes.add(new ArrayList<>(max_capacity));
        }
    }
    
    /**
     * Metoda care adauga o entitate in lista de entitati
     * @param ent entitatea ce trebuie adaugata
     */
    public void addEntity(Entity ent)
    {
        entities.add(ent);
    }
    
    /**
     * Metoda care cauta o entitate in lista
     * @param name numele entitatii
     * @return pozitia din lista la care se afla entitatea sau -1 daca nu exista 
     */
    public int findEntity(String name)
    {
        for(int i = 0; i < entities.size(); i++)
        {
            if(entities.get(i).name.equals(name))
                return i;
        }
        return -1;
    }
    
    /**
     * Metoda care returneaza o entitate de la un anumit index
     * @param index pozitia din lista la care se afla entitatea
     * @return entitatea respectiva
     */
    public Entity getEntity(int index)
    {
        return entities.get(index);
    }
    
    /**
     * Mteoda care modifica valoarea unui atribut al unei instante 
     * @param type tipul instantei
     * @param key cheia instantei
     * @param attribute_name numele atributului ce trebuie modificat
     * @param attribute_value noua valoare a atributului
     */
    public void updateInstance(String type, String key, String attribute_name, String attribute_value)
    {
        for(int i = 0; i < nr_nodes; i++)
        {
            for(int j = 0; j < nodes.get(i).size(); j++)
            {
                Entity instance = nodes.get(i).get(j);
                
                if(instance.getType().equals(type) && instance.getKey().equals(key))
                {
                    instance.update(attribute_name, attribute_value);
                    instance.updateTimestamp();
                }
            }
        }
    }
    
    /**
     * Metoda care sterge o instanta
     * @param type tipul instantei
     * @param key cheia instantei
     * @param pr PrintWriterul care scrie in fisierul de output
     */
    public void deleteInstance(String type, String key, PrintWriter pr)
    {
        boolean existaInstanta = false;
        for(int i = 0; i < nr_nodes; i++)
        {
            for(int j = 0; j < nodes.get(i).size(); j++)
            {
                Entity instance = nodes.get(i).get(j);
                
                if(instance.getType().equals(type) && instance.getKey().equals(key))
                {
                    nodes.get(i).remove(j);
                    existaInstanta = true;
                }
            }
        }
        
        if(existaInstanta == false)
            pr.println("NO INSTANCE TO DELETE");
    }
    
    /**
     * Metoda care afiseaza informatii dspre o instanta
     * @param type tipul instantei
     * @param key cheia instantei
     * @param pr PritnWriter care scrie in fisierul de output
     */
    public void getInstance(String type, String key, PrintWriter pr)
    {
        Entity instance2 = null;
        boolean existaInstanta = false;
        for(int i = 0; i < nr_nodes; i++)
        {
            for(int j = 0; j < nodes.get(i).size(); j++)
            {
                Entity instance = nodes.get(i).get(j);
                
                if(instance.getType().equals(type) && instance.getKey().equals(key))
                {
                    instance2 = instance;
                    int index = i + 1;
                    
                    if(existaInstanta == true)
                        pr.print(" ");
                    pr.print("Nod" + index);
                    existaInstanta = true;
                }
            }
        }
        
        if(existaInstanta == true)
        {
            pr.print(" " + type);
            if(instance2 != null)
                instance2.displayAttributes(pr);
        }
        else pr.println("NO INSTANCE FOUND");
    }
    
    /**
     * Metoda care returneaza indexul primului nod care nu este plin
     * @return indexul nodului
     */
    public int FreeNode()
    {
        int size;
        for(int i = 0; i < nr_nodes; i++)
        {
            size = nodes.get(i).size();
            if(size != max_capacity)
                return i;
        }
        return nr_nodes;
    }
    
    /**
     * Metoda care adauga o instanta in baza de date
     * @param instance instanta ce trebuie adaugata
     * @param index indexul primului nod care nu este plin
     */
    public void addInNode(Entity instance, int index)
    {
        if(instance.getRf() >= nr_nodes - index + 1)
        {
            for(int i = nr_nodes - index; i < instance.getRf(); i++)
            {
                nodes.add(new ArrayList<>(max_capacity));
                nr_nodes++;
            }
        }
            
        //if(instance.getRf() <= nr_nodes - index + 1)
        //{
            for(int i = 0; i < instance.getRf(); i++)
            {
                Entity instance_clone = instance.cloneInstance();
                nodes.get(index + i).add(0, instance_clone);
            }
        //}
    }
    
    /**
     * Metoda care afiseaza baza de date
     * @param pr PrintWriterul care scrie in fisier
     */
    public void displayAll(PrintWriter pr)
    {
        boolean existaElement = false;
        for(int i = 0; i < nr_nodes; i++)
        {
            if(nodes.get(i).size() > 0)
            {
                existaElement = true;
                int nr_nod = i + 1;
                pr.println("Nod" + nr_nod);
            
                for(int j = 0; j < nodes.get(i).size(); j++)
                {
                    Entity instance = nodes.get(i).get(j);
                    instance.display(pr);
                }
            }
        }
        if(existaElement == false)
            pr.println("EMPTY DB");
    }
    
    /**
     * Metoda care sorteaza instantele din noduri
     */
    public void sortNodes()
    {
        for(int i = 0; i < nr_nodes; i++)
        {
            Collections.sort(nodes.get(i));
        }
    }
}
