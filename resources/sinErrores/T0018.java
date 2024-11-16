//8&exitosamente
class A {
    int a;

    void m1(){
        a = 7;
        a+=1;
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


