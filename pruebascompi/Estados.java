/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pruebascompi;

import java.util.LinkedList;

/**
 *
 * @author Eduardo Garcia
 */
public class Estados {
    
    int estado_global=0; //contador para cualquier metodo
    String todo=""; //aca se almacenaran todas las relaciones de estados
    /**
     * la lista de contenido, para esta entrada:
     * .letra*|letra|digito"-"
     * quedaria de esta forma:
     * [.][letra][*][|][letra][|][digito]["-"]
     * @param contenido : lista previamente separada.
     */
    
    public void inicio_de_afn(LinkedList<String> contenido){
        //obtengo el primer dato
        int estado_inicio=0;
        String top = contenido.get(0);
        String a, b, tmp;
        switch(top){
            case ".":
                //como es concatenacion, obtengo el siguiente elemento
                a = contenido.get(1);
                tmp = metodo_concatenar(contenido, a, 1, estado_inicio);
                mostrar(tmp);
                break;
            case "|":
                a = contenido.get(1);
                b = contenido.get(2);
                tmp = metodo_or(contenido, a, b, 2, estado_inicio, 0, 1);
                mostrar(tmp);
                break;
        }
        
    }
   
    //***-----------------------cada metodo retorna las relaciones
        
    private String metodo_concatenar(LinkedList<String> contenido, String transicion_a, int posicion_en_lista, int estado_qn){
        //.ab
        int inicio_concat, intermedio_concat=0, final_concat; //posiciones de estado para guiarme
        String concatenacion; //parte_a + parte_b
        
        //parte de A
        String parte_a="", aa, ab;
        inicio_concat=estado_qn;
        switch(transicion_a){
            case ".":
                aa = contenido.get(posicion_en_lista+1);
                posicion_en_lista++;
                parte_a = metodo_concatenar(contenido, aa, posicion_en_lista, estado_qn);
                break;
            case "|":
                //como es or envio los siguientes dos datos en la lista
                posicion_en_lista++;
                aa = contenido.get(posicion_en_lista);
                posicion_en_lista++;
                ab = contenido.get(posicion_en_lista);
                /*
                los datos retornados serian: segundo_b_or<%>final_or<%>relaciones
                segundo_b_or: donde continuara numerando el siguiente estado_qn (0)
                final_or: donde empezara el siguiente estado (1)
                relaciones: codigo de graphviz (2)
                */
                String tmp = metodo_or(contenido, aa, ab, posicion_en_lista, estado_qn, inicio_concat, 0);
                String[] datos = tmp.split("<%>");
                
                estado_qn = Integer.parseInt(datos[0]);
                intermedio_concat = Integer.parseInt(datos[1]);
                parte_a = datos[2];
                break;
            case "*":
                break;
            case "?":
                break;
            case "+":
                break;
            default:
                //solo es: letra, numero, "-",...
                estado_qn++;
                intermedio_concat=estado_qn;
                
                parte_a="\"q"+inicio_concat+"\"->\"q"+intermedio_concat+"\" [label=\""+transicion_a+"\"];\n";
                break;
        }
        
        

        //parte de B, que inicia desde intermedio_concat
        String parte_b="", ba, bb, transicion_b;
        posicion_en_lista++;
        transicion_b=contenido.get(posicion_en_lista);
        
        switch(transicion_b){
            case ".":
                bb = contenido.get(posicion_en_lista+1);
                posicion_en_lista++;
                parte_b = metodo_concatenar(contenido, bb, posicion_en_lista, estado_qn);
                break;
            case "|":
                //como es or envio los siguientes dos datos en la lista
                posicion_en_lista++;
                ba = contenido.get(posicion_en_lista);
                posicion_en_lista++;
                bb = contenido.get(posicion_en_lista);
                /*
                los datos retornados serian: segundo_b_or<%>final_or<%>relaciones
                segundo_b_or: donde continuara numerando el siguiente estado_qn
                final_or: donde empezara el siguiente estado
                relaciones: codigo de graphviz
                */
                String tmp = metodo_or(contenido, ba, bb, posicion_en_lista, estado_qn, intermedio_concat, 0);
                String[] datos = tmp.split("<%>");
                
                //estado_qn = Integer.parseInt(datos[0]);
                //intermedio_concat = Integer.parseInt(datos[1]);
                parte_b = datos[2];
                break;
            case "*":
                break;
            case "?":
                break;
            case "+":
                break;
            default:
                //solo es: letra, numero, "-",...
                final_concat=intermedio_concat+1;
                estado_qn++;
                parte_b="\"q"+intermedio_concat+"\"->\"q"+final_concat+"\" [label=\""+transicion_b+"\"];\n";
                break;
        }
        concatenacion = parte_a + parte_b;
        return concatenacion;
    }
    
