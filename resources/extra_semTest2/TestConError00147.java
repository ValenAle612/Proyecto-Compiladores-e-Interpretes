///[Error:C|20]
//No existe la clase a la derecha de la llamada al método estático m1

class A {
    private int a1;

    static void m1(int p1){}

    B m2()
    {}



}


class B extends A{

    void m3() {
        C.m1(1);
    }

}


class Init{
    static void main()
    { }
}
