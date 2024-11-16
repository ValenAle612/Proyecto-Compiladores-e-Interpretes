//8&14&exitosamente

class Init{
    static void main(){
        var a = new A();
        var v1 = a.m1(1); //6
        var v2 = a.m2(v1,a.m3());
        System.printIln(v2); //14
        var v3 = a.a4;
    }
}

class A{
    public int a1;
    public char a2;
    public String a3;
    public boolean a4;
    public A a5;

    int m1(int p1){
        a1 = 5;
        a2 = 'd';
        a3 = "hola";
        a4 = false;
        while(!a4){
            a1 += p1;
            if(p1 != 0){
                a4 = true;
            }
        }
        return a1; //6
    }

    int m2(int p1, int p2){
        return p1 + p2; //14
    }

    int m3(){
        var v1 = 1;
        while(v1 < 7){
            v1 += v1;
        }
        System.printIln(v1);
        return v1; // 8
    }

    B m4(){
        return new B();
    }
}

class B{}