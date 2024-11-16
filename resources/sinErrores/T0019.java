//14&exitosamente
class A {
    int a;

    void m1(){
        var b = 7;
        var c = 1;
        var t = true;
        switch(b){
            case 1:
            {
                a = 1;
                System.printSln("aca no entro");
            }
            case 7:
            {
                a = 14;
            }
            default:
            {
                a = 3;
                System.printSln("aca tampoco");
            }
        }

    }

}

class B extends A{

}


class Init{
    static void main()
    {
        var b = new B();
        b.m1();
        debugPrint(b.a);
    }
}


