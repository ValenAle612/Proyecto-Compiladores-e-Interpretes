class A {
    private int a1;

    static void m1(int p1){}

    A m2()
    {}



}


class B extends A{

    void m3() {
        B.m1(1);
    }

}

class Init{
    static void main()
    { }
}
