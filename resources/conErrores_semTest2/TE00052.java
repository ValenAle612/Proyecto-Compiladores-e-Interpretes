///[Error:m1|20]


class A {
    private int a1;

    void m1(int p1){}

    int m2()
    {}



}


class B extends A{

    void m3() {
        var x = this.m2().m1(1);
    }

}


class Init{
    static void main()
    { }
}
