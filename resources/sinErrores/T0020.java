//14&exitosamente
class A {
    int a;

    void m1(){
        var b = 7;
        var c = 5;
        switch(b) {
            case 7:
            {
                switch (c) {
                    case 2:
                        a += 1;
                    case 5:
                        a -= 1;
                }
                a = 14;
            }
            case 2:
                a = 2;
            default:
                a = 3;
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


