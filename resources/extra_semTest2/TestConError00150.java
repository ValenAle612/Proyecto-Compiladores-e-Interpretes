///[Error:m1|20]
//En la llamada al metodo estático m1 no conforma el parámetro actual con el parámetro formal

class A {
    private int a1;

    static void m1(int p1){}

    static C m2()
    {}



}


class B extends A{

    void m3() {
        B.m2().m1(1);
    }

}

class C {}

class Init{
    static void main()
    { }
}
