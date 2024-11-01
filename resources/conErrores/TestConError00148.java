///[Error:m1|20]
//En la llamada al metodo estático m1 no conforma el parámetro actual con el parámetro formal

class A {
    private int a1;

    static void m1(int p1){}

    C m2()
    {}



}


class B extends A{

    void m3() {
        A.m1("hola");
    }

}

class C {}

class Init{
    static void main()
    { }
}
