/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pruebascompi;

import javax.swing.JOptionPane;
import java.util.LinkedList;

/**
 *
 * @author Eduardo Garcia
 */
public class Estados {
    
    /**
     * la 'lista de contenido', para esta entrada:
     * .letra*|letra|digito"-"
     * quedaria de esta forma:
     * [.][letra][*][|][letra][|][digito]["-"]
     * @param contenido : lista previamente separada.
     */
    
    public void inicio_de_afn(LinkedList<String> contenido){
        /*
        V7: metodo de thompson graficado.
        
        V8:una vez completado la grafica de thompson.
        agregue un contador para las transiciones el cual me servira para la 'tabla de transiciones' para el met. de subconjuntos
        el contador lo agregue al final de cada retorno de los metodos y es el ultimo parametro de cada metodo.
        */
        try{
            //obtengo el primer dato
            int estado_inicio=0, no_transiciones=0;
            String top = contenido.get(0);
            String a, b, tmp;
            switch(top){
                case ".":
                    //como es concatenacion, obtengo el siguiente elemento
                    a = contenido.get(1);
                    tmp = metodo_concatenar(contenido, a, 1, estado_inicio, no_transiciones);
                    mostrar(tmp);
                    break;
                case "|":
                    a = contenido.get(1);
                    b = contenido.get(2);
                    //FALTA SEPARARLO MUESTRA DFSKJ<%>FJSDK<%>FJKSD
                    tmp = metodo_or(contenido, a, b, 2, estado_inicio, 0, 1, no_transiciones);
                    mostrar(tmp);
                    break;
                case "*":
                    a = contenido.get(1);
                    tmp = metodo_cero_o_mas(contenido, a, 1, estado_inicio, 0, 1, no_transiciones);
                    mostrar(tmp);
                    break;
                case "+":
                    a = contenido.get(1);
                    tmp = metodo_almenos_uno(contenido, a, 1, estado_inicio, 0, 1, no_transiciones);
                    mostrar(tmp);
                    break;
                case "?":
                    a = contenido.get(1);
                    tmp = metodo_opcional(contenido, a, 1, estado_inicio, 0, 1, no_transiciones);
                    mostrar(tmp);
                    break;
                default:

                    break;
            }
        }catch(IndexOutOfBoundsException e){
            JOptionPane.showMessageDialog(null, "No has ingresado correctamente la expresion regular.\n"+e.toString(), "Error al reconocer la expresion.", JOptionPane.WARNING_MESSAGE);
        }
        
    }
   
    //***-----------------------cada metodo retorna las relaciones, y otros datos explicados en cada metodo.
        
