class A {

    public A obj;

    static void main(){}

    void test() {
        m1(-5);
    }

    void m1(int a) {
        obj.m1(0);
        A.main();
        new A();
        m1(1);
        this.test();
    }

}