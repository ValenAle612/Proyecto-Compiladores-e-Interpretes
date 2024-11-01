//El atributo a1 no se puede acceder con m2 porque NO es una clase

class A {
    private int a1;

    void m1(int p1){}

    void m2()
    {}



}


class B extends A{

    private int a2;

    void m3() {
        var x = this.a2;
    }

}


class Init{
    static void main()
    { }
}