    private String metodo_concatenar(LinkedList<String> contenido, String transicion_a, int posicion_en_lista, int estado_qn, int no_transiciones){
        //.ab
        int inicio_concat, intermedio_concat=0, final_concat=0; //posiciones de estado para guiarme
        String concatenacion; //parte_a + parte_b
        
        //parte de A
        String parte_a, aa, ab;
        inicio_concat=estado_qn;
        switch(transicion_a){
            case ".":
                aa = contenido.get(posicion_en_lista+1);
                posicion_en_lista++;
                parte_a = metodo_concatenar(contenido, aa, posicion_en_lista, estado_qn, no_transiciones);
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
                (4) no_transiciones
                */
                
                String tmp = metodo_or(contenido, aa, ab, posicion_en_lista, estado_qn, inicio_concat, 0, no_transiciones);
                String[] datos = tmp.split("<%>");
                
                estado_qn = Integer.parseInt(datos[0]);
                intermedio_concat = Integer.parseInt(datos[1]);
                parte_a = datos[2];
                //AGREGUE ESTO NUEVO
                posicion_en_lista = Integer.parseInt(datos[3]);
                no_transiciones = Integer.parseInt(datos[4]);
                break;
            case "*":
                posicion_en_lista++;
                aa = contenido.get(posicion_en_lista);
                /*
                datos retornados: final_cero_mas<%>posicion_en_lista<%>relaciones
                (0) final_cero_mas: donde finalizo la transicion
                (1) posicion_en_lista
                (2) codigo de graphviz
                (3) no_transiciones
                */
                String tmp_cero = metodo_cero_o_mas(contenido, aa, posicion_en_lista, estado_qn, inicio_concat, 0, no_transiciones);
                String[] datos_cero = tmp_cero.split("<%>");
                
                intermedio_concat = Integer.parseInt(datos_cero[0]);
                estado_qn = Integer.parseInt(datos_cero[0]);
                posicion_en_lista = Integer.parseInt(datos_cero[1]);
                parte_a = datos_cero[2];
                no_transiciones = Integer.parseInt(datos_cero[3]);
                break;
            case "?":
                posicion_en_lista++;
                aa = contenido.get(posicion_en_lista);
                System.out.println("aa: "+aa);
                /*
                (0) final_opcional: estado donde finalizo anteriormente
                (1) posicion_en_lista
                (2) relaciones
                (3) no_transiciones
                */
                String tmp_op = metodo_opcional(contenido, aa, posicion_en_lista, estado_qn, inicio_concat, 0, no_transiciones);
                String[] datos_op = tmp_op.split("<%>");
                
                intermedio_concat = Integer.parseInt(datos_op[0]);
                estado_qn = Integer.parseInt(datos_op[0]);
                posicion_en_lista = Integer.parseInt(datos_op[1]);
                System.out.println("recien horneado: "+posicion_en_lista);
                parte_a = datos_op[2];
                no_transiciones = Integer.parseInt(datos_op[3]);
                break;
            case "+":
                posicion_en_lista++;
                ab = contenido.get(posicion_en_lista);
                
                /*
                datos retornados:
                (0) final_uno_mas: donde finalizo la transicion
                (1) posicion_en_lista
                (2) codigo de graphviz
                (3) no_transiciones
                */
                
                String tmp_uno = metodo_almenos_uno(contenido, ab, posicion_en_lista, estado_qn, inicio_concat, 0, no_transiciones);
                String[] datos_uno = tmp_uno.split("<%>");
                
                intermedio_concat = Integer.parseInt(datos_uno[0]);
                estado_qn = Integer.parseInt(datos_uno[0]);
                posicion_en_lista = Integer.parseInt(datos_uno[1]);
                parte_a = datos_uno[2];
                no_transiciones = Integer.parseInt(datos_uno[3]);
                break;
            default:
                //solo es: letra, numero, "-",...
                estado_qn++;
                intermedio_concat=estado_qn;
                no_transiciones++;
                parte_a="\"q"+inicio_concat+"\"->\"q"+intermedio_concat+"\" [label=\""+transicion_a+"\"];\n";
                break;
        }
        
        

        //parte de B, que inicia desde intermedio_concat
        String parte_b, ba, bb, transicion_b;
        posicion_en_lista++;
        transicion_b=contenido.get(posicion_en_lista);
        
        switch(transicion_b){
            case ".":
                bb = contenido.get(posicion_en_lista+1);
                posicion_en_lista++;
                parte_b = metodo_concatenar(contenido, bb, posicion_en_lista, estado_qn, no_transiciones);
                break;
            case "|":
                //como es or envio los siguientes dos datos en la lista
                posicion_en_lista++;
                ba = contenido.get(posicion_en_lista);
                posicion_en_lista++;
                bb = contenido.get(posicion_en_lista);
                
                /*
                los datos retornados serian:
                (0) segundo_b_or: donde continuara numerando el siguiente estado_qn 
                (1) final_or: donde empezara el siguiente estado 
                (2) relaciones: codigo de graphviz 
                (3) posicion_en_lista 
                (4) no_transiciones
                */
                
                String tmp = metodo_or(contenido, ba, bb, posicion_en_lista, estado_qn, intermedio_concat, 0, no_transiciones);
                String[] datos = tmp.split("<%>");
                
                //estado_qn = Integer.parseInt(datos[0]);
                //intermedio_concat = Integer.parseInt(datos[1]);
                parte_b = datos[2];
                no_transiciones = Integer.parseInt(datos[4]);
                break;
            case "*":
                posicion_en_lista++;
                ba = contenido.get(posicion_en_lista);
                /*
                datos retornados: final_cero_mas<%>posicion_en_lista<%>relaciones
                (0) final_cero_mas: donde finalizo la transicion
                (1) posicion_en_lista
                (2) codigo de graphviz
                (3) no_transiciones
                */
                String tmp_b_concat = metodo_cero_o_mas(contenido, ba, posicion_en_lista, estado_qn, intermedio_concat, 0, no_transiciones);
                String[] datos_cero = tmp_b_concat.split("<%>");
                
                final_concat = Integer.parseInt(datos_cero[0]);
                parte_b = datos_cero[2];
                no_transiciones = Integer.parseInt(datos_cero[3]);
                break;
            case "?":
                posicion_en_lista++;
                ba = contenido.get(posicion_en_lista);
                
                /*
                (0) final_opcional: estado donde finalizo anteriormente
                (1) posicion_en_lista
                (2) relaciones
                (3) no_transiciones
                */
                String tmp_op = metodo_opcional(contenido, ba, posicion_en_lista, estado_qn, intermedio_concat, 0, no_transiciones);
                String[] datos_op = tmp_op.split("<%>");
                
                final_concat = Integer.parseInt(datos_op[0]);
                estado_qn = Integer.parseInt(datos_op[0]);
                posicion_en_lista = Integer.parseInt(datos_op[1]);
                parte_b = datos_op[2];
                no_transiciones = Integer.parseInt(datos_op[3]);
                break;
            case "+":
                posicion_en_lista++;
                ba = contenido.get(posicion_en_lista);
                
                /*
                datos retornados: final_cero_mas<%>posicion_en_lista<%>relaciones
                (0) final_uno_mas: donde finalizo la transicion
                (1) posicion_en_lista
                (2) codigo de graphviz
                (3) no_transiciones
                */
                
                String tmp_uno = metodo_almenos_uno(contenido, ba, posicion_en_lista, estado_qn, intermedio_concat, 0, no_transiciones);
                String[] datos_uno = tmp_uno.split("<%>");
                
                final_concat = Integer.parseInt(datos_uno[0]);
                estado_qn = Integer.parseInt(datos_uno[0]);
                posicion_en_lista = Integer.parseInt(datos_uno[1]);
                parte_b = datos_uno[2];
                no_transiciones = Integer.parseInt(datos_uno[3]);
                break;
            default:
                //solo es: letra, numero, "-",...
                final_concat=intermedio_concat+1;
                estado_qn++;
                no_transiciones++;
                parte_b="\"q"+intermedio_concat+"\"->\"q"+final_concat+"\" [label=\""+transicion_b+"\"];\n";
                break;
        }
        concatenacion = final_concat + "<%>" + posicion_en_lista + "<%>" + parte_a+parte_b + "<%>" + no_transiciones;
        return concatenacion;
    }
    
