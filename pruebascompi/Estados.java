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
                //FALTA SEPARARLO MUESTRA DFSKJ<%>FJSDK<%>FJKSD
                tmp = metodo_or(contenido, a, b, 2, estado_inicio, 0, 1);
                mostrar(tmp);
                break;
            case "*":
                a = contenido.get(1);
                tmp = metodo_cero_o_mas(contenido, a, 1, estado_inicio, 0, 1);
                mostrar(tmp);
                break;
        }
        
    }
   
    //***-----------------------cada metodo retorna las relaciones
        
    private String metodo_concatenar(LinkedList<String> contenido, String transicion_a, int posicion_en_lista, int estado_qn){
        //.ab
        int inicio_concat, intermedio_concat=0, final_concat=0; //posiciones de estado para guiarme
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
                los datos retornados serian: segundo_b_or<%>final_or<%>relaciones<%>posicion_en_lista<%>segundo_b_or_ultimo
                (0) segundo_b_or: donde continuara numerando el siguiente estado_qn 
                (1) final_or: donde empezara el siguiente estado 
                (2) relaciones: codigo de graphviz 
                (3) posicion_en_lista 
                (4) segundo_b_or_ultimo: tendra almacenado el estado donde se quedo anteriormente segundo_b_or 
                */
                
                String tmp = metodo_or(contenido, aa, ab, posicion_en_lista, estado_qn, inicio_concat, 0);
                String[] datos = tmp.split("<%>");
                
                estado_qn = Integer.parseInt(datos[0]);
                intermedio_concat = Integer.parseInt(datos[1]);
                parte_a = datos[2];
                //AGREGUE ESTO NUEVO
                posicion_en_lista = Integer.parseInt(datos[3]);
                
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
                los datos retornados serian: segundo_b_or<%>final_or<%>relaciones<%>posicion_en_lista<%>segundo_b_or_ultimo
                (0) segundo_b_or: donde continuara numerando el siguiente estado_qn 
                (1) final_or: donde empezara el siguiente estado 
                (2) relaciones: codigo de graphviz 
                (3) posicion_en_lista 
                (4) segundo_b_or_ultimo: tendra almacenado el estado donde se quedo anteriormente segundo_b_or 
                */
                
                String tmp = metodo_or(contenido, ba, bb, posicion_en_lista, estado_qn, intermedio_concat, 0);
                String[] datos = tmp.split("<%>");
                
                //estado_qn = Integer.parseInt(datos[0]);
                //intermedio_concat = Integer.parseInt(datos[1]);
                parte_b = datos[2];
                break;
            case "*":
                posicion_en_lista++;
                ba = contenido.get(posicion_en_lista);
                /*
                los datos retornados serian: final_cero_mas<%>relaciones
                (0) final_cero_mas: estado donde termino la anterior transicion
                (1) relaciones: codigo de graphviz
                */
                System.out.println("ba: "+ba+"\nposicion en lista: "+posicion_en_lista+"\nestado qn: "+estado_qn);
                String tmp_b_concat = metodo_cero_o_mas(contenido, ba, posicion_en_lista, estado_qn, intermedio_concat, 0);
                String[] datos_cero = tmp_b_concat.split("<%>");
                
                final_concat = Integer.parseInt(datos_cero[0]);
                parte_b = datos_cero[1];
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
        concatenacion = final_concat +"<%>"+parte_a + parte_b;
        return concatenacion;
    }
    
    private String metodo_or(LinkedList<String> contenido, String transicion_a, String transicion_b, int posicion_en_lista, int estado_qn, int principio, int aob) {
        //|ab
        //para identificar los estados a los cuales apuntar
        int sig_estado, inicial_or, primero_a_or, segundo_a_or=0, primero_b_or, segundo_b_or=0, final_or, segundo_b_or_ultimo=0;
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
                String tmp_concat = metodo_concatenar(contenido, aa, posicion_en_lista, estado_qn);
                /*
                retorno de concatenar: final_concat<%>relaciones
                */
                String[] datos_concat = tmp_concat.split("<%>");
                estado_qn = Integer.parseInt(datos_concat[0]);
                segundo_a_or = Integer.parseInt(datos_concat[0]);
                paso_a = datos_concat[1];
                break;
            case "|":
                /*
                como transicion_a tiene un or (|) se le envian los siguientes dos elementos en la lista
                el siguiente elemento seria lo que tiene transicion_b lo que hize fue regresar la ubicacion en la que me encuentro
                en la lista (posicion_en_lista) asi de este modo las siguientes dos posiciones corresponderian
                con a y b a partir de la posicion del or en transicion_a
                ej.:
                 0   1    2    3
                [|][|][letra]["-"]
                transicion_a: tiene | (posicion 1)
                transicion_b: tiene letra (posicion 2)
                */
                
                //como estoy en 'A' regreso una posicion debido a que la cantidad en posicion_en_lista esta en la siguiente posicion
                //esa posicion corresponde a transicion_b que mande junto al metodo, por eso regreso una posicion
                posicion_en_lista--;
                aa=contenido.get(posicion_en_lista+1);
                ab=contenido.get(posicion_en_lista+2);
                posicion_en_lista++;
                posicion_en_lista++;
                
                /*
                los datos retornados serian: segundo_b_or<%>final_or<%>relaciones<%>posicion_en_lista<%>segundo_b_or_ultimo
                (0) segundo_b_or: donde continuara numerando el siguiente estado_qn 
                (1) final_or: donde empezara el siguiente estado 
                (2) relaciones: codigo de graphviz 
                (3) posicion_en_lista 
                (4) segundo_b_or_ultimo: tendra almacenado el estado donde se quedo anteriormente segundo_b_or 
                */
                
                String tmp =metodo_or(contenido, aa, ab, posicion_en_lista, estado_qn, primero_a_or, 0);
                String[] datos = tmp.split("<%>");
                
                //estado_qn = Integer.parseInt(datos[0]);
                segundo_a_or = Integer.parseInt(datos[1]);
                paso_a = datos[2];
                posicion_en_lista = Integer.parseInt(datos[3]);
                estado_qn = Integer.parseInt(datos[4]);
                
                //a transicion_b le corresponde la siguiente posicion en la que se quedo la parte de 'b' de la anterior relacion
                posicion_en_lista++;
                transicion_b = contenido.get(posicion_en_lista);
                
                //System.out.println("posicion en lista despues de regresar de a: "+ posicion_en_lista);
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
                String tmp_concat = metodo_concatenar(contenido, bb, posicion_en_lista, estado_qn);
                
                /*
                retorno de concatenar: final_concat<%>relaciones
                */
                String[] datos_concat = tmp_concat.split("<%>");
                estado_qn = Integer.parseInt(datos_concat[0]);
                segundo_a_or = Integer.parseInt(datos_concat[0]);
                paso_a = datos_concat[1];
                break;
            case "|":
                //como es or envio los siguientes dos datos en la lista
                ba=contenido.get(posicion_en_lista+1);
                bb=contenido.get(posicion_en_lista+2);
                posicion_en_lista++;
                posicion_en_lista++;
                /*
                los datos retornados serian: segundo_b_or<%>final_or<%>relaciones<%>posicion_en_lista<%>segundo_b_or_ultimo
                (0) segundo_b_or: donde continuara numerando el siguiente estado_qn 
                (1) final_or: donde empezara el siguiente estado 
                (2) relaciones: codigo de graphviz 
                (3) posicion_en_lista 
                (4) segundo_b_or_ultimo: tendra almacenado el estado donde se quedo anteriormente segundo_b_or 
                */
                String tmp = metodo_or(contenido, ba, bb, posicion_en_lista, estado_qn, primero_b_or, 0);
                String[] datos = tmp.split("<%>");
                
                segundo_b_or_ultimo = Integer.parseInt(datos[0]);
                
                System.out.println("segundo b or ultimo: "+segundo_b_or_ultimo);
                //estado_qn = Integer.parseInt(datos[0]);
                segundo_b_or = Integer.parseInt(datos[1]);
                paso_b = datos[2];
                posicion_en_lista = Integer.parseInt(datos[3]);
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
        
        or_ =segundo_b_or + "<%>" +final_or + "<%>" + parte_a + parte_b + "<%>" + posicion_en_lista + "<%>" + segundo_b_or_ultimo;
        return or_;
    }
    
    private String metodo_cero_o_mas(LinkedList<String> contenido, String transicion_a, int posicion_en_lista, int estado_qn, int principio, int aob){
        //*a
        int inicial_cero_mas, primero_cero_mas, regrecion_cero_mas=0, final_cero_mas=0;
        String primer_epsilon="", segundo_epsilon="", regresion="", paso_epsilon="", paso_a="";
        String cero_mas=""; //cero_mas = primer_epsilon + paso_a + segundo_epsilon + regrecion + paso_epsilon
        
        //***--------------------- primer transicion epsilon
        if(aob==1){
            //aun no tiene transicion
            inicial_cero_mas = estado_qn;
        }else{
            //ya hay una transicion
            inicial_cero_mas = principio;
        }
        primero_cero_mas = inicial_cero_mas+1;
        estado_qn++;
        primer_epsilon="\"q"+inicial_cero_mas+"\"->\"q"+primero_cero_mas+"\" [label=\"\u03B5\"];\n";
        
        //***--------------------- contenido de A
        switch(transicion_a){
            case ".":
                posicion_en_lista++;
                String a = contenido.get(posicion_en_lista);
                /*
                retorno de concatenar: final_concat<%>relaciones
                */
                String tmp_concat = metodo_concatenar(contenido, a, posicion_en_lista, estado_qn);
                String [] datos_concat = tmp_concat.split("<%>");
                regrecion_cero_mas = Integer.parseInt(datos_concat[0]);
                paso_a = datos_concat[1];
                break;
            case "|":
                //como es or envio los siguientes dos datos en la lista
                posicion_en_lista++;
                String aa = contenido.get(posicion_en_lista);
                posicion_en_lista++;
                String ab = contenido.get(posicion_en_lista);
                
                System.out.println("aa tiene: "+aa+"\nab tiene: "+ab);
                
                /*
                los datos retornados serian: segundo_b_or<%>final_or<%>relaciones<%>posicion_en_lista<%>segundo_b_or_ultimo
                (0) segundo_b_or: donde continuara numerando el siguiente estado_qn 
                (1) final_or: donde empezara el siguiente estado 
                (2) relaciones: codigo de graphviz 
                (3) posicion_en_lista 
                (4) segundo_b_or_ultimo: tendra almacenado el estado donde se quedo anteriormente segundo_b_or 
                */
                
                String tmp_or = metodo_or(contenido, aa, ab, posicion_en_lista, estado_qn, primero_cero_mas, 0);
                String[] datos_or = tmp_or.split("<%>");
                
                //estado_qn = Integer.parseInt(datos_or[0]);
                regrecion_cero_mas = Integer.parseInt(datos_or[1]);
                paso_a = datos_or[2];
                estado_qn = Integer.parseInt(datos_or[4]);
                
                //***--------------------- segunda transicion epsilon
                estado_qn++;
                final_cero_mas = estado_qn;
                segundo_epsilon = "\"q"+regrecion_cero_mas+"\"->\"q"+final_cero_mas+"\" [label=\"\u03B5\"];\n";
                break;
            case "*":
                break;
            case "?":
                break;
            case "+":
                break;
            default:
                //solo es un id
                regrecion_cero_mas = primero_cero_mas+1;
                paso_a = "\"q"+primero_cero_mas+"\"->\"q"+regrecion_cero_mas+"\" [label=\""+transicion_a+"\"];\n";
                estado_qn++;
                //***--------------------- segunda transicion epsilon
                final_cero_mas = regrecion_cero_mas+1;
                estado_qn++;
                segundo_epsilon = "\"q"+regrecion_cero_mas+"\"->\"q"+final_cero_mas+"\" [label=\"\u03B5\"];\n";
                break;
        }
        //CAMBIE LO DE SEGUNDA REGRECION Y LO METI EN CADA CASE. CODIGO ORIGINAL ESTA EN DEFAULT VA AQUI.
        
        
        //***--------------------- regresion con epsilon
        regresion="\"q"+regrecion_cero_mas+"\"->\"q"+primero_cero_mas+"\" [label=\"\u03B5\"];\n";
        
        //***--------------------- paso epsilon
        paso_epsilon="\"q"+inicial_cero_mas+"\"->\"q"+final_cero_mas+"\" [label=\"\u03B5\"];\n";
        System.out.println("estado_qn se queda con: " + estado_qn);
        
        cero_mas = final_cero_mas + "<%>" + primer_epsilon + paso_a + segundo_epsilon + regresion + paso_epsilon;
        return cero_mas;
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
