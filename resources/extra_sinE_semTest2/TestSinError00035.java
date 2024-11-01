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
        {
            {{{{}x = 1;}}}
        }
        a3 = false;
    }
}

class C extends B {

    public A a2;

    void m3() {
        var a = 1;
        {
            {
                a = 78;
            }
            {{a+=1;}{a2 = new A();}a1 -= 1;}
        }
        a2 = new C();
        a2 = null;
        a1 = 23456;
        a = 789;
    }
}