    private String metodo_or(LinkedList<String> contenido, String transicion_a, String transicion_b, int posicion_en_lista, int estado_qn, int principio, int aob, int no_transiciones) {
        //|ab
        //para identificar los estados a los cuales apuntar
        int inicial_or, primero_a_or, segundo_a_or, primero_b_or, segundo_b_or=0, final_or;
        String parte_a, parte_b;//almacenara las relaciones de la respectiva parte
        String or_; //or_ = parte_a + parte_b
        
        //*** --------------------------------------------------------------------- parte de arriba (A)
        String inicio_a, paso_a, final_a, aa, ab;
        
        if(aob==1){
            //(aun no tiene transicion)
            inicial_or = estado_qn;
        }else{
            //(ya tiene una transicion)
            inicial_or = principio;
        }
        
        estado_qn++;
        primero_a_or=estado_qn;
        no_transiciones++;
        inicio_a="\"q"+inicial_or+"\"->\"q"+primero_a_or+"\" [label=\"\u03B5\"];\n";//primera transicion epsilon
        
        switch(transicion_a){
            case ".":
                aa = contenido.get(posicion_en_lista+1);
                posicion_en_lista++;
                String tmp_concat = metodo_concatenar(contenido, aa, posicion_en_lista, estado_qn, no_transiciones);
                /*
                retorno de concatenar: final_concat<%>relaciones<%>no_transiciones
                */
                String[] datos_concat = tmp_concat.split("<%>");
                estado_qn = Integer.parseInt(datos_concat[0]);
                segundo_a_or = Integer.parseInt(datos_concat[0]);
                paso_a = datos_concat[1];
                no_transiciones = Integer.parseInt(datos_concat[2]);
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
                /*
                posicion_en_lista--;
                aa=contenido.get(posicion_en_lista+1);
                ab=contenido.get(posicion_en_lista+2);
                posicion_en_lista++;
                posicion_en_lista++;
                */
                aa = contenido.get(posicion_en_lista);
                posicion_en_lista++;
                ab = contenido.get(posicion_en_lista);
                
                /*
                los datos retornados serian:
                (0) segundo_b_or: donde continuara numerando el siguiente estado_qn 
                (1) final_or: donde empezara el siguiente estado 
                (2) relaciones: codigo de graphviz 
                (3) posicion_en_lista
                (4) no_transiciones
                */
                
                String tmp =metodo_or(contenido, aa, ab, posicion_en_lista, estado_qn, primero_a_or, 0, no_transiciones);
                String[] datos = tmp.split("<%>");
                
                estado_qn = Integer.parseInt(datos[1]);
                segundo_a_or = Integer.parseInt(datos[1]);
                paso_a = datos[2];
                posicion_en_lista = Integer.parseInt(datos[3]);
                no_transiciones = Integer.parseInt(datos[4]);
                //a transicion_b le corresponde la siguiente posicion en la que se quedo la parte de 'b' de la anterior relacion
                posicion_en_lista++;
                transicion_b = contenido.get(posicion_en_lista);
                
                break;
            case "*":
                aa = contenido.get(posicion_en_lista);
                /*
                datos retornados:
                (0) final_cero_mas: donde finalizo la transicion
                (1) posicion_en_lista
                (2) codigo de graphviz
                (3) no_transiciones
                */
                String tmp_cero = metodo_cero_o_mas(contenido, aa, posicion_en_lista, estado_qn, primero_a_or, 0, no_transiciones);
                String[] datos_cero = tmp_cero.split("<%>");
                
                segundo_a_or = Integer.parseInt(datos_cero[0]);
                estado_qn = Integer.parseInt(datos_cero[0]);
                
                posicion_en_lista = Integer.parseInt(datos_cero[1]);
                posicion_en_lista++;
                paso_a = datos_cero[2];
                no_transiciones = Integer.parseInt(datos_cero[3]);
                //nueva transicion b
                transicion_b = contenido.get(posicion_en_lista);
                break;
            case "?":
                aa = contenido.get(posicion_en_lista);
                /*
                (0) final_opcional: estado donde finalizo anteriormente
                (1) posicion_en_lista
                (2) relaciones
                (3) no_transiciones
                */
                String tmp_op = metodo_opcional(contenido, aa, posicion_en_lista, estado_qn, primero_a_or, 0, no_transiciones);
                String[] datos_op = tmp_op.split("<%>");
                
                segundo_a_or = Integer.parseInt(datos_op[0]);
                estado_qn = Integer.parseInt(datos_op[0]);
                posicion_en_lista = Integer.parseInt(datos_op[1]);
                paso_a = datos_op[2];
                no_transiciones = Integer.parseInt(datos_op[3]);
                //nueva transicion_b
                posicion_en_lista++;
                transicion_b = contenido.get(posicion_en_lista);
                break;
            case "+":
                ab = contenido.get(posicion_en_lista);
                
                /*
                datos retornados:
                (0) final_uno_mas: donde finalizo la transicion
                (1) posicion_en_lista
                (2) codigo de graphviz
                (3) no_transiciones
                */
                
                String tmp_uno = metodo_almenos_uno(contenido, ab, posicion_en_lista, estado_qn, primero_a_or, 0, no_transiciones);
                String[] datos_uno = tmp_uno.split("<%>");
                
                segundo_a_or = Integer.parseInt(datos_uno[0]);
                estado_qn = Integer.parseInt(datos_uno[0]);
                posicion_en_lista = Integer.parseInt(datos_uno[1]);
                paso_a = datos_uno[2];
                no_transiciones = Integer.parseInt(datos_uno[3]);
                //nueva transicion_b
                posicion_en_lista++;
                transicion_b = contenido.get(posicion_en_lista);
                break;
            default:
                //solo es: letra, numero, "-",...
                estado_qn++;
                segundo_a_or=estado_qn;
                no_transiciones++;
                paso_a="\"q"+primero_a_or+"\"->\"q"+segundo_a_or+"\" [label=\""+transicion_a+"\"];\n";
                break;
        }
        
        //quite codigo: #01
        
        //*** --------------------------------------------------------------------- parte de abajo (B)
        String inicio_b, paso_b="", final_b, ba, bb;
        estado_qn++;
        primero_b_or=estado_qn;
        no_transiciones++;
        inicio_b="\"q"+inicial_or+"\"->\"q"+primero_b_or+"\" [label=\"\u03B5\"];\n";
        
        switch(transicion_b){
            case ".":
                bb = contenido.get(posicion_en_lista+1);
                posicion_en_lista++;
                String tmp_concat = metodo_concatenar(contenido, bb, posicion_en_lista, estado_qn, no_transiciones);
                
                /*
                retorno de concatenar: final_concat<%>relaciones<%>no_transiciones
                */
                String[] datos_concat = tmp_concat.split("<%>");
                estado_qn = Integer.parseInt(datos_concat[0]);
                segundo_a_or = Integer.parseInt(datos_concat[0]);
                paso_a = datos_concat[1];
                no_transiciones = Integer.parseInt(datos_concat[2]);
                break;
            case "|":
                //como es or envio los siguientes dos datos en la lista
                ba=contenido.get(posicion_en_lista+1);
                bb=contenido.get(posicion_en_lista+2);
                posicion_en_lista++;
                posicion_en_lista++;
                /*
                los datos retornados serian:
                (0) segundo_b_or: donde continuara numerando el siguiente estado_qn 
                (1) final_or: donde empezara el siguiente estado 
                (2) relaciones: codigo de graphviz 
                (3) posicion_en_lista 
                (4) no_transiciones
                */
                String tmp = metodo_or(contenido, ba, bb, posicion_en_lista, estado_qn, primero_b_or, 0, no_transiciones);
                String[] datos = tmp.split("<%>");
                
                estado_qn = Integer.parseInt(datos[1]);
                segundo_b_or = Integer.parseInt(datos[1]);
                paso_b = datos[2];
                posicion_en_lista = Integer.parseInt(datos[3]);
                no_transiciones = Integer.parseInt(datos[4]);
                break;
            case "*":
                posicion_en_lista++;
                ba = contenido.get(posicion_en_lista);
                /*
                datos retornados:
                (0) final_cero_mas: donde finalizo la transicion
                (1) posicion_en_lista
                (2) codigo de graphviz
                (3) no_transiciones
                */
                String tmp_cero = metodo_cero_o_mas(contenido, ba, posicion_en_lista, estado_qn, primero_b_or, 0, no_transiciones);
                String[] datos_cero = tmp_cero.split("<%>");
                
                segundo_b_or = Integer.parseInt(datos_cero[0]);
                estado_qn = Integer.parseInt(datos_cero[0]);
                posicion_en_lista = Integer.parseInt(datos_cero[1]);
                paso_b = datos_cero[2];
                no_transiciones = Integer.parseInt(datos_cero[3]);
                break;
            case "?":
                posicion_en_lista++;
                ba = contenido.get(posicion_en_lista);
                /*
                (0) final_opcional: estado donde finalizo anteriormente
                (1) posicion_en_lista
                (2) relaciones
                (3) no_transiciones
                */
                String tmp_op = metodo_opcional(contenido, ba, posicion_en_lista, estado_qn, primero_b_or, 0, no_transiciones);
                String[] datos_op = tmp_op.split("<%>");
                
                segundo_b_or = Integer.parseInt(datos_op[0]);
                estado_qn = Integer.parseInt(datos_op[0]);
                posicion_en_lista = Integer.parseInt(datos_op[1]);
                paso_b = datos_op[2];
                no_transiciones = Integer.parseInt(datos_op[3]);
                break;
            case "+":
                posicion_en_lista++;
                ba = contenido.get(posicion_en_lista);
                
                /*
                datos retornados:
                (0) final_uno_mas: donde finalizo la transicion
                (1) posicion_en_lista
                (2) codigo de graphviz
                (3) no_transiciones
                */
                
                String tmp_uno = metodo_almenos_uno(contenido, ba, posicion_en_lista, estado_qn, primero_b_or, 0, no_transiciones);
                String[] datos_uno = tmp_uno.split("<%>");
                
                segundo_b_or = Integer.parseInt(datos_uno[0]);
                estado_qn = Integer.parseInt(datos_uno[0]);
                posicion_en_lista = Integer.parseInt(datos_uno[1]);
                paso_b = datos_uno[2];
                no_transiciones = Integer.parseInt(datos_uno[3]);
                break;
            default:
                estado_qn++;
                segundo_b_or=estado_qn;
                no_transiciones++;
                paso_b="\"q"+primero_b_or+"\"->\"q"+segundo_b_or+"\" [label=\""+transicion_b+"\"];\n";
                break;
        }
        
        //codigo #01
        //transicion de segundo_a_or a final_or
        estado_qn++;
        final_or=estado_qn;
        no_transiciones++;
        final_a="\"q"+segundo_a_or+"\"->\"q"+final_or+"\" [label=\"\u03B5\"];\n";//transicion epsilon que apunta al final
        parte_a=inicio_a+paso_a+final_a;
        //fin codigo #01
        
        no_transiciones++;
        final_b="\"q"+segundo_b_or+"\"->\"q"+final_or+"\" [label=\"\u03B5\"];\n";
        
        parte_b=inicio_b+paso_b+final_b;
        
        or_ =segundo_b_or + "<%>" +final_or + "<%>" + parte_a + parte_b + "<%>" + posicion_en_lista + "<%>" + no_transiciones;
        return or_;
    }
    
