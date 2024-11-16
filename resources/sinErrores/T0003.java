//hola&6&d&true&6&6&exitosamente

class Init{
    static void main(){
        var a = new A();
        a.m1(1);
        System.printIln(a.a1);
        a.m0();
    }
}

class A{
    int a1;
    char a2;
    String a3;
    boolean a4;
    A a5;

    void m0() {
        System.printIln(a1);
    }

    void m1(int p1){
        a1 = 5;
        a2 = 'd';
        a3 = "hola";
        a4 = false;
        while(!a4){
            a1 += p1; //a1 es 6
            if(p1 != 0){
                a4 = true;
            }
        }
        System.printSln(a3); //hola
        System.printIln(a1); //6
        System.printCln(a2); //d
        System.printBln(a4); //true
    }

    void m2(int p1, int p2){
        System.printIln(p1);
        System.printIln(p2);
    }

    void m3(int p1, char p2){
        System.printIln(p1);
        System.printCln(p2);
    }
}