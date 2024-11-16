//14&exitosamente
class A {
    int a;

    void m1(){
        var b = 7;
        var c = 1;
        var t = true;

        if(b==1)
            a = 1;
        else
            if(b==7)
                a = 14;
            else
                a = 3;

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