    private String metodo_cero_o_mas(LinkedList<String> contenido, String transicion_a, int posicion_en_lista, int estado_qn, int principio, int aob, int no_transiciones){
        //*a
        int inicial_cero_mas, primero_cero_mas, regrecion_cero_mas, final_cero_mas;
        String primer_epsilon, segundo_epsilon, regresion, paso_epsilon, paso_a;
        String cero_mas; //cero_mas = primer_epsilon + paso_a + segundo_epsilon + regrecion + paso_epsilon
        
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
        no_transiciones++;
        primer_epsilon="\"q"+inicial_cero_mas+"\"->\"q"+primero_cero_mas+"\" [label=\"\u03B5\"];\n";
        
        //***--------------------- contenido de A
        switch(transicion_a){
            case ".":
                posicion_en_lista++;
                String a = contenido.get(posicion_en_lista);
                /*
                retorno de concatenar: final_concat<%>posicion_en_lista<%>relaciones<%>no_transiciones
                */
                String tmp_concat = metodo_concatenar(contenido, a, posicion_en_lista, estado_qn, no_transiciones);
                String [] datos_concat = tmp_concat.split("<%>");
                
                regrecion_cero_mas = Integer.parseInt(datos_concat[0]);
                estado_qn = Integer.parseInt(datos_concat[0]);
                posicion_en_lista = Integer.parseInt(datos_concat[1]);
                paso_a = datos_concat[2];
                no_transiciones = Integer.parseInt(datos_concat[3]);
                
                //***-------- segundo paso epsilon
                estado_qn++;
                final_cero_mas=estado_qn;
                no_transiciones++;
                segundo_epsilon = "\"q"+regrecion_cero_mas+"\"->\"q"+final_cero_mas+"\" [label=\"\u03B5\"];\n";
                break;
            case "|":
                //como es or envio los siguientes dos datos en la lista
                posicion_en_lista++;
                String aa = contenido.get(posicion_en_lista);
                posicion_en_lista++;
                String ab = contenido.get(posicion_en_lista);
                
                //System.out.println("aa tiene: "+aa+"\nab tiene: "+ab);
                
                /*
                los datos retornados serian:
                (0) segundo_b_or: donde continuara numerando el siguiente estado_qn 
                (1) final_or: donde empezara el siguiente estado 
                (2) relaciones: codigo de graphviz 
                (3) posicion_en_lista
                (4) no_transiciones
                */
                
                String tmp_or = metodo_or(contenido, aa, ab, posicion_en_lista, estado_qn, primero_cero_mas, 0, no_transiciones);
                String[] datos_or = tmp_or.split("<%>");
                
                estado_qn = Integer.parseInt(datos_or[1]);
                regrecion_cero_mas = Integer.parseInt(datos_or[1]);
                paso_a = datos_or[2];
                posicion_en_lista = Integer.parseInt(datos_or[3]);
                no_transiciones = Integer.parseInt(datos_or[4]);
                //***--------------------- segunda transicion epsilon
                estado_qn++;
                final_cero_mas = estado_qn;
                no_transiciones++;
                segundo_epsilon = "\"q"+regrecion_cero_mas+"\"->\"q"+final_cero_mas+"\" [label=\"\u03B5\"];\n";
                break;
            case "*":
                posicion_en_lista++;
                String aa_ = contenido.get(posicion_en_lista);
                
                /*
                datos retornados:
                (0) final_cero_mas: donde finalizo la transicion
                (1) posicion_en_lista
                (2) codigo de graphviz
                (3) no_transiciones
                */
                
                String tmp_cero = metodo_cero_o_mas(contenido, aa_, posicion_en_lista, estado_qn, primero_cero_mas, 0, no_transiciones);
                String[] datos_cero = tmp_cero.split("<%>");
                
                regrecion_cero_mas = Integer.parseInt(datos_cero[0]);
                estado_qn = Integer.parseInt(datos_cero[0]);
                posicion_en_lista = Integer.parseInt(datos_cero[1]);
                paso_a = datos_cero[2];
                no_transiciones = Integer.parseInt(datos_cero[3]);
                
                //***--------------------- segunda transicion epsilon
                estado_qn++;
                final_cero_mas=estado_qn;
                no_transiciones++;
                segundo_epsilon = "\"q"+regrecion_cero_mas+"\"->\"q"+final_cero_mas+"\" [label=\"\u03B5\"];\n";
                break;
            case "?":
                posicion_en_lista++;
                a = contenido.get(posicion_en_lista);
                /*
                (0) final_opcional: estado donde finalizo anteriormente
                (1) posicion_en_lista
                (2) relaciones
                (3) no_transiciones
                */
                String tmp_op = metodo_opcional(contenido, a, posicion_en_lista, estado_qn, primero_cero_mas, 0, no_transiciones);
                String[] datos_op = tmp_op.split("<%>");
                
                regrecion_cero_mas = Integer.parseInt(datos_op[0]);
                estado_qn = Integer.parseInt(datos_op[0]);
                posicion_en_lista = Integer.parseInt(datos_op[1]);
                paso_a = datos_op[2];
                no_transiciones = Integer.parseInt(datos_op[3]);
                
                //***-------------------- segunda transicion epsilon
                estado_qn++;
                final_cero_mas=estado_qn;
                no_transiciones++;
                segundo_epsilon = "\"q"+regrecion_cero_mas+"\"->\"q"+final_cero_mas+"\" [label=\"\u03B5\"];\n";
                break;
            case "+":
                posicion_en_lista++;
                String a_uno = contenido.get(posicion_en_lista);
                
                /*
                datos retornados:
                (0) final_uno_mas: donde finalizo la transicion
                (1) posicion_en_lista
                (2) codigo de graphviz
                (3) no_transiciones
                */
                
                String tmp_uno = metodo_almenos_uno(contenido, a_uno, posicion_en_lista, estado_qn, primero_cero_mas, 0, no_transiciones);
                String[] datos_uno = tmp_uno.split("<%>");
                
                regrecion_cero_mas = Integer.parseInt(datos_uno[0]);
                estado_qn = Integer.parseInt(datos_uno[0]);
                posicion_en_lista = Integer.parseInt(datos_uno[1]);
                paso_a = datos_uno[2];
                no_transiciones = Integer.parseInt(datos_uno[3]);
                
                //***--------------------- segunda transicion epsilon
                estado_qn++;
                final_cero_mas=estado_qn;
                no_transiciones++;
                segundo_epsilon = "\"q"+regrecion_cero_mas+"\"->\"q"+final_cero_mas+"\" [label=\"\u03B5\"];\n";
                break;
            default:
                //solo es un id
                regrecion_cero_mas = primero_cero_mas+1;
                no_transiciones++;
                paso_a = "\"q"+primero_cero_mas+"\"->\"q"+regrecion_cero_mas+"\" [label=\""+transicion_a+"\"];\n";
                estado_qn++;
                
                //***--------------------- segunda transicion epsilon
                final_cero_mas = regrecion_cero_mas+1;
                estado_qn++;
                no_transiciones++;
                segundo_epsilon = "\"q"+regrecion_cero_mas+"\"->\"q"+final_cero_mas+"\" [label=\"\u03B5\"];\n";
                break;
        }
        //CAMBIE LO DE SEGUNDA REGRECION Y LO METI EN CADA CASE. CODIGO ORIGINAL ESTA EN DEFAULT VA AQUI.
        
        
        //***--------------------- regresion con epsilon
        no_transiciones++;
        regresion="\"q"+regrecion_cero_mas+"\"->\"q"+primero_cero_mas+"\" [label=\"\u03B5\"];\n";
        
        //***--------------------- paso epsilon
        no_transiciones++;
        paso_epsilon="\"q"+inicial_cero_mas+"\"->\"q"+final_cero_mas+"\" [label=\"\u03B5\"];\n";
        
        cero_mas = final_cero_mas + "<%>" + posicion_en_lista + "<%>" + primer_epsilon+paso_a+segundo_epsilon+regresion+paso_epsilon + "<%>" + no_transiciones;
        return cero_mas;
    }
    
