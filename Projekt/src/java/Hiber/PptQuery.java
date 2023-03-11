/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Hiber;

import java.sql.Connection;
import java.sql.Blob;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import org.hibernate.Session;
import java.util.List;
import javax.imageio.spi.ServiceRegistry;
import javax.sql.rowset.serial.SerialBlob;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistryBuilder;
import javax.servlet.http.Part;
import org.hibernate.Hibernate;



/**
 *
 * @author student
 */
public class PptQuery {
    
    private Session session = null;
    private List<Ppt> listaPpt = null;
    private Query q = null;
    
    public List<Ppt> listaPpt(){
        try {
            org.hibernate.Transaction tx = session.beginTransaction();
            q = session.createQuery("from Ppt");
            listaPpt = (List<Ppt>) q.list();
            session.close();
            tx.commit();
            } catch (HibernateException e) {
            }
        return listaPpt;
    }
    
    public String showPptList(){
        List<Ppt> lista;
        String wiersz;
        wiersz = ("<table><tr>");
        wiersz = wiersz.concat(
                 "<td><b>ID</b></td>"
                +"<td><b>FILE</b></td>"
                +"<td><b>DESCRIPTION</b></td>");
        wiersz = wiersz.concat("</tr>");
        lista = listaPpt();
        for (Ppt pliczkiPpt : lista){
            wiersz = wiersz.concat("<tr>");
            wiersz = wiersz.concat("<td>" + pliczkiPpt.getId()+ "</a></td>");
            wiersz = wiersz.concat("<td>" + pliczkiPpt.getNazwa()+ "</td>");   
            wiersz = wiersz.concat("<td>" + pliczkiPpt.getOpis()+"</td>");
            wiersz = wiersz.concat("</tr>");   
        }
        wiersz = wiersz.concat("</table>");
    return wiersz;
    }

        
    public int najwiekszeId(){
        List<Ppt> lista = listaPpt();
        List<Integer> listaId= new ArrayList<>();
        
        if(lista.isEmpty()){
            return 0;
        }else{
            for (Ppt pliki : lista){
                int najwiekszeId = pliki.getId();
                listaId.add(najwiekszeId);
            }
            int najwiekszeIdZListy = java.util.Collections.max(listaId);
            return najwiekszeIdZListy;
        }
        
        
    }

    public int walidacjaCzyIstaniejeId(int id){
        //try{
        org.hibernate.Transaction tx = session.beginTransaction();
        Ppt pliki = (Ppt)session.get(Ppt.class, id);
        session.close();
        
        if(pliki == null){
            return 1;
        }else{
            return 0;
        }
        
        //} catch (HibernateException e){
        //}
    }
    
    public void deletePptFile(int id){
        try{
        org.hibernate.Transaction tx = session.beginTransaction();
        Ppt pliki = (Ppt)session.get(Ppt.class, id);
        session.delete(pliki);   
        session.getTransaction().commit();
        session.close();
        tx.commit();    
        } catch (HibernateException e){
        }
        
    }
    
    public static Blob zamianaFileNaBlob(File file) throws SQLException, IOException{
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] bFile = new byte[(int) file.length()];
        fileInputStream.read(bFile);
        fileInputStream.close();
        return new javax.sql.rowset.serial.SerialBlob(bFile);
    }
        
    
    public int uploadFile(Part filePart, String nazwaPliku, int id, String opis){
        if(nazwaPliku.endsWith("ppt") || nazwaPliku.endsWith("pptx")){
        
            try {
            org.hibernate.Transaction tx = session.beginTransaction();
            
            InputStream fileContent = filePart.getInputStream();
            Blob fileBlob = Hibernate.getLobCreator(session).createBlob(fileContent, filePart.getSize());
            
            Ppt plikiPpt = new Ppt();
            
            plikiPpt.setId(id);
            plikiPpt.setNazwa(nazwaPliku);
            plikiPpt.setDane(fileBlob);
            plikiPpt.setOpis(opis);
            
            
            session.save(plikiPpt);
            session.getTransaction().commit();
            session.close();
            tx.commit();

                         
            }catch (Exception e) {
                 //e.printStackTrace();
            }
            
        }else{
            return 1;
        }
        return 0;
    }
    
    public byte[] zamianaBlobNaByte(Blob blob) throws SQLException, IOException{
        byte[] byteArray = null;
        try (InputStream inputStream = blob.getBinaryStream()) {
            byteArray = new byte[(int) blob.length()];
            inputStream.read(byteArray);
        }
        return byteArray;
    }
    
   
    
    
    public void downloadFile(int id) throws SQLException, IOException {
        try{
            
            org.hibernate.Transaction tx = session.beginTransaction();
            Ppt plikppt = (Ppt)session.get(Ppt.class, id);
            Blob dane = plikppt.getDane();
            String nazwaPliku = plikppt.getNazwa();
        
            InputStream inputStream = dane.getBinaryStream();

            String katalogPobieraniaSciezka = Paths.get(System.getProperty("user.home"), "Downloads").toString();

            String sciezkaPlik = katalogPobieraniaSciezka+File.separator+nazwaPliku;
            FileOutputStream fos = new FileOutputStream(sciezkaPlik);

            int daneDlugosc = (int)dane.length();
            byte[] buffer = new byte[daneDlugosc];
            int bytesRead = -1;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
            
            tx.commit();
            session.close();   
            
        }catch(Exception e){
            
        }   
        
             
    }
    
    
    public PptQuery(){
        
        this.session = HibernateUtil.getSessionFactory().getCurrentSession();
    }
}