    private String metodo_or(LinkedList<String> contenido, String transicion_a, String transicion_b, int posicion_en_lista, int estado_qn, int principio, int aob){
        //|ab
        //para identificar los estados a los cuales apuntar
        int sig_estado, inicial_or, primero_a_or, segundo_a_or=0, primero_b_or, segundo_b_or=0, final_or;
        String parte_a, parte_b;//almacenara las relaciones de la respectiva parte
        String or_; //or_ = parte_a + parte_b
        
        //*** --------------------------------------------------------------------- parte de arriba (A)
        String inicio_a, paso_a="", final_a, aa, ab;
        
        if(aob==1){
            //(aun no tiene transicion)
            inicial_or = estado_qn;
        }else{
            //(ya tiene una transicion)
            inicial_or = principio;
        }
        
        estado_qn++;
        primero_a_or=estado_qn;
        inicio_a="\"q"+inicial_or+"\"->\"q"+primero_a_or+"\" [label=\"\u03B5\"];\n";//primera transicion epsilon
        
        switch(transicion_a){
            case ".":
                aa = contenido.get(posicion_en_lista+1);
                posicion_en_lista++;
                paso_a = metodo_concatenar(contenido, aa, posicion_en_lista, estado_qn);
                break;
            case "|":
                aa=contenido.get(posicion_en_lista+1);
                ab=contenido.get(posicion_en_lista+2);
                posicion_en_lista++;
                posicion_en_lista++;
                paso_a=metodo_or(contenido, aa, ab, posicion_en_lista, estado_qn, primero_a_or, 0);
                
                break;
            case "*":
                break;
            case "?":
                break;
            case "+":
                break;
            default:
                //solo es: letra, numero, "-",...
                estado_qn++;
                segundo_a_or=estado_qn;
                paso_a="\"q"+primero_a_or+"\"->\"q"+segundo_a_or+"\" [label=\""+transicion_a+"\"];\n";
                break;
        }
        estado_qn++;
        final_or=estado_qn;
        final_a="\"q"+segundo_a_or+"\"->\"q"+final_or+"\" [label=\"\u03B5\"];\n";//transicion epsilon que apunta al final
        parte_a=inicio_a+paso_a+final_a;
        
        //*** --------------------------------------------------------------------- parte de abajo (B)
        String inicio_b, paso_b="", final_b, ba, bb;
        estado_qn++;
        primero_b_or=estado_qn;
        inicio_b="\"q"+inicial_or+"\"->\"q"+primero_b_or+"\" [label=\"\u03B5\"];\n";
        
        switch(transicion_b){
            case ".":
                bb = contenido.get(posicion_en_lista+1);
                posicion_en_lista++;
                paso_b = metodo_concatenar(contenido, bb, posicion_en_lista, estado_qn);
                break;
            case "|":
                //como es or envio los siguientes dos datos en la lista
                ba=contenido.get(posicion_en_lista+1);
                bb=contenido.get(posicion_en_lista+2);
                posicion_en_lista++;
                posicion_en_lista++;
                /*
                los datos retornados serian: segundo_b_or<%>final_or<%>relaciones
                segundo_b_or: donde continuara numerando el siguiente estado_qn (0)
                final_or: donde empezara el siguiente estado (1)
                relaciones: codigo de graphviz (2)
                */
                String tmp =metodo_or(contenido, ba, bb, posicion_en_lista, estado_qn, primero_b_or, 0);
                String[] datos = tmp.split("<%>");
                
                estado_qn = Integer.parseInt(datos[0]);
                segundo_b_or = Integer.parseInt(datos[1]);
                paso_b = datos[2];
                break;
            case "*":
                break;
            case "?":
                break;
            case "+":
                break;
            default:
                estado_qn++;
                segundo_b_or=estado_qn;
                paso_b="\"q"+primero_b_or+"\"->\"q"+segundo_b_or+"\" [label=\""+transicion_b+"\"];\n";
                break;
        }
        final_b="\"q"+segundo_b_or+"\"->\"q"+final_or+"\" [label=\"\u03B5\"];\n";
        //System.out.println("en final_or: "+final_or);
        parte_b=inicio_b+paso_b+final_b;
        
        or_ =segundo_b_or + "<%>" +final_or + "<%>" + parte_a + parte_b;
        return or_;
    }
    
    private void metodo_cero_o_mas(LinkedList<String> contenido, int posicion_lista, String transicion_a, int estado_regresion, int estado_final){
        //*a
        //parte de arriba
        //primer transicion epsilon
        int sig_estado=0, primer_estado_e=0, inicial=0;
        String primer_epsilon="", segundo_epsilon="", regresion="", paso="";
        inicial=estado_global;//almaceno lo que hasta ese momento lleve estado_global para usarlo despues con paso_epsilon
        sig_estado=estado_global+1;
        primer_estado_e=sig_estado;//primer_estado_e lo uso para saber donde apuntar estado_regresion
        primer_epsilon="\"q"+estado_global+"\"->\"q"+sig_estado+"\" [label=\"\u03B5\"];\n";
        estado_global++;
        
        //contenido de A
        switch(transicion_a){
            case ".":
                break;
            case "|":
                break;
            case "*":
                break;
            case "?":
                break;
            case "+":
                break;
            default:
                //solo es un id
                break;
        }
        
        //segunda transicion epsilon
        sig_estado=estado_global+1;
        segundo_epsilon = "\"q"+estado_global+"\"->\"q"+sig_estado+"\" [label=\"\u03B5\"];\n";
        estado_global++;
        
        //parte de transiciones epsilon: regresion y paso_epsilon
        //regresion
        regresion="q\""+estado_regresion+"\"->\"q"+primer_estado_e+"\" [label=\"\u03B5\"];\n";
        //paso epsilon
        paso="q\""+inicial+"\"->\"q"+estado_final+"\" [label=\"\u03B5\"];\n";
    }
    
    private void metodo_almenos_uno(){
        //+a
    }
    
    private void metodo_opcional(){
        //?a
    }
    
    private void mostrar(String a){
        System.out.println(a);
    }
    
}
