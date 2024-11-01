class Init{
    static void main(){}
}

class A {
    public B x;

    A m1(){}
}

class B extends A {

    void m2() {
        m1();
    }
}

class C extends B {

    void m3() {
        m1();
        m2();
        m1().x.m2();
    }
}

class D extends C{

    void m4() {
        m1();
        m2();
        m3();
    }
}