    private String metodo_almenos_uno(LinkedList<String> contenido, String transicion_a, int posicion_en_lista, int estado_qn, int principio, int aob, int no_transiciones){
        //+a
        int inicial_uno_mas, primero_uno_mas, regrecion_uno_mas, final_uno_mas;
        String primer_epsilon, paso_a, segundo_epsilon, regrecion_epsilon, almenos_uno;
        
        //***--------- primer epsilon
        if(aob==1){
            //aun no tiene transicion
            inicial_uno_mas = estado_qn;
        }else{
            //ya hay una transicion
            inicial_uno_mas = principio;
        }
        
        primero_uno_mas = inicial_uno_mas + 1;
        estado_qn++;
        no_transiciones++;
        primer_epsilon="\"q"+inicial_uno_mas+"\"->\"q"+primero_uno_mas+"\" [label=\"\u03B5\"];\n";
        
        //***---------- paso a
        String a;
        switch(transicion_a){
            case ".":
                posicion_en_lista++;
                a = contenido.get(posicion_en_lista);
                /*
                retorno de concatenar: final_concat<%>posicion_en_lista<%>relaciones<%>no_transiciones
                */
                String tmp_concat = metodo_concatenar(contenido, a, posicion_en_lista, estado_qn, no_transiciones);
                String [] datos_concat = tmp_concat.split("<%>");
                
                regrecion_uno_mas = Integer.parseInt(datos_concat[0]);
                estado_qn = Integer.parseInt(datos_concat[0]);
                posicion_en_lista = Integer.parseInt(datos_concat[1]);
                paso_a = datos_concat[2];
                no_transiciones = Integer.parseInt(datos_concat[3]);
                
                //***--------- segunda transicion epsilon
                estado_qn++;
                final_uno_mas = estado_qn;
                no_transiciones++;
                segundo_epsilon = "\"q"+regrecion_uno_mas+"\"->\"q"+final_uno_mas+"\" [label=\"\u03B5\"];\n";
                break;
            case "|":
                //como es or envio los siguientes dos datos en la lista
                posicion_en_lista++;
                String aa = contenido.get(posicion_en_lista);
                posicion_en_lista++;
                String ab = contenido.get(posicion_en_lista);
                
                /*
                los datos retornados serian:
                (0) segundo_b_or: donde continuara numerando el siguiente estado_qn 
                (1) final_or: donde empezara el siguiente estado 
                (2) relaciones: codigo de graphviz 
                (3) posicion_en_lista
                (4) no_transiciones
                */
                
                String tmp_or = metodo_or(contenido, aa, ab, posicion_en_lista, estado_qn, primero_uno_mas, 0, no_transiciones);
                String[] datos_or = tmp_or.split("<%>");
                
                estado_qn = Integer.parseInt(datos_or[1]);
                regrecion_uno_mas = Integer.parseInt(datos_or[1]);
                paso_a = datos_or[2];
                posicion_en_lista = Integer.parseInt(datos_or[3]);
                no_transiciones = Integer.parseInt(datos_or[4]);
                
                //***--------------------- segunda transicion epsilon
                estado_qn++;
                final_uno_mas = estado_qn;
                no_transiciones++;
                segundo_epsilon = "\"q"+regrecion_uno_mas+"\"->\"q"+final_uno_mas+"\" [label=\"\u03B5\"];\n";
                break;
            case "*":
                posicion_en_lista++;
                String aa_ = contenido.get(posicion_en_lista);
                
                /*
                datos retornados:
                (0) final_cero_mas: donde finalizo la transicion
                (1) posicion_en_lista
                (2) codigo de graphviz
                (3) no_transiciones
                */
                
                String tmp_cero = metodo_cero_o_mas(contenido, aa_, posicion_en_lista, estado_qn, primero_uno_mas, 0, no_transiciones);
                String[] datos_cero = tmp_cero.split("<%>");
                
                regrecion_uno_mas = Integer.parseInt(datos_cero[0]);
                estado_qn = Integer.parseInt(datos_cero[0]);
                posicion_en_lista = Integer.parseInt(datos_cero[1]);
                paso_a = datos_cero[2];
                no_transiciones = Integer.parseInt(datos_cero[3]);
                
                //***--------------------- segunda transicion epsilon
                estado_qn++;
                final_uno_mas=estado_qn;
                no_transiciones++;
                segundo_epsilon = "\"q"+regrecion_uno_mas+"\"->\"q"+final_uno_mas+"\" [label=\"\u03B5\"];\n";
                break;
            case "?":
                posicion_en_lista++;
                a = contenido.get(posicion_en_lista);
                /*
                (0) final_opcional: estado donde finalizo anteriormente
                (1) posicion_en_lista
                (2) relaciones
                (3) no_transiciones
                */
                String tmp_op = metodo_opcional(contenido, a, posicion_en_lista, estado_qn, primero_uno_mas, 0, no_transiciones);
                String[] datos_op = tmp_op.split("<%>");
                
                regrecion_uno_mas = Integer.parseInt(datos_op[0]);
                estado_qn = Integer.parseInt(datos_op[0]);
                posicion_en_lista = Integer.parseInt(datos_op[1]);
                paso_a = datos_op[2];
                no_transiciones = Integer.parseInt(datos_op[3]);
                
                //***-------------------- segunda transicion epsilon
                estado_qn++;
                final_uno_mas=estado_qn;
                no_transiciones++;
                segundo_epsilon = "\"q"+regrecion_uno_mas+"\"->\"q"+final_uno_mas+"\" [label=\"\u03B5\"];\n";
                break;
            case "+":
                posicion_en_lista++;
                a = contenido.get(posicion_en_lista);
                
                /*
                datos retornados:
                (0) final_uno_mas: donde finalizo la transicion
                (1) posicion_en_lista
                (2) codigo de graphviz
                (3) no_transiciones
                */
                
                String tmp_uno = metodo_almenos_uno(contenido, a, posicion_en_lista, estado_qn, primero_uno_mas, 0, no_transiciones);
                String[] datos_uno = tmp_uno.split("<%>");
                
                regrecion_uno_mas = Integer.parseInt(datos_uno[0]);
                estado_qn = Integer.parseInt(datos_uno[0]);
                posicion_en_lista = Integer.parseInt(datos_uno[1]);
                paso_a = datos_uno[2];
                no_transiciones = Integer.parseInt(datos_uno[3]);
                
                //***--------------------- segunda transicion epsilon
                estado_qn++;
                final_uno_mas=estado_qn;
                no_transiciones++;
                segundo_epsilon = "\"q"+regrecion_uno_mas+"\"->\"q"+final_uno_mas+"\" [label=\"\u03B5\"];\n";
                break;
            default:
                regrecion_uno_mas = primero_uno_mas + 1;
                no_transiciones++;
                paso_a = "\"q"+primero_uno_mas+"\"->\"q"+regrecion_uno_mas+"\" [label=\""+transicion_a+"\"];\n";
                estado_qn++;
                
                //*** ------------ segundo paso epsilon
                final_uno_mas = regrecion_uno_mas + 1;
                estado_qn++;
                no_transiciones++;
                segundo_epsilon = "\"q"+regrecion_uno_mas+"\"->\"q"+final_uno_mas+"\" [label=\"\u03B5\"];\n";
                break;
        }
        
        //*** ------------ regrecion epsilon
        no_transiciones++;
        regrecion_epsilon = "\"q"+regrecion_uno_mas+"\"->\"q"+primero_uno_mas+"\" [label=\"\u03B5\"];\n";
        
        almenos_uno = final_uno_mas + "<%>" + posicion_en_lista + "<%>" + primer_epsilon+paso_a+segundo_epsilon+regrecion_epsilon + "<%>" + no_transiciones;
        return almenos_uno;
    }
    
