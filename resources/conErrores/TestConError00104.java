///[Error:a1|20]
//El atributo a1 no se puede acceder con m2 porque NO es una clase

class A {
    private int a1;

    void m1(int p1){}

    void m2()
    {}



}


class B extends A{

    void m3() {
        var x = m2().a1;
    }

}


class Init{
    static void main()
    { }
}
