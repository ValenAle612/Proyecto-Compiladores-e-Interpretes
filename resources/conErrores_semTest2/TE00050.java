///[Error:;|11]

class Init{
    static void main(){}
}

class A {

    public int a;
    void m1(){
        m2().a;
    }

    A m2() {}
}
