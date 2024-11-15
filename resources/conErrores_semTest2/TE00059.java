///[Error:m2|19]

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
