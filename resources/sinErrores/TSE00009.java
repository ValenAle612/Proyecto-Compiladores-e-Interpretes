
class A {
    void m1(A p1) {
        (p1).m3().m2();
    }
    void m2() { }
    A m3() {
        return new A();
    }


    static void main(){}

}