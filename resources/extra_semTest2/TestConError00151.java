///[Error:m2|20]
//m2 no es un método estático por lo que no se puede llamar de esa forma

class A {
    private int a1;

    static void m1(int p1){}

    C m2()
    {}



}


class B extends A{

    void m3() {
        B.m2();
    }

}

class C {}

class Init{
    static void main()
    { }
}
