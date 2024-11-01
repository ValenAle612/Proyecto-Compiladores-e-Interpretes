///[Error:m1|20]
//El metodo m1 no se puede acceder con m2 porque la clase a la derecha de la llamada no lo tiene

class A {
    private int a1;

    static void m1(int p1){}

    C m2()
    {}



}


class B extends A{

    void m3() {
        C.m1(1);
    }

}

class C {}

class Init{
    static void main()
    { }
}
