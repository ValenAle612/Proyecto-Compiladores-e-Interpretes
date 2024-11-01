class Init {
    static void main() {
        return ;
    }
}

class A {

    int m1(int a) {
        return 1789*098-1%a+5677-86+222;
    }

    boolean m2() {
        return true && false || (1 < 2) && (((11) >= 1));
    }

    char m3() {
        return 'a';
    }

    String m4() {
        return "hola";
    }

    A m5(){
        return new B();
    }

    A m6(){
        return new C();
    }

    B m7(){
        return new C();
    }

    C m8(){
        return new C();
    }

}

class B extends A {

}

class C extends B {

}