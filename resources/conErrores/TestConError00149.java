///[Error:m2|8]
//No es posible llamar a un método dinámico en un método estático

class A {
    private int a1;

    static void m1(int p1){
        m2();
    }

    C m2()
    {}



}

class C {}

class Init{
    static void main()
    { }
}
