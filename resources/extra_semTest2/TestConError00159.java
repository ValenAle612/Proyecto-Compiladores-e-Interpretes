///[Error:a3|30]

class Init{
    static void main(){}
}

class A {

    public int a1;

    B m1(){}
}

class B extends A {
    private boolean a3;

    C m2(int x) {
        x = 3;
        x += 21;
        a1 = 21;
        a3 = false;
    }
}

class C extends B {

    public A a2;

    void m3() {
        a3 = true;
        a2 = new C();
        a2 = null;
    }
}
