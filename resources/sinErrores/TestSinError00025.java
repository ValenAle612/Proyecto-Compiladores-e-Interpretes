class A {
    public int a1;
    public C a3;

    void m1(int p1){}

    void m2()
    {}



}


class B extends A{

    private int a2;

    void m3() {
        var x = this.a1;
        x = this.a2;
        a3 = new C();
    }

}
class C {

}


class Init{
    static void main()
    { }
}