    private String metodo_opcional(LinkedList<String> contenido, String transicion_a, int posicion_en_lista, int estado_qn, int principio, int aob, int no_transiciones){
        //?a
        int inicial_opcional, primero_opcional, segundo_opcional, final_opcional;
        String primer_epsilon, segundo_epsilon, paso_a, paso_epsilon, opcional;
        
        //***--------------------- primer transicion epsilon
        if(aob==1){
            //aun no tiene transicion
            inicial_opcional = estado_qn;
        }else{
            //ya hay una transicion
            inicial_opcional = principio;
        }
        primero_opcional = inicial_opcional+1;
        estado_qn++;
        primer_epsilon="\"q"+inicial_opcional+"\"->\"q"+primero_opcional+"\" [label=\"\u03B5\"];\n";
        
        //***-------------------- parte de A
        String a;
        switch(transicion_a){
            case ".":
                posicion_en_lista++;
                a = contenido.get(posicion_en_lista);
                /*
                retorno de concatenar: final_concat<%>posicion_en_lista<%>relaciones
                */
                String tmp_concat = metodo_concatenar(contenido, a, posicion_en_lista, estado_qn, no_transiciones);
                String [] datos_concat = tmp_concat.split("<%>");
                
                segundo_opcional = Integer.parseInt(datos_concat[0]);
                estado_qn = Integer.parseInt(datos_concat[0]);
                posicion_en_lista = Integer.parseInt(datos_concat[1]);
                paso_a = datos_concat[2];
                
                //***--------- segunda transicion epsilon
                estado_qn++;
                final_opcional = estado_qn;
                no_transiciones++;
                segundo_epsilon = "\"q"+segundo_opcional+"\"->\"q"+final_opcional+"\" [label=\"\u03B5\"];\n";
                break;
            case "|":
                //como es or envio los siguientes dos datos en la lista
                posicion_en_lista++;
                String aa = contenido.get(posicion_en_lista);
                posicion_en_lista++;
                String ab = contenido.get(posicion_en_lista);
                
                //System.out.println("aa tiene: "+aa+"\nab tiene: "+ab);
                
                /*
                los datos retornados serian: segundo_b_or<%>final_or<%>relaciones<%>posicion_en_lista
                (0) segundo_b_or: donde continuara numerando el siguiente estado_qn 
                (1) final_or: donde empezara el siguiente estado 
                (2) relaciones: codigo de graphviz 
                (3) posicion_en_lista
                (4) no_transiciones
                */
                
                String tmp_or = metodo_or(contenido, aa, ab, posicion_en_lista, estado_qn, primero_opcional, 0, no_transiciones);
                String[] datos_or = tmp_or.split("<%>");
                
                estado_qn = Integer.parseInt(datos_or[1]);
                segundo_opcional = Integer.parseInt(datos_or[1]);
                paso_a = datos_or[2];
                posicion_en_lista = Integer.parseInt(datos_or[3]);
                no_transiciones = Integer.parseInt(datos_or[4]);
                
                //***--------------------- segunda transicion epsilon
                estado_qn++;
                final_opcional = estado_qn;
                no_transiciones++;
                segundo_epsilon = "\"q"+segundo_opcional+"\"->\"q"+final_opcional+"\" [label=\"\u03B5\"];\n";
                break;
            case "*":
                posicion_en_lista++;
                a = contenido.get(posicion_en_lista);
                
                /*
                datos retornados: final_cero_mas<%>posicion_en_lista<%>relaciones
                (0) final_cero_mas: donde finalizo la transicion
                (1) posicion_en_lista
                (2) codigo de graphviz
                (3) no_transiciones
                */
                
                String tmp_cero = metodo_cero_o_mas(contenido, a, posicion_en_lista, estado_qn, primero_opcional, 0, no_transiciones);
                String[] datos_cero = tmp_cero.split("<%>");
                
                segundo_opcional = Integer.parseInt(datos_cero[0]);
                estado_qn = Integer.parseInt(datos_cero[0]);
                posicion_en_lista = Integer.parseInt(datos_cero[1]);
                paso_a = datos_cero[2];
                no_transiciones = Integer.parseInt(datos_cero[3]);
                
                //***--------------------- segunda transicion epsilon
                estado_qn++;
                final_opcional=estado_qn;
                no_transiciones++;
                segundo_epsilon = "\"q"+segundo_opcional+"\"->\"q"+final_opcional+"\" [label=\"\u03B5\"];\n";
                break;
            case "?":
                posicion_en_lista++;
                a = contenido.get(posicion_en_lista);
                /*
                (0) final_opcional: estado donde finalizo anteriormente
                (1) posicion_en_lista
                (2) relaciones
                (3) no_transiciones
                */
                String tmp_op = metodo_opcional(contenido, a, posicion_en_lista, estado_qn, primero_opcional, 0, no_transiciones);
                String[] datos_op = tmp_op.split("<%>");
                
                segundo_opcional = Integer.parseInt(datos_op[0]);
                estado_qn = Integer.parseInt(datos_op[0]);
                posicion_en_lista = Integer.parseInt(datos_op[1]);
                paso_a = datos_op[2];
                no_transiciones = Integer.parseInt(datos_op[3]);
                
                //***-------------------- segunda transicion epsilon
                estado_qn++;
                final_opcional=estado_qn;
                no_transiciones++;
                segundo_epsilon = "\"q"+segundo_opcional+"\"->\"q"+final_opcional+"\" [label=\"\u03B5\"];\n";
                break;
            case "+":
                posicion_en_lista++;
                a = contenido.get(posicion_en_lista);
                
                /*
                datos retornados: final_uno_mas<%>posicion_en_lista<%>relaciones
                (0) final_uno_mas: donde finalizo la transicion
                (1) posicion_en_lista
                (2) codigo de graphviz
                (3) no_transiciones
                */
                
                String tmp_uno = metodo_almenos_uno(contenido, a, posicion_en_lista, estado_qn, primero_opcional, 0, no_transiciones);
                String[] datos_uno = tmp_uno.split("<%>");
                
                segundo_opcional = Integer.parseInt(datos_uno[0]);
                estado_qn = Integer.parseInt(datos_uno[0]);
                posicion_en_lista = Integer.parseInt(datos_uno[1]);
                paso_a = datos_uno[2];
                no_transiciones = Integer.parseInt(datos_uno[3]);
                
                //***--------------------- segunda transicion epsilon
                estado_qn++;
                final_opcional=estado_qn;
                no_transiciones++;
                segundo_epsilon = "\"q"+segundo_opcional+"\"->\"q"+final_opcional+"\" [label=\"\u03B5\"];\n";
                break;
            default:
                segundo_opcional = primero_opcional+1;
                no_transiciones++;
                paso_a = "\"q"+primero_opcional+"\"->\"q"+segundo_opcional+"\" [label=\""+transicion_a+"\"];\n";
                estado_qn++;
                
                //***--------------------- segunda transicion epsilon
                final_opcional = segundo_opcional+1;
                estado_qn++;
                no_transiciones++;
                segundo_epsilon = "\"q"+segundo_opcional+"\"->\"q"+final_opcional+"\" [label=\"\u03B5\"];\n";
                break;
        }
        
        //***-------------------- paso epsilon
        no_transiciones++;
        paso_epsilon = "\"q"+inicial_opcional+"\"->\"q"+final_opcional+"\" [label=\"\u03B5\"];\n";
        
        opcional = final_opcional +"<%>"+ posicion_en_lista + "<%>" + primer_epsilon+paso_a+segundo_epsilon+paso_epsilon + "<%>" + no_transiciones;
        return opcional;
    }
    
    private void mostrar(String a){
        System.out.println(a);
    }
    
}
