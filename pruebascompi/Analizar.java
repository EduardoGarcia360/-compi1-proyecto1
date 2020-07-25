/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pruebascompi;

import java.util.LinkedList;

/**
 *test2 desde eclipse
 * @author Eduardo Garcia
 */
public class Analizar {
    
    public LinkedList<String> patron = new LinkedList<String>();
    
    public void separar_caracteres(String cadena){
        int estado=0;
        char token;
        String lexema="";
        for(int indice=0; indice < cadena.length(); indice++){
            token = cadena.charAt(indice);
            switch(estado){
                case 0:
                    if(Character.isLetter(token)){
                        lexema += Character.toString(token);
                        int pos = indice+1;
                        try{
                            //valido el siguiente caracter
                            char tmp = cadena.charAt(pos);
                            estado=0;
                            if(tmp=='.' || tmp=='|' || tmp=='*' || tmp=='?' || tmp=='+' || tmp=='"' || Character.isWhitespace(tmp)){
                                //si lo que viene despues es un simbolo o un espacio en blanco
                                //eje. (1): . letra | numero (separado)
                                //eje. (2): .letra|numero (junto)
                                //eje. (3): .letra"-"
                                patron.add(lexema);
                                lexema="";
                                estado=0;
                            } 
                        }catch(Exception e){
                            //fuera de rango ya que no finaliza con un simbolo, por eje.: .letra"-"numero
                            //termina con la letra que ya esta en lexema.
                            patron.add(lexema);
                            lexema="";
                            indice++;
                        }
                        
                    }else if(token=='.' || token=='|' || token=='*' || token=='?' || token=='+'){
                        //cualquier simbolo sera agregado a la lista
                        patron.add(Character.toString(token));
                        estado=0;
                    }else if(token=='"'){
                        //puede ser algo asi: "//_", "-", " algo."...
                        lexema+=Character.toString(token);
                        estado=1;
                    }else{
                        estado=0;
                        lexema="";
                    }
                    break;
                case 1: //para contenido dentro de comillas dobles
                    if(token=='"'){
                        //cuando encuentra la otra comilla lo agrega a la lista
                        lexema+=Character.toString(token);
                        patron.add(lexema);
                        lexema="";
                        estado=0;
                    }else{
                        //se mantiene concatenando todo lo que este dentro de comillas dobles
                        lexema+=Character.toString(token);
                        estado=1;
                    }
                    break;
            }
        }
        
        
        for(int i=0; i<patron.size(); i++){
            System.out.println("pos: "+i+ " -> " + patron.get(i));
        }
        
        
        Estados e = new Estados();
        e.inicio_de_afn(patron);
        
    }
    
    private boolean validar_simbolo_ascii(char token){
        //para validar que los simbolos esten en el rango
        return ((int)token >= 33 && (int)token <= 47) || ((int)token >=58 && (int)token <= 64) || ((int)token >= 91 && (int)token <= 96) || ((int)token >=123 && (int)token <= 125);
    }
    
}
