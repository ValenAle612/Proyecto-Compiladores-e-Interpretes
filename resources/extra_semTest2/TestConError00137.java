///[Error:m1|20]
//El metodo m1 no se puede acceder con m2 porque la clase a la izquierda no tiene el método

class A {
    private int a1;

    void m1(int p1){}

    C m2()
    {}



}


class B extends A{

    void m3() {
        var x = this.m2().m1(1);
    }

}

class C {}

class Init{
    static void main()
    { }
}
