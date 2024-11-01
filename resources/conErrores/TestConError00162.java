///[Error:return|6]

class A {

    void m1() {
        return new A();
    }

    static void main(){}
}