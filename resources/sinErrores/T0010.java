//Hola&k&10&xd&k&10&aaaA&#&jjj&k&20&exitosamente

class A extends System {
    public static void main(){
        var v0 = "Hola";
        var v2 = 'k';
        var v1 = m1(v0, v2, 10);//Hola, k, 10
        printIln(v1);
        printIln(m1("xd", v2, v1));
        printIln(m1("jjj", v2, m1("aaaA", '#', 20)));

    }

    private static int m1(String p1, char p2, int p3){
        var v0 = p1;//Hola //xd //aaaA //jjj
        printSln(v0);
        var v1 = p2;//k //k //# //k
        printCln(v1);
        return p3;//10 //10 //20 //20
    }
}
