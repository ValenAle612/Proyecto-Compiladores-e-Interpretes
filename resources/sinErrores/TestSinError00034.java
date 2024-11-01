class Init{
    static void main(){}
}

class A {

    B m1(){}
}

class B extends A {

    C m2(int x) {
        m1();
    }
}

class C extends B {

    void m3() {
        m1();
        m2(4);
        m1().m2(35);
    }
}

class D extends C{

    void m4() {
        m1();
        m2(4);
        m3();
        m2(2).m3();
        m2(1).m1();
    }
}