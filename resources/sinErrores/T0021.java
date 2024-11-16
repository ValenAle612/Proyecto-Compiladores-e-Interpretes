//14&exitosamente
class A {
    int a;

    void m1(){
        var b = 7;
        switch(b){
            case 1:
                a = 1;
            case 2:
                a = 2;
            default:
                a = 14;
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


