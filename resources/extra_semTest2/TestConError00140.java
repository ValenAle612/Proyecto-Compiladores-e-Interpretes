///[Error:m3|21]

class Init{

    static void main() {

    }
}

class A {
    int m1(){}

    void m2(int x) {}

    void m3(A a, B b, String c){}
}

class B extends A {

    void m4(B b) {
        m3(b, "hola");
    }

}